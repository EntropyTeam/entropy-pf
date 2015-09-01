package backend.resoluciones;

import backend.diseños.PreguntaVerdaderoFalso;
import java.io.Serializable;

/**
 * Clase que representa una respuesta a una preegunta  verdadero/falso.
 * 
 * @author gaston
 */
public class RespuestaPreguntaVerdaderoFalso extends Respuesta implements Serializable {

    private boolean blnSeleccionoVerdadero;
    private boolean blnSeleccionoFalso;
    private String strJustificacion;
    private PreguntaVerdaderoFalso preguntaVerdaderoFalso;
    private double dblCalificacion;

    /**
     * Constructor por defecto.
     */
    public RespuestaPreguntaVerdaderoFalso() {
        this.dblCalificacion = -1;
    }
    
    /**
     * Constructor Pregunta Verdadero Falso.
     *
     * @param preguntaVerdaderoFalso pregunta a ser contestada con este objeto.
     */
    public RespuestaPreguntaVerdaderoFalso(PreguntaVerdaderoFalso preguntaVerdaderoFalso) {
        this.preguntaVerdaderoFalso = preguntaVerdaderoFalso;
        this.dblCalificacion = -1;
    }
    
    /**
     * Constructor Pregunta Verdadero Falso.
     *
     * @param blnRespuestaEsVerdadera Boolean que indica si el alumno contesto
     * Verdadero.
     * @param blnSeleccionoFalso Boolean que indica si el alumno contesto
     * Falso.
     * @param preguntaVerdaderoFalso pregunta a ser contestada con este objeto.
     */
    public RespuestaPreguntaVerdaderoFalso(boolean blnRespuestaEsVerdadera,
            boolean blnSeleccionoFalso,
            PreguntaVerdaderoFalso preguntaVerdaderoFalso) {
        this.blnSeleccionoVerdadero = blnRespuestaEsVerdadera;
        this.blnSeleccionoFalso = blnSeleccionoFalso;
        this.preguntaVerdaderoFalso = preguntaVerdaderoFalso;
        this.dblCalificacion = -1;
    }
    
    /**
     * Constructor Pregunta Verdadero Falso.
     *
     * @param blnSeleccionoVerdadero Boolean que indica si el alumno contesto
     * Verdadero.
     * @param blnSeleccionoFalso Boolean que indica si el alumno contesto
     * Falso.
     * @param strJustificacion justificación del alumno para su elección.
     * @param preguntaVerdaderoFalso pregunta a ser contestada con este objeto.
     */
    public RespuestaPreguntaVerdaderoFalso(boolean blnSeleccionoVerdadero,
            boolean blnSeleccionoFalso,
            String strJustificacion,
            PreguntaVerdaderoFalso preguntaVerdaderoFalso) {
        this.blnSeleccionoVerdadero = blnSeleccionoVerdadero;
        this.blnSeleccionoFalso = blnSeleccionoFalso;
        this.strJustificacion = strJustificacion;
        this.preguntaVerdaderoFalso = preguntaVerdaderoFalso;
        this.dblCalificacion = -1;
    }

    public boolean isBlnSeleccionoVerdadero() {
        return blnSeleccionoVerdadero;
    }

    public void setBlnSeleccionoVerdadero(boolean blnSeleccionoVerdadero) {
        this.blnSeleccionoVerdadero = blnSeleccionoVerdadero;
    }

    public boolean isBlnSeleccionoFalso() {
        return blnSeleccionoFalso;
    }

    public void setBlnSeleccionoFalso(boolean blnSeleccionoFalso) {
        this.blnSeleccionoFalso = blnSeleccionoFalso;
    }

    public String getStrJustificacion() {
        return strJustificacion;
    }

    public void setStrJustificacion(String strJustificacion) {
        this.strJustificacion = strJustificacion;
    }

    @Override
    public PreguntaVerdaderoFalso getPregunta() {
        return preguntaVerdaderoFalso;
    }

    public void setPreguntaVerdaderoFalso(PreguntaVerdaderoFalso preguntaVerdaderoFalso) {
        this.preguntaVerdaderoFalso = preguntaVerdaderoFalso;
    }

    public void setCalificacion(double dblCalificacion) {
        this.dblCalificacion = dblCalificacion;
    }
    
    @Override
    public boolean esCorreccionAutomatica() {
        return !preguntaVerdaderoFalso.isBlnConJustificacion();
    }

    @Override
    public double getCalificacion() {
        boolean blnEsCorrecta = true;
        if (!preguntaVerdaderoFalso.isBlnConJustificacion()) {
            if ((!blnSeleccionoFalso && !blnSeleccionoVerdadero) //No seleccionó ninguna
                    || (blnSeleccionoVerdadero && !preguntaVerdaderoFalso.isBlnEsVerdadera())
                    || (blnSeleccionoFalso && preguntaVerdaderoFalso.isBlnEsVerdadera())) {
                blnEsCorrecta = false;
            }
            return (blnEsCorrecta) ? preguntaVerdaderoFalso.getDblPuntaje() : 0;   
        } else {
            return (dblCalificacion >= 0) ? dblCalificacion : -1;
        }
    }

    @Override
    public boolean fueRespondida() {
        boolean blnFueRespondida = true;
        if (!blnSeleccionoFalso && !blnSeleccionoVerdadero){
                blnFueRespondida = false;
        }
        return blnFueRespondida;
    }
    
}
