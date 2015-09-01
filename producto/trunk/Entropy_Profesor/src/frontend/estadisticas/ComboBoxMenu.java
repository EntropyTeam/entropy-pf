package frontend.estadisticas;

import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * Clase que representa a un combobox que se utiliza como menú expansible.
 * 
 * @author Denise
 */
public class ComboBoxMenu extends JComboBox<Object> {

    private JTextField txtEditorCombo;
    private final String strTextoPorDefecto = "Otras estadísticas";

    public ComboBoxMenu() {
        initComponents();
    }

    public ComboBoxMenu(ComboBoxModel<Object> aModel) {
        super(aModel);
        initComponents();
    }

    public ComboBoxMenu(Object[] items) {
        super(items);
        initComponents();
    }

    public ComboBoxMenu(Vector<Object> items) {
        super(items);
        initComponents();
    }

    private void initComponents() {
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new JButton() {
                    @Override
                    public int getWidth() {
                        return 0;
                    }
                };
            }
        });
        setRenderer(new BotonComboMenuRenderer());
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setEditable(true);
        txtEditorCombo = (JTextField) getEditor().getEditorComponent();
        txtEditorCombo.setHorizontalAlignment(JTextField.CENTER);
        txtEditorCombo.setText(strTextoPorDefecto);
        txtEditorCombo.setEditable(false);
        txtEditorCombo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtEditorCombo.setFont(LookAndFeelEntropy.FUENTE_TITULO);
        txtEditorCombo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        txtEditorCombo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        showPopup();
                    }
                });
            }
        });
        this.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.err.println("FALTA IMPLEMENTAR CLASE COMBOBOXMENU");
                }
            }
        });
        for (int i = 0;; i++) {
            if (getUI().getAccessibleChild(this, i) instanceof JPopupMenu) {
                JPopupMenu menu = ((JPopupMenu) getUI().getAccessibleChild(this, i));
                menu.setBorder(new EmptyBorder(10, 10, 10, 10));
                menu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                break;
            }
        }
    }

    @Override
    public void configureEditor(ComboBoxEditor anEditor, Object anItem) {
        super.configureEditor(anEditor, strTextoPorDefecto);
    }
    
    class BotonComboMenuRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            BotonMenu btnMenu = new BotonMenu((value != null) ? value.toString() : "");
            if (isSelected) {
                btnMenu.setFont(LookAndFeelEntropy.FUENTE_TITULO);
                btnMenu.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
                btnMenu.setBorder((new javax.swing.border.LineBorder(new java.awt.Color(204, 102, 0), 2, true)));
            }
            return btnMenu;
        }

    }

    class BotonMenu extends JButton {

        public BotonMenu() {
            initComponents();
        }

        public BotonMenu(String text) {
            super(text);
            initComponents();
        }

        private void initComponents() {
            setFont(LookAndFeelEntropy.FUENTE_REGULAR);
            setBorder(javax.swing.BorderFactory.createEtchedBorder());
            setContentAreaFilled(false);
            setMinimumSize(new Dimension(30, 30));
            setPreferredSize(new Dimension(30, 30));
            setIconTextGap(10);
        }
    }
}
