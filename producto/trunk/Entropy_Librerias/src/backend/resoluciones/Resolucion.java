package backend.resoluciones;

import backend.usuarios.Alumno;
import backend.examenes.Examen;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class Resolucion implements Serializable {

    private Alumno alumno;
    private int intTiempoEmpleado;
    private int intID;
    private ArrayList<Respuesta> colRespuestas;
    private Examen examen;
    private boolean blnAnulada;
    private boolean fueEnviadaPorEmail;
    private String strJustificacionAnulacion;

    public Resolucion() {
        this.intID = -1;
        this.fueEnviadaPorEmail = false;
        this.blnAnulada = false;
    }

    public Resolucion(Alumno alumno, int intTiempoEmpleado, ArrayList<Respuesta> colRespuestas) {
        this.intID = -1;
        this.fueEnviadaPorEmail = false;
        this.alumno = alumno;
        this.intTiempoEmpleado = intTiempoEmpleado;
        this.colRespuestas = colRespuestas;
        this.blnAnulada = false;
    }

    public Resolucion(int intID, Alumno alumno, int intTiempoEmpleado, ArrayList<Respuesta> colRespuestas) {
        this.intID = intID;
        this.fueEnviadaPorEmail = false;
        this.alumno = alumno;
        this.intTiempoEmpleado = intTiempoEmpleado;
        this.colRespuestas = colRespuestas;
        this.blnAnulada = false;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public int getIntTiempoEmpleado() {
        return intTiempoEmpleado;
    }

    public void setIntTiempoEmpleado(int intTiempoEmpleado) {
        this.intTiempoEmpleado = intTiempoEmpleado;
    }

    public ArrayList<Respuesta> getColRespuestas() {
        return colRespuestas;
    }

    public void setColRespuestas(ArrayList<Respuesta> colRespuestas) {
        this.colRespuestas = colRespuestas;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public boolean esCorreccionCompleta() {
        return getCantidadPreguntasCorregidas() == colRespuestas.size();
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public boolean isBlnAnulada() {
        return blnAnulada;
    }

    public void setBlnAnulada(boolean blnAnulada) {
        this.blnAnulada = blnAnulada;
    }

    public String getStrJustificacionAnulacion() {
        return strJustificacionAnulacion;
    }

    public void setStrJustificacionAnulacion(String strJustificacionAnulacion) {
        this.strJustificacionAnulacion = strJustificacionAnulacion;
    }

    public boolean fueEnviadaPorEmail() {
        return fueEnviadaPorEmail;
    }

    public void setFueEnviadaPorEmail(boolean fueEnviadaPorEmail) {
        this.fueEnviadaPorEmail = fueEnviadaPorEmail;
    }
    
    @Override
    public String toString() {
        return alumno.getStrNombre();
    }

    /**
     * Calcula y devuelve la calificaciób de total de la resolución.
     *
     * @return la calificación total, suma de las calificaciones de todas las
     * respuestas.
     */
    public double getCalificacion() {
        double dblCalificacion = 0;
        for (Respuesta rta : colRespuestas) {
            double dblCalificacionRta = rta.getCalificacion();
            if (dblCalificacionRta > 0) {
                dblCalificacion += dblCalificacionRta;
            }
        }
        return dblCalificacion;
    }

    /**
     * Devuelve de la cantidad de respuestas que han sido corregidas.
     *
     * @return cantidad de respuestas que han sido corregidas.
     */
    public int getCantidadPreguntasCorregidas() {
        int intNoCorregidas = colRespuestas.size();
        for (Respuesta respuesta : colRespuestas) {
            if (respuesta.getCalificacion() < 0) {
                intNoCorregidas--;
            }
        }
        return intNoCorregidas;
    }

    /**
     * Devuelve de la cantidad de respuestas que han sido respondidas por el
     * alumno..
     *
     * @return cantidad de respuestas que han sido respondidas.
     */
    public int getCantidadPreguntasRespondidas() {
        int intRespondidas = 0;
        for (Respuesta respuesta : colRespuestas) {
            if (respuesta.fueRespondida()) {
                intRespondidas++;
            }
        }
        return intRespondidas;
    }

    public boolean estaAprobada() throws Exception {
        double dblCalificacion = getCalificacion();
        double dblPuntajeTotal = getExamen().getPuntajeTotal();
        return (100 * dblCalificacion / dblPuntajeTotal) >= examen.getDblPorcentajeAprobacion();
    }
    
    public double getPorcentajeAprobacion(){
        double dblCalificacion = getCalificacion();
        double dblPuntajeTotal = getExamen().getPuntajeTotal();
        return 100 * dblCalificacion / dblPuntajeTotal;
    }
    
    public String getCalificacionDeTrajo(){
        Double dblCalificacion = this.getCalificacion()/this.getExamen().getPuntajeTotal();
            DecimalFormat format = new DecimalFormat("##.00");
            String strCalificacion = format.format(dblCalificacion * 100);
            return strCalificacion;
    }
}
