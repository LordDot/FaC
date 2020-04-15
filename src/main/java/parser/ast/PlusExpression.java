package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;

public class PlusExpression extends BinaryExpression{


    public PlusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.ADDITION);
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(getLhs().toPrettyString());
        builder.append((") + ("));
        builder.append(getRhs().toPrettyString());
        builder.append(")");
        return builder.toString();
    }

    @Override
    public int getValue() {
        return getLhs().getValue() + getRhs().getValue();
    }
}
