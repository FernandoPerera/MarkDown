public final class Anchor {
    private final String link;
    private final String text;

    private Anchor(String link, String text) {
        this.link = link;
        this.text = text;
    }

    public static Anchor of(String link, String text) {
        return new Anchor(link, text);
    }

    public boolean matches(String link) {
        return this.link.equals(link);
    }

    public String getLink() {
        return this.link;
    }

    public String getText() {
        return this.text;
    }
}
