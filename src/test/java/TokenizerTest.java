import org.junit.jupiter.api.Test;
import tokenizer.*;
import tokenizer.tokens.BooleanLiteralToken;
import tokenizer.tokens.IdentifierToken;
import tokenizer.tokens.IntLiteralToken;
import tokenizer.tokens.Token;
import tokenizer.tokens.Token.TokenType;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    @Test
    void testSimpleString() throws IOException {
        String s = "void main(){\n" +
                "\t int i = 0;\n" +
                "}";
        Token[] result = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(0),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };

        testString(s, result);
    }

    @Test
    void testMultiDigitInt() throws IOException {
        String s = "12";
        Token[] result = {new IntLiteralToken(12)};
        testString(s, result);
    }

    private void testString(String s, Token[] result) throws IOException {
        Reader input = new StringReader(s);
        Tokenizer tokenizer = new Tokenizer(input);
        for (int i = 0; i < result.length; i++) {
            if(!tokenizer.hasNext()){
                fail("Tokenstream ended to early after " + result[i-1].toString());
            }
            Token token = tokenizer.next();
            if(!result[i].equals(token)){
                fail("Token is " + token + ", but should be " + result[i]);
            }
        }
        if(tokenizer.hasNext()){
            fail();
        }
    }

    @Test
    public void testIfElse() throws IOException {
        String program = "if else";
        Token[] result = {new Token(TokenType.KEYWORD_IF),new Token(TokenType.KEYWORD_ELSE)};
        testString(program, result);
    }

    @Test
    public void testSimpleOperators() throws IOException {
        String program = "+-/*";
        Token[] result = {new Token(TokenType.PLUS), new Token(TokenType.MINUS), new Token(TokenType.SLASH), new Token(TokenType.STAR)};
        testString(program,result);
    }

    @Test
    public void testGreaterSigns() throws IOException {
        String program = "> >> >> > >";
        Token[] result = {new Token(TokenType.GREATER), new Token(TokenType.RIGHT_SHIFT), new Token(TokenType.RIGHT_SHIFT), new Token(TokenType.GREATER), new Token(TokenType.GREATER)};
        testString(program, result);
    }

    @Test
    public void testSmallerSigns() throws IOException {
        String program = "< << << < <";
        Token[] result = {new Token(TokenType.SMALLER), new Token(TokenType.LEFT_SHIFT), new Token(TokenType.LEFT_SHIFT), new Token(TokenType.SMALLER), new Token(TokenType.SMALLER)};
        testString(program, result);
    }

    @Test
    public void testTrailingWhiteSpace() throws IOException {
        String program = "asgjk ";
        Token[] result = {new IdentifierToken("asgjk")};
        testString(program, result);
    }

    @Test
    public void testBooleanLiterals() throws IOException {
        String program = "true false";
        Token[] result = {new BooleanLiteralToken(true), new BooleanLiteralToken(false)};
        testString(program, result);
    }

    @Test
    public void testBoolKeyWord() throws IOException {
        String program = "bool";
        Token[] result = {new Token(TokenType.KEYWORD_BOOL)};
        testString(program, result);
    }

    @Test
    public void testBooleanOperator() throws IOException {
        String program = "&& ! ||";
        Token[] result = {new Token(TokenType.BOOLEAN_AND), new Token(TokenType.EXCLAMATION), new Token(TokenType.BOOLEAN_OR)};
        testString(program, result);
    }

    @Test
    public void testCompareOperators() throws IOException {
        String program = "< > <= >= == !=";
        Token[] result = {new Token(TokenType.SMALLER), new Token(TokenType.GREATER), new Token(TokenType.SMALLER_EQUAL), new Token(TokenType.GREATER_EQUAL), new Token(TokenType.COMPARE), new Token(TokenType.NOT_EQUAL)};
        testString(program, result);
    }

    @Test
    public void testUnknownSymbol() throws IOException {
        String program = new String(new int[]{0x1F60D}, 0, 1);
        Tokenizer tokenizer = new Tokenizer(new StringReader(program));
        assertThrows(CompilerException.class, ()->tokenizer.next());
    }

    @Test
    public void testWhile() throws IOException {
        String program = "while break nobreak";
        Token[] result = {new Token(TokenType.KEYWORD_WHILE), new Token(TokenType.KEYWORD_BREAK), new Token(TokenType.KEYWORD_NOBREAK)};
        testString(program, result);
    }
}
