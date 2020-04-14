package tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.function.Predicate;

public class Tokenizer implements Iterator<Token>{

    private Reader input;
    private int currentChar;

    public Tokenizer(Reader input) throws IOException {
        this.input = input;
        currentChar = input.read();
    }

    @Override
    public boolean hasNext() {
        return currentChar != -1;
    }

    @Override
    public Token next() {
        try {
            if (currentChar == -1) {
                return null;
            }
            boolean cont = true;
            char c = (char) currentChar;
            while (cont) {
                if(Character.isDigit(c)){
                    return new IntLiteralToken(readInt());
                }else if(Character.isLetter(c) || c == '_') {
                    String str = readString();
                    if (str.equals("int")) {
                        return new Token(Token.TokenType.KEYWORD_INT);
                    } else if (str.equals("void")) {
                        return new Token(Token.TokenType.KEYWORD_VOID);
                    }else if (str.equals("return")) {
                        return new Token(Token.TokenType.KEYWORD_RETURN);
                    }else if(str.equals("if")){
                        return new Token(Token.TokenType.KEYWORD_IF);
                    }else if(str.equals("else")){
                        return new Token(Token.TokenType.KEYWORD_ELSE);
                    }else{
                        return new IdentifierToken(str);
                    }
                }else if(Character.isWhitespace(c)){
                    currentChar = input.read();
                }else{
                    switch (c) {
                        case '(':
                            currentChar = input.read();
                            return new Token(Token.TokenType.BRACE_OPEN);
                        case ')':
                            currentChar = input.read();
                            return new Token(Token.TokenType.BRACE_CLOSE);
                        case '*':
                            currentChar = input.read();
                            return new Token(Token.TokenType.STAR);
                        //case '!':
                        case '+':
                            currentChar = input.read();
                            return new Token(Token.TokenType.PLUS);
                        case '-':
                            currentChar = input.read();
                            return new Token(Token.TokenType.MINUS);
                        case '/':
                            currentChar = input.read();
                            return new Token(Token.TokenType.SLASH);
                        case ';':
                            currentChar = input.read();
                            return new Token(Token.TokenType.SEMICOLON);
                        //case '<':
                        case '=':
                            currentChar = input.read();
                            return new Token(Token.TokenType.EQUALS);
                        /*case '>':
                        case '^':*/
                        case '{':
                            currentChar = input.read();
                            return new Token(Token.TokenType.CURLY_BRACE_OPEN);
                        case '}':
                            currentChar = input.read();
                            return new Token(Token.TokenType.CURLY_BRACE_CLOSE);
                    }
                }
                if (currentChar == -1) {
                    cont = false;
                } else {
                    c = (char) currentChar;
                }
            }
            return null;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private String readString() throws IOException {
        return read((c)->Character.isLetterOrDigit(c) || c=='_');
    }

    private int readInt() throws IOException {
        return Integer.valueOf(read((c)->Character.isLetter(c)));
    }

    private String read(Predicate<Character> test) throws IOException {
        StringBuffer buff = new StringBuffer();
        buff.append((char)currentChar);
        currentChar = input.read();
        char c = (char)currentChar;
        while(test.test(c)){
            buff.append(c);
            currentChar = input.read();
            c = (char)currentChar;
        }
        return buff.toString();
    }
}
