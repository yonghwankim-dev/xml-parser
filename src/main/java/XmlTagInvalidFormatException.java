public class XmlTagInvalidFormatException extends RuntimeException {
    public XmlTagInvalidFormatException(String text) {
        super(text);
        System.out.printf("올바른 XML 형식이 아닙니다. : %s%n", text);
    }
}
