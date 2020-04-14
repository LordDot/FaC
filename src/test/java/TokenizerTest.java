import org.junit.jupiter.api.Test;
import tokenizer.IdentifierToken;
import tokenizer.IntLiteralToken;
import tokenizer.Token;
import tokenizer.Token.TokenType;
import tokenizer.Tokenizer;

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

}
