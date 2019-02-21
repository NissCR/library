package auth;

import Utils.LibDAO;
import entities.User;
import main.Library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Authorization extends JFrame{
    public Authorization(){
        super("Авторизация");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        LibDAO libDAO = new LibDAO();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        //Панель для размещения логина
        JPanel login = new JPanel();
        login.setLayout(new BoxLayout(login, BoxLayout.X_AXIS));
        login.add(new JLabel("Номер читательского билета:"));
        login.add(Box.createHorizontalStrut(15));
        JTextField tfLogin = new JTextField(10);
        //TODO - проверка на ввод не чисел
        login.add(tfLogin);

        //Панель пароля
        JPanel password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
        password.add(new JLabel("Пароль:"));
        password.add(Box.createHorizontalStrut(30));
        JPasswordField tfPassword = new JPasswordField(6);
        password.add(tfPassword);

        //Панель кнопок
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        JButton btnOk = new JButton("Войти");
        JButton btnCancel = new JButton("Отмена");

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Проверка на незаполненные поля
                if(tfLogin.getText().isEmpty() && tfPassword.getPassword().length == 0){
                    JOptionPane.showMessageDialog(Authorization.this, "Не все поля заполнены",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else {
                    User user = null;
                    try {
                        user = libDAO.getUser(tfLogin.getText(), getPassword(tfPassword.getPassword()));
                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(Authorization.this, "Ошибка соединения с БД.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(user == null){
                        JOptionPane.showMessageDialog(Authorization.this, "Неверная пара логин / пароль",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    } else{
                        JOptionPane.showMessageDialog(Authorization.this, "Вход осуществлен",
                                "Вход", JOptionPane.PLAIN_MESSAGE);
                        //Открытие главного окна
                        new Library(user);
                        dispose();
                    }
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());

        //Размещение панелей
        gridPanel.add(btnOk);
        gridPanel.add(btnCancel);
        buttons.add(gridPanel);

        panel.add(login);
        panel.add(Box.createVerticalStrut(10));
        panel.add(password);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttons);
        panel.add(Box.createVerticalStrut(10));
        getContentPane().add(panel);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);    //Расположить по ценру экрана
        setVisible(true);
    }

    public String getPassword(char[] array){
        StringBuilder strBuilder = new StringBuilder();
        for(char c : array)
            strBuilder.append(c);
        return strBuilder.toString();
    }
}
