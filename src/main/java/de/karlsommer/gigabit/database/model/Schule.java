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
    
    private int id;
    private int Interne_Nummer;
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
    private String FB_Text;
    private String Zustaendig;
    private String Bedarf_S1;
    private String Status_S1;
    private String Moderator_S1;
    private String Datum_S1;
    private String Bedarf_S2;
    private String Status_S2;
    private String Moderator_S2;
    private String Datum_S2;
    private String Bedarf_S3;
    private String Status_S3;
    private String Moderator_S3;
    private String Datum_S3;
    private String Bedarf_S4;
    private String Status_S4;
    private String Moderator_S4;
    private String Datum_S4;
    private String Bedarf_R1;
    private String Status_R1;
    private String Moderator_R1;
    private String Datum_R1;
    private String Bedarf_R2;
    private String Status_R2;
    private String Moderator_R2;
    private String Datum_R2;
    private String Bedarf_L1;
    private String Status_L1;
    private String Moderator_L1;
    private String Datum_L1;
    private String Bedarf_K1;
    private String Status_K1;
    private String Moderator_K1;
    private String Datum_K1;
    private String Bedarf_K2;
    private String Status_K2;
    private String Moderator_K2;
    private String Datum_K2;
    private String Bedarf_A1;
    private String Status_A1;
    private String Moderator_A1;
    private String Datum_A1;
    private String Bedarf_X2;
    private String Status_X2;
    private String Moderator_X2;
    private String Datum_X2;
    private String Bedarf_X3;
    private String Status_X3;
    private String Moderator_X3;
    private String Datum_X3;
    private String Bemerkungen;
    private boolean flag;
    private String Status_GB;
    private int Anbindung_Kbit_DL;
    private int Anbindung_Kbit_UL;
    private String Status_MK;
    private String Status_Inhouse;
    private double lat;
    private double lng;

    public Schule(ArrayList<String> daten, boolean fromDatabase)
    {
        int i = 0;
        if(fromDatabase)
            this.id = Integer.parseInt(daten.get(i++));
        else
            this.id = -1;
        if(daten.get(i).equals(""))
        {
            this.Interne_Nummer = 0;
            i++;
        }else
            this.Interne_Nummer = Integer.parseInt(daten.get(i++));
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
        this.FB_Text = daten.get(i++);
        this.Zustaendig = daten.get(i++);
        this.Bedarf_S1 = daten.get(i++);
        this.Status_S1 = daten.get(i++);
        this.Moderator_S1 = daten.get(i++);
        this.Datum_S1 = daten.get(i++);
        this.Bedarf_S2 = daten.get(i++);
        this.Status_S2 = daten.get(i++);
        this.Moderator_S2 = daten.get(i++);
        this.Datum_S2 = daten.get(i++);
        this.Bedarf_S3 = daten.get(i++);
        this.Status_S3 = daten.get(i++);
        this.Moderator_S3 = daten.get(i++);
        this.Datum_S3 = daten.get(i++);
        this.Bedarf_S4 = daten.get(i++);
        this.Status_S4 = daten.get(i++);
        this.Moderator_S4 = daten.get(i++);
        this.Datum_S4 = daten.get(i++);
        this.Bedarf_R1 = daten.get(i++);
        this.Status_R1 = daten.get(i++);
        this.Moderator_R1 = daten.get(i++);
        this.Datum_R1 = daten.get(i++);
        this.Bedarf_R2 = daten.get(i++);
        this.Status_R2 = daten.get(i++);
        this.Moderator_R2 = daten.get(i++);
        this.Datum_R2 = daten.get(i++);
        this.Bedarf_L1 = daten.get(i++);
        this.Status_L1 = daten.get(i++);
        this.Moderator_L1 = daten.get(i++);
        this.Datum_L1 = daten.get(i++);
        this.Bedarf_K1 = daten.get(i++);
        this.Status_K1 = daten.get(i++);
        this.Moderator_K1 = daten.get(i++);
        this.Datum_K1 = daten.get(i++);
        this.Bedarf_K2 = daten.get(i++);
        this.Status_K2 = daten.get(i++);
        this.Moderator_K2 = daten.get(i++);
        this.Datum_K2 = daten.get(i++);
        this.Bedarf_A1 = daten.get(i++);
        this.Status_A1 = daten.get(i++);
        this.Moderator_A1 = daten.get(i++);
        this.Datum_A1 = daten.get(i++);
        this.Bedarf_X2 = daten.get(i++);
        this.Status_X2 = daten.get(i++);
        this.Moderator_X2 = daten.get(i++);
        this.Datum_X2 = daten.get(i++);
        if(fromDatabase)
        {
        this.Bedarf_X3 = daten.get(i++);
        this.Status_X3 = daten.get(i++);
        this.Moderator_X3 = daten.get(i++);
        this.Datum_X3 = daten.get(i++);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInterne_Nummer() {
        return Interne_Nummer;
    }

    public void setInterne_Nummer(int Interne_Nummer) {
        this.Interne_Nummer = Interne_Nummer;
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

    public String getFB_Text() {
        return FB_Text;
    }

    public void setFB_Text(String FB_Text) {
        this.FB_Text = FB_Text;
    }

    public String getZustaendig() {
        return Zustaendig;
    }

    public void setZustaendig(String Zuständig) {
        this.Zustaendig = Zuständig;
    }

    public String getBedarf_S1() {
        return Bedarf_S1;
    }

    public void setBedarf_S1(String Bedarf_S1) {
        this.Bedarf_S1 = Bedarf_S1;
    }

    public String getStatus_S1() {
        return Status_S1;
    }

    public void setStatus_S1(String Status_S1) {
        this.Status_S1 = Status_S1;
    }

    public String getModerator_S1() {
        return Moderator_S1;
    }

    public void setModerator_S1(String Moderator_S1) {
        this.Moderator_S1 = Moderator_S1;
    }

    public String getDatum_S1() {
        return Datum_S1;
    }

    public void setDatum_S1(String Datum_S1) {
        this.Datum_S1 = Datum_S1;
    }

    public String getBedarf_S2() {
        return Bedarf_S2;
    }

    public void setBedarf_S2(String Bedarf_S2) {
        this.Bedarf_S2 = Bedarf_S2;
    }

    public String getStatus_S2() {
        return Status_S2;
    }

    public void setStatus_S2(String Status_S2) {
        this.Status_S2 = Status_S2;
    }

    public String getModerator_S2() {
        return Moderator_S2;
    }

    public void setModerator_S2(String Moderator_S2) {
        this.Moderator_S2 = Moderator_S2;
    }

    public String getDatum_S2() {
        return Datum_S2;
    }

    public void setDatum_S2(String Datum_S2) {
        this.Datum_S2 = Datum_S2;
    }

    public String getBedarf_S3() {
        return Bedarf_S3;
    }

    public void setBedarf_S3(String Bedarf_S3) {
        this.Bedarf_S3 = Bedarf_S3;
    }

    public String getStatus_S3() {
        return Status_S3;
    }

    public void setStatus_S3(String Status_S3) {
        this.Status_S3 = Status_S3;
    }

    public String getModerator_S3() {
        return Moderator_S3;
    }

    public void setModerator_S3(String Moderator_S3) {
        this.Moderator_S3 = Moderator_S3;
    }

    public String getDatum_S3() {
        return Datum_S3;
    }

    public void setDatum_S3(String Datum_S3) {
        this.Datum_S3 = Datum_S3;
    }

    public String getBedarf_S4() {
        return Bedarf_S4;
    }

    public void setBedarf_S4(String Bedarf_S4) {
        this.Bedarf_S4 = Bedarf_S4;
    }

    public String getStatus_S4() {
        return Status_S4;
    }

    public void setStatus_S4(String Status_S4) {
        this.Status_S4 = Status_S4;
    }

    public String getModerator_S4() {
        return Moderator_S4;
    }

    public void setModerator_S4(String Moderator_S4) {
        this.Moderator_S4 = Moderator_S4;
    }

    public String getDatum_S4() {
        return Datum_S4;
    }

    public void setDatum_S4(String Datum_S4) {
        this.Datum_S4 = Datum_S4;
    }

    public String getBedarf_R1() {
        return Bedarf_R1;
    }

    public void setBedarf_R1(String Bedarf_R1) {
        this.Bedarf_R1 = Bedarf_R1;
    }

    public String getStatus_R1() {
        return Status_R1;
    }

    public void setStatus_R1(String Status_R1) {
        this.Status_R1 = Status_R1;
    }

    public String getModerator_R1() {
        return Moderator_R1;
    }

    public void setModerator_R1(String Moderator_R1) {
        this.Moderator_R1 = Moderator_R1;
    }

    public String getDatum_R1() {
        return Datum_R1;
    }

    public void setDatum_R1(String Datum_R1) {
        this.Datum_R1 = Datum_R1;
    }

    public String getBedarf_R2() {
        return Bedarf_R2;
    }

    public void setBedarf_R2(String Bedarf_R2) {
        this.Bedarf_R2 = Bedarf_R2;
    }

    public String getStatus_R2() {
        return Status_R2;
    }

    public void setStatus_R2(String Status_R2) {
        this.Status_R2 = Status_R2;
    }

    public String getModerator_R2() {
        return Moderator_R2;
    }

    public void setModerator_R2(String Moderator_R2) {
        this.Moderator_R2 = Moderator_R2;
    }

    public String getDatum_R2() {
        return Datum_R2;
    }

    public void setDatum_R2(String Datum_R2) {
        this.Datum_R2 = Datum_R2;
    }

    public String getBedarf_L1() {
        return Bedarf_L1;
    }

    public void setBedarf_L1(String Bedarf_L1) {
        this.Bedarf_L1 = Bedarf_L1;
    }

    public String getStatus_L1() {
        return Status_L1;
    }

    public void setStatus_L1(String Status_L1) {
        this.Status_L1 = Status_L1;
    }

    public String getModerator_L1() {
        return Moderator_L1;
    }

    public void setModerator_L1(String Moderator_L1) {
        this.Moderator_L1 = Moderator_L1;
    }

    public String getDatum_L1() {
        return Datum_L1;
    }

    public void setDatum_L1(String Datum_L1) {
        this.Datum_L1 = Datum_L1;
    }

    public String getBedarf_K1() {
        return Bedarf_K1;
    }

    public void setBedarf_K1(String Bedarf_K1) {
        this.Bedarf_K1 = Bedarf_K1;
    }

    public String getStatus_K1() {
        return Status_K1;
    }

    public void setStatus_K1(String Status_K1) {
        this.Status_K1 = Status_K1;
    }

    public String getModerator_K1() {
        return Moderator_K1;
    }

    public void setModerator_K1(String Moderator_K1) {
        this.Moderator_K1 = Moderator_K1;
    }

    public String getDatum_K1() {
        return Datum_K1;
    }

    public void setDatum_K1(String Datum_K1) {
        this.Datum_K1 = Datum_K1;
    }

    public String getBedarf_K2() {
        return Bedarf_K2;
    }

    public void setBedarf_K2(String Bedarf_K2) {
        this.Bedarf_K2 = Bedarf_K2;
    }

    public String getStatus_K2() {
        return Status_K2;
    }

    public void setStatus_K2(String Status_K2) {
        this.Status_K2 = Status_K2;
    }

    public String getModerator_K2() {
        return Moderator_K2;
    }

    public void setModerator_K2(String Moderator_K2) {
        this.Moderator_K2 = Moderator_K2;
    }

    public String getDatum_K2() {
        return Datum_K2;
    }

    public void setDatum_K2(String Datum_K2) {
        this.Datum_K2 = Datum_K2;
    }

    public String getBedarf_A1() {
        return Bedarf_A1;
    }

    public void setBedarf_A1(String Bedarf_A1) {
        this.Bedarf_A1 = Bedarf_A1;
    }

    public String getStatus_A1() {
        return Status_A1;
    }

    public void setStatus_A1(String Status_A1) {
        this.Status_A1 = Status_A1;
    }

    public String getModerator_A1() {
        return Moderator_A1;
    }

    public void setModerator_A1(String Moderator_A1) {
        this.Moderator_A1 = Moderator_A1;
    }

    public String getDatum_A1() {
        return Datum_A1;
    }

    public void setDatum_A1(String Datum_A1) {
        this.Datum_A1 = Datum_A1;
    }

    public String getBedarf_X2() {
        return Bedarf_X2;
    }

    public void setBedarf_X2(String Bedarf_X2) {
        this.Bedarf_X2 = Bedarf_X2;
    }

    public String getStatus_X2() {
        return Status_X2;
    }

    public void setStatus_X2(String Status_X2) {
        this.Status_X2 = Status_X2;
    }

    public String getModerator_X2() {
        return Moderator_X2;
    }

    public void setModerator_X2(String Moderator_X2) {
        this.Moderator_X2 = Moderator_X2;
    }

    public String getDatum_X2() {
        return Datum_X2;
    }

    public void setDatum_X2(String Datum_X2) {
        this.Datum_X2 = Datum_X2;
    }

    public String getBedarf_X3() {
        return Bedarf_X3;
    }

    public void setBedarf_X3(String Bedarf_X3) {
        this.Bedarf_X3 = Bedarf_X3;
    }

    public String getStatus_X3() {
        return Status_X3;
    }

    public void setStatus_X3(String Status_X3) {
        this.Status_X3 = Status_X3;
    }

    public String getModerator_X3() {
        return Moderator_X3;
    }

    public void setModerator_X3(String Moderator_X3) {
        this.Moderator_X3 = Moderator_X3;
    }

    public String getDatum_X3() {
        return Datum_X3;
    }

    public void setDatum_X3(String Datum_X3) {
        this.Datum_X3 = Datum_X3;
    }

    public String getBemerkungen() {
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
        return Status_MK;
    }

    public void setStatus_MK(String Status_MK) {
        this.Status_MK = Status_MK;
    }

    public String getStatus_Inhouse() {
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


   public static final String ausgabeSpalten[] = {"Interne Nummer","SNR","Name der Schule","Art der Schule","PLZ","Ort", "Upload", "Download"};
    
    public Vector<String> getVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(Interne_Nummer));
        v.add(String.valueOf(SNR));
        v.add(Name_der_Schule);
        v.add(Art_der_Schule);
        v.add(String.valueOf(PLZ));
        v.add(Ort);
        v.add(String.valueOf(Anbindung_Kbit_DL));
        v.add(String.valueOf(Anbindung_Kbit_UL));
        return v;
    }

    public static final int GIGABIT_TABELLE_SNR = 11;
    public static final int GIGABIT_TABELLE_DOWNSTREAM = 13;
    public static final int GIGABIT_TABELLE_UPSTREAM = 14;

    public void updateBreitbandData(ArrayList<String> data)
    { //13 ist Download 14 Upload
        if(this.getSNR() == Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)))
        {
            this.Anbindung_Kbit_DL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_DOWNSTREAM));
            this.Anbindung_Kbit_UL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_UPSTREAM));
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

    public static final String fehlendeSchulenSpalten[] = {"Interne Nummer","SNR","Name der Schule","Art der Schule","PLZ","Ort"};

    public Vector<String> getfehlendVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(Interne_Nummer));
        v.add(String.valueOf(SNR));
        v.add(Name_der_Schule);
        v.add(Art_der_Schule);
        v.add(String.valueOf(PLZ));
        v.add(Ort);
        return v;
    }
    public String getFehlendSting()
    {
        String v = String.valueOf(Interne_Nummer).concat(";").concat(String.valueOf(SNR)).concat(";").concat(Name_der_Schule).concat(";").concat(Art_der_Schule).concat(";").concat(String.valueOf(PLZ)).concat(";").concat(Ort).concat(";");
        return v;
    }
    
}
