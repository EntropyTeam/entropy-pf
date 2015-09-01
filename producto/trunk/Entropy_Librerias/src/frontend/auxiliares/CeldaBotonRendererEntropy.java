package frontend.auxiliares;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Clase para el manejo celdas botón en JTables.
 * 
 * @author Denise
 */
public class CeldaBotonRendererEntropy extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {

    private final JTable tblTabla;
    private final Action accion;
    private final Border bordeOriginal;
    private final Border bordeConFoco;
    private final JButton btnRenderer;
    private final JButton btnEditor;
    private Object valorEditor;
    private boolean esEditor;
    private boolean esConBorde;
    private Color colorSeleccion;
    private Color colorPrimario;
    private Color colorSecudario;

    /**
     * Crea el botón que será usado como render y como editor. Se setea como 
     * manejador predeterminado de la columna pasada por parámetro.
     *
     * ATENCIÓN! Requiere que la columna sea EDITABLE.
     * 
     * @param tblTabla tabla que posee la columna botón
     * @param accion la acción a ser invocada cuando se presiona el botón
     * @param intColumna el id de columna a la que pertenece el botón
     * @param imgBoton la imagen del botón
     * @param imgBotonPresionado la imagen del botón presionado
     */
    public CeldaBotonRendererEntropy(JTable tblTabla, Action accion, int intColumna, ImageIcon imgBoton, ImageIcon imgBotonPresionado) {
        this.tblTabla = tblTabla;
        this.accion = accion;
        colorSeleccion = LookAndFeelEntropy.COLOR_SELECCION_ITEM;
        colorPrimario = LookAndFeelEntropy.COLOR_TABLA_PRIMARIO;
        colorSecudario = LookAndFeelEntropy.COLOR_TABLA_SECUNDARIO;
        esConBorde = true;
        
        bordeOriginal = new EmptyBorder(1, 1, 1, 1);
        bordeConFoco = LookAndFeelEntropy.BORDE_NARANJA;
        
        btnRenderer = new JButton();
        btnRenderer.setContentAreaFilled(false);
        btnRenderer.setOpaque(true);
        btnRenderer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRenderer.setIcon(imgBoton);
        
        btnEditor = new JButton();
        btnEditor.setIcon(imgBotonPresionado);
        btnEditor.setContentAreaFilled(false);
        btnEditor.setOpaque(true);
        btnEditor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditor.setBorder(bordeConFoco);
        btnEditor.setFocusPainted(false);
        btnEditor.addActionListener(this);

        TableColumnModel modeloColumna = tblTabla.getColumnModel();
        modeloColumna.getColumn(intColumna).setCellRenderer(this);
        modeloColumna.getColumn(intColumna).setCellEditor(this);
        tblTabla.addMouseListener(this);
    }

    public boolean isEsConBorde() {
        return esConBorde;
    }

    public void setEsConBorde(boolean esConBorde) {
        this.esConBorde = esConBorde;
    }

    public Color getColorSeleccion() {
        return colorSeleccion;
    }

    public void setColorSeleccion(Color colorSeleccion) {
        this.colorSeleccion = colorSeleccion;
    }

    public Color getColorPrimario() {
        return colorPrimario;
    }

    public void setColorPrimario(Color colorPrimario) {
        this.colorPrimario = colorPrimario;
    }

    public Color getColorSecudario() {
        return colorSecudario;
    }

    public void setColorSecudario(Color colorSecudario) {
        this.colorSecudario = colorSecudario;
    }
    
    /**
     * Setea el atajo para activar el botón cuando la celda tiene el foco.
     *
     * @param intMnemonic el atajo
     */
    public void setMnemonic(int intMnemonic) {
        btnRenderer.setMnemonic(intMnemonic);
        btnEditor.setMnemonic(intMnemonic);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.valorEditor = value;
        return btnEditor;
    }

    @Override
    public Object getCellEditorValue() {
        return valorEditor;
    }

    //
    //  Se implementa TableCellRenderer.
    //
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (isSelected) {
            btnRenderer.setBackground(colorSeleccion);  
        } else {
            if (row%2 == 0) {
                btnRenderer.setBackground(colorPrimario);
            } else {
                btnRenderer.setBackground(colorSecudario);
            }
        }

        if (hasFocus) {
            btnRenderer.setBorder(bordeConFoco);
        } else {
            btnRenderer.setBorder(bordeOriginal);
        }
        
        return btnRenderer;
    }

    //
    //  Se implementa ActionListener.
    //
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int intFila = tblTabla.convertRowIndexToModel(tblTabla.getEditingRow());
        fireEditingStopped();
        ActionEvent evento = new ActionEvent(
                tblTabla,
                ActionEvent.ACTION_PERFORMED,
                "" + intFila);
        accion.actionPerformed(evento);
    }

    //
    //  Se implement MouseListener.
    //
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (tblTabla.isEditing() && tblTabla.getCellEditor() == this) {
            esEditor = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (esEditor && tblTabla.isEditing()) {
            tblTabla.getCellEditor().stopCellEditing();
        }

        esEditor = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

