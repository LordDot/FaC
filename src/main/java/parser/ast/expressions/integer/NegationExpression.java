package parser.ast.expressions.integer;

import codeGeneration.AssemblyGenerator.Operation;
import parser.ast.expressions.UnaryExpression;
import parser.ast.expressions.Expression;
import parser.types.Type;
import static parser.types.Type.*;


public class NegationExpression extends UnaryExpression {
    public NegationExpression(Expression operand) {
        super(operand, Operation.NEGATION);
    }

    @Override
    protected String getPrefix() {
        return "-";
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    public Type getType() {
        return getTypeInt();
    }
}
