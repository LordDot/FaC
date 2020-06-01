package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;

public abstract class LExpression<T> extends Expression<T>{
    @Override
    public boolean isLValue() {
        return true;
    }

    public abstract void generateAddress(AssemblyGenerator generator, int into);
}
