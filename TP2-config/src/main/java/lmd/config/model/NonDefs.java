package lmd.config.model;

import java.util.ArrayList;
import java.util.List;

import lmd.config.exception.NonObjectException;

public class NonDefs {

    private List<NonObject> nonObjects;

    public NonDefs() {
        nonObjects = new ArrayList<>();
    }

    public List<NonObject> getNonObjects() {
        return nonObjects;
    }

    public NonObject getNonObject(String name) {
        for(NonObject nonObj : nonObjects) {
            if(nonObj.getName().equals(name)) {
                return nonObj;
            }
        }
        return null;
    }

    public void setNonObjects(List<NonObject> nonObjects) {
        this.nonObjects = nonObjects;
    }

    public void addNonObject(NonObject nonObject) {
        nonObjects.add(nonObject);
    }

    public Non at(String id) throws NonObjectException {
        NonObject nonObject = getNonObject(id);
        if(nonObject == null) {
            throw new NonObjectException(id);
        }
        return new Non(nonObject);
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for(NonObject nonObject : nonObjects) {
            sb.append(nonObject.toJson());
            sb.append(",\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n}");
        return sb.toString();

    }

    public String toNon() {
        StringBuilder sb = new StringBuilder();
        for(NonObject nonObject : nonObjects) {
            sb.append(nonObject.toNon());
        }
        return sb.toString();
    }
    
}
