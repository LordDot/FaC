package parser;

import parser.ast.*;
import parser.ast.expressions.DereferenceExpression;
import parser.ast.expressions.Expression;
import parser.ast.expressions.VariableAccess;
import parser.ast.expressions.bool.*;
import parser.ast.expressions.integer.*;
import tokenizer.*;
import tokenizer.tokens.BooleanLiteralToken;
import tokenizer.tokens.IdentifierToken;
import tokenizer.tokens.IntLiteralToken;
import tokenizer.tokens.Token;
import tokenizer.tokens.Token.TokenType;

import static parser.types.Type.*;


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
        Expression lhs = parseLevel3();
        while(tokens.getCurrentType() == TokenType.BOOLEAN_AND || tokens.getCurrentType() == TokenType.BOOLEAN_OR){
            if(tokens.getCurrentType() == TokenType.BOOLEAN_AND){
                tokens.step();
                Expression rhs = parseLevel3();
                lhs = new AndExpression(lhs, rhs);
            }else if(tokens.getCurrentType() == TokenType.BOOLEAN_OR){
                tokens.step();
                Expression rhs = parseLevel3();
                lhs = new OrExpression(lhs,rhs);
            }
        }
        return lhs;
    }

    private Expression parseLevel3() {
        if(tokens.getCurrentType() == TokenType.EXCLAMATION){
            tokens.step();
            return new NotExpression(parseLevel3());
        }else {
            return parseLevel4();
        }
    }

    private Expression parseLevel4(){
        Expression lhs = parseLevel5();
        while(isCompareExpression(tokens.getCurrentType())){
            TokenType type = tokens.getCurrentType();
            tokens.step();
            Expression rhs = parseLevel5();
            lhs = constructComparison(type, lhs, rhs);
        }
        return lhs;
    }

    private boolean isCompareExpression(TokenType type){
        if(type == TokenType.COMPARE){
            return true;
        }
        if(type == TokenType.NOT_EQUAL){
            return true;
        }
        if(type == TokenType.SMALLER){
            return true;
        }
        if(type == TokenType.SMALLER_EQUAL){
            return true;
        }
        if(type == TokenType.GREATER){
            return true;
        }
        if(type == TokenType.GREATER_EQUAL){
            return true;
        }
        return false;
    }

    private Expression parseLevel5(){
        Expression lhs = parseLevel6();
        while(tokens.getCurrentToken().getType() == TokenType.PLUS || tokens.getCurrentToken().getType() == TokenType.MINUS){
            TokenType type = tokens.getCurrentToken().getType();
            tokens.step();
            Expression rhs = parseLevel6();
            if (type == TokenType.PLUS) {
                lhs = new PlusExpression(lhs, rhs);
            }else if(type == TokenType.MINUS){
                lhs = new MinusExpression(lhs, rhs);
            }
        }
        return lhs;
    }

    private Expression parseLevel6(){
        Expression lhs =  parseLevel7();
        while(tokens.getCurrentToken().getType() == TokenType.STAR || tokens.getCurrentToken().getType() == TokenType.SLASH){
            TokenType type = tokens.getCurrentToken().getType();
            tokens.step();
            Expression rhs = parseLevel7();
            if(type == TokenType.STAR){
                lhs = new MultiplicationExpression(lhs, rhs);
            }else if(type == TokenType.SLASH){
                lhs = new DivisionExpression(lhs, rhs);
            }
        }
        return lhs;
    }

    private Expression parseLevel7(){
        if(tokens.getCurrentToken().getType() == TokenType.PLUS){
            tokens.step();
            return parseLevel7();
        }else if(tokens.getCurrentToken().getType() == TokenType.MINUS) {
            tokens.step();
            return new NegationExpression(parseLevel7());
        }else if(tokens.getCurrentType() == TokenType.STAR){
            tokens.step();
            return new DereferenceExpression(parseLevel7());
        }else {
            return parseLevel8();
        }
    }

    private Expression parseLevel8(){
        return parseLevel9();
    }

    private Expression parseLevel9() {
        if (tokens.getCurrentToken().getType() == Token.TokenType.INT_LITERAL) {
            int value = ((IntLiteralToken) tokens.getCurrentToken()).getValue();
            tokens.step();
            return new IntLiteral(value);
        }else if(tokens.getCurrentType() == TokenType.BOOLEAN_LITERAL){
            boolean value =((BooleanLiteralToken) tokens.getCurrentToken()).getValue();
            tokens.step();;
            return new BoolLiteral(value);
        } else if (tokens.getCurrentToken().getType() == Token.TokenType.IDENTIFIER) {
            String name = ((IdentifierToken) tokens.getCurrentToken()).getName();
            Variable v = scoper.findVariable(name);
            if (v == null) {
                throw new CompilerException("Undeclared variable: " + name);
            }
            tokens.step();
            return new VariableAccess(v);
        }else if (tokens.getCurrentType() ==TokenType.BRACE_OPEN){
            tokens.step();
            Expression ret = parseLevel1();
            tokens.expectedToken(TokenType.BRACE_CLOSE);
            return ret;
        } else {
            throw new CompilerException("Unknown Expression");

        }
    }

    private Expression constructComparison(TokenType operator, Expression lhs, Expression rhs){
        if(lhs.getType().equals(getTypeInt()) && rhs.getType().equals(getTypeInt())) {
            return constructIntComparison(operator, lhs, rhs);
        }else if(lhs.getType().equals(getTypeBool())){
            return constructBoolComparison(operator, lhs, rhs);
        }else{
            throw new CompilerException("Invalid operands for Comparison");
        }
    }

    private Expression constructBoolComparison(TokenType operator, Expression lhs, Expression rhs){
        if(operator == TokenType.COMPARE) {
            return new CompareExpression(lhs, rhs);
        }else if(operator == TokenType.NOT_EQUAL){
            return new UnequalExpression(lhs, rhs);

        }else{
            throw new CompilerException("Unknown comparison for Booleans");
        }
    }

    private Expression constructIntComparison(TokenType operator, Expression lhs, Expression rhs){
        if(operator == TokenType.COMPARE) {
            return new CompareExpression(lhs, rhs);
        }else if(operator == TokenType.NOT_EQUAL){
            return new UnequalExpression(lhs, rhs);
        }else if(operator == TokenType.SMALLER){
            return new SmallerExpression(lhs, rhs);
        }else if(operator == TokenType.SMALLER_EQUAL){
            return new SmallerEqualsExpression(lhs, rhs);
        }else if(operator == TokenType.GREATER){
            return new GreaterExpression(lhs, rhs);
        }else if(operator == TokenType.GREATER_EQUAL){
            return new GreaterEqualsExpression(lhs, rhs);
        }else{
            throw new CompilerException("Unknown comparison for Integers");
        }
    }
}
