package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;

public class DivisionExpression extends BinaryExpression{
    public DivisionExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.DIVISION);
    }

    @Override
    protected String getOperator() {
        return "/";
    }

    @Override
    public int getValue() {
        return getLhs().getValue() / getRhs().getValue();
    }
}
