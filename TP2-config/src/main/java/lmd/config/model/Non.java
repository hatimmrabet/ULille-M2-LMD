package lmd.config.model;

import static org.petitparser.parser.primitive.CharacterParser.noneOf;
import static org.petitparser.parser.primitive.CharacterParser.of;
import static org.petitparser.parser.primitive.CharacterParser.word;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import lmd.config.field.Field;
import lmd.config.field.ReferenceField;
import lmd.config.field.StringField;

public class Non {

    private String id;
    final static private CharacterParser APOSTROPHE = of('\'');
    final static private CharacterParser NOT_APOSTROPHE = noneOf("\'");
    final static private CharacterParser POINT = of('.');
    final static private CharacterParser AROBASE = of('@');


    public Non() {
    }

    public Non(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static NonDefs fromString(String content) {
        NonDefs nonDefs = new NonDefs();
        String[] lines = content.split("\n");
        
        Parser idParser = word().plus().seq(of(':')).end("\n");
        Parser instanceParser = word().plus().seq(of(':')).seq(CharacterParser.whitespace()).seq(word().star()).trim().end("\n");
        Parser fieldParser = POINT.seq(word().star());
        Parser stringParser = APOSTROPHE.seq(NOT_APOSTROPHE.star());
        Parser arobaseParser = AROBASE.end();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // check if id
            if (idParser.parse(line).isSuccess()) {
                String idStr = idParser.flatten().parse(line).get().toString();
                idStr = idStr.replace(":", "");
                // creation object
                NonObject nonObj = new NonObject();
                nonObj.setName(idStr);
                i++;
                line = lines[i];
                while (i < lines.length - 1 && line.startsWith(".")) {
                    // creer les fields
                    String fieldStr = fieldParser.flatten().parse(line).get().toString();
                    String valueStr = line.substring(fieldStr.length() + 1).trim();
                    // switch parser based on field value
                    if (stringParser.parse(valueStr).isSuccess()) {
                        valueStr = stringParser.flatten().parse(valueStr).get().toString();
                        valueStr = valueStr.replaceAll("\'", "");
                        fieldStr = fieldStr.replace(".","");
                        nonObj.addField(fieldStr, new StringField(fieldStr, valueStr));
                    }
                    else if(fieldParser.end("\n").parse(valueStr).isSuccess()) {
                        fieldStr = fieldStr.replace(".","");
                        // valueStr = valueStr.replace(".","");
                        ReferenceField refField = new ReferenceField(fieldStr, valueStr);
                        refField.setValue("not implemented");
                        nonObj.addField(fieldStr, refField);
                    } else if(arobaseParser.parse(valueStr).isSuccess()) {
                        fieldStr = fieldStr.replace(".","");
                        // valueStr = valueStr.replace("@", idStr);
                        ReferenceField refField = new ReferenceField(fieldStr, valueStr);
                        refField.setValue("not implemented");
                        nonObj.addField(fieldStr, refField);
                    } else {
                        System.out.println("Error parsing field : " + fieldStr + " : " + valueStr);
                    }
                    i++;
                    line = lines[i];
                }
                nonDefs.addNonObject(nonObj);
            }
            // check if is instance
            if (instanceParser.parse(line).isSuccess())
            {
                String instanceStr = instanceParser.flatten().parse(line).get().toString();
                instanceStr = instanceStr.replace(":", "");
                String[] instanceStrSplit = instanceStr.split(" ");
                String idStr = instanceStrSplit[0];
                String instanceName = instanceStrSplit[1];
                NonObject nonObj = new NonObject();
                nonObj.setName(idStr);
                NonObject instance = nonDefs.getNonObject(instanceName);
                for(Field field : instance.getfields()) {
                    if(field instanceof ReferenceField) {
                        ReferenceField myRefField = new ReferenceField();
                        ReferenceField refField = (ReferenceField) field;
                        String refFieldName = refField.getRefName();
                        myRefField.setName(refField.getName());
                        myRefField.setRefName(refFieldName);
                        if(refFieldName.startsWith("@")) {
                            refFieldName = refFieldName.replace("@", idStr);
                            myRefField.setValue(refFieldName);
                        } 
                        if(refFieldName.startsWith(".")) {
                            refFieldName = refFieldName.replace(".", "");
                            for(Field f : instance.getfields()) {
                                if(f.getName().equals(refFieldName)) {
                                    myRefField.setRefName(f.getName());
                                    myRefField.setValue(((ReferenceField) f).getRefName());
                                }
                            }
                        }
                        nonObj.addField(field.getName(), myRefField);
                    }
                    else {
                        nonObj.addField(field.getName(), field);
                    }
                }
                nonDefs.addNonObject(nonObj);
            }
        }

        return nonDefs;
    }

    public String get(String champs) {
        // TODO implement
        return null;
    }

}
