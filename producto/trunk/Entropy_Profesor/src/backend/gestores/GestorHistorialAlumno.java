package backend.gestores;

import backend.Presentacion.Presentacion;
import backend.dao.examenes.DAOExamen;
import backend.dao.examenes.DAOPreguntaExamen;
import backend.dao.presentacion.DAOPresentacion;
import backend.dao.presentacion.IDAOPresentacion;
import backend.dao.resoluciones.DAOResolucion;
import backend.examenes.Examen;
import backend.resoluciones.Resolucion;
import java.util.ArrayList;

/**
 *
 * @author Jose
 */
public class GestorHistorialAlumno {
    
    
    public ArrayList<Resolucion> getResoluciones (int idAlumno) {
        ArrayList<Resolucion> colResoluciones = new ArrayList<Resolucion>();
        DAOResolucion dAOResolucion = new DAOResolucion();
        colResoluciones = dAOResolucion.getResolucionesDeUnAlumno(idAlumno);
        return colResoluciones;
    }
    
    
        public Examen getExamen (int idExamen) {
        DAOExamen dAOExamen = new DAOExamen();
        Examen examen = dAOExamen.getExamen(idExamen);
        examen.setColPreguntas(new DAOPreguntaExamen().getPreguntasPorExamen(examen));
        return examen;
    }
    
        public ArrayList<Presentacion> getAsistencias (int idAlumno) {
        ArrayList<Presentacion> colAsistencia = new ArrayList<Presentacion>();
        IDAOPresentacion daoPresentacion = new DAOPresentacion();
        colAsistencia = daoPresentacion.recuperarPresentacionesDeUnAlumno(idAlumno);
        return colAsistencia;
    }
}
