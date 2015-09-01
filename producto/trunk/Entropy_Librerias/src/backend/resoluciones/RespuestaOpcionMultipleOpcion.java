package backend.resoluciones;

import backend.diseños.Pregunta;
import java.io.Serializable;

/**
 * Clase que representa a cada una de las opciones de una respuesta multiple opcion.
 * 
 * @author gaston
 */
public class RespuestaOpcionMultipleOpcion extends Respuesta implements Serializable {

    private int intOrden;
    private String strRespuesta;
    private boolean blnEsMarcada;

    /**
     * Constructor de Respuesta Opcion Multiple Opcion.
     *
     * @param intOrden Orden de la opcion en el Examen.
     * @param strRespuesta Descripcion de la opcion.
     * @param blnEsMarcada Boolean que determina si el alumno selecciono esa
     * opcion como respuesta correcta.
     */
    public RespuestaOpcionMultipleOpcion(int intOrden,
            String strRespuesta,
            boolean blnEsMarcada) {
        this.intOrden = intOrden;
        this.strRespuesta = strRespuesta;
        this.blnEsMarcada = blnEsMarcada;
    }

    public int getIntOrden() {
        return intOrden;
    }

    public void setIntOrden(int intOrden) {
        this.intOrden = intOrden;
    }

    public String getStrRespuesta() {
        return strRespuesta;
    }

    public void setStrRespuesta(String strRespuesta) {
        this.strRespuesta = strRespuesta;
    }

    public boolean isBlnEsMarcada() {
        return blnEsMarcada;
    }

    public void setBlnEsMarcada(boolean blnEsMarcada) {
        this.blnEsMarcada = blnEsMarcada;
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
