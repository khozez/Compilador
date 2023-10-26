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
   15,   15,   15,   15,   15,   15,   15,   24,   24,   24,
   24,   24,   24,   24,   23,   23,   23,   16,   16,   16,
   16,   17,   17,   25,    9,    9,   18,   18,   18,   26,
   26,   26,   26,   26,   27,   27,   27,   27,   12,   12,
   12,   12,   19,   19,   28,   28,   13,   13,   13,   13,
   21,   21,   21,   29,   29,   29,   30,   30,   30,   30,
   30,   22,   22,   22,   22,   22,   22,   22,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    3,    1,    2,    5,
    3,    2,    1,    2,    3,    2,    1,    3,    3,    3,
    4,    2,    3,    2,    3,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    1,    1,    2,    3,    3,
   14,   13,   10,    9,   12,   13,    9,   17,   16,   11,
   10,   12,   15,    9,    3,    2,    1,   10,    8,    9,
    7,    2,    1,    1,    9,    7,    9,    8,    7,    3,
    3,    3,    1,    2,    2,    3,    1,    2,    4,    3,
    4,    5,    2,    2,    3,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    3,    3,    1,    2,    1,    2,
    3,    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   63,    0,    0,    0,    0,   87,   88,   89,    0,
    0,    0,    0,    0,    8,    0,   36,   33,   28,   32,
    0,   29,   30,   31,   34,    0,    0,    0,    0,    0,
    0,    0,   62,    0,    0,    2,    0,    3,    6,    0,
    9,    0,   84,   83,   35,    0,   99,    0,    0,    0,
    0,   94,    0,   18,   19,   20,    0,    0,   80,    0,
    0,    0,    0,   11,    0,    0,    0,    1,    7,    0,
   98,  100,    0,    0,    0,    0,    0,   81,    0,    0,
   79,    0,    0,    0,    0,   17,    0,    0,  102,  103,
  104,  105,  108,  106,  107,    0,    0,    0,    0,    0,
    0,   85,  101,    0,    0,   95,   96,   82,   22,    0,
   10,   14,    0,   16,    0,    0,    0,    0,    0,    0,
    0,    0,   15,   23,    0,    0,    0,    0,    0,   37,
    0,   77,    0,    0,    0,    0,    0,    0,   90,    0,
    0,    0,   21,    0,    0,    0,    0,   78,   38,    0,
   69,   64,    0,   75,    0,    0,    0,    0,   61,    0,
    0,    0,   26,    0,   57,    0,    0,    0,   68,    0,
    0,   76,   72,   70,    0,    0,    0,   59,    0,    0,
   25,   56,    0,    0,    0,   47,    0,   67,   60,    0,
   66,    0,    0,   43,   55,    0,    0,    0,   58,    0,
    0,    0,    0,    0,   65,    0,    0,   45,    0,    0,
    0,   46,    0,    0,   41,    0,    0,   54,    0,    0,
    0,    0,   50,    0,    0,    0,    0,   52,    0,    0,
    0,    0,    0,   53,    0,   48,
};
final static short yydgoto[] = {                         12,
   13,   14,  165,  166,   17,   84,   85,   86,   87,   18,
   19,   20,   21,  143,   22,   23,   24,   25,   26,  132,
   50,   96,  167,  133,  156,  134,  135,   44,   51,   52,
};
final static short yysindex[] = {                       -75,
  -27,    0, -217,   38, -175, -168,    0,    0,    0,   61,
  134,    0,  -22,  226,    0,   62,    0,    0,    0,    0,
 -148,    0,    0,    0,    0,   71,  -10, -139,  -10,   29,
   -6,  -10,    0,   88,  -10,    0,    5,    0,    0,   93,
    0,  -17,    0,    0,    0, -135,    0, -114,  -10,   51,
   18,    0,  -27,    0,    0,    0,   51,  -44,    0,  111,
 -107,  110,  -48,    0,    6,  -41,    6,    0,    0, -100,
    0,    0,  138,  -10,  -10,  -10,  -10,    0,  -43,  121,
    0,  124,  -79,   63,  -48,    0,  143,  -56,    0,    0,
    0,    0,    0,    0,    0,  -10,  180,   85,  -35,   -4,
  164,    0,    0,   18,   18,    0,    0,    0,    0,  184,
    0,    0,  192,    0,   -5,  155,  105,  239,  208,  -21,
  286,  -16,    0,    0,    7,  -62,  239,  217,  219,    0,
  238,    0,  240,  160,  212,  165,   42,   17,    0,  171,
   45,   43,    0,  226,  260,  185,  -10,    0,    0,  252,
    0,    0,  271,    0,  278,  284,  239,  226,    0,   48,
  239,  291,    0,    7,    0,  297,  147, -142,    0,    6,
  252,    0,    0,    0,  211,  158,  226,    0,  218,  221,
    0,    0,  -90,  302,   57,    0,  -10,    0,    0,  167,
    0,  239,  229,    0,    0,  226,  312,  292,    0,  232,
  226,  183,   95, -103,    0,  194,   98,    0,  226,  318,
  100,    0,   90,  -87,    0,  246,   81,    0,  -57,  226,
  320,  249,    0,  109,  112,  226,  330,    0,  109,  256,
  333,  119,  258,    0,  122,    0,
};
final static short yyrindex[] = {                         0,
  127,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   24,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,  386,    0,    0,    0,
    0,  344,    0,    0,    0,  -39,    0,    0,    0,   15,
  -34,    0,    0,    0,    0,    0,   41,   34,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  127,    0,    0,  264,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  127,    0,    0,    0,
  344,    0,    0,  -29,    2,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -54,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  265,    0,    0,    0,    0,    0,
    0,  200,    0,    0,    0,    0,    0,    0,    0,  272,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  280,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  352,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  363,    0,    0,    0,    0,    0,    0,    0,  365,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  366,    0,
};
final static short yygindex[] = {                         0,
  401,    0,  125,  163,    0,    0,    0,  328,  329,  390,
   70,  392,   20,  257,    0,    0,    0,  -28,  -23,  -78,
   23,  -38,  -85,  288, -116,  -47,  275,  359,   72,   83,
};
final static int YYTABLESIZE=521;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         98,
   12,   97,   97,   97,   97,   97,   91,   97,   91,   91,
   91,   92,   30,   92,   92,   92,   29,   29,   28,  209,
   97,   97,   97,    5,  140,   91,   91,   91,  100,   49,
   92,   92,   92,   29,   48,   49,  120,   64,  124,   31,
   48,   70,   93,   29,   93,   93,   93,   11,   74,   61,
   75,   57,   62,  125,   65,   40,  154,   67,   40,   76,
  144,   93,   93,   93,   77,   94,   93,   95,   49,   59,
   24,   73,  176,   48,   97,   97,   97,   32,   97,  146,
   97,   39,   88,   33,   39,   99,  163,  210,   34,  130,
   43,  190,  154,   74,  131,   75,  216,   55,  130,   60,
   35,  164,   38,  131,   88,   41,  130,  227,   42,  175,
  202,  131,  231,  179,   45,  206,   63,   53,  116,  185,
  186,  130,  121,  213,   15,   12,  131,   66,  130,   68,
   80,  187,  130,  131,  224,   15,   69,  131,   39,   71,
  229,  141,  130,   72,  200,  104,  105,  131,    5,   79,
   81,   78,   74,   53,   75,    2,  101,    4,  106,  107,
    5,  108,   16,  130,  158,   10,  152,  109,  131,  170,
  177,  193,  194,   16,  217,  218,   40,  110,  103,  196,
   74,    1,   75,    2,    3,    4,  114,  111,    5,    6,
    7,    8,    9,   10,   53,  126,    2,   74,    4,   75,
  115,    5,   24,  220,  222,  223,   10,  118,   82,  198,
   24,   24,   24,   24,  183,   97,   83,    7,    8,    9,
  117,  119,   70,  122,    7,    8,    9,  127,   27,   27,
   71,   97,   97,   97,   97,  123,   91,   91,   91,   91,
  139,   92,   92,   92,   92,   27,   46,   47,  136,    7,
    8,    9,   46,   47,  137,   27,  147,   12,   36,   12,
   12,   12,  148,  142,   12,   12,   12,   12,   12,   12,
   12,  183,   93,   93,   93,   93,   89,   90,   91,   92,
  129,  149,  189,  150,  151,   58,   47,  157,  145,  129,
   90,  199,  160,  161,    7,    8,    9,  153,   53,  159,
    2,  162,    4,  168,   53,    5,    2,  207,    4,  169,
   10,    5,  129,   53,  172,    2,   10,    4,  211,  129,
    5,  173,  178,  129,   27,   10,  138,  174,   74,  184,
   75,  180,  204,  153,   74,  188,   75,   53,  184,    2,
  182,    4,  191,  192,    5,  195,   53,  197,    2,   10,
    4,  201,  184,    5,  129,  203,  205,  208,   10,  152,
  212,  214,  215,  225,  184,   53,  145,    2,  184,    4,
  219,  226,    5,  230,  228,  184,  233,   10,  152,  221,
  232,  234,  235,   90,  236,    4,  184,   86,   13,   73,
    1,  184,    2,    3,    4,   44,   74,    5,    6,    7,
    8,    9,   10,   53,   71,    2,   42,    4,   51,   49,
    5,   37,  112,  113,   53,   10,    2,   54,    4,   56,
  181,    5,  155,   53,  171,    2,   10,    4,  102,    0,
    5,    0,    0,    0,    0,   10,    0,    0,    0,   53,
    0,    2,    0,    4,    0,    0,    5,    0,    0,    0,
   53,   10,    2,    0,    4,    0,   27,    5,    0,    0,
    0,    0,   10,    0,   27,   27,   27,   27,    1,    0,
    2,    0,  128,    0,    0,    5,    6,    7,    8,    9,
   10,  152,    1,    0,    2,    3,    4,    0,    0,    5,
    6,    7,    8,    9,   10,    1,    0,    2,    0,  128,
    0,    0,    5,    6,    7,    8,    9,   10,    1,    0,
    2,    0,    4,    0,    0,    5,    6,    7,    8,    9,
   10,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   44,   45,   41,   47,   43,   44,
   45,   41,   40,   43,   44,   45,   61,   61,   46,  123,
   60,   61,   62,    0,   41,   60,   61,   62,   67,   40,
   60,   61,   62,   61,   45,   40,   41,   44,   44,  257,
   45,   59,   41,   61,   43,   44,   45,  123,   43,   30,
   45,   29,   30,   59,   32,   41,  135,   35,   44,   42,
  123,   60,   61,   62,   47,   60,   61,   62,   40,   41,
  125,   49,  158,   45,   41,   42,   43,   40,   45,  127,
   47,   41,   63,  259,   44,   66,   44,  204,  257,  118,
   21,  177,  171,   43,  118,   45,  213,   28,  127,   30,
   40,   59,  125,  127,   85,   44,  135,  224,  257,  157,
  196,  135,  229,  161,   44,  201,  123,  257,   96,  262,
  263,  150,  100,  209,    0,  125,  150,   40,  157,  125,
   61,  170,  161,  157,  220,   11,   44,  161,   14,  275,
  226,  122,  171,  258,  192,   74,   75,  171,  125,  257,
   41,   41,   43,  257,   45,  259,  257,  261,   76,   77,
  264,   41,    0,  192,  123,  269,  270,   44,  192,  147,
  123,  262,  263,   11,  262,  263,   14,  257,   41,  123,
   43,  257,   45,  259,  260,  261,   44,  125,  264,  265,
  266,  267,  268,  269,  257,   41,  259,   43,  261,   45,
  257,  264,  257,  123,  262,  263,  269,  123,  257,  187,
  265,  266,  267,  268,  125,  257,  265,  266,  267,  268,
   41,  257,   59,   40,  266,  267,  268,  123,  273,  273,
  275,  271,  272,  273,  274,   44,  271,  272,  273,  274,
  257,  271,  272,  273,  274,  273,  257,  258,   41,  266,
  267,  268,  257,  258,  276,  273,   40,  257,  125,  259,
  260,  261,   44,  257,  264,  265,  266,  267,  268,  269,
  270,  125,  271,  272,  273,  274,  271,  272,  273,  274,
  118,   44,  125,   44,  125,  257,  258,  123,  126,  127,
  257,  125,  276,  123,  266,  267,  268,  135,  257,  137,
  259,  257,  261,   44,  257,  264,  259,  125,  261,  125,
  269,  264,  150,  257,   44,  259,  269,  261,  125,  157,
  264,   44,  160,  161,  125,  269,   41,   44,   43,  167,
   45,   41,   41,  171,   43,  125,   45,  257,  176,  259,
   44,  261,  125,  123,  264,   44,  257,  185,  259,  269,
  261,  123,  190,  264,  192,   44,  125,  263,  269,  270,
  263,   44,  263,   44,  202,  257,  204,  259,  206,  261,
  125,  123,  264,   44,  263,  213,   44,  269,  270,  217,
  125,  263,  125,  257,  263,    0,  224,   44,  125,  125,
  257,  229,  259,  260,  261,   44,  125,  264,  265,  266,
  267,  268,  269,  257,  125,  259,   44,  261,   44,   44,
  264,   11,   85,   85,  257,  269,  259,   28,  261,   28,
  164,  264,  135,  257,  150,  259,  269,  261,   70,   -1,
  264,   -1,   -1,   -1,   -1,  269,   -1,   -1,   -1,  257,
   -1,  259,   -1,  261,   -1,   -1,  264,   -1,   -1,   -1,
  257,  269,  259,   -1,  261,   -1,  257,  264,   -1,   -1,
   -1,   -1,  269,   -1,  265,  266,  267,  268,  257,   -1,
  259,   -1,  261,   -1,   -1,  264,  265,  266,  267,  268,
  269,  270,  257,   -1,  259,  260,  261,   -1,   -1,  264,
  265,  266,  267,  268,  269,  257,   -1,  259,   -1,  261,
   -1,   -1,  264,  265,  266,  267,  268,  269,  257,   -1,
  259,   -1,  261,   -1,   -1,  264,  265,  266,  267,  268,
  269,
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
"sentenciaIf : IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' ELSE '{' bloque_ejecucion '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' END_IF",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion return '}' ELSE '{' bloque_ejecucion return ',' '}' END_IF",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion return '}' ELSE '{' bloque_ejecucion return ',' '}'",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion return '}' END_IF",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' '{' bloque_ejecucion return '}'",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' return ',' ELSE sentenciaEjecutable ',' END_IF",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' return ',' ELSE '{' bloque_ejecucion return ',' '}' END_IF",
"sentenciaIfRetorno : IF '(' expresion comparador expresion ')' return ',' END_IF",
"bloque_ejecucion : bloque_ejecucion sentenciaEjecutable ','",
"bloque_ejecucion : sentenciaEjecutable ','",
"bloque_ejecucion : sentenciaDeclarativa",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' DO '{' bloque_ejecucion '}'",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' DO sentenciaEjecutable",
"sentenciaWhile : WHILE '(' expresion comparador ')' DO '{' bloque_ejecucion '}'",
"sentenciaWhile : WHILE '(' expresion comparador ')' DO sentenciaEjecutable",
"print : PRINT CADENA",
"print : CADENA",
"return : RETURN",
"declaracionMetodo : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionMetodo : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' ID ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"cuerpoMetodo : listaSentenciasMetodo return ','",
"cuerpoMetodo : sentenciaIfRetorno ',' listaSentenciasMetodo",
"cuerpoMetodo : listaSentenciasMetodo sentenciaIfRetorno ','",
"cuerpoMetodo : listaSentenciasMetodo",
"cuerpoMetodo : sentenciaIfRetorno ','",
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

//#line 193 "gramatica.y"

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
            valor = "-"+valor;
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
//#line 626 "Parser.java"
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
case 47:
//#line 94 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 48:
//#line 97 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 49:
//#line 98 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 50:
//#line 99 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 51:
//#line 100 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 52:
//#line 101 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 53:
//#line 102 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 54:
//#line 103 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 57:
//#line 108 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una sentencia WHILE no puede contener una sentencia declarativa.");}
break;
case 58:
//#line 111 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 59:
//#line 112 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 60:
//#line 113 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 61:
//#line 114 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 62:
//#line 117 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");}
break;
case 63:
//#line 118 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
break;
case 65:
//#line 124 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 66:
//#line 125 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 67:
//#line 128 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 68:
//#line 129 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 69:
//#line 130 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 73:
//#line 136 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR: Falta la sentencia RETURN");}
break;
case 79:
//#line 146 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
break;
case 80:
//#line 147 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
break;
case 81:
//#line 148 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 82:
//#line 149 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 100:
//#line 179 "gramatica.y"
{comprobarRango(val_peek(0).sval);}
break;
case 108:
//#line 189 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
break;
//#line 955 "Parser.java"
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
