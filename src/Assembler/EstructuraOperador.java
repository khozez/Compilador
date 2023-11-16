package Assembler;

import Etapas.Nodo;
import Etapas.TablaSimbolos;

public class EstructuraOperador implements GeneradorEstructura{

    String operando;

    public EstructuraOperador(String operando) {
        this.operando = operando;
    }

    String generar(Nodo nodo){
        String codigo;
        String subArbol1 = nodo.getIzq().getNombre();
        String subArbol2 = nodo.getDer().getNombre();
        String tipo = nodo.getTipo();

        if (tipo.equals("SHORT")){ // operaciones para tipo de dato SHORT (8 bits)

            if (operando.equals("DIV")){ // Division
                codigo = "MOV AH, 0\nMOV AX, " + subArbol1 + "\nMOV BL, " + subArbol2 + "\nCMP BL, 0\nJE division_por_cero\nDIV BL\nMOV @aux" + aux + ", AL\n";
                TablaSimbolos.agregarSimbolo("aux" + aux);
                TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), tipo, "", "auxiliar");
                setNodoAux(nodo, aux);
                aux++;

            } else if (operando.equals("ADD")) {
                codigo = "MOV";
                
            }

        } else if (tipo.equals("ULONG")){
            if (operando.equals("DIV")){
                codigo = "MOV DX, 0\nMOV AX, " + subArbol1 + "\nMOV BX, " + subArbol2 + "\nCMP BX, 0\nJE division_por_cero\nDIV BX\nMOV @aux" + aux + ", AX\n";
                TablaSimbolos.agregarSimbolo("aux" + aux);
                TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), tipo, "", "auxiliar");
                setNodoAux(nodo, aux);
                aux++;
            }
        } else if (tipo.equals("FLOAT")){
            if (operando.equals("DIV")) {
                codigo = "FLD " + subArbol1 + "\nFLD " + subArbol2 + "\nFLDZ\nFCOM\nFSTSW aux_mem_2bytes\nSAHF\nJE division_por_cero\nFDIV\nFSTP @aux" + aux + "\n";
                TablaSimbolos.agregarSimbolo("aux" + aux);
                TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), tipo, "", "auxiliar");
                setNodoAux(nodo, aux);
                aux++;
            }
        }

    }
}
