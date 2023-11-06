package Etapas;

import java.io.*;

public class OutputManager {
    File file;
    FileWriter fw;

    public OutputManager(String file) {

        try {
            File myFile = new File(file);
            this.file = myFile;
            new PrintStream(this.file);

            if (myFile.createNewFile()) {
                //Si no existe, lo crea
                System.out.println("File created: " + myFile.getName());
            }

        } catch (IOException e) {
            System.out.println("Cannot create file");
        }

    }
    public void write(String line) {

        try {
            this.fw = new FileWriter(this.file, true);

            this.fw.write(line);
            this.fw.write('\n');

            this.fw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
