package frontend.auxiliares;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Clase encargada de manejar eventos de foco para implementaciones de
 * <code>IComponenteConTextoPorDefecto</code>.
 *
 * @author Denise
 */
public class FocusAdapterTextoEntropy extends FocusAdapter {

    private final IComponenteConTextoPorDefecto componente;

    /**
     * Constructor de la clase.
     *
     * @param componente IComponenteConTextoPorDefecto cuyos eventos serán
     * manejados.
     */
    public FocusAdapterTextoEntropy(IComponenteConTextoPorDefecto componente) {
        super();
        this.componente = componente;
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent evt) {
        super.focusGained(evt);
        if (componente.muestraTextoPorDefecto()) {
            componente.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);
        String aux = componente.getText();
        if (aux.isEmpty()) {
            componente.mostrarTextoPorDefecto();
        }
    }

}
