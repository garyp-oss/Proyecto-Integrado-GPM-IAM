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
 * @author Gary
 * @version 1.0
 */
public class Catalogo {

    /** Lista principal de videojuegos del catálogo. */
    private ArrayList<Videojuego> catalogo = new ArrayList<>();

    /** Índice secundario: clave = género, valor = lista de juegos de ese género. */
    private HashMap<String, List<Videojuego>> porGenero = new HashMap<>();

    // ─── Carga de datos de ejemplo ────────────────────────────────────────────

    /**
     * Rellena el catálogo con juegos de ejemplo (digitales y físicos).
     * Útil para probar la jerarquía en el Main.
     */
    public void catalogoCreate() {
        // Digitales
        agregarJuego(new VideojuegoDigitales("Minecraft",                  "Sandbox",   7,  "PC",    new String[]{"multijugador", "construcción"},  19.99, "Microsoft Store"));
        agregarJuego(new VideojuegoDigitales("Final Fantasy VII Remake",   "RPG",       16, "PC",    new String[]{"historia", "combate"},            39.99, "Steam"));
        agregarJuego(new VideojuegoDigitales("Pokémon Scarlet",            "RPG",       7,  "Switch",new String[]{"colección", "aventura"},          34.99, "Emulador"));
        agregarJuego(new VideojuegoDigitales("Garry's Mod",                "Sandbox",   12, "PC",    new String[]{"multijugador", "mods"},           14.99, "Steam"));
        agregarJuego(new VideojuegoDigitales("The Last of Us Remastered",  "Acción",    18, "PS4",   new String[]{"historia", "survival"},           29.99, "Emulador"));
        agregarJuego(new VideojuegoDigitales("Red Dead Redemption 2",      "Aventura",  18, "PC",    new String[]{"mundo abierto", "historia"},      59.99, "Steam"));
        agregarJuego(new VideojuegoDigitales("Stardew Valley",             "Simulación",3,  "PC",    new String[]{"relajante", "granja"},             9.99, "Steam"));
        agregarJuego(new VideojuegoDigitales("GTA V",                      "Acción",    18, "PC",    new String[]{"mundo abierto", "multijugador"},  19.99, "Epic Store"));
        agregarJuego(new VideojuegoDigitales("Terraria",                   "Sandbox",   7,  "PC",    new String[]{"construcción", "aventura"},        7.99, "Epic Store"));
        agregarJuego(new VideojuegoDigitales("Uncharted 4",                "Aventura",  16, "PS4",   new String[]{"historia", "acción"},            19.99, "Microsoft Store"));

        // Físicos
        agregarJuego(new VideojuegoFisico("The Legend of Zelda: BOTW",  "Aventura",  12, "Switch",new String[]{"mundo abierto", "aventura"},      49.99, "GAME",     false, 9.8));
        agregarJuego(new VideojuegoFisico("FIFA 24",                     "Deportes",  3,  "PS5",   new String[]{"fútbol", "multijugador"},         39.99, "MediaMarkt",true, 6.5));
        agregarJuego(new VideojuegoFisico("God of War Ragnarök",         "Acción",    18, "PS5",   new String[]{"historia", "mitología"},          69.99, "GAME",     false, 9.5));
    }

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