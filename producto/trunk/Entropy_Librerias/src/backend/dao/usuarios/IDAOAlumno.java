package backend.dao.usuarios;

import backend.usuarios.Alumno;

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
    
}
