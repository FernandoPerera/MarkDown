import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PageContent {

    private String content;

    private final static String LINKED_TEXT_REGEX = "\\[([^]]+)]\\(([^)]+)\\)";

    private PageContent(String content) {
        this.content = content;
    }

    public static PageContent of(String content) {
        return new PageContent(content);
    }

    public List<Anchor> findAnchors() {
        List<Anchor> anchors = new ArrayList<>();
        int anchorIndex = 0;

        Matcher matcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(this.content);

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

    public void removeBrackets() {
        Matcher matcher = Pattern.compile(LINKED_TEXT_REGEX).matcher(this.content);

        while (matcher.find()) {
            String linkedText = matcher.group(1);
            String linkedTextWithBrackets = String.format("[%s]", linkedText);
            this.content = this.content.replace(linkedTextWithBrackets, linkedText);
        }
    }

    public void replaceLinksWithAnchors(List<Anchor> anchors) {
        Collections.reverse(anchors);

        anchors.forEach((anchor) -> {
            String linkWithParenthesis = String.format("\\(%s\\)", anchor.getLink());
            String anchorText = String.format(" %s", anchor.getText());
            this.content = this.content.replaceAll(linkWithParenthesis, anchorText);
        });
    }

    public String getContent() {
        return this.content;
    }
}
