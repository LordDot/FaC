package tokenizer;

import tokenizer.tokens.BooleanLiteralToken;
import tokenizer.tokens.IdentifierToken;
import tokenizer.tokens.IntLiteralToken;
import tokenizer.tokens.Token;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.function.Predicate;

import static tokenizer.tokens.Tokens.*;

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
                        return KeywordInt();
                    } else if (str.equals("void")) {
                        return KeywordVoid();
                    }else if (str.equals("return")) {
                        return KeywordReturn();
                    }else if(str.equals("if")){
                        return KeywordIf();
                    }else if(str.equals("else")) {
                        return KeywordElse();
                    }else if(str.equals("bool")) {
                        return KeywordBool();
                    }else if(str.equals("true")) {
                        return new BooleanLiteralToken(true);
                    } else if (str.equals("false")) {
                        return new BooleanLiteralToken(false);
                    }else if (str.equals("while")) {
                        return KeywordWhile();
                    }else if (str.equals("break")) {
                        return KeywordBreak();
                    }else if (str.equals("nobreak")){
                        return KeywordNobreak();
                    }else{
                        return new IdentifierToken(str);
                    }
                }else if(Character.isWhitespace(c)){
                    readWhiteSpaces();
                }else{
                    switch (c) {
                        case '(':
                            currentChar = input.read();
                            return BraceOpen();
                        case ')':
                            currentChar = input.read();
                            return BraceClose();
                        case '*':
                            currentChar = input.read();
                            return Star();
                        case '!':
                            currentChar = input.read();
                            if(currentChar == '='){
                                currentChar = input.read();
                                return NotEqual();
                            }else{
                                return Exclamation();
                            }
                        case '&':
                            currentChar = input.read();
                            if(currentChar == '&'){
                                currentChar = input.read();
                                return BooleanAnd();
                            }else {
                                throw new CompilerException("& Expected");
                            }
                        case '|':
                            currentChar = input.read();
                            if(currentChar == '|'){
                                currentChar = input.read();
                                return BooleanOr();
                            }else{
                                throw new CompilerException("| Expected");
                            }
                        case '+':
                            currentChar = input.read();
                            return Plus();
                        case '-':
                            currentChar = input.read();
                            return Minus();
                        case '/':
                            currentChar = input.read();
                            return Slash();
                        case ';':
                            currentChar = input.read();
                            return Semicolon();
                        case '<':
                            currentChar = input.read();
                            if(currentChar == '<') {
                                currentChar = input.read();
                                return LeftShift();
                            }else if(currentChar == '='){
                                currentChar = input.read();
                                return SmallerEqual();
                            }else{
                                return Smaller();
                            }
                        case '>':
                            currentChar = input.read();
                            if(currentChar == '>') {
                                currentChar = input.read();
                                return RightShift();
                            }else if(currentChar == '='){
                                currentChar = input.read();
                                return GreaterEqual();
                            }else{
                                return Greater();
                            }
                            //case '^':
                        case '=':
                            currentChar = input.read();
                            if(currentChar == '='){
                                currentChar = input.read();
                                return Compare();
                            }else {
                                return Equals();
                            }
                        case '{':
                            currentChar = input.read();
                            return CurlyBraceOpen();
                        case '}':
                            currentChar = input.read();
                            return CurlyBraceClose();
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
        return Integer.parseInt(read((c)->Character.isDigit(c)));
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
