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
        String subArbol1 = nodo.getIzq().getNombre();
        String subArbol2 = nodo.getDer().getNombre();
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
                subArbol1 = subArbol1.replaceAll("_s", "");
                subArbol2 = subArbol2.replaceAll("_s", "");
                switch (operando) {
                    case "DIV":
                        codigo = String.format("MOV AH, 0\nMOV AX, %s\nMOV BL, %s\nCMP BL, 0\nJE division_por_cero\nDIV BL\nMOV @aux%d, AL\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "ADD":
                        codigo = String.format("MOV AL, %s\nMOV BL, %s\nADD AL, BL\nMOV @aux%d, AL\nJO error_overflow", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "SUB":
                        codigo = String.format("MOV AL, %s\nMOV BL, %s\nSUB AL, BL\nMOV @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "MUL":
                        codigo = String.format("MOV AL, %s\nMOV BL, %s\nMUL AL, BL\nMOV @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                }
                break;
            case "ULONG":
                subArbol1 = subArbol1.replaceAll("_ul", "");
                subArbol2 = subArbol2.replaceAll("_ul", "");
                switch (operando) {
                    case "DIV":
                        codigo = String.format("MOV EDX, 0\nMOV EAX, %s\nMOV EBX, %s\nCMP EBX, 0\nJE division_por_cero\nDIV BX\nMOV @aux%d, EAX\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "ADD":
                        codigo = String.format("MOV EAX, %s\nMOV EBX, %s\nADD EAX, EBX\nMOV @aux%d, EAX\nJO error_overflow", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "SUB":
                        codigo = String.format("MOV EAX, %s\nMOV EBX, %s\nSUB EAX, EBX\nMOV @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                    case "MUL":
                        codigo = String.format("MOV EAX, %s\nMOV EBX, %s\nMUL EAX, EBX\nMOV @aux%d\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                }
                break;
            case "FLOAT":
                switch (operando) {
                    case "DIV":
                        codigo = String.format("FLD %s\nFLD %s\nFLDZ\nFCOM\nFSTSW aux_mem_2bytes\nSAHF\nJE division_por_cero\nFDIV\nFSTP @aux%d\n", subArbol1, subArbol2, aux);
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
                        codigo = String.format("FLD %s\nFLD %s\nFMUL\nFSTP @aux%d\nFSTSW aux_mem_2bytes\nSAHF\nJO error_overflow\n", subArbol1, subArbol2, aux);
                        agregarAuxiliar(tipo, nodo);
                        break;
                }
                break;
        }

        return codigo;
    }

}

