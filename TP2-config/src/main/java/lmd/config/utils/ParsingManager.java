package lmd.config.utils;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

import lmd.config.field.AtField;
import lmd.config.field.ConcatField;
import lmd.config.field.DotField;
import lmd.config.field.Field;
import lmd.config.field.StringField;
import lmd.config.model.NonDefs;
import lmd.config.model.NonObject;

import static org.petitparser.parser.primitive.CharacterParser.noneOf;
import static org.petitparser.parser.primitive.CharacterParser.of;
import static org.petitparser.parser.primitive.CharacterParser.word;

import java.util.List;

public class ParsingManager {

    static private CharacterParser APOSTROPHE = of('\'');
    static private CharacterParser NOT_APOSTROPHE = noneOf("\'");
    static private CharacterParser POINT = of('.');
    static private CharacterParser AROBASE = of('@');
    static Parser idParser = word().plus().seq(of(':')).end("\n").flatten();
    static Parser instanceParser = word().plus().seq(of(':')).seq(CharacterParser.whitespace()).seq(word().star())
            .trim()
            .end("\n").flatten();
    static Parser fieldParser = POINT.seq(word().star()).flatten();
    static Parser externFieldParser = word().plus().seq(POINT).seq(word().plus()).flatten();
    static Parser stringParser = APOSTROPHE.seq(NOT_APOSTROPHE.star()).seq(APOSTROPHE).flatten();
    static Parser arobaseParser = AROBASE.flatten();
    static Parser concatParser = fieldParser
            .or(stringParser, arobaseParser, externFieldParser, CharacterParser.whitespace()).plus();

    public static void parse(NonDefs nonDefs, String content) {
        String[] lines = content.split("\n");
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
                    String fieldStr = fieldParser.parse(line).get().toString();
                    fieldStr = fieldStr.replace(".", "");
                    String valueStr = line.substring(fieldStr.length() + 1).trim();
                    // switch parser based on field value
                    if (stringParser.end("\n").parse(valueStr).isSuccess()) {
                        valueStr = stringParser.parse(valueStr).get().toString();
                        valueStr = valueStr.replaceAll("\'", "");
                        nonObj.addField(fieldStr, new StringField(fieldStr, valueStr));
                    } else if (fieldParser.end("\n").parse(valueStr).isSuccess()) {
                        DotField dotField = new DotField();
                        dotField.setName(fieldStr);
                        dotField.setValue(valueStr);
                        nonObj.addField(fieldStr, dotField);
                    } else if (arobaseParser.end("\n").parse(valueStr).isSuccess()) {
                        AtField atField = new AtField();
                        atField.setName(fieldStr);
                        atField.setValue(valueStr);
                        nonObj.addField(fieldStr, atField);
                    } else if (concatParser.parse(valueStr).isSuccess()) {
                        nonObj.addField(fieldStr, creerConcatField(fieldStr, valueStr, nonDefs));
                    }
                    // else {
                    // System.out.println("Error parsing field : !!!!!!!!");
                    // }
                    i++;
                }
                nonDefs.addNonObject(nonObj);
            }
            // check if isInstance
            if (instanceParser.parse(line).isSuccess()) {
                String instanceStr = instanceParser.parse(line).get().toString();
                instanceStr = instanceStr.replace(":", "");
                String[] instanceStrSplit = instanceStr.split(" ");
                String idStr = instanceStrSplit[0];
                String instanceName = instanceStrSplit[1];
                NonObject superObj = nonDefs.getNonObject(instanceName);
                NonObject newNonObj = creerInstance(superObj, idStr);
                // verifier s'il y a des champs à modifier apres
                i++;
                if (lines[i].startsWith(".")) {
                    while (i < lines.length && lines[i].startsWith(".")) {
                        line = lines[i];
                        // creer les fields
                        String fieldStr = fieldParser.flatten().parse(line).get().toString();
                        fieldStr = fieldStr.replace(".", "");
                        String valueStr = line.substring(fieldStr.length() + 1).trim();
                        // switch parser based on field value
                        if (stringParser.parse(valueStr).isSuccess()) {
                            valueStr = stringParser.parse(valueStr).get().toString();
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
    }

    public static String returnValue(NonObject superObj, Field field, String idStr) {
        String value = field.getValue();
        if (field instanceof DotField) {
            DotField dotField = (DotField) field;
            for (Field subField : superObj.getFields()) {
                if (("." + subField.getName()).equals(dotField.getValue())) {
                    if (subField instanceof DotField) {
                        value = returnValue(superObj, subField, idStr);
                    } else if (subField instanceof AtField) {
                        value = idStr;
                    } else {
                        value = subField.getValue();
                    }
                    break;
                }
            }
        }
        if (field instanceof AtField) {
            return idStr;
        }
        return value;
    }

    public static NonObject creerInstance(NonObject superObj, String idStr) {
        NonObject newNonObj = new NonObject();
        newNonObj.setName(idStr);
        for (Field field : superObj.getFields()) {
            if (field instanceof DotField) {
                DotField dotField = (DotField) field;
                DotField newDotField = new DotField();
                newDotField.setName(dotField.getName());
                newDotField.setValue(returnValue(superObj, dotField, idStr));
                newNonObj.addField(field.getName(), newDotField);
            }
            if (field instanceof AtField) {
                AtField atField = (AtField) field;
                AtField newAtField = new AtField();
                newAtField.setName(atField.getName());
                newAtField.setValue(idStr);
                newNonObj.addField(field.getName(), newAtField);
            }
            if (field instanceof ConcatField) {
                ConcatField concatField = (ConcatField) field;
                ConcatField newConcatField = new ConcatField();
                newConcatField.setName(concatField.getName());
                newConcatField.setValue(concatField.getValue());
                for (Field subField : concatField.getFields()) {
                    if (subField instanceof DotField) {
                        DotField dotField = (DotField) subField;
                        DotField newDotField = new DotField();
                        newDotField.setName(dotField.getName());
                        newDotField.setValue(returnValue(superObj, dotField, idStr));
                        newConcatField.addField(newDotField);
                    }
                    if (subField instanceof AtField) {
                        AtField atField = (AtField) subField;
                        AtField newAtField = new AtField();
                        newAtField.setName(atField.getName());
                        newAtField.setValue(idStr);
                        newConcatField.addField(newAtField);
                    }
                    if (subField instanceof StringField) {
                        StringField stringField = (StringField) subField;
                        StringField newStringField = new StringField();
                        newStringField.setName(stringField.getName());
                        newStringField.setValue(stringField.getValue());
                        newConcatField.addField(newStringField);
                    }
                }
                String concatValue = "";
                for (Field subField : newConcatField.getFields()) {
                    concatValue += subField.getValue();
                }
                newConcatField.setValue(concatValue);
                newNonObj.addField(field.getName(), newConcatField);
            }
        }
        return newNonObj;
    }

    public static ConcatField creerConcatField(String fieldStr, String valueStr, NonDefs nonDefs) {
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
            } else if (fieldParser.parse(champStr).isSuccess()) {
                DotField dotField = new DotField();
                dotField.setName(fieldStr);
                dotField.setValue(champStr);
                concatField.addField(dotField);
            } else if (arobaseParser.parse(champStr).isSuccess()) {
                AtField atField = new AtField();
                atField.setName(fieldStr);
                atField.setValue(champStr);
                concatField.addField(atField);
            } else if (externFieldParser.parse(champStr).isSuccess()) {
                String[] parts = champStr.split("\\.");
                NonObject superObj = nonDefs.getNonObject(parts[0]);
                Field field = superObj.getField(parts[1]);
                concatField.addField(field);
            }
        }
        return concatField;
    }

}
