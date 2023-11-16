package Etapas;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class Generador {
    private static final HashMap<String, GeneradorCodigo> mapa = new HashMap<>(); //mapa de plantillas
    public static final Stack<Integer> PilaIF = new Stack<>(); //pila de enteros para etiquetas
    public static int label = 1; //numero de etiqueta disponible
    public static int aux = 1; //Para asignar variables auxiliares

    static final StringBuilder CodigoMain = new StringBuilder("");
    static final StringBuilder Funciones = new StringBuilder(""); // Buffer pque guarda codigo de funciones antes de escribirlas en el archivo

    public static final OutputManager out_assembler = new OutputManager("./Assembler.asm");
    public static final OutputManager out_funciones = new OutputManager("./Assembler.asm");

    public static final Stack<StringBuilder> PilaFuncion = new Stack<>(); //Guarda el ambito de la funciones
    public static final Stack<StringBuilder> PilaClases = new Stack<>();


    public String GenerarCodigoArbol(Nodo padre){
        if (padre != null && !padre.esHoja()){
            if( ((padre.getIzq() == null) || (padre.getIzq().esHoja()))
                && ((padre.getDer() == null) || (padre.getDer().esHoja()))) { // Sus hijos son hojas??
                GenerarCodigo(padre);
            }
            else{
            if (padre.getIzq() != null && !padre.getIzq().esHoja())
                GenerarCodigoArbol(padre.getIzq());
            if ( padre.getDer() != null && !padre.getDer().esHoja())
                GenerarCodigoArbol(padre.getDer());
            if(padre.getNombre().equals("funcion"))
                PilaFuncion.push(new StringBuilder(""));
            
            GenerarCodigo(padre);
            }
        }
        
    }

    public Generador() {
            GeneradorCodigo noAction = n -> { limpiarNodo(n); return ""; };
            Generador.deffered.push(new StringBuilder(""));
            mapa.put("+", new Operacion("ADD"));
            mapa.put("-", new Operacion("SUB"));
            mapa.put("*", new Operacion("IMUL"));
            mapa.put("/", new Operacion("DIV"));
            mapa.put(">", new Comparador("JBE"));
            mapa.put("<", new Comparador("JAE"));
            mapa.put("=", new Comparador("JNE"));
            mapa.put(">=", new Comparador("JB"));
            mapa.put("<=", new Comparador("JA"));
            mapa.put("=!", new Comparador("JE"));
            mapa.put("Asignacion", new Assign());
            mapa.put("Asignacion_FOR",
                n -> {
                    var texto = "\nlabel" + label + ":";
                    pila.addFirst(label);
                    label++;
                    pilafor.push(label);
                    iteradorFor.addFirst("_"+ NodoToEntrada.toEntrada(n.getIzq()).getLexema());
                    return new Assign().gen(n) + texto;
                } );
            mapa.put("Iteracion", new SentenciaIteracion());
            mapa.put("for", new SentenciaFor());
            mapa.put("condicion_for", noAction);
            mapa.put("parentesis_for", noAction);
            mapa.put("sentencias", noAction);
            mapa.put("cuerpoIf", noAction);
            mapa.put("When", noAction);
            mapa.put("if", noAction);
            mapa.put("Print", n -> {
                String aux ;
                String variable_string = acomodarString(n.getIzq().getNombre().substring(1).replace("_", "__").replace(" ", "_s"));
                if (n.getTipo().equals(TablaSimbolos.USOSTRING))
                    aux = "invoke StdOut, addr str_"+ variable_string +"\n";
                else if (n.getTipo().equals(TablaSimbolos.UI32))
                    aux = String.format("print str$(_%s), 13, 10", n.getIzq().getNombre().replace(":","_"))+"\n";
                else
                    aux = "invoke printf, cfm$(\"%.20Lf\\n\"), _" + n.getIzq().getNombre().replace(":", "_") + "\n";
                return  aux + noAction.gen(n);
            });
            mapa.put("Tagfor", n -> n.getIzq() != null ? "_" + NodoToEntrada.toEntrada(n.getIzq()).getLexema() + noAction.gen(n) + ":\n" : noAction.gen(n));
            mapa.put("Break", n -> n.getIzq() != null ? "JMP _" + n.getIzq().getNombre() + noAction.gen(n) + "\n" : noAction.gen(n) + "JMP label" +pilafor.peek() );
            mapa.put("then", new SentenciaThen());
            mapa.put("else", new SentenciaElse());
            mapa.put("program", n -> getDeffered() + noAction.gen(n));
            mapa.put("TOF64", n -> {
                var texto = "FILD "+ acomodarParametro(NodoToEntrada.toEntrada(n.getIzq())) + "\nFSTP @aux"+ aux;
                TablaSimbolos.getInstance().addEntrada(new EntradaTablaSimbolos(261, "aux" + aux,
                               TablaSimbolos.F64, TablaSimbolos.AUXILIAR));
                setNodoVarAuxiliar(n,aux);
                aux ++;
                return texto;
            });
            mapa.put("CallToFunction", new LLamadosAFunciones());
            mapa.put("parametrosReales", n -> {n.setHoja(true); return "";});
            mapa.put("Funcion", n -> { deffered.pop();  PopFuncion(n.getIzq().getIzq().getNombre());
                return noAction.gen(n); });
            mapa.put("Parametros", n -> {n.setHoja(true); return ""; });
            mapa.put("Return",  n -> ReadDefered() + "MOV __funcion_actual__, 0\n" +(n.getTipo().equals(TablaSimbolos.UI32) ?
                    "MOV EAX, " + acomodarParametro(NodoToEntrada.toEntrada(n.getIzq()))
                    : "FLD " + acomodarParametro(NodoToEntrada.toEntrada(n.getIzq()))) + "\nRET\n" + noAction.gen(n));
}


    public String GenerarCodigo(Nodo padre){
        if (mapa.isEmpty())
            generarMapa();
        return mapa.get(padre.getNombre()) != null ?
                mapa.get(padre.getNombre()).generar(padre) : "no Definido";
    }

    protected static void setNodoVarAuxiliar(Nodo nodo, int aux){
        nodo.setNombre("aux" + (aux));
    }

    private void generarMapa() {
    }

    public static boolean isFunctionEmpty(){
        return PilaFuncion.isEmpty();
    }

    public static void addFuncion() {
        PilaFuncion.push(new StringBuilder(""));
    }

    public static void PopFuncion(String s) {
        Funciones.append(Generador.PilaFuncion.pop().toString());
        Funciones.append("_"+ s.replace(":", "_") + " ENDP\n" ).append("\n");
    }

    public static void WriteFunc(String S){
        PilaFuncion.peek().append(S);
    }

    public static void escribirFunciones() {
        out_funciones.write(Funciones.toString());

    }

    public static void escribirCodigo() {
        out_assembler.write(CodigoMain.toString());
    }

    public static void addClase(){
        PilaClases.push(new StringBuilder(""));
    }

    public static void popClase(String s){
        Funciones.append(Generador.PilaClases.pop().toString());
        Funciones.append("_"+ s.replace(":", "_") + " ENDP\n" ).append("\n");
    }

    public static void WriteClase(String S){
        PilaClases.peek().append(S);
    }
}
