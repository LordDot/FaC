package parser.types;

public class Bool implements Type {
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
