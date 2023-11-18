package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;

public class AS4 implements AccionSemantica{
    //ASOCIADA AL CONTROL DE FLOTANTES

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        double valor = Double.parseDouble(lexema);
        if (valor <= Double.parseDouble(AnalizadorLexico.MIN_FLOAT_VALUE)){
            //INFORMAR WARNING, SE TRUNCA AL MENOR VALOR.
            Parser.anotar(Parser.ERROR_LEXICO, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Constante "+lexema+" fue truncado ya que es inferior al valor minimo");
            AnalizadorLexico.lexema = AnalizadorLexico.MIN_FLOAT_VALUE;

        } else if (valor >= Double.parseDouble(AnalizadorLexico.MAX_FLOAT_VALUE)) {
            //INFORMAR WARNING, SE TRUNCA AL MENOR VALOR.
            Parser.anotar(Parser.ERROR_LEXICO, "LINEA "+AnalizadorLexico.getCantLineas()+": WARNING! Constante "+lexema+" fue truncado ya que supera el valor maximo");
            AnalizadorLexico.lexema = AnalizadorLexico.MAX_FLOAT_VALUE;

        }
        boolean agregado = TablaSimbolos.agregarSimbolo(AnalizadorLexico.lexema);
        int id = TablaSimbolos.obtenerSimbolo(AnalizadorLexico.lexema);
        if (agregado){
            TablaSimbolos.agregarAtributo(id, "tipo", "FLOAT");
            TablaSimbolos.agregarAtributo(id, "referencias", "1");
        }
        else {
            int ref = Integer.parseInt(TablaSimbolos.obtenerAtributo(id, "referencias"));
            TablaSimbolos.modificarAtributo(id, "referencias", Integer.toString(ref+1));
        }

        //RETORNAMOS EL ID DEL TOKEN DE UNA CONSTANTE
        AnalizadorLexico.out_tokens.write("Constante float: "+AnalizadorLexico.lexema+" ("+AnalizadorLexico.CONSTANTE+")");
        return AnalizadorLexico.CONSTANTE;
    }
}