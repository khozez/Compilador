package Etapas;

import Assembler.*;

import java.util.HashMap;
import java.util.Stack;

public abstract class Generador {
    private static final HashMap<String, GeneradorEstructura> mapa = new HashMap<>(); //mapa de plantillas
    public static final Stack<Integer> pilaEtiquetas = new Stack<>(); //pila de enteros para etiquetas
    public static int etiqueta = 1; //numero de etiqueta disponible
    public static int aux = 1; //Para asignar variables auxiliares

    public static final OutputManager outAssembler = new OutputManager("./Assembler.asm");
    public static final OutputManager outFunciones = new OutputManager("./Assembler.asm");

    public static final Stack<StringBuilder> pilaFuncion = new Stack<>(); //Guarda el ambito de la funciones


    public static String GenerarCodigoArbol(Nodo padre){
        if (padre != null && !padre.esHoja()){
            if( ((padre.getIzq() == null) || (padre.getIzq().esHoja()))
                && ((padre.getDer() == null) || (padre.getDer().esHoja()))) { // Sus hijos son hojas??
                return GenerarCodigo(padre);
            }
            else{

            if(padre.getNombre().equals("Funcion") || padre.getNombre().equals("MetodoClase"))
                pilaFuncion.push(new StringBuilder(""));
                else if(padre.getNombre().equals("while")){
                    if (isFunctionEmpty()) {
                        escribirWhile(outAssembler);
                    }
                    else {
                        String codigo = "etiqueta" + etiqueta + ":";
                        pilaEtiquetas.add(etiqueta);
                        etiqueta++;
                        WriteFunc(codigo);
                    }
                }

            if (padre.getIzq() != null)
                GenerarCodigoArbol(padre.getIzq());
            if ( padre.getDer() != null)
                GenerarCodigoArbol(padre.getDer());

            return GenerarCodigo(padre);
            }
        }
        return "";
    }

    private static void escribirWhile(OutputManager A) {
        String codigo = "etiqueta" + etiqueta + ":";
        pilaEtiquetas.add(etiqueta);
        etiqueta++;
        A.escribirBuffer(codigo);
    }

    public static String GenerarCodigo(Nodo padre){
        if (mapa.isEmpty())
            generarMapa();
        if (mapa.get(padre.getNombre()) != null){
            if(!pilaFuncion.isEmpty()){
                WriteFunc(mapa.get(padre.getNombre()).generar(padre));
            }
            else
            {
                outAssembler.escribirBuffer(mapa.get(padre.getNombre()).generar(padre));
            }
            return "";
        }
        else return "no Definido";
    }

    protected static void setNodoVarAuxiliar(Nodo nodo, int aux){
        nodo.setNombre("aux" + (aux));
    }

    private static void generarMapa() {
        var ts = AnalizadorLexico.TS;
        GeneradorEstructura noAction = nodo -> "";
        mapa.put("+", new EstructuraOperador("ADD"));
        mapa.put("-", new EstructuraOperador("SUB"));
        mapa.put("*", new EstructuraOperador("MUL"));
        mapa.put("/", new EstructuraOperador("DIV"));
        mapa.put(">", new EstructuraComparador("JBE"));
        mapa.put("<", new EstructuraComparador("JAE"));
        mapa.put("==", new EstructuraComparador("JNE"));
        mapa.put(">=", new EstructuraComparador("JB"));
        mapa.put("<=", new EstructuraComparador("JA"));
        mapa.put("!!", new EstructuraComparador("JE"));
        mapa.put("Asignacion", new EstructuraAsignacion());
        mapa.put("while", new EstructuraWhile()); 
        mapa.put("sentencias", noAction);
        mapa.put("cuerpoIf", noAction);
        mapa.put("if", noAction);
        mapa.put("Print", nodo -> {
            String variable_string = nodo.getIzq().getNombre();
            String aux = "invoke MessageBox, NULL, addr "+ variable_string +", addr "+ variable_string +", MB_OK\n";
            return  aux + noAction.generar(nodo);
        });
        mapa.put("then", new EstructuraIf());
        mapa.put("else", new EstructuraElse());
        mapa.put("program", noAction);
        mapa.put("STOF", nodo -> {
            String var = EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq());
            var codigo = "MOV AL, "+var+"\nCBW" +"\nMOV __aux_conv, AX\nFILD __aux_conv"+"\nFSTP @aux"+ aux+"\n";
            nodo.setNombre("aux"+aux);
            nodo.setTipo("FLOAT");
            EstructuraOperador.agregarAuxiliar("FLOAT", nodo.getIzq());
            aux ++;
            return codigo;
        });
        mapa.put("LTOF", nodo -> {
            String codigo;
            if (EstructuraAsignacion.obtenerUsoVariable(ts, nodo.getIzq()).equals("constante")) {
                codigo = "MOV __aux_conv, " + EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq()) + "\nFILD __aux_conv" + "\nFSTP @aux" + aux + "\n";
            }
            else {
                codigo = "FILD " + EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq()) + "\nFSTP @aux" + aux + "\n";
            }
            nodo.setNombre("aux"+aux);
            nodo.setTipo("LONG");
            EstructuraOperador.agregarAuxiliar("FLOAT", nodo.getIzq());
            aux ++;
            return codigo;
        });
        mapa.put("LlamadaFuncion", new EstructuraFuncion());
        mapa.put("LlamadoMetodo", new EstructuraFuncion());
        mapa.put("Funcion", nodo -> {PopFuncion(nodo.getIzq().getIzq().getNombre());
            return noAction.generar(nodo); });
        mapa.put("MetodoClase", nodo -> {PopFuncion(nodo.getIzq().getIzq().getNombre());
            return noAction.generar(nodo); });
        mapa.put("parametro", noAction);
    }

    public static boolean isFunctionEmpty(){
        return pilaFuncion.isEmpty();
    }

    public static void addFuncion() {
        pilaFuncion.push(new StringBuilder(""));
    }

    public static void PopFuncion(String s) {
        outFunciones.escribirBuffer("__"+ s.replaceAll(":", "_")+":");
        String directiva = String.valueOf(pilaFuncion.pop().append("MOV __funcion_actual__, 0\nRET\n"));
        outFunciones.escribirBuffer(directiva);
    }

    public static void WriteFunc(String S){
        if (!pilaFuncion.isEmpty())
            pilaFuncion.peek().append(S);
    }
}