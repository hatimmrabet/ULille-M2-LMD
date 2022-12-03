package univ.lmd.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;


public class JavaClassesManip {

    /* Interface */

    public static ClassOrInterfaceDeclaration generateInterface(ArrayList<?> list) {
        String interfaceName = (String) list.get(0);
        CompilationUnit compilationUnit = new CompilationUnit();
        final ClassOrInterfaceType returnType = new ClassOrInterfaceType(interfaceName);
        final ClassOrInterfaceDeclaration myInterface = compilationUnit.setPackageDeclaration("univ.lmd.result")
                .addInterface(interfaceName)
                .setPublic(true);
        List<String> methods = getMethodes((ArrayList<String>) list.get(1));
        methods.add("next");
        for (String m : methods) {
            NodeList<Modifier> nodeList = new NodeList();
            nodeList.add(new Modifier(Modifier.Keyword.PUBLIC));
            MethodDeclaration method = new MethodDeclaration(nodeList, returnType, m);
            myInterface.addMember(method.removeBody());
        }
        return myInterface;
    }


    public static ClassOrInterfaceDeclaration implementInterface(ClassOrInterfaceDeclaration _class, 
    ClassOrInterfaceDeclaration _interface) {
        ClassOrInterfaceType type = new ClassOrInterfaceType(_interface.getName().asString());
        _class.addImplementedType(type);
        for (MethodDeclaration methodDeclaration : _interface.getMethods()) {
            _class.addMember(methodDeclaration.clone());
        }
        return _class;
    }

    /* Classe */

    public static List<String> getClasses(List<List> list) {
        List<String> classes = new ArrayList<>();
        for (List<String> line : list) {
            String startState = line.get(0);
            for (String s : prepareClassLine(startState)) {
                if (!classes.contains(s)) {
                    classes.add(s);
                }
            }
            String targetState = line.get(1);
            for (String s : prepareClassLine(targetState)) {
                if (!classes.contains(s)) {
                    classes.add(s);
                }
            }
        }
        return classes;
    }

    public static ClassOrInterfaceDeclaration generateClasseByName(String name) {
        CompilationUnit compilationUnit = new CompilationUnit();
        ClassOrInterfaceDeclaration myClass = compilationUnit
                .addClass(name)
                .setPublic(true);
        return myClass;
    }

    public static List<String> prepareClassLine(String text) {
        ArrayList<String> list = new ArrayList<String>();
        String cleantext = text.trim().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(" ", "");
        String[] states = cleantext.split(",");
        for (String state : states) {
            list.add(state);
        }
        return list;
    }

    /* Methodes */

    private static List<String> getMethodes(ArrayList<?> list) {
        List<String> methodes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> line = (List<String>) list.get(i);
            if (line.size() > 2) {
                String method = line.get(2).replaceAll("\"", "");
                if (!methodes.contains(method)) {
                    methodes.add(method);
                }
            }
        }
        return methodes;
    }

    public static ClassOrInterfaceDeclaration implementMethod(ClassOrInterfaceDeclaration classe, Transition transition) {
        ClassOrInterfaceDeclaration _class = classe;
        NodeList<Statement> nodeList = new NodeList<>();
        nodeList.add(new ReturnStmt("new " + transition.getTarget() + "()"));
        _class.getMethodsByName(transition.getAction()).get(0).setBody(new BlockStmt(nodeList));
        return _class;
    }

    public static void implementAllMethods(HashMap<String, ClassOrInterfaceDeclaration> classesHashMap) {
        for (ClassOrInterfaceDeclaration classe : classesHashMap.values()) {
            for (MethodDeclaration method : classe.getMethods()) {
                if (method.getBody().isEmpty()) {
                    NodeList<Statement> nodeList = new NodeList<>();
                    nodeList.add(new ReturnStmt("this"));
                    method.setBody(new BlockStmt(nodeList));
                }
            }
        }
    }


    /* Transition */

    public static List<Transition> generateTransitions(List<List<String>> list) throws Exception {
        List<Transition> transitions = new ArrayList<Transition>();
        for (List<String> line : list) {
            String from = line.get(0);
            String target = line.get(1);
            String action = line.size() >= 3 ? line.get(2).replaceAll("\"", "") : "next";
            if (from.contains(",")) {
                for (String s : prepareClassLine(from)) {
                    transitions.add(new Transition(s, target, action));
                }
            } else {
                transitions.add(new Transition(from, target, action));
            }
        }
        return transitions;
    }

    
}
