package frontend.auxiliares;

import java.awt.KeyboardFocusManager;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * Clase que representa un JTextArea con la personalización del tema Entropy.
 * 
 * @author Denise
 */
public class TextAreaEntropy extends JTextArea implements IComponenteConTextoPorDefecto{

    private String strTextoPorDefecto;
    private Document filtroTexto;
    
    /**
     * Constructor de la clase.
     */
    public TextAreaEntropy() {
        initComponents();
    }
    
    /**
     * Constructor de la clase.
     * @param text texto a incluir en el componente
     */
    public TextAreaEntropy(String text) {
        super(text);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param rows cantidad de filas
     * @param columns cantidad de columnas
     */
    public TextAreaEntropy(int rows, int columns) {
        super(rows, columns);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param text texto a incluir en el componente
     * @param rows cantidad de filas
     * @param columns cantidad de columnas
     */
    public TextAreaEntropy(String text, int rows, int columns) {
        super(text, rows, columns);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param doc documento para el componente
     */
    public TextAreaEntropy(Document doc) {
        super(doc);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * @param doc documento para el componente
     * @param text texto a incluir en el componente
     * @param rows cantidad de filas
     * @param columns cantidad de columnas
     */
    public TextAreaEntropy(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        initComponents();
    }

    /**
     * Inicializa las características personalizadas.
     */
    private void initComponents() {
        strTextoPorDefecto = "";
        setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
        setLineWrap(true);
        setWrapStyleWord(true);
        setCaretColor(LookAndFeelEntropy.COLOR_CURSOR);
        setSelectionColor(LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                controlarFoco(evt);
            }
        });
    }
    
    /**
     * Maneja el paso del foco con las taclas TAB y Shift TAB. Éste método cobra
     * importancia para los componentes JTextArea.
     *
     * @param evt
     */
    private void controlarFoco(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_TAB && !evt.isShiftDown()) {
            evt.consume();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
        } else if (evt.getKeyCode() == KeyEvent.VK_TAB
                && evt.isShiftDown()) {
            evt.consume();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
        }
    }
    
    //
    // Se implementa IComponenteConTextoPorDefecto.
    //

    @Override
    public void setTextoPorDefecto(String strTextoPorDefecto) {
        this.strTextoPorDefecto = strTextoPorDefecto;
        this.setForeground(LookAndFeelEntropy.COLOR_FUENTE_DESHABILITADA);
        boolean blnAgregar = true;
        for (FocusListener fl : getFocusListeners()){
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
                if (filtroTexto != null)
                    super.setDocument(new FiltroTexto(FiltroTexto.TipoFiltro.TODO.toString()));
            } else {
                setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
                if (filtroTexto != null)
                    super.setDocument(filtroTexto);
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
