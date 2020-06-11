package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;
import parser.types.Pointer;
import parser.types.Type;

public class DereferenceExpression extends LExpression{
    private Expression pointer;

    public DereferenceExpression(Expression pointer) {
        this.pointer = pointer;
    }

    @Override
    public String toPrettyString() {
        return "*(" + pointer.toPrettyString() + ")";
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        int pointerAddress = generator.getFreeAddress();
        generator.copyFromPointer(pointerAddress, into);
    }

    @Override
    public void generateAssemblyByPointer(AssemblyGenerator generator, int intoPointer) {
        int pointerAddress = generator.getFreeAddress();
        generator.copyFromPointerIntoPointer(pointerAddress,intoPointer);
    }

    @Override
    public Type getType() {
        return ((Pointer)pointer.getType()).getSubType();
    }

    @Override
    public void generateAddress(AssemblyGenerator generator, int into) {
        pointer.generateAssembly(generator, into);
    }
}
