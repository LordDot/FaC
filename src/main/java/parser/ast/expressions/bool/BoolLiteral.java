package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator;
import parser.ast.expressions.Expression;
import parser.ast.expressions.integer.IntLiteral;
import parser.types.Type;

import static parser.types.Type.*;


public class BoolLiteral extends Expression {
    boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public String toPrettyString() {
        return value?"true":"false";
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        generator.generateAssignment(into, new IntLiteral(value?1:0));
    }

    @Override
    public void generateAssemblyByPointer(AssemblyGenerator generator, int intoPointer) {
        generator.loadValueByPointer(intoPointer, value?1:0);
    }

    @Override
    public Type getType() {
        return getTypeBool();
    }
}
