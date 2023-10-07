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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    1,    1,    2,    2,
    2,    2,    4,    4,    5,    5,    6,    6,    6,    9,
    9,    9,    7,    7,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   18,   18,   18,   19,   19,    3,    3,
    3,    3,   13,   13,   14,   14,   14,   14,   15,   15,
   16,   16,   10,   10,   23,   17,   17,   20,   20,   24,
   24,    8,    8,    8,    8,   21,   21,   21,   25,   25,
   25,   26,   26,   26,   26,   26,   22,   22,   22,   22,
   22,   22,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    2,    1,    1,    6,    5,
    3,    2,    2,    1,    1,    1,    4,    3,    2,    3,
    2,    1,    3,    2,    3,    2,    2,    1,    1,    1,
    1,    1,    1,    3,    2,    2,    1,    1,    3,    1,
    1,    1,    3,    3,   14,   13,   10,    9,    9,    8,
    2,    3,    9,    7,    1,    5,    3,    2,    2,    3,
    1,    1,    1,    1,    1,    1,    3,    3,    1,    3,
    3,    1,    2,    1,    2,    3,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   62,   63,   64,    0,    0,
    0,    0,    0,    7,    8,    0,   37,    0,    0,   28,
   29,   30,   32,   33,    0,    0,   38,    0,    0,    0,
    0,    0,    0,    0,    0,   74,    0,   51,    0,    0,
    0,   69,    2,    0,    3,    5,    6,    0,   59,   58,
    0,   26,   27,    0,   35,   36,    0,    0,   65,   57,
    0,    0,   11,    0,    0,    0,   73,   75,    0,    0,
    0,   52,    0,    0,    1,    0,    0,   31,   25,    0,
   34,    0,    0,   14,   15,    0,    0,    0,   77,   78,
   79,   80,   81,   82,    0,    0,    0,    0,   76,    0,
    0,   70,   71,    0,   60,   56,    0,   13,    0,    0,
   24,    0,    0,    0,    0,    0,    9,   23,   18,    0,
    0,   55,    0,    0,    0,    0,    0,   17,    0,   54,
    0,    0,    0,   21,    0,    0,    0,   50,    0,   20,
    0,   53,   49,    0,   47,    0,    0,    0,   45,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   83,   84,   85,   86,   16,  128,   17,
   18,   19,   20,   21,   22,   23,   24,   25,   26,   27,
   40,   95,  123,   50,   41,   42,
};
final static short yysindex[] = {                       -85,
  -38,    0, -251,  -21, -225,    0,    0,    0,   13,  -29,
  267,    0,  281,    0,    0, -221,    0,   28,  -22,    0,
    0,    0,    0,    0,   35,   11,    0,  -12,  -12,  -41,
   -9,  -12,   50,  -12, -173,    0, -152,    0,  -12,  -19,
   31,    0,    0,  297,    0,    0,    0,  -30,    0,    0,
 -162,    0,    0,  316,    0,    0,   46,   46,    0,    0,
 -147,  -65,    0,  -35,  -26,  -35,    0,    0,   92,  -12,
  -12,    0,  -12,  -12,    0, -140,  -38,    0,    0,   28,
    0,   77, -107,    0,    0,   78, -133,   88,    0,    0,
    0,    0,    0,    0,  -12,   16, -121,  -36,    0,   31,
   31,    0,    0,   81,    0,    0,  100,    0, -117,    8,
    0,  102,  330,  112,   34,  111,    0,    0,    0,  -95,
   44,    0,   39,   45, -162,   54,   10,    0, -162,    0,
  330,  -37, -162,    0,  -95,  -32,   40,    0,  -31,    0,
 -204,    0,    0,   56,    0, -162,  -27,  -94,    0,
};
final static short yyrindex[] = {                         0,
  -84,   87,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  191,    0,    0,
    0,    0,    0,    0,  205,    0,    0,    0,    0,    0,
  220,    0,    0,    0,    1,    0,    0,    0,    0,    0,
   21,    0,    0,  186,    0,    0,    0,  105,    0,    0,
    0,    0,    0,    0,    0,    0,  119,  134,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  234,
    0,    0,    0,    0,    0,  -69,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   41,
   68,    0,    0,  105,    0,    0,  252,    0,    0,  158,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  178,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  151,    0,    0,    0,    0,    0,    0,  171,    0,
};
final static short yygindex[] = {                         0,
  179,  -10,    7,    0,  106,    0,    0,    9,   59,  -13,
   -4,  153,  190,    0,    0,    0,    0,    0,  154,    0,
  540,  143,   79,  135,    5,   42,
};
final static int YYTABLESIZE=638;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   72,   30,   46,   39,  115,   31,   51,   70,   37,   71,
   39,   51,   51,   53,   96,   37,   51,  107,   32,   47,
   66,   52,   29,   70,   93,   71,   94,   39,   76,   38,
   29,   33,   37,   46,   63,   48,   72,   11,   61,   72,
   67,   72,   72,   72,   72,   72,   56,   72,   88,   80,
   47,  119,   34,  134,   55,   16,   66,  144,  145,   72,
   72,   66,   72,   66,   66,   66,  120,   68,  135,   88,
   87,   51,   73,   97,  100,  101,   67,   74,   54,   66,
   66,   67,   66,   67,   67,   67,   42,  138,   70,   65,
   71,   87,  141,  143,   77,  118,   78,  148,    4,   67,
   67,   67,   67,   68,   61,   68,    9,   10,   68,   82,
   68,   68,   68,   62,  102,  103,  104,  106,   44,  122,
  132,  109,   31,  110,  136,   72,   68,   68,  139,   68,
   31,  111,   99,   43,   70,  114,   71,  122,  113,   76,
   61,  147,  121,  117,   70,   66,   71,    5,   61,   59,
   48,  126,  124,   70,   44,   71,  125,    5,    6,    7,
    8,  127,   44,  130,  142,   67,  129,  131,  149,   43,
   46,    1,   65,    2,    3,    4,  133,   43,  146,    5,
    6,    7,    8,    9,   10,    4,   48,   16,  108,   44,
   41,   59,   68,  140,   48,   16,   16,   16,   16,    5,
    6,    7,    8,   79,   40,   49,   46,   81,   98,  137,
  105,   42,    0,    0,   46,   59,    0,    0,    0,   12,
   35,   36,    0,    0,    6,    7,    8,   35,   36,   61,
   59,    0,    0,   39,   28,   89,   90,   91,   92,    6,
    7,    8,   28,   44,   35,   36,    0,    0,    0,    0,
    0,   10,    0,    0,    0,    0,    0,   72,   43,   72,
   72,   72,    0,    0,    0,   72,   72,   72,   72,   72,
   72,   72,   72,   72,   72,   48,    0,   66,    0,   66,
   66,   66,   19,    0,    0,   66,   66,   66,   66,   66,
   66,   66,   66,   66,   66,   46,    0,   67,    0,   67,
   67,   67,   22,    0,    0,   67,   67,   67,   67,   67,
   67,   67,   67,   67,   67,   41,    0,    0,    0,    0,
    0,    0,    0,    0,   68,    0,   68,   68,   68,   40,
    0,    0,   68,   68,   68,   68,   68,   68,   68,   68,
   68,   68,    0,   42,   12,   42,   42,   42,    0,    0,
    0,   42,   42,   42,   42,   42,   42,    0,   39,    0,
    0,   61,    0,   61,   61,   61,    0,    0,    0,   61,
   61,   61,   61,   61,   61,   44,   10,   44,   44,   44,
    0,    0,    0,   44,   44,   44,   44,   44,   44,    0,
   43,   43,   43,   43,   43,    0,    0,    0,   43,   43,
   43,   43,   43,   43,    0,   45,    0,   48,    0,   48,
   48,   48,    0,    0,   19,   48,   48,   48,   48,   48,
   48,   75,   19,   19,   19,   19,    0,   46,    0,   46,
   46,   46,    0,    0,   22,   46,   46,   46,   46,   46,
   46,    0,   22,   22,   22,   22,    0,   41,    0,   41,
   41,   41,    0,    0,    0,   41,   41,   41,   41,   41,
   41,   40,    0,   40,   40,   40,    0,    0,    0,   40,
   40,   40,   40,   40,   40,    0,   12,    0,   12,   12,
   12,    0,    0,    0,   12,   12,   12,   12,   12,   12,
   39,    0,   39,   39,   39,    0,    0,    0,   39,   39,
   39,   39,   39,   39,    0,    0,    0,    0,   10,    0,
   10,   10,   10,    0,    0,    0,   10,   10,   10,   10,
   10,   10,    0,    1,    0,    2,    3,    4,    0,    0,
    0,    5,    6,    7,    8,    9,   10,    1,    0,    2,
    3,    4,    0,    0,    0,    5,    6,    7,    8,    9,
   10,    0,    0,    1,    0,    2,    3,    4,    0,    0,
    0,    5,    6,    7,    8,    9,   10,   57,   58,    0,
    0,   64,    1,   66,   78,    0,    4,    0,   69,    0,
    5,    6,    7,    8,    9,   10,    1,    0,    2,    0,
    4,    0,    0,    0,    5,    6,    7,    8,    9,   10,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  112,    0,    0,  116,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   13,   40,   41,  257,   44,   43,   45,   45,
   40,   44,   44,   36,   41,   45,   44,  125,   40,   13,
    0,   44,   61,   43,   60,   45,   62,   40,   59,   59,
   61,  257,   45,   44,   44,  257,   36,  123,   30,   59,
    0,   41,   42,   43,   44,   45,   36,   47,   62,   54,
   44,   44,   40,   44,   44,  125,   36,  262,  263,   59,
   60,   41,   62,   43,   44,   45,   59,    0,   59,   83,
   62,   44,   42,   65,   70,   71,   36,   47,   44,   59,
   60,   41,   62,   43,   44,   45,    0,  125,   43,   40,
   45,   83,  125,  125,  257,  109,  259,  125,  261,   59,
   60,  275,   62,   36,    0,  258,  269,  270,   41,  257,
   43,   44,   45,  123,   73,   74,  257,   41,    0,  113,
  125,   44,   36,  257,  129,  125,   59,   60,  133,   62,
   44,   44,   41,    0,   43,  257,   45,  131,  123,   59,
   36,  146,   41,   44,   43,  125,   45,  265,   44,  257,
    0,   41,   41,   43,   36,   45,  123,  265,  266,  267,
  268,  257,   44,  125,  125,  125,  123,  123,  263,   36,
    0,  257,  257,  259,  260,  261,  123,   44,  123,  265,
  266,  267,  268,  269,  270,    0,   36,  257,   83,   11,
    0,  257,  125,  135,   44,  265,  266,  267,  268,  265,
  266,  267,  268,   51,    0,   16,   36,   54,   66,  131,
   76,  125,   -1,   -1,   44,  257,   -1,   -1,   -1,    0,
  257,  258,   -1,   -1,  266,  267,  268,  257,  258,  125,
  257,   -1,   -1,    0,  273,  271,  272,  273,  274,  266,
  267,  268,  273,  125,  257,  258,   -1,   -1,   -1,   -1,
   -1,    0,   -1,   -1,   -1,   -1,   -1,  257,  125,  259,
  260,  261,   -1,   -1,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   -1,  257,   -1,  259,
  260,  261,  125,   -1,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   -1,  257,   -1,  259,
  260,  261,  125,   -1,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,  273,  274,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,   -1,  259,  260,  261,  125,
   -1,   -1,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,   -1,  257,  125,  259,  260,  261,   -1,   -1,
   -1,  265,  266,  267,  268,  269,  270,   -1,  125,   -1,
   -1,  257,   -1,  259,  260,  261,   -1,   -1,   -1,  265,
  266,  267,  268,  269,  270,  257,  125,  259,  260,  261,
   -1,   -1,   -1,  265,  266,  267,  268,  269,  270,   -1,
  257,  125,  259,  260,  261,   -1,   -1,   -1,  265,  266,
  267,  268,  269,  270,   -1,  125,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,  257,  265,  266,  267,  268,  269,
  270,  125,  265,  266,  267,  268,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,  257,  265,  266,  267,  268,  269,
  270,   -1,  265,  266,  267,  268,   -1,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,  265,  266,  267,  268,  269,
  270,  257,   -1,  259,  260,  261,   -1,   -1,   -1,  265,
  266,  267,  268,  269,  270,   -1,  257,   -1,  259,  260,
  261,   -1,   -1,   -1,  265,  266,  267,  268,  269,  270,
  257,   -1,  259,  260,  261,   -1,   -1,   -1,  265,  266,
  267,  268,  269,  270,   -1,   -1,   -1,   -1,  257,   -1,
  259,  260,  261,   -1,   -1,   -1,  265,  266,  267,  268,
  269,  270,   -1,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,  265,  266,  267,  268,  269,  270,  257,   -1,  259,
  260,  261,   -1,   -1,   -1,  265,  266,  267,  268,  269,
  270,   -1,   -1,  257,   -1,  259,  260,  261,   -1,   -1,
   -1,  265,  266,  267,  268,  269,  270,   28,   29,   -1,
   -1,   32,  257,   34,  259,   -1,  261,   -1,   39,   -1,
  265,  266,  267,  268,  269,  270,  257,   -1,  259,   -1,
  261,   -1,   -1,   -1,  265,  266,  267,  268,  269,  270,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   95,   -1,   -1,   98,
};
}
final static short YYFINAL=12;
final static short YYMAXTOKEN=275;
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
"MENORIGUAL","IGUAL","DISTINTO","MENOSMENOS",
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
"sentenciaEjecutable : CADENA",
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
"listaSentencias : CADENA",
"asignacion : ID '=' expresion",
"asignacion : ID IGUAL expresion",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' ELSE '{' listaEjecutables '}'",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}' END_IF",
"sentenciaIf : IF '(' expresion comparador expresion ')' '{' listaEjecutables '}'",
"sentenciaWhile : WHILE '(' expresion comparador expresion ')' '{' listaEjecutables '}'",
"sentenciaWhile : WHILE '(' expresion comparador ')' '{' listaEjecutables '}'",
"return : RETURN ';'",
"return : RETURN expresion ';'",
"declaracionMetodo : VOID ID '(' tipo ID ')' '{' cuerpoMetodo '}'",
"declaracionMetodo : VOID ID '(' ')' '{' cuerpoMetodo '}'",
"cuerpoMetodo : listaSentencias",
"invocacionMetodo : ID '(' tipo ID ')'",
"invocacionMetodo : ID '(' ')'",
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

//#line 158 "gramatica.y"

public static final String ERROR_LEXICO = "Error";
public static final String ERROR_SINTACTICO = "Error_sintactico";
public static final String WARNING = "Warning";
public static List<String> errorLexico = new ArrayList<>();
public static List<String> errorSintactico = new ArrayList<>();


void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
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
    	case "Warning":
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
                AnalizadorLexico.TS.imprimirTabla();
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
//#line 581 "Parser.java"
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
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Programa vacio");}
break;
case 3:
//#line 23 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta llave de apertura '{'");}
break;
case 4:
//#line 24 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta llave de cierre '}'");}
break;
case 10:
//#line 34 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta coma al final de linea");}
break;
case 12:
//#line 36 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta coma (',')");}
break;
case 19:
//#line 49 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta coma (',') al final de linea");}
break;
case 22:
//#line 54 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta coma (',') al final de linea");}
break;
case 27:
//#line 63 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta coma (',') al final de linea");}
break;
case 36:
//#line 76 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta coma (',') al final de linea");}
break;
case 44:
//#line 90 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Una asignación no se debe realizar con ==");}
break;
case 46:
//#line 94 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta 'END_IF'");}
break;
case 48:
//#line 96 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Falta 'END_IF'");}
break;
case 50:
//#line 100 "gramatica.y"
{anotar(ERROR_SINTACTICO, "LINEA "+(AnalizadorLexico.getCantLineas()+1)+": Mal definida la condicion");}
break;
//#line 782 "Parser.java"
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
