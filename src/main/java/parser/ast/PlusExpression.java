package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;

public class PlusExpression extends BinaryExpression{


    public PlusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.ADDITION);
    }

    @Override
    public int getValue() {
        return getLhs().getValue() + getRhs().getValue();
    }

    @Override
    protected String getOperator() {
        return "+";
    }
}
