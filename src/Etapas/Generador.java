package Etapas;

import Assembler.*;

import java.util.HashMap;
import java.util.Stack;

public class Generador {
    private static final HashMap<String, GeneradorEstructura> mapa = new HashMap<>(); //mapa de plantillas
    public static final Stack<Integer> pilaEtiquetas = new Stack<>(); //pila de enteros para etiquetas
    public static int etiqueta = 1; //numero de etiqueta disponible
    public static int aux = 1; //Para asignar variables auxiliares

    static final StringBuilder codigoMain = new StringBuilder("");
    static final StringBuilder funciones = new StringBuilder(""); // Buffer pque guarda codigo de funciones antes de escribirlas en el archivo

    public static final OutputManager outAssembler = new OutputManager("./Assembler.asm");
    public static final OutputManager outFunciones = new OutputManager("./Assembler.asm");

    public static final Stack<StringBuilder> pilaFuncion = new Stack<>(); //Guarda el ambito de la funciones
    public static final Stack<StringBuilder> pilaClases = new Stack<>();



    public String GenerarCodigoArbol(Nodo padre){
        if (padre != null && !padre.esHoja()){
            if( ((padre.getIzq() == null) || (padre.getIzq().esHoja()))
                && ((padre.getDer() == null) || (padre.getDer().esHoja()))) { // Sus hijos son hojas??
                return GenerarCodigo(padre);
            }
            else{
            if (padre.getIzq() != null && !padre.getIzq().esHoja())
                GenerarCodigoArbol(padre.getIzq());
            if ( padre.getDer() != null && !padre.getDer().esHoja())
                GenerarCodigoArbol(padre.getDer());
            if(padre.getNombre().equals("funcion"))
                pilaFuncion.push(new StringBuilder(""));
            
            return GenerarCodigo(padre);
            }
        }
        return "";
    }

    public Generador() {
            var ts = AnalizadorLexico.TS;
            GeneradorEstructura noAction = nodo -> "";
            mapa.put("+", new EstructuraOperador("ADD"));
            mapa.put("-", new EstructuraOperador("SUB"));
            mapa.put("*", new EstructuraOperador("MUL"));
            mapa.put("/", new EstructuraOperador("DIV"));
            mapa.put(">", new EstructuraComparador("JBE"));
            mapa.put("<", new EstructuraComparador("JAE"));
            mapa.put("=", new EstructuraComparador("JNE"));
            mapa.put(">=", new EstructuraComparador("JB"));
            mapa.put("<=", new EstructuraComparador("JA"));
            mapa.put("=!", new EstructuraComparador("JE"));
            mapa.put("Asignacion", new EstructuraAsignacion());
            mapa.put("while", nodo -> {var e = new EstructuraWhile();
                return "etiqueta" + etiqueta + ":\n" + GenerarCodigo(nodo.getIzq()) + e.generar(nodo);});  //todo: revisar funcionamiento, creo esta mal
            mapa.put("sentencias", noAction);
            mapa.put("cuerpoIf", noAction);
            mapa.put("if", noAction);
            mapa.put("Print", nodo -> {
                String aux ;
                String variable_string = acomodarString(nodo.getIzq().getNombre().substring(1).replace("_", "__").replace(" ", "_s"));
                if (nodo.getTipo().equals("STRING"))
                    aux = "invoke StdOut, addr str_"+ variable_string +"\n";
                else if (nodo.getTipo().equals("SHORT") || nodo.getTipo().equals("LONG"))
                    aux = String.format("print str$(_%s), 13, 10", nodo.getIzq().getNombre().replace(":","_"))+"\n";
                else
                    aux = "invoke printf, cfm$(\"%.20Lf\\n\"), _" + nodo.getIzq().getNombre().replace(":", "_") + "\n";
                return  aux + noAction.generar(nodo);
            });
            mapa.put("then", new EstructuraIf());
            mapa.put("else", new EstructuraElse());
            mapa.put("program", noAction);
            mapa.put("STOF", nodo -> {
                var codigo = "FILD "+ EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq()) + "\nFSTP @aux"+ aux;
                EstructuraOperador.agregarAuxiliar("FLOAT", nodo.getIzq());
                aux ++;
                return codigo;
            });
            mapa.put("LTOF", nodo -> {
                var codigo = "FILD "+ EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq()) + "\nFSTP @aux"+ aux;
                EstructuraOperador.agregarAuxiliar("FLOAT", nodo.getIzq());
                aux ++;
                return codigo;
            });
            mapa.put("LlamadaFuncion", new EstructuraFuncion());
            mapa.put("Funcion", nodo -> {PopFuncion(nodo.getIzq().getIzq().getNombre());
                return noAction.generar(nodo); });
            mapa.put("parametro", noAction);
            mapa.put("Return", nodo -> "MOV __funcion_actual__, 0\n" +
                (nodo.getTipo().equals("SHORT")
                        ? "MOV AL, " + EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq())
                        : (nodo.getTipo().equals("LONG")
                        ? "MOV EAX, " + EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq())
                        : "FLD " + EstructuraAsignacion.obtenerNombreVariable(ts, nodo.getIzq())) + "\nRET\n" + noAction.generar(nodo)));

}


    public String GenerarCodigo(Nodo padre){
        if (mapa.isEmpty())
            generarMapa();
        if (mapa.get(padre.getNombre()) != null){
            if(!pilaFuncion.isEmpty()){
                WriteFunc(mapa.get(padre.getNombre()).generar(padre));
            }
            else
            {
                codigoMain.append(mapa.get(padre.getNombre()).generar(padre)).append("\n");
            }
            return null;
        }
        else return "no Definido";
    }

    protected static void setNodoVarAuxiliar(Nodo nodo, int aux){
        nodo.setNombre("aux" + (aux));
    }

    private void generarMapa() {
    }

    public static boolean isFunctionEmpty(){
        return pilaFuncion.isEmpty();
    }

    public static void addFuncion() {
        pilaFuncion.push(new StringBuilder(""));
    }

    public static void PopFuncion(String s) {
        funciones.append(Generador.pilaFuncion.pop().toString());
        funciones.append("_"+ s.replace(":", "_") + " ENDP\n" ).append("\n");
    }

    public static void WriteFunc(String S){
        pilaFuncion.peek().append(S);
    }

    public static void escribirFunciones() {
        outFunciones.write(funciones.toString());

    }

    public static void escribirCodigo() {
        outAssembler.write(codigoMain.toString());
    }

    public static void addClase(){
        pilaClases.push(new StringBuilder(""));
    }

    public static void popClase(String s){
        funciones.append(Generador.pilaClases.pop().toString());
        funciones.append("_"+ s.replace(":", "_") + " ENDP\n" ).append("\n");
    }

    public static void WriteClase(String S){
        pilaClases.peek().append(S);
    }

    public static String acomodarString(String i){
        StringBuilder string = new StringBuilder();
        char[] c = i.toCharArray();

        for (char c1 : c){
            int caracter = (int) c1;
            Boolean es_letra = ( ( caracter >= 97 && caracter < 123) || ( caracter >= 65 && caracter < 90));
            if (!es_letra)
                string.append((int)c1);
            else
                string.append(c1);
        }
        return string.toString();
    }
}
