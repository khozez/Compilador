package AccionesSemanticas;
import java.io.BufferedInputStream;
import Etapas.*;

public class AS1 implements AccionSemantica{
    /*
        ASOCIADA A LOS IDENTIFICADORES:
        - Trunca el identificador si su longitud es mayor a la establecida en el analizador lexico
     */

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        if (lexema.length() > AnalizadorLexico.MAX_LONG_ID){
            lexema = lexema.substring(0, AnalizadorLexico.MAX_LONG_ID);
            Parser.anotar(Parser.ERROR_LEXICO, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Identificador fue truncado ya que supera la longitud maxima.");
        }
        boolean simbolo_encontrado = TablaSimbolos.obtenerSimbolo(lexema) != TablaSimbolos.NO_ENCONTRADO;
        if (!simbolo_encontrado) {
            AnalizadorLexico.TS.agregarSimbolo(lexema);
        }
        AnalizadorLexico.out_tokens.write("Identificador: "+lexema+" ("+AnalizadorLexico.IDENTIFICADOR+")");
        return AnalizadorLexico.IDENTIFICADOR;
    }
}
