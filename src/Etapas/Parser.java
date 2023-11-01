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
    0,    0,    0,    0,    1,    1,    2,    2,    5,    5,
    5,    6,    7,    7,    7,    7,   10,   10,   10,    8,
    8,    8,    8,   14,   14,   14,    4,    4,    4,    4,
    4,    4,    3,    3,    3,   20,   20,   11,   11,   15,
   15,   15,   15,   15,   15,   15,   22,   22,   22,   25,
   25,   25,   25,   25,   25,   25,   27,   27,   27,   27,
   27,   27,   27,   23,   23,   26,   26,   16,   16,   17,
   17,   28,    9,    9,   18,   18,   18,   29,   30,   30,
   30,   30,   12,   12,   12,   12,   19,   31,   31,   13,
   13,   13,   13,   21,   21,   21,   32,   32,   32,   33,
   33,   33,   33,   33,   24,   24,   24,   24,   24,   24,
   24,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    1,    2,    5,    3,
    2,    1,    2,    3,    2,    1,    3,    3,    3,    4,
    2,    3,    2,    3,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    1,    1,    2,    3,    3,   12,
   11,    8,    7,   10,   11,    7,    3,    2,    2,   13,
   12,    8,    7,   10,   12,    7,    1,    1,    1,    1,
    1,    1,    1,    3,    2,    3,    2,    8,    6,    2,
    1,    1,    9,    7,    9,    8,    7,    1,    2,    3,
    1,    2,    4,    3,    4,    5,    2,    3,    1,    1,
    1,    1,    1,    1,    3,    3,    1,    3,    3,    1,
    2,    1,    2,    3,    1,    1,    1,    1,    1,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,   71,    0,    0,    0,    0,   90,   91,   92,    0,
    0,    0,    0,    5,    7,    0,   35,   32,   27,   31,
    0,   28,   29,   30,   33,    0,    0,    0,    0,    0,
    0,    0,   70,    0,    0,    2,    0,    3,    6,    8,
    0,   87,   34,    0,  102,    0,    0,    0,    0,   97,
    0,   17,   18,   19,    0,    0,   84,    0,    0,    0,
    0,   10,  105,  106,  107,  108,  111,  109,  110,    0,
    0,    0,    0,    0,    1,    0,  101,  103,    0,    0,
    0,    0,    0,   85,    0,    0,   83,    0,    0,    0,
    0,   16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   88,  104,    0,    0,   98,   99,   86,   21,    0,
    9,   13,    0,   15,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   14,   22,    0,    0,    0,    0,    0,
    0,   72,   62,   58,   61,   60,   57,   36,    0,   81,
   59,    0,   63,    0,    0,    0,    0,   69,   93,    0,
    0,    0,   20,   65,    0,    0,    0,   46,    0,    0,
   37,   82,   77,   79,    0,    0,    0,    0,    0,   25,
    0,    0,   42,   64,    0,    0,   76,    0,   80,    0,
   68,    0,    0,   24,    0,    0,    0,    0,   75,   74,
    0,    0,    0,   44,    0,    0,    0,    0,   45,    0,
    0,    0,   73,   40,    0,    0,   67,    0,   56,    0,
   52,   66,    0,    0,    0,    0,    0,    0,    0,   54,
    0,    0,    0,   55,   50,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   90,   91,   92,   93,   18,
   19,   20,   21,  153,   22,   23,   24,  138,  139,  140,
   70,   71,  128,   72,  141,  200,  142,  143,  144,  145,
   42,   49,   50,
};
final static short yysindex[] = {                        76,
  -23,    0, -208,   13, -189, -180,    0,    0,    0,   57,
  110,    0,  165,    0,    0,   60,    0,    0,    0,    0,
 -157,    0,    0,    0,    0,   64,   -6, -133,   -6,  -12,
  -25,  -40,    0,   91,  -40,    0,  179,    0,    0,    0,
   80,    0,    0, -130,    0, -110,   -6,   15,   65,    0,
  -23,    0,    0,    0,   15,  -59,    0,  112, -102,   70,
  241,    0,    0,    0,    0,    0,    0,    0,    0,   11,
  120,   -6,  -38,  132,    0, -157,    0,    0,  152,   -6,
   -6,   -6,   -6,    0,  -26,  155,    0,  131,  -56,   86,
  241,    0,  171,  -37,   -6,  -57,   15,  180,  100,  -32,
  -50,    0,    0,   65,   65,    0,    0,    0,    0,  200,
    0,    0,  204,    0,   -2,   15,   89,  205,  140,  269,
  216,   90,   20,    0,    0,   35,  220,  134, -103,  269,
  231,    0,    0,    0,    0,    0,    0,    0,  251,    0,
    0,  253,    0,  173,  269,  176,   89,    0,    0,  177,
   49,   30,    0,    0,  -97,  264,  104,    0,  184,  -40,
    0,    0,    0,    0,  267,  269,  147,  269,  271,    0,
   35,  190,    0,    0,   89,  275,    0,  280,    0,  201,
    0,  202,  206,    0,   89,  192,   71, -107,    0,    0,
  269,  198,   75,    0, -123,  308,  230,   93,    0,   61,
  313,  -94,    0,    0,  -83,  320,    0,  -87,    0,  243,
    0,    0, -123,  328, -123,   32,  117,   46,  256,    0,
  258,  121,  123,    0,    0,
};
final static short yyrindex[] = {                         0,
  130,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,  388,    0,    0,    0,
  346,    0,    0,  -35,    0,    0,    0,   -4,  -30,    0,
    0,    0,    0,    0,   99,   37,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  130,    0,    0,
  272,    0,    0,    0,  351,    0,  355,  130,    0,    0,
    0,    0,    0,    2,    7,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  203,  358,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  276,    0,    0,    0,    0,    0,
    0,  207,    0,    0,   64,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  361,    0,    0,
    0,    0,    0,    0,  369,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  370,    0,    0,
};
final static short yygindex[] = {                         0,
  389,   28,    0,  403,    0,    0,    0,  324,  327,  -24,
  194,  -10,   14,  248,    0,  297,  356,   81,   88,  278,
   56,  -11,  -52,  350,    0,  -96,  -86,    0,  -75,    0,
  345,  107,  127,
};
final static int YYTABLESIZE=595;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   11,   29,   99,   52,   46,  100,  100,  100,  100,  100,
   94,  100,   94,   94,   94,  195,   30,   54,   62,   68,
   67,   69,   28,   74,  100,  100,  100,   47,   57,   94,
   94,   94,   46,   47,   29,  213,   39,   29,   46,   39,
   39,  125,   95,   59,   95,   95,   95,   96,   31,   96,
   96,   96,   32,   80,  159,   81,  126,   80,  165,   81,
  150,   95,   95,   95,   39,  117,   96,   96,   96,   33,
   68,   67,   69,  170,   94,  219,   34,  100,  100,  100,
   25,  100,   48,  100,   55,   60,  100,   26,  171,  221,
  180,   25,  182,   25,  167,  133,   35,   61,   26,   41,
   26,  196,   79,   40,   94,  133,   82,   43,  201,  135,
   87,   83,   80,  206,   81,  197,  216,   25,  218,  135,
  133,  214,  186,   51,   26,   11,  201,   97,  201,  206,
   73,  206,  192,   51,  135,    2,  151,  131,   76,   38,
    5,  133,   38,  133,   77,   10,  132,   78,  178,   51,
  116,    2,   84,  131,   85,  135,    5,  135,  157,  158,
   96,   10,  132,  133,  172,  173,  133,  208,  209,   51,
  133,    2,  101,  131,  109,  133,    5,  135,  210,  211,
  135,   10,  132,  133,  135,  205,  104,  105,  133,  135,
  133,  133,  103,  133,   80,  108,   81,  135,   11,   51,
  110,    2,  135,    4,  135,  135,    5,  135,  106,  107,
  111,   10,  147,   27,  114,   77,   44,   45,   98,  115,
  119,   53,  120,   58,  121,  122,  175,    7,    8,    9,
   63,   64,   65,   66,   36,  100,  100,  100,  100,  123,
   94,   94,   94,   94,   56,   45,   27,  124,  129,   27,
   44,   45,   86,    7,    8,    9,  146,   11,  155,   11,
   11,   11,  130,  154,   11,   11,   11,   11,   11,   11,
  160,  181,   95,   95,   95,   95,  149,   96,   96,   96,
   96,   63,   64,   65,   66,    7,    8,    9,   51,   38,
    2,  152,  131,   93,  161,    5,  162,  163,  166,  168,
   10,  132,   51,   75,    2,  169,  131,  174,  177,    5,
  179,  183,  185,  134,   10,  132,  193,   51,  187,    2,
  188,  131,  198,  134,    5,  189,  190,   23,  191,   10,
  132,   26,    1,  194,    2,    3,    4,  199,  134,    5,
    6,    7,    8,    9,   10,   51,   51,    2,    2,    4,
    4,  202,    5,    5,  203,  204,  207,   10,   10,  134,
   51,  134,    2,  212,    4,  215,    1,    5,    2,    3,
    4,  217,   10,    5,    6,    7,    8,    9,   10,  220,
  222,  134,  223,  224,  134,  225,   93,    4,  134,   89,
   51,   49,    2,  134,    4,   48,   12,    5,   47,   37,
   78,  134,   10,   51,   41,    2,  134,    4,  134,  134,
    5,  134,   53,   51,  112,   10,  136,  113,  184,   95,
  102,    1,  164,    2,    3,    4,  136,    0,    5,    6,
    7,    8,    9,   10,    0,    1,    0,    2,    3,    4,
    0,  136,    5,    6,    7,    8,    9,   10,   51,    0,
    2,    0,    4,    0,   51,    5,    2,    0,    4,   23,
   10,    5,  136,   26,  136,    0,   10,   23,   23,   23,
   23,   26,   26,   26,   26,  137,    0,    0,    0,    0,
    0,    0,    0,    0,  136,  137,    0,  136,    0,    0,
    0,  136,    0,    0,    0,    0,  136,   88,  118,    0,
  137,    0,    0,    0,  136,   89,    7,    8,    9,  136,
    0,  136,  136,    0,  136,    0,    0,    0,    0,  127,
    0,  137,    0,  137,  148,    1,    0,    2,    0,  131,
  156,    0,    5,    6,    7,    8,    9,   10,  132,    0,
    0,    0,    0,  137,    0,    0,  137,    0,    0,  127,
  137,    0,    0,    0,    0,  137,    0,    0,    0,  176,
    0,    0,    0,  137,    0,    0,    0,    0,  137,  156,
  137,  137,    0,  137,    0,    0,    0,  127,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  127,  156,    0,
    0,    0,    0,    0,  156,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   61,   41,   28,   45,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,  123,   40,   28,   44,   60,
   61,   62,   46,   35,   60,   61,   62,   40,   41,   60,
   61,   62,   45,   40,   61,  123,   41,   61,   45,   44,
   13,   44,   41,   30,   43,   44,   45,   41,  257,   43,
   44,   45,   40,   43,  130,   45,   59,   43,  145,   45,
   41,   60,   61,   62,   37,  123,   60,   61,   62,  259,
   60,   61,   62,   44,   61,   44,  257,   41,   42,   43,
    0,   45,   27,   47,   29,   30,   73,    0,   59,   44,
  166,   11,  168,   13,  147,  120,   40,  123,   11,  257,
   13,  188,   47,   44,   91,  130,   42,   44,  195,  120,
   41,   47,   43,  200,   45,  191,  213,   37,  215,  130,
  145,  208,  175,  257,   37,  125,  213,   72,  215,  216,
   40,  218,  185,  257,  145,  259,  123,  261,   59,   41,
  264,  166,   44,  168,  275,  269,  270,  258,  160,  257,
   95,  259,   41,  261,  257,  166,  264,  168,  262,  263,
   41,  269,  270,  188,  262,  263,  191,  262,  263,  257,
  195,  259,   41,  261,   44,  200,  264,  188,  262,  263,
  191,  269,  270,  208,  195,  125,   80,   81,  213,  200,
  215,  216,   41,  218,   43,   41,   45,  208,  123,  257,
  257,  259,  213,  261,  215,  216,  264,  218,   82,   83,
  125,  269,  123,  273,   44,  275,  257,  258,  257,  257,
   41,   28,  123,   30,  257,  276,  123,  266,  267,  268,
  271,  272,  273,  274,  125,  271,  272,  273,  274,   40,
  271,  272,  273,  274,  257,  258,  273,   44,   44,  273,
  257,  258,   59,  266,  267,  268,   41,  257,  125,  259,
  260,  261,  123,   44,  264,  265,  266,  267,  268,  269,
   40,  125,  271,  272,  273,  274,  257,  271,  272,  273,
  274,  271,  272,  273,  274,  266,  267,  268,  257,  125,
  259,  257,  261,  257,   44,  264,   44,  125,  123,  123,
  269,  270,  257,  125,  259,  257,  261,   44,  125,  264,
   44,   41,  123,  120,  269,  270,  125,  257,   44,  259,
   41,  261,  125,  130,  264,  125,  125,  125,  123,  269,
  270,  125,  257,  263,  259,  260,  261,  263,  145,  264,
  265,  266,  267,  268,  269,  257,  257,  259,  259,  261,
  261,   44,  264,  264,  125,  263,   44,  269,  269,  166,
  257,  168,  259,   44,  261,  123,  257,  264,  259,  260,
  261,   44,  269,  264,  265,  266,  267,  268,  269,  263,
  125,  188,  125,  263,  191,  263,  257,    0,  195,   44,
  257,   41,  259,  200,  261,   41,  125,  264,   41,   11,
  125,  208,  269,  257,   44,  259,  213,  261,  215,  216,
  264,  218,   44,   44,   91,  269,  120,   91,  171,   70,
   76,  257,  145,  259,  260,  261,  130,   -1,  264,  265,
  266,  267,  268,  269,   -1,  257,   -1,  259,  260,  261,
   -1,  145,  264,  265,  266,  267,  268,  269,  257,   -1,
  259,   -1,  261,   -1,  257,  264,  259,   -1,  261,  257,
  269,  264,  166,  257,  168,   -1,  269,  265,  266,  267,
  268,  265,  266,  267,  268,  120,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  188,  130,   -1,  191,   -1,   -1,
   -1,  195,   -1,   -1,   -1,   -1,  200,  257,   96,   -1,
  145,   -1,   -1,   -1,  208,  265,  266,  267,  268,  213,
   -1,  215,  216,   -1,  218,   -1,   -1,   -1,   -1,  117,
   -1,  166,   -1,  168,  122,  257,   -1,  259,   -1,  261,
  128,   -1,  264,  265,  266,  267,  268,  269,  270,   -1,
   -1,   -1,   -1,  188,   -1,   -1,  191,   -1,   -1,  147,
  195,   -1,   -1,   -1,   -1,  200,   -1,   -1,   -1,  157,
   -1,   -1,   -1,  208,   -1,   -1,   -1,   -1,  213,  167,
  215,  216,   -1,  218,   -1,   -1,   -1,  175,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  185,  186,   -1,
   -1,   -1,   -1,   -1,  192,
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
"declaracionMetodo : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionMetodo : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' ID ')' '{' cuerpoMetodo '}'",
"declaracionFuncion : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"cuerpoMetodo : listaSentenciasMetodo",
"listaSentenciasMetodo : listaSentenciasMetodo sentenciaDeclarativaMetodo",
"listaSentenciasMetodo : listaSentenciasMetodo sentencia_ejecutable_return ','",
"listaSentenciasMetodo : sentenciaDeclarativaMetodo",
"listaSentenciasMetodo : sentencia_ejecutable_return ','",
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

//#line 262 "gramatica.y"

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
//#line 788 "Parser.java"
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
{raiz = new Nodo("program", (Nodo) val_peek(1).obj , null); System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");}
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
case 5:
//#line 27 "gramatica.y"
{yyval = val_peek(0)}
break;
case 6:
//#line 28 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(1).obj, (Nodo) val_peek(0).obj));}
break;
case 8:
//#line 32 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 9:
//#line 35 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 10:
//#line 36 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 11:
//#line 37 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 12:
//#line 40 "gramatica.y"
{ yyval = val_peek(0) }
break;
case 18:
//#line 50 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Referencia a atributo de clase.");}
break;
case 23:
//#line 57 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la definición de variable.");}
break;
case 26:
//#line 62 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
break;
case 27:
//#line 66 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 28:
//#line 67 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 29:
//#line 68 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 30:
//#line 69 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 31:
//#line 70 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 32:
//#line 71 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 38:
//#line 84 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");
			      var x = new Nodo("Asignacion", new Nodo(getNombreTablaSimbolosVariables(val_peek(2).sval), getTipoTablaSimbolosVariables(val_peek(2).sval)), (Nodo) val_peek(0).obj);
			      x.setTipo(validarTiposAssign(x.getIzq(), x.getDer()));
			      yyval = new ParserVal(x); }
break;
case 39:
//#line 88 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 40:
//#line 91 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
												yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(9).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null)))); }
break;
case 41:
//#line 93 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 42:
//#line 94 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(5).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
										}
break;
case 43:
//#line 97 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 44:
//#line 98 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(7).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(5).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
													   }
break;
case 45:
//#line 101 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(8).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
													    }
break;
case 46:
//#line 104 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(4).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
									      }
break;
case 47:
//#line 109 "gramatica.y"
{ yyval = new ParserVal(new Nodo(val_peek(1).sval, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj, validarTipos((Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj)));}
break;
case 48:
//#line 110 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el primer miembro de la condicion");}
break;
case 49:
//#line 111 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el segundo miembro de la condicion");}
break;
case 50:
//#line 114 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 51:
//#line 115 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 52:
//#line 116 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 53:
//#line 117 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 54:
//#line 118 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 55:
//#line 119 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 56:
//#line 120 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 57:
//#line 123 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 58:
//#line 124 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 59:
//#line 125 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 60:
//#line 126 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 61:
//#line 127 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 62:
//#line 128 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 63:
//#line 129 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 64:
//#line 132 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj)); }
break;
case 65:
//#line 133 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 66:
//#line 137 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj)); }
break;
case 67:
//#line 138 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 68:
//#line 141 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
								     yyval = new ParserVal( new Nodo("while", (Nodo) val_peek(5).obj, (Nodo) val_peek(1).obj));
								    }
break;
case 69:
//#line 144 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
               							 yyval = new ParserVal( new Nodo("while", (Nodo) val_peek(3).obj, (Nodo) val_peek(0).obj));
               							}
break;
case 70:
//#line 149 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");
		    yyval = new ParserVal( new Nodo("Print", new Nodo(getVariableConAmbitoTS(val_peek(0).sval)), null, "string"));}
break;
case 71:
//#line 151 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
break;
case 73:
//#line 157 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 74:
//#line 158 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 75:
//#line 161 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 76:
//#line 162 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 77:
//#line 163 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 83:
//#line 175 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
					var x = new Nodo("LlamadaFuncion", new Nodo(getNombreTablaSimbolosVariables(val_peek(3).sval)), null, getTipoTablaSimbolosVariables(val_peek(3).sval));
					yyval = new ParserVal(x);
					chequearLlamadoFuncion(val_peek(3).sval);
					}
break;
case 84:
//#line 180 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");
		  		var x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS(val_peek(2).sval)), null, getTipoVariableConAmbitoTS(val_peek(2).sval));
		  		yyval = new ParserVal(x);
		  		chequearLlamadoFuncion(val_peek(2).sval);
		  		}
break;
case 85:
//#line 185 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 86:
//#line 186 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 87:
//#line 190 "gramatica.y"
{
                     var t = TablaSimbolos.getInstance();
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
case 88:
//#line 214 "gramatica.y"
{ if (pilaWhen.isEmpty() || pilaWhen.getFirst()) lista_variables.add(val_peek(2).sval + Parser.ambito); }
break;
case 89:
//#line 215 "gramatica.y"
{ if (pilaWhen.isEmpty() || pilaWhen.getFirst()) lista_variables.add(val_peek(0).sval + Parser.ambito);}
break;
case 90:
//#line 218 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 91:
//#line 219 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 92:
//#line 220 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 93:
//#line 221 "gramatica.y"
{declaracion de clase, que hacemos?}
break;
case 94:
//#line 224 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 95:
//#line 225 "gramatica.y"
{ yyval = new ParserVal(new Nodo("+", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj, validarTipos((Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj))); }
break;
case 96:
//#line 226 "gramatica.y"
{ yyval = new ParserVal(new Nodo("-", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj, validarTipos((Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj))); }
break;
case 97:
//#line 229 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 98:
//#line 230 "gramatica.y"
{ var x = new Nodo("*", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj);
    			       x.setTipo(validarTipos((Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
    			       yyval = new ParserVal(x); }
break;
case 99:
//#line 233 "gramatica.y"
{ var x = new Nodo("/", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj);
                               x.setTipo(validarTipos((Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
                               yyval = new ParserVal(x); }
break;
case 100:
//#line 238 "gramatica.y"
{String x = getTipoVariableConAmbitoTS(val_peek(0).sval);
            if (!x == TablaSimbolos.NO_ENCONTRADO_MESSAGE)
            	yyval =  new ParserVal( new Nodo(getVariableConAmbitoTS(val_peek(0).sval), x));
            else{
            	yyval =  new ParserVal( new Nodo("variableNoEncontrada", x));
                yyerror("No se encontro esta variable en un ambito adecuado");
                }
            }
break;
case 101:
//#line 246 "gramatica.y"
{en este caso debemos actualizar el valor de la variable segun enunciado, PREGUNTAR}
break;
case 102:
//#line 247 "gramatica.y"
{ yyval = new ParserVal( new Nodo(val_peek(0).sval, getTipo(val_peek(0).sval))); }
break;
case 103:
//#line 248 "gramatica.y"
{ String x = comprobarRango(val_peek(0).sval); yyval = new ParserVal( new Nodo(x, getTipo(x))); }
break;
case 104:
//#line 249 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 105:
//#line 252 "gramatica.y"
{ yyval = new ParserVal(">="); }
break;
case 106:
//#line 253 "gramatica.y"
{ yyval = new ParserVal("<="); }
break;
case 107:
//#line 254 "gramatica.y"
{ yyval = new ParserVal("=="); }
break;
case 108:
//#line 255 "gramatica.y"
{ yyval = new ParserVal("!!"); }
break;
case 109:
//#line 256 "gramatica.y"
{ yyval = new ParserVal("<"); }
break;
case 110:
//#line 257 "gramatica.y"
{ yyval = new ParserVal("<"); }
break;
case 111:
//#line 258 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
break;
//#line 1345 "Parser.java"
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
