package parser.types;

public abstract class Type {
    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }
}
