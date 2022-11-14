package lmd.config;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import lmd.config.model.Non;
import lmd.config.model.NonDefs;

import static org.petitparser.parser.primitive.CharacterParser.*;

public class App 
{
    public static void main( String[] args )
    {
        Parser id = letter().seq(letter().or(digit()).star());
        Result id1 = id.parse("yeah");
        Result id2 = id.parse("f12");
        System.out.println(id1.get());
        System.out.println(id2.get());
    }
}
