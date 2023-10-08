package AccionesSemanticas;

import Etapas.*;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ASc implements AccionSemantica{
    //ACCION SEMANTICA PARA INFORMAR ERROR EN COMPARADOR !!, se agrega el ! faltante para recuperarse.

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            lector.mark(1);
            char value = (char) lector.read();
            lector.reset();
            Parser.anotar(Parser.ERROR_LEXICO, "WARNING "+ AnalizadorLexico.getCantLineas()+": Se esperaba otro '!' y se obtuvo '"+value+"', se agrego.");
            lexema+='!';
            return TablaPalabrasReservadas.obtenerIdentificador(lexema);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
