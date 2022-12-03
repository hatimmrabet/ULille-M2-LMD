package univ.lmd.generator;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import univ.lmd.parser.DotParser;

public class JavaGenerator {

    private DotParser dotParser = new DotParser();
    private HashMap<String, ClassOrInterfaceDeclaration> classesHashMap = new HashMap<>();

    public JavaGenerator(){
        this.classesHashMap = new HashMap<>();
        this.dotParser = new DotParser();
    }

    public void generateFromString(String text) throws Exception {
        ArrayList<Object> list = dotParser.parse(text);
        String packageLine = "package univ.lmd.result;\n\n";
        List<ClassOrInterfaceDeclaration> classesAndInterface = generate(list);
        for(ClassOrInterfaceDeclaration c : classesAndInterface){
            File tmp = new File("./src/main/java/univ/lmd/result/", c.getName() + ".java");
            tmp.createNewFile();
            FileWriter myWriter = new FileWriter(tmp.getAbsolutePath());
            myWriter.write(packageLine + c.toString());
            myWriter.close();
        }
    }

    private List<ClassOrInterfaceDeclaration> generate(ArrayList<?> list) throws Exception {
        List<ClassOrInterfaceDeclaration> result = new ArrayList<>();
        ClassOrInterfaceDeclaration interfaceDeclaration = JavaClassesManip.generateInterface(list);
        List<Transition> transitionList = JavaClassesManip.generateTransitions((List<List<String>>)list.get(1));
        List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();
        List<String> classesNames = JavaClassesManip.getClasses((List) list.get(1));
        for (String className : classesNames) {
            classes.add(JavaClassesManip.generateClasseByName(className));
        }
        for (ClassOrInterfaceDeclaration classe : classes) {
            classesHashMap.put(classe.getNameAsString(), classe);
            JavaClassesManip.implementInterface(classe, interfaceDeclaration);
        }
        for(Transition t : transitionList) {
            JavaClassesManip.implementMethod(this.classesHashMap.get(t.getStart()), t);
        }
        JavaClassesManip.implementAllMethods(this.classesHashMap);
        result.add(interfaceDeclaration);
        for (ClassOrInterfaceDeclaration classe : classes) {
            result.add(classe);
        }
        return result;
    }

}
