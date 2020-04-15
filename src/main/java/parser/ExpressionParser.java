package parser;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.*;
import tokenizer.CompilerException;
import tokenizer.IdentifierToken;
import tokenizer.IntLiteralToken;
import tokenizer.Token;
import tokenizer.Token.TokenType;

public class ExpressionParser {
    private Scoper scoper;
    private TokenIterator tokens;

    public ExpressionParser(Scoper scoper, TokenIterator tokens) {
        this.scoper = scoper;
        this.tokens = tokens;
    }

    public Expression parseExpression() {
        return parseLevel1();
    }

    private Expression parseLevel1(){
        return parseLevel2();
    }

    private Expression parseLevel2(){
        return parseLevel3();
    }

    private Expression parseLevel3(){
        return parseLevel4();
    }

    private Expression parseLevel4(){
        return parseLevel5();
    }

    private Expression parseLevel5(){
        return parseLevel6();
    }

    private Expression parseLevel6(){
        Expression lhs = parseLevel7();
        while(tokens.getCurrentToken().getType() == TokenType.PLUS){
            tokens.step();
            Expression rhs = parseLevel7();
            lhs = new PlusExpression(lhs, rhs);
        }
        return lhs;
    }

    private Expression parseLevel7(){
        return parseLevel8();
    }

    private Expression parseLevel8() {
        if (tokens.getCurrentToken().getType() == Token.TokenType.INT_LITERAL) {
            int value = ((IntLiteralToken) tokens.getCurrentToken()).getValue();
            tokens.step();
            return new IntLiteral(value);
        } else if (tokens.getCurrentToken().getType() == Token.TokenType.IDENTIFIER) {
            String name = ((IdentifierToken) tokens.getCurrentToken()).getName();
            Variable v = scoper.findVariable(name);
            if (v == null) {
                throw new CompilerException("Undeclared variable: " + name);
            }
            tokens.step();
            return new VariableAccess(v);
        } else {
            throw new CompilerException("Unknown Expression");

        }
    }
}
