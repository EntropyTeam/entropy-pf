package frontend.auxiliares;

import javax.swing.JLabel;

/**
 * Clase que permite el manejo de paneles de estado.
 *
 * @author Denise
 */
public final class GestorBarrasDeEstado {

    private final JLabel lblActualizacionEstado;
    private final JLabel lblIconoEstado;
    private String strUltimoEstadoImportate;

    public static enum TipoEstado {

        ADVERTENCIA, OK
    }

    /**
     * Constructor de la clase.
     *
     * @param lblActualizacionEstado JLabel en la cual mostra los estados
     * @param lblIconoEstado JLabel en la cual mostrar el ícono de advertencia
     */
    public GestorBarrasDeEstado(JLabel lblActualizacionEstado, JLabel lblIconoEstado) {
        this.lblActualizacionEstado = lblActualizacionEstado;
        this.lblIconoEstado = lblIconoEstado;
        this.setNuevoEstadoImportante("");
    }

    /**
     * Muestra en la etiqueta de estado un nuevo estado importante, y lo
     * almacena como tal en la variable de la clase
     * <code>strUltimoEstadoImportante</code> para que sea mostrado hasta que
     * ocurra un nuevo estado importante.
     *
     * @param estado descripción del nuevo estado importante.
     */
    public void setNuevoEstadoImportante(String estado) {
        this.strUltimoEstadoImportate = estado;
        this.lblActualizacionEstado.setText(estado);
        if (this.lblIconoEstado != null) {
            this.lblIconoEstado.setIcon(null);
        }
    }

    /**
     * Muestra en la etiqueta de estado un nuevo estado importante, y lo
     * almacena como tal en la variable de la clase
     * <code>strUltimoEstadoImportante</code> para que sea mostrado hasta que
     * ocurra un nuevo estado importante.
     *
     * @param strEstado descripción del nuevo estado importante.
     * @param tipo variable del tipo TipoEstado
     */
    public void setNuevoEstadoImportante(String strEstado, TipoEstado tipo) {
        this.strUltimoEstadoImportate = strEstado;
        this.lblActualizacionEstado.setText(strEstado);
        if (tipo == TipoEstado.OK) {
            this.lblIconoEstado.setIcon(GestorImagenes.crearImageIcon("/frontend/imagenes/ic_estado_ok.png"));
        } else {
            this.lblIconoEstado.setIcon(GestorImagenes.crearImageIcon("/frontend/imagenes/ic_estado_advertencia.png"));
        }
    }

    /**
     * Muestra un nuevo estado en la etiqueta de estado, sin marcarlo como
     * último estado importante.
     *
     * @param strEstado nuevo estado instantáneo
     */
    public void setEstadoInstantaneo(String strEstado) {
        this.lblActualizacionEstado.setText(strEstado);
    }

    /**
     * Muestra el último estado importante en la etiqueta de estados.
     */
    public void volverAEstadoImportante() {
        this.lblActualizacionEstado.setText(this.strUltimoEstadoImportate);
    }

}
