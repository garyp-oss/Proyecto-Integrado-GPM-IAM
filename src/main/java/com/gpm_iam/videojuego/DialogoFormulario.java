package com.gpm_iam.videojuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Diálogo modal para añadir un nuevo videojuego al catálogo.
 *
 * <p>Construido a mano con Swing. Valida todos los campos antes de aceptar.
 * El resultado se obtiene con {@link #getResultado()} tras cerrar el diálogo.</p>
 *
 * @author Gary
 * @version 3.0
 * @see VentanaPrincipal
 */
public class DialogoFormulario extends JDialog {

    // ─── Campos del formulario ────────────────────────────────────────────────

    private JTextField campoNombre;
    private JTextField campoGenero;
    private JComboBox<String> comboPegi;
    private JTextField campoConsola;
    private JTextField campoEtiquetas;
    private JTextField campoPrecio;
    private JTextField campoTienda;
    private JComboBox<String> comboTipo;

    // Solo para juegos físicos
    private JCheckBox checkSegundaMano;
    private JTextField campoValoracion;
    private JPanel panelFisico;

    /** Resultado: el videojuego creado, o null si el usuario canceló. */
    private Videojuego resultado = null;

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea el diálogo modal anclado a la ventana padre.
     *
     * @param padre ventana padre
     */
    public DialogoFormulario(JFrame padre) {
        super(padre, "Añadir videojuego", true); // true = modal
        construirDialogo();
        pack();
        setLocationRelativeTo(padre);
    }

    // ─── Construcción del diálogo ─────────────────────────────────────────────

    /**
     * Construye todos los componentes del formulario.
     */
    private void construirDialogo() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelBotones(),    BorderLayout.SOUTH);
    }

    /**
     * Crea el panel con todos los campos del formulario usando {@link GridBagLayout}.
     *
     * @return panel con los campos
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(4, 4, 4, 4);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Tipo
        comboTipo = new JComboBox<>(new String[]{"Digital", "Físico"});
        agregarFila(panel, gbc, fila++, "Tipo:", comboTipo);

        // Nombre
        campoNombre = new JTextField(22);
        agregarFila(panel, gbc, fila++, "Nombre:", campoNombre);

        // Género
        campoGenero = new JTextField(22);
        agregarFila(panel, gbc, fila++, "Género:", campoGenero);

        // PEGI
        comboPegi = new JComboBox<>(new String[]{"3", "7", "12", "16", "18"});
        agregarFila(panel, gbc, fila++, "PEGI:", comboPegi);

        // Consola
        campoConsola = new JTextField(22);
        agregarFila(panel, gbc, fila++, "Consola:", campoConsola);

        // Etiquetas
        campoEtiquetas = new JTextField(22);
        agregarFila(panel, gbc, fila++, "Etiquetas (coma):", campoEtiquetas);

        // Precio
        campoPrecio = new JTextField(22);
        agregarFila(panel, gbc, fila++, "Precio (€):", campoPrecio);

        // Tienda
        campoTienda = new JTextField(22);
        agregarFila(panel, gbc, fila++, "Tienda:", campoTienda);

        // Panel exclusivo para juegos físicos (segunda mano + valoración)
        panelFisico = new JPanel(new GridBagLayout());
        panelFisico.setBorder(BorderFactory.createTitledBorder("Solo juego físico"));

        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.insets = new Insets(3, 4, 3, 4);
        gbcF.anchor = GridBagConstraints.WEST;
        gbcF.fill   = GridBagConstraints.HORIZONTAL;

        checkSegundaMano = new JCheckBox();
        agregarFila(panelFisico, gbcF, 0, "Segunda mano:", checkSegundaMano);

        campoValoracion = new JTextField("5.0", 22);
        agregarFila(panelFisico, gbcF, 1, "Valoración (0-10):", campoValoracion);

        // El panelFisico se muestra u oculta según el tipo seleccionado
        gbc.gridx = 0; gbc.gridy = fila++; gbc.gridwidth = 2;
        panel.add(panelFisico, gbc);
        gbc.gridwidth = 1;

        // Listener para mostrar/ocultar panelFisico según tipo
        comboTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean esFisico = comboTipo.getSelectedItem().equals("Físico");
                panelFisico.setVisible(esFisico);
                pack();
            }
        });
        panelFisico.setVisible(false); // por defecto Digital

        return panel;
    }

    /**
     * Crea el panel inferior con los botones Aceptar y Cancelar.
     *
     * @return panel de botones
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAceptar  = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        // Aceptar con clase anónima para variar respecto a los lambdas de VentanaPrincipal
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarYCrear()) {
                    dispose();
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnAceptar);
        panel.add(btnCancelar);
        return panel;
    }

    // ─── Validación y creación ────────────────────────────────────────────────

    /**
     * Valida todos los campos y, si son correctos, crea el {@link Videojuego} y lo asigna a {@code resultado}.
     *
     * @return {@code true} si la validación fue correcta y el juego fue creado
     */
    private boolean validarYCrear() {
        String nombre  = campoNombre.getText().trim();
        String genero  = campoGenero.getText().trim();
        String consola = campoConsola.getText().trim();
        String tienda  = campoTienda.getText().trim();
        int    pegi    = Integer.parseInt((String) comboPegi.getSelectedItem());

        // Validaciones básicas
        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío.");
            campoNombre.requestFocus();
            return false;
        }
        if (genero.isEmpty()) {
            mostrarError("El género no puede estar vacío.");
            campoGenero.requestFocus();
            return false;
        }
        if (consola.isEmpty()) {
            mostrarError("La consola no puede estar vacía.");
            campoConsola.requestFocus();
            return false;
        }
        if (tienda.isEmpty()) {
            mostrarError("La tienda no puede estar vacía.");
            campoTienda.requestFocus();
            return false;
        }

        // Validar precio
        double precio;
        try {
            precio = Double.parseDouble(campoPrecio.getText().trim().replace(",", "."));
            if (precio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            mostrarError("El precio debe ser un número positivo (ej: 19.99).");
            campoPrecio.requestFocus();
            return false;
        }

        // Etiquetas
        String etiqTexto = campoEtiquetas.getText().trim();
        String[] etiquetas = etiqTexto.isEmpty() ? new String[0] : etiqTexto.split(",");
        for (int i = 0; i < etiquetas.length; i++) {
            etiquetas[i] = etiquetas[i].trim();
        }

        String tipo = (String) comboTipo.getSelectedItem();

        try {
            if ("Digital".equals(tipo)) {
                resultado = new VideojuegoDigitales(nombre, genero, pegi, consola, etiquetas, precio, tienda);
            } else {
                // Validar valoración
                double valoracion;
                try {
                    valoracion = Double.parseDouble(campoValoracion.getText().trim().replace(",", "."));
                    if (valoracion < 0 || valoracion > 10) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    mostrarError("La valoración debe ser un número entre 0 y 10.");
                    campoValoracion.requestFocus();
                    return false;
                }
                boolean segundaMano = checkSegundaMano.isSelected();
                resultado = new VideojuegoFisico(nombre, genero, pegi, consola, etiquetas,
                                                  precio, tienda, segundaMano, valoracion);
            }
        } catch (IllegalArgumentException ex) {
            mostrarError("Error al crear el juego:\n" + ex.getMessage());
            return false;
        }

        return true;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Añade una fila etiqueta + componente al panel con GridBagLayout.
     *
     * @param panel  panel destino
     * @param gbc    constraints de GridBag
     * @param fila   número de fila
     * @param label  texto de la etiqueta
     * @param comp   componente a añadir
     */
    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(comp, gbc);
    }

    /**
     * Muestra un mensaje de error en un {@link JOptionPane}.
     *
     * @param mensaje texto del error
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    // ─── Getter del resultado ─────────────────────────────────────────────────

    /**
     * Devuelve el videojuego creado por el formulario, o {@code null} si se canceló.
     *
     * @return videojuego creado, o {@code null}
     */
    public Videojuego getResultado() {
        return resultado;
    }
}