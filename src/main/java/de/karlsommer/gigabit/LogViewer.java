package de.karlsommer.gigabit;

import de.karlsommer.gigabit.database.model.LogEntry;
import de.karlsommer.gigabit.database.repositories.LogEntryRepository;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class LogViewer {
    private JTable tableLogging;
    public JPanel mainView;
    private JButton refreshButton;
    private JFrame frame;

    public static final String ausgabeSpalten[] = {"id","Zeitpunkt","Nachricht"};

    public LogViewer(JFrame frame) {
        this.frame = frame;

        updateView();
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateView();
            }
        });
    }

    private void updateView()
    {
        LogEntryRepository logEntryRepository = new LogEntryRepository();
        ArrayList<LogEntry> logEntries = logEntryRepository.getAll();

        if(logEntries.size() > 0) {
            DefaultTableModel tableModel = new DefaultTableModel(ausgabeSpalten, 0);
            for (LogEntry entry : logEntries) {
                tableModel.addRow(entry.getVector());
            }
            tableLogging.setAutoCreateRowSorter(true);
            tableLogging.setModel(tableModel);
        }
    }
}
