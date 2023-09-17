package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS6 implements AccionSemantica{
    //LEE UN CARACTER Y LO CONCATENA AL LEXEMA

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            AnalizadorLexico.lexema += (char) lector.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
