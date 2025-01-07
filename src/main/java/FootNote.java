import java.util.List;

public final class FootNote {

    private String content;

    private FootNote(String footNote) {
        this.content = footNote;
    }

    public static FootNote empty() {
        return new FootNote("");
    }

    public String getContent() {
        return this.content;
    }

    public void addLinks(List<Anchor> anchors) {
        String lineBreak = "\n";

        anchors.reversed().forEach((anchor) ->
                addFootnote(
                        String.format("%s%s: %s", lineBreak, anchor.getText(), anchor.getLink())
                )
        );
    }

    private void addFootnote(String footNote) {
        content = content + footNote;
    }
}
