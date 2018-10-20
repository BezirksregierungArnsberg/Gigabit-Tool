/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.karlsommer.gigabit.database.model;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author karl
 */
public class Schule {

    public static final int GIGABIT_TABELLE_SNR = 11;
    public static final int GIGABIT_TABELLE_DOWNSTREAM = 13;
    public static final int GIGABIT_TABELLE_UPSTREAM = 14;

    // 1982,169298,Immanuel-Kant-Gymnasium,44319,Dortmund,"Grüningsweg 42",231,5012100,1000000,1000000
    public static final int CSV_INTERNE_ID = 0;
    public static final int CSV_DATA_SRN = 1;
    public static final int CSV_DATA_SCHULNAME = 2;
    public static final int CSV_DATA_PLZ = 3;
    public static final int CSV_DATA_ORT = 4;
    public static final int CSV_DATA_STRASSE = 5;
    public static final int CSV_DATA_VORWAHL = 6;
    public static final int CSV_DATA_TELEFONNUMMER = 7;
    public static final int CSV_DATA_UPLOAD = 9;
    public static final int CSV_DATA_DOWNLOAD = 8;

    public static final String HAUPTSTANDORT = "H";
    
    private int id;
    private int SNR;
    private String Name_der_Schule;
    private String Art_der_Schule;
    private int PLZ;
    private String Ort;
    private String Strasse_Hsnr;
    private String Zustaendiges_Schulamt;
    private String Vorwahl;
    private String Rufnummer;
    private String SF;
    private String Schultyp;
    private String Mailadresse;
    private String Bemerkungen;
    private boolean flag;
    private String Status_GB;
    private int Anbindung_Kbit_DL;
    private int Anbindung_Kbit_UL;
    private String Status_MK;
    private String Status_Inhouse;
    private double lat;
    private double lng;

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    private String standort;

    public Schule(ArrayList<String> daten, boolean fromDatabase)
    {
        int i = 0;
        if(fromDatabase)
            this.id = Integer.parseInt(daten.get(i++));
        else
            this.id = -1;
        this.SNR = Integer.parseInt(daten.get(i++));
        this.Name_der_Schule = daten.get(i++);
        this.Art_der_Schule = daten.get(i++);
        this.PLZ = Integer.parseInt(daten.get(i++));
        this.Ort = daten.get(i++);
        this.Strasse_Hsnr = daten.get(i++);
        this.Zustaendiges_Schulamt = daten.get(i++);
        this.Vorwahl = (daten.get(i++));
        this.Rufnummer = daten.get(i++);
        this.SF = daten.get(i++);
        this.Schultyp = daten.get(i++);
        this.Mailadresse = daten.get(i++);
        if(fromDatabase)
        {
            this.Bemerkungen = daten.get(i++);
            this.flag = daten.get(i++).equals("true");
            this.Status_GB = daten.get(i++);
            if(daten.get(i) != null && !daten.get(i).isEmpty() && !daten.get(i).equals("null"))
                this.Anbindung_Kbit_DL = Integer.parseInt(daten.get(i));
            else
                this.Anbindung_Kbit_DL = 0;
            i++;
            if(daten.get(i) != null && !daten.get(i).isEmpty() && !daten.get(i).equals("null"))
                this.Anbindung_Kbit_UL = Integer.parseInt(daten.get(i));
            else
                this.Anbindung_Kbit_UL = 0;
            i++;
            this.Status_MK = daten.get(i++);
            this.Status_Inhouse = daten.get(i++);
            this.lat = Double.parseDouble(daten.get(i++));
            this.lng = Double.parseDouble(daten.get(i++));
            this.standort = daten.get(i);
        }
        else
        {
            this.lat = 0;
            this.lng = 0;
            this.flag = false;
        }
    }

    public Schule() {

    }

    // 1982,169298,Immanuel-Kant-Gymnasium,44319,Dortmund,"Grüningsweg 42",231,5012100,1000000,1000000
    public void setData(ArrayList<String> data)
    {
        this.id = Integer.parseInt(data.get(CSV_INTERNE_ID));
        this.SNR = Integer.parseInt(data.get(CSV_DATA_SRN));
        this.Name_der_Schule = (data.get(CSV_DATA_SCHULNAME));
        this.PLZ = Integer.parseInt(data.get(CSV_DATA_PLZ));
        this.Ort = (data.get(CSV_DATA_ORT));
        this.Strasse_Hsnr = (data.get(CSV_DATA_STRASSE));
        this.Vorwahl = (data.get(CSV_DATA_VORWAHL));
        this.Rufnummer = (data.get(CSV_DATA_TELEFONNUMMER));
        this.Anbindung_Kbit_UL = Integer.parseInt(data.get(CSV_DATA_UPLOAD));
        this.Anbindung_Kbit_DL = Integer.parseInt(data.get(CSV_DATA_DOWNLOAD));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSNR() {
        return SNR;
    }

    public void setSNR(int SNR) {
        this.SNR = SNR;
    }

    public String getName_der_Schule() {
        return Name_der_Schule;
    }

    public void setName_der_Schule(String Name_der_Schule) {
        this.Name_der_Schule = Name_der_Schule;
    }

    public String getArt_der_Schule() {
        return Art_der_Schule;
    }

    public void setArt_der_Schule(String Art_der_Schule) {
        this.Art_der_Schule = Art_der_Schule;
    }

    public int getPLZ() {
        return PLZ;
    }

    public void setPLZ(int PLZ) {
        this.PLZ = PLZ;
    }

    public String getOrt() {
        return Ort;
    }

    public void setOrt(String Ort) {
        this.Ort = Ort;
    }

    public String getStrasse_Hsnr() {
        return Strasse_Hsnr;
    }

    public void setStrasse_Hsnr(String Straße_Hsnr) {
        this.Strasse_Hsnr = Straße_Hsnr;
    }

    public String getZustaendiges_Schulamt() {
        return Zustaendiges_Schulamt;
    }

    public void setZustaendiges_Schulamt(String Zuständiges_Schulamt) {
        this.Zustaendiges_Schulamt = Zuständiges_Schulamt;
    }

    public String getVorwahl() {
        return Vorwahl;
    }

    public void setVorwahl(String Vorwahl) {
        this.Vorwahl = Vorwahl;
    }

    public String getRufnummer() {
        return Rufnummer;
    }

    public void setRufnummer(String Rufnummer) {
        this.Rufnummer = Rufnummer;
    }

    public String getSF() {
        return SF;
    }

    public void setSF(String SF) {
        this.SF = SF;
    }

    public String getSchultyp() {
        return Schultyp;
    }

    public void setSchultyp(String Schultyp) {
        this.Schultyp = Schultyp;
    }

    public String getMailadresse() {
        return Mailadresse;
    }

    public void setMailadresse(String Mailadresse) {
        this.Mailadresse = Mailadresse;
    }

    public String getBemerkungen() {
        if(this.Bemerkungen == null)
            return "";
        if(this.Bemerkungen.equals("null"))
            return "";
        return Bemerkungen;
    }

    public void setBemerkungen(String Bemerkungen) {
        this.Bemerkungen = Bemerkungen;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getStatus_GB() {
        if(this.Status_GB == null)
            return "";
        if(this.Status_GB.equals("null"))
            return "";
        return Status_GB;
    }

    public void setStatus_GB(String Status_GB) {
        this.Status_GB = Status_GB;
    }

    public int getAnbindung_Kbit_DL() {
        return Anbindung_Kbit_DL;
    }

    public void setAnbindung_Kbit_DL(int Anbindung_Kbit_DL) {
        this.Anbindung_Kbit_DL = Anbindung_Kbit_DL;
    }

    public int getAnbindung_Kbit_UL() {
        return Anbindung_Kbit_UL;
    }

    public void setAnbindung_Kbit_UL(int Anbindung_Kbit_UL) {
        this.Anbindung_Kbit_UL = Anbindung_Kbit_UL;
    }

    public String getStatus_MK() {
        if(this.Status_MK == null)
            return "";
        if(this.Status_MK.equals("null"))
            return "";
        return Status_MK;
    }

    public void setStatus_MK(String Status_MK) {
        this.Status_MK = Status_MK;
    }

    public String getStatus_Inhouse() {
        if(this.Status_Inhouse == null)
            return "";
        if(this.Status_Inhouse.equals("null"))
            return "";
        return Status_Inhouse;
    }

    public void setStatus_Inhouse(String Status_Inhouse) {
        this.Status_Inhouse = Status_Inhouse;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


   public static final String ausgabeSpalten[] = {"id","SNR","Name der Schule","Art der Schule","PLZ","Ort", "Upload", "Download"};
    
    public Vector<String> getVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(id));
        v.add(String.valueOf(SNR));
        v.add(Name_der_Schule);
        v.add(Art_der_Schule);
        v.add(String.valueOf(PLZ));
        v.add(Ort);
        v.add(String.valueOf(Anbindung_Kbit_DL));
        v.add(String.valueOf(Anbindung_Kbit_UL));
        return v;
    }


    public void updateBreitbandData(ArrayList<String> data)
    { //13 ist Download 14 Upload
        if(this.getSNR() == Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)))
        {
            this.Anbindung_Kbit_DL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_DOWNSTREAM));
            this.Anbindung_Kbit_UL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_UPSTREAM));
        }
    }

    public void updateCSVData(ArrayList<String> data)
    { //13 ist Download 14 Upload
        this.Anbindung_Kbit_DL = getNormalizedDataInKbitFromString(data.get(CSV_DATA_DOWNLOAD));
        this.Anbindung_Kbit_UL = getNormalizedDataInKbitFromString(data.get(CSV_DATA_UPLOAD));
        this.Strasse_Hsnr = data.get(CSV_DATA_STRASSE);
        this.Name_der_Schule = data.get(CSV_DATA_SCHULNAME);
        if(data.get(CSV_DATA_VORWAHL) != null && !data.get(CSV_DATA_VORWAHL).equals("") && !data.get(CSV_DATA_VORWAHL).equals("0"))
        {
            this.Vorwahl = data.get(CSV_DATA_VORWAHL);
        }
        if(data.get(CSV_DATA_TELEFONNUMMER) != null && !data.get(CSV_DATA_TELEFONNUMMER).equals("") && !data.get(CSV_DATA_TELEFONNUMMER).equals("0"))
        {
            this.Rufnummer = data.get(CSV_DATA_TELEFONNUMMER);
        }
    }

    private int getNormalizedDataInKbitFromString(String data)
    {
        if(data.contains(","))
        {
            double temp = Double.parseDouble(data.replaceAll(",",""));
            return (int) (temp * 1000);
        }
        else
        {
            int temp = 0;
            if(!data.equals("") && StringUtils.isNumeric(data)) {
                temp = Integer.parseInt(data);
                if (temp <= 1000) {
                    temp = temp * 1000;
                }
            }
            return temp;
        }
    }

    public static final String fehlendeSchulenSpalten[] = {"ID","SNR","Name der Schule","Art der Schule","PLZ","Ort"};

    public Vector<String> getfehlendVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(id));
        v.add(String.valueOf(SNR));
        v.add(Name_der_Schule);
        v.add(Art_der_Schule);
        v.add(String.valueOf(PLZ));
        v.add(Ort);
        return v;
    }
    public String getFehlendSting()
    {
        String v = String.valueOf(String.valueOf(SNR)).concat(";").concat(Name_der_Schule).concat(";").concat(Art_der_Schule).concat(";").concat(String.valueOf(PLZ)).concat(";").concat(Ort).concat(";");
        return v;
    }
    
}
