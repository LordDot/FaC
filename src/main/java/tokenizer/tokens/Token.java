package tokenizer.tokens;

public class Token {
    public enum TokenType {
        KEYWORD_VOID,
        KEYWORD_INT,
        KEYWORD_BOOL,
        KEYWORD_RETURN,
        KEYWORD_IF,
        KEYWORD_ELSE,
        KEYWORD_WHILE,
        KEYWORD_BREAK,
        KEYWORD_NOBREAK,
        CURLY_BRACE_OPEN,
        CURLY_BRACE_CLOSE,
        BRACE_OPEN,
        BRACE_CLOSE,
        PLUS,
        MINUS,
        STAR,
        SLASH,
        LEFT_SHIFT,
        RIGHT_SHIFT,
        BOOLEAN_AND,
        BOOLEAN_OR,
        SMALLER,
        GREATER,
        SMALLER_EQUAL,
        GREATER_EQUAL,
        COMPARE,
        NOT_EQUAL,
        EQUALS,
        EXCLAMATION,
        SEMICOLON,

        INT_LITERAL,
        BOOLEAN_LITERAL,
        IDENTIFIER,
    }

    private TokenType type;

    public Token(TokenType type) {
        this.type = type;
    }

    public TokenType getType(){
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return type == token.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                '}';
    }
}
