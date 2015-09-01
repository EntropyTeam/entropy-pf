package backend.diseños;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class PreguntaRelacionColumnas extends Pregunta implements Serializable {

    private ArrayList<CombinacionRelacionColumnas> colCombinaciones;

    public PreguntaRelacionColumnas() {
        super();
    }

    public PreguntaRelacionColumnas(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
    }

    /**
     * Constructor de la clase.
     *
     * @param intPreguntaId ID de la pregunta que se crea
     * @param strEnunciado Enunciado de la preguta que se crea
     * @param intIdTipoPre  tipo de pregunta que se crea
     * @param strNivel nivel de pregunta que se crea
     */
    public PreguntaRelacionColumnas(int intPreguntaId, String strEnunciado, int intIdTipoPre, String strNivel) {
        super(intPreguntaId, strEnunciado, intIdTipoPre, strNivel);
    }

    public PreguntaRelacionColumnas(ArrayList<CombinacionRelacionColumnas> colCombinaciones, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.colCombinaciones = colCombinaciones;
    }

    public PreguntaRelacionColumnas(ArrayList<CombinacionRelacionColumnas> colCombinaciones, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.colCombinaciones = colCombinaciones;
    }

    public PreguntaRelacionColumnas(ArrayList<CombinacionRelacionColumnas> colCombinaciones, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.colCombinaciones = colCombinaciones;
    }

    public PreguntaRelacionColumnas(ArrayList<CombinacionRelacionColumnas> colCombinaciones, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.colCombinaciones = colCombinaciones;
    }
    public PreguntaRelacionColumnas(ArrayList<CombinacionRelacionColumnas> colCombinaciones, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags);
        this.colCombinaciones = colCombinaciones;
    }

    public ArrayList<CombinacionRelacionColumnas> getColCombinaciones() {
        return colCombinaciones;
    }

    public void setColCombinaciones(ArrayList<CombinacionRelacionColumnas> colCombinaciones) {
        this.colCombinaciones = colCombinaciones;
    }
    
    @Override
    public PreguntaRelacionColumnas clone() {
        PreguntaRelacionColumnas clonada = new PreguntaRelacionColumnas();
        this.clone(clonada);
        clonada.colCombinaciones = this.colCombinaciones;
        
        return clonada;
    }
}
