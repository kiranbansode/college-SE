package eval;

public final class Token {
    public enum Kind {
        NUMBER,
        IDENT,
        PLUS,
        MINUS,
        STAR,
        SLASH,
        CARET,
        PERCENT,
        FACT,
        LPAREN,
        RPAREN,
        EOF
    }

    public final Kind kind;
    public final String text;

    public Token(Kind kind, String text) {
        this.kind = kind;
        this.text = text;
    }
}
