package backend.diseños;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class PreguntaNumerica extends Pregunta implements Serializable {

    private boolean blnEsRango;
    private double dblNumero;
    private double dblRangoDesde;
    private double dblRangoHasta;
    private double dblVariacion;

    public PreguntaNumerica() {
        super();
    }

    /**
     * Constructor de la clase.
     *
     * @param intPreguntaId ID de la pregunta que se crea
     * @param strEnunciado Enunciado de la preguta que se crea
     * @param intIdTipoPre tipo de pregunta que se crea
     * @param strNivel nivel de pregunta que se crea
     */
    public PreguntaNumerica(int intPreguntaId, String strEnunciado, int intIdTipoPre, String strNivel) {
        super(intPreguntaId, strEnunciado, intIdTipoPre, strNivel);
    }

    public PreguntaNumerica(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
    }

    public PreguntaNumerica(boolean blnEsRango, double dblNúmero, double dblRangoDesde, double dblRangoHasta, double dblVariacion, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.blnEsRango = blnEsRango;
        this.dblNumero = dblNúmero;
        this.dblRangoDesde = dblRangoDesde;
        this.dblRangoHasta = dblRangoHasta;
        this.dblVariacion = dblVariacion;
    }

    public PreguntaNumerica(boolean blnEsRango, double dblNúmero, double dblRangoDesde, double dblRangoHasta, double dblVariacion, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.blnEsRango = blnEsRango;
        this.dblNumero = dblNúmero;
        this.dblRangoDesde = dblRangoDesde;
        this.dblRangoHasta = dblRangoHasta;
        this.dblVariacion = dblVariacion;
    }

    public PreguntaNumerica(boolean blnEsRango, double dblNúmero, double dblRangoDesde, double dblRangoHasta, double dblVariacion, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia);
        this.blnEsRango = blnEsRango;
        this.dblNumero = dblNúmero;
        this.dblRangoDesde = dblRangoDesde;
        this.dblRangoHasta = dblRangoHasta;
        this.dblVariacion = dblVariacion;
    }

    public PreguntaNumerica(boolean blnEsRango, double dblNúmero, double dblRangoDesde, double dblRangoHasta, double dblVariacion, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags, tema);
        this.blnEsRango = blnEsRango;
        this.dblNumero = dblNúmero;
        this.dblRangoDesde = dblRangoDesde;
        this.dblRangoHasta = dblRangoHasta;
        this.dblVariacion = dblVariacion;
    }
    public PreguntaNumerica(boolean blnEsRango, double dblNúmero, double dblRangoDesde, double dblRangoHasta, double dblVariacion, int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags) {
        super(intPreguntaId, intOrden, strEnunciado, intTipo, strNivel, dblPuntaje, strReferencia, colAdjuntos, colTags);
        this.blnEsRango = blnEsRango;
        this.dblNumero = dblNúmero;
        this.dblRangoDesde = dblRangoDesde;
        this.dblRangoHasta = dblRangoHasta;
        this.dblVariacion = dblVariacion;
    }

    public boolean esRango() {
        return blnEsRango;
    }

    public void setBlnEsRango(boolean blnEsRango) {
        this.blnEsRango = blnEsRango;
    }

    public double getDblNumero() {
        return dblNumero;
    }

    public void setDblNumero(double dblNumero) {
        this.dblNumero = dblNumero;
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

    public double getDblVariacion() {
        return dblVariacion;
    }

    public void setDblVariacion(double dblVariacion) {
        this.dblVariacion = dblVariacion;
    }
    
    @Override
    public PreguntaNumerica clone() {
        PreguntaNumerica clonada = new PreguntaNumerica();
        this.clone(clonada);
        clonada.blnEsRango = this.blnEsRango;
        clonada.dblNumero = this.dblNumero;
        clonada.dblRangoDesde = this.dblRangoDesde;
        clonada.dblRangoHasta = this.dblRangoHasta;
        clonada.dblVariacion = this.dblVariacion;
        
        return clonada;
    }
}
