package backend.resoluciones;

import backend.diseños.OpcionMultipleOpcion;
import backend.diseños.PreguntaMultipleOpcion;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represnta una respuesta de una pregunta múltiple opción.
 * 
 * @author denise
 */
public class RespuestaPreguntaMultipleOpcion extends Respuesta implements Serializable{
    
    private PreguntaMultipleOpcion preguntaMultipleOpcion;
    private ArrayList<RespuestaOpcionMultipleOpcion> colOpciones;

    /**
     * Constructor por defecto.
     */
    public RespuestaPreguntaMultipleOpcion() {
    }
    
    /**
     * Constructor de la clase.
     * 
     * @param preguntaMultipleOpcion pregunta a ser respondida en este objeto.
     */
    public RespuestaPreguntaMultipleOpcion(PreguntaMultipleOpcion preguntaMultipleOpcion) {
        this.preguntaMultipleOpcion = preguntaMultipleOpcion;
        this.colOpciones = new ArrayList<>();
    }
    
    @Override
    public PreguntaMultipleOpcion getPregunta() {
        return preguntaMultipleOpcion;
    }

    public void setPreguntaMultipleOpcion(PreguntaMultipleOpcion preguntaMultipleOpcion) {
        this.preguntaMultipleOpcion = preguntaMultipleOpcion;
    }

    public ArrayList<RespuestaOpcionMultipleOpcion> getColeccionOpciones() {
        return colOpciones;
    }

    public void setColeccionOpciones(ArrayList<RespuestaOpcionMultipleOpcion> colOpciones) {
        this.colOpciones = colOpciones;
    }

    @Override
    public boolean esCorreccionAutomatica() {
        return true;
    }

    @Override
    public double getCalificacion() {
        boolean blnEsCorrecta = true;
        for(RespuestaOpcionMultipleOpcion rtaOpcionMO : colOpciones) {
            if (!blnEsCorrecta) break;
            for (OpcionMultipleOpcion ptaOpcionMO : preguntaMultipleOpcion.getColOpciones()) {
                if (rtaOpcionMO.getIntOrden() == ptaOpcionMO.getIntOrden()){
                    blnEsCorrecta = rtaOpcionMO.isBlnEsMarcada() == ptaOpcionMO.isBlnEsVerdadera();
                    break;
                }
            }
        }
        return (blnEsCorrecta) ? preguntaMultipleOpcion.getDblPuntaje() : 0;
    }

    @Override
    public boolean fueRespondida() {
        boolean blnFueRespondida = false;
        for(RespuestaOpcionMultipleOpcion rtaOpcionMO : colOpciones) {
            if (blnFueRespondida) break;
            blnFueRespondida = rtaOpcionMO.isBlnEsMarcada();
        }
        return blnFueRespondida;
    }

}
