package com.gpm_iam.videojuego;

public class VideojuegoDigitales extends Videojuego {
    
    double precioDigital;
    String tiendaDigital;

    public VideojuegoDigitales(double precioDigital, String tiendaDigital, String nombre, String genero) {
        super(nombre, genero);
        this.precioDigital = precioDigital;
        this.tiendaDigital = tiendaDigital;
    }

    public double getPrecioDigital() {
        return precioDigital;
    }

    public void setPrecioDigital(int precioDigital) {
        this.precioDigital = precioDigital;
    }

    public String getTiendaDigital() {
        return tiendaDigital;
    }

    public void setTiendaDigital(String tiendaDigital) {
        this.tiendaDigital = tiendaDigital;
    }

}
