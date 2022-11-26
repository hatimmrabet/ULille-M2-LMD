package lmd.config.exception;

public class NonFieldException extends Exception {

    private static final long serialVersionUID = 1L;

    public NonFieldException(String fieldName) {
        super("NonField " + fieldName + " not found");
    }
    
}
