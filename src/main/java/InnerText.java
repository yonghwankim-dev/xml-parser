import java.util.ArrayList;
import java.util.List;

public class InnerText {
    private List<String> innerTexts;

    public InnerText() {
        this.innerTexts = new ArrayList<>();
    }

    public void add(String text){
        this.innerTexts.add(text);
    }

    @Override
    public String toString() {
        return innerTexts.toString();
    }
}
