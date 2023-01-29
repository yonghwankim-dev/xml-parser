public class XmlTagNotMatchException extends RuntimeException {
    public XmlTagNotMatchException(String value, String tagName) {
        throw new XmlTagNotMatchException(String.format("닫는 태그의 이름이 다릅니다. %s != %s%n", value, tagName));
    }

    public XmlTagNotMatchException(String message) {
        super(message);
        System.out.println(message);
    }
}
