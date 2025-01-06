import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

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
    void transform_content_of_input_file_by_writing_to_output_file() throws IOException {
        String expectedContent = "This book [^anchor1]\n[^anchor1]: https://bestbooks/thisbook";
        String inputContent = "[This book](https://bestbooks/thisbook)";
        writeInInputFile(inputContent);

        transformer.execute(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

        String content = readOutputFile();
        assertEquals(expectedContent, content);
    }

    @Test
    void not_transform_when_input_file_does_not_exist() {
        String nonexistentInputPath = "./nonexistentFile.md";

        assertThrows(
                FileNotFoundException.class,
                () -> transformer.execute(nonexistentInputPath, outputFile.getAbsolutePath())
        );
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