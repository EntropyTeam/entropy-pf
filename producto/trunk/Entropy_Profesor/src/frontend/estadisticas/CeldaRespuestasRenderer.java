package frontend.estadisticas;

import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Denise
 */
public class CeldaRespuestasRenderer implements TableCellRenderer{

    public static final Color INCORRECTO = new Color(255, 75, 75);
    public static final Color CORRECTO = new Color(118, 229, 155);
    public static final Color INCOMPLETO = new Color(249, 197, 106);
    public static final Color SIN_CALIFICAR = new Color(192, 191, 189);
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {        
        String strTexto = (value instanceof String) ? value.toString() : "";
        JLabel lbl = new JLabel(strTexto);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lbl.setOpaque(true);
        switch (strTexto){
            case "Anulada":
            case "Incorrecta":
                lbl.setBackground(INCORRECTO);
                break;
            case "Sin calificar":
                lbl.setBackground(INCORRECTO);
                break;
            case "Correcta":
                lbl.setBackground(CORRECTO);
                break;
            case "Incompleta":
                lbl.setBackground(INCOMPLETO);
                break;
        }
        return lbl;        
    }
    
}
