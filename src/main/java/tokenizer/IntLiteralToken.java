package tokenizer;

public class IntLiteralToken extends Token{
    private int value;

    public IntLiteralToken(int value) {
        super(TokenType.INT_LITERAL);

        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntLiteralToken that = (IntLiteralToken) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "IntLiteralToken{" +
                "value=" + value +
                '}';
    }
}
