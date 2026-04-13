package com.gpm_iam.videojuego;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * Menú interactivo de consola para gestionar el catálogo GameVault.
 *
 * <p>Requisitos del Hito 2 que cubre esta clase:</p>
 * <ul>
 *   <li>Menú con {@code printf} y columnas alineadas.</li>
 *   <li>Validación de campos con expresiones regulares.</li>
 *   <li>Búsqueda con regex (nombre parcial, insensible a mayúsculas).</li>
 *   <li>Al menos 4 operaciones con Stream API: filter, sorted, groupingBy, reducción.</li>
 *   <li>Iterator explícito en la eliminación.</li>
 *   <li>Lectura/escritura de CSV y exportación a JSON mediante {@link GestorFicheros}.</li>
 * </ul>
 *
 * @author Gary
 * @version 1.0
 */
public class MenuConsola {

    // ─── Regex compiladas y reutilizadas ──────────────────────────────────────

    /** Valida que el nombre tenga entre 1 y 60 caracteres no vacíos. */
    private static final Pattern REGEX_NOMBRE =
        Pattern.compile("^.{1,60}$");

    /** Valida géneros: solo letras, espacios y guiones, entre 2 y 30 chars. */
    private static final Pattern REGEX_GENERO =
        Pattern.compile("^[\\p{L} \\-]{2,30}$");

    /** Valida precio: número positivo con hasta 2 decimales (ej: 19.99 o 5). */
    private static final Pattern REGEX_PRECIO =
        Pattern.compile("^\\d{1,5}(\\.\\d{1,2})?$");

    /** Valida PEGI: exactamente 3, 7, 12, 16 o 18. */
    private static final Pattern REGEX_PEGI =
        Pattern.compile("^(3|7|12|16|18)$");

    /** Valida valoración: número de 0 a 10 con hasta 1 decimal (ej: 9.5 o 10). */
    private static final Pattern REGEX_VALORACION =
        Pattern.compile("^(10(\\.0)?|[0-9](\\.\\d)?)$");

    // ─── Dependencias ─────────────────────────────────────────────────────────

    private final Catalogo       catalogo;
    private final GestorFicheros gestor;
    private final Scanner        sc;

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea el menú cargando el catálogo desde el CSV si existe.
     *
     * @param catalogo  instancia del catálogo a gestionar
     * @param gestor    gestor de ficheros para carga/guardado
     */
    public MenuConsola(Catalogo catalogo, GestorFicheros gestor) {
        this.catalogo = catalogo;
        this.gestor   = gestor;
        this.sc       = new Scanner(System.in);
    }

    // ─── Bucle principal ──────────────────────────────────────────────────────

    /**
     * Arranca el menú principal y gestiona el bucle de opciones.
     */
    public void iniciar() {
        // Intentamos cargar el CSV al arrancar
        try {
            List<Videojuego> cargados = gestor.cargar();
            cargados.forEach(catalogo::agregarJuego);
        } catch (IOException e) {
            System.out.println("  ⚠ Error al cargar CSV: " + e.getMessage());
        }

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            switch (opcion) {
                case "1": listarTodos();        break;
                case "2": anyadirJuego();       break;
                case "3": buscarPorNombre();    break;
                case "4": listarPorGenero();    break;
                case "5": estadisticasStreams(); break;
                case "6": eliminarJuego();      break;
                case "7": buscarPorId();        break;
                case "8": guardarCSV();         break;
                case "9": exportarJSON();       break;
                case "0": salir = confirmarSalida(); break;
                default:  System.out.println("  ⚠ Opción no válida. Introduce un número del 0 al 9.");
            }
        }
        sc.close();
    }

    // ─── Menú principal ───────────────────────────────────────────────────────

    /**
     * Imprime el menú principal con formato tabular usando {@code printf}.
     */
    private void mostrarMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          GAMEVAULT  —  Menú          ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf( "║  %-3s %-33s║%n", "1.", "Listar todos los juegos");
        System.out.printf( "║  %-3s %-33s║%n", "2.", "Añadir juego");
        System.out.printf( "║  %-3s %-33s║%n", "3.", "Buscar por nombre");
        System.out.printf( "║  %-3s %-33s║%n", "4.", "Filtrar por género");
        System.out.printf( "║  %-3s %-33s║%n", "5.", "Estadísticas (streams)");
        System.out.printf( "║  %-3s %-33s║%n", "6.", "Eliminar juego");
        System.out.printf( "║  %-3s %-33s║%n", "7.", "Buscar por ID");
        System.out.printf( "║  %-3s %-33s║%n", "8.", "Guardar catálogo (CSV)");
        System.out.printf( "║  %-3s %-33s║%n", "9.", "Exportar a JSON");
        System.out.printf( "║  %-3s %-33s║%n", "0.", "Salir");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Opción: ");
    }

    // ─── Opción 1: Listar todos ───────────────────────────────────────────────

    /**
     * Lista todos los juegos del catálogo en formato tabla con {@code printf}.
     */
    private void listarTodos() {
        List<Videojuego> lista = catalogo.getCatalogo();
        if (lista.isEmpty()) {
            System.out.println("  ℹ El catálogo está vacío.");
            return;
        }
        System.out.println();
        imprimirCabecera();
        int id = 1;
        for (Videojuego j : lista) {
            imprimirFila(id++, j);
        }
        imprimirSeparador();
        System.out.printf("  Total: %d juego(s)%n", lista.size());
    }

    // ─── Opción 2: Añadir juego ───────────────────────────────────────────────

    /**
     * Guía al usuario para añadir un juego nuevo con validación de campos por regex.
     */
    private void anyadirJuego() {
        System.out.println("\n── Añadir juego ─────────────────────────────────────────────");

        // Tipo
        System.out.print("  Tipo (1=Digital / 2=Físico): ");
        String tipo = sc.nextLine().trim();
        if (!tipo.equals("1") && !tipo.equals("2")) {
            System.out.println("  ⚠ Tipo no válido.");
            return;
        }

        // Campos comunes
        String nombre  = pedirCampo("Nombre del juego", REGEX_NOMBRE);
        String genero  = pedirCampo("Género (ej: RPG, Acción)", REGEX_GENERO);
        String pegiStr = pedirCampo("PEGI (3/7/12/16/18)", REGEX_PEGI);
        String consola = pedirCampoLibre("Consola/Plataforma");

        // Etiquetas (campo libre, sin validación estricta)
        System.out.print("  Etiquetas separadas por coma (o Enter para ninguna): ");
        String etiqInput = sc.nextLine().trim();
        String[] etiquetas = etiqInput.isEmpty() ? new String[0] : etiqInput.split(",");
        for (int i = 0; i < etiquetas.length; i++) etiquetas[i] = etiquetas[i].trim();

        String precioStr = pedirCampo("Precio (ej: 19.99)", REGEX_PRECIO);
        double precio    = Double.parseDouble(precioStr);
        int    pegi      = Integer.parseInt(pegiStr);

        Videojuego nuevo;
        if (tipo.equals("1")) {
            // Digital
            String tienda = pedirCampoLibre("Tienda (Steam, Epic Store...)");
            nuevo = new VideojuegoDigitales(nombre, genero, pegi, consola, etiquetas, precio, tienda);
        } else {
            // Físico
            String tienda      = pedirCampoLibre("Tienda física");
            String smStr       = pedirCampo("¿Segunda mano? (true/false)", Pattern.compile("^(true|false)$"));
            String valorStr    = pedirCampo("Valoración (0.0 - 10.0)", REGEX_VALORACION);
            boolean sm         = Boolean.parseBoolean(smStr);
            double  valoracion = Double.parseDouble(valorStr);
            nuevo = new VideojuegoFisico(nombre, genero, pegi, consola, etiquetas, precio, tienda, sm, valoracion);
        }

        catalogo.agregarJuego(nuevo);
        System.out.println("  ✓ Juego añadido: " + nuevo.getNombre());
    }

    // ─── Opción 3: Buscar por nombre (regex) ─────────────────────────────────

    /**
     * Busca juegos cuyo nombre coincida con un patrón regex introducido por el usuario.
     * Usa {@code CASE_INSENSITIVE} para ignorar mayúsculas.
     */
    private void buscarPorNombre() {
        System.out.print("\n  Introduce nombre o patrón a buscar: ");
        String patron = sc.nextLine().trim();

        Pattern p;
        try {
            // Búsqueda con regex, CASE_INSENSITIVE
            p = Pattern.compile(patron, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            // Si el usuario no sabe regex, tratamos su texto como literal
            p = Pattern.compile(Pattern.quote(patron), Pattern.CASE_INSENSITIVE);
        }

        final Pattern patternFinal = p;

        // Stream con filter usando regex
        List<Videojuego> resultado = catalogo.getCatalogo().stream()
            .filter(j -> patternFinal.matcher(j.getNombre()).find())
            .collect(Collectors.toList());

        if (resultado.isEmpty()) {
            System.out.println("  ℹ No se encontraron juegos con ese patrón.");
        } else {
            System.out.println();
            imprimirCabecera();
            int id = 1;
            for (Videojuego j : resultado) imprimirFila(id++, j);
            imprimirSeparador();
        }
    }

    // ─── Opción 4: Filtrar por género ─────────────────────────────────────────

    /**
     * Muestra los juegos de un género concreto usando el HashMap del catálogo.
     */
    private void listarPorGenero() {
        System.out.print("\n  Introduce el género: ");
        String genero = sc.nextLine().trim();

        List<Videojuego> resultado = catalogo.obtenerPorGenero(genero);
        if (resultado.isEmpty()) {
            System.out.println("  ℹ No hay juegos del género '" + genero + "'.");
        } else {
            System.out.println();
            imprimirCabecera();
            int id = 1;
            for (Videojuego j : resultado) imprimirFila(id++, j);
            imprimirSeparador();
        }
    }

    // ─── Opción 5: Estadísticas con Streams ──────────────────────────────────

    /**
     * Muestra estadísticas del catálogo usando al menos 4 operaciones Stream distintas:
     * filter, sorted, groupingBy y una reducción (average/count).
     */
    private void estadisticasStreams() {
        List<Videojuego> lista = catalogo.getCatalogo();
        if (lista.isEmpty()) {
            System.out.println("  ℹ El catálogo está vacío.");
            return;
        }

        System.out.println("\n── Estadísticas del catálogo ────────────────────────────────");

        // 1. filter — solo juegos digitales
        long totalDigitales = lista.stream()
            .filter(j -> j instanceof VideojuegoDigitales)
            .count();

        // 2. filter — solo juegos físicos
        long totalFisicos = lista.stream()
            .filter(j -> j instanceof VideojuegoFisico)
            .count();

        System.out.printf("  %-28s %d%n", "Total juegos digitales:", totalDigitales);
        System.out.printf("  %-28s %d%n", "Total juegos físicos:",   totalFisicos);

        // 3. reducción — precio medio de todos
        OptionalDouble precioMedio = lista.stream()
            .mapToDouble(Videojuego::getPrecio)
            .average();
        precioMedio.ifPresent(p ->
            System.out.printf("  %-28s %.2f€%n", "Precio medio:", p));

        // 4. reducción — precio máximo y mínimo
        lista.stream().mapToDouble(Videojuego::getPrecio).max()
            .ifPresent(p -> System.out.printf("  %-28s %.2f€%n", "Precio más alto:", p));
        lista.stream().mapToDouble(Videojuego::getPrecio).min()
            .ifPresent(p -> System.out.printf("  %-28s %.2f€%n", "Precio más bajo:", p));

        // 5. sorted — top 3 más caros
        System.out.println("\n  Top 3 juegos más caros:");
        lista.stream()
            .sorted(Comparator.comparingDouble(Videojuego::getPrecio).reversed())
            .limit(3)
            .forEach(j -> System.out.printf("    %-35s %.2f€%n", j.getNombre(), j.getPrecio()));

        // 6. groupingBy — cantidad de juegos por género
        System.out.println("\n  Juegos por género:");
        lista.stream()
            .collect(Collectors.groupingBy(Videojuego::getGenero, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(e -> System.out.printf("    %-20s %d juego(s)%n", e.getKey() + ":", e.getValue()));

        // 7. filter + media — valoración media de juegos físicos
        OptionalDouble valorMedia = lista.stream()
            .filter(j -> j instanceof VideojuegoFisico)
            .mapToDouble(j -> ((VideojuegoFisico) j).getValoracion())
            .average();
        valorMedia.ifPresent(v ->
            System.out.printf("%n  %-28s %.1f/10%n", "Valoración media (físicos):", v));
    }

    // ─── Opción 6: Eliminar juego (Iterator explícito) ────────────────────────

    /**
     * Elimina un juego del catálogo por su nombre exacto.
     * Usa un {@link Iterator} explícito para eliminar de forma segura durante la iteración.
     */
    private void eliminarJuego() {
        System.out.print("\n  Nombre exacto del juego a eliminar: ");
        String nombre = sc.nextLine().trim();

        // Iterator explícito — requerido por la rúbrica
        Iterator<Videojuego> it = catalogo.getCatalogo().iterator();
        boolean eliminado = false;

        while (it.hasNext()) {
            Videojuego j = it.next();
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                it.remove();
                eliminado = true;
                System.out.println("  ✓ Juego eliminado: " + j.getNombre());
                break;
            }
        }

        if (!eliminado) {
            System.out.println("  ℹ No se encontró ningún juego con ese nombre.");
        }
    }

    // ─── Opción 7: Buscar por ID (RandomAccessFile) ───────────────────────────

    /**
     * Busca un juego por su ID (número de línea en el CSV) usando {@link GestorFicheros#buscarPorId(int)}.
     */
    private void buscarPorId() {
        System.out.print("\n  Introduce el ID (número de línea en el CSV): ");
        String input = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(input);
            Videojuego j = gestor.buscarPorId(id);
            if (j == null) {
                System.out.println("  ℹ No se encontró ningún juego con ID " + id);
            } else {
                System.out.println("  " + j.mostrarInfo());
            }
        } catch (NumberFormatException e) {
            System.out.println("  ⚠ ID no válido, introduce un número entero.");
        } catch (IOException e) {
            System.out.println("  ⚠ Error al leer el fichero: " + e.getMessage());
        }
    }

    // ─── Opción 8: Guardar CSV ────────────────────────────────────────────────

    /**
     * Guarda el catálogo actual en el fichero CSV.
     */
    private void guardarCSV() {
        try {
            gestor.guardar(catalogo.getCatalogo());
        } catch (IOException e) {
            System.out.println("  ⚠ Error al guardar: " + e.getMessage());
        }
    }

    // ─── Opción 9: Exportar JSON ──────────────────────────────────────────────

    /**
     * Exporta el catálogo a JSON manualmente con {@link GestorFicheros#exportarJson(List)}.
     */
    private void exportarJSON() {
        try {
            gestor.exportarJson(catalogo.getCatalogo());
        } catch (IOException e) {
            System.out.println("  ⚠ Error al exportar JSON: " + e.getMessage());
        }
    }

    // ─── Opción 0: Salir ──────────────────────────────────────────────────────

    /**
     * Pregunta si guardar antes de salir.
     *
     * @return {@code true} si el usuario confirma la salida
     */
    private boolean confirmarSalida() {
        System.out.print("\n  ¿Guardar el catálogo antes de salir? (s/n): ");
        String resp = sc.nextLine().trim().toLowerCase();
        if (resp.equals("s") || resp.equals("si") || resp.equals("sí")) {
            guardarCSV();
        }
        System.out.println("  ¡Hasta luego!");
        return true;
    }

    // ─── Helpers de formato ───────────────────────────────────────────────────

    /** Imprime la cabecera de la tabla con {@code printf}. */
    private void imprimirCabecera() {
        imprimirSeparador();
        System.out.printf("  %-4s %-32s %-14s %-6s %-10s %-10s %-6s%n",
            "ID", "Nombre", "Género", "PEGI", "Consola", "Precio", "Tipo");
        imprimirSeparador();
    }

    /** Imprime una fila de la tabla con {@code printf}. */
    private void imprimirFila(int id, Videojuego j) {
        String tipo = (j instanceof VideojuegoDigitales) ? "Digital" : "Físico";
        System.out.printf("  %-4d %-32s %-14s %-6d %-10s %6.2f€  %-7s%n",
            id, truncar(j.getNombre(), 32), truncar(j.getGenero(), 14),
            j.getPegi(), truncar(j.getConsola(), 10), j.getPrecio(), tipo);
    }

    /** Imprime una línea separadora. */
    private void imprimirSeparador() {
        System.out.println("  " + "─".repeat(88));
    }

    /**
     * Trunca un texto a la longitud máxima indicada.
     *
     * @param texto texto a truncar
     * @param max   longitud máxima
     * @return texto truncado con "…" si supera el máximo
     */
    private String truncar(String texto, int max) {
        if (texto == null) return "";
        return texto.length() <= max ? texto : texto.substring(0, max - 1) + "…";
    }

    // ─── Helpers de entrada con validación regex ──────────────────────────────

    /**
     * Pide un campo por consola y lo repite hasta que coincida con el patrón regex.
     *
     * @param etiqueta nombre del campo que se muestra al usuario
     * @param patron   patrón regex que debe cumplir el valor
     * @return valor introducido y validado
     */
    private String pedirCampo(String etiqueta, Pattern patron) {
        String valor;
        do {
            System.out.print("  " + etiqueta + ": ");
            valor = sc.nextLine().trim();
            if (!patron.matcher(valor).matches()) {
                System.out.println("    ⚠ Formato no válido. Inténtalo de nuevo.");
            }
        } while (!patron.matcher(valor).matches());
        return valor;
    }

    /**
     * Pide un campo de texto libre (sin validación regex estricta, solo no vacío).
     *
     * @param etiqueta nombre del campo
     * @return valor introducido, nunca vacío
     */
    private String pedirCampoLibre(String etiqueta) {
        String valor;
        do {
            System.out.print("  " + etiqueta + ": ");
            valor = sc.nextLine().trim();
            if (valor.isEmpty()) System.out.println("    ⚠ Este campo no puede estar vacío.");
        } while (valor.isEmpty());
        return valor;
    }
}