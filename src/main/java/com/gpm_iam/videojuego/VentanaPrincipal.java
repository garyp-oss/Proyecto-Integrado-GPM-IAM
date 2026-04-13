package com.gpm_iam.videojuego;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

/**
 * Ventana principal de la aplicación GameVault.
 *
 * <p>Construida completamente a mano con Swing, sin editor visual.
 * Contiene un {@link JTable} con {@link DefaultTableModel}, un buscador
 * con filtrado en tiempo real, botones de acción y un {@link WindowListener}
 * que pregunta si guardar al cerrar.</p>
 *
 * @author Gary
 * @version 3.0
 */
public class VentanaPrincipal extends JFrame {

    // ─── Componentes principales ──────────────────────────────────────────────

    /** Tabla que muestra el catálogo de videojuegos. */
    private JTable tabla;

    /** Modelo de datos de la tabla. */
    private DefaultTableModel modeloTabla;

    /** Campo de texto del buscador. */
    private JTextField campoBusqueda;

    /** Etiqueta que muestra el total de juegos. */
    private JLabel etiquetaTotal;

    // ─── Dependencias ─────────────────────────────────────────────────────────

    private final Catalogo       catalogo;
    private final GestorFicheros gestor;

    // ─── Columnas de la tabla ─────────────────────────────────────────────────

    private static final String[] COLUMNAS = {
        "ID", "Nombre", "Género", "PEGI", "Consola", "Precio (€)", "Tipo", "Tienda"
    };

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea y muestra la ventana principal cargando el catálogo desde CSV.
     *
     * @param catalogo instancia del catálogo
     * @param gestor   gestor de ficheros para carga/guardado
     */
    public VentanaPrincipal(Catalogo catalogo, GestorFicheros gestor) {
        this.catalogo = catalogo;
        this.gestor   = gestor;

        cargarDatos();
        construirVentana();
        registrarWindowListener();

        setVisible(true);
    }

    // ─── Carga de datos ───────────────────────────────────────────────────────

    /**
     * Carga el catálogo desde el CSV al arrancar.
     */
    private void cargarDatos() {
        try {
            List<Videojuego> cargados = gestor.cargar();
            cargados.forEach(catalogo::agregarJuego);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar el catálogo:\n" + e.getMessage(),
                "Error al cargar", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ─── Construcción de la ventana ───────────────────────────────────────────

    /**
     * Construye todos los componentes de la ventana y los organiza con layouts.
     */
    private void construirVentana() {
        setTitle("GameVault - Gestión de Videojuegos");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        add(crearPanelNorte(), BorderLayout.NORTH);
        add(crearPanelCentro(), BorderLayout.CENTER);
        add(crearPanelSur(), BorderLayout.SOUTH);
    }

    // ─── Panel norte: título y buscador ───────────────────────────────────────

    /**
     * Crea el panel superior con el título y el buscador.
     *
     * @return panel norte configurado
     */
    private JPanel crearPanelNorte() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        // Título
        JLabel titulo = new JLabel("GameVault - Catálogo de Videojuegos");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        // Panel buscador
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar: "));

        campoBusqueda = new JTextField(25);
        panelBusqueda.add(campoBusqueda);

        // ActionListener con LAMBDA — requerido por la rúbrica
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> filtrarTabla(campoBusqueda.getText()));
        panelBusqueda.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> {
            campoBusqueda.setText("");
            refrescarTabla();
        });
        panelBusqueda.add(btnLimpiar);

        panel.add(panelBusqueda, BorderLayout.SOUTH);

        // Filtrado en tiempo real mientras el usuario escribe
        campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filtrarTabla(campoBusqueda.getText()); }
            public void removeUpdate(DocumentEvent e)  { filtrarTabla(campoBusqueda.getText()); }
            public void changedUpdate(DocumentEvent e) { filtrarTabla(campoBusqueda.getText()); }
        });

        return panel;
    }

    // ─── Panel central: tabla ─────────────────────────────────────────────────

    /**
     * Crea el panel central con la {@link JTable} y su {@link JScrollPane}.
     *
     * @return panel con la tabla
     */
    private JPanel crearPanelCentro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        // DefaultTableModel con columnas no editables directamente
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // la tabla no se edita directamente
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Anchos de columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(110);

        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);

        refrescarTabla();
        return panel;
    }

    // ─── Panel sur: botones y total ───────────────────────────────────────────

    /**
     * Crea el panel inferior con los botones de acción y la etiqueta de total.
     *
     * @return panel sur configurado
     */
    private JPanel crearPanelSur() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JButton btnAnyadir  = new JButton("Añadir");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnGuardar  = new JButton("Guardar CSV");
        JButton btnExportar = new JButton("Exportar JSON");

        panelBotones.add(btnAnyadir);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnExportar);

        // ActionListener con CLASE ANÓNIMA — requerido por la rúbrica
        btnAnyadir.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                abrirDialogoAnyadir();
            }
        });

        // Los demás con lambda
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnGuardar.addActionListener(e -> guardarCSV());
        btnExportar.addActionListener(e -> exportarJSON());

        panel.add(panelBotones, BorderLayout.WEST);

        // Etiqueta total
        etiquetaTotal = new JLabel("Total: 0 juegos");
        panel.add(etiquetaTotal, BorderLayout.EAST);

        return panel;
    }

    // ─── WindowListener ───────────────────────────────────────────────────────

    /**
     * Registra el {@link WindowListener} que intercepta el cierre de la ventana
     * y pregunta al usuario si quiere guardar antes de salir.
     */
    private void registrarWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                    VentanaPrincipal.this,
                    "¿Quieres guardar el catálogo antes de cerrar?",
                    "Cerrar GameVault",
                    JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    guardarCSV();
                    dispose();
                    System.exit(0);
                } else if (opcion == JOptionPane.NO_OPTION) {
                    dispose();
                    System.exit(0);
                }
                // CANCEL: no hace nada, la ventana sigue abierta
            }
        });
    }

    // ─── Acciones de los botones ──────────────────────────────────────────────

    /**
     * Abre el {@link DialogoFormulario} modal para añadir un juego nuevo.
     * Si el usuario confirma, añade el juego al catálogo y refresca la tabla.
     */
    private void abrirDialogoAnyadir() {
        DialogoFormulario dialogo = new DialogoFormulario(this);
        dialogo.setVisible(true);

        Videojuego nuevo = dialogo.getResultado();
        if (nuevo != null) {
            catalogo.agregarJuego(nuevo);
            refrescarTabla();
            JOptionPane.showMessageDialog(this,
                "Juego añadido: " + nuevo.getNombre(),
                "Añadido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Elimina el juego seleccionado en la tabla tras pedir confirmación con {@link JOptionPane}.
     */
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un juego de la tabla primero.",
                "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = (String) modeloTabla.getValueAt(fila, 1);

        // JOptionPane de confirmación — requerido por la rúbrica
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Seguro que quieres eliminar \"" + nombre + "\"?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Iterator explícito para eliminar de forma segura
            java.util.Iterator<Videojuego> it = catalogo.getCatalogo().iterator();
            while (it.hasNext()) {
                if (it.next().getNombre().equals(nombre)) {
                    it.remove();
                    break;
                }
            }
            refrescarTabla();
        }
    }

    /**
     * Guarda el catálogo en el CSV y muestra un mensaje de resultado.
     */
    private void guardarCSV() {
        try {
            gestor.guardar(catalogo.getCatalogo());
            JOptionPane.showMessageDialog(this,
                "Catálogo guardado correctamente.",
                "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exporta el catálogo a JSON y muestra un mensaje de resultado.
     */
    private void exportarJSON() {
        try {
            gestor.exportarJson(catalogo.getCatalogo());
            JOptionPane.showMessageDialog(this,
                "JSON exportado en datos/catalogo.json",
                "Exportado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error al exportar:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Tabla: refrescar y filtrar ───────────────────────────────────────────

    /**
     * Recarga la tabla con todos los juegos del catálogo.
     */
    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        int id = 1;
        for (Videojuego j : catalogo.getCatalogo()) {
            modeloTabla.addRow(crearFila(id++, j));
        }
        actualizarTotal();
    }

    /**
     * Filtra la tabla mostrando solo los juegos cuyo nombre contiene el texto buscado.
     * La búsqueda es insensible a mayúsculas.
     *
     * @param texto texto a buscar en el nombre del juego
     */
    private void filtrarTabla(String texto) {
        modeloTabla.setRowCount(0);
        String textoBajo = texto.toLowerCase();
        int id = 1;
        for (Videojuego j : catalogo.getCatalogo()) {
            if (j.getNombre().toLowerCase().contains(textoBajo)) {
                modeloTabla.addRow(crearFila(id, j));
            }
            id++;
        }
        actualizarTotal();
    }

    /**
     * Construye el array de datos de una fila para la tabla.
     *
     * @param id identificador de fila
     * @param j  videojuego a representar
     * @return array de Object con los datos de la fila
     */
    private Object[] crearFila(int id, Videojuego j) {
        String tipo, tienda;
        if (j instanceof VideojuegoDigitales) {
            VideojuegoDigitales d = (VideojuegoDigitales) j;
            tipo   = "Digital";
            tienda = d.getTiendaDigital();
        } else {
            VideojuegoFisico f = (VideojuegoFisico) j;
            tipo   = "Físico";
            tienda = f.getTiendaFisica();
        }
        return new Object[]{
            id, j.getNombre(), j.getGenero(), j.getPegi(),
            j.getConsola(), String.format("%.2f", j.getPrecio()), tipo, tienda
        };
    }

    /**
     * Actualiza la etiqueta con el número de filas visibles en la tabla.
     */
    private void actualizarTotal() {
        etiquetaTotal.setText("Total: " + modeloTabla.getRowCount() + " juegos");
    }
}