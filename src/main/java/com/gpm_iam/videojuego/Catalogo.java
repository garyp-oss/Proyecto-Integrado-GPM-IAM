package com.gpm_iam.videojuego;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Gestiona la colección principal de videojuegos del catálogo GameVault.
 *
 * <p>Internamente usa dos estructuras:</p>
 * <ul>
 *   <li>{@code catalogo} — {@link ArrayList} polimórfico con todos los juegos.</li>
 *   <li>{@code porGenero} — {@link HashMap} que indexa los juegos por género
 *       para búsquedas rápidas.</li>
 * </ul>
 *
 * @author Gary, Ivan
 * @version 1.0
 */
public class Catalogo {

    /** Lista principal de videojuegos del catálogo. */
    private ArrayList<Videojuego> catalogo = new ArrayList<>();

    /** Índice secundario: clave = género, valor = lista de juegos de ese género. */
    private HashMap<String, List<Videojuego>> porGenero = new HashMap<>();


    // ─── Operaciones CRUD ────────────────────────────────────────────────────

    /**
     * Añade un videojuego al catálogo (ArrayList) y al índice por género (HashMap).
     *
     * @param juego videojuego a añadir; no debe ser null
     */
    public void agregarJuego(Videojuego juego) {
        catalogo.add(juego);
        // computeIfAbsent crea la lista si no existe para ese género
        porGenero.computeIfAbsent(juego.getGenero(), k -> new ArrayList<>()).add(juego);
    }

    /**
     * Devuelve todos los videojuegos del catálogo.
     *
     * @return ArrayList con todos los juegos
     */
    public ArrayList<Videojuego> getCatalogo() {
        return catalogo;
    }

    /**
     * Devuelve la lista de juegos que pertenecen a un género determinado.
     *
     * @param genero género a buscar (sensible a mayúsculas)
     * @return lista de juegos de ese género; lista vacía si no hay ninguno
     */
    public List<Videojuego> obtenerPorGenero(String genero) {
        return porGenero.getOrDefault(genero, new ArrayList<>());
    }

    // ─── Método genérico ─────────────────────────────────────────────────────

    /**
     * Método genérico que muestra por consola solo los juegos del tipo indicado.
     *
     * <p>Usa un bounded type para garantizar en tiempo de compilación que
     * {@code T} es siempre un subtipo de {@link Videojuego}.</p>
     *
     * <p>Ejemplo de uso:</p>
     * <pre>
     *   catalogo.mostrarPorTipo(VideojuegoDigital.class);
     *   catalogo.mostrarPorTipo(VideojuegoFisico.class);
     * </pre>
     *
     * @param <T>   tipo concreto de videojuego a filtrar; debe extender {@link Videojuego}
     * @param clase clase del tipo a mostrar (por ejemplo {@code VideojuegoDigital.class})
     */
    public <T extends Videojuego> void mostrarPorTipo(Class<T> clase) {
        System.out.println("\n── Juegos de tipo: " + clase.getSimpleName() + " ──");
        catalogo.stream()
                .filter(j -> clase.isInstance(j))
                .map(clase::cast)
                .forEach(j -> System.out.println("  " + j.mostrarInfo()));
    }
}