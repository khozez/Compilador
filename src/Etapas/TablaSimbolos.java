package Etapas;

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

    public static int obtenerSimbolo(String lexema) {
        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            String lexema_actual = entrada.getValue().get(LEXEMA);

            if (lexema_actual.equals(lexema)) {
                return entrada.getKey();
            }
        }

        return NO_ENCONTRADO;
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

    public static Map<String, String> obtenerAtributos(int clave) {
        if (simbolos.containsKey(clave)) {
            return simbolos.get(clave);
        }
        return null;
    }

    public static void imprimirTabla() {
        System.out.println("\nEtapas.TablaSimbolos:");

        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            Map<String, String> atributos = entrada.getValue();
            System.out.print(entrada.getKey() + ": ");

            for (Map.Entry<String, String> atributo: atributos.entrySet()) {
                System.out.print("(" + atributo.getKey() + ": " + atributo.getValue() + ") ");
            }

            System.out.println();
        }
    }
}
