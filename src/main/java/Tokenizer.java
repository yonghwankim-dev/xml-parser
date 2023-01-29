import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tokenizer {

    private static final List<Pattern> TOKEN_TYPE_PATTERNS = Arrays.stream(TokenType.values())
                                                                    .map(TokenType::getPattern)
                                                                    .collect(Collectors.toList());

    public List<String> tokenize(String text) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String recentStr = "";
        for(int i = 0; i < text.length(); i++){
            sb.append(text.charAt(i));
            if(isBlank(sb.toString())){
                sb = new StringBuilder();
                continue;
            }

            if(!TokenType.matches(sb.toString())){
                if(recentStr.equals(sb.toString())){
                    throw new XmlTagInvalidFormatException(recentStr);
                }
                recentStr = sb.toString();
                result.add(slice(sb.toString()));
                sb = new StringBuilder();
                i--;
            }
        }
        if(TokenType.matches(sb.toString())){
            result.add(sb.toString());
        }
        return result;
    }

    private boolean isBlank(String text) {
        return text.isBlank();
    }

    private String slice(String text){
        return text.substring(0, text.length() - 1);
    }
}
