import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownTransformer {

    private final FileManager fileManager;

    private final static String LINKED_TEXT_REGEX = "\\[([^]]+)]\\(([^)]+)\\)";

    public MarkDownTransformer(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void execute(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        fileManager.verifyExistence(inputFile);
        fileManager.verifyExistence(outputFile);

        String inputFileContent = fileManager.read(inputFile);
        String transformedContent = transformContentWithAnchor(inputFileContent, 1);

        fileManager.writeContent(transformedContent, outputFile);
    }

    private String transformContentWithAnchor(String content, int anchorNumber) {
        Matcher linkedTextMatcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(content);
        boolean needsTransformation = linkedTextMatcher.find();

        if (needsTransformation) {
            String linkedText = linkedTextMatcher.group(1);
            String url = linkedTextMatcher.group(2);

            String contentWithoutTransformation = content.substring(content.indexOf(url) + url.length() + 1);
            String replacedTextByAnchor = String.format("%s [^anchor%s]%s", linkedText, anchorNumber, contentWithoutTransformation);
            String lineBreak = "\n";

            StringBuffer transformedContent = new StringBuffer()
                    .append(getInitialContent(content, linkedText))
                    .append(replacedTextByAnchor)
                    .append(lineBreak)
                    .append(buildFootnote(anchorNumber, url));

            Matcher linkedTextInRestMatcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(transformedContent);
            boolean needsMoreTransformation = linkedTextInRestMatcher.find();

            return needsMoreTransformation
                    ? transformContentWithAnchor(transformedContent.toString(), anchorNumber + 1)
                    : transformedContent.toString();
        }
        
        return content;
    }

    private static String getInitialContent(String content, String linkedText) {
        return content.substring(0, content.indexOf("[" + linkedText + "]"));
    }

    private static String buildFootnote(int anchorNumber, String url) {
        return String.format("[^anchor%s]: %s", anchorNumber, url);
    }

}
