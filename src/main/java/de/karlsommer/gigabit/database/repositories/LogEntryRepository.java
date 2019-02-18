package de.karlsommer.gigabit.database.repositories;

import de.karlsommer.gigabit.database.DatabaseConnector;
import de.karlsommer.gigabit.database.model.LogEntry;
import de.karlsommer.gigabit.datastructures.QueryResult;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class LogEntryRepository {

    /**
     * Logeinträge aus der Datenbank laden
     * @return alle Logeinträge in der Datenbank
     */
    public ArrayList<LogEntry> getAll()
    {
        String query = "SELECT * FROM LogEntry ORDER BY id DESC;";
        DatabaseConnector.getInstance().executeStatement(query);
        QueryResult result = DatabaseConnector.getInstance().getCurrentQueryResult();
        ArrayList<LogEntry> logEntries = new ArrayList<>();
        for(int i = 0; i < result.getRowCount(); i++)
        {
            logEntries.add(new LogEntry(new ArrayList<String>(Arrays.asList(result.getData()[i]))));
        }
        return logEntries;
    }

    /**
     * Logeintragsobjekt in der Datenbank speichern
     * @param logEntry zu speichernder Logeintrag
     */
    public void save(LogEntry logEntry) {
        String query;
        if(logEntry.getId()<0)
            query = "INSERT INTO LogEntry VALUES(null,'"+getCurrentTimestamp()+"','"+logEntry.getMessage()+"');";
        else
            query = "UPDATE LogEntry SET message='"+logEntry.getMessage()+"', timestamp='"+logEntry.getTimeStamp()+"' WHERE id="+logEntry.getId()+" ";
        //System.out.println(query);
        DatabaseConnector.getInstance().executeStatement(query);
    }

    /**
     * Aktuelle Zeit
     * @return Zeitstring
     */
    private String getCurrentTimestamp()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        return ts;
    }

    /**
     * Log in die Datenbank schreiben
     * @param log zu loggende Nachricht
     */
    public void log(String log) {
        String query = "INSERT INTO LogEntry VALUES(null,'"+getCurrentTimestamp()+"','"+log+"');";
        //System.out.println(query);
        DatabaseConnector.getInstance().executeStatement(query);
    }
}
