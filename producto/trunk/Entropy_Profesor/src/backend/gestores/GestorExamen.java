package backend.gestores;

import backend.auxiliares.Mensajes;
import backend.dao.diseños.DAOCurso;
import backend.dao.examenes.DAOExamen;
import backend.dao.examenes.DAOPreguntaExamen;
import backend.dao.resoluciones.DAOResolucion;
import backend.dao.resoluciones.DAORespuesta;
import backend.dao.usuarios.DAOAlumno;
import backend.diseños.Curso;
import backend.examenes.EstadoExamen;
import backend.examenes.Examen;
import backend.reporte.GestorGraficos;
import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import frontend.estadisticas.PanelEstadisticasExamen;
import frontend.inicio.VentanaPrincipal;
import frontend.resoluciones.PanelResoluciones;
import frontend.resoluciones.PanelRespuesta;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Singleton que representa al gestor de administración de exámenes.
 *
 * @author Denise
 */
public class GestorExamen {

    private static GestorExamen INSTANCIA = null;

    private GestorExamen() {
    }

    private synchronized static void createInstance() {
        if (INSTANCIA == null) {
            INSTANCIA = new GestorExamen();
        }
    }

    public static GestorExamen getInstancia() {
        createInstance();
        return INSTANCIA;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public ArrayList<Examen> getExamen(Curso curso) {
        return new DAOExamen().getExamenes(curso);
    }

    public void guardarExamen(Examen examen) {
        examen.setDteFecha(new Date());
        examen.setIntEstado(EstadoExamen.DESARROLLANDO);
        DAOExamen daoDiseñoExamen = new DAOExamen();
        daoDiseñoExamen.guardarExamen(examen);
    }

    public boolean verResolucion(Examen examen) {
        examen.setColPreguntas(new DAOPreguntaExamen().getPreguntasPorExamen(examen));
        ArrayList<Resolucion> colResoluciones = new DAOResolucion().getResoluciones(examen);
        if (colResoluciones.isEmpty()) {
            Mensajes.mostrarInformacion("El examen no posee resoluciones.");
            return false;
        }
        for (Resolucion resolucion : colResoluciones) {
            resolucion.setExamen(examen);
        }
        PanelResoluciones pnlResoluciones = new PanelResoluciones(examen, colResoluciones);
        pnlResoluciones.setName("Ver resoluciones");
        VentanaPrincipal.getInstancia().ocultarMenu();
        VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(pnlResoluciones);
        VentanaPrincipal.getInstancia().setTitle("Resoluciones - " + examen.getStrNombre());
        if (VentanaPrincipal.getInstancia().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            VentanaPrincipal.getInstancia().pack();
        }
        return true;
    }

    public void verRespuestas(PanelResoluciones pnlPadre, Resolucion resolucion) {
        try {
            if (resolucion.getExamen().getCurso().getStrNombre() == null) {
                resolucion.getExamen().setCurso(new DAOCurso().recuperarCurso(resolucion.getExamen().getCurso().getIntCursoId()));
            }            
        } catch (Exception e){
            System.err.println("El curso asociado a la resolución es null.");
        }
        resolucion.setAlumno(new DAOAlumno().getAlumnoByResolucion(resolucion.getIntID()));
        PanelRespuesta pnlRespuestas = new PanelRespuesta(pnlPadre, resolucion);
        pnlRespuestas.setName("Ver respuestas");
        VentanaPrincipal.getInstancia().ocultarMenu();
        VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(pnlRespuestas);
        VentanaPrincipal.getInstancia().setTitle("Examen de " + resolucion.toString() + " - " + resolucion.getExamen().getStrNombre());
        if (VentanaPrincipal.getInstancia().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            VentanaPrincipal.getInstancia().pack();
        }
    }

    public boolean actualizarRespuesta(Respuesta respuesta) {
        return new DAORespuesta().actualizarRespuesta(respuesta);
    }

    public boolean actualizarEstadoExamen(int intEstado, int intExamenId) {
        return new DAOExamen().actualizarEstado(intEstado, intExamenId);
    }

    /**
     * Chequea si todas las resoluciones han sido corregidas, y de ser el caso
     * cambia el estado del examen a CORREGIDO. Sino, se asigna un estado
     * FINALIZADO.
     *
     * @param intExamenId id del examen a actualizar
     * @return true si la actualización fue exitosa, false de lo contrario
     */
    public boolean actualizarEstadoExamen(int intExamenId) {
        return new DAOExamen().actualizarEstado(intExamenId);
    }

    /**
     * Permite cancelar un examen.
     *
     * @param intExamenId id del examen a cancelar.
     * @param strMotivoCancelacion descripción del motivo de cancelación.
     * @return true si la cancelación fue exitosa, false de lo contrario.
     */
    public boolean cancelarExamen(int intExamenId, String strMotivoCancelacion) {
        return new DAOExamen().cancelarExamen(intExamenId, strMotivoCancelacion);
    }

    /**
     * Permite calificar las respuestas manuales que aún no poseen calificación.
     *
     * @param pnlPadre panel al cual retornar luego.
     * @param examen  examen cuyas respuestas se quieren corregir.
     * @return false si no existen respuestas por corregir, true de lo contrario.
     */
    public boolean calificarRespuestasSinCalificacion(JPanel pnlPadre, Examen examen) {
        ArrayList<Respuesta> colRespuestas = new DAORespuesta().getRespuestasNoCalificadas(examen.getIntExamenId());
        if (colRespuestas.isEmpty()) {
            Mensajes.mostrarInformacion("El examen no posee respuestas sin corregir.");
            return false;
        }
        PanelRespuesta pnlRespuestas = new PanelRespuesta(pnlPadre, colRespuestas, examen);
        pnlRespuestas.setName("Ver respuestas");
        VentanaPrincipal.getInstancia().ocultarMenu();
        VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(pnlRespuestas);
        VentanaPrincipal.getInstancia().setTitle("Respuestas sin corrección - " + examen.getStrNombre());
        if (VentanaPrincipal.getInstancia().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            VentanaPrincipal.getInstancia().pack();
        }
        return true;
    }

    public boolean verEstadisticas(Examen examenSeleccionado) {
        Examen examen = new DAOExamen().getExamen(examenSeleccionado.getIntExamenId());
        examen.setColPreguntas(new DAOPreguntaExamen().getPreguntasPorExamen(examen));
        ArrayList<Resolucion> colResoluciones = new DAOResolucion().getResoluciones(examenSeleccionado);
        if (colResoluciones.isEmpty()) {
            Mensajes.mostrarInformacion("El examen no posee resoluciones.");
            return false;
        }
        for (Resolucion resolucion : colResoluciones) {
            resolucion.setExamen(examen);
        }
        PanelEstadisticasExamen pnlEstadisticas = new PanelEstadisticasExamen(VentanaPrincipal.getInstancia(), new GestorGraficos(colResoluciones));
        pnlEstadisticas.setName("Ver estadísticas");
        VentanaPrincipal.getInstancia().ocultarMenu();
        VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(pnlEstadisticas);
        VentanaPrincipal.getInstancia().setTitle("Estadísticas de examen " + examenSeleccionado.getStrNombre() + " - " + new SimpleDateFormat("dd/MM/yyyy  -  HH:mm").format(examenSeleccionado.getDteFecha()));
        if (VentanaPrincipal.getInstancia().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            VentanaPrincipal.getInstancia().pack();
        }
        return true;
    }

    
}
