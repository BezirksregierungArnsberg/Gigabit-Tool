package de.karlsommer.gigabit.database;

import de.karlsommer.gigabit.datastructures.QueryResult;
import de.karlsommer.gigabit.helper.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 * Vorschlag fuer die Materialien zu den zentralen NRW-Abiturpruefungen im Fach Informatik ab 2018.
 * </p>
 * <p>
 * Klasse DatabaseConnector
 * </p>
 * <p>
 * Ein Objekt der Klasse DatabaseConnector ermoeglicht die Abfrage und Manipulation 
 * einer SQLite-Datenbank. 
 * Beim Erzeugen des Objekts wird eine Datenbankverbindung aufgebaut, so dass 
 * anschließend SQL-Anweisungen an diese Datenbank gerichtet werden koennen.
 * </p>
 * 
 * @author Volker Quade
 * @version 2016-01-24
 */
public class DatabaseConnector{
  private Connection connection;
  private QueryResult currentQueryResult = null;
  private String message = null;
  private static DatabaseConnector _this = null;

  /**
   * Ein Objekt vom Typ DatabaseConnector wird erstellt, und eine Verbindung zur Datenbank 
   * wird aufgebaut. Mit den Parametern pIP und pPort werden die IP-Adresse und die 
   * Port-Nummer uebergeben, unter denen die Datenbank mit Namen pDatabase zu erreichen ist. 
   * Mit den Parametern pUsername und pPassword werden Benutzername und Passwort fuer die 
   * Datenbank uebergeben.
   */
  private DatabaseConnector(String pIP, String pPort, String pDatabase, String pUsername, String pPassword){
    //Eine Impementierung dieser Schnittstelle fuer SQLite ignoriert pID und pPort, da die Datenbank immer lokal ist. 
    //Auch pUsername und pPassword werden nicht verwendet, da SQLite sie nicht unterstuetzt.
    try {
      //Laden der Treiberklasse
      Class.forName("org.sqlite.JDBC");

      //Verbindung herstellen
      connection = DriverManager.getConnection("jdbc:sqlite:"+pDatabase);

    } catch (Exception e) {
      message = e.getMessage();
      System.exit(0);
    }
  }
  /**
   * Erweiterung des Datenbankconnectors zum Singleton
   * @return Singleton-Instanz
   */
  public static DatabaseConnector getInstance()
  {
      if(_this == null) {
        Date date = new Date();
        Format formatter = new SimpleDateFormat("YYYY-MM-dd");
        File src = new File(Settings.getInstance().getDatabaseFolderPath()+"gigabit.db");
        File target = new File(Settings.getInstance().getDatabaseFolderPath()+"gigabit-"+formatter.format(date)+".db");

        try {
          if(!target.exists() && !target.isDirectory()) {
            Files.copy(src.toPath(), target.toPath());
          }
        } catch (IOException e) {
          e.printStackTrace();
        }

        _this = new DatabaseConnector("", "", Settings.getInstance().getDatabaseFolderPath()+"gigabit.db", "", "");
      }
      return _this;
  }

  /**
   * Der Auftrag schickt den im Parameter pSQLStatement enthaltenen SQL-Befehl an die 
   * Datenbank ab. 
   * Handelt es sich bei pSQLStatement um einen SQL-Befehl, der eine Ergebnismenge 
   * liefert, so kann dieses Ergebnis anschließend mit der Methode getCurrentQueryResult 
   * abgerufen werden.
   */
  public void executeStatement(String pSQLStatement){  
    //Altes Ergebnis loeschen
    currentQueryResult = null;
    message = null;

    try {
      //Neues Statement erstellen
      Statement statement = connection.createStatement();

      //SQL Anweisung an die DB schicken.
      if (statement.execute(pSQLStatement)) { //Fall 1: Es gibt ein Ergebnis

        //Resultset auslesen
        ResultSet resultset = statement.getResultSet();

        //Spaltenanzahl ermitteln
        int columnCount = resultset.getMetaData().getColumnCount();
        
        //Spaltennamen und Spaltentypen in Felder uebertragen
        ArrayList<String> resultColumnNames = new ArrayList<>();
        ArrayList<String> resultColumnTypes = new ArrayList<>();
        for (int i = 0; i < columnCount; i++){
          resultColumnNames.add(resultset.getMetaData().getColumnLabel(i+1));
          resultColumnTypes.add(resultset.getMetaData().getColumnTypeName(i+1));
        }
        ArrayList<ArrayList<String>> resultData = new ArrayList<>();

        while (resultset.next()){
          resultData.add(new ArrayList<>());
          for (int s = 0; s < columnCount; s++){
            resultData.get(resultData.size()-1).add(resultset.getString(s+1));
          }
        }
        //Statement schließen und Ergebnisobjekt erstellen
        statement.close();
        currentQueryResult =  new QueryResult(resultData, resultColumnNames, resultColumnTypes); 

      } else { //Fall 2: Es gibt kein Ergebnis.
        //Statement ohne Ergebnisobjekt schliessen
        statement.close();       
      }

    } catch (Exception e) {
      //Fehlermeldung speichern
      System.out.println(e.getMessage());
      message = e.getMessage();
    }
  }

  /**
   * Die Anfrage liefert das Ergebnis des letzten mit der Methode executeStatement an
   * die Datenbank geschickten SQL-Befehls als Ob-jekt vom Typ QueryResult zurueck.
   * Wurde bisher kein SQL-Befehl abgeschickt oder ergab der letzte Aufruf von
   * executeStatement keine Ergebnismenge (z.B. bei einem INSERT-Befehl oder einem
   * Syntaxfehler), so wird null geliefert.
   */
  public QueryResult getCurrentQueryResult(){
    return currentQueryResult;
  }

  /**
   * Die Anfrage liefert null oder eine Fehlermeldung, die sich jeweils auf die letzte zuvor ausgefuehrte 
   * Datenbankoperation bezieht.
   */
  public String getErrorMessage(){
    return message;
  }

  /**
   * Die Datenbankverbindung wird geschlossen.
   */
  public void close(){
    try{
      connection.close();
    } catch (Exception e) {
      message = e.getMessage();
    }
  }

}
