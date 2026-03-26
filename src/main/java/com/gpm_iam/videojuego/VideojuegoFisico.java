package com.gpm_iam.videojuego;

public class VideojuegoFisico extends Videojuego {
    int precioFisico;
    String tiendaFisica;
    boolean segundaMano;

    public VideojuegoFisico(int precioFisico, boolean segundaMano, String tiendaFisica, int PEGI, String consola, String genero, String nombre) {
        super(PEGI, consola, genero, nombre);
        this.precioFisico = precioFisico;
        this.segundaMano = segundaMano;
        this.tiendaFisica = tiendaFisica;
    }

    public int getPrecioFisico() {
        return precioFisico;
    }

    public void setPrecioFisico(int precioFisico) {
        this.precioFisico = precioFisico;
    }

    public String getTiendaFisica() {
        return tiendaFisica;
    }

    public void setTiendaFisica(String tiendaFisica) {
        this.tiendaFisica = tiendaFisica;
    }

    public boolean isSegundaMano() {
        return segundaMano;
    }

    public void setSegundaMano(boolean segundaMano) {
        this.segundaMano = segundaMano;
    }

    
    
}
