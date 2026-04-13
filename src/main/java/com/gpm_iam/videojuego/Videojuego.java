package com.gpm_iam.videojuego;

/**
 * Superclase abstracta que representa un videojuego genérico.
 * Todas las subclases deben indicar su precio y mostrar su información.
 *
 * <p>Contiene atributos comunes como nombre, género, PEGI, consola y etiquetas.</p>
 *
 * @author Gary
 * @version 1.0
 */
public abstract class Videojuego {

    /** Nombre del videojuego. */
    private String nombre;

    /** Género del videojuego (Acción, RPG, Deportes...). */
    private String genero;

    /** Clasificación PEGI del juego (3, 7, 12, 16, 18). */
    private int pegi;

    /** Consola o plataforma principal del juego. */
    private String consola;

    /**
     * Array de etiquetas descriptivas del juego.
     * Ejemplo: {"multijugador", "mundo abierto", "historia"}
     */
    private String[] etiquetas;

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea un videojuego con sus datos básicos.
     *
     * @param nombre   nombre del juego, no puede ser nulo ni vacío
     * @param genero   género del juego, no puede ser nulo ni vacío
     * @param pegi     clasificación PEGI; debe ser 3, 7, 12, 16 o 18
     * @param consola  plataforma principal del juego
     * @param etiquetas array de etiquetas descriptivas (puede ser null)
     * @throws IllegalArgumentException si nombre o género son vacíos, o PEGI no es válido
     */
    public Videojuego(String nombre, String genero, int pegi, String consola, String[] etiquetas) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("El género no puede estar vacío.");
        }
        if (pegi != 3 && pegi != 7 && pegi != 12 && pegi != 16 && pegi != 18) {
            throw new IllegalArgumentException("PEGI debe ser 3, 7, 12, 16 o 18. Valor recibido: " + pegi);
        }

        this.nombre   = nombre;
        this.genero   = genero;
        this.pegi     = pegi;
        this.consola  = consola;
        this.etiquetas = (etiquetas != null) ? etiquetas : new String[0];
    }

    // ─── Métodos abstractos ───────────────────────────────────────────────────

    /**
     * Devuelve el precio del videojuego según su tipo (digital o físico).
     *
     * @return precio en euros
     */
    public abstract double getPrecio();

    /**
     * Devuelve una cadena con la información completa del videojuego.
     * Cada subclase incluye sus atributos propios.
     *
     * @return String con la información formateada del juego
     */
    public abstract String mostrarInfo();

    // ─── Getters y setters ────────────────────────────────────────────────────

    /**
     * Devuelve el nombre del videojuego.
     *
     * @return nombre del juego
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del videojuego.
     *
     * @param nombre nuevo nombre, no puede ser vacío
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    /**
     * Devuelve el género del videojuego.
     *
     * @return género del juego
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Establece el género del videojuego.
     *
     * @param genero nuevo género
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Devuelve la clasificación PEGI del videojuego.
     *
     * @return PEGI (3, 7, 12, 16 o 18)
     */
    public int getPegi() {
        return pegi;
    }

    /**
     * Devuelve la consola o plataforma del videojuego.
     *
     * @return nombre de la consola
     */
    public String getConsola() {
        return consola;
    }

    /**
     * Devuelve el array de etiquetas del videojuego.
     *
     * @return array de etiquetas; puede ser un array vacío pero nunca null
     */
    public String[] getEtiquetas() {
        return etiquetas;
    }

    /**
     * Establece las etiquetas del videojuego.
     *
     * @param etiquetas nuevo array de etiquetas
     */
    public void setEtiquetas(String[] etiquetas) {
        this.etiquetas = (etiquetas != null) ? etiquetas : new String[0];
    }

    /**
     * Devuelve las etiquetas como una cadena separada por comas.
     *
     * @return etiquetas en formato "tag1, tag2, tag3"
     */
    public String getEtiquetasComoTexto() {
        return String.join(", ", etiquetas);
    }

    /**
     * Representación básica del videojuego (nombre y género).
     *
     * @return String en formato "Nombre [Género]"
     */
    @Override
    public String toString() {
        return nombre + " [" + genero + "]";
    }
}