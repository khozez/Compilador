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
    0,    0,    0,    0,    1,    1,    2,    2,    5,    6,
    6,    6,    7,    7,    7,    7,   10,   10,   12,   13,
   13,   11,   11,    8,    8,   16,   16,   16,   17,    4,
    4,    4,    4,    4,    4,    3,    3,    3,   25,   25,
   18,   18,   19,   19,   19,   19,   19,   19,   19,   27,
   27,   27,   30,   30,   30,   30,   30,   30,   30,   32,
   32,   32,   32,   32,   32,   32,   28,   28,   31,   31,
   20,   20,   21,   21,   33,   34,   35,   35,   35,    9,
   37,   37,   37,   23,   26,   36,   38,   39,   39,   39,
   39,   40,   40,   40,   40,   22,   22,   22,   22,   24,
   41,   41,   15,   15,   15,   15,   14,   14,   14,   42,
   42,   42,   43,   43,   43,   43,   43,   29,   29,   29,
   29,   29,   29,   29,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    1,    2,    2,    4,
    2,    1,    2,    3,    2,    1,    2,    2,    3,    3,
    4,    3,    2,    2,    1,    3,    2,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    2,    1,    1,    2,
    3,    3,   12,   11,    8,    7,   10,   11,    7,    3,
    2,    2,   13,   12,    8,    7,   10,   12,    7,    1,
    1,    1,    1,    1,    1,    1,    3,    2,    3,    2,
    8,    6,    2,    1,    1,    2,    5,    5,    4,    4,
    5,    5,    4,    4,    4,    1,    1,    2,    3,    1,
    2,    2,    3,    1,    2,    4,    3,    4,    5,    2,
    3,    1,    1,    1,    1,    1,    1,    3,    3,    1,
    3,    3,    1,    2,    1,    2,    3,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   74,    0,    0,    0,    0,  103,  104,  105,    0,
    0,    0,    0,    5,    7,    0,    0,   38,   35,    0,
    0,   30,   31,   32,   33,   34,   36,    0,    0,    0,
    0,    0,   23,    9,    0,   73,    0,    0,    2,    0,
    3,    6,    8,    0,   11,    0,   17,   18,    0,  100,
   37,    0,    0,  115,    0,    0,    0,    0,  110,    0,
    0,   97,    0,    0,    0,  118,  119,  120,  121,  124,
  122,  123,    0,    0,    0,    0,    0,    1,    0,    0,
    0,   16,    0,    0,   25,    0,    0,    0,   22,    0,
    0,   75,   65,   61,   63,   60,   64,    0,   94,   39,
   62,    0,   66,    0,    0,    0,  114,  116,    0,    0,
    0,    0,    0,   96,    0,    0,   98,    0,    0,    0,
    0,   83,    0,    0,    0,   29,    0,   10,   13,    0,
   15,    0,   24,    0,    0,   20,    0,  101,    0,   40,
   95,    0,   84,   92,    0,  117,    0,    0,  111,  112,
   99,    0,    0,    0,    0,   82,   76,   81,    0,    0,
   14,   27,    0,    0,   90,    0,    0,   21,    0,    0,
   93,    0,    0,    0,    0,   72,    0,   79,    0,   26,
   91,   80,    0,   88,    0,   85,   68,    0,    0,    0,
   49,    0,   78,   77,   89,    0,    0,    0,   45,   67,
    0,    0,   71,    0,    0,    0,    0,    0,    0,    0,
    0,   70,    0,   59,    0,    0,   47,    0,   55,   69,
    0,    0,    0,   48,    0,    0,    0,   43,    0,    0,
   57,    0,    0,    0,   58,   53,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   18,   81,   82,   83,   19,
   20,   47,   48,   73,   21,  133,   85,   22,   23,   24,
   25,   26,   27,   98,   99,  100,   74,  173,   75,  101,
  204,  205,  103,  124,   86,  166,  104,  105,  167,  106,
   50,   58,   59,
};
final static short yysindex[] = {                        97,
  -23,    0, -216,    9, -200, -161,    0,    0,    0,   62,
  187,    0,  221,    0,    0,   60,  -26,    0,    0, -144,
 -136,    0,    0,    0,    0,    0,    0,   83,    6,   37,
   37,  -12,    0,    0,  -40,    0,   94,  -40,    0,  234,
    0,    0,    0, -158,    0,   55,    0,    0,   87,    0,
    0, -126, -109,    0, -122,   37,   -1,   77,    0,   -1,
  -59,    0,  109,  -88,  130,    0,    0,    0,    0,    0,
    0,    0,   11,  134,   37,  -38,  139,    0,  161,  -51,
  -55,    0,  164,  -42,    0,   99,   37,   -6,    0, -136,
  183,    0,    0,    0,    0,    0,    0,  180,    0,    0,
    0,  181,    0,  103,  102, -126,    0,    0,  129,   37,
   37,   37,   37,    0,  -25,  199,    0,   37,   86,   -1,
  206,    0,   -8,  212,   -5,    0,  232,    0,    0,  246,
    0,   -4,    0,  286,   -1,    0,  145,    0,  -40,    0,
    0, -126,    0,    0,  248,    0,   77,   77,    0,    0,
    0,   -1,  -23, -139,  254,    0,    0,    0,  112,   20,
    0,    0,  -42,  260,    0,  184,  286,    0,  265,  189,
    0,  277,  204, -182, -139,    0,  267,    0,  282,    0,
    0,    0,  283,    0,  -70,    0,    0, -151,  293,  141,
    0,  247,    0,    0,    0,  157,  294,  203,    0,    0,
 -139,  295,    0, -106,  298, -102, -139,  253,   89,  -80,
  304,    0,   61,    0,  264,  104,    0,  237,    0,    0,
  157,  324,  107,    0,  157,   32,  111,    0,   46,  250,
    0,  252,  116,  117,    0,    0,
};
final static short yyrindex[] = {                         0,
  125,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  383,
    0,    0,    0,    0,    0,    0,    0,    0,  340,    0,
    0,    0,  -35,    0,    0,    0,   16,  -30,    0,  118,
   42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  125,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  261,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  344,    0,  347,
  125,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   67,    0,    0,  346,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    2,    7,    0,    0,
    0,  350,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  268,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  125,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  351,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  353,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  355,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  357,    0,    0,
};
final static short yygindex[] = {                         0,
  393,   53,    0,  373,    0,    0,    0,  313,  325,  -48,
    0,    0,    0,  504,   47,  244,    0,  115,    0,  -28,
  207,  245,    0,   75,  -69,    0,  -22, -101,  335,    0,
  -76,  211,    0,  249,    0,    0,   92,  270,    0,    0,
  323,   85,   91,
};
final static int YYTABLESIZE=622;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   12,   31,  122,   93,   55,  113,  113,  113,  113,  113,
  107,  113,  107,  107,  107,   77,   32,   45,  210,   71,
   70,   72,   33,   95,  113,  113,  113,   56,   62,  107,
  107,  107,   55,   56,  136,   31,  144,   31,   55,  162,
   34,  110,  108,  111,  108,  108,  108,  109,   35,  109,
  109,  109,  196,  110,  163,  111,   42,   93,   36,   42,
  178,  108,  108,  108,  165,   42,  109,  109,  109,  128,
   71,   70,   72,  192,   28,  230,   56,   95,   64,  190,
  191,   55,  113,  113,  113,   28,  113,   28,  113,  232,
   84,   29,   42,   93,   88,   37,   44,  184,   79,  208,
   89,   38,   29,   43,   29,  215,   80,    7,    8,    9,
  198,  199,   46,   95,   28,   87,  169,  153,  112,    2,
   49,    4,  123,  113,    5,   12,   51,   84,   52,   10,
    1,   29,    2,   76,   91,  108,   93,    5,    6,    7,
    8,    9,   10,   92,  226,   90,   65,   93,  229,  114,
  153,  110,    2,  111,   91,   93,   95,    5,   41,  213,
  214,   41,   10,   92,   93,  107,   94,   95,  115,  146,
  117,  110,   93,  111,  119,   95,   93,   93,  116,  125,
   93,  218,  219,  221,   95,  168,  153,  110,    2,  111,
   91,   28,   95,    5,  147,  148,   95,   95,   10,   92,
   95,   79,  149,  150,  126,  127,  123,  131,  154,   80,
    7,    8,    9,   30,  132,  107,   53,   54,  121,   11,
   94,  134,  139,  140,  141,  142,  143,    7,    8,    9,
   66,   67,   68,   69,  175,  113,  113,  113,  113,  151,
  107,  107,  107,  107,   61,   54,  156,   30,  157,   30,
   53,   54,  158,    7,    8,    9,   94,   12,   96,   12,
   12,   12,  102,  201,   12,   12,   12,   12,   12,   12,
  159,  160,  108,  108,  108,  108,  177,  109,  109,  109,
  109,   66,   67,   68,   69,    7,    8,    9,  153,  161,
    2,  171,   91,   53,   54,    5,   97,  174,  106,   94,
   10,   92,  153,  181,    2,  185,   91,  193,  182,    5,
   94,   39,   96,  186,   10,   92,  145,  153,   94,    2,
  187,   91,  194,   28,    5,  207,  195,   94,  188,   10,
   92,   28,   28,   28,   28,   94,  200,  206,  209,   94,
   94,  212,  153,   94,    2,   41,    4,  220,   96,    5,
   97,  217,  102,    1,   10,    2,    3,    4,   78,  225,
    5,    6,    7,    8,    9,   10,  224,  227,  153,  228,
    2,  203,    4,  231,  233,    5,  234,  216,  235,  236,
   10,  106,    4,  102,   52,   87,   97,   51,  223,   19,
   50,   96,   86,  129,   46,  197,   56,  153,   44,    2,
   54,    4,   96,   40,    5,  130,  180,  118,  179,   10,
   96,  170,  138,  153,  211,    2,    0,   91,    0,   96,
    5,    0,    0,  222,    0,   10,   92,   96,    0,   97,
    0,   96,   96,    0,    0,   96,  211,    0,    0,  211,
   97,    0,    0,    1,    0,    2,    3,    4,   97,    0,
    5,    6,    7,    8,    9,   10,    0,   97,    0,    0,
  153,    0,    2,    0,    4,   97,    0,    5,    0,   97,
   97,    0,   10,   97,    0,    0,    0,    1,    0,    2,
    3,    4,    0,    0,    5,    6,    7,    8,    9,   10,
    1,  155,    2,    3,    4,    0,    0,    5,    6,    7,
    8,    9,   10,  153,    0,    2,  164,    4,    0,  153,
    5,    2,    0,    4,    0,   10,    5,    0,    0,    0,
  153,   10,    2,    0,    4,    0,  172,    5,    0,    0,
    0,  176,   10,   57,   60,   63,    0,    0,    0,  183,
    0,    0,    1,    0,    2,  189,    4,  172,    0,    5,
    6,    7,    8,    9,   10,    0,    0,    0,    0,  109,
    0,    0,  202,    0,  189,    0,    0,    0,    0,    0,
    0,    0,    0,  172,    0,    0,    0,    0,  120,  172,
  189,    0,    0,    0,    0,    0,    0,  189,    0,    0,
  135,  137,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  152,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   61,   41,   52,   45,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,   38,   40,   44,  125,   60,
   61,   62,   46,   52,   60,   61,   62,   40,   41,   60,
   61,   62,   45,   40,   41,   61,  106,   61,   45,   44,
  257,   43,   41,   45,   43,   44,   45,   41,   40,   43,
   44,   45,  123,   43,   59,   45,   41,  106,  259,   44,
   41,   60,   61,   62,  134,   13,   60,   61,   62,  125,
   60,   61,   62,  175,    0,   44,   40,  106,   32,  262,
  263,   45,   41,   42,   43,   11,   45,   13,   47,   44,
   44,    0,   40,  142,   40,  257,  123,  167,  257,  201,
   46,   40,   11,   44,   13,  207,  265,  266,  267,  268,
  262,  263,  257,  142,   40,   61,  139,  257,   42,  259,
  257,  261,   76,   47,  264,  125,   44,   81,  123,  269,
  257,   40,  259,   40,  261,  258,  185,  264,  265,  266,
  267,  268,  269,  270,  221,   59,   32,  196,  225,   41,
  257,   43,  259,   45,  261,  204,  185,  264,   41,  262,
  263,   44,  269,  270,  213,  275,   52,  196,  257,   41,
   41,   43,  221,   45,   41,  204,  225,  226,   64,   41,
  229,  262,  263,  123,  213,   41,  257,   43,  259,   45,
  261,  125,  221,  264,  110,  111,  225,  226,  269,  270,
  229,  257,  112,  113,   44,  257,  160,   44,  123,  265,
  266,  267,  268,  273,  257,  275,  257,  258,  257,  123,
  106,  123,   40,   44,   44,  123,  125,  266,  267,  268,
  271,  272,  273,  274,  123,  271,  272,  273,  274,   41,
  271,  272,  273,  274,  257,  258,   41,  273,  257,  273,
  257,  258,   41,  266,  267,  268,  142,  257,   52,  259,
  260,  261,   52,  123,  264,  265,  266,  267,  268,  269,
  276,   40,  271,  272,  273,  274,  257,  271,  272,  273,
  274,  271,  272,  273,  274,  266,  267,  268,  257,   44,
  259,   44,  261,  257,  258,  264,   52,   44,  257,  185,
  269,  270,  257,   44,  259,   41,  261,   41,  125,  264,
  196,  125,  106,  125,  269,  270,  106,  257,  204,  259,
   44,  261,   41,  257,  264,  123,   44,  213,  125,  269,
  270,  265,  266,  267,  268,  221,   44,   44,   44,  225,
  226,   44,  257,  229,  259,  125,  261,   44,  142,  264,
  106,  263,  142,  257,  269,  259,  260,  261,  125,  123,
  264,  265,  266,  267,  268,  269,  263,   44,  257,  263,
  259,  125,  261,  263,  125,  264,  125,  125,  263,  263,
  269,  257,    0,   44,   41,  125,  142,   41,  125,   44,
   41,  185,  125,   81,   44,  185,   44,  257,   44,  259,
   44,  261,  196,   11,  264,   81,  163,   73,  160,  269,
  204,  142,   90,  257,  204,  259,   -1,  261,   -1,  213,
  264,   -1,   -1,  213,   -1,  269,  270,  221,   -1,  185,
   -1,  225,  226,   -1,   -1,  229,  226,   -1,   -1,  229,
  196,   -1,   -1,  257,   -1,  259,  260,  261,  204,   -1,
  264,  265,  266,  267,  268,  269,   -1,  213,   -1,   -1,
  257,   -1,  259,   -1,  261,  221,   -1,  264,   -1,  225,
  226,   -1,  269,  229,   -1,   -1,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  269,
  257,  119,  259,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  257,   -1,  259,  134,  261,   -1,  257,
  264,  259,   -1,  261,   -1,  269,  264,   -1,   -1,   -1,
  257,  269,  259,   -1,  261,   -1,  154,  264,   -1,   -1,
   -1,  159,  269,   30,   31,   32,   -1,   -1,   -1,  167,
   -1,   -1,  257,   -1,  259,  173,  261,  175,   -1,  264,
  265,  266,  267,  268,  269,   -1,   -1,   -1,   -1,   56,
   -1,   -1,  190,   -1,  192,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  201,   -1,   -1,   -1,   -1,   75,  207,
  208,   -1,   -1,   -1,   -1,   -1,   -1,  215,   -1,   -1,
   87,   88,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  118,
};
}
final static short YYFINAL=12;
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
"cuerpoPrograma : sentencia",
"cuerpoPrograma : cuerpoPrograma sentencia",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaEjecutable ','",
"encabezadoClase : CLASS ID",
"declaracionClase : encabezadoClase '{' cuerpoClase '}'",
"declaracionClase : encabezadoClase ','",
"declaracionClase : encabezadoClase",
"cuerpoClase : cuerpoClase seccionAtributos",
"cuerpoClase : cuerpoClase declaracionMetodo ','",
"cuerpoClase : declaracionMetodo ','",
"cuerpoClase : seccionAtributos",
"referenciaClase : listaReferencia asignacionClase",
"referenciaClase : listaReferencia referenciaMetodo",
"asignacionClase : ID '=' expresion",
"referenciaMetodo : ID '(' ')'",
"referenciaMetodo : ID '(' expresion ')'",
"listaReferencia : listaReferencia ID '.'",
"listaReferencia : ID '.'",
"seccionAtributos : tipo listaAtributos",
"seccionAtributos : herenciaNombre",
"listaAtributos : ID ';' listaAtributos",
"listaAtributos : ID ','",
"listaAtributos : ID",
"herenciaNombre : ID ','",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : sentenciaIf",
"sentenciaEjecutable : sentenciaWhile",
"sentenciaEjecutable : print",
"sentenciaEjecutable : invocacionMetodo",
"sentenciaEjecutable : referenciaClase",
"sentenciaDeclarativa : declaracionFuncion",
"sentenciaDeclarativa : declaracion ','",
"sentenciaDeclarativa : declaracionClase",
"sentenciaDeclarativaMetodo : declaracionFuncionLocal",
"sentenciaDeclarativaMetodo : declaracion ','",
"asignacion : ID '=' expresion",
"asignacion : ID IGUAL expresion",
"sentenciaIf : IF '(' condicion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}' END_IF",
"sentenciaIf : IF '(' condicion ')' '{' bloque_ejecucion '}' ELSE '{' bloque_ejecucion '}'",
"sentenciaIf : IF '(' condicion ')' '{' bloque_ejecucion '}' END_IF",
"sentenciaIf : IF '(' condicion ')' '{' bloque_ejecucion '}'",
"sentenciaIf : IF '(' condicion ')' sentenciaEjecutable ',' ELSE sentenciaEjecutable ',' END_IF",
"sentenciaIf : IF '(' condicion ')' sentenciaEjecutable ',' ELSE '{' bloque_ejecucion '}' END_IF",
"sentenciaIf : IF '(' condicion ')' sentenciaEjecutable ',' END_IF",
"condicion : expresion comparador expresion",
"condicion : comparador expresion",
"condicion : expresion comparador",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return ',' '}' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return ',' '}'",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}'",
"sentenciaIfRetorno : IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE sentencia_ejecutable_return ',' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE '{' bloque_ejecucion_return ',' '}' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' sentencia_ejecutable_return ',' END_IF",
"sentencia_ejecutable_return : print",
"sentencia_ejecutable_return : asignacion",
"sentencia_ejecutable_return : sentenciaIfRetorno",
"sentencia_ejecutable_return : sentenciaWhile",
"sentencia_ejecutable_return : invocacionMetodo",
"sentencia_ejecutable_return : referenciaClase",
"sentencia_ejecutable_return : return",
"bloque_ejecucion : bloque_ejecucion sentenciaEjecutable ','",
"bloque_ejecucion : sentenciaEjecutable ','",
"bloque_ejecucion_return : bloque_ejecucion_return sentencia_ejecutable_return ','",
"bloque_ejecucion_return : sentencia_ejecutable_return ','",
"sentenciaWhile : WHILE '(' condicion ')' DO '{' bloque_ejecucion '}'",
"sentenciaWhile : WHILE '(' condicion ')' DO sentenciaEjecutable",
"print : PRINT CADENA",
"print : CADENA",
"return : RETURN",
"parametro : tipo ID",
"encabezadoMetodo : VOID ID '(' parametro ')'",
"encabezadoMetodo : VOID ID '(' ID ')'",
"encabezadoMetodo : VOID ID '(' ')'",
"declaracionMetodo : encabezadoMetodo '{' cuerpoMetodo '}'",
"encabezadoFuncion : VOID ID '(' parametro ')'",
"encabezadoFuncion : VOID ID '(' ID ')'",
"encabezadoFuncion : VOID ID '(' ')'",
"declaracionFuncion : encabezadoFuncion '{' cuerpoFuncion '}'",
"declaracionFuncionLocal : encabezadoFuncion '{' cuerpoFuncion '}'",
"cuerpoMetodo : listaSentenciasMetodo",
"cuerpoFuncion : listaSentenciasFuncion",
"listaSentenciasMetodo : listaSentenciasMetodo sentenciaDeclarativaMetodo",
"listaSentenciasMetodo : listaSentenciasMetodo sentenciaEjecutable ','",
"listaSentenciasMetodo : sentenciaDeclarativaMetodo",
"listaSentenciasMetodo : sentenciaEjecutable ','",
"listaSentenciasFuncion : listaSentenciasFuncion sentenciaDeclarativaMetodo",
"listaSentenciasFuncion : listaSentenciasFuncion sentencia_ejecutable_return ','",
"listaSentenciasFuncion : sentenciaDeclarativaMetodo",
"listaSentenciasFuncion : sentencia_ejecutable_return ','",
"invocacionMetodo : ID '(' expresion ')'",
"invocacionMetodo : ID '(' ')'",
"invocacionMetodo : ID '(' asignacion ')'",
"invocacionMetodo : ID '(' tipo asignacion ')'",
"declaracion : tipo listaDeclaracion",
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

//#line 510 "gramatica.y"

public static Nodo raiz = null;
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
    			else{
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
//#line 952 "Parser.java"
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
{raiz = new Nodo("program", (Nodo) val_peek(1).obj , null);
				  System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");
				  chequeoAsignacionVariables();}
break;
case 2:
//#line 24 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Programa vacio");}
break;
case 3:
//#line 25 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de apertura '{'");}
break;
case 4:
//#line 26 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta llave de cierre '}'");}
break;
case 5:
//#line 29 "gramatica.y"
{yyval = val_peek(0);}
break;
case 6:
//#line 30 "gramatica.y"
{ if (val_peek(0).obj != null)
						yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(1).obj, (Nodo) val_peek(0).obj));}
break;
case 7:
//#line 34 "gramatica.y"
{yyval = new ParserVal(null);}
break;
case 8:
//#line 35 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 9:
//#line 38 "gramatica.y"
{var t = AnalizadorLexico.TS;
                           int clave = t.obtenerSimbolo(val_peek(0).sval + Parser.ambito);
                           if (clave != t.NO_ENCONTRADO)
                           	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                	yyerror("El nombre de la clase " + val_peek(0).sval +  " ya fue utilizado en este ambito");
                                else { /*la tabla de simbolos contiene la clase pero no tiene el uso asignado.*/
                                        t.agregarAtributo(clave, "uso", "nombre_clase");
                                }
                           else {
                                t.agregarSimbolo(val_peek(0).sval + Parser.ambito);
                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_clase");
                           }
                           if (val_peek(5).sval.equals(!Parser.ambito.isBlank() ? Parser.ambito.substring(1) : Parser.ambito))
                           	yyerror("No se puede declarar una clase con el mismo nombre que el de su ambito");
                           yyval = new ParserVal(new Nodo("EncabezadoClase", new Nodo(val_peek(0).sval + Parser.ambito), null));
                           lista_clases.add(val_peek(0).sval + Parser.ambito.replace(':','_'));
                           claseActual = val_peek(0).sval;
                           ambitoClase = Parser.ambito;
                           agregarAmbito(claseActual);
                          }
break;
case 10:
//#line 60 "gramatica.y"
{lista_clases_fd.remove(Parser.ambito.substring(1));
							chequeoAsignacionVariables();
                                                        salirAmbito();
                                                        claseActual = "";
							System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 11:
//#line 65 "gramatica.y"
{lista_clases_fd.add(Parser.ambito.substring(1));
		 			chequeoAsignacionVariables();
                                        salirAmbito();
                                        claseActual = "";
                                        ambitoClase = "";
		 			System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 12:
//#line 71 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 14:
//#line 75 "gramatica.y"
{ yyval = new ParserVal( new Nodo("cuerpoClase", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj));}
break;
case 15:
//#line 76 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 17:
//#line 81 "gramatica.y"
{yyval = val_peek(0);}
break;
case 18:
//#line 82 "gramatica.y"
{yyval = val_peek(0);}
break;
case 19:
//#line 85 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación a atributo de clase");
				   var t = AnalizadorLexico.TS;
                                   variableAmbitoClase = ":" + val_peek(2).sval + ambitoClase;
                                   chequearHerenciaVariable(variableAmbitoClase);
                                   var x = new Nodo("Asignacion", new Nodo(variableAmbitoClase, t.obtenerAtributo(t.obtenerSimbolo(variableAmbitoClase), "tipo")), (Nodo) val_peek(0).obj);
                                   x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));
                                   yyval = new ParserVal(x);
				  }
break;
case 20:
//#line 95 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Llamado a metodo de clase");
                              var t = AnalizadorLexico.TS;
                              variableAmbitoClase = ":" + val_peek(2).sval + ambitoClase;
                              chequearHerenciaVariable(variableAmbitoClase);
                              var x = new Nodo("LlamadaFuncion", new Nodo(variableAmbitoClase, null, null, "void"), null);
                              yyval = new ParserVal(x);
                              chequearLlamadoFuncion(val_peek(2).sval);
                              }
break;
case 22:
//#line 106 "gramatica.y"
{variableAmbitoClase = ":" + val_peek(1).sval + variableAmbitoClase;}
break;
case 23:
//#line 107 "gramatica.y"
{variableAmbitoClase = ":"+ val_peek(1).sval + variableAmbitoClase;}
break;
case 24:
//#line 111 "gramatica.y"
{
                                             var t = AnalizadorLexico.TS;
                                             lista_variables
                                                 .forEach( x ->
                                                     {
                                                       int clave = t.obtenerSimbolo(x);
                                                       if (clave != t.NO_ENCONTRADO){
                                                         if (t.obtenerAtributo(clave, "tipo") == t.NO_ENCONTRADO_MESSAGE) {
                                                         	t.agregarAtributo(clave, "uso", "variable");
                                                         	t.agregarAtributo(clave, "tipo", val_peek(1).sval);
                                                         } else {
                                                           	yyerror("La variable declarada ya existe " + (x.contains(":") ? x.substring(0, x.indexOf(':')) : "en ambito global"));
                                                       	 }
                                                       } else {
                                                       		t.agregarSimbolo(x);
                                                       		t.agregarAtributo(t.obtenerID(), "tipo", val_peek(1).sval);
                                                       		t.agregarAtributo(t.obtenerID(), "uso", "variable");
                                                       }
                                                     });
                                                 lista_variables.clear();
                          		}
break;
case 26:
//#line 135 "gramatica.y"
{ lista_variables.add(val_peek(2).sval + Parser.ambito); }
break;
case 27:
//#line 136 "gramatica.y"
{ lista_variables.add(val_peek(1).sval + Parser.ambito); }
break;
case 28:
//#line 137 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
break;
case 29:
//#line 140 "gramatica.y"
{ var t = AnalizadorLexico.TS;
			 int clave = t.obtenerSimbolo(val_peek(1).sval+Parser.ambito);
 		       	 if (clave != t.NO_ENCONTRADO){
                         	if (t.obtenerAtributo(clave, "tipo") == t.NO_ENCONTRADO_MESSAGE) {
                         		t.agregarAtributo(clave, "uso", "herencia");
                                } else {
                                	yyerror("La variable declarada ya existe en ambito global");
                                }
                         } else {
                                 t.agregarSimbolo(val_peek(1).sval+Parser.ambito);
                                 t.agregarAtributo(t.obtenerID(), "uso", "herencia");
                         }
                         t.aplicarHerencia(val_peek(1).sval, claseActual, Parser.ambito);
			 chequearNivelesHerencia(claseActual+Parser.ambito);
 		       }
break;
case 30:
//#line 157 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 31:
//#line 158 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 32:
//#line 159 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 33:
//#line 160 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 34:
//#line 161 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 35:
//#line 162 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 41:
//#line 175 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");
			      var x = new Nodo("Asignacion", new Nodo(getVariableConAmbitoTS(val_peek(2).sval), getTipoVariableConAmbitoTS(val_peek(2).sval)), (Nodo) val_peek(0).obj);
			      x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));
			      yyval = new ParserVal(x);
			      variables_no_asignadas.remove(val_peek(2).sval + Parser.ambito);}
break;
case 42:
//#line 180 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 43:
//#line 183 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
												yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(9).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null)))); }
break;
case 44:
//#line 185 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 45:
//#line 186 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(5).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
										}
break;
case 46:
//#line 189 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 47:
//#line 190 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(7).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(5).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
													   }
break;
case 48:
//#line 193 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(8).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
													    }
break;
case 49:
//#line 196 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(4).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
									      }
break;
case 50:
//#line 201 "gramatica.y"
{ var x = new Nodo(val_peek(1).sval, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj, null);
					    x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
					    yyval = new ParserVal(x);
					  }
break;
case 51:
//#line 205 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el primer miembro de la condicion");}
break;
case 52:
//#line 206 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el segundo miembro de la condicion");}
break;
case 53:
//#line 209 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 54:
//#line 210 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 55:
//#line 211 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 56:
//#line 212 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 57:
//#line 213 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 58:
//#line 214 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 59:
//#line 215 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 60:
//#line 218 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 61:
//#line 219 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 62:
//#line 220 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 63:
//#line 221 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 64:
//#line 222 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 65:
//#line 223 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 66:
//#line 224 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 67:
//#line 227 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj)); }
break;
case 68:
//#line 228 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 69:
//#line 232 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj)); }
break;
case 70:
//#line 233 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 71:
//#line 236 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
								     yyval = new ParserVal( new Nodo("while", (Nodo) val_peek(5).obj, (Nodo) val_peek(1).obj));
								    }
break;
case 72:
//#line 239 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
               							 yyval = new ParserVal( new Nodo("while", (Nodo) val_peek(3).obj, (Nodo) val_peek(0).obj));
               							}
break;
case 73:
//#line 244 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");
		    yyval = new ParserVal( new Nodo("Print", new Nodo(getVariableConAmbitoTS(val_peek(0).sval)), null, "string"));}
break;
case 74:
//#line 246 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
break;
case 75:
//#line 249 "gramatica.y"
{yyval = new ParserVal( new Nodo(val_peek(0).sval, null));}
break;
case 76:
//#line 252 "gramatica.y"
{yyval = new ParserVal( new Nodo(val_peek(0).sval, val_peek(1).sval));}
break;
case 77:
//#line 255 "gramatica.y"
{ System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");
                  					     var t = AnalizadorLexico.TS;
                  					     int clave = t.obtenerSimbolo(val_peek(3).sval + Parser.ambito);
                                                               if (clave != t.NO_ENCONTRADO)
                                                               	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	yyerror("El nombre del metodo " + val_peek(3).sval +  " ya fue utilizado en este ambito");
                                                                 else { /*la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.*/
                                                                  	t.agregarAtributo(clave, "tipo", "void");
                                                                  	t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                   }
                                                               else {
                                                               	t.agregarSimbolo(val_peek(3).sval + Parser.ambito);
                                                               	t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                               	t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                               }
                                                               if (val_peek(5).sval.equals(!Parser.ambito.isBlank() ? Parser.ambito.substring(1) : Parser.ambito))
                                                               	yyerror("No se puede declarar un metodo con el mismo nombre que el de su ambito");
                                                               yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(3).sval + Parser.ambito), (Nodo) val_peek(1).obj, "void"));
                                                               lista_funciones.add(val_peek(3).sval + Parser.ambito.replace(':','_'));
                                                               agregarAmbito(val_peek(3).sval);
                                                               parametro((Nodo) val_peek(1).obj);
                  					   }
break;
case 78:
//#line 277 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 79:
//#line 278 "gramatica.y"
{ System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");
                  		   			  var t = AnalizadorLexico.TS;
                                                            int clave = t.obtenerSimbolo(val_peek(2).sval + Parser.ambito);
                                                            if (clave != t.NO_ENCONTRADO)
                                                            	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	yyerror("El nombre del metodo " + val_peek(2).sval +  " ya fue utilizado en este ambito");
                                                                  else { /*la tabla de simbolos contiene el metodo pero no tiene el tipo asignado.*/
                                                                          t.agregarAtributo(clave, "tipo", "void");
                                                                          t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                  }
                                                            else {
                                                                  t.agregarSimbolo(val_peek(2).sval + Parser.ambito);
                                                                  t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                                  t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                            }
                                                            if (val_peek(5).sval.equals(!Parser.ambito.isBlank() ? Parser.ambito.substring(1) : Parser.ambito))
                                                            	yyerror("No se puede declarar un metodo con el mismo nombre que el de su ambito");
                                                            yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(2).sval + Parser.ambito), null, "void"));
                                                            lista_funciones.add(val_peek(2).sval + Parser.ambito.replace(':','_'));
                                                            agregarAmbito(val_peek(2).sval);
                                                            }
break;
case 80:
//#line 301 "gramatica.y"
{ System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Función");
                                                           yyval = new ParserVal(
                                                           new Nodo( "Funcion",
                                                                                    (Nodo) val_peek(3).obj ,
                                                                                    (Nodo) val_peek(2).obj ,
                                                                                    "void"));

                                                           /* Acciones de desapilar */
                                                           if (!verificarRetornoArbol((Nodo) val_peek(1).obj))
                                                           	yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                           chequeoAsignacionVariables();
                                                           salirAmbito();
                                                           funcLocales = 0;}
break;
case 81:
//#line 316 "gramatica.y"
{
					     var t = AnalizadorLexico.TS;
					     int clave = t.obtenerSimbolo(val_peek(3).sval + Parser.ambito);
                                             if (clave != t.NO_ENCONTRADO)
                                             	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                	yyerror("El nombre de la funcion " + val_peek(3).sval +  " ya fue utilizado en este ambito");
                                                else { /*la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.*/
                                                	t.agregarAtributo(clave, "tipo", "void");
                                                	t.agregarAtributo(clave, "uso", "nombre_funcion");
                                                 }
                                             else {
                                             	t.agregarSimbolo(val_peek(3).sval + Parser.ambito);
                                             	t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                             	t.agregarAtributo(t.obtenerID(), "uso", "nombre_funcion");
                                             }
                                             if (val_peek(5).sval.equals(!Parser.ambito.isBlank() ? Parser.ambito.substring(1) : Parser.ambito))
                                             	yyerror("No se puede declarar una funcion con el mismo nombre que el de su ambito");
                                             yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(3).sval + Parser.ambito), (Nodo) val_peek(1).obj, "void"));
                                             lista_funciones.add(val_peek(3).sval + Parser.ambito.replace(':','_'));
                                             agregarAmbito(val_peek(3).sval);
                                             parametro((Nodo) val_peek(1).obj);
					   }
break;
case 82:
//#line 338 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 83:
//#line 339 "gramatica.y"
{
		   			  var t = AnalizadorLexico.TS;
                                          int clave = t.obtenerSimbolo(val_peek(2).sval + Parser.ambito);
                                          if (clave != t.NO_ENCONTRADO)
                                          	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                	yyerror("El nombre de la funcion " + val_peek(2).sval +  " ya fue utilizado en este ambito");
                                                else { /*la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.*/
                                                        t.agregarAtributo(clave, "tipo", "void");
                                                        t.agregarAtributo(clave, "uso", "nombre_funcion");
                                                }
                                          else {
                                                t.agregarSimbolo(val_peek(2).sval + Parser.ambito);
                                                t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_funcion");
                                          }
                                          if (val_peek(5).sval.equals(!Parser.ambito.isBlank() ? Parser.ambito.substring(1) : Parser.ambito))
                                          	yyerror("No se puede declarar una funcion con el mismo nombre que el de su ambito");
                                          yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(2).sval + Parser.ambito), null, "void"));
                                          lista_funciones.add(val_peek(2).sval + Parser.ambito.replace(':','_'));
                                          agregarAmbito(val_peek(2).sval);
                                          }
break;
case 84:
//#line 362 "gramatica.y"
{ System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Función");
                                                            yyval = new ParserVal(
                                                            new Nodo( "Funcion",
                                                                         	    (Nodo) val_peek(3).obj ,
                                                                                    (Nodo) val_peek(2).obj ,
                                                                                    		    "void"));

                                                            /* Acciones de desapilar */
                                                            if (!verificarRetornoArbol((Nodo) val_peek(1).obj))
                                                            	yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                            chequeoAsignacionVariables();
                                                            salirAmbito();}
break;
case 85:
//#line 376 "gramatica.y"
{ System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Función");
								var x = (Nodo) val_peek(3).obj;
								String nombre = x.getIzq().getNombre();
								funcLocales += 1;
                                                            	if (funcLocales <= 1){
                                                            		yyval = new ParserVal(
                                                                	new Nodo( "Funcion",
                                                                         	    (Nodo) val_peek(3).obj ,
                                                                                    (Nodo) val_peek(2).obj ,
                                                                                    		    "void"));

                                                                	/* Acciones de desapilar */
                                                                	if (!verificarRetornoArbol((Nodo) val_peek(1).obj))
                                                                		yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                                	chequeoAsignacionVariables();
                                                                	salirAmbito();
                                                            	}
                                                            	else
                                                            		yyerror("En la función: '"+nombre+"' el nivel maximo de anidamiento (1) es superado.");}
break;
case 86:
//#line 397 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 87:
//#line 400 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 89:
//#line 404 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj));}
break;
case 91:
//#line 406 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", null, (Nodo) val_peek(1).obj));}
break;
case 93:
//#line 410 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj));}
break;
case 95:
//#line 412 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", null, (Nodo) val_peek(1).obj));}
break;
case 96:
//#line 415 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
					var x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS(val_peek(3).sval)), null, "void");
					yyval = new ParserVal(x);
					chequearLlamadoFuncion(val_peek(3).sval);
					}
break;
case 97:
//#line 420 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
		  		var x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS(val_peek(2).sval)), null, "void");
		  		yyval = new ParserVal(x);
		  		chequearLlamadoFuncion(val_peek(2).sval);
		  		}
break;
case 98:
//#line 425 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 99:
//#line 426 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 100:
//#line 430 "gramatica.y"
{
                     var t = AnalizadorLexico.TS;
                     lista_variables
                         .forEach( x ->
                             {
                               int clave = t.obtenerSimbolo(x);
                               if (clave != t.NO_ENCONTRADO){
                                 if (t.obtenerAtributo(clave, "tipo") == t.NO_ENCONTRADO_MESSAGE) {
                                 	t.agregarAtributo(clave, "uso", "variable");
                                 	t.agregarAtributo(clave, "tipo", val_peek(1).sval);
                                 } else {
                                   	yyerror("La variable declarada ya existe " + (x.contains(":") ? x.substring(0, x.indexOf(':')) : "en ambito global"));
                               	 }
                               } else {
                               		t.agregarSimbolo(x);
                               		t.agregarAtributo(t.obtenerID(), "tipo", val_peek(1).sval);
                               		t.agregarAtributo(t.obtenerID(), "uso", "variable");
                               }
                             });
                         lista_variables.clear();
             	}
break;
case 101:
//#line 454 "gramatica.y"
{  lista_variables.add(val_peek(2).sval + Parser.ambito);
 					     variables_no_asignadas.add(val_peek(2).sval + Parser.ambito);
 					  }
break;
case 102:
//#line 457 "gramatica.y"
{ lista_variables.add(val_peek(0).sval + Parser.ambito);
		       variables_no_asignadas.add(val_peek(0).sval + Parser.ambito);
		     }
break;
case 103:
//#line 462 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 104:
//#line 463 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 105:
//#line 464 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 106:
//#line 465 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 107:
//#line 468 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 108:
//#line 469 "gramatica.y"
{ var x = new Nodo("+", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj, null);
                                  x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
                                  yyval = new ParserVal(x); }
break;
case 109:
//#line 472 "gramatica.y"
{ var x = new Nodo("-", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj, null);
    				  x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
    				  yyval = new ParserVal(x); }
break;
case 110:
//#line 477 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 111:
//#line 478 "gramatica.y"
{ var x = new Nodo("*", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj);
    			       x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
    			       yyval = new ParserVal(x); }
break;
case 112:
//#line 481 "gramatica.y"
{ var x = new Nodo("/", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj);
                               x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
                               yyval = new ParserVal(x); }
break;
case 113:
//#line 486 "gramatica.y"
{String x = getTipoVariableConAmbitoTS(val_peek(0).sval);
            if (x != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
            	yyval =  new ParserVal( new Nodo(getVariableConAmbitoTS(val_peek(0).sval), x));
            else{
            	yyval =  new ParserVal( new Nodo("variableNoEncontrada", x));
                yyerror("No se encontro esta variable en un ambito adecuado");
                }
            }
break;
case 115:
//#line 495 "gramatica.y"
{ yyval = new ParserVal( new Nodo(val_peek(0).sval, getTipo(val_peek(0).sval))); }
break;
case 116:
//#line 496 "gramatica.y"
{ String x = comprobarRango(val_peek(0).sval); yyval = new ParserVal( new Nodo(x, getTipo(x))); }
break;
case 117:
//#line 497 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 118:
//#line 500 "gramatica.y"
{ yyval = new ParserVal(">="); }
break;
case 119:
//#line 501 "gramatica.y"
{ yyval = new ParserVal("<="); }
break;
case 120:
//#line 502 "gramatica.y"
{ yyval = new ParserVal("=="); }
break;
case 121:
//#line 503 "gramatica.y"
{ yyval = new ParserVal("!!"); }
break;
case 122:
//#line 504 "gramatica.y"
{ yyval = new ParserVal("<"); }
break;
case 123:
//#line 505 "gramatica.y"
{ yyval = new ParserVal("<"); }
break;
case 124:
//#line 506 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
break;
//#line 1811 "Parser.java"
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
