package com.gpm_iam.videojuego;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Clase principal de GameVault — Hito 3.
 *
 * <p>Arranca la interfaz gráfica Swing.</p>
 *
 * @author Gary
 * @version 3.0
 */
public class App {

    /**
     * Punto de entrada de la aplicación.
     * Lanza la GUI en el Event Dispatch Thread de Swing.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        Catalogo       catalogo = new Catalogo();
        GestorFicheros gestor   = new GestorFicheros();

        // Swing debe arrancarse siempre en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new VentanaPrincipal(catalogo, gestor);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al ejecutar :(" + e.getMessage());
            }
        });
    }
}