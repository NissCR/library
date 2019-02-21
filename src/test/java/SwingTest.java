import main.Library;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.QueueTool;
import org.netbeans.jemmy.operators.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SwingTest {
    JFrameOperator mainFrame;
    QueueTool mainQueue;

    @BeforeEach
    void getFrame(){
        Library.startApp();
        mainFrame = new JFrameOperator();
        //Приостановление очереди выполнения событий
        //для удобного просмотра
        mainQueue = new QueueTool();
        mainQueue.waitEmpty(200);
    }

    @BeforeAll
    static void setTimeouts(){
        //Скорость набора символов
        JemmyProperties.setCurrentTimeout("JTextComponentOperator.PushKeyTimeout", 50);
        //Максимальное время ожидания
        JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", 10000);
        //Максимальное время ожидания всех компонентов
        JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentOperator", 1000);
    }

    @AfterEach
    void setDown(){
        mainFrame.dispose();
    }

    @Test
    void testTitleFrame(){
        String titleFrame = mainFrame.getTitle();
        assertEquals("Библиотечный каталог - Библиотекарь", titleFrame);
        mainQueue.waitEmpty(100);
    }

    @Test
    void testMenu(){
        JMenuOperator menuBook = new JMenuOperator(mainFrame, "Книги");
        assertNotNull(menuBook);
        menuBook.push();
        JMenuItemOperator itemFind = new JMenuItemOperator(mainFrame, "Поиск");
        assertNotNull(itemFind);
        itemFind.push();
        new JInternalFrameOperator(mainFrame, "Поиск книг").dispose();

        JMenuOperator menuRequest = new JMenuOperator(mainFrame, "Заявки");
        assertNotNull(menuRequest);
        menuRequest.push();
        JMenuItemOperator itemReqSearch = new JMenuItemOperator(mainFrame, "Просмотр заявок на закупку");
        assertNotNull(itemReqSearch);
        JMenuItemOperator itemFormOrders = new JMenuItemOperator(mainFrame, "Сформировать список книг на заказ");
        assertNotNull(itemFormOrders);
        JMenuItemOperator itemFormPurch = new JMenuItemOperator(mainFrame, "Сформировать список книг на закупку");
        assertNotNull(itemFormPurch);
/*
        JMenuOperator menuOther = new JMenuOperator(mainFrame, "Дополнительно");
        assertNotNull(menuOther);
        menuOther.push();
        JMenuItemOperator itemDebts = new JMenuItemOperator(mainFrame, "Долги");
        assertNotNull(itemDebts);
        menuOther.push();
        JMenuItemOperator itemComm = new JMenuItemOperator(mainFrame, "Связь с библиотекарем");
        assertNotNull(itemComm);
        itemComm.push();*/
    }

    @Test
    void testActionFind(){
        //Находим меню Книги
        JMenuOperator menuBooks = new JMenuOperator(mainFrame, "Книги");
        //Кликаем
        menuBooks.push();
        //Находим элемент меню Поиск
        JMenuItemOperator itemFind = new JMenuItemOperator(mainFrame, "Поиск");
        //Осуществляем клик
        itemFind.push();

        //Внутреннее окно поиска книг
        JInternalFrameOperator internalFrame = new JInternalFrameOperator(mainFrame, "Поиск книг");
        assertNotNull(internalFrame);
        //Проверка названия
        assertEquals("Поиск книг", internalFrame.getTitle());
        JComboBoxOperator comboBox = new JComboBoxOperator(internalFrame);
        //System.out.println(comboBox.getItemAt(0) + "___");
        //Поиск по названию
        comboBox.setSelectedIndex(0);
        JTextFieldOperator textField  = new JTextFieldOperator(internalFrame);
        //Ввод названия
        textField.setText("А");
        //Запуск поиска
        JButtonOperator btnFind = new JButtonOperator(internalFrame, "Поиск");
        btnFind.push();
        //Проверка наличия хотя бы 1го элемента в таблице
        JTableOperator table = new JTableOperator(internalFrame);
        assertNotEquals(0, table.getModel().getRowCount());
        //Выделение 1ой строки
        table.setRowSelectionInterval(0, 0);
        //System.out.println(table.getSelectedRow());
    }

    @Test
    void testActionDebts(){
        new JMenuOperator(mainFrame, "Дополнительно").push();
        new JMenuItemOperator(mainFrame, "Долги").push();
        //JOptionPane
    }

    @Test
    void testActionRequest(){
        new JMenuOperator(mainFrame, "Заявки").push();
        new JMenuItemOperator(mainFrame, "Просмотр заявок на закупку").push();

        //Внутреннее окно заявок
        JInternalFrameOperator internalFrame = new JInternalFrameOperator(mainFrame, "Заявки");
        assertNotNull(internalFrame);
        JTableOperator table = new JTableOperator(internalFrame);
        assertNotEquals(0, table.getModel().getRowCount());
        //Поле деталей
        JTextAreaOperator textArea = new JTextAreaOperator(internalFrame, 0);
        assertEquals("...Подробности...", textArea.getText());
        table.setRowSelectionInterval(0, 0);
        assertEquals("id: " + table.getModel().getValueAt(0, 0),
                textArea.getText(0, textArea.getText().indexOf('\n')));
    }

    @Test
    void testActionCommunication(){
        new JMenuOperator(mainFrame, "Дополнительно").push();
        new JMenuItemOperator(mainFrame, "Связь с библиотекарем").push();

        //Внутреннее окно связи
        JInternalFrameOperator internalFrame = new JInternalFrameOperator(mainFrame);
        assertNotNull(internalFrame);
        JListOperator list = new JListOperator(internalFrame);
        assertNotEquals(0, list.getVisibleRowCount());
    }
}