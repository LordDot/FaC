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
    void loadConstant(int address, Expression value);
}
