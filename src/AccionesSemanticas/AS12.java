package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;

public class AS12 implements AccionSemantica{

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        int pr = TablaPalabrasReservadas.obtenerIdentificador(lexema);
        if (pr == -1){
            Parser.anotar(Parser.ERROR_LEXICO, "LINEA "+ AnalizadorLexico.getCantLineas()+": WARNING! El identificador '"+lexema+"' se modificó a todos sus caracteres en minuscula.");
            String identificador = lexema.toLowerCase();
            if (identificador.length() > AnalizadorLexico.MAX_LONG_ID) {
                identificador = identificador.substring(0, AnalizadorLexico.MAX_LONG_ID);
            }
            System.out.println("Identificador: "+identificador);
            boolean simbolo_encontrado = TablaSimbolos.obtenerSimbolo(identificador) != TablaSimbolos.NO_ENCONTRADO;
            if (!simbolo_encontrado) {
                AnalizadorLexico.TS.agregarSimbolo(identificador);
            }
            return AnalizadorLexico.IDENTIFICADOR;
        }
        else {
            System.out.println("Palabra reservada: "+lexema);
            return pr;
        }
    }
}
