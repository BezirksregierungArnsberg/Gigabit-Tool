package de.karlsommer.gigabit.datastructures;

import de.karlsommer.gigabit.database.model.Schule;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Tabelle{
    private ArrayList<ArrayList<String>> daten;
    private ArrayList<String> spaltenbezeichner;

    public Tabelle(String pDateiname, char pTrenner, boolean pSpaltenbezeichner){
        //Zeilen aus Datei lesen und an Trennzeichen in Stringfelder zerlegen.
        System.out.println(pDateiname);
        daten = new ArrayList<>();
        spaltenbezeichner = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pDateiname), "UTF8"));

            String zeile = null;
            int firstLine = 0;
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

    public Tabelle (ArrayList<String> pSpaltenbezeichner, ArrayList<ArrayList<String>> pData) {
        spaltenbezeichner = pSpaltenbezeichner;
        daten = pData;
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
                
                //tableModel.addRow(schule.getVector());
                }
                }
            }
            return schulen;
    }

    public ArrayList<ArrayList<String>>getStrings()
    {
        return daten;
    }

    public Tabelle erstelleProjektion(String... pSpaltenbezeichner){
        String[][] neueDaten = new String[daten.size()][pSpaltenbezeichner.length];

        for (int aktS = 0; aktS < pSpaltenbezeichner.length; aktS++){
            for (int s = 0; s < spaltenbezeichner.size(); s++){
                if (pSpaltenbezeichner[aktS].equals(spaltenbezeichner.get(s))) {
                    for (int z = 0; z < daten.size(); z++){
                        neueDaten[z][aktS] = daten.get(z).get(s);
                    }
                }
            }   
        }
        ArrayList<ArrayList<String>> neueArrayList = new ArrayList<>();
        for(int i = 0; i < neueDaten.length; i++)
            neueArrayList.add(new ArrayList<>(Arrays.asList(neueDaten[i])));
            
        return new Tabelle(new ArrayList<>(Arrays.asList(pSpaltenbezeichner)), neueArrayList);
    }

    public ArrayList<String> gibSpaltenbezeichner(){
        return spaltenbezeichner;
    }

    public ArrayList<ArrayList<String>> gibDaten() {
        return daten;
    }

    public int gibZeilenanzahl(){
        return daten.size();
    }

    public int gibSpaltenanzahl(){
        return spaltenbezeichner.size();
    }

    public void speichere (String pDateiname, char pTrenner, boolean pDoppelte){
        //Ausgabezeilen erstellen
        String[] zeilen = new String[gibZeilenanzahl()];

        for (int j = 0; j < gibZeilenanzahl(); j++) {
            String zeile = "";
            for (int i = 0; i < gibSpaltenanzahl(); i++) {
                zeile = zeile.concat(daten.get(j).get(i));
                if (i != gibSpaltenanzahl()-1) {
                    zeile = zeile + pTrenner;
                }
            }
            zeilen[j] = zeile;
        }

        //Doppelte Zeilen entfernen wenn verlangt
        if (pDoppelte!=true){
            for (int j = 0; j < gibZeilenanzahl()-1; j++) {
                for (int i = j+1; i < gibZeilenanzahl(); i++) {
                    if (zeilen[i].equals(zeilen[j])){
                        zeilen[j] = null;
                    }
                }
            }
        }

        //Ausgabe in Datei schreiben
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pDateiname), "ISO-8859-1"));

            //Spaltenbezeichner
            for (int i = 0; i < gibSpaltenanzahl(); i++) {
                writer.write(spaltenbezeichner.get(i));
                if (i != gibSpaltenanzahl()-1) {
                    writer.write(pTrenner);
                }
            }
            writer.newLine();

            //Daten
            for (int j = 0; j < gibZeilenanzahl(); j++) {
                if (zeilen[j] != null){
                    writer.write(zeilen[j]);
                    writer.newLine();
                }
            }

            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}