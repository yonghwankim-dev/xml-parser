import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testcase1() throws IOException, URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample.xml").toURI();
        String text = convertFileToString(new File(fileUri));
        List<String> tokenize = new Tokenizer().tokenize(text);
        Lexer lexer = new Lexer();
        //when
        List<Token> tokens = lexer.analyzeLexical(tokenize);
        //then
        tokens.forEach(System.out::println);
    }

    @Test
    public void testcase2() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample2.xml").toURI();
        String text = convertFileToString(new File(fileUri));
        List<String> tokenize = new Tokenizer().tokenize(text);
        Lexer lexer = new Lexer();
        //when
        List<Token> tokens = lexer.analyzeLexical(tokenize);
        //then
        tokens.forEach(System.out::println);
    }

    @Test
    public void testcase3() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample3.xml").toURI();
        String text = convertFileToString(new File(fileUri));
        List<String> tokenize = new Tokenizer().tokenize(text);
        Lexer lexer = new Lexer();
        //when
        List<Token> tokens = lexer.analyzeLexical(tokenize);
        //then
        tokens.forEach(System.out::println);
    }

    @Test
    public void testcase4() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample4.xml").toURI();
        String text = convertFileToString(new File(fileUri));
        List<String> tokenize = new Tokenizer().tokenize(text);
        Lexer lexer = new Lexer();
        //when
        List<Token> tokens = lexer.analyzeLexical(tokenize);
        //then
        tokens.forEach(System.out::println);
    }

    private String convertFileToString(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}