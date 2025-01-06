import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownTransformer {

    public void execute(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        if (!inputFile.exists()) {
            throw new FileNotFoundException();
        }

        if (!outputFile.exists()) {
            throw new FileNotFoundException();
        }

        String inputFileContent = readFile(inputFilePath);
        String transformedContent = getTransformedContent(inputFileContent);

        writeContentInFile(transformedContent, outputFilePath);
    }

    private static void writeContentInFile(String contentToInput, String outputFilePath) {
        File outputFile = new File(outputFilePath);

        try (FileWriter writer = new FileWriter(outputFile)) {
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

    private String readFile(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        String content = "";

        try (FileReader reader = new FileReader(inputFile)) {
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
