package Assembler;

import Etapas.Nodo;
import Etapas.TablaSimbolos;

public class EstructuraAsignacion implements GeneradorEstructura {
    String generar(Nodo nodo) {
        String codigo;
        Nodo subArbol1 = Nodo.getIzq();
        Nodo subArbol2 = Nodo.getDer();

        comprimirNodo(nodo);
        if (nodo.getTipo().equals("SHORT"))
            codigo = "MOV AL, " + subArbol2 + "\nMOV " + subArbol1 + ", AL\n";
        else if (nodo.getTipo().equals("LONG"))
            codigo = "MOV AX, " + subArbol2 + "\nMOV " + subArbol1 + ", AX\n";
        else
            codigo = "FLD " + subArbol2 + "\nFSTP " + subArbol1 +"\n";

        return codigo;
    }
}
