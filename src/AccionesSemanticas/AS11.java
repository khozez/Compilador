package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS11 implements AccionSemantica{
    //ASOCIADA AL CONTROL DE LOS ENTEROS: TERMINACION _s

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            boolean truncada = false;
            AnalizadorLexico.lexema += (char) lector.read();
            String valor_numerico = AnalizadorLexico.lexema;
            valor_numerico = valor_numerico.replace("_", "");
            valor_numerico = valor_numerico.replace("s", "");

            int num = Integer.parseInt(valor_numerico);
            if (num > AnalizadorLexico.MAX_SHORT_INT){
                truncada = true;
                num = AnalizadorLexico.MAX_SHORT_INT;
                //INFORMAR WARNING, SE TRUNCA A MAXIMO SHORT
                Parser.anotar(Parser.ERROR_LEXICO, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Constante "+AnalizadorLexico.lexema+" fue truncado ya que supera el valor maximo");
            }

            AnalizadorLexico.lexema = Integer.toString(num);
            AnalizadorLexico.lexema += "_s";
            boolean agregado = TablaSimbolos.agregarSimbolo(AnalizadorLexico.lexema);
            int id = TablaSimbolos.obtenerSimbolo(AnalizadorLexico.lexema);
            if (agregado)
            {
                TablaSimbolos.agregarAtributo(id, "tipo", "short");
                TablaSimbolos.agregarAtributo(id, "referencias", "1");
            }
            else {
                int ref = Integer.parseInt(TablaSimbolos.obtenerAtributo(id, "referencias"));
                TablaSimbolos.modificarAtributo(id, "referencias", Integer.toString(ref+1));
            }
            if (truncada)
            {
                TablaSimbolos.agregarAtributo(id, "valor_original", valor_numerico);
            }
            System.out.println("Constante entera: "+AnalizadorLexico.lexema);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return AnalizadorLexico.CONSTANTE;
    }
}
