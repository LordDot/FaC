package parser.types;

public class Bool extends Type {
    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Bool)){
            return false;
        }
        return true;
    }
}
