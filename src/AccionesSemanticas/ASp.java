package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class ASp implements AccionSemantica{
    //ACCION SEMANTICA PARA INFORMAR ERROR EN PALABRA RESERVADA

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            lector.mark(1);
            char value = (char) lector.read();
            lector.reset();
            System.out.println("LINEA "+ AnalizadorLexico.getCantLineas()+": ERROR, se esperaba una letra y se obtuvo '"+value+"'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
