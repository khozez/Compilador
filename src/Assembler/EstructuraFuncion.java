package Assembler;

import Etapas.*;

public class EstructuraFuncion extends Generador implements GeneradorEstructura {
    public String generar(Nodo nodo) {
        String codigo;
        var ts = AnalizadorLexico.TS;
        String nombreMetodo = nodo.getIzq().getNombre().replaceAll(":", "_");
        Nodo subArbol = nodo.getDer();
        codigo = "MOV EAX, __" + nombreMetodo + "__\nCMP EAX, __funcion_actual__\nJE error_recursion\nMOV __funcion_actual__, EAX\n";
        String parametro = obtenerNombreVariable(ts, subArbol);

        if (!parametro.isEmpty()) {
            if (subArbol.getTipo().equals("SHORT")) {
                codigo = codigo + "MOV AL, " + parametro + "\n";
            } else if (subArbol.getTipo().equals("LONG")) {
                codigo = codigo + "MOV EAX, " + parametro + "\n";
            } else {
                codigo = codigo + "FLD, " + parametro + "\n";

            }
        }

        codigo = codigo + "call __" + nombreMetodo + "\n";
        return codigo;
    }

    public static String obtenerNombreVariable(TablaSimbolos ts, Nodo subArbol) {
        if (subArbol != null) {
            int id = ts.obtenerSimbolo(subArbol.getNombre());
            String uso = ts.obtenerAtributo(id, "uso");

            if (uso.equals("auxiliar")) {
                return "@" + subArbol.getNombre();
            } else if (uso.equals("variable") || uso.equals("constante")) {
                return subArbol.getNombre().replace(":", "_");
            }

            return "";
        }
        return "";
    }
}
