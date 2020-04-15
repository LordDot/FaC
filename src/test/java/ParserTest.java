import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import parser.Parser;
import parser.Scoper;
import parser.TokenIterator;
import tokenizer.IdentifierToken;
import tokenizer.IntLiteralToken;
import tokenizer.Token;
import tokenizer.Token.TokenType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParserTest {
    @Test
    public void simpleAst() {
        Token[] tokens = {
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
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = 0;\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    private void compareResult(Token[] tokens, String expected) {
        List<Token> tokenList = Arrays.asList(tokens);
        Scoper s = new Scoper();
        Parser parser = new Parser(new TokenIterator(tokenList.iterator()) ,s);
        parser.parse();
        String result = parser.getAst().toPrettyString();
        assertEquals(expected, result);
    }

    @Test
    public void noStatements() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void declarationWithoutAssignment() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void globalVariable() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.CURLY_BRACE_CLOSE),
        };
        String expected = "int i;\n" +
                "\n" +
                "void main(){\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void globalVariableWithAssignment() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(0),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.CURLY_BRACE_CLOSE),
        };
        String expected = "int i;\n" +
                "i = 0;\n" +
                "\n" +
                "void main(){\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void interScopeAssignment() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(2),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE),
        };
        String expected = "int i;\n" +
                "\n" +
                "void main(){\n" +
                "i = 2;\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void ifElse() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.KEYWORD_IF),
                new Token(TokenType.BRACE_OPEN),
                new IntLiteralToken(1),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(0),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE),
                new Token(TokenType.KEYWORD_ELSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_CLOSE),
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "if(1) {\n" +
                "i = 0;\n" +
                "} else {\n" +
                "i = 1;\n" +
                "}\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testVariableAccess() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("j"),
                new Token(TokenType.SEMICOLON),
                new IdentifierToken("j"),
                new Token(TokenType.EQUALS),
                new IdentifierToken("i"),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "int j;\n" +
                "i = 1;\n" +
                "j = i;\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testVariableAccessWithDeclaration() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("j"),
                new Token(TokenType.EQUALS),
                new IdentifierToken("i"),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "int j;\n" +
                "i = 1;\n" +
                "j = i;\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testPlus() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.PLUS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = (1) + (1);\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testMinus() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.MINUS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = (1) - (1);\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testTimes() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.STAR),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = (1) * (1);\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testDivision() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.SLASH),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = (1) / (1);\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testPrecedence() {
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new IntLiteralToken(1),
                new Token(TokenType.MINUS),
                new IntLiteralToken(1),
                new Token(TokenType.STAR),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = (1) - ((1) * (1));\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testUnaryPlus(){
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new Token(TokenType.PLUS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = 1;\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testUnaryMinus(){
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new Token(TokenType.MINUS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = -(1);\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testUnaryOperationChaining(){
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new Token(TokenType.MINUS),
                new Token(TokenType.MINUS),
                new Token(TokenType.PLUS),
                new Token(TokenType.PLUS),
                new Token(TokenType.MINUS),
                new Token(TokenType.PLUS),
                new Token(TokenType.MINUS),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = -(-(-(-(1))));\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }

    @Test
    public void testParenthesis(){
        Token[] tokens = {
                new Token(TokenType.KEYWORD_VOID),
                new IdentifierToken("main"),
                new Token(TokenType.BRACE_OPEN),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.CURLY_BRACE_OPEN),
                new Token(TokenType.KEYWORD_INT),
                new IdentifierToken("i"),
                new Token(TokenType.EQUALS),
                new Token(TokenType.BRACE_OPEN),
                new IntLiteralToken(1),
                new Token(TokenType.MINUS),
                new IntLiteralToken(1),
                new Token(TokenType.BRACE_CLOSE),
                new Token(TokenType.STAR),
                new IntLiteralToken(1),
                new Token(TokenType.SEMICOLON),
                new Token(TokenType.CURLY_BRACE_CLOSE)
        };
        String expected = "\n" +
                "void main(){\n" +
                "int i;\n" +
                "i = ((1) - (1)) * (1);\n" +
                "}\n\n";
        compareResult(tokens, expected);
    }
}
