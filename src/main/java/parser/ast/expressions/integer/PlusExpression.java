package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.BinaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;


public class PlusExpression extends BinaryExpression {


    public PlusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.ADDITION);
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }

    @Override
    protected String getOperator() {
        return "+";
    }
}
