package parser.types;

import java.util.Objects;

public class Pointer extends Type {
    Type subType;

    public Pointer(Type subType) {
        this.subType = subType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pointer pointer = (Pointer) o;
        return Objects.equals(subType, pointer.subType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subType);
    }

    @Override
    public String toString() {
        return subType.toString() + "*";
    }

    public Type getSubType() {
        return subType;
    }
}
