import Etapas.AnalizadorLexico;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src\\codigo.txt");
        BufferedInputStream lector = new BufferedInputStream(fis);
        AnalizadorLexico Alexico = new AnalizadorLexico();
        Alexico.lector = lector;
        int id_token = -44;
        while (true) {
            try {
                lector.mark(1);
                int value = lector.read();
                lector.reset();
                if (value < 0) {
                    break;
                }

                //OBTENEMOS CARACTER DEL CODIGO, Y RESETEAMOS EL READER PARA QUE NO AFECTE A LAS AS
                lector.mark(1);
                char c = (char) lector.read();
                lector.reset();
                id_token = Alexico.getToken(lector, c);

                /*
                if (id_token != -1) {
                    System.out.println("Token: " + id_token);
                }*/
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}