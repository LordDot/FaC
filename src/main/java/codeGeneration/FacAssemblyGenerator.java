package codeGeneration;

import parser.ast.expressions.Expression;
import tokenizer.CompilerException;

import java.util.*;

public class FacAssemblyGenerator implements AssemblyGenerator{
    public static Map<AssemblyGenerator.Operation, FacOperation> operationMapping = new HashMap<>();
    {
        operationMapping.put(Operation.ADDITION,new FacOperation("steel chest"));
        operationMapping.put(Operation.SUBTRACTION,new FacOperation("tank"));
        operationMapping.put(Operation.MULTIPLIKATION,new FacOperation("wooden chest"));
        operationMapping.put(Operation.DIVISION,new FacOperation("iron chest"));
    }

    private Stack<Map<String,Integer>> scopes;
    private int scopePointer;

    private List<Map<String,Integer>> generatedAssembly;
    private Map<String,Integer>  functionAdresses;

    public FacAssemblyGenerator(int adressBuffer){
        scopes = new Stack<>();
        scopePointer = adressBuffer;

        generatedAssembly = new LinkedList<>();
        functionAdresses = new HashMap<>();
    }

    public List<Map<String, Integer>> getGeneratedAssembly() {
        return generatedAssembly;
    }

    @Override
    public int getFreeAddress(){
        return scopePointer++;
    }

    @Override
    public void generateUnaryOperation(Operation operation, int into, int operandAddress) {
        throw new RuntimeException("NotImplemented yet!");
    }

    @Override
    public void generateBinaryOperation(Operation op, int into, int lhs, int rhs) {
        Map<String, Integer> command = new HashMap<>();
        command.put("A", lhs);
        command.put("B", rhs);
        command.put("R", into);
        command.put(operationMapping.get(op).getOpCode(), 1);
        generatedAssembly.add(command);
    }

    @Override
    public void pushScope() {
        scopes.push(new HashMap<>());
    }

    @Override
    public void popScope() {
        Map<String, Integer> poppedScope = scopes.pop();
        scopePointer -= poppedScope.size();
    }

    @Override
    public void declareVariable(String name) {
        scopes.peek().put(name, getFreeAddress());
    }

    @Override
    public void declareFunction(String name) {
        functionAdresses.put(name,generatedAssembly.size());
        generateSaveVariablesOnStack(0);
    }

    @Override
    public void generateAssignment(String variableName, Expression value) {
        Integer potentialAddress = lookupVariable(variableName);
        if(potentialAddress == null){
            throw new CompilerException("Unknown Variable: " + variableName);
        }
        generateAssignment(potentialAddress.intValue(), value);
    }

    @Override
    public void generateAssignment(int address, Expression value) {
        value.generateAssembly(this, address);
    }

    @Override
    public void loadConstant(int address, int value) {
        Map<String, Integer> command = new HashMap<>();
        command.put("R", address);
        command.put("O", value);
        generatedAssembly.add(command);
    }

    @Override
    public void generateCopy(int address, String varName) {
        Map<String, Integer> command = new HashMap<>();
        command.put("R", address);
        command.put("steel chest", 1);
        Integer potentialAddress = lookupVariable(varName);
        if(potentialAddress == null){
            throw new CompilerException("Unknown Variable: " + varName);
        }
        command.put("A",potentialAddress.intValue());
        generatedAssembly.add(command);
    }

    private void generateSaveVariablesOnStack(int number){
        //TODO
    }

    private Integer lookupVariable(String variableName) {
        for (ListIterator<Map<String, Integer>> i = scopes.listIterator(scopes.size()); i.hasPrevious();) {
            Map<String, Integer> current = i.previous();
            Integer ret = current.getOrDefault(variableName, null);
            if(ret != null){
                return ret;
            }
        }
        return null;
    }

    private class FacOperation{
        private String opCode;

        public FacOperation(String opCode) {
            this.opCode = opCode;
        }

        public String getOpCode() {
            return opCode;
        }
    }
}
