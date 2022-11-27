package lmd.config.model;

import java.util.List;

import lmd.config.exception.NonFieldException;
import lmd.config.field.Field;
import lmd.config.utils.ParsingManager;

public class Non {
            
    private String id;
    private List<Field> fields;

    public Non(NonObject nonObject) {
        this.id = nonObject.getName();
        this.fields = nonObject.getFields();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id() {
        return this.getId();
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public static NonDefs fromString(String content) {
        ParsingManager pm = new ParsingManager(content);
        return pm.parse();
    }

    public String get(String champ) throws NonFieldException {
        for (Field field : this.getFields()) {
            if (field.getName().equals(champ)) {
                return field.getValue();
            }
        }
        throw new NonFieldException(champ);
    }

}
