package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;
import parser.types.Bool;
import parser.types.Type;

public class GreaterEqualsExpression extends BinaryExpression<Boolean, Integer, Integer> {
    public GreaterEqualsExpression(Expression<Integer> lhs, Expression<Integer> rhs) {
        super(lhs, rhs, Operation.GREATER_EQUALS);
    }

    @Override
    protected String getOperator() {
        return ">=";
    }

    @Override
    public Boolean getValue() {
        return ((Integer)getLhs().getValue()) >= (Integer)getRhs().getValue();
    }

    @Override
    public Type getType() {
        return new Bool();
    }
}
