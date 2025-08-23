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
				  chequeoAsignacionVariables();
				  out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");}
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
		| sentenciaEjecutable {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la coma ',' al final de la sentencia.");}
;

encabezadoClase: CLASS ID {var t = AnalizadorLexico.TS;
                           int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                           if (clave != t.NO_ENCONTRADO)
                           	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                	yyerror("El nombre de la clase " + $2.sval +  " ya fue utilizado en este ambito");
                                else { //la tabla de simbolos contiene la clase pero no tiene el uso asignado.
                                        t.agregarAtributo(clave, "uso", "nombre_clase");
                                        t.agregarAtributo(clave, "tipo", $2.sval);
                                }
                           else {
                                t.agregarSimbolo($2.sval + Parser.ambito);
                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_clase");
                                t.agregarAtributo(t.obtenerID(), "tipo", $2.sval);
                           }
                           $$ = new ParserVal(new Nodo("EncabezadoClase", new Nodo($2.sval + Parser.ambito), null));
                           lista_clases.add($2.sval + Parser.ambito.replace(':','_'));
                           claseActual = $2.sval;
                           ambitoClase = Parser.ambito;
                           cantHerencias = 0;
                           agregarAmbito(claseActual);
                          }
;

encabezadoForward: CLASS ID {lista_clases_fd.add($2.sval+Parser.ambito);
                            claseActual = "";
                            ambitoClase = "";
                            out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Forward declaración de clase.");}
;

declaracionClase: encabezadoClase '{' cuerpoClase '}'  {lista_clases_fd.remove(Parser.ambito.substring(1));
                                                        salirAmbito();
                                                        claseActual = "";
                                                        out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");
							$$ = new ParserVal( new Nodo("Clase", (Nodo) $1.obj, (Nodo) $3.obj) );}
		 | encabezadoForward ','
		 | encabezadoClase {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
;

cuerpoClase: cuerpoClase seccionAtributos {$$ = $1;}
	     | cuerpoClase declaracionMetodo ',' { $$ = new ParserVal( new Nodo("cuerpoClase", (Nodo) $1.obj, (Nodo) $2.obj));}
	     | declaracionMetodo ','  { $$ = $1; }
	     | seccionAtributos
;


referenciaClase: listaReferencia asignacionClase {$$ = $2; variableAmbitoClase = "";}
		 | listaReferencia referenciaMetodo {$$ = $2; variableAmbitoClase = "";}
;

asignacionClase: ID '=' expresion {
				   var t = AnalizadorLexico.TS;
                                   variableAmbitoClase = $1.sval + variableAmbitoClase;
                                   String claseBase;
                                   if (claseActual.equals(""))
                                   	claseBase = variableAmbitoClase.substring(variableAmbitoClase.lastIndexOf(":")+1);
                                   else
                                   {
                                   	claseBase = claseActual;
                                   	variableAmbitoClase = variableAmbitoClase + ":" + claseBase;
                                   }
                                   if (!chequearReferencia($1.sval, claseBase, variableAmbitoClase+":main"))
                                   	yyerror("No existe el atributo en la clase a la que se intenta hacer referencia.");
                                   String nombre_nodo = $1.sval+":"+claseBase+":main";
                                   var x = new Nodo("Asignacion", new Nodo(nombre_nodo, t.obtenerAtributo(t.obtenerSimbolo(nombre_nodo), "tipo")), (Nodo) $3.obj);
                                   x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));
                                   out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación a atributo de clase");
                                   $$ = new ParserVal(x);
				  }
;

referenciaMetodo: ID '(' ')' {
			      out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Llamado a metodo de clase");
                              var t = AnalizadorLexico.TS;
                              variableAmbitoClase = $1.sval + variableAmbitoClase;
                              String claseBase = variableAmbitoClase.substring(variableAmbitoClase.lastIndexOf(":")+1);
                              int clave = t.obtenerSimbolo($1.sval+":"+claseBase+":main");
                              Nodo x;
                              x = new Nodo("LlamadoMetodo", new Nodo(t.obtenerAtributo(clave, "ref"), null, null, "void"), null);
                              if(chequearMetodoClase(claseBase, $1.sval, variableAmbitoClase+":main",x))
                              {
                              	    if (tieneParametrosMetodo(claseBase, $1.sval))
                              	    	  yyerror("El metodo al que desea llamar requiere parametros");
                              }
                              else
                              	    yyerror("No existe el metodo al que se intenta invocar en la clase.");
                              $$ = new ParserVal(x);
                              }
                 | ID '(' expresion ')' {
                                        out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Llamado a metodo de clase");
                                        var t = AnalizadorLexico.TS;
                                        variableAmbitoClase = $1.sval + variableAmbitoClase;
                                        String claseBase = variableAmbitoClase.substring(variableAmbitoClase.lastIndexOf(":")+1);
                                        int clave = t.obtenerSimbolo($1.sval+":"+claseBase+":main");
                                        Nodo x;
                                        x = new Nodo("LlamadoMetodo", new Nodo(t.obtenerAtributo(clave, "ref")), (Nodo) $3.obj, "void");
                                        if(chequearMetodoClase(claseBase, $1.sval, variableAmbitoClase+":main",x))
                                        {
                                        	if (!tieneParametrosMetodo(claseBase, $1.sval))
                                                	yyerror("El metodo al que desea llamar no acepta parametros");
                                                else
                                                {
                                                        if (!validarParametro(x))
                                                        	yyerror("Incompatibilidad de tipos en llamado a metodo");
                                                }
                                        }
                                        else
                                                yyerror("No existe el metodo al que se intenta invocar en la clase.");
                                        $$ = new ParserVal(x);
                                        }
;

listaReferencia: listaReferencia ID '.' { var t = AnalizadorLexico.TS;
                                          int clave = t.obtenerSimbolo($2.sval+":main");
                                          String tipo = t.obtenerAtributo(clave, "tipo");
                                          if (tipo != TablaSimbolos.NO_ENCONTRADO_MESSAGE && !tipo.equals("variable"))
                                          	variableAmbitoClase = ":"+ tipo + variableAmbitoClase;
                                          else
                                        	variableAmbitoClase = ":"+ $2.sval + variableAmbitoClase;
                                         }
		| ID '.' { var t = AnalizadorLexico.TS;
			   int clave = t.obtenerSimbolo($1.sval+":main");
			   String tipo = t.obtenerAtributo(clave, "tipo");
			   if (tipo != TablaSimbolos.NO_ENCONTRADO_MESSAGE && !tipo.equals("variable"))
			   	variableAmbitoClase = ":"+ tipo + variableAmbitoClase;
			   else
			   	variableAmbitoClase = ":"+ $1.sval + variableAmbitoClase;
			   }
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
                                                         	t.modificarAtributo(clave, "lexema", x);
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

herenciaNombre: ID ',' { if (lista_clases_fd.contains($1.sval+":main"))
				yyerror("La clase aun no fue declarada (forward declaration)");
			 else{
			 	 cantHerencias = cantHerencias+1;
			 	 if (cantHerencias > 1)
			 	 {
			 	 	yyerror("Una clase no puede heredar de más de una clase");
			 	 }
			 	 else
			 	 {
					 var t = AnalizadorLexico.TS;
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
					 herencia = true;

					 if (!aplicarHerencia($1.sval, claseActual))
						yyerror("No se puede sobreescribir atributos de clase.");
					 chequearNivelesHerencia(Parser.ambito.substring(1));
				}
			 }
 		       }
;

sentenciaEjecutable: asignacion {$$ = new ParserVal( $1.obj);}
    		   | sentenciaIf {$$ = new ParserVal( $1.obj);}
    		   | sentenciaWhile {$$ = new ParserVal( $1.obj);}
    		   | print {$$ = new ParserVal( $1.obj);}
		   | invocacionMetodo {$$ = new ParserVal( $1.obj);}
		   | referenciaClase {$$ = new ParserVal( $1.obj);}
;

sentenciaDeclarativa: declaracionFuncion {$$ = new ParserVal($1.obj);}
			| declaracion ',' {$$ = new ParserVal(null);}
			| declaracionClase {$$ = new ParserVal($1.obj);}
			| declaracion {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la coma ',' al final de la declaracion.");}
;

sentenciaDeclarativaMetodo: declaracionFuncionLocal
			| declaracion ','
;


asignacion: ID '=' expresion {
			      out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");
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

sentenciaIf: IF '(' condicion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
												$$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $6.obj, null), new Nodo("else", (Nodo) $10.obj, null)))); }
			| IF '(' condicion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' '{' bloque_ejecucion '}' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										$$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $6.obj, null), new Nodo("else", null, null))));
										}
			| IF '(' condicion ')' '{' bloque_ejecucion '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' sentenciaEjecutable ',' ELSE sentenciaEjecutable ',' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", (Nodo) $8.obj, null))));
													   }
			| IF '(' condicion ')' sentenciaEjecutable ',' ELSE '{' bloque_ejecucion '}' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", (Nodo) $9.obj, null))));
													    }
			| IF '(' condicion ')' sentenciaEjecutable ',' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
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

sentenciaIfRetorno: IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return '}' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
														          $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $6.obj, null), new Nodo("else", (Nodo) $10.obj, null))));
														         }
			| IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' '{' bloque_ejecucion_return '}' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										       $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $6.obj, null), new Nodo("else", null, null))));
										      }
			| IF '(' condicion ')' '{' bloque_ejecucion_return '}' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
			| IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE sentencia_ejecutable_return ',' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
															    $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", (Nodo) $8.obj, null))));
															   }
			| IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE '{' bloque_ejecucion_return '}' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
															        $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", (Nodo) $9.obj, null))));
															       }
			| IF '(' condicion ')' sentencia_ejecutable_return ',' END_IF {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										       $$ = new ParserVal( new Nodo("if", (Nodo) $3.obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) $5.obj, null), new Nodo("else", null, null))));
										      }
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

sentenciaWhile: WHILE '(' condicion ')' DO '{' bloque_ejecucion '}' {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
								     $$ = new ParserVal( new Nodo("while", (Nodo) $3.obj, (Nodo) $7.obj));
								    }
               | WHILE '(' condicion ')' DO sentenciaEjecutable {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
               							 $$ = new ParserVal( new Nodo("while", (Nodo) $3.obj, (Nodo) $6.obj));
               							}
;

print: PRINT CADENA {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena.");
		     var t = AnalizadorLexico.TS;
		     int clave = t.obtenerSimbolo($2.sval);
		     t.agregarAtributo(clave, "cadena", $2.sval);
		     String nombreNodo = "Cadena"+AnalizadorLexico.getIdCadena();
		     t.modificarAtributo(clave, "lexema", nombreNodo);
		     var x = new Nodo(nombreNodo, null, null, "STRING");
		     $$ = new ParserVal( new Nodo("Print", x, null, "STRING"));}
       | CADENA {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
;

return: RETURN {$$ = new ParserVal( new Nodo($1.sval));}
;

parametro: tipo ID {$$ = new ParserVal( new Nodo($2.sval, $1.sval));}
;

encabezadoMetodo: VOID ID '(' parametro ')' {		     funcLocales = 0;
                  					     var t = AnalizadorLexico.TS;
                  					     int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                                               if (clave != t.NO_ENCONTRADO)
                                                               	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	t.modificarAtributo(clave, "ref", $2.sval + Parser.ambito);
                                                                 else { //la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.
                                                                  	t.agregarAtributo(clave, "tipo", "void");
                                                                  	t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                  	t.agregarAtributo(clave, "ref", $2.sval + Parser.ambito);
                                                                   }
                                                               else {
                                                                t.agregarSimbolo($2.sval + Parser.ambito);
                                                                t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                                t.agregarAtributo(t.obtenerID(), "ref", $2.sval + Parser.ambito);
                                                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                               }
                                                               $$ = new ParserVal(new Nodo("Encabezado", new Nodo($2.sval + Parser.ambito), (Nodo) $4.obj, "void"));
                                                               lista_funciones.add($2.sval + Parser.ambito.replace(':','_'));
                                                               agregarAmbito($2.sval);
                                                               parametro((Nodo) $4.obj);
                  					   }
                  		   | VOID ID '(' ID ')' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
                  		   | VOID ID '('')'	{ funcLocales = 0;
                  		   			  var t = AnalizadorLexico.TS;
                                                            int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                                            if (clave != t.NO_ENCONTRADO)
                                                            	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	t.modificarAtributo(clave, "ref", $2.sval + Parser.ambito);
                                                                  else { //la tabla de simbolos contiene el metodo pero no tiene el tipo asignado.
                                                                          t.agregarAtributo(clave, "tipo", "void");
                                                                          t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                          t.agregarAtributo(clave, "ref", $2.sval + Parser.ambito);
                                                                  }
                                                            else {
                                                                  t.agregarSimbolo($2.sval + Parser.ambito);
                                                                  t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                                  t.agregarAtributo(t.obtenerID(), "ref", $2.sval + Parser.ambito);
                                                                  t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                            }
                                                            $$ = new ParserVal(new Nodo("Encabezado", new Nodo($2.sval + Parser.ambito), null, "void"));
                                                            lista_funciones.add($2.sval + Parser.ambito.replace(':','_'));
                                                            agregarAmbito($2.sval);
                                                            }
;

declaracionMetodo: encabezadoMetodo '{' cuerpoMetodo '}' { out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de metodo.");
                                                           $$ = new ParserVal(
                                                           new Nodo( "MetodoClase",
                                                                                    (Nodo) $1.obj ,
                                                                                    (Nodo) $3.obj ,
                                                                                    "void"));
                                                           salirAmbito();}
;

encabezadoFuncion: VOID ID '(' parametro ')' {
					     var t = AnalizadorLexico.TS;
					     int clave = t.obtenerSimbolo($2.sval + Parser.ambito);
                                             if (clave != t.NO_ENCONTRADO)
                                             	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                	yyerror("El nombre de la funcion " + $2.sval +  " ya fue utilizado en este ambito");
                                                else { //la tabla de simbolos contiene la funcion pero no tiene el uso asignado.
                                                	t.agregarAtributo(clave, "tipo", "void");
                                                	t.agregarAtributo(clave, "uso", "nombre_funcion");
                                                	t.agregarAtributo(clave, "ref", $2.sval+Parser.ambito);
                                                 }
                                             else {
                                             	t.agregarSimbolo($2.sval + Parser.ambito);
                                             	t.agregarAtributo(t.obtenerID(), "ref", $2.sval+Parser.ambito);
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

declaracionFuncion: encabezadoFuncion '{' cuerpoFuncion '}' { out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de función VOID.");
                                                            $$ = new ParserVal(
                                                            new Nodo( "Funcion",
                                                                         	    (Nodo) $1.obj ,
                                                                                    (Nodo) $3.obj ,
                                                                                    		    "void"));

                                                            /* Acciones de desapilar */
                                                            if (!verificarRetornoArbol((Nodo) $3.obj))
                                                            	yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                            salirAmbito();}
;

declaracionFuncionLocal: encabezadoFuncion '{' cuerpoFuncion '}' { out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de función VOID local a metodo.");
								var x = (Nodo) $1.obj;
								String nombre = x.getIzq().getNombre();
								funcLocales += 1;
                                                            	if (funcLocales <= 1){
                                                            		$$ = new ParserVal(
                                                                	new Nodo( "Funcion",
                                                                         	    (Nodo) $1.obj ,
                                                                                    (Nodo) $3.obj ,
                                                                                    		    "void"));

                                                                	/* Acciones de desapilar */
                                                                	if (!verificarRetornoArbol((Nodo) $3.obj))
                                                                		yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                                	salirAmbito();
                                                            	}
                                                            	else
                                                            	{
                                                            		yyerror("En la función: '"+nombre+"' el nivel maximo de anidamiento (1) es superado.");
                                                            		salirAmbito();
                                                            	}}
;

cuerpoMetodo: listaSentenciasMetodo { $$ = $1; }
;

cuerpoFuncion:  listaSentenciasFuncion { $$ = $1; }
;

listaSentenciasMetodo: listaSentenciasMetodo sentenciaDeclarativaMetodo {$$ = $1;}
                       	| listaSentenciasMetodo sentenciaEjecutable ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj));}
                       	| sentenciaDeclarativaMetodo
                       	| sentenciaEjecutable ',' { $$ = $1; }
;

listaSentenciasFuncion: listaSentenciasFuncion sentenciaDeclarativaMetodo {$$ = $1;}
                       	| listaSentenciasFuncion sentencia_ejecutable_return ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, (Nodo) $2.obj));}
                       	| sentenciaDeclarativaMetodo
                       	| sentencia_ejecutable_return ',' { $$ = new ParserVal( new Nodo("sentencias", (Nodo) $1.obj, null));}
;

invocacionMetodo: ID '(' expresion ')' {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a funcion VOID.");
					String fun = getTipoVariableConAmbitoTS($1.sval);
                                        Nodo x;
                                        if (fun != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
                                        {
						x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS($1.sval)), (Nodo) $3.obj, "void");
						if (generarMenosMenos())
                                                {
                                                	$$ = new ParserVal( new Nodo("sentencias", x, menosMenos));
                                                	menosMenos = null;
                                                }
                                                else
                                                {
                                                        $$ = new ParserVal(x);
                                                }

                                                if (!chequearLlamadoFuncion($1.sval))
                                                	yyerror("La funcion a la que se desea llamar no acepta parametros.");
                                                else
                                                	if (!validarParametro(x))
                                                        	yyerror("Incompatibilidad de tipos en llamado a función");
                                        }
                                        else{
                                                x = new Nodo("LlamadaFuncion", new Nodo("Funcion no encontrada"), null, "void");
                                                yyerror("No se encontro la función en un ambito adecuado");
                                        }
					}
		  | ID '(' ')' {out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a funcion VOID.");
		  		String fun = getTipoVariableConAmbitoTS($1.sval);
		  		Nodo x;
                                if (fun != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
                                {
                                	x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS($1.sval)), null, "void");
                                	if (chequearLlamadoFuncion($1.sval))
                                        	yyerror("La funcion a la que se desea llamar tiene parametro.");
                                }
                                else{
                                	x = new Nodo("LlamadaFuncion", new Nodo("Funcion no encontrada"), null, "void");
                                        yyerror("No se encontro la función en un ambito adecuado");
                                }

		  		$$ = new ParserVal(x);
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
                                 	if (!instanciaClase)
                                 		t.agregarAtributo(clave, "uso", "variable");
                                 	else
                                 		t.agregarAtributo(clave, "uso", "instanciaClase");
                                 	t.agregarAtributo(clave, "tipo", $1.sval);
                                 	t.modificarAtributo(clave, "lexema", x);
                                 } else {
                                   	yyerror("La variable declarada ya existe " + (x.contains(":") ? x.substring(0, x.indexOf(':')) : "en ambito global"));
                               	 }
                               } else {
                               		t.agregarSimbolo(x);
                               		t.agregarAtributo(t.obtenerID(), "tipo", $1.sval);
                               		if (!instanciaClase)
                                        	t.agregarAtributo(t.obtenerID(), "uso", "variable");
                                        else
                                                t.agregarAtributo(t.obtenerID(), "uso", "instanciaClase");
                               }
                             });
                         lista_variables.clear();
                         instanciaClase = false;
             	}
;


listaDeclaracion: ID ';' listaDeclaracion {  lista_variables.add($1.sval + Parser.ambito);
					     if (!instanciaClase)
					     {
 					     	variables_no_asignadas.add($1.sval + Parser.ambito);
 					     }
 					  }
		| ID { lista_variables.add($1.sval + Parser.ambito);
		       if (!instanciaClase)
		       {
                       	   variables_no_asignadas.add($1.sval + Parser.ambito);
                       }
		     }
;

tipo: SHORT  { $$ = $1; }
    	| ULONG { $$ = $1; }
	| FLOAT { $$ = $1; }
    	| ID { $$ = $1;
    	        instanciaClase = true;}
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
	    | '>'  { $$ = new ParserVal(">"); }
	    | '=' {anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
;

%%

public static Nodo raiz = null;
private static Nodo menosMenos = null;
public static String ambito = ":main";
public static String variableAmbitoClase = "";
public static String ambitoClase = ":main";
public static final String ERROR_LEXICO = "Error_lexico";
public static final String ERROR_SINTACTICO = "Error_sintactico";
public static final String WARNING = "Warning";
private static int funcLocales = 0;
private static int cantHerencias = 0;
public static String claseActual = "";
private static Boolean herencia = false;
private static Boolean instanciaClase = false;
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();
public static List<String> errorSemantico = new ArrayList<>();
public static List<String> warnings = new ArrayList<>();
public static ArrayList<String> lista_variables = new ArrayList<>();
public static ArrayList<String> variables_no_asignadas = new ArrayList<>();
public static ArrayList<String> lista_funciones = new ArrayList<>();
public static ArrayList<String> lista_clases = new ArrayList<>();
public static ArrayList<String> lista_clases_fd = new ArrayList<>();
public static OutputManager out_arbol = new OutputManager("./Arbol.txt");
public static OutputManager out_estructura = new OutputManager("./Estructura.txt");
public static OutputManager out_errores = new OutputManager("./Errores.txt");
public static OutputManager out_tabla = new OutputManager("./Tabla_Simbolos.txt");

public void setYylval(ParserVal yylval) {
	this.yylval = yylval;
}

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        String error = "LINEA: "+AnalizadorLexico.getCantLineas()+" ERROR SEMANTICO! "+mensaje;
        errorSemantico.add(error);
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
        		case("ULONG"):
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
    					if (obj1.getTipo().equals("ULONG"))
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
                            		if (obj.getTipo().equals("ULONG"))
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
    				if (der.getTipo().equals("ULONG"))
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
    		case "while":
      			return verificarRetornoArbol(n.getDer());
    		case "RETURN":
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
private Boolean chequearLlamadoFuncion(String funcion) {
  	var t = AnalizadorLexico.TS;
  	int entrada = t.obtenerSimbolo(funcion + getAmbito(funcion));

	//SI LA FUNCION TIENE UN PARAMETRO, RETORNA TRUE
  	if (!t.obtenerAtributo(entrada, "parametro").equals(TablaSimbolos.NO_ENCONTRADO_MESSAGE))
  		return true;
  	return false;
}

private Boolean chequearMetodoClase(String claseBase, String metodo, String referenciaCompleta, Nodo x)
{
	var t = AnalizadorLexico.TS;
	int entrada = t.obtenerSimbolo(metodo+":"+claseBase+":main");
	if (entrada == TablaSimbolos.NO_ENCONTRADO)
	{
		int clave = t.obtenerSimbolo(claseBase+":"+claseActual+":main");
		entrada = t.obtenerSimbolo(metodo+":"+t.obtenerAtributo(clave, "tipo")+":main");
		if (entrada == TablaSimbolos.NO_ENCONTRADO)
			return false;
		else
		{
			String llamado = metodo+":"+t.obtenerAtributo(clave, "tipo")+":main";
			x.setIzq(new Nodo(llamado));
			return true;
		}
        }
        else
        {
        	if (t.obtenerAtributo(entrada, "herencia").equals(referenciaCompleta) || t.obtenerAtributo(entrada, "ref").equals(referenciaCompleta)){
        		x.setIzq(new Nodo(t.obtenerAtributo(entrada, "ref")));
        		return true;
        	}
        	return false;
        }
}

private Boolean tieneParametrosMetodo(String claseBase, String metodo)
{
	var t = AnalizadorLexico.TS;
        int entrada = t.obtenerSimbolo(metodo+":"+claseBase+":main");
        if (t.obtenerAtributo(entrada, "parametro").equals(t.NO_ENCONTRADO_MESSAGE))
        {
        	int clave = t.obtenerSimbolo(claseBase+":"+claseActual+":main");
                entrada = t.obtenerSimbolo(metodo+":"+t.obtenerAtributo(clave, "tipo")+":main");
                if (t.obtenerAtributo(entrada, "parametro").equals(t.NO_ENCONTRADO_MESSAGE))
        		return false;
        }
        return true;
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
                anotar(WARNING, "LINEA "+(AnalizadorLexico.getCantLineas())+": WARNING! Se truncó la constante entera negativa -"+original+"_s por ser inferior al valor minimo.");
                numero = AnalizadorLexico.MIN_SHORT_INT;
            }
            valor_final = Integer.toString(numero)+"_s";
            agregado = TablaSimbolos.agregarSimbolo(valor_final);
            TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), "uso", "constante");
            modificar_referencias(agregado, valor_final, "SHORT");
            TablaSimbolos.eliminarAtributo(id, "valor_original");
        }
        else
        {
            valor_final = "-"+valor;
            agregado = TablaSimbolos.agregarSimbolo(valor_final);
            TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), "uso", "constante");
            modificar_referencias(agregado, valor_final, "SHORT");
        }
	}

	if (tipo.equals("FLOAT")){
		valor_final = "-"+valor;
        	agregado = TablaSimbolos.agregarSimbolo(valor_final);
        	TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), "uso", "constante");
        	modificar_referencias(agregado, valor_final, "FLOAT");
	}

    if (tipo.equals("ULONG")){
        anotar(WARNING, "LINEA "+(AnalizadorLexico.getCantLineas())+": WARNING! Se truncó la constante long -"+valor+" ya que no se aceptan valores negativos.");
        valor_final = "0_ul";
        agregado = TablaSimbolos.agregarSimbolo(valor_final);
        TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerID(), "uso", "constante");
        modificar_referencias(agregado, valor_final, "ULONG");
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

private static Boolean chequearReferencia(String atributo, String claseBase, String referenciaCompleta){
	var t = AnalizadorLexico.TS;
	int clave = t.obtenerSimbolo(atributo+":"+claseBase+":main");
	if (clave != t.NO_ENCONTRADO)
	{
		if (!claseActual.equals("") && !t.obtenerAtributo(clave, "herencia").equals(referenciaCompleta))
			return false;
		else
			return true;
	}
	return false;
}

private static Boolean validarParametro (Nodo funcion)
{
	var t = AnalizadorLexico.TS;
	Nodo parametro = funcion.getDer();
	Nodo fun = funcion.getIzq();
	int clave_funcion = t.obtenerSimbolo(fun.getNombre());
	int clave_atributo = t.obtenerSimbolo(parametro.getNombre());
	String tipo = t.obtenerAtributo(clave_atributo, "tipo");
	String uso = t.obtenerAtributo(clave_atributo, "uso");
	String nombre_parametro_func = t.obtenerAtributo(clave_funcion, "parametro");
	int clave_p = t.obtenerSimbolo(nombre_parametro_func);
	String tipo_parametro_func = t.obtenerAtributo(clave_p, "tipo");
	if (uso.equals("constante"))
	{
		if (tipo_parametro_func.equals(tipo))
			return true;
		else
			return false;
	}else
	{
		if (!tipo_parametro_func.equals(tipo))
                {
                	if (tipo_parametro_func.equals("FLOAT"))
                	{
				if (tipo.equals("SHORT"))
				{
					var x = new Nodo("STOF", parametro, null);
					funcion.setDer(x);
				}else{
					var x = new Nodo("LTOF", parametro, null);
					funcion.setDer(x);
				}
				return true;
			}
			else
			{
				return false;
			}
               	}else
               		return true;
	}
}

private static Boolean aplicarHerencia(String heredada, String heredera)
{
	var t = AnalizadorLexico.TS;
	ArrayList<Integer> clave_variables_herencia = new ArrayList<>();
	clave_variables_herencia = t.variablesEnHerencia(heredada);
	for (Integer clave : clave_variables_herencia)
                {
                    String lexema_actual = t.obtenerAtributo(clave, "lexema");
                    if (t.obtenerAtributo(clave, "uso").equals("variable"))
                    {
                        String variable = lexema_actual.substring(0, lexema_actual.indexOf(":"));
                        variable = variable + ":" + heredera + ":main";
                        if (!t.agregarSimbolo(variable))
                            return false;
                        t.agregarAtributos(t.obtenerID(), t.obtenerAtributos(clave));
                        t.modificarAtributo(t.obtenerID(), "lexema", variable);
                        String herencia = lexema_actual.substring(0, lexema_actual.lastIndexOf(":"))+":"+heredera+":main";
                        t.agregarAtributo(t.obtenerID(), "herencia", herencia);
                    }else {
                        String variable = lexema_actual.substring(0, lexema_actual.indexOf(":"))+":"+heredera+":main";
                        if (t.agregarSimbolo(variable))
                        {	//SE AGREGA METODO HEREDADO
				t.agregarAtributos(t.obtenerID(), t.obtenerAtributos(clave));
				t.modificarAtributo(t.obtenerID(), "lexema", variable);
				variable = lexema_actual.substring(0, lexema_actual.lastIndexOf(":"))+":"+heredera+":main";
				t.agregarAtributo(t.obtenerID(), "herencia", variable);
			}else
			{	//EL METODO HEREDADO YA FUE DEFINIDO EN CLASE DERIVADA, SE SOBREESCRIBE
				t.modificarAtributo(clave, "ref", variable);
				variable = lexema_actual.substring(0, lexema_actual.lastIndexOf(":"))+":"+heredera+":main";
				if (t.obtenerAtributo(clave, "herencia") == t.NO_ENCONTRADO_MESSAGE)
					t.agregarAtributo(clave, "herencia", lexema_actual);
				else
					t.modificarAtributo(clave, "herencia", lexema_actual);
			}
                    }
                }

        int clave_heredera = t.obtenerSimbolo(Parser.ambito.substring(1));
        int clave_heredada = t.obtenerSimbolo(heredada+":main");
        t.agregarAtributo(clave_heredera, "clase_heredada", heredada+":main");
        t.agregarAtributo(clave_heredera, "niveles_herencia", "0");

        if (t.obtenerAtributo(clave_heredada, "niveles_herencia") == t.NO_ENCONTRADO_MESSAGE)
        	t.agregarAtributo(clave_heredada, "niveles_herencia", "0");
        return true;
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
    	case "Warning":
    		warnings.add(descripcion);
    		break;
    }
}

public static void imprimir(List<String> lista, String cabecera) {
        if (!lista.isEmpty()) {
                out_errores.write(cabecera+":");
                for (String x: lista) {
                         out_errores.write(x);
                }
                out_errores.write("\n");
        }
}

static void generarAssembler()
{
	  String rutaBatch = System.getProperty("user.dir")+"/build.bat";
	  ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "/B", "\"\"", rutaBatch);
	  pb.inheritIO();

	  // Iniciar el proceso
	  try {
	    Process proceso = pb.start();
	  } catch (IOException e) {
	    throw new RuntimeException(e);
	  }
}

static void imprimirResultados()
{
	  Parser.imprimir(warnings, "Warnings:");
	  Parser.imprimir(errorLexico, "Errores Lexicos");
	  Parser.imprimir(errorSintactico, "Errores Sintacticos");
	  Parser.imprimir(errorSemantico, "Errores Semanticos");
	  ArrayList<String> t = AnalizadorLexico.TS.imprimirTabla();
	  for (String x: t)
	  {
	    out_tabla.write(x);
	  }
	  String rutaBatch = System.getProperty("user.dir")+"/resultado.bat";
	  ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "\"\"", rutaBatch);
	  pb.redirectErrorStream(true);
	  try {
	    Process proceso = pb.start();
	  } catch (IOException e) {
	    throw new RuntimeException(e);
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
                AnalizadorLexico.TS.limpiarTabla();
                ArbolSintactico as = new ArbolSintactico(Parser.raiz);
                as.print(Parser.out_arbol);
                Estructura es = new Estructura();
                if (errorLexico.isEmpty() && errorSintactico.isEmpty() && errorSemantico.isEmpty())
                {
                	es.generateCode(raiz);
                	generarAssembler();
               	}
                else
                {
                	System.out.println("\nHay errores, no se genera codigo.");
                }
                imprimirResultados();
        } else {
                System.out.println("No se especifico el archivo a compilar");
        }
}