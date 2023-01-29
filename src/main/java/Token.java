import java.util.ArrayList;
import java.util.Objects;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean hasTokenType(TokenType type) {
        return this.type == type;
    }

    public boolean equalTagName(String tagName) {
        return this.value.equals(tagName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return String.format("{type : %s, value : %s}", type.name(), value);
    }

}
