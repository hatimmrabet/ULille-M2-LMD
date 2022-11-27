package lmd.config.field;

public class StringField implements Field {

    private String name;
    private String value;

    public StringField() {
    }

    public StringField(String value) {
        this.value = value;
    }

    public StringField(String name, String value) {
        this.name = name;
        this.value = value;
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

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(name);
        sb.append("\":\"");
        sb.append(value);
        sb.append("\"");
        return sb.toString();
    }
}
