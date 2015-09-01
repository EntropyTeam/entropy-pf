package backend.diseños;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti comentada por Gaston Noves
 */
public class Pregunta implements Serializable {

    private int intPreguntaId;
    private int intOrden;
    private String strEnunciado;
    private int intTipo;
    private String strNivel;
    private double dblPuntaje;
    private String strReferencia;
    private ArrayList<Object> colAdjuntos;
    private ArrayList<String> colTags;
    private Tema tema;

    public Pregunta() {
    }

    /**
     * Constructor de la clase.
     *
     * @param intPreguntaId ID de la pregunta que se crea
     * @param strEnunciado Enunciado de la preguta que se crea
     * @param intTipo tipo de pregunta que se crea
     * @param strNivel nivel de pregunta que se crea
     */
    public Pregunta(int intPreguntaId, String strEnunciado, int intTipo, String strNivel) {
        this.intPreguntaId = intPreguntaId;
        this.strEnunciado = strEnunciado;
        this.intTipo = intTipo;
        this.strNivel = strNivel;
    }

    public Pregunta(int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        this.intOrden = intOrden;
        this.strEnunciado = strEnunciado;
        this.intTipo = intTipo;
        this.strNivel = strNivel;
        this.dblPuntaje = dblPuntaje;
        this.strReferencia = strReferencia;
    }

    public Pregunta(int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        this.intOrden = intOrden;
        this.strEnunciado = strEnunciado;
        this.intTipo = intTipo;
        this.strNivel = strNivel;
        this.dblPuntaje = dblPuntaje;
        this.strReferencia = strReferencia;
        this.colAdjuntos = colAdjuntos;
        this.colTags = colTags;
        this.tema = tema;
    }

    public Pregunta(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia) {
        this.intPreguntaId = intPreguntaId;
        this.intOrden = intOrden;
        this.strEnunciado = strEnunciado;
        this.intTipo = intTipo;
        this.strNivel = strNivel;
        this.dblPuntaje = dblPuntaje;
        this.strReferencia = strReferencia;
    }

    public Pregunta(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags, Tema tema) {
        this.intPreguntaId = intPreguntaId;
        this.intOrden = intOrden;
        this.strEnunciado = strEnunciado;
        this.intTipo = intTipo;
        this.strNivel = strNivel;
        this.dblPuntaje = dblPuntaje;
        this.strReferencia = strReferencia;
        this.colAdjuntos = colAdjuntos;
        this.colTags = colTags;
        this.tema = tema;
    }
    public Pregunta(int intPreguntaId, int intOrden, String strEnunciado, int intTipo, String strNivel, double dblPuntaje, String strReferencia, ArrayList<Object> colAdjuntos, ArrayList<String> colTags) {
        this.intPreguntaId = intPreguntaId;
        this.intOrden = intOrden;
        this.strEnunciado = strEnunciado;
        this.intTipo = intTipo;
        this.strNivel = strNivel;
        this.dblPuntaje = dblPuntaje;
        this.strReferencia = strReferencia;
        this.colAdjuntos = colAdjuntos;
        this.colTags = colTags;
    }

    public int getIntPreguntaId() {
        return intPreguntaId;
    }

    public void setIntPreguntaId(int intPreguntaId) {
        this.intPreguntaId = intPreguntaId;
    }

    public int getIntOrden() {
        return intOrden;
    }

    public void setIntOrden(int intOrden) {
        this.intOrden = intOrden;
    }

    public String getStrEnunciado() {
        return strEnunciado;
    }

    public void setStrEnunciado(String strEnunciado) {
        this.strEnunciado = strEnunciado;
    }

    public int getIntTipo() {
        return intTipo;
    }

    public void setIntTipo(int intTipo) {
        this.intTipo = intTipo;
    }

    public String getStrNivel() {
        return strNivel;
    }

    public void setStrNivel(String strNivel) {
        this.strNivel = strNivel;
    }

    public double getDblPuntaje() {
        return dblPuntaje;
    }

    public void setDblPuntaje(double dblPuntaje) {
        this.dblPuntaje = dblPuntaje;
    }

    public String getStrReferencia() {
        return strReferencia;
    }

    public void setStrReferencia(String strReferencia) {
        this.strReferencia = strReferencia;
    }

    public ArrayList<Object> getColAdjuntos() {
        return colAdjuntos;
    }

    public void setColAdjuntos(ArrayList<Object> colAdjuntos) {
        this.colAdjuntos = colAdjuntos;
    }

    public ArrayList<String> getColTags() {
        return colTags;
    }

    public void setColTags(ArrayList<String> colTags) {
        this.colTags = colTags;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }
    
    public void clone(Pregunta clonada) {
        clonada.intPreguntaId = this.intPreguntaId;
        clonada.intOrden = this.intOrden;
        clonada.strEnunciado = this.strEnunciado;
        clonada.intTipo = this.intTipo;
        clonada.strNivel = this.strNivel;
        clonada.dblPuntaje = this.dblPuntaje;
        clonada.strReferencia = this.strReferencia;
        clonada.colAdjuntos = this.colAdjuntos;
        clonada.colTags = this.colTags;
        clonada.tema = this.tema;
    }

    /*
    * Metodo toString() de la clase, usado para cargar el enunciado de las preguntas en la lista
    */
    @Override
    public String toString(){
        return this.strEnunciado;
    }
}
