package Assembler;

import Etapas.*;

public class EstructuraOperador extends Generador implements GeneradorEstructura {

    private String operando;

    public EstructuraOperador(String operando) {
        this.operando = operando;
    }

    public String generar(Nodo nodo) {
        var ts = AnalizadorLexico.TS;
        String codigo = "";
        String subArbol1 = obtenerNombreVariable(ts, nodo.getIzq());
        String subArbol2 = obtenerNombreVariable(ts, nodo.getDer());
        String tipo = nodo.getTipo();

        switch (operando) {
            case "DIV":
            case "ADD":
            case "SUB":
            case "MUL":
                codigo = generarOperacion(tipo, operando, subArbol1, subArbol2, nodo);
                break;
        }

        return codigo;
    }
    public static void agregarAuxiliar(String tipo, Nodo nodo) {
        var ts = AnalizadorLexico.TS;
        ts.agregarSimbolo("aux" + aux);
        ts.agregarAtributo(ts.obtenerID(), "tipo", tipo);
        ts.agregarAtributo(ts.obtenerID(), "uso", "auxiliar");
        setNodoVarAuxiliar(nodo, aux);
        aux++;
    }

    private String generarOperacion(String tipo, String operando, String subArbol1, String subArbol2, Nodo nodo) {
        String codigo = "";

        switch (tipo) {
            case "SHORT":
                switch (operando) {
                    case "DIV":
                        codigo = String.format("MOV AH, 0\nMOV AL, %s\nMOV BL, %s\nCMP BL, 0\nJE ErrorDiv0\nDIV BL\nMOV @aux%d, AL\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "ADD":
                        codigo = String.format("MOV AL, %s\nMOV BL, %s\nADD AL, BL\nMOV @aux%d, AL\nJO ErrorOverflowSum\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "SUB":
                        codigo = String.format("MOV AL, %s\nMOV BL, %s\nSUB AL, BL\nMOV @aux%d, AL\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "MUL":
                        codigo = String.format("MOV AL, %s\nMOV BL, %s\nMUL AL, BL\nMOV @aux%d, AL\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                }
                break;
            case "ULONG":
                switch (operando) {
                    case "DIV":
                        codigo = String.format("MOV EDX, 0\nMOV EAX, %s\nMOV EBX, %s\nCMP EBX, 0\nJE ErrorDiv0\nDIV BX\nMOV @aux%d, EAX\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "ADD":
                        codigo = String.format("MOV EAX, %s\nMOV EBX, %s\nADD EAX, EBX\nMOV @aux%d, EAX\nJO ErrorOverflowSum\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "SUB":
                        codigo = String.format("MOV EAX, %s\nMOV EBX, %s\nSUB EAX, EBX\nMOV @aux%d, EAX\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "MUL":
                        codigo = String.format("MOV EAX, %s\nMOV EBX, %s\nMUL EAX, EBX\nMOV @aux%d, EAX\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                }
                break;
            case "FLOAT":
                switch (operando) {
                    case "DIV":
                        codigo = String.format("FLD %s\nFLD %s\nFLDZ\nFCOM\nFSTSW aux_mem_2bytes\nSAHF\nJE ErrorDiv0\nFDIV\nFSTP @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "ADD":
                        codigo = String.format("FLD %s\nFLD %s\nFADD\nFSTP @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "SUB":
                        codigo = String.format("FLD %s\nFLD %s\nFSUB\nFSTP @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "MUL":
                        codigo = String.format("FLD %s\nFLD %s\nFMUL\nFSTP @aux%d\nFSTSW aux_mem_2bytes\nSAHF\nJC ErrorOverflowProd\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                }
                break;
        }

        return codigo;
    }

    public static String obtenerNombreVariable(TablaSimbolos ts, Nodo subArbol) {
        int id = ts.obtenerSimbolo(subArbol.getNombre());
        String uso = ts.obtenerAtributo(id, "uso");

        if (uso.equals("auxiliar")) {
            return "@"+subArbol.getNombre();
        } else if (uso.equals("variable") || uso.equals("parametro")) {
            return "_"+subArbol.getNombre().replaceAll(":", "_")+"_";
        } else if (uso.equals("constante")) {
            String tipo = ts.obtenerAtributo(id, "tipo");
            if (tipo.equals("SHORT"))
                return subArbol.getNombre().replaceAll("_s","");
            if (tipo.equals("ULONG"))
                return subArbol.getNombre().replaceAll("_ul","");
            else {
                String f = subArbol.getNombre().replaceAll("E", "e");
                f = f.replace("+", "");
                return "__"+f;
            }
        }
        return "";
    }

}

