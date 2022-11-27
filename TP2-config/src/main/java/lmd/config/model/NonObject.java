package lmd.config.model;

import java.util.ArrayList;
import java.util.List;

import lmd.config.field.Field;

public class NonObject {

    String name;
    boolean isSuperObject;
    String superObjectName;
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

    public boolean getIsSuperObject() {
        return isSuperObject;
    }

    public String getSuperObjectName() {
        return superObjectName;
    }

    public void setSuperObjectName(String superObjectName) {
        this.superObjectName = superObjectName;
    }

    public void setisSuperObject(boolean isSuperObject) {
        this.isSuperObject = isSuperObject;
    }

    public List<Field> getFields() {
        return fields;
    }
    
    public Field getField(String name) {
        return this.fields.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
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


    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(name);
        sb.append("\":{");
        for(Field field : fields) {
            sb.append(field.toJson());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    public String toNon() {
        StringBuilder sb = new StringBuilder();
        if(isSuperObject) {
            sb.append(name+":\n");
        } else {
            sb.append(name+": "+superObjectName+"\n");
        }
        for(Field field : fields) {
            sb.append(field.toNon());
        }
        return sb.toString();
    }

}
