package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS13 implements AccionSemantica{
    //LEE Y RETORNA EL TOKEN

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            char c = (char) lector.read();
            AnalizadorLexico.lexema += c;
            AnalizadorLexico.out_tokens.write(" ("+(int)AnalizadorLexico.lexema.charAt(0)+") --> "+AnalizadorLexico.lexema);
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
