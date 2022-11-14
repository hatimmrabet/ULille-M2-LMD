package lmd.config.model;

public class Non {

    private String id;
    private String content;

    public Non() {
    }
    public Non(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public static NonDefs fromString(String content) {
        return new NonDefs(content);
    }

    public String get(String champs) {
        //TODO implement
        return null;
    }

    
}
