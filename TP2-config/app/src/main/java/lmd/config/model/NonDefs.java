package lmd.config.model;

public class NonDefs {

    private String content;

    public NonDefs() {        
    }
    public NonDefs(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Non at(String id) {
        Non non = new Non();
        non.setId(id);

        return non;
    }
    
}
