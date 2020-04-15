package codeGeneration;

import parser.ast.Expression;
import parser.ast.Variable;

import java.util.List;

public interface AssemblyGenerator {
    void pushScope();
    void popScope();
    void declareVariable(String name);
    void declareFunction(String name);
    void generateAssignment(String variableName, Expression value);
    void generateAssignment(int address, Expression value);
    void generateCopy(int address, String variableName);
    void loadConstant(int address, int value);
    int getFreeAddress();
    void generateUnaryOperation(Operation operation, int into, int operandAddress);
    void generateBinaryOperation(Operation op, int into, int lhs, int rhs);

    public enum Operation {
        ADDITION,
        SUBTRACTION,
        MULTIPLIKATION,
        DIVISION,
        NEGATION
    };
}
