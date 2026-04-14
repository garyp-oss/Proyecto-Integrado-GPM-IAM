package com.gpm_iam.videojuego;

/**
 * Representa un videojuego en formato digital, disponible en una tienda online.
 *
 * <p>Extiende {@link Videojuego} e implementa {@link Exportable}, ya que los
 * juegos digitales pueden exportarse con su enlace de descarga.</p>
 *
 * @author Gary, Ivan
 * @version 1.0
 * @see Videojuego
 * @see Exportable
 */
public class VideojuegoDigitales extends Videojuego implements Exportable {

    /** Precio en euros de la versión digital. */
    private double precioDigital;

    /** Nombre de la tienda digital donde está disponible. */
    private String tiendaDigital;

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea un videojuego digital con todos sus datos.
     *
     * @param nombre       nombre del juego
     * @param genero       género del juego
     * @param pegi         clasificación PEGI (3, 7, 12, 16 o 18)
     * @param consola      plataforma principal
     * @param etiquetas    array de etiquetas descriptivas
     * @param precioDigital precio en euros de la versión digital; debe ser mayor que 0
     * @param tiendaDigital nombre de la tienda digital (Steam, Epic Store...)
     * @throws IllegalArgumentException si el precio es negativo o cero
     */
    public VideojuegoDigitales(String nombre, String genero, int pegi, String consola,
                             String[] etiquetas, double precioDigital, String tiendaDigital) {
        super(nombre, genero, pegi, consola, etiquetas);

        if (precioDigital <= 0) {
            throw new IllegalArgumentException("El precio digital debe ser mayor que 0.");
        }

        this.precioDigital  = precioDigital;
        this.tiendaDigital  = tiendaDigital;
    }

    // ─── Métodos abstractos implementados ────────────────────────────────────

    /**
     * {@inheritDoc}
     * Devuelve el precio de la versión digital.
     *
     * @return precio digital en euros
     */
    @Override
    public double getPrecio() {
        return precioDigital;
    }

    /**
     * {@inheritDoc}
     * Muestra la información completa del juego digital.
     *
     * @return String formateado con todos los datos del juego digital
     */
    @Override
    public String mostrarInfo() {
        return String.format(
            "[DIGITAL] %-30s | Género: %-12s | PEGI: %2d | Consola: %-10s | Precio: %6.2f€ | Tienda: %s",
            getNombre(), getGenero(), getPegi(), getConsola(), precioDigital, tiendaDigital
        );
    }

    /**
     * {@inheritDoc}
     * Exporta el juego digital a formato JSON.
     *
     * @return String con el JSON del juego digital
     */
    @Override
    public String exportarJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("  {\n");
        sb.append("    \"tipo\": \"digital\",\n");
        sb.append("    \"nombre\": \"").append(getNombre().replace("\"", "\\\"")).append("\",\n");
        sb.append("    \"genero\": \"").append(getGenero()).append("\",\n");
        sb.append("    \"pegi\": ").append(getPegi()).append(",\n");
        sb.append("    \"consola\": \"").append(getConsola()).append("\",\n");
        sb.append("    \"precio\": ").append(String.format("%.2f", precioDigital)).append(",\n");
        sb.append("    \"tienda\": \"").append(tiendaDigital).append("\"\n");
        sb.append("  }");
        return sb.toString();
    }

    // ─── Getters y setters ────────────────────────────────────────────────────

    /**
     * Devuelve el precio digital del juego.
     *
     * @return precio en euros
     */
    public double getPrecioDigital() {
        return precioDigital;
    }

    /**
     * Establece el precio digital del juego.
     *
     * @param precioDigital nuevo precio; debe ser mayor que 0
     * @throws IllegalArgumentException si el precio es negativo o cero
     */
    public void setPrecioDigital(double precioDigital) {
        if (precioDigital <= 0) {
            throw new IllegalArgumentException("El precio digital debe ser mayor que 0.");
        }
        this.precioDigital = precioDigital;
    }

    /**
     * Devuelve el nombre de la tienda digital.
     *
     * @return nombre de la tienda
     */
    public String getTiendaDigital() {
        return tiendaDigital;
    }

    /**
     * Establece el nombre de la tienda digital.
     *
     * @param tiendaDigital nuevo nombre de la tienda
     */
    public void setTiendaDigital(String tiendaDigital) {
        this.tiendaDigital = tiendaDigital;
    }
}