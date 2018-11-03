package de.karlsommer.gigabit.filehandling;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.datastructures.Tabelle;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Datenbank:
 * CREATE TABLE Schulen (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    [Interne Nummer] INTEGER,
    SNR              INTEGER,
    [Name der Schule] TEXT,
    [Art der Schule] TEXT,
    PLZ BIGINT,
    Ort TEXT,
    [Straße + Hsnr.] TEXT,
    [Zuständiges Schulamt] VARCHAR(4),
    Vorwahl BIGINT,
    Rufnummer BIGINT,
    SF VARCHAR (3),
    Schultyp VARCHAR(15),
    Mailadresse TEXT,
    FB Text,
    Zuständig TEXT,
    [Bedarf S1] VARCHAR(12),
    [Status S1] VARCHAR(12),
    [Moderator S1] TEXT,
    [Datum S1] INTEGER,
    [Bedarf S2] VARCHAR(12),
    [Status S2] VARCHAR(12),
    [Moderator S2] TEXT,
    [Datum S2] INTEGER,
    [Bedarf S3] VARCHAR(12),
    [Status S3] VARCHAR(12),
    [Moderator S3] TEXT,
    [Datum S3] INTEGER,
    [Bedarf S4] VARCHAR(12),
    [Status S4] VARCHAR(12),
    [Moderator S4] TEXT,
    [Datum S4] INTEGER,
    [Bedarf R1] VARCHAR(12),
    [Status R1] VARCHAR(12),
    [Moderator R1] TEXT,
    [Datum R1] INTEGER,
    [Bedarf R2] VARCHAR(12),
    [Status R2] VARCHAR(12),
    [Moderator R2] TEXT,
    [Datum R2] INTEGER,
    [Bedarf L1] VARCHAR(12),
    [Status L1] VARCHAR(12),
    [Moderator L1] TEXT,
    [Datum L1] INTEGER,
    [Bedarf K1] VARCHAR(12),
    [Status K1] VARCHAR(12),
    [Moderator K1] TEXT,
    [Datum K1] INTEGER,
    [Bedarf K2] VARCHAR(12),
    [Status K2] VARCHAR(12),
    [Moderator K2] TEXT,
    [Datum K2] INTEGER,
    [Bedarf A1] VARCHAR(12),
    [Status A1] VARCHAR(12),
    [Moderator A1] TEXT,
    [Datum A1] INTEGER,
    [Bedarf X2] VARCHAR(12),
    [Status X2] VARCHAR(12),
    [Moderator X2] TEXT,
    [Datum X2] INTEGER,
    [Bedarf X3] VARCHAR(12),
    [Status X3] VARCHAR(12),
    [Moderator X3] TEXT,
    [Datum X3] INTEGER,
    Bemerkungen TEXT,
    flag TINYINT(1),
    [Status GB] VARCHAR(12),
    [Anbindung MBit DL] INTEGER,
    [Anbindung MBit UL] INTEGER,
    [Status MK] VARCHAR(12),
    [Status Inhouse] VARCHAR(12),
    lat REAL,
    lng REAL
);
*/

public class ImportBuilder {
    private Tabelle daten;

    public ImportBuilder(){
    }

    public boolean ladeCSVWebsiteDaten(String filename)
    {
        daten = new Tabelle(filename,',', true, false);

        return (daten.gibZeilenanzahl() > 0);
    }

    public boolean ladeCSVSchuelerzahlen(String filename)
    {
        daten = new Tabelle(filename,';', true, false);

        return (daten.gibZeilenanzahl() > 0);
    }

    public boolean ladeBreitbandDaten(String filename)
    {
        daten = new Tabelle(filename,';', true, true);
        if (daten.gibZeilenanzahl() > 0){
            daten.gibSpaltenbezeichner().set(0, "Angaben zum Schulträger"); //Hack, da am Anfang immer ein Sonderzeichen steht.
        }

        return (daten.gibZeilenanzahl() > 0);
    }

    public boolean ladeMemasysDaten(String filename){
        daten = new Tabelle(filename,';', true, true);
        if (daten.gibZeilenanzahl() > 0){
            daten.gibSpaltenbezeichner().set(0, "Interne Nummer"); //Hack, da am Anfang immer ein Sonderzeichen steht.
        }

        return (daten.gibZeilenanzahl() > 0);        
    }
    public ArrayList<ArrayList<String>> gibArrayListsAusTabellen(){

        return daten.getStrings();

    }

    public static Vector<String> getStringVectorFromArrayListData(ArrayList<String> data)
    {
        Vector<String> v =  new Vector<>();
        for(String value:data)
        {
            v.add(value);
        }
        return v;
    }

    public ArrayList<Schule> holeSchuldatenAusMemasysDatei(){

        return daten.getSchulen();        

    }

}