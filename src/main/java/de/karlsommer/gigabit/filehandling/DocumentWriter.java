package de.karlsommer.gigabit.filehandling;

import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static de.karlsommer.gigabit.helper.MathHelper.round;


public class DocumentWriter {

    private static String[] columns = {"Standorte gesamt", "Grundschulen", "Förderschulen", "Sek I", "Sek II", "Berufskollegs", "Sonstige Schulen","BO","DO","EN","HA","HAM","HER","HSK","MK","OE","SI","SO","UN"};
    private static String[] rows = {"In der Datenbank", "Anschluss erfasst", "ohne Gigabit-Anschluss", "AVG-Download (KBit/s)", "AVG-Upload (KBit/s)", "Bandbreite pro Schüler DL (KBit/s)", "Bandbreite pro Schüler UL (KBit/s)"};
    private static String[] schuleaemter = {"BO","DO","EN","HA","HAM","HER","HSK","MK","OE","SI","SO","UN"};

    public void publishXLSXBericht() throws Exception
    {
        SchuleRepository schuleRepository = new SchuleRepository();

        String[] formenQuery = {"SF='G'", "SF='SO' OR SF='SOBK'", "SF='HS' OR SF='SEK' OR SF='RS' OR SF='PR' OR SF='GMS'", "SF='GE' OR SF='GY'", "SF='BK' OR SF='KO'", "SF='KR' OR SF='WS' OR SF='null'"};

        ArrayList<Integer> gesamt = new ArrayList<>();
        gesamt.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen"));
        for(String query:formenQuery)
        {
            gesamt.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE "+query));
        }
        for(String query: schuleaemter)
        {
            gesamt.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='"+query+"'"));
        }

        ArrayList<Integer> anschluesse = new ArrayList<>();
        anschluesse.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0"));
        for(String query:formenQuery)
        {
            anschluesse.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND "+query));
        }
        for(String query: schuleaemter)
        {
            anschluesse.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='"+query+"'"));
        }

        ArrayList<Integer> ohneGigabit = new ArrayList<>();
        ohneGigabit.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000"));
        for(String query:formenQuery)
        {
            ohneGigabit.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND "+query));
        }
        for(String query: schuleaemter)
        {
            ohneGigabit.add(schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='"+query+"'"));
        }

        ArrayList<Integer> durchschnittDownload = new ArrayList<>();
        durchschnittDownload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0",1))));
        for(String query:formenQuery)
        {
            durchschnittDownload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND "+query,1))));
        }
        for(String query: schuleaemter)
        {
            durchschnittDownload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND [Zuständiges Schulamt]='"+query+"'",1))));
        }

        ArrayList<Integer> durchschnittUpload = new ArrayList<>();
        durchschnittUpload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0",1))));
        for(String query:formenQuery)
        {
            durchschnittUpload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND "+query,1))));
        }
        for(String query: schuleaemter)
        {
            durchschnittUpload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='"+query+"'",1))));
        }



        ArrayList<Integer> bandbreiteProSchuelerDownload = new ArrayList<>();
        bandbreiteProSchuelerDownload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT SUM([Anbindung Kbit DL])/SUM(Schuelerzahl) FROM Schulen WHERE [Anbindung Kbit DL] > 0",1))));
        for(String query:formenQuery)
        {
            bandbreiteProSchuelerDownload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT SUM([Anbindung Kbit DL])/SUM(Schuelerzahl) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND "+query,1))));
        }
        for(String query: schuleaemter)
        {
            bandbreiteProSchuelerDownload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT SUM([Anbindung Kbit DL])/SUM(Schuelerzahl) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND [Zuständiges Schulamt]='"+query+"'",1))));
        }

        ArrayList<Integer> bandbreiteProSchuelerUpload = new ArrayList<>();
        bandbreiteProSchuelerUpload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT SUM([Anbindung Kbit UL])/SUM(Schuelerzahl) FROM Schulen WHERE [Anbindung Kbit UL] > 0",1))));
        for(String query:formenQuery)
        {
            bandbreiteProSchuelerUpload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT SUM([Anbindung Kbit UL])/SUM(Schuelerzahl) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND "+query,1))));
        }
        for(String query: schuleaemter)
        {
            bandbreiteProSchuelerUpload.add(((int)(schuleRepository.getDoubleQueryValue("SELECT SUM([Anbindung Kbit UL])/SUM(Schuelerzahl) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='"+query+"'",1))));
        }

        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Daten");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);


        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell((i*2)+1);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        Row nextRow = sheet.createRow(1);
        for(int i = 0; i < columns.length; i++) {
            Cell cell2 = nextRow.createCell((i*2)+1);
            cell2.setCellValue("Anzahl");
            cell2.setCellStyle(headerCellStyle);
            Cell cell3 = nextRow.createCell((i*2)+2);
            cell3.setCellValue("Prozent");
            cell3.setCellStyle(headerCellStyle);
        }



        int rowNum = 2;
        ArrayList<Row> tableRows = new ArrayList<>();
        for (String rowString: rows) {
            tableRows.add(sheet.createRow(rowNum++));

            Cell temp = tableRows.get(tableRows.size()-1).createCell(0);
            temp.setCellValue(rowString);
            temp.setCellStyle(headerCellStyle);
        }

        for(int i = 0; i < columns.length*2; i++) {
            sheet.autoSizeColumn(i);
        }


        int cellcount = 1;
        for(int number: gesamt)
        {
            tableRows.get(0).createCell(cellcount++).setCellValue(number);
            tableRows.get(0).createCell(cellcount++).setCellValue(round((double)(number*100)/(double)gesamt.get(0),1));
        }
        tableRows.get(0).createCell(1).setCellValue(gesamt.get(0));
        tableRows.get(0).createCell(2).setCellValue("");

        cellcount = 1;
        int num = 0;
        for(int number: anschluesse)
        {
            tableRows.get(1).createCell(cellcount++).setCellValue(number);
            tableRows.get(1).createCell(cellcount++).setCellValue(round((double)(number*100)/(double)gesamt.get(num++),1));
        }

        cellcount = 1;
        num = 0;
        for(int number: ohneGigabit)
        {
            tableRows.get(2).createCell(cellcount++).setCellValue(number);
            tableRows.get(2).createCell(cellcount++).setCellValue(round((double)(number*100)/(double)anschluesse.get(num++),1));
        }

        cellcount = 1;
        num = 0;
        for(int number: durchschnittDownload)
        {
            tableRows.get(3).createCell(cellcount++).setCellValue(number);
            tableRows.get(3).createCell(cellcount++).setCellValue("");
        }

        cellcount = 1;
        num = 0;
        for(int number: durchschnittUpload)
        {
            tableRows.get(4).createCell(cellcount++).setCellValue(number);
            tableRows.get(4).createCell(cellcount++).setCellValue("");
        }

        cellcount = 1;
        num = 0;
        for(int number: bandbreiteProSchuelerDownload)
        {
            tableRows.get(5).createCell(cellcount++).setCellValue(number);
            tableRows.get(5).createCell(cellcount++).setCellValue("");
        }

        cellcount = 1;
        num = 0;
        for(int number: bandbreiteProSchuelerUpload)
        {
            tableRows.get(6).createCell(cellcount++).setCellValue(number);
            tableRows.get(6).createCell(cellcount++).setCellValue("");
        }

        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy") ;
        SimpleDateFormat textDateFormat = new SimpleDateFormat("dd.MM.yyyy") ;

        FileOutputStream fileOut = new FileOutputStream("output/Bericht-"+dateFormat.format(date)+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

    public void publishDocumentBericht() throws Exception
    {
        SchuleRepository schuleRepository = new SchuleRepository();


        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = null;
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy") ;
        SimpleDateFormat textDateFormat = new SimpleDateFormat("dd.MM.yyyy") ;
        try {
            out = new FileOutputStream( new File("output/Bericht-"+dateFormat.format(date)+".docx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        XWPFStyles styles = document.createStyles();

        String heading = "Überschrift";
        String heading2= "Überschrift2";
        String tableHead = "Tabellenkopf";
        String evenCell = "Gerade Tabellenzelle";
        String unevencell = "Ungerade Tabellenzelle";
        String text = "Text";
        addCustomHeadingStyle(document, styles, heading, 1, 68, "4288BC");
        addCustomHeadingStyle(document, styles, heading2, 2, 56, "000000");
        addCustomHeadingStyle(document, styles, tableHead, 3, 32, "4288BC");
        addCustomHeadingStyle(document, styles, evenCell, 4, 28, "000000");
        addCustomHeadingStyle(document, styles, unevencell, 5, 28, "000000");
        addCustomHeadingStyle(document, styles, text, 6,28, "000000");



        int standorteGesamt = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen");
        int grundschulen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SF='G'");
        int foerderschulen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SF='SO' OR SF='SOBK'");
        int sekI = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SF='HS' OR SF='SEK' OR SF='RS' OR SF='PR' OR SF='GMS'");
        int sekII = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SF='GE' OR SF='GY'");
        int bk = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SF='BK' OR SF='KO'");
        int sonstige = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SF='KR' OR SF='WS' OR SF='null'");

        int anschlussGesamt = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0");
        int anschlussGrundschulen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND SF='G'");
        int anschlussFoerderschulen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND (SF='SO' OR SF='SOBK')");
        int anschlussSekI = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND (SF='HS' OR SF='SEK' OR SF='RS' OR SF='PR' OR SF='GMS')");
        int anschlussSekII = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND (SF='GE' OR SF='GY')");
        int anschlussBK = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND (SF='BK' OR SF='KO')");
        int anschlussSonstige = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND (SF='KR' OR SF='WS' OR SF='null')");

        int ohneGigabitGesamt = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000");
        int ohneGigabitGrundschulen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND SF='G'");
        int ohneGigabitFoerderschulen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND (SF='SO' OR SF='SOBK')");
        int ohneGigabitSekI = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND (SF='HS' OR SF='SEK' OR SF='RS' OR SF='PR' OR SF='GMS')");
        int ohneGigabitSekII = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND (SF='GE' OR SF='GY')");
        int ohneGigabitBK = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND (SF='BK' OR SF='KO')");
        int ohneGigabitSonstige = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND (SF='KR' OR SF='WS' OR SF='null')");

        int durchschnittlicheBandbreiteDownLoadGesamt = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0",1)/1024));
        int durchschnittlicheBandbreiteDownLoadGrundschulen = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND SF='G'",1)/1024));
        int durchschnittlicheBandbreiteDownLoadFoerderschulen = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND (SF='SO' OR SF='SOBK')",1)/1024));
        int durchschnittlicheBandbreiteDownLoadSekI = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND (SF='HS' OR SF='SEK' OR SF='RS' OR SF='PR' OR SF='GMS')",1)/1024));
        int durchschnittlicheBandbreiteDownLoadSekII = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND (SF='GE' OR SF='GY')",1)/1024));
        int durchschnittlicheBandbreiteDownLoadBK = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND (SF='BK' OR SF='KO')",1)/1024));
        int durchschnittlicheBandbreiteDownLoadSonstige = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND (SF='KR' OR SF='WS' OR SF='null')",1)/1024));

        int durchschnittlicheBandbreiteUpLoadGesamt = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0",1)/1024));
        int durchschnittlicheBandbreiteUpLoadGrundschulen = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND SF='G'",1)/1024));
        int durchschnittlicheBandbreiteUpLoadFoerderschulen = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND (SF='SO' OR SF='SOBK')",1)/1024));
        int durchschnittlicheBandbreiteUpLoadSekI = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND (SF='HS' OR SF='SEK' OR SF='RS' OR SF='PR' OR SF='GMS')",1)/1024));
        int durchschnittlicheBandbreiteUpLoadSekII = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND (SF='GE' OR SF='GY')",1)/1024));
        int durchschnittlicheBandbreiteUpLoadBK = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND (SF='BK' OR SF='KO')",1)/1024));
        int durchschnittlicheBandbreiteUpLoadSonstige = ((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND (SF='KR' OR SF='WS' OR SF='null')",1)/1024));

        createParagraph(document, heading,"Bericht der Gigabit-Geschäftsstelle vom "+textDateFormat.format(date),ParagraphAlignment.CENTER);
        createParagraph(document, text,"Insgesamt sind "+schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen")+" förderfähige Adresspunkte in der Datenbank erfasst. " +
                "Diese gehören zu "+schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM (SELECT DISTINCT SNR FROM Schulen WHERE SNR > 1)")+" Schulen nach Schulgesetz " +
                "und "+schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE SNR = 0")+" anderen förderfähigen Standorten.");
        createParagraph(document, text,"Von den förderfähigen Adresspunkten existiert von "+schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0")+" Adresspunkten " +
                "eine Rückmeldung über die aktuelle Versorgungslage. Die durchschnittliche Bandbreite der erfassten Adresspunkte liegt " +
                "bei "+((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0",1)/1024))+" MBit/s im Download und " +
                "bei "+((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0",1)/1024))+" MBit/s im Upload." +
                "");
        createParagraph(document, text,"Von den erfassten Anschlüssen verfügen "+round((double)(ohneGigabitGesamt*100)/(double)anschlussGesamt,1)+"% über keinen Gigabit-Anschluss.");
        createParagraph(document, heading2,"Statistik");

        createParagraph(document, text,"Die Statistik der Auswertung ist einmal nach Schulformen und nach Kreisen aufgeteilt.");
        createParagraph(document, text,"\"Anschluss erfasst\" gibt wieder, wie hoch der Anteil der erfassten Anschlüsse bezogen auf die " +
                "ermittelten Gesamtanschlüsse der Schulform oder der Region ist.");
        createParagraph(document, text,"\"Ohne Gigabit-Anschluss\" stellt den Anteil der Schulen dar, von denen der Anschluss erfasst und die aktuelle Geschwindigkeit in der Datenbank vorhanden, dieser aber nicht symmetrisch gigabitfähig, wie in der " +
                "Schulförderrichtlinie gefordert, ist.");
        createParagraph(document, text,"Die Werte \"AVG Download\" und \"AVG Upload\" geben den durchschnittlichen Up- bzw. Download der jeweiligen Schulen an. Hierbei werden nur Schulen berücksichtigt, von denen " +
                "die Anschlussgeschwindigkeit gemeldet ist.");
        createParagraph(document, text,"Der Wert \"Bandbreite pro Schüler\" gibt den rechnerisch ermittelten Wert an, den ein Schüler durchschnittlich an Bandbreite zur Verfügung hat.");

        createParagraph(document, tableHead,"Statistiken nach Schulformen:");

        //create table
        XWPFTable table = document.createTable();
        table.setCellMargins(10, 100, 10, 100);
        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("");
        setCellValue(tableRowOne.addNewTableCell(),"Stand- orte gesamt");
        setCellValue(tableRowOne.addNewTableCell(),"Grund- schulen");
        setCellValue(tableRowOne.addNewTableCell(),"Förder- schulen");
        setCellValue(tableRowOne.addNewTableCell(),"Sek I");
        setCellValue(tableRowOne.addNewTableCell(),"Sek II");
        setCellValue(tableRowOne.addNewTableCell(),"BK");
        setCellValue(tableRowOne.addNewTableCell(),"Sonstige Schulen");
        //create second row
        XWPFTableRow tableRowTwo = table.createRow();
        setCellValue(tableRowTwo.getCell(0), "In der", "Datenbank");
        setCellValue(tableRowTwo.getCell(1), String.valueOf(standorteGesamt));
        setCellValue(tableRowTwo.getCell(2), grundschulen+" /", round((double)(grundschulen*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo.getCell(3), foerderschulen+" /", round((double)(foerderschulen*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo.getCell(4), sekI+" /", round((double)(sekI*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo.getCell(5), sekII+" /", round((double)(sekII*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo.getCell(6), bk+" /", round((double)(bk*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo.getCell(7), sonstige+" /", round((double)(sonstige*100)/(double)standorteGesamt,1)+"%");

        XWPFTableRow tableRowThree = table.createRow();
        setCellValue(tableRowThree.getCell(0), "Anschluss erfasst");
        setCellValue(tableRowThree.getCell(1), String.valueOf(anschlussGesamt)+" /", round((double)(anschlussGesamt*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowThree.getCell(2),String.valueOf(anschlussGrundschulen)+" /",round((double)(anschlussGrundschulen*100)/(double)grundschulen,1)+"%");
        setCellValue(tableRowThree.getCell(3),String.valueOf(anschlussFoerderschulen)+" /",round((double)(anschlussFoerderschulen*100)/(double)foerderschulen,1)+"%");
        setCellValue(tableRowThree.getCell(4),String.valueOf(anschlussSekI)+" /",round((double)(anschlussSekI*100)/(double)sekI,1)+"%");
        setCellValue(tableRowThree.getCell(5),String.valueOf(anschlussSekII)+" /",round((double)(anschlussSekII*100)/(double)sekII,1)+"%");
        setCellValue(tableRowThree.getCell(6),String.valueOf(anschlussBK)+" /",round((double)(anschlussBK*100)/(double)bk,1)+"%");
        setCellValue(tableRowThree.getCell(7),String.valueOf(anschlussSonstige)+" /",round((double)(anschlussSonstige*100)/(double)sonstige,1)+"%");

        XWPFTableRow tableRowFour = table.createRow();
        setCellValue(tableRowFour.getCell(0), "ohne Gigabit-Anschluss");
        setCellValue(tableRowFour.getCell(1), String.valueOf(ohneGigabitGesamt)+" /", round((double)(ohneGigabitGesamt*100)/(double)anschlussGesamt,1)+"%");
        setCellValue(tableRowFour.getCell(2),String.valueOf(ohneGigabitGrundschulen)+" /",round((double)(ohneGigabitGrundschulen*100)/(double)anschlussGrundschulen,1)+"%");
        setCellValue(tableRowFour.getCell(3),String.valueOf(ohneGigabitFoerderschulen)+" /",round((double)(ohneGigabitFoerderschulen*100)/(double)anschlussFoerderschulen,1)+"%");
        setCellValue(tableRowFour.getCell(4),String.valueOf(ohneGigabitSekI)+" /",round((double)(ohneGigabitSekI*100)/(double)anschlussSekI,1)+"%");
        setCellValue(tableRowFour.getCell(5),String.valueOf(ohneGigabitSekII)+" /",round((double)(ohneGigabitSekII*100)/(double)anschlussSekII,1)+"%");
        setCellValue(tableRowFour.getCell(6),String.valueOf(ohneGigabitBK)+" /",round((double)(ohneGigabitBK*100)/(double)anschlussBK,1)+"%");
        setCellValue(tableRowFour.getCell(7),String.valueOf(ohneGigabitSonstige)+" /",round((double)(ohneGigabitSonstige*100)/(double)anschlussSonstige,1)+"%");

        XWPFTableRow tableRowFife = table.createRow();

        setCellValue(tableRowFife.getCell(0), "AVG", "Download");
        setCellValue(tableRowFife.getCell(1),String.valueOf(durchschnittlicheBandbreiteDownLoadGesamt)+" MBit/s");
        setCellValue(tableRowFife.getCell(2),String.valueOf(durchschnittlicheBandbreiteDownLoadGrundschulen)+" MBit/s");
        setCellValue(tableRowFife.getCell(3),String.valueOf(durchschnittlicheBandbreiteDownLoadFoerderschulen)+" MBit/s");
        setCellValue(tableRowFife.getCell(4),String.valueOf(durchschnittlicheBandbreiteDownLoadSekI)+" MBit/s");
        setCellValue(tableRowFife.getCell(5),String.valueOf(durchschnittlicheBandbreiteDownLoadSekII)+" MBit/s");
        setCellValue(tableRowFife.getCell(6),String.valueOf(durchschnittlicheBandbreiteDownLoadBK)+" MBit/s");
        setCellValue(tableRowFife.getCell(7),String.valueOf(durchschnittlicheBandbreiteDownLoadSonstige)+" MBit/s");

        XWPFTableRow tableRowSix = table.createRow();

        setCellValue(tableRowSix.getCell(0), "AVG", "Upload");
        setCellValue(tableRowSix.getCell(1),String.valueOf(durchschnittlicheBandbreiteUpLoadGesamt)+" MBit/s");
        setCellValue(tableRowSix.getCell(2),String.valueOf(durchschnittlicheBandbreiteUpLoadGrundschulen)+" MBit/s");
        setCellValue(tableRowSix.getCell(3),String.valueOf(durchschnittlicheBandbreiteUpLoadFoerderschulen)+" MBit/s");
        setCellValue(tableRowSix.getCell(4),String.valueOf(durchschnittlicheBandbreiteUpLoadSekI)+" MBit/s");
        setCellValue(tableRowSix.getCell(5),String.valueOf(durchschnittlicheBandbreiteUpLoadSekII)+" MBit/s");
        setCellValue(tableRowSix.getCell(6),String.valueOf(durchschnittlicheBandbreiteUpLoadBK)+" MBit/s");
        setCellValue(tableRowSix.getCell(7),String.valueOf(durchschnittlicheBandbreiteUpLoadSonstige)+" MBit/s");

        XWPFTableRow tableRowSeven = table.createRow();
        tableRowSeven.getCell(0).setText("Bandbreite pro Schüler");
        tableRowSeven.getCell(1).setText("todo");

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run3 = paragraph.createRun();
        run3.addBreak();
        run3.addBreak();

        int gesamtBochum = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='BO'");
        int gesamtDortmund = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='DO'");
        int gesamtEnnepetal = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='EN'");
        int gesamtHagen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='HA'");
        int gesamtHamm = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='HAM'");
        int gesamtHerne = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='HER'");
        int gesamtHochsauerlandkreis = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='HSK'");
        int gesamtMaerkischerKreis = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='MK'");
        int gesamtOlpe = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='OE'");
        int gesamtSiegen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='SI'");
        int gesamtSoest = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='SO'");
        int gesamtUnna = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen WHERE [Zuständiges Schulamt]='UN'");

        int anschlussBochum = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='BO'");
        int anschlussDortmund = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='DO'");
        int anschlussEnnepetal = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='EN'");
        int anschlussHagen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='HA'");
        int anschlussHamm = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='HAM'");
        int anschlussHerne = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='HER'");
        int anschlussHochsauerlandkreis = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='HSK'");
        int anschlussMaerkischerKreis = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='MK'");
        int anschlussOlpe = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='OE'");
        int anschlussSiegen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='SI'");
        int anschlussSoest = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='SO'");
        int anschlussUnna = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='UN'");

        int ohneGigabitBochum = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='BO'");
        int ohneGigabitDortmund = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='DO'");
        int ohneGigabitEnnepetal = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='EN'");
        int ohneGigabitHagen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='HA'");
        int ohneGigabitHamm = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='HAM'");
        int ohneGigabitHerne = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='HER'");
        int ohneGigabitHochsauerlandkreis = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='HSK'");
        int ohneGigabitMaerkischerKreis = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='MK'");
        int ohneGigabitOlpe = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='OE'");
        int ohneGigabitSiegen = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='SI'");
        int ohneGigabitSoest = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='SO'");
        int ohneGigabitUnna = schuleRepository.getIntQueryValue("SELECT COUNT(*) FROM Schulen  WHERE [Anbindung Kbit DL] > 0 AND [Anbindung Kbit UL] > 0 AND [Anbindung Kbit UL] < 1000000 AND [Zuständiges Schulamt]='UN'");

        ArrayList<String> kreise = new ArrayList<String>();
        kreise.add("BO");
        kreise.add("DO");
        kreise.add("EN");
        kreise.add("HA");
        kreise.add("HAM");
        kreise.add("HER");
        kreise.add("HSK");
        kreise.add("MK");
        kreise.add("OE");
        kreise.add("SI");
        kreise.add("SO");
        kreise.add("UN");

        ArrayList<Integer> durchschnittlicheBandbreiteDownload = new ArrayList<Integer>();
        ArrayList<Integer> durchschnittlicheBandbreiteUpload = new ArrayList<Integer>();
        for(String kreis:kreise)
        {
            durchschnittlicheBandbreiteDownload.add((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit DL]) FROM Schulen WHERE [Anbindung Kbit DL] > 0 AND [Zuständiges Schulamt]='"+kreis+"'",1)/1024));
            durchschnittlicheBandbreiteUpload.add((int)(schuleRepository.getDoubleQueryValue("SELECT AVG([Anbindung Kbit UL]) FROM Schulen WHERE [Anbindung Kbit UL] > 0 AND [Zuständiges Schulamt]='"+kreis+"'",1)/1024));
        }


        createParagraph(document, tableHead,"Statistiken nach Regionen:");
        //create table
        XWPFTable table2 = document.createTable();
        table2.setCellMargins(10, 100, 10, 100);
        //create first row
        XWPFTableRow tableRowOne2 = table2.getRow(0);
        tableRowOne2.getCell(0).setText("");
        setCellValue(tableRowOne2.addNewTableCell(),"Gesamt");
        setCellValue(tableRowOne2.addNewTableCell(),"BO");
        setCellValue(tableRowOne2.addNewTableCell(),"DO");
        setCellValue(tableRowOne2.addNewTableCell(),"EN");
        setCellValue(tableRowOne2.addNewTableCell(),"HA");
        setCellValue(tableRowOne2.addNewTableCell(),"HAM");
        setCellValue(tableRowOne2.addNewTableCell(),"HER");


        //create second row
        XWPFTableRow tableRowTwo2 = table2.createRow();
        setCellValue(tableRowTwo2.getCell(0), "In der", "Datenbank");
        setCellValue(tableRowTwo2.getCell(1),String.valueOf(standorteGesamt));
        setCellValue(tableRowTwo2.getCell(2), gesamtBochum+" /", round((double)(gesamtBochum*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo2.getCell(3), gesamtDortmund+" /", round((double)(gesamtDortmund*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo2.getCell(4), gesamtEnnepetal+" /", round((double)(gesamtEnnepetal*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo2.getCell(5), gesamtHagen+" /", round((double)(gesamtHagen*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo2.getCell(6), gesamtHamm+" /", round((double)(gesamtHamm*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo2.getCell(7), gesamtHerne+" /", round((double)(gesamtHerne*100)/(double)standorteGesamt,1)+"%");


        XWPFTableRow tableRowThree2 = table2.createRow();
        setCellValue(tableRowThree2.getCell(0), "Anschluss", "erfasst");
        setCellValue(tableRowThree2.getCell(1), String.valueOf(anschlussGesamt)+" /", round((double)(anschlussGesamt*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowThree2.getCell(2),String.valueOf(anschlussBochum)+" /",round((double)(anschlussBochum*100)/(double)gesamtBochum,1)+"%");
        setCellValue(tableRowThree2.getCell(3),String.valueOf(anschlussDortmund)+" /",round((double)(anschlussDortmund*100)/(double)gesamtDortmund,1)+"%");
        setCellValue(tableRowThree2.getCell(4),String.valueOf(anschlussEnnepetal)+" /",round((double)(anschlussEnnepetal*100)/(double)gesamtEnnepetal,1)+"%");
        setCellValue(tableRowThree2.getCell(5),String.valueOf(anschlussHagen)+" /",round((double)(anschlussHagen*100)/(double)gesamtHagen,1)+"%");
        setCellValue(tableRowThree2.getCell(6),String.valueOf(anschlussHamm)+" /",round((double)(anschlussHamm*100)/(double)gesamtHamm,1)+"%");
        setCellValue(tableRowThree2.getCell(7),String.valueOf(anschlussHerne)+" /",round((double)(anschlussHerne*100)/(double)gesamtHerne,1)+"%");

        XWPFTableRow tableRowFour2 = table2.createRow();
        setCellValue(tableRowFour2.getCell(0), "ohne Gigabit-", "Anschluss");
        setCellValue(tableRowFour2.getCell(1), String.valueOf(ohneGigabitGesamt)+" /", round((double)(ohneGigabitGesamt*100)/(double)anschlussGesamt,1)+"%");
        setCellValue(tableRowFour2.getCell(2),String.valueOf(ohneGigabitBochum)+" /",round((double)(ohneGigabitBochum*100)/(double)anschlussBochum,1)+"%");
        setCellValue(tableRowFour2.getCell(3),String.valueOf(ohneGigabitDortmund)+" /",round((double)(ohneGigabitDortmund*100)/(double)anschlussDortmund,1)+"%");
        setCellValue(tableRowFour2.getCell(4),String.valueOf(ohneGigabitEnnepetal)+" /",round((double)(ohneGigabitEnnepetal*100)/(double)anschlussEnnepetal,1)+"%");
        setCellValue(tableRowFour2.getCell(5),String.valueOf(ohneGigabitHagen)+" /",round((double)(ohneGigabitHagen*100)/(double)anschlussHagen,1)+"%");
        setCellValue(tableRowFour2.getCell(6),String.valueOf(ohneGigabitHamm)+" /",round((double)(ohneGigabitHamm*100)/(double)anschlussHamm,1)+"%");
        if(anschlussHerne == 0)
            setCellValue(tableRowFour2.getCell(7),String.valueOf(ohneGigabitHerne)+" /","0%");
        else
            setCellValue(tableRowFour2.getCell(7),String.valueOf(ohneGigabitHerne)+" /",round((double)(ohneGigabitHerne*100)/(double)anschlussHerne,1)+"%");

        XWPFTableRow tableRowFive2 = table2.createRow();

        setCellValue(tableRowFive2.getCell(0), "AVG", "Download");
        setCellValue(tableRowFive2.getCell(1),String.valueOf(durchschnittlicheBandbreiteDownLoadGesamt)+" MBit/s");
        for(int i = 0; i < 6; i++)
        {
            setCellValue(tableRowFive2.getCell(i+2),String.valueOf(durchschnittlicheBandbreiteDownload.get(i))+" MBit/s");
        }

        XWPFTableRow tableRowSix2 = table2.createRow();

        setCellValue(tableRowSix2.getCell(0), "AVG", "Upload");
        setCellValue(tableRowSix2.getCell(1),String.valueOf(durchschnittlicheBandbreiteUpLoadGesamt)+" MBit/s");
        for(int i = 0; i < 6; i++)
        {
            setCellValue(tableRowSix2.getCell(i+2),String.valueOf(durchschnittlicheBandbreiteUpload.get(i))+" MBit/s");
        }

        XWPFTableRow tableRowSeven2 = table2.createRow();
        setCellValue(tableRowSeven2.getCell(0), "Bandbreite", "pro Schüler");
        tableRowSeven2.getCell(1).setText("todo");

        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun run4 = paragraph2.createRun();
        run4.addBreak();

        //create table
        XWPFTable table3 = document.createTable();
        table3.setCellMargins(10, 100, 10, 100);
        //create first row
        XWPFTableRow tableRowOne3 = table3.getRow(0);
        tableRowOne3.getCell(0).setText("");
        setCellValue(tableRowOne3.addNewTableCell(),"Gesamt");
        setCellValue(tableRowOne3.addNewTableCell(),"HSK");
        setCellValue(tableRowOne3.addNewTableCell(),"MK");
        setCellValue(tableRowOne3.addNewTableCell(),"OE");
        setCellValue(tableRowOne3.addNewTableCell(),"SI");
        setCellValue(tableRowOne3.addNewTableCell(),"SO");
        setCellValue(tableRowOne3.addNewTableCell(),"UN");


        //create second row
        XWPFTableRow tableRowTwo3 = table3.createRow();
        setCellValue(tableRowTwo3.getCell(0), "In der", "Datenbank");
        setCellValue(tableRowTwo3.getCell(1),String.valueOf(standorteGesamt));
        setCellValue(tableRowTwo3.getCell(2), gesamtHochsauerlandkreis+" /", round((double)(gesamtHochsauerlandkreis*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo3.getCell(3), gesamtMaerkischerKreis+" /", round((double)(gesamtMaerkischerKreis*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo3.getCell(4), gesamtOlpe+" /", round((double)(gesamtOlpe*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo3.getCell(5), gesamtSiegen+" /", round((double)(gesamtSiegen*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo3.getCell(6), gesamtSoest+" /", round((double)(gesamtSoest*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowTwo3.getCell(7), gesamtUnna+" /", round((double)(gesamtUnna*100)/(double)standorteGesamt,1)+"%");

        XWPFTableRow tableRowThree3 = table3.createRow();
        setCellValue(tableRowThree3.getCell(0), "Anschluss", "erfasst");
        setCellValue(tableRowThree3.getCell(1), String.valueOf(anschlussGesamt)+" /", round((double)(anschlussGesamt*100)/(double)standorteGesamt,1)+"%");
        setCellValue(tableRowThree3.getCell(2),String.valueOf(anschlussHochsauerlandkreis)+" /",round((double)(anschlussHochsauerlandkreis*100)/(double)gesamtHochsauerlandkreis,1)+"%");
        setCellValue(tableRowThree3.getCell(3),String.valueOf(anschlussMaerkischerKreis)+" /",round((double)(anschlussMaerkischerKreis*100)/(double)gesamtMaerkischerKreis,1)+"%");
        setCellValue(tableRowThree3.getCell(4),String.valueOf(anschlussOlpe)+" /",round((double)(anschlussOlpe*100)/(double)gesamtOlpe,1)+"%");
        setCellValue(tableRowThree3.getCell(5),String.valueOf(anschlussSiegen)+" /",round((double)(anschlussSiegen*100)/(double)gesamtSiegen,1)+"%");
        setCellValue(tableRowThree3.getCell(6),String.valueOf(anschlussSoest)+" /",round((double)(anschlussSoest*100)/(double)gesamtSoest,1)+"%");
        setCellValue(tableRowThree3.getCell(7),String.valueOf(anschlussUnna)+" /",round((double)(anschlussUnna*100)/(double)gesamtUnna,1)+"%");

        XWPFTableRow tableRowFour3 = table3.createRow();
        setCellValue(tableRowFour3.getCell(0), "ohne Gigabit-", "Anschluss");
        setCellValue(tableRowFour3.getCell(1), String.valueOf(ohneGigabitGesamt)+" /", round((double)(ohneGigabitGesamt*100)/(double)anschlussGesamt,1)+"%");
        setCellValue(tableRowFour3.getCell(2),String.valueOf(ohneGigabitHochsauerlandkreis)+" /",round((double)(ohneGigabitHochsauerlandkreis*100)/(double)anschlussHochsauerlandkreis,1)+"%");
        setCellValue(tableRowFour3.getCell(3),String.valueOf(ohneGigabitMaerkischerKreis)+" /",round((double)(ohneGigabitMaerkischerKreis*100)/(double)anschlussMaerkischerKreis,1)+"%");
        setCellValue(tableRowFour3.getCell(4),String.valueOf(ohneGigabitOlpe)+" /",round((double)(ohneGigabitOlpe*100)/(double)anschlussOlpe,1)+"%");
        setCellValue(tableRowFour3.getCell(5),String.valueOf(ohneGigabitSiegen)+" /",round((double)(ohneGigabitSiegen*100)/(double)anschlussSiegen,1)+"%");
        setCellValue(tableRowFour3.getCell(6),String.valueOf(ohneGigabitSoest)+" /",round((double)(ohneGigabitSoest*100)/(double)anschlussSoest,1)+"%");
        setCellValue(tableRowFour3.getCell(7),String.valueOf(ohneGigabitUnna)+" /",round((double)(ohneGigabitUnna*100)/(double)anschlussUnna,1)+"%");

        XWPFTableRow tableRowFife3 = table3.createRow();

        setCellValue(tableRowFife3.getCell(0), "AVG", "Download");
        setCellValue(tableRowFife3.getCell(1),String.valueOf(durchschnittlicheBandbreiteDownLoadGesamt)+" MBit/s");
        for(int i = 0; i < 6; i++)
        {
            setCellValue(tableRowFife3.getCell(i+2),String.valueOf(durchschnittlicheBandbreiteDownload.get(i+6))+" MBit/s");
        }

        XWPFTableRow tableRowSix3 = table3.createRow();

        setCellValue(tableRowSix3.getCell(0), "AVG", "Upload");
        setCellValue(tableRowSix3.getCell(1),String.valueOf(durchschnittlicheBandbreiteUpLoadGesamt)+" MBit/s");
        for(int i = 0; i < 6; i++)
        {
            setCellValue(tableRowSix3.getCell(i+2),String.valueOf(durchschnittlicheBandbreiteUpload.get(i+6))+" MBit/s");
        }

        XWPFTableRow tableRowSeven3 = table3.createRow();
        setCellValue(tableRowSeven3.getCell(0), "Bandbreite", "pro Schüler");
        tableRowSeven3.getCell(1).setText("todo");

        try {
            document.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCellValue(XWPFTableCell tableCell, String text)
    {

        XWPFParagraph para = tableCell.getParagraphs().get(0);
        para.setIndentationLeft(10);
        para.setIndentationRight(10);
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setText(text);
        tableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    private void setCellValue(XWPFTableCell tableCell, String upper, String lower)
    {

        XWPFParagraph para = tableCell.getParagraphs().get(0);
        para.setIndentationLeft(100);
        para.setIndentationRight(100);
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setText(upper);
        run.addBreak();
        run.setText(lower);
        tableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    private XWPFParagraph createParagraph(XWPFDocument document, String style, String text, ParagraphAlignment alignment) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setStyle(style);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.addBreak();
        return paragraph;
    }

    private XWPFParagraph createParagraph(XWPFDocument document, String style, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setStyle(style);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.addBreak();
        return paragraph;
    }

    private static void addCustomHeadingStyle(XWPFDocument docxDocument, XWPFStyles styles, String strStyleId, int headingLevel, int pointSize, String hexColor) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);


        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
        size.setVal(new BigInteger(String.valueOf(pointSize)));
        CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
        size2.setVal(new BigInteger("24"));

        CTFonts fonts = CTFonts.Factory.newInstance();
        fonts.setAscii("Times New Roman" );

        CTRPr rpr = CTRPr.Factory.newInstance();
        rpr.setRFonts(fonts);
        rpr.setSz(size);
        rpr.setSzCs(size2);

        CTColor color= CTColor.Factory.newInstance();
        color.setVal(hexToBytes(hexColor));
        rpr.setColor(color);
        style.getCTStyle().setRPr(rpr);
        // is a null op if already defined

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    public static byte[] hexToBytes(String hexString) {
        HexBinaryAdapter adapter = new HexBinaryAdapter();
        byte[] bytes = adapter.unmarshal(hexString);
        return bytes;
    }

}
