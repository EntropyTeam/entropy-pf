package backend.alumnos;

import backend.dao.usuarios.DAOAlumno;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.usuarios.Alumno;
import java.util.ArrayList;

/**
 * Singleton.
 * 
 * @author Denise
 */
public class GestorAlumnos {
    
    private static GestorAlumnos INSTANCIA = null;

    private GestorAlumnos() {
    }

    private synchronized static void createInstance() {
        if (INSTANCIA == null) {
            INSTANCIA = new GestorAlumnos();
        }
    }

    public static GestorAlumnos getInstancia() {
        createInstance();
        return INSTANCIA;
    }
    
    public ArrayList<Alumno> buscarAlumnos(String strNombre, String strApellido, 
            String strDocumento, String strLegajo, Institucion institucion, 
            Curso curso){
        return new DAOAlumno().getAlumnosBy(strNombre, strApellido, strDocumento, strLegajo, institucion, curso);
    }
}
