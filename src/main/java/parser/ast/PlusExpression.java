package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;
import parser.types.Int;
import parser.types.Type;

public class PlusExpression extends BinaryExpression<Integer, Integer, Integer>{


    public PlusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.ADDITION);
    }

    @Override
    public Integer getValue() {
        return ((Integer)getLhs().getValue()) + ((Integer)getRhs().getValue());
    }

    @Override
    public Type getType() {
        return new Int();
    }

    @Override
    protected String getOperator() {
        return "+";
    }
}
