package lmd.config.field;

public class AtField implements Field {
    
    private String name;
    private String value;
    private Field field;

    public AtField() {
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

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
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

    @Override
    public String toNon() {
        StringBuilder sb = new StringBuilder();
        sb.append("." + name + " " + value + "\n");
        return sb.toString();
    }

}
