package backend.main;

import backend.auxiliares.Mensajes;
import backend.gestores.GestorConfiguracion;
import backend.persistencia.GestorPersistencia;
import backend.resoluciones.Resolucion;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.VentanaPrincipal;
import frontend.red.DialogConectarServidor;
import frontend.usuario.PanelDatosUsuario;
import java.io.File;
import java.io.IOException;
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

        /* Configuración del look & feel */
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TextField.font", LookAndFeelEntropy.FUENTE_REGULAR);
            UIManager.put("TextArea.font", LookAndFeelEntropy.FUENTE_REGULAR);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal principal = VentanaPrincipal.getInstancia();
                principal.setVisible(true);
                comprobarPerfilUsuario();
                comprobarExamenReanudar(principal);
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
    
    private static void comprobarExamenReanudar(VentanaPrincipal principal) {
        GestorPersistencia gestorPersistencia = new GestorPersistencia();
        if(gestorPersistencia.existeExamenRecuperar()) {
            Resolucion resolucion = gestorPersistencia.leerResolucion();
            String strMensaje = "Existe un examen para recuperar:"
                    + "\n\n"
                    + "\"" + resolucion.getExamen().getStrNombre() + "\""
                    + "\n\n"
                    + "¿Qué desea hacer?"
                    + "\n\n"
                    + "NOTA: Si descarta los cambios se borrará el examen definitivamente,"
                    + " si pospone puede guardar la copia del examen en un lugar seguro para"
                    + " recuperarla luego.";
            int intOpcion = Mensajes.mostrarOpcion(strMensaje, new String[] {"Recuperar", "Posponer", "Descartar"});
            
            switch(intOpcion) {
                // Recupera
                case 0:
                    new DialogConectarServidor(principal, true, DialogConectarServidor.TipoAccion.EXAMEN, resolucion).setVisible(true);
                    break;
                
                // Pospone    
                case 1:
                    File directorio = gestorPersistencia.seleccionarDirectorio(VentanaPrincipal.getInstancia().getPnlInicio());
                    if (directorio != null) {
                        gestorPersistencia.guardarResolucionEnDirectorio(resolucion, directorio);
                        gestorPersistencia.eliminarResolucion();
                    }
                    else comprobarExamenReanudar(principal);
                    break;
                
                // Descarta
                case 2:
                    if (Mensajes.mostrarConfirmacion("¿Está seguro que desea eliminar el examen?")) gestorPersistencia.eliminarResolucion();
                    else comprobarExamenReanudar(principal);
            }
        }
    }
}
