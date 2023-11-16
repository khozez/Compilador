package Assembler;

import Etapas.Nodo;

public class EstructuraElse implements GeneradorEstructura{
    String generar(Nodo nodo){
        comprimirNodo(nodo);
        return "etiqueta" + etiquetas.pop() + ": \n";
    }
}
