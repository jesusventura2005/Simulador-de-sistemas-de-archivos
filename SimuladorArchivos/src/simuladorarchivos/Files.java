package simuladorarchivos;

import DataStructures.SimpleList;

/**
 *
 * @author jesusventura
 */
public class Files {

    private String nombre;
    private SimpleList lista;
    private int tamañoBloques;
    private int tamañoArchivo;
    private String permisos;
    private String modo;

    public Files(String nombre, int tamañoBloques) {
        this.nombre = nombre;
        this.lista = new SimpleList();
        this.tamañoBloques = tamañoBloques;

    }

    public void agregarBloques(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            lista.insertLast(nombre + " " + i);
        }
    }

    /// Aun no se si hacer los metodos aqui o en el interfaz , hablar con el chinox 

    
   
    
    // Getters y setters 
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public SimpleList getLista() {
        return lista;
    }

    public void setLista(SimpleList lista) {
        this.lista = lista;
    }

    public int getTamañoBloques() {
        return tamañoBloques;
    }

    public void setTamañoBloques(int tamañoBloques) {
        this.tamañoBloques = tamañoBloques;
    }

    public int getTamañoArchivo() {
        return tamañoArchivo;
    }

    public void setTamañoArchivo(int tamañoArchivo) {
        this.tamañoArchivo = tamañoArchivo;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

}
