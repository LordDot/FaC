package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;
import parser.ast.Statement;
import parser.ast.Variable;

import java.util.List;
import java.util.function.IntConsumer;

public class WhileStatement extends Statement {
    private Expression condition;
    private List<Statement> bodyStatements;
    private List<Variable> bodyScope;
    private List<Statement> nobreakStatements;
    private List<Variable> nobreakScope;

    public WhileStatement(Expression condition, List<Statement> bodyStatements, List<Variable> bodyScope, List<Statement> nobreakStatements, List<Variable> nobreakScope) {
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
        int conditionCounter = assGen.getCurrentProgramAddress();
        IntConsumer conditionalJump = assGen.generateConditionalJump(condition);
        IntConsumer jumpToNoBreak = assGen.generateJump();
        conditionalJump.accept(assGen.getCurrentProgramAddress());
        assGen.beginLoop();
        assGen.pushScope();
        for(Variable v : bodyScope){
            assGen.declareVariable(v.getName());
        }
        for(Statement s : bodyStatements){
            s.generateAssembly(assGen);
        }
        assGen.popScope();
        assGen.generateJump().accept(conditionCounter);
        jumpToNoBreak.accept(assGen.getCurrentProgramAddress());
        assGen.pushScope();
        for(Variable v : nobreakScope){
            assGen.declareVariable(v.getName());
        }
        for(Statement s : nobreakStatements){
            s.generateAssembly(assGen);
        }
        assGen.popScope();
        assGen.endLoop();
    }
}
