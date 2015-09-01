package frontend.auxiliares;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Clase que representa al panel que desliza otros paneles en su interior.
 *
 * @author Denise
 */
public class PanelDeslizante extends JPanel {

    /**
     * Constructor por defecto.
     */
    public PanelDeslizante() {
    }

    /**
     * Cambia el panel mostrado.
     *
     * @param panelSiguiente panel a mostrar
     */
    public void setPanelMostrado(JPanel panelSiguiente) {
        add(panelSiguiente, panelSiguiente.getName());
        deslizarPanel(10, panelSiguiente, true);
        refrescar();
    }

    /**
     * Cambia el panel mostrado.
     *
     * @param panelSiguiente panel a mostrar
     * @param blnDireccion dirección del movimiento, true: hacia la derecha,
     * false: hacia la izquierda
     */
    public void setPanelMostrado(JPanel panelSiguiente, boolean blnDireccion) {
        add(panelSiguiente, panelSiguiente.getName());
        deslizarPanel(10, panelSiguiente, blnDireccion);
        refrescar();
    }

    /**
     * Determina si un panel pasado por parámetro se muestra actualmente.
     *
     * @param panel panel a corroborar
     * @return true si es mostrado falso de lo contrario
     */
    public boolean esPanelMostrado(JPanel panel) {
        return getNombreComponenteActual(this).equals(panel.getName());
    }

    /**
     * Desliza un nuevo panel dentro de éste.
     *
     * @param panelSiguiente panel a mostrarse
     */
    public void deslizarPanel(Component panelSiguiente) {
        deslizarPanel(10, 40, panelSiguiente, true);
    }

    /**
     * Desliza un nuevo panel dentro de éste.
     *
     * @param intVelocidad velocidad de deslizamiento
     * @param panelSiguiente panel a mostrarse
     */
    public void deslizarPanel(int intVelocidad, Component panelSiguiente) {
        deslizarPanel(intVelocidad, 40, panelSiguiente, true);
    }

    /**
     * Desliza un nuevo panel dentro de éste.
     *
     * @param intVelocidad velocidad de deslizamiento
     * @param panelSiguiente panel a mostrarse
     * @param blnDireccion dirección del movimiento, true: hacia la derecha,
     * false: hacia la izquierda
     */
    public void deslizarPanel(int intVelocidad, Component panelSiguiente, boolean blnDireccion) {
        deslizarPanel(intVelocidad, 40, panelSiguiente, blnDireccion);
    }

    /**
     * Desliza un nuevo panel dentro de éste.
     *
     * @param intVelocidad velocidad de deslizamiento
     * @param intTiempo intervalo para el inicio y activación del temporizador
     * en milisegundos
     * @param panelSiguiente panel a mostrarse
     * @param blnDireccion dirección del movimiento, true: hacia la derecha,
     * false: hacia la izquierda
     */
    public void deslizarPanel(int intVelocidad, int intTiempo, Component panelSiguiente, boolean blnDireccion) {
        Component componenteActual = getComponenteActual(this);
        if (componenteActual instanceof IValidarSalida) {
            if (((IValidarSalida) componenteActual).validarSalida().equals(IValidarSalida.TipoAccion.CONTINUAR)) {
                if (panelSiguiente.getName().equals(componenteActual.getName()))
                    intTiempo = 0;
                panelSiguiente.setVisible(true);
                PanelDeslizanteListener listener = new PanelDeslizanteListener(intVelocidad, componenteActual, panelSiguiente, blnDireccion);
                Timer nuevoTemporizador = new Timer(intTiempo, listener);
                listener.temporizador = nuevoTemporizador;
                nuevoTemporizador.start();
            }
        } else if (!panelSiguiente.getName().equals(componenteActual.getName())) {
            panelSiguiente.setVisible(true);
            PanelDeslizanteListener listener = new PanelDeslizanteListener(intVelocidad, componenteActual, panelSiguiente, blnDireccion);
            Timer nuevoTemporizador = new Timer(intTiempo, listener);
            listener.temporizador = nuevoTemporizador;
            nuevoTemporizador.start();
        }
    }

    /**
     * Devuelve el componente que actulmente se muestra.
     *
     * @param padre instancia contenedora de los componentes mostrados
     * @return el componente actualmente mostrado, null si no existe
     */
    public Component getComponenteActual(Container padre) {
        Component componente = null;
        int intCantidadComponentes = padre.getComponentCount();
        for (int i = 0; i < intCantidadComponentes; i++) {
            componente = padre.getComponent(i);
            if (componente.isVisible()) {
                return componente;
            }
        }
        return componente;
    }

    /**
     * Devuelve el nombre del componente que actulmente se muestra.
     *
     * @param padre instancia contenedora de los componentes mostrados
     * @return el nombre del componente actualmente mostrado, null si no existe
     */
    public String getNombreComponenteActual(Container padre) {
        String strNombrePanel = null;
        Component componente;
        int intCantidadComponentes = padre.getComponentCount();
        for (int i = 0; i < intCantidadComponentes; i++) {
            componente = padre.getComponent(i);
            if (componente.isVisible()) {
                strNombrePanel = componente.getName();
                return strNombrePanel;
            }
        }
        return strNombrePanel;
    }

    /**
     * Clase encargada de deslizar el panel.
     */
    public class PanelDeslizanteListener implements ActionListener {

        Component panelAOcultar;
        Component panelAMostrar;
        int intVelocidad;
        int intFraccion = 0;
        Timer temporizador;
        boolean blnEsProximo;

        /**
         * Constructor de la clase.
         *
         * @param intVelocidad velocidad de deslizamiento
         * @param panelAnterior panel a ocultarse
         * @param panelSiguiente panel a mostrarse
         * @param blnDireccion direccion del movimiento, true: hacia la derecha,
         * false: hacia la izquierda
         */
        public PanelDeslizanteListener(int intVelocidad, Component panelAnterior, Component panelSiguiente, boolean blnDireccion) {
            this.intVelocidad = intVelocidad;
            this.panelAOcultar = panelAnterior;
            this.panelAMostrar = panelSiguiente;
            this.blnEsProximo = blnDireccion;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Rectangle bounds = panelAOcultar.getBounds();
            int shift = bounds.width / intVelocidad;
            if (!blnEsProximo) {
                panelAOcultar.setLocation(bounds.x - shift, bounds.y);
                panelAMostrar.setLocation(bounds.x - shift + bounds.width, bounds.y);

            } else {
                panelAOcultar.setLocation(bounds.x + shift, bounds.y);
                panelAMostrar.setLocation(bounds.x + shift - bounds.width, bounds.y);

            }

            repaint();
            intFraccion++;

            if (intFraccion == intVelocidad) {
                temporizador.stop();
                panelAOcultar.setVisible(false);

            }

        }

    }

    /**
     * Actualiza la pantalla.
     */
    public void refrescar() {
        this.revalidate();
        this.repaint();
    }
}
