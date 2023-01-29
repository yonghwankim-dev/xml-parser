public class XmlTagNotDeclaredRootElementException extends RuntimeException {
    public XmlTagNotDeclaredRootElementException(String value) {
        super(value);
        System.out.printf("최상위 요소가 정의되지 않았습니다. %s", value);
    }
}
