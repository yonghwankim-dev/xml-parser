import java.util.*;
import java.util.regex.Pattern;

public class Parser {
    private final List<Token> tokens;

    public Parser(){
        this(null);
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public DomTree analyzeSyntax() {
        DomTree result = new DomTree();
        Stack<Token> stack = new Stack<>();
        Stack<String> tagOpenStack = new Stack<>();
        XmlNode cur_node = new XmlNode(XMLNodeType.ELEMENT);

        for(int i = 0; i < tokens.size(); i++){
            if(tokens.get(i).hasTokenType(TokenType.TAG_OPENING)){
                // 태그의 제목 및 속성들을 스택에 저장
                i = readTag(++i, stack);

                // 새 태그 생성 및 부모 노드 설정
                if(!cur_node.existParent()){
                    cur_node.setParent(cur_node);
                }else{
                    XmlNode parent = cur_node;
                    cur_node = new XmlNode(XMLNodeType.ELEMENT);
                    parent.addChild(cur_node);
                    cur_node.setParent(parent);
                }

                // 노드에 태그의 속성 저장
                saveTagAttribute(stack, cur_node);

                // 노드에 태그 이름 저장
                cur_node.setTagName(stack.pop().getValue());

                if(tokens.get(i).hasTokenType(TokenType.TAG_SELF_CLOSING)){
                    cur_node = cur_node.getParent();
                }else if(!tokens.get(i).hasTokenType(TokenType.TAG_SELF_CLOSING)){
                    if (!tagOpenStack.isEmpty() && tagOpenStack.peek().equals(cur_node.getTagName())) {
                        throw new XmlTagNotClosedException(tagOpenStack.peek());
                    }
                    tagOpenStack.push(cur_node.getTagName());
                }
            }else if(tokens.get(i).hasTokenType(TokenType.TEXT)){ // 노드에 태그 안에 텍스트 저장
                cur_node.addInnerText(tokens.get(i).getValue());
            }else if(tokens.get(i).hasTokenType(TokenType.TAG_CLOSE_OPENING)) { // 닫는 태그 시작
                if (!tokens.get(++i).equalTagName(cur_node.getTagName())) {
                    throw new XmlTagNotMatchException(tokens.get(i).getValue(), cur_node.getTagName());
                }
                if (!tagOpenStack.peek().equals(cur_node.getTagName())) {
                    throw new XmlTagDiffOrderClosedTagException(tagOpenStack.peek(), cur_node.getTagName());
                }
                tagOpenStack.pop();
                cur_node = cur_node.getParent();
            }else if(tokens.get(i).hasTokenType(TokenType.COMMENT)){
                i = readTag(++i, stack);
                stack.clear();
            }
        }

        result.add(cur_node);
        return result;
    }

    private int readTag(int idx, Stack<Token> stack){
        while(!tokens.get(idx).hasTokenType(TokenType.TAG_CLOSING) && !tokens.get(idx).hasTokenType(TokenType.TAG_SELF_CLOSING)){
            if(tokens.get(idx).hasTokenType(TokenType.TAG_OPENING)){
                throw new XmlTagNotClosedException(tokens.get(idx).getValue());
            }
            stack.push(tokens.get(idx));
            idx++;
        }
        return idx;
    }

    private void saveTagAttribute(Stack<Token> stack, XmlNode cur_node) {
        while(stack.size() != 1){
            String attrValue = stack.pop().getValue();
            stack.pop();
            String attrKey = stack.pop().getValue();
            cur_node.addAttribute(attrKey, attrValue);
        }
    }
}
