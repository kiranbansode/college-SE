package eval;

import error.CalculatorException;
import error.IncompleteExpressionException;
import model.AngleMode;
import op.OperationRegistry;

/**
 * Turns a finished expression into a number, honoring operator precedence.
 */
public final class ExpressionEvaluator {
    private final OperationRegistry operations = new OperationRegistry();
    private Lexer lexer;
    private Token current;
    private AngleMode mode;

    public double evaluate(String expression, AngleMode mode) {
        if (expression == null || expression.trim().length() == 0) {
            throw new IncompleteExpressionException("Incomplete expression");
        }
        this.mode = mode;
        this.lexer = new Lexer(expression);
        this.current = lexer.next();
        double value = parseExpression();
        if (current.kind != Token.Kind.EOF) {
            throw new CalculatorException("Unexpected '" + current.text + "'");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new CalculatorException("Math error");
        }
        return value;
    }

    private void advance() {
        current = lexer.next();
    }

    private double parseExpression() {
        double value = parseTerm();
        while (current.kind == Token.Kind.PLUS || current.kind == Token.Kind.MINUS) {
            Token.Kind operator = current.kind;
            advance();
            double right = parseTerm();
            if (operator == Token.Kind.PLUS) {
                value = operations.binary("+", value, right, mode);
            } else {
                value = operations.binary("-", value, right, mode);
            }
        }
        return value;
    }

    private double parseTerm() {
        double value = parsePower();
        while (current.kind == Token.Kind.STAR || current.kind == Token.Kind.SLASH) {
            Token.Kind operator = current.kind;
            advance();
            double right = parsePower();
            if (operator == Token.Kind.STAR) {
                value = operations.binary("*", value, right, mode);
            } else {
                value = operations.binary("/", value, right, mode);
            }
        }
        return value;
    }

    private double parsePower() {
        double value = parseUnary();
        if (current.kind == Token.Kind.CARET) {
            advance();
            double exponent = parsePower();
            value = operations.binary("^", value, exponent, mode);
        }
        return value;
    }

    private double parseUnary() {
        if (current.kind == Token.Kind.MINUS || current.kind == Token.Kind.PLUS) {
            Token.Kind operator = current.kind;
            advance();
            double value = parseUnary();
            if (operator == Token.Kind.MINUS) {
                return -value;
            }
            return value;
        }
        return parsePostfix();
    }

    private double parsePostfix() {
        double value = parsePrimary();
        while (true) {
            if (current.kind == Token.Kind.FACT) {
                advance();
                value = operations.unary("fact", value, mode);
            } else if (current.kind == Token.Kind.PERCENT) {
                advance();
                value = operations.unary("percent", value, mode);
            } else if (startsPrimary(current.kind)) {
                value = value * parsePrimary();
            } else {
                break;
            }
        }
        return value;
    }

    private double parsePrimary() {
        if (current.kind == Token.Kind.NUMBER) {
            double value;
            try {
                value = Double.parseDouble(current.text);
            } catch (NumberFormatException ex) {
                throw new CalculatorException("Invalid number");
            }
            advance();
            return value;
        }
        if (current.kind == Token.Kind.IDENT) {
            String name = current.text;
            advance();
            if ("pi".equals(name)) {
                return Math.PI;
            }
            if ("e".equals(name)) {
                return Math.E;
            }
            if (current.kind != Token.Kind.LPAREN) {
                throw new IncompleteExpressionException("Incomplete expression");
            }
            advance();
            double argument = parseExpression();
            if (current.kind != Token.Kind.RPAREN) {
                throw new IncompleteExpressionException("Incomplete expression");
            }
            advance();
            return operations.unary(name, argument, mode);
        }
        if (current.kind == Token.Kind.LPAREN) {
            advance();
            double value = parseExpression();
            if (current.kind != Token.Kind.RPAREN) {
                throw new IncompleteExpressionException("Incomplete expression");
            }
            advance();
            return value;
        }
        throw new IncompleteExpressionException("Incomplete expression");
    }

    private static boolean startsPrimary(Token.Kind kind) {
        return kind == Token.Kind.NUMBER || kind == Token.Kind.IDENT || kind == Token.Kind.LPAREN;
    }
}
