package com.gpm_iam.videojuego;

/**
 * Interfaz para los videojuegos que pueden recibir una valoración numérica.
 *
 * <p>Define el contrato de puntuación. Las clases que implementen esta interfaz
 * deben exponer y permitir modificar una valoración del 0 al 10.</p>
 *
 * <p>Esta interfaz se diferencia de {@link Exportable} en que {@code Valorable}
 * representa la evaluación subjetiva del objeto, no su exportación.</p>
 *
 * @author Gary
 * @version 1.0
 * @see Exportable
 */
public interface Valorable {

    /**
     * Devuelve la valoración actual del videojuego.
     *
     * @return valoración entre 0.0 y 10.0
     */
    double getValoracion();

    /**
     * Establece la valoración del videojuego.
     *
     * @param valoracion nueva valoración; debe estar entre 0 y 10
     * @throws IllegalArgumentException si la valoración está fuera del rango [0, 10]
     */
    void setValoracion(double valoracion);
}