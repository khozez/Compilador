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
    0,    0,    0,    0,    1,    1,    1,    1,    2,    2,
    2,    2,    4,    4,    5,    5,    6,    6,    6,    9,
    9,    9,    7,    7,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   19,   19,   19,   20,   20,    3,    3,
    3,    3,   13,   13,   14,   14,   14,   14,   14,   14,
   15,   15,   15,   16,   17,   10,   10,   24,   24,   18,
   18,   18,   18,   21,   21,   25,   25,    8,    8,    8,
    8,   22,   22,   22,   26,   26,   26,   27,   27,   27,
   27,   27,   23,   23,   23,   23,   23,   23,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    2,    1,    1,    6,    5,
    3,    2,    2,    1,    1,    1,    4,    3,    2,    3,
    2,    1,    3,    2,    3,    2,    2,    1,    1,    1,
    1,    1,    1,    3,    1,    2,    1,    2,    3,    1,
    1,    1,    3,    3,   14,   13,   10,    9,   12,    8,
   10,    8,    9,    2,    1,    9,    7,    2,    1,    4,
    3,    4,    5,    2,    2,    3,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    3,    3,    1,    2,    1,
    2,    3,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   42,    0,    0,    0,    0,   68,   69,   70,    0,
   55,    0,    0,    0,    7,    8,    0,   37,    0,    0,
   28,   29,   30,   31,   32,   33,    0,    0,    0,    0,
    0,    0,    0,    0,   54,    0,    0,    2,    0,    3,
    5,    6,    0,   65,   64,    0,   26,   27,    0,   36,
   38,    0,   80,    0,    0,    0,    0,   75,    0,    0,
   61,    0,    0,    0,    0,   11,    0,    0,    0,    1,
    0,    0,   25,    0,   34,   79,   81,    0,    0,    0,
    0,    0,    0,    0,   62,   60,   71,    0,   14,   15,
    0,    0,    0,   83,   84,   85,   86,   87,   88,    0,
    0,    0,    0,    0,   66,   82,    0,    0,   76,   77,
   63,    0,   13,    0,    0,   24,    0,    0,    0,    0,
    0,    9,   23,   18,    0,    0,   59,    0,    0,    0,
    0,    0,   17,    0,    0,    0,   57,   58,    0,    0,
    0,   21,    0,    0,   50,    0,    0,    0,    0,   52,
   20,    0,    0,   56,   53,    0,    0,   47,    0,   51,
    0,    0,    0,   49,    0,   45,
};
final static short yydgoto[] = {                         13,
   14,   15,   16,   88,   89,   90,   91,   17,  133,   18,
   19,   20,   21,   22,   23,   24,   25,   26,   27,   28,
   29,   56,  100,  128,   45,   57,   58,
};
final static short yysindex[] = {                       -84,
  -38,    0, -206,   27, -179, -167,    0,    0,    0,   52,
    0,  253,    0,  267,    0,    0, -157,    0,   63,  -14,
    0,    0,    0,    0,    0,    0,   70,   79,   72,  -34,
  -34,  -25,  -15,  -34,    0,   82,  -34,    0,  281,    0,
    0,    0,  -33,    0,    0, -126,    0,    0,  368,    0,
    0, -156,    0, -135,  -34,   46,  -23,    0,   46,  -58,
    0, -130,   92,  163, -105,    0,  -35,  -41,  -35,    0,
 -121,  -38,    0,   63,    0,    0,    0,  166,  -34,  -34,
  -34,  -34,  -44,   96,    0,    0,    0,  317,    0,    0,
   95, -108,  113,    0,    0,    0,    0,    0,    0,  -34,
   36,  -92,  -36,  110,    0,    0,  -23,  -23,    0,    0,
    0,  128,    0,  -91,   12,    0,  169,  354,  147,  -87,
  276,    0,    0,    0,  -61,  -13,    0,  -67,   97,  105,
  -46,   15,    0, -126,  -45,  202,    0,    0,  354, -126,
 -116,    0,  -61,  -32,    0,  -10,  297,  -31, -126,    0,
    0, -230, -126,    0,    0,  -30,  124,    0,  209,    0,
 -126,   -9,  -26,    0,   -8,    0,
};
final static short yyrindex[] = {                         0,
    6,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  171,    0,
    0,    0,    0,    0,    0,    0,  191,  151,    0,    0,
    0,    0,  205,    0,    0,    0,    0,    0,  249,    0,
    0,    0,  220,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,   88,   21,    0,  106,   -7,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  219,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  321,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  220,    0,    0,   41,   68,    0,    0,
    0,  234,    0,    0,  338,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  342,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  120,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  134,    0,
};
final static short yygindex[] = {                         0,
  265,   39,   40,    0,  195,    0,    0,   37,  136,  -18,
  419,   14,   55,    0,    0,    0,    0,    0,    0,  235,
    0,   18,  228,  160,  232,   -4,   16,
};
final static int YYTABLESIZE=638;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        101,
   78,   32,   31,   55,  120,   55,  149,   79,   54,   80,
   54,   46,   46,   46,   55,   61,   31,   46,   81,   54,
   72,   48,   31,   82,   98,   71,   99,   31,   66,   47,
  135,  157,  158,   78,   78,   78,   78,   78,   12,   78,
   73,   78,   78,   78,   78,   78,   93,   78,   59,   64,
   33,   67,   41,   42,   69,  124,   72,  137,  142,   73,
   78,   72,   78,   72,   72,   72,   34,   74,   62,   93,
  125,   44,   78,  143,  107,  108,   73,   41,   42,   35,
   72,   73,   72,   73,   73,   73,   63,   44,   79,   36,
   80,   37,  152,  155,  160,  123,  109,  110,  165,   43,
   73,   92,   73,   74,  102,   43,   46,   65,   74,  134,
   74,   74,   74,   49,   50,   51,   84,  117,   76,   48,
  121,   68,   77,   44,   92,   78,   83,   74,   44,   74,
   72,   44,   85,   46,    4,  104,  111,    5,  114,  136,
   72,   43,   10,   11,    4,   72,   43,    5,  115,   43,
   35,   87,   10,   11,  150,   48,  116,  127,  118,    6,
    7,    8,    9,   48,  119,   73,  159,  138,   71,   46,
   41,  122,    1,    6,    2,    3,    4,   46,  127,    5,
    6,    7,    8,    9,   10,   11,  138,  129,  130,    1,
   40,    2,   74,    4,   35,  132,    5,    6,    7,    8,
    9,   10,   11,   86,   12,   79,  106,   80,   79,  126,
   80,   79,   44,   80,   30,   87,   76,  145,   39,  139,
   52,   53,   52,   53,    7,    8,    9,  140,   30,  141,
   43,   60,   53,   10,   30,   94,   95,   96,   97,   30,
    7,    8,    9,   72,   48,  146,  161,    4,    4,   71,
    5,  153,  162,  164,  166,   10,   11,   78,   46,   78,
   78,   78,   71,   67,   78,   78,   78,   78,   78,   78,
   78,   78,   78,   78,   78,   35,   39,   72,  151,   72,
   72,   72,  113,   75,   72,   72,   72,   72,   72,   72,
   72,   72,   72,   72,   72,   41,  103,   73,  147,   73,
   73,   73,  105,    0,   73,   73,   73,   73,   73,   73,
   73,   73,   73,   73,   73,   40,  131,    0,   79,    0,
   80,    0,    0,    0,   74,    0,   74,   74,   74,   12,
    0,   74,   74,   74,   74,   74,   74,   74,   74,   74,
   74,   74,    0,   39,   44,    0,   44,   44,   44,    0,
    0,   44,   44,   44,   44,   44,   44,   44,   10,    0,
    0,    0,   43,    0,   43,   43,   43,    0,    0,   43,
   43,   43,   43,   43,   43,   43,   48,   38,   48,   48,
   48,    0,    0,   48,   48,   48,   48,   48,   48,   48,
   46,   40,   46,   46,   46,    0,    0,   46,   46,   46,
   46,   46,   46,   46,    0,   70,    0,   35,    0,   35,
   35,   35,    0,    0,   35,   35,   35,   35,   35,   35,
   35,  154,    0,    0,    0,    0,    0,   41,    0,   41,
   41,   41,    0,    0,   41,   41,   41,   41,   41,   41,
   41,  112,    0,    0,    0,   16,    0,   40,    0,   40,
   40,   40,    0,    0,   40,   40,   40,   40,   40,   40,
   40,   12,   19,   12,   12,   12,   22,   74,   12,   12,
   12,   12,   12,   12,   12,   39,    0,   39,   39,   39,
    0,    0,   39,   39,   39,   39,   39,   39,   39,    0,
   10,    0,   10,   10,   10,    0,    0,   10,   10,   10,
   10,   10,   10,   10,    0,    0,    0,    0,    0,    1,
    0,    2,    3,    4,    0,    0,    5,    6,    7,    8,
    9,   10,   11,    1,    0,    2,    3,    4,    0,    0,
    5,    6,    7,    8,    9,   10,   11,    1,    0,    2,
    3,    4,    0,    0,    5,    6,    7,    8,    9,   10,
   11,    0,  144,    1,    0,    2,    0,    4,  148,    0,
    5,    6,    7,    8,    9,   10,   11,  156,    0,    0,
    0,    0,    0,   87,    0,    0,    0,   16,    0,  163,
    0,    6,    7,    8,    9,   16,   16,   16,   16,    0,
    0,    0,    0,    0,   19,    0,    0,    0,   22,    0,
    0,    0,   19,   19,   19,   19,   22,   22,   22,   22,
    1,    0,    2,    0,    4,    0,    0,    5,    6,    7,
    8,    9,   10,   11,    1,    0,    0,    0,    4,    0,
    0,    5,    6,    7,    8,    9,   10,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   61,   40,   41,   40,  123,   43,   45,   45,
   45,   44,   44,   44,   40,   41,   61,   44,   42,   45,
    0,   36,   61,   47,   60,   59,   62,   61,   44,   44,
   44,  262,  263,   41,   42,   43,   36,   45,  123,   47,
    0,   41,   42,   43,   44,   45,   65,   47,   31,   32,
  257,   34,   14,   14,   37,   44,   36,  125,   44,   46,
   60,   41,   62,   43,   44,   45,   40,    0,   32,   88,
   59,   17,   55,   59,   79,   80,   36,   39,   39,  259,
   60,   41,   62,   43,   44,   45,   32,    0,   43,  257,
   45,   40,  125,  125,  125,  114,   81,   82,  125,  257,
   60,   65,   62,   36,   68,    0,   44,  123,   41,  123,
   43,   44,   45,   44,   36,   44,   62,  100,  275,    0,
  103,   40,  258,   36,   88,  125,  257,   60,   41,   62,
  257,   44,   41,    0,  261,  257,   41,  264,   44,  126,
  257,   36,  269,  270,  261,  125,   41,  264,  257,   44,
    0,  257,  269,  270,  141,   36,   44,  118,  123,  265,
  266,  267,  268,   44,  257,  125,  153,  128,   59,   36,
    0,   44,  257,  265,  259,  260,  261,   44,  139,  264,
  265,  266,  267,  268,  269,  270,  147,   41,  276,  257,
    0,  259,  125,  261,   44,  257,  264,  265,  266,  267,
  268,  269,  270,   41,    0,   43,   41,   45,   43,   41,
   45,   43,  125,   45,  273,  257,  275,  263,    0,  123,
  257,  258,  257,  258,  266,  267,  268,  123,  273,  276,
  125,  257,  258,    0,  273,  271,  272,  273,  274,  273,
  266,  267,  268,  257,  125,   44,  123,  261,    0,  257,
  264,  262,   44,  263,  263,  269,  270,  257,  125,  259,
  260,  261,  257,   44,  264,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   12,  257,  143,  259,
  260,  261,   88,   49,  264,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   69,  257,  139,  259,
  260,  261,   71,   -1,  264,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   41,   -1,   43,   -1,
   45,   -1,   -1,   -1,  257,   -1,  259,  260,  261,  125,
   -1,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,   -1,  125,  257,   -1,  259,  260,  261,   -1,
   -1,  264,  265,  266,  267,  268,  269,  270,  125,   -1,
   -1,   -1,  257,   -1,  259,  260,  261,   -1,   -1,  264,
  265,  266,  267,  268,  269,  270,  257,  125,  259,  260,
  261,   -1,   -1,  264,  265,  266,  267,  268,  269,  270,
  257,  125,  259,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  270,   -1,  125,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  269,
  270,  125,   -1,   -1,   -1,   -1,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  269,
  270,  125,   -1,   -1,   -1,  125,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  269,
  270,  257,  125,  259,  260,  261,  125,   49,  264,  265,
  266,  267,  268,  269,  270,  257,   -1,  259,  260,  261,
   -1,   -1,  264,  265,  266,  267,  268,  269,  270,   -1,
  257,   -1,  259,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  270,   -1,   -1,   -1,   -1,   -1,  257,
   -1,  259,  260,  261,   -1,   -1,  264,  265,  266,  267,
  268,  269,  270,  257,   -1,  259,  260,  261,   -1,   -1,
  264,  265,  266,  267,  268,  269,  270,  257,   -1,  259,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  269,
  270,   -1,  134,  257,   -1,  259,   -1,  261,  140,   -1,
  264,  265,  266,  267,  268,  269,  270,  149,   -1,   -1,
   -1,   -1,   -1,  257,   -1,   -1,   -1,  257,   -1,  161,
   -1,  265,  266,  267,  268,  265,  266,  267,  268,   -1,
   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,  257,   -1,
   -1,   -1,  265,  266,  267,  268,  265,  266,  267,  268,
  257,   -1,  259,   -1,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  270,  257,   -1,   -1,   -1,  261,   -1,
   -1,  264,  265,  266,  267,  268,  269,  270,
};
}
final static short YYFINAL=13;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"'$'",null,null,null,"'('","')'","'*'","'+'",
"','","'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
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
null,null,null,null,null,null,null,null,"ID","CTE","CADENA","CLASS","IF","ELSE",
"END_IF","PRINT","VOID","SHORT","ULONG","FLOAT","WHILE","RETURN","MAYORIGUAL",
"MENORIGUAL","IGUAL","DISTINTO","MENOSMENOS","DO",
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
"listaDeclarativa : sentenciaDeclarativa",
"listaDeclarativa : sentenciaDeclarativa '$'",
"sentenciaDeclarativa : declaracionMetodo",
"sentenciaDeclarativa : declaracion ','",
"listaSentencias : listaDeclarativa ',' listaEjecutables",
"listaSentencias : listaDeclarativa",
"listaSentencias : listaEjecutables",
"listaSentencias : CADENA",
"asignacion : ID '=' expresion",
"asignacion : ID IGUAL expresion",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}'",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}'",
"sentenciaIf : IF '(' expresion comparador expresion ')' sentenciaEjecutable ',' ELSE sentenciaEjecutable ',' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' ',' END_IF",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' DO '{' listaEjecutables '}'",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' DO sentenciaEjecutable",
"sentenciaWhile : WHILE '(' expresion comparador ')' DO '{' listaEjecutables '}'",
"print : PRINT CADENA",
"return : RETURN",
"declaracionMetodo : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionMetodo : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"cuerpoMetodo : cuerpoMetodo listaSentencias",
"cuerpoMetodo : listaSentencias",
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
};

//#line 166 "gramatica.y"

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
        String archivo = "src\\AL-TEST-1.txt";
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
//#line 653 "Parser.java"
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
//#line 34 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma al final de linea");}
break;
case 12:
//#line 36 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',')");}
break;
case 19:
//#line 49 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 22:
//#line 54 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 27:
//#line 63 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 36:
//#line 76 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 43:
//#line 89 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");}
break;
case 44:
//#line 90 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 45:
//#line 93 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 46:
//#line 94 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": ERROR! Falta 'END_IF'");}
break;
case 47:
//#line 95 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 48:
//#line 96 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": ERROR! Falta 'END_IF'");}
break;
case 49:
//#line 97 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 51:
//#line 101 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 52:
//#line 102 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 53:
//#line 103 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 54:
//#line 106 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");}
break;
case 56:
//#line 112 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 57:
//#line 113 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 60:
//#line 120 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a metodo");}
break;
case 61:
//#line 121 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a metodo");}
break;
case 62:
//#line 122 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 63:
//#line 123 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 81:
//#line 153 "gramatica.y"
{comprobarRango(val_peek(0).sval);}
break;
//#line 910 "Parser.java"
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
