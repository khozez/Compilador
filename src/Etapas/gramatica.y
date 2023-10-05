%{
	package Etapas;
	import java.io.*;
        import AccionesSemanticas.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Stack;
%}


%token ID CTE CADENA CLASS IF ELSE END_IF PRINT VOID SHORT ULONG FLOAT WHILE RETURN MAYORIGUAL MENORIGUAL IGUAL DISTINTO MENOSMENOS
%left '+' '-'
%left '*' '/'


%start programa

%%

programa: '{' cuerpoPrograma '}'
		| '{' '}' {anotar(ERROR_SINTACTICO, "Programa vacio");}
		| cuerpoPrograma '}' {anotar(ERROR_SINTACTICO, "Falta llave de apertura '{'");}
		| '{' cuerpoPrograma {anotar(ERROR_SINTACTICO, "Falta llave de cierre '}'");}
;

cuerpoPrograma: cuerpoPrograma declaracionClase
		|  cuerpoPrograma listaSentencias
		| declaracionClase
		| listaSentencias
;

declaracionClase: CLASS ID '{' cuerpoClase '}' ','
				| CLASS ID '{' cuerpoClase '}' {anotar(ERROR_SINTACTICO, "Falta coma al final de linea");}
				| CLASS ID ','
				| CLASS ID {anotar(ERROR_SINTACTICO, "Falta coma (',')");}
;

cuerpoClase: cuerpoClase seccionClase
		| seccionClase
;

seccionClase: seccionAtributos
			| seccionMetodos
;

seccionAtributos: tipo ID ';' listaAtributos
				| tipo ID ','
				| tipo ID {anotar(ERROR_SINTACTICO, "Falta coma (',') al final de linea");}
;

listaAtributos: ID ';' listaAtributos
			| ID ','
			| ID {anotar(ERROR_SINTACTICO, "Falta coma (',') al final de linea");}
;

seccionMetodos: seccionMetodos ',' declaracionMetodo
			| declaracionMetodo ','
;

listaEjecutables: listaEjecutables ',' sentenciaEjecutable
			| sentenciaEjecutable ','
			| sentenciaEjecutable '$' {anotar(ERROR_SINTACTICO, "Falta coma (',') al final de linea");}
;

sentenciaEjecutable: asignacion
    	| sentenciaIf
    	| sentenciaWhile
    	| print
    	| return
	| invocacionMetodo
;

listaDeclarativa: listaDeclarativa ',' sentenciaDeclarativa
				| sentenciaDeclarativa ','
				| sentenciaDeclarativa '$' {anotar(ERROR_SINTACTICO, "Falta coma (',') al final de linea");}
;

sentenciaDeclarativa: declaracionMetodo
					| declaracion
;

listaSentencias: listaDeclarativa ',' listaEjecutables
				| listaDeclarativa
				| listaEjecutables
;

asignacion: ID '=' expresion
;

sentenciaIf: IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' END_IF
			| IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' {anotar(ERROR_SINTACTICO, "Falta 'END_IF'");}
			| IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' END_IF
			| IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' {anotar(ERROR_SINTACTICO, "Falta 'END_IF'");}
;

sentenciaWhile: WHILE '(' expresion comparador expresion ')' '{' listaEjecutables '}'
			| WHILE '(' expresion comparador ')' '{' listaEjecutables '}' {anotar(ERROR_SINTACTICO, "Mal definida la condicion");}
;

print: '%' CADENA '%'
;

return: RETURN ';'
    		| RETURN expresion ';'
;

declaracionMetodo: VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'
			| VOID ID '('')' '{' cuerpoMetodo '}'
;

cuerpoMetodo: listaSentencias
;

invocacionMetodo: ID '(' tipo ID ')'
		  | ID '(' ')'
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
    	| '-' CTE
    	| '(' expresion ')'
;

comparador: MAYORIGUAL
	    | MENORIGUAL
	    | IGUAL
	    | DISTINTO
            | '<'
	    | '>'
;

%%

public static final String ERROR_LEXICO = "Error";
public static final String ERROR_SINTACTICO = "Error_sintactico";
public static final String WARNING = "Warning";
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();


void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
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
    	case "Warning":
    		errorSintactico.add(descripcion);
    	case "Error_sintactico":
    		errorSintactico.add(descripcion);
    }
}

public static void imprimir(List<String> lista, String cabecera) {
        if (!lista.isEmpty()) {
                System.out.println();
                System.out.println(cabecera + ":");
		System.out.println(lista.size());
                for (String x: lista) {
                        System.out.println(x);
                }
        }
}

public static void main(String[] args) {
        String archivo = "src\\AL-TEST-1.txt";
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
}

/* MAIN PARA CUANDO EJECUTEMOS POR CONSOLA
public static void main(String[] args) {
        if (args.length > 1) {
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
} */

//BORRAR ESTO - REFERENCIA 2021
/*
public static void main(String[] args) {
        if (args.length > 1) {
                String archivo_a_leer = args[0];
                System.out.println("Se esta compilando el siguiente archivo: " + archivo_a_leer);

                try {
                        AnalizadorLexico.lector = new BufferedReader(new FileReader(archivo_a_leer));
                        Parser parser = new Parser();
                        parser.run();
                } catch (IOException excepcion) {
                        excepcion.printStackTrace();
                }

                Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
                Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");

                if (!errores_compilacion) {
                        GeneradorCodigo.generarCodigo();
                        FileHelper.writeProgram(args[1], GeneradorCodigo.codigo.toString());
                }

                Parser.imprimirErrores(errores_semanticos, "Errores Semanticos");
                Parser.imprimirPolaca();
                TablaSimbolos.imprimirTabla();
        } else {
                System.out.println("No se especifico el archivo a compilar");
        }
}
*/