package com.gpm_iam.videojuego;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia del catálogo en ficheros.
 *
 * <p>Implementa tres mecanismos de E/S distintos según el requisito:</p>
 * <ul>
 *   <li>{@link BufferedReader} — lectura secuencial del CSV.</li>
 *   <li>{@link FileWriter} + {@link BufferedWriter} — escritura del CSV.</li>
 *   <li>{@link RandomAccessFile} — búsqueda de un juego por ID (número de línea).</li>
 * </ul>
 *
 * <p>Formato CSV:</p>
 * <pre>tipo;nombre;genero;pegi;consola;etiquetas;precio;tiendaODatos;segundaMano;valoracion</pre>
 * <p>Para juegos digitales, {@code segundaMano} y {@code valoracion} se guardan como {@code -} y {@code 0.0}.</p>
 *
 * @author Gary
 * @version 1.0
 */
public class GestorFicheros {

    /** Ruta relativa al fichero CSV del catálogo. */
    private static final String RUTA_CSV  = "datos/catalogo.csv";

    /** Ruta relativa al fichero JSON de exportación. */
    private static final String RUTA_JSON = "datos/catalogo.json";

    // ─── Escritura CSV ────────────────────────────────────────────────────────

    /**
     * Guarda toda la lista de videojuegos en el fichero CSV, sobreescribiendo el contenido anterior.
     *
     * <p>Usa {@link FileWriter} con {@link BufferedWriter} y {@code try-with-resources}.</p>
     *
     * @param juegos lista de videojuegos a guardar
     * @throws IOException si ocurre un error de escritura
     */
    public void guardar(List<Videojuego> juegos) throws IOException {
        // Nos aseguramos de que la carpeta datos/ existe
        new File("datos").mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_CSV, false))) {
            for (Videojuego j : juegos) {
                bw.write(convertirALinea(j));
                bw.newLine();
            }
        }
        System.out.println("  ✓ Catálogo guardado en " + RUTA_CSV + " (" + juegos.size() + " juegos).");
    }

    // ─── Lectura CSV (BufferedReader) ─────────────────────────────────────────

    /**
     * Carga los videojuegos desde el fichero CSV.
     *
     * <p>Usa {@link BufferedReader} con {@code try-with-resources}.
     * Si el fichero no existe devuelve una lista vacía sin lanzar excepción.</p>
     *
     * @return lista de videojuegos leídos; vacía si el fichero no existe o está vacío
     * @throws IOException si ocurre un error de lectura distinto a fichero inexistente
     */
    public List<Videojuego> cargar() throws IOException {
        List<Videojuego> lista = new ArrayList<>();
        File fichero = new File(RUTA_CSV);

        if (!fichero.exists()) {
            System.out.println("  ℹ No se encontró " + RUTA_CSV + ". Se arranca con catálogo vacío.");
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                try {
                    Videojuego j = parsearLinea(linea);
                    if (j != null) lista.add(j);
                } catch (Exception e) {
                    System.err.println("  ⚠ Línea " + numeroLinea + " ignorada (formato incorrecto): " + linea);
                }
            }
        }
        System.out.println("  ✓ Catálogo cargado: " + lista.size() + " juegos.");
        return lista;
    }

    // ─── Búsqueda por ID con RandomAccessFile ────────────────────────────────

    /**
     * Busca un videojuego por su ID (número de línea, empezando en 1) usando {@link RandomAccessFile}.
     *
     * <p>Lee el fichero byte a byte para localizar la línea concreta sin cargar todo el fichero.</p>
     *
     * @param id número de línea del juego (1 = primera línea)
     * @return el {@link Videojuego} encontrado, o {@code null} si el ID no existe
     * @throws IOException si ocurre un error de lectura
     */
    public Videojuego buscarPorId(int id) throws IOException {
        File fichero = new File(RUTA_CSV);
        if (!fichero.exists()) return null;

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            int lineaActual = 1;
            StringBuilder sb = new StringBuilder();
            int byteLeido;

            while ((byteLeido = raf.read()) != -1) {
                char c = (char) byteLeido;
                if (c == '\n') {
                    if (lineaActual == id) {
                        return parsearLinea(sb.toString().trim());
                    }
                    lineaActual++;
                    sb.setLength(0);
                } else if (c != '\r') {
                    sb.append(c);
                }
            }
            // Última línea sin salto de línea final
            if (lineaActual == id && sb.length() > 0) {
                return parsearLinea(sb.toString().trim());
            }
        }
        return null;
    }

    // ─── Exportación JSON manual ──────────────────────────────────────────────

    /**
     * Exporta el catálogo completo a un fichero JSON construido manualmente con {@link StringBuilder}.
     *
     * <p>Solo exporta los juegos que implementan {@link Exportable}.
     * El JSON generado es válido y puede verificarse en jsonlint.com.</p>
     *
     * @param juegos lista de videojuegos a exportar
     * @throws IOException si ocurre un error de escritura
     */
    public void exportarJson(List<Videojuego> juegos) throws IOException {
        new File("datos").mkdirs();

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"gamevault\": [\n");

        boolean primero = true;
        for (Videojuego j : juegos) {
            if (j instanceof Exportable) {
                Exportable exportable = (Exportable) j;
                if (!primero) sb.append(",\n");
                sb.append(exportable.exportarJson());
                primero = false;
            }
        }

        sb.append("\n  ]\n");
        sb.append("}\n");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_JSON, false))) {
            bw.write(sb.toString());
        }
        System.out.println("  ✓ JSON exportado en " + RUTA_JSON);
    }

    // ─── Helpers privados ─────────────────────────────────────────────────────

    /**
     * Convierte un {@link Videojuego} a una línea CSV.
     *
     * @param j videojuego a convertir
     * @return línea en formato CSV
     */
    private String convertirALinea(Videojuego j) {
        // tipo;nombre;genero;pegi;consola;etiquetas;precio;tienda;segundaMano;valoracion
        String tipo        = (j instanceof VideojuegoDigitales) ? "digital" : "fisico";
        String etiquetas   = String.join("|", j.getEtiquetas());
        String segundaMano = "false";
        String valoracion  = "0.0";
        String tienda;

        if (j instanceof VideojuegoDigitales) {
            VideojuegoDigitales d = (VideojuegoDigitales) j;
            tienda = d.getTiendaDigital();
        } else {
            VideojuegoFisico f = (VideojuegoFisico) j;
            tienda      = f.getTiendaFisica();
            segundaMano = String.valueOf(f.isSegundaMano());
            valoracion  = String.valueOf(f.getValoracion());
        }

        return String.join(";",
            tipo,
            escapar(j.getNombre()),
            j.getGenero(),
            String.valueOf(j.getPegi()),
            j.getConsola(),
            etiquetas,
            String.format("%.2f", j.getPrecio()).replace(",", "."),
            escapar(tienda),
            segundaMano,
            valoracion
        );
    }

    /**
     * Parsea una línea CSV y devuelve el {@link Videojuego} correspondiente.
     *
     * @param linea línea CSV a parsear
     * @return videojuego creado, o {@code null} si la línea está vacía
     * @throws IllegalArgumentException si el formato es incorrecto
     */
    private Videojuego parsearLinea(String linea) {
        if (linea == null || linea.isBlank()) return null;

        String[] partes = linea.split(";", -1);
        if (partes.length < 10) {
            throw new IllegalArgumentException("Faltan campos: se esperaban 10, hay " + partes.length);
        }

        String   tipo       = partes[0].trim();
        String   nombre     = partes[1].trim();
        String   genero     = partes[2].trim();
        int      pegi       = Integer.parseInt(partes[3].trim());
        String   consola    = partes[4].trim();
        String[] etiquetas  = partes[5].trim().isEmpty() ? new String[0] : partes[5].trim().split("\\|");
        double   precio     = Double.parseDouble(partes[6].trim());
        String   tienda     = partes[7].trim();
        boolean  segundaMano = Boolean.parseBoolean(partes[8].trim());
        double   valoracion = Double.parseDouble(partes[9].trim());

        if (tipo.equals("digital")) {
            return new VideojuegoDigitales(nombre, genero, pegi, consola, etiquetas, precio, tienda);
        } else if (tipo.equals("fisico")) {
            return new VideojuegoFisico(nombre, genero, pegi, consola, etiquetas, precio, tienda, segundaMano, valoracion);
        } else {
            throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }
    }

    /**
     * Escapa el carácter punto y coma en cadenas para no romper el CSV.
     *
     * @param texto texto a escapar
     * @return texto con los puntos y coma reemplazados por una coma
     */
    private String escapar(String texto) {
        return texto == null ? "" : texto.replace(";", ",");
    }
}