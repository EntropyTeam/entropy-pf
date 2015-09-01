package frontend.inicio;

import frontend.auxiliares.PanelDeslizante;
import javax.swing.JPanel;

/**
 *
 * @author Denise
 */
public interface IVentanaPrincipal {
    
    public PanelDeslizante getPanelDeslizante();
    public void ocultarMenu();
    public void volverAInicio();
    public JPanel getPnlInicio();
    public void setTitulo(String strTitulo);
    
}
