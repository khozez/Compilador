package Assembler;

import Etapas.Generador;
import Etapas.GeneradorEstructura;
import Etapas.Nodo;


public class EstructuraIf extends Generador implements GeneradorEstructura {
    public String generar(Nodo nodo){
        String codigo = "JMP etiqueta" + etiqueta + "\n" + "etiqueta" + pilaEtiquetas.pop() + ": \n";
        pilaEtiquetas.add(etiqueta);
        etiqueta++;
        return codigo;
    }
}
