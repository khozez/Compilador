package AccionesSemanticas;

import Etapas.AnalizadorLexico;
import Etapas.TablaPalabrasReservadas;
import java.io.BufferedInputStream;
import java.io.IOException;

public class AS7 implements AccionSemantica{
    //LEE EL ULTIMO CARACTER, CONCATENA Y DEVUELVE EL TOKEN ASOCIADO (PALABRAS RESERVADAS)

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        try {
            AnalizadorLexico.lexema += (char) lector.read();
            AnalizadorLexico.out_tokens.write(" ("+TablaPalabrasReservadas.obtenerIdentificador(AnalizadorLexico.lexema+") --> Palabra reservada: "+AnalizadorLexico.lexema));
            return TablaPalabrasReservadas.obtenerIdentificador(AnalizadorLexico.lexema);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ERROR
        return -5;
    }
}
