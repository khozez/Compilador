package Assembler;

import Etapas.Nodo;

public class EstructuraComparador implements GeneradorEstructura {

    String tipo;

    public EstructuraComparador(String tipo) {
        this.tipo = tipo;
    }

    String generador(Nodo nodo) {
        String codigo;
        Nodo subArbol1 = Nodo.getIzq();
        Nodo subArbol2 = Nodo.getDer();

        comprimirNodo(nodo); // reduce el nodo para no seguir recorriendolo, lo comprime a una hoja

        if (nodo.getTipo().equals("SHORT"))
            codigo = "MOV AL, " + subArbol1 + "\nMOV AH, " + subArbol2 + "\nCMP AL, AH\n" + tipo + " etiqueta" + getEtiqueta();
        else if (nodo.getTipo().equals("LONG"))
            codigo = "MOV AX, " + subArbol1 + "\nMOV BX, " + subArbol2 + "\nCMP AX, BX\n" + tipo + " etiqueta" + getEtiqueta();
        else
            codigo = "FLD " + subArbol1 + "\nFCOM " + subArbol2 + "\nFSTSW aux_mem_2bytes\"\n" + "MOV AX, aux_mem_2bytes\n SAHF\n" + tipo + " etiqueta" + getEtiqueta();

        return codigo;
    }
}
