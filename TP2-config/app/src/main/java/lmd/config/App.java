package lmd.config;
import static org.petitparser.parser.primitive.CharacterParser.digit;
import static org.petitparser.parser.primitive.CharacterParser.letter;

import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import lmd.config.field.Field;
import lmd.config.model.Non;
import lmd.config.model.NonDefs;
import lmd.config.model.NonObject;
import lmd.config.utils.FileManager;

public class App
{
    public static void main( String[] args )
    {
        Parser id = letter().seq(letter().or(digit()).star());
        Result id1 = id.parse("yeah");
        Result id2 = id.parse("f12");
        // System.out.println(id1.get());
        // System.out.println(id2.get());
        String content = FileManager.getFileContent("./config.non");
        NonDefs nonDefs = Non.fromString(content);
        for(NonObject nonObj : nonDefs.getNonObjects()) {
            System.out.println(nonObj.getName());
            for(Field field : nonObj.getfields()) {
                System.out.println(field.getName() + " | " + field.getValue());
            }
        }
    }
}
