package AccionesSemanticas;

import Etapas.*;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ASs implements AccionSemantica{
    //ACCION SEMANTICA PARA INFORMAR ERROR EN FLOTANTES (signo exponente)

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            lector.mark(1);
            char value = (char) lector.read();
            lector.reset();
            Parser.anotar(Parser.WARNING, "LINEA "+ AnalizadorLexico.getCantLineas()+": WARNING! Falta signo del exponente, se considera positivo.");
            AnalizadorLexico.lexema+='+';
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}