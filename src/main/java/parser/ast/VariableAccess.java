package parser.ast;

import codeGeneration.AssemblyGenerator;

public class VariableAccess extends Expression {
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
    public int getValue() {
        return 0;
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        generator.generateCopy(into, accessed.getName());
    }
}
