package backend.resoluciones;

import backend.diseños.CombinacionRelacionColumnas;
import backend.diseños.PreguntaRelacionColumnas;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa a la respuesta del alumno a una pregunta de combinación de 
 * columnas.
 * 
 * @author denise
 */
public class RespuestaPreguntaRelacionColumnas extends Respuesta implements Serializable{
    
    private PreguntaRelacionColumnas preguntaRelacionColumnas;
    private ArrayList<RespuestaCombinacionRelacionColumnas> colCombinaciones;

    /**
     * Constructor por defecto.
     */
    public RespuestaPreguntaRelacionColumnas() {
    }

     /**
     * Constructor de la clase.
     * 
     * @param preguntaRelacionColumnas pregunta a ser respondida en este objeto.
     */
    public RespuestaPreguntaRelacionColumnas(PreguntaRelacionColumnas preguntaRelacionColumnas) {
        this.preguntaRelacionColumnas = preguntaRelacionColumnas;
        this.colCombinaciones = new ArrayList<>();
    }

    @Override
    public PreguntaRelacionColumnas getPregunta() {
        return preguntaRelacionColumnas;
    }

    public void setPreguntaRelacionColumnas(PreguntaRelacionColumnas preguntaRelacionColumnas) {
        this.preguntaRelacionColumnas = preguntaRelacionColumnas;
    }

    public ArrayList<RespuestaCombinacionRelacionColumnas> getColCombinaciones() {
        return colCombinaciones;
    }

    public void setColCombinaciones(ArrayList<RespuestaCombinacionRelacionColumnas> colCombinaciones) {
        this.colCombinaciones = colCombinaciones;
    }
    
    @Override
    public boolean esCorreccionAutomatica() {
        return true;
    }

    @Override
    public double getCalificacion() {
        boolean blnEsCorrecta = (colCombinaciones.size() > 0);
        for (RespuestaCombinacionRelacionColumnas combinacionRTA : colCombinaciones) {
            if (!blnEsCorrecta) break;
            for (CombinacionRelacionColumnas combinacionPTA : preguntaRelacionColumnas.getColCombinaciones()){
                if (combinacionRTA.getIntOrden() == combinacionPTA.getIntOrden()) {
                    blnEsCorrecta = combinacionRTA.getStrColumnaDerecha().equals(combinacionPTA.getStrColumnaDerecha());
                    break;
                }
            }
        }
        return (blnEsCorrecta) ? preguntaRelacionColumnas.getDblPuntaje() : 0;
    }

    @Override
    public boolean fueRespondida() {
        boolean blnFueRespondida = false;
        for (RespuestaCombinacionRelacionColumnas combinacionRTA : colCombinaciones) {
            if (blnFueRespondida) break;
            blnFueRespondida = !combinacionRTA.getStrColumnaDerecha().isEmpty();
        }
        return blnFueRespondida;
    }
}
