package parser;

import parser.ast.Variable;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class Scoper {
    private Stack<List<Variable>> scopes;

    public Scoper(){
        scopes = new Stack<>();
        scopes.push(new LinkedList<>());
    }

    public List<Variable> popScope(){
        return scopes.pop();
    }

    public List<Variable> getCurrentScope(){
        return scopes.peek();
    }

    public void pushScope(){
        scopes.push(new LinkedList<>());
    }

    Variable findVariable(String name) {
        for (ListIterator<List<Variable>> i = scopes.listIterator(scopes.size()); i.hasPrevious();) {
            List<Variable> scope = i.previous();
            for (Variable v : scope) {
                if (v.getName().equals(name)) {
                    return v;
                }
            }
        }
        return null;
    }
}
