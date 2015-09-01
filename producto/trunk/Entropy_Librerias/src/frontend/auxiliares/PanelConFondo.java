package frontend.auxiliares;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Clase que representa un panel que admite una imagen de fondo.
 *
 * @author Denise
 */
public class PanelConFondo extends JPanel {

    private Image imgFondo;

    /**
     * Constructor por defecto.
     */
    public PanelConFondo() {
    }

    /**
     * Constructor de la clase.
     *
     * @param strRutaImagen ruta al archivo imagen
     */
    public PanelConFondo(String strRutaImagen) {
        if (strRutaImagen != null) {
            imgFondo = GestorImagenes.crearImage(strRutaImagen);
        }
    }

    /**
     * Constructor de la clase.
     *
     * @param imgImagen objeto Image que representa la imagen de fondo
     */
    public PanelConFondo(Image imgImagen) {
        if (imgImagen != null) {
            imgFondo = imgImagen;
        }
    }

    /**
     * Configura la imagen de fondo.
     *
     * @param strRutaImagen ruta al archivo imagen
     */
    public void setImagen(String strRutaImagen) {
        if (strRutaImagen != null) {
            imgFondo = GestorImagenes.crearImage(strRutaImagen);
        } else {
            imgFondo = null;
        }
        repaint();
    }

    /**
     * Configura la imagen de fondo.
     *
     * @param imgImagen objeto Image que representa la imagen de fondo
     */
    public void setImagen(Image imgImagen) {
        imgFondo = imgImagen;
        repaint();
    }

    /**
     * Permite dibujar la imagen de fondo en el panel.
     *
     * @param graphics objeto Graphics
     */
    @Override
    public void paint(Graphics graphics) {
        if (imgFondo != null) {
            graphics.drawImage(imgFondo, 0, 0, getWidth(), getHeight(),
                    this);
            setOpaque(false);
        } else {
            setOpaque(true);
        }

        super.paint(graphics);
    }
}
