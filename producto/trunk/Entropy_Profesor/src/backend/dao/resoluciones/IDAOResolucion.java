package backend.dao.resoluciones;

import backend.examenes.Examen;
import backend.resoluciones.Resolucion;
import java.util.ArrayList;

/**
 * Interfaz que especifica las operaciones de cualquier objeto que maneje la
 * persistencia de las resoluciones.
 *
 * @author Denise
 */
public interface IDAOResolucion {

    /**
     * Almacena al resolución del alumno en la base de datos.
     *
     * @param resolucion resolución a almacenar.
     * @return true si la acción fue efectiva, false de lo contrario.
     */
    public boolean guardarResolucion(Resolucion resolucion);

    /**
     * Busca y devuelve una resolución por su ID , con las respuestas y
     * preguntas relacionadas seteadas.
     *
     * @param idResolucion identificador de la resolución a buscar.
     * @return la resolución si la encuentra, null de lo contrario
     */
    public Resolucion getResolucionCompleta(int idResolucion);

    /**
     * Busca todas las resoluciones de un examen.
     *
     * @param examen objeto Examen con el id del mismo.
     * @return las resoluciones del examen, una lista vacía si no se encontró
     * ninguna.
     */
    public ArrayList<Resolucion> getResoluciones(Examen examen);
    
    
    
        /**
     * Busca todas las resoluciones de un Alumno.
     *
     * @param idAlumno identificador del alumno.
     * @return las resoluciones del Alumno, una lista vacía si no se encontró
     * ninguna.
     */
    public ArrayList<Resolucion> getResolucionesDeUnAlumno(int idAlumno);
    
    public ArrayList<Resolucion> getResolucionesByCurso(int idCurso);
    
    public ArrayList<Resolucion> getResolucionesByInstitucion(int idInstitucion);

}
