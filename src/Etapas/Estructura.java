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
            +"include \\masm32\\include\\masm32.inc\n" +"include \\masm32\\include\\user32.inc\n"
                    + "includelib \\masm32\\lib\\kernel32.lib\n" + "includelib \\masm32\\lib\\masm32.lib\n"
                    +"includelib \\masm32\\lib\\user32.lib\n";
    public static final String STACK = ".STACK 200h";
    public static final String DATA = ".DATA";
    public static final String CODE = ".CODE";
    public static final String START = "START:";
    public static final String ERRORES = "JMP final\nErrorOverflowSum:\ninvoke MessageBox, NULL, addr _ErrorOverflowSum, addr _ErrorOverflowSum, MB_OK"+"\ninvoke ExitProcess, 0"+"\nJMP final" +
                                         "\nErrorDiv0:\ninvoke MessageBox, NULL, addr _ErrorDiv0, addr _ErrorDiv0, MB_OK"+"\ninvoke ExitProcess, 0"+"\nJMP final" +
                                         "\nErrorOverflowProd:\ninvoke MessageBox, NULL, addr _ErrorOverflowProd, addr _ErrorOverflowProd, MB_OK"+"\ninvoke ExitProcess, 0"+
                                         "\nerror_recursion:\ninvoke MessageBox, NULL, addr _ErrorRec, addr _ErrorRec, MB_OK"+"\nfinal:";
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
                                    "    __aux_conv DW ?\n"+
                                    "   _ErrorOverflowSum DB \"Error por Overflow en una suma\", 0\n"+
                                    "   _ErrorDiv0 DB \"Error Division por Cero en ejecucion\", 0\n"+
                                    "   _ErrorRec DB \"Error recursion en funcion\", 0\n"+
                                    "   _ErrorOverflowProd DB \"Error por Overflow en un producto\", 0\n");
        writeTs(); //.DATA
        if (!Parser.lista_funciones.isEmpty()) {
            this.out_assembler.write("  ; constante para cada funcion a partir de su hashcode");
            Parser.lista_funciones.forEach(x -> this.out_assembler.write("   __"+x+"__" + " DD " + x.hashCode()));
        }
        this.out_assembler.write(CODE);
        Generador.outFunciones.WriteFile();
        this.out_assembler.write(START);
            // Código intermedio
        Generador.outAssembler.WriteFile();
        this.out_assembler.write(ERRORES);
        this.out_assembler.write(END_START);
    }


    public void writeTs(){
        List<Map<String, String>> tsList = new ArrayList<Map<String, String>>(TablaSimbolos.getElementos());

        for(Map<String, String> fila : tsList) {

            String var1 = fila.get("lexema");
            if(fila.get("lexema") != null){
                var uso = fila.get("uso");
                if (uso != null) {
                    if (fila.get("uso").equals("variable") || fila.get("uso").equals("auxiliar") || fila.get("uso").equals("parametro")) {
                        if (fila.get("uso").equals("variable") || fila.get("uso").equals("parametro"))
                        {
                            var1 += "_";
                        }

                        String directiva;

                        if (fila.get("uso").equals("auxiliar")) // el inicio depende de si es auxiliar o no
                            directiva = "    @";
                        else
                            directiva = "    _";

                        if (fila.get("tipo").equals("SHORT")) {

                            directiva += var1 + " DB " + "?";
                            this.out_assembler.write(directiva.replaceAll(":", "_"));

                        } else {   //La cantidad de bytes es larga, solo short usa una longitud corta

                            directiva += var1 + " DD " + "?";
                            this.out_assembler.write(directiva.replaceAll(":", "_"));
                        }
                    } else if (fila.get("uso").equals("cadena")) {
                        String directiva = "    "+fila.get("lexema")+" DB \""+fila.get("cadena")+"\", 0";
                        this.out_assembler.write(directiva);
                    } else if (fila.get("uso").equals("constante") ) {
                        if (fila.get("tipo").equals("FLOAT")) {
                            var1 = var1.replace(".", "");
                            var1 = var1.replace("+", "");
                            var1 = var1.replaceAll("-", "");
                            if (fila.get("lexema").charAt(0) == '-')
                                var1 = "    __n" + var1 + " DD " + fila.get("lexema");
                            else
                                var1 = "    __" + var1 + " DD " + fila.get("lexema");
                            this.out_assembler.write(var1);
                        }
                    }
                }
            }
        }
        }
}

