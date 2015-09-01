package frontend.resoluciones;

import backend.resoluciones.Resolucion;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.auxiliares.TextAreaEntropy;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Clase que representa el renderer multi línea de las celdas de una lista de
 * resoluciones, con un icono que indica si la resolución está completa o
 * parcialmente corregida.
 *
 * @author Denise
 */
public class CeldaListaResoluciones extends DefaultListCellRenderer {

    private final ImageIcon icnError;
    private final ImageIcon icnExito;
    private final TextAreaEntropy txaCelda;
    private Component padre;

    /**
     * Constructor por defecto.
     */
    public CeldaListaResoluciones() {
        padre = null;
        icnError = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_estado_advertencia.png");
        icnExito = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_estado_ok.png");
        txaCelda = new TextAreaEntropy();
        txaCelda.setEditable(false);
        txaCelda.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(final JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));

        JLabel lblIcono = new JLabel();
        lblIcono.setMaximumSize(new Dimension(icnError.getIconWidth(), icnError.getIconHeight()));
        lblIcono.setMinimumSize(new Dimension(icnError.getIconWidth(), icnError.getIconHeight()));
        lblIcono.setPreferredSize(new Dimension(icnError.getIconWidth(), icnError.getIconHeight()));

        if (value instanceof Resolucion) {
            if (((Resolucion) value).esCorreccionCompleta()) {
                lblIcono.setIcon(icnExito);
            } else {
                lblIcono.setIcon(icnError);
            }
        }

        if (padre == null) {
            padre = list.getParent();
            padre.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    list.setCellRenderer(new CeldaListaResoluciones());
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

        pnl.add(lblIcono);
        pnl.add(txaCelda);

        return pnl;
    }

}
