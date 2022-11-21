package lmd.config.field;

public class StringField implements Field {

    private String name;
    private String value;

    public StringField() {
    }

    public StringField(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
