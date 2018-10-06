package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.MDBConnector;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import de.karlsommer.gigabit.filehandling.ImportBuilder;
import de.karlsommer.gigabit.filehandling.JavascriptWriter;
import de.karlsommer.gigabit.geocoding.GoogleGeoUtils;
import de.karlsommer.gigabit.database.model.Schule;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static de.karlsommer.gigabit.database.model.Schule.ausgabeSpalten;
import static de.karlsommer.gigabit.database.model.Schule.fehlendeSchulenSpalten;
import static org.apache.commons.lang.StringUtils.trim;

public class Interface {
    private JButton memasysEinlesenUndBedarfeAktualisieren;
    private JPanel mainView;
    private JButton geokoordinatenLaden;
    private JButton javascriptUndHTMLGigabitSchreiben;
    private JButton compareToMemasys;
    private JLabel ausgabeLabel;
    private JButton goButton;
    private JButton toDoButton1;
    private JButton toDoButton3;
    private JButton toDoButton2;
    private JButton toDoButton4;
    private JButton toDoButton5;
    private JButton toDoButton6;
    private JButton toDoButton;
    private JButton showAndLoadSchoolsWithoutGeodata;
    private JButton toDoButton8;
    private JButton breitbandCSVDateiEinlesen;
    private JTable ausgabeTabelle;
    private JButton toDoButton7;
    private JButton goButton1;
    private String filename = "";
    private GoogleGeoUtils geoUtils;
    private ImportBuilder builder;
    private SchuleRepository schuleRepository;
    private ArrayList<Schule> schulenImZwischenspeicher;

    private Interface()
    {
        schuleRepository = new SchuleRepository();
        builder = new ImportBuilder();

        memasysEinlesenUndBedarfeAktualisieren.addActionListener(new ActionListener() {
            /**
             * Memasys-Tabelle einlesen und Bedarfe aktualisieren.
             * Die Memasys-Tabelle muss im .csv-Format vorliegen und überschreibt alle Bedarfe in der Datenbank
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser c = new JFileChooser();
                // Demonstrate "Open" dialog:
                int rVal = c.showOpenDialog(mainView);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    //Hier Änderung für Dateiauswahl ohne Dialog
                        /*
                        filename = "/Users/karl/ownCloud/ADV/temp.csv";
                        */
                    filename = c.getCurrentDirectory().toString() + File.separator + c.getSelectedFile().getName();
                    ausgabeLabel.setText("done");
                    if (!builder.ladeMemasysDaten(filename)) {
                        ausgabeLabel.setText("FEHLER");
                        ausgabeLabel.setVisible(true);
                    }
                    else
                    {
                        ausgabeLabel.setText("GEFUNDEN");
                        ausgabeLabel.setVisible(true);

                        ArrayList<Schule> eingeleseneDaten = builder.holeSchuldatenAusMemasysDatei();


                        String col[] = {"Interne Nummer","SNR","Name der Schule","Art der Schule","PLZ","Ort"};//,"Stra�e + Hsnr.","Zust�ndiges Schulamt","Vorwahl","Rufnummer","SF","Schultyp","Mailadresse","FB","Zust�ndig","Bedarf S1","Status S1","Moderator S1","Datum S1","Bedarf S2","Status S2","Moderator S2","Datum S2","Bedarf S3","Status S3","Moderator S3","Datum S3","Bedarf S4","Status S4","Moderator S4","Datum S4","Bedarf R1","Status R1","Moderator R1","Datum R1","Bedarf R2","Status R2","Moderator R2","Datum R2","Bedarf L1","Status L1","Moderator L1","Datum L1","Bedarf K1","Status K1","Moderator K1","Datum K1","Bedarf K2","Status K2","Moderator K2","Datum K2","Bedarf A1","Status A1","Moderator A1","Datum A1","Bedarf X2","Status X2","Moderator X2","Datum X2","Bedarf X3","Status X3","Moderator X3","Datum X3","Bemerkungen"};

                        DefaultTableModel tableModel = new DefaultTableModel(col,0);

                        for(Schule schule: eingeleseneDaten)
                        {
                            if(schuleRepository.schoolWithSNRExists(schule.getSNR()))
                            {
                                schuleRepository.saveSchoolBedarfe(schule);
                            }
                        }
                        ausgabeLabel.setText("Schulbedarfe aktualisiert");

                    }
                }
                else if (rVal == JFileChooser.CANCEL_OPTION) {
                    ausgabeLabel.setText("Bitte vor Abgleich auswählen");
                }
            }
        });
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
        compareToMemasys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                filename = "/Users/karl/NetBeansProjects/JSIandGMC/Breitband_Schulen.csv";
                if (!builder.ladeBreitbandDaten(filename)) {
                    ausgabeLabel.setText("FEHLER");
                    ausgabeLabel.setVisible(true);
                }
                else
                {
                    ausgabeLabel.setText("GEFUNDEN");
                    ausgabeLabel.setVisible(true);

                    //String  col[] ={"Bezeichnung","Straße und Hausnummer","Ort","Postleitzahl","Auskunft erteilt (Ansprechpartner)","Telefonnummer Ansprechpartner","E-Mail-Adresse Ansprechpartner","Schulbezeichnung","Straße u. Hausnummer","Ort3","Postleitzahl4","Schul-ID(Schulnummer)"};

                    ArrayList<ArrayList<String>> eingeleseneDaten = builder.holeSchuldatenAusBreitbandDatei();
                    schuleRepository.resetFlags();
                    for (ArrayList<String> data:eingeleseneDaten)
                    {
                        if(data.size() > 11)
                        {
                            data.set(11,trim(data.get(11)).replaceAll(" ",""));
                            if(StringUtils.isNumeric(data.get(11)) && data.get(11).length()==6) {
                                schuleRepository.flagSchool(data.get(11));
                            }
                        }
                    }
                    ArrayList<Schule> nichtGefundeneSchulen = schuleRepository.getAllUnflagedSchools();
                    showSchooldataInTable(ausgabeSpalten,nichtGefundeneSchulen,"Fehlende Schulen", true);

                    //Ausgabe in Datei schreiben
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Fehlende_Schulen.txt"), "UTF-8"));

                        //Spaltenbezeichner
                        for (int i = 0; i < fehlendeSchulenSpalten.length; i++) {
                            writer.write(fehlendeSchulenSpalten[i]);
                            if (i != fehlendeSchulenSpalten.length-1) {
                                writer.write(";");
                            }
                        }
                        writer.newLine();

                        //Daten
                        for (Schule schule: nichtGefundeneSchulen) {
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
            //else if (rVal == JFileChooser.CANCEL_OPTION) {
            //    ausgabeLabel.setText("Bitte vor Abgleich auswählen");
            // }
            //}
        });
        toDoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ArrayList<String>> itNRWSchulen = MDBConnector.getInstance().getAllSchools();
                schuleRepository.resetFlags();
                int i = 0;
                for (ArrayList<String> school:itNRWSchulen)
                {
                    if(!schuleRepository.schoolWithSNRExists(Integer.parseInt(school.get(0))))
                    {
                        System.out.println(++i+":Schule mit Schulnummer:"+school.get(0)+" fehlt. Adding...");
                        for (int j = 0; j < MDBConnector.columnsFromDatabase.size();j++)
                        {
                            System.out.print(school.get(j)+";");
                        }
                        Schule schule = new Schule();
                        schule.setSNR(Integer.parseInt(school.get(0)));
                        schule.setName_der_Schule(school.get(2));
                        schule.setArt_der_Schule(school.get(3));
                        schule.setPLZ(Integer.parseInt(school.get(5)));
                        schule.setOrt(school.get(6));
                        schule.setStrasse_Hsnr(school.get(7));
                        schule.setVorwahl(school.get(8));
                        schule.setRufnummer(school.get(9));
                        //schule.set
                        //schuleRepository.save(schule);
                    }
                    else
                    {
                        schuleRepository.flagSchool(school.get(0));
                    }
                }
                ArrayList<Schule> nichtGefundeneSchulen = schuleRepository.getAllUnflagedSchools();
                showSchooldataInTable(ausgabeSpalten,nichtGefundeneSchulen,"Schulen in der Datenbank, die nicht in der IT-NRW-Datenbank vorhanden sind",true);
            }
        });
        goButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Schule> schulen = schuleRepository.getSchulenMitGleichenGeolocations();
                for(Schule schule:schulen)
                {
                    schule.setLat(schule.getLat()+0.0002);
                    schule.setLng(schule.getLng()+0.0002);
                    schuleRepository.save(schule);
                    System.out.println("Schule mit SNR:"+schule.getSNR()+" und Name:"+schule.getName_der_Schule()+" bereinigt.");
                }
            }
        });
        toDoButton7.addActionListener(new ActionListener() {
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
                filename = "/Users/karl/NetBeansProjects/JSIandGMC/Breitband_Schulen.csv";
                if (!builder.ladeBreitbandDaten(filename)) {
                    ausgabeLabel.setText("FEHLER");
                    ausgabeLabel.setVisible(true);
                }
                else
                {
                    ausgabeLabel.setText("GEFUNDEN");
                    ausgabeLabel.setVisible(true);

                    //String  col[] ={"Bezeichnung","Straße und Hausnummer","Ort","Postleitzahl","Auskunft erteilt (Ansprechpartner)","Telefonnummer Ansprechpartner","E-Mail-Adresse Ansprechpartner","Schulbezeichnung","Straße u. Hausnummer","Ort3","Postleitzahl4","Schul-ID(Schulnummer)"};

                    ArrayList<ArrayList<String>> eingeleseneDaten = builder.holeSchuldatenAusBreitbandDatei();

                    ArrayList<Schule> anzuzeigendeSchulen = new ArrayList<>();


                    for (ArrayList<String> data:eingeleseneDaten)
                    {
                        if(data.size() > 11)
                        {
                            data.set(11,trim(data.get(Schule.GIGABIT_TABELLE_SNR)).replaceAll(" ",""));
                            if(StringUtils.isNumeric(data.get(Schule.GIGABIT_TABELLE_SNR)) && data.get(Schule.GIGABIT_TABELLE_SNR).length()==6) {
                                if(schuleRepository.schoolWithSNRExists(Integer.parseInt(data.get(Schule.GIGABIT_TABELLE_SNR)))) {
                                    Schule toUpdate = schuleRepository.getSchool(data.get(Schule.GIGABIT_TABELLE_SNR));
                                    if (toUpdate != null) {
                                        System.out.println("Updating Schule:"+toUpdate.getSNR());
                                        toUpdate.updateBreitbandData(data);
                                        schuleRepository.save(toUpdate);
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

    private void showSchooldataInTable(String[] col, ArrayList<Schule> schulen, String ausgabe, boolean fehlend) {
        DefaultTableModel tableModel = new DefaultTableModel(col,0);
        for (Schule schule : schulen) {
            if(fehlend)
                tableModel.addRow(schule.getfehlendVector());
            else
                tableModel.addRow(schule.getVector());
        }
        Interface.this.ausgabeTabelle.setModel(tableModel);
        ausgabeLabel.setText(ausgabe);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manager");
        frame.setContentPane(new Interface().mainView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
