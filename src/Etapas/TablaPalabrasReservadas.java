package Etapas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TablaPalabrasReservadas {
    public static final int PALABRA_NO_RESERVADA = -1;
    private static final String ARCHIVO_PALABRAS_RESERVADAS = "src\\PalabrasReservadas.txt";
    private static Map<String, Integer> palabras_reservadas;

    public TablaPalabrasReservadas(){
        Map<String, Integer> map = new HashMap<>();

        try {
            File archivo = new File(ARCHIVO_PALABRAS_RESERVADAS);
            Scanner scanner = new Scanner(archivo);

            while (scanner.hasNext()) {
                String palabra_reservada = scanner.next();
                int identificador = scanner.nextInt();
                map.put(palabra_reservada, identificador);
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {
            System.out.println("No se pudo leer el archivo " + ARCHIVO_PALABRAS_RESERVADAS);
            excepcion.printStackTrace();
        }
        palabras_reservadas = map;
    }

    public static int obtenerIdentificador(String palabra_reservada) {
        return palabras_reservadas.getOrDefault(palabra_reservada, PALABRA_NO_RESERVADA);
    }
}
