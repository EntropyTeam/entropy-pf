package backend.gestores;

import backend.dao.diseños.DAOCurso;
import backend.dao.diseños.DAOInstitucion;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import java.util.ArrayList;

/**
 *
 * @author Pelito
 */
public class GestorCursosEInstituciones {

    private ArrayList<Curso> cursos;
    private ArrayList<Institucion> instituciones;

    public GestorCursosEInstituciones() {
        this.cursos = new ArrayList<Curso>();
        this.instituciones = new ArrayList<Institucion>();
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public ArrayList<Institucion> getInstituciones() {
        return instituciones;
    }

    public void setInstituciones(ArrayList<Institucion> instituciones) {
        this.instituciones = instituciones;
    }

    public boolean validarCamposCurso() {
        return true;
    }

    public boolean validarCamposInstituciones() {
        return true;
    }

    public void guardarInstitucion(Institucion institucion, boolean blnEditar) {
        DAOInstitucion daoInstitucion = new DAOInstitucion();
        daoInstitucion.guardarInstitucion(institucion, blnEditar);
    }

    public void guardadCurso(Curso curso, Institucion institucion, boolean blnEditar) {
        DAOCurso daoCurso = new DAOCurso();
        daoCurso.guardarCurso(curso, institucion, blnEditar);
    }

    public ArrayList<Institucion> recuperarTodasLasInstituciones(String strNombre) {
        DAOInstitucion daoInstitucion = new DAOInstitucion();
        return daoInstitucion.recuperarTodasLasInstituciones(strNombre);
    }

    public Curso recuperarCurso(int intICurso) {
        DAOCurso daoCurso = new DAOCurso();
        return daoCurso.recuperarCurso(intICurso);
    }

    public void eliminarCurso(int intIdCurso, int intInstitucionId) {
        DAOCurso daoCurso = new DAOCurso();
        daoCurso.borrarCurso(intIdCurso, intInstitucionId);
    }

    public void eliminarInstitucion(int intInstitucionId) {
        DAOInstitucion daoInstitucion = new DAOInstitucion();
        daoInstitucion.borrarInstitucion(intInstitucionId);
    }

    public int ultimaInstitucion() {
        DAOInstitucion daoIsAOInstitucion = new DAOInstitucion();
        return daoIsAOInstitucion.idInstitucion();
    }

    public int ultimoCurso() {
        DAOCurso daoCurso = new DAOCurso();
        return daoCurso.idCurso();
    }

    /*
     * Metodo para buscar un curso de acuerdo a un numero de diseño de examen
     * @param idDiseño, el id del diseño del examen seleccionado para editar
     */
    public Curso buscarCurso(int idDiseño) {
        DAOCurso daoCurso = new DAOCurso();
        return daoCurso.buscarCurso(idDiseño);
    }

    public boolean esAsociadoAExamenODiseño(Curso curso) {
        return new DAOCurso().tieneDiseñosOExamenesAsociados(curso.getIntCursoId());
    }

    public boolean esAsociadoAExamenODiseño(Institucion institucion) {
        DAOCurso daoCurso = new DAOCurso();
        for (Curso curso : new GestorDiseñoExamen().getCursosPorFiltro("", institucion)){
            if (daoCurso.tieneDiseñosOExamenesAsociados(curso.getIntCursoId())){
                return true;
            }
        }
        return false;
    }
}
