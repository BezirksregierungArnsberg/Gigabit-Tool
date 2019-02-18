package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.MDBConnector;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import de.karlsommer.gigabit.filehandling.DocumentWriter;
import de.karlsommer.gigabit.filehandling.ImportBuilder;
import de.karlsommer.gigabit.filehandling.JavascriptWriter;
import de.karlsommer.gigabit.geocoding.GoogleGeoUtils;
import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.helper.DataUpdater;
import de.karlsommer.gigabit.helper.Settings;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import static de.karlsommer.gigabit.database.model.Schule.*;
import static org.apache.commons.lang.StringUtils.trim;

public class Interface implements DataUpdater {

    public static final boolean RELEASE = false; // Sollen die Admin-Optionen mit eingeblendet werden, auf true stellen, wenn Version für Anwender compiliert wird.
    public static final String version = "1.72"; //Versionsnummer
    public static final String releaseDate = "18.02.2019"; //Versionsdatum
    public static final String IMPORT_STRING_NOTHING = "-";
    public static final String IMPORT_STRING_SCHULE_MIT_FEHLENDEN_GEOCOORDINATEN = "Schule mit fehlenden Geocoordinaten zeigen";
    public static final String IMPORT_STRING_GEOCOORDINATEN_IN_DB_LADEN = "Geocoordinaten in DB laden";
    public static final String IMPORT_STRING_DOPPELTE_GEOCOORDINATEN_VERSCHIEBEN = "Doppelte Geocoordinaten verschieben";
    public static final String IMPORT_STRING_DATENBANK_VON_IT_NRW_EINLESEN = "Datenbank von IT-NRW einlesen";
    public static final String IMPORT_STRING_TABELLE_VON_IT_NRW_EINLESEN = "Tabelle von IT-NRW einlesen";
    public static final String IMPORT_STRING_DATEN_AUS_CSV_ABGLEICHEN = "Daten aus CSV abgleichen";
    public static final String IMPORT_STRING_SONDERIMPORT = "Sonderimport";
    public static final String IMPORT_STRING_GIGABITTABELLE_ABGLEICHEN = "Gigabittabelle abgleichen";
    public static final String EXPORT_STRING_GIGABIT_KARTE_SCHREIBEN = "Gigabit-Karte schreiben";
    public static final String EXPORT_STRING_BERICHT_SCHREIBEN =  "Bericht schreiben";
    public static final String EXPORT_STRING_SPEZIALKARTE_SCHREIBEN = "Spezialkarte schreiben";

    private JPanel mainView;
    private JLabel ausgabeLabel;
    private JTable ausgabeTabelle;
    private JPanel topPanel;
    private JLabel versionName;
    private JTextField textFieldSchulnummern;
    private JButton suchenButtonSchullnummern;
    private JButton alleSchulenAnzeigenButton;
    private JComboBox comboBoxSchulaemter;
    private JComboBox comboBoxOrte;
    private JComboBox comboBoxAusbaustatus;
    private JButton exportButton;
    private JButton importButton;
    private JButton changeHistoryButton;
    private JComboBox comboBoxAdminImport;
    private JButton adminImportButton;
    private JComboBox comboBoxAdminExport;
    private JButton adminExportButton;
    private JButton sendMailButton;
    private String filename = "";
    private GoogleGeoUtils geoUtils;
    private ImportBuilder builder;
    private SchuleRepository schuleRepository;
    private ArrayList<Schule> schulenImZwischenspeicher;
    private JFrame schuleBearbeitenFrame;
    private EditSchoolFrame editSchoolFrame;
    private JFrame loggerFrame;
    private LogViewer logViewer;

    private Interface()
    {
        schuleRepository = new SchuleRepository();
        builder = new ImportBuilder();


        loggerFrame = new JFrame("Schulen bearbeiten");
        logViewer = new LogViewer(loggerFrame);
        loggerFrame.setContentPane(logViewer.mainView);
        loggerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        loggerFrame.addWindowListener(new WindowAdapter() {
            //I skipped unused callbacks for readability

            @Override
            public void windowClosing(WindowEvent e) {
                loggerFrame.setVisible(false);
                loggerFrame.dispose();
            }
        });

        loggerFrame.pack();


        schuleBearbeitenFrame = new JFrame("Schulen bearbeiten");
        editSchoolFrame = new EditSchoolFrame(schuleBearbeitenFrame, this);
        schuleBearbeitenFrame.setContentPane(editSchoolFrame.mainView);
        schuleBearbeitenFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        schuleBearbeitenFrame.addWindowListener(new WindowAdapter() {
            //I skipped unused callbacks for readability

            @Override
            public void windowClosing(WindowEvent e) {
                schuleBearbeitenFrame.setVisible(false);
                schuleBearbeitenFrame.dispose();
            }
        });

        schuleBearbeitenFrame.pack();

        if(RELEASE)
            topPanel.setVisible(false);

        versionName.setText("Version: "+version);

        updateData();

        //Alle Schulen in Tool anzeigen
        alleSchulenAnzeigenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSNR = "";
                filterOrt = "-";
                filterSchulamt = "-";
                updateData();
            }
        });
        //Suchbutton nach Schulnummern geklickt
        suchenButtonSchullnummern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(StringUtils.isNumeric(textFieldSchulnummern.getText()) || textFieldSchulnummern.getText().equals(""))
                    filterSNR = textFieldSchulnummern.getText();
                filterSchulamt = ((String)comboBoxSchulaemter.getSelectedItem());
                filterOrt = ((String)comboBoxOrte.getSelectedItem());
                filterAusbau = ((String)comboBoxAusbaustatus.getSelectedItem());
                updateData();
            }
        });

        //Exportieren der gefilterten Schulen in eine Excel-Datei
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Schule> schulen = schuleRepository.getAllSchools(filterSNR,filterOrt,filterSchulamt,filterAusbau);
                DocumentWriter documentWriter = new DocumentWriter();
                documentWriter.writeSchulenInExcel(schulen);

            }
        });
        //Importieren der exportiereten Excel-Datei
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                int rVal = c.showOpenDialog(mainView);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();

                    FileInputStream file = null;
                    try {
                        file = new FileInputStream(new File(filename));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    XSSFWorkbook workbook = null;
                    try {
                        workbook = new XSSFWorkbook(file);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    for(int i = 1; i <= sheet.getLastRowNum() && sheet.getRow(i).getCell(0) != null;i++){

                        XSSFRow row = sheet.getRow(i);
                        if(schuleRepository.schoolWithIDExists(Integer.parseInt((row.getCell(0) + " ").split("\\.")[0]))) {
                            Schule toUpdate = schuleRepository.getSchoolWithID((row.getCell(0) + " ").split("\\.")[0]);
                            toUpdate.updateData(row);
                            schuleRepository.save(toUpdate);
                        }


                        updateData();
                        ausgabeLabel.setText("Importiert!");
                        ausgabeLabel.setVisible(true);
                    }

                } else if (rVal == JFileChooser.CANCEL_OPTION) {
                    ausgabeLabel.setText("Bitte vor Abgleich auswählen");
                    ausgabeLabel.setVisible(true);
                }
            }
        });
        //Button mit Aufruf der History
        changeHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggerFrame.setVisible(true);
            }
        });
        //Import-Button für den Adminview, ruft entsprechende Funktionen auf
        adminImportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String)comboBoxAdminImport.getSelectedItem()){
                    case IMPORT_STRING_NOTHING:
                        break;
                    case IMPORT_STRING_SCHULE_MIT_FEHLENDEN_GEOCOORDINATEN:
                        showSchuleMitFehlendenGeocoordinaten();
                        break;
                    case IMPORT_STRING_GEOCOORDINATEN_IN_DB_LADEN:
                        geocoordinatenInDBLaden();
                        break;
                    case IMPORT_STRING_DOPPELTE_GEOCOORDINATEN_VERSCHIEBEN:
                        doppelteGeocoordinatenVerschieben();
                        break;
                    case IMPORT_STRING_DATENBANK_VON_IT_NRW_EINLESEN:
                        tabelleVonITNRWDatenbankEinlesen();
                        break;
                    case IMPORT_STRING_TABELLE_VON_IT_NRW_EINLESEN:
                        datenbankVonITNRWEinlesen();
                        break;
                    case IMPORT_STRING_DATEN_AUS_CSV_ABGLEICHEN:
                        datenAusCSVAbgleichen();
                        break;
                    case IMPORT_STRING_SONDERIMPORT:
                        specialImport();
                        break;
                    case IMPORT_STRING_GIGABITTABELLE_ABGLEICHEN:
                        gigabitTabelleAbgleichen();
                        break;
                    default:break;
                }
            }
        });
        //Exportbutton für den Adminview, ruft entsprechende Funktionen auf
        adminExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String)comboBoxAdminExport.getSelectedItem()){
                    case EXPORT_STRING_GIGABIT_KARTE_SCHREIBEN:
                        gigabitkarteSchreiben();
                        break;
                    case EXPORT_STRING_BERICHT_SCHREIBEN:
                        berichtSchreiben();
                        break;
                    case EXPORT_STRING_SPEZIALKARTE_SCHREIBEN:
                        spezialKartenSchreiben();
                        break;
                    default:break;
                }
            }
        });
        //Massenmailversand aus dem Tool
        sendMailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String to = "karl-georg.sommer@bra.nrw.de, giovanni.lore@bra.nrw.de, sarah.hengesbach@bra.nrw.de, 900153@schule.nrw.de";

                    // Sender's email ID needs to be mentioned
                    String from = "gigabit@bra.nrw.de";

                    // Assuming you are sending email from localhost
                    String host = "10.64.112.141";

                    // Get system properties
                    Properties properties = System.getProperties();

                    // Setup mail server
                    properties.setProperty("mail.smtp.host", host);
                    properties.setProperty("mail.smtp.port", "25");
                    properties.setProperty("mail.imap.auth.plain.disable","true");
                    properties.setProperty("mail.debug", "true");
                    Session session = Session.getDefaultInstance(properties);
                String[] recipientList = to.split(",");
                InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
                for (String recipient : recipientList) {
                    try {
                        // Create a default MimeMessage object.
                        MimeMessage message = new MimeMessage(session);
                        // Set From: header field of the header.
                        message.setFrom(new InternetAddress(from));
                        // Set To: header field of the header.
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                        // Set Subject: header field
                        message.setSubject("Schulen die wir anschreiben sollten");
                        // Now set the actual message
                        String messagetext = "Liebe Kollegen, <br><br> folgende Schulen müssen wir anschreiben (ungeklärt):<br>";
                        ArrayList<Schule> schules = schuleRepository.getSchools(" WHERE Ausbau=\"Ungeklärt\" ORDER BY SNR");
                        int SNR = 0;
                        for (Schule schule : schules) {
                            if (SNR != schule.getSNR())
                                messagetext += "Email:" + schule.getSNR() + ".dienst@schule.nrw.de ; Schulname:" + schule.getName_der_Schule() + "; Status ungeklärt. <br>";
                            SNR = schule.getSNR();
                        }
                        schules = schuleRepository.getSchools(" WHERE Ausbau=\"Land\" ORDER BY SNR");
                        SNR = 0;
                        messagetext += " Folgende Schulen müssen in das Landesprogramm:<br>";
                        for (Schule schule : schules) {
                            if (SNR != schule.getSNR())
                                messagetext += "Email:" + schule.getSNR() + ".dienst@schule.nrw.de ; Schulname:" + schule.getName_der_Schule() + "; Status Landesförderung. <br>";
                            SNR = schule.getSNR();
                        }
                        schules = schuleRepository.getSchools(" WHERE Ausbau=\"Bund\" ORDER BY SNR");
                        SNR = 0;
                        messagetext += " Folgende Schulen können wahrscheinlich in ein Bundesprogramm:<br>";
                        for (Schule schule : schules) {
                            if (SNR != schule.getSNR())
                                messagetext += "Email:" + schule.getSNR() + ".dienst@schule.nrw.de ; Schulname:" + schule.getName_der_Schule() + "; Status Landesförderung. <br>";
                            SNR = schule.getSNR();
                        }
                        message.setContent(messagetext, "text/html; charset=utf-8");
                        // Send message
                        Transport.send(message);
                    } catch (MessagingException mex) {
                        mex.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * Methode zum Abgleich der Gigabit-Tabelle mit der Datenbank.
     * (Inzwischen veraltet und muss manuell angepasst werden, war für den initialen Bulk-Import gedacht)
     */
    private void gigabitTabelleAbgleichen() {
        if (!RELEASE) {
            filename = Settings.getInstance().getDatabaseFolderPath()+"FINAL Uebersicht_Breitbandanschluesse_Geschäftsstelle BRA.csv";
            if (!builder.ladeBreitbandDaten(filename)) {
                ausgabeLabel.setText("FEHLER");
                ausgabeLabel.setVisible(true);
            } else {
                ausgabeLabel.setText("GEFUNDEN");
                ausgabeLabel.setVisible(true);

                ArrayList<ArrayList<String>> eingeleseneDaten = builder.gibArrayListsAusTabellen();
                schuleRepository.resetFlags();
                for (ArrayList<String> data : eingeleseneDaten) {
                    if (data.size() > 8) {
                        data.set(GIGABIT_TABELLE_SNR, trim(data.get(GIGABIT_TABELLE_SNR)).replaceAll(" ", ""));
                        if (StringUtils.isNumeric(data.get(GIGABIT_TABELLE_SNR)) && data.get(GIGABIT_TABELLE_SNR).length() == 6) {
                            if (!schuleRepository.schoolWithSNRExists(Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)))) {
                                //Schule toUpdate = schuleRepository.getSchoolWithSNR(data.get(Schule.GIGABIT_TABELLE_SNR));
                                //if (toUpdate != null) {
                                //System.out.println("Updating Schule:" + toUpdate.getSNR());
                                //toUpdate.updateBreitbandData(data);
                                //schuleRepository.save(toUpdate);
                                //

                                Schule toInsert = new Schule();
                                toInsert.setSNR(Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)));
                                toInsert.setStrasse_Hsnr(data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                                toInsert.setName_der_Schule(data.get(GIGABIT_TABELLE_SCHULNAME));
                                toInsert.setPLZ(Integer.parseInt(data.get(GIGABIT_TABELLE_PLZ)));
                                toInsert.setOrt(data.get(GIGABIT_TABELLE_ORT));
                                toInsert.setAnbindung_Kbit_DL(toInsert.getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_DOWNSTREAM)));
                                toInsert.setAnbindung_Kbit_UL(toInsert.getNormalizedDataInKbitFromString(data.get(GIGABIT_TABELLE_UPSTREAM)));
                                System.out.println("Schule eingefügt:"+data.get(GIGABIT_TABELLE_SNR)+":"+data.get(GIGABIT_TABELLE_SCHULNAME)+". "+data.get(Schule.GIGABIT_TABELLE_PLZ)+" "+data.get(Schule.GIGABIT_TABELLE_ORT)+ ", "+data.get(Schule.GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                                schuleRepository.save(toInsert);
                                //}
                            }
                            else {
                                Schule toUpdate = schuleRepository.getSchoolWithSNR(data.get(GIGABIT_TABELLE_SNR));
                                if (toUpdate != null) {
                                    //System.out.println("Updating Schule:" + toUpdate.getSNR());
                                    //toUpdate.updateBreitbandData(data);
                                    //schuleRepository.save(toUpdate);
                                    //Alle Schulen, die in beiden Datenquellen vorhanden sind
                                    schuleRepository.flagSchool(data.get(GIGABIT_TABELLE_SNR));
                                }
                            }
                        }
                        else {
                            System.out.println("Schule nicht verarbeitbar. Finding similar. SNR:" + data.get(Schule.GIGABIT_TABELLE_SNR) + ":" + data.get(Schule.GIGABIT_TABELLE_SCHULNAME) + ". " + data.get(Schule.GIGABIT_TABELLE_PLZ) + " " + data.get(Schule.GIGABIT_TABELLE_ORT) + ", " + data.get(Schule.GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                            Schule similar = schuleRepository.findSchuleWithValues(data.get(Schule.GIGABIT_TABELLE_SCHULNAME),data.get(Schule.GIGABIT_TABELLE_PLZ),data.get(Schule.GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                            if(similar == null)
                            {
                                similar = schuleRepository.findSchuleWithValues(data.get(Schule.GIGABIT_TABELLE_SCHULNAME),data.get(Schule.GIGABIT_TABELLE_PLZ), null);
                                if(similar == null)
                                {
                                    System.out.println("Schule nicht zu finden -- Neu eingfügen:"+data.get(Schule.GIGABIT_TABELLE_SCHULNAME)+";"+data.get(Schule.GIGABIT_TABELLE_PLZ)+";"+data.get(Schule.GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                                    Schule toInsert = new Schule();
                                    toInsert.createFromGigabitTabelle(data);
                                    //schuleRepository.save(toInsert);
                                }
                                else
                                {
                                    System.out.println("----Neuer Teilstandort:"+similar.getSNR()+". Name:"+similar.getName_der_Schule());
                                }
                            }
                            else
                                System.out.println("----Ähnliche Schule gefunden: SNR:"+similar.getSNR()+". Name:"+similar.getName_der_Schule());
                        }
                    }
                    else
                        System.out.println("Nicht verarbeitbar:"+data+";"+data.size());
                }

                ArrayList<Schule> gefundeneSchulen = schuleRepository.getAllFlagedSchools(true);
                schuleRepository.resetFlags();
                System.out.println("---- Ermittle Schulen, die über die ID eindeutig sind. ----");
                for (Schule schule: gefundeneSchulen)
                {
                    ArrayList<ArrayList<String>> teilstandOrte = new ArrayList<>();
                    int count = 0;
                    for (ArrayList<String> data : eingeleseneDaten) {
                        if (data.size() > 11) {
                            if (StringUtils.isNumeric(data.get(GIGABIT_TABELLE_SNR)) && data.get(GIGABIT_TABELLE_SNR).length() == 6) {
                                if(schule.getSNR() == Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)))
                                {
                                    count++;
                                    teilstandOrte.add(data);
                                }
                            }
                        }
                    }
                    if(count == 1 && teilstandOrte.size() == 1) {
                        schule.updateBreitbandData(teilstandOrte.get(0));
                        //schuleRepository.save(schule);
                    }
                    else if(count > 1 && teilstandOrte.size() > 1)
                    {
                        int found = 0;
                        System.out.println("Mehrere Adresspunkte ("+teilstandOrte.size()+") für Schule:"+schule.getSNR() );
                        for (ArrayList<String> data:teilstandOrte)
                        {
                            if(schule.getStrasse_Hsnr().equals(data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER)) || schule.getStrasse_Hsnr().replace("traße","tr.").equals(data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER)) || schule.getStrasse_Hsnr().equals(data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER).replace("traße","tr."))) {
                                System.out.println("Hauptstandort Schule:" + schule.getSNR() + " Adresse:" + schule.getStrasse_Hsnr() + " zu " + data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                                schule.updateBreitbandData(data);
                                //schuleRepository.save(schule);

                                found++;
                            }
                            else {
                                System.out.println("Teilstandort Schule:" + schule.getSNR() + " Adresse:" + schule.getStrasse_Hsnr() + " zu " + data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                                Schule teilstandort = schuleRepository.findSchuleWithValues(null,data.get(GIGABIT_TABELLE_PLZ),data.get(GIGABIT_TABELLE_STRASSE_UND_HAUSNUMMER));
                                if(teilstandort == null)
                                {
                                    teilstandort = new Schule();
                                    teilstandort.createTeilstandortWithCSV(schule, data);
                                    System.out.println("Teilstandort nicht gefunden -> Neuer Teilstandort");
                                }
                                else
                                {
                                    System.out.println("Teilstandort gefunden ->UPDATE");
                                    teilstandort.updateBreitbandData(data);
                                    teilstandort.insertHauptstandortData(schule);
                                }
                                //schuleRepository.save(teilstandort);
                            }
                        }
                        if(found != 1)
                            System.out.println("-------Error_-----------");
                    }
                    else
                    {
                        System.out.println("Fehler bei Schule:"+schule.getSNR() );
                    }
                }
                //for (Schule schule : nichtGefundeneSchulen) {
                //    System.out.println(schule.getSNR()+":"+schule.getName_der_Schule()+". "+schule.getPLZ()+" "+schule.getOrt()+ ", "+schule.getStrasse_Hsnr());
                //}
                        /*
                        showSchooldataInTable(ausgabeSpalten, nichtGefundeneSchulen, "Fehlende Schulen", true);

                        //Ausgabe in Datei schreiben
                        try {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Fehlende_Schulen.txt"), "UTF-8"));

                            //Spaltenbezeichner
                            for (int i = 0; i < fehlendeSchulenSpalten.length; i++) {
                                writer.write(fehlendeSchulenSpalten[i]);
                                if (i != fehlendeSchulenSpalten.length - 1) {
                                    writer.write(";");
                                }
                            }
                            writer.newLine();

                            //Daten
                            for (Schule schule : nichtGefundeneSchulen) {
                                writer.write(schule.getFehlendSting());
                                System.out.println(schule.getFehlendSting());
                                writer.newLine();
                            }

                            writer.close();
                        } catch (Exception e2) {
                            System.err.println(e2);
                        }
                        schuleRepository.resetFlags();
                        //Auf fehlende Schulen prüfen
                        //Daten aktualisieren
                        /*


                        String col[] = {"Interne Nummer","SNR","Name der Schule","Art der Schule","PLZ","Ort"};//,"Stra�e + Hsnr.","Zust�ndiges Schulamt","Vorwahl","Rufnummer","SF","Schultyp","Mailadresse","FB","Zust�ndig","Bedarf S1","Status S1","Moderator S1","Datum S1","Bedarf S2","Status S2","Moderator S2","Datum S2","Bedarf S3","Status S3","Moderator S3","Datum S3","Bedarf S4","Status S4","Moderator S4","Datum S4","Bedarf R1","Status R1","Moderator R1","Datum R1","Bedarf R2","Status R2","Moderator R2","Datum R2","Bedarf L1","Status L1","Moderator L1","Datum L1","Bedarf K1","Status K1","Moderator K1","Datum K1","Bedarf K2","Status K2","Moderator K2","Datum K2","Bedarf A1","Status A1","Moderator A1","Datum A1","Bedarf X2","Status X2","Moderator X2","Datum X2","Bedarf X3","Status X3","Moderator X3","Datum X3","Bemerkungen"};

                        DefaultTableModel tableModel = new DefaultTableModel(col,0);

                        for(Schule schule: eingeleseneDaten)
                        {
                            if(schuleRepository.schoolWithSNRExists(schule.getSNR()))
                            {
                                schuleRepository.saveSchoolBedarfe(schule);
                            }
                        }
                        ausgabeLabel.setText("Schulbedarfe aktualisiert");*/

            }
        }
    }

    /**
     * Methode, um spezielle Kartenschreibmethoden auszuführen.
     * Wurde für spezielle Anlässe benutzt, um aus Exceltabellen Karten zu generieren.
     */
    private void spezialKartenSchreiben() {

        DocumentWriter documentWriter = new DocumentWriter();
        JavascriptWriter javascriptWriter = new JavascriptWriter();
        try {
            documentWriter.handleWeiterbildungsTabelle(Settings.getInstance().getDatabaseFolderPath()+"Weiterbildungseinrichtungen.xlsx");
            documentWriter.handleKrankenhausTabelle(Settings.getInstance().getDatabaseFolderPath()+"gesundheit_krankenhaus_daten_nrw.xlsx");
            javascriptWriter.writeMapForKrankenhaeuser(Settings.getInstance().getDatabaseFolderPath()+"gesundheit_krankenhaus_daten_nrw.xlsx");
            javascriptWriter.writeMapForWeiterbildungseinrichtungen(Settings.getInstance().getDatabaseFolderPath()+"Weiterbildungseinrichtungen.xlsx");

        } catch (Exception e1) {
            e1.printStackTrace();
            ausgabeLabel.setText(e1.getMessage());
        }
    }

    /**
     * Methode zum automatischen Schreiben des Berichtswesens. Inzwischen nicht mehr weiter implementiert.
     */
    private void berichtSchreiben() {
        DocumentWriter documentWriter = new DocumentWriter();
        try {
            documentWriter.publishXLSXBericht();
        } catch (Exception e1) {
            e1.printStackTrace();
            ausgabeLabel.setText(e1.getMessage());
        }
    }

    /**
     * Den Javascript-Writer aufrufen und alle Ausgaben für die Gigabit-Geschäftsstellenkarte erzeugen.
     *
     */
    private void gigabitkarteSchreiben() {
        JavascriptWriter javascriptWriter = new JavascriptWriter();
        javascriptWriter.writeJavaScript();
    }

    /**
     * Methode zum Abgleich mit verschiedenen CSV-Dateien.
     * Musste im Verlauf immer manuell in verschiedenen Importszenarien angepasst werden.
     */
    private void datenAusCSVAbgleichen() {
        if (!RELEASE) {
            JFileChooser c = new JFileChooser();
            int rVal = c.showOpenDialog(mainView);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                ausgabeLabel.setText("done");
                if (!builder.ladeCSVWebsiteDaten(filename)) {
                    ausgabeLabel.setText("FEHLER");
                    ausgabeLabel.setVisible(true);
                } else {
                    ausgabeLabel.setText("GEFUNDEN");
                    ausgabeLabel.setVisible(true);

                    ArrayList<ArrayList<String>> eingeleseneDaten = builder.gibArrayListsAusTabellen();

                    ArrayList<Schule> anzuzeigendeSchulen = new ArrayList<>();


                    for (ArrayList<String> data : eingeleseneDaten) {
                        System.out.println(data.get(1));
                        if (data.size() > 9) {
                            if (StringUtils.isNumeric(data.get(Schule.CSV_DATA_SRN)) && data.get(Schule.CSV_DATA_SRN).length() == 6) {
                                if (schuleRepository.schoolWithSNRExists(Integer.parseInt(data.get(Schule.CSV_DATA_SRN)))) {
                                    Schule toUpdate = schuleRepository.getSchoolWithSNR(data.get(Schule.CSV_DATA_SRN));
                                    if (toUpdate != null) {
                                        System.out.println("Updating Schule:" + toUpdate.getSNR());
                                        toUpdate.updateCSVData(data);
                                        schuleRepository.save(toUpdate);
                                        anzuzeigendeSchulen.add(toUpdate);
                                    }
                                }
                            } else if (StringUtils.isNumeric(data.get(Schule.CSV_INTERNE_ID)) && data.get(Schule.CSV_INTERNE_ID).length() > 0) {
                                if (schuleRepository.schoolWithIDExists(Integer.parseInt(data.get(Schule.CSV_INTERNE_ID)))) {
                                    Schule toUpdate = schuleRepository.getSchoolWithID(data.get(Schule.CSV_INTERNE_ID));
                                    if (toUpdate != null) {
                                        System.out.println("Updating Schule:" + toUpdate.getSNR());
                                        toUpdate.updateCSVData(data);
                                        schuleRepository.save(toUpdate);
                                        anzuzeigendeSchulen.add(toUpdate);
                                    }
                                }
                            } else {
                                Schule toUpdate = new Schule();
                                toUpdate.setData(data);
                                schuleRepository.save(toUpdate);
                                anzuzeigendeSchulen.add(toUpdate);
                            }

                        }
                    }
                    showSchooldataInTable(ausgabeSpalten, anzuzeigendeSchulen, "Schulen in der Datenbank, die aktualisiert wurden", true);
                }

            } else if (rVal == JFileChooser.CANCEL_OPTION) {
                ausgabeLabel.setText("Bitte vor Abgleich auswählen");
            }
        }
    }

    /**
     * Auswahldialog für die Dateiauswahl
     *
     * @param endsWith Angabe des gewünschten Dateityps
     * @return String auf den Pfad der Datei.
     * @throws FileNotFoundException, bei Cancel oder nicht gewählter Approve-Option
     *
     */
    private String getFilenameFromFileChoosen(String endsWith) throws FileNotFoundException {
        JFileChooser c = new JFileChooser();
        int rVal = c.showOpenDialog(mainView);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
            ausgabeLabel.setText("done");
            if (!filename.endsWith(endsWith)) {
                ausgabeLabel.setText("Fehlerhafte Datei ausgewählt");
                ausgabeLabel.setVisible(true);
                throw new FileNotFoundException();
            } else {
                ausgabeLabel.setText("GEFUNDEN");
                ausgabeLabel.setVisible(true);
                return filename;
            }
        } else if (rVal == JFileChooser.CANCEL_OPTION) {
            ausgabeLabel.setText("Bitte vor Abgleich auswählen");
            throw new FileNotFoundException();
        }
        throw new FileNotFoundException();
    }

    /**
     * Methode zum Abgleich der Daten mit der von IT-NRW gelieferten Tabelle
     */
    private void datenbankVonITNRWEinlesen()
        {
            int ROW_SCHULNUMMER = 0;
            int ROW_HAUPTSTANDORT = 12;
            int ROW_STRASSE_UND_HAUSNUMMER = 14;
            int ROW_SCHUELERANZAHL = 29;
            if (!RELEASE) {
            String filename = null;
            XSSFWorkbook workbook = null;
            try {
                filename = getFilenameFromFileChoosen(".xlsx");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileInputStream file = null;
            try {
                file = new FileInputStream(new File(filename));
                workbook = new XSSFWorkbook(file);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            schuleRepository.resetFlags();
            XSSFSheet sheet = workbook.getSheetAt(0);
            for(int i = 1; i <= sheet.getLastRowNum() && sheet.getRow(i).getCell(0) != null;i++){

                XSSFRow row = sheet.getRow(i);
                if(row.getCell(ROW_SCHUELERANZAHL).getNumericCellValue() != 0) {

                    int current_schulnummer = Integer.parseInt((row.getCell(ROW_SCHULNUMMER) + " ").split("\\.")[0]);
                    if (schuleRepository.schoolWithSNRExists(current_schulnummer)) {
                        ArrayList<Schule> schules = schuleRepository.getStandorteZu(String.valueOf(current_schulnummer));
                        boolean found = false;
                        for (Schule schule : schules) {
                            if (schule.getStrasse_Hsnr().substring(0,4).equals(row.getCell(ROW_STRASSE_UND_HAUSNUMMER).getStringCellValue().substring(0,4))) {
                                if ((schule.getStandort().equals(HAUPTSTANDORT) && row.getCell(ROW_HAUPTSTANDORT).getStringCellValue().equals("1")) || (schule.getStandort().equals(TEILSTANDORT) && row.getCell(ROW_HAUPTSTANDORT).getStringCellValue().equals("0"))) {
                                    found = true;
                                    schule.setSchuelerzahlIT((int)Math.round(row.getCell(ROW_SCHUELERANZAHL).getNumericCellValue()));
                                    System.out.println("Updating Schule mit ID:" + schule.getId() + "; setze SchülerzahlIT auf: "+((int)Math.round(row.getCell(ROW_SCHUELERANZAHL).getNumericCellValue()))+"!");
                                    schuleRepository.save(schule);
                                }
                            }
                        }
                        if (!found) {
                            boolean found2 = false;
                            for (Schule schule : schules) {
                                if (schule.getStrasse_Hsnr().substring(0,4).equals(row.getCell(ROW_STRASSE_UND_HAUSNUMMER).getStringCellValue().substring(0,4))) {
                                    found2 = true;
                                    if(schule.getSchuelerzahlIT() == 0)
                                    {
                                        System.out.println("Updating Schule mit ID:" + schule.getId() + "; setze SchülerzahlIT auf: "+((int)Math.round(row.getCell(ROW_SCHUELERANZAHL).getNumericCellValue()))+"!");
                                        schule.setSchuelerzahlIT((int)Math.round(row.getCell(ROW_SCHUELERANZAHL).getNumericCellValue()));
                                        schuleRepository.save(schule);
                                    }
                                }
                            }
                            if(!found2)
                                System.out.println("Schule mit ID:" + current_schulnummer + ", Standort:" + row.getCell(ROW_STRASSE_UND_HAUSNUMMER).getStringCellValue() + " und Hauptstandort:" + row.getCell(ROW_HAUPTSTANDORT).getStringCellValue() + " nicht in der Datenbank gefunden!");
                            else
                                System.out.println("Schule mit ID:" + current_schulnummer + ", Standort:" + row.getCell(ROW_STRASSE_UND_HAUSNUMMER).getStringCellValue() + " hat Haupt- und Teilstandort gedreht!");
                        }
                    }
                    else {
                        System.out.println("Schule mit SNR:" + current_schulnummer + " nicht in der Datenbank gefunden!");
                    }

                }
            }
            ArrayList<Schule> schules = schuleRepository.getSchools("WHERE schuelerzahlIT=0");
            for (Schule schule : schules) {
                System.out.println("Schule :" + schule.getSNR() + " mit ID:"+schule.getId()+" ohne IT-Schülerzahl.");
            }


            updateData();
            ausgabeLabel.setText("Importiert!");
            ausgabeLabel.setVisible(true);
        }
    }

    /**
     * Abgleich der internen Daten mit den Daten von IT-NRW aus der schulver.mdb
     * (Inzwischen überholt, da die Tabelle von IT-NRW deutlich bessere Daten enthält)
     */
    private void tabelleVonITNRWDatenbankEinlesen() {
        if (!RELEASE) {
            String filename = null;
            try {
                filename = getFilenameFromFileChoosen(".mdb");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ArrayList<ArrayList<String>> itNRWSchulen = MDBConnector.getInstance().getAllSchools(Settings.getInstance().getBezirksSuchString(), filename);
            schuleRepository.resetFlags();
            int i = 0;
            for (ArrayList<String> school : itNRWSchulen) {
                Schule schule = schuleRepository.getSchoolWithSNR(school.get(0));
                i++;
                if (schule == null) {
                    System.out.print(":Schule mit Schulnummer:" + school.get(0) + " fehlt:");
                    for (int j = 0; j < MDBConnector.columnsFromDatabase.size(); j++) {
                        System.out.print(school.get(j) + ";");
                    }
                    System.out.println("");
                    schule = new Schule();
                    schule.setSNR(Integer.parseInt(school.get(0)));
                    schule.setName_der_Schule(school.get(2));
                    schule.setArt_der_Schule(school.get(3));
                    schule.setPLZ(Integer.parseInt(school.get(5)));
                    schule.setOrt(school.get(6));
                    schule.setStrasse_Hsnr(school.get(7));
                    schule.setVorwahl(school.get(8));
                    schule.setRufnummer(school.get(9));
                    schuleRepository.save(schule);
                } else {
                            /*
                            if (schule.getPLZ() != Integer.parseInt(school.get(5)) && school.get(5).length() > 4) {
                                schule.setPLZ(Integer.parseInt(school.get(5)));
                                System.out.println(":Schule mit Schulnummer:" + school.get(0) + " PLZänderung. Changing:" + schule.getPLZ() + " to:" + school.get(5) + "...");
                                schuleRepository.save(schule);
                            }
                            if (!schule.getOrt().equals(school.get(6))) {
                                System.out.println(":Schule mit Schulnummer:" + school.get(0) + " Ortsänderung. Changing:" + schule.getOrt() + " to:" + school.get(6) + "...");
                                schule.setOrt(school.get(6));
                                schuleRepository.save(schule);
                            }
                            if (!schule.getStrasse_Hsnr().equals(school.get(7).replaceAll("'", ""))) {
                                System.out.println(":Schule mit Schulnummer:" + school.get(0) + " Strassenänderung. Changing:" + schule.getStrasse_Hsnr() + " to:" + school.get(7).replaceAll("'", "") + "...");
                                schule.setStrasse_Hsnr(school.get(7).replaceAll("'", ""));
                                schuleRepository.save(schule);
                            }
                            if (!schule.getVorwahl().equals(school.get(8)) && !("0" + schule.getVorwahl()).equals(school.get(8)) && school.get(8).length() > 3) {
                                System.out.println(":Schule mit Schulnummer:" + school.get(0) + " Vorwahländerung. Changing:" + schule.getVorwahl() + " to:" + school.get(8) + "...");
                                schule.setVorwahl(school.get(8));
                                schuleRepository.save(schule);
                            }
                            if (!schule.getRufnummer().equals(school.get(9))) {
                                System.out.println(":Schule mit Schulnummer:" + school.get(0) + " Rufnummernänderung. Changing:" + schule.getRufnummer() + " to:" + school.get(9) + "...");
                                schule.setRufnummer(school.get(9));
                                schuleRepository.save(schule);
                            }*/

                    schuleRepository.flagSchool(school.get(0));
                }
            }
            ArrayList<Schule> nichtGefundeneSchulen = schuleRepository.getAllFlagedSchools(false);
            showSchooldataInTable(ausgabeSpalten, nichtGefundeneSchulen, "Schulen in der Datenbank, die nicht in der IT-NRW-Datenbank vorhanden sind", true);
            schuleRepository.resetFlags();
        }
    }


    /**
     * Spezialimportmethode. Musste im Verlauf immer manuell angepasst werden.
     */
    private void specialImport() {
        if (!RELEASE) {
            filename = Settings.getInstance().getDatabaseFolderPath()+"SchulNrUndCall.csv";
            if (!builder.ladeBreitbandDaten(filename)) {
                ausgabeLabel.setText("FEHLER");
                ausgabeLabel.setVisible(true);
            } else {
                ausgabeLabel.setText("GEFUNDEN");
                ausgabeLabel.setVisible(true);

                //String  col[] ={"Bezeichnung","Straße und Hausnummer","Ort","Postleitzahl","Auskunft erteilt (Ansprechpartner)","Telefonnummer Ansprechpartner","E-Mail-Adresse Ansprechpartner","Schulbezeichnung","Straße u. Hausnummer","Ort3","Postleitzahl4","Schul-ID(Schulnummer)"};

                ArrayList<ArrayList<String>> eingeleseneDaten = builder.gibArrayListsAusTabellen();
                schuleRepository.resetFlags();
                for (ArrayList<String> data : eingeleseneDaten) {
                    if (data.size() > 1) {
                        data.set(0, trim(data.get(0)).replaceAll(" ", ""));
                        if (StringUtils.isNumeric(data.get(0)) && data.get(0).length() == 6) {
                            if (!schuleRepository.schoolWithSNRExists(Integer.parseInt(data.get(0)))) {
                                System.out.println("Schulnummer nicht gefunden:" + data.get(0));
                                //}
                            } else {
                                Schule toUpdate = schuleRepository.getHauptstandorteZu(data.get(0));
                                if(toUpdate.getAusbau(false).equals(AUSBAU_BUND) || toUpdate.getAusbau(false).equals(AUSBAU_LAND) || toUpdate.getAusbau(false).equals(AUSBAU_UNGEKLAERT)) {
                                    System.out.println("Updating Schule:" + toUpdate.getSNR());
                                    toUpdate.setAusbau("Bund " + data.get(1) + ". Call");
                                    schuleRepository.save(toUpdate);
                                }
                                else
                                {
                                    System.out.println("Schule nicht geupdatet, da Schule eigentlich nicht in Bundesprogramm:" + data.get(0)+". Schule in "+toUpdate.getAusbau(false));
                                }
                            }
                        }
                    } else
                        System.out.println("Nicht verarbeitbar:" + data);
                }

            }

            schuleRepository.resetFlags();
        }
    }


    /**
     * Wenn zwei Schulen die gleiche Adresse haben, werden die Geocoordinaten leicht verschoben,
     * um beide Schulstandorte auf der Karte sichtbar zu machen.
     */
    private void doppelteGeocoordinatenVerschieben() {
        ArrayList<Schule> schulen = schuleRepository.getSchulenMitGleichenGeolocations();
        while(schulen.size() > 0) {
            for (Schule schule : schulen) {
                //Geringfügig verschieben, um alle Schulen auf der Karte sichtbar zu machen.
                schule.setLat(schule.getLat() + 0.0002);
                schule.setLng(schule.getLng() + 0.0002);
                schuleRepository.save(schule);
                System.out.println("Schule mit SNR:" + schule.getSNR() + " und Name:" + schule.getName_der_Schule() + " bereinigt.");
            }
            schulen = schuleRepository.getSchulenMitGleichenGeolocations();
        }
    }

    /**
     * Methode füllt für alle Schulen im Zwischenspeicher die Geodaten aus.
     * Wichtig ist vorher die Schulen mittels showAndLoadSchoolsWithoutGeodata zu laden.
     */
    private void geocoordinatenInDBLaden() {

        //Nur ermittelte Schulen werden upgedatet
        for(Schule schule: schulenImZwischenspeicher)
        {
            try{
                System.out.print("Finde Geocoordinaten für:"+schule.getName_der_Schule());
                geoUtils = new GoogleGeoUtils();
                Thread.sleep(1000); //Wahrscheinlich überflüssig
                GoogleGeoUtils.GoogleGeoLatLng geoCode = geoUtils.getGeoCode(schule.getStrasse_Hsnr()+", "+schule.getPLZ()+" "+schule.getOrt(), true);
                System.out.println(schule.getSNR()+":"+geoCode.getLatLng()); //Testausgabe
                schule.setLat(geoCode.getLat());
                schule.setLng(geoCode.getLng());
                schuleRepository.save(schule);
                System.out.print("Geocoordinaten für :"+schule.getName_der_Schule()+" sind Lat:"+schule.getLat()+"; Lng:"+schule.getLng());

            }catch(Exception ex)
            {

            }
        }
    }


    /**
     * Schulen, die in der aktuellen Datenbank keine Geocoordinaten haben in der Übersicht anzeigen.
     */
    private void showSchuleMitFehlendenGeocoordinaten() {
        String col[] = {"Interne Nummer","SNR","Name der Schule","Art der Schule","PLZ","Ort"};//,"Stra�e + Hsnr.","Zust�ndiges Schulamt","Vorwahl","Rufnummer","SF","Schultyp","Mailadresse","FB","Zust�ndig","Bedarf S1","Status S1","Moderator S1","Datum S1","Bedarf S2","Status S2","Moderator S2","Datum S2","Bedarf S3","Status S3","Moderator S3","Datum S3","Bedarf S4","Status S4","Moderator S4","Datum S4","Bedarf R1","Status R1","Moderator R1","Datum R1","Bedarf R2","Status R2","Moderator R2","Datum R2","Bedarf L1","Status L1","Moderator L1","Datum L1","Bedarf K1","Status K1","Moderator K1","Datum K1","Bedarf K2","Status K2","Moderator K2","Datum K2","Bedarf A1","Status A1","Moderator A1","Datum A1","Bedarf X2","Status X2","Moderator X2","Datum X2","Bedarf X3","Status X3","Moderator X3","Datum X3","Bemerkungen"};


        schulenImZwischenspeicher = schuleRepository.getSchoolsWithoutGeodata();
        if(schulenImZwischenspeicher != null && !schulenImZwischenspeicher.isEmpty()) {

            showSchooldataInTable(fehlendeSchulenSpalten,schulenImZwischenspeicher,"Folgenden Schulen fehlt die Geocoordinate.", true);
        }
        else
        {
            ausgabeLabel.setText("Alle Schulen haben Geokoordinaten.");
        }
    }

    private String filterSNR = ""; //Filter nach Schulnummer
    private String filterOrt = "-"; //Filtertext nach Ort
    private String filterSchulamt = "-"; //Filter nach Schulamt
    private String filterAusbau = "alle"; //Filter nach Ausbaustatus


    /**
     * Ausgabe nach Auswahl anzeigen.
     */
    public void updateData()
    {
        ArrayList<Schule> schulen = schuleRepository.getAllSchools(filterSNR,filterOrt,filterSchulamt,filterAusbau);
        String ausgabe = "Alle Schulen";
        if(!filterOrt.equals("-") || !filterSchulamt.equals("-") || !filterSNR.equals("") || !filterAusbau.equals("alle"))
            ausgabe+=" mit ";
        if(!filterSNR.equals(""))
        {
            ausgabe+="Schulnummer beinhaltet "+filterSNR+"";
            if(!filterSchulamt.equals("-") || !filterOrt.equals("-") || !filterAusbau.equals("alle"))
                ausgabe+= " und ";
        }
        if(!filterSchulamt.equals("-"))
        {
            ausgabe+= "Zuständiges Schulamt ist "+filterSchulamt+"";
            if(!filterOrt.equals("-") || !filterAusbau.equals("alle"))
                ausgabe+= " und ";
        }
        if(!filterOrt.equals("-"))
        {
            ausgabe+= "Ort ist "+filterOrt+"";
            if(!filterAusbau.equals("alle"))
                ausgabe+= " und ";
        }
        if(!filterAusbau.equals("alle"))
        {
            ausgabe+= "Ausbaustatus ist "+filterAusbau+"";
        }
        ausgabe+=". Anzahl: "+schulen.size()+".";
        showSchooldataInTable(ausgabeSpalten,schulen,ausgabe, false);
    }


    /**
     * Ermittelte Daten in Table-Bereich anzeigen.
     */
    private void showSchooldataInTable(String[] col, ArrayList<Schule> schulen, String ausgabe, boolean fehlend) {
        DefaultTableModel tableModel = new DefaultTableModel(col,0);
        for (Schule schule : schulen) {
            if(fehlend)
                tableModel.addRow(schule.getfehlendVector());
            else
                tableModel.addRow(schule.getVector());
        }
        ausgabeTabelle.setAutoCreateRowSorter(true);
        ausgabeTabelle.setModel(tableModel);
        ausgabeLabel.setText(ausgabe);
        ausgabeTabelle.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // Wenn Schule ausgewählt, Detailansicht zeigen
                if(ausgabeTabelle.getSelectedRow()>-1)
                {
                    System.out.println(ausgabeTabelle.getValueAt(ausgabeTabelle.getSelectedRow(), 0).toString());
                    editSchoolFrame.setSchule(schuleRepository.getHauptstandortWithID(ausgabeTabelle.getValueAt(ausgabeTabelle.getSelectedRow(), 0).toString()));
                    schuleBearbeitenFrame.setVisible(true);
                }
            }
        });
    }

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame("Manager V"+version+" "+releaseDate);
        mainFrame.setContentPane(new Interface().mainView);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }


    /**
     * Custom create für spezielle UI-Komponenten.
     */
    private void createUIComponents() {
        SchuleRepository schuleRepository = new SchuleRepository();
        String[] staedteArray = schuleRepository.getAllStaedte();
        String[] allSschulaemterArray = schuleRepository.getAllSschulaemter();
        String[] allAusbaustatusArray = schuleRepository.getAllAusbaustatus();
        String importFunctions[] = {IMPORT_STRING_NOTHING, IMPORT_STRING_SCHULE_MIT_FEHLENDEN_GEOCOORDINATEN, IMPORT_STRING_GEOCOORDINATEN_IN_DB_LADEN, IMPORT_STRING_DOPPELTE_GEOCOORDINATEN_VERSCHIEBEN, IMPORT_STRING_DATENBANK_VON_IT_NRW_EINLESEN,IMPORT_STRING_TABELLE_VON_IT_NRW_EINLESEN, IMPORT_STRING_DATEN_AUS_CSV_ABGLEICHEN, IMPORT_STRING_SONDERIMPORT, IMPORT_STRING_GIGABITTABELLE_ABGLEICHEN};
        String exportFunctions[] = {EXPORT_STRING_GIGABIT_KARTE_SCHREIBEN,EXPORT_STRING_BERICHT_SCHREIBEN , EXPORT_STRING_SPEZIALKARTE_SCHREIBEN};

        String[] finalStaedteArray = new String[staedteArray.length +1];
        String[] finalSchulaemterArray = new String[allSschulaemterArray.length +1];
        String[] finalAusbaustatusArray = new String[allAusbaustatusArray.length +2];
        //Arrays um Leerfelder erweitern
        for(int i=0;i < staedteArray.length;i++)
            finalStaedteArray[i+1] = staedteArray[i];
        for(int i=0;i < allSschulaemterArray.length;i++)
            finalSchulaemterArray[i+1] = allSschulaemterArray[i];
        for(int i=0;i < allAusbaustatusArray.length;i++)
            finalAusbaustatusArray[i+2] = allAusbaustatusArray[i];
        finalSchulaemterArray[0] = "-";
        finalStaedteArray[0] = "-";
        finalAusbaustatusArray[0] = "alle";
        finalAusbaustatusArray[1] = finalAusbaustatusArray[2];
        finalAusbaustatusArray[2] = "Bund (alle)";


        comboBoxSchulaemter = new JComboBox<>(finalSchulaemterArray);
        comboBoxOrte = new JComboBox<>(finalStaedteArray);
        comboBoxAusbaustatus = new JComboBox<>(finalAusbaustatusArray);
        comboBoxAdminImport = new JComboBox<>(importFunctions);
        comboBoxAdminExport = new JComboBox<>(exportFunctions);
    }
}
