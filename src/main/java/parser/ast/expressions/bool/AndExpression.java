package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Bool;
import parser.types.Type;

public class AndExpression extends BinaryExpression<Boolean, Boolean, Boolean> {
    public AndExpression(Expression<Boolean> lhs, Expression<Boolean> rhs) {
        super(lhs, rhs, Operation.MULTIPLICATION);
    }

    @Override
    protected String getOperator() {
        return "&&";
    }

    @Override
    public Boolean getValue() {
        return ((Boolean)getLhs().getValue()) && (Boolean)getRhs().getValue();
    }

    @Override
    public Type getType() {
        return new Bool();
    }
}
