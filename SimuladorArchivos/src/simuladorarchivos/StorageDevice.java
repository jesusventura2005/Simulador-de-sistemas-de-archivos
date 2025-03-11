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

    public StorageDevice(int tamañoSD) {
        this.tamañoSD = tamañoSD;
        this.bloques = new SimpleList();
        for (int i = 0; i < tamañoSD; i++) {
            bloques.insertLast("");
        }
    }
    
    
    
    public  void imprimir(){
        
        bloques.printList();
    
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
    
public void removerArchivo(String nombreArchivo) {
    int i = 0;
    while (i < bloques.getSize()) {
        if (bloques.getValor(i).equals(nombreArchivo + " " + i)) {
            bloques.replaceBLock("", i); // Reemplaza con bloque libre
        } else {
            i++; // Avanza solo si no se eliminó un elemento
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
