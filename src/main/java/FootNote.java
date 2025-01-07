import java.util.List;

public final class FootNote {

    private String content = "";

    public String getContent() {
        return this.content;
    }

    public void addLinks(List<Anchor> anchors) {
        String lineBreak = "\n";

        anchors.reversed().forEach((anchor) ->
                addFootnote(
                        String.format("%s%s: %s", lineBreak, anchor.get(), anchor.getLink())
                )
        );
    }

    private void addFootnote(String footNote) {
        this.content = content + footNote;
    }
}
