package lmd.config.model;

import static org.petitparser.parser.primitive.CharacterParser.*;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;
import org.petitparser.parser.primitive.StringParser;

import lmd.config.field.StringField;

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
        String[] lines = content.split("\n");
        System.out.println(lines.length);
        for (int i = 0; i < lines.length; i++) {
            // check if id
            Parser id = word().star().seq(of(':')).end("\n");
            if (id.parse(lines[i]).isSuccess()) {
                String idStr = id.flatten().parse(lines[i]).get().toString();
                // creation object
                NonObject nonObj = new NonObject();
                nonObj.setName(idStr.replace(":", ""));
                System.out.println(nonObj.getName());
                i++;
                while (i < lines.length - 1 && lines[i].startsWith(".")) {
                    // System.out.println("line: " + lines[i]);
                    // creer les field*
                    Parser field = CharacterParser.of('.').seq(word().star()); // .field
                    String fieldStr = field.flatten().parse(lines[i]).get().toString();
                    String valueStr = lines[i].replace(fieldStr, "").trim();
                    System.out.println(fieldStr + " : " + valueStr);
                    // switch parser based on field value
                    Parser stringParser = CharacterParser.anyOf("'").seq(CharacterParser.noneOf("'").star());
                    // System.out.println(valueStr);
                    if (stringParser.parse(valueStr).isSuccess()) {
                        System.out.println("----------" + stringParser.flatten().parse(valueStr).get().toString());
                        nonObj.addField(fieldStr.replace(".", ""),
                                new StringField(stringParser.flatten().parse(valueStr).get().toString()));
                    }
                    i++;
                }
                nonDefs.addNonObject(nonObj);
            }
        }

        // String[] blocs = content.split("");
        // for (String bloc : blocs) {
        // String[] lines = bloc.split("\n");
        // for (String line : lines) {
        // Parser id = word().star().seq(of(':')).end("\n");
        // if(id.parse(line).isSuccess()) {
        // String idStr = id.parse(line).get().toString();
        // System.out.println(id.flatten().parse(line).get());
        // }
        // }
        // }

        // Parser parseBloc = word().star();
        // if(parseBloc.parse(content).isSuccess()) {
        // String bloc = parseBloc.parse(content).get().toString();
        // System.out.println(bloc);
        // }

        return nonDefs;
    }

    public String get(String champs) {
        // TODO implement
        return null;
    }

}
