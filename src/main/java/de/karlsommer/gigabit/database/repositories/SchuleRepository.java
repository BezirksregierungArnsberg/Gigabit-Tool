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

import static de.karlsommer.gigabit.database.model.Schule.HAUPTSTANDORT;
import static de.karlsommer.gigabit.helper.MathHelper.round;


public class SchuleRepository {
    

    public boolean schoolWithSNRExists(int schulnummer) {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE SNR="+String.valueOf(schulnummer)+";");
        /*SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
        String[] projection = {
                SubjectTable._ID,
                SubjectTable.COLUMN_NAME_NAME,
                SubjectTable.COLUMN_NAME_TEACHER,
                SubjectTable.COLUMN_NAME_IS_MAJOR_CLASS
        };

        String selection = SubjectTable._ID + " = ?";
        String[] selectionArgs = { Integer.toString(subjectId) };

        String sortOrder = SubjectTable.COLUMN_NAME_NAME;

        Cursor cursor = db.query(
                SubjectTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        cursor.moveToNext();
        Subject subject = createSubjectFromSql(cursor);
        cursor.close();*/
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null)
            return false;
        return result.getColumnCount()>0;
    }

    public boolean schoolWithIDExists(int _id)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE id="+_id+";");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null)
            return false;
        return result.getColumnCount()>0;
    }

    public boolean schuleWithPLZExisits(int PLZ)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE PLZ="+PLZ+";");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null)
            return false;
        return result.getColumnCount()>0;
    }

    public ArrayList<Schule> getSchoolsOrderedByPLZ()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen ORDER BY PLZ;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public ArrayList<Schule> getSchoolsWithoutAusbau()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE Ausbau IS NULL;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public ArrayList<Schule> getSchulenMitGleichenGeolocations()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT Schulen1.* FROM Schulen AS Schulen1 JOIN (SELECT SNR,lat,lng FROM Schulen) AS Schulen2 ON (Schulen1.lat=Schulen2.lat) WHERE Schulen1.SNR <> Schulen2.SNR AND Schulen1.lng=Schulen2.lng AND Schulen1.SNR < Schulen2.SNR ORDER BY Schulen1.SNR;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }
    
    public ArrayList<Schule> getSchoolsWithoutGeodata()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE lat=0;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public String[] getAllAusbaustatus()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT DISTINCT Ausbau FROM Schulen ORDER BY Ausbau ASC;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        String[] ausbau = new String[result.getRowCount()];
        for(int i = 0; i < result.getRowCount(); i++)
        {
            ausbau[i] = result.getData()[i][0];
        }
        return ausbau;
    }

    public String[] getAllSschulaemter()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT DISTINCT [Zuständiges Schulamt] FROM Schulen ORDER BY [Zuständiges Schulamt] ASC;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        String[] staedte = new String[result.getRowCount()];
        for(int i = 0; i < result.getRowCount(); i++)
        {
            staedte[i] = result.getData()[i][0];
        }
        return staedte;
    }

    public String[] getAllStaedte()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT DISTINCT Ort FROM Schulen ORDER BY Ort ASC;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        String[] staedte = new String[result.getRowCount()];
        for(int i = 0; i < result.getRowCount(); i++)
        {
            staedte[i] = result.getData()[i][0];
        }
        return staedte;
    }

    public ArrayList<Schule> getSchoolsWithAusbau(String status)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE Ausbau LIKE '%"+status+"%';");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }
    
    public ArrayList<Schule> getSchoolsWithGeodata()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE lat<>0;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public int getMaxID()
    {
        DatabaseConnector.getInstance().executeStatement("SELECT MAX(id) FROM Schulen;");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        return Integer.parseInt(result.getData()[0][0]);
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
        LogEntry logEntry;
        if(schoolWithIDExists(schule.getId()))
        {
            String query;
            Schule oldSchule = getSchoolWithID(String.valueOf(schule.getId()));
            if(schule.getLng() != 0 && schule.getLat() != 0)
             query = "UPDATE Schulen SET SNR="+schule.getSNR()+",[Name der Schule]='"+
                    schule.getName_der_Schule()+"',[Art der Schule]='"+schule.getArt_der_Schule()+"',PLZ="+schule.getPLZ()+",Ort='"+schule.getOrt()+"',[Straße + Hsnr.]='"+schule.getStrasse_Hsnr()+"',[Zuständiges Schulamt]='"+schule.getZustaendiges_Schulamt()+"',Vorwahl ='"+
                    schule.getVorwahl()+"',Rufnummer ='"+schule.getRufnummer()+"',SF ='"+schule.getSF()+"',Schultyp ='"+schule.getSchultyp()+"',Mailadresse ='"+schule.getMailadresse()+"',Bemerkungen='"+
                    schule.getBemerkungen()+"',flag="+schule.isFlag()+",[Status GB]='"+schule.getStatus_GB()+"',[Anbindung Kbit DL]='"+schule.getAnbindung_Kbit_DL()+"',[Anbindung Kbit UL]='"+schule.getAnbindung_Kbit_UL()+"',[Status MK]='"+schule.getStatus_MK()+"',[Status Inhouse]='"+schule.getStatus_Inhouse()+"',lat="+schule.getLat()+",lng="+
                    schule.getLng()+", Standort='"+schule.getStandort()+"', Ansprechpartner='"+schule.getAnsprechpartner()+"', [Telefon Ansprechpartner]='"+schule.getTelefon_Ansprechpartner()+"', [Email Ansprechpartner]='"+schule.getEmail_Ansprechpartner()+"', Schuelerzahl="+schule.getSchuelerzahl()+", Ausbau='"+schule.getAusbau(false)+"'," +
                    "Klassenanzahl="+schule.getKlassenanzahl()+",[PWC Download]="+schule.getPWCDownload()+",[PWC Upload]="+schule.getPWCUpload()+", schuelerzahlIT="+schule.getSchuelerzahlIT()+" WHERE id="+schule.getId()+";";
            else
                query = "UPDATE Schulen SET SNR="+schule.getSNR()+",[Name der Schule]='"+
                    schule.getName_der_Schule()+"',[Art der Schule]='"+schule.getArt_der_Schule()+"',PLZ="+schule.getPLZ()+",Ort='"+schule.getOrt()+"',[Straße + Hsnr.]='"+schule.getStrasse_Hsnr()+"',[Zuständiges Schulamt]='"+schule.getZustaendiges_Schulamt()+"',Vorwahl ='"+
                    schule.getVorwahl()+"',Rufnummer ='"+schule.getRufnummer()+"',SF ='"+schule.getSF()+"',Schultyp ='"+schule.getSchultyp()+"',Mailadresse ='"+schule.getMailadresse()+"',Bemerkungen='"+
                    schule.getBemerkungen()+"',flag="+schule.isFlag()+",[Status GB]='"+schule.getStatus_GB()+"',[Anbindung Kbit DL]='"+schule.getAnbindung_Kbit_DL()+"',[Anbindung Kbit UL]='"+schule.getAnbindung_Kbit_UL()+"',[Status MK]='"+schule.getStatus_MK()+"',[Status Inhouse]='"+
                    schule.getStatus_Inhouse()+"', Standort='"+schule.getStandort()+"', Ansprechpartner='"+schule.getAnsprechpartner()+"', [Telefon Ansprechpartner]='"+schule.getTelefon_Ansprechpartner()+"', [Email Ansprechpartner]='"+schule.getEmail_Ansprechpartner()+"', Schuelerzahl="+schule.getSchuelerzahl()+", Ausbau='"+schule.getAusbau(false)+"'," +
                    "Klassenanzahl="+schule.getKlassenanzahl()+",[PWC Download]="+schule.getPWCDownload()+",[PWC Upload]="+schule.getPWCUpload()+", schuelerzahlIT="+schule.getSchuelerzahlIT()+" WHERE id="+schule.getId()+";";

            DatabaseConnector.getInstance().executeStatement(query);
            if(!oldSchule.isEqualTo(schule)) {
                String logText = "Schule mit ID:" + schule.getId() + " geändert. Veränderte Werte: "+oldSchule.getChangedValues(schule);
                logEntry = new LogEntry(logText);
                logEntryRepository.save(logEntry);
            }
        }
        else
        {
            String query = "INSERT INTO Schulen VALUES(null,'"+schule.getSNR()+"','"+schule.getName_der_Schule()+"','"+schule.getArt_der_Schule()+"','"+schule.getPLZ()+"','"+schule.getOrt()+"','"+schule.getStrasse_Hsnr()+"','"+schule.getZustaendiges_Schulamt()+"','"+schule.getVorwahl()+"','"+schule.getRufnummer()+"','"+schule.getSF()+"','"+schule.getSchultyp()+"','"+schule.getMailadresse()+"','"+schule.getBemerkungen()+"','"+schule.isFlag()+"','"+schule.getStatus_GB()+"','"+schule.getAnbindung_Kbit_DL()+"','"+schule.getAnbindung_Kbit_UL()+"','"+schule.getStatus_MK()+"','"+schule.getStatus_Inhouse()+"','"+schule.getLat()+"','"+schule.getLng()+"','"+schule.getStandort()+"','"+schule.getAnsprechpartner()+"','"+schule.getTelefon_Ansprechpartner()+"','"+schule.getEmail_Ansprechpartner()+"', "+schule.getSchuelerzahl()+",'"+schule.getAusbau(false)+"',"+schule.getKlassenanzahl()+","+schule.getPWCDownload()+","+schule.getPWCUpload()+", "+schule.getSchuelerzahlIT()+");";
            DatabaseConnector.getInstance().executeStatement(query);
            logEntry = new LogEntry("Neue Schule angelegt. Werte: '"+schule.getSNR()+"','"+schule.getName_der_Schule()+"','"+schule.getArt_der_Schule()+"','"+schule.getPLZ()+"','"+schule.getOrt()+"','"+schule.getStrasse_Hsnr()+"','"+schule.getZustaendiges_Schulamt()+"','"+schule.getVorwahl()+"','"+schule.getRufnummer()+"','"+schule.getSF()+"','"+schule.getSchultyp()+"','"+schule.getMailadresse()+"','"+schule.getBemerkungen()+"','"+schule.isFlag()+"','"+schule.getStatus_GB()+"','"+schule.getAnbindung_Kbit_DL()+"','"+schule.getAnbindung_Kbit_UL()+"','"+schule.getStatus_MK()+"','"+schule.getStatus_Inhouse()+"','"+schule.getLat()+"','"+schule.getLng()+"','"+schule.getStandort()+"','"+schule.getAnsprechpartner()+"','"+schule.getTelefon_Ansprechpartner()+"','"+schule.getEmail_Ansprechpartner()+"', "+schule.getSchuelerzahl()+",'"+schule.getAusbau(false)+"', "+schule.getKlassenanzahl()+","+schule.getPWCDownload()+","+schule.getPWCUpload()+", "+schule.getSchuelerzahlIT()+"");
            logEntryRepository.save(logEntry);
        }
        /*
        ContentValues values = new ContentValues();
        values.put(SubjectTable.COLUMN_NAME_NAME, subject.getName());
        values.put(SubjectTable.COLUMN_NAME_TEACHER, subject.getTeacher());
        values.put(SubjectTable.COLUMN_NAME_IS_MAJOR_CLASS, subject.isMajorClass());

        SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();

        if (subject.getId() < 0) {
            // Subject has not been persisted yet => insert
            int id = (int) db.insert(SubjectTable.TABLE_NAME, null, values);
            subject.setId(id);
        } else {
            // Subject has been persisted => update
            db.update(SubjectTable.TABLE_NAME, values, SubjectTable._ID + " = ?"', new String[] {Integer.toString(subject.getId())});
        }
    }

    public void deleteSubject(int subjectId) {
        SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
        db.delete(SubjectTable.TABLE_NAME, SubjectTable._ID + " = ?"', new String[] {Integer.toString(subjectId)});
    }

    private Subject createSubjectFromSql(Cursor cursor) {
        Subject subject = new Subject(cursor.getInt(cursor.getColumnIndexOrThrow(SubjectTable._ID)));
        subject.setName(cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_NAME_NAME)));
        subject.setTeacher(cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_NAME_TEACHER)));
        subject.setMajorClass(cursor.getInt(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_NAME_IS_MAJOR_CLASS)) > 0);
        return subject;
   */ }

    public void flagSchool(String SNR) {
        String query = "UPDATE Schulen SET flag=1 WHERE SNR="+SNR+";";
        DatabaseConnector.getInstance().executeStatement(query);
    }

    public ArrayList<Schule> getSchools(String condition)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen "+condition+";");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public Schule findSchuleWithValues(String schulname, String PLZ, String strasse)
    {


        if(strasse == null || strasse.equals(""))
            return findSchuleWithNameAndPLZ(schulname,PLZ);
        else if (PLZ == null || PLZ.equals("") || PLZ.length() < 5)
            return findSchuleWithNameAndStrasse(schulname,strasse);
        else if (schulname == null || schulname.equals(""))
            return findSchuleWithStrasse(PLZ,strasse);

        if(strasse != null && !strasse.equals(""))
            strasse = strasse.substring(0,strasse.length()-8);
        if(schulname != null && !schulname.equals(""))
            schulname = schulname.substring(0,5);
        String query = "SELECT * FROM Schulen WHERE PLZ="+PLZ+" AND [Straße + Hsnr.] LIKE \""+strasse+"%\" AND [Name der Schule] LIKE \""+schulname+"%\";";
        //System.out.println(query);
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if (checkValidity(result)) return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    public Schule findSchuleWithNameAndStrasse(String schulname, String strasse)
    {
        String query = "SELECT * FROM Schulen WHERE [Straße + Hsnr.] LIKE \""+strasse+"%\" AND [Name der Schule] LIKE \""+schulname+"%\";";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if (checkValidity(result)) return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    public Schule findSchuleWithStrasse(String PLZ, String strasse)
    {
        String query = "SELECT * FROM Schulen WHERE PLZ="+PLZ+" AND [Straße + Hsnr.] LIKE \""+strasse+"%\";";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if (checkValidity(result)) return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    public Schule findSchuleWithNameAndPLZ(String schulname, String PLZ)
    {
        String query = "SELECT * FROM Schulen WHERE PLZ="+PLZ+" AND [Name der Schule] LIKE \""+schulname+"%\";";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if (checkValidity(result)) return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
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

    public Schule getSchoolWithSNR(String SNR) {
        String query = "SELECT * FROM Schulen WHERE SNR="+SNR+";";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if (checkValidity(result)) return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    public void deleteSchoolWithID(String _id)
    {
        DatabaseConnector.getInstance().executeStatement("DELETE FROM Schulen WHERE id="+_id+";");
    }

    public ArrayList<Schule> getStandorteZu(String _snr)
    {
        return this.getSchools("WHERE SNR="+_snr+";");
    }

    public ArrayList<Schule> getTeilstandorteZu(String _snr)
    {
        return this.getSchools("WHERE SNR="+_snr+" AND Standort='T';");
    }

    public Schule getHauptstandortWithID(String id)
    {
        Schule standort = getSchoolWithID(id);
        if (standort.getStandort().equals(HAUPTSTANDORT))
            return standort;
        else
            return getHauptstandorteZu(String.valueOf(standort.getSNR()));
    }

    public Schule getHauptstandorteZu(String _snr)
    {
        String query = "SELECT * FROM Schulen WHERE SNR="+_snr+" AND Standort='H';";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null || result.getData()[0] == null || result.getData()[0].length < 10)
            return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    public Schule getSchoolWithID(String _id)
    {
        String query = "SELECT * FROM Schulen WHERE id="+_id+";";
        //System.out.println(query);
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        if(result == null || result.getData()[0] == null || result.getData()[0].length < 10)
            return null;
        return new Schule(new ArrayList<String>(Arrays.asList(result.getData()[0])),true);
    }

    public ArrayList<Schule> getAllSchools(String filterSNR,String filterOrt,String filterSchulamt, String filterAusbau)
    {
        String query = "SELECT * FROM Schulen";
        if(!filterOrt.equals("-") || !filterSchulamt.equals("-") || !filterSNR.equals("") || !filterAusbau.equals("alle"))
            query+=" WHERE ";
        if(!filterSNR.equals(""))
        {
            query+= "SNR LIKE '%"+filterSNR+"%'";
            if(!filterSchulamt.equals("-") || !filterOrt.equals("-") || !filterAusbau.equals("alle"))
                query+= " AND ";
        }
        if(!filterSchulamt.equals("-"))
        {
            query+= "[Zuständiges Schulamt] LIKE '"+filterSchulamt+"'";
            if(!filterOrt.equals("-") || !filterAusbau.equals("alle"))
                query+= " AND ";
        }
        if(!filterOrt.equals("-"))
        {
            query+= "Ort LIKE '"+filterOrt+"'";
            if(!filterAusbau.equals("alle"))
                query+= " AND ";
        }
        if(!filterAusbau.equals("alle"))
        {
            if(filterAusbau.equals("Bund (alle)"))
                query+= "Ausbau LIKE 'Bund%'";
            else
                query+= "Ausbau LIKE '"+filterAusbau+"'";
        }
        query += ";";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public ArrayList<Schule> getAllFlagedSchools(boolean flaged)
    {
        DatabaseConnector.getInstance().executeStatement("SELECT * FROM Schulen WHERE flag="+(flaged?"1":"0")+";");
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<Schule> schulen = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            schulen.add(new Schule(new ArrayList<String>(Arrays.asList(result.getData()[i])),true));
        }
        return schulen;
    }

    public void resetFlags()
    {
        String query = "UPDATE Schulen SET flag=0 WHERE id>0;";
        DatabaseConnector.getInstance().executeStatement(query);
    }
}
