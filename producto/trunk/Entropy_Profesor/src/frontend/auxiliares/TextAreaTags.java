package frontend.auxiliares;

import backend.dao.diseños.IDAOTag;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Clase que representa un JTextArea que admite tags.
 *
 * @author Denise
 */
public class TextAreaTags extends TextAreaEntropy implements CaretListener {

    private final char SEPARADOR;
    private final JScrollPane scrLista;
    private final IDAOTag daoTags;
    private int intPosicionComaAnterior;
    private int intPosicionComaPosterior;
    private String strTagActual;
    private JPopupMenu pmnMenu;
    private JList lstMatches;

    /**
     * Constructor de la clase.
     *
     * @param frmVentana jframe o jdialog padre del pnlPadre.
     * @param pnlPadre contenedor del jtextarea
     * @param daoTags gestor dao de tags
     */
    public TextAreaTags(Container frmVentana, Container pnlPadre, IDAOTag daoTags) {
        /**
         * Inicialización de variables de clase.
         */
        this.strTagActual = "";
        this.SEPARADOR = ',';
        this.pmnMenu = new JPopupMenu();
        this.scrLista = new JScrollPane();
        this.lstMatches = new JList();
        this.daoTags = daoTags;

        /**
         * Configuración de componentes.
         */
        this.lstMatches.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        this.lstMatches.setCellRenderer(new CeldaListaRendererEntropy());
        this.lstMatches.setModel(new DefaultListModel());
        this.scrLista.setViewportView(lstMatches);

        /**
         * Listeners.
         */
        this.lstMatches.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1 && pmnMenu.isVisible()) {
                    seleccionar(lstMatches.getSelectedValue().toString());
                    ocultarPopMenu();
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (pmnMenu.isVisible()) {
                    if (e.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0).getKeyCode()) {
                        int index = lstMatches.getSelectedIndex();
                        if (index > 0) {
                            index--;
                            lstMatches.setSelectedIndex(index);
                            pmnMenu.repaint();
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0).getKeyCode()) {
                        int index = lstMatches.getSelectedIndex();
                        if (index < lstMatches.getModel().getSize() - 1) {
                            index++;
                            lstMatches.setSelectedIndex(index);
                            pmnMenu.repaint();
                        }
                        e.consume();
                    } else if (e.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0).getKeyCode()) {
                        seleccionar(lstMatches.getSelectedValue().toString());
                        ocultarPopMenu();
                        e.consume();
                    } else if (e.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0).getKeyCode()) {
                        ocultarPopMenu();
                        e.consume();
                    }
                }
            }
        });

        this.addCaretListener(this);

        frmVentana.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent evt) {
                ocultarPopMenu();
            }
        });

        pnlPadre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent evt) {
                ocultarPopMenu();
            }

            @Override
            public void componentResized(ComponentEvent evt) {
                ocultarPopMenu();
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                ocultarPopMenu();
                super.focusLost(e);
            }
        });
    }

    /**
     * Método encargado de volver a mostrar el popMenu en base al tag actual.
     */
    private void mostrarPopMenu() {

        if (lstMatches.getModel().getSize() == 0) {
            pmnMenu.setVisible(false);
            return;
        }

        pmnMenu.add(lstMatches);

        try {
            Rectangle posicion = modelToView(getCaretPosition());
            int x = getLocationOnScreen().x + 2;
            int y = getLocationOnScreen().y;
            pmnMenu.setLocation(x + posicion.x, y + posicion.y);
            pmnMenu.setVisible(true);
            grabFocus();

        } catch (Exception e) {
            System.err.println("Problema al graficar el PopMenu para los Tags:" + e.getMessage());
        }
    }

    /**
     * Calcula el tag actual a medida que éste va escribiéndose, o ante un
     * cambio de posición del cursor.
     */
    private void getTagActual() {
        intPosicionComaAnterior = -1;
        intPosicionComaPosterior = -1;
        String tags = getText();
        
        if (tags.isEmpty()) return;
        
        int posCursor = getCaretPosition() - 1;

        if (posCursor == -1 || tags.charAt(posCursor) == SEPARADOR) {
            strTagActual = "";
            pmnMenu.setVisible(false);
            lstMatches.setModel(new DefaultListModel());
            return;
        }

        for (int i = posCursor; i >= 0; i--) {
            if (tags.charAt(i) == SEPARADOR) {
                intPosicionComaAnterior = i;
                break;
            }
        }
        for (int i = posCursor; i < tags.length(); i++) {
            if (tags.charAt(i) == SEPARADOR) {
                intPosicionComaPosterior = i;
                break;
            }
        }

        if (intPosicionComaAnterior == -1) {
            intPosicionComaAnterior = 0;
        } else {
            intPosicionComaAnterior++;
        }

        if (intPosicionComaPosterior == -1) {
            intPosicionComaPosterior = tags.length();
        }

        if (intPosicionComaAnterior == posCursor) {
            this.strTagActual = tags.charAt(posCursor) + "";
        } else {
            this.strTagActual = tags.substring(intPosicionComaAnterior, posCursor + 1);
        }

        if (strTagActual.trim().isEmpty()) {
            pmnMenu.setVisible(false);
            lstMatches.setModel(new DefaultListModel());
            return;
        }

        DefaultListModel dlm = new DefaultListModel();
        for (String strMatch : daoTags.getTags(strTagActual.trim().toLowerCase())) {
            dlm.addElement(strMatch);
        }
        lstMatches.setModel(dlm);
        lstMatches.setSelectedIndex(0);
    }

    /**
     * Toma el tag seleccionado por click o enter de la lista desplegada en el
     * popmenu , y lo escribe en su correspondiente lugar en el textarea.
     *
     * @param strItemSeleccionado tag seleccionado
     */
    private void seleccionar(String strItemSeleccionado) {
        StringBuilder tags = new StringBuilder(getText());
        tags.replace(intPosicionComaAnterior, intPosicionComaPosterior, strItemSeleccionado);
        setText(tags.toString());
    }

    /**
     * Oculta el menú.
     */
    private void ocultarPopMenu() {
        pmnMenu.setVisible(false);
        lstMatches.setModel(new DefaultListModel());
    }

    //
    // Se implementa CaretListener.
    //
    @Override
    public void caretUpdate(CaretEvent e) {
        getTagActual();
        mostrarPopMenu();
    }

}
