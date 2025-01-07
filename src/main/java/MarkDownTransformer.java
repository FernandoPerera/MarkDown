import java.io.*;
import java.util.HashMap;
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
        HashMap<String, String> anchors = new HashMap<>();

        Matcher visibleTextMatcher = Pattern.compile("\\[(.*?)]").matcher(content);
        visibleTextMatcher.find();
        String linkedText = visibleTextMatcher.group(1);

        Matcher urlMatcher = Pattern.compile("\\((.*?)\\)").matcher(content);
        urlMatcher.find();
        String url = urlMatcher.group(1);

        String contentWithoutTransformation = content.substring(content.indexOf(url) + url.length() + 1);
        String remainingContent = String.format("%s [^anchor1]%s", linkedText, contentWithoutTransformation);

        anchors.put("[^anchor1]", url);

        Matcher visibleTextMatcher2 = Pattern.compile("\\[(.*?)]").matcher(contentWithoutTransformation);
        Matcher urlMatcher2 = Pattern.compile("\\((.*?)\\)").matcher(contentWithoutTransformation);

        if (urlMatcher2.find() && visibleTextMatcher2.find()) {
            String visibleText2 = visibleTextMatcher2.group(1);
            String urlText2 = urlMatcher2.group(1);

            String contentWithoutSecondTransformation = contentWithoutTransformation.substring(contentWithoutTransformation.indexOf(urlText2) + urlText2.length() + 1);
            String secondTransformation = String.format("%s [^anchor2]", visibleText2);
            remainingContent = remainingContent.replace("[" + visibleText2 + "]", String.format("%s%s", secondTransformation, contentWithoutSecondTransformation)).replace("(" + urlText2 + ")", "");;

            anchors.put("[^anchor2]", urlText2);
        }
        
        String finalRemainingContent = remainingContent;
        StringBuffer finalContent = new StringBuffer(finalRemainingContent);
        
        anchors.forEach((key, value) -> {
            finalContent.insert(finalRemainingContent.length(), String.format("\n%s: %s", key, value));
        });
        return finalContent.toString();
    }

}
