package parser.ast.expressions.bool;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.Expression;
import parser.ast.expressions.UnaryExpression;
import parser.types.Type;
import static parser.types.Type.*;


public class NotExpression extends UnaryExpression<Boolean, Boolean> {
    public NotExpression(Expression operand) {
        super(operand, Operation.BOOL_NOT);
    }

    @Override
    protected String getPrefix() {
        return "!";
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    public Boolean getValue() {
        return !((Boolean)getOperand().getValue());
    }

    @Override
    public Type getType() {
        return getTypeBool();
    }
}
