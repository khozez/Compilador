package Etapas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/*
  PARA LA TABLA DE SIMBOLOS USAMOS COMO CLAVE UN ENTERO, EN LA TEORIA RECOMIENDAN USAR EL LEXEMA PARA SER Etapas.MAS EFICIENTE
  PERO QUE PASA SI TENGO A=-33 Y B=33, EL LEXICO NO RECONOCE EL SIGNO ENTONCES CREA UNA UNICA ENTRADA EN LA
  TABLA DE SIMBOLOS, PERO LUEGO EL SINTACTICO SE DEBE DAR CUENTA QUE SON DOS VARIABLES DISTINTAS Y CREAR OTRA ENTRADA
*/

public class TablaSimbolos {
    public static final int NO_ENCONTRADO = -1;
    public static final String NO_ENCONTRADO_MESSAGE = "No encontrado";
    public static final String LEXEMA = "lexema";
    public static Set<Integer> obtenerConjuntoPunteros() {
        return simbolos.keySet();
    }
    private static final Map<Integer, Map<String, String>> simbolos = new HashMap<>();
    private static int identificador_siguiente = 1;


    public static boolean agregarSimbolo(String simbolo_nuevo) {
        if (obtenerSimbolo(simbolo_nuevo) == NO_ENCONTRADO) {
            Map<String, String> atributos = new HashMap<>();
            atributos.put(LEXEMA, simbolo_nuevo);
            simbolos.put(identificador_siguiente, atributos);
            ++identificador_siguiente;
            return true;
        }
        return false;
    }

    public static int obtenerID()
    {
        return identificador_siguiente-1;
    }

    public static ArrayList<Map<String, String>> getElementos()
    {
        ArrayList<Map<String, String>> elements = new ArrayList<Map<String, String>>();
        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            elements.add(entrada.getValue());
        }
        return elements;
    }

    public static int obtenerSimbolo(String lexema) {
        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            String lexema_actual = entrada.getValue().get(LEXEMA);

            if (lexema_actual.equals(lexema)) {
                return entrada.getKey();
            }
        }

        return NO_ENCONTRADO;
    }

    public static ArrayList<Integer> variablesEnHerencia(String heredada)
    {
        ArrayList<Integer> clave_variables_herencia = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            String lexema_actual = entrada.getValue().get(LEXEMA);

            if (lexema_actual.contains(heredada + ":main") && !lexema_actual.equals(heredada+":main")) {
                clave_variables_herencia.add(entrada.getKey());
            }
        }
        return clave_variables_herencia;
    }


    public static void eliminarSimbolo(int clave) {
        simbolos.remove(clave);
    }

    public static void agregarAtributo(int clave, String atributo, String valor) {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);
            atributos.put(atributo, valor);
        }
    }

    public static void eliminarAtributo(int clave, String atributo)
    {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);
            atributos.remove(atributo);
        }
    }

    public static void modificarAtributo(int clave, String atributo, String nuevo)
    {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);
            atributos.put(atributo, nuevo);
        }
    }

    public static String obtenerAtributo(int clave, String atributo) {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);

            if (atributos.containsKey(atributo)) {
                return atributos.get(atributo);
            }
        }
        return NO_ENCONTRADO_MESSAGE;
    }

    public static void agregarAtributos(int clave, Map<String, String> atributos)
    {
        simbolos.put(clave, atributos);
    }

    public static Map<String, String> obtenerAtributos(int clave) {
        if (simbolos.containsKey(clave)) {
            Map<String, String> copia = new HashMap<>();
            for (Map.Entry<String, String> entry : simbolos.get(clave).entrySet()) {
                copia.put(entry.getKey(), entry.getValue());
            }
            return copia;
        }
        return null;
    }

    public static ArrayList<String> imprimirTabla() {
        ArrayList<String> t = new ArrayList<>();
        String x;

        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            Map<String, String> atributos = entrada.getValue();
            x = entrada.getKey() + ": ";

            for (Map.Entry<String, String> atributo: atributos.entrySet()) {
                x = x + ("(" + atributo.getKey() + ": " + atributo.getValue() + ") ");
            }
            t.add(x);
        }
        return t;
    }

    public static void limpiarTabla()
    {
        ArrayList<Integer> a_eliminar = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            if (obtenerAtributo(entrada.getKey(), "tipo").equals(NO_ENCONTRADO_MESSAGE) && obtenerAtributo(entrada.getKey(), "uso").equals(NO_ENCONTRADO_MESSAGE) )
            {
                a_eliminar.add(entrada.getKey());
            }
        }
        a_eliminar.forEach(n -> simbolos.remove(n));
    }
}
