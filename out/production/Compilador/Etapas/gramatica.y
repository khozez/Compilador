%{
	package Etapas;
	import java.io.*;
        import AccionesSemanticas.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Stack;
%}


%token ID CTE CADENA CLASS IF ELSE END_IF PRINT VOID SHORT ULONG FLOAT WHILE RETURN MAYORIGUAL MENORIGUAL IGUAL DISTINTO MENOSMENOS DO
%left '+' '-'
%left '*' '/'


%start programa

%%

programa: '{' cuerpoPrograma '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");}
		| '{' '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Programa vacio");}
		| cuerpoPrograma '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de apertura '{'");}
		| '{' cuerpoPrograma {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de cierre '}'");}
;

cuerpoPrograma: listaSentencias
;

listaSentencias: listaSentencias sentenciaDeclarativa
		| listaSentencias sentenciaEjecutable ','
		| sentenciaDeclarativa
		| sentenciaEjecutable ','
;

declaracionClase: CLASS ID '{' cuerpoClase '}'  {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
		 | CLASS ID ',' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
		 | CLASS ID {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
;

cuerpoClase: seccionClase
;

seccionClase: seccionClase seccionAtributos
	     | seccionClase declaracionMetodo ','
	     | declaracionMetodo ','
	     | seccionAtributos
;

referenciaClase: ID '.' referenciaClase
		| ID '.' asignacion {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Referencia a atributo de clase.");}
		| ID '.' invocacionMetodo {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocacion de metodo de clase");}
;

seccionAtributos: tipo ID ';' listaAtributos
				| ID ','
				| tipo ID ','
				| tipo ID {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la definición de variable.");}
;

listaAtributos: ID ';' listaAtributos
			| ID ','
			| ID {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
;


sentenciaEjecutable: asignacion
    	| sentenciaIf
    	| sentenciaWhile
    	| print
	| invocacionMetodo
	| referenciaClase
;

sentenciaDeclarativa: declaracionFuncion
			| declaracion ','
			| declaracionClase
;

sentenciaDeclarativaMetodo: declaracionFuncion
			| declaracion ','
;

asignacion: ID '=' expresion {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");}
	   | ID IGUAL expresion {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
;

sentenciaIf: IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' ELSE sentenciaEjecutable ',' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' expresion comparador expresion ')' ',' END_IF
;

bloque_ejecucion: bloque_ejecucion sentenciaEjecutable ','
		     | sentenciaEjecutable ','
;

sentenciaWhile: WHILE '(' expresion comparador expresion ')' DO '{' bloque_ejecucion '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
               | WHILE '(' expresion comparador expresion ')' DO sentenciaEjecutable {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
	       | WHILE '(' expresion comparador ')' DO '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
;

print: PRINT CADENA {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");}
;

return: RETURN ','
;

declaracionMetodo: VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
			| VOID ID '('')' '{' cuerpoMetodo '}'  {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
;

declaracionFuncion: VOID ID '(' tipo ID ')' '{' cuerpoMetodo return '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
			| VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia RETURN");}
			| VOID ID '('')' '{' cuerpoMetodo return '}'   {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
			| VOID ID '('')' '{' cuerpoMetodo '}'   {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia RETURN");}
;

cuerpoMetodo: listaSentenciasMetodo
;

listaSentenciasMetodo: listaSentenciasMetodo sentenciaDeclarativaMetodo
                       	| listaSentenciasMetodo sentenciaEjecutable ','
                       	| sentenciaDeclarativaMetodo
                       	| sentenciaEjecutable ','
;

invocacionMetodo: ID '(' expresion ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
		  | ID '(' ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
		  | ID '(' asignacion ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
		  | ID '(' tipo asignacion ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
;

declaracion: tipo listaDeclaracion
	    | tipo asignacion
;

listaDeclaracion: ID ';' listaDeclaracion
		| ID
;

tipo: SHORT
    	| ULONG
	| FLOAT
    	| ID
;

expresion: termino
    	| expresion '+' termino
    	| expresion '-' termino
;

termino: factor
    	| termino '*' factor
    	| termino '/' factor
;

factor: ID
	| ID MENOSMENOS
    	| CTE
    	| '-' CTE {comprobarRango($2.sval);}
    	| '(' expresion ')'
;

comparador: MAYORIGUAL
	    | MENORIGUAL
	    | IGUAL
	    | DISTINTO
            | '<'
	    | '>'
	    | '=' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
;

%%

public static final String ERROR_LEXICO = "Error_lexico";
public static final String ERROR_SINTACTICO = "Error_sintactico";
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();


void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

public static void comprobarRango(String valor){
    int id = TablaSimbolos.obtenerSimbolo(valor);
	String tipo = TablaSimbolos.obtenerAtributo(id, "tipo");
    int referencias = Integer.parseInt(TablaSimbolos.obtenerAtributo(id, "referencias"));
    TablaSimbolos.modificarAtributo(id, "referencias", Integer.toString(referencias-1));
    boolean agregado;
	if (tipo.equals("short")){
        // Controlamos rango negativo de SHORT (el cual pudo haber sido truncado)
        String original = TablaSimbolos.obtenerAtributo(id, "valor_original");
        if (original != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
        {
            //LA CONSTANTE FUE TRUNCADA, RECUPERAMOS VALOR ORIGINAL
            int numero = -Integer.parseInt(original);
            if (numero < AnalizadorLexico.MIN_SHORT_INT){
                anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": WARNING! Se truncó la constante entera negativa -"+original+"_s por ser inferior al valor minimo.");
                numero = AnalizadorLexico.MIN_SHORT_INT;
            }
            agregado = TablaSimbolos.agregarSimbolo(Integer.toString(numero)+"_s");
            modificar_referencias(agregado, Integer.toString(numero)+"_s", "short");
            TablaSimbolos.eliminarAtributo(id, "valor_original");
        }
        else
        {
            valor = "-"+valor+"_s";
            agregado = TablaSimbolos.agregarSimbolo(valor);
            modificar_referencias(agregado, valor, "short");
        }
	}

	if (tipo.equals("float")){
		valor = "-"+valor;
        agregado = TablaSimbolos.agregarSimbolo(valor);
        modificar_referencias(agregado, valor, "float");
	}

    if (tipo.equals("long")){
        anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": WARNING! Se truncó la constante long -"+valor+" ya que no se aceptan valores negativos.");
        valor = "0_ul";
        agregado = TablaSimbolos.agregarSimbolo(valor);
        modificar_referencias(agregado, valor, "long");
    }

    if (!(referencias-1 > 0))
    {
        TablaSimbolos.eliminarSimbolo(id);
    }
}

private static void modificar_referencias(boolean agregado, String numero, String tipo)
{
    if (agregado)
    {
        TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(numero), "tipo", tipo);
        TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(numero), "referencias", "1");
    }
    else
    {
        int ref = Integer.parseInt(TablaSimbolos.obtenerAtributo(TablaSimbolos.obtenerSimbolo(numero), "referencias"));
        TablaSimbolos.modificarAtributo(TablaSimbolos.obtenerSimbolo(numero), "referencias", Integer.toString(ref+1));
    }

}

int yylex(){
    int IDtoken = 0; // IDtoken va a guardar el ID del token que genere el AL
    AnalizadorLexico.estado = 0;
    BufferedInputStream lector = AnalizadorLexico.lector; // agarro el lector
    while (true) {
        try {
            if (lector.available() <= 0) { // me aseguro que no este vacio el buffer
                break; // si esta vacio se detiene el while
            }

            lector.mark(1);
            char c = (char) lector.read(); // consigo el siguiente caracter
            lector.reset();
            IDtoken = AnalizadorLexico.getToken(lector, c); // pido el token
            if (IDtoken != -1){ // si AL no define un token entonces sigue probando con los proximos caracteres
                yylval = new ParserVal(AnalizadorLexico.lexema);
                AnalizadorLexico.lexema= ""; // borro lexema porque no lo necesito mas
                return IDtoken; // devuelvo el ID del token
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    return IDtoken;
}

public static void anotar (String tipo, String descripcion){ // Agrega un error encontrado, "error" es la descripcion
    switch (tipo){
    	case "Error_lexico":
    		errorLexico.add(descripcion);
    		break;
    	case "Error_sintactico":
    		errorSintactico.add(descripcion);
    		break;
    }
}

public static void imprimir(List<String> lista, String cabecera) {
        if (!lista.isEmpty()) {
                System.out.println();
                System.out.println(cabecera + ":");
                for (String x: lista) {
                        System.out.println(x);
                }
        }
}

public static void main(String[] args) {
        if (args.length >= 1) {
                String archivo = args[0];
		System.out.println("Compilando el archivo: " + archivo);
                try {
                	FileInputStream fis = new FileInputStream(archivo);
                        BufferedInputStream lector = new BufferedInputStream(fis);
                        AnalizadorLexico.lector = lector;
                        Parser parser = new Parser();
                        parser.run();
                } catch (IOException excepcion) {
                        excepcion.printStackTrace();
                }
                Parser.imprimir(errorLexico, "Errores Lexicos");
                Parser.imprimir(errorSintactico, "Errores Sintacticos");
                TablaSimbolos.imprimirTabla();
        } else {
                System.out.println("No se especifico el archivo a compilar");
        }
}