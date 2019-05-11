package de.karlsommer.gigabit.database;

import com.healthmarketscience.jackcess.*;
import de.karlsommer.gigabit.datastructures.QueryResult;
import de.karlsommer.gigabit.helper.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MDBConnector{
    private static MDBConnector _instance = null;
    private String bezirk;
    private String filename;
    public static final ArrayList<String> columnsFromDatabase = new ArrayList<String>() {{
        add("Schulnr");
        add("Regschl");
        add("ABez1");
        add("ABez2");
        add("ABez3");
        add("PLZ");
        add("Ort");
        add("Strasse");
        add("TelVorw");
        add("Telefon");
        add("SF");
        add("KurzBez");
    }};

    protected MDBConnector()
    {
        super();
        bezirk = Settings.getInstance().getBezirksSuchString();
    }

    /**
     * Gibt die Singelton-Instanz
     * @return aktuelle Instanz
     */
    public static MDBConnector getInstance()
    {
        if(_instance == null)
            _instance = new MDBConnector();
        return _instance;
    }

    public QueryResult executeStatement(String filename) {
        ArrayList<ArrayList<String>> returnList = new ArrayList<>();
        try (Database db = DatabaseBuilder.open(new File(filename))) {
            Table schoolTable = db.getTable("DBS");

            Cursor cursor = CursorBuilder.createCursor(schoolTable);
            for (Row row : cursor.newIterable().addMatchPattern("RP", bezirk)) {
                ArrayList<String> toAdd = new ArrayList<>();
                for (String columnName:columnsFromDatabase
                ) {
                    toAdd.add(row.getString(columnName));
                }
                returnList.add(toAdd);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        return new QueryResult(returnList, columnsFromDatabase, null);
    }

}
