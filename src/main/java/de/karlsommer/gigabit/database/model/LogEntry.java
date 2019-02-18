package de.karlsommer.gigabit.database.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

public class LogEntry {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private int id = -1;
    private String message;//Änderungstext
    private String timeStamp;//Zeitpunkt der Änderung

    /**
     * Vektor für die Darstellung in der Liste erhalten
     * @return Vector mit den Logeinträgen
     */
    public Vector<String> getVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(id));
        v.add(timeStamp);
        v.add(message);
        return v;
    }

    /**
     * Konstruktor mit zu loggender Nachricht
     * @param message Nachrichtentext
     */
    public LogEntry(String message)
    {
        int id = -1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        this.message = message;
        this.timeStamp = ts;
    }

    /**
     * Kunstruktor für die Übergabe aus der Datenbank
     * @param data ArrayList aus der Datenbank
     */
    public LogEntry(ArrayList<String> data)
    {

        int i = 0;
        this.id = Integer.parseInt(data.get(i++));
        this.timeStamp = data.get(i++);
        this.message = data.get(i++);
    }
}
