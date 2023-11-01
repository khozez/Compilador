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

programa: '{' cuerpoPrograma '}' {raiz = new Nodo("program", (Nodo) $2.obj , null); System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");}
		| '{' '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Programa vacio");}
		| cuerpoPrograma '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de apertura '{'");}
		| '{' cuerpoPrograma {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de cierre '}'");}
;

cuerpoPrograma: sentencia {$$ = $1}
		| cuerpoPrograma sentencia { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj));}
;

sentencia: sentenciaDeclarativa
		| sentenciaEjecutable ','  {$$ = new ParserVal( $1.obj);}
;

declaracionClase: CLASS ID '{' cuerpoClase '}'  {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
		 | CLASS ID ',' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
		 | CLASS ID {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
;

cuerpoClase: seccionClase { $$ = $1 }
;

seccionClase: seccionClase seccionAtributos
	     | seccionClase declaracionMetodo ','
	     | declaracionMetodo ','
	     | seccionAtributos
;

referenciaClase: ID '.' referenciaClase
		| ID '.' asignacion {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Referencia a atributo de clase.");}
		| ID '.' invocacionMetodo
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


sentenciaEjecutable: asignacion {$$ = new ParserVal( $1.obj);}
    		   | sentenciaIf {$$ = new ParserVal( $1.obj);}
    		   | sentenciaWhile {$$ = new ParserVal( $1.obj);}
    		   | print {$$ = new ParserVal( $1.obj);}
		   | invocacionMetodo {$$ = new ParserVal( $1.obj);}
		   | referenciaClase {$$ = new ParserVal( $1.obj);}
;

sentenciaDeclarativa: declaracionFuncion
			| declaracion ','
			| declaracionClase
;

sentenciaDeclarativaMetodo: declaracionFuncion
			| declaracion ','
;


asignacion: ID '=' expresion {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");
			      var x = new Nodo("Asignacion", new Nodo(getNombreTablaSimbolosVariables($1.sval), getTipoTablaSimbolosVariables($1.sval)), (Nodo) $3.obj);
			      x.setTipo(validarTiposAssign(x.getIzq(), x.getDer()));
			      $$ = new ParserVal(x); }
	   | ID IGUAL expresion {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
;

sentenciaIf: IF '(' condicion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
												$$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $6.obj, null), new Nodo("else", (Nodo) $10.obj, null)))); }
			| IF '(' condicion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' '{' bloque_ejecucion '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										$$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $6.obj, null), new Nodo("else", null, null))));
										}
			| IF '(' condicion ')' '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' sentenciaEjecutable ',' ELSE sentenciaEjecutable ',' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", (Nodo) $8.obj, null))));
													   }
			| IF '(' condicion ')' sentenciaEjecutable ',' ELSE '{' bloque_ejecucion '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", (Nodo) $9.obj, null))));
													    }
			| IF '(' condicion ')' sentenciaEjecutable ',' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										$$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", null, null))));
									      }
;

condicion: expresion comparador expresion { $$ = new ParserVal(new Nodo($2.sval, (Nodo) $1.obj, (Nodo) $3.obj, validarTipos((Nodo) $1.obj, (Nodo) $3.obj)));}
			| comparador expresion {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el primer miembro de la condicion");}
			| expresion comparador {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el segundo miembro de la condicion");}
;

sentenciaIfRetorno: IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return ',' '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return ',' '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' '{' bloque_ejecucion_return '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' condicion ')' '{' bloque_ejecucion_return '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE sentencia_ejecutable_return ',' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE '{' bloque_ejecucion_return ',' '}' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
			| IF '(' condicion ')' sentencia_ejecutable_return ',' END_IF {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
;

sentencia_ejecutable_return:  print {$$ = new ParserVal( $1.obj);}
				| asignacion {$$ = new ParserVal( $1.obj);}
                                | sentenciaIfRetorno {$$ = new ParserVal( $1.obj);}
                                | sentenciaWhile {$$ = new ParserVal( $1.obj);}
                                | invocacionMetodo {$$ = new ParserVal( $1.obj);}
                                | referenciaClase {$$ = new ParserVal( $1.obj);}
                                | return {$$ = new ParserVal( $1.obj);}
;

bloque_ejecucion: bloque_ejecucion sentenciaEjecutable ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj)); }
                  | sentenciaEjecutable ',' {$$ = new ParserVal( $1.obj);}

;

bloque_ejecucion_return: bloque_ejecucion_return sentencia_ejecutable_return ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj)); }
		     | sentencia_ejecutable_return ',' {$$ = new ParserVal( $1.obj);}
;

sentenciaWhile: WHILE '(' condicion ')' DO '{' bloque_ejecucion '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
								     $$ = new ParserVal( new Nodo("while", (Nodo) $3.obj, (Nodo) $7.obj));
								    }
               | WHILE '(' condicion ')' DO sentenciaEjecutable {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
               							 $$ = new ParserVal( new Nodo("while", (Nodo) $3.obj, (Nodo) $6.obj));
               							}
;

print: PRINT CADENA {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");
		    $$ = new ParserVal( new Nodo("Print", new Nodo(getVariableConAmbitoTS($2.sval)), null, "string"));}
       | CADENA {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
;

return: RETURN
;

declaracionMetodo: VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
			| VOID ID '('')' '{' cuerpoMetodo '}'  {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
;

declaracionFuncion: VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
			| VOID ID '(' ID ')' '{' cuerpoMetodo '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
			| VOID ID '('')' '{' cuerpoMetodo '}'   {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
;

cuerpoMetodo: listaSentenciasMetodo
;

listaSentenciasMetodo: listaSentenciasMetodo sentenciaDeclarativaMetodo
                       	| listaSentenciasMetodo sentencia_ejecutable_return ','
                       	| sentenciaDeclarativaMetodo
                       	| sentencia_ejecutable_return ','
;

invocacionMetodo: ID '(' expresion ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
					var x = new Nodo("LlamadaFuncion", new Nodo(getNombreTablaSimbolosVariables($1.sval)), null, getTipoTablaSimbolosVariables($1.sval));
					$$ = new ParserVal(x);
					chequearLlamadoFuncion($1.sval);
					}
		  | ID '(' ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
		  		var x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS($1.sval)), null, getTipoVariableConAmbitoTS($1.sval));
		  		$$ = new ParserVal(x);
		  		chequearLlamadoFuncion($1.sval);
		  		}
		  | ID '(' asignacion ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
		  | ID '(' tipo asignacion ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
;

declaracion: tipo listaDeclaracion
	         {
                     var t = TablaSimbolos.getInstance();
                     lista_variables
                         .forEach( x ->
                             {
                               int clave = t.obtenerSimbolo(x);
                               if (clave != t.NO_ENCONTRADO){
                                 if (t.obtenerAtributo(clave, "tipo") == t.NO_ENCONTRADO_MESSAGE) {
                                 	t.agregarAtributo(clave, "uso", "variable");
                                 	t.agregarAtributo(clave, "tipo", $1.sval);
                                 } else {
                                   	yyerror("La variable declarada ya existe " + (x.contains(":") ? x.substring(0, x.indexOf(':')) : "en ambito global"));
                               	 }
                               } else {
                               		t.agregarSimbolo(x);
                               		t.agregarAtributo(t.obtenerID(), "tipo", $1.sval);
                               		t.agregarAtributo(t.obtenerID(), "uso", "variable");
                               }
                             });
                         lista_variables.clear();
             	}
;


listaDeclaracion: ID ';' listaDeclaracion { if (pilaWhen.isEmpty() || pilaWhen.getFirst()) lista_variables.add($1.sval + Parser.ambito); }
		| ID { if (pilaWhen.isEmpty() || pilaWhen.getFirst()) lista_variables.add($1.sval + Parser.ambito);}
;

tipo: SHORT  { $$ = $1; }
    	| ULONG { $$ = $1; }
	| FLOAT { $$ = $1; }
    	| ID {declaracion de clase, que hacemos?}
;

expresion: termino { $$ = $1; }
    	| expresion '+' termino { $$ = new ParserVal(new Nodo("+", (Nodo) $1.obj, (Nodo)  $3.obj, validarTipos((Nodo) $1.obj, (Nodo) $3.obj))); }
    	| expresion '-' termino { $$ = new ParserVal(new Nodo("-", (Nodo) $1.obj, (Nodo)  $3.obj, validarTipos((Nodo) $1.obj, (Nodo) $3.obj))); }
;

termino: factor { $$ = $1; }
    	| termino '*' factor { var x = new Nodo("*", (Nodo) $1.obj, (Nodo)  $3.obj);
    			       x.setTipo(validarTipos((Nodo) $1.obj, (Nodo) $3.obj));
    			       $$ = new ParserVal(x); }
    	| termino '/' factor { var x = new Nodo("/", (Nodo) $1.obj, (Nodo)  $3.obj);
                               x.setTipo(validarTipos((Nodo) $1.obj, (Nodo) $3.obj));
                               $$ = new ParserVal(x); }
;

factor: ID {String x = getTipoVariableConAmbitoTS($1.sval);
            if (!x == TablaSimbolos.NO_ENCONTRADO_MESSAGE)
            	$$ =  new ParserVal( new Nodo(getVariableConAmbitoTS($1.sval), x));
            else{
            	$$ =  new ParserVal( new Nodo("variableNoEncontrada", x));
                yyerror("No se encontro esta variable en un ambito adecuado");
                }
            }
	| ID MENOSMENOS {en este caso debemos actualizar el valor de la variable segun enunciado, PREGUNTAR}
    	| CTE { $$ = new ParserVal( new Nodo($1.sval, getTipo($1.sval))); }
    	| '-' CTE { String x = comprobarRango($2.sval); $$ = new ParserVal( new Nodo(x, getTipo(x))); }
    	| '(' expresion ')' { $$ = $2; }
;

comparador: MAYORIGUAL   { $$ = new ParserVal(">="); }
	    | MENORIGUAL { $$ = new ParserVal("<="); }
	    | IGUAL    { $$ = new ParserVal("=="); }
	    | DISTINTO { $$ = new ParserVal("!!"); }
            | '<'  { $$ = new ParserVal("<"); }
	    | '>'  { $$ = new ParserVal("<"); }
	    | '=' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
;

%%

public static Nodo raiz = null;
public static String ambito = "";
public static final String ERROR_LEXICO = "Error_lexico";
public static final String ERROR_SINTACTICO = "Error_sintactico";
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();
public static ArrayList<String> lista_variables = new ArrayList<>();
public static LinkedList<Boolean> pilaWhen = new LinkedList<>();


void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

public String getTipo(String lexema){
	if (getTipoTS(lexema) == TablaSimbolos.NO_ENCONTRADO_MESSAGE)
    		yyerror("La variable no esta declarada");
  	return x;
}

private String validarTipos(Nodo obj, Nodo obj1) {
	if (obj == null )
        	return "obj is null";
        if (obj1 == null)
          	return "obj1 is null";
        if (obj.getTipo() == "null")
          	return "obj type is null";
        if (obj1.getTipo() == "null")
          	return "obj1 type is null";


    	if (obj.getTipo().equals("Error") || (obj1.getTipo().equals("Error"))){
       		return "Error";
    	}


    	if (!obj.getTipo().equals(obj1.getTipo())) {
    		//AL MENOS UNO DE LOS OPERANDOS DEBE SER FLOAT, SINO ERROR
    		if (obj.getTipo() == "float" || obj1.getTipo() == "float)
    			//chequear cual de los 2 es float, y al otro crearle nodo intermedio de conversion
    			return "float";
    		else{
    			yyerror("Incompatibilidad de tipos");
        		return "Error";
        	}
    	}
    	else return obj.getTipo();
}

private String validarTiposAssign(Nodo izq, Nodo der) {
	if (izq == null )
        	return "obj is null";
        if (der == null)
          	return "obj1 is null";
        if (izq.getTipo() == "null")
          	return "obj type is null";
        if (der.getTipo() == "null")
          	return "obj1 type is null";


    	if (izq.getTipo().equals("Error") || (der.getTipo().equals("Error"))){
       		return "Error";
    	}


    	if (!izq.getTipo().equals(der.getTipo())) {
    		//EL OPERANDO DE LA IZQUIERDA DEBE SER FLOAT, SINO ERROR
    		if (izq.getTipo() == "float")
    			if (der.getTipo() == "short")
    				//crear nodo stof y que der apunte a él
    			if (der.getTipo() == "long")
    				//crear nodo ltof y que der apunte a él
    			return "float";
    		else{
    			yyerror("Incompatibilidad de tipos en la asignación.");
        		return "Error";
        	}
    	}
    	else return obj.getTipo();
}

private String getTipoTS(String lexema)
{
	int clave = TablaSimbolos.getInstance().obtenerSimbolo(lexema);
	return TablaSimbolos.getInstance().obtenerAtributo(clave, "tipo");
}

public void agregarAmbito(String ambito){
      //Agregamos al principio el nuevo ambito para que sea más sencillo eliminarlo luego con substring
      Parser.ambito = ":" + ambito + Parser.ambito;
}

public void salirAmbito(){
    Parser.ambito = Parser.ambito.substring(1);  //ELIMINAMOS ; DEL PRINCIPIO
    if (Parser.ambito.contains(":"))
      Parser.ambito = Parser.ambito.substring(Parser.ambito.indexOf(':'));
    else
      Parser.ambito = "";
}


private String getVariableConAmbitoTS(String sval) {
	String ambito_actual = getAmbito(sval);

  	if (getTipoTS(sval + ambito_actual) == TablaSimbolos.NO_ENCONTRADO_MESSAGE)
    		yyerror("La variable: '" + sval + "' no fue encontrada en un ambito permitido.");

  	return (sval+ambito_actual);
}

private String getTipoVariableConAmbitoTS(String sval) {
  	String ambito_actual = getAmbito(sval);

  	if (getTipoTS(sval + ambito_actual) == TablaSimbolos.NO_ENCONTRADO_MESSAGE && !falloNombre(sval) )
    		yyerror("No se encontro el tipo para esta variable en un ambito valido");

	//RETORNAMOS EL TIPO DE LA VARIABLE
  	return getTipoTS(sval+ ambito_actual);
}

private Boolean falloNombre(String sval){
	//QUE PASA SI BORRAMOS ESTA FUNCION? NO ENTIENDO QUE FUNCIONAMIENTO TIENE
	//REVISAR

     	String ambito_actual = getAmbito(sval);
      	return (getTipoTS(sval + ambito_actual) == TablaSimbolos.NO_ENCONTRADO_MESSAGE);
}

private String getAmbito(String nombreVar){
    	String ambito_actual = Parser.ambito;
    	while(!ambito_actual.isBlank() && getTipoTS(nombreVar + ambito_actual) == TablaSimbolos.NO_ENCONTRADO_MESSAGE){
        	ambito_actual = ambito_actual.substring(1);
        	if (ambito_actual.contains(":"))
            		ambito_actual = ambito_actual.substring(ambito_actual.indexOf(':'));
        	else
            		ambito_actual = "";
      		}
    	return ambito_actual;
  }

//CHEQUEO DE LLAMADO A FUNCION SIN PARAMETROS
private void chequearLlamadoFuncion(String funcion) {
  	var t = TablaSimbolos.getInstance();
  	int entrada = TablaSimbolos.getInstance().obtenerSimbolo(funcion + getAmbito(funcion));

	//SI LA FUNCION TIENE UN PARAMETRO, SE NOTIFICA ERROR
  	if (TablaSimbolos.getInstance().obtenerAtributo(clave, "parametro") != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
    		yyerror("La funcion a la que se desea llamar tiene parametro");
}

public static String comprobarRango(String valor){
    int id = TablaSimbolos.obtenerSimbolo(valor);
    String tipo = TablaSimbolos.obtenerAtributo(id, "tipo");
    String valor_final;
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
            valor_final = Integer.toString(numero)+"_s";
            agregado = TablaSimbolos.agregarSimbolo(valor_final);
            modificar_referencias(agregado, valor_final, "short");
            TablaSimbolos.eliminarAtributo(id, "valor_original");
        }
        else
        {
            valor_final = "-"+valor;
            agregado = TablaSimbolos.agregarSimbolo(valor_final);
            modificar_referencias(agregado, valor_final, "short");
        }
	}

	if (tipo.equals("float")){
		valor_final = "-"+valor;
        	agregado = TablaSimbolos.agregarSimbolo(valor_final);
        	modificar_referencias(agregado, valor_final, "float");
	}

    if (tipo.equals("long")){
        anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": WARNING! Se truncó la constante long -"+valor+" ya que no se aceptan valores negativos.");
        valor_final = "0_ul";
        agregado = TablaSimbolos.agregarSimbolo(valor_final);
        modificar_referencias(agregado, valor_final, "long");
    }

    if (!(referencias-1 > 0))
    {
        TablaSimbolos.eliminarSimbolo(id);
    }

    return valor_final;
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