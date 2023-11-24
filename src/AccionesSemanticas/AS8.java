package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS8 implements AccionSemantica{
    //RETORNA EL TOKEN

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        AnalizadorLexico.out_tokens.write(" ("+(int)AnalizadorLexico.lexema.charAt(0)+") --> "+AnalizadorLexico.lexema);
        char token = lexema.charAt(0);
        return token;
    }
}
