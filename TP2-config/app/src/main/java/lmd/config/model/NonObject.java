package lmd.config.model;

import java.util.ArrayList;
import java.util.List;

public class NonObject {
    String name;
    List<?> fileds;

    public NonObject() {
        fileds = new ArrayList<>();
    }

    public NonObject(String name) {
        this.name = name;
        this.fileds = new ArrayList<>();
    }




}
