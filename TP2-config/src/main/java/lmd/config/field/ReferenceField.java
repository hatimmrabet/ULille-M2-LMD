package lmd.config.field;

public class ReferenceField implements Field {

    private String name;
    private String refName;
    private String value;

    public ReferenceField() {
    }

    public ReferenceField(String name, String refName) {
        this.name = name;
        this.refName = refName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

   
}
