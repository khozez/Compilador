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
public final static short MASIGUAL=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    2,    2,    2,    5,
    6,    7,    7,    7,    8,    8,    8,    8,   11,   11,
   13,   14,   14,   12,   12,    9,    9,   17,   17,   17,
   18,    4,    4,    4,    4,    4,    4,    3,    3,    3,
    3,   26,   26,   19,   19,   19,   20,   20,   20,   20,
   20,   20,   20,   28,   28,   28,   31,   31,   31,   31,
   31,   31,   31,   33,   33,   33,   33,   33,   33,   33,
   29,   29,   32,   32,   21,   21,   22,   22,   34,   35,
   36,   36,   36,   10,   38,   38,   38,   24,   27,   37,
   39,   40,   40,   40,   40,   41,   41,   41,   41,   23,
   23,   23,   23,   25,   42,   42,   16,   16,   16,   16,
   15,   15,   15,   43,   43,   43,   44,   44,   44,   44,
   44,   30,   30,   30,   30,   30,   30,   30,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    1,    2,    1,    2,
    2,    4,    2,    1,    2,    3,    2,    1,    2,    2,
    3,    3,    4,    3,    2,    2,    1,    3,    2,    1,
    2,    1,    1,    1,    1,    1,    1,    1,    2,    1,
    1,    1,    2,    3,    3,    3,   12,   11,    8,    7,
   10,   11,    7,    3,    2,    2,   12,   11,    8,    7,
   10,   11,    7,    1,    1,    1,    1,    1,    1,    1,
    3,    2,    3,    2,    8,    6,    2,    1,    1,    2,
    5,    5,    4,    4,    5,    5,    4,    4,    4,    1,
    1,    2,    3,    1,    2,    2,    3,    1,    2,    4,
    3,    4,    5,    2,    3,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    3,    3,    1,    2,    1,    2,
    3,    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   78,    0,    0,    0,    0,  107,  108,  109,    0,
    0,    0,    0,    5,    7,    0,    0,    0,   40,   37,
    0,    0,   32,   33,   34,   35,   36,   38,    0,    0,
    0,    0,    0,    0,   25,    0,    0,   77,    0,    0,
    2,    0,    3,    6,    8,    0,   13,    0,   19,   20,
    0,  104,   39,    0,    0,  119,    0,    0,    0,    0,
  114,    0,    0,    0,  101,    0,    0,    0,  122,  123,
  124,  125,  128,  126,  127,    0,    0,    0,    0,    0,
    1,    0,    0,    0,   18,    0,    0,   27,    0,    0,
    0,   24,    0,    0,   79,   69,   65,   67,   64,   68,
    0,   98,   42,   66,    0,   70,    0,    0,    0,  118,
  120,    0,    0,    0,    0,    0,  100,    0,    0,  102,
    0,    0,    0,    0,   87,    0,    0,    0,   31,    0,
   12,   15,    0,   17,    0,   26,    0,    0,   22,    0,
  105,    0,   43,   99,    0,   88,   96,    0,  121,    0,
    0,  115,  116,  103,    0,    0,    0,    0,   86,   80,
   85,    0,    0,   16,   29,    0,    0,   94,    0,    0,
   23,    0,    0,   97,    0,    0,    0,    0,   76,    0,
   83,    0,   28,   95,   84,    0,   92,    0,   89,   72,
    0,    0,    0,   53,    0,   82,   81,   93,    0,    0,
    0,   49,   71,    0,    0,   75,    0,    0,    0,    0,
    0,    0,    0,    0,   74,    0,   63,    0,    0,   51,
    0,   59,   73,    0,    0,    0,   52,    0,    0,    0,
   47,    0,    0,   61,    0,   62,   57,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   18,   19,   84,   85,   86,
   20,   21,   49,   50,   76,   22,  136,   88,   23,   24,
   25,   26,   27,   28,  101,  102,  103,   77,  176,   78,
  104,  207,  208,  106,  127,   89,  169,  107,  108,  170,
  109,   52,   60,   61,
};
final static short yysindex[] = {                       323,
  -40,    0, -254,  -30, -205, -180,    0,    0,    0,   39,
  364,    0,  474,    0,    0,   54,  -20,   61,    0,    0,
 -157, -141,    0,    0,    0,    0,    0,    0,   67,   -5,
  -11,  -11,  -11,  -36,    0,    0,  -29,    0,   88,  -29,
    0,  487,    0,    0,    0, -130,    0,   12,    0,    0,
   75,    0,    0,  526, -136,    0, -108,  -11,   10,   18,
    0,   10,   10,  -24,    0,   56, -103,  116,    0,    0,
    0,    0,    0,    0,    0,  155,  117,  -11,  120,  128,
    0,  143,  -58,  -86,    0,  157,  -54,    0,   81,  -11,
  -33,    0, -141,  165,    0,    0,    0,    0,    0,    0,
  163,    0,    0,    0,  164,    0,   86,   85,  526,    0,
    0,   76,  -11,  -11,  -11,  -11,    0,  -21,  170,    0,
  -11,  290,   10,  172,    0,  -37,  182,  -50,    0,  199,
    0,    0,  197,    0,   13,    0,  310,   10,    0,  121,
    0,  -29,    0,    0,  526,    0,    0,  210,    0,   18,
   18,    0,    0,    0,   10,  -40,  -94,  211,    0,    0,
    0,  346,  137,    0,    0,  -54,  215,    0,  138,  310,
    0,  223,  146,    0,  233,  289, -227,  -94,    0,  237,
    0,  238,    0,    0,    0,  241,    0,  -73,    0,    0,
 -176,  242,  405,    0,  386,    0,    0,    0,  512,  249,
  175,    0,    0,  -94,  257,    0,  431,  258, -140,  -94,
  414,   40, -138,  261,    0,  272,    0,  511,   60,    0,
  187,    0,    0,  512,  280,   64,    0,  512,  445,   82,
    0,  460,   84,    0,  101,    0,    0,
};
final static short yyrindex[] = {                         0,
   94,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  219,  235,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  248,    0,
    0,    0,    0,    0,    0,   89,    0,    0,    0,    0,
    0,  332,    0,    0,    0,    0,    0,    0,    0,    0,
  151,    0,    0,    0,    1,    0,    0,    0,  102,   23,
    0,  115,  132,  -23,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   94,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  214,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  311,    0,  324,   94,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  493,    0,    0,  174,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   47,
   69,    0,    0,    0,  337,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  260,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   94,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  192,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  358,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  206,    0,    0,    0,    0,
    0,    0,    0,    0,  362,    0,    0,
};
final static short yygindex[] = {                         0,
  396,   38,    0,  627,    0,    0,    0,    0,  325,  338,
  -39,    0,    0,    0,   62,   -8,  255,    0,  410,    0,
  336,  428,  452,    0,   14,  -96,    0,  -38, -129,  347,
    0, -146,   93,    0,  262,    0,    0,   17,  279,    0,
    0,  339,   28,   29,
};
final static int YYTABLESIZE=845;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         34,
  117,   80,   36,   58,   65,   35,   58,  139,   57,   37,
   58,   57,  147,   29,   96,   57,   30,  117,  117,  117,
   33,  117,  111,  117,   29,   67,   29,   30,   58,   30,
   74,   73,   75,   57,  193,  194,   33,   87,  131,   33,
  168,  117,  117,  117,  117,  117,  112,  117,  195,  199,
   44,   91,  113,   38,  114,   29,  165,   92,   30,  115,
  117,  117,  117,  111,  116,  111,  111,  111,  113,   96,
  126,  166,   90,  187,  211,   87,   39,  229,   40,   44,
  218,  232,  111,  111,  111,  201,  202,  112,   10,  112,
  112,  112,   59,   62,   63,   66,  117,   45,  113,   48,
  114,   45,   46,  172,   47,   96,  112,  112,  112,  113,
   53,  113,  113,  113,   46,   51,  149,   54,  113,  112,
  114,  216,  217,  221,  222,  117,   82,   79,  113,  113,
  113,   44,   11,   93,   83,    7,    8,    9,  110,  123,
  150,  151,   45,  152,  153,   45,  105,  111,   96,  111,
  106,  138,  140,  118,  126,   46,  120,  122,   46,   96,
  125,  171,  156,  113,    2,  114,    4,   96,  128,    5,
   82,  112,   44,   21,   10,   44,   96,  181,   83,    7,
    8,    9,  155,  156,   96,    2,  129,   94,   96,   96,
    5,   50,   96,  113,  106,   10,   95,  113,  130,  114,
  134,  148,  135,  137,  142,   48,  143,  144,  145,  146,
  154,   10,  159,   10,   74,   73,   75,   21,    9,  160,
   64,   56,  161,   55,   56,  162,   45,   55,   56,    7,
    8,    9,   31,  110,   14,   50,   32,  105,  163,   46,
  164,   69,   70,   71,   72,   55,   56,   41,   31,   48,
  110,   31,   32,  174,  177,   32,   44,  117,  184,  117,
  117,  117,  185,  188,  117,  117,  117,  117,  117,  117,
  189,  117,  117,  117,  117,  106,  190,  196,  197,  111,
  200,  111,  111,  111,  198,  203,  111,  111,  111,  111,
  111,  111,  209,  111,  111,  111,  111,  210,   21,  214,
  212,  215,  220,  112,  223,  112,  112,  112,  225,  228,
  112,  112,  112,  112,  112,  112,   50,  112,  112,  112,
  112,  214,  227,  230,  214,  113,  231,  113,  113,  113,
   48,    4,  113,  113,  113,  113,  113,  113,   91,  113,
  113,  113,  113,    9,  234,   10,  236,   10,   10,   10,
  110,   56,   10,   10,   10,   10,   10,   10,   45,   14,
   45,   45,   45,  237,   55,   45,   45,   45,   45,   45,
   45,   46,   41,   46,   46,   46,  124,   54,   46,   46,
   46,   46,   46,   46,   90,    7,    8,    9,   44,   98,
   44,   44,   44,  180,  224,   44,   44,   44,   44,   44,
   44,   60,    7,    8,    9,   58,   42,  106,  132,  106,
  106,  106,  157,  191,  106,  106,  106,  106,  106,  106,
  183,  133,  121,  173,  182,   69,   70,   71,   72,    0,
   21,  141,   21,   21,   21,    0,    0,   21,   21,   21,
   21,   21,   21,   68,   98,   11,    0,    0,   50,    0,
   50,   50,   50,    0,    0,   50,   50,   50,   50,   50,
   50,    0,   48,   97,   48,   48,   48,    0,  178,   48,
   48,   48,   48,   48,   48,    9,  119,    9,    9,    9,
   98,   99,    9,    9,    9,    9,    9,    9,   41,    0,
    0,   14,    0,   14,   14,   14,    0,    0,   14,   14,
   14,   14,   14,   14,   41,  100,   41,   41,   41,    0,
  206,   41,   41,   41,   41,   41,   41,    0,   97,    0,
    0,    0,    0,   98,    0,    0,    0,  204,  156,    0,
    2,    0,   94,    0,   98,    5,   99,    0,  219,    0,
   10,   95,   98,    0,    0,  156,  156,    2,    2,    4,
    4,   98,    5,    5,   97,  213,    0,   10,   10,   98,
  100,    0,    0,   98,   98,    0,    1,   98,    2,  233,
    4,    0,   99,    5,    6,    7,    8,    9,   10,    1,
    0,    2,    3,    4,  235,    0,    5,    6,    7,    8,
    9,   10,    0,    0,    0,    0,  100,   97,   43,    0,
    0,    0,  156,    0,    2,    0,    4,    0,   97,    5,
    0,   81,    0,    0,   10,   99,   97,   30,    0,    0,
    1,    0,    2,    3,    4,   97,   99,    5,    6,    7,
    8,    9,   10,   97,   99,  226,    0,   97,   97,  100,
    0,   97,  156,   99,    2,    0,    4,    0,    0,    5,
  100,   99,    0,    0,   10,   99,   99,    0,  100,   99,
    0,  156,    0,    2,    0,    4,    0,  100,    5,    0,
  156,    0,    2,   10,    4,  100,    0,    5,    0,  100,
  100,    0,   10,  100,    0,    0,    0,  156,    0,    2,
    0,   94,    0,    0,    5,    0,    0,    0,    0,   10,
   95,  156,    0,    2,    0,   94,    0,    0,    5,    0,
    0,    0,    0,   10,   95,    0,  156,    0,    2,    0,
   94,    0,    0,    5,    0,    0,    0,    0,   10,   95,
    1,    0,    2,    3,    4,    0,    0,    5,    6,    7,
    8,    9,   10,    1,    0,    2,    3,    4,  158,   30,
    5,    6,    7,    8,    9,   10,    0,   30,   30,   30,
   30,    0,    0,  167,    0,    0,    0,  156,  156,    2,
    2,    4,   94,    0,    5,    5,    0,    0,    0,   10,
   10,   95,    1,  175,    2,    0,   94,    0,  179,    5,
    6,    7,    8,    9,   10,   95,  186,    0,    0,    0,
    0,    0,  192,    0,  175,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  205,
    0,  192,    0,    0,    0,    0,    0,    0,    0,    0,
  175,    0,    0,    0,    0,    0,  175,  192,    0,    0,
    0,    0,    0,    0,  192,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,  257,   40,   41,   46,   40,   41,   45,   40,
   40,   45,  109,    0,   54,   45,    0,   41,   42,   43,
   61,   45,    0,   47,   11,   34,   13,   11,   40,   13,
   60,   61,   62,   45,  262,  263,   61,   46,  125,   61,
  137,   41,   42,   43,   44,   45,    0,   47,  178,  123,
   13,   40,   43,  259,   45,   42,   44,   46,   42,   42,
   60,   61,   62,   41,   47,   43,   44,   45,    0,  109,
   79,   59,   61,  170,  204,   84,  257,  224,   40,   42,
  210,  228,   60,   61,   62,  262,  263,   41,    0,   43,
   44,   45,   31,   32,   33,   34,   41,   44,   43,  257,
   45,    0,  123,  142,   44,  145,   60,   61,   62,   41,
   44,   43,   44,   45,    0,  257,   41,  123,   43,   58,
   45,  262,  263,  262,  263,  125,  257,   40,   60,   61,
   62,    0,   44,   59,  265,  266,  267,  268,  275,   78,
  113,  114,   41,  115,  116,   44,   54,  125,  188,  258,
    0,   90,   91,  257,  163,   41,   41,   41,   44,  199,
   41,   41,  257,   43,  259,   45,  261,  207,   41,  264,
  257,  125,   41,    0,  269,   44,  216,   41,  265,  266,
  267,  268,  121,  257,  224,  259,   44,  261,  228,  229,
  264,    0,  232,  125,   44,  269,  270,   43,  257,   45,
   44,  109,  257,  123,   40,    0,   44,   44,  123,  125,
   41,  123,   41,  125,   60,   61,   62,   44,    0,  257,
  257,  258,   41,  257,  258,  276,  125,  257,  258,  266,
  267,  268,  273,  257,    0,   44,  277,  145,   40,  125,
   44,  271,  272,  273,  274,  257,  258,    0,  273,   44,
  275,  273,  277,   44,   44,  277,  125,  257,   44,  259,
  260,  261,  125,   41,  264,  265,  266,  267,  268,  269,
  125,  271,  272,  273,  274,  125,   44,   41,   41,  257,
  188,  259,  260,  261,   44,   44,  264,  265,  266,  267,
  268,  269,   44,  271,  272,  273,  274,  123,  125,  207,
   44,   44,  263,  257,   44,  259,  260,  261,  216,  123,
  264,  265,  266,  267,  268,  269,  125,  271,  272,  273,
  274,  229,  263,   44,  232,  257,  263,  259,  260,  261,
  125,    0,  264,  265,  266,  267,  268,  269,  125,  271,
  272,  273,  274,  125,  263,  257,  263,  259,  260,  261,
  257,   41,  264,  265,  266,  267,  268,  269,  257,  125,
  259,  260,  261,  263,   41,  264,  265,  266,  267,  268,
  269,  257,  125,  259,  260,  261,  257,   41,  264,  265,
  266,  267,  268,  269,  125,  266,  267,  268,  257,   54,
  259,  260,  261,  257,  123,  264,  265,  266,  267,  268,
  269,   44,  266,  267,  268,   44,   11,  257,   84,  259,
  260,  261,  123,  125,  264,  265,  266,  267,  268,  269,
  166,   84,   76,  145,  163,  271,  272,  273,  274,   -1,
  257,   93,  259,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,   34,  109,  123,   -1,   -1,  257,   -1,
  259,  260,  261,   -1,   -1,  264,  265,  266,  267,  268,
  269,   -1,  257,   54,  259,  260,  261,   -1,  123,  264,
  265,  266,  267,  268,  269,  257,   67,  259,  260,  261,
  145,   54,  264,  265,  266,  267,  268,  269,  125,   -1,
   -1,  257,   -1,  259,  260,  261,   -1,   -1,  264,  265,
  266,  267,  268,  269,  257,   54,  259,  260,  261,   -1,
  125,  264,  265,  266,  267,  268,  269,   -1,  109,   -1,
   -1,   -1,   -1,  188,   -1,   -1,   -1,  123,  257,   -1,
  259,   -1,  261,   -1,  199,  264,  109,   -1,  125,   -1,
  269,  270,  207,   -1,   -1,  257,  257,  259,  259,  261,
  261,  216,  264,  264,  145,  125,   -1,  269,  269,  224,
  109,   -1,   -1,  228,  229,   -1,  257,  232,  259,  125,
  261,   -1,  145,  264,  265,  266,  267,  268,  269,  257,
   -1,  259,  260,  261,  125,   -1,  264,  265,  266,  267,
  268,  269,   -1,   -1,   -1,   -1,  145,  188,  125,   -1,
   -1,   -1,  257,   -1,  259,   -1,  261,   -1,  199,  264,
   -1,  125,   -1,   -1,  269,  188,  207,  125,   -1,   -1,
  257,   -1,  259,  260,  261,  216,  199,  264,  265,  266,
  267,  268,  269,  224,  207,  125,   -1,  228,  229,  188,
   -1,  232,  257,  216,  259,   -1,  261,   -1,   -1,  264,
  199,  224,   -1,   -1,  269,  228,  229,   -1,  207,  232,
   -1,  257,   -1,  259,   -1,  261,   -1,  216,  264,   -1,
  257,   -1,  259,  269,  261,  224,   -1,  264,   -1,  228,
  229,   -1,  269,  232,   -1,   -1,   -1,  257,   -1,  259,
   -1,  261,   -1,   -1,  264,   -1,   -1,   -1,   -1,  269,
  270,  257,   -1,  259,   -1,  261,   -1,   -1,  264,   -1,
   -1,   -1,   -1,  269,  270,   -1,  257,   -1,  259,   -1,
  261,   -1,   -1,  264,   -1,   -1,   -1,   -1,  269,  270,
  257,   -1,  259,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  257,   -1,  259,  260,  261,  122,  257,
  264,  265,  266,  267,  268,  269,   -1,  265,  266,  267,
  268,   -1,   -1,  137,   -1,   -1,   -1,  257,  257,  259,
  259,  261,  261,   -1,  264,  264,   -1,   -1,   -1,  269,
  269,  270,  257,  157,  259,   -1,  261,   -1,  162,  264,
  265,  266,  267,  268,  269,  270,  170,   -1,   -1,   -1,
   -1,   -1,  176,   -1,  178,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  193,
   -1,  195,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  204,   -1,   -1,   -1,   -1,   -1,  210,  211,   -1,   -1,
   -1,   -1,   -1,   -1,  218,
};
}
final static short YYFINAL=12;
final static short YYMAXTOKEN=277;
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
"MENORIGUAL","IGUAL","DISTINTO","MENOSMENOS","DO","MASIGUAL",
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
"sentencia : sentenciaEjecutable",
"encabezadoClase : CLASS ID",
"encabezadoForward : CLASS ID",
"declaracionClase : encabezadoClase '{' cuerpoClase '}'",
"declaracionClase : encabezadoForward ','",
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
"sentenciaDeclarativa : declaracion",
"sentenciaDeclarativaMetodo : declaracionFuncionLocal",
"sentenciaDeclarativaMetodo : declaracion ','",
"asignacion : ID '=' expresion",
"asignacion : ID IGUAL expresion",
"asignacion : ID MASIGUAL expresion",
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
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return '}' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}' ELSE '{' bloque_ejecucion_return '}'",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' '{' bloque_ejecucion_return '}'",
"sentenciaIfRetorno : IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE sentencia_ejecutable_return ',' END_IF",
"sentenciaIfRetorno : IF '(' condicion ')' sentencia_ejecutable_return ',' ELSE '{' bloque_ejecucion_return '}' END_IF",
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

//#line 692 "gramatica.y"

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


    	if (izq.getTipo().equals("Error") || (der.getTipo().equals("Error"))){
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
//#line 1224 "Parser.java"
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
				  chequeoAsignacionVariables();
				  out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de programa");}
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
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 8:
//#line 35 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 9:
//#line 36 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la coma ',' al final de la sentencia.");}
break;
case 10:
//#line 39 "gramatica.y"
{var t = AnalizadorLexico.TS;
                           int clave = t.obtenerSimbolo(val_peek(0).sval + Parser.ambito);
                           if (clave != t.NO_ENCONTRADO)
                           	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                	yyerror("El nombre de la clase " + val_peek(0).sval +  " ya fue utilizado en este ambito");
                                else { /*la tabla de simbolos contiene la clase pero no tiene el uso asignado.*/
                                        t.agregarAtributo(clave, "uso", "nombre_clase");
                                        t.agregarAtributo(clave, "tipo", val_peek(0).sval);
                                }
                           else {
                                t.agregarSimbolo(val_peek(0).sval + Parser.ambito);
                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_clase");
                                t.agregarAtributo(t.obtenerID(), "tipo", val_peek(0).sval);
                           }
                           yyval = new ParserVal(new Nodo("EncabezadoClase", new Nodo(val_peek(0).sval + Parser.ambito), null));
                           lista_clases.add(val_peek(0).sval + Parser.ambito.replace(':','_'));
                           claseActual = val_peek(0).sval;
                           ambitoClase = Parser.ambito;
                           cantHerencias = 0;
                           agregarAmbito(claseActual);
                          }
break;
case 11:
//#line 62 "gramatica.y"
{lista_clases_fd.add(val_peek(0).sval+Parser.ambito);
                            claseActual = "";
                            ambitoClase = "";
                            out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Forward declaración de clase.");}
break;
case 12:
//#line 68 "gramatica.y"
{lista_clases_fd.remove(Parser.ambito.substring(1));
                                                        salirAmbito();
                                                        claseActual = "";
                                                        out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");
							yyval = new ParserVal( new Nodo("Clase", (Nodo) val_peek(3).obj, (Nodo) val_peek(1).obj) );}
break;
case 14:
//#line 74 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 15:
//#line 77 "gramatica.y"
{yyval = val_peek(1);}
break;
case 16:
//#line 78 "gramatica.y"
{ yyval = new ParserVal( new Nodo("cuerpoClase", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj));}
break;
case 17:
//#line 79 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 19:
//#line 84 "gramatica.y"
{yyval = val_peek(0); variableAmbitoClase = "";}
break;
case 20:
//#line 85 "gramatica.y"
{yyval = val_peek(0); variableAmbitoClase = "";}
break;
case 21:
//#line 88 "gramatica.y"
{
				   var t = AnalizadorLexico.TS;
                                   variableAmbitoClase = val_peek(2).sval + variableAmbitoClase;
                                   String claseBase;
                                   if (claseActual.equals(""))
                                   	claseBase = variableAmbitoClase.substring(variableAmbitoClase.lastIndexOf(":")+1);
                                   else
                                   {
                                   	claseBase = claseActual;
                                   	variableAmbitoClase = variableAmbitoClase + ":" + claseBase;
                                   }
                                   if (!chequearReferencia(val_peek(2).sval, claseBase, variableAmbitoClase+":main"))
                                   	yyerror("No existe el atributo en la clase a la que se intenta hacer referencia.");
                                   String nombre_nodo = val_peek(2).sval+":"+claseBase+":main";
                                   var x = new Nodo("Asignacion", new Nodo(nombre_nodo, t.obtenerAtributo(t.obtenerSimbolo(nombre_nodo), "tipo")), (Nodo) val_peek(0).obj);
                                   x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));
                                   out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación a atributo de clase");
                                   yyval = new ParserVal(x);
				  }
break;
case 22:
//#line 109 "gramatica.y"
{
			      out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Llamado a metodo de clase");
                              var t = AnalizadorLexico.TS;
                              variableAmbitoClase = val_peek(2).sval + variableAmbitoClase;
                              String claseBase = variableAmbitoClase.substring(variableAmbitoClase.lastIndexOf(":")+1);
                              int clave = t.obtenerSimbolo(val_peek(2).sval+":"+claseBase+":main");
                              Nodo x;
                              x = new Nodo("LlamadoMetodo", new Nodo(t.obtenerAtributo(clave, "ref"), null, null, "void"), null);
                              if(chequearMetodoClase(claseBase, val_peek(2).sval, variableAmbitoClase+":main",x))
                              {
                              	    if (tieneParametrosMetodo(claseBase, val_peek(2).sval))
                              	    	  yyerror("El metodo al que desea llamar requiere parametros");
                              }
                              else
                              	    yyerror("No existe el metodo al que se intenta invocar en la clase.");
                              yyval = new ParserVal(x);
                              }
break;
case 23:
//#line 126 "gramatica.y"
{
                                        out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Llamado a metodo de clase");
                                        var t = AnalizadorLexico.TS;
                                        variableAmbitoClase = val_peek(3).sval + variableAmbitoClase;
                                        String claseBase = variableAmbitoClase.substring(variableAmbitoClase.lastIndexOf(":")+1);
                                        int clave = t.obtenerSimbolo(val_peek(3).sval+":"+claseBase+":main");
                                        Nodo x;
                                        x = new Nodo("LlamadoMetodo", new Nodo(t.obtenerAtributo(clave, "ref")), (Nodo) val_peek(1).obj, "void");
                                        if(chequearMetodoClase(claseBase, val_peek(3).sval, variableAmbitoClase+":main",x))
                                        {
                                        	if (!tieneParametrosMetodo(claseBase, val_peek(3).sval))
                                                	yyerror("El metodo al que desea llamar no acepta parametros");
                                                else
                                                {
                                                        if (!validarParametro(x))
                                                        	yyerror("Incompatibilidad de tipos en llamado a metodo");
                                                }
                                        }
                                        else
                                                yyerror("No existe el metodo al que se intenta invocar en la clase.");
                                        yyval = new ParserVal(x);
                                        }
break;
case 24:
//#line 150 "gramatica.y"
{ var t = AnalizadorLexico.TS;
                                          int clave = t.obtenerSimbolo(val_peek(1).sval+":main");
                                          String tipo = t.obtenerAtributo(clave, "tipo");
                                          if (tipo != TablaSimbolos.NO_ENCONTRADO_MESSAGE && !tipo.equals("variable"))
                                          	variableAmbitoClase = ":"+ tipo + variableAmbitoClase;
                                          else
                                        	variableAmbitoClase = ":"+ val_peek(1).sval + variableAmbitoClase;
                                         }
break;
case 25:
//#line 158 "gramatica.y"
{ var t = AnalizadorLexico.TS;
			   int clave = t.obtenerSimbolo(val_peek(1).sval+":main");
			   String tipo = t.obtenerAtributo(clave, "tipo");
			   if (tipo != TablaSimbolos.NO_ENCONTRADO_MESSAGE && !tipo.equals("variable"))
			   	variableAmbitoClase = ":"+ tipo + variableAmbitoClase;
			   else
			   	variableAmbitoClase = ":"+ val_peek(1).sval + variableAmbitoClase;
			   }
break;
case 26:
//#line 169 "gramatica.y"
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
                                                         	t.modificarAtributo(clave, "lexema", x);
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
case 28:
//#line 194 "gramatica.y"
{ lista_variables.add(val_peek(2).sval + Parser.ambito); }
break;
case 29:
//#line 195 "gramatica.y"
{ lista_variables.add(val_peek(1).sval + Parser.ambito); }
break;
case 30:
//#line 196 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
break;
case 31:
//#line 199 "gramatica.y"
{ if (lista_clases_fd.contains(val_peek(1).sval+":main"))
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
					 herencia = true;

					 if (!aplicarHerencia(val_peek(1).sval, claseActual))
						yyerror("No se puede sobreescribir atributos de clase.");
					 chequearNivelesHerencia(Parser.ambito.substring(1));
				}
			 }
 		       }
break;
case 32:
//#line 231 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 33:
//#line 232 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 34:
//#line 233 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 35:
//#line 234 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 36:
//#line 235 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 37:
//#line 236 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 38:
//#line 239 "gramatica.y"
{yyval = new ParserVal(val_peek(0).obj);}
break;
case 39:
//#line 240 "gramatica.y"
{yyval = new ParserVal(null);}
break;
case 40:
//#line 241 "gramatica.y"
{yyval = new ParserVal(val_peek(0).obj);}
break;
case 41:
//#line 242 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la coma ',' al final de la declaracion.");}
break;
case 44:
//#line 250 "gramatica.y"
{
			      out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");
			      var x = new Nodo("Asignacion", new Nodo(getVariableConAmbitoTS(val_peek(2).sval), getTipoVariableConAmbitoTS(val_peek(2).sval)), (Nodo) val_peek(0).obj);
			      x.setTipo(validarTiposAssign(x, x.getIzq(), x.getDer()));

			      if (generarMenosMenos())
                              {
				      yyval = new ParserVal( new Nodo("sentencias", x, menosMenos));
				      menosMenos = null;
                              }
                              else
                              {
                              	yyval = new ParserVal(x);
                              }
			      variables_no_asignadas.remove(val_peek(2).sval + Parser.ambito);}
break;
case 45:
//#line 265 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 46:
//#line 266 "gramatica.y"
{
				    out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignacion");
				
				    Nodo variable = new Nodo(getVariableConAmbitoTS(val_peek(2).sval),
				                             getTipoVariableConAmbitoTS(val_peek(2).sval));
				
				    Nodo variableAux = new Nodo(getVariableConAmbitoTS(val_peek(2).sval),
				                                 getTipoVariableConAmbitoTS(val_peek(2).sval));
				
				    Nodo suma = new Nodo("+", variableAux, (Nodo) val_peek(0).obj);
				
				    Nodo x = new Nodo("Asignacion", variable, suma);
				
				    /* ⚠️ validamos contra la expresión, no contra el nodo suma*/
				    x.setTipo(validarTiposAssign(x, variable, (Nodo) val_peek(0).obj));
				
				    if (generarMenosMenos()) {
				        yyval = new ParserVal(new Nodo("sentencias", x, menosMenos));
				        menosMenos = null;
				    } else {
				        yyval = new ParserVal(x);
				    }
				
				    variables_no_asignadas.remove(val_peek(2).sval + Parser.ambito);
				}
break;
case 47:
//#line 294 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
												yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(9).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null)))); }
break;
case 48:
//#line 296 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 49:
//#line 297 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(5).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
										}
break;
case 50:
//#line 300 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 51:
//#line 301 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(7).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(5).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
													   }
break;
case 52:
//#line 304 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
													     yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(8).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
													    }
break;
case 53:
//#line 307 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(4).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
									      }
break;
case 54:
//#line 312 "gramatica.y"
{ var x = new Nodo(val_peek(1).sval, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj, null);
					    x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));

					    if (generarMenosMenos())
                                            {
						    yyval = new ParserVal( new Nodo("sentencias", x, menosMenos));
						    menosMenos = null;
                                            }
                                            else
                                            	yyval = new ParserVal(x);}
break;
case 55:
//#line 322 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el primer miembro de la condicion");}
break;
case 56:
//#line 323 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el segundo miembro de la condicion");}
break;
case 57:
//#line 326 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
														          yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(9).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
														         }
break;
case 58:
//#line 329 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 59:
//#line 330 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										       yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(5).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
										      }
break;
case 60:
//#line 333 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
break;
case 61:
//#line 334 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
															    yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(7).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(5).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
															   }
break;
case 62:
//#line 337 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
															        yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(8).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(6).obj, null), new Nodo("else", (Nodo) val_peek(2).obj, null))));
															       }
break;
case 63:
//#line 340 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");
										       yyval = new ParserVal( new Nodo("if", (Nodo) val_peek(4).obj, new Nodo("cuerpoIf", new Nodo("then", (Nodo) val_peek(2).obj, null), new Nodo("else", null, null))));
										      }
break;
case 64:
//#line 345 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 65:
//#line 346 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 66:
//#line 347 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 67:
//#line 348 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 68:
//#line 349 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 69:
//#line 350 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 70:
//#line 351 "gramatica.y"
{yyval = new ParserVal( val_peek(0).obj);}
break;
case 71:
//#line 354 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj)); }
break;
case 72:
//#line 355 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 73:
//#line 359 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj)); }
break;
case 74:
//#line 360 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 75:
//#line 363 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
								     yyval = new ParserVal( new Nodo("while", (Nodo) val_peek(5).obj, (Nodo) val_peek(1).obj));
								    }
break;
case 76:
//#line 366 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");
               							 yyval = new ParserVal( new Nodo("while", (Nodo) val_peek(3).obj, (Nodo) val_peek(0).obj));
               							}
break;
case 77:
//#line 371 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena.");
		     var t = AnalizadorLexico.TS;
		     int clave = t.obtenerSimbolo(val_peek(0).sval);
		     t.agregarAtributo(clave, "cadena", val_peek(0).sval);
		     String nombreNodo = "Cadena"+AnalizadorLexico.getIdCadena();
		     t.modificarAtributo(clave, "lexema", nombreNodo);
		     var x = new Nodo(nombreNodo, null, null, "STRING");
		     yyval = new ParserVal( new Nodo("Print", x, null, "STRING"));}
break;
case 78:
//#line 379 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
break;
case 79:
//#line 382 "gramatica.y"
{yyval = new ParserVal( new Nodo(val_peek(0).sval));}
break;
case 80:
//#line 385 "gramatica.y"
{yyval = new ParserVal( new Nodo(val_peek(0).sval, val_peek(1).sval));}
break;
case 81:
//#line 388 "gramatica.y"
{		     funcLocales = 0;
                  					     var t = AnalizadorLexico.TS;
                  					     int clave = t.obtenerSimbolo(val_peek(3).sval + Parser.ambito);
                                                               if (clave != t.NO_ENCONTRADO)
                                                               	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	t.modificarAtributo(clave, "ref", val_peek(3).sval + Parser.ambito);
                                                                 else { /*la tabla de simbolos contiene la funcion pero no tiene el tipo asignado.*/
                                                                  	t.agregarAtributo(clave, "tipo", "void");
                                                                  	t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                  	t.agregarAtributo(clave, "ref", val_peek(3).sval + Parser.ambito);
                                                                   }
                                                               else {
                                                                t.agregarSimbolo(val_peek(3).sval + Parser.ambito);
                                                                t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                                t.agregarAtributo(t.obtenerID(), "ref", val_peek(3).sval + Parser.ambito);
                                                                t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                               }
                                                               yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(3).sval + Parser.ambito), (Nodo) val_peek(1).obj, "void"));
                                                               lista_funciones.add(val_peek(3).sval + Parser.ambito.replace(':','_'));
                                                               agregarAmbito(val_peek(3).sval);
                                                               parametro((Nodo) val_peek(1).obj);
                  					   }
break;
case 82:
//#line 410 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 83:
//#line 411 "gramatica.y"
{ funcLocales = 0;
                  		   			  var t = AnalizadorLexico.TS;
                                                            int clave = t.obtenerSimbolo(val_peek(2).sval + Parser.ambito);
                                                            if (clave != t.NO_ENCONTRADO)
                                                            	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                                  	t.modificarAtributo(clave, "ref", val_peek(2).sval + Parser.ambito);
                                                                  else { /*la tabla de simbolos contiene el metodo pero no tiene el tipo asignado.*/
                                                                          t.agregarAtributo(clave, "tipo", "void");
                                                                          t.agregarAtributo(clave, "uso", "nombre_metodo");
                                                                          t.agregarAtributo(clave, "ref", val_peek(2).sval + Parser.ambito);
                                                                  }
                                                            else {
                                                                  t.agregarSimbolo(val_peek(2).sval + Parser.ambito);
                                                                  t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                                                  t.agregarAtributo(t.obtenerID(), "ref", val_peek(2).sval + Parser.ambito);
                                                                  t.agregarAtributo(t.obtenerID(), "uso", "nombre_metodo");
                                                            }
                                                            yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(2).sval + Parser.ambito), null, "void"));
                                                            lista_funciones.add(val_peek(2).sval + Parser.ambito.replace(':','_'));
                                                            agregarAmbito(val_peek(2).sval);
                                                            }
break;
case 84:
//#line 434 "gramatica.y"
{ out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de metodo.");
                                                           yyval = new ParserVal(
                                                           new Nodo( "MetodoClase",
                                                                                    (Nodo) val_peek(3).obj ,
                                                                                    (Nodo) val_peek(1).obj ,
                                                                                    "void"));
                                                           salirAmbito();}
break;
case 85:
//#line 443 "gramatica.y"
{
					     var t = AnalizadorLexico.TS;
					     int clave = t.obtenerSimbolo(val_peek(3).sval + Parser.ambito);
                                             if (clave != t.NO_ENCONTRADO)
                                             	if (t.obtenerAtributo(clave, "uso") != t.NO_ENCONTRADO_MESSAGE)
                                                	yyerror("El nombre de la funcion " + val_peek(3).sval +  " ya fue utilizado en este ambito");
                                                else { /*la tabla de simbolos contiene la funcion pero no tiene el uso asignado.*/
                                                	t.agregarAtributo(clave, "tipo", "void");
                                                	t.agregarAtributo(clave, "uso", "nombre_funcion");
                                                	t.agregarAtributo(clave, "ref", val_peek(3).sval+Parser.ambito);
                                                 }
                                             else {
                                             	t.agregarSimbolo(val_peek(3).sval + Parser.ambito);
                                             	t.agregarAtributo(t.obtenerID(), "ref", val_peek(3).sval+Parser.ambito);
                                             	t.agregarAtributo(t.obtenerID(), "tipo", "void");
                                             	t.agregarAtributo(t.obtenerID(), "uso", "nombre_funcion");
                                             }
                                             yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(3).sval + Parser.ambito), (Nodo) val_peek(1).obj, "void"));
                                             lista_funciones.add(val_peek(3).sval + Parser.ambito.replace(':','_'));
                                             agregarAmbito(val_peek(3).sval);
                                             parametro((Nodo) val_peek(1).obj);
					   }
break;
case 86:
//#line 465 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 87:
//#line 466 "gramatica.y"
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
                                          yyval = new ParserVal(new Nodo("Encabezado", new Nodo(val_peek(2).sval + Parser.ambito), null, "void"));
                                          lista_funciones.add(val_peek(2).sval + Parser.ambito.replace(':','_'));
                                          agregarAmbito(val_peek(2).sval);
                                          }
break;
case 88:
//#line 487 "gramatica.y"
{ out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de función VOID.");
                                                            yyval = new ParserVal(
                                                            new Nodo( "Funcion",
                                                                         	    (Nodo) val_peek(3).obj ,
                                                                                    (Nodo) val_peek(1).obj ,
                                                                                    		    "void"));

                                                            /* Acciones de desapilar */
                                                            if (!verificarRetornoArbol((Nodo) val_peek(1).obj))
                                                            	yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                            salirAmbito();}
break;
case 89:
//#line 500 "gramatica.y"
{ out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de función VOID local a metodo.");
								var x = (Nodo) val_peek(3).obj;
								String nombre = x.getIzq().getNombre();
								funcLocales += 1;
                                                            	if (funcLocales <= 1){
                                                            		yyval = new ParserVal(
                                                                	new Nodo( "Funcion",
                                                                         	    (Nodo) val_peek(3).obj ,
                                                                                    (Nodo) val_peek(1).obj ,
                                                                                    		    "void"));

                                                                	/* Acciones de desapilar */
                                                                	if (!verificarRetornoArbol((Nodo) val_peek(1).obj))
                                                                		yyerror("La Funcion declarada '" + Parser.ambito.substring(1) + "' no tiene retorno o existe camino sin retorno");
                                                                	salirAmbito();
                                                            	}
                                                            	else
                                                            	{
                                                            		yyerror("En la función: '"+nombre+"' el nivel maximo de anidamiento (1) es superado.");
                                                            		salirAmbito();
                                                            	}}
break;
case 90:
//#line 523 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 91:
//#line 526 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 92:
//#line 529 "gramatica.y"
{yyval = val_peek(1);}
break;
case 93:
//#line 530 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj));}
break;
case 95:
//#line 532 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 96:
//#line 535 "gramatica.y"
{yyval = val_peek(1);}
break;
case 97:
//#line 536 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(2).obj, (Nodo) val_peek(1).obj));}
break;
case 99:
//#line 538 "gramatica.y"
{ yyval = new ParserVal( new Nodo("sentencias", (Nodo) val_peek(1).obj, null));}
break;
case 100:
//#line 541 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a funcion VOID.");
					String fun = getTipoVariableConAmbitoTS(val_peek(3).sval);
                                        Nodo x;
                                        if (fun != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
                                        {
						x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS(val_peek(3).sval)), (Nodo) val_peek(1).obj, "void");
						if (generarMenosMenos())
                                                {
                                                	yyval = new ParserVal( new Nodo("sentencias", x, menosMenos));
                                                	menosMenos = null;
                                                }
                                                else
                                                {
                                                        yyval = new ParserVal(x);
                                                }

                                                if (!chequearLlamadoFuncion(val_peek(3).sval))
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
break;
case 101:
//#line 568 "gramatica.y"
{out_estructura.write("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a funcion VOID.");
		  		String fun = getTipoVariableConAmbitoTS(val_peek(2).sval);
		  		Nodo x;
                                if (fun != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
                                {
                                	x = new Nodo("LlamadaFuncion", new Nodo(getVariableConAmbitoTS(val_peek(2).sval)), null, "void");
                                	if (chequearLlamadoFuncion(val_peek(2).sval))
                                        	yyerror("La funcion a la que se desea llamar tiene parametro.");
                                }
                                else{
                                	x = new Nodo("LlamadaFuncion", new Nodo("Funcion no encontrada"), null, "void");
                                        yyerror("No se encontro la función en un ambito adecuado");
                                }

		  		yyval = new ParserVal(x);
		  		}
break;
case 102:
//#line 584 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 103:
//#line 585 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 104:
//#line 589 "gramatica.y"
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
                                 	t.agregarAtributo(clave, "tipo", val_peek(1).sval);
                                 	t.modificarAtributo(clave, "lexema", x);
                                 } else {
                                   	yyerror("La variable declarada ya existe " + (x.contains(":") ? x.substring(0, x.indexOf(':')) : "en ambito global"));
                               	 }
                               } else {
                               		t.agregarSimbolo(x);
                               		t.agregarAtributo(t.obtenerID(), "tipo", val_peek(1).sval);
                               		if (!instanciaClase)
                                        	t.agregarAtributo(t.obtenerID(), "uso", "variable");
                                        else
                                                t.agregarAtributo(t.obtenerID(), "uso", "instanciaClase");
                               }
                             });
                         lista_variables.clear();
                         instanciaClase = false;
             	}
break;
case 105:
//#line 621 "gramatica.y"
{  lista_variables.add(val_peek(2).sval + Parser.ambito);
					     if (!instanciaClase)
					     {
 					     	variables_no_asignadas.add(val_peek(2).sval + Parser.ambito);
 					     }
 					  }
break;
case 106:
//#line 627 "gramatica.y"
{ lista_variables.add(val_peek(0).sval + Parser.ambito);
		       if (!instanciaClase)
		       {
                       	   variables_no_asignadas.add(val_peek(0).sval + Parser.ambito);
                       }
		     }
break;
case 107:
//#line 635 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 108:
//#line 636 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 109:
//#line 637 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 110:
//#line 638 "gramatica.y"
{ yyval = val_peek(0);
    	        instanciaClase = true;}
break;
case 111:
//#line 642 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 112:
//#line 643 "gramatica.y"
{ var x = new Nodo("+", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj, null);
                                  x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
                                  yyval = new ParserVal(x);}
break;
case 113:
//#line 646 "gramatica.y"
{ var x = new Nodo("-", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj, null);
    				  x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
    				  yyval = new ParserVal(x);}
break;
case 114:
//#line 651 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 115:
//#line 652 "gramatica.y"
{ var x = new Nodo("*", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj);
    			       x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
    			       yyval = new ParserVal(x);}
break;
case 116:
//#line 655 "gramatica.y"
{ var x = new Nodo("/", (Nodo) val_peek(2).obj, (Nodo)  val_peek(0).obj);
                               x.setTipo(validarTipos(x, (Nodo) val_peek(2).obj, (Nodo) val_peek(0).obj));
                               yyval = new ParserVal(x);}
break;
case 117:
//#line 660 "gramatica.y"
{String x = getTipoVariableConAmbitoTS(val_peek(0).sval);
            if (x != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
            	yyval =  new ParserVal( new Nodo(getVariableConAmbitoTS(val_peek(0).sval), x));
            else{
            	yyval =  new ParserVal( new Nodo("variableNoEncontrada", x));
                yyerror("No se encontro esta variable en un ambito adecuado");
                }
            }
break;
case 118:
//#line 668 "gramatica.y"
{String x = getTipoVariableConAmbitoTS(val_peek(1).sval);
                         if (x != TablaSimbolos.NO_ENCONTRADO_MESSAGE)
                         	yyval =  new ParserVal( new Nodo(getVariableConAmbitoTS(val_peek(1).sval), x));
                         else{
                                yyval =  new ParserVal( new Nodo("variableNoEncontrada", x));
                                yyerror("No se encontro esta variable en un ambito adecuado");
                         }
                         menosMenos = (Nodo) yyval.obj;
        }
break;
case 119:
//#line 677 "gramatica.y"
{ yyval = new ParserVal( new Nodo(val_peek(0).sval, getTipo(val_peek(0).sval))); }
break;
case 120:
//#line 678 "gramatica.y"
{ String x = comprobarRango(val_peek(0).sval); yyval = new ParserVal( new Nodo(x, getTipo(x))); }
break;
case 121:
//#line 679 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 122:
//#line 682 "gramatica.y"
{ yyval = new ParserVal(">="); }
break;
case 123:
//#line 683 "gramatica.y"
{ yyval = new ParserVal("<="); }
break;
case 124:
//#line 684 "gramatica.y"
{ yyval = new ParserVal("=="); }
break;
case 125:
//#line 685 "gramatica.y"
{ yyval = new ParserVal("!!"); }
break;
case 126:
//#line 686 "gramatica.y"
{ yyval = new ParserVal("<"); }
break;
case 127:
//#line 687 "gramatica.y"
{ yyval = new ParserVal(">"); }
break;
case 128:
//#line 688 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
break;
//#line 2302 "Parser.java"
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
