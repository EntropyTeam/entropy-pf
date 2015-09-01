package backend.diseños;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class PreguntaVerdaderoFalso extends Pregunta implements Serializable {

    private boolean blnEsVerdadera;
    private boolean blnConJustificacion;

    public PreguntaVerdaderoFalso() {
        super();
    }

    public PreguntaVerdaderoFalso(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
    }

    /**
     * Constructor de la clase.
     *
     * @param intPreguntaId ID de la pregunta que se crea
     * @param strEnunciado Enunciado de la preguta que se crea
     * @param intIdTipoPre tipo de pregunta que se crea
     * @param strNivel nivel de pregunta que se crea
     */
    public PreguntaVerdaderoFalso(int intPreguntaId, String strEnunciado, int intIdTipoPre, String strNivel) {
        super(intPreguntaId, strEnunciado, intIdTipoPre, strNivel);
    }

    public PreguntaVerdaderoFalso(boolean blnEsVerdadera, boolean blnConJustificacion, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.blnEsVerdadera = blnEsVerdadera;
        this.blnConJustificacion = blnConJustificacion;
    }

    public PreguntaVerdaderoFalso(boolean blnEsVerdadera, boolean blnConJustificacion, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.blnEsVerdadera = blnEsVerdadera;
        this.blnConJustificacion = blnConJustificacion;
    }

    public PreguntaVerdaderoFalso(boolean blnEsVerdadera, boolean blnConJustificacion, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.blnEsVerdadera = blnEsVerdadera;
        this.blnConJustificacion = blnConJustificacion;
    }

    public PreguntaVerdaderoFalso(boolean blnEsVerdadera, boolean blnConJustificacion, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.blnEsVerdadera = blnEsVerdadera;
        this.blnConJustificacion = blnConJustificacion;
    }

    public boolean isBlnEsVerdadera() {
        return blnEsVerdadera;
    }

    public void setBlnEsVerdadera(boolean blnEsVerdadera) {
        this.blnEsVerdadera = blnEsVerdadera;
    }

    public boolean isBlnConJustificacion() {
        return blnConJustificacion;
    }

    public void setBlnConJustificacion(boolean blnConJustificacion) {
        this.blnConJustificacion = blnConJustificacion;
    }

    @Override
    public PreguntaVerdaderoFalso clone() {
        PreguntaVerdaderoFalso clonada = new PreguntaVerdaderoFalso();
        this.clone(clonada);
        clonada.blnEsVerdadera = this.blnEsVerdadera;
        clonada.blnConJustificacion = this.blnConJustificacion;
        
        return clonada;
    }
}
