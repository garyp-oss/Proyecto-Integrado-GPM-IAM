package com.gpm_iam.videojuego;

public abstract class Persona {
    private String nombre;
    private String dni;

    public Persona(String nombre, String dni){
        this.nombre = nombre;
        this.dni = dni;
    }

    public String getDni() {
        return this.dni;
    }

    public void getPersona(){
        System.out.println("Klk menol soy " + nombre + " tengo ");
    }

}
