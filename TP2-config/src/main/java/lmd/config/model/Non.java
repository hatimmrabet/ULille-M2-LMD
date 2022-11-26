package lmd.config.model;

import static org.petitparser.parser.primitive.CharacterParser.noneOf;
import static org.petitparser.parser.primitive.CharacterParser.of;
import static org.petitparser.parser.primitive.CharacterParser.word;

import java.util.List;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import lmd.config.field.Field;
import lmd.config.field.AtField;
import lmd.config.field.ConcatField;
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
        
        Parser idParser = word().plus().seq(of(':')).end("\n").flatten();
        Parser instanceParser = word().plus().seq(of(':')).seq(CharacterParser.whitespace()).seq(word().star()).trim().end("\n").flatten();
        Parser fieldParser = POINT.seq(word().star()).flatten();
        Parser externFieldParser = word().plus().seq(POINT).seq(word().plus()).flatten();
        Parser stringParser = APOSTROPHE.seq(NOT_APOSTROPHE.star()).seq(APOSTROPHE).flatten();
        Parser arobaseParser = AROBASE.flatten();
        
        // accept strings and whitespaces
        Parser concatParser = fieldParser.or(stringParser, arobaseParser, externFieldParser, CharacterParser.whitespace()).plus();

        // sfdgd sdfgdf gsfdgs
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
                while (i < lines.length && lines[i].startsWith(".")) {
                    line = lines[i];
                    // creer les fields
                    String fieldStr = fieldParser.flatten().parse(line).get().toString();
                    fieldStr = fieldStr.replace(".","");
                    String valueStr = line.substring(fieldStr.length() + 1).trim();
                    // switch parser based on field value
                    if (stringParser.end("\n").parse(valueStr).isSuccess()) {
                        valueStr = stringParser.flatten().parse(valueStr).get().toString();
                        valueStr = valueStr.replaceAll("\'", "");
                        nonObj.addField(fieldStr, new StringField(fieldStr, valueStr));
                    }
                    else if(fieldParser.end("\n").parse(valueStr).isSuccess()) {
                        DotField dotField = new DotField();
                        dotField.setName(fieldStr);
                        dotField.setValue(valueStr);
                        nonObj.addField(fieldStr, dotField);
                    } else if(arobaseParser.end("\n").parse(valueStr).isSuccess()) {
                        AtField atField = new AtField();
                        atField.setName(fieldStr);
                        atField.setValue(valueStr);
                        nonObj.addField(fieldStr, atField);
                    } else if(concatParser.parse(valueStr).isSuccess()) {
                        ConcatField concatField = new ConcatField();
                        concatField.setName(fieldStr);
                        concatField.setValue(valueStr);
                        List<Object> champs = concatParser.parse(valueStr).get();
                        // enlever les espaces
                        champs = champs.stream().filter(c -> !c.toString().trim().isEmpty()).toList();
                        // parcourir les champs
                        for (Object champ : champs) {
                            String champStr = champ.toString();
                            if (stringParser.parse(champStr).isSuccess()) {
                                StringField stringField = new StringField();
                                valueStr = stringParser.parse(champStr).get().toString();
                                valueStr = valueStr.replaceAll("\'", "");
                                stringField.setValue(valueStr);
                                concatField.addField(stringField);
                            }
                            else if(fieldParser.parse(champStr).isSuccess()) {
                                DotField dotField = new DotField();
                                dotField.setName(fieldStr);
                                dotField.setValue(champStr);
                                concatField.addField(dotField);
                            } else if(arobaseParser.parse(champStr).isSuccess()) {
                                AtField atField = new AtField();
                                atField.setName(fieldStr);
                                atField.setValue(champStr);
                                concatField.addField(atField);
                            } else if(externFieldParser.parse(champStr).isSuccess()) {
                                String[] parts = champStr.split("\\.");
                                NonObject superObj = nonDefs.getNonObject(parts[0]);
                                Field field = superObj.getField(parts[1]);
                                concatField.addField(field);
                            }
                        }
                        nonObj.addField(fieldStr, concatField);
                    } else {
                        System.out.println("Error parsing field : !!!!!!!!");
                    }
                    i++;
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
                    if(field instanceof ConcatField) {
                        ConcatField concatField = (ConcatField) field;
                        ConcatField newConcatField = new ConcatField();
                        newConcatField.setName(concatField.getName());
                        newConcatField.setValue(concatField.getValue());
                        for(Field subField : concatField.getFields()) {
                            if(subField instanceof DotField) {
                                DotField dotField = (DotField) subField;
                                DotField newDotField = new DotField();
                                newDotField.setName(dotField.getName());
                                newDotField.setValue(returnValue(superObj, dotField, idStr));
                                newConcatField.addField(newDotField);
                            }
                            if(subField instanceof AtField) {
                                AtField atField = (AtField) subField;
                                AtField newAtField = new AtField();
                                newAtField.setName(atField.getName());
                                newAtField.setValue(idStr);
                                newConcatField.addField(newAtField);
                            }
                            if(subField instanceof StringField) {
                                StringField stringField = (StringField) subField;
                                StringField newStringField = new StringField();
                                newStringField.setName(stringField.getName());
                                newStringField.setValue(stringField.getValue());
                                newConcatField.addField(newStringField);
                            }
                        }
                        String concatValue = "";
                        for(Field subField : newConcatField.getFields()) {
                            concatValue += subField.getValue();
                        }
                        newConcatField.setValue(concatValue);
                        newNonObj.addField(field.getName(), newConcatField);
                    }
                }
                // verifier s'il y a des champs à modifier apres
                i++;
                if(lines[i].startsWith(".")) {
                    while (i < lines.length && lines[i].startsWith(".")) {
                        line = lines[i];
                        // creer les fields
                        String fieldStr = fieldParser.flatten().parse(line).get().toString();
                        fieldStr = fieldStr.replace(".","");
                        String valueStr = line.substring(fieldStr.length() + 1).trim();
                        // switch parser based on field value
                        if (stringParser.parse(valueStr).isSuccess()) {
                            valueStr = stringParser.flatten().parse(valueStr).get().toString();
                            valueStr = valueStr.replaceAll("\'", "");
                            newNonObj.addField(fieldStr, new StringField(fieldStr, valueStr));
                        }
                        i++;
                    }
                } else {
                    i--;
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
