package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JLabel jLabelSchuelerzahl;
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

                schule.setSNR(AddTeilstandortFrame.this.hauptstandort.getSNR());
                schule.setZustaendiges_Schulamt(AddTeilstandortFrame.this.hauptstandort.getZustaendiges_Schulamt());
                schule.setSF(AddTeilstandortFrame.this.hauptstandort.getSF());
                schule.setSchultyp(AddTeilstandortFrame.this.hauptstandort.getSchultyp());
                schule.setStandort(TEILSTANDORT);
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
        jLabelAnsprechpartner.setText(this.hauptstandort.getAnsprechpartner());
        jLabelEmailAnsprechpartner.setText(this.hauptstandort.getEmail_Ansprechpartner());
        jLabelTelefonAnsprechpartner.setText(this.hauptstandort.getTelefon_Ansprechpartner());
        jLabelSchuelerzahl.setText(String.valueOf(this.hauptstandort.getSchuelerzahl()));
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
        }
    }
}
