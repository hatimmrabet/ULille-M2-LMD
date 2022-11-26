package lmd.config.exception;

public class NonObjectException extends Exception {

    private static final long serialVersionUID = 1L;

    public NonObjectException(String objName) {
        super("NonObject " + objName + " not found");
    }
    
}
