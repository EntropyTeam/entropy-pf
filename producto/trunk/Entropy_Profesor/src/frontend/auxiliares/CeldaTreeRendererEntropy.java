package frontend.auxiliares;

import backend.diseños.Curso;
import backend.diseños.Institucion;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Clase que representa al renderer de un JTree con las características del 
 * tema Entropy.
 * 
 * @author Denise
 */
public class CeldaTreeRendererEntropy extends DefaultTreeCellRenderer {

    @Override
    public Color getBackgroundNonSelectionColor() {
        return (null);
    }

    @Override
    public Color getBackgroundSelectionColor() {
        return LookAndFeelEntropy.COLOR_TABLA_PRIMARIO;
    }

    @Override
    public Color getBackground() {
        return (null);
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode nodo =(DefaultMutableTreeNode) value;
        if (nodo.getLevel() > 0){
            Object objeto = ((DefaultMutableTreeNode) value).getUserObject();
            if (objeto instanceof Institucion){
                setIcon(GestorImagenes.crearImageIcon("/frontend/imagenes/ic_tree_i.png"));
            } else if (objeto instanceof Curso) {
                setIcon(GestorImagenes.crearImageIcon("/frontend/imagenes/ic_tree_c.png"));
            } else {
                setIcon(GestorImagenes.crearImageIcon("/frontend/imagenes/ic_punto_2.png"));            
            }
        } else {
            setIcon(null);
        }
        
        return this;
    }
    
}
