package tokenizer;

public class IdentifierToken extends Token {
    private String name;

    public IdentifierToken(String name) {
        super(TokenType.IDENTIFIER);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IdentifierToken that = (IdentifierToken) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "IdentifierToken{" +
                "name='" + name + '\'' +
                '}';
    }
}
