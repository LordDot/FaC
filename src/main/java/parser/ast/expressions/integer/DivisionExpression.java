package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.Expression;
import parser.ast.expressions.BinaryExpression;
import parser.types.Type;
import static parser.types.Type.*;

public class DivisionExpression extends BinaryExpression<Integer, Integer, Integer> {
    public DivisionExpression(Expression<Integer> lhs, Expression<Integer> rhs) {
        super(lhs, rhs, Operation.DIVISION);
    }

    @Override
    protected String getOperator() {
        return "/";
    }

    @Override
    public Integer getValue() {
        return ((Integer)getLhs().getValue()) / ((Integer)getRhs().getValue());
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }
}
