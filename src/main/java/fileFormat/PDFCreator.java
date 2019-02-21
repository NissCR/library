package fileFormat;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Создание PDF-документов в папке pdf
 */
public class PDFCreator {
    private static final String DEST_ROOT = "pdf/";
    private static final String EXT = ".pdf";
    private static final String FONT = "C:/Windows/Fonts/Calibri.ttf";

    public static void createPDF(String title, String dates, ArrayList<Paragraph> paragraphs) throws IOException{
        //Инициализация PDF-документа
        PdfDocument pdf = new PdfDocument(new PdfWriter(DEST_ROOT + title + "(" + dates + ")" + EXT));
        //Инициализация документа отображения
        Document document = new Document(pdf);
        //Установка шрифта
        PdfFont font = PdfFontFactory.createFont(FONT, "Cp1251", true);
        document.setFont(font);

        //Заголовок по середине, жирный
        document.add(new Paragraph().setBold().setTextAlignment(TextAlignment.CENTER)
                .add(new Text(title)));
        //Даты по середине
        document.add(new Paragraph().setTextAlignment(TextAlignment.CENTER).add(new Text(dates)));//"xx.xx.xxxx-xx.xx.xxxx"));
        document.add(new Paragraph(""));

        for(Paragraph par : paragraphs){
            document.add(par);
        }

        document.close();
    }
}