package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS8 implements AccionSemantica{
    //RETORNA EL TOKEN

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        System.out.println(AnalizadorLexico.lexema);
        char token = lexema.charAt(0);
        return token;
    }
}
