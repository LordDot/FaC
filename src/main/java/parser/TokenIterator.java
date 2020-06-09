package parser;

import tokenizer.CompilerException;
import tokenizer.tokens.Token;
import tokenizer.tokens.Token.TokenType;

import java.util.Iterator;

public class TokenIterator implements Iterator<Token> {

    private Iterator<Token> source;
    private Token currentToken;

    public TokenIterator(Iterator<Token> source) {
        this.source = source;
        currentToken = hasNext()? source.next(): null;

    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public Token next() {
        step();
        return currentToken;
    }

    public void step() {
        if(currentToken == null){
            throw new CompilerException("Unexpected end stream");
        }
        currentToken = hasNext()? source.next(): null;
    }

    public Token getCurrentToken(){
        return currentToken;
    }

    public Token.TokenType getCurrentType(){
        return getCurrentToken().getType();
    }

    public void expectedToken(TokenType type) {
        if (getCurrentType() != type) {
            throw new CompilerException("Unexpected symbol: " + getCurrentToken().getType().toString());
        }
        step();
    }

    @Override
    public String toString() {
        return "TokenIterator{" +
                "currentToken=" + currentToken +
                '}';
    }
}
