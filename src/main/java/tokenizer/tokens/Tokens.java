package tokenizer.tokens;

import static tokenizer.tokens.Token.TokenType;

public class Tokens {
    private static final Token TOKEN_KEYWORD_VOID = new Token(TokenType.KEYWORD_VOID);
    private static final Token TOKEN_KEYWORD_INT = new Token(TokenType.KEYWORD_INT);
    private static final Token TOKEN_KEYWORD_BOOL = new Token(TokenType.KEYWORD_BOOL);
    private static final Token TOKEN_KEYWORD_RETURN = new Token(TokenType.KEYWORD_RETURN);
    private static final Token TOKEN_KEYWORD_IF = new Token(TokenType.KEYWORD_IF);
    private static final Token TOKEN_KEYWORD_ELSE = new Token(TokenType.KEYWORD_ELSE);
    private static final Token TOKEN_KEYWORD_WHILE = new Token(TokenType.KEYWORD_WHILE);
    private static final Token TOKEN_KEYWORD_BREAK = new Token(TokenType.KEYWORD_BREAK);
    private static final Token TOKEN_KEYWORD_NOBREAK = new Token(TokenType.KEYWORD_NOBREAK);
    private static final Token TOKEN_CURLY_BRACE_OPEN = new Token(TokenType.CURLY_BRACE_OPEN);
    private static final Token TOKEN_CURLY_BRACE_CLOSE = new Token(TokenType.CURLY_BRACE_CLOSE);
    private static final Token TOKEN_BRACE_OPEN = new Token(TokenType.BRACE_OPEN);
    private static final Token TOKEN_BRACE_CLOSE = new Token(TokenType.BRACE_CLOSE);
    private static final Token TOKEN_PLUS = new Token(TokenType.PLUS);
    private static final Token TOKEN_MINUS = new Token(TokenType.MINUS);
    private static final Token TOKEN_STAR = new Token(TokenType.STAR);
    private static final Token TOKEN_SLASH = new Token(TokenType.SLASH);
    private static final Token TOKEN_LEFT_SHIFT = new Token(TokenType.LEFT_SHIFT);
    private static final Token TOKEN_RIGHT_SHIFT = new Token(TokenType.RIGHT_SHIFT);
    private static final Token TOKEN_BOOLEAN_AND = new Token(TokenType.BOOLEAN_AND);
    private static final Token TOKEN_BOOLEAN_OR = new Token(TokenType.BOOLEAN_OR);
    private static final Token TOKEN_SMALLER = new Token(TokenType.SMALLER);
    private static final Token TOKEN_GREATER = new Token(TokenType.GREATER);
    private static final Token TOKEN_SMALLER_EQUAL = new Token(TokenType.SMALLER_EQUAL);
    private static final Token TOKEN_GREATER_EQUAL = new Token(TokenType.GREATER_EQUAL);
    private static final Token TOKEN_COMPARE = new Token(TokenType.COMPARE);
    private static final Token TOKEN_NOT_EQUAL = new Token(TokenType.NOT_EQUAL);
    private static final Token TOKEN_EQUALS = new Token(TokenType.EQUALS);
    private static final Token TOKEN_EXCLAMATION = new Token(TokenType.EXCLAMATION);
    private static final Token TOKEN_SEMICOLON = new Token(TokenType.SEMICOLON);


    public static Token KeywordVoid(){
        return TOKEN_KEYWORD_VOID;
    }

    public static Token KeywordInt(){
        return TOKEN_KEYWORD_INT;
    }

    public static Token KeywordBool(){
        return TOKEN_KEYWORD_BOOL;
    }

    public static Token KeywordReturn(){
        return TOKEN_KEYWORD_RETURN;
    }

    public static Token KeywordIf(){
        return TOKEN_KEYWORD_IF;
    }

    public static Token KeywordElse(){
        return TOKEN_KEYWORD_ELSE;
    }

    public static Token KeywordWhile(){
        return TOKEN_KEYWORD_WHILE;
    }

    public static Token KeywordBreak(){
        return TOKEN_KEYWORD_BREAK;
    }

    public static Token KeywordNobreak(){
        return TOKEN_KEYWORD_NOBREAK;
    }

    public static Token CurlyBraceOpen(){
        return TOKEN_CURLY_BRACE_OPEN;
    }

    public static Token CurlyBraceClose(){
        return TOKEN_CURLY_BRACE_CLOSE;
    }

    public static Token BraceOpen(){
        return TOKEN_BRACE_OPEN;
    }

    public static Token BraceClose(){
        return TOKEN_BRACE_CLOSE;
    }

    public static Token Plus(){
        return TOKEN_PLUS;
    }

    public static Token Minus(){
        return TOKEN_MINUS;
    }

    public static Token Star(){
        return TOKEN_STAR;
    }

    public static Token Slash(){
        return TOKEN_SLASH;
    }

    public static Token LeftShift(){
        return TOKEN_LEFT_SHIFT;
    }

    public static Token RightShift(){
        return TOKEN_RIGHT_SHIFT;
    }

    public static Token BooleanAnd(){
        return TOKEN_BOOLEAN_AND;
    }

    public static Token BooleanOr(){
        return TOKEN_BOOLEAN_OR;
    }

    public static Token Smaller(){
        return TOKEN_SMALLER;
    }

    public static Token Greater(){
        return TOKEN_GREATER;
    }

    public static Token SmallerEqual(){
        return TOKEN_SMALLER_EQUAL;
    }

    public static Token GreaterEqual(){
        return TOKEN_GREATER_EQUAL;
    }

    public static Token Compare(){
        return TOKEN_COMPARE;
    }

    public static Token NotEqual(){
        return TOKEN_NOT_EQUAL;
    }

    public static Token Equals(){
        return TOKEN_EQUALS;
    }

    public static Token Exclamation(){
        return TOKEN_EXCLAMATION;
    }

    public static Token Semicolon(){
        return TOKEN_SEMICOLON;
    }

    public static Token Identifier(String name){
        return new IdentifierToken(name);
    }

    public static Token IntLiteral(int value){
        return new IntLiteralToken(value);
    }

    public static Token BooleanLiteral(boolean value){
        return new BooleanLiteralToken(value);
    }
}
