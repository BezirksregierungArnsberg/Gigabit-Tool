package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private Schule schule;
    private Schule hauptstandort;

    public AddTeilstandortFrame() {
        SchuleRepository schuleRepository = new SchuleRepository();

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schule.setName_der_Schule(textFieldNameDerSchule.getText());
                schule.setArt_der_Schule(textFieldArtDerSchule.getText());
                schule.setOrt(textFieldOrt.getText());
                schule.setStrasse_Hsnr(textFieldStrasseUndHausummer.getText());
                schule.setBemerkungen(textAreaBemerkungen.getText());
                schule.setVorwahl(textFieldVorwahl.getText());
                schule.setMailadresse(textFieldMailadresse.getText());
                schule.setRufnummer(textFieldRufnummer.getText());
                schule.setPLZ(Integer.parseInt(textFieldPLZ.getText()));
                schule.setAnbindung_Kbit_UL(Integer.parseInt(textFieldUpload.getText()));
                schule.setAnbindung_Kbit_DL(Integer.parseInt(textFieldDownload.getText()));
                schule.setLat(Double.parseDouble(textFieldLatitude.getText()));
                schule.setLng(Double.parseDouble(textFieldLongitude.getText()));

                schule.setSNR(AddTeilstandortFrame.this.hauptstandort.getSNR());
                schule.setZustaendiges_Schulamt(AddTeilstandortFrame.this.hauptstandort.getZustaendiges_Schulamt());
                schule.setSF(AddTeilstandortFrame.this.hauptstandort.getSF());
                schule.setSchultyp(AddTeilstandortFrame.this.hauptstandort.getSchultyp());
                schuleRepository.save(schule);
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
        if(schule != null) {
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
        }else
        {
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
        }
    }
}
