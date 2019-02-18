package de.karlsommer.gigabit.geocoding;

import de.karlsommer.gigabit.helper.Settings;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

// https://halexv.blogspot.com/2015/07/java-geocoding-using-google-maps-api.html
public class GoogleGeoUtils {

    /**
     * Given an address asks google for geocode
     *
     * If ssl is true API_KEY should be a valid developer key (given by google)
     *
     * @param address the address to find
     * @param ssl defines if ssl should be used
     * @return the GoogleGeoCode found
     * @throws Exception in case of any error
     *
     */
    public GoogleGeoLatLng getGeoCode(String address, boolean ssl) throws Exception {
        // build url
        System.out.println("Adresse:"+address);
        StringBuilder url = new StringBuilder("http");
        if ( ssl ) {
            url.append("s");
        }

        url.append("://maps.googleapis.com/maps/api/geocode/json?");

        if ( ssl ) {
            url.append("key=");
            url.append(Settings.getInstance().getGoogleGeoUtilsKey());
            url.append("&");
        }
        url.append("sensor=false&address=");
        url.append( URLEncoder.encode(address) );

        // request url like: http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(address) + "&sensor=false"
        // do request
        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
            System.out.println("URL:"+url.toString());
            HttpGet request = new HttpGet(url.toString());

            // set common headers (may useless)
            request.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:31.0) Gecko/20100101 Firefox/31.0 Iceweasel/31.6.0");
            request.setHeader("Host", "maps.googleapis.com");
            request.setHeader("Connection", "keep-alive");
            request.setHeader("Accept-Language", "en-US,en;q=0.5");
            request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            request.setHeader("Accept-Encoding", "gzip, deflate");

            try (CloseableHttpResponse response = httpclient.execute(request)) {
                HttpEntity entity = response.getEntity();

                // recover String response (for debug purposes)
                StringBuilder result = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        result.append(inputLine);
                        result.append("\n");
                    }
                }

                System.out.println(result);
                if(result.toString().toLowerCase().contains("error_message"))
                {
                    System.out.println("Fehler");
                    return null;
                }
                // parse result
                JSONObject json = new JSONObject(result.toString());
                JSONObject temp = ((JSONObject)json.getJSONArray("results").get(0)).getJSONObject("geometry").getJSONObject("location");




                return new GoogleGeoLatLng(temp.getDouble("lat"),temp.getDouble("lng"));
            }
        }
    }

    /**
     * Given an address and google geocode find the most probable location of
     * address, the measure uses the longest common subsequence algorithm and a
     * minimum requirement for similarity
     *
     * @param address the original address
     * @param geocode the google geocode results
     * @return the most probable location (lat, lng), null if no one matches
     */
    public GoogleGeoLatLng getMostProbableLocation(String address, GoogleGeoCode geocode) {
        address = address.toLowerCase();
        int expected = address.length() / 2;
        int sz = geocode.getResults().length;
        int best = expected;
        GoogleGeoLatLng latlng = null;
        for (GoogleGeoResult result : geocode.getResults()) {
            GoogleGeoLatLng cur = result.getGeometry().getLocation();
            String formattedAddress = result.getFormatted_address().toLowerCase();
            int p = lcs(address, formattedAddress);

            if (p > best) {
                latlng = cur;
                best = p;
            }
        }
        return latlng;
    }
    /**
     * The longest common subsequence of s and t using dynamic programming
     *
     * @param s the first string
     * @param t the second string
     * @return the length of the longest common subsequence
     */
    private int lcs(String s, String t) {
        int N = s.length();
        int M = t.length();
        int[][] ans = new int[N + 1][M + 1];
        for (int k = N - 1; k >= 0; k--) {
            for (int m = M - 1; m >= 0; m--) {
                if (s.charAt(k) == t.charAt(m)) {
                    ans[k][m] = 1 + ans[k + 1][m + 1];
                } else {
                    ans[k][m] = Math.max(ans[k + 1][m], ans[k][m + 1]);
                }
            }
        }
        return ans[0][0];
    }

    public class GoogleGeoCode {
        private String status;
        private GoogleGeoResult [] results;

        public GoogleGeoResult [] getResults()
        {
            return results;
        }

        public String getStatus()
        {
            return status;
        }
    }

    public class GoogleGeoResult   {
        private GoogleGeoAdressComponent [] address_components;
        private String formatted_address;
        private GoogleGeoGeometry geometry;
        private Boolean partial_match;
        private String place_id;
        private String [] types;

        public GoogleGeoGeometry getGeometry()
        {
            return geometry;
        }

        public String getFormatted_address()
        {
            return formatted_address;
        }
    }

    public class GoogleGeoAdressComponent {
        private String long_name;
        private String short_name;
        private String [] types;
    }

    public class GoogleGeoGeometry {
        private GoogleGeoBounds bounds;
        private GoogleGeoLatLng location;
        private String location_type;
        private GoogleGeoBounds viewport;

        public GoogleGeoLatLng getLocation()
        {
            return location;
        }
    }

    public class GoogleGeoBounds   {
        private GoogleGeoLatLng northeast;
        private GoogleGeoLatLng southwest;
    }

    public class GoogleGeoLatLng {

        public GoogleGeoLatLng(double lat, double lng)
        {
            this.lat = lat;
            this.lng = lng;
        }
        private double lat;
        private double lng;
        public double getLat()
        {
            return lat;
        }
        public double getLng()
        {
            return lng;
        }
        public String getLatLng()
        {
            return lat+ ";"+lng;
        }
    }
}
