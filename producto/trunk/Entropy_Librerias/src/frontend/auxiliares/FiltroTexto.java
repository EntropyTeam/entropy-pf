package frontend.auxiliares;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Clase que maneja el filtrado de caracteres tipeados.
 *
 * @author Denise
 */
public final class FiltroTexto extends PlainDocument {

    public final static int LONGITUD_PROHIBIDA = -1;
    private int longitudMaxima = LONGITUD_PROHIBIDA;
    private TipoFiltro tipoFiltro;
    private String caracteresAceptados;

    /**
     * Constructor de la clase.
     *
     * @param caracteresAceptados cadena de caracteres aceptados
     */
    public FiltroTexto(String caracteresAceptados) {
        this.caracteresAceptados = caracteresAceptados;
        this.tipoFiltro = TipoFiltro.getEnum(caracteresAceptados);
    }

    public FiltroTexto(TipoFiltro tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
        this.caracteresAceptados =  tipoFiltro.toString();
    }

    public FiltroTexto(String caracteresAceptados, int longitudMaxima) {
        this.caracteresAceptados = caracteresAceptados;
        this.tipoFiltro = TipoFiltro.getEnum(caracteresAceptados);
        setLongitudMaxima(longitudMaxima);
    }

    public FiltroTexto(int longitudMaxima) {
        setLongitudMaxima(longitudMaxima);
    }

    public String getCaracteresAceptados() {
        return caracteresAceptados;
    }

    public void setCaracteresAceptados(String caracteresAceptados) {
        this.caracteresAceptados = caracteresAceptados;
        this.tipoFiltro = TipoFiltro.getEnum(caracteresAceptados);
    }

    public int getLongitudMaxima() {
        return longitudMaxima;
    }

    public void setLongitudMaxima(int longitudMaxima) {
        if (longitudMaxima < LONGITUD_PROHIBIDA || longitudMaxima == 0) {
            throw new IllegalArgumentException(
                    "La longitud máxima debe ser >=1.");
        }
        this.longitudMaxima = longitudMaxima;
    }

    @Override
    public void insertString(int offset, String strCadena, AttributeSet atributos)
            throws BadLocationException {
        if (strCadena == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (tipoFiltro != null) {
            // Deniega tipeo de doble guión.
            if (tipoFiltro == FiltroTexto.TipoFiltro.NUMEROS_DECIMALES) {
                if (strCadena.contains("-") && offset != 0) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }

            // Deniega tipeo de doble punto.
            if (tipoFiltro == FiltroTexto.TipoFiltro.NUMEROS_DECIMALES || tipoFiltro == FiltroTexto.TipoFiltro.NUMEROS_DECIMALES_POSITIVOS) {
                if (strCadena.startsWith(".")
                        && (offset == 0
                        || (super.getText(0, super.getLength()).endsWith("-") || super.getText(0, super.getLength()).contains(".")))) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }            
        }

        // Chequea longitud máxima
        if (longitudMaxima != LONGITUD_PROHIBIDA
                && getLength() + strCadena.length() > longitudMaxima) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        // Chequea caracteres aceptados
        if (caracteresAceptados != null) {
            for (int i = 0; i < strCadena.length(); i++) {
                if (caracteresAceptados.indexOf(strCadena.charAt(i)) == -1) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }
        }

        // Interta la cadena de texto
        super.insertString(offset, strCadena, atributos);
    }
    
    /**
     * Diferentes tipos de filtros para el texto.
     *
     * @author Denise
     */
    public enum TipoFiltro {
        DIGITOS("0123456789"),
        LETRAS(" abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZaÄöÖüÜíÍáÁúÚéÉóÓâÂêÊîÎôÔûÛàÀèÈìÌòÒùÙçÇ"),
        DIGITOS_Y_COMA(DIGITOS + ","),
        DIGITOS_Y_PUNTO(DIGITOS + "."),
        DIGITOS_Y_LETRAS(DIGITOS.toString() + LETRAS.toString()),
        NUMEROS_DECIMALES(DIGITOS_Y_PUNTO + "-"),
        NUMEROS_DECIMALES_POSITIVOS(DIGITOS_Y_PUNTO.toString()),
        USUARIO_Y_CONTRASEÑA("ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz_-.,;:?!0123456789"),
        TODO(" abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZaÄöÖüÜíÍáÁúÚéÉóÓâÂêÊîÎôÔûÛàÀèÈìÌòÒùÙçÇ_-.,;:¿?¡!0123456789()/\\{}[]*+&%$\"'#@|º=:;");

        private final String value;
        
        /**
         * Constructor de la enumeración.
         *
         * @param value valor a almacenar.
         */
        private TipoFiltro(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static TipoFiltro getEnum(String value) {
            for (TipoFiltro v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            return null;
        }

    }

}
