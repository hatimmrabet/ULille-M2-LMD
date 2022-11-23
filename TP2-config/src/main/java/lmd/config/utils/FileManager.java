package lmd.config.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager {


    public static String getFileContent(String path) {
        File file = new File(path);
        String content = "";
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                content += myReader.nextLine() +"\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

    }
        return content;
    }
}
