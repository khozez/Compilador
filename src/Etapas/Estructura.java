package Etapas;
import Etapas.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Etapas.OutputManager;
import Etapas.TablaSimbolos;

public class Estructura {
    public static final String MODEL = "; .386\n; .MODEL flat, stdcall\n; option casemap :none";
    public static final String INCLUDE =
            "include \\masm32\\include\\masm32rt.inc\n" + "includelib \\masm32\\lib\\kernel32.lib\n" +
            "includelib \\masm32\\lib\\masm32.lib\n" + "dll_dllcrt0 PROTO C\n" + "printf PROTO C :VARARG\n" ;
    public static final String STACK = ".STACK 200h";
    public static final String DATA = ".DATA";
    public static final String CODE = ".CODE";
    public static final String START = "START:";
    public static final String ERRORES = "JMP final\nERROROVERFLOWSUM:\ninvoke StdOut, addr ErrorOverflowSum\nJMP final" +
                                         "\nERRORDIV0:\ninvoke StdOut, addr ErrorDiv0\nJMP final" +
                                         "\nERROROVERFLOWPROD:\ninvoke StdOut, addr ErrorOverflowProd\n" +
                                         "final:";
    public static final String END_START = "END START";

    private final OutputManager out_assembler = new OutputManager("./Assembler.asm");

    //private final TsPentium ts;
    public Estructura() {}

    public void generateCode(Nodo padre) {
        // Generar código assembler en órden
        this.out_assembler.write(MODEL);
        this.out_assembler.write(INCLUDE);
        this.out_assembler.write(STACK);
        this.out_assembler.write(DATA);
        this.out_assembler.write("    aux_mem_2bytes DW ?\n" +
                                    "    funcion_actual DD 0\n" +
                                    "   ErrorOverflowSum DB \"Error por Overflow en una suma\", 10, 0\n"+
                                    "   ErrorDiv0 DB \"Error Division por Cero en ejecucion\", 10, 0\n"+
                                    "   ErrorOverflowProd DB \"Error por Overflow en un producto\", 10, 0\n");
        this.out_assembler.write("; constante para cada funcion a partir de su hashcode");
        Parser.lista_funciones.forEach(x-> this.out_assembler.write(x+" DD " + x.hashCode()));
        //.DATA
            writeTs();
        this.out_assembler.write(CODE);
        Generador.escribirFunciones();
        this.out_assembler.write(START);
            // Código intermedio
        Generador.escribirCodigo();
        this.out_assembler.write(ERRORES);
        this.out_assembler.write(END_START);
    }


    public void writeTs(){
        //List<EntradaTablaSimbolos> tsList = this.tablaToList();
        List<Map<String, String>> tsList = new ArrayList<Map<String, String>>(TablaSimbolos.getValue());

        for(Map<String, String> fila : tsList) {

            String var1 = fila.get("lexema");
            if(fila.get("lexema") != null){
                //if() {
                    var1 += "_";
                    if(fila.get("tipo").equals("SHORT")) {

                        String directiva;
                        
                        directiva = "    _";

                        directiva += var1 + " DB " + "?";
                        this.out_assembler.write(directiva);

                    } else {

                        String directiva;
                        directiva = "    _";

                        directiva += var1 + " DD " + "?";
                        this.out_assembler.write(directiva);
                    }

                }
                /*else if(fila.getUso().equals(TablaSimbolos.VARCONSTANTE)) {

                    var par1 = (fila.getParametro1() != null) ? fila.getParametro1().getLexema() : "" ;

                    // 32 bits
                    String directiva;
                    if(fila.getTipo().equals(TablaSimbolos.UI32)) {
                        directiva = "    _" + var1 + " DD " + par1;
                        // 64 bits
                    } else {
                        directiva = "    _" + var1 + " DQ " + par1;
                    }
                    this.out_assembler.write(directiva);
                }
                else if(fila.getUso().equals(TablaSimbolos.USOSTRING)) {

                    String directiva = "    str_" + Generador.acomodarString(var1.substring(1).replace("_", "__").replace(" ", "_s")) + " DB " + "\"" + var1.substring(1) + "\", 10, 0";
                    this.out_assembler.write(directiva);
                }
                else if(fila.getUso().equals(TablaSimbolos.USOCONSTANTE) && fila.getTipo().equals(TablaSimbolos.F64)){
                    String directiva = "    _" + var1.replace(".", "_") + " DQ " + var1;
                    this.out_assembler.write(directiva);
                }*/
            }
        }
}

