package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.Expression;
import parser.ast.expressions.BinaryExpression;
import parser.types.Type;
import static parser.types.Type.*;

public class DivisionExpression extends BinaryExpression {
    public DivisionExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.DIVISION);
    }

    @Override
    protected String getOperator() {
        return "/";
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }
}
