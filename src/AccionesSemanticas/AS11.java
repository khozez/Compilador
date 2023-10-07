package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS11 implements AccionSemantica{
    //ASOCIADA AL CONTROL DE LOS ENTEROS: TERMINACION _s

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            AnalizadorLexico.lexema += (char) lector.read();
            String valor_numerico = AnalizadorLexico.lexema;
            valor_numerico = valor_numerico.replace("_", "");
            valor_numerico = valor_numerico.replace("s", "");

            int num = Integer.parseInt(valor_numerico);
            if (num > AnalizadorLexico.MAX_SHORT_INT){
                num = AnalizadorLexico.MAX_SHORT_INT;

                //INFORMAR WARNING, SE TRUNCA A MAXIMO LONG
                Parser.anotar(Parser.WARNING, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Constante "+AnalizadorLexico.lexema+" fue truncado ya que supera el valor maximo");
            }

            lexema = Integer.toString(num);
            lexema += '_';
            lexema += 's';
            if (TablaSimbolos.agregarSimbolo(lexema)){
                int id = TablaSimbolos.obtenerSimbolo(lexema);
                TablaSimbolos.agregarAtributo(id, "tipo", "short");
            }
            System.out.println("Constante entera: "+lexema);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return AnalizadorLexico.CONSTANTE;
    }
}
