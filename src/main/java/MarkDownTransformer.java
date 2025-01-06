import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownTransformer {

    public void execute(String inputFilePath, String outputFilePath) {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

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

        Matcher visibleTextMatcher = Pattern.compile("\\[(.*?)]").matcher(content);
        visibleTextMatcher.find();
        String visibleText = visibleTextMatcher.group(1);

        Matcher urlMatcher = Pattern.compile("\\((.*?)\\)").matcher(content);
        urlMatcher.find();
        String urlText = urlMatcher.group(1);

        String contentToInput = visibleText + " [^anchor1]\n[^anchor1]: " + urlText;

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(contentToInput);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
