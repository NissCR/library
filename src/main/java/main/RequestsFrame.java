package main;

import entities.BookPurchasing;
import entities.LibAvailability;
import entities.UserType;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Внутренне окно системы
 * Окно заявки
 */
public class RequestsFrame extends JInternalFrame {
    private static JTable table;
    private static String[] colHeaders = new String[]{"id", "Дата", "Книга", "Пользователь", "Статус состояния", "Цена"};
    private static final String DETAILS = "...Подробности...";
    public RequestsFrame() {
        super("Заявки", true, true, true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        table = new JTable(new TableModel());
        ((TableModel)table.getModel()).setPurchasingsFromDB();

        JTextArea taComment = new JTextArea();
        JButton btnAgree = new JButton("Подтвердить");
        //Подтверждение заявки
        btnAgree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookPurchasing purch = getSelectedPurchasing();
                if(purch == null) return;
                purch.getBook().setAvailability(LibAvailability.PURSHASING);
                purch.setState("ПОДТВЕРЖДЕНО.\n" + taComment.getText());
                Library.libDAO.update(purch);
            }
        });
        JButton btnDisagree = new JButton("Отклонить");
        //Отклонение заявки
        btnDisagree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookPurchasing purch = getSelectedPurchasing();
                if(purch == null) return;
                purch.getBook().setAvailability(LibAvailability.MISSING);
                purch.setState("ОТКЛОНЕНО.\n" + taComment.getText());
                Library.libDAO.update(purch);
            }
        });

        JTextArea taDetails = new JTextArea();
        taDetails.setMaximumSize(new Dimension(getWidth() / 3, getHeight()));
        //При выделении строки таблицы показывать подробную информацию
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                BookPurchasing purch = getSelectedPurchasing();
                if(purch == null) {
                    taDetails.setText(DETAILS);
                    return;
                }
                taComment.setText(purch.getState());
                String builder = "id: " + purch.getId() +
                        "\nДата: " + Library.formatDate(purch.getDate()) +
                        "\nКнига: " +
                        "\n\tISBN: " + purch.getBook().getISBN() +
                        "\n\tНазвание: " + purch.getBook().getName() +
                        "\n\tАвтор: " + purch.getBook().getAuthor().getFullName() +
                        "\n\tИздательство: " + purch.getBook().getPublisher().getName() +
                        "\n\tГод издания: " + purch.getBook().getPublishYear() +
                        "\n\tЖанр: " + purch.getBook().getGenre().getValue() +
                        "\n\tКоличество страниц: " + purch.getBook().getPageNum() +
                        "\n\tКоличество экземпляров: " + purch.getBook().getCopyNum() +
                        "\nПреподаватель: " + purch.getUser().getLibCardNum() +
                        "\nСтатус состояния: " + purch.getState() +
                        "\nПримерная цена: " + purch.getPrice();
                taDetails.setText(builder);
            }
        });
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /*Container content = getContentPane();
        SpringLayout layout = new SpringLayout();
        content.setLayout(layout);

        content.add(table);
        content.add(lDetails);
        content.add(tfComment);
        content.add(btnAgree);
        content.add(btnDisagree);
        layout.putConstraint(SpringLayout.WEST, table, 20,
                             SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.NORTH, table, 20,
                             SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.NORTH, lDetails, 20,
                             SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.WEST, lDetails, 15,
                             SpringLayout.EAST, table);
        layout.putConstraint(SpringLayout.NORTH, tfComment, 15,
                             SpringLayout.SOUTH, table);
        layout.putConstraint(SpringLayout.WEST, tfComment, 20,
                             SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.NORTH, btnAgree,15,
                             SpringLayout.SOUTH, lDetails);
        layout.putConstraint(SpringLayout.WEST, btnAgree, 15,
                             SpringLayout.EAST, tfComment);
        layout.putConstraint(SpringLayout.NORTH, btnDisagree, 15,
                             SpringLayout.SOUTH, lDetails);
        layout.putConstraint(SpringLayout.WEST, btnDisagree, 15,
                             SpringLayout.EAST, btnAgree);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(content);
        content.add(new JLabel("AAAAAAAAAAAAAAAAAAAAAAAAa"));*/

        /*Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel tableLev = new JPanel();
        tableLev.setLayout(new BoxLayout(tableLev, BoxLayout.X_AXIS));
        tableLev.add(new JScrollPane(table));
        tableLev.add(lDetails);

        JPanel buttons = new JPanel();
        buttons.add(btnAgree);
        buttons.add(btnDisagree);
        content.add(buttons);

        content.add(tableLev);
        content.add(tfComment);
        content.add(buttons);*/

        Container content = getContentPane();
        content.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 10, 10); // Отступы
        constraints.weighty = 0.8;//Вес по вертикали
        constraints.weightx = 0.8;//Вес по горизонтали
        constraints.gridx = 0;//Номер ячейки по вертикали
        constraints.gridy = 0;//Номер ячейки по горизонтали
        constraints.gridwidth = 2;//Ширина элемента в ячейках
        content.add(new JScrollPane(table), constraints);

        //Показать элементы, соответствующие пользователю
        if(Library.user.getUserType().equals(UserType.TEACHER)){
            btnAgree.setVisible(false);
            btnDisagree.setVisible(false);
            taComment.setVisible(false);
        }

        constraints.insets = new Insets(5, 0, 10, 5);
        constraints.weightx = 0.2;
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        content.add(taDetails, constraints); taDetails.setText(DETAILS);

        constraints.insets = new Insets(0, 5, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.2;
        content.add(taComment, constraints);

        constraints.insets = new Insets(0, 10, 5, 5);
        constraints.gridx = 1;
        JPanel buttons = new JPanel();
        buttons.add(btnAgree);
        buttons.add(btnDisagree);
        content.add(buttons);
        content.add(buttons, constraints);


        //Обновление таблицы при активации (разворачивании) окна
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                super.internalFrameActivated(e);
                ((TableModel)table.getModel()).setPurchasingsFromDB();
                ((TableModel)table.getModel()).fireTableDataChanged();
            }
        });

        //JPanel commentLev = new JPanel();
        //commentLev.setLayout(new BoxLayout());
        /*content.add(new JScrollPane(table));
        content.add(lDetails);
        content.add(tfComment);
        JPanel buttons = new JPanel();
        buttons.add(btnAgree);
        buttons.add(btnDisagree);
        content.add(buttons);*/

//        setVisible(true);
//        setSize(500, 500);
    }

    class TableModel extends AbstractTableModel{
        private ArrayList<BookPurchasing> purchasings;
        public TableModel(){
            this.purchasings = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return purchasings.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int row, int column) {
            BookPurchasing purchasing = purchasings.get(row);
            switch (column){
                case 0: return purchasing.getId();
                case 1: return Library.formatDate(purchasing.getDate());
                case 2: return purchasing.getBook().getName();
                case 3: return purchasing.getUser().getLibCardNum();
                case 4: return purchasing.getState();
                case 5: return purchasing.getPrice();
                default: return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return colHeaders[column];
        }

        public void setPurchasingsFromDB(){
            try{
                if(Library.user.getUserType().equals(UserType.TEACHER)){
                    this.purchasings = Library.libDAO.getPurchasingBooksByCardNum(Library.user.getLibCardNum());
                } else {
                    this.purchasings = Library.libDAO.getAllPurchasingBooks();
                }
            } catch (Exception ex){
                JOptionPane.showMessageDialog(RequestsFrame.this, "Нет соединения с БД.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //Возвращает выделенную заявку из таблицы
    private static BookPurchasing getSelectedPurchasing(){
        int idx = table.getSelectedRow();
        if(idx == -1) return null;
        return ((TableModel) table.getModel()).purchasings.get(idx);
    }
}
