package parser.ast;

import codeGeneration.AssemblyGenerator.Operation;

public class MinusExpression extends BinaryExpression {
    public MinusExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs, Operation.SUBTRACTION);
    }

    @Override
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(getLhs().toPrettyString());
        builder.append(") - (");
        builder.append(getRhs().toPrettyString());
        builder.append(")");
        return builder.toString();
    }

    @Override
    public int getValue() {
        return getLhs().getValue() - getRhs().getValue();
    }
}
