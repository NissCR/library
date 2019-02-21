package Utils;

import entities.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;

/**
 * Прослойка между БД и системой
 */
public class LibDAO {
    private Session session;

    public LibDAO(){
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void add(Author author){
        session.beginTransaction();
        session.save(author);
        session.getTransaction().commit();
    }

    public void add(Publisher publisher){
        session.beginTransaction();
        session.save(publisher);
        session.getTransaction().commit();
    }

    public void add(Book book){
        session.beginTransaction();
        //System.out.println(getAuthorByName(book.getAuthor().getFullName()).isEmpty());
        //if(getAuthorByName(book.getAuthor().getFullName()).isEmpty())
        //    session.save(book.getAuthor());
        //session.save(book.getPublisher());
        session.save(book);
        session.getTransaction().commit();
    }

    public void add(User user){
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    public void addFull(Book book){
        session.beginTransaction();
        session.save(book.getAuthor());
        session.save(book.getPublisher());
        session.save(book);
        session.getTransaction().commit();
    }

    public void add(BookOrder order){
        session.beginTransaction();
        session.save(order);
        session.getTransaction().commit();
    }

    public void add(BookPurchasing purchasing){
        session.beginTransaction();
        session.save(purchasing);
        session.getTransaction().commit();
    }

    public void add(Message message){
        session.beginTransaction();
        session.save(message);
        session.getTransaction().commit();
    }

    public User getUser(String login, String password){
        session.beginTransaction();
        ArrayList<User> result = (ArrayList<User>) session.createQuery("FROM User WHERE libCardNum = :pLogin AND password = :pPassword")
                .setParameter("pLogin", login)
                .setParameter("pPassword", password)
                .list();
        session.getTransaction().commit();
        if(result.size() != 0)
            return result.get(0);
        else
            return null;
    }

    public User getUser(String lcNum){
        session.beginTransaction();
        ArrayList<User> result = (ArrayList<User>) session.createQuery("FROM User WHERE libCardNum = :pLCNum")
                .setParameter("pLCNum", lcNum)
                .list();
        session.getTransaction().commit();
        if(result.size() != 0)
            return result.get(0);
        else
            return null;
    }

    public ArrayList<Author> getAuthorByName(String name){
        session.beginTransaction();
        ArrayList<Author> authors = (ArrayList<Author>) session.createQuery("FROM Author WHERE fullName LIKE :pName")
                    .setParameter("pName", "%" + name + "%")
                    .list();
        session.getTransaction().commit();
        return authors;
    }

    public ArrayList<Publisher> getPublisherByName(String name){
        session.beginTransaction();
        ArrayList<Publisher> publishers = (ArrayList<Publisher>)
                session.createQuery("FROM Publisher WHERE name LIKE :pName")
                        .setParameter("pName", "%" + name + "%")
                        .list();
        session.getTransaction().commit();
        return publishers;
    }

    public ArrayList<Book> getBookByISBN(String ISBN){
        session.beginTransaction();
        ArrayList<Book> books = (ArrayList<Book>) session.createQuery("FROM Book WHERE ISBN LIKE :pISBN")
                .setParameter("pISBN", "%" + ISBN + "%")
                .list();
        session.getTransaction().commit();
        return books;
    }
    public ArrayList<Book> getBookByName(String name){
        session.beginTransaction();
        ArrayList<Book> books = (ArrayList<Book>) session.createQuery("FROM Book WHERE name LIKE :pName")
                .setParameter("pName", "%" + name + "%")
                .list();
        session.getTransaction().commit();
        return books;
    }
    public ArrayList<Book> getBookByGenre(Genre genre){
        session.beginTransaction();
        ArrayList<Book> books = (ArrayList<Book>) session.createQuery("FROM Book WHERE genre = :pGenre")
                .setParameter("pGenre", genre)
                .list();
        session.getTransaction().commit();
        return books;
    }
    public ArrayList<Book> getBookByAuthor(Author author){
        session.beginTransaction();
        ArrayList<Book> books = (ArrayList<Book>) session.createQuery("FROM Book WHERE author = :pAuthor")
                .setParameter("pAuthor", author)
                .list();
        session.getTransaction().commit();
        return books;
    }

    public ArrayList<BookOrder> getOrderedBooks(Date from, Date to){
        session.beginTransaction();
        ArrayList<BookOrder> orders = (ArrayList<BookOrder>) session
                .createQuery("FROM BookOrder WHERE date BETWEEN :pFrom AND :pTo")
                .setParameter("pFrom", from)
                .setParameter("pTo", to)
                .list();
        session.getTransaction().commit();
        return orders;
    }

    public ArrayList<BookPurchasing> getAllPurchasingBooks(){
        session.beginTransaction();
        ArrayList<BookPurchasing> purchasings = (ArrayList<BookPurchasing>) session.createCriteria(BookPurchasing.class).list();
        session.getTransaction().commit();
        return purchasings;
    }

    public ArrayList<BookPurchasing> getPurchasingBooksByCardNum(String cardNum){
        session.beginTransaction();
        ArrayList<BookPurchasing> purchasings = (ArrayList<BookPurchasing>)
                session.createQuery("FROM BookPurchasing WHERE user.libCardNum = :pCardNum")
                .setParameter("pCardNum", cardNum)
                .list();
        session.getTransaction().commit();
        return purchasings;
    }

    public ArrayList<Message> getAllMessages(){
        session.beginTransaction();
        ArrayList<Message> messages = (ArrayList<Message>) session.createCriteria(Message.class).list();
        session.getTransaction().commit();
        return messages;
    }

    public void delete(Publisher publisher){
        session.beginTransaction();
        session.delete(publisher);
        session.getTransaction().commit();
    }

    public void delete(Author author){
        session.beginTransaction();
        session.delete(author);
        session.getTransaction().commit();
    }
    public void delete(Book book){
        session.beginTransaction();
        session.delete(book);
        session.getTransaction().commit();
    }

    public void delete(Message message){
        session.beginTransaction();
        session.delete(message);
        session.getTransaction().commit();
    }

    public void update(Book book){
        session.beginTransaction();
        session.update(book);
        session.getTransaction().commit();
    }

    public void update(BookPurchasing purchasing){
        session.beginTransaction();
        session.update(purchasing.getBook());
        session.update(purchasing);
        session.getTransaction().commit();
    }

    public void update(Message message){
        session.beginTransaction();
        session.update(message);
        session.getTransaction().commit();
    }

    public ArrayList<Book> getAllBooks(){
        return (ArrayList<Book>) session.createCriteria(Book.class).list();
    }

    public static void main(String[] args) {
        LibDAO libDAO = new LibDAO();

        libDAO.add(new Author("Вентцель"));
        //libDAO.add(new Book("132l", "Книга", 1980, 45, "", 10,
        //        new Author("Автор"), Genre.SCIENTIFIC, new Publisher("Издательство"), LibAvailability.AVAILABLE));
        /*Author author = libDAO.getAuthorByName("Автор").get(0);
        if(author != null){
            System.out.println(author.getId() + " :-| " + author.getFullName());
            libDAO.add(new Book(4321, "Книга2", 2000, 110, "", 2, author,
                    Genre.CLASSIC, new Publisher("ИркИзд"), LibAvailability.AVAILABLE));
        }
        ArrayList<Book> books = libDAO.getAllBooks();
        for (Book book : books) {
            System.out.println(book.getName());
        }*/
        /*libDAO.delete(libDAO.getPublisherByName("Издательсво Х").get(0));
        libDAO.add(new Publisher("Издательсво Х"));
        libDAO.session.getSession().close();*/

        libDAO.addFull(new Book("1327", "Книга", 1980, 45, "", 10,
                new Author("Автор"), Genre.SCIENTIFIC, new Publisher("Издательство"), LibAvailability.AVAILABLE));
        libDAO.addFull(new Book("144", "Спектр", 2001, 180, "", 5,
                new Author("Лукьяненко С. В."), Genre.FICTION, new Publisher("МоскИзд"), LibAvailability.AVAILABLE));
        libDAO.addFull(new Book("9923", "Кибернетика", 1977, 457, "", 3,
                new Author("Винер Н."), Genre.SCIENTIFIC, new Publisher("ИздНоу"), LibAvailability.AVAILABLE));
        libDAO.addFull(new Book("34598", "Машина времени", 1955, 331, "", 1,
                new Author("Уэллс Г."), Genre.FICTION, new Publisher("НовИзд"), LibAvailability.MISSING));
        libDAO.addFull(new Book("7305", "Информатика", 2010, 545, "", 15,
                new Author("Конон"), Genre.TEACHING_AIDS, new Publisher("ИроШт"), LibAvailability.PURSHASING));
        libDAO.addFull(new Book("9769", "Война и мир I-II", 2013, 928,
                "e-Version/War&Peace/link", 2, new Author("Толстой Л. Н."),
                Genre.CLASSIC, new Publisher("Эксмо"), LibAvailability.AVAILABLE));

        libDAO.add(new User("111", "admin", UserType.LIBRARIAN, false));
        libDAO.add(new User("12345", "student1", UserType.STUDENT, true));
        libDAO.add(new User("12346", "student2", UserType.STUDENT, false));
        libDAO.add(new User("12347", "teacher1", UserType.TEACHER, false));
        User admin = new User();
        try {
            admin = libDAO.getUser("111", "admin");
        } catch (Exception e) {}
        System.out.println(admin.getLibCardNum() + " | " + admin.getPassword() + " | " + admin.getUserType().toString());

        //System.out.println(libDAO.getBookByAuthor(libDAO.getAuthorByName("Конон").get(0)).get(0).getName());

        System.exit(1);
    }
}
