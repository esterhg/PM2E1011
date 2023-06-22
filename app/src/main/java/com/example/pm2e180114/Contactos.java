package com.example.pm2e180114;

import android.graphics.Bitmap;

public class Contactos {
    private String nombre, pais, notas;
    private String telefono;

    private Bitmap img;
    private int id;


    public Contactos(String nombre, String pais, String telefono , Bitmap img, String notas, int id) {
        this.nombre = nombre;
        this.pais = pais;
        this.notas = notas;
        this.telefono = telefono;
        this.img = img;
        this.id = id;
    }

    public Contactos() {
        this.nombre = nombre;
        this.pais = pais;
        this.notas = notas;
        this.telefono = telefono;
        this.img = img;
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public String getNotas() {
        return notas;
    }

    public String getTelefono() {
        return telefono;
    }

    public Bitmap getImg() {
        return img;
    }

    public int getId() {
        return id;
    }

    public void setImagen(Bitmap imageBitmap) {

    }
}


