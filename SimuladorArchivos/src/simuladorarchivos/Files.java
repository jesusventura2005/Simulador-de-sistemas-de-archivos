package simuladorarchivos;

import DataStructures.SimpleList;

/**
 *
 * @author jesusventura
 */
public class Files {

    private String nombre;
    private String fatherDiretory;
    private int tamañoBloques;
<<<<<<< HEAD
    private int tamañoArchivo;
    private String permisos;
    private String modo;
    private int bloqueInicial;
=======
>>>>>>> 8587d22 (cargar WIP 2.0)

    public Files(String nombre, int tamañoBloques , int bloqueInicial) {
        this.nombre = nombre;
        this.tamañoBloques = tamañoBloques;
        this.bloqueInicial = bloqueInicial;

    }

<<<<<<< HEAD
    public void agregarBloques(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            lista.insertLast(nombre + " " + i);
        }
    }
=======
    /// Aun no se si hacer los metodos aqui o en el interfaz , hablar con el chinox 

>>>>>>> 8587d22 (cargar WIP 2.0)
    
    public void encontrarBloque(String nombre){
        
    
    
    }
    
    
    public int getBloqueInicial() {
        return bloqueInicial;
    }

    public void setBloqueInicial(int bloqueInicial) {
        this.bloqueInicial = bloqueInicial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTamañoBloques() {
        return tamañoBloques;
    }

    public void setTamañoBloques(int tamañoBloques) {
        this.tamañoBloques = tamañoBloques;
    }

    public String getFatherDiretory() {
        return fatherDiretory;
    }

    public void setFatherDiretory(String fatherDiretory) {
        this.fatherDiretory = fatherDiretory;
    }

}
