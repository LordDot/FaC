package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.Expression;
import parser.ast.expressions.BinaryExpression;
import parser.types.Type;
import static parser.types.Type.*;


public class CompareExpression<T> extends BinaryExpression<Boolean, T, T> {
    public CompareExpression(Expression<T> lhs, Expression<T> rhs) {
        super(lhs, rhs, Operation.COMPARISON);
    }

    @Override
    protected String getOperator() {
        return "==";
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
