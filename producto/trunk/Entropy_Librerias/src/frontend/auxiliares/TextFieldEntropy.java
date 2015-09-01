package frontend.auxiliares;

import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Clase que representa un JTextField con la personalización del tema Entropy.
 *
 * @author Denise
 */
public class TextFieldEntropy extends JTextField implements IComponenteConTextoPorDefecto {

    private String strTextoPorDefecto;
    private Document filtroTexto;

    /**
     * Constructor de la clase.
     */
    public TextFieldEntropy() {
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param text texto a incluir en el componente
     */
    public TextFieldEntropy(String text) {
        super(text);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param columns cantidad de columnas
     */
    public TextFieldEntropy(int columns) {
        super(columns);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param text texto a incluir en el componente
     * @param columns cantidad de columnas
     */
    public TextFieldEntropy(String text, int columns) {
        super(text, columns);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param doc documento para el componente
     * @param text texto a incluir en el componente
     * @param columns cantidad de columnas
     */
    public TextFieldEntropy(Document doc, String text, int columns) {
        super(doc, text, columns);
        initComponents();
    }

    /**
     * Inicializa las características personalizadas.
     */
    private void initComponents() {
        strTextoPorDefecto = "";
        setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
        setCaretColor(LookAndFeelEntropy.COLOR_CURSOR);
        setSelectionColor(LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
    }
    
    //
    // Se implementa IComponenteConTextoPorDefecto.
    //

    @Override
    public void setTextoPorDefecto(String strTextoPorDefecto) {
        this.strTextoPorDefecto = strTextoPorDefecto;
        this.setForeground(LookAndFeelEntropy.COLOR_FUENTE_DESHABILITADA);
        boolean blnAgregar = true;
        for (FocusListener fl : getFocusListeners()) {
            if (fl instanceof FocusAdapterTextoEntropy) {
                blnAgregar = false;
                break;
            }
        }
        if (blnAgregar) {
            addFocusListener(new FocusAdapterTextoEntropy(this));
        }
    }

    @Override
    public String getTextoPorDefecto() {
        return strTextoPorDefecto;
    }

    @Override
    public void mostrarTextoPorDefecto() {
        super.setDocument(new FiltroTexto(FiltroTexto.TipoFiltro.TODO.toString()));
        setText(strTextoPorDefecto);
    }

    @Override
    public void setText(String strNuevoTexto) {
        if (strNuevoTexto == null) strNuevoTexto = "";
        if (!strTextoPorDefecto.isEmpty()) {
            if ((strNuevoTexto.isEmpty() || strNuevoTexto.equals(strTextoPorDefecto)) && !hasFocus()) {
                setForeground(LookAndFeelEntropy.COLOR_FUENTE_DESHABILITADA);
                strNuevoTexto = strTextoPorDefecto;
                if (filtroTexto != null) {
                    super.setDocument(new FiltroTexto(FiltroTexto.TipoFiltro.TODO.toString()));
                }
            } else {
                setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
                if (filtroTexto != null) {
                    super.setDocument(filtroTexto);
                }
            }
        }
        super.setText(strNuevoTexto);
    }

    @Override
    public void eliminarTextoPorDefecto() {
        this.strTextoPorDefecto = "";
        this.setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
        for (FocusListener fl : getFocusListeners()) {
            if (fl instanceof FocusAdapterTextoEntropy) {
                this.removeFocusListener(fl);
                break;
            }
        }
    }

    @Override
    public void setDocument(Document doc) {
        if (doc instanceof FiltroTexto) {
            this.filtroTexto = doc;
        }
        super.setDocument(doc);
    }
    
    @Override
    public String getText() {
        if (!this.strTextoPorDefecto.isEmpty() && super.getText().equals(strTextoPorDefecto)) {
            return "";
        }
        return super.getText();
    }
    
    @Override
    public boolean muestraTextoPorDefecto() {
        return super.getText().equals(strTextoPorDefecto);
    }
}
