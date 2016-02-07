package backend.resoluciones;


import backend.diseños.Pregunta;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lucas
 */
public class RespuestaDesarrollo extends Respuesta implements Serializable {
    
    private String strRespuesta;
    private Pregunta pregunta;
    private double dblCalificacion;

    /**
     * Constructor por defecto.
     */
    public RespuestaDesarrollo() {
        this.dblCalificacion = -1;
    }
    
    /**
     * Constructor de una Respuesta desarrollada por el alumno.
     *
     * @param pregunta pregunta a ser contestada con este objeto.
     */
    public RespuestaDesarrollo(Pregunta pregunta) {
        this.dblCalificacion = -1;
        this.pregunta = pregunta;
    }
    
    /**
     * Constructor de una Respuesta desarrollada por el alumno.
     *
     * @param strRespuesta String Respuesta dada por el Alumno.
     * @param pregunta pregunta a ser contestada con este objeto.
     */
    public RespuestaDesarrollo(String strRespuesta, Pregunta pregunta) {
        this.dblCalificacion = -1;
        this.strRespuesta = strRespuesta;
        this.pregunta = pregunta;
    }

    /**
     * Constructor de una Respuesta desarrollada por el alumno.
     *
     * @param strRespuesta String Respuesta dada por el Alumno.
     */
    public RespuestaDesarrollo(String strRespuesta) {
        this.dblCalificacion = -1;
        this.strRespuesta = strRespuesta;
    }

    public String getStrRespuesta() {
        return strRespuesta;
    }

    public void setStrRespuesta(String strRespuesta) {
        this.strRespuesta = strRespuesta;
    }

    @Override
    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    @Override
    public double getCalificacion() {
        if (!fueRespondida())
            return 0;
        return dblCalificacion;
    }

    public void setCalificacion(double dblCalificacion) {
        this.dblCalificacion = dblCalificacion;
    }
    
    @Override
    public boolean esCorreccionAutomatica() {
        return false;
    }

    @Override
    public boolean fueRespondida() {
        return strRespuesta != null && !strRespuesta.isEmpty();
    }

}
