package frontend.auxiliares;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Clase para el manejo celdas de texto con líneas múltiples en JTables.
 *
 * @author Denise
 */
public class CeldaMultiLineaRendererEntropy extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private final TextAreaEntropy txaRenderer;
    private final TextAreaEdicion txaEditor;
    private final Border bordeOriginal;
    private final Border bordeConFoco;
    private final JTable tblTabla;
    private final boolean blnHabilitarInsercion;

    /**
     * Constructor de la clase.
     *
     * @param tabla JTable a renderizarse.
     * @param blnHabilitarInsercion true si permite la inserción automática de
     * filas, false de lo contrario
     */
    public CeldaMultiLineaRendererEntropy(JTable tabla, boolean blnHabilitarInsercion) {
        this.tblTabla = tabla;
        this.tblTabla.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        this.blnHabilitarInsercion = blnHabilitarInsercion;

        bordeOriginal = new EmptyBorder(1, 1, 1, 1);
        bordeConFoco = new LineBorder(new Color(255, 102, 0));

        txaRenderer = new TextAreaEntropy();
        txaRenderer.setOpaque(true);
        txaRenderer.setEditable(false);
        txaRenderer.setBorder(bordeOriginal);

        txaEditor = new TextAreaEdicion();
        txaEditor.setOpaque(true);
        txaEditor.setEditable(true);
        txaEditor.setBorder(bordeConFoco);
    }

    //
    // Se implementa TableCellRenderer.
    //
    /**
     * Para setear las características de cada celda en una tabla
     *
     * @param table JTable a la que pertenece la celda
     * @param value Object el valor de la celda
     * @param isSelected boolean true si la celda está seleccionada
     * @param hasFocus boolean true si la celda posee el foco
     * @param row int índice de la fila a la que pertenece la celda
     * @param column int índice de la columna a la que pertenece la celda
     * @return el JTextArea a incluirse dentro de la celda
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            txaRenderer.setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        } else {
            if (row % 2 == 0) {
                txaRenderer.setBackground(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
            } else {
                txaRenderer.setBackground(LookAndFeelEntropy.COLOR_TABLA_SECUNDARIO);
            }
        }

        if (hasFocus) {
            txaRenderer.setBorder(bordeConFoco);
        } else {
            txaRenderer.setBorder(bordeOriginal);
        }
        if (blnHabilitarInsercion) {
            if (row == table.getRowCount() - 1) {
                txaRenderer.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
                txaRenderer.setForeground(LookAndFeelEntropy.COLOR_FUENTE_DESHABILITADA);
                if (value == null || value.toString().equals("")) {
                    value = "Ingrese nueva opción...";
                }
            } else {
                txaRenderer.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
                txaRenderer.setForeground(Color.BLACK);
            }
        }

        if (value instanceof String) {
            int intAltoCaracter = table.getFontMetrics(table.getFont()).getHeight() + 2;
            int intAnchoActual = (int) table.getCellRect(row, column, false).getWidth();
            int intAnchoDeseado = 0;
            int intCantidadLineas = 1;
            for (char c : value.toString().toCharArray()) {
                intAnchoDeseado += table.getFontMetrics(table.getFont()).charWidth(c);
                if (intAnchoDeseado > intAnchoActual * intCantidadLineas) {
                    intCantidadLineas++;
                }
            }
            int intNuevaAltura = intAltoCaracter * intCantidadLineas;
            if (intNuevaAltura > table.getRowHeight(row)) {
                table.setRowHeight(row, intNuevaAltura);
            }
        }

        txaRenderer.setText((value == null) ? "" : value.toString());

        return txaRenderer;
    }

    //
    // Se implementa TableCellEditor.
    //
    @Override
    public Object getCellEditorValue() {
        return txaEditor.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if (value instanceof String) {
            txaEditor.intFilaEnEdicion = row;
            txaEditor.intColumnaEnEdicion = column;
        }

        txaEditor.setText((value == null) ? "" : value.toString());

        return txaEditor;
    }
    
    /**
     * Clase interna para el manejo de la edición multilínea.
     */
    private class TextAreaEdicion extends TextAreaEntropy {

        int intFilaEnEdicion;
        int intColumnaEnEdicion;

        /**
         * Constructor por defecto de la clase interna.
         */
        public TextAreaEdicion() {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    actualizarAlturaFila();
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_TAB && !e.isShiftDown()) {
                        e.consume();
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    } else if (e.getKeyCode() == KeyEvent.VK_TAB && e.isShiftDown()) {
                        e.consume();
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isAltDown()){
                        e.consume();
                        tblTabla.getCellEditor().stopCellEditing();
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown()){
                        txaEditor.append("\n");
                        e.consume();
                    }
                }
            });
            addAncestorListener(new AncestorListener() {
                @Override
                public void ancestorAdded(AncestorEvent e) {
                    requestFocus();
                }

                @Override
                public void ancestorMoved(AncestorEvent e) {
                }

                @Override
                public void ancestorRemoved(AncestorEvent e) {
                }
            });
        }

        /**
         * Actualiza el alto de la fila.
         */
        private void actualizarAlturaFila() {
            int intAlturaMaxima = 0;
            for (int j = 0; j < tblTabla.getColumnCount(); j++) {
                int intAlturaAuxiliar;
                if ((intAlturaAuxiliar = getPreferredSize().height) > intAlturaMaxima) {
                    intAlturaMaxima = intAlturaAuxiliar;
                }
            }
            tblTabla.setRowHeight(intFilaEnEdicion, intAlturaMaxima);
        }
    }
}
