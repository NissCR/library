package entities;

/**
 * Типы пользователей
 */
public enum UserType {
    /*LIBRARIAN,
    STUDENT,
    TEACHER;*/
    LIBRARIAN("Библиотекарь"),
    STUDENT("Студент"),
    TEACHER("Преподаватель");

    private String value;
    UserType(String value){
        this.value = value;
    }

    public String getValue(){return value;}
}
