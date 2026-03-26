package com.gpm_iam.videojuego;

public class Tecnico extends Persona {

    boolean follador;

    public Tecnico(String nombre, String dni) {
        super(nombre, dni);
        this.follador = follador;
    }

    public boolean isFollador(){
        return follador;
    }
    
}
