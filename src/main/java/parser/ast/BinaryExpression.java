package parser.ast;

import codeGeneration.AssemblyGenerator;
import codeGeneration.AssemblyGenerator.Operation;

public abstract class BinaryExpression extends Expression{
    private Expression rhs;
    private Expression lhs;
    private Operation operation;

    public BinaryExpression(Expression lhs, Expression rhs, Operation operation) {
        this.rhs = rhs;
        this.lhs = lhs;
        this.operation = operation;
    }

    @Override
    public boolean isConstant() {
        return rhs.isConstant() && lhs.isConstant();
    }

    @Override
    public void generateAssembly(AssemblyGenerator generator, int into) {
        int lhsAddress = generator.getFreeAddress();
        int rhsAddress = generator.getFreeAddress();
        lhs.generateAssembly(generator, lhsAddress);
        rhs.generateAssembly(generator, rhsAddress);
        generateOperation(generator, into, lhsAddress, rhsAddress);
    }

    public void generateOperation(AssemblyGenerator generator, int into, int lhs, int rhs){
        generator.generateBinaryOperation(operation, into, lhs, rhs);
    }

    public Expression getRhs() {
        return rhs;
    }

    public Expression getLhs() {
        return lhs;
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(lhs.toPrettyString());
        builder.append(") ");
        builder.append(getOperator());
        builder.append(" (");
        builder.append(rhs.toPrettyString());
        builder.append(")");
        return builder.toString();
    }

    protected abstract String getOperator();
}
