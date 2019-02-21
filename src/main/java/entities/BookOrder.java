package entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Заявка на заказ книги
 */
@Entity
@Table(name = "BOOK_ORDER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BookOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private int id;
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISBN")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIB_CARD_NUM")
    private User user;

    public BookOrder(Date date, Book book, User user) {
        this.date = date;
        this.book = book;
        this.user = user;
    }

    public BookOrder(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
