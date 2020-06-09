package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;


public class MinusExpression extends BinaryExpression<Integer, Integer, Integer> {
    public MinusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.SUBTRACTION);
    }

    @Override
    public Integer getValue() {
        return ((Integer)getLhs().getValue()) - ((Integer)getRhs().getValue());
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }

    @Override
    protected String getOperator() {
        return "-";
    }
}
