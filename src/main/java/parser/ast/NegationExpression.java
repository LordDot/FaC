package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;
import parser.types.Int;
import parser.types.Type;

public class NegationExpression extends UnaryExpression<Integer, Integer> {
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
    public Integer getValue() {
        return -((Integer)getOperand().getValue());
    }

    @Override
    public Type getType() {
        return new Int();
    }
}
