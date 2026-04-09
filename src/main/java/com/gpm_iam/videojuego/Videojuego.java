package com.gpm_iam.videojuego;

public abstract class Videojuego {
    private String nombre;
    private String genero;
     
    public Videojuego(String nombre, String genero) {
        this.nombre = nombre;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {  // ← y este método
        return genero;
    }

 
    
}
