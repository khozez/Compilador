package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;

public class AS12 implements AccionSemantica{

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        int pr = TablaPalabrasReservadas.obtenerIdentificador(lexema);
        if (pr == -1){
            Parser.anotar(Parser.WARNING, "LINEA "+ AnalizadorLexico.getCantLineas()+": WARNING! El identificador '"+lexema+"' se modificó a todos sus caracteres en minuscula.");
            String identificador = lexema.toLowerCase();
            if (identificador.length() > AnalizadorLexico.MAX_LONG_ID) {
                identificador = identificador.substring(0, AnalizadorLexico.MAX_LONG_ID);
            }
            AnalizadorLexico.out_tokens.write("Identificador: "+identificador+" ("+AnalizadorLexico.IDENTIFICADOR+")");
            boolean simbolo_encontrado = TablaSimbolos.obtenerSimbolo(identificador) != TablaSimbolos.NO_ENCONTRADO;
            if (!simbolo_encontrado) {
                AnalizadorLexico.TS.agregarSimbolo(identificador);
            }
            return AnalizadorLexico.IDENTIFICADOR;
        }
        else {
            AnalizadorLexico.out_tokens.write("Palabra reservada: "+lexema+" ("+pr+")");
            return pr;
        }
    }
}
