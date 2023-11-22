package Assembler;

import Etapas.*;

public class EstructuraClase extends Generador implements GeneradorEstructura {


    public String generar(Nodo nodo) {
        String codigo;
        var ts = AnalizadorLexico.TS;
        String nombreMetodo = nodo.getIzq().getNombre();
        Nodo subArbol = nodo.getDer();
        codigo = "MOV EAX, __" + nombreMetodo + "__\nCMP EAX, __funcion_actual__\nJE error_recursion\nMOV __funcion_actual__, EAX\n";

        codigo = codigo + "call _" + nombreMetodo + "\n";
        return codigo;
    }
}
