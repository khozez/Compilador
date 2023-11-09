package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import Etapas.TablaSimbolos;

import java.io.BufferedInputStream;
import java.io.IOException;

public class AS10 implements AccionSemantica{
    //AGREGA LA CADENA MULTILINEA A LA TABLA DE SIMBOLOS (sin saltos)

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            lexema += (char) lector.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lexema_sin_saltos = lexema.replaceAll("\\r|\\n", "");
        if (TablaSimbolos.obtenerSimbolo(lexema_sin_saltos) == TablaSimbolos.NO_ENCONTRADO) {
            TablaSimbolos.agregarSimbolo(lexema_sin_saltos);
        }
        AnalizadorLexico.lexema = lexema_sin_saltos;
        return AnalizadorLexico.CADENA;
    }
}