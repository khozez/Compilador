package Assembler;

import Etapas.*;

public class EstructuraComparador extends Generador implements GeneradorEstructura {

    String tipo;

    public EstructuraComparador(String tipo) {
        this.tipo = tipo;
    }

    public String generar(Nodo nodo) {
        String codigo;
        var ts = AnalizadorLexico.TS;
        Nodo subArbol1 = nodo.getIzq();
        Nodo subArbol2 = nodo.getDer();

        if (nodo.getTipo().equals("SHORT"))
            codigo = "MOV AL, " + obtenerNombreVariable(ts, subArbol1) + "\nMOV AH, " + obtenerNombreVariable(ts,subArbol2) + "\nCMP AL, AH\n" + tipo + " etiqueta" + etiqueta+"\n";

        else if (nodo.getTipo().equals("LONG"))
            codigo = "MOV EAX, " + subArbol1 + "\nMOV EBX, " + subArbol2 + "\nCMP EAX, EBX\n" + tipo + " etiqueta" + etiqueta+"\n";
        else
            codigo = "FLD " + subArbol1 + "\nFCOM " + subArbol2 + "\nFSTSW aux_mem_2bytes\"\n" + "MOV AX, aux_mem_2bytes\n SAHF\n" + tipo + " etiqueta" + etiqueta+"\n";
        pilaEtiquetas.add(etiqueta);
        etiqueta++;
        return codigo;
    }

    public static String obtenerNombreVariable(TablaSimbolos ts, Nodo subArbol) {
        int id = ts.obtenerSimbolo(subArbol.getNombre());
        String uso = ts.obtenerAtributo(id, "uso");

        if (uso.equals("auxiliar")) {
            return "@" + subArbol.getNombre();
        } else if (uso.equals("variable") || uso.equals("constante")) {
            return subArbol.getNombre().replaceAll(":", "_")+"_";
        }

        return "";
    }
}
