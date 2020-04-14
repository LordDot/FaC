package parser;

import codeGeneration.AssemblyGenerator;
import parser.ast.Assignment;
import parser.ast.Function;
import parser.ast.Variable;

import java.util.LinkedList;
import java.util.List;

public class Ast {
    private List<Function> functions;
    private List<Variable> variables;
    private List<Assignment> initializations;

    public Ast(){
        functions = new LinkedList<>();
        variables = new LinkedList<>();
        initializations = new LinkedList<>();
    }

    public void addFunction(Function function){
        functions.add(function);
    }

    public void addVariable(Variable variable){
        variables.add(variable);
    }

    public void addVariables(List<Variable> variables){
        this.variables.addAll(variables);
    }

    public void addInitialization(Assignment assignment){
        initializations.add(assignment);
    }

    public String toPrettyString(){
        StringBuilder builder = new StringBuilder();
        for(Variable v : variables){
            builder.append(v.toPrettyString() + "\n");
        }
        for(Assignment a: initializations){
            builder.append(a.toPrettyString() + "\n");
        }
        builder.append("\n");
        for(Function f: functions){
            builder.append(f.toPrettyString() + "\n\n");
        }
        return builder.toString();
    }

    public void generateAssembly(AssemblyGenerator generator){
        generator.pushScope();
        for(Variable v: variables){
            generator.declareVariable(v.getName());
        }
        for(Assignment a: initializations){
            a.generateAssembly(generator);
        }
        for(Function f: functions){
            f.generateAssembly(generator);
        }
        generator.popScope();
    }
}
