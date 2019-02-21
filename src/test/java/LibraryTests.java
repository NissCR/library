import main.Library;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTests {

    @Test
    void formatDate(){
        Date date = new Date();
        date.setTime(1526054426460L);
        assertEquals("12.05.2018  00:00", Library.formatDate(date));
    }
}
