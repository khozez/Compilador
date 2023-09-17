package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import Etapas.TablaSimbolos;
import java.io.BufferedInputStream;

public class AS4 implements AccionSemantica{
    //ASOCIADA AL CONTROL DE FLOTANTES

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        double valor = Double.parseDouble(lexema);
        if (valor <= AnalizadorLexico.MIN_FLOAT_VALUE){
            //INFORMAR WARNING, SE TRUNCA AL MENOR VALOR.
            System.out.println("WARNING: Constante"+lexema+" fue truncado ya que es inferior al valor minimo");
            valor = AnalizadorLexico.MIN_FLOAT_VALUE;

        } else if (valor >= AnalizadorLexico.MAX_FLOAT_VALUE) {
            //INFORMAR WARNING, SE TRUNCA AL MENOR VALOR.
            System.out.println("WARNING: Constante"+lexema+" fue truncado ya que supera el valor maximo");
            valor = AnalizadorLexico.MAX_FLOAT_VALUE;

        }

        lexema = Double.toString(valor);
        if (TablaSimbolos.agregarSimbolo(lexema)){
            int id = TablaSimbolos.obtenerSimbolo(lexema);
            TablaSimbolos.agregarAtributo(id, "tipo", "float");
        }

        //RETORNAMOS EL ID DEL TOKEN DE UNA CONSTANTE
        System.out.println("Constante float: "+lexema);
        return 259;
    }
}