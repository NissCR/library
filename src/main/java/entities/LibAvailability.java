package entities;

/**
 * Наличие в библиотеке
 * литературного труда
 */
public enum LibAvailability {
    AVAILABLE("В наличии"),
    MISSING("Отсутствует"),
    //ORDERED("Заказан");
    PURSHASING("Закупается");

    private String value;
    LibAvailability(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
