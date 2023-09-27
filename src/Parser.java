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
	package Compilador;

	import AccionesSemanticas.AccionSemantica;

	import java.io.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Stack;
//#line 27 "Parser.java"




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
public final static short CADENA=258;
public final static short VALOR=259;
public final static short CLASS=260;
public final static short IF=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short PRINT=264;
public final static short VOID=265;
public final static short SHORT=266;
public final static short ULONG=267;
public final static short WHILE=268;
public final static short DO=269;
public final static short RETURN=270;
public final static short MAYOR=271;
public final static short MENOR=272;
public final static short MAYORIGUAL=273;
public final static short MENORIGUAL=274;
public final static short IGUAL=275;
public final static short DISTINTO=276;
public final static short ASIGN=277;
public final static short MENOSMENOS=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    1,    1,    2,    2,
    2,    2,    4,    4,    5,    5,    6,    6,    6,    9,
    9,    9,    7,    7,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   19,   19,   19,   20,   20,    3,    3,
    3,   13,   14,   14,   14,   14,   15,   15,   16,   17,
   17,   10,   10,   24,   18,   18,   21,   25,   25,    8,
    8,    8,   22,   22,   22,   26,   26,   26,   27,   27,
   27,   27,   27,   23,   23,   23,   23,   23,   23,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    2,    1,    1,    6,    5,
    3,    2,    2,    1,    1,    1,    4,    3,    2,    3,
    2,    1,    3,    2,    3,    2,    2,    1,    1,    1,
    1,    1,    1,    3,    2,    2,    1,    1,    3,    1,
    1,    3,   14,   13,   10,    9,    9,    8,    4,    2,
    3,    9,    7,    1,    5,    3,    2,    3,    1,    1,
    1,    1,    1,    3,    3,    1,    3,    3,    1,    2,
    1,    1,    3,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   60,   61,    0,    0,    0,
    0,    0,    7,    8,    0,   37,    0,    0,   28,   29,
   30,   31,   32,   33,    0,    0,   38,    0,    0,    0,
    0,    0,    0,    0,    0,   71,   72,   50,    0,    0,
    0,   66,    2,    0,    3,    5,    6,    0,   57,    0,
   26,   27,    0,   35,   36,    0,   62,   56,    0,    0,
   11,    0,    0,    0,    0,   70,    0,    0,    0,   51,
    0,    0,    1,    0,    0,   25,    0,   34,    0,    0,
   14,   15,    0,    0,    0,   74,   75,   76,   77,   78,
   79,    0,   49,    0,    0,    0,   73,    0,    0,   67,
   68,   58,   55,    0,   13,    0,    0,   24,    0,    0,
    0,    0,    0,    9,   23,   18,    0,    0,   54,    0,
    0,    0,    0,    0,   17,    0,   53,    0,    0,    0,
   21,    0,    0,    0,   48,    0,   20,    0,   52,   47,
    0,   45,    0,    0,    0,   43,
};
final static short yydgoto[] = {                         11,
   12,   13,   14,   80,   81,   82,   83,   15,  125,   16,
   17,   18,   19,   20,   21,   22,   23,   24,   25,   26,
   27,   40,   92,  120,   49,   41,   42,
};
final static short yysindex[] = {                       -88,
  -40, -252,  -17,   26, -246,    0,    0,   33,  -37,  -69,
    0,  -57,    0,    0, -181,    0,   39,   -5,    0,    0,
    0,    0,    0,    0,   64,   14,    0,  -21,   -7,  -20,
  -21, -145,   78,  -21, -157,    0,    0,    0,  -21,   10,
   12,    0,    0,  214,    0,    0,    0,   63,    0, -134,
    0,    0,  251,    0,    0,  -16,    0,    0, -133, -156,
    0,  -33,   91,   -3,  -33,    0,  -15,  -21,  -21,    0,
  -21,  -21,    0, -181,  -40,    0,   39,    0,   94, -118,
    0,    0,   88, -119,   96,    0,    0,    0,    0,    0,
    0,  -21,    0,   18, -115,  -32,    0,   12,   12,    0,
    0,    0,    0,  100,    0, -112,  -26,    0,   29,  251,
  109,   31,   71,    0,    0,    0, -101,   34,    0,   40,
   35, -134,   44,  -19,    0, -134,    0,  251,  -38, -134,
    0, -101,  -31,   43,    0,  -30,    0, -174,    0,    0,
   47,    0, -134,  -29,  -99,    0,
};
final static short yyrindex[] = {                         0,
  -86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  131,    0,    0,    0,
    0,    0,    0,    0,  145,    0,    0,    0,    0,  159,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
   21,    0,    0,  175,    0,    0,    0,   81,    0,    0,
    0,    0,    0,    0,    0,   93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  174,    0,    0,    0,
    0,    0, -105,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,   61,    0,
    0,    0,    0,  194,    0,    0,  230,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  237,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  107,    0,    0,
    0,    0,    0,    0,  119,    0,
};
final static short yygindex[] = {                         0,
  171,   -8,    5,    0,  103,    0,    0,  -13,   52,  -28,
  -51,  135,    0,    0,    0,    0,    0,    0,    0,  134,
    0,  418,  124,   62,  128,   22,   27,
};
final static int YYTABLESIZE=521;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         29,
   69,   77,   39,   46,   30,   50,  104,   39,  112,   68,
   33,   69,   50,   50,   50,   59,   47,  116,   39,   16,
   63,   38,   31,   61,  131,   97,   68,   68,   69,   69,
   52,   85,  117,   58,   10,   46,   69,   94,   51,  132,
   64,   69,   69,   69,   69,   69,   84,   69,   47,   55,
   95,   85,   68,   71,   69,   43,   63,   54,   72,   69,
   65,   63,   32,   63,   63,   63,   84,   45,   70,  118,
  129,   68,   34,   69,  133,   48,   64,  115,  136,   63,
   59,   64,   50,   64,   64,   64,  135,  141,  142,   98,
   99,  144,   42,  138,  140,  145,   65,  100,  101,   64,
   57,   65,   60,   65,   65,   65,   46,   53,    5,    6,
    7,  123,   63,   68,  119,   69,   59,   64,   44,   65,
   66,   74,   75,   79,   59,   69,    3,   93,   42,    4,
   41,  106,  119,    8,  103,    9,   42,  107,   57,  108,
  110,  111,   46,  114,   40,   63,    5,    6,    7,  121,
   46,   16,    5,  122,   44,  124,  126,  128,   12,   16,
   16,   16,   44,  146,  127,   64,  130,  139,    1,  143,
   62,    2,    3,   39,    4,    4,    5,    6,    7,    8,
   44,    9,  105,  137,   76,   65,   78,    1,   96,  134,
    2,    3,    0,   10,    4,    5,    6,    7,    8,    1,
    9,  102,    2,    3,    0,   59,    4,    5,    6,    7,
    8,    0,    9,    0,    0,    0,    0,   42,    0,   35,
    0,    0,    0,    0,   35,    0,    0,    0,   36,   37,
    0,   46,    0,   36,   37,   35,   28,   86,   87,   88,
   89,   90,   91,   44,   36,   37,    0,    0,    0,   57,
    0,    0,    0,   57,    0,   41,    0,   69,    6,    7,
   69,   69,    6,    7,   69,   69,   69,   69,   69,   40,
   69,   69,   69,   69,   69,   69,   69,   63,    0,    0,
   63,   63,    0,   12,   63,   63,   63,   63,   63,    0,
   63,   63,   63,   63,   63,   63,   63,   64,   39,    0,
   64,   64,    0,    0,   64,   64,   64,   64,   64,    0,
   64,   64,   64,   64,   64,   64,   64,   65,   10,    0,
   65,   65,    0,    0,   65,   65,   65,   65,   65,    0,
   65,   65,   65,   65,   65,   65,   65,   59,   73,    0,
   59,   59,    0,    0,   59,   59,   59,   59,   59,   42,
   59,    0,   42,   42,   19,    0,   42,   42,   42,   42,
   42,   22,   42,   46,    0,    0,   46,   46,    0,    0,
   46,   46,   46,   46,   46,   44,   46,    0,   44,   44,
    0,    0,   44,   44,   44,   44,   44,   41,   44,    0,
   41,   41,    0,    0,   41,   41,   41,   41,   41,    0,
   41,   40,    0,    0,   40,   40,    0,    0,   40,   40,
   40,   40,   40,    0,   40,   12,    0,    0,   12,   12,
    0,    0,   12,   12,   12,   12,   12,    0,   12,    0,
   39,    0,    0,   39,   39,    0,    0,   39,   39,   39,
   39,   39,    0,   39,    0,   56,    0,    0,   62,    0,
   10,   65,    0,   10,   10,    0,   67,   10,   10,   10,
   10,   10,    0,   10,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    2,    3,    0,    0,    4,    5,    6,
    7,    8,    0,    9,    0,    0,   19,    0,    0,    0,
    0,    0,    0,   22,   19,   19,   19,    0,    0,    0,
    0,   22,   22,   22,    0,    0,    0,    1,    0,  109,
    0,    3,    0,  113,    4,    5,    6,    7,    8,    0,
    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   53,   40,   12,  257,   44,  125,   40,   41,   43,
  257,   45,   44,   44,   44,   29,   12,   44,   40,  125,
    0,   59,   40,   44,   44,   41,   43,   43,   45,   45,
   36,   60,   59,   41,  123,   44,   36,   41,   44,   59,
    0,   41,   42,   43,   44,   45,   60,   47,   44,   36,
   64,   80,   43,   42,   45,  125,   36,   44,   47,   59,
    0,   41,   37,   43,   44,   45,   80,  125,   59,   41,
  122,   43,   40,   45,  126,  257,   36,  106,  130,   59,
    0,   41,   44,   43,   44,   45,  125,  262,  263,   68,
   69,  143,    0,  125,  125,  125,   36,   71,   72,   59,
  257,   41,  123,   43,   44,   45,    0,   44,  265,  266,
  267,   41,  258,   43,  110,   45,   36,   40,    0,   59,
  278,   59,  257,  257,   44,  125,  261,   37,   36,  264,
    0,   44,  128,  268,   41,  270,   44,  257,  257,   44,
  123,  257,   36,   44,    0,  125,  265,  266,  267,   41,
   44,  257,  265,  123,   36,  257,  123,  123,    0,  265,
  266,  267,   44,  263,  125,  125,  123,  125,  257,  123,
  257,  260,  261,    0,    0,  264,  265,  266,  267,  268,
   10,  270,   80,  132,   50,  125,   53,  257,   65,  128,
  260,  261,   -1,    0,  264,  265,  266,  267,  268,  257,
  270,   74,  260,  261,   -1,  125,  264,  265,  266,  267,
  268,   -1,  270,   -1,   -1,   -1,   -1,  125,   -1,  257,
   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,  266,  267,
   -1,  125,   -1,  266,  267,  257,  277,  271,  272,  273,
  274,  275,  276,  125,  266,  267,   -1,   -1,   -1,  257,
   -1,   -1,   -1,  257,   -1,  125,   -1,  257,  266,  267,
  260,  261,  266,  267,  264,  265,  266,  267,  268,  125,
  270,  271,  272,  273,  274,  275,  276,  257,   -1,   -1,
  260,  261,   -1,  125,  264,  265,  266,  267,  268,   -1,
  270,  271,  272,  273,  274,  275,  276,  257,  125,   -1,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,   -1,
  270,  271,  272,  273,  274,  275,  276,  257,  125,   -1,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,   -1,
  270,  271,  272,  273,  274,  275,  276,  257,  125,   -1,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  257,
  270,   -1,  260,  261,  125,   -1,  264,  265,  266,  267,
  268,  125,  270,  257,   -1,   -1,  260,  261,   -1,   -1,
  264,  265,  266,  267,  268,  257,  270,   -1,  260,  261,
   -1,   -1,  264,  265,  266,  267,  268,  257,  270,   -1,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,   -1,
  270,  257,   -1,   -1,  260,  261,   -1,   -1,  264,  265,
  266,  267,  268,   -1,  270,  257,   -1,   -1,  260,  261,
   -1,   -1,  264,  265,  266,  267,  268,   -1,  270,   -1,
  257,   -1,   -1,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,   -1,  270,   -1,   28,   -1,   -1,   31,   -1,
  257,   34,   -1,  260,  261,   -1,   39,  264,  265,  266,
  267,  268,   -1,  270,   -1,   -1,   -1,   -1,   -1,   -1,
  257,   -1,   -1,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,   -1,  270,   -1,   -1,  257,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  265,  266,  267,   -1,   -1,   -1,
   -1,  265,  266,  267,   -1,   -1,   -1,  257,   -1,   92,
   -1,  261,   -1,   96,  264,  265,  266,  267,  268,   -1,
  270,
};
}
final static short YYFINAL=11;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"'$'","'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'",null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"ID","CADENA","VALOR","CLASS","IF",
"ELSE","END_IF","PRINT","VOID","SHORT","ULONG","WHILE","DO","RETURN","MAYOR",
"MENOR","MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO","ASIGN","MENOSMENOS",
};
final static String yyrule[] = {
"$accept : programa",
"programa : '{' cuerpoPrograma '}'",
"programa : '{' '}'",
"programa : cuerpoPrograma '}'",
"programa : '{' cuerpoPrograma",
"cuerpoPrograma : cuerpoPrograma declaracionClase",
"cuerpoPrograma : cuerpoPrograma listaSentencias",
"cuerpoPrograma : declaracionClase",
"cuerpoPrograma : listaSentencias",
"declaracionClase : CLASS ID '{' cuerpoClase '}' ','",
"declaracionClase : CLASS ID '{' cuerpoClase '}'",
"declaracionClase : CLASS ID ','",
"declaracionClase : CLASS ID",
"cuerpoClase : cuerpoClase seccionClase",
"cuerpoClase : seccionClase",
"seccionClase : seccionAtributos",
"seccionClase : seccionMetodos",
"seccionAtributos : tipo ID ';' listaAtributos",
"seccionAtributos : tipo ID ','",
"seccionAtributos : tipo ID",
"listaAtributos : ID ';' listaAtributos",
"listaAtributos : ID ','",
"listaAtributos : ID",
"seccionMetodos : seccionMetodos ',' declaracionMetodo",
"seccionMetodos : declaracionMetodo ','",
"listaEjecutables : listaEjecutables ',' sentenciaEjecutable",
"listaEjecutables : sentenciaEjecutable ','",
"listaEjecutables : sentenciaEjecutable '$'",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : sentenciaIf",
"sentenciaEjecutable : sentenciaWhile",
"sentenciaEjecutable : print",
"sentenciaEjecutable : return",
"sentenciaEjecutable : invocacionMetodo",
"listaDeclarativa : listaDeclarativa ',' sentenciaDeclarativa",
"listaDeclarativa : sentenciaDeclarativa ','",
"listaDeclarativa : sentenciaDeclarativa '$'",
"sentenciaDeclarativa : declaracionMetodo",
"sentenciaDeclarativa : declaracion",
"listaSentencias : listaDeclarativa ',' listaEjecutables",
"listaSentencias : listaDeclarativa",
"listaSentencias : listaEjecutables",
"asignacion : ID ASIGN expresion",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}'",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}'",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' '{' listaEjecutables '}'",
"sentenciaWhile : WHILE '(' expresion comparador ')' '{' listaEjecutables '}'",
"print : PRINT '%' CADENA '%'",
"return : RETURN ';'",
"return : RETURN expresion ';'",
"declaracionMetodo : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionMetodo : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"cuerpoMetodo : listaSentencias",
"invocacionMetodo : ID '(' tipo ID ')'",
"invocacionMetodo : ID '(' ')'",
"declaracion : tipo listaDeclaracion",
"listaDeclaracion : ID ';' listaDeclaracion",
"listaDeclaracion : ID",
"tipo : SHORT",
"tipo : ULONG",
"tipo : ID",
"expresion : termino",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"termino : factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"factor : ID",
"factor : ID MENOSMENOS",
"factor : SHORT",
"factor : ULONG",
"factor : '(' expresion ')'",
"comparador : MAYOR",
"comparador : MENOR",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
};

//#line 131 "gramatica.y"

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
                yylval = new ParserVal(AnalizadorLexico.lexema.toString());
                AnalizadorLexico.lexema= ""; // borro lexema porque no lo necesito mas
                return IDtoken; // devuelvo el ID del token
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    return 0; // 0 indica que se llego al EOF

void anotarError (ArrayList<String> listaError, String error){ // Agrega un error encontrado, "error" es la descripcion
    listaError.add(error + " Linea: " + AnalizadorLexico.getCantLineas() + " Posicion: " + AnalizadorLexico.getPosicion());
}

public static void main(String[] args) {
        if (args.length > 1) {
                String archivo_a_leer = args[0];

                try {
                        AnalizadorLexico.lector = new BufferedReader(new FileReader(archivo_a_leer));
                        Parser parser = new Parser();
                        parser.run();
                } catch (IOException excepcion) {
                        excepcion.printStackTrace();
                }
                TablaSimbolos.imprimirTabla();
        } else {
                System.out.println("No se especifico el archivo a compilar");
        }
//#line 480 "Parser.java"
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
case 2:
//#line 23 "gramatica.y"
{anotarError(errorSintactico, "Programa vacio")}
break;
case 3:
//#line 24 "gramatica.y"
{anotarError(errorSintactico, "Falta llave de apertura '{'")}
break;
case 4:
//#line 25 "gramatica.y"
{anotarError(errorSintactico, "Falta llave de cierre '}'")}
break;
case 10:
//#line 33 "gramatica.y"
{anotarError(errorSintactico, "Falta coma al final de linea")}
break;
case 12:
//#line 35 "gramatica.y"
{anotarError(errorSintactico, "Falta coma (',')")}
break;
case 19:
//#line 45 "gramatica.y"
{anotarError(errorSintactico, "Falta coma (',') al final de linea")}
break;
case 22:
//#line 49 "gramatica.y"
{anotarError(errorSintactico, "Falta coma (',') al final de linea")}
break;
case 27:
//#line 56 "gramatica.y"
{anotarError(errorSintactico, "Falta coma (',') al final de linea")}
break;
case 36:
//#line 67 "gramatica.y"
{anotarError(errorSintactico, "Falta coma (',') al final de linea")}
break;
case 44:
//#line 79 "gramatica.y"
{anotarError(errorSintactico, "Falta 'END_IF'")}
break;
case 46:
//#line 81 "gramatica.y"
{anotarError(errorSintactico, "Falta 'END_IF'")}
break;
case 48:
//#line 84 "gramatica.y"
{anotarError(errorSintactico, "Mal definida la condicion")}
break;
//#line 677 "Parser.java"
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
