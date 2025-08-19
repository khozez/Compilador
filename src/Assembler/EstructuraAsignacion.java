package Assembler;

import Etapas.AnalizadorLexico;
import Etapas.GeneradorEstructura;
import Etapas.Nodo;
import Etapas.TablaSimbolos;

public class EstructuraAsignacion implements GeneradorEstructura {

    public String generar(Nodo nodo) {
        String codigo;
        var ts = AnalizadorLexico.TS;
        
        // "x += 5"
        Nodo variable = nodo.getIzq();
        Nodo expresion = nodo.getDer();
        
        if (expresion.getNombre().equals("+")) {
            Nodo sumaIzq = expresion.getIzq();
            Nodo sumaDer = expresion.getDer();

            // Si coincide variable = variable + expresion
            if (sumaIzq.getNombre().equals(variable.getNombre())) {
                String var = obtenerNombreVariable(ts, variable);
                String op2 = obtenerNombreVariable(ts, sumaDer);

                if (nodo.getTipo().equals("SHORT")) {
                    return "MOV AL, " + var + "\nADD AL, " + op2 + "\nMOV " + var + ", AL\n";
                } else if (nodo.getTipo().equals("ULONG")) {
                    return "MOV EAX, " + var + "\nADD EAX, " + op2 + "\nMOV " + var + ", EAX\n";
                } else {
                    return "FLD " + var + "\nFADD " + op2 + "\nFSTP " + var + "\n";
                }
            }
        }
        
        String variable1 = obtenerNombreVariable(ts, nodo.getIzq());
        String variable2 = obtenerNombreVariable(ts, nodo.getDer());

        if (nodo.getTipo().equals("SHORT"))
            codigo = "MOV AL, " + variable2 + "\nMOV " + variable1 + ", AL\n";
        else if (nodo.getTipo().equals("ULONG"))
            codigo = "MOV EAX, " + variable2 + "\nMOV " + variable1 + ", EAX\n";
        else
            codigo = "FLD " + variable2.replaceAll(":","_") + "\nFSTP " + variable1.replaceAll(":","_")+"\n";

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
            else {
                String f = subArbol.getNombre().replace(".", "");
                f = f.replace("+", "");
                if (f.charAt(0) == '-') {
                    f = f.replaceAll("-", "");
                    return "__n" + f;
                }else {
                    f = f.replaceAll("-", "");
                    return "__" + f;
                }
            }
        }
        return "";
    }

    public static String obtenerUsoVariable(TablaSimbolos ts, Nodo subArbol)
    {
        int id = ts.obtenerSimbolo(subArbol.getNombre());
        String uso = ts.obtenerAtributo(id, "uso");
        return uso;
    }
}
