/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorarchivos;

import DataStructures.SimpleList;

/**
 *
 * @author jesusventura
 */
public class StorageDevice {

    private SimpleList bloques;
    private int tamañoSD;

    public StorageDevice(SimpleList bloques, int tamañoSD) {
        this.bloques = bloques;
        this.tamañoSD = tamañoSD;
    }
    

    public StorageDevice(int tamañoSD) {
        this.tamañoSD = tamañoSD;
        this.bloques = new SimpleList();
        for (int i = 0; i < tamañoSD; i++) {
                
            bloques.insertLast("");
        }
    }
    
    public int obtenerPrimerbloque(Object elemento){
            
        return bloques.primerBloque(elemento, tamañoSD);
        
    }
    
    
    public  String imprimir(){
        
       return bloques.printList();
    
    }
    
    public void remplazar( String nombreArchivo ,int index){
   
        bloques.replaceBLock(nombreArchivo, index);
        
    }
    
    
    public int asignarBloques(int cantidad, String nombreArchivo) {
    int bloquesAsignados = 0;
    int primerBloque = -1; 

    for (int i = 0; i <= bloques.getSize() - cantidad; i++) {
        
        boolean libres = true;
        for (int j = 0; j < cantidad; j++) {
            if (!bloques.getValor(i + j).equals("")) {
                libres = false;
                break; 
            }
        }


        if (libres) {
            primerBloque = i;
            for (int j = 0; j < cantidad; j++) {
                bloques.replaceBLock(nombreArchivo + " " + j , i + j); 
                bloquesAsignados++;
            }
            break; 
        }
    }


    if (bloquesAsignados < cantidad) {
        if (bloquesAsignados > 0) {
            for (int j = 0; j < bloquesAsignados; j++) {
                bloques.replaceBLock("", primerBloque + j); 
            }
        }
        return -1; 
    }

    return primerBloque; 
}
    
public void removerArchivo(String nombreArchivo, int primerBloque) {
    int i = 0;
    int j = 0;
    while (i < bloques.getSize()) {
                    
        if (bloques.getValor(i).equals(nombreArchivo + " " + j )) {
            bloques.replaceBLock("", i); // Reemplaza con bloque libre
            j ++;
        } else {
            i++; // Avanza solo si no se eliminó un elemento
        }
    }
}


    public void actualizarNombreArchivoEnBloques(String nombreActual, String nuevoNombre) {
        for (int i = 0; i < bloques.getSize(); i++) {
            String bloque = (String) bloques.getValor(i);
            if (bloque.startsWith(nombreActual + " ")) { // Verifica si el bloque pertenece al archivo
                String nuevoBloque = bloque.replaceFirst(nombreActual, nuevoNombre);
                bloques.replaceBLock(nuevoBloque, i);
            }
        }
    }
    

    
    
    
    
    
    // Getters y setters 



    public SimpleList getBloques() {
        return bloques;
    }

    public void setBloques(SimpleList bloques) {
        this.bloques = bloques;
    }

    public int getTamañoSD() {
        return tamañoSD;
    }

    public void setTamañoSD(int tamañoSD) {
        this.tamañoSD = tamañoSD;
    }

}
