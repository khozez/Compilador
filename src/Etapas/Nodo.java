package Etapas;

public class Nodo {
    private String nombre;
    private  Nodo izq;
    private  Nodo der;
    private  boolean es_hoja;
    private String tipo = "null";

    public Nodo(String nom, Nodo izq, Nodo der){
        this.nombre = nom;
        this.der = der;
        this.izq = izq;
        this.es_hoja = false;
    }

    public Nodo(String nom, Nodo izq, Nodo der, String tipo){
        this.nombre = nom;
        this.der = der;
        this.izq = izq;
        this.es_hoja = false;
        this.tipo = tipo;
    }

    public Nodo(String nom){
        this.nombre = nom;
        this.der = null;
        this.izq = null;
        this.es_hoja = true;
    }

    public Nodo(String nom, String tipo){
        this.nombre = nom;
        this.der = null;
        this.izq = null;
        this.es_hoja = true;
        this.tipo = tipo;
    }

    public Nodo getDer() {
        return der;
    }

    public Nodo getIzq() {
        return izq;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esHoja(){
        return es_hoja;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setDer(Nodo der) {
        this.der = der;
    }

    public void setEs_hoja(boolean es_hoja) {
        this.es_hoja = es_hoja;
    }

    public void setIzq(Nodo izq) {
        this.izq = izq;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {

        if (!es_hoja)
            return
                    nombre + " " + "Tipo:" + tipo + '\n' +
                            "izq= "   + izq + '\n' +
                            "der= "  + der + '\n' +
                            "hoja=" + false + " " +
                            nombre + " }";
        else
            return "[hoja " + nombre + " | Tipo " + tipo + "]";
    }
}
