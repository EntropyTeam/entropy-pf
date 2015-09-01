package frontend.tomaexamenes;

import backend.examenes.EstadoTomaExamen;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
public class CeldaTomarExamenRenderer implements TableCellRenderer {

    private static final ImageIcon imgNoAutenticado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_no_autenticado_35px.png");
    private static final ImageIcon imgAutenticado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_autenticado_35px.png");
    private static final ImageIcon imgIniciado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_examen_iniciado_35px.png");
    private static final ImageIcon imgCompletado = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_examen_terminado_35px.png");
    private static final ImageIcon imgInterrumpido = GestorImagenes.crearImageIcon("/frontend/imagenes/ic_usuario_anulado_35px.png");

    /**
     * Constructor de la clase.
     */
    public CeldaTomarExamenRenderer() {
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
            case "Progreso":
                JPanel pnl = new JPanel();
                pnl.setLayout(new FlowLayout(FlowLayout.CENTER));
                if (row % 2 == 0) {
//                        pnl.setBackground(new Color(204, 204, 204, 123));
                        pnl.setBackground(new Color(220, 220, 220));
                    } else {
//                        pnl.setBackground(new Color(204, 204, 204, 50));
                        pnl.setBackground(new Color(235, 235, 235));
                    }
                JProgressBar bar = new JProgressBar(0, 100);
                bar.setPreferredSize(
                        new Dimension(
                                table.getColumn("Progreso").getWidth()-10,
                                25));
                bar.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
                bar.setBorder(LookAndFeelEntropy.BORDE_NARANJA);
                bar.setBackground(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
                bar.setForeground(Color.ORANGE);
                bar.setStringPainted(true);
                bar.setValue((value == null) ? 0 : Integer.parseInt(value.toString()));
                pnl.add(bar);
                return pnl;
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
        if (strEstadoExamen.equals(EstadoTomaExamen.NO_AUTENTICADO.toString())) {
            return imgNoAutenticado;
        } else if (strEstadoExamen.equals(EstadoTomaExamen.AUTENTICADO.toString())) {
            return imgAutenticado;
        } else if (strEstadoExamen.equals(EstadoTomaExamen.INICIADO.toString())) {
            return imgIniciado;
        } else if (strEstadoExamen.equals(EstadoTomaExamen.COMPLETADO.toString())) {
            return imgCompletado;
        } else if (strEstadoExamen.equals(EstadoTomaExamen.INTERRUMPIDO.toString())) {
            return imgInterrumpido;
        }
        return null;
    }

}
