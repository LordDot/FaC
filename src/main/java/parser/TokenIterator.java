package parser;

import startUp.Compiler;
import tokenizer.CompilerException;
import tokenizer.Token;

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

    @Override
    public String toString() {
        return "TokenIterator{" +
                "currentToken=" + currentToken +
                '}';
    }
}
