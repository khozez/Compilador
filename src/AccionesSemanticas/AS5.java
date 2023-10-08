package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS5 implements AccionSemantica{
    //ASOCIADA AL CONTROL DE LOS ENTEROS LARGOS SIN SIGNO: TERMINACION _UL

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            AnalizadorLexico.lexema += (char) lector.read();
            String valor_numerico = AnalizadorLexico.lexema;
            valor_numerico = valor_numerico.replace("_", "");
            valor_numerico = valor_numerico.replace("u", "");
            valor_numerico = valor_numerico.replace("l", "");
            long num = Long.parseLong(valor_numerico);
            if (num > AnalizadorLexico.MAX_ULONG_INT){
                num = AnalizadorLexico.MAX_ULONG_INT;
                //INFORMAR WARNING, SE TRUNCA A MAXIMO LONG
                Parser.anotar(Parser.WARNING, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Constante "+AnalizadorLexico.lexema+" fue truncado ya que supera el valor maximo");
            } else if (num < 0) {
                num = 0;
                Parser.anotar(Parser.WARNING, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Constante "+AnalizadorLexico.lexema+" fue truncado ya que es inferior al valor minimo");
            }

            AnalizadorLexico.lexema = Long.toString(num);
            AnalizadorLexico.lexema += "_ul";
            boolean agregado = TablaSimbolos.agregarSimbolo(AnalizadorLexico.lexema);
            int id = TablaSimbolos.obtenerSimbolo(AnalizadorLexico.lexema);
            if (agregado)
            {
                TablaSimbolos.agregarAtributo(id, "tipo", "long");
                TablaSimbolos.agregarAtributo(id, "referencias", "1");
            }
            else {
                int ref = Integer.parseInt(TablaSimbolos.obtenerAtributo(id, "referencias"));
                TablaSimbolos.modificarAtributo(id, "referencias", Integer.toString(ref+1));
            }
            System.out.println("Constante entera long: "+AnalizadorLexico.lexema);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AnalizadorLexico.CONSTANTE;
    }
}
