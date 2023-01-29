import java.io.*;
import java.util.List;

public class XmlDomParser {
    private final String text;
    private final Tokenizer tokenizer;
    private final Lexer lexer;

    public XmlDomParser(File file){
        this.text = convertFileToString(file);
        this.tokenizer = new Tokenizer();
        this.lexer = new Lexer();
    }

    private String convertFileToString(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DomTree parse() {
        List<String> tokenCandidate = tokenizer.tokenize(text);
        List<Token> tokens = lexer.analyzeLexical(tokenCandidate);
        Parser parser = new Parser(tokens);
        return parser.analyzeSyntax();
    }
}
