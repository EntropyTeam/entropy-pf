package backend.dao.alumnos;

import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.usuarios.Alumno;
import java.util.ArrayList;

/**
 *
 * @author Denise
 */
public interface IDAOAlumno {
    public ArrayList<Alumno> buscarAlumnos(String strNombre, String strApellido, 
        String strDocumento, String strLegajo, Institucion institucion, 
        Curso curso);
}
