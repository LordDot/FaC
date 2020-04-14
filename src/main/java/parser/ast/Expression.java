package parser.ast;

import codeGeneration.AssemblyGenerator;

public abstract class Expression{
    public abstract String toPrettyString();
    public abstract boolean isConstant();
    public abstract int getValue();
    public abstract void generateAssembly(AssemblyGenerator generator, int into);
}
