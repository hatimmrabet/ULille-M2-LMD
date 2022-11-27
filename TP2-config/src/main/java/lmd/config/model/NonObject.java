package lmd.config.model;

import java.util.ArrayList;
import java.util.List;

import lmd.config.field.Field;

public class NonObject {

    String name;
    List<Field> fields;

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

    public List<Field> getFields() {
        return fields;
    }

    public void setFilds(List<Field> fields) {
        this.fields = fields;
    }

    public void addField(String name, Field value) {
        this.fields.stream().filter(f -> f.getName().equals(name)).findFirst().ifPresentOrElse(f -> {
            // if exists, replace
            this.fields.remove(f);
            this.fields.add(value);
        }, () -> {
            // if not exists, add
            this.fields.add(value);
        });
    }

    public Field getField(String name) {
        return this.fields.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }



}
