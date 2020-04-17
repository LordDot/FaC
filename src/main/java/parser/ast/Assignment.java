package parser.ast;

import codeGeneration.AssemblyGenerator;
import parser.ast.expressions.Expression;

public class Assignment extends Statement{
    private Variable target;
    private Expression value;

    public Assignment(Variable target, Expression value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public String toPrettyString() {
        return target.getName() + " = " + value.toPrettyString() + ";";
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator) {
        generator.generateAssignment(target.getName(), value);
    }
}
