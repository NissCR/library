import com.itextpdf.layout.element.Paragraph;
import fileFormat.PDFCreator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PDFCreatorTest {

    @Test
    void createPDF(){
        try {
            PDFCreator.createPDF("Test", "xx.xx.xxxx-xx.xx.xxxx", new ArrayList<Paragraph>());
        } catch (IOException e) {
            e.printStackTrace();

        }
        File file = new File("pdf/Test(xx.xx.xxxx-xx.xx.xxxx).pdf");
        assertTrue(file.exists());
        file.delete();
    }
}
