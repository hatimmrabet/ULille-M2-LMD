package lmd.config.model;

import java.util.ArrayList;
import java.util.List;

import lmd.config.field.Field;

public class NonObject {

    String name;
    List<Field> fields;

    public NonObject() {
        fields = new ArrayList<>();
    }

    public NonObject(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getfields() {
        return fields;
    }

    public void setFilds(List<Field> fields) {
        this.fields = fields;
    }

    public void addField(String name, Field value) {
        fields.add(value);
    }



}
