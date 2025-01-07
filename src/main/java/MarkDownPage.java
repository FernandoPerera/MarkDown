import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownPage {

    private String content;

    private final static String LINKED_TEXT_REGEX = "\\[([^]]+)]\\(([^)]+)\\)";

    public MarkDownPage(String content) {
        this.content = content;
    }

    public void buildFooterMovingLinks() {
        HashMap<String, String> anchors = findLinksInPage();

        removeBrackets();
        replaceLinksWithAnchors(anchors);

        addLinksToFootNotes(anchors);
    }

    private void removeBrackets() {
        Matcher matcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(content);

        while (matcher.find()) {
            String linkedText = matcher.group(1);
            String linkedTextWithBrackets = String.format("[%s]", linkedText);
            content = content.replace(linkedTextWithBrackets, linkedText);
        }
    }

    private void addLinksToFootNotes(HashMap<String, String> anchors) {
        List<Map.Entry<String, String>> list = new ArrayList<>(anchors.entrySet());
        Collections.reverse(list);

        StringBuilder contentWithFootNotes = new StringBuilder(content);
        for (Map.Entry<String, String> entry : list) {
            contentWithFootNotes.append(String.format("\n%s: %s", entry.getKey(), entry.getValue()));
        }
        this.content = contentWithFootNotes.toString();
    }


    private void replaceLinksWithAnchors(HashMap<String, String> anchors) {
        anchors.forEach((anchor, url) -> {
            String linkWithParenthesis = String.format("\\(%s\\)", url);
            String anchorText = String.format(" %s", anchor);
            content = content.replaceAll(linkWithParenthesis, anchorText);
        });
    }

    private HashMap<String, String> findLinksInPage() {
        HashMap<String, String> anchors = new HashMap<>();
        int anchorIndex = 0;

        Matcher matcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(content);

        while (matcher.find()) {
            String url = matcher.group(2);
            boolean urlAppearMultipleTimes = anchors.containsValue(url);
            if (urlAppearMultipleTimes) {
                continue;
            }

            anchorIndex++;
            anchors.put(
                    String.format("[^anchor%s]", anchorIndex),
                    url
            );
        }
        return anchors;
    }

    public String getContent() {
        return this.content;
    }
}
