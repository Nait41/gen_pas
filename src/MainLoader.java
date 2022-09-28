import data.InfoList;
import javafx.application.Platform;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;

import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainLoader extends JFrame {
    XWPFDocument workbook;
    XWPFDocument workbookTemp;
    String nameObr;
    public MainLoader(String nameObr) throws IOException, InvalidFormatException {
        File file = new File("C:\\Program Files\\genpass_obr\\" + nameObr + ".docx");
        workbook = new XWPFDocument(new FileInputStream(file));
        this.nameObr = nameObr;
    }

    public void getClose() throws IOException {
        workbook.close();
    }

    public void saveFile(InfoList infoList, File docPath) throws IOException {
        workbook.write(new FileOutputStream(new File(docPath.getPath() + "\\" + infoList.fileName.replace(".xlsx", "")) + ".docx"));
    }

    public void saveObrFile() throws IOException {
        workbookTemp.write(new FileOutputStream(new File("C:\\Program Files\\genpass_obr\\" + nameObr + ".docx")));
        workbookTemp.close();
    }
}

