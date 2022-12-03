package univ.lmd;

import univ.lmd.generator.JavaGenerator;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        String text = "digraph HeroState {\n" +
        "Standing -> Jumping [ label = \"up\" ];\n" +
        "Jumping -> Diving [ label = \"down\" ];\n" +
        "{Jumping, Diving} -> Standing;\n" +
        "Standing -> Crouching [ label = \"down\" ];\n" +
        "Crouching -> Standing [ label = \"release\" ];\n" +
                "}";
        JavaGenerator generator = new JavaGenerator();
        generator.generateFromString(text);

    }
}
