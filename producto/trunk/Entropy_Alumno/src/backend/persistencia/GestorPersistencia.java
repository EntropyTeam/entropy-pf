package backend.persistencia;

import backend.resoluciones.Resolucion;
import frontend.inicio.PanelInicio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author gaston
 */
public class GestorPersistencia {
    
    private final File archivoBackUp;
    private static final String NOMBRE = "temp.etp";
    
    /**
     * Constructor del gestor de persistencia para guardar el examen y
     * recuperarlo del disco en un directorio seleccionado.
     */
    public GestorPersistencia() {
        this.archivoBackUp = new File(this.NOMBRE);
    }
    
    /**
     * Metoro para persistir un Examen en disco.
     *
     * @param resolucion El Examen que se quiere guardar.
     */
    public void guardarResolucion(Resolucion resolucion) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.archivoBackUp.getAbsolutePath());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(resolucion);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para leer un Examen guardado.
     *
     * @return El Examen que se esta leyendo.
     */
    public Resolucion leerResolucion() {
        Resolucion resolucion = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(this.archivoBackUp);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            resolucion = (Resolucion) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resolucion;
    }
    
    public void eliminarResolucion() {
        this.archivoBackUp.delete();
    }
    
    /**
     * Metodo que invoca a un jFileChooser para seleccionar el directorio donde
     * se va a guardar el Examen en disco.
     *
     * @param padre El jPanel padre que llamo al gestor para volver luego de la
     * ejecucion del jFileChooser.
     * @return Retorno del directorio seleccionado o null en caso que no
     * seleccione.
     */
    public File seleccionarDirectorio(PanelInicio padre) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jFileChooser.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile();
        }
        return null;
    }
    
    /**
     * 
     * @param resolucion
     * @param directorio 
     */
    public void guardarResolucionEnDirectorio(Resolucion resolucion, File directorio) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(directorio + File.separator + resolucion.getExamen().getStrNombre() + ".etp");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(resolucion);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param padre
     * @return 
     */
    public Resolucion seleccionarResolucionALeer(PanelInicio padre) {
        Resolucion resolucion = null;
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter entropyFilesFilter = new FileNameExtensionFilter("Archivos Entropy", "etp");
        jFileChooser.setFileFilter(entropyFilesFilter);
        if (jFileChooser.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File archivo = jFileChooser.getSelectedFile();
            try {
                FileInputStream fileInputStream = new FileInputStream(archivo);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                resolucion = (Resolucion) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return resolucion;
    }
    
    /**
     * 
     * @return 
     */
    public boolean existeExamenRecuperar() {
        return new File(this.NOMBRE).exists();
    }
}
