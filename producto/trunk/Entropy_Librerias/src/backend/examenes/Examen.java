package backend.examenes;

import backend.diseños.Curso;
import backend.diseños.Pregunta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que representa a un objeto diseño de examen.
 *
 * @author gaston
 */
public class Examen implements Serializable {

    public enum OrdenLista {

        ALEATORIO, POR_TEMA, SEGUN_DISEÑO
    }
    private OrdenLista orden;

    private int intExamenId;
    private String strNombre;
    private String strDescripcion;
    private String strMotivoCancelacion;
    private ArrayList<Pregunta> colPreguntas;
    private Curso curso;
    private int intEstado;
    private int intTiempo; // En minutos
    private Date dteFecha;
    private Double dblPorcentajeAprobacion;

    /**
     * Constructor por defecto.
     *
     */
    public Examen() {
        this.intExamenId = -1;
        this.orden = OrdenLista.ALEATORIO;
    }

    /**
     * Constructor de Examen
     *
     * @param intExamenId iD del Examen en la base de datos.
     * @param strNombre Nombre del examen.
     * @param strDescripcion Descripcion del Examen.
     * @param colPreguntas Coleccion de preguntas que contienen el Examen.
     * @param curso Curso al que pertenece el Examen.
     * @param intEstado Estado del Examen.
     * @param intTiempo Duracion del examen en minutos.
     * @param dteFecha Fecha en la que fue tomado.
     */
    public Examen(int intExamenId, String strNombre, String strDescripcion, ArrayList<Pregunta> colPreguntas, Curso curso, int intEstado, int intTiempo, Date dteFecha) {
        this.intExamenId = intExamenId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.colPreguntas = colPreguntas;
        this.curso = curso;
        this.intEstado = intEstado;
        this.intTiempo = intTiempo;
        this.dteFecha = dteFecha;
        this.orden = OrdenLista.ALEATORIO;
    }

    /**
     * Constructor de Examen para usarlo en DAOExamen
     *
     * @param intExamenId iD del Examen en la base de datos.
     * @param strNombre Nombre del examen.
     * @param strDescripcion Descripcion del Examen.
     * @param intEstado Estado del Examen.
     */
    public Examen(int intExamenId, String strNombre, String strDescripcion, int intEstado) {
        this.intExamenId = intExamenId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.intEstado = intEstado;
        this.orden = OrdenLista.ALEATORIO;
    }

    public Examen(int intExamenId, String strNombre, String strDescripcion, Curso curso, int intEstado, int intTiempo) {
        this.intExamenId = intExamenId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.curso = curso;
        this.intEstado = intEstado;
        this.intTiempo = intTiempo;
        this.orden = OrdenLista.ALEATORIO;
    }

    public int getIntExamenId() {
        return intExamenId;
    }

    public void setIntExamenId(int intExamenId) {
        this.intExamenId = intExamenId;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrMotivoCancelacion() {
        return strMotivoCancelacion;
    }

    public void setStrMotivoCancelacion(String strMotivoCancelacion) {
        this.strMotivoCancelacion = strMotivoCancelacion;
    }

    
    public ArrayList<Pregunta> getColPreguntas() {
        return colPreguntas;
    }

    public void setColPreguntas(ArrayList<Pregunta> colPreguntas) {
        this.colPreguntas = colPreguntas;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getIntEstado() {
        return intEstado;
    }

    public void setIntEstado(int intEstado) {
        this.intEstado = intEstado;
    }

    public int getIntTiempo() {
        return intTiempo;
    }

    public void setIntTiempo(int intTiempo) {
        this.intTiempo = intTiempo;
    }

    public Date getDteFecha() {
        return dteFecha;
    }

    public void setDteFecha(Date dteFecha) {
        this.dteFecha = dteFecha;
    }

    public OrdenLista getOrdenPresentacion() {
        return orden;
    }

    public void setOrdenPresentacion(OrdenLista orden) {
        this.orden = orden;
    }

    public Double getDblPorcentajeAprobacion() {
        return dblPorcentajeAprobacion;
    }

    public void setDblPorcentajeAprobacion(Double dblPorcentajeAprobacion) {
        this.dblPorcentajeAprobacion = dblPorcentajeAprobacion;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Examen) {
            return intExamenId == ((Examen) obj).intExamenId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.intExamenId;
        return hash;
    }

    @Override
    public String toString() {
        return strNombre;
    }

    public double getPuntajeTotal() {
        double dblPuntaje = 0;
        if(colPreguntas!=null)
        {
        for (Pregunta pregunta : colPreguntas){
            dblPuntaje += pregunta.getDblPuntaje();
        }
        }
        return dblPuntaje;
    }
    
    
}
