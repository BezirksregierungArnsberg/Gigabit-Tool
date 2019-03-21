/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.karlsommer.gigabit.database.repositories;

import de.karlsommer.gigabit.database.DatabaseConnector;
import de.karlsommer.gigabit.database.model.LogEntry;
import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.datastructures.QueryResult;

import java.util.ArrayList;
import java.util.Arrays;

import static de.karlsommer.gigabit.database.model.Schule.*;
import static de.karlsommer.gigabit.helper.MathHelper.round;


public class SchuleRepository {

    private Schule getSchuleForQuery(String query)
    {
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if (checkValidity(result)) return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    private ArrayList<Schule> getArrayListForQuery(String query)
    {
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public boolean entryExisits(String condition, String value)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM "+TABLENAME+" WHERE "+condition+"='"+value+"';");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null)
            return false;
        return result.getColumnCount()>0;
    }

    public ArrayList<Schule> getSchoolsOrderedBy(String orderedBy)
    {
        return getArrayListForQuery("SELECT * FROM "+TABLENAME+" ORDER BY "+orderedBy+";");
    }

    public ArrayList<Schule> getSchulenMitGleichenGeolocations()
    {
        return getArrayListForQuery("SELECT Schulen1.* FROM "+TABLENAME+" AS Schulen1 JOIN (SELECT "+KEY_SNR+",lat,lng FROM "+TABLENAME+") AS Schulen2 ON (Schulen1.lat=Schulen2.lat) WHERE Schulen1."+KEY_SNR+" <> Schulen2."+KEY_SNR+" AND Schulen1.lng=Schulen2.lng AND Schulen1."+KEY_SNR+" < Schulen2."+KEY_SNR+" ORDER BY Schulen1."+KEY_SNR+";");
    }

    public String[] getAllValuesForKeyInDatabase(String type)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT DISTINCT "+type+" FROM "+TABLENAME+" ORDER BY "+type+" ASC;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        String[] ausbau = new String[result.getRowCount()];
        for(int i = 0; i < result.getRowCount(); i++)
        {
            ausbau[i] = result.getData()[i][0];
        }
        return ausbau;
    }

    public int getMaxID()
    {
        return getIntQueryValue("SELECT MAX("+KEY_ID+") FROM "+TABLENAME+";");
    }

    public int getIntQueryValue(String query)
    {
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null || result.getData() == null || result.getData()[0] == null || result.getData()[0][0] == null)
            return 0;
        return Integer.parseInt(result.getData()[0][0]);
    }

    public double getDoubleQueryValue(String query, int places)
    {
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null || result.getData() == null || result.getData()[0][0] == null)
            return 0d;
        return round(Double.parseDouble(result.getData()[0][0]),places);
    }

    public void save(Schule schule) {
        LogEntryRepository logEntryRepository = new LogEntryRepository();
        if(entryExisits(KEY_ID, String.valueOf(schule.getId())))
        {
            StringBuilder query = new StringBuilder();
            Schule oldSchule = getSchoolWith(KEY_ID+"="+String.valueOf(schule.getId()));

            query.append("UPDATE "+TABLENAME+" SET "+KEY_SNR+"="+schule.getSNR()+"," +
                    "[Name der Schule]='"+schule.getName_der_Schule()+"'," +
                    "[Art der Schule]='"+schule.getArt_der_Schule()+"'," +
                    ""+KEY_PLZ+"="+schule.getPLZ()+"," +
                    ""+KEY_ORT+"='"+schule.getOrt()+"'," +
                    "[Straße + Hsnr.]='"+schule.getStrasse_Hsnr()+"'," +
                    ""+KEY_ZUSTAENDIGES_SCHULAMT+"='"+schule.getZustaendiges_Schulamt()+"'" +
                    ",Vorwahl ='"+schule.getVorwahl()+"'," +
                    "Rufnummer ='"+schule.getRufnummer()+"'," +
                    "SF ='"+schule.getSF()+"'," +
                    "Schultyp ='"+schule.getSchultyp()+"'," +
                    "Mailadresse ='"+schule.getMailadresse()+"'," +
                    "Bemerkungen='"+schule.getBemerkungen()+"'," +
                    "flag="+schule.isFlag()+"," +
                    "[Status GB]='"+schule.getStatus_GB()+"'," +
                    "[Anbindung Kbit DL]='"+schule.getAnbindung_Kbit_DL()+"'," +
                    "[Anbindung Kbit UL]='"+schule.getAnbindung_Kbit_UL()+"'," +
                    "[Status MK]='"+schule.getStatus_MK()+"'," +
                    "[Status Inhouse]='"+schule.getStatus_Inhouse()+"',");

            if(schule.getLng() != 0 && schule.getLat() != 0) {
                query.append("lat=" + schule.getLat() + ",");
                query.append("lng=" + schule.getLng() + ",");
            }

            query.append("Standort='"+schule.getStandort()+"'," +
                    "Ansprechpartner='"+schule.getAnsprechpartner()+"'," +
                    "[Telefon Ansprechpartner]='"+schule.getTelefon_Ansprechpartner()+"'," +
                    "[Email Ansprechpartner]='"+schule.getEmail_Ansprechpartner()+"'," +
                    "Schuelerzahl="+schule.getSchuelerzahl()+"," +
                    "Ausbau='"+schule.getAusbau(false)+"'," +
                    "Klassenanzahl="+schule.getKlassenanzahl()+"," +
                    "[PWC Download]="+schule.getPWCDownload()+"," +
                    "[PWC Upload]="+schule.getPWCUpload()+"," +
                    "schuelerzahlIT="+schule.getSchuelerzahlIT()+"," +
                    "schultraeger='"+schule.getSchultraeger()+"'," +
                    "Beratungsstatus='"+schule.getBeratungsstatus()+"', " +
                    "aktenzeichenBund='"+schule.getAktenzeichenBund()+"'," +
                    "aktenzeichenLand='"+schule.getAktenzeichenLand()+"'" +
                    "WHERE id="+schule.getId()+";");


            DatabaseConnector.getInstance().executeStatement(query.toString());
            if(!oldSchule.isEqualTo(schule)) {
                logEntryRepository.save(new LogEntry("Schule mit ID:" + schule.getId() + " geändert. Veränderte Werte: "+oldSchule.getChangedValues(schule)));
            }
        }
        else
        {
            String query = "INSERT INTO "+TABLENAME+" VALUES(null,'"+schule.getSNR()+"','"+schule.getName_der_Schule()+"','"+schule.getArt_der_Schule()+"','"+schule.getPLZ()+"','"+schule.getOrt()+"','"+schule.getStrasse_Hsnr()+"','"+schule.getZustaendiges_Schulamt()+"','"+schule.getVorwahl()+"','"+schule.getRufnummer()+"','"+schule.getSF()+"','"+schule.getSchultyp()+"','"+schule.getMailadresse()+"','"+schule.getBemerkungen()+"','"+schule.isFlag()+"','"+schule.getStatus_GB()+"','"+schule.getAnbindung_Kbit_DL()+"','"+schule.getAnbindung_Kbit_UL()+"','"+schule.getStatus_MK()+"','"+schule.getStatus_Inhouse()+"','"+schule.getLat()+"','"+schule.getLng()+"','"+schule.getStandort()+"','"+schule.getAnsprechpartner()+"','"+schule.getTelefon_Ansprechpartner()+"','"+schule.getEmail_Ansprechpartner()+"', "+schule.getSchuelerzahl()+",'"+schule.getAusbau(false)+"',"+schule.getKlassenanzahl()+","+schule.getPWCDownload()+","+schule.getPWCUpload()+", "+schule.getSchuelerzahlIT()+",'"+schule.getSchultraeger()+"', '"+schule.getBeratungsstatus()+"','"+schule.getAktenzeichenBund()+"','"+schule.getAktenzeichenLand()+"');";
            DatabaseConnector.getInstance().executeStatement(query);
            logEntryRepository.save(new LogEntry("Neue Schule angelegt. Werte: '"+schule.getSNR()+"','"+schule.getName_der_Schule()+"','"+schule.getArt_der_Schule()+"','"+schule.getPLZ()+"','"+schule.getOrt()+"','"+schule.getStrasse_Hsnr()+"','"+schule.getZustaendiges_Schulamt()+"','"+schule.getVorwahl()+"','"+schule.getRufnummer()+"','"+schule.getSF()+"','"+schule.getSchultyp()+"','"+schule.getMailadresse()+"','"+schule.getBemerkungen()+"','"+schule.isFlag()+"','"+schule.getStatus_GB()+"','"+schule.getAnbindung_Kbit_DL()+"','"+schule.getAnbindung_Kbit_UL()+"','"+schule.getStatus_MK()+"','"+schule.getStatus_Inhouse()+"','"+schule.getLat()+"','"+schule.getLng()+"','"+schule.getStandort()+"','"+schule.getAnsprechpartner()+"','"+schule.getTelefon_Ansprechpartner()+"','"+schule.getEmail_Ansprechpartner()+"', "+schule.getSchuelerzahl()+",'"+schule.getAusbau(false)+"', "+schule.getKlassenanzahl()+","+schule.getPWCDownload()+","+schule.getPWCUpload()+", "+schule.getSchuelerzahlIT()+", '"+schule.getSchultraeger()+"', '"+schule.getBeratungsstatus()+"','"+schule.getAktenzeichenBund()+"','"+schule.getAktenzeichenLand()+"'"));
        }
    }

    public void flagSchool(String SNR) {
        String query = "UPDATE "+TABLENAME+" SET flag=1 WHERE "+KEY_SNR+"="+SNR+";";
        DatabaseConnector.getInstance().executeStatement(query);
    }

    public Schule findSchuleWithValues(String schulname, String PLZ, String strasse)
    {
        if(strasse == null || strasse.equals(""))
            return getSchuleForQuery("SELECT * FROM "+TABLENAME+" WHERE PLZ="+PLZ+" AND [Name der Schule] LIKE \""+schulname+"%\";");
        else if (PLZ == null || PLZ.equals("") || PLZ.length() < 5)
            return getSchuleForQuery("SELECT * FROM "+TABLENAME+" WHERE [Straße + Hsnr.] LIKE \""+strasse+"%\" AND [Name der Schule] LIKE \""+schulname+"%\";");
        else if (schulname == null || schulname.equals(""))
            return getSchuleForQuery("SELECT * FROM "+TABLENAME+" WHERE PLZ="+PLZ+" AND [Straße + Hsnr.] LIKE \""+strasse+"%\";");

        if(!strasse.equals(""))
            strasse = strasse.substring(0,strasse.length()-8);
        if(!schulname.equals(""))
            schulname = schulname.substring(0,5);
        String query = "SELECT * FROM "+TABLENAME+" WHERE "+KEY_PLZ+"="+PLZ+" AND [Straße + Hsnr.] LIKE \""+strasse+"%\" AND [Name der Schule] LIKE \""+schulname+"%\";";
        //System.out.println(query);
        return getSchuleForQuery(query);
    }

    public void deleteSchoolWithID(String _id)
    {
        DatabaseConnector.getInstance().executeStatement("DELETE FROM "+TABLENAME+" WHERE "+KEY_ID+"="+_id+";");
    }

    public Schule getHauptstandortWithID(String id)
    {
        Schule standort = getSchoolWith(KEY_ID+"="+id);
        if(standort == null)
            return null;
        if (standort.getStandort().equals(HAUPTSTANDORT))
            return standort;
        else
            return getHauptstandorteZu(String.valueOf(standort.getSNR()));
    }

    private boolean checkValidity(QueryResult result) {
        if(result == null)
            return true;
        else if(result.getData() == null)
            return true;
        else if(result.getData().length<1)
            return true;
        else if(result.getData()[0] == null)
            return true;
        else if(result.getData()[0].length < 10)
            return true;
        return false;
    }

    public Schule getSchoolWith(String condition)
    {
        return getSchuleForQuery("SELECT * FROM "+TABLENAME+" WHERE "+condition+"");
    }

    public Schule getHauptstandorteZu(String _snr)
    {
        return getSchoolWith(KEY_SNR+"="+_snr+" AND "+KEY_STANDORT+"='H'");
    }

    public ArrayList<Schule> getSchools(String condition)
    {
        return getArrayListForQuery("SELECT * FROM "+TABLENAME+" WHERE "+condition+";");
    }

    public ArrayList<Schule> getAllFlagedSchools(boolean flaged)
    {
        return getSchools("flag="+(flaged?"1":"0")+"");
    }

    public ArrayList<Schule> getSchoolsWithCondition(String filterSNR, String filterOrt, String filterSchulamt, String filterAusbau, String filterBeratungsstatus)
    {
        String query = "SELECT * FROM "+TABLENAME;
        if(!filterOrt.equals("-") || !filterSchulamt.equals("-") || !filterSNR.equals("") || !filterAusbau.equals("alle") || !filterBeratungsstatus.equals("alle"))
            query+=" WHERE ";
        if(!filterSNR.equals(""))
        {
            query+= KEY_SNR+" LIKE '%"+filterSNR+"%'";
            if(!filterSchulamt.equals("-") || !filterOrt.equals("-") || !filterAusbau.equals("alle") || !filterBeratungsstatus.equals("alle"))
                query+= " AND ";
        }
        if(!filterSchulamt.equals("-"))
        {
            query+= KEY_ZUSTAENDIGES_SCHULAMT+" LIKE '"+filterSchulamt+"'";
            if(!filterOrt.equals("-") || !filterAusbau.equals("alle") || !filterBeratungsstatus.equals("alle"))
                query+= " AND ";
        }
        if(!filterOrt.equals("-"))
        {
            query+= KEY_ORT+" LIKE '"+filterOrt+"'";
            if(!filterAusbau.equals("alle") || !filterBeratungsstatus.equals("alle"))
                query+= " AND ";
        }
        if(!filterAusbau.equals("alle"))
        {
            if(filterAusbau.equals("Bund (alle)"))
                query+= KEY_AUSBAU+" LIKE 'Bund%'";
            else
                query+= KEY_AUSBAU+" LIKE '"+filterAusbau+"'";
            if(!filterBeratungsstatus.equals("alle"))
                query+= " AND ";
        }
        if(!filterBeratungsstatus.equals("alle"))
        {
            query+= KEY_BERATUNGSSTATUS+" LIKE '"+filterBeratungsstatus+"'";
        }
        query += ";";
        return getArrayListForQuery(query);
    }

    public void resetFlags()
    {
        String query = "UPDATE "+TABLENAME+" SET flag=0 WHERE "+KEY_ID+">0;";
        DatabaseConnector.getInstance().executeStatement(query);
    }
}
