package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import de.karlsommer.gigabit.helper.DataUpdater;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static de.karlsommer.gigabit.EditSchoolFrame.ausbauArray;
import static de.karlsommer.gigabit.database.model.Schule.AUSBAU_UNGEKLAERT;
import static de.karlsommer.gigabit.database.model.Schule.TEILSTANDORT;

public class AddTeilstandortFrame {
    public JPanel mainView;
    private JButton speichernButton;
    private JTextField textFieldNameDerSchule;
    private JTextField textFieldPLZ;
    private JTextField textFieldOrt;
    private JTextField textFieldStrasseUndHausummer;
    private JTextField textFieldUpload;
    private JTextField textFieldDownload;
    private JTextArea textAreaBemerkungen;
    private JTextField textFieldLatitude;
    private JTextField textFieldLongitude;
    private JTextField textFieldMailadresse;
    private JTextField textFieldVorwahl;
    private JTextField textFieldRufnummer;
    private JLabel jLabelID;
    private JLabel jLabelSchulnummer;
    private JLabel jLabelSchultyp;
    private JLabel jLabelSchulform;
    private JLabel jLAbelSchulamt;
    private JTextField textFieldArtDerSchule;
    private JButton deleteButton;
    private JLabel jLabelAnsprechpartner;
    private JLabel jLabelTelefonAnsprechpartner;
    private JLabel jLabelEmailAnsprechpartner;
    private JTextField textFieldStatusGB;
    private JTextField textFieldStatusMK;
    private JTextField textFieldStatusInhouse;
    private JComboBox comboBoxAusbaustatus;
    private JTextField textFieldPWCUpload;
    private JTextField textFieldPWCDownload;
    private JTextField textFieldKlassenanzahl;
    private JTextField textFieldSchuelerzahl;
    private JTextField textFieldSchuelerzahlIT;
    private JLabel labelSchultraeger;
    private Schule schule;
    private Schule hauptstandort;
    private SchuleRepository schuleRepository;
    private JFrame frame;
    private DataUpdater dataUpdater;

    public AddTeilstandortFrame(JFrame frame, DataUpdater dataUpdater) {
        this.frame = frame;
        this.dataUpdater = dataUpdater;
        schuleRepository = new SchuleRepository();

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(schule == null) {
                    schule = new Schule();
                    schule.setId(schuleRepository.getMaxID()+1);
                }
                schule.setName_der_Schule(textFieldNameDerSchule.getText());
                schule.setArt_der_Schule(textFieldArtDerSchule.getText());
                schule.setOrt(textFieldOrt.getText());
                schule.setSchultraeger(hauptstandort.getSchultraeger());
                schule.setStrasse_Hsnr(textFieldStrasseUndHausummer.getText());
                schule.setBemerkungen(textAreaBemerkungen.getText());
                schule.setVorwahl(textFieldVorwahl.getText());
                schule.setMailadresse(textFieldMailadresse.getText());
                schule.setRufnummer(textFieldRufnummer.getText());
                schule.setFlag(false);

                if(!textFieldPLZ.getText().equals("") && StringUtils.isNumeric(textFieldPLZ.getText()))
                    schule.setPLZ(Integer.parseInt(textFieldPLZ.getText()));
                else
                    schule.setPLZ(0);
                if(!textFieldUpload.getText().equals("") && StringUtils.isNumeric(textFieldUpload.getText()))
                    schule.setAnbindung_Kbit_UL(Integer.parseInt(textFieldUpload.getText()));
                else
                    schule.setAnbindung_Kbit_UL(0);
                if(!textFieldDownload.getText().equals("") && StringUtils.isNumeric(textFieldDownload.getText()))
                    schule.setAnbindung_Kbit_DL(Integer.parseInt(textFieldDownload.getText()));
                else
                    schule.setAnbindung_Kbit_DL(0);
                if(!textFieldLatitude.getText().equals("") && StringUtils.isNumeric(textFieldLatitude.getText()))
                    schule.setLat(Double.parseDouble(textFieldLatitude.getText()));
                else
                    schule.setLat(0);
                if(!textFieldLongitude.getText().equals("") && StringUtils.isNumeric(textFieldLongitude.getText()))
                    schule.setLng(Double.parseDouble(textFieldLongitude.getText()));
                else
                    schule.setLng(0);
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
                if(!textFieldSchuelerzahl.getText().equals("") && StringUtils.isNumeric(textFieldSchuelerzahl.getText()))
                    schule.setSchuelerzahl(Integer.parseInt(textFieldSchuelerzahl.getText()));
                else
                    schule.setSchuelerzahl(0);
                if(!textFieldSchuelerzahlIT.getText().equals("") && StringUtils.isNumeric(textFieldSchuelerzahlIT.getText()))
                    schule.setSchuelerzahlIT(Integer.parseInt(textFieldSchuelerzahlIT.getText()));
                else
                    schule.setSchuelerzahlIT(0);

                schule.setSNR(AddTeilstandortFrame.this.hauptstandort.getSNR());
                schule.setZustaendiges_Schulamt(AddTeilstandortFrame.this.hauptstandort.getZustaendiges_Schulamt());
                schule.setSF(AddTeilstandortFrame.this.hauptstandort.getSF());
                schule.setSchultyp(AddTeilstandortFrame.this.hauptstandort.getSchultyp());
                schule.setStandort(TEILSTANDORT);
                schule.setAusbau((String) comboBoxAusbaustatus.getSelectedItem());

                schuleRepository.save(schule);

                AddTeilstandortFrame.this.dataUpdater.updateData();
                AddTeilstandortFrame.this.frame.setVisible(false);
                AddTeilstandortFrame.this.frame.dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(schule != null)
                    schuleRepository.deleteSchoolWithID(String.valueOf(schule.getId()));

                dataUpdater.updateData();
                AddTeilstandortFrame.this.frame.setVisible(false);
                AddTeilstandortFrame.this.frame.dispose();
            }
        });
    }
    public void setSchule(Schule schule, Schule hauptstandort)
    {
        this.hauptstandort = hauptstandort;
        this.schule = schule;
        jLabelSchulnummer.setText(String.valueOf(this.hauptstandort.getSNR()));
        jLAbelSchulamt.setText(this.hauptstandort.getZustaendiges_Schulamt());
        jLabelSchulform.setText(this.hauptstandort.getSF());
        jLabelSchultyp.setText(this.hauptstandort.getSchultyp());
        labelSchultraeger.setText(this.hauptstandort.getSchultraeger());
        jLabelAnsprechpartner.setText(this.hauptstandort.getAnsprechpartner());
        jLabelEmailAnsprechpartner.setText(this.hauptstandort.getEmail_Ansprechpartner());
        jLabelTelefonAnsprechpartner.setText(this.hauptstandort.getTelefon_Ansprechpartner());
        if(schule != null) {
            jLabelID.setText(String.valueOf(schule.getId()));
            textFieldNameDerSchule.setText(schule.getName_der_Schule());
            textFieldPLZ.setText(String.valueOf(schule.getPLZ()));
            textFieldArtDerSchule.setText(schule.getArt_der_Schule());
            textFieldOrt.setText(schule.getOrt());
            textFieldStrasseUndHausummer.setText(schule.getStrasse_Hsnr());
            textFieldUpload.setText(String.valueOf(schule.getAnbindung_Kbit_UL()));
            textFieldDownload.setText(String.valueOf(schule.getAnbindung_Kbit_DL()));
            textAreaBemerkungen.setText(schule.getBemerkungen());
            textFieldLatitude.setText(String.valueOf(schule.getLat()));
            textFieldLongitude.setText(String.valueOf(schule.getLng()));
            textFieldMailadresse.setText(schule.getMailadresse());
            textFieldVorwahl.setText("0" + schule.getVorwahl());
            textFieldRufnummer.setText(schule.getRufnummer());
            textFieldPWCUpload.setText(String.valueOf(schule.getPWCUpload()));
            textFieldPWCDownload.setText(String.valueOf(schule.getPWCDownload()));
            textFieldSchuelerzahl.setText(String.valueOf(schule.getSchuelerzahl()));
            textFieldKlassenanzahl.setText(String.valueOf(schule.getKlassenanzahl()));
            textFieldSchuelerzahlIT.setText(String.valueOf(schule.getSchuelerzahlIT()));

            if((Arrays.asList(ausbauArray)).contains(schule.getAusbau(false)))
                comboBoxAusbaustatus.setSelectedIndex((Arrays.asList(ausbauArray)).indexOf(schule.getAusbau(false)));
            else
                comboBoxAusbaustatus.setSelectedIndex((Arrays.asList(ausbauArray)).indexOf(AUSBAU_UNGEKLAERT));
        }else
        {
            jLabelID.setText(String.valueOf(schuleRepository.getMaxID()+1));
            textFieldNameDerSchule.setText("");
            textFieldArtDerSchule.setText("");
            textFieldPLZ.setText("");
            textFieldOrt.setText("");
            textFieldStrasseUndHausummer.setText("");
            textFieldUpload.setText("");
            textFieldDownload.setText("");
            textAreaBemerkungen.setText("");
            textFieldLatitude.setText("");
            textFieldLongitude.setText("");
            textFieldMailadresse.setText("");
            textFieldVorwahl.setText("");
            textFieldRufnummer.setText("");
            textFieldPWCUpload.setText("");
            textFieldPWCDownload.setText("");
            textFieldKlassenanzahl.setText("");
            textFieldSchuelerzahl.setText("");
            textFieldSchuelerzahlIT.setText("");
            comboBoxAusbaustatus.setSelectedIndex((Arrays.asList(ausbauArray)).indexOf(AUSBAU_UNGEKLAERT));
        }
    }

    private void createUIComponents() {
        comboBoxAusbaustatus = new JComboBox<>(ausbauArray);
    }
}
