/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorarchivos;

import DataStructures.SimpleList;

public class Directory {

    private String name;
    private SimpleList files;

    public Directory(String nombre, SimpleList archivos) {
        this.name = name;
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

}
