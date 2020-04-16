package parser.ast;

import codeGeneration.AssemblyGenerator;
import parser.types.Bool;
import parser.types.Type;

public class BoolLiteral extends Expression<Boolean>{
    boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public String toPrettyString() {
        return value?"true":"false";
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Type getType() {
        return new Bool();
    }
}
