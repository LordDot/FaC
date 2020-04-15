package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;

public class MinusExpression extends BinaryExpression {
    public MinusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.SUBTRACTION);
    }

    @Override
    public int getValue() {
        return getLhs().getValue() - getRhs().getValue();
    }

    @Override
    protected String getOperator() {
        return "-";
    }
}
