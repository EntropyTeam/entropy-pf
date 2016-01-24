package backend.auxiliares;

import frontend.auxiliares.DialogMensaje;
import javax.swing.JOptionPane;

/**
 * Clase para mostrar mensajes de información, de error y de opción.
 * 
 * @author Lucas Cunibertti
 */
public class Mensajes {
    
    /**
     * Metodo dedicado a mostrar un mensaje informacion
     *
     * @param informacion mensaje de información
     */
    public static void mostrarInformacion(String informacion) {
        new DialogMensaje(null, true, DialogMensaje.TipoMensaje.INFORMACION, null, informacion).setVisible(true);
    }
    
    /**
     * Metodo dedicado a mostrar un mensaje error
     *
     * @param error error a mostrar
     */
    public static void mostrarError(String error) {
        new DialogMensaje(null, true, DialogMensaje.TipoMensaje.ERROR, null, error).setVisible(true);
    }
    
    /**
     * Metodo dedicado a mostrar un mensaje de advertencia.
     *
     * @param advertencia error a mostrar
     */
    public static void mostrarAdvertencia(String advertencia) {
        new DialogMensaje(null, true, DialogMensaje.TipoMensaje.ADVERTENCIA, null, advertencia).setVisible(true);
    }
    
    /**
     * Metodo dedicado a mostrar un mensaje de éxito.
     *
     * @param exito error a mostrar
     */
    public static void mostrarExito(String exito) {
        new DialogMensaje(null, true, DialogMensaje.TipoMensaje.EXITO, null, exito).setVisible(true);
    }
    
    /**
     * Metodo dedicado a mostrar un mensaje de confirmación
     *
     * @param mensaje mensaje a mostrar
     * @return true si se confirma, false si no se confirma
     */
    public static boolean mostrarConfirmacion(String mensaje) {
        DialogMensaje dm = new DialogMensaje(null, true, DialogMensaje.TipoMensaje.OPCION_YES_CANCEL, null, mensaje);
        dm.setVisible(true);
        
        int opcion = dm.getOpcionElegida();
        
        return opcion == JOptionPane.YES_OPTION;
    }
   
    
    /**
     * Metodo dedicado a mostrar un mensaje de opción.
     *
     * @param mensaje mensaje a mostrar
     * @return <code>JOptionPane.YES_OPTION</code>, 
     *         <code>JOptionPane.NO_OPTION</code>, 
     *         <code>JOptionPane.CANCEL_OPTION</code>, 
     */
    public static int mostrarOpcion(String mensaje) {
        DialogMensaje dm = new DialogMensaje(null, true, DialogMensaje.TipoMensaje.OPCION_YES_NO_CANCEL, null, mensaje);
        dm.setVisible(true);
        return dm.getOpcionElegida();
    }
    
   /**
     * Metodo dedicado a mostrar un mensaje de opción personalizado.
     *
     * @param mensaje mensaje a mostrar
     * @param colOpciones vector de strings a mostrarse en cada uno de los botones; un string = boton.
     * @return el indice del boton presionado, desde  hasta colOpciones.length.
     */
    public static int mostrarOpcion(String mensaje, String[] colOpciones) {
        DialogMensaje dm = new DialogMensaje(null, true, DialogMensaje.TipoMensaje.OPCION_CUSTOM, null, mensaje, colOpciones);
        dm.setVisible(true);
        return dm.getOpcionElegida();
    }

    public static void mostrarAcercaDe() {
        new DialogMensaje(null, true, DialogMensaje.TipoMensaje.ACERCA_DE, null, "Sistema de administración para entornos educativos.\nVersión 1.0.0\nProgramado por Entropy Team.").setVisible(true);
    }
}
