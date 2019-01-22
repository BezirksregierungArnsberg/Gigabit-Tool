/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.karlsommer.gigabit.database.model;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author karl
 */
public class Schule {


    public static final int GIGABIT_TABELLE_ANSPRECHPARTNER = 4;
    public static final int GIGABIT_TABELLE_TELEFON_ANSPRECHPARTNER = 5;
    public static final int GIGABIT_TABELLE_EMAIL_ANSPRECHPARTNER = 6;
    public static final int GIGABIT_TABELLE_SCHULNAME = 7;
    public static final int GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER = 8;
    public static final int GIGABIT_TABELLE_ORT = 9;
    public static final int GIGABIT_TABELLE_PLZ = 10;
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
    public static final String TEILSTANDORT = "T";

    public static final String AUSBAU_AUSGEBAUT = "Ausgebaut";
    public static final String AUSBAU_IM_AUSBAU = "Im Ausbau";
    public static final String AUSBAU_EIGENWIRTSCHAFTLICH = "Eigenwirtschaftlich";
    public static final String AUSBAU_BUND = "Bund";
    public static final String AUSBAU_LAND = "Land";
    public static final String AUSBAU_GUTE_SCHULE = "Gute Schule";
    public static final String AUSBAU_UNGEKLAERT = "Ungeklärt";
    public static final String AUSBAU_TRAEGERENTSCHEIDUNG = "Trägerentscheidung";
    public static final String AUSBAU_E_BUND = "Berechnet Bund";
    public static final String AUSBAU_E_LAND = "Berechnet Land";
    
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
    private int Schuelerzahl;
    private String Ausbau;
    private String Ansprechpartner;
    private String Telefon_Ansprechpartner;
    private String Email_Ansprechpartner;
    private String standort;

    public String getAusbau(boolean html)
    {

        if(Ausbau.equals(AUSBAU_AUSGEBAUT) || Ausbau.equals(AUSBAU_EIGENWIRTSCHAFTLICH) ||Ausbau.equals(AUSBAU_BUND) ||Ausbau.equals(AUSBAU_LAND))
            return Ausbau;
        else if(html)
            return "ungekl&auml;rt";
        else
            return "Ungeklärt";
    }

    public void setAusbau(String pAusbau)
    {
        if(pAusbau != null) {
            switch (pAusbau) {
                case "Kein Bedarf - bereits Glas":
                    pAusbau=AUSBAU_AUSGEBAUT;
                    Ausbau = pAusbau;
                    break;
                case "noch unbekannt":
                    pAusbau=AUSBAU_UNGEKLAERT;
                    Ausbau = pAusbau;
                    break;
                case "ungekl&auml;rt":
                    pAusbau=AUSBAU_UNGEKLAERT;
                    Ausbau = pAusbau;
                    break;
                case AUSBAU_AUSGEBAUT:
                    ;
                case AUSBAU_UNGEKLAERT:
                    ;
                case AUSBAU_IM_AUSBAU:
                    ;
                case AUSBAU_EIGENWIRTSCHAFTLICH:
                    ;
                case AUSBAU_BUND:
                    ;
                case AUSBAU_LAND:
                    ;
                case AUSBAU_GUTE_SCHULE:
                    ;
                case AUSBAU_TRAEGERENTSCHEIDUNG:
                    ;
                case AUSBAU_E_BUND:
                    ;
                case AUSBAU_E_LAND:
                    Ausbau = pAusbau;
                    break;
                default:
                    System.out.println("Ausbau Fehlerhaft. " + pAusbau + " nicht in Datenbank");
            }
        }

    }

    public int getSchuelerzahl()
    {
        return Schuelerzahl;
    }

    public void setSchuelerzahl(int schuelerzahl)
    {
        Schuelerzahl = schuelerzahl;
    }

    public String getAnsprechpartner() {
        return Ansprechpartner;
    }

    public void setAnsprechpartner(String ansprechpartner) {
        Ansprechpartner = ansprechpartner;
    }

    public String getTelefon_Ansprechpartner() {
        return Telefon_Ansprechpartner;
    }

    public void setTelefon_Ansprechpartner(String telefon_Ansprechpartner) {
        Telefon_Ansprechpartner = telefon_Ansprechpartner;
    }

    public String getEmail_Ansprechpartner() {
        return Email_Ansprechpartner;
    }

    public void setEmail_Ansprechpartner(String email_Ansprechpartner) {
        Email_Ansprechpartner = email_Ansprechpartner;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }



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
            if(daten.get(i) != null)
                this.lat = Double.parseDouble(daten.get(i++));
            else
                i++;
            if(daten.get(i) != null)
                this.lng = Double.parseDouble(daten.get(i++));
            else
                i++;
            this.standort = daten.get(i++);
            this.Ansprechpartner = daten.get(i++);
            this.Telefon_Ansprechpartner = daten.get(i++);
            this.Email_Ansprechpartner = daten.get(i++);
            this.Schuelerzahl = Integer.parseInt(daten.get(i++));
            setAusbau(daten.get(i));
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

    public void createFromGigabitTabelle(ArrayList<String> data)
    {
        this.standort = HAUPTSTANDORT;
        this.Ansprechpartner = data.get(GIGABIT_TABELLE_ANSPRECHPARTNER);
        this.Telefon_Ansprechpartner = data.get(GIGABIT_TABELLE_TELEFON_ANSPRECHPARTNER);
        this.Email_Ansprechpartner = data.get(GIGABIT_TABELLE_EMAIL_ANSPRECHPARTNER);
        this.Name_der_Schule = data.get(GIGABIT_TABELLE_SCHULNAME);
        this.Strasse_Hsnr = data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER);
        this.Ort = data.get(GIGABIT_TABELLE_ORT);
        this.PLZ = Integer.parseInt(data.get(GIGABIT_TABELLE_PLZ));
        if(!data.get(GIGABIT_TABELLE_DOWNSTREAM).equals(""))
            this.Anbindung_Kbit_DL = Integer.parseInt(data.get(GIGABIT_TABELLE_DOWNSTREAM));
        else
            this.Anbindung_Kbit_DL = 0;
        if(!data.get(GIGABIT_TABELLE_UPSTREAM).equals(""))
            this.Anbindung_Kbit_UL = Integer.parseInt(data.get(GIGABIT_TABELLE_UPSTREAM));
        else
            this.Anbindung_Kbit_UL = 0;
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


   public static final String ausgabeSpalten[] = {"id","SNR","Name der Schule", "PLZ","Ort", "Strasse und HsNr","Download", "Upload", "Schüleranzahl", "Ausbau", "Schulamt"};



    public void updateData(Row row)
    {
        //"Id", "Schulnummer", ,"Rufnummer","Schulform","Schultyp","Mailadresse", "Bemerkungen",
        //        "Status GB" , ,"Ansprechpartner","Telefon Ansprechpartner","Email Ansprechpartner", "Schüleranzahl", "Ausbau"};
        int i = 1;
        this.setSNR(getIntValue(row.getCell(i++)));
        this.setName_der_Schule(getStringValue(row.getCell(i++)));
        this.setPLZ(getIntValue(row.getCell(i++)));
        this.setOrt(getStringValue(row.getCell(i++)));
        this.setStrasse_Hsnr(getStringValue(row.getCell(i++)));
        this.setZustaendiges_Schulamt(getStringValue(row.getCell(i++)));
        this.setVorwahl(String.valueOf(getIntValue(row.getCell(i++))));
        this.setRufnummer(String.valueOf(getIntValue(row.getCell(i++))));
        this.setSF(getStringValue(row.getCell(i++)));
        this.setSchultyp(getStringValue(row.getCell(i++)));
        this.setMailadresse(getStringValue(row.getCell(i++)));
        this.setBemerkungen(getStringValue(row.getCell(i++)));
        this.setStatus_GB(getStringValue(row.getCell(i++)));
        this.setAnbindung_Kbit_DL(getIntValue(row.getCell(i++)));
        this.setAnbindung_Kbit_UL(getIntValue(row.getCell(i++)));
        this.setStatus_MK(getStringValue(row.getCell(i++)));
        this.setStatus_Inhouse(getStringValue(row.getCell(i++)));
        this.setStandort(getStringValue(row.getCell(i++)));
        this.setAnsprechpartner(getStringValue(row.getCell(i++)));
        this.setTelefon_Ansprechpartner(getStringValue(row.getCell(i++)));
        this.setEmail_Ansprechpartner(getStringValue(row.getCell(i++)));
        this.setSchuelerzahl(getIntValue(row.getCell(i++)));
        this.setAusbau(getStringValue(row.getCell(i++)));
    }
    public void updateBreitbandData(ArrayList<String> data)
    { //13 ist Download 14 Upload
        if(this.getSNR() == Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)))
        {
            if(data.size() > 14) {
                this.Anbindung_Kbit_DL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_DOWNSTREAM));
                this.Anbindung_Kbit_UL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_UPSTREAM));
                this.Ansprechpartner = data.get(GIGABIT_TABELLE_ANSPRECHPARTNER);
                this.Telefon_Ansprechpartner = data.get(GIGABIT_TABELLE_TELEFON_ANSPRECHPARTNER);
                this.Email_Ansprechpartner = data.get(GIGABIT_TABELLE_EMAIL_ANSPRECHPARTNER);
            }
            else
                System.out.println("-----"+data+"----");
        }
    }

    public String getChangedValues(Schule schule)
    {
        String returnString = "";

        if(!(this.SNR == (schule.getSNR())))
        {
            returnString += "SNR von "+this.SNR+" zu "+schule.getSNR()+" ";
        }
        if(!this.Name_der_Schule.equals(schule.getName_der_Schule()))
        {
            returnString += "Name_der_Schule von "+this.Name_der_Schule+" zu "+schule.getName_der_Schule()+" ";
        }
        if(!this.Art_der_Schule.equals(schule.getArt_der_Schule()))
        {
            returnString += "Art_der_Schule von "+this.Art_der_Schule+" zu "+schule.getArt_der_Schule()+" ";
        }
        if(!(this.PLZ == (schule.getPLZ())))
        {
            returnString += "PLZ von "+this.PLZ+" zu "+schule.getPLZ()+" ";
        }
        if(!this.Ort.equals(schule.getOrt()))
        {
            returnString += "Ort von "+this.Ort+" zu "+schule.getOrt()+" ";
        }
        if(!this.Strasse_Hsnr.equals(schule.getStrasse_Hsnr()))
        {
            returnString += "Strasse_Hsnr von "+this.Strasse_Hsnr+" zu "+schule.getStrasse_Hsnr()+" ";
        }
        if(!this.Zustaendiges_Schulamt.equals(schule.getZustaendiges_Schulamt()))
        {
            returnString += "Zustaendiges_Schulamt von "+this.Zustaendiges_Schulamt+" zu "+schule.getZustaendiges_Schulamt()+" ";
        }
        if(!this.Vorwahl.equals(schule.getVorwahl()))
        {
            returnString += "Vorwahl von "+this.Vorwahl+" zu "+schule.getVorwahl()+" ";
        }
        if(!this.Rufnummer.equals(schule.getRufnummer()))
        {
            returnString += "Rufnummer von "+this.Rufnummer+" zu "+schule.getRufnummer()+" ";
        }
        if(!this.SF.equals(schule.getSF()))
        {
            returnString += "SF von "+this.SF+" zu "+schule.getSF()+" ";
        }
        if(!this.Schultyp.equals(schule.getSchultyp()))
        {
            returnString += "Schultyp von "+this.Schultyp+" zu "+schule.getSchultyp()+" ";
        }
        if(!this.Mailadresse.equals(schule.getMailadresse()))
        {
            returnString += "Mailadresse von "+this.Mailadresse+" zu "+schule.getMailadresse()+" ";
        }
        if(!this.Bemerkungen.equals(schule.getBemerkungen()))
        {
            returnString += "Bemerkungen von "+this.Bemerkungen+" zu "+schule.getBemerkungen()+" ";
        }
        if(!(this.flag == (schule.isFlag())))
        {
            returnString += "flag von "+this.flag+" zu "+schule.isFlag()+" ";
        }
        if(!this.Status_GB.equals(schule.getStatus_GB()))
        {
            returnString += "Status_GB von "+this.Status_GB+" zu "+schule.getStatus_GB()+" ";
        }
        if(!(this.Anbindung_Kbit_DL == (schule.getAnbindung_Kbit_DL())))
        {
            returnString += "Anbindung_Kbit_DL von "+this.Anbindung_Kbit_DL+" zu "+schule.getAnbindung_Kbit_DL()+" ";
        }
        if(!(this.Anbindung_Kbit_UL == (schule.getAnbindung_Kbit_UL())))
        {
            returnString += "Anbindung_Kbit_UL von "+this.Anbindung_Kbit_UL+" zu "+schule.getAnbindung_Kbit_UL()+" ";
        }
        if(!this.Status_MK.equals(schule.getStatus_MK()))
        {
            returnString += "Status_MK von "+this.Status_MK+" zu "+schule.getStatus_MK()+" ";
        }
        if(!this.Status_Inhouse.equals(schule.getStatus_Inhouse()))
        {
            returnString += "Status_Inhouse von "+this.Status_Inhouse+" zu "+schule.getStatus_Inhouse()+" ";
        }
        if(!(this.lat == (schule.getLat())))
        {
            returnString += "lat von "+this.lat+" zu "+schule.getLat()+" ";
        }
        if(!(this.lng == (schule.getLng())))
        {
            returnString += "lng von "+this.lng+" zu "+schule.getLng()+" ";
        }
        if(!(this.Schuelerzahl == (schule.getSchuelerzahl())))
        {
            returnString += "Schuelerzahl von "+this.Schuelerzahl+" zu "+schule.getSchuelerzahl()+" ";
        }
        if(!this.Ausbau.equals(schule.getAusbau(false)))
        {
            returnString += "Ausbau von "+this.Ausbau+" zu "+schule.getAusbau(false)+" ";
        }
        if(!this.Ansprechpartner.equals(schule.getAnsprechpartner()))
        {
            returnString += "Ansprechpartner von "+this.Ansprechpartner+" zu "+schule.getAnsprechpartner()+" ";
        }
        if(!this.Telefon_Ansprechpartner.equals(schule.getTelefon_Ansprechpartner()))
        {
            returnString += "Telefon_Ansprechpartner von "+this.Telefon_Ansprechpartner+" zu "+schule.getTelefon_Ansprechpartner()+" ";
        }
        if(!this.Email_Ansprechpartner.equals(schule.getEmail_Ansprechpartner()))
        {
            returnString += "Email_Ansprechpartner von "+this.Email_Ansprechpartner+" zu "+schule.getEmail_Ansprechpartner()+" ";
        }
        if(!this.standort.equals(schule.getStandort()))
        {
            returnString += "standort von "+this.standort+" zu "+schule.getStandort()+" ";
        }
        return returnString;
    }

    public boolean isEqualTo(Schule schule)
    {
        if(getChangedValues(schule).equals(""))
            return true;
        else
            return false;
    }

    private int getIntValue(Cell cell)
    {
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return (int)(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                if(cell.getStringCellValue().equals("null"))
                    return 0;
                else if(cell.getStringCellValue().equals(""))
                    return 0;
                else
                    return Integer.parseInt(cell.getStringCellValue());
            case Cell.CELL_TYPE_BLANK:
                return 0;
            default:
                return 0;
        }
    }

    private String getStringValue(Cell cell)
    {
        if(cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case Cell.CELL_TYPE_BLANK:
                    return "";
                default:
                    return "";
            }
        }
        return "";
    }

    public void createTeilstandortWithCSV(Schule hauptstandort, ArrayList<String> data)
    {
        this.standort = TEILSTANDORT;
        this.Ansprechpartner = data.get(GIGABIT_TABELLE_ANSPRECHPARTNER);
        this.Telefon_Ansprechpartner = data.get(GIGABIT_TABELLE_TELEFON_ANSPRECHPARTNER);
        this.Email_Ansprechpartner = data.get(GIGABIT_TABELLE_EMAIL_ANSPRECHPARTNER);
        this.Name_der_Schule = hauptstandort.getName_der_Schule();
        this.Strasse_Hsnr = data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER);
        this.Ort = data.get(GIGABIT_TABELLE_ORT);
        if(data.get(GIGABIT_TABELLE_PLZ).equals(""))
            this.PLZ = hauptstandort.getPLZ();
        else
            this.PLZ = Integer.parseInt(data.get(GIGABIT_TABELLE_PLZ));
        if(!data.get(GIGABIT_TABELLE_DOWNSTREAM).equals(""))
            this.Anbindung_Kbit_DL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_DOWNSTREAM));
        else
            this.Anbindung_Kbit_DL = 0;
        if(!data.get(GIGABIT_TABELLE_UPSTREAM).equals(""))
            this.Anbindung_Kbit_UL = getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_UPSTREAM));
        else
            this.Anbindung_Kbit_UL = 0;
        this.insertHauptstandortData(hauptstandort);
    }

    public void insertHauptstandortData(Schule hauptstandort)
    {
        this.Zustaendiges_Schulamt = hauptstandort.getZustaendiges_Schulamt();
        this.SF = hauptstandort.getSF();
        this.Schultyp = hauptstandort.getSchultyp();
        this.SNR = hauptstandort.getSNR();
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

    public int getNormalizedDataInKbitFromString(String data)
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

    public Vector<String> getVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(id));
        v.add(String.valueOf(SNR));
        v.add(Name_der_Schule);
        v.add(String.valueOf(PLZ));
        v.add(Ort);
        v.add(Strasse_Hsnr);
        v.add(String.valueOf(Anbindung_Kbit_DL));
        v.add(String.valueOf(Anbindung_Kbit_UL));
        v.add(String.valueOf(Schuelerzahl));
        v.add(String.valueOf(Ausbau));
        v.add(String.valueOf(Zustaendiges_Schulamt));
        return v;
    }

    public String getFehlendSting()
    {
        String v = String.valueOf(String.valueOf(SNR)).concat(";").concat(Name_der_Schule).concat(";").concat(Art_der_Schule).concat(";").concat(String.valueOf(PLZ)).concat(";").concat(Ort).concat(";");
        return v;
    }

}
