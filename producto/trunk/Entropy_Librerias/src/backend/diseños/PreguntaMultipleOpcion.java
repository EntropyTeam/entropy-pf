package backend.diseños;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class PreguntaMultipleOpcion extends Pregunta implements Serializable {

    private ArrayList<OpcionMultipleOpcion> colOpciones;

    public PreguntaMultipleOpcion() {
        super();
    }

    /**
     * Constructor de la clase.
     *
     * @param intPreguntaId ID de la pregunta que se crea
     * @param strEnunciado Enunciado de la preguta que se crea
     * @param intIdTipoPre  tipo de pregunta que se crea
     * @param strNivel nivel de pregunta que se crea
     */
    public PreguntaMultipleOpcion(int intPreguntaId, String strEnunciado, int intIdTipoPre, String strNivel) {
        super(intPreguntaId, strEnunciado, intIdTipoPre, strNivel);
    }

    public PreguntaMultipleOpcion(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
    }

    public PreguntaMultipleOpcion(ArrayList<OpcionMultipleOpcion> colOpciones, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.colOpciones = colOpciones;
    }

    public PreguntaMultipleOpcion(ArrayList<OpcionMultipleOpcion> colOpciones, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.colOpciones = colOpciones;
    }

    public PreguntaMultipleOpcion(ArrayList<OpcionMultipleOpcion> colOpciones, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.colOpciones = colOpciones;
    }

    public PreguntaMultipleOpcion(ArrayList<OpcionMultipleOpcion> colOpciones, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.colOpciones = colOpciones;
    }

    public ArrayList<OpcionMultipleOpcion> getColOpciones() {
        return colOpciones;
    }

    public void setColOpciones(ArrayList<OpcionMultipleOpcion> colOpciones) {
        this.colOpciones = colOpciones;
    }

    @Override
    public PreguntaMultipleOpcion clone() {
        PreguntaMultipleOpcion clonada = new PreguntaMultipleOpcion();
        this.clone(clonada);
        clonada.colOpciones = this.colOpciones;
        
        return clonada;
    }
    
    /**
     * Evalúa si existen o no múltiples opciones correctas.
     * 
     * @return true si es una única opción correcta, false de lo contrario.
     */
    public boolean esUnicaRespuesta() {
        if (colOpciones == null) return false;
        int intCorrectas = 0;
        for (OpcionMultipleOpcion opcion : colOpciones) {
            if (opcion.isBlnEsVerdadera()) intCorrectas++;
        }
        return !(intCorrectas > 1);
    }
}
