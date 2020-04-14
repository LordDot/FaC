package codeGeneration;

import parser.ast.Expression;
import parser.ast.Variable;
import parser.types.Int;
import tokenizer.CompilerException;

import java.util.*;

public class FacAssemblyGenerator implements AssemblyGenerator{
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

    private int getNewVariableAdress(){
        return scopePointer++;
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
        scopes.peek().put(name,getNewVariableAdress());
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
    public void loadConstant(int address, Expression value) {
        Map<String, Integer> command = new HashMap<>();
        command.put("R", address);
        command.put("O", value.getValue());
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
}
