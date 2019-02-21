package entities;

/**
 * Жанры книг
 */
public enum Genre {
    FICTION("Художественная"),
    SCIENTIFIC("Научная"),
    CLASSIC("Классическая"),
    PERIODICAL("Периодическая"),
    TEACHING_AIDS("Учебные пособия");

    String value;
    Genre(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Genre fromString(String value){
        if(value != null){
            for(Genre genre : Genre.values()){
                if(genre.getValue().equalsIgnoreCase(value))
                    return genre;
            }
        }
        //throw new IllegalArgumentException("No such value");
        return null;
    }
}


