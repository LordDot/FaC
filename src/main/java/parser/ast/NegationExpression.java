package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;

public class NegationExpression extends UnaryExpression {
    public NegationExpression(Expression operand) {
        super(operand, Operation.NEGATION);
    }

    @Override
    protected String getPrefix() {
        return "-";
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    public int getValue() {
        return -getOperand().getValue();
    }
}
