package frontend.auxiliares;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Clase que representa el renderer multi línea de las celdas de una lista.
 *
 * @author Denise
 */
public class CeldaListaRendererEntropy extends DefaultListCellRenderer {

    private final TextAreaEntropy txaCelda;
    private Component padre;
    
    /**
     * Constructor por defecto.
     */
    public CeldaListaRendererEntropy() {
        padre = null;
        txaCelda = new TextAreaEntropy();
        txaCelda.setEditable(false);
        txaCelda.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(final JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        
        if (padre == null) {
            padre = list.getParent();
            padre.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    list.setCellRenderer(new CeldaListaRendererEntropy());
                    list.repaint();
                }
            });
        }

        if (isSelected) {
            txaCelda.setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
        } else {
            txaCelda.setBackground(Color.WHITE);
        }

        String strMostrar = value == null ? "" : value.toString();

        int intAnchoActual = list.getWidth();
        if (intAnchoActual != 0) {

            int intMaxCaracteres = 100;

            strMostrar = strMostrar.substring(0, strMostrar.length() <= intMaxCaracteres ? strMostrar.length() : intMaxCaracteres);
            if (strMostrar.length() == intMaxCaracteres) {
                strMostrar += "...";
            }

            int intAnchoDeseado = 0;
            int intCantidadLineas = 1;
            for (char c : strMostrar.toCharArray()) {
                intAnchoDeseado += txaCelda.getFontMetrics(txaCelda.getFont()).charWidth(c);
                if (intAnchoDeseado > intAnchoActual * intCantidadLineas) {
                    intCantidadLineas++;
                }
            }
            txaCelda.setRows(intCantidadLineas);
        }
        
        txaCelda.setText(strMostrar);
        
        return txaCelda;
    }

}
