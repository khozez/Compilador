package Etapas;

import java.io.*;
import AccionesSemanticas.*;


public class AnalizadorLexico {
    public static final char TAB = '\t';
    public static final char BLANCO = ' ';
    public static final char NUEVA_LINEA = '\n';
    public static final char DIGITO = 'd';
    public static final char MAYUSCULA = 'M';
    public static final char MINUSCULA = 'm';
    public static final char PORCIENTO = '%';
    public static final int IDENTIFICADOR = 257;
    public static final int CONSTANTE = 258;
    public static final int CADENA = 259;
    public static final int MAX_LONG_ID = 20;
    public static final int MAX_SHORT_INT = (int) Math.pow(2, 7) - 1;
    public static final int MIN_SHORT_INT = (int) -Math.pow(2, 7);
    public static final long MAX_ULONG_INT = (long) Math.pow(2, 32) - 1;
    public static final double MIN_FLOAT_VALUE = 1.17549435E-38;
    public static final double MAX_FLOAT_VALUE = 3.40282347E+38;
    public static final double MIN_NEGATIVE_FLOAT_VALUE = -3.40282347E+38;
    public static final double MAX_NEGATIVE_FLOAT_VALUE = -1.17549435E-38;
    public static BufferedInputStream lector;
    private static TablaPalabrasReservadas PR = new TablaPalabrasReservadas();
    public static TablaSimbolos TS = new TablaSimbolos();

    private static int cant_lineas = 1;
    private static int posicion = 0;
    private char entry;
    private static MTE matriz_estados = new MTE();
    private static MAS mas = new MAS();
    public static String lexema = "";
    public static int estado = 0;

    public static void newLine(){
        posicion = 0;
        cant_lineas += 1;
    }

    public static int getCantLineas(){ return cant_lineas;}

    public static int getPosicon() { return posicion;}

    private static char getTipo(char c){
        int caracter = c;
        if (caracter >= 48 && caracter <= 57){
            return DIGITO;
        }
        if (caracter >= 65 && caracter <= 90) {
            if (c != 'E')
                return MAYUSCULA;
        }
        if (caracter >= 97 && caracter <=122){
            if (c != 'e' && c != 'u' && c != 's' && c != 'l')
                return MINUSCULA;
        }
        if (caracter == 37)
            return PORCIENTO;
        if (caracter == 13)
            return NUEVA_LINEA;
        return c;
    }

    public static int getToken(BufferedInputStream lector, char c){
        posicion+=1;
        int id_columna;
        switch (getTipo(c)){
            case BLANCO:
                id_columna = 0;
                break;
            case TAB:
                id_columna = 1;
                break;
            case NUEVA_LINEA:
                id_columna = 2;
                break;
            case 'E':
                id_columna = 3;
                break;
            case 'e':
                id_columna = 4;
                break;
            case 'u':
                id_columna = 5;
                break;
            case 's':
                id_columna = 6;
                break;
            case 'l':
                id_columna = 7;
                break;
            case MINUSCULA:
                id_columna = 8;
                break;
            case MAYUSCULA:
                id_columna = 9;
                break;
            case DIGITO:
                id_columna = 10;
                break;
            case '_':
                id_columna = 11;
                break;
            case '=':
                id_columna = 12;
                break;
            case '!':
                id_columna = 13;
                break;
            case '/':
                id_columna = 14;
                break;
            case '*':
                id_columna = 15;
                break;
            case '+':
                id_columna = 16;
                break;
            case '-':
                id_columna = 17;
                break;
            case '(':
                id_columna = 18;
                break;
            case ')':
                id_columna = 19;
                break;
            case '{':
                id_columna = 20;
                break;
            case '}':
                id_columna = 21;
                break;
            case ';':
                id_columna = 22;
                break;
            case ',':
                id_columna = 23;
                break;
            case '.':
                id_columna = 24;
                break;
            case '<':
                id_columna = 25;
                break;
            case '>':
                id_columna = 26;
                break;
            case PORCIENTO:
                id_columna = 27;
                break;
            default:
                id_columna = 28;
                break;
        }

        //System.out.println("Caracter:"+c+" - Estado:"+estado+" id_c:"+id_columna);
        AccionSemantica as = mas.action_matrix[estado][id_columna];
        int id_token = as.ejecutar(lector, lexema);
        estado = matriz_estados.states_matrix[estado][id_columna];
        //System.out.println("Proximo estado:"+estado);
        return id_token;
    }
}
