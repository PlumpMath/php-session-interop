package parser.objects;

public class PHPObject<T> {
    protected T value;
    private String type;
    private Integer length;

    public PHPObject(T value, String type, Integer length) {
        this.value = value;
        this.type = type;
        this.length = length;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}