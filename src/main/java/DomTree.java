public class DomTree {
    private final Children child;

    public DomTree() {
        this.child = new Children();
    }

    public String stringify() {
        return String.format("type: root, \nchild: %s", child.stringify(4));
    }

    public void add(XmlNode node){
        this.child.add(node);
    }
}
