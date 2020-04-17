package parser.ast;

import codeGeneration.AssemblyGenerator;
import parser.ast.expressions.Expression;

import java.util.List;

public class IfStatement extends Statement {
    private Expression condition;
    private List<Statement> ifStatements;
    private List<Statement> elseStatements;
    private List<Variable> ifScope;
    private List<Variable> elseScope;

    public IfStatement(Expression condition, List<Statement> ifStatements, List<Statement> elseStatements, List<Variable> ifScope, List<Variable> elseScope) {
        this.condition = condition;
        this.ifStatements = ifStatements;
        this.elseStatements = elseStatements;
        this.ifScope = ifScope;
        this.elseScope = elseScope;
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("if(");
        builder.append(condition.toPrettyString());
        builder.append(") {\n");
        for(Variable v : ifScope){
            builder.append(v.toPrettyString() + "\n");
        }
        for(Statement s : ifStatements){
            builder.append(s.toPrettyString());
        }
        builder.append("\n} else {\n");
        for(Statement s : elseStatements){
            builder.append(s.toPrettyString());
        }
        builder.append("\n}");
        return builder.toString();
    }

    @Override
    public void generateAssembly(AssemblyGenerator assGen) {

    }
}
