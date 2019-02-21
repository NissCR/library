package entities;

import javax.persistence.*;

/**
 * Книга библиотеки
 */
@Entity
@Table(name = "BOOK")
public class Book {
    @Id
    @Column(name = "ISBN")
    private String ISBN;
    @Column(name = "NAME", length = 100)
    private String name;
    @Column(name = "PUBLISH_YEAR", length = 4)
    private int publishYear;
    @Column(name = "PAGE_NUM", length = 4)
    private int pageNum;
    @Column(name = "E_VERSION")
    private String eVersion;
    @Column(name = "COPY_NUM", length = 3)
    private int copyNum;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private Author author;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "GENRE")
    private Genre genre;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID")
    private Publisher publisher;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "AVAILABILYTY")
    private LibAvailability availability;

    public Book(String ISBN, String name, int publishYear, int pageNum, String eVersion, int copyNum,
                Author author, Genre genre, Publisher publisher, LibAvailability availability) {
        this.ISBN = ISBN;
        this.name = name;
        this.publishYear = publishYear;
        this.pageNum = pageNum;
        this.eVersion = eVersion;
        this.copyNum = copyNum;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.availability = availability;
    }

    public Book() {
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String geteVersion() {
        return eVersion;
    }

    public void seteVersion(String eVersion) {
        this.eVersion = eVersion;
    }

    public int getCopyNum() {
        return copyNum;
    }

    public void setCopyNum(int copyNum) {
        this.copyNum = copyNum;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public LibAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(LibAvailability availability) {
        this.availability = availability;
    }
}
