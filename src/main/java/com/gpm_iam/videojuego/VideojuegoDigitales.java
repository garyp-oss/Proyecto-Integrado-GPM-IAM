package com.gpm_iam.videojuego;

public class VideojuegoDigitales extends Videojuego {
    
    int precioDigital;
    String tiendaDigital;

    public VideojuegoDigitales(int PEGI, String consola, String genero, String nombre, int precioDigital,
            String tiendaDigital) {
        super(PEGI, consola, genero, nombre);
        this.precioDigital = precioDigital;
        this.tiendaDigital = tiendaDigital;
    }

    public int getPrecioDigital() {
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
