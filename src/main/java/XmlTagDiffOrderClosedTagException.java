public class XmlTagDiffOrderClosedTagException extends RuntimeException {
    public XmlTagDiffOrderClosedTagException(String tagName1, String tagName2) {
        throw new XmlTagDiffOrderClosedTagException(String.format("닫는 태그의 순서가 다릅니다. %s != %s%n", tagName1, tagName2));
    }

    public XmlTagDiffOrderClosedTagException(String message) {
        super(message);
    }
}
