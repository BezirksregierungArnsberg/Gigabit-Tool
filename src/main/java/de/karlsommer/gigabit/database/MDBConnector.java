package de.karlsommer.gigabit.database;

import com.healthmarketscience.jackcess.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MDBConnector {
    private String filename = "schulver.mdb";
    private static MDBConnector _instance = null;
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

    private MDBConnector()
    {
    }

    public static MDBConnector getInstance()
    {
        if(_instance == null)
            _instance = new MDBConnector();
        return _instance;
    }

    public ArrayList<ArrayList<String>> getAllSchools()
    {
        ArrayList<ArrayList<String>> returnList = new ArrayList<>();
        try (Database db = DatabaseBuilder.open(new File(filename))) {
            System.out.println("The database file has been opened.");
            Table schoolTable = db.getTable("DBS");

            /*
            for(Column column:schoolTable.getColumns())
            {
                System.out.print(column.getName()+"-");
            }*/

            Cursor cursor = CursorBuilder.createCursor(schoolTable);
            for (Row row : cursor.newIterable().addMatchPattern("RP", "9")) {
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
        return returnList;
    }

}
