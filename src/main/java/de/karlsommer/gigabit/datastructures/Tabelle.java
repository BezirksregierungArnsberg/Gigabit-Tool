package de.karlsommer.gigabit.datastructures;

import de.karlsommer.gigabit.database.model.Schule;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Tabelle{
    private ArrayList<ArrayList<String>> daten;
    private ArrayList<String> spaltenbezeichner;

    public Tabelle(String pDateiname, char pTrenner, boolean pSpaltenbezeichner, boolean firstLineContainsData){
        //Zeilen aus Datei lesen und an Trennzeichen in Stringfelder zerlegen.
        System.out.println(pDateiname);
        daten = new ArrayList<>();
        spaltenbezeichner = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pDateiname), "UTF8"));

            String zeile = null;
            int firstLine;
            if(firstLineContainsData)
                firstLine = 0;
            else
                firstLine = 1;
            zeile = reader.readLine();
            do {
                zeile = zeile.replace("'","");
                if(firstLine == 0)
                {
                    spaltenbezeichner = new ArrayList<>(Arrays.asList(zeile.replaceAll("\"","").split(Pattern.quote(""+pTrenner))));
                    firstLine=1;
                }
                if (zeile != null) {
                    daten.add(new ArrayList<>(Arrays.asList(zeile.replaceAll("\"","").split(Pattern.quote(""+pTrenner)))));
                }
                zeile = reader.readLine();
            } while (zeile != null);     

            reader.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public ArrayList<Schule> getSchulen()
    {
        
            ArrayList<Schule> schulen = new ArrayList<>();
            for(ArrayList<String> date:daten)
            {
                if(date != null && !date.isEmpty() && date.get(0) != null)
                {
                if(!date.get(0).equals("Interne Nummer"))
                {
                Schule schule = new Schule(date, false);
                
                schulen.add(schule);
                }
                }
            }
            return schulen;
    }

    public ArrayList<ArrayList<String>> getDaten()
    {
        return daten;
    }

    public ArrayList<String> gibSpaltenbezeichner(){
        return spaltenbezeichner;
    }

    public int gibZeilenanzahl(){
        return daten.size();
    }


}