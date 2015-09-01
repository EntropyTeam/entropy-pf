package frontend.auxiliares;

import java.awt.event.FocusListener;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * Clase que representa un JComboBox con la personalización del tema Entropy.
 * 
 * @author Denise
 */
public class ComboBoxEntropy extends JComboBox<Object> implements IComponenteConTextoPorDefecto{
    
    private String strTextoPorDefecto;
    private JTextField txtEditorCombo;

    /**
     * Contructor de la clase.
     * 
     * @param aModel modelo
     */
    public ComboBoxEntropy(ComboBoxModel<Object> aModel) {
        super(aModel);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * 
     * @param items array de objetos ítems
     */
    public ComboBoxEntropy(Object[] items) {
        super(items);
        initComponents();
    }

    /**
     * Constructor de la clase.
     * 
     * @param items vector de objetos ítems
     */
    public ComboBoxEntropy(Vector<Object> items) {
        super(items);
        initComponents();
    }

    /**
     * Constructor por defecto de la clase.
     */
    public ComboBoxEntropy() {
        initComponents();
    }

    /**
     * Inicializa las características personalizadas.
     */
    private void initComponents() {
        strTextoPorDefecto = "";
        setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        setEditable(true);
        setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        setRenderer(new CeldaListaRendererEntropy());
        setRequestFocusEnabled(true);
        txtEditorCombo = (JTextField) getEditor().getEditorComponent();
    }

    //
    // Se implementa IComponenteConTextoPorDefecto.
    //
    
    @Override
    public void setTextoPorDefecto(String strTextoPorDefecto) {
        this.strTextoPorDefecto = strTextoPorDefecto;
        boolean blnAgregar = true;
        for (FocusListener fl : txtEditorCombo.getFocusListeners()) {
            if (fl instanceof FocusAdapterTextoEntropy) {
                blnAgregar = false;
                break;
            }
        }
        if (blnAgregar) {
            txtEditorCombo.addFocusListener(new FocusAdapterTextoEntropy(this));
        }
        txtEditorCombo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_DESHABILITADA);
        txtEditorCombo.setCaretColor(LookAndFeelEntropy.COLOR_CURSOR);
        txtEditorCombo.setSelectionColor(LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
    }

    @Override
    public String getTextoPorDefecto() {
        return strTextoPorDefecto;
    }

    @Override
    public void mostrarTextoPorDefecto() {
        setText(strTextoPorDefecto);
    }

    @Override
    public String getText() {
        if (!this.strTextoPorDefecto.isEmpty() && txtEditorCombo.getText().equals(strTextoPorDefecto)) {
            return "";
        }
        return txtEditorCombo.getText();
    }

    @Override
    public void setText(String strTexto) {
        if (strTexto == null) strTexto = "";
        if (!strTextoPorDefecto.isEmpty()) {
            if ((strTexto.isEmpty() || strTexto.equals(strTextoPorDefecto)) && !txtEditorCombo.hasFocus()) {
                txtEditorCombo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_DESHABILITADA);
                strTexto = strTextoPorDefecto;
            } else {
                txtEditorCombo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
            }
            txtEditorCombo.setText(strTexto);
        }
    }

    @Override
    public void eliminarTextoPorDefecto() {
        this.strTextoPorDefecto = "";
        for (FocusListener fl : txtEditorCombo.getFocusListeners()) {
            if (fl instanceof FocusAdapterTextoEntropy) {
                txtEditorCombo.removeFocusListener(fl);
                break;
            }
        }
    }
    
    @Override
    public boolean muestraTextoPorDefecto() {
        return txtEditorCombo.getText().equals(strTextoPorDefecto);
    }

    @Override
    public void setSelectedIndex(int anIndex) {
        super.setSelectedIndex(anIndex);
        if (!strTextoPorDefecto.isEmpty() && anIndex != -1) {
            txtEditorCombo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
        }
    }

    @Override
    public void setSelectedItem(Object anObject) {
        super.setSelectedItem(anObject);
        if (!strTextoPorDefecto.isEmpty() && anObject != null && !anObject.toString().equals(strTextoPorDefecto)) {
            txtEditorCombo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_HABILITADA);
        }
    }
    
}
