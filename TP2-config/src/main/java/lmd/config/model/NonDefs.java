package lmd.config.model;

import java.util.ArrayList;
import java.util.List;

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

    public Non at(String id) {
        Non non = new Non();
        non.setId(id);

        return non;
    }
    
}
