%{
	package Compilador;

	import AccionesSemanticas.AccionSemantica;

	import java.io.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Stack;
%}

%token ID CADENA VALOR CLASS IF ELSE END_IF PRINT VOID SHORT ULONG FLOAT WHILE DO RETURN MAYORIGUAL MENORIGUAL IGUAL DISTINTO MENOSMENOS
%left '+' '-'
%left '*' '/'


%start programa

%%

programa: '{' cuerpoPrograma '}'
		| '{' '}' {anotarError(errorSintactico, "Programa vacio")}
		| cuerpoPrograma '}' {anotarError(errorSintactico, "Falta llave de apertura '{'")}
		| '{' cuerpoPrograma {anotarError(errorSintactico, "Falta llave de cierre '}'")}

cuerpoPrograma: cuerpoPrograma declaracionClase
		|  cuerpoPrograma listaSentencias
		| declaracionClase
		| listaSentencias

declaracionClase: CLASS ID '{' cuerpoClase '}' ','
				| CLASS ID '{' cuerpoClase '}' {anotarError(errorSintactico, "Falta coma al final de linea")}
				| CLASS ID ','
				| CLASS ID {anotarError(errorSintactico, "Falta coma (',')")}

cuerpoClase: cuerpoClase seccionClase
		| seccionClase

seccionClase: seccionAtributos
			| seccionMetodos

seccionAtributos: tipo ID ';' listaAtributos
				| tipo ID ','
				| tipo ID {anotarError(errorSintactico, "Falta coma (',') al final de linea")}

listaAtributos: ID ';' listaAtributos
			| ID ','
			| ID {anotarError(errorSintactico, "Falta coma (',') al final de linea")}

seccionMetodos: seccionMetodos ',' declaracionMetodo
			| declaracionMetodo ','

listaEjecutables: listaEjecutables ',' sentenciaEjecutable
			| sentenciaEjecutable ','
			| sentenciaEjecutable '$' {anotarError(errorSintactico, "Falta coma (',') al final de linea")}

sentenciaEjecutable: asignacion
    	| sentenciaIf
    	| sentenciaWhile
    	| print
    	| return
	| invocacionMetodo

listaDeclarativa: listaDeclarativa ',' sentenciaDeclarativa
				| sentenciaDeclarativa ','
				| sentenciaDeclarativa '$' {anotarError(errorSintactico, "Falta coma (',') al final de linea")}

sentenciaDeclarativa: declaracionMetodo
					| declaracion

listaSentencias: listaDeclarativa ',' listaEjecutables
				| listaDeclarativa
				| listaEjecutables

asignacion: ID '=' expresion

sentenciaIf: IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' END_IF
			| IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' {anotarError(errorSintactico, "Falta 'END_IF'")}
			| IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' END_IF
			| IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' {anotarError(errorSintactico, "Falta 'END_IF'")}

sentenciaWhile: WHILE '(' expresion comparador expresion ')' '{' listaEjecutables '}'
			| WHILE '(' expresion comparador ')' '{' listaEjecutables '}' {anotarError(errorSintactico, "Mal definida la condicion")}

print: PRINT '%' CADENA '%'

return: RETURN ';'
    		| RETURN expresion ';'

declaracionMetodo: VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'
			| VOID ID '('')' '{' cuerpoMetodo '}'

cuerpoMetodo: listaSentencias

invocacionMetodo: ID '(' tipo ID ')'
		  | ID '(' ')'

declaracion: tipo listaDeclaracion

listaDeclaracion: ID ';' listaDeclaracion
				| ID

tipo: SHORT
    	| ULONG
	| FLOAT
    	| ID

expresion: termino
    	| expresion '+' termino
    	| expresion '-' termino

termino: factor
    	| termino '*' factor
    	| termino '/' factor

factor: ID
	| ID MENOSMENOS
    	| CTE
    	| '-' CTE
    	| '(' expresion ')'

comparador: MAYORIGUAL
	    | MENORIGUAL
	    | IGUAL
	    | DISTINTO
            | '<'
	    | '>'

%%

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();


void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

int yylex(){
    int IDtoken = 0; // IDtoken va a guardar el ID del token que genere el AL
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
    }
    return 0; // 0 indica que se llego al EOF

void anotar (String tipo, String descripcion){ // Agrega un error encontrado, "error" es la descripcion
    switch (tipo){
    	case "Error":
    		errorLexico.add(descripcion);
    	case "Warning":
    		errorSintactico.add(descripcion);
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