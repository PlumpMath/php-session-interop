package parser;

import parser.objects.PHPObject;

public class PHPSessionEntry {
    private String fieldName;
    private PHPObject value;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public PHPObject getValue() {
        return value;
    }

    public void setValue(PHPObject value) {
        this.value = value;
    }
}
