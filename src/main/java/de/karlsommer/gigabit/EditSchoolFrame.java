package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import de.karlsommer.gigabit.helper.DataUpdater;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import static de.karlsommer.gigabit.database.model.Schule.*;

public class EditSchoolFrame implements DataUpdater {
    public JPanel mainView;
    private JButton teilstandortHinzufügenButton;
    private JButton speichernButton;
    private JTextField textFieldSchulnummer;
    private JTextField textFieldNameDerSchule;
    private JTextField textFieldArtDerSchule;
    private JTextField textFieldPLZ;
    private JTextField textFieldOrt;
    private JTextField textFieldStrasseUndHausummer;
    private JTextField textFieldZustaendigesSchulamt;
    private JTextField textFieldSchulform;
    private JTextField textFieldUpload;
    private JTextField textFieldDownload;
    private JTextArea textAreaBemerkungen;
    private JTextField textFieldLatitude;
    private JTextField textFieldLongitude;
    private JTextField textFieldSchultyp;
    private JTextField textFieldMailadresse;
    private JTextField textFieldVorwahl;
    private JTextField textFieldRufnummer;
    private JTextField textFieldStatusGB;
    private JTextField textFieldStatusMK;
    private JTextField textFieldStatusInhouse;
    private JLabel textLabelFlag;
    private JLabel jLabelID;
    private JTable ausgabeTabelle;
    private JTextField textFieldAnsprechpartner;
    private JTextField textFieldTelefonAnsprechpartner;
    private JTextField textFieldEmailAnsprechpartner;
    private JTextField textFieldSchuelerzahl;
    private JComboBox comboBoxAusbaustatus;
    private JTextField textFieldKlassenanzahl;
    private JTextField textFieldPWCUpload;
    private JTextField textFieldPWCDownload;
    private JTextField textFieldSchuelerzahlIT;
    private JTextField textFieldSchultraeger;
    private Schule schule;
    private AddTeilstandortFrame addTeilstandortFrame;
    private JFrame teilstandortBearbeitenFrame;
    private SchuleRepository schuleRepository;
    private JFrame frame;
    private DataUpdater dataUpdater;
    public static final String[] ausbauArray = new String[] { AUSBAU_AUSGEBAUT, AUSBAU_EIGENWIRTSCHAFTLICH,AUSBAU_BUND,AUSBAU_BUND_1,AUSBAU_BUND_2,AUSBAU_BUND_3,AUSBAU_BUND_4,AUSBAU_BUND_5,AUSBAU_BUND_6,AUSBAU_BUND_SONDER,AUSBAU_LAND,AUSBAU_UNGEKLAERT, AUSBAU_ERMTTELT_BUND, AUSBAU_ERMTTELT_LAND };

    private void createUIComponents() {
        comboBoxAusbaustatus = new JComboBox<>(ausbauArray);
    }

    public EditSchoolFrame(JFrame frame, DataUpdater dataUpdater) {
        this.frame = frame;
        this.dataUpdater = dataUpdater;
        schuleRepository = new SchuleRepository();

        teilstandortBearbeitenFrame = new JFrame("Teilstandort bearbeiten");
        addTeilstandortFrame = new AddTeilstandortFrame(teilstandortBearbeitenFrame, this);
        teilstandortBearbeitenFrame.setContentPane(addTeilstandortFrame.mainView);
        teilstandortBearbeitenFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        teilstandortBearbeitenFrame.addWindowListener(new WindowAdapter() {
            //I skipped unused callbacks for readability

            @Override
            public void windowClosing(WindowEvent e) {
                teilstandortBearbeitenFrame.setVisible(false);
                teilstandortBearbeitenFrame.dispose();
            }
        });

        teilstandortBearbeitenFrame.pack();

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                schule.setName_der_Schule(textFieldNameDerSchule.getText());
                schule.setArt_der_Schule(textFieldArtDerSchule.getText());
                schule.setOrt(textFieldOrt.getText());
                schule.setStrasse_Hsnr(textFieldStrasseUndHausummer.getText());
                schule.setZustaendiges_Schulamt(textFieldZustaendigesSchulamt.getText());
                schule.setSF(textFieldSchulform.getText());
                schule.setBemerkungen(textAreaBemerkungen.getText());
                schule.setSchultyp(textFieldSchultyp.getText());
                schule.setVorwahl(textFieldVorwahl.getText());
                schule.setMailadresse(textFieldMailadresse.getText());
                schule.setRufnummer(textFieldRufnummer.getText());
                schule.setStatus_GB(textFieldStatusGB.getText());
                schule.setStatus_MK(textFieldStatusMK.getText());
                schule.setStatus_Inhouse(textFieldStatusInhouse.getText());
                schule.setSNR(Integer.parseInt(textFieldSchulnummer.getText()));
                schule.setPLZ(Integer.parseInt(textFieldPLZ.getText()));
                schule.setAnbindung_Kbit_UL(Integer.parseInt(textFieldUpload.getText()));
                schule.setAnbindung_Kbit_DL(Integer.parseInt(textFieldDownload.getText()));

                if(!textFieldLatitude.getText().equals(""))
                    schule.setLat(Double.parseDouble(textFieldLatitude.getText()));
                else
                    schule.setLat(0);
                if(!textFieldLongitude.getText().equals(""))
                    schule.setLng(Double.parseDouble(textFieldLongitude.getText()));
                else
                    schule.setLng(0);
                schule.setSchuelerzahl(Integer.parseInt(textFieldSchuelerzahl.getText()));
                schule.setAnsprechpartner(textFieldAnsprechpartner.getText());
                schule.setEmail_Ansprechpartner(textFieldEmailAnsprechpartner.getText());
                schule.setTelefon_Ansprechpartner(textFieldTelefonAnsprechpartner.getText());

                if(!textFieldPWCUpload.getText().equals("") && StringUtils.isNumeric(textFieldPWCUpload.getText()))
                    schule.setPWCUpload(Integer.parseInt(textFieldPWCUpload.getText()));
                else
                    schule.setPWCUpload(0);
                if(!textFieldPWCDownload.getText().equals("") && StringUtils.isNumeric(textFieldPWCDownload.getText()))
                    schule.setPWCDownload(Integer.parseInt(textFieldPWCDownload.getText()));
                else
                    schule.setPWCDownload(0);
                if(!textFieldKlassenanzahl.getText().equals("") && StringUtils.isNumeric(textFieldKlassenanzahl.getText()))
                    schule.setKlassenanzahl(Integer.parseInt(textFieldKlassenanzahl.getText()));
                else
                    schule.setKlassenanzahl(0);
                if(!textFieldSchuelerzahlIT.getText().equals("") && StringUtils.isNumeric(textFieldSchuelerzahlIT.getText()))
                    schule.setSchuelerzahlIT(Integer.parseInt(textFieldSchuelerzahlIT.getText()));
                else
                    schule.setSchuelerzahlIT(0);

                schule.setAusbau((String) comboBoxAusbaustatus.getSelectedItem());
                schule.setSchultraeger(textFieldSchultraeger.getText());
                schuleRepository.save(schule);

                EditSchoolFrame.this.dataUpdater.updateData();

                EditSchoolFrame.this.frame.setVisible(false);
                EditSchoolFrame.this.frame.dispose();
            }
        });
        teilstandortHinzufügenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTeilstandortFrame.setSchule(null, EditSchoolFrame.this.schule);
                teilstandortBearbeitenFrame.setVisible(true);
            }
        });
    }

    public void setSchule(Schule schule)
    {
        if(!schule.getStandort().equals(HAUPTSTANDORT))
            this.schule = schuleRepository.getSchoolWithSNR(String.valueOf(schule.getSNR()));
        this.schule = schule;
        updateData();
    }

    @Override
    public void updateData() {
        jLabelID.setText(String.valueOf(this.schule.getId()));
        textFieldSchulnummer.setText(String.valueOf(this.schule.getSNR()));
        textFieldNameDerSchule.setText(this.schule.getName_der_Schule());
        textFieldArtDerSchule.setText(this.schule.getArt_der_Schule());
        textFieldPLZ.setText(String.valueOf(this.schule.getPLZ()));
        textFieldOrt.setText(this.schule.getOrt());
        textFieldStrasseUndHausummer.setText(this.schule.getStrasse_Hsnr());
        textFieldZustaendigesSchulamt.setText(this.schule.getZustaendiges_Schulamt());
        textFieldSchulform.setText(this.schule.getSF());
        textFieldUpload.setText(String.valueOf(this.schule.getAnbindung_Kbit_UL()));
        textFieldDownload.setText(String.valueOf(this.schule.getAnbindung_Kbit_DL()));
        textAreaBemerkungen.setText(this.schule.getBemerkungen());
        textFieldLatitude.setText(String.valueOf(this.schule.getLat()));
        textFieldLongitude.setText(String.valueOf(this.schule.getLng()));
        textFieldSchultyp.setText(this.schule.getSchultyp());
        textFieldMailadresse.setText(this.schule.getMailadresse());
        textFieldVorwahl.setText("0"+this.schule.getVorwahl());
        textFieldRufnummer.setText(this.schule.getRufnummer());
        textLabelFlag.setText(this.schule.isFlag()?"flaged":"unflaged");
        textFieldStatusGB.setText(this.schule.getStatus_GB());
        textFieldStatusMK.setText(this.schule.getStatus_MK());
        textFieldStatusInhouse.setText(this.schule.getStatus_Inhouse());
        textFieldAnsprechpartner.setText(schule.getAnsprechpartner());
        textFieldTelefonAnsprechpartner.setText(schule.getTelefon_Ansprechpartner());
        textFieldEmailAnsprechpartner.setText(schule.getEmail_Ansprechpartner());
        textFieldSchuelerzahl.setText(String.valueOf(schule.getSchuelerzahl()));
        textFieldPWCUpload.setText(String.valueOf(schule.getPWCUpload()));
        textFieldPWCDownload.setText(String.valueOf(schule.getPWCDownload()));
        textFieldKlassenanzahl.setText(String.valueOf(schule.getKlassenanzahl()));
        textFieldSchuelerzahlIT.setText(String.valueOf(schule.getSchuelerzahlIT()));
        textFieldSchultraeger.setText(this.schule.getSchultraeger());

        if((Arrays.asList(ausbauArray)).contains(schule.getAusbau(false)))
            comboBoxAusbaustatus.setSelectedIndex((Arrays.asList(ausbauArray)).indexOf(schule.getAusbau(false)));
        else
            comboBoxAusbaustatus.setSelectedIndex((Arrays.asList(ausbauArray)).indexOf(AUSBAU_UNGEKLAERT));


        if(schuleRepository.getTeilstandorteZu(String.valueOf(schule.getSNR())).size() > 0)
            showTeilstandorteInTable();
        else {
            DefaultTableModel model = (DefaultTableModel) ausgabeTabelle.getModel();
            model.setRowCount(0);
        }
    }



    private void showTeilstandorteInTable() {
        DefaultTableModel tableModel = new DefaultTableModel(ausgabeSpalten,0);
        for (Schule teilstandOrt : schuleRepository.getTeilstandorteZu(String.valueOf(schule.getSNR()))) {
            tableModel.addRow(teilstandOrt.getVector());
        }
        ausgabeTabelle.setAutoCreateRowSorter(true);
        ausgabeTabelle.setModel(tableModel);
        ausgabeTabelle.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if(ausgabeTabelle.getSelectedRow()>-1)
                {
                    System.out.println(ausgabeTabelle.getValueAt(ausgabeTabelle.getSelectedRow(), 0).toString());
                    addTeilstandortFrame.setSchule(schuleRepository.getSchoolWithID(ausgabeTabelle.getValueAt(ausgabeTabelle.getSelectedRow(), 0).toString()), schule);
                    teilstandortBearbeitenFrame.setVisible(true);
                }
            }
        });
    }
}
