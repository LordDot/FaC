package parser.ast;

import codeGeneration.AssemblyGenerator;
import parser.types.Type;

public abstract class Expression<T>{
    public abstract String toPrettyString();
    public abstract boolean isConstant();
    public abstract T getValue();
    public abstract void generateAssembly(AssemblyGenerator generator, int into);
    public abstract Type getType();
}
