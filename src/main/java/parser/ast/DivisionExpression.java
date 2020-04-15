package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;
import parser.types.Int;
import parser.types.Type;

public class DivisionExpression extends BinaryExpression<Integer, Integer, Integer>{
    public DivisionExpression(Expression<Integer> lhs, Expression<Integer> rhs) {
        super(lhs, rhs, Operation.DIVISION);
    }

    @Override
    protected String getOperator() {
        return "/";
    }

    @Override
    public Integer getValue() {
        return ((Integer)getLhs().getValue()) / ((Integer)getRhs().getValue());
    }

    @Override
    public Type getType() {
        return new Int();
    }
}
