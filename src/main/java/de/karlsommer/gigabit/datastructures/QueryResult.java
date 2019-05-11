package de.karlsommer.gigabit.datastructures;

import java.util.ArrayList;

/**
 * <p>
 * Klasse QueryResult
 * </p>
 * <p>
 * Ein Objekt der Klasse QueryResult stellt die Ergebnistabelle einer Datenbankanfrage mit Hilfe 
 * der Klasse DatabaseConnector dar. Objekte dieser Klasse werden nur von der Klasse DatabaseConnector erstellt. 
 * Die Klasse verfuegt ueber keinen oeffentlichen Konstruktor.
 * </p>
 * 
 * @author Karl Sommer
 * @version 2019-01-31
 */
public class QueryResult{
  private ArrayList<ArrayList<String>> data;
  private ArrayList<String> columnNames;
  private ArrayList<String> columnTypes;

  /**
   * Paketinterner Konstruktor.
   */
  public QueryResult(ArrayList<ArrayList<String>> pData, ArrayList<String> pColumnNames, ArrayList<String> pColumnTypes){
    data = pData;
    columnNames = pColumnNames;   
    columnTypes = pColumnTypes;
  }

  /**
   * Die Anfrage liefert die Eintraege der Ergebnistabelle als zweidimensionale ArrayList
   * vom Typ String. Der erste Index des Arrays stellt die Zeile und der zweite die
   * Spalte dar.
   */
  public ArrayList<ArrayList<String>> getData(){
    return data;
  }

  /**
   * Die Anfrage liefert die Bezeichner der Spalten der Ergebnistabelle als Feld vom 
   * Typ String zurueck.
   */
  public ArrayList<String> getColumnNames(){
    return columnNames;
  }

  /**
   * Die Anfrage liefert die Typenbezeichnung der Spalten der Ergebnistabelle als Feld 
   * vom Typ String zurueck. Die Bezeichnungen entsprechen den Angaben in der MySQL-Datenbank.
   */
  public ArrayList<String> getColumnTypes(){
    return columnTypes;
  }

  /**
   * Die Anfrage liefert die Anzahl der Zeilen der Ergebnistabelle als Integer.
   */
  public int getRowCount(){
    if (data != null )
      return data.size();
    else 
      return 0;
  }

  /**
   * Die Anfrage liefert die Anzahl der Spalten der Ergebnistabelle als Integer.
   */
  public int getColumnCount(){
    if (data != null && data.size() > 0 && data.get(0) != null)
      return data.get(0).size();
    else
      return 0;
  }

}