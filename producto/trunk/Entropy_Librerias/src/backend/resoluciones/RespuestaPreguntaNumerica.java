package backend.resoluciones;

import backend.diseños.PreguntaNumerica;
import java.io.Serializable;

/**
 *
 * @author gaston
 */
public class RespuestaPreguntaNumerica extends Respuesta implements Serializable {

    private double dblRespuestaNumero;
    private double dblRangoDesde;
    private double dblRangoHasta;
    private PreguntaNumerica preguntaNumerica;

    /**
     * Constructor por defecto de la clase.
     */
    public RespuestaPreguntaNumerica() {
    }

    /**
     * Constructor de Respuesta Pregunta Numerica.
     *
     * @param preguntaNumerica pregunta que es respondida en este objeto.
     */
    public RespuestaPreguntaNumerica(PreguntaNumerica preguntaNumerica) {
        this.preguntaNumerica = preguntaNumerica;
    }

    /**
     * Constructor de Respuesta Pregunta Numerica.
     *
     * @param dblRespuestaNumero Numero que respondio el alumno a la pregunta.
     * @param preguntaNumerica pregunta que es respondida en este objeto.
     */
    public RespuestaPreguntaNumerica(double dblRespuestaNumero, PreguntaNumerica preguntaNumerica) {
        this.dblRespuestaNumero = dblRespuestaNumero;
        this.preguntaNumerica = preguntaNumerica;
    }

    /**
     * Constructor de Respuesta Pregunta Numerica.
     *
     * @param dblRangoDesde Respuesta del alumno para el valor del límite
     * inferior del intervalo.
     * @param dblRangoHasta Respuesta del alumno para el valor del límite
     * superior del intervalo.
     * @param preguntaNumerica pregunta que es respondida en este objeto.
     */
    public RespuestaPreguntaNumerica(double dblRangoDesde, double dblRangoHasta, PreguntaNumerica preguntaNumerica) {
        this.dblRangoDesde = dblRangoDesde;
        this.dblRangoHasta = dblRangoHasta;
        this.preguntaNumerica = preguntaNumerica;
    }

    @Override
    public PreguntaNumerica getPregunta() {
        return preguntaNumerica;
    }

    public void setPreguntaNumerica(PreguntaNumerica preguntaNumerica) {
        this.preguntaNumerica = preguntaNumerica;
    }

    public double getDblRespuestaNumero() {
        return dblRespuestaNumero;
    }

    public void setDblRespuestaNumero(double dblRespuestaNumero) {
        this.dblRespuestaNumero = dblRespuestaNumero;
    }

    public double getDblRangoDesde() {
        return dblRangoDesde;
    }

    public void setDblRangoDesde(double dblRangoDesde) {
        this.dblRangoDesde = dblRangoDesde;
    }

    public double getDblRangoHasta() {
        return dblRangoHasta;
    }

    public void setDblRangoHasta(double dblRangoHasta) {
        this.dblRangoHasta = dblRangoHasta;
    }

    @Override
    public boolean esCorreccionAutomatica() {
        return true;
    }

    @Override
    public double getCalificacion() {
        boolean blnEsCorrecta = true;
        if (!preguntaNumerica.esRango()) {
            if (dblRespuestaNumero < preguntaNumerica.getDblNumero() - preguntaNumerica.getDblVariacion()
                    || dblRespuestaNumero > preguntaNumerica.getDblNumero() + preguntaNumerica.getDblVariacion()) {
                blnEsCorrecta = false;
            }
        } else {
            if ((dblRangoDesde < preguntaNumerica.getDblRangoDesde() - preguntaNumerica.getDblVariacion()
                    || dblRangoDesde > preguntaNumerica.getDblRangoDesde() + preguntaNumerica.getDblVariacion())
                    || (dblRangoHasta < preguntaNumerica.getDblRangoHasta() - preguntaNumerica.getDblVariacion()
                    || dblRangoHasta > preguntaNumerica.getDblRangoHasta() + preguntaNumerica.getDblVariacion())) {
                blnEsCorrecta = false;
            }
        }
        return (blnEsCorrecta) ? preguntaNumerica.getDblPuntaje() : 0;
    }

    @Override
    public boolean fueRespondida() {
        return true;
    }
}
