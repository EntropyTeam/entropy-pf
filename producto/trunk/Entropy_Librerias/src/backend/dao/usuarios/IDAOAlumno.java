package backend.dao.usuarios;

import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.usuarios.Alumno;
import java.util.ArrayList;

/**
 *
 * @author Denise
 */
public interface IDAOAlumno {

    /**
     * Almacena un usuario en la BD.
     *
     * @param alumno Entidad Alumno a almacenar.
     * @return el id del nuevo usuario guardado.
     */
    public int guardarAlumno(Alumno alumno);

    /**
     * Obtiene el ID de un alumno guardado en la BD
     *
     * @param alumno para consultar por su tipo y numero de documento
     * @return el id del alumno.
     */
    public int getAlumnoId(Alumno alumno);
    
    
    /**
     * @param idAlumno 
     * @return el objeto alumno
     */
    public Alumno getAlumno(int idAlumno);
    
    public ArrayList<Alumno> getAlumnosBy(String strNombre, String strApellido, 
        String strDocumento, String strLegajo, Institucion institucion, 
        Curso curso);
        
    public Alumno getAlumnoByResolucion(int intResolucionID);
    
    public Alumno getAlumnoByRespuesta(int intRespuestaID);
}
