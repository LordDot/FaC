package parser.ast;

import codeGeneration.AssemblyGenerator;
import parser.types.Type;

import java.util.LinkedList;
import java.util.List;

public class Function extends AstNode{
    private String name;
    private Type returnType;
    private List<Statement> statements;
    private List<Variable> scope;

    public Function(String name, Type returnType, List<Variable> scope) {
        this.name = name;
        this.returnType = returnType;
        this.scope = scope;
        statements = new LinkedList<>();
    }

    public Function addStatement(Statement statement){
        statements.add(statement);
        return this;
    }

    public Function addStatements(List<Statement> statements){
        this.statements.addAll(statements);
        return this;
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(returnType.toString());
        builder.append(" " + name + "(){\n");
        for(Variable v : scope){
            builder.append(v.toPrettyString() + "\n");
        }
        for(Statement s : statements){
            builder.append(s.toPrettyString() +"\n");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator) {
        generator.declareFunction(name);
        generator.pushScope();
        for(Variable v: scope){
            generator.declareVariable(v.getName());
        }
        for(Statement s: statements){
            s.generateAssembly(generator);
        }
        generator.popScope();
    }
}
