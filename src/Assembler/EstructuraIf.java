package Assembler;

import Etapas.Nodo;

public class EstructuraIf implements GeneradorEstructura{
    String generar(Nodo nodo){
        String codigo = "JMP etiqueta" + getSigEtiqueta() + "\n" + "etiqueta" + etiquetas.pop() + ": \n";
        etiquetas.pop();
        etiquetas.add(getEtiqueta()); //todo: REVISAR
        incremetarEtiqueta();
        comprimirNodo(nodo);
        return codigo;
    }
}
