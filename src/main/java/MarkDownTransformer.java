import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownTransformer {

    public void execute(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        verifyFileExistence(inputFile);
        verifyFileExistence(outputFile);

        String inputFileContent = readFile(inputFile);
        String transformedContent = getTransformedContent(inputFileContent);

        writeContentInFile(transformedContent, outputFile);
    }

    private void verifyFileExistence(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
    }

    private void writeContentInFile(String contentToInput, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(contentToInput);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTransformedContent(String content) {
        Matcher visibleTextMatcher = Pattern.compile("\\[(.*?)]").matcher(content);
        visibleTextMatcher.find();
        String visibleText = visibleTextMatcher.group(1);

        Matcher urlMatcher = Pattern.compile("\\((.*?)\\)").matcher(content);
        urlMatcher.find();
        String urlText = urlMatcher.group(1);

        return visibleText + " [^anchor1]\n[^anchor1]: " + urlText;
    }

    private String readFile(File file) {
        String content = "";

        try (FileReader reader = new FileReader(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            int accumulator;
            while ((accumulator = reader.read()) != -1) {
                stringBuilder.append((char) accumulator);
            }
            content = stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }

}
