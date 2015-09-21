import java.util.ArrayList;

public class PHPSessionRepresentationSerializer {

    private ArrayList<Class> registeredTypes = new ArrayList<Class>();

    public PHPSessionRepresentationSerializer() {}

    public void registerType(Class c) {
        registeredTypes.add(c);
    }

    public void deserialize() {

    }
}
