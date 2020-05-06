package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;
import jdk.jshell.spi.ExecutionControl;
import parser.ast.Statement;
import parser.ast.Variable;

import java.util.List;

public class WhileStatement extends Statement {
    private Expression<Boolean> condition;
    private List<Statement> bodyStatements;
    private List<Variable> bodyScope;
    private List<Statement> nobreakStatements;
    private List<Variable> nobreakScope;

    public WhileStatement(Expression<Boolean> condition, List<Statement> bodyStatements, List<Variable> bodyScope, List<Statement> nobreakStatements, List<Variable> nobreakScope) {
        this.condition = condition;
        this.bodyStatements = bodyStatements;
        this.bodyScope = bodyScope;
        this.nobreakStatements = nobreakStatements;
        this.nobreakScope = nobreakScope;
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("while(");
        builder.append(condition.toPrettyString());
        builder.append("){\n");
        for(Variable v : bodyScope){
            builder.append(v.toPrettyString() + "\n");
        }
        for(Statement s : bodyStatements){
            builder.append(s.toPrettyString());
        }
        builder.append("\n}nobreak{\n");

        for(Variable v : nobreakScope){
            builder.append(v.toPrettyString() + "\n");
        }
        for(Statement s : nobreakStatements){
            builder.append(s.toPrettyString() + "\n");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void generateAssembly(AssemblyGenerator assGen) {
        throw new RuntimeException("not implemented");
    }
}
