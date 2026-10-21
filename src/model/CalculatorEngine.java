package model;

import error.CalculatorException;
import error.IncompleteExpressionException;
import eval.ExpressionEvaluator;
import eval.ExpressionFormatter;
import eval.NumberFormatter;

/**
 * Calculator state: the expression being typed, the latest result, angle mode,
 * and memory. The window only calls these methods and then reads the text.
 */
public final class CalculatorEngine {
    private static final String[] LONG_TOKENS = {
        "asin(", "acos(", "atan(", "sqrt(",
        "sin(", "cos(", "tan(", "log(", "ln(",
        "sqr(", "inv(", "abs(", "pi"
    };

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final MemoryRegister memory = new MemoryRegister();

    private String expression = "";
    private String history = "";
    private String result = "0";
    private String stableResult = "0";
    private String lastAnswer = "0";
    private boolean error;
    private boolean committedError;
    private boolean replaceOnNumber;
    private AngleMode angleMode = AngleMode.DEGREES;

    public String getExpressionText() {
        if (replaceOnNumber && history.length() > 0) {
            return ExpressionFormatter.pretty(history);
        }
        return ExpressionFormatter.pretty(expression);
    }

    public String getResultText() {
        if (result.length() == 0) {
            return "0";
        }
        return result;
    }

    public boolean isError() {
        return error;
    }

    public AngleMode getAngleMode() {
        return angleMode;
    }

    public boolean hasMemory() {
        return memory.hasValue();
    }

    public String getMemoryText() {
        if (!memory.hasValue()) {
            return "";
        }
        return NumberFormatter.format(memory.recall());
    }

    public void appendDigit(char digit) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            expression = "";
            replaceOnNumber = false;
        }
        if (endsWithClosedValue() || endsWithScientificNumber()) {
            expression += "*";
        }
        if (isLoneZero()) {
            if (digit == '0') {
                updatePreview();
                return;
            }
            expression = expression.substring(0, expression.length() - 1) + digit;
            updatePreview();
            return;
        }
        expression += digit;
        updatePreview();
    }

    public void appendDigits(String digits) {
        for (int i = 0; i < digits.length(); i++) {
            appendDigit(digits.charAt(i));
        }
    }

    public void appendDot() {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            expression = "0.";
            replaceOnNumber = false;
            updatePreview();
            return;
        }
        if (endsWithClosedValue() || endsWithScientificNumber()) {
            expression += "*0.";
            updatePreview();
            return;
        }
        String number = trailingNumber();
        if (number == null) {
            expression += "0.";
        } else if (number.indexOf('.') >= 0) {
            return;
        } else {
            expression += ".";
        }
        updatePreview();
    }

    public void appendOperator(char op) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            replaceOnNumber = false;
        }
        if (expression.length() == 0) {
            if (op == '-') {
                expression = "-";
            }
            updatePreview();
            return;
        }
        finishTrailingDot();
        if (lastChar() == '(') {
            if (op == '-') {
                expression += "-";
            }
            updatePreview();
            return;
        }
        if (endsWithUnaryMinus()) {
            if (op == '-') {
                updatePreview();
                return;
            }
            expression = expression.substring(0, expression.length() - 1);
            if (expression.length() > 0 && isOperator(lastChar())) {
                expression = expression.substring(0, expression.length() - 1) + op;
            } else if (expression.length() > 0 && lastChar() != '(') {
                expression += op;
            }
            updatePreview();
            return;
        }
        if (isOperator(lastChar())) {
            if (op == '-' && lastChar() != '-') {
                expression += "-";
            } else {
                expression = expression.substring(0, expression.length() - 1) + op;
            }
        } else {
            expression += op;
        }
        updatePreview();
    }

    public void appendFunction(String name) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            expression = "";
            replaceOnNumber = false;
        }
        finishTrailingDot();
        if (endsWithClosedValue() || endsWithDigitOrDot()) {
            expression += "*";
        }
        expression += name + "(";
        updatePreview();
    }

    public void appendConstant(String symbol) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            expression = "";
            replaceOnNumber = false;
        }
        finishTrailingDot();
        if (endsWithClosedValue() || endsWithDigitOrDot()) {
            expression += "*";
        }
        expression += symbol;
        updatePreview();
    }

    public void appendParen(char paren) {
        startFreshIfCommitted();
        if (paren == '(') {
            if (replaceOnNumber) {
                history = "";
                expression = "";
                replaceOnNumber = false;
            }
            finishTrailingDot();
            if (endsWithClosedValue() || endsWithDigitOrDot()) {
                expression += "*";
            }
            expression += "(";
            updatePreview();
            return;
        }
        if (replaceOnNumber || expression.length() == 0 || openParenCount() <= 0) {
            return;
        }
        char last = lastChar();
        if (last == '(' || isOperator(last) || last == '.') {
            return;
        }
        expression += ")";
        updatePreview();
    }

    public void appendFactorial() {
        appendPostfix('!');
    }

    public void appendPercent() {
        appendPostfix('%');
    }

    public void toggleSign() {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            replaceOnNumber = false;
        }
        if (expression.length() == 0) {
            expression = "-";
            updatePreview();
            return;
        }
        int start = startOfLastValue(expression);
        if (start < 0) {
            if (endsWithUnaryMinus()) {
                expression = expression.substring(0, expression.length() - 1);
            } else if (lastChar() == '(' || isOperator(lastChar())) {
                expression += "-";
            }
            updatePreview();
            return;
        }
        if (expression.charAt(start) == '-' && isUnaryPosition(expression, start)) {
            expression = expression.substring(0, start) + expression.substring(start + 1);
        } else {
            expression = expression.substring(0, start) + "-" + expression.substring(start);
        }
        updatePreview();
    }

    public void wrapLastValue(String functionName) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            expression = functionName + "(" + expression + ")";
            replaceOnNumber = false;
            updatePreview();
            return;
        }
        if (expression.length() == 0) {
            expression = functionName + "(";
            updatePreview();
            return;
        }
        int start = startOfLastValue(expression);
        if (start < 0) {
            finishTrailingDot();
            expression += functionName + "(";
        } else {
            String value = expression.substring(start);
            expression = expression.substring(0, start) + functionName + "(" + value + ")";
        }
        updatePreview();
    }

    public void appendAnswer() {
        insertNumber(lastAnswer);
    }

    public void backspace() {
        if (committedError || replaceOnNumber) {
            clear();
            return;
        }
        if (expression.length() == 0) {
            return;
        }
        for (int i = 0; i < LONG_TOKENS.length; i++) {
            String token = LONG_TOKENS[i];
            if (expression.endsWith(token)) {
                expression = expression.substring(0, expression.length() - token.length());
                updatePreview();
                return;
            }
        }
        expression = expression.substring(0, expression.length() - 1);
        updatePreview();
    }

    public void clear() {
        expression = "";
        history = "";
        result = "0";
        stableResult = "0";
        error = false;
        committedError = false;
        replaceOnNumber = false;
    }

    public void equals() {
        if (committedError || expression.length() == 0) {
            return;
        }
        try {
            double value = evaluator.evaluate(expression, angleMode);
            result = NumberFormatter.format(value);
            stableResult = result;
            history = expression + " =";
            lastAnswer = result;
            expression = result;
            replaceOnNumber = true;
            error = false;
            committedError = false;
        } catch (IncompleteExpressionException ex) {
            result = "Incomplete expression";
            error = true;
            committedError = true;
        } catch (CalculatorException ex) {
            result = ex.getMessage();
            error = true;
            committedError = true;
        }
    }

    public void toggleAngleMode() {
        angleMode = angleMode.other();
        if (replaceOnNumber && history.endsWith(" =")) {
            String original = history.substring(0, history.length() - 2);
            try {
                double value = evaluator.evaluate(original, angleMode);
                result = NumberFormatter.format(value);
                stableResult = result;
                lastAnswer = result;
                expression = result;
                history = original + " =";
                error = false;
                committedError = false;
            } catch (IncompleteExpressionException ex) {
                result = "Incomplete expression";
                error = true;
                committedError = true;
            } catch (CalculatorException ex) {
                result = ex.getMessage();
                error = true;
                committedError = true;
            }
            return;
        }
        updatePreview();
    }

    public void memoryClear() {
        memory.clear();
    }

    public void memoryRecall() {
        insertNumber(NumberFormatter.format(memory.recall()));
    }

    public void memoryAdd() {
        Double value = currentValue();
        if (value == null) {
            return;
        }
        memory.add(value.doubleValue());
    }

    public void memorySubtract() {
        Double value = currentValue();
        if (value == null) {
            return;
        }
        memory.subtract(value.doubleValue());
    }

    private void insertNumber(String number) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            expression = "";
            replaceOnNumber = false;
        }
        finishTrailingDot();
        if (endsWithClosedValue() || endsWithDigitOrDot()) {
            expression += "*";
        }
        if (number.startsWith("-")) {
            expression += "(" + number + ")";
        } else {
            expression += number;
        }
        updatePreview();
    }

    private void appendPostfix(char symbol) {
        startFreshIfCommitted();
        if (replaceOnNumber) {
            history = "";
            replaceOnNumber = false;
        }
        finishTrailingDot();
        if (!canPostfix()) {
            return;
        }
        expression += symbol;
        updatePreview();
    }

    private void startFreshIfCommitted() {
        if (!committedError) {
            return;
        }
        expression = "";
        history = "";
        result = "0";
        stableResult = "0";
        error = false;
        committedError = false;
        replaceOnNumber = false;
    }

    private void finishTrailingDot() {
        if (expression.length() > 0 && lastChar() == '.') {
            expression += "0";
        }
    }

    private void updatePreview() {
        history = "";
        if (expression.length() == 0) {
            result = "0";
            stableResult = "0";
            error = false;
            committedError = false;
            return;
        }
        try {
            double value = evaluator.evaluate(expression, angleMode);
            result = NumberFormatter.format(value);
            stableResult = result;
            error = false;
            committedError = false;
        } catch (IncompleteExpressionException ex) {
            result = stableResult;
            error = false;
            committedError = false;
        } catch (CalculatorException ex) {
            result = ex.getMessage();
            error = true;
            committedError = false;
        }
    }

    private Double currentValue() {
        if (committedError || error) {
            return null;
        }
        try {
            if (expression.length() > 0) {
                return Double.valueOf(evaluator.evaluate(expression, angleMode));
            }
            return Double.valueOf(Double.parseDouble(stableResult));
        } catch (CalculatorException ex) {
            return null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean canPostfix() {
        if (expression.length() == 0) {
            return false;
        }
        char last = lastChar();
        return Character.isDigit(last) || last == ')' || last == '!' || last == '%' || endsWithConstant();
    }

    private boolean endsWithClosedValue() {
        if (expression.length() == 0) {
            return false;
        }
        char last = lastChar();
        return last == ')' || last == '!' || last == '%' || endsWithConstant();
    }

    private boolean endsWithScientificNumber() {
        String number = trailingNumber();
        return number != null && number.indexOf('E') >= 0;
    }

    private boolean endsWithDigitOrDot() {
        if (expression.length() == 0) {
            return false;
        }
        char last = lastChar();
        return Character.isDigit(last) || last == '.';
    }

    private boolean endsWithConstant() {
        if (expression.endsWith("pi")) {
            int start = expression.length() - 2;
            return start == 0 || !Character.isLetter(expression.charAt(start - 1));
        }
        if (expression.endsWith("e")) {
            int start = expression.length() - 1;
            return start == 0 || !Character.isLetter(expression.charAt(start - 1));
        }
        return false;
    }

    private boolean endsWithUnaryMinus() {
        return expression.length() > 0 && isUnaryPosition(expression, expression.length() - 1);
    }

    private boolean isLoneZero() {
        return "0".equals(trailingNumber());
    }

    private String trailingNumber() {
        if (expression.length() == 0) {
            return null;
        }
        char last = lastChar();
        if (!Character.isDigit(last) && last != '.') {
            return null;
        }
        int start = numberStart(expression, expression.length() - 1);
        return expression.substring(start);
    }

    private char lastChar() {
        return expression.charAt(expression.length() - 1);
    }

    private static boolean isOperator(char current) {
        return current == '+' || current == '-' || current == '*' || current == '/' || current == '^';
    }

    private int openParenCount() {
        int count = 0;
        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);
            if (current == '(') {
                count++;
            } else if (current == ')') {
                count--;
            }
        }
        return count;
    }

    private static boolean isUnaryPosition(String expr, int index) {
        if (index < 0 || index >= expr.length() || expr.charAt(index) != '-') {
            return false;
        }
        if (index == 0) {
            return true;
        }
        char prev = expr.charAt(index - 1);
        return prev == '+' || prev == '-' || prev == '*' || prev == '/' || prev == '^' || prev == '(';
    }

    private static int startOfLastValue(String expr) {
        if (expr.length() == 0) {
            return -1;
        }
        int end = expr.length() - 1;
        while (end >= 0 && (expr.charAt(end) == '!' || expr.charAt(end) == '%')) {
            end--;
        }
        if (end < 0) {
            return -1;
        }
        int start;
        char current = expr.charAt(end);
        if (current == ')') {
            int index = end;
            int depth = 1;
            index--;
            while (index >= 0 && depth > 0) {
                char ch = expr.charAt(index);
                if (ch == ')') {
                    depth++;
                } else if (ch == '(') {
                    depth--;
                }
                index--;
            }
            if (depth != 0) {
                return -1;
            }
            while (index >= 0 && Character.isLetter(expr.charAt(index))) {
                index--;
            }
            start = index + 1;
        } else if (Character.isDigit(current) || current == '.') {
            start = numberStart(expr, end);
        } else if (current == 'i' && end >= 1 && expr.charAt(end - 1) == 'p'
                && (end < 2 || !Character.isLetter(expr.charAt(end - 2)))) {
            start = end - 1;
        } else if (current == 'e' && (end == 0 || !Character.isLetter(expr.charAt(end - 1)))) {
            start = end;
        } else {
            return -1;
        }
        if (start > 0 && isUnaryPosition(expr, start - 1)) {
            start--;
        }
        return start;
    }

    static int numberStart(String text, int end) {
        int index = end;
        if (index >= 0 && text.charAt(index) == '.') {
            index--;
            while (index >= 0 && Character.isDigit(text.charAt(index))) {
                index--;
            }
            return index + 1;
        }
        while (index >= 0 && Character.isDigit(text.charAt(index))) {
            index--;
        }
        if (index >= 0 && (text.charAt(index) == 'e' || text.charAt(index) == 'E')) {
            int afterMarker = index + 1;
            index--;
            if (index >= 0 && Character.isDigit(text.charAt(index))) {
                while (index >= 0 && Character.isDigit(text.charAt(index))) {
                    index--;
                }
                if (index >= 0 && text.charAt(index) == '.') {
                    int prev = index - 1;
                    if (prev >= 0 && Character.isDigit(text.charAt(prev))) {
                        index--;
                        while (index >= 0 && Character.isDigit(text.charAt(index))) {
                            index--;
                        }
                    }
                }
                return index + 1;
            }
            if (index >= 0 && text.charAt(index) == '.') {
                int prev = index - 1;
                if (prev >= 0 && Character.isDigit(text.charAt(prev))) {
                    index--;
                    while (index >= 0 && Character.isDigit(text.charAt(index))) {
                        index--;
                    }
                    return index + 1;
                }
                return index;
            }
            return afterMarker;
        }
        if (index >= 0 && (text.charAt(index) == '+' || text.charAt(index) == '-')) {
            if (index - 1 >= 0 && (text.charAt(index - 1) == 'e' || text.charAt(index - 1) == 'E')) {
                index -= 2;
                if (index >= 0 && Character.isDigit(text.charAt(index))) {
                    while (index >= 0 && Character.isDigit(text.charAt(index))) {
                        index--;
                    }
                }
                if (index >= 0 && text.charAt(index) == '.') {
                    int prev = index - 1;
                    if (prev >= 0 && Character.isDigit(text.charAt(prev))) {
                        index--;
                        while (index >= 0 && Character.isDigit(text.charAt(index))) {
                            index--;
                        }
                    } else {
                        return index;
                    }
                }
                return index + 1;
            }
            return index + 1;
        }
        if (index >= 0 && text.charAt(index) == '.') {
            int prev = index - 1;
            if (prev >= 0 && Character.isDigit(text.charAt(prev))) {
                index--;
                while (index >= 0 && Character.isDigit(text.charAt(index))) {
                    index--;
                }
                return index + 1;
            }
            return index;
        }
        return index + 1;
    }
}
