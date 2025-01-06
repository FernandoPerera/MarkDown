import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownTransformer {

    private final FileManager fileManager;

    public MarkDownTransformer(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void execute(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        fileManager.verifyExistence(inputFile);
        fileManager.verifyExistence(outputFile);

        String inputFileContent = fileManager.read(inputFile);
        String transformedContent = getTransformedContent(inputFileContent);

        fileManager.writeContent(transformedContent, outputFile);
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

}
