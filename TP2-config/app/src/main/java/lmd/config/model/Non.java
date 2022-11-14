package lmd.config.model;

import static org.petitparser.parser.primitive.CharacterParser.*;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.StringParser;

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
        NonDefs nonDefs = new NonDefs();

        String[] blocs = content.split("\n\n");
        for (String bloc : blocs) {
            String[] lines = bloc.split("\n");
            for (String line : lines) {
                Parser id = word().star().seq(of(':')).end("\n");
                if(id.parse(line).isSuccess()) {
                    String idStr = id.parse(line).get().toString();
                    System.out.println(id.flatten().parse(line).get());
                }
            }
        }

        // Parser id = any().star().and().seq(of('\n')).seq(of('\n'));
        // if(id.parse(content).isSuccess()) {
        //     System.out.println(id.flatten().parse(content).get());
        // }



        

        return nonDefs;
    }

    public String get(String champs) {
        // TODO implement
        return null;
    }

}
