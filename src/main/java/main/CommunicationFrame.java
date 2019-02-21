package main;

import antlr.debug.MessageAdapter;
import entities.Message;
import entities.UserType;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * Связь студентов с
 * библиотекарем
 */
public class CommunicationFrame extends JInternalFrame {
    private static JList lQuestAns;
    private static JTextArea taQuestion;
    private static JButton btnAsk;
    private static JButton btnDelete;
    private static ArrayList<Message> messages;

    public CommunicationFrame() {
        super("Связь с библиотекарем", true, true, true);

        lQuestAns = new JList();

        messages = new ArrayList<>();

        taQuestion = new JTextArea();
        taQuestion.setColumns(20);
        taQuestion.setRows(3);

        btnAsk = new JButton();
        //Задать вопрос / ответить на впрос
        btnAsk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Library.user.getUserType().equals(UserType.STUDENT)){
                    //Задать вопрос
                    if(taQuestion.getText().equals("") || taQuestion.getText().equals("Не нашли ответ на вопрос? Задайте новый.")){
                        JOptionPane.showMessageDialog(CommunicationFrame.this,
                                "Введите вопрос.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    } else{
                        Library.libDAO.add(new Message(taQuestion.getText(), "", new Date(), Library.user));
                        JOptionPane.showMessageDialog(CommunicationFrame.this,
                                "Вопрос добавлен. Библиотекарь ответит на ваш вопрос по возможности.",
                                "Добавление вопроса", JOptionPane.PLAIN_MESSAGE);
                    }
                } else{
                    //Ответить на вопрос
                    if(taQuestion.getText().equals("") || taQuestion.getText().equals("Введите ответ.")){
                        JOptionPane.showMessageDialog(CommunicationFrame.this,
                                "Введите ответ.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Message selMessage = messages.get(lQuestAns.getSelectedIndex());
                        selMessage.setAnswer(taQuestion.getText());
                        Library.libDAO.update(selMessage);
                        setListData();
                        taQuestion.setText("Введите ответ.");
                    }
                }
            }
        });
        btnDelete = new JButton("Удалить вопрос");
        //Удалить вопрос
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Library.libDAO.delete(messages.get(lQuestAns.getSelectedIndex()));
                    JOptionPane.showMessageDialog(CommunicationFrame.this,
                            "Вопрос удален.", "Удаление", JOptionPane.PLAIN_MESSAGE);
                    setListData();
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(CommunicationFrame.this,
                            "Нет соединения с БД.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setElementsAct();

        JPanel pAsking = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pAsking.add(taQuestion);
        pAsking.add(btnAsk);
        pAsking.add(btnDelete);

        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(new JScrollPane(lQuestAns));
        content.add(pAsking);

        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                super.internalFrameActivated(e);
                setListData();
            }
        });
    }

    private static void setElementsAct(){
        if(Library.user.getUserType().equals(UserType.LIBRARIAN)) {
            taQuestion.setText("Введите ответ.");
            btnAsk.setText("Ответить на вопрос");
        } else {
            taQuestion.setText("Не нашли ответ на вопрос? Задайте новый.");
            btnAsk.setText("Задать вопрос");
            btnDelete.setVisible(false);
        }
    }

    //Загрузка данных вопрос/ответ
    private static void setListData(){
        ArrayList<Message> messagesDB = Library.libDAO.getAllMessages();
        String[] listData = new String[messagesDB.size()];
        //TODO студент / библиотекарь?
        int i = 0;
        for(Message message : messagesDB){
            String student = "";
            if(Library.user.getUserType().equals(UserType.LIBRARIAN)){
                student = message.getStudent().getLibCardNum();
            }
            if(Library.user.getUserType().equals(UserType.STUDENT) && message.getAnswer().equals("")) {
                i--;
                continue;
            }
            String builder = "<html><p><b>Вопрос</b> (" + Library.formatDate(message.getDate()) + student + "): " +
                message.getQuestion() + "</p><p><b>Ответ</b>: " + message.getAnswer() + "</p></html>";
            listData[i] = builder;
            i++;
        }
        messages = messagesDB;
        lQuestAns.setListData(listData);
    }
}
