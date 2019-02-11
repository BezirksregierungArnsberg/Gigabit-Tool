package de.karlsommer.gigabit.filehandling;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.datastructures.Tabelle;

import java.util.ArrayList;
import java.util.Vector;


public class ImportBuilder {
    private Tabelle daten;

    public ImportBuilder(){
    }

    public boolean ladeCSVWebsiteDaten(String filename)
    {
        daten = new Tabelle(filename,',', true, false);

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

    public ArrayList<ArrayList<String>> gibArrayListsAusTabellen(){

        return daten.getDaten();

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

}