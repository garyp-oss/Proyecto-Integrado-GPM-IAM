package com.gpm_iam.videojuego;

public class Gaymer extends Persona{
    int edad;

    public Gaymer(int edad, String nombre, String dni) {
        super(nombre, dni);
        this.edad = edad;
    }

    @Override
    public String getDni() {
        return super.getDni();
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public void getPersona() {
        super.getPersona();
    }
    
}
