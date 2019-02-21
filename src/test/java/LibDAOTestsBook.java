import Utils.LibDAO;
import entities.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LibDAOTestsBook {
    static LibDAO libDAO;
    static Author author;
    static Publisher publisher;
    static Book book;

    @Test
    @DisplayName("FirstTest")
    void firstTest(){
        assertTrue('a' < 'b', "Testing");
    }

    @Test
    void secondTest(){
        assertNotNull("null");
    }

    @Test
    void thirdTest(){
        assertAll("habr",
                () -> assertTrue("https://habrahabr.ru".startsWith("https")),
                () -> assertTrue("https://habrahabr.ru".endsWith(".ru"))
        );
    }

    @Test
    @BeforeAll
    @DisplayName("Создание и добавление")
    static void createAndAdd(){
        libDAO = new LibDAO();
        addAuthor();
        addPublisher();
        addBook();
    }

    static void addAuthor(){
        author = new Author("TestAuthor");
        libDAO.add(author);
    }

    static void addPublisher(){
        publisher = new Publisher("TestPublisher");
        libDAO.add(publisher);
    }

    static void addBook(){
        author = libDAO.getAuthorByName(author.getFullName()).get(0);
        publisher = libDAO.getPublisherByName(publisher.getName()).get(0);
        book = new Book("00000", "TestBook", 2000, 100, "e-version",
                15, author, Genre.FICTION, publisher, LibAvailability.AVAILABLE);
        libDAO.add(book);
    }

    @Test
    @DisplayName("Все выводы (get) книг из БД")
    void getBook(){
        assertAll("getBook",
                () -> assertEquals(libDAO.getBookByName(book.getName()).get(0), book),
                () -> assertEquals(libDAO.getBookByAuthor(book.getAuthor()).get(0), book),
                () -> assertEquals(libDAO.getBookByISBN(book.getISBN()).get(0), book)
        );
    }

    @Test
    void updateAuthor(){
        book.getAuthor().setFullName("TestAuthor");
        libDAO.update(book);
        getBook();
    }

    @Test
    void updateBook(){
        book.setName("Naaaaaame");
        getBook();
    }

    static void deleteBook(){
        book = libDAO.getBookByISBN("00000").get(0);
        libDAO.delete(book);
    }

    static void deleteAuthor(){
        author = libDAO.getAuthorByName("TestAuthor").get(0);
        libDAO.delete(author);
    }

    static void deletePublisher(){
        publisher = libDAO.getPublisherByName("TestPublisher").get(0);
        libDAO.delete(publisher);
    }

    @AfterAll
    @Test
    @DisplayName("Удаление всех объктов")
    static void delete(){
        deleteAuthor();
        deletePublisher();
        deleteBook();
        libDAO = null;
    }
}