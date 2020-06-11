package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;

public class IntLiteral extends Expression {
    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public String toPrettyString() {
        return Integer.toString(value);
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        generator.loadValue(into, value);
    }

    @Override
    public void generateAssemblyByPointer(AssemblyGenerator generator, int intoPointer) {
        generator.loadValueByPointer(intoPointer, value);
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }
}
