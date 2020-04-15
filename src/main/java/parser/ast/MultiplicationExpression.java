package parser.ast;

import codeGeneration.AssemblyGenerator. Operation;

public class MultiplicationExpression extends BinaryExpression{
    public MultiplicationExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.MULTIPLIKATION);
    }

    @Override
    public int getValue() {
        return getLhs().getValue() * getRhs().getValue();
    }

    @Override
    protected String getOperator() {
        return "*";
    }
}
