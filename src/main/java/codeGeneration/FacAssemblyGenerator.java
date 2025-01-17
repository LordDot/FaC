package codeGeneration;

import parser.ast.expressions.Expression;
import tokenizer.CompilerException;

import java.util.*;
import java.util.function.IntConsumer;

public class FacAssemblyGenerator implements AssemblyGenerator{
    public static Map<AssemblyGenerator.Operation, FacOperation> operationMapping = new HashMap<>();
    {
        operationMapping.put(Operation.ADDITION,new FacOperation("steel chest"));
        operationMapping.put(Operation.SUBTRACTION,new FacOperation("tank"));
        operationMapping.put(Operation.MULTIPLICATION,new FacOperation("wooden chest"));
        operationMapping.put(Operation.DIVISION,new FacOperation("iron chest"));
        operationMapping.put(Operation.NEGATION, new FacOperation("tank", false, 0));
        operationMapping.put(Operation.BOOL_NOT, new FacOperation("fast inserter", true, 0));
        operationMapping.put(Operation.SMALLER, new FacOperation("blue splitter"));
        operationMapping.put(Operation.COMPARISON, new FacOperation("burner inserter"));
        operationMapping.put(Operation.GREATER, new FacOperation("red splitter"));
        operationMapping.put(Operation.GREATER_EQUALS, new FacOperation("inserter"));
        operationMapping.put(Operation.SMALLER_EQUALS, new FacOperation("long inserter"));
        operationMapping.put(Operation.UNEQUAL, new FacOperation("fast inserter"));
    }

    private Stack<Map<String,Integer>> scopes;
    private int scopePointer;

    private List<Map<String,Integer>> generatedAssembly;
    private Map<String,Integer>  functionAdresses;
    private Stack<List<IntConsumer>> loopBreaks;

    public FacAssemblyGenerator(int adressBuffer){
        scopes = new Stack<>();
        scopePointer = adressBuffer;

        generatedAssembly = new LinkedList<>();
        functionAdresses = new HashMap<>();
        loopBreaks = new Stack<>();
    }

    public List<Map<String, Integer>> getGeneratedAssembly() {
        return generatedAssembly;
    }

    @Override
    public int getFreeAddress(){
        return scopePointer++;
    }

    @Override
    public int getAddressForVariable(String variableName) {
        return lookupVariable(variableName);
    }

    @Override
    public void generateUnaryOperation(Operation operation, int into, int operandAddress) {
        Map<String, Integer> command = new HashMap<>();
        FacOperation operator = operationMapping.get(operation);
        command.put(operator.getOpCode(), 1);
        if(operator.isLhs()){
            command.put("A", operandAddress);
            command.put("2", operator.getOtherSide());
        }else{
            command.put("1", operator.getOtherSide());
            command.put("B", operandAddress);
        }
        command.put("R", into);
        generatedAssembly.add(command);
    }

    @Override
    public void generateUnaryOperationByPointer(Operation operation, int intoPointer, int operandAddress) {
        Map<String, Integer> command = new HashMap<>();
        FacOperation operator = operationMapping.get(operation);
        command.put(operator.getOpCode(), 1);
        if(operator.isLhs()){
            command.put("A", operandAddress);
            command.put("2", operator.getOtherSide());
        }else{
            command.put("1", operator.getOtherSide());
            command.put("B", operandAddress);
        }
        command.put("Q", intoPointer);
        generatedAssembly.add(command);
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
    public void generateBinaryOperationByPointer(Operation op, int intoPointer, int lhs, int rhs) {
        Map<String, Integer> command = new HashMap<>();
        command.put("A", lhs);
        command.put("B", rhs);
        command.put("Q", intoPointer);
        command.put(operationMapping.get(op).getOpCode(), 1);
        generatedAssembly.add(command);
    }

    @Override
    public IntConsumer generateJump() {
        Map<String, Integer> command = new HashMap<>();
        command.put("J", 1);
        generatedAssembly.add(command);
        return (int i)->command.put("O", i);
    }

    @Override
    public IntConsumer generateConditionalJump(Expression condition) {
        int conditionAddress = getFreeAddress();
        condition.generateAssembly(this, conditionAddress);

        Map<String, Integer> command = new HashMap<>();
        command.put("J",2);
        command.put("fast inserter",1);
        command.put("A", conditionAddress);
        command.put("2", 0);
        generatedAssembly.add(command);
        return (int i)->command.put("D", i);
    }

    @Override
    public int getCurrentProgramAddress() {
        return generatedAssembly.size() + 1;
    }

    @Override
    public void beginLoop() {
        loopBreaks.push(new LinkedList<>());
    }

    @Override
    public void endLoop() {
        List<IntConsumer> popped = loopBreaks.pop();
        for (IntConsumer intConsumer : popped) {
            intConsumer.accept(getCurrentProgramAddress());
        }
    }

    @Override
    public void generateBreak(int loops) {
        List<IntConsumer> loop = loopBreaks.get(loopBreaks.size() - 1 - loops);
        loop.add(generateJump());
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
        //TODO
        //generateSaveVariablesOnStack(0);
    }

    @Override
    public void generateAssignment(int address, Expression value) {
        value.generateAssembly(this, address);
    }

    @Override
    public void generateAssignmentByPointer(int pointer, Expression value){
        value.generateAssemblyByPointer(this, pointer);
    }

    @Override
    public void loadValue(int address, int value) {
        Map<String, Integer> command = new HashMap<>();
        command.put("R", address);
        command.put("O", value);
        generatedAssembly.add(command);
    }

    @Override
    public void loadValueByPointer(int intoPointer, int value) {
        Map<String, Integer> command = new HashMap<>();
        command.put("O", value);
        command.put("Q", intoPointer);
        generatedAssembly.add(command);
    }

    @Override
    public void copyVariableIntoPointer(String variableName, int intoPointer) {
        int variableAddress = lookupVariable(variableName);
        Map<String,Integer> command = new HashMap<>();
        command.put("A", variableAddress);
        command.put("Q", intoPointer);
        command.put("2", 1);
        command.put("steel chest", 1);
        generatedAssembly.add(command);
    }

    @Override
    public void copyFromPointer(int pointerAddress, int into) {
        Map<String, Integer> command = new HashMap<>();
        command.put("R", into);
        command.put("S", pointerAddress);
        command.put("steel chest", 1);
        command.put("2", 1);
    }

    @Override
    public void copyFromPointerIntoPointer(int fromPointerAddress, int intoPointerAddress) {

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
        throw new RuntimeException("Not Implemented");
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

    private static class FacOperation{
        private String opCode;
        private boolean lhs;
        private int otherSide;

        public FacOperation(String opCode) {
            this.opCode = opCode;
        }

        public FacOperation(String opCode, boolean lhs, int otherSide) {
            this.opCode = opCode;
            this.lhs = lhs;
            this.otherSide = otherSide;
        }

        public String getOpCode() {
            return opCode;
        }

        public boolean isLhs() {
            return lhs;
        }

        public int getOtherSide() {
            return otherSide;
        }
    }
}
