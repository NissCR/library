package entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Заявка на закупку книги
 */
@Entity
@SecondaryTable(name = "BOOK_PURCHASING",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "ORDER_ID"))
public class BookPurchasing extends BookOrder {
    @Column(table = "BOOK_PURCHASING", name = "STATE")
    private String state;
    @Column(table = "BOOK_PURCHASING", name = "PRICE")
    private double price;

    public BookPurchasing(Date date, Book book, User user, String state, double price) {
        super(date, book, user);
        this.state = state;
        this.price = price;
    }

    public BookPurchasing(){
        super();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
