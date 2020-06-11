package parser.ast.expressions;

import codeGeneration.AssemblyGenerator;
import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.Expression;

public abstract class UnaryExpression extends Expression {
    private Expression operand;
    private Operation operation;

    public UnaryExpression(Expression operand, Operation operation) {
        this.operand = operand;
        this.operation = operation;
    }

    protected Expression getOperand() {
        return operand;
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getPrefix());
        builder.append("(");
        builder.append(operand.toPrettyString());
        builder.append(")");
        builder.append(getSuffix());
        return builder.toString();
    }

    protected abstract String getPrefix();
    protected abstract String getSuffix();

    @Override
    public void generateAssemblyByPointer(AssemblyGenerator generator, int intoPointer) {
        int operandAddress = generator.getFreeAddress();
        operand.generateAssembly(generator, operandAddress);
        generateOperationByPointer(generator, intoPointer, operandAddress);
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        int operandAddress = generator.getFreeAddress();
        operand.generateAssembly(generator, operandAddress);
        generateOperation(generator, into, operandAddress);
    }

    public void generateOperation(AssemblyGenerator generator, int into, int operandAddress){
        generator.generateUnaryOperation(operation, into, operandAddress);
    }

    public void generateOperationByPointer(AssemblyGenerator generator, int intoPointer, int operandAddress){
        generator.generateUnaryOperationByPointer(operation, intoPointer, operandAddress);
    }
}
