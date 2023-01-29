import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenizerTest {
    @Test
    public void testcase1() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample.xml").toURI();
        Tokenizer tokenizer = new Tokenizer();
        File file = new File(fileUri);
        String text = convertFileToString(file);
        //when
        List<String> actual = tokenizer.tokenize(text);
        //then
        Assertions.assertThat(actual).isEqualTo(List.of(
                "<","span",">","hello","</","span",">"
        ));
    }

    @Test
    public void testcase2() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample2.xml").toURI();
        Tokenizer tokenizer = new Tokenizer();
        File file = new File(fileUri);
        String text = convertFileToString(file);
        //when
        List<String> actual = tokenizer.tokenize(text);
        //then
        Assertions.assertThat(actual).isEqualTo(List.of(
                "<","span","lang","=","\"ko\"",">","hello","</","span",">"
        ));
    }

    @Test
    public void testcase3() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample4.xml").toURI();
        Tokenizer tokenizer = new Tokenizer();
        File file = new File(fileUri);
        String text = convertFileToString(file);
        //when
        List<String> actual = tokenizer.tokenize(text);
        //then
        Assertions.assertThat(actual).isEqualTo(List.of(
                "<","p",">","BOOST","<","img","src","=","\"codesquard.kr\"",">","</","img",">","</","p",">"
        ));
    }

    @Test
    public void testcase4() throws URISyntaxException {
        //given
        URI fileUri = TokenizerTest.class.getResource("sample11.xml").toURI();
        Tokenizer tokenizer = new Tokenizer();
        File file = new File(fileUri);
        String text = convertFileToString(file);
        //when
        Assertions.assertThatThrownBy(()->tokenizer.tokenize(text));
        //then

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