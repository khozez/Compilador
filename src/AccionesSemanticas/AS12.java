package AccionesSemanticas;

import Etapas.*;
import java.io.BufferedInputStream;

public class AS12 implements AccionSemantica{

    @Override
    public int ejecutar(BufferedInputStream lector, String lexema) {
        int pr = TablaPalabrasReservadas.obtenerIdentificador(lexema);
        if (pr == -1){
            Parser.anotar(Parser.ERROR_LEXICO, "LINEA "+ AnalizadorLexico.getCantLineas()+": ERROR, '"+lexema+"' no es una palabra reservada");
            return 0;
        }
        else {
            System.out.println("Palabra reservada: "+lexema);
            return pr;
        }
    }
}
