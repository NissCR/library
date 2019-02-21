package auth;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() ->{
            //Открыть окно авторизации
            new Authorization();
        });
    }
}