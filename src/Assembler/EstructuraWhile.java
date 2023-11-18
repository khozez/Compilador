package Assembler;

import Etapas.Generador;
import Etapas.GeneradorEstructura;
import Etapas.Nodo;

public class EstructuraWhile extends Generador implements GeneradorEstructura {
    public String generar(Nodo nodo){
        int etiqueta1 = pilaEtiquetas.pop();
        return "JMP etiqueta" + pilaEtiquetas.pop() + "\netiqueta" + etiqueta1 + ":";
    }
}
