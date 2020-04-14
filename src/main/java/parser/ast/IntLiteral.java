package parser.ast;

import codeGeneration.AssemblyGenerator;

public class IntLiteral extends Expression{
    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public int getValue(){
        return value;
    }

    @Override
    public String toPrettyString() {
        return Integer.toString(value);
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        generator.loadConstant(into, this);
    }
}
