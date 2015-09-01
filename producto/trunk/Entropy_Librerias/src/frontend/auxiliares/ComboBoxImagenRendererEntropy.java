package frontend.auxiliares;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Clase que representa un combobox con imagenes asociadas a cada item.
 * 
 * @author Denise
 */
public class ComboBoxImagenRendererEntropy extends JLabel implements ListCellRenderer {
    
    private final ImageIcon[] imagenes;

    private String[] rutas;
   
    /**
     * Constructor de la clase.
     * 
     * @param rutas vector de las rutas que corresponden a cada item, en orden.
     */
    public ComboBoxImagenRendererEntropy(String[] rutas) {
        this.rutas = rutas;
        imagenes = cargarImagenes();
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
        setFont(LookAndFeelEntropy.FUENTE_REGULAR);
    }
    
    /**
     * Construye objetos para las imágenes para las rutas cargadas.
     * 
     * @return vector de imagenes
     */
    private ImageIcon[] cargarImagenes(){
        ImageIcon[] imagenes = new ImageIcon[rutas.length];
        for (int i = 0; i < rutas.length; i++) {
            imagenes[i] = crearImageIcon(rutas[i]);
        }
        return imagenes;
    }
    
    /**
     * Retorna la imagen en la ruta pasada por parámetro.
     * 
     * @param strRutaImagen ruta a la imagen
     * @return imagen si la misma existe, null de lo contrario
     */
    private ImageIcon crearImageIcon(String strRutaImagen) {
        return GestorImagenes.crearImageIcon(strRutaImagen);
    }

    /**
     * Configura el vector de rutas para los items del combo.
     * 
     * @param rutas vector de rutas
     */
    public void setRutas(String[] rutas) {
        this.rutas = rutas;
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        
        if (isSelected) {
            setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        } else {
            setBackground(Color.WHITE);
        }

        if (index >= 0){
            setIcon(imagenes[index]);
        } else if (value != null) {
            int i;
            for (i = 0; i < list.getModel().getSize(); i++) {
                if(list.getModel().getElementAt(i).toString().equals(value.toString())){
                    break;
                }
            }
            setIcon(imagenes[i]);
        }
        
        setText((value == null) ? "" : value.toString());

        return this;
    }
}
