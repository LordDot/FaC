package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;

public abstract class LExpression extends Expression{
    @Override
    public boolean isLValue() {
        return true;
    }

    public abstract void generateAddress(AssemblyGenerator generator, int into);
}
