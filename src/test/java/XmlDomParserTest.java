import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class XmlDomParserTest {
    @Test
    public void testcase1() throws URISyntaxException {
        //given
        URI fileUri = Objects.requireNonNull(TokenizerTest.class.getResource("index.xml")).toURI();
        File file = new File(fileUri);
        XmlDomParser parser = new XmlDomParser(file);
        //when
        DomTree actual = parser.parse();
        //then
        String expected = "type: root, child: [{xmlNodeType: ELEMENT, tagName: html, attribute: [{lang=\"ko\"}], child: [{xmlNodeType: ELEMENT, tagName: body, attribute: [], child: [{xmlNodeType: ELEMENT, tagName: p, attribute: [], child: [{xmlNodeType: ELEMENT, tagName: img, attribute: [{src=\"codesquard.kr\"}], child: [], innerText: []}, {xmlNodeType: ELEMENT, tagName: br, attribute: [], child: [], innerText: []}], innerText: [BOOST]}], innerText: []}], innerText: []}]";
        System.out.println(actual.stringify());
//        Assertions.assertThat(actual.stringify()).isEqualTo(expected);
    }
}
