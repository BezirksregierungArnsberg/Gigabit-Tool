package de.karlsommer.gigabit.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static de.karlsommer.gigabit.Interface.RELEASE;

public class Settings {
    public static final String SETTINGS_FILE = "settings.xml";
    private static Settings _this = null;

    private String PROPERTY_STRING_DATABASE_FOLDER = "database_folder";
    private String PROPERTY_STRING_OUTPUT_FOLDER = "output_folder";
    private String PROPERTY_STRING_BEZIRKS_STRING = "bezirks_string";
    private String PROPERTY_STRING_GOOGLE_GEO_UTILS_KEY_STRING = "geoutils_key";

    public String getDatabaseFolderPath() {
        return databaseFolderPath;
    }

    public String getOutputFolderPath() {
        return outputFolderPath;
    }

    public String getBezirksSuchString() {
        return bezirksSuchString;
    }

    public String getGoogleGeoUtilsKey() { return googleGeoUtilsKey;}

    private String databaseFolderPath;
    private String outputFolderPath;
    private String bezirksSuchString;
    private String googleGeoUtilsKey;

    public static Settings getInstance()
    {
        if(_this == null)
            _this = new Settings();
        return _this;
    }
    private Settings()
    {
        Properties loadProps = new Properties();
        try {
            if(RELEASE)
                loadProps.loadFromXML(new FileInputStream(SETTINGS_FILE));
            else
                loadProps.loadFromXML(new FileInputStream("../databases/"+SETTINGS_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            Properties saveProps = new Properties();
            saveProps.setProperty(PROPERTY_STRING_DATABASE_FOLDER, "./databases/");
            saveProps.setProperty(PROPERTY_STRING_OUTPUT_FOLDER, "./output/");
            saveProps.setProperty(PROPERTY_STRING_BEZIRKS_STRING, "9");
            saveProps.setProperty(PROPERTY_STRING_GOOGLE_GEO_UTILS_KEY_STRING, "");
            try {
                saveProps.storeToXML(new FileOutputStream(SETTINGS_FILE), "");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        databaseFolderPath = loadProps.getProperty(PROPERTY_STRING_DATABASE_FOLDER);
        outputFolderPath = loadProps.getProperty(PROPERTY_STRING_OUTPUT_FOLDER);
        bezirksSuchString = loadProps.getProperty(PROPERTY_STRING_BEZIRKS_STRING);
        googleGeoUtilsKey = loadProps.getProperty(PROPERTY_STRING_GOOGLE_GEO_UTILS_KEY_STRING);

    }
}
