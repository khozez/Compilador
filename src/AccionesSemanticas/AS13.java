package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import Etapas.Parser;

import java.io.BufferedInputStream;
import java.io.IOException;

public class AS13 implements AccionSemantica{
    //LEE Y RETORNA EL TOKEN

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            char c = (char) lector.read();
            if (c == '+') {
                lector.mark(1);  // marco la posición para poder volver atras si no encuentro un '='
                int siguiente = -1;
                try {
                    siguiente = lector.read();
                } catch (IOException e) {  // hay que catchear porque se hace E/S
                    e.printStackTrace();
                }
                if (siguiente == '=') {
                    lexema = "+=";
                    return Parser.MASIGUAL;
                } else {
                    try {
                        lector.reset();  // reseteamos la posicion
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            AnalizadorLexico.lexema += c;
            AnalizadorLexico.out_tokens.write(" ("+(int)AnalizadorLexico.lexema.charAt(0)+") --> "+AnalizadorLexico.lexema);
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
