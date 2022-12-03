package univ.lmd.generator;

public class Transition {
    private String start;
    private String target;
    private String action;

    public Transition(String start, String target, String action) {
        this.start = start;
        this.target = target;
        this.action = action;
    }

    public String getStart() {
        return start;
    }

    public void setstart(String start) {
        this.start = start;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
