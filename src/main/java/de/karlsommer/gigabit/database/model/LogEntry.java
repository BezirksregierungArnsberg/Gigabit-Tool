package de.karlsommer.gigabit.database.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

public class LogEntry {

    public static final int ID = 0;
    public static final int TIMESTAMP = 1;
    public static final int MESSAGE = 2;

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
    private String message;
    private String timeStamp;


    public Vector<String> getVector()
    {
        Vector<String> v =  new Vector<>();
        v.add(String.valueOf(id));
        v.add(timeStamp);
        v.add(message);
        return v;
    }

    public LogEntry(String message)
    {
        int id = -1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        this.message = message;
        this.timeStamp = ts;
    }

    public LogEntry(ArrayList<String> data)
    {

        int i = 0;
        this.id = Integer.parseInt(data.get(i++));
        this.timeStamp = data.get(i++);
        this.message = data.get(i++);
    }
}
