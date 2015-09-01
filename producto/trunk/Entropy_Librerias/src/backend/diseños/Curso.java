package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class Curso implements Serializable {

    private int intCursoId;
    private String strNombre;
    private String strDescripcion;
    private Institucion institucion;

    public Curso() {
        this.intCursoId = -1;
    }

    /**
     * Constructor de la clase.
     *
     * @param strNombre nombre o titulo del diseño de examen.
     */
    public Curso(String strNombre) {
        this.intCursoId = -1;
        this.strNombre = strNombre;
    }

    public Curso(String strNombre, Institucion institucion) {
        this.intCursoId = -1;
        this.strNombre = strNombre;
        this.institucion = institucion;
    }

    public Curso(String strNombre, String strDescripcion, Institucion institucion) {
        this.intCursoId = -1;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.institucion = institucion;
    }

    public Curso(int intCursoId, String strNombre, String strDescripcion) {
        this.intCursoId = intCursoId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
    }

    public Curso(int intCursoId, String strNombre, String strDescripcion, Institucion institucion) {
        this.intCursoId = intCursoId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.institucion = institucion;
    }

    public Curso(int intCursoId) {
        this.intCursoId = intCursoId;
    }

    
    public int getIntCursoId() {
        return intCursoId;
    }

    public void setIntCursoId(int intCursoId) {
        this.intCursoId = intCursoId;
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

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    @Override
    public String toString() {
        return this.strNombre;
    }
    
        @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Curso)) return false;
        Curso curso=(Curso) obj;
        return curso.getIntCursoId()==this.getIntCursoId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.intCursoId;
        return hash;
    }
}
