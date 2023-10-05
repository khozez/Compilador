package AccionesSemanticas;

import Etapas.AnalizadorLexico;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ASe implements AccionSemantica{
    //ACCION SEMANTICA PARA INFORMAR ERROR EN ENTEROS

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            lector.mark(1);
            char value = (char) lector.read();
            lector.reset();
            Parser.anotar(Parser.ERROR, "LINEA "+ AnalizadorLexico.getCantLineas()+": ERROR, se esperaba 's' o 'ul' y se obtuvo '"+value+"'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}