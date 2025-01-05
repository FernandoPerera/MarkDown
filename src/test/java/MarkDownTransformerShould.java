import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
class MarkDownTransformerShould {

    private static File inputFile;
    private static File outputFile;
    private FileReader reader;
    private FileWriter writer;

    private final MarkDownTransformer transformer = new MarkDownTransformer();

    @BeforeEach
    void setUp() throws IOException {
        inputFile = new File("./inputFile.md");
        outputFile = new File("./outputFile.md");
        inputFile.createNewFile();
        outputFile.createNewFile();

        writer = new FileWriter(inputFile);
        reader = new FileReader(outputFile);
    }

    @AfterAll
    static void tearDown() {
        inputFile.delete();
        outputFile.delete();
    }

    @Test
    void turn_links_into_footnotes_and_replace_anchors() throws IOException {
        String expectedContent = "This book [^anchor1]\n[^anchor1]: https://bestbooks/thisbook";
        String inputContent = "[This book](https://bestbooks/thisbook)";
        writeInInputFile(inputContent);

        transformer.execute(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

        String content = readOutputFile();
        assertEquals(expectedContent, content);
    }

    private void writeInInputFile(String data) throws IOException {
        writer.write(data);
        writer.flush();
    }

    private String readOutputFile() throws IOException {
        StringBuilder content = new StringBuilder();
        int accumulator;
        while ((accumulator = reader.read()) != -1) {
            content.append((char) accumulator);
        }
        return content.toString();
    }
  
}