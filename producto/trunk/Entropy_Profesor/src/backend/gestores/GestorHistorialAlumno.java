package backend.gestores;

import backend.Presentacion.Presentacion;
import backend.dao.diseños.DAOCurso;
import backend.dao.examenes.DAOExamen;
import backend.dao.examenes.DAOPreguntaExamen;
import backend.dao.presentacion.DAOPresentacion;
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
        return new DAOResolucion().getResolucionesDeUnAlumno(idAlumno);
    }
        
    public Examen getExamen (int idExamen) {
        DAOExamen dAOExamen = new DAOExamen();
        Examen examen = dAOExamen.getExamen(idExamen);
        examen.setCurso(new DAOCurso().recuperarCurso(examen.getCurso().getIntCursoId()));        
        examen.setColPreguntas(new DAOPreguntaExamen().getPreguntasPorExamen(examen));
        return examen;
    }
    
        public ArrayList<Presentacion> getAsistencias (int idAlumno) {
        return new DAOPresentacion().recuperarPresentacionesDeUnAlumno(idAlumno);
    }
}
