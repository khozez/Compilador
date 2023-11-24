package Assembler;

import Etapas.AnalizadorLexico;
import Etapas.GeneradorEstructura;
import Etapas.Nodo;
import Etapas.TablaSimbolos;

public class EstructuraAsignacion implements GeneradorEstructura {

    public String generar(Nodo nodo) {
        String codigo;
        var ts = AnalizadorLexico.TS;
        String variable1 = obtenerNombreVariable(ts, nodo.getIzq());
        String variable2 = obtenerNombreVariable(ts, nodo.getDer());

        if (nodo.getTipo().equals("SHORT"))
            codigo = "MOV AL, " + variable2 + "\nMOV " + variable1 + ", AL\n";
        else if (nodo.getTipo().equals("LONG"))
            codigo = "MOV EAX, " + variable2 + "\nMOV " + variable1 + ", EAX\n";
        else
            codigo = "FLD " + variable2 + "\nFSTP " + variable1+"\n";

        return codigo;
    }

    public static String obtenerNombreVariable(TablaSimbolos ts, Nodo subArbol) {
        int id = ts.obtenerSimbolo(subArbol.getNombre());
        String uso = ts.obtenerAtributo(id, "uso");

        if (uso.equals("auxiliar")) {
            return "@" + subArbol.getNombre();
        } else if (uso.equals("variable") || uso.equals("parametro")) {
            return "_"+subArbol.getNombre().replaceAll(":", "_")+"_";
        } else if (uso.equals("constante")) {
            String tipo = ts.obtenerAtributo(id, "tipo");
            if (tipo.equals("SHORT"))
                return subArbol.getNombre().replaceAll("_s","");
            if (tipo.equals("ULONG"))
                return subArbol.getNombre().replaceAll("_ul","");
            return subArbol.getNombre();
        }
        return "";
    }
}
