import java.io.File;

public class Application {
    public static void main(String[] args) {
        String stringify = new XmlDomParser(new File(args[0])).parse().stringify();
        System.out.println(stringify);
    }
}
