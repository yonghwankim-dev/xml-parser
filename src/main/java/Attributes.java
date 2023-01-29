import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Attributes {
    private Map<String, String> attributes;

    public Attributes() {
        this.attributes = new HashMap<>();
    }


    public void add(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public String toString() {
        return attributes.keySet()
                            .stream()
                            .map(key->String.format("%s=%s",key,attributes.get(key)))
                            .collect(Collectors.joining(", ", "[","]"));
    }
}
