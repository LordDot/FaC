package parser.ast;

import codeGeneration.AssemblyGenerator. Operation;
import parser.types.Int;
import parser.types.Type;

public class MultiplicationExpression extends BinaryExpression<Integer, Integer, Integer>{
    public MultiplicationExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.MULTIPLIKATION);
    }

    @Override
    public Integer getValue() {
        return ((Integer)getLhs().getValue()) * ((Integer)getRhs().getValue());
    }

    @Override
    public Type getType() {
        return new Int();
    }

    @Override
    protected String getOperator() {
        return "*";
    }
}
