package backend.gestores;

import backend.dao.diseños.DAOCurso;
import backend.dao.diseños.DAODiseñoExamen;
import backend.dao.diseños.DAOInstitucion;
import backend.dao.diseños.DAOPregunta;
import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Filtro;
import backend.diseños.Institucion;
import backend.diseños.Pregunta;
import java.util.ArrayList;

/**
 *
 * @author Gaston
 */
public class GestorImportarPregunta {

    /**
     * Constructor por defecto de la clase GestorImportarPregunra.
     *
     */
    public GestorImportarPregunta() {
    }

    /**
     * Se comunica con las clases DAO, para traer todas las preguntas que
     * cumplen con un conjunto de filtros.
     *
     * @param colFiltro Coleccion de filtros con las que se realiza la consulta.
     * @return coleccion de preguntas (Seguir).
     */
    public ArrayList<Pregunta> getPreguntaPorFiltro(ArrayList<Filtro> colFiltro) {
        ArrayList<Pregunta> colPregunta = new ArrayList<>();
        DAOPregunta dAOPregunta = new DAOPregunta();
        colPregunta = dAOPregunta.getPreguntasPorFiltros(colFiltro);
        return colPregunta;
    }

    /**
     * Obtiene todas las instituciones que existen.
     *
     * @return Coleccion de instituciones.
     */
    public ArrayList<Institucion> getInstituciones() {
        DAOInstitucion dAOIns = new DAOInstitucion();
        ArrayList<Institucion> colInstitucion = dAOIns.recuperarTodasLasInstituciones("");
        return colInstitucion;
    }

    /**
     * Obtiene todos los cursos que pertenecen a una determinada institucion.
     *
     * @param institucion Institucion por la que quiero filtrar los cursos.
     * @return Coleccion de cursos que pertenecen a una Institucion.
     */
    public ArrayList<Curso> getCursos(Institucion institucion) {
        ArrayList<Curso> colCurso = new ArrayList<Curso>();
        DAOCurso dAOCurso = new DAOCurso();
        colCurso = dAOCurso.recuperarTodosLosCursos(institucion, "");
        return colCurso;
    }

    /**
     * Obtiene los Diseños de Examen que pertenecen a un Curso.
     *
     * @param curso Curso por el que quiero filtrar los Diseños de Examen.
     * @return Coleccion de Diseños de Examen que pertenecen a un Curso.
     */
    public ArrayList<DiseñoExamen> getDiseñoExamen(Curso curso) {
        ArrayList<DiseñoExamen> colDiseñoExamen = new ArrayList<DiseñoExamen>();
        DAODiseñoExamen dAODiseñoExamen = new DAODiseñoExamen();
        colDiseñoExamen = dAODiseñoExamen.getDiseñosExamenes(curso);
        return colDiseñoExamen;
    }

    /**
     * Obtiene todas las preguntas que pretenecen a un Diseño de Examen.
     *
     * @param diseñoExamen Diseño de Examen por el cual quiero filtrar las
     * preguntas.
     * @return Coleccion de Preguntas que pretenecen a un Diseño de Examen.
     */
    public ArrayList<Pregunta> getPreguntasPorDE(DiseñoExamen diseñoExamen) {
        ArrayList<Pregunta> colPreguntaPorDE = new ArrayList<Pregunta>();
        DAOPregunta dAOPregunta = new DAOPregunta();
        colPreguntaPorDE = dAOPregunta.getPreguntasPorDiseñoExamen(diseñoExamen);
        return colPreguntaPorDE;
    }

    /**
     * Obtiene todas las preguntas que tienen como ID el contenido en una
     * Coleccion de IDs.
     *
     * @param colID Coleccion de IDs por los que se busca a la pregunta.
     * @return Coleccion de preguntas que tienen los IDs requeridos.
     */
    public ArrayList<Pregunta> getPreguntasPorId(ArrayList<Integer> colID) {
        ArrayList<Pregunta> colPregunta = new ArrayList<>();
        DAOPregunta dAOPregunta = new DAOPregunta();
        colPregunta = dAOPregunta.getPreguntasPorID(colID);
        return colPregunta;
    }

    /**
     * Busca todas las preguntas de la BD.
     *
     * @return Coleccion de preguntas que estan contenidas en la BD.
     */
    public ArrayList<Pregunta> buscarTodasLasPreguntas() {
        ArrayList<Pregunta> colPregunta = new ArrayList<>();
        DAOPregunta dAOPregunta = new DAOPregunta();
        colPregunta = dAOPregunta.buscarTodasLasPreguntas();
        return colPregunta;

    }

    /**
     * Obtiene todos las preguntas que pertencen a una Institucion. Notar que
     * las preguntas devueltas no son completas.
     *
     * @param institucionSeleccionada coleccion con el cual comparar las
     * preguntas.
     * @return Coleccion de preguntas de la Institucion.
     */
    public ArrayList<Pregunta> buscarPreguntasPorInstitucion(Institucion institucionSeleccionada) {
        ArrayList<Pregunta> colPregunta = new ArrayList<>();
        DAOPregunta dAOPregunta = new DAOPregunta();
        colPregunta = dAOPregunta.buscarTodasLasPreguntasPorInstitucion(institucionSeleccionada);
        return colPregunta;
    }

    /**
     * Obtiene todos las preguntas que pertencen a un Curso. Notar que las
     * preguntas devueltas no son completas.
     *
     * @param cursoSeleccionado con el cual comparar las preguntas.
     * @return Coleccion de preguntas del Curso.
     */
    public ArrayList<Pregunta> buscarPreguntasPorCurso(Curso cursoSeleccionado) {
        ArrayList<Pregunta> colPregunta = new ArrayList<>();
        DAOPregunta dAOPregunta = new DAOPregunta();
        colPregunta = dAOPregunta.buscarTodasLasPreguntasPorCurso(cursoSeleccionado);
        return colPregunta;
    }

    /**
     * Busca todos los exámenes sin curso.
     * 
     * @return lista de exámenes sin curso.
     */
    public ArrayList<DiseñoExamen> getDiseñosExamenesSinCurso() {
        return new DAODiseñoExamen().getDiseñosExamenesSinCurso();
    }

    public ArrayList<DiseñoExamen> getDiseñosExamenes() {
        return new DAODiseñoExamen().getDiseñosExamenes();
    }

}
