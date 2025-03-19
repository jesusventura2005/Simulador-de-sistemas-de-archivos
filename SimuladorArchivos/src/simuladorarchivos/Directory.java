/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorarchivos;

import DataStructures.SimpleList;

public class Directory {

    private String name;
    private SimpleList files;
    private String fatherDirectory;

    public Directory(String nombre) {
        this.name = nombre;
        this.files = new SimpleList();

    }

    public void agregar(Object file) {

        files.insertLast(file);

    }
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleList getFiles() {
        return files;
    }

    public void setFiles(SimpleList files) {
        this.files = files;
    }

    public String getFatherDirectory() {
        return fatherDirectory;
    }

    public void setFatherDirectory(String fatherDirectory) {
        this.fatherDirectory = fatherDirectory;
    }

 
    
    

}
