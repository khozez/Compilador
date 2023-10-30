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
    0,    0,    0,    0,    1,    2,    2,    5,    5,    5,
    6,    7,    7,    7,    7,   10,   10,   10,    8,    8,
    8,    8,   14,   14,   14,    4,    4,    4,    4,    4,
    4,    3,    3,    3,   20,   20,   11,   11,   15,   15,
   15,   15,   15,   15,   15,   24,   24,   24,   24,   24,
   24,   24,   23,   23,   23,   16,   16,   16,   16,   17,
   17,   25,    9,    9,   18,   18,   18,   26,   26,   26,
   26,   26,   27,   27,   27,   27,   12,   12,   12,   12,
   19,   19,   28,   28,   13,   13,   13,   13,   21,   21,
   21,   29,   29,   29,   30,   30,   30,   30,   30,   22,
   22,   22,   22,   22,   22,   22,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    2,    5,    3,    2,
    1,    2,    3,    2,    1,    3,    3,    3,    4,    2,
    3,    2,    3,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    2,    3,    3,   14,   13,
   10,    9,   12,   13,    9,   17,   16,   11,   10,   12,
   15,    9,    3,    2,    1,   10,    8,    9,    7,    2,
    1,    1,    9,    7,    9,    8,    7,    3,    3,    3,
    1,    2,    2,    3,    1,    2,    4,    3,    4,    5,
    2,    2,    3,    1,    1,    1,    1,    1,    1,    3,
    3,    1,    3,    3,    1,    2,    1,    2,    3,    1,
    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,   61,    0,    0,    0,
    0,   85,   86,   87,    0,    3,    5,    6,    0,   34,
   31,   26,   30,    0,   27,   28,   29,   32,    0,    1,
    0,    0,    0,    0,    0,    0,   60,    0,    0,    7,
    0,   82,   81,   33,    0,   97,    0,    0,    0,    0,
   92,    0,   16,   17,   18,    0,    0,   78,    0,    0,
    0,    0,    9,    0,    0,    0,    0,   96,   98,    0,
    0,    0,    0,    0,   79,    0,    0,   77,    0,    0,
    0,    0,   15,    0,    0,  100,  101,  102,  103,  106,
  104,  105,    0,    0,    0,    0,    0,    0,   83,   99,
    0,    0,   93,   94,   80,   20,    0,    8,   12,    0,
   14,    0,    0,    0,    0,    0,    0,    0,    0,   13,
   21,    0,    0,    0,    0,    0,   35,    0,   75,    0,
    0,    0,    0,    0,    0,   88,    0,    0,    0,   19,
    0,    0,    0,    0,   76,   36,    0,   67,   62,    0,
   73,    0,    0,    0,    0,   59,    0,    0,    0,   24,
    0,   55,    0,    0,    0,   66,    0,    0,   74,   70,
   68,    0,    0,    0,   57,    0,    0,   23,   54,    0,
    0,    0,   45,    0,   65,   58,    0,   64,    0,    0,
   41,   53,    0,    0,    0,   56,    0,    0,    0,    0,
    0,   63,    0,    0,   43,    0,    0,    0,   44,    0,
    0,   39,    0,    0,   52,    0,    0,    0,    0,   48,
    0,    0,    0,    0,   50,    0,    0,    0,    0,    0,
   51,    0,   46,
};
final static short yydgoto[] = {                          2,
    3,   17,  162,  163,   20,   81,   82,   83,   84,   21,
   22,   23,   24,  140,   25,   26,   27,   28,   29,  129,
   49,   93,  164,  130,  153,  131,  132,   43,   50,   51,
};
final static short yysindex[] = {                       -83,
  -77,    0,   76,    0,  134,  -27,    0, -203,   20, -187,
 -155,    0,    0,    0,   73,    0,    0,    0,   86,    0,
    0,    0,    0, -120,    0,    0,    0,    0,  100,    0,
  -10, -111,  -10,   29,  -15,  -10,    0,  110,  -10,    0,
  -17,    0,    0,    0, -123,    0,  -98,  -10,   16,    8,
    0,  -27,    0,    0,    0,   16,  -44,    0,  121,  -89,
   51, -156,    0,    6,  -41,    6,  -79,    0,    0,  112,
  -10,  -10,  -10,  -10,    0,  -43,  140,    0,  139,  -65,
   70, -156,    0,  153,  -54,    0,    0,    0,    0,    0,
    0,    0,  -10,  165,   85,  -46,   -4,  169,    0,    0,
    8,    8,    0,    0,    0,    0,  209,    0,    0,  213,
    0,   14,  159,  132,  215,  241,   12,  262,  -16,    0,
    0,   36,  -85,  215,  254,  255,    0,  272,    0,  273,
  186,  188,  195,  -84,   43,    0,  198,   65,   21,    0,
  202,  281,  201,  -10,    0,    0,  228,    0,    0,  283,
    0,  284,  285,  215,  202,    0,  -71,  215,  290,    0,
   36,    0,  288,   90, -180,    0,    6,  228,    0,    0,
    0,  221,   99,  202,    0,  225,  229,    0,    0, -143,
  294,  -70,    0,  -10,    0,    0,  147,    0,  215,  234,
    0,    0,  202,  317,  267,    0,  237,  202,  158,  101,
 -103,    0,  167,  103,    0,  202,  327,  115,    0,   45,
  -49,    0,  248,  -52,    0,  -40,  202,  331,  256,    0,
  113,  118,  202,  340,    0,  113,  260,  342,  125,  264,
    0,  127,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  396,  148,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
  353,    0,    0,    0,  -39,    0,    0,    0,   74,  -34,
    0,    0,    0,    0,    0,   83,   34,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  148,    0,
    0,  282,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  148,    0,    0,    0,  353,    0,    0,
  -29,    2,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  287,    0,    0,    0,    0,    0,    0,  176,    0,
    0,    0,    0,    0,    0,    0,  289,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  293,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  365,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   86,    0,    0,
    0,    0,    0,    0,    0,  366,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  369,    0,
};
final static short yygindex[] = {                         0,
  419,    0,  133,  166,    0,    0,    0,  339,  341,  393,
   71,  397,   22,  269,    0,    0,    0,  -26,  -25,  -75,
   52,  -42,  -58,  300,  -87,   -7,  291,  367,  192,  211,
};
final static int YYTABLESIZE=497;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         95,
   10,   95,   95,   95,   95,   95,   89,   95,   89,   89,
   89,   90,   34,   90,   90,   90,   33,   33,   32,  206,
   95,   95,   95,   97,  137,   89,   89,   89,   63,   48,
   90,   90,   90,   33,   47,   48,  117,  141,  155,    1,
   47,   67,   91,   33,   91,   91,   91,    4,   71,   73,
   72,  174,  193,   35,   74,   60,  151,  121,   71,   36,
   72,   91,   91,   91,  160,   91,   90,   92,   48,   58,
  217,   37,  122,   47,   95,   95,   95,   22,   95,  161,
   95,  182,  183,   85,   56,   61,   96,   64,  127,  128,
   66,   78,  151,   71,   42,   72,  173,  127,  128,   70,
   79,   38,   54,   85,   59,  127,  128,   62,   80,   12,
   13,   14,   39,  207,   38,  187,  143,   38,  190,  191,
  127,  128,  213,   37,  184,   10,   37,  127,  128,   40,
   77,  127,  128,  224,  199,   18,   41,   18,  228,  203,
  138,  127,  128,   44,  113,   52,  172,  210,  118,   65,
  176,   68,  100,   52,   71,    7,   72,    9,  221,   69,
   10,   75,  127,  128,  226,   15,  149,   76,   19,  180,
   19,   52,   52,    7,    7,    9,    9,   98,   10,   10,
  105,  197,  106,   15,   15,   52,   52,    7,    7,    9,
    9,  107,   10,   10,  108,  167,  111,   15,   15,  123,
   16,   71,  112,   72,   52,  114,    7,  115,    9,   22,
  116,   10,  214,  215,  180,   94,   15,   22,   22,   22,
   22,  219,  220,  186,   12,   13,   14,   67,   31,   31,
   68,   95,   95,   95,   95,  195,   89,   89,   89,   89,
  136,   90,   90,   90,   90,   31,   45,   46,  119,   12,
   13,   14,   45,   46,  124,   31,  120,   10,   30,   10,
   10,   10,  101,  102,   10,   10,   10,   10,   10,   10,
   10,  196,   91,   91,   91,   91,   86,   87,   88,   89,
  126,  133,  204,  103,  104,   57,   46,  134,  142,  126,
   88,  208,  139,  144,   12,   13,   14,  150,  145,  156,
   25,   52,  135,    7,   71,    9,   72,  201,   10,   71,
  148,   72,  126,   15,  149,  146,  147,  154,  157,  126,
  158,  159,  175,  126,  165,  166,  169,  170,  171,  181,
  177,  179,    6,  150,    7,    8,    9,  192,  181,   10,
   11,   12,   13,   14,   15,  185,   52,  194,    7,  188,
    9,  189,  181,   10,  126,   52,  198,    7,   15,    9,
  200,  202,   10,  205,  181,  209,  142,   15,  181,   52,
  211,    7,  216,    9,  222,  181,   10,  212,  223,  218,
  225,   15,  149,  227,  229,  230,  181,  231,  232,  233,
    6,  181,    7,    8,    9,    4,   84,   10,   11,   12,
   13,   14,   15,   52,   88,    7,   11,    9,   42,   49,
   10,   71,   47,   72,   52,   15,    7,   69,    9,    5,
  109,   10,  110,   52,   53,    7,   15,    9,   55,  178,
   10,  152,   25,   99,    0,   15,    0,  168,    0,    0,
   25,   25,   25,   25,    6,    0,    7,    0,  125,    0,
    0,   10,   11,   12,   13,   14,   15,  149,    6,    0,
    7,    8,    9,    0,    0,   10,   11,   12,   13,   14,
   15,    6,    0,    7,    0,  125,    0,    0,   10,   11,
   12,   13,   14,   15,    6,    0,    7,    0,    9,    0,
    0,   10,   11,   12,   13,   14,   15,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   42,   43,   44,   45,   41,   47,   43,   44,
   45,   41,   40,   43,   44,   45,   61,   61,   46,  123,
   60,   61,   62,   66,   41,   60,   61,   62,   44,   40,
   60,   61,   62,   61,   45,   40,   41,  123,  123,  123,
   45,   59,   41,   61,   43,   44,   45,  125,   43,   42,
   45,  123,  123,  257,   47,   34,  132,   44,   43,   40,
   45,   60,   61,   62,   44,   60,   61,   62,   40,   41,
  123,  259,   59,   45,   41,   42,   43,  125,   45,   59,
   47,  262,  263,   62,   33,   34,   65,   36,  115,  115,
   39,   41,  168,   43,   24,   45,  155,  124,  124,   48,
  257,  257,   32,   82,   34,  132,  132,  123,  265,  266,
  267,  268,   40,  201,   41,  174,  124,   44,  262,  263,
  147,  147,  210,   41,  167,  125,   44,  154,  154,   44,
   60,  158,  158,  221,  193,    3,  257,    5,  226,  198,
  119,  168,  168,   44,   93,  257,  154,  206,   97,   40,
  158,  275,   41,  257,   43,  259,   45,  261,  217,  258,
  264,   41,  189,  189,  223,  269,  270,  257,    3,  125,
    5,  257,  257,  259,  259,  261,  261,  257,  264,  264,
   41,  189,   44,  269,  269,  257,  257,  259,  259,  261,
  261,  257,  264,  264,  125,  144,   44,  269,  269,   41,
  125,   43,  257,   45,  257,   41,  259,  123,  261,  257,
  257,  264,  262,  263,  125,  257,  269,  265,  266,  267,
  268,  262,  263,  125,  266,  267,  268,   59,  273,  273,
  275,  271,  272,  273,  274,  184,  271,  272,  273,  274,
  257,  271,  272,  273,  274,  273,  257,  258,   40,  266,
  267,  268,  257,  258,  123,  273,   44,  257,  125,  259,
  260,  261,   71,   72,  264,  265,  266,  267,  268,  269,
  270,  125,  271,  272,  273,  274,  271,  272,  273,  274,
  115,   41,  125,   73,   74,  257,  258,  276,  123,  124,
  257,  125,  257,   40,  266,  267,  268,  132,   44,  134,
  125,  257,   41,  259,   43,  261,   45,   41,  264,   43,
  125,   45,  147,  269,  270,   44,   44,  123,  276,  154,
  123,  257,  157,  158,   44,  125,   44,   44,   44,  164,
   41,   44,  257,  168,  259,  260,  261,   44,  173,  264,
  265,  266,  267,  268,  269,  125,  257,  182,  259,  125,
  261,  123,  187,  264,  189,  257,  123,  259,  269,  261,
   44,  125,  264,  263,  199,  263,  201,  269,  203,  257,
   44,  259,  125,  261,   44,  210,  264,  263,  123,  214,
  263,  269,  270,   44,  125,   44,  221,  263,  125,  263,
  257,  226,  259,  260,  261,    0,   44,  264,  265,  266,
  267,  268,  269,  257,  257,  259,  125,  261,   44,   44,
  264,  125,   44,  125,  257,  269,  259,  125,  261,    1,
   82,  264,   82,  257,   32,  259,  269,  261,   32,  161,
  264,  132,  257,   67,   -1,  269,   -1,  147,   -1,   -1,
  265,  266,  267,  268,  257,   -1,  259,   -1,  261,   -1,
   -1,  264,  265,  266,  267,  268,  269,  270,  257,   -1,
  259,  260,  261,   -1,   -1,  264,  265,  266,  267,  268,
  269,  257,   -1,  259,   -1,  261,   -1,   -1,  264,  265,
  266,  267,  268,  269,  257,   -1,  259,   -1,  261,   -1,
   -1,  264,  265,  266,  267,  268,  269,
};
}
final static short YYFINAL=2;
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

//#line 191 "gramatica.y"

public static Nodo raiz = null;
public static String ambito = "";
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
//#line 620 "Parser.java"
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
case 7:
//#line 31 "gramatica.y"
{yyval = new ParserVal( val_peek(1).obj);}
break;
case 8:
//#line 34 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 9:
//#line 35 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 10:
//#line 36 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 17:
//#line 49 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Referencia a atributo de clase.");}
break;
case 22:
//#line 56 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la definición de variable.");}
break;
case 25:
//#line 61 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de la lista de variables.");}
break;
case 37:
//#line 82 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");}
break;
case 38:
//#line 83 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 39:
//#line 86 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 40:
//#line 87 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
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
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 45:
//#line 92 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 46:
//#line 95 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 47:
//#line 96 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta 'END_IF'");}
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
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 52:
//#line 101 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 55:
//#line 106 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una sentencia WHILE no puede contener una sentencia declarativa.");}
break;
case 56:
//#line 109 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 57:
//#line 110 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 58:
//#line 111 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 59:
//#line 112 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 60:
//#line 115 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");}
break;
case 61:
//#line 116 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta la sentencia PRINT para el comentario.");}
break;
case 63:
//#line 122 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 64:
//#line 123 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 65:
//#line 126 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 66:
//#line 127 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta el tipo asociado a los atributos");}
break;
case 67:
//#line 128 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de funcion VOID");}
break;
case 71:
//#line 134 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR: Falta la sentencia RETURN");}
break;
case 77:
//#line 144 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
break;
case 78:
//#line 145 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a función");}
break;
case 79:
//#line 146 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 80:
//#line 147 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 98:
//#line 177 "gramatica.y"
{comprobarRango(val_peek(0).sval);}
break;
case 106:
//#line 187 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal escrito el comparador ==");}
break;
//#line 953 "Parser.java"
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
