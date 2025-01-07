import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkDownPage {

    private String content;
    private final FootNote footNote = new FootNote();

    private final static String LINKED_TEXT_REGEX = "\\[([^]]+)]\\(([^)]+)\\)";

    public MarkDownPage(String content) {
        this.content = content;
    }

    public void buildFooterMovingLinks() {
        List<Anchor> anchors = findLinksInPage();

        removeBrackets();
        replaceLinksWithAnchors(anchors);

        footNote.addLinks(anchors);
    }

    private void removeBrackets() {
        Matcher matcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(content);

        while (matcher.find()) {
            String linkedText = matcher.group(1);
            String linkedTextWithBrackets = String.format("[%s]", linkedText);
            content = content.replace(linkedTextWithBrackets, linkedText);
        }
    }

    private void replaceLinksWithAnchors(List<Anchor> anchors) {
        Collections.reverse(anchors);

        anchors.forEach((anchor) -> {
            String linkWithParenthesis = String.format("\\(%s\\)", anchor.getLink());
            String anchorText = String.format(" %s", anchor.get());
            content = content.replaceAll(linkWithParenthesis, anchorText);
        });
    }

    private List<Anchor> findLinksInPage() {
        List<Anchor> anchors = new ArrayList<>();
        int anchorIndex = 0;

        Matcher matcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(content);

        while (matcher.find()) {
            String url = matcher.group(2);
            boolean urlAppearMultipleTimes = anchors.stream().anyMatch(anchor -> anchor.matches(url));
            if (urlAppearMultipleTimes) {
                continue;
            }

            anchorIndex++;
            Anchor anchor = Anchor.of(url, String.format("[^anchor%s]", anchorIndex));
            anchors.add(anchor);
        }
        return anchors;
    }

    public String getContent() {
        return this.content + footNote.getContent();
    }
}
