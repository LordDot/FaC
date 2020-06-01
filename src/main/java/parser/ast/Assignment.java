package parser.ast;

import codeGeneration.AssemblyGenerator;
import parser.ast.expressions.Expression;
import parser.ast.expressions.LExpression;

public class Assignment extends Statement{
    private LExpression target;
    private Expression value;

    public Assignment(LExpression target, Expression value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public String toPrettyString() {
        return target.toPrettyString() + " = " + value.toPrettyString() + ";";
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator) {
        int targetAddress = generator.getFreeAddress();
        target.generateAddress(generator, targetAddress);
        generator.generateAssignmentByPointer(targetAddress, value);
    }
}
