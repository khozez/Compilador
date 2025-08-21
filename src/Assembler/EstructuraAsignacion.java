package Assembler;

import Etapas.AnalizadorLexico;

import Etapas.GeneradorEstructura;
import Etapas.Nodo;
import Etapas.TablaSimbolos;

public class EstructuraAsignacion implements GeneradorEstructura {

    public String generar(Nodo nodo) {
        String codigo = "";
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
                    codigo = "MOV AL, " + var + "\nADD AL, " + op2 + "\nMOV " + var + ", AL\n";
                } else if (nodo.getTipo().equals("ULONG")) {
                    codigo = "MOV EAX, " + var + "\nADD EAX, " + op2 + "\nMOV " + var + ", EAX\n";
                } else {
                    codigo = "FLD " + var + "\nFADD " + op2 + "\nFSTP " + var + "\n";
                }
                // No puede ser constante si es +=
                int idVar = ts.obtenerSimbolo(variable.getNombre());
                ts.modificarAtributo(idVar, "constanteVigente", "false");
                ts.modificarAtributo(idVar, "valorConstante", "");
                return codigo;
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
        
        // update de atributos de constantes
        int idVariable = ts.obtenerSimbolo(variable.getNombre());
        int idExpresion = ts.obtenerSimbolo(expresion.getNombre());
        if (expresion.esHoja() && ts.obtenerAtributo(idExpresion, "uso").equals("constante")) { // la asignacion es una constante
        	ts.modificarAtributo(idVariable, TablaSimbolos.VIGENTE, "True");
        	ts.modificarAtributo(idVariable, TablaSimbolos.VALOR, expresion.getNombre());
        } else {  // otro tipo de asignaciones, no se puede propagar o seguir propagando
        	ts.modificarAtributo(idVariable, TablaSimbolos.VIGENTE, "False");
        	ts.modificarAtributo(idVariable, TablaSimbolos.VALOR, "");
        }

        return codigo;
    }

    public static String obtenerNombreVariable(TablaSimbolos ts, Nodo subArbol) {
        int id = ts.obtenerSimbolo(subArbol.getNombre());
        String uso = ts.obtenerAtributo(id, "uso");

        if (uso.equals("auxiliar")) {
            return "@" + subArbol.getNombre();
        } else if (uso.equals("variable") || uso.equals("parametro")) {
        	String vigente = ts.obtenerAtributo(id, "constanteVigente");
            String valor = ts.obtenerAtributo(id, "valorConstante");
            if ("true".equals(vigente) && valor != null) {
                return valor; // propago directamente por valor de la constante
            }
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
