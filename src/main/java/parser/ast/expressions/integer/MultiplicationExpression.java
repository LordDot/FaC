package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator. Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;


public class MultiplicationExpression extends BinaryExpression {
    public MultiplicationExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.MULTIPLICATION);
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }

    @Override
    protected String getOperator() {
        return "*";
    }
}
