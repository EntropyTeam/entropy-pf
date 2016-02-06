package backend.main;

import backend.auxiliares.Mensajes;
import backend.dao.DAOConexion;
import backend.gestores.GestorConfiguracion;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.VentanaPrincipal;
import frontend.usuario.PanelDatosUsuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Clase para el arranque de la aplicación.
 *
 * @author Denise
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /**
         * La primer conexión a la BD tarda mucho, por eso se hace una conexión
         * ahora, para ahorrar tiempo después.
         */
        DAOConexion.conectarBaseDatos();

        /* Configuración del look & feel */
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TextField.font", LookAndFeelEntropy.FUENTE_REGULAR);
            UIManager.put("TextArea.font", LookAndFeelEntropy.FUENTE_REGULAR);
            UIManager.put("Tree.collapsedIcon", GestorImagenes.crearImageIcon("/frontend/imagenes/ic_tree_collapsed.png"));
            UIManager.put("Tree.expandedIcon", GestorImagenes.crearImageIcon("/frontend/imagenes/ic_tree_expanded.png"));
            UIManager.put("MenuItem.selectionBackground", LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
            UIManager.put("Menu.selectionBackground", LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
            UIManager.put("PopupMenu.border", LookAndFeelEntropy.BORDE_ENTROPY);
            UIManager.put("ComboBox.selectionBackground", LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal.getInstancia().setVisible(true);
                comprobarPerfilUsuario();
            }
        });
    }

    private static void comprobarPerfilUsuario() {
        try {
            if (!GestorConfiguracion.getInstancia().getIDAOUsuarios().hayUsuariosGuardados()) {
                if (Mensajes.mostrarConfirmacion("Aún no ha configurado los datos de su perfil. ¿Desea hacerlo ahora?")) {
                    PanelDatosUsuario pnlDatosUsuarios = new PanelDatosUsuario(VentanaPrincipal.getInstancia());
                    pnlDatosUsuarios.setName("Perfil Usuario");
                    VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(pnlDatosUsuarios);
                    VentanaPrincipal.getInstancia().setTitle("Perfil de Usuario");
                    if (!VentanaPrincipal.getInstancia().isMaximized()){
                        VentanaPrincipal.getInstancia().pack();
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
