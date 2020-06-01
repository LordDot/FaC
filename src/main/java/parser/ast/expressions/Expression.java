package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;
import parser.types.Type;

public abstract class Expression<T>{
    public abstract String toPrettyString();
    public abstract boolean isConstant();
    public abstract T getValue();
    public abstract void generateAssembly(AssemblyGenerator generator, int into);
    public abstract void generateAssemblyByPointer(AssemblyGenerator generator, int intoPointer);
    public abstract Type getType();
    public boolean isLValue(){
        return false;
    }
}
