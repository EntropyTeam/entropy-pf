package frontend.presentaciones;

import backend.presentaciones.EstadoPresentacion;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Clase que representa al renderer para las celdas de la tabla en el panel de 
 * control de la toma de exámenes. Encargada de devolver los componentes 
 * adecuados.
 * 
 * @author Denise
 */
public class CeldaPresentacionRenderer implements TableCellRenderer {

    private static final ImageIcon imgAutenticado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_autenticado_35px.png");
    private static final ImageIcon imgConectado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_examen_terminado_35px.png");
    private static final ImageIcon imgDesconectado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_no_autenticado_35px.png");
    private static final ImageIcon imgInterrumpido = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_anulado_35px.png");

    /**
     * Constructor de la clase.
     */
    public CeldaPresentacionRenderer() {
        UIManager.put("ProgressBar.selectionBackground", LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        UIManager.put("ProgressBar.selectionForeground", LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lbl = new JLabel();
        table.setRowHeight(36);
        
        switch (table.getColumnName(column)){
            case "Alumno":
                lbl.setOpaque(true);
                lbl.setFont(LookAndFeelEntropy.FUENTE_TITULO);
                lbl.setHorizontalAlignment(JLabel.LEADING);
                lbl.setVerticalAlignment(JLabel.CENTER);
                if (isSelected) {
                    lbl.setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
                } else {
                    if (row % 2 == 0) {
//                        lbl.setBackground(new Color(204, 204, 204, 123));
                        lbl.setBackground(new Color(220, 220, 220));
                    } else {
//                        lbl.setBackground(new Color(204, 204, 204, 50));
                        lbl.setBackground(new Color(235, 235, 235));
                    }
                }
                lbl.setBorder(null);
                int index = table.getColumn("Estado").getModelIndex();
                if(table.getModel().getValueAt(row, index) != null){
                    lbl.setIcon(getImagenEstado(table.getModel().getValueAt(row, index).toString()));
                }
                lbl.setText((value == null) ? "" : value.toString());
                return lbl;
            default:
                lbl.setOpaque(true);
                lbl.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setVerticalAlignment(JLabel.CENTER);
                if (isSelected) {
                    lbl.setBackground(LookAndFeelEntropy.COLOR_SELECCION_ITEM);
                } else {
                    if (row % 2 == 0) {
//                        lbl.setBackground(new Color(204, 204, 204, 123));
                        lbl.setBackground(new Color(220, 220, 220));
                    } else {
//                        lbl.setBackground(new Color(204, 204, 204, 50));
                        lbl.setBackground(new Color(235, 235, 235));
                    }
                }
                lbl.setBorder(null);
                lbl.setText((value == null) ? "" : value.toString());
                return lbl;
        }
    }

    /**
     * Carga las imágenes que representan a los estados de los usuarios.
     * 
     * @param strEstadoExamen estado cuya imagen busca cargarse.
     * @return la imagen del estado Icon.
     */
    private Icon getImagenEstado(String strEstadoExamen) {
        if (strEstadoExamen.equals(EstadoPresentacion.AUTENTICADO.toString())) {
            return imgAutenticado;
        } else if (strEstadoExamen.equals(EstadoPresentacion.CONECTADO.toString())) {
            return imgConectado;
        } else if (strEstadoExamen.equals(EstadoPresentacion.DESCONECTADO.toString())) {
            return imgDesconectado;
        } else if (strEstadoExamen.equals(EstadoPresentacion.INTERRUMPIDO.toString())) {
            return imgInterrumpido;
        }
        return null;
    }

}
