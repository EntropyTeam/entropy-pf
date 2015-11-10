package backend.diseños;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa a un objeto diseño de examen.
 *
 * @author Matias comentada por Denise y Gaston Noves
 */
public class DiseñoExamen implements Serializable {

    private int intDiseñoExamenId;
    private String strNombre;
    private String strDescripcion;
    private ArrayList<Pregunta> colPreguntas;
    private Curso curso;

    public DiseñoExamen() {
        this.intDiseñoExamenId = -1;
    }

    /**
     * Constructor de la clase.
     *
     * @param intDiseñoExamenId ID del diseño de examen a crear.
     * @param strNombre nombre o titulo del diseño de examen.
     */
    public DiseñoExamen(int intDiseñoExamenId, String strNombre) {
        this.intDiseñoExamenId = intDiseñoExamenId;
        this.strNombre = strNombre;
    }

    /**
     * Constructor de la clase.
     *
     * @param strNombre nombre o titulo del diseño de examen.
     */
    public DiseñoExamen(String strNombre) {
        this.intDiseñoExamenId = -1;
        this.strNombre = strNombre;
    }

    public DiseñoExamen(String strNombre, String strDescripcion, ArrayList<Pregunta> colPreguntas, Curso curso) {
        this.intDiseñoExamenId = -1;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.colPreguntas = colPreguntas;
        this.curso = curso;
    }

    public DiseñoExamen(int intDiseñoExamenId, String strNombre, String strDescripcion, ArrayList<Pregunta> colPreguntas, Curso curso) {
        this.intDiseñoExamenId = intDiseñoExamenId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.colPreguntas = colPreguntas;
        this.curso = curso;
    }

    public int getIntDiseñoExamenId() {
        return intDiseñoExamenId;
    }

    public void setIntDiseñoExamenId(int intDiseñoExamenId) {
        this.intDiseñoExamenId = intDiseñoExamenId;
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
    
    //Devuelve true si el diseño e examen no tiene preguntas, es decir es nuevo.
    public boolean isNuevoExamen(){
        boolean blnRetorno = true;
        if(this.colPreguntas.size() > 0){
            blnRetorno = false;
        }
        return blnRetorno;
    }

    @Override
    public String toString() {
        return this.strNombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiseñoExamen) {
            return intDiseñoExamenId == ((DiseñoExamen)obj).intDiseñoExamenId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.intDiseñoExamenId;
        return hash;
    }
    
}
