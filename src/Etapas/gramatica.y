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

programa: '{' cuerpoPrograma '}' {raiz = new Nodo("program", (Nodo) $2.obj , null);
				  System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");
				  chequeoAsignacionVariables();}
		| '{' '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Programa vacio");}
		| cuerpoPrograma '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de apertura '{'");}
		| '{' cuerpoPrograma {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de cierre '}'");}
;

cuerpoPrograma: sentencia {$$ = $1;}
		| cuerpoPrograma sentencia { if ($2.obj != null)
						$$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj));}
;

sentencia: sentenciaDeclarativa {$$ = new ParserVal( $1.obj);}
		| sentenciaEjecutable ','  {$$ = new ParserVal( $1.obj);}
;

encabezadoClase: CLASS ID {var t = AnalizadorLexico.TS;
                           int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                           if (clave != t.NO_ENCONTRADO)
                           	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                	yyerror("El nombre de la clase " + $2.sval +  " ya fue utilizado en este ambito");
                                else { //la tabla de simbolos contiene la clase pero no tiene el uso asignado.
                                        t.agregarAtributo(clave, "uso", "nombre_clase");
                                }
                           else {
                                t.agregarSimbolo($2.sval + Parser.ambito);
                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_clase");
                           }
                           $$ = new ParserVal(new Nodo("EncabezadoClase", new Nodo($2.sval + Parser.ambito), null));
                           lista_clases.add($2.sval + Parser.ambito.replace(':','_'));
                           claseActual = $2.sval;
                           ambitoClase = Parser.ambito;
                           agregarAmbito(claseActual);
                          }
;

declaracionClase: encabezadoClase '{' cuerpoClase '}'  {lista_clases_fd.remove(Parser.ambito.substring(1));
							chequeoAsignacionVariables();
                                                        salirAmbito();
                                                        claseActual = "";
							System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");
							$$ = new ParserVal( new Nodo("Clase", (Nodo) $1.obj, (Nodo) $3.obj) );}
		 | encabezadoClase ',' {lista_clases_fd.add(Parser.ambito.substring(1));
		 			chequeoAsignacionVariables();
                                        salirAmbito();
                                        claseActual = "";
                                        ambitoClase = "";
		 			System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
		 | encabezadoClase {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
;

cuerpoClase: cuerpoClase seccionAtributos
	     | cuerpoClase declaracionMetodo ',' { $$ = new ParserVal( new Nodo("cuerpoClase", (Nodo) $1.obj, (Nodo) $2.obj));}
	     | declaracionMetodo ','  { $$ = $1; }
	     | seccionAtributos
;


referenciaClase: listaReferencia asignacionClase {$$ = $2;}
		 | listaReferencia referenciaMetodo {$$ = $2;}
;

asignacionClase: ID '=' expresion {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación a atributo de clase");
				   var t = AnalizadorLexico.TS;
                                   variableAmbitoClase = ":" + $1.sval + ambitoClase;
                                   chequearHerenciaVariable(variableAmbitoClase);
                                   var x = new Nodo("Asignacion", new Nodo(variableAmbitoClase, t.obtenerAtributo(t.obtenerSimbolo(variableAmbitoClase), "tipo")), (Nodo) $3.obj);
                                   x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));
                                   $$ = new ParserVal(x);
				  }
;

referenciaMetodo: ID '(' ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Llamado a metodo de clase");
                              var t = AnalizadorLexico.TS;
                              variableAmbitoClase = ":" + $1.sval + ambitoClase;
                              chequearHerenciaVariable(variableAmbitoClase);
                              var x = new Nodo("LlamadaFuncion", new Nodo(variableAmbitoClase, null, null, "void"), null);
                              $$ = new ParserVal(x);
                              chequearLlamadoFuncion($1.sval);
                              }
                 | ID '(' expresion ')'
;

listaReferencia: listaReferencia ID '.' {variableAmbitoClase = ":" + $2.sval + variableAmbitoClase;}
		| ID '.' {variableAmbitoClase = ":"+ $1.sval + variableAmbitoClase;}
;

seccionAtributos: tipo listaAtributos
					{
                                             var t = AnalizadorLexico.TS;
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
                 | herenciaNombre
;

listaAtributos: ID ';' listaAtributos { lista_variables.add($1.sval + Parser.ambito); }
		| ID ',' { lista_variables.add($1.sval + Parser.ambito); }
		| ID {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
;

herenciaNombre: ID ',' { var t = AnalizadorLexico.TS;
			 int clave = t.obtenerSimbolo($1.sval+Parser.ambito);
 		       	 if (clave != t.NO_ENCONTRADO){
                         	if (t.obtenerAtributo(clave, "tipo") == t.NO_ENCONTRADO_MESSAGE) {
                         		t.agregarAtributo(clave, "uso", "herencia");
                                } else {
                                	yyerror("La variable declarada ya existe en ambito global");
                                }
                         } else {
                                 t.agregarSimbolo($1.sval+Parser.ambito);
                                 t.agregarAtributo(t.obtenerID(), "uso", "herencia");
                         }
                         t.aplicarHerencia($1.sval, claseActual, Parser.ambito);
			 chequearNivelesHerencia(Parser.ambito.substring(1));
 		       }
;

sentenciaEjecutable: asignacion {$$ = new ParserVal( $1.obj);}
    		   | sentenciaIf {$$ = new ParserVal( $1.obj);}
    		   | sentenciaWhile {$$ = new ParserVal( $1.obj);}
    		   | print {$$ = new ParserVal( $1.obj);}
		   | invocacionMetodo {$$ = new ParserVal( $1.obj);}
		   | referenciaClase {$$ = new ParserVal( $1.obj);}
;

sentenciaDeclarativa: declaracionFuncion {$$ = new ParserVal(null);}
			| declaracion ',' {$$ = new ParserVal(null);}
			| declaracionClase {$$ = $1;}
;

sentenciaDeclarativaMetodo: declaracionFuncionLocal
			| declaracion ','
;


asignacion: ID '=' expresion {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");
			      var x = new Nodo("Asignacion", new Nodo(getVariableConAmbitoTS($1.sval), getTipoVariableConAmbitoTS($1.sval)), (Nodo) $3.obj);
			      x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));

			      if (generarMenosMenos())
                              {
				      $$ = new ParserVal( new Nodo("sentencias", x, menosMenos));
				      menosMenos = null;
                              }
                              else
                              {
                              	$$ = new ParserVal(x);
                              }
			      variables_no_asignadas.remove($1.sval + Parser.ambito);}
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

condicion: expresion comparador expresion { var x = new Nodo($2.sval, (Nodo) $1.obj, (Nodo) $3.obj, null);
					    x.setTipo(validarTipos(x, (Nodo) $1.obj, (Nodo) $3.obj));

					    if (generarMenosMenos())
                                            {
						    $$ = new ParserVal( new Nodo("sentencias", x, menosMenos));
						    menosMenos = null;
                                            }
                                            else
                                            	$$ = new ParserVal(x);}
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

return: RETURN {$$ = new ParserVal( new Nodo($1.sval, null));}
;

parametro: tipo ID {$$ = new ParserVal( new Nodo($2.sval, $1.sval));}
;

encabezadoMetodo: VOID ID '(' parametro ')' { System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");
                  					     var t = AnalizadorLexico.TS;
                  					     int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                                               if (clave != t.NO_ENCONTRADO)
                                                               	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	yyerror("El nombre del metodo " + $2.sval +  " ya fue utilizado en este ambito");
                                                                 else { //la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.
                                                                  	t.agregarAtributo(clave, "tipo", "void");
                                                                  	t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                   }
                                                               else {
                                                               	t.agregarSimbolo($2.sval + Parser.ambito);
                                                               	t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                               	t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                               }
                                                               $$ = new ParserVal(new Nodo("Encabezado", new Nodo($2.sval + Parser.ambito), (Nodo) $4.obj, "void"));
                                                               lista_funciones.add($2.sval + Parser.ambito.replace(':','_'));
                                                               agregarAmbito($2.sval);
                                                               parametro((Nodo) $4.obj);
                  					   }
                  		   | VOID ID '(' ID ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
                  		   | VOID ID '('')'	{ System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");
                  		   			  var t = AnalizadorLexico.TS;
                                                            int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                                            if (clave != t.NO_ENCONTRADO)
                                                            	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	yyerror("El nombre del metodo " + $2.sval +  " ya fue utilizado en este ambito");
                                                                  else { //la tabla de simbolos contiene el metodo pero no tiene el tipo asignado.
                                                                          t.agregarAtributo(clave, "tipo", "void");
                                                                          t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                  }
                                                            else {
                                                                  t.agregarSimbolo($2.sval + Parser.ambito);
                                                                  t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                                  t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                            }
                                                            $$ = new ParserVal(new Nodo("Encabezado", new Nodo($2.sval + Parser.ambito), null, "void"));
                                                            lista_funciones.add($2.sval + Parser.ambito.replace(':','_'));
                                                            agregarAmbito($2.sval);
                                                            }
;

declaracionMetodo: encabezadoMetodo '{' cuerpoMetodo '}' { System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Función");
                                                           $$ = new ParserVal(
                                                           new Nodo( "Funcion",
                                                                                    (Nodo) $1.obj ,
                                                                                    (Nodo) $3.obj ,
                                                                                    "void"));
                                                           chequeoAsignacionVariables();
                                                           salirAmbito();
                                                           funcLocales = 0;}
;

encabezadoFuncion: VOID ID '(' parametro ')' {
					     var t = AnalizadorLexico.TS;
					     int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                             if (clave != t.NO_ENCONTRADO)
                                             	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                	yyerror("El nombre de la funcion " + $2.sval +  " ya fue utilizado en este ambito");
                                                else { //la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.
                                                	t.agregarAtributo(clave, "tipo", "void");
                                                	t.agregarAtributo(clave, "uso", "nombre_funcion");
                                                 }
                                             else {
                                             	t.agregarSimbolo($2.sval + Parser.ambito);
                                             	t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                             	t.agregarAtributo(t.obtenerID(), "uso", "nombre_funcion");
                                             }
                                             $$ = new ParserVal(new Nodo("Encabezado", new Nodo($2.sval + Parser.ambito), (Nodo) $4.obj, "void"));
                                             lista_funciones.add($2.sval + Parser.ambito.replace(':','_'));
                                             agregarAmbito($2.sval);
                                             parametro((Nodo) $4.obj);
					   }
		   | VOID ID '(' ID ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
		   | VOID ID '('')'	{
		   			  var t = AnalizadorLexico.TS;
                                          int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                          if (clave != t.NO_ENCONTRADO)
                                          	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                	yyerror("El nombre de la funcion " + $2.sval +  " ya fue utilizado en este ambito");
                                                else { //la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.
                                                        t.agregarAtributo(clave, "tipo", "void");
                                                        t.agregarAtributo(clave, "uso", "nombre_funcion");
                                                }
                                          else {
                                                t.agregarSimbolo($2.sval + Parser.ambito);
                                                t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_funcion");
                                          }
                                          $$ = new ParserVal(new Nodo("Encabezado", new Nodo($2.sval + Parser.ambito), null, "void"));
                                          lista_funciones.add($2.sval + Parser.ambito.replace(':','_'));
                                          agregarAmbito($2.sval);
                                          }
;

declaracionFuncion: encabezadoFuncion '{' cuerpoFuncion '}' { System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Función");
                                                            $$ = new ParserVal(
                                                            new Nodo( "Funcion",
                                                                         	    (Nodo) $1.obj ,
                                                                                    (Nodo) $2.obj ,
                                                                                    		    "void"));

                                                            /* Acciones de desapilar */
                                                            if (!verificarRetornoArbol((Nodo) $3.obj))
                                                            	yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                            chequeoAsignacionVariables();
                                                            salirAmbito();}
;

declaracionFuncionLocal: encabezadoFuncion '{' cuerpoFuncion '}' { System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Función");
								var x = (Nodo) $1.obj;
								String nombre = x.getIzq().getNombre();
								funcLocales += 1;
                                                            	if (funcLocales <= 1){
                                                            		$$ = new ParserVal(
                                                                	new Nodo( "Funcion",
                                                                         	    (Nodo) $1.obj ,
                                                                                    (Nodo) $2.obj ,
                                                                                    		    "void"));

                                                                	/* Acciones de desapilar */
                                                                	if (!verificarRetornoArbol((Nodo) $3.obj))
                                                                		yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                                	chequeoAsignacionVariables();
                                                                	salirAmbito();
                                                            	}
                                                            	else
                                                            		yyerror("En la función: '"+nombre+"' el nivel maximo de anidamiento (1) es superado.");}
;

cuerpoMetodo: listaSentenciasMetodo { $$ = $1; }
;

cuerpoFuncion:  listaSentenciasFuncion { $$ = $1; }
;

listaSentenciasMetodo: listaSentenciasMetodo sentenciaDeclarativaMetodo
                       	| listaSentenciasMetodo sentenciaEjecutable ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj));}
                       	| sentenciaDeclarativaMetodo
                       	| sentenciaEjecutable ',' { $$ = new ParserVal( new Nodo("sentencias", null, (Nodo) $1.obj));}
;

listaSentenciasFuncion: listaSentenciasFuncion sentenciaDeclarativaMetodo
                       	| listaSentenciasFuncion sentencia_ejecutable_return ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj));}
                       	| sentenciaDeclarativaMetodo
                       	| sentencia_ejecutable_return ',' { $$ = new ParserVal( new Nodo("sentencias", null, (Nodo) $1.obj));}
;

invocacionMetodo: ID '(' expresion ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
					var x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS($1.sval)), null, "void");
					if (generarMenosMenos())
                                        {
						$$ = new ParserVal( new Nodo("sentencias", x, menosMenos));
						menosMenos = null;
                                        }
                                        else
                                        {
                                        	$$ = new ParserVal(x);
                                        }
					chequearLlamadoFuncion($1.sval);
					}
		  | ID '(' ')' {System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
		  		var x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS($1.sval)), null, "void");
		  		$$ = new ParserVal(x);
		  		chequearLlamadoFuncion($1.sval);
		  		}
		  | ID '(' asignacion ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
		  | ID '(' tipo asignacion ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
;

declaracion: tipo listaDeclaracion
	         {
                     var t = AnalizadorLexico.TS;
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


listaDeclaracion: ID ';' listaDeclaracion {  lista_variables.add($1.sval + Parser.ambito);
 					     variables_no_asignadas.add($1.sval + Parser.ambito);
 					  }
		| ID { lista_variables.add($1.sval + Parser.ambito);
		       variables_no_asignadas.add($1.sval + Parser.ambito);
		     }
;

tipo: SHORT  { $$ = $1; }
    	| ULONG { $$ = $1; }
	| FLOAT { $$ = $1; }
    	| ID { $$ = $1; }
;

expresion: termino { $$ = $1;}
    	| expresion '+' termino { var x = new Nodo("+", (Nodo) $1.obj, (Nodo)  $3.obj, null);
                                  x.setTipo(validarTipos(x, (Nodo) $1.obj, (Nodo) $3.obj));
                                  $$ = new ParserVal(x);}
    	| expresion '-' termino { var x = new Nodo("-", (Nodo) $1.obj, (Nodo)  $3.obj, null);
    				  x.setTipo(validarTipos(x, (Nodo) $1.obj, (Nodo) $3.obj));
    				  $$ = new ParserVal(x);}
;

termino: factor { $$ = $1; }
    	| termino '*' factor { var x = new Nodo("*", (Nodo) $1.obj, (Nodo)  $3.obj);
    			       x.setTipo(validarTipos(x, (Nodo) $1.obj, (Nodo) $3.obj));
    			       $$ = new ParserVal(x);}
    	| termino '/' factor { var x = new Nodo("/", (Nodo) $1.obj, (Nodo)  $3.obj);
                               x.setTipo(validarTipos(x, (Nodo) $1.obj, (Nodo) $3.obj));
                               $$ = new ParserVal(x);}
;

factor: ID {String x = getTipoVariableConAmbitoTS($1.sval);
            if (x != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
            	$$ =  new ParserVal( new Nodo(getVariableConAmbitoTS($1.sval), x));
            else{
            	$$ =  new ParserVal( new Nodo("variableNoEncontrada", x));
                yyerror("No se encontro esta variable en un ambito adecuado");
                }
            }
	| ID MENOSMENOS {String x = getTipoVariableConAmbitoTS($1.sval);
                         if (x != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
                         	$$ =  new ParserVal( new Nodo(getVariableConAmbitoTS($1.sval), x));
                         else{
                                $$ =  new ParserVal( new Nodo("variableNoEncontrada", x));
                                yyerror("No se encontro esta variable en un ambito adecuado");
                         }
                         menosMenos = (Nodo) $$.obj;
        }
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
private static Nodo menosMenos = null;
public static String ambito = ":main";
public static String variableAmbitoClase = ":main";
public static String ambitoClase = ":main";
public static final String ERROR_LEXICO = "Error_lexico";
public static final String ERROR_SINTACTICO = "Error_sintactico";
private static int funcLocales = 0;
public static String claseActual = "";
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();
public static ArrayList<String> lista_variables = new ArrayList<>();
public static ArrayList<String> variables_no_asignadas = new ArrayList<>();
public static ArrayList<String> lista_funciones = new ArrayList<>();
public static ArrayList<String> lista_clases = new ArrayList<>();
public static ArrayList<String> lista_clases_fd = new ArrayList<>();
public static OutputManager out_arbol = new OutputManager("./Arbol.txt");

public void setYylval(ParserVal yylval) {
	this.yylval = yylval;
}

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

public String getTipo(String lexema){
	String tipo = getTipoTS(lexema);
	if (tipo.equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE))
    		yyerror("La variable no esta declarada");
  	return tipo;
}

public Boolean generarMenosMenos()
{
	if (menosMenos != null)
	{
        	String tipo = menosMenos.getTipo();
                String uno = "";
                switch(tipo){
        		case("FLOAT"):
        			uno = "1.0";
        			break;
        		case("SHORT"):
        			uno = "1_s";
        			break;
        		case("LONG"):
        			uno = "1_ul";
        			break;
        		default:
        			yyerror("La variable no tiene tipo");
        			break;
                }
                var y = new Nodo(uno, null, null, tipo);
                var z = new Nodo("-", menosMenos, y, tipo);
                var w = new Nodo("Asignacion", menosMenos, z, tipo);
                menosMenos = w;
                return true;
	}
	return false;
}

public void parametro(Nodo n){
	var t = AnalizadorLexico.TS;
    	var nombre = "" ;
    	int clave_funcion = t.obtenerSimbolo(Parser.ambito.substring(1));

    	String nombre_parametro = n.getNombre() + Parser.ambito;
      	n.setNombre(nombre_parametro);
      	t.agregarSimbolo(n.getNombre());
      	t.agregarAtributo(t.obtenerID(), "tipo", n.getTipo());
      	t.agregarAtributo(t.obtenerID(), "uso", "parametro");
      	t.agregarAtributo(clave_funcion, "parametro", nombre_parametro);
}


private String validarTipos(Nodo x, Nodo obj, Nodo obj1) {
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
    		if (obj.getTipo().equals("FLOAT") || obj1.getTipo().equals("FLOAT"))
    		{
    			if (obj.getTipo().equals("FLOAT"))
    			{
    				if (obj1.getTipo().equals("SHORT"))
    				{
    					var conv = new Nodo("STOF", obj1, null);
    					x.setDer(conv);
    				}else
    				{
    					if (obj1.getTipo().equals("LONG"))
    					{
    						var conv = new Nodo("LTOF", obj1, null);
    						x.setDer(conv);
    					}
    				}
    			}
    			else{
    				if (obj.getTipo().equals("SHORT"))
                            	{
                            		var conv = new Nodo("STOF", obj, null);
                            		x.setIzq(conv);
                            	}else
                            	{
                            		if (obj.getTipo().equals("LONG"))
                            		{
                            			var conv = new Nodo("LTOF", obj, null);
                            			x.setIzq(conv);
                            		}
                            	}
    			}
    			return "FLOAT";
    		}
    		else{
    			yyerror("Incompatibilidad de tipos");
        		return "Error";
        	}
    	}
    	else return obj.getTipo();
}

private String validarTiposAssign(Nodo x, Nodo izq, Nodo der) {
	if (izq == null )
        	return "obj is null";
        if (der == null)
          	return "obj1 is null";
        if (izq.getTipo() == "null")
          	return "obj type is null";
        if (der.getTipo() == "null")
          	return "obj1 type is null";


    	if (izq.getTipo() == "Error" || (der.getTipo() == "Error")){
       		return "Error";
    	}

    	if (!izq.getTipo().equals(der.getTipo())) {
    		//EL OPERANDO DE LA IZQUIERDA DEBE SER FLOAT, SINO ERROR
    		if (izq.getTipo().equals("FLOAT"))
    		{
    			if (der.getTipo().equals("SHORT"))
    			{
    				var conv = new Nodo("STOF", der, null);
    				x.setDer(conv);
    			}
    			else {
    				if (der.getTipo().equals("LONG"))
    				{
    					var conv = new Nodo("LTOF", der, null);
                                	x.setDer(conv);
    				}
    			}
    			return "FLOAT";
    		}
    		else{
    			yyerror("Incompatibilidad de tipos en la asignación.");
        		return "Error";
        	}
    	}
    	else return izq.getTipo();
}

private boolean verificarRetornoArbol( Nodo n ){
	if (n == null)
    		return false;
  	switch (n.getNombre()) {
    		case "if":
    		case "for":
      			return verificarRetornoArbol(n.getDer());
    		case "Return":
      			return true;
    		case "Funcion":
   		case "Print":
    		case "Asignacion":
      			return false; //Se deben ignorar las funciones anidadas
    		case "cuerpoIf":
      			return verificarRetornoArbol(n.getIzq()) && verificarRetornoArbol(n.getDer());
    		default:
      		return verificarRetornoArbol(n.getIzq()) || verificarRetornoArbol(n.getDer());
  	}
}

private String getTipoTS(String lexema)
{
	int clave = AnalizadorLexico.TS.obtenerSimbolo(lexema);
	return AnalizadorLexico.TS.obtenerAtributo(clave, "tipo");
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

public String ultimoAmbito()
{
	String a = Parser.ambito.substring(1);  //ELIMINAMOS ; DEL PRINCIPIO
	a = a.substring(0, a.indexOf(":")-1);
	return a;
}

private String getVariableConAmbitoTS(String sval) {
	String ambito_actual = getAmbito(sval);

  	if (getTipoTS(sval + ambito_actual) == TablaSimbolos.NO_ENCONTRADO_MESSAGE)
    		yyerror("La variable: '" + sval + "' no fue encontrada en un ambito permitido.");

  	return (sval+ambito_actual);
}

private String getTipoVariableConAmbitoTS(String sval) {
  	String ambito_actual = getAmbito(sval);

  	if (getTipoTS(sval + ambito_actual).equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE) && !falloNombre(sval) )
    		yyerror("No se encontro el tipo para esta variable en un ambito valido");

	//RETORNAMOS EL TIPO DE LA VARIABLE
  	return getTipoTS(sval+ ambito_actual);
}

private Boolean falloNombre(String sval){
	//QUE PASA SI BORRAMOS ESTA FUNCION? NO ENTIENDO QUE FUNCIONAMIENTO TIENE
	//REVISAR

     	String ambito_actual = getAmbito(sval);
      	return (getTipoTS(sval + ambito_actual).equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE));
}

private String getAmbito(String nombreVar){
    	String ambito_actual = Parser.ambito;
    	while(!ambito_actual.isBlank() && getTipoTS(nombreVar + ambito_actual).equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE)){
        	ambito_actual = ambito_actual.substring(1);
        	if (ambito_actual.contains(":"))
        	{
            		ambito_actual = ambito_actual.substring(ambito_actual.indexOf(':'));
            		System.out.println(ambito_actual);
                }
        	else
            		ambito_actual = "";
      		}
    	return ambito_actual;
  }

public void chequeoAsignacionVariables()
{
	 for (String variable : variables_no_asignadas) {
         	yyerror("La variable: '"+variable+ "' no fue utilizada en el ambito donde se declaró.");
         }
         variables_no_asignadas.clear();
}

//CHEQUEO DE LLAMADO A FUNCION SIN PARAMETROS
private void chequearLlamadoFuncion(String funcion) {
  	var t = AnalizadorLexico.TS;
  	int entrada = t.obtenerSimbolo(funcion + getAmbito(funcion));

	//SI LA FUNCION TIENE UN PARAMETRO, SE NOTIFICA ERROR
  	if (!t.obtenerAtributo(entrada, "parametro").equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE))
    		yyerror("La funcion a la que se desea llamar tiene parametro");
}

private void chequearHerenciaVariable(String variableConAmbito)
{
	var t = AnalizadorLexico.TS;
	int entrada = t.obtenerSimbolo(variableConAmbito);
	if (entrada == TablaSimbolos.NO_ENCONTRADO)
        	yyerror("La clase a la que se desea heredar no fue declarada.");
        else{
        	String x = t.obtenerAtributo(entrada, "herencia");
        	if (!x.equals(t.NO_ENCONTRADO_MESSAGE) && t.obtenerAtributo(entrada, "tipo").equals("void"))
        		yyerror("No se puede sobreescribir un atributo heredado.");
        }
}

private void chequearNivelesHerencia(String clase_actual)
{
	if (!clase_actual.equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE))
	{
		var t = AnalizadorLexico.TS;
		int entrada = t.obtenerSimbolo(clase_actual);
		String heredada = t.obtenerAtributo(entrada, "clase_heredada");
		if (!heredada.equals(t.NO_ENCONTRADO_MESSAGE))
		{
			int clave_heredada = t.obtenerSimbolo(heredada);
			String niveles = t.obtenerAtributo(clave_heredada, "niveles_herencia");
			int c = Integer.parseInt(niveles) + 1;
			if (c < 3)
			{
				t.modificarAtributo(clave_heredada, "niveles_herencia", String.valueOf(c));
				chequearNivelesHerencia(heredada);
			}
			else
				yyerror("Se excedió el limite maximo de niveles de herencia.");
		}
	}
}


public static String comprobarRango(String valor){
    int id = TablaSimbolos.obtenerSimbolo(valor);
    String tipo = TablaSimbolos.obtenerAtributo(id, "tipo");
    String valor_final = "";
    int referencias = Integer.parseInt(TablaSimbolos.obtenerAtributo(id, "referencias"));
    TablaSimbolos.modificarAtributo(id, "referencias", Integer.toString(referencias-1));
    boolean agregado;
    if (tipo.equals("SHORT")){
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

	if (tipo.equals("FLOAT")){
		valor_final = "-"+valor;
        	agregado = TablaSimbolos.agregarSimbolo(valor_final);
        	modificar_referencias(agregado, valor_final, "float");
	}

    if (tipo.equals("LONG")){
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
                ArbolSintactico as = new ArbolSintactico(Parser.raiz);
                as.print(Parser.out_arbol);
        } else {
                System.out.println("No se especifico el archivo a compilar");
        }
}