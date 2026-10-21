package eval;

import error.CalculatorException;

/**
 * Splits an expression into numbers, names, and operators.
 */
public final class Lexer {
    private final String input;
    private final int length;
    private int pos;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
    }

    public Token next() {
        skipSpaces();
        if (pos >= length) {
            return new Token(Token.Kind.EOF, "");
        }
        char current = input.charAt(pos);
        if (Character.isDigit(current) || isDotDigit(current)) {
            return readNumber();
        }
        if (current == '\u03c0') {
            pos++;
            return new Token(Token.Kind.IDENT, "pi");
        }
        if (Character.isLetter(current)) {
            return readIdent();
        }
        pos++;
        switch (current) {
            case '+':
                return new Token(Token.Kind.PLUS, "+");
            case '-':
                return new Token(Token.Kind.MINUS, "-");
            case '*':
            case '\u00d7':
                return new Token(Token.Kind.STAR, "*");
            case '/':
            case '\u00f7':
                return new Token(Token.Kind.SLASH, "/");
            case '^':
                return new Token(Token.Kind.CARET, "^");
            case '%':
                return new Token(Token.Kind.PERCENT, "%");
            case '!':
                return new Token(Token.Kind.FACT, "!");
            case '(':
                return new Token(Token.Kind.LPAREN, "(");
            case ')':
                return new Token(Token.Kind.RPAREN, ")");
            default:
                throw new CalculatorException("Unexpected '" + current + "'");
        }
    }

    private boolean isDotDigit(char current) {
        return current == '.' && pos + 1 < length && Character.isDigit(input.charAt(pos + 1));
    }

    private void skipSpaces() {
        while (pos < length && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }

    private Token readNumber() {
        int start = pos;
        if (input.charAt(pos) == '.') {
            pos++;
        }
        while (pos < length && Character.isDigit(input.charAt(pos))) {
            pos++;
        }
        if (input.charAt(start) != '.' && pos < length && input.charAt(pos) == '.') {
            pos++;
            while (pos < length && Character.isDigit(input.charAt(pos))) {
                pos++;
            }
        }
        if (pos < length && (input.charAt(pos) == 'e' || input.charAt(pos) == 'E')) {
            int mark = pos;
            int probe = pos + 1;
            if (probe < length && (input.charAt(probe) == '+' || input.charAt(probe) == '-')) {
                probe++;
            }
            if (probe < length && Character.isDigit(input.charAt(probe))) {
                pos = probe;
                while (pos < length && Character.isDigit(input.charAt(pos))) {
                    pos++;
                }
            } else {
                pos = mark;
            }
        }
        return new Token(Token.Kind.NUMBER, input.substring(start, pos));
    }

    private Token readIdent() {
        int start = pos;
        while (pos < length && Character.isLetter(input.charAt(pos))) {
            pos++;
        }
        String text = input.substring(start, pos);
        if ("pi".equals(text) || "\u03c0".equals(text)) {
            text = "pi";
        }
        return new Token(Token.Kind.IDENT, text);
    }
}
