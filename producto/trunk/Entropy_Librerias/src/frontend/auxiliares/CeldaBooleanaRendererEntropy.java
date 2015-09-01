package frontend.auxiliares;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

/**
 * Clase para el manejo celdas booleanas en JTables.
 *
 * @author Denise
 */
public class CeldaBooleanaRendererEntropy extends JCheckBox implements TableCellRenderer {

    /**
     * Constructor por defecto.
     */
    public CeldaBooleanaRendererEntropy() {
        setHorizontalAlignment(CENTER);
        setOpaque(true);
        setBorderPainted(true);
    }

    //
    // Se implementa TableCellRenderer.
    //
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        } else {
            if (row % 2 == 0) {
                setBackground(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
            } else {
                setBackground(LookAndFeelEntropy.COLOR_TABLA_SECUNDARIO);
            }
        }

        if (hasFocus) {
            setBorder(new LineBorder(new Color(255, 102, 0)));
        } else {
            setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        if (value instanceof Boolean) {
            setSelected((Boolean) value);
        } else {
            setSelected(false);
        }
        
        return this;

    }

}
