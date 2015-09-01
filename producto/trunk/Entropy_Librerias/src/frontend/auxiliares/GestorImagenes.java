package frontend.auxiliares;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Clase para la cara y el redimensionamiento de imágenes.
 *
 * @author Denise
 */
public class GestorImagenes {

    /**
     * Retorna la imagen en la ruta pasada por parámetro.
     *
     * @param strRutaImagen ruta a la imagen
     * @return objeto ImageIcon si la imagen existe, null de lo contrario
     */
    public static ImageIcon crearImageIcon(String strRutaImagen) {
        try {
            return new ImageIcon(GestorImagenes.class.getResource(strRutaImagen));
        } catch (Exception ex) {
            System.err.println("No se encontró el archivo de imagen en la ruta: " + strRutaImagen);
            return null;
        }
    }

    /**
     * Retorna la imagen en la ruta pasada por parámetro.
     *
     * @param strRutaImagen ruta a la imagen
     * @return objeto Image si la imagen existe, null de lo contrario
     */
    public static Image crearImage(String strRutaImagen) {
        try {
            return new ImageIcon(GestorImagenes.class.getResource(strRutaImagen)).getImage();
        } catch (Exception ex) {
            System.err.println("No se encontró el archivo de imagen en la ruta: " + strRutaImagen);
            return null;
        }
    }

    /**
     * Retorna una imagen redimensionada.
     *
     * @param strRutaAbsoluta ruta absoluta al archivo imagen
     * @param intAncho ancho de la nueva imagen en pixeles
     * @param intAlto alto de la nueva imagen en pixeles
     * @return la imagen redimensionada, null si no existe el archivo
     */
    public static BufferedImage redimensionarImagen(String strRutaAbsoluta, int intAncho, int intAlto) {

        try {
            BufferedImage imgOriginal = ImageIO.read(new File(strRutaAbsoluta));
            int intTipo = imgOriginal.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : imgOriginal.getType();

            BufferedImage imgRedimensionada = new BufferedImage(intAncho, intAlto, intTipo);

            Graphics2D g = imgRedimensionada.createGraphics();
            g.drawImage(imgOriginal, 0, 0, intAncho, intAlto, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            return imgRedimensionada;

        } catch (IOException ex) {
            Logger.getLogger(GestorImagenes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
