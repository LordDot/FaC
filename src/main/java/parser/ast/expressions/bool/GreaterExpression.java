package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Bool;
import parser.types.Type;
import static parser.types.Type.*;


public class GreaterExpression extends BinaryExpression<Boolean, Integer, Integer> {
    public GreaterExpression(Expression<Integer> lhs, Expression<Integer> rhs) {
        super(lhs, rhs, Operation.GREATER);
    }

    @Override
    protected String getOperator() {
        return ">";
    }

    @Override
    public Boolean getValue() {
        return ((Integer)getLhs().getValue()) > (Integer) getRhs().getValue();
    }

    @Override
    public Type getType() {
        return getTypeBool();
    }
}
