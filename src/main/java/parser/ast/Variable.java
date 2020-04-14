package parser.ast;

import parser.types.Type;

public class Variable {
    private String name;
    private Type type;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String toPrettyString(){
        return type.toString() + " " + name + ";";
    }
}
