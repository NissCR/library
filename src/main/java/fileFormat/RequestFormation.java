package fileFormat;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import entities.BookOrder;
import entities.BookPurchasing;
import main.Library;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Формирование списков книг и запись в pdf файл
 */
public class RequestFormation {

    public static void writeOrders(ArrayList<BookOrder> orders, String dates) throws IOException {
        PDFCreator.createPDF("Книги на заказ", dates, addOrderedBooks(orders));
    }

    public static void writePurchasings(ArrayList<BookPurchasing> purchasings) throws IOException {
        PDFCreator.createPDF("Книги на закупку", "", addPurchasingBooks(purchasings));
    }

    private static ArrayList<Paragraph> addOrderedBooks(ArrayList<BookOrder> orders){
        ArrayList<Paragraph> paragraphs = new ArrayList<>(orders.size());
        int i = 1;
        for(BookOrder order : orders){
            Paragraph par = new Paragraph().add(i + ". ").add(new Text(getBook(order)))
                    .add("\n\t" + Library.formatDate(order.getDate()))
                    .add(" - " + order.getUser().getUserType().getValue() + ": ").add(order.getUser().getLibCardNum());

            paragraphs.add(par);
            i++;
        }
        return paragraphs;
    }

    private static ArrayList<Paragraph> addPurchasingBooks(ArrayList<BookPurchasing> purchasings){
        ArrayList<Paragraph> paragraphs = new ArrayList<>(purchasings.size());
        int i = 1;
        for(BookPurchasing purch : purchasings){
            Paragraph par = new Paragraph().add(i + ". ").add(new Text(getBook(purch)))
                    .add("\n\t" + Library.formatDate(purch.getDate()))
                    .add(" - " + purch.getUser().getUserType().getValue() + ": ").add(purch.getUser().getLibCardNum())
                    .add("\nЦена 1 экземпляра: " + purch.getPrice());

            paragraphs.add(par);
            i++;
        }
        return paragraphs;
    }

    private static String getBook(BookOrder order){
        String builder = order.getBook().getISBN() + "  " +
                order.getBook().getName() + ", " +
                order.getBook().getAuthor().getFullName() + "; " +
                order.getBook().getGenre().getValue() + ". " +
                order.getBook().getPublisher().getName() + ", " +
                order.getBook().getPublishYear() + "г. - " +
                order.getBook().getPageNum() + "стр.";
        return builder;
    }
}
