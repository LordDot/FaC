package parser.types;

public abstract class Type {
    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }

    public static Type getTypeBool(){
        return new Bool();
    }

    public static Type getTypeInt(){
        return new Int();
    }

    public static Type getTypeVoid(){
        return new Void();
    }
}
