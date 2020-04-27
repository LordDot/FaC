package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator. Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Int;
import parser.types.Type;

public class MultiplicationExpression extends BinaryExpression<Integer, Integer, Integer> {
    public MultiplicationExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.MULTIPLICATION);
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
