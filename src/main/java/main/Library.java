package main;

import Utils.LibDAO;
import entities.BookOrder;
import entities.BookPurchasing;
import entities.User;
import entities.UserType;
import fileFormat.RequestFormation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Главное окно программы
 * Предоставляет доступ ко всем функциям
 */
public class Library extends JFrame {
    public static User user;
    static LibDAO libDAO;

    public Library(User user) {
        super("Библиотечный каталог");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width - 200,
                Toolkit.getDefaultToolkit().getScreenSize().height - 50);

        this.user = user;
        setTitle(getTitle() + " - " + user.getUserType().getValue());
        libDAO = new LibDAO();
        SearchFrame searchFrame = new SearchFrame();
        RequestsFrame requestsFrame = new RequestsFrame();
        CommunicationFrame communicationFrame = new CommunicationFrame();

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, getWidth(), 20);

        JMenu menuBook = new JMenu("Книги");
        JMenu menuReq = new JMenu("Заявки");
        //Долги, связь с библиотекарем
        JMenu menuOther = new JMenu("Дополнительно");

        JMenuItem itemSearch = new JMenuItem("Поиск");
        itemSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFrame.setVisible(true);
                /*searchFrame.setLocation(100, 50);
                searchFrame.setSize(200, getHeight());*/
                searchFrame.setBounds(10, 30, (int) (getWidth() * 0.7), (int) (getHeight() * 0.8));
                searchFrame.toFront();
            }
        });

        JMenuItem itemPurch = new JMenuItem("Просмотр заявок на закупку");
        itemPurch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestsFrame.setVisible(true);
                requestsFrame.setBounds(5, 20, (int) (getWidth() * 0.8), (int) (getHeight() * 0.7));
                requestsFrame.toFront();
            }
        });
        JMenuItem itemFormOrder = new JMenuItem("Сформировать список книг на заказ");
        //Формирование списка книг за период в PDF
        itemFormOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Запрос дат
                new DateDialog();
            }
        });
        JMenuItem itemFormPurch = new JMenuItem("Сформировать список книг на закупку");
        //Формирование списка книг на заказ в PDF
        itemFormPurch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<BookPurchasing> purchasings = libDAO.getAllPurchasingBooks();
                for(BookPurchasing purchasing : purchasings){
                    if(purchasing.getState().startsWith("ОТКЛ"))
                        purchasings.remove(purchasing);
                }
                if(purchasings.size() == 0){
                    JOptionPane.showMessageDialog(Library.this, "Не найдено книг на закупку.",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    RequestFormation.writePurchasings(purchasings);
                    JOptionPane.showMessageDialog(Library.this, "Список книг на закупку успешно создан.",
                            "Формирование списка", JOptionPane.PLAIN_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Library.this, "Невозможно записать данные в файл",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem itemDebts = new JMenuItem("Долги");
        //Вывод долгов
        itemDebts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = JOptionPane.showInputDialog(Library.this, "Введите номер читательского билета: ",
                        "Вывод долгов", JOptionPane.OK_CANCEL_OPTION);
                User deptUser;
                if(value == null) return;
                deptUser = libDAO.getUser(value);
                if(deptUser == null){
                    JOptionPane.showMessageDialog(Library.this, "Пользователь с таким номером не найден",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else{
                    JOptionPane.showMessageDialog(Library.this,deptUser.getUserType().getValue() + " "
                            + value + (deptUser.isHasDebt() ? " имеет долги" : " не имеет долгов") + ".", "Долги",
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        JMenuItem itemCommunication = new JMenuItem("Связь с библиотекарем");
        itemCommunication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicationFrame.setVisible(true);
                communicationFrame.setBounds(50, 50, (int) (getWidth() * 0.5), (int) (getHeight() * 0.5));
                communicationFrame.toFront();
            }
        });

        menuBook.add(itemSearch);
        menuReq.add(itemPurch);
        menuReq.add(itemFormOrder);
        menuReq.add(itemFormPurch);
        menuOther.add(itemCommunication);
        menuOther.add(itemDebts);

        menuBar.add(menuBook);
        menuBar.add(menuReq);
        menuBar.add(menuOther);

        //Видимость пунктов меню в соответствии с типом пользователя
        if(user.getUserType().equals(UserType.STUDENT)){
            menuReq.setVisible(false);
            /*itemPurch.setVisible(false);
            itemFormOrder.setVisible(false);
            itemFormPurch.setVisible(false);*/
            itemCommunication.setVisible(true);
            itemDebts.setVisible(false);
        } else if(user.getUserType().equals(UserType.TEACHER)){
            itemFormOrder.setVisible(false);
            itemFormPurch.setVisible(false);
            menuOther.setVisible(false);
            //itemCommunication.setVisible(false);
            //itemDebts.setVisible(false);
        }

        getContentPane().setLayout(null);

        getContentPane().add(menuBar);
        getContentPane().add(searchFrame);
        getContentPane().add(requestsFrame);
        getContentPane().add(communicationFrame);

        //pack();
        setVisible(true);
    }

    //Форматированный вывод даты для таблиц
    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
        return dateFormat.format(date);
    }

    //Диалоговое окно ввода временного периода
    class DateDialog extends JDialog{
        public DateDialog() {
            setTitle("Период");
            setModal(true);

            JSpinner sDateFrom = new JSpinner(new SpinnerDateModel());
            JSpinner sDateTo = new JSpinner(new SpinnerDateModel());

            sDateFrom.setEditor(new JSpinner.DateEditor(sDateFrom, "dd.MM.yyyy"));
            sDateFrom.setValue(new Date());
            sDateTo.setEditor(new JSpinner.DateEditor(sDateTo, "dd.MM.yyyy"));
            sDateTo.setValue(new Date());

            Container content = getContentPane();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            Box dates = new Box(BoxLayout.X_AXIS);
            dates.add(new JLabel("С "));
            dates.add(sDateFrom);
            dates.add(new JLabel("По "));
            dates.add(sDateTo);

            JButton btnEnter = new JButton("Ок");
            //Формирование списка
            btnEnter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<BookOrder> orders = libDAO.getOrderedBooks((Date) sDateFrom.getValue(), (Date) sDateTo.getValue());
                    ArrayList<BookPurchasing> purchasings = libDAO.getAllPurchasingBooks();
                    /*for (BookOrder order : orders) {
                        //if (((BookPurchasing) order).getState() != null)
                        for(BookPurchasing purchasing : purchasings){
                            if(order.getId())
                        }
                    }*/
                    //Убрать заказы на закупку
                    orders.removeIf(order -> {
                        for(BookPurchasing purchasing : purchasings){
                            if(order.getId() == purchasing.getId())
                                return true;
                        }
                        return false;
                    });
                    if(orders == null){
                        JOptionPane.showMessageDialog(Library.this, "Не найдено книг заказ.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        RequestFormation.writeOrders(orders, formatDate((Date) sDateFrom.getValue()).substring(0, 10) + "-" +
                                formatDate((Date) sDateTo.getValue()).substring(0, 10));
                        JOptionPane.showMessageDialog(Library.this, "Список книг на заказ успешно создан.",
                                "Формирование списка", JOptionPane.PLAIN_MESSAGE);
                        dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Library.this, "Невозможно записать данные в файл",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            content.add(new JPanel(new FlowLayout()).add(new JLabel("Введите временной период:")));
            content.add(dates);
            content.add(btnEnter);

            pack();
            setVisible(true);
        }
    }

    //Для тестирования
    public static void startApp(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Library(new User("999", "999000", UserType.LIBRARIAN, false));
            }
        });
    }
}
