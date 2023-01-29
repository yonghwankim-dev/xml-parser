import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<Token> analyzeLexical(List<String> texts) {
        List<Token> result = new ArrayList<>();
        for(String text : texts){
            TokenType tokenType = TokenType.find(text);
            result.add(new Token(tokenType, text));
        }
        return result;
    }
}
