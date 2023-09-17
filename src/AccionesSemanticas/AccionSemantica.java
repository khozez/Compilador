package AccionesSemanticas;

import java.io.BufferedInputStream;

public interface AccionSemantica {

    int ejecutar(BufferedInputStream lector, String lexema);
}
