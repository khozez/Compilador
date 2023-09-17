package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import Etapas.TablaSimbolos;
import java.io.BufferedInputStream;

public class AS11 implements AccionSemantica{
    //ASOCIADA AL CONTROL DE LOS ENTEROS: TERMINACION _s

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        String valor_numerico = lexema;
        valor_numerico = valor_numerico.replace("_", "");
        valor_numerico = valor_numerico.replace("s", "");

        long num = Long.parseLong(valor_numerico);
        if (num > AnalizadorLexico.MAX_SHORT_INT){
            num = AnalizadorLexico.MAX_SHORT_INT;

            //INFORMAR WARNING, SE TRUNCA A MAXIMO LONG
            System.out.println("WARNING: Constante"+lexema+" fue truncado ya que supera el valor maximo");
        }

        lexema = Long.toString(num);
        lexema += '_';
        lexema += 's';
        if (TablaSimbolos.agregarSimbolo(lexema)){
            int id = TablaSimbolos.obtenerSimbolo(lexema);
            TablaSimbolos.agregarAtributo(id, "tipo", "short");
        }
        System.out.println("Constante entera: "+lexema);
        return 259;
    }
}
