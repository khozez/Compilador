package Assembler;

import Etapas.Nodo;

public class EstructuraFuncion implements GeneradorEstructura{
    String generar(Nodo nodo) {
        String codigo;
        String nombreMetodo = nodo.getIzq().getNombre();
        String parametroMetodo = nodo.getDer();
        if (metodosLlamados.contains(nombreMetodo)) {
            // Error: llamada recursiva
            codigo = "\nJMP error_recursion"; // todo: Preguntarle a Kevin si este chequeo se hace en etapas anteriores
        }else {
            // Si no existe llamada recursiva, se llama a la funcion y se agrega el nombre del metodo al conjunto
            metodosLlamados.add(nombreMetodo);
            codigo = "call " + nombreMetodo + "\n" ;
        }
        // metodosLlamados.remove(nombreMetodo);
        return codigo;
    }
}
