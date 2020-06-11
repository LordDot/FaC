package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.Expression;
import parser.ast.expressions.BinaryExpression;
import parser.types.Type;
import static parser.types.Type.*;


public class CompareExpression<T> extends BinaryExpression {
    public CompareExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.COMPARISON);
    }

    @Override
    protected String getOperator() {
        return "==";
    }

    @Override
    public Type getType() {
        return getTypeBool();
    }
}
