package Assembler;

import Etapas.Generador;
import Etapas.GeneradorEstructura;
import Etapas.Nodo;

public class EstructuraElse extends Generador implements GeneradorEstructura {
    public String generar(Nodo nodo){
        return "etiqueta" + pilaEtiquetas.pop() + ": \n";
    }
}
