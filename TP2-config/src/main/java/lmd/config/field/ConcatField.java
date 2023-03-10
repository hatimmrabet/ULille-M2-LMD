package lmd.config.field;

import java.util.ArrayList;
import java.util.List;

public class ConcatField implements Field {

    private String name;
    private String value;
    private List<Field> fields = new ArrayList<>();


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

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field field) {
        this.fields.add(field);
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
