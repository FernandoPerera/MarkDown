import java.util.List;

public final class MarkDownPage {

    private final PageContent page;
    private final FootNote footNote = FootNote.empty();

    public MarkDownPage(String page) {
        this.page = PageContent.of(page);
    }

    public void buildFooterMovingLinks() {
        List<Anchor> anchors = page.findAnchors();

        page.removeBrackets();
        page.replaceLinksWithAnchors(anchors);

        footNote.addLinks(anchors);
    }

    public String getPage() {
        return this.page.getContent() + footNote.getContent();
    }
}
