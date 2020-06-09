package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;


public class UnequalExpression<T> extends BinaryExpression<Boolean, T, T> {
    public UnequalExpression(Expression<T> lhs, Expression<T> rhs) {
        super(lhs, rhs, Operation.UNEQUAL);
    }

    @Override
    protected String getOperator() {
        return "!=";
    }

    @Override
    public Boolean getValue() {
        return ((T)getLhs().getValue()).equals((T)getRhs().getValue());
    }

    @Override
    public Type getType() {
        return getTypeBool();
    }
}
