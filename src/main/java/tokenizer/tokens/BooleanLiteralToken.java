package tokenizer.tokens;

public class BooleanLiteralToken extends Token {
    private boolean value;

    public BooleanLiteralToken(boolean value) {
        super(Token.TokenType.BOOLEAN_LITERAL);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
