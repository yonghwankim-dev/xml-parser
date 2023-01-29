import java.util.regex.Pattern;

public enum TokenType {
    TAG_OPENING(Pattern.compile("<")),
    TAG_CLOSING(Pattern.compile(">")),
    TAG_CLOSE_OPENING(Pattern.compile("</")),
    TAG_SELF_CLOSING(Pattern.compile("/>*")),
    COMMENT(Pattern.compile("<!")),
    TEXT(Pattern.compile("[\\w.]+")),
    ASSIGNMENT(Pattern.compile("=")),
    ATTRIBUTE_VALUE(Pattern.compile("\"[^\n\r\"]*\"*")),
    INVALID(Pattern.compile(""));

    private Pattern pattern;

    TokenType(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public static TokenType find(String text){
        for(TokenType type : values()){
            if(type.getPattern().matcher(text).matches()){
                return type;
            }
        }
        return INVALID;
    }

    public static boolean matches(String text){
        for(TokenType type : values()){
            if(type.pattern.matcher(text).matches()){
                return true;
            }
        }
        return false;
    }
}
