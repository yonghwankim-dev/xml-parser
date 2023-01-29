import java.text.MessageFormat;

public class XmlNode {
    private final XMLNodeType type;
    private String tagName;
    private final Attributes attributes;
    private XmlNode parent;
    private final Children child;
    private final InnerText innerText;

    public XmlNode(XMLNodeType type) {
        this.type = type;
        this.tagName = null;
        this.attributes = new Attributes();
        this.parent = null;
        this.child = new Children();
        this.innerText = new InnerText();
    }

    public void addChild(XmlNode child) {
        this.child.add(child);
    }

    public boolean existParent(){
        return this.parent != null;
    }

    public void addInnerText(String text) {
        if(tagName == null){
            throw new XmlTagNotDeclaredRootElementException(text);
        }
        this.innerText.add(text);
    }

    public void addAttribute(String key, String value) {
        this.attributes.add(key, value);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public XmlNode getParent() {
        return this.parent;
    }

    public void setParent(XmlNode parent) {
        this.parent = parent;
    }

    public String stringify(int spaceCnt) {
        String space = String.format(String.format("%%%ds", spaceCnt), " ");
        String join = String.join(String.format(",\n%s", space),
                                    String.format("xmlNodeType: %s", type),
                                    String.format("%s tagName: %s", space, tagName),
                                    String.format("%s attribute: %s", space, attributes),
                                    String.format("%s child: %s", space, child.stringify(spaceCnt + 4)),
                                    String.format("%s innerText: %s", space, innerText));
        return String.format("{%s}\n", join);
    }

}
