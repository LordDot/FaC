package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;


public class UnequalExpression extends BinaryExpression {
    public UnequalExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.UNEQUAL);
    }

    @Override
    protected String getOperator() {
        return "!=";
    }

    @Override
    public Type getType() {
        return getTypeBool();
    }
}
