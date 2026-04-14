package com.gpm_iam.videojuego;

/**
 * Interfaz para los videojuegos que pueden exportarse a un formato externo.
 *
 * <p>Define el contrato de exportación. Las clases que implementen esta interfaz
 * deben ser capaces de serializar su información a JSON de forma manual.</p>
 *
 * <p>Esta interfaz se diferencia de {@link Valorable} en que {@code Exportable}
 * trata la representación externa del objeto, no su evaluación.</p>
 *
 * @author Gary, Ivan
 * @version 1.0
 * @see Valorable
 */
public interface Exportable {

    /**
     * Exporta la información del objeto a una cadena en formato JSON.
     *
     * <p>La implementación debe construir el JSON manualmente con {@link StringBuilder},
     * escapando correctamente los caracteres especiales.</p>
     *
     * @return String con el JSON válido del objeto
     */
    String exportarJson();
}