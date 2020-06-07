package parser;

import parser.ast.*;
import parser.ast.expressions.Expression;
import parser.ast.expressions.LExpression;
import parser.ast.expressions.VariableAccess;
import parser.ast.expressions.WhileStatement;
import parser.ast.expressions.integer.IntLiteral;
import parser.types.*;
import parser.types.Void;
import tokenizer.CompilerException;
import tokenizer.IdentifierToken;
import tokenizer.IntLiteralToken;
import tokenizer.Token;
import tokenizer.Token.TokenType;

import java.util.*;

public class Parser {
    private TokenIterator tokens;
    private Scoper scoper;
    private Ast ast;
    private ExpressionParser expressionParser;


    public Parser(TokenIterator tokens, Scoper scoper) {
        this.tokens = tokens;
        this.scoper = scoper;
        expressionParser = new ExpressionParser(scoper, tokens);

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

        if (tokens.getCurrentToken().getType() != TokenType.IDENTIFIER) {
            throw new CompilerException("Identifier expected");
        }
        String name = ((IdentifierToken) tokens.getCurrentToken()).getName();

        tokens.step();
        if (tokens.getCurrentToken().getType() == TokenType.BRACE_OPEN) {
            parseFunction(type, name);
        } else if (tokens.getCurrentToken().getType() == TokenType.SEMICOLON) {
            scoper.getCurrentScope().add(new Variable(name, type));
            tokens.step();
        } else if (tokens.getCurrentToken().getType() == TokenType.EQUALS) {
            Variable v = new Variable(name, type);
            scoper.getCurrentScope().add(v);
            tokens.step();

            Expression rhs = expressionParser.parseExpression();
            if (tokens.getCurrentToken().getType() != TokenType.SEMICOLON) {
                throw new CompilerException("';' Expected");
            }
            tokens.step();

            ast.addInitialization(new Assignment(new VariableAccess(v), rhs));
        } else {
            throw new CompilerException("Unexpected Symbol");
        }
    }

    private void parseFunction(Type returnType, String name) {
        tokens.step();
        if (tokens.getCurrentToken().getType() != TokenType.BRACE_CLOSE) {
            throw new CompilerException("Parameters are not supported yet");
        }
        tokens.step();
        List<Statement> statements = parseBlock();

        Function function = new Function(name, returnType, scoper.popScope());
        function.addStatements(statements);
        ast.addFunction(function);
    }

    private List<Statement> parseBlock() {
        scoper.pushScope();
        List<Statement> ret = new LinkedList<>();
        if (tokens.getCurrentToken().getType() != TokenType.CURLY_BRACE_OPEN) {
            throw new CompilerException("Block Expected");
        }
        tokens.step();
        while (tokens.getCurrentToken().getType() != TokenType.CURLY_BRACE_CLOSE) {
            Statement statement = parseStatement();
            if (statement != null) {
                ret.add(statement);
            }
        }
        tokens.step();
        return ret;
    }

    private Statement parseStatement() {
        if (tokens.getCurrentToken().getType() == TokenType.KEYWORD_IF) {
            return parseIfElseStatement();
        } else if (tokens.getCurrentType() == TokenType.KEYWORD_WHILE) {
            return parseWhileLoop();
        } else if (tokens.getCurrentType() == TokenType.KEYWORD_BREAK) {
            return parseBreak();
        }else if (isType(tokens.getCurrentToken())) {
            Variable v = declareVariable();
            tokens.step();
            if (tokens.getCurrentToken().getType() == TokenType.EQUALS) {
                return parseAssignment(new VariableAccess(v));
            } else if (tokens.getCurrentToken().getType() == TokenType.SEMICOLON) {
                tokens.step();
                return null;
            } else {
                throw new CompilerException("Unexpected thing");
            }
        } else {
            return parseAssignment();
        }
    }

    private Statement parseBreak() {
        tokens.step();
        tokens.expectedToken(TokenType.BRACE_OPEN);
        if(tokens.getCurrentType() != TokenType.INT_LITERAL){
            throw new CompilerException("Int Literal expected");
        }
        int loops = ((IntLiteralToken)tokens.getCurrentToken()).getValue();
        tokens.step();
        tokens.expectedToken(TokenType.BRACE_CLOSE);
        tokens.expectedToken(TokenType.SEMICOLON);
        return new BreakStatement(loops);
    }

    private Statement parseWhileLoop() {
        tokens.step();
        tokens.expectedToken(TokenType.BRACE_OPEN);
        Expression<Boolean> condition = expressionParser.parseExpression();
        tokens.expectedToken(TokenType.BRACE_CLOSE);
        List<Statement> bodyStatements = parseBlock();
        List<Variable> bodyScope = scoper.popScope();
        List<Statement> nobreakStatements;
        List<Variable> nobreakScope;
        if (tokens.getCurrentType() == TokenType.KEYWORD_NOBREAK) {
            tokens.step();
            nobreakStatements = parseBlock();
            nobreakScope = scoper.popScope();
        } else {
            nobreakStatements = new LinkedList<>();
            nobreakScope = new LinkedList<>();
        }
        return new WhileStatement(condition, bodyStatements, bodyScope, nobreakStatements, nobreakScope);
    }

    private Statement parseIfElseStatement() {
        tokens.step();
        tokens.expectedToken(TokenType.BRACE_OPEN);
        Expression condition = expressionParser.parseExpression();
        tokens.expectedToken(TokenType.BRACE_CLOSE);
        List<Statement> ifStatements = parseBlock();
        List<Variable> ifScope = scoper.popScope();
        List<Statement> elseStatements;
        List<Variable> elseScope;
        if (tokens.getCurrentToken().getType() == TokenType.KEYWORD_ELSE) {
            tokens.step();
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

        if (tokens.getCurrentToken().getType() != TokenType.IDENTIFIER) {
            throw new CompilerException("identifier expected");
        }
        String name = ((IdentifierToken) tokens.getCurrentToken()).getName();
        Variable v = scoper.findVariable(name);
        if (v != null) {
            throw new CompilerException("Redeclaration of Variable " + name);
        }
        Variable newVariable = new Variable(name, type);
        scoper.getCurrentScope().add(newVariable);
        return newVariable;
    }

    private Assignment parseAssignment() {
        Expression lhs = expressionParser.parseExpression();
        if(!lhs.isLValue()){
            throw new CompilerException("LValue expected");
        }
        return parseAssignment((LExpression)lhs);
    }

    private Assignment parseAssignment(LExpression lhs) {

        if (tokens.getCurrentToken().getType() != TokenType.EQUALS) {
            throw new CompilerException("Expected =");
        }
        tokens.step();
        Expression rhs = expressionParser.parseExpression();
        if (tokens.getCurrentToken().getType() != TokenType.SEMICOLON) {
            throw new CompilerException("';' Expected");
        }
        tokens.step();
        return new Assignment(lhs, rhs);
    }


    private static boolean isType(Token token) {
        if (token.getType() == TokenType.KEYWORD_INT) {
            return true;
        } else if (token.getType() == TokenType.KEYWORD_BOOL) {
            return true;
        }
        return false;
    }

    private Type parseType() {
        Type type = null;
        if (tokens.getCurrentToken().getType() == TokenType.KEYWORD_VOID) {
            type = new Void();
            tokens.step();
        } else if (tokens.getCurrentToken().getType() == TokenType.KEYWORD_INT) {
            type = new Int();
            tokens.step();
        } else if (tokens.getCurrentType() == TokenType.KEYWORD_BOOL) {
            type = new Bool();
            tokens.step();
        } else {
            throw new CompilerException("Function or global Variable Declaration must start with type");
        }

        if(tokens.getCurrentType() == TokenType.STAR){
            type = new Pointer(type);
            tokens.step();
        }
        return type;
    }
}
