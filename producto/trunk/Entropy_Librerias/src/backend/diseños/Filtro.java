package backend.diseños;

/**
 * Clase que representa un valor por el cual se puede llegar a filtrar una
 * pregunta, un filtro.
 *
 * @author gaston
 */
public class Filtro {

    private TipoFiltro tipo;

    public enum TipoFiltro {

        INSTITUCION, CURSO, CON_CURSO, SIN_CURSO, DISEÑOEXAMEN, TAG
    };
    
    private Institucion institucion;
    private Curso curso;
    private String tag;
    private DiseñoExamen diseñoExamen;

    /**
     * Constructor de la clase.
     * 
     * @param tipo instancia de 
     * <code>Filtro.TipoFiltro.SIN_CURSO</code>
     * <code>Filtro.TipoFiltro.CON_CURSO</code>
     */
    public Filtro(TipoFiltro tipo) {
        this.tipo = tipo;
    }
    
    /**
     * Constructor de la clase.
     * 
     * @param tipo Filtro.TipoFiltro.CURSO
     * @param institucion instancia de Institucion
     */
    public Filtro(TipoFiltro tipo, Institucion institucion) {
        this.tipo = tipo;
        this.institucion = institucion;
    }

    /**
     * Constructor de la clase.
     * 
     * @param tipo Filtro.TipoFiltro.CURSO
     * @param curso instancia de Curso.
     */
    public Filtro(TipoFiltro tipo, Curso curso) {
        this.tipo = tipo;
        this.curso = curso;
    }

    /**
     * Constructor de la clase.
     * 
     * @param tipo instancia de Filtro.TipoFiltro.TAG
     * @param tag string tag
     */
    public Filtro(TipoFiltro tipo, String tag) {
        this.tipo = tipo;
        this.tag = tag;
    }
    
    /**
     * Constructor de la clase.
     * 
     * @param tipo instancia de Filtro.TipoFiltro.DISEÑOEXAMEN
     * @param diseñoExamen instancia de DiseñoExamen.
     */
    public Filtro(TipoFiltro tipo, DiseñoExamen diseñoExamen) {
        this.tipo = tipo;
        this.diseñoExamen = diseñoExamen;
    }

    public DiseñoExamen getDiseñoExamen() {
        return diseñoExamen;
    }

    public void setDiseñoExamen(DiseñoExamen diseñoExamen) {
        this.diseñoExamen = diseñoExamen;
    }

    public TipoFiltro getTipo() {
        return tipo;
    }

    public void setTipo(TipoFiltro tipo) {
        this.tipo = tipo;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String Tag) {
        this.tag = Tag;
    }

    public TipoFiltro getFiltro() {
        return tipo;
    }

    @Override
    public String toString() {
        return this.tipo.toString();
    }

}
