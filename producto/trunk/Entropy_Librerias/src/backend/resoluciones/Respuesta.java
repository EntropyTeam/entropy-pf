package backend.resoluciones;

import backend.diseños.Pregunta;
import java.io.Serializable;

/**
 * Clase que representa a la respuesta del alumno.
 *
 * @author gaston
 */
public abstract class Respuesta implements Serializable {

    private String strComentario;
    private int intRespuestaId;
    private int intTipo;

    public Respuesta() {
        this.intRespuestaId = -1;
    }
    
    public Respuesta(int tipoRespuesta){
        this.intTipo = tipoRespuesta;
    }

    //Este constructor no se usa en ningun lado me parece, asi que lo comente porque me sobreescribia el que recibe tipo de respuesta.
//    public Respuesta(int intRespuestaId, int tipo) {
//        this.intRespuestaId = intRespuestaId;
//    }

    public int getIntRespuestaId() {
        return intRespuestaId;
    }

    public void setIntRespuestaId(int intRespuestaId) {
        this.intRespuestaId = intRespuestaId;
    }

    public String getStrComentario() {
        return strComentario;
    }

    public void setStrComentario(String strComentario) {
        this.strComentario = strComentario;
    }

    public int getIntTipo() {
        return intTipo;
    }

    public void setIntTipo(int intTipo) {
        this.intTipo = intTipo;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.intRespuestaId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Respuesta other = (Respuesta) obj;
        if (this.intRespuestaId != other.intRespuestaId) {
            return false;
        }
        return true;
    }

    /**
     * Devuelve la pregunta relacionada con esta respuesta.
     *
     * @return la pregunta relacionada con esta respuesta.
     */
    public abstract Pregunta getPregunta();

    /**
     * Permite saber si la respuesta puede corregirse de manera automática. Este
     * método debiese estar realmente en la clase Pregunta, pero como no existe
     * una PreguntaDesarrollo, y Pregunta no es abstract, no se puede hacer. Es
     * un viaje cambiarlo, lo dejo acá :)
     *
     * @return true si es correción automática, false de lo contrario.
     */
    public abstract boolean esCorreccionAutomatica();

    /**
     * Permite acceder la calificacion de la respuesta. Si la respuesta aún no
     * se ha corregido, devuelve -1.
     *
     * @return la calificacion de la respuesta, -1 si no ha sido corregida aún.
     */
    public abstract double getCalificacion();

    /**
     * Permite saber si la respuesta fue respondida por el alumno.
     *
     * @return true si lo fue, false de lo contrario.
     */
    public abstract boolean fueRespondida();

    /**
     * 
     * @return 
     *      2 si está incompleta,
     *      1 si es correcta,
     *      0 si es incorrecta,
     *     -1 si no ha sido calificada aún..
     */
    public int esCorrecta() {
        double dblCalificacion = getCalificacion();
        double dblPuntajeMaximo = getPregunta().getDblPuntaje();
        
        if (dblCalificacion < 0) return -1;
        else if (dblCalificacion == 0) return 0;
        else if (dblCalificacion == dblPuntajeMaximo) return 1;
        else return 2;
    }

}
