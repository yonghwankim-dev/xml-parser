public class XmlTagNotClosedException extends RuntimeException{
    public XmlTagNotClosedException(String tag) {
        super(tag);
        System.out.printf("%s 태그가 닫히지 않았습니다.%n", tag);
    }
}
