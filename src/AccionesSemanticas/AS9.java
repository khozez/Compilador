package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS9 implements AccionSemantica{
    //LEE EL CARACTER Y LO DESCARTA (COMENTARIOS)

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            lector.read();
            AnalizadorLexico.lexema = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
