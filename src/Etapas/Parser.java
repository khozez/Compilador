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
    2,    2,    4,    4,    5,    5,    8,    8,    8,    6,
    6,    6,    6,   12,   12,   12,    7,    7,   14,   14,
   14,   15,   15,   15,   15,   15,   15,   15,   20,   20,
   20,   21,   21,    3,    3,    3,    3,    9,    9,   16,
   16,   16,   16,   16,   16,   17,   17,   17,   18,   19,
   13,   13,   25,   25,   10,   10,   10,   10,   22,   22,
   26,   26,   11,   11,   11,   11,   23,   23,   23,   27,
   27,   27,   28,   28,   28,   28,   28,   24,   24,   24,
   24,   24,   24,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    2,    1,    1,    6,    5,
    3,    2,    2,    1,    1,    1,    3,    3,    3,    4,
    2,    3,    2,    3,    2,    1,    3,    2,    3,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    3,    1,
    2,    1,    2,    3,    1,    1,    1,    3,    3,   14,
   13,   10,    9,   12,    8,   10,    8,    9,    2,    1,
    9,    7,    2,    1,    4,    3,    4,    5,    2,    2,
    3,    1,    1,    1,    1,    1,    1,    3,    3,    1,
    3,    3,    1,    2,    1,    2,    3,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   47,    0,    0,    0,    0,   73,   74,   75,    0,
   60,    0,    0,    0,    7,    8,   38,   32,   37,    0,
   42,    0,    0,   33,   34,   35,   36,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   59,    0,    0,    2,
    0,    3,    5,    6,    0,   70,   69,    0,   30,   31,
    0,   41,   43,    0,   85,    0,    0,    0,    0,   80,
    0,   17,   18,   19,    0,    0,   66,    0,    0,    0,
    0,   11,    0,    0,    0,    1,    0,   29,    0,   39,
   84,   86,    0,    0,    0,    0,    0,   67,    0,    0,
   65,    0,    0,   14,   15,    0,    0,    0,   88,   89,
   90,   91,   92,   93,    0,   76,    0,    0,    0,    0,
   71,   87,    0,    0,   81,   82,   68,   21,    0,   13,
    0,    0,   28,    0,    0,    0,    0,    0,    9,   27,
   22,    0,    0,   64,    0,    0,    0,    0,    0,   20,
    0,    0,    0,   62,   63,    0,    0,    0,   25,    0,
    0,   55,    0,    0,    0,    0,   57,   24,    0,    0,
   61,   58,    0,    0,   52,    0,   56,    0,    0,    0,
   54,    0,   50,
};
final static short yydgoto[] = {                         13,
   14,   15,   16,   93,   94,   95,   96,   17,   18,   19,
   20,  140,   21,   22,   23,   24,   25,   26,   27,   28,
   29,   30,   58,  105,  135,   47,   59,   60,
};
final static short yysindex[] = {                       -84,
  -38,    0, -228,   -8, -206, -201,    0,    0,    0,   29,
    0,  253,    0,  267,    0,    0,    0,    0,    0, -181,
    0,   43,  -11,    0,    0,    0,    0,   53,   69,   78,
  -28, -164,  -28,  -25,   15,  -28,    0,   85,  -28,    0,
  281,    0,    0,    0,  -37,    0,    0,  382,    0,    0,
  368,    0,    0, -136,    0, -115,  -28,   76,   49,    0,
  -38,    0,    0,    0,   76,  -58,    0,  103, -108,   57,
 -150,    0,  -34,  -41,  -34,    0, -105,    0,   43,    0,
    0,    0,  116,  -28,  -28,  -28,  -28,    0,  -10,  119,
    0,  118,  317,    0,    0,  121,  -94,  125,    0,    0,
    0,    0,    0,    0,  -28,    0,   51,  -85,  -35,  129,
    0,    0,   49,   49,    0,    0,    0,    0,  145,    0,
  -69,  -40,    0,  163,  354,  169,  -64,  166,    0,    0,
    0,  -43,  -13,    0,  -67,   95,   97,  -55,   16,    0,
  382,  -39,  184,    0,    0,  354,  382, -116,    0,  -43,
  -31,    0,  -16,  297,  -30,  382,    0,    0, -190,  382,
    0,    0,    6,  124,    0,  208,    0,  382,  -14,    8,
    0,   -9,    0,
};
final static short yyrindex[] = {                         0,
   -2,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  171,    0,    0,    0,    0,    0,  191,  151,    0,
    0,    0,    0,    0,  205,    0,    0,    0,    0,    0,
  264,    0,    0,    0,  209,    0,    0,    0,    0,    0,
    0,    0,    0,    1,    0,    0,    0,   88,   21,    0,
    0,    0,    0,    0,  106,   -7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  219,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -2,    0,    0,    0,  321,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  209,
    0,    0,   41,   68,    0,    0,    0,    0,  234,    0,
    0,  338,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  342,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  120,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  134,    0,
};
final static short yygindex[] = {                         0,
  265,   13,   33,    0,  186,    0,    0,  251,   58,  252,
   -4,  147,  -22,  -33,    7,    0,    0,    0,    0,    0,
  248,    0,  472,  228,  158,  240,   -5,   50,
};
final static int YYTABLESIZE=652;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        107,
   83,   34,   33,  131,   57,  127,  156,   32,   84,   56,
   85,   57,   48,   48,   57,   67,   56,   79,  132,   56,
   77,   77,   33,   33,   50,  103,   43,  104,   35,   69,
  142,   36,   49,   83,   83,   83,   83,   83,   12,   83,
   78,   83,   83,   83,   83,   83,   44,   83,   98,   48,
   33,   48,   37,   43,   78,   38,   77,  144,   72,  149,
   83,   77,   83,   77,   77,   77,   97,   79,   39,  108,
   98,  164,  165,   44,  150,   45,   78,   46,  113,  114,
   77,   78,   77,   78,   78,   78,   48,   49,   97,   63,
   86,   68,   61,  159,  162,   87,   51,   91,  130,   84,
   78,   85,   78,   79,   52,   48,   92,  151,   79,  141,
   79,   79,   79,  155,    6,    7,    8,    9,   84,   53,
   85,   53,  163,   49,   74,   83,   90,   79,   49,   79,
  167,   49,  172,   51,  170,  115,  116,   71,   81,  143,
   61,   48,   82,   88,    4,   77,   48,    5,   89,   48,
   40,  110,   10,   11,  157,   53,  112,  134,   84,  117,
   85,  118,  122,   53,  121,   78,  166,  145,  123,   51,
   46,  126,    1,  125,    2,    3,    4,   51,  134,    5,
    6,    7,    8,    9,   10,   11,  145,   77,  129,    1,
   45,    2,   79,    4,   40,    6,    5,    6,    7,    8,
    9,   10,   11,  133,   12,   84,  138,   85,   84,  136,
   85,  137,   49,  139,   31,  106,   81,  146,   44,  147,
  148,   54,   55,  152,    7,    8,    9,  153,   54,   55,
   48,   66,   55,   10,   31,   31,   99,  100,  101,  102,
    7,    8,    9,   61,   53,  160,  168,    4,  171,   76,
    5,  169,   72,  173,   76,   10,   11,   83,   51,   83,
   83,   83,   31,    4,   83,   83,   83,   83,   83,   83,
   83,   83,   83,   83,   83,   40,   41,   77,  120,   77,
   77,   77,   62,   64,   77,   77,   77,   77,   77,   77,
   77,   77,   77,   77,   77,   46,  158,   78,   80,   78,
   78,   78,  109,  154,   78,   78,   78,   78,   78,   78,
   78,   78,   78,   78,   78,   45,  111,    0,    0,    0,
    0,    0,    0,    0,   79,    0,   79,   79,   79,   12,
    0,   79,   79,   79,   79,   79,   79,   79,   79,   79,
   79,   79,    0,   44,   49,    0,   49,   49,   49,    0,
    0,   49,   49,   49,   49,   49,   49,   49,   10,    0,
    0,    0,   48,    0,   48,   48,   48,    0,    0,   48,
   48,   48,   48,   48,   48,   48,   53,   40,   53,   53,
   53,    0,    0,   53,   53,   53,   53,   53,   53,   53,
   51,   42,   51,   51,   51,    0,    0,   51,   51,   51,
   51,   51,   51,   51,    0,   76,    0,   40,    0,   40,
   40,   40,    0,    0,   40,   40,   40,   40,   40,   40,
   40,  161,    0,    0,    0,    0,    0,   46,    0,   46,
   46,   46,    0,    0,   46,   46,   46,   46,   46,   46,
   46,  119,    0,    0,    0,   16,    0,   45,    0,   45,
   45,   45,    0,    0,   45,   45,   45,   45,   45,   45,
   45,   12,   23,   12,   12,   12,   26,    0,   12,   12,
   12,   12,   12,   12,   12,   44,    0,   44,   44,   44,
    0,    0,   44,   44,   44,   44,   44,   44,   44,    0,
   10,    0,   10,   10,   10,    0,    0,   10,   10,   10,
   10,   10,   10,   10,   65,   70,    0,   73,    0,    1,
   75,    2,    3,    4,    0,    0,    5,    6,    7,    8,
    9,   10,   11,    1,    0,    2,    3,    4,   83,    0,
    5,    6,    7,    8,    9,   10,   11,    1,    0,    2,
    3,    4,    0,    0,    5,    6,    7,    8,    9,   10,
   11,    0,    0,    1,    0,    2,    0,    4,    0,    0,
    5,    6,    7,    8,    9,   10,   11,    0,    0,    0,
    0,    0,    0,   92,    0,    0,  124,   16,    0,    0,
  128,    6,    7,    8,    9,   16,   16,   16,   16,    0,
    0,    0,    0,    0,   23,    0,    0,    0,   26,    0,
    0,    0,   23,   23,   23,   23,   26,   26,   26,   26,
    1,    0,    2,    0,    4,    0,    0,    5,    6,    7,
    8,    9,   10,   11,    1,    0,    0,    0,    4,    0,
    0,    5,    6,    7,    8,    9,   10,   11,   61,    0,
    0,    0,    4,    0,    0,    5,    0,    0,    0,    0,
   10,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   61,   44,   40,   41,  123,   46,   43,   45,
   45,   40,   44,   44,   40,   41,   45,   51,   59,   45,
    0,   59,   61,   61,   36,   60,   14,   62,  257,   34,
   44,   40,   44,   41,   42,   43,   36,   45,  123,   47,
    0,   41,   42,   43,   44,   45,   14,   47,   71,   44,
   61,   44,  259,   41,   48,  257,   36,  125,   44,   44,
   60,   41,   62,   43,   44,   45,   71,    0,   40,   74,
   93,  262,  263,   41,   59,  257,   36,   20,   84,   85,
   60,   41,   62,   43,   44,   45,   44,    0,   93,   32,
   42,   34,  257,  125,  125,   47,   44,   41,  121,   43,
   60,   45,   62,   36,   36,    0,  257,  141,   41,  123,
   43,   44,   45,  147,  265,  266,  267,  268,   43,    0,
   45,   44,  156,   36,   40,  125,   69,   60,   41,   62,
  125,   44,  125,    0,  168,   86,   87,  123,  275,  133,
  257,   36,  258,   41,  261,  125,   41,  264,  257,   44,
    0,  257,  269,  270,  148,   36,   41,  125,   43,   41,
   45,   44,  257,   44,   44,  125,  160,  135,   44,   36,
    0,  257,  257,  123,  259,  260,  261,   44,  146,  264,
  265,  266,  267,  268,  269,  270,  154,   59,   44,  257,
    0,  259,  125,  261,   44,  265,  264,  265,  266,  267,
  268,  269,  270,   41,    0,   43,   41,   45,   43,   41,
   45,  276,  125,  257,  273,  257,  275,  123,    0,  123,
  276,  257,  258,  263,  266,  267,  268,   44,  257,  258,
  125,  257,  258,    0,  273,  273,  271,  272,  273,  274,
  266,  267,  268,  257,  125,  262,  123,  261,  263,  257,
  264,   44,   44,  263,  257,  269,  270,  257,  125,  259,
  260,  261,  273,    0,  264,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   12,  257,   93,  259,
  260,  261,   32,   32,  264,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,  150,  257,   51,  259,
  260,  261,   75,  146,  264,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   77,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,   -1,  259,  260,  261,  125,
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
  270,  257,  125,  259,  260,  261,  125,   -1,  264,  265,
  266,  267,  268,  269,  270,  257,   -1,  259,  260,  261,
   -1,   -1,  264,  265,  266,  267,  268,  269,  270,   -1,
  257,   -1,  259,  260,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  270,   33,   34,   -1,   36,   -1,  257,
   39,  259,  260,  261,   -1,   -1,  264,  265,  266,  267,
  268,  269,  270,  257,   -1,  259,  260,  261,   57,   -1,
  264,  265,  266,  267,  268,  269,  270,  257,   -1,  259,
  260,  261,   -1,   -1,  264,  265,  266,  267,  268,  269,
  270,   -1,   -1,  257,   -1,  259,   -1,  261,   -1,   -1,
  264,  265,  266,  267,  268,  269,  270,   -1,   -1,   -1,
   -1,   -1,   -1,  257,   -1,   -1,  105,  257,   -1,   -1,
  109,  265,  266,  267,  268,  265,  266,  267,  268,   -1,
   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,  257,   -1,
   -1,   -1,  265,  266,  267,  268,  265,  266,  267,  268,
  257,   -1,  259,   -1,  261,   -1,   -1,  264,  265,  266,
  267,  268,  269,  270,  257,   -1,   -1,   -1,  261,   -1,
   -1,  264,  265,  266,  267,  268,  269,  270,  257,   -1,
   -1,   -1,  261,   -1,   -1,  264,   -1,   -1,   -1,   -1,
  269,  270,
};
}
final static short YYFINAL=13;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"'$'",null,null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,
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
"sentenciaEjecutable : referenciaClase",
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

//#line 173 "gramatica.y"

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
//#line 667 "Parser.java"
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
case 9:
//#line 33 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 10:
//#line 34 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma al final de linea");}
break;
case 11:
//#line 35 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de declaración de clase.");}
break;
case 12:
//#line 36 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',')");}
break;
case 23:
//#line 55 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 26:
//#line 60 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 31:
//#line 69 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 41:
//#line 83 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Falta coma (',') al final de linea");}
break;
case 48:
//#line 96 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Asignación");}
break;
case 49:
//#line 97 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Una asignación no se debe realizar con ==");}
break;
case 50:
//#line 100 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 51:
//#line 101 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": ERROR! Falta 'END_IF'");}
break;
case 52:
//#line 102 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 53:
//#line 103 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": ERROR! Falta 'END_IF'");}
break;
case 54:
//#line 104 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia IF");}
break;
case 56:
//#line 108 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 57:
//#line 109 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de sentencia WHILE");}
break;
case 58:
//#line 110 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! Mal definida la condicion");}
break;
case 59:
//#line 113 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de cadena");}
break;
case 61:
//#line 119 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 62:
//#line 120 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Fin de Metodo");}
break;
case 65:
//#line 127 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a metodo");}
break;
case 66:
//#line 128 "gramatica.y"
{System.out.println("LINEA "+(AnalizadorLexico.getCantLineas())+": Invocación a metodo");}
break;
case 67:
//#line 129 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una asignación como parametro.");}
break;
case 68:
//#line 130 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas())+": ERROR! No se puede invocar una funcion con una declaración como parametro.");}
break;
case 86:
//#line 160 "gramatica.y"
{comprobarRango(val_peek(0).sval);}
break;
//#line 932 "Parser.java"
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
