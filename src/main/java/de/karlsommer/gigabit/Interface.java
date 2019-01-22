package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.MDBConnector;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import de.karlsommer.gigabit.filehandling.DocumentWriter;
import de.karlsommer.gigabit.filehandling.ImportBuilder;
import de.karlsommer.gigabit.filehandling.JavascriptWriter;
import de.karlsommer.gigabit.geocoding.GoogleGeoUtils;
import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.helper.SpinnerCircularListModel;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static de.karlsommer.gigabit.database.model.Schule.*;
import static org.apache.commons.lang.StringUtils.trim;

public class Interface implements DataUpdater{

    public static final boolean RELEASE = true;
    public static final String version = "1.5";
    public static final String releaseDate = "22.01.2019";
    private JPanel mainView;
    private JButton geokoordinatenLaden;
    private JButton javascriptUndHTMLGigabitSchreiben;
    private JLabel ausgabeLabel;
    private JButton testButton;
    private JButton csvDataEinlesenButton;
    private JButton tabelleMitITNRWAbgleichenButton;
    private JButton showAndLoadSchoolsWithoutGeodata;
    private JButton breitbandCSVDateiEinlesen;
    private JTable ausgabeTabelle;
    private JButton gigabitTabelleabgleichen;
    private JButton goButton1;
    private JButton writeBerichtButton;
    private JButton toDoButton1;
    private JButton internButton;
    private JButton customButton;
    private JButton specialButton;
    private JPanel topPanel;
    private JLabel versionName;
    private JButton exportSpecialButton;
    private JTextField textFieldSchulnummern;
    private JButton suchenButtonSchullnummern;
    private JButton alleSchulenAnzeigenButton;
    private JComboBox comboBoxSchulaemter;
    private JComboBox comboBoxOrte;
    private JComboBox comboBoxAusbaustatus;
    private JButton exportButton;
    private JButton importButton;
    private JButton changeHistoryButton;
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

        geokoordinatenLaden.addActionListener(new ActionListener() {
            /**
             * Methode füllt für alle Schulen im Zwischenspeicher die Geodaten aus.
             * Wichtig ist vorher die Schulen mittels showAndLoadSchoolsWithoutGeodata zu laden.
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

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
        });
        javascriptUndHTMLGigabitSchreiben.addActionListener(new ActionListener() {
            /**
             * Den Javascript-Writer aufrufen und alle Ausgaben für die Gigabit-Geschäftsstelle erzeugen
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JavascriptWriter javascriptWriter = new JavascriptWriter();
                javascriptWriter.writeJavaScript();
            }
        });
        //Testbutton
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String col[] = {"Interne Nummer","SNR","Name der Schule","Art der Schule","PLZ","Ort"};//,"Stra�e + Hsnr.","Zust�ndiges Schulamt","Vorwahl","Rufnummer","SF","Schultyp","Mailadresse","FB","Zust�ndig","Bedarf S1","Status S1","Moderator S1","Datum S1","Bedarf S2","Status S2","Moderator S2","Datum S2","Bedarf S3","Status S3","Moderator S3","Datum S3","Bedarf S4","Status S4","Moderator S4","Datum S4","Bedarf R1","Status R1","Moderator R1","Datum R1","Bedarf R2","Status R2","Moderator R2","Datum R2","Bedarf L1","Status L1","Moderator L1","Datum L1","Bedarf K1","Status K1","Moderator K1","Datum K1","Bedarf K2","Status K2","Moderator K2","Datum K2","Bedarf A1","Status A1","Moderator A1","Datum A1","Bedarf X2","Status X2","Moderator X2","Datum X2","Bedarf X3","Status X3","Moderator X3","Datum X3","Bemerkungen"};


                ArrayList<Schule> schulen = schuleRepository.getAllFlagedSchools(false);
                showSchooldataInTable(ausgabeSpalten,schulen,"Alle Schulen.", false);
            }
        });
        showAndLoadSchoolsWithoutGeodata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
        breitbandCSVDateiEinlesen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                JFileChooser c = new JFileChooser();
                // Demonstrate "Open" dialog:
                int rVal = c.showOpenDialog(mainView);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    //Hier Änderung für Dateiauswahl ohne Dialog
                        /*
                        filename = "/Users/karl/ownCloud/ADV/temp.csv";

                    filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                    ausgabeLabel.setText("done");*/
                if (!RELEASE) {
                    filename = "/Users/karl/projects/Gigabit-DB-Tool/databases/FINAL Uebersicht_Breitbandanschluesse_Geschäftsstelle BRA.csv";
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
            //else if (rVal == JFileChooser.CANCEL_OPTION) {
            //    ausgabeLabel.setText("Bitte vor Abgleich auswählen");
            // }
            //}
        });
        tabelleMitITNRWAbgleichenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!RELEASE) {
                    ArrayList<ArrayList<String>> itNRWSchulen = MDBConnector.getInstance().getAllSchools();
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
        });
        goButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Schule> schulen = schuleRepository.getSchulenMitGleichenGeolocations();
                while(schulen.size() > 0) {
                    for (Schule schule : schulen) {
                        schule.setLat(schule.getLat() + 0.0002);
                        schule.setLng(schule.getLng() + 0.0002);
                        schuleRepository.save(schule);
                        System.out.println("Schule mit SNR:" + schule.getSNR() + " und Name:" + schule.getName_der_Schule() + " bereinigt.");
                    }
                    schulen = schuleRepository.getSchulenMitGleichenGeolocations();
                }
            }
        });
        gigabitTabelleabgleichen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!RELEASE) {
                    JFileChooser c = new JFileChooser();
                    // Demonstrate "Open" dialog:
                    int rVal = c.showOpenDialog(mainView);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        //Hier Änderung für Dateiauswahl ohne Dialog
                        //filename = "/Users/karl/NetBeansProjects/JSIandGMC/Breitband_Schulen.csv";
                        filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                        if (!builder.ladeCSVWebsiteDaten(filename)) {
                            ausgabeLabel.setText("FEHLER");
                            ausgabeLabel.setVisible(true);
                        } else {
                            ausgabeLabel.setText("GEFUNDEN");
                            ausgabeLabel.setVisible(true);

                            //String  col[] ={"Bezeichnung","Straße und Hausnummer","Ort","Postleitzahl","Auskunft erteilt (Ansprechpartner)","Telefonnummer Ansprechpartner","E-Mail-Adresse Ansprechpartner","Schulbezeichnung","Straße u. Hausnummer","Ort3","Postleitzahl4","Schul-ID(Schulnummer)"};

                            ArrayList<ArrayList<String>> eingeleseneDaten = builder.gibArrayListsAusTabellen();

                            ArrayList<Schule> anzuzeigendeSchulen = new ArrayList<>();


                            for (ArrayList<String> data : eingeleseneDaten) {
                                if (data.size() > 11) {
                                    data.set(11, trim(data.get(GIGABIT_TABELLE_SNR)).replaceAll(" ", ""));
                                    if (StringUtils.isNumeric(data.get(GIGABIT_TABELLE_SNR)) && data.get(GIGABIT_TABELLE_SNR).length() == 6) {
                                        if (schuleRepository.schoolWithSNRExists(Integer.parseInt(data.get(GIGABIT_TABELLE_SNR)))) {
                                            Schule toUpdate = schuleRepository.getSchoolWithSNR(data.get(GIGABIT_TABELLE_SNR));
                                            if (toUpdate != null) {
                                                System.out.println("Updating Schule:" + toUpdate.getSNR());
                                                toUpdate.updateBreitbandData(data);
                                                //schuleRepository.save(toUpdate);
                                                anzuzeigendeSchulen.add(toUpdate);
                                            }
                                        }
                                    }
                                }
                            }

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
                    } else if (rVal == JFileChooser.CANCEL_OPTION) {
                        ausgabeLabel.setText("Bitte vor Abgleich auswählen");
                    }
                }
            }
        });
        csvDataEinlesenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!RELEASE) {
                    JFileChooser c = new JFileChooser();
                    int rVal = c.showOpenDialog(mainView);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        //Hier Änderung für Dateiauswahl ohne Dialog
                        /*
                        filename = "/Users/karl/ownCloud/ADV/temp.csv";
                        */
                        filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                        ausgabeLabel.setText("done");
                        if (!builder.ladeCSVWebsiteDaten(filename)) {
                            ausgabeLabel.setText("FEHLER");
                            ausgabeLabel.setVisible(true);
                        } else {
                            ausgabeLabel.setText("GEFUNDEN");
                            ausgabeLabel.setVisible(true);

                            //String  col[] ={"Bezeichnung","Straße und Hausnummer","Ort","Postleitzahl","Auskunft erteilt (Ansprechpartner)","Telefonnummer Ansprechpartner","E-Mail-Adresse Ansprechpartner","Schulbezeichnung","Straße u. Hausnummer","Ort3","Postleitzahl4","Schul-ID(Schulnummer)"};

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
        });
        // Bericht schreiben
        writeBerichtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocumentWriter documentWriter = new DocumentWriter();
                try {
                    documentWriter.publishXLSXBericht();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    ausgabeLabel.setText(e1.getMessage());
                }
            }
        });
        internButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!RELEASE) {
                    JFileChooser c = new JFileChooser();
                    int rVal = c.showOpenDialog(mainView);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        //Hier Änderung für Dateiauswahl ohne Dialog
                        /*
                        filename = "/Users/karl/ownCloud/ADV/temp.csv";
                        */
                        filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                        ausgabeLabel.setText("done");
                        if (!builder.ladeCSVSchuelerzahlen(filename)) {
                            ausgabeLabel.setText("FEHLER");
                            ausgabeLabel.setVisible(true);
                        } else {
                            ausgabeLabel.setText("GEFUNDEN");
                            ausgabeLabel.setVisible(true);

                            //String  col[] ={"Bezeichnung","Straße und Hausnummer","Ort","Postleitzahl","Auskunft erteilt (Ansprechpartner)","Telefonnummer Ansprechpartner","E-Mail-Adresse Ansprechpartner","Schulbezeichnung","Straße u. Hausnummer","Ort3","Postleitzahl4","Schul-ID(Schulnummer)"};

                            ArrayList<ArrayList<String>> eingeleseneDaten = builder.gibArrayListsAusTabellen();


                            int found = 0;
                            for (ArrayList<String> data : eingeleseneDaten) {
                                System.out.println(data.get(0)+";"+data.get(1));

                                if (data.size() > 1) {
                                    if (StringUtils.isNumeric(data.get(0)) && data.get(0).length() == 6) {
                                        if (schuleRepository.schoolWithSNRExists(Integer.parseInt(data.get(0)))) {
                                            Schule toUpdate = schuleRepository.getSchoolhauptstandortWithSNR(data.get(0));
                                            if (toUpdate != null) {
                                                System.out.println("Updating Schule:" + toUpdate.getSNR()+ " setze Schülerzahl auf:"+data.get(1));
                                                toUpdate.setSchuelerzahl(Integer.parseInt(data.get(1)));
                                                schuleRepository.save(toUpdate);
                                                found++;
                                            }
                                        }
                                    }
                                }
                            }
                            System.out.println(("Schülerzahlen bei "+found+" Schulen aktualisiert"));
                        }

                    } else if (rVal == JFileChooser.CANCEL_OPTION) {
                        ausgabeLabel.setText("Bitte vor Abgleich auswählen");
                    }
                }
            }
        });
        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DocumentWriter documentWriter = new DocumentWriter();
                JavascriptWriter javascriptWriter = new JavascriptWriter();
                try {
                    documentWriter.handleWeiterbildungsTabelle("databases/Weiterbildungseinrichtungen.xlsx");
                    documentWriter.handleKrankenhausTabelle("databases/gesundheit_krankenhaus_daten_nrw.xlsx");
                    javascriptWriter.writeMapForKrankenhaeuser("databases/gesundheit_krankenhaus_daten_nrw.xlsx");
                    javascriptWriter.writeMapForWeiterbildungseinrichtungen("databases/Weiterbildungseinrichtungen.xlsx");

                } catch (Exception e1) {
                    e1.printStackTrace();
                    ausgabeLabel.setText(e1.getMessage());
                }
            }
        });
        toDoButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        specialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!RELEASE) {
                    filename = "/Users/karl/projects/Gigabit-DB-Tool/databases/Import_Ausbau_Tabelle.csv";
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
                                        ArrayList<Schule> toUpdate = schuleRepository.getStandorteZu(data.get(0));
                                        if (toUpdate.size() > 0) {
                                            boolean found = false;
                                            for(Schule toUp: toUpdate)
                                            {
                                                if(toUp.getStrasse_Hsnr().substring(0,2).equals(data.get(2).substring(0,2)))
                                                {
                                                    System.out.println("Updating Schule:" + toUp.getSNR());
                                                    toUp.setAusbau(data.get(1));
                                                    toUp.setFlag(true);
                                                    schuleRepository.save(toUp);
                                                    found = true;
                                                }
                                            }
                                            if(!found)
                                            {
                                                System.out.println("Schulnummer: " + data.get(0) + "; und Adresse:"+data.get(2)+" nicht gefunden:");
                                            }
                                            //Alle Schulen, die in beiden Datenquellen vorhanden sind
                                            //schuleRepository.flagSchool(data.get(0));
                                        }
                                        else
                                        {
                                            System.out.println("Schulnummer nicht gefunden:" + data.get(0));
                                        }
                                    }
                                } /*else {
                                    //System.out.println("Schule nicht verarbeitbar. Finding similar. SNR:" + data.get(0) + ":" + data.get(1) + ". " + data.get(6) + " " + data.get(7) + ", " + data.get(4));
                                    Schule similar = schuleRepository.findSchuleWithValues(data.get(1), data.get(6), data.get(4));
                                    if (similar == null) {
                                        similar = schuleRepository.findSchuleWithValues(data.get(1), data.get(6), null);
                                        if (similar == null) {
                                            System.out.println("Schule nicht zu finden:" + data.get(1) + ";" + data.get(6) + ";" + data.get(4));
                                            //Schule toInsert = new Schule();
                                            //toInsert.createFromGigabitTabelle(data);
                                            //schuleRepository.save(toInsert);
                                        } else {
                                            System.out.println("----Neuer Teilstandort:" + similar.getSNR() + ". Name:" + similar.getName_der_Schule());
                                        }
                                    } else {
                                        if(similar.getAusbau() == null) {
                                            System.out.println("----Ähnliche Schule gefunden. Updating Schule: SNR:" + similar.getSNR() + ". ID:" + similar.getId() + ", Name:" + similar.getName_der_Schule());
                                            similar.setAusbau(data.get(8));
                                            //schuleRepository.save(similar);
                                        }
                                    }
                                }*/
                            } else
                                System.out.println("Nicht verarbeitbar:" + data);
                        }
                        ArrayList<Schule> leereSchulen = schuleRepository.getSchoolsWithoutAusbau();
                        for(Schule schule: leereSchulen){
                            //System.out.println("Schule ohne Ausbau calculating:" + schule.getSNR());
                            if(schule.getSchuelerzahl() > 0)
                            {
                                if((Math.ceil(schule.getSchuelerzahl() / 23) + 1) * 30 > (schule.getAnbindung_Kbit_DL() / 1000))
                                {
                                    System.out.println("Schule Bund:" + schule.getSNR() + "; Schüler:"+schule.getSchuelerzahl()+"; DL:"+schule.getAnbindung_Kbit_DL());
                                    schule.setAusbau(AUSBAU_E_BUND);
                                    schuleRepository.save(schule);
                                }
                                else
                                {
                                    System.out.println("Schule Land:" + schule.getSNR() + "; Schüler:"+schule.getSchuelerzahl()+"; DL:"+schule.getAnbindung_Kbit_DL());
                                    schule.setAusbau(AUSBAU_E_LAND);
                                    schuleRepository.save(schule);
                                }
                            }
                            else
                            {
                                System.out.println("Nicht genug Daten:" + schule.getSNR());
                            }
                        }

                    }

                    schuleRepository.resetFlags();
                    //ArrayList<Schule> schulen = schuleRepository.getAllSchools();
                    //DocumentWriter documentWriter = new DocumentWriter();
                    //try {
                    //    documentWriter.writeAnschlussSchoolTexts();
                    //} catch (Exception e1) {
                    //    e1.printStackTrace();
                    //}
                }
            }
        });
        exportSpecialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Schule> schulen = schuleRepository.getSchoolsWithAusbau("Ungekl");
                DocumentWriter documentWriter = new DocumentWriter();
                documentWriter.writeSchulenInTabelle(schulen);
            }
        });
        alleSchulenAnzeigenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSNR = "";
                filterOrt = "-";
                filterSchulamt = "-";
                updateData();
            }
        });
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
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Schule> schulen = schuleRepository.getAllSchools(filterSNR,filterOrt,filterSchulamt,filterAusbau);
                DocumentWriter documentWriter = new DocumentWriter();
                documentWriter.writeSchulenInExcel(schulen);

            }
        });
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                int rVal = c.showOpenDialog(mainView);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    //Hier Änderung für Dateiauswahl ohne Dialog
                    /*
                    filename = "/Users/karl/ownCloud/ADV/temp.csv";
                    */
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
                }
            }
        });
        changeHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggerFrame.setVisible(true);
            }
        });
    }

    private void showStringdataInTable(String[] col, ArrayList<ArrayList<String>> schulen, String ausgabe)
    {

        DefaultTableModel tableModel = new DefaultTableModel(col,0);
        for (ArrayList<String> data : schulen) {
            tableModel.addRow(ImportBuilder.getStringVectorFromArrayListData(data));
        }
        Interface.this.ausgabeTabelle.setModel(tableModel);
        ausgabeLabel.setText(ausgabe);
    }

    private String filterSNR = "";
    private String filterOrt = "-";
    private String filterSchulamt = "-";
    private String filterAusbau = "alle";

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
                // do some actions here, for example
                // print first column value from selected row
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

    private void createUIComponents() {
        SchuleRepository schuleRepository = new SchuleRepository();
        String[] staedteArray = schuleRepository.getAllStaedte();
        String[] allSschulaemterArray = schuleRepository.getAllSschulaemter();
        String[] allAusbaustatusArray = schuleRepository.getAllAusbaustatus();

        String[] finalStaedteArray = new String[staedteArray.length +1];
        String[] finalSchulaemterArray = new String[allSschulaemterArray.length +1];
        String[] finalAusbaustatusArray = new String[allAusbaustatusArray.length +1];
        for(int i=0;i < staedteArray.length;i++)
            finalStaedteArray[i+1] = staedteArray[i];
        for(int i=0;i < allSschulaemterArray.length;i++)
            finalSchulaemterArray[i+1] = allSschulaemterArray[i];
        for(int i=0;i < allAusbaustatusArray.length;i++)
            finalAusbaustatusArray[i+1] = allAusbaustatusArray[i];
        finalSchulaemterArray[0] = "-";
        finalStaedteArray[0] = "-";
        finalAusbaustatusArray[0] = "alle";


        comboBoxSchulaemter = new JComboBox<>(finalSchulaemterArray);
        comboBoxOrte = new JComboBox<>(finalStaedteArray);
        comboBoxAusbaustatus = new JComboBox<>(finalAusbaustatusArray);
    }
}
