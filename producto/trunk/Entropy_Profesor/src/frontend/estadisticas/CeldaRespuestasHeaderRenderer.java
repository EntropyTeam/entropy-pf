package frontend.estadisticas;

import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Denise
 */
public class CeldaRespuestasHeaderRenderer extends JLabel implements TableCellRenderer{

    public CeldaRespuestasHeaderRenderer() {
        setOpaque(false);
        setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        setOpaque(true);
        setForeground(Color.DARK_GRAY);
        //setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        setBackground(UIManager.getColor("TableHeader.background"));
        //setBackground(Color.LIGHT_GRAY);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.TOP);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
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
            
        setText((value == null) ? "" : ("<html><center>" + value.toString() + "</center></html>"));
        return this;
    }
    
    
}
