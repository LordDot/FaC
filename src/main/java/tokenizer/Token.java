package tokenizer;

import java.util.Objects;

public class Token {
    public enum TokenType {
        KEYWORD_VOID,
        KEYWORD_INT,
        KEYWORD_RETURN,
        KEYWORD_IF,
        KEYWORD_ELSE,
        CURLY_BRACE_OPEN,
        CURLY_BRACE_CLOSE,
        PLUS,
        MINUS,
        STAR,
        SLASH,
        IDENTIFIER,
        BRACE_OPEN,
        BRACE_CLOSE,
        INT_LITERAL,
        SEMICOLON,
        EQUALS,
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
