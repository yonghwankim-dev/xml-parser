import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Children {
    private List<XmlNode> child;

    public Children() {
        this.child = new ArrayList<>();
    }

    public void add(XmlNode node){
        child.add(node);
    }

    public String stringify(int spaceCnt) {
        String space = String.format(String.format("%%%ds", spaceCnt), " ");
        if(child.size() == 0){
            return "[]";
        }
        return child.stream()
                    .map(c-> c.stringify(spaceCnt))
                    .collect(Collectors.joining(String.format("%s, ", space),
                                            "[", String.format("%s]",space)));
    }
}
