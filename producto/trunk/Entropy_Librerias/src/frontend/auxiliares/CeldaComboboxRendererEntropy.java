package frontend.auxiliares;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Clase que representa al renderer para mostrar un combobox en una tabla. Al
 * seleccionar un item en un combobox, el item deja de estar disponible en los
 * demás comboboxes.
 *
 * @author Denise
 */
public class CeldaComboboxRendererEntropy extends DefaultCellEditor implements TableCellRenderer {

    private ComboBoxEntropy cmbRenderer, cmbEditor;
    private TableColumn columna;
    private static Object[] listaCompleta;
    private static ArrayList disponibles;
    private Object actual;
    private final JTable tabla;

    /**
     * Constructor de la clase.
     *
     * @param tabla tabla que contiene las opciones de columnas.
     * @param columna columna que tiene los comboboxes.
     * @param cmbEditor botón a mostrarse al editar.
     * @param listaCompleta vector con la lista completa de las posibles
     * opciones a mostrar.
     * @param blnEsEditable true si el combobox debe poder editarse, false de lo
     * contrario
     */
    public CeldaComboboxRendererEntropy(final JTable tabla, TableColumn columna, ComboBoxEntropy cmbEditor, Object[] listaCompleta, boolean blnEsEditable) {
        super(cmbEditor);
        this.columna = columna;
        this.tabla = tabla;
        CeldaComboboxRendererEntropy.listaCompleta = listaCompleta;
        CeldaComboboxRendererEntropy.disponibles = new ArrayList();
        disponibles.addAll(Arrays.asList(listaCompleta));

        this.cmbEditor = cmbEditor;
        this.cmbEditor.setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        this.cmbEditor.setEditable(false);

        this.cmbRenderer = new ComboBoxEntropy(listaCompleta);
        this.cmbRenderer.setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        this.cmbRenderer.setEditable(blnEsEditable);

        tabla.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    calcularOpcionesDisponibles();
                }
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnl.add(cmbRenderer);

        if (row % 2 == 0) {
            pnl.setBackground(new Color(204, 204, 204, 123));
        } else {
            pnl.setBackground(new Color(204, 204, 204, 50));
        }

        cmbRenderer.setPreferredSize(
                new Dimension(
                        columna.getWidth() - 10,
                        table.getRowHeight(row) - 10));

        cmbRenderer.setSelectedItem(value);

        if (cmbRenderer.getSelectedIndex() == -1) {
            cmbRenderer.mostrarTextoPorDefecto();
        }

        return pnl;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnl.add(cmbEditor);

        actual = ((DefaultTableModel) tabla.getModel()).getValueAt(row, 1);
        cmbEditor.removeAllItems();
        boolean añadir = true;
        for (Object obj : disponibles) {
            cmbEditor.addItem(obj);
            if (añadir) {
                añadir = !actual.equals(obj);
            }
        }
        if (añadir) {
            cmbEditor.addItem(actual);
            cmbEditor.setSelectedItem(actual);
        } else {
            actual = null;
            cmbEditor.setSelectedItem(value);
        }

        cmbEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (actual != null) {
                    cmbEditor.removeItem(actual);
                }
            }
        });

        if (row % 2 == 0) {
            pnl.setBackground(new Color(204, 204, 204, 123));
        } else {
            pnl.setBackground(new Color(204, 204, 204, 50));
        }

        cmbEditor.setPreferredSize(
                new Dimension(
                        columna.getWidth() - 10,
                        table.getRowHeight(row) - 10));

        return pnl;
    }

    public void setCmbEditor(ComboBoxEntropy cmbEditor) {
        this.cmbEditor = cmbEditor;
    }

    public ComboBoxEntropy getCmbEditor() {
        return this.cmbEditor;
    }

    /**
     * Cuando una opción es seleccionada en un combobox, actualiza las opciones
     * que deben mostrarse en el resto;
     *
     * @param e evento que origina el cambio.
     */
    private void calcularOpcionesDisponibles() {
        ArrayList usados = new ArrayList<>();
        disponibles = new ArrayList();
        disponibles.add(listaCompleta[0]);
        for (int i = 0; i < tabla.getRowCount(); i++) {
            Object value = ((DefaultTableModel) tabla.getModel()).getValueAt(i, 1);
            usados.add(value);
        }
        for (Object obj : listaCompleta) {
            if (obj.equals(listaCompleta[0])) {
                continue;
            }
            if (!usados.contains(obj)) {
                disponibles.add(obj);
            }
        }
    }
}
