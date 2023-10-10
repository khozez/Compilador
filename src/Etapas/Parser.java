//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
	package Etapas;
	import java.io.*;
        import AccionesSemanticas.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Stack;
//#line 25 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short CLASS=260;
public final static short IF=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short PRINT=264;
public final static short VOID=265;
public final static short SHORT=266;
public final static short ULONG=267;
public final static short FLOAT=268;
public final static short WHILE=269;
public final static short RETURN=270;
public final static short MAYORIGUAL=271;
public final static short MENORIGUAL=272;
public final static short IGUAL=273;
public final static short DISTINTO=274;
public final static short MENOSMENOS=275;
public final static short DO=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    2,    2,    2,    2,    5,
    5,    5,    6,    7,    7,    7,    7,   10,   10,   10,
    8,    8,    8,    8,   14,   14,   14,    4,    4,    4,
    4,    4,    4,    3,    3,    3,   20,   20,   11,   11,
   15,   15,   15,   15,   15,   15,   15,   23,   23,   16,
   16,   16,   17,   24,    9,    9,   18,   18,   18,   18,
   25,   26,   26,   26,   26,   12,   12,   12,   12,   19,
   19,   27,   27,   13,   13,   13,   13,   21,   21,   21,
   28,   28,   28,   29,   29,   29,   29,   29,   22,   22,
   22,   22,   22,   22,   22,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    3,    1,    2,    5,
    3,    2,    1,    2,    3,    2,    1,    3,    3,    3,
    4,    2,    3,    2,    3,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    1,    1,    2,    3,    3,
   14,   13,   10,    9,   12,    9,    8,    3,    2,   10,
    8,    9,    2,    2,    9,    7,   10,    9,    8,    7,
    1,    2,    3,    1,    2,    4,    3,    4,    5,    2,
    2,    3,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    3,    3,    1,    2,    1,    2,    3,    1,    1,
    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   74,   75,   76,    0,    0,
    0,    0,    0,    8,    0,   36,   33,   28,   32,    0,
   29,   30,   31,   34,    0,    0,    0,    0,    0,    0,
    0,   53,    0,    0,    2,    0,    3,    6,    0,    9,
    0,   71,   70,   35,    0,   86,    0,    0,    0,    0,
   81,    0,   18,   19,   20,    0,    0,   67,    0,    0,
    0,    0,   11,    0,    0,    0,    1,    7,    0,   85,
   87,    0,    0,    0,    0,    0,   68,    0,    0,   66,
    0,    0,    0,    0,   17,    0,    0,   89,   90,   91,
   92,   95,   93,   94,    0,   77,    0,    0,    0,    0,
   72,   88,    0,    0,   82,   83,   69,   22,    0,   10,
   14,    0,   16,    0,    0,    0,    0,    0,    0,    0,
   15,   23,    0,    0,    0,   37,    0,   64,    0,    0,
    0,    0,    0,    0,    0,    0,   21,    0,    0,    0,
   65,   38,    0,   60,    0,    0,   62,    0,    0,    0,
    0,    0,   26,    0,    0,    0,   47,    0,   54,   59,
   63,    0,    0,    0,   51,    0,    0,   25,   49,    0,
    0,    0,   46,   58,    0,   52,    0,   56,    0,    0,
   43,   48,    0,   57,   50,    0,    0,    0,   55,    0,
   45,    0,   41,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,  125,   16,   83,   84,   85,   86,   17,
   18,   19,   20,  137,   21,   22,   23,  126,  127,  128,
   49,   95,  156,  145,  129,  130,   43,   50,   51,
};
final static short yysindex[] = {                       -85,
  -27, -217,   25, -205, -132,    0,    0,    0,   89,  -54,
    0,   15, -148,    0,   98,    0,    0,    0,    0, -109,
    0,    0,    0,    0,  113,  -10, -103,  -10,   29,  -15,
  -10,    0,  123,  -10,    0,   44,    0,    0,  129,    0,
  -17,    0,    0,    0,  -87,    0,  -57,  -10,   57,   80,
    0,  -27,    0,    0,    0,   57,  -44,    0,  153,  -52,
  102, -162,    0,    6,  -41,    6,    0,    0,  -49,    0,
    0,  110,  -10,  -10,  -10,  -10,    0,  -43,  182,    0,
  180,  -21,  103, -162,    0,  205,   -2,    0,    0,    0,
    0,    0,    0,    0,  -10,    0,  134,    3,   -4,  200,
    0,    0,   80,   80,    0,    0,    0,    0,  223,    0,
    0,  220,    0,   32,  144, -133,  231,    5,  157,  -16,
    0,    0,   26,   37,  238,    0,  240,    0,  -77, -133,
  165,  166,   16,  167,   36,   34,    0, -120,   39,  259,
    0,    0,  261,    0,  183,  270,    0, -133, -120,  -99,
 -133,  274,    0,   26,  275, -105,    0, -124,    0,    0,
    0,  -75,  -65, -120,    0,  195,  199,    0,    0,  -96,
  279, -120,    0,    0,  206,    0,   43,    0, -133,  207,
    0,    0,  288,    0,    0,  209, -120,   72,    0,   52,
    0,   73,    0,
};
final static short yyrindex[] = {                         0,
   82,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   53,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,  337,    0,    0,    0,    0,
  296,    0,    0,    0,  -39,    0,    0,    0,   45,  -34,
    0,    0,    0,    0,    0,   70,   14,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   82,    0,    0,  216,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  296,
    0,    0,  -29,    2,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -48,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -73,
    0,    0,    0,    0,    0,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  113,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  298,    0,
};
final static short yygindex[] = {                         0,
  333,    0,  331,  161,    0,    0,    0,  262,  263,  318,
   63,  322,   10,  196,    0,    0,    0,   88,   97,  222,
   51,  287,  -91,  192,  -64,    0,  286,  117,  146,
};
final static int YYTABLESIZE=355;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
   12,   84,   84,   84,   84,   84,   78,   84,   78,   78,
   78,   79,   29,   79,   79,   79,   28,   28,   27,  170,
   84,   84,   84,  164,  134,   78,   78,   78,   63,   48,
   79,   79,   79,   28,   47,   48,  118,   10,   60,   30,
   47,   69,   80,   28,   80,   80,   80,  144,   73,  174,
   74,   61,    5,   32,   84,   84,   84,  163,   84,  176,
   84,   80,   80,   80,   31,   93,   92,   94,   48,   58,
   35,   87,  177,   47,   98,  122,   24,  153,   56,   61,
  139,   64,   42,  162,   66,   40,  166,   24,   40,   54,
  123,   59,  154,   87,   81,  190,   25,   24,   72,   73,
   24,   74,   82,    6,    7,    8,   25,   62,    1,   25,
   39,    2,    3,   39,  186,    4,    5,    6,    7,    8,
    9,   75,   79,    1,   33,   12,   76,    3,   34,  135,
    4,    5,    6,    7,    8,    9,   52,  172,  173,   37,
    3,   40,   80,    4,   73,  115,   74,   41,    9,  119,
  102,   52,   73,   52,   74,    3,   44,   52,    4,  138,
   15,    3,   65,    9,    4,  180,  181,  185,   67,    9,
   15,    1,   68,   39,    2,    3,  192,    5,    4,    5,
    6,    7,    8,    9,  124,   27,   73,   70,   74,  103,
  104,   52,  143,   77,  143,    3,   61,  133,    4,   73,
   71,   74,    1,    9,   78,    2,    3,  100,   24,    4,
    5,    6,    7,    8,    9,   96,   24,   24,   24,   24,
  105,  106,  107,  108,    6,    7,    8,  110,   26,   26,
   70,   84,   84,   84,   84,  109,   78,   78,   78,   78,
   96,   79,   79,   79,   79,   26,   45,   46,  113,    6,
    7,    8,   45,   46,  114,   26,  116,   12,   69,  117,
   12,   12,  120,  121,   12,   12,   12,   12,   12,   12,
   77,  131,   80,   80,   80,   80,   88,   89,   90,   91,
  132,  141,  136,  142,  140,   57,   46,  148,  149,  151,
  146,  150,  152,   52,    6,    7,    8,    3,  155,   52,
    4,  157,  158,    3,  159,    9,    4,  160,   52,  155,
  165,    9,    3,  161,  167,    4,  171,   27,  169,  178,
    9,  179,  182,  171,  155,   27,   27,   27,   27,  187,
  184,  188,  183,  189,  191,  193,    4,  171,   77,   73,
   13,   42,   36,   38,   53,  111,  112,  155,   55,  168,
  171,  147,   99,  175,  101,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   44,   45,   41,   47,   43,   44,
   45,   41,   40,   43,   44,   45,   61,   61,   46,  125,
   60,   61,   62,  123,   41,   60,   61,   62,   44,   40,
   60,   61,   62,   61,   45,   40,   41,  123,   29,  257,
   45,   59,   41,   61,   43,   44,   45,  125,   43,  125,
   45,  125,    0,  259,   41,   42,   43,  149,   45,  125,
   47,   60,   61,   62,   40,   60,   61,   62,   40,   41,
  125,   62,  164,   45,   65,   44,  125,   44,   28,   29,
   44,   31,   20,  148,   34,   41,  151,    0,   44,   27,
   59,   29,   59,   84,  257,  187,    0,   10,   48,   43,
   13,   45,  265,  266,  267,  268,   10,  123,  257,   13,
   41,  260,  261,   44,  179,  264,  265,  266,  267,  268,
  269,   42,   60,  257,  257,  125,   47,  261,   40,  120,
  264,  265,  266,  267,  268,  269,  257,  262,  263,  125,
  261,   44,   41,  264,   43,   95,   45,  257,  269,   99,
   41,  257,   43,  257,   45,  261,   44,  257,  264,  123,
    0,  261,   40,  269,  264,  262,  263,  125,  125,  269,
   10,  257,   44,   13,  260,  261,  125,  125,  264,  265,
  266,  267,  268,  269,   41,  125,   43,  275,   45,   73,
   74,  257,  270,   41,  270,  261,  270,   41,  264,   43,
  258,   45,  257,  269,  257,  260,  261,  257,  257,  264,
  265,  266,  267,  268,  269,  257,  265,  266,  267,  268,
   75,   76,   41,   44,  266,  267,  268,  125,  273,  273,
  275,  271,  272,  273,  274,  257,  271,  272,  273,  274,
  257,  271,  272,  273,  274,  273,  257,  258,   44,  266,
  267,  268,  257,  258,  257,  273,  123,  257,   59,  257,
  260,  261,   40,   44,  264,  265,  266,  267,  268,  269,
  257,   41,  271,  272,  273,  274,  271,  272,  273,  274,
  276,   44,  257,   44,  124,  257,  258,  123,  123,  123,
  130,  276,  257,  257,  266,  267,  268,  261,  138,  257,
  264,  263,   44,  261,   44,  269,  264,  125,  257,  149,
  150,  269,  261,   44,   41,  264,  156,  257,   44,  125,
  269,  123,   44,  163,  164,  265,  266,  267,  268,  123,
  125,   44,  172,  125,  263,  263,    0,  177,  257,   44,
  125,   44,   10,   13,   27,   84,   84,  187,   27,  154,
  190,  130,   66,  162,   69,
};
}
final static short YYFINAL=11;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","CLASS","IF","ELSE",
"END_IF","PRINT","VOID","SHORT","ULONG","FLOAT","WHILE","RETURN","MAYORIGUAL",
"MENORIGUAL","IGUAL","DISTINTO","MENOSMENOS","DO",
};
final static String yyrule[] = {
"$accept : programa",
"programa : '{' cuerpoPrograma '}'",
"programa : '{' '}'",
"programa : cuerpoPrograma '}'",
"programa : '{' cuerpoPrograma",
"cuerpoPrograma : listaSentencias",
"listaSentencias : listaSentencias sentenciaDeclarativa",
"listaSentencias : listaSentencias sentenciaEjecutable ','",
"listaSentencias : sentenciaDeclarativa",
"listaSentencias : sentenciaEjecutable ','",
"declaracionClase : CLASS ID '{' cuerpoClase '}'",
"declaracionClase : CLASS ID ','",
"declaracionClase : CLASS ID",
"cuerpoClase : seccionClase",
"seccionClase : seccionClase seccionAtributos",
"seccionClase : seccionClase declaracionMetodo ','",
"seccionClase : declaracionMetodo ','",
"seccionClase : seccionAtributos",
"referenciaClase : ID '.' referenciaClase",
"referenciaClase : ID '.' asignacion",
"referenciaClase : ID '.' invocacionMetodo",
"seccionAtributos : tipo ID ';' listaAtributos",
"seccionAtributos : ID ','",
"seccionAtributos : tipo ID ','",
"seccionAtributos : tipo ID",
"listaAtributos : ID ';' listaAtributos",
"listaAtributos : ID ','",
"listaAtributos : ID",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : sentenciaIf",
"sentenciaEjecutable : sentenciaWhile",
"sentenciaEjecutable : print",
"sentenciaEjecutable : invocacionMetodo",
"sentenciaEjecutable : referenciaClase",
"sentenciaDeclarativa : declaracionFuncion",
"sentenciaDeclarativa : declaracion ','",
"sentenciaDeclarativa : declaracionClase",
"sentenciaDeclarativaMetodo : declaracionFuncion",
"sentenciaDeclarativaMetodo : declaracion ','",
"asignacion : ID '=' expresion",
"asignacion : ID IGUAL expresion",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}'",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion '}'",
"sentenciaIf : IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' ELSE sentenciaEjecutable ',' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' ',' END_IF",
"bloque_ejecucion : bloque_ejecucion sentenciaEjecutable ','",
"bloque_ejecucion : sentenciaEjecutable ','",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' DO '{' bloque_ejecucion '}'",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' DO sentenciaEjecutable",
"sentenciaWhile : WHILE '(' expresion comparador ')' DO '{' bloque_ejecucion '}'",
"print : PRINT CADENA",
"return : RETURN ','",
"declaracionMetodo : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionMetodo : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' tipo ID ')' '{' cuerpoMetodo return '}'",
"declaracionFuncion : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' ')' '{' cuerpoMetodo return '}'",
"declaracionFuncion : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"cuerpoMetodo : listaSentenciasMetodo",
"listaSentenciasMetodo : listaSentenciasMetodo sentenciaDeclarativaMetodo",
"listaSentenciasMetodo : listaSentenciasMetodo sentenciaEjecutable ','",
"listaSentenciasMetodo : sentenciaDeclarativaMetodo",
"listaSentenciasMetodo : sentenciaEjecutable ','",
"invocacionMetodo : ID '(' expresion ')'",
"invocacionMetodo : ID '(' ')'",
"invocacionMetodo : ID '(' asignacion ')'",
"invocacionMetodo : ID '(' tipo asignacion ')'",
"declaracion : tipo listaDeclaracion",
"declaracion : tipo asignacion",
"listaDeclaracion : ID ';' listaDeclaracion",
"listaDeclaracion : ID",
"tipo : SHORT",
"tipo : ULONG",
"tipo : FLOAT",
"tipo : ID",
"expresion : termino",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"termino : factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"factor : ID",
"factor : ID MENOSMENOS",
"factor : CTE",
"factor : '-' CTE",
"factor : '(' expresion ')'",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
};

//#line 178 "gramatica.y"

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
        String archivo = "src\\TEST-ERRORES-PARSER.txt";
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
//#line 615 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 21 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");}
break;
case 2:
//#line 22 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Programa vacio");}
break;
case 3:
//#line 23 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de apertura '{'");}
break;
case 4:
//#line 24 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de cierre '}'");}
break;
case 10:
//#line 36 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 11:
//#line 37 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 12:
//#line 38 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 19:
//#line 51 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Referencia a atributo de clase.");}
break;
case 20:
//#line 52 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocacion de metodo de clase");}
break;
case 24:
//#line 58 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la definición de variable.");}
break;
case 27:
//#line 63 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
break;
case 39:
//#line 84 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");}
break;
case 40:
//#line 85 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 41:
//#line 88 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 42:
//#line 89 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 43:
//#line 90 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 44:
//#line 91 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 45:
//#line 92 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 46:
//#line 93 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 50:
//#line 101 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 51:
//#line 102 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 52:
//#line 103 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 53:
//#line 106 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");}
break;
case 55:
//#line 112 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 56:
//#line 113 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 57:
//#line 116 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 58:
//#line 117 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia RETURN");}
break;
case 59:
//#line 118 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 60:
//#line 119 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia RETURN");}
break;
case 66:
//#line 131 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
break;
case 67:
//#line 132 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
break;
case 68:
//#line 133 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 69:
//#line 134 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 87:
//#line 164 "gramatica.y"
{comprobarRango(val_peek(0).sval);}
break;
case 95:
//#line 174 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
break;
//#line 904 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
