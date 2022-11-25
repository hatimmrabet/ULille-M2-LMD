package lmd.config.model;

import static org.petitparser.parser.primitive.CharacterParser.noneOf;
import static org.petitparser.parser.primitive.CharacterParser.of;
import static org.petitparser.parser.primitive.CharacterParser.word;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import lmd.config.field.Field;
import lmd.config.field.AtField;
import lmd.config.field.DotField;
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
                    fieldStr = fieldStr.replace(".","");
                    String valueStr = line.substring(fieldStr.length() + 1).trim();
                    // switch parser based on field value
                    if (stringParser.parse(valueStr).isSuccess()) {
                        valueStr = stringParser.flatten().parse(valueStr).get().toString();
                        valueStr = valueStr.replaceAll("\'", "");
                        nonObj.addField(fieldStr, new StringField(fieldStr, valueStr));
                    }
                    else if(fieldParser.end("\n").parse(valueStr).isSuccess()) {
                        // valueStr = valueStr.replace(".","");
                        DotField dotField = new DotField();
                        dotField.setName(fieldStr);
                        dotField.setValue(valueStr);
                        nonObj.addField(fieldStr, dotField);
                    } else if(arobaseParser.parse(valueStr).isSuccess()) {
                        // valueStr = valueStr.replace("@", idStr);
                        AtField atField = new AtField();
                        atField.setName(fieldStr);
                        atField.setValue(valueStr);
                        nonObj.addField(fieldStr, atField);
                    } else {
                        System.out.println("Error parsing field : " + fieldStr + " : " + valueStr);
                    }
                    i++;
                    line = lines[i];
                }
                nonDefs.addNonObject(nonObj);
            }
            // check if isInstance
            if (instanceParser.parse(line).isSuccess())
            {
                String instanceStr = instanceParser.flatten().parse(line).get().toString();
                instanceStr = instanceStr.replace(":", "");
                String[] instanceStrSplit = instanceStr.split(" ");
                String idStr = instanceStrSplit[0];
                String instanceName = instanceStrSplit[1];
                NonObject newNonObj = new NonObject();
                newNonObj.setName(idStr);
                NonObject superObj = nonDefs.getNonObject(instanceName);
                // creation à partir de l'objet parent
                for(Field field : superObj.getfields()) {
                    if(field instanceof DotField) {
                        DotField dotField = (DotField) field;
                        DotField newDotField = new DotField();
                        newDotField.setName(dotField.getName());
                        newDotField.setValue(returnValue(superObj, dotField, idStr));
                        newNonObj.addField(field.getName(), newDotField);
                    }
                    if(field instanceof AtField) {
                        AtField atField = (AtField) field;
                        AtField newAtField = new AtField();
                        newAtField.setName(atField.getName());
                        newAtField.setValue(idStr);
                        newNonObj.addField(field.getName(), newAtField);
                    }
                }
                // verifier s'il y a des champs à modifier apres
                i++;
                System.out.println("===> "+lines[i]);
                line = lines[i];
                if(line.startsWith(".")) {
                    while (i < lines.length - 1 && line.startsWith(".")) {
                        // creer les fields
                        String fieldStr = fieldParser.flatten().parse(line).get().toString();
                        fieldStr = fieldStr.replace(".","");
                        String valueStr = line.substring(fieldStr.length() + 1).trim();
                        System.out.println("===> "+fieldStr+" : "+valueStr);
                        // switch parser based on field value
                        if (stringParser.parse(valueStr).isSuccess()) {
                            valueStr = stringParser.flatten().parse(valueStr).get().toString();
                            valueStr = valueStr.replaceAll("\'", "");
                            newNonObj.addField(fieldStr, new StringField(fieldStr, valueStr));
                        }
                        i++;
                        line = lines[i];
                    }
                } else {
                    i--;
                    line = lines[i];
                }
                nonDefs.addNonObject(newNonObj);
            }
        }
        return nonDefs;
    }

    public static String returnValue(NonObject superObj, Field field, String idStr)
    {
        String value = field.getValue();
        if(field instanceof DotField) {
            DotField dotField = (DotField) field;
            for(Field subField : superObj.getfields()) {
                if(("."+subField.getName()).equals(dotField.getValue())) {
                    if(subField instanceof DotField) {
                        value = returnValue(superObj, subField, idStr);
                    }
                    else if(subField instanceof AtField) {
                        value = idStr;
                    }
                    else {
                        value = subField.getValue();
                    }
                    break;
                }
            }
        }
        if(field instanceof AtField) {
            return idStr;
        }
        return value;
    }

    public String get(String champs) {
        // TODO implement
        return null;
    }

}
