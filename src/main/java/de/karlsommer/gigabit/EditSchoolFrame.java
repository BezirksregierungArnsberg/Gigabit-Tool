package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static de.karlsommer.gigabit.database.model.Schule.HAUPTSTANDORT;

public class EditSchoolFrame{
    public JPanel mainView;
    private JList list1;
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
    private Schule schule;
    private AddTeilstandortFrame addTeilstandortFrame;
    private JFrame teilstandortBearbeitenFrame;
    private SchuleRepository schuleRepository;

    public EditSchoolFrame() {
        schuleRepository = new SchuleRepository();

        teilstandortBearbeitenFrame = new JFrame("Schulen bearbeiten");
        addTeilstandortFrame = new AddTeilstandortFrame();
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
                schule.setLat(Double.parseDouble(textFieldLatitude.getText()));
                schule.setLng(Double.parseDouble(textFieldLongitude.getText()));
                schuleRepository.save(schule);
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
    }
}
