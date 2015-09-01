package backend.resoluciones;

import backend.diseños.Pregunta;
import java.io.Serializable;

/**
 *
 * @author gaston
 */
public class RespuestaCombinacionRelacionColumnas extends Respuesta implements Serializable {

    private int intOrden;
    private String strColumnaIzquierda;
    private String strColumnaDerecha;

    /**
     * Constructor por defecto.
     *
     */
    public RespuestaCombinacionRelacionColumnas() {
    }

    /**
     * Constructor de una Respuesta Relacion de Columnas.
     *
     * @param intOrden Orden de la relacion.
     * @param strColumnaIzquierda Descripcion de la columna izquierda que el
     * alumno selecciono con la derecha.
     * @param strColumnaDerecha Descripcion de la coluna derecha que el alumno
     * selecciono con la izquierda.
     */
    public RespuestaCombinacionRelacionColumnas(int intOrden, String strColumnaIzquierda, String strColumnaDerecha) {
        this.intOrden = intOrden;
        this.strColumnaIzquierda = strColumnaIzquierda;
        this.strColumnaDerecha = strColumnaDerecha;
    }

    public int getIntOrden() {
        return intOrden;
    }

    public void setIntOrden(int intOrden) {
        this.intOrden = intOrden;
    }

    public String getStrColumnaIzquierda() {
        return strColumnaIzquierda;
    }

    public void setStrColumnaIzquierda(String strColumnaIzquierda) {
        this.strColumnaIzquierda = strColumnaIzquierda;
    }

    public String getStrColumnaDerecha() {
        return strColumnaDerecha;
    }

    public void setStrColumnaDerecha(String strColumnaDerecha) {
        this.strColumnaDerecha = strColumnaDerecha;
    }

    @Override
    public Pregunta getPregunta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean esCorreccionAutomatica() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getCalificacion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean fueRespondida() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
