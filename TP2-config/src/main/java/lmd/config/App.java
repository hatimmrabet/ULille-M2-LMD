package lmd.config;
import lmd.config.exception.NonFieldException;
import lmd.config.exception.NonObjectException;
import lmd.config.field.Field;
import lmd.config.model.Non;
import lmd.config.model.NonDefs;
import lmd.config.model.NonObject;
import lmd.config.utils.FileManager;

public class App
{
    public static void main( String[] args ) throws NonObjectException, NonFieldException
    {
        String content = FileManager.getFileContent("./config.non");
        NonDefs nonDefs = Non.fromString(content);
        for(NonObject nonObj : nonDefs.getNonObjects()) {
            System.out.println(nonObj.getName());
            for(Field field : nonObj.getFields()) {
                System.out.println(field.getName() + " | " + field.getValue());
            }
        }
        // nonDefs.at("student").get("aa");
    }
}
