package de.karlsommer.gigabit.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    public static final String SETTINGS_FILE = "settings.xml";
    private static Settings _this = null;

    private String PROPERTY_STRING_DATABASE_FOLDER = "database_folder";
    private String PROPERTY_STRING_OUTPUT_FOLDER = "output_folder";
    private String PROPERTY_STRING_BEZIRKS_STRING = "bezirks_string";

    public String getDatabaseFolderPath() {
        return databaseFolderPath;
    }

    public void setDatabaseFolderPath(String databaseFolderPath) {
        this.databaseFolderPath = databaseFolderPath;
    }

    public String getOutputFolderPath() {
        return outputFolderPath;
    }

    public void setOutputFolderPath(String outputFolderPath) {
        this.outputFolderPath = outputFolderPath;
    }

    public String getBezirksSuchString() {
        return bezirksSuchString;
    }

    public void setBezirksSuchString(String bezirksSuchString) {
        this.bezirksSuchString = bezirksSuchString;
    }

    private String databaseFolderPath;
    private String outputFolderPath;
    private String bezirksSuchString;

    public static Settings getInstance()
    {
        if(_this == null)
            _this = new Settings();
        return _this;
    }
    private Settings()
    {
        Properties loadProps = new Properties();
        try {
            loadProps.loadFromXML(new FileInputStream(SETTINGS_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            Properties saveProps = new Properties();
            saveProps.setProperty(PROPERTY_STRING_DATABASE_FOLDER, "./databases/");
            saveProps.setProperty(PROPERTY_STRING_OUTPUT_FOLDER, "./output/");
            saveProps.setProperty(PROPERTY_STRING_BEZIRKS_STRING, "9");
            try {
                saveProps.storeToXML(new FileOutputStream(SETTINGS_FILE), "");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        databaseFolderPath = loadProps.getProperty(PROPERTY_STRING_DATABASE_FOLDER);
        outputFolderPath = loadProps.getProperty(PROPERTY_STRING_OUTPUT_FOLDER);
        bezirksSuchString = loadProps.getProperty(PROPERTY_STRING_BEZIRKS_STRING);

    }
}
