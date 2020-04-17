package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator;
import parser.ast.expressions.Expression;
import parser.types.Int;
import parser.types.Type;

public class IntLiteral extends Expression<Integer> {
    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Integer getValue(){
        return value;
    }

    @Override
    public String toPrettyString() {
        return Integer.toString(value);
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        generator.loadConstant(into, value);
    }

    @Override
    public Type getType() {
        return new Int();
    }
}
