package parser.types;

public class Type {
    private static final Type VOID = new Type(TypeEnum.VOID);
    private static final Type INT = new Type(TypeEnum.INT);
    private static final Type BOOL = new Type(TypeEnum.BOOL);

    public enum TypeEnum{
        VOID, INT, BOOL, POINTER
    };

    private TypeEnum type;

    public Type(TypeEnum type) {
        this.type = type;
    }

    @Override
    public String toString(){
        switch (type){
            case VOID:
                return "void";
            case INT:
                return "int";
            case BOOL:
                return "bool";
            default:
                throw new RuntimeException("Subtype of 'Type' need to implement 'toString'");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type1 = (Type) o;
        return type == type1.type;
    }

    public static Type getTypeBool(){
        return BOOL;
    }

    public static Type getTypeInt(){
        return INT;
    }

    public static Type getTypeVoid(){
        return VOID;
    }
}
