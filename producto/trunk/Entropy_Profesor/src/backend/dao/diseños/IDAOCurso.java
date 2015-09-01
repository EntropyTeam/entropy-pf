package backend.dao.diseños;

import backend.diseños.Curso;
import backend.diseños.Institucion;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Clase que representa el objeto interfaz para la entidad curso.
 * @author Jose Ruiz
 */
public interface IDAOCurso {

    /**
     * Obtiene un curso almacenada en la base de datos.
     * @param intCursoId variable para identificar el curso a recuperar.
     * @return  retorna la pregunta almacenada  correspondiente al id de curso pasado por parametro.
     */
    public Curso recuperarCurso(int intCursoId);

    /**
     * Obtiene todos los cursos de una institucion pasada por parametro y en caso de mandarle un parametro del nombre de curso obtiene todos los cursos.
     * @param institucion variable que representa una institucion.
     * @param strNombre varaible para la busqueda del curso.
     * @return retorna una coleccion de todos los cursos para una institucion pasada por parametro y en caso de especificarse un filtro de busqueda te retornara todos los cursos que coincidan con la busqueda del filtro.
     */
    public ArrayList<Curso> recuperarTodosLosCursos(Institucion institucion, String strNombre);

    /**
     * Almacena una curso pasada por parámetro.
     * @param curso curso a almacenar.
     * @param institucion institucion perteneciente al curso a guardar.
     * @param editar variable que verifica si se a guardar o editar.
     */
    public void guardarCurso(Curso curso, Institucion institucion, boolean editar);

    /*Metodo que devuelve el id del ultimo curso almacenado en la BD
    */
    public int idCurso(); 

    /**
     * Metodo que se encarga de borrar un curso pasado por parametro.
    *@param intCursoId  variable que representa el curso que se desea borrar.
    *@param intInstitucionId  variable que representa la institucion a la cual pertenece el curso que se desea borrar.
    */
    public void borrarCurso(int intCursoId, int intInstitucionId);

}
