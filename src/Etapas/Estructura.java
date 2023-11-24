package Etapas;
import Etapas.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Etapas.OutputManager;
import Etapas.TablaSimbolos;

public class Estructura {
    public static final String MODEL = ".386\n.MODEL flat, stdcall\noption casemap :none";
    public static final String INCLUDE =
            "include \\masm32\\include\\windows.inc\n" + "include \\masm32\\include\\kernel32.inc\n"
            + "include \\masm32\\include\\user32.inc\n" + "includelib \\masm32\\lib\\user32.lib\n" +
            "include \\masm32\\include\\masm32rt.inc\n" + "includelib \\masm32\\lib\\kernel32.lib\n" +
            "includelib \\masm32\\lib\\masm32.lib\n";
    public static final String STACK = ".STACK 200h";
    public static final String DATA = ".DATA";
    public static final String CODE = ".CODE";
    public static final String START = "START:";
    public static final String ERRORES = "JMP final\nerror_overflow:\ninvoke MessageBox, NULL, addr ErrorOverflowSum, addr ErrorOverflowSum, MB_OK"+"\ninvoke ExitProcess, 0"+"\nJMP final" +
                                         "\ndivision_por_cero:\ninvoke MessageBox, NULL, addr ErrorDiv, addr ErrorDiv, MB_OK"+"\ninvoke ExitProcess, 0"+"\nJMP final" +
                                         "\nerror_overflow:\ninvoke MessageBox, NULL, addr ErrorOverflowProd, addr ErrorOverflowProd, MB_OK"+"\ninvoke ExitProcess, 0"+
                                         "\nfinal:";
    public static final String END_START = "END START";

    private final OutputManager out_assembler = new OutputManager("./Assembler.asm");

    public Estructura() {}

    public void generateCode(Nodo padre) {
        // Generar código assembler en órden
        Generador.GenerarCodigoArbol(padre);
        this.out_assembler.write(MODEL);
        this.out_assembler.write(INCLUDE);
        this.out_assembler.write(STACK);
        this.out_assembler.write(DATA);
        this.out_assembler.write("   aux_mem_2bytes DW ?\n" +
                                    "    __funcion_actual__ DD 0\n" +
                                    "   ErrorOverflowSum DB \"Error por Overflow en una suma\", 0\n"+
                                    "   ErrorDiv0 DB \"Error Division por Cero en ejecucion\", 0\n"+
                                    "   ErrorOverflowProd DB \"Error por Overflow en un producto\", 0\n");
        writeTs(); //.DATA
        if (!Parser.lista_funciones.isEmpty()) {
            this.out_assembler.write("  ; constante para cada funcion a partir de su hashcode");
            Parser.lista_funciones.forEach(x -> this.out_assembler.write(x + " DD " + x.hashCode()));
        }
        this.out_assembler.write(CODE);
        Generador.outFunciones.WriteFile();// --ERROR PISA EL ARCIVHO -- Ni idea porque
        this.out_assembler.write(START);
            // Código intermedio
        Generador.outAssembler.WriteFile(); //--ERROR PISA EL ARCIVHO
        this.out_assembler.write(ERRORES);
        this.out_assembler.write(END_START);
    }


    public void writeTs(){ //VARIOS ERRORES, CON LAS CLASES POR EJEMPLO TIRA PUNTERO EN NULL
        //List<EntradaTablaSimbolos> tsList = this.tablaToList();
        List<Map<String, String>> tsList = new ArrayList<Map<String, String>>(TablaSimbolos.getElementos());

        for(Map<String, String> fila : tsList) {

            String var1 = fila.get("lexema");
            if(fila.get("lexema") != null){
                var uso = fila.get("uso");
                if (uso != null) {
                    if (fila.get("uso").equals("variable") || fila.get("uso").equals("auxiliar") || fila.get("uso").equals("parametro")) {
                        var1 += "_";

                        String directiva;

                        if (fila.get("uso").equals("auxiliar")) // el inicio depende de si es auxiliar o no
                            directiva = "    @";
                        else
                            directiva = "    _";

                        if (fila.get("tipo").equals("SHORT")) {  //POR QUE SOLO SHORT? Indica que la cantidad de bytes es corta

                            directiva += var1 + " DB " + "?";
                            this.out_assembler.write(directiva);

                        } else {   //La cantidad de bytes es larga, solo short usa una longitud corta

                            directiva += var1 + " DD " + "?";
                            this.out_assembler.write(directiva);
                        }
                    } else if (fila.get("uso").equals("cadena")) {
                        String directiva = "    "+fila.get("lexema")+" DB \""+fila.get("cadena")+"\", 0";
                        this.out_assembler.write(directiva);
                    }
                }
            }
        }
        }
}

