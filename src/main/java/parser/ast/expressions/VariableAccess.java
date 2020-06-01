package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;
import parser.ast.Variable;
import parser.ast.expressions.Expression;
import parser.types.Type;

public class VariableAccess extends LExpression<Integer> {
    private Variable accessed;

    public VariableAccess(Variable accessed) {
        this.accessed = accessed;
    }

    @Override
    public String toPrettyString() {
        return accessed.getName();
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Integer getValue() {
        return null;
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        generator.generateCopy(into, accessed.getName());
    }

    @Override
    public void generateAssemblyByPointer(AssemblyGenerator generator, int intoPointer) {
        generator.copyVariableIntoPointer(accessed.getName(), intoPointer);
    }

    @Override
    public Type getType() {
        return accessed.getType();
    }

    @Override
    public void generateAddress(AssemblyGenerator generator, int into) {
        generator.loadValue(into,generator.getAddressForVariable(accessed.getName()));
    }
}
