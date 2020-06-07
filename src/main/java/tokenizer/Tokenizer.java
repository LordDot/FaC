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
        if(Character.isWhitespace(currentChar)){
            try {
                readWhiteSpaces();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    }else if(str.equals("else")) {
                        return new Token(Token.TokenType.KEYWORD_ELSE);
                    }else if(str.equals("bool")) {
                        return new Token(Token.TokenType.KEYWORD_BOOL);
                    }else if(str.equals("true")) {
                        return new BooleanLiteralToken(true);
                    } else if (str.equals("false")) {
                        return new BooleanLiteralToken(false);
                    }else if (str.equals("while")) {
                        return new Token(Token.TokenType.KEYWORD_WHILE);
                    }else if (str.equals("break")) {
                        return new Token(Token.TokenType.KEYWORD_BREAK);
                    }else if (str.equals("nobreak")){
                        return new Token(Token.TokenType.KEYWORD_NOBREAK);
                    }else{
                        return new IdentifierToken(str);
                    }
                }else if(Character.isWhitespace(c)){
                    readWhiteSpaces();
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
                        case '!':
                            currentChar = input.read();
                            if(currentChar == '='){
                                currentChar = input.read();
                                return new Token(Token.TokenType.NOT_EQUAL);
                            }else{
                                return new Token(Token.TokenType.EXCLAMATION);
                            }
                        case '&':
                            currentChar = input.read();
                            if(currentChar == '&'){
                                currentChar = input.read();
                                return new Token(Token.TokenType.BOOLEAN_AND);
                            }else {
                                throw new CompilerException("& Expected");
                            }
                        case '|':
                            currentChar = input.read();
                            if(currentChar == '|'){
                                currentChar = input.read();
                                return new Token(Token.TokenType.BOOLEAN_OR);
                            }else{
                                throw new CompilerException("| Expected");
                            }
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
                        case '<':
                            currentChar = input.read();
                            if(currentChar == '<') {
                                currentChar = input.read();
                                return new Token(Token.TokenType.LEFT_SHIFT);
                            }else if(currentChar == '='){
                                currentChar = input.read();
                                return new Token(Token.TokenType.SMALLER_EQUAL);
                            }else{
                                return new Token(Token.TokenType.SMALLER);
                            }
                        case '>':
                            currentChar = input.read();
                            if(currentChar == '>') {
                                currentChar = input.read();
                                return new Token(Token.TokenType.RIGHT_SHIFT);
                            }else if(currentChar == '='){
                                currentChar = input.read();
                                return new Token(Token.TokenType.GREATER_EQUAL);
                            }else{
                                return new Token(Token.TokenType.GREATER);
                            }
                            //case '^':
                        case '=':
                            currentChar = input.read();
                            if(currentChar == '='){
                                currentChar = input.read();
                                return new Token(Token.TokenType.COMPARE);
                            }else {
                                return new Token(Token.TokenType.EQUALS);
                            }
                        case '{':
                            currentChar = input.read();
                            return new Token(Token.TokenType.CURLY_BRACE_OPEN);
                        case '}':
                            currentChar = input.read();
                            return new Token(Token.TokenType.CURLY_BRACE_CLOSE);
                        default:
                            throw new CompilerException("Unknown character");
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
        return Integer.valueOf(read((c)->Character.isDigit(c)));
    }

    private void readWhiteSpaces() throws IOException {
        read((c)->Character.isWhitespace(c));
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
