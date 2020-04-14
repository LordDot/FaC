package parser;

import parser.ast.*;
import parser.types.Int;
import parser.types.Type;
import parser.types.Void;
import startUp.Compiler;
import tokenizer.CompilerException;
import tokenizer.IdentifierToken;
import tokenizer.IntLiteralToken;
import tokenizer.Token;
import tokenizer.Token.TokenType;

import java.util.*;

public class Parser {
    private Iterator<Token> tokens;
    private Scoper scoper;
    private Ast ast;
    private Token currentToken;



    public Parser(Iterator<Token> tokens, Scoper scoper) {

        this.tokens = tokens;
        this.scoper = scoper;
        if (tokens.hasNext()) {
            currentToken = tokens.next();
        }
        ast = new Ast();

    }

    public void parse() {
        try {
            while (tokens.hasNext()) {
                parseTopLevel();
            }
            ast.addVariables(scoper.popScope());
        } catch (NullPointerException e) {
            throw new CompilerException("Unexpected end of file");
        }
    }

    public Ast getAst() {
        return this.ast;
    }

    private void parseTopLevel() {
        Type type = parseType();
        nextToken();

        if (currentToken.getType() != TokenType.IDENTIFIER) {
            throw new CompilerException("Identifier expected");
        }
        String name = ((IdentifierToken) currentToken).getName();

        nextToken();
        if (currentToken.getType() == TokenType.BRACE_OPEN) {
            parseFunction(type, name);
        } else if (currentToken.getType() == TokenType.SEMICOLON) {
            scoper.getCurrentScope().add(new Variable(name, type));
            nextToken();
        } else if (currentToken.getType() == TokenType.EQUALS) {
            Variable v = new Variable(name, type);
            scoper.getCurrentScope().add(v);
            nextToken();

            Expression rhs = parseExpression();
            if (currentToken.getType() != TokenType.SEMICOLON) {
                throw new CompilerException("';' Expected");
            }
            nextToken();

            ast.addInitialization(new Assignment(v, rhs));
        } else {
            throw new CompilerException("Unexpected Symbol");
        }
    }

    private void parseFunction(Type returnType, String name) {
        nextToken();
        if (currentToken.getType() != TokenType.BRACE_CLOSE) {
            throw new CompilerException("Parameters are not supported yet");
        }
        nextToken();
        List<Statement> statements = parseBlock();

        Function function = new Function(name, returnType, scoper.popScope());
        function.addStatements(statements);
        ast.addFunction(function);
    }

    private List<Statement> parseBlock() {
        scoper.pushScope();
        List<Statement> ret = new LinkedList<>();
        if (currentToken.getType() != TokenType.CURLY_BRACE_OPEN) {
            throw new CompilerException("Block Expected");
        }
        nextToken();
        while (currentToken.getType() != TokenType.CURLY_BRACE_CLOSE) {
            Statement statement = parseStatement();
            if (statement != null) {
                ret.add(statement);
            }
        }
        nextToken();
        return ret;
    }

    private Statement parseStatement() {
        if (isType(currentToken)) {
            Variable v = declareVariable();
            nextToken();
            if (currentToken.getType() == TokenType.EQUALS) {
                return parseAssignment(v);
            } else if (currentToken.getType() == TokenType.SEMICOLON) {
                nextToken();
                return null;
            } else {
                throw new CompilerException("Unexpected thing");
            }
        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
            return parseAssignment();
        } else if (currentToken.getType() == TokenType.KEYWORD_IF) {
            return parseIfElseStatement();
        } else {
            throw new CompilerException("Unknown thing");
        }
    }

    private Statement parseIfElseStatement() {
        nextToken();
        expectedToken(TokenType.BRACE_OPEN);
        Expression condition = parseExpression();
        expectedToken(TokenType.BRACE_CLOSE);
        List<Statement> ifStatements = parseBlock();
        List<Variable> ifScope = scoper.popScope();
        List<Statement> elseStatements;
        List<Variable> elseScope;
        if (currentToken.getType() == TokenType.KEYWORD_ELSE) {
            nextToken();
            elseStatements = parseBlock();
            elseScope = scoper.popScope();
        } else {
            elseStatements = new LinkedList<>();
            elseScope = new LinkedList<>();
        }
        return new IfStatement(condition, ifStatements, elseStatements, ifScope, elseScope);
    }

    private Variable declareVariable() {
        Type type = parseType();
        nextToken();
        if (currentToken.getType() != TokenType.IDENTIFIER) {
            throw new CompilerException("identifier expected");
        }
        String name = ((IdentifierToken) currentToken).getName();
        Variable v = scoper.findVariable(name);
        if (v != null) {
            throw new CompilerException("Redeclaration of Variable " + name);
        }
        Variable newVariable = new Variable(name, type);
        scoper.getCurrentScope().add(newVariable);
        return newVariable;
    }

    private Assignment parseAssignment() {
        String name = ((IdentifierToken) currentToken).getName();
        Variable v = scoper.findVariable(name);
        if (v == null) {
            throw new CompilerException("Undeclared variable " + name);
        }
        nextToken();
        return parseAssignment(v);
    }

    private Assignment parseAssignment(Variable v) {

        if (currentToken.getType() != TokenType.EQUALS) {
            throw new CompilerException("Expected =");
        }
        nextToken();
        Expression rhs = parseExpression();
        if (currentToken.getType() != TokenType.SEMICOLON) {
            throw new CompilerException("';' Expected");
        }
        nextToken();
        return new Assignment(v, rhs);
    }

    private Expression parseExpression() {
        if (currentToken.getType() == TokenType.INT_LITERAL) {
            int value = ((IntLiteralToken) currentToken).getValue();
            nextToken();
            return new IntLiteral(value);
        } else if(currentToken.getType() == TokenType.IDENTIFIER) {
            String name = ((IdentifierToken) currentToken).getName();
            Variable v = scoper.findVariable(name);
            if(v == null){
                throw new CompilerException("Undeclared variable: " + name);
            }
            nextToken();
            return new VariableAccess(v);
        } else {
            throw new CompilerException("Unknown Expression");
        }
    }

    private static boolean isType(Token token) {
        return token.getType() == TokenType.KEYWORD_INT;
    }

    private Type parseType() {
        if (currentToken.getType() == TokenType.KEYWORD_VOID) {
            return new Void();
        } else if (currentToken.getType() == TokenType.KEYWORD_INT) {
            return new Int();
        } else {
            throw new CompilerException("Function or global Variable Declaration must start with type");
        }
    }

    private void nextToken() {
        if (currentToken == null) {
            throw new CompilerException("Unexpected end of file");
        }
        if (tokens.hasNext()) {
            currentToken = tokens.next();
        } else {
            currentToken = null;
        }
    }

    private void expectedToken(TokenType type) {
        if (currentToken.getType() != type) {
            throw new CompilerException("Unexpected symbol: " + currentToken.getType().toString());
        }
        nextToken();
    }
}
