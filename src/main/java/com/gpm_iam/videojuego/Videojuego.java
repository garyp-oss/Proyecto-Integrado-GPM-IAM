package com.gpm_iam.videojuego;

import java.util.ArrayList;

public abstract class Videojuego {
    private String nombre;
    private ArrayList<String> catalogo = new ArrayList<>(); 
    private String genero;
    private int PEGI;
    private String consola;
     
    public Videojuego(int PEGI, String consola, String genero, String nombre) {
        this.PEGI = PEGI;
        this.consola = consola;
        this.genero = genero;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<String> getCatalogo() {
        return catalogo;
    }

    public String getGenero() {
        return genero;
    }

    public int getPEGI() {
        return PEGI;
    }

    public String getConsola() {
        return consola;
    }

    
}
