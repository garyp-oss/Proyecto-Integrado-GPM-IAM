package com.gpm_iam.videojuego;

/**
 * Representa un videojuego en formato físico (cartucho o disco).
 *
 * <p>Extiende {@link Videojuego} e implementa {@link Valorable}, ya que los
 * juegos físicos pueden tener una valoración de su estado y puntuación.</p>
 *
 * @author Gary, Ivan
 * @version 1.0
 * @see Videojuego
 * @see Valorable
 */
public class VideojuegoFisico extends Videojuego implements Valorable {

    /** Precio en euros de la versión física. */
    private double precioFisico;

    /** Nombre de la tienda física donde se compró. */
    private String tiendaFisica;

    /** Indica si el juego es de segunda mano. */
    private boolean segundaMano;

    /** Valoración del juego entre 0 y 10. */
    private double valoracion;

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea un videojuego físico con todos sus datos.
     *
     * @param nombre       nombre del juego
     * @param genero       género del juego
     * @param pegi         clasificación PEGI (3, 7, 12, 16 o 18)
     * @param consola      plataforma principal
     * @param etiquetas    array de etiquetas descriptivas
     * @param precioFisico precio en euros de la versión física; debe ser mayor que 0
     * @param tiendaFisica nombre de la tienda física
     * @param segundaMano  true si es un juego de segunda mano
     * @param valoracion   valoración del 0 al 10
     * @throws IllegalArgumentException si el precio es negativo o la valoración está fuera de rango
     */
    public VideojuegoFisico(String nombre, String genero, int pegi, String consola,
                            String[] etiquetas, double precioFisico, String tiendaFisica,
                            boolean segundaMano, double valoracion) {
        super(nombre, genero, pegi, consola, etiquetas);

        if (precioFisico <= 0) {
            throw new IllegalArgumentException("El precio físico debe ser mayor que 0.");
        }
        if (valoracion < 0 || valoracion > 10) {
            throw new IllegalArgumentException("La valoración debe estar entre 0 y 10.");
        }

        this.precioFisico = precioFisico;
        this.tiendaFisica = tiendaFisica;
        this.segundaMano  = segundaMano;
        this.valoracion   = valoracion;
    }

    // ─── Métodos abstractos implementados ────────────────────────────────────

    /**
     * {@inheritDoc}
     * Devuelve el precio de la versión física.
     *
     * @return precio físico en euros
     */
    @Override
    public double getPrecio() {
        return precioFisico;
    }

    /**
     * {@inheritDoc}
     * Muestra la información completa del juego físico.
     *
     * @return String formateado con todos los datos del juego físico
     */
    @Override
    public String mostrarInfo() {
        return String.format(
            "[FÍSICO]  %-30s | Género: %-12s | PEGI: %2d | Consola: %-10s | Precio: %6.2f€ | Tienda: %-15s | 2ª mano: %-5s | Valoración: %.1f/10",
            getNombre(), getGenero(), getPegi(), getConsola(),
            precioFisico, tiendaFisica,
            (segundaMano ? "Sí" : "No"), valoracion
        );
    }

    /**
     * {@inheritDoc}
     * Devuelve la valoración del juego.
     *
     * @return valoración entre 0.0 y 10.0
     */
    @Override
    public double getValoracion() {
        return valoracion;
    }

    /**
     * {@inheritDoc}
     * Establece la valoración del juego.
     *
     * @param valoracion nueva valoración entre 0 y 10
     * @throws IllegalArgumentException si la valoración está fuera del rango permitido
     */
    @Override
    public void setValoracion(double valoracion) {
        if (valoracion < 0 || valoracion > 10) {
            throw new IllegalArgumentException("La valoración debe estar entre 0 y 10.");
        }
        this.valoracion = valoracion;
    }

    // ─── Getters y setters ────────────────────────────────────────────────────

    /**
     * Devuelve el precio físico del juego.
     *
     * @return precio en euros
     */
    public double getPrecioFisico() {
        return precioFisico;
    }

    /**
     * Establece el precio físico del juego.
     *
     * @param precioFisico nuevo precio; debe ser mayor que 0
     * @throws IllegalArgumentException si el precio es negativo o cero
     */
    public void setPrecioFisico(double precioFisico) {
        if (precioFisico <= 0) {
            throw new IllegalArgumentException("El precio físico debe ser mayor que 0.");
        }
        this.precioFisico = precioFisico;
    }

    /**
     * Devuelve el nombre de la tienda física.
     *
     * @return nombre de la tienda
     */
    public String getTiendaFisica() {
        return tiendaFisica;
    }

    /**
     * Establece el nombre de la tienda física.
     *
     * @param tiendaFisica nuevo nombre de la tienda
     */
    public void setTiendaFisica(String tiendaFisica) {
        this.tiendaFisica = tiendaFisica;
    }

    /**
     * Indica si el juego es de segunda mano.
     *
     * @return true si es de segunda mano, false si es nuevo
     */
    public boolean isSegundaMano() {
        return segundaMano;
    }

    /**
     * Establece si el juego es de segunda mano.
     *
     * @param segundaMano true para segunda mano, false para nuevo
     */
    public void setSegundaMano(boolean segundaMano) {
        this.segundaMano = segundaMano;
    }
}