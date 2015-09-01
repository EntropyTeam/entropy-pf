package frontend.auxiliares;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Denise
 */
public class CeldaHeaderMultiLineRenderer extends JLabel implements TableCellRenderer {

    public CeldaHeaderMultiLineRenderer() {
        setOpaque(false);
        setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        setBackground(UIManager.getColor("TableHeader.background"));
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.TOP);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        int intAlturaAuxiliar = 0;
        Enumeration enumeration = table.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn tcol = ((TableColumn) enumeration.nextElement());
            Object value1 = tcol.getHeaderValue();
            int intAltoCaracter1 = table.getFontMetrics(table.getFont()).getHeight() + 2;
            int intAnchoActual1 = (int) table.getCellRect(row, column, false).getWidth();
//            int intAnchoActual1 = tcol.getPreferredWidth();
            int intAnchoDeseado1 = 0;
            int intCantidadLineas1 = 1;
            for (char c : value1.toString().toCharArray()) {
                intAnchoDeseado1 += table.getFontMetrics(table.getFont()).charWidth(c);
                if (intAnchoDeseado1 > intAnchoActual1 * intCantidadLineas1) {
                    intCantidadLineas1++;
                }
            }
            int intNuevaAltura1 = intAltoCaracter1 * intCantidadLineas1;
            if (intNuevaAltura1 > intAlturaAuxiliar) {
                intAlturaAuxiliar = intNuevaAltura1;
            }
        }
            table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getPreferredWidth(), intAlturaAuxiliar));
//            table.getTableHeader().setPreferredSize(new Dimension((int) table.getCellRect(row, column, false).getWidth(), intAlturaAuxiliar));
            
        setText((value == null) ? "" : ("<html><center>" + value.toString() + "</center></html>"));
        return this;
    }

}
