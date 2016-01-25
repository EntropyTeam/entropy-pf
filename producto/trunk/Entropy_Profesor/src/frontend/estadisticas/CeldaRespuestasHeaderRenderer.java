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
public class CeldaRespuestasHeaderRenderer extends JLabel implements TableCellRenderer {
    
    public CeldaRespuestasHeaderRenderer() {
        setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        setOpaque(true);
        setForeground(Color.DARK_GRAY);
        setBackground(UIManager.getColor("TableHeader.background"));
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.TOP);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        int intAlturaAuxiliar = 0;
        int intAnchoAuxiliar = table.getColumnModel().getColumn(column).getPreferredWidth();
        
        Enumeration enumeration = table.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn tcol = ((TableColumn) enumeration.nextElement());
            Object value1 = tcol.getHeaderValue();
            int intAltoCaracter = table.getFontMetrics(table.getFont()).getHeight() + 2;
            int intAnchoActual = (int) table.getCellRect(row, column, false).getWidth();
            int intAnchoCaracteres = 0;
            int intCantidadLineas = 1;
            for (char c : value1.toString().toCharArray()) {
                intAnchoCaracteres += table.getFontMetrics(table.getFont()).charWidth(c);
                if (intAnchoCaracteres > intAnchoActual * intCantidadLineas) {
                    intCantidadLineas++;
                }
            }
            if (intCantidadLineas > 6) {
                int intNuevoAncho = intAnchoCaracteres / 6;
                if (intNuevoAncho > intAnchoAuxiliar) {
                    intAnchoAuxiliar = intNuevoAncho;
                }
                intCantidadLineas = 6;
                table.getColumnModel().getColumn(column).setPreferredWidth(intAnchoAuxiliar);
            }
            int intNuevaAltura = intAltoCaracter * intCantidadLineas;
            if (intNuevaAltura > intAlturaAuxiliar) {
                intAlturaAuxiliar = intNuevaAltura;
            }            
        }
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        try{
            setPreferredSize(new Dimension(intAnchoAuxiliar, intAlturaAuxiliar));
            setText((value == null) ? "" : ("<html><center>" + value.toString() + "</center></html>"));
        } catch(Exception e){
        }
        return this;
    }
    
}
