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
    private int tamañoArchivo;
    private String permisos;
    private String modo;
    private int bloqueInicial;

    public Files(String nombre, int tamañoBloques , int bloqueInicial) {
        this.nombre = nombre;
        this.tamañoBloques = tamañoBloques;
        this.bloqueInicial = bloqueInicial;

    }

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
