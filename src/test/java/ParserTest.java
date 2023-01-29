import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class ParserTest {

    private Tokenizer tokenizer;
    private Lexer lexer;
    @BeforeEach
    public void setup(){
        this.tokenizer = new Tokenizer();
        this.lexer = new Lexer();
    }

    @Test
    public void testcase1() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        DomTree actual = parser.analyzeSyntax();

        //then
        String expected = """
                type: root,\s
                child: [{xmlNodeType: ELEMENT,
                         tagName: span,
                         attribute: [],
                         child: [],
                         innerText: [hello]}
                    ]""";
        Assertions.assertThat(actual.stringify()).isEqualTo(expected);
    }
    
    @Test
    public void testcase2() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample3.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        DomTree actual = parser.analyzeSyntax();

        //then
        String expected = """
                type: root,\s
                child: [{xmlNodeType: ELEMENT,
                         tagName: span,
                         attribute: [lang="ko", class="aaa"],
                         child: [],
                         innerText: [hello]}
                    ]""";
        Assertions.assertThat(actual.stringify()).isEqualTo(expected);
    }

    @Test
    public void testcase4() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample4.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        DomTree actual = parser.analyzeSyntax();

        //then
        String expected = """
                type: root,\s
                child: [{xmlNodeType: ELEMENT,
                         tagName: p,
                         attribute: [],
                         child: [{xmlNodeType: ELEMENT,
                                 tagName: img,
                                 attribute: [src="codesquard.kr"],
                                 child: [],
                                 innerText: []}
                        ],
                         innerText: [BOOST]}
                    ]""";
        Assertions.assertThat(actual.stringify()).isEqualTo(expected);
    }

    @Test
    public void testcase5() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("index.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        DomTree actual = parser.analyzeSyntax();

        //then
        String expected = """
                type: root,\s
                child: [{xmlNodeType: ELEMENT,
                         tagName: html,
                         attribute: [lang="ko"],
                         child: [{xmlNodeType: ELEMENT,
                                 tagName: body,
                                 attribute: [],
                                 child: [{xmlNodeType: ELEMENT,
                                         tagName: p,
                                         attribute: [],
                                         child: [{xmlNodeType: ELEMENT,
                                                 tagName: img,
                                                 attribute: [src="codesquard.kr"],
                                                 child: [],
                                                 innerText: []}
                                , {xmlNodeType: ELEMENT,
                                                 tagName: br,
                                                 attribute: [],
                                                 child: [],
                                                 innerText: []}
                                ],
                                         innerText: [BOOST]}
                            ],
                                 innerText: []}
                        ],
                         innerText: []}
                    ]""";
        Assertions.assertThat(actual.stringify()).isEqualTo(expected);
    }


    @Test
    public void testcase6() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample5.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        DomTree actual = parser.analyzeSyntax();

        //then
        String expected = """
                type: root,\s
                child: [{xmlNodeType: ELEMENT,
                         tagName: p,
                         attribute: [],
                         child: [{xmlNodeType: ELEMENT,
                                 tagName: img,
                                 attribute: [src="codesquard.kr"],
                                 child: [],
                                 innerText: []}
                        , {xmlNodeType: ELEMENT,
                                 tagName: br,
                                 attribute: [],
                                 child: [],
                                 innerText: []}
                        ],
                         innerText: [BOOST]}
                    ]""";
        Assertions.assertThat(actual.stringify()).isEqualTo(expected);
    }

    @Test
    @DisplayName("태그가 제대로 닫히지 않는 경우")
    public void testcase7() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample7.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        Assertions.assertThatThrownBy(parser::analyzeSyntax);
        //then
    }

    @Test
    @DisplayName("태그간의 순서가 잘못된 경우")
    public void testcase8() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample8.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        Assertions.assertThatThrownBy(parser::analyzeSyntax);
        //then
    }

    @Test
    @DisplayName("닫는 태그의 이름이 맞지 않는 경우")
    public void testcase9() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample6.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        Assertions.assertThatThrownBy(parser::analyzeSyntax);
        //then
    }

    @Test
    @DisplayName("여는 태그의 형식이 올바르지 않는 경우")
    public void testcase10() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample9.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        Assertions.assertThatThrownBy(parser::analyzeSyntax);
        //then
    }

    @Test
    @DisplayName("태그가 없이 텍스트가 있는 경우")
    public void testcase11() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("sample10.xml")).toURI();
        File file = new File(fileUri);
        List<Token> tokens = lexer.analyzeLexical(tokenizer.tokenize(convertFileToString(file)));
        Parser parser = new Parser(tokens);
        //when
        Assertions.assertThatThrownBy(parser::analyzeSyntax);
        //then
    }

    private String convertFileToString(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}