/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simuladorarchivos;

/**
 *
 * @author manch
 */
public class SimuladorArchivos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        StorageDevice sd = new StorageDevice(20);

        sd.asignarBloques(5, "document.pdf");

        sd.imprimir();

        sd.remplazar("", 2);

        sd.imprimir();

        sd.asignarBloques(3, "sexy.exe");

        sd.imprimir();

        sd.removerArchivo("document.pdf");

        sd.imprimir();

    }

}
