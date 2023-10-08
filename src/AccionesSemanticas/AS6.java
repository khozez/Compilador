package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS6 implements AccionSemantica{
    //LEE UN CARACTER Y LO CONCATENA AL LEXEMA

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            char c = (char) lector.read();
            AnalizadorLexico.lexema += c;

            if (c == AnalizadorLexico.NUEVA_LINEA)
                AnalizadorLexico.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
