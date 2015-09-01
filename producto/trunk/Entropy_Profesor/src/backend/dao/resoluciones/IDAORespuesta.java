package backend.dao.resoluciones;

import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaz que especifica las operaciones de cualquier objeto que maneje la
 * persistencia de las respuestas.
 *
 * @author Denise
 */
public interface IDAORespuesta {

    /**
     * Almacena una respuesta en la base de datos.
     *
     * @param respuesta respuesta a almacenar.
     * @param idResolucion identificador de la resolución a la que pertenece.
     * @param conexion conexión a la base de datos que maneja la transacción.
     * @return true si la operación fue exitosa, flse de lo contrario.
     * @throws SQLException
     */
    public boolean guardarRespuesta(Respuesta respuesta, int idResolucion, Connection conexion) throws SQLException;

    /**
     * Obtiene todas las respuestas correspondientes a una resolución.
     *
     * @param resolucion objeto resolución con el identificador setetado.
     * @return lista de respuestas correspondientes a esa resolución, una lista
     * vacía si no encuentra ninguna respuesta.
     */
    public ArrayList<Respuesta> getRespuestas(Resolucion resolucion);

    /**
     * Obtiene una respuesta correspondiente a un ID pasado por parámetro.
     *
     * @param intPreguntaID id de la respuesta a recuperar.
     * @return la respuesta en particular, null si no se encuentra en la bd.
     */
    public Respuesta getRespuesta(int intPreguntaID);

    /**
     * Guarda comentarios agregador por el profesor, y la calificación si es una
     * respuesta de corrección no automática.
     *
     * @param respuesta la respuesta. a actualizar.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean actualizarRespuesta(Respuesta respuesta);

    /**
     * Obtiene todas las respuestas correspondientes a una resolución.
     *
     * @param intIDExamen ID del examen cuyas respuestas no calificadas deben
     * buscarse.
     * @return lista de respuestas no calificadas correspondientes a ese examen,
     * una lista vacía si no encuentra ninguna respuesta.
     */
    public ArrayList<Respuesta> getRespuestasNoCalificadas(int intIDExamen);
}
