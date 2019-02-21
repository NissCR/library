package main;

import entities.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

/**
 * Внутренне окно системы
 * Окно поиска
 */
public class SearchFrame extends JInternalFrame implements ItemListener{
    final static String NAME = "По названию";
    final static String AUTHOR = "По автору";
    final static String ALPHAB = "Алфавитный";
    final static String GENRE = "Тематический";

    private static JPanel cards;
    private static JButton btnGetEVersion;
    private static JButton btnOrder;
    private static JButton btnPurchase;
    private static JTable table;

    private static String[] colHeaders = new String[] {"ISBN", "Название", "Автор", "Издательство", "Год издания",
            "Жанр", "Количество страниц", "Число экземпляров", "Наличие"};

    public SearchFrame(){
        super("Поиск книг", true, true, true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JComboBox<String> cbSearch = new JComboBox<>(new String[]{NAME, AUTHOR, ALPHAB, GENRE});
        cbSearch.addItemListener(this);

        JPanel cardName = new JPanel();
        cardName.add(new JLabel("Название:"));
        JTextField tfName = new JTextField(30);
        cardName.add(tfName);

        JPanel cardAuthor = new JPanel();
        cardAuthor.add(new JLabel("Автор:"));
        JTextField tfAuthor = new JTextField(30);
        cardAuthor.add(tfAuthor);

        JPanel cardAlphab = new JPanel();
        cardAlphab.add(new JLabel("Буква (-ы) названия: "));
        //JComboBox<Character> cbLetters = new JComboBox<>(LETTERS);
        JList<Character> listLetters = new JList<>(getLetters());
        listLetters.setFixedCellHeight(12);
        cardAlphab.add(new JScrollPane(listLetters));

        JPanel cardGenre = new JPanel();
        cardGenre.add(new JLabel("Жанр:"));
        JComboBox<String> cbGenre = new JComboBox<>(new String[]{Genre.CLASSIC.getValue(), Genre.FICTION.getValue(),
            Genre.SCIENTIFIC.getValue(), Genre.TEACHING_AIDS.getValue(), Genre.PERIODICAL.getValue()});
        cardGenre.add(cbGenre);

        cards = new JPanel(new CardLayout());
        cards.add(cardName, NAME);
        cards.add(cardAuthor, AUTHOR);
        cards.add(cardAlphab, ALPHAB);
        cards.add(cardGenre, GENRE);

        table = new JTable(new TableModel());
        //При выделении строки таблицы отображать кнопки
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setButtonsAct(!(table.getSelectedRow() == -1));
            }
        });

        JButton btnSearch = new JButton("Поиск");
        btnGetEVersion = new JButton("Получить эл. версию");//TODO - ICON
        //Вывод эл. версии
        btnGetEVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = getSelectedBook();
                if(book.geteVersion().equals("")){
                    JOptionPane.showMessageDialog(SearchFrame.this, "В настоящее время " +
                            "электронная версия данной книги отсутствует.", "eBook", JOptionPane.INFORMATION_MESSAGE);
                } else{
                    JTextField eVers = new JTextField(book.geteVersion());
                    JOptionPane.showMessageDialog(SearchFrame.this, book.getName() + ":\n" + eVers,
                            "eBook", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnOrder = new JButton("Заказать");
        //Заказ книги
        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = getSelectedBook();
                if(book.getAvailability().getValue().equals(LibAvailability.AVAILABLE.getValue())) {
                    //Есть ли книга в наличии
                    if (book.getCopyNum() > 0) {
                        //Нет ли долги у запрашивающего
                        if (!Library.user.isHasDebt()) {
                            int answer = JOptionPane.showConfirmDialog(SearchFrame.this, "Вы хотите " +
                                            "заказать книгу " + book.getAuthor().getFullName() + ". " + book.getName() + "?", "Подтверждение",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (answer == 0) {//Если ДА
                                Library.libDAO.add(new BookOrder(new Date(), book, Library.user));
                                book.setCopyNum(book.getCopyNum() - 1);
                                Library.libDAO.update(book);
                                JOptionPane.showMessageDialog(SearchFrame.this, "Вы можете забрать книгу " +
                                        "в течение следующего рабочего дня.");
                            }
                        } else {//Есть долги
                            JOptionPane.showMessageDialog(SearchFrame.this, "У вас есть долги. " +
                                    "Погасите их прежде чем заказывать книгу.", "Внимание", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {//Нет книги в наличии
                        JOptionPane.showMessageDialog(SearchFrame.this, "В данный момент нет свободного" +
                                "экземпляра данной книги.", "Внимание", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(SearchFrame.this, "Книги нет в наличии",
                            "Внимание", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnPurchase = new JButton("Закупить");
        //Закупка книг
        btnPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPurchase();
            }
        });

        setButtonsAct(false);

        //Поиск
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = cbSearch.getSelectedIndex();

                //Проверка на заполненность полей
                if((idx == 0 && tfName.getText().equals("")) || (idx == 1 && tfAuthor.getText().equals(""))){
                    JOptionPane.showMessageDialog(SearchFrame.this, "Не все поля заполнены",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else {
                    ArrayList<Book> books = new ArrayList<>();
                    switch (idx){
                        //Поиск по названию
                        case 0:
                            books = Library.libDAO.getBookByName(tfName.getText());
                            break;
                        //Поик по автору
                        case 1:
                            ArrayList<Author> authors = Library.libDAO.getAuthorByName(tfAuthor.getText());
                            if(authors.size() == 0){
                                JOptionPane.showMessageDialog(SearchFrame.this, "Автор неизвестен",
                                        "Поиск", JOptionPane.PLAIN_MESSAGE);
                            } else{
                                for(Author author : authors){
                                    ArrayList<Book> tmp = Library.libDAO.getBookByAuthor(author);
                                    if(tmp.size() != 0){
                                        for(Book book : tmp)
                                            books.add(book);
                                    }
                                }
                            }
                            break;
                        //Алфавитный поиск
                        case 2:
                            ArrayList<Character> selLetters = new ArrayList<>();
                            selLetters.addAll(listLetters.getSelectedValuesList());
                            for(Character ch : selLetters){
                                ArrayList<Book> tmp = Library.libDAO.getBookByName(ch + "");
                                if(tmp.size() != 0){
                                    for(Book book : tmp)
                                        books.add(book);
                                }
                            }
                            break;
                        //Тематический поиск
                        case 3:
                            books = Library.libDAO.getBookByGenre(Genre.fromString(cbGenre.getSelectedItem().toString()));
                            break;
                        default:
                            books = null;
                    }

                    if(books.size() == 0){
                        JOptionPane.showMessageDialog(SearchFrame.this,
                                "Поиск не дал результатов", "Поиск", JOptionPane.PLAIN_MESSAGE);
                    }
                    ((TableModel) table.getModel()).setBooks(books);
                }
            }
        });

        Container content = getContentPane();

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));//FlowLayout());

        JPanel boxLeftPanel = new JPanel();
        boxLeftPanel.setLayout(new BoxLayout(boxLeftPanel, BoxLayout.Y_AXIS));
        boxLeftPanel.add(cbSearch);
        boxLeftPanel.add(cards);

        JPanel boxRightPanel = new JPanel();
        boxRightPanel.setLayout(new BoxLayout(boxRightPanel, BoxLayout.Y_AXIS));
        boxRightPanel.add(btnSearch);
        boxRightPanel.add(Box.createVerticalStrut(15));
        boxRightPanel.add(btnGetEVersion);
        boxRightPanel.add(btnOrder);
        boxRightPanel.add(btnPurchase);

        JPanel enterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enterPanel.add(boxLeftPanel);
        enterPanel.add(boxRightPanel);

        content.add(enterPanel);
        content.add(new JScrollPane(table));
        //pack();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        CardLayout layout = (CardLayout) cards.getLayout();
        layout.show(cards, e.getItem().toString());
    }

    //Заглавные буквы русского и английского алфавита
    private static Character[] getLetters(){
        ArrayList<Character> letters = new ArrayList<>();
        for(char i = 'A'; i <= 'Z'; i++)
            letters.add(i);
        for(char i = 'А'; i <= 'Я'; i++)
            letters.add(i);
        Character[] result = new Character[letters.size()];
        for(int i = 0; i < letters.size(); i++)
            result[i] = letters.get(i);
        return result;
    }

    //Активировать кнопки, соответствующие пользователю
    private static void setButtonsAct(boolean isSelected){
        switch(Library.user.getUserType()){
            case LIBRARIAN:
                btnOrder.setVisible(false);
                btnGetEVersion.setVisible(false);
                btnPurchase.setVisible(false);
                break;
            case STUDENT:
                btnGetEVersion.setVisible(isSelected);
                btnPurchase.setVisible(false);
                btnOrder.setVisible(isSelected);
                break;
            case TEACHER:
                btnGetEVersion.setVisible(isSelected);
                btnOrder.setVisible(false);
                btnPurchase.setVisible(true);
                break;
        }
    }

    //Возвращает выделенную книгу из таблицы
    private static Book getSelectedBook(){
        int idx = table.getSelectedRow();
        return ((TableModel) table.getModel()).books.get(idx);
    }

    //Модель таблицы
    class TableModel extends AbstractTableModel{
        private ArrayList<Book> books;

        TableModel(){
            books = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return books.size();
        }

        @Override
        public int getColumnCount() {
            return 9;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Book book = books.get(row);
            switch (column){
                case 0: return book.getISBN();
                case 1: return book.getName();
                case 2: return book.getAuthor().getFullName();
                case 3: return book.getPublisher().getName();
                case 4: return book.getPublishYear();
                case 5: return book.getGenre().getValue();
                case 6: return book.getPageNum();
                case 7: return book.getCopyNum();
                case 8: return book.getAvailability().getValue();
                default: return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return colHeaders[column];
        }

        public void setBooks(ArrayList<Book> books){
            this.books = books;
            this.fireTableDataChanged();
        }
    }

    //Форма заявки
    class AddPurchase extends JDialog{
        private final String[] genres = new String[]{Genre.CLASSIC.getValue(), Genre.FICTION.getValue(),
                Genre.SCIENTIFIC.getValue(), Genre.TEACHING_AIDS.getValue(), Genre.PERIODICAL.getValue()};
        AddPurchase(){
            setTitle("Заявка");
            setModal(true);

            JTextField tfISBN = new JTextField(13);
            tfISBN.setMaximumSize(tfISBN.getPreferredSize());
            JTextField tfName = new JTextField(30);
            JTextField tfAuthor = new JTextField(20);
            JComboBox<String> cbGenre = new JComboBox<>(genres);
            JTextField tfPublisher = new JTextField(20);
            int currentYear = Year.now().getValue();
            JSpinner sPubYear = new JSpinner(new SpinnerNumberModel(currentYear, 1500, currentYear, 1));
            sPubYear.setMaximumSize(sPubYear.getPreferredSize());
            //TODO - или не tf или сделать проверку на ввод чисел
            JTextField tfPageNum = new JTextField(5);
            JTextField tfCopyNum = new JTextField(5);
            JTextField tfPrice = new JTextField(5);//Или не tf?

            JButton btnAdd = new JButton("Подтвердить");
            //Формирование заявки
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Закупка
                    if(tfName.getText().equals("") && tfAuthor.getText().equals("") && tfPublisher.getText().equals("") &&
                            tfPageNum.getText().equals("") && tfCopyNum.getText().equals("") && tfPrice.getText().equals("")){
                        JOptionPane.showMessageDialog(AddPurchase.this, "Не все поля заполнены",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    } else{
                        //Формирование заявки
                        Author enteredAuthor;
                        Publisher enteredPublisher;
                        ArrayList<Author> authors = Library.libDAO.getAuthorByName(tfAuthor.getText().trim());
                        //try {
                            if (authors.size() == 0) {
                                enteredAuthor = new Author(tfAuthor.getText().trim());
                                Library.libDAO.add(enteredAuthor);
                            } else
                                enteredAuthor = authors.get(0);
                            ArrayList<Publisher> publishers = Library.libDAO.getPublisherByName(tfPublisher.getText().trim());
                            if (publishers.size() == 0) {
                                enteredPublisher = new Publisher(tfPublisher.getText().trim());
                                Library.libDAO.add(enteredPublisher);
                            } else
                                enteredPublisher = publishers.get(0);

                            Book enteredBook = new Book(tfISBN.getText().trim(), tfName.getText().trim(),
                                    Integer.parseInt(sPubYear.getValue().toString().trim()), Integer.parseInt(tfPageNum.getText().trim()),
                                    "", Integer.parseInt(tfCopyNum.getText().trim()), enteredAuthor,
                                    Genre.fromString(genres[cbGenre.getSelectedIndex()]), enteredPublisher, LibAvailability.MISSING);

                            Library.libDAO.add(enteredBook);
                            Library.libDAO.add(new BookPurchasing(new Date(), enteredBook, Library.user,
                                    "В ОБРАБОТКЕ", Integer.parseInt(tfPrice.getText())));
                            JOptionPane.showMessageDialog(AddPurchase.this, "Заявка добавлена.\n" +
                                    "Ваше предложение будет рассмотрено в ближайшее время", "Подтверждение", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        /*} catch (Exception ex){
                            JOptionPane.showMessageDialog(AddPurchase.this, "Нет соединения с БД.",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }*/
                    }
                }
            });

            JButton btnCancel = new JButton("Отмена");
            btnCancel.addActionListener(e -> dispose());

            JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0));
            grid.add(btnAdd);
            grid.add(btnCancel);
            JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            flow.add(grid);

            Box boxY = new Box(BoxLayout.Y_AXIS);
            boxY.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

            Box bISBN = new Box(BoxLayout.X_AXIS);
            bISBN.add(new JLabel("ISBN:"));
            bISBN.add(Box.createHorizontalStrut(12));
            bISBN.add(tfISBN);

            Box bName = new Box(BoxLayout.X_AXIS);
            bName.add(new JLabel("Название:"));
            bName.add(Box.createHorizontalStrut(12));
            bName.add(tfName);

            Box bAuthor = new Box(BoxLayout.X_AXIS);
            bAuthor.add(new JLabel("Автор:"));
            bAuthor.add(Box.createHorizontalStrut(12));
            bAuthor.add(tfAuthor);

            Box bGenre = new Box(BoxLayout.X_AXIS);
            bGenre.add(new JLabel("Жанр:"));
            bGenre.add(Box.createHorizontalStrut(12));
            bGenre.add(cbGenre);

            Box bPub = new Box(BoxLayout.X_AXIS);
            bPub.add(new JLabel("Издательство:"));
            bPub.add(Box.createHorizontalStrut(12));
            bPub.add(tfPublisher);

            Box bPubYear = new Box(BoxLayout.X_AXIS);
            bPubYear.add(new JLabel("Год издания:"));
            bPubYear.add(Box.createHorizontalStrut(12));
            bPubYear.add(sPubYear);

            Box bPageNum = new Box(BoxLayout.X_AXIS);
            bPageNum.add(new JLabel("Количество страниц:"));
            bPageNum.add(Box.createHorizontalStrut(12));
            bPageNum.add(tfPageNum);

            Box bCopyNum = new Box(BoxLayout.X_AXIS);
            bCopyNum.add(new JLabel("Количество экземпляров:"));
            bCopyNum.add(Box.createHorizontalStrut(12));
            bCopyNum.add(tfCopyNum);

            Box bPrice = new Box(BoxLayout.X_AXIS);
            bPrice.add(new JLabel("Примерная стоимость 1 экземпляра:"));
            bPrice.add(Box.createHorizontalStrut(12));
            bPrice.add(tfPrice);

            boxY.add(bISBN);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bName);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bAuthor);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bGenre);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bPub);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bPubYear);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bPageNum);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bCopyNum);
            boxY.add(Box.createVerticalStrut(10));
            boxY.add(bPrice);
            //boxY.add(new JLabel("* - обязательные поля"));
            boxY.add(Box.createVerticalStrut(15));
            boxY.add(flow, BorderLayout.SOUTH);

            getContentPane().add(boxY, FlowLayout.LEFT);
            pack();
            setVisible(true);
        }
    }
}