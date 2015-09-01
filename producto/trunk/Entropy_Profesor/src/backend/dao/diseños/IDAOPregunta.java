package backend.dao.diseños;

import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Filtro;
import backend.diseños.Institucion;
import backend.diseños.Pregunta;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones necesarias en los manejadores DAO de los
 * tags almacenados en la base de datos.
 *
 * @author Lucas Cunibertti comentada por GastonNoves
 */
public interface IDAOPregunta {

    /**
     * Obtiene una pregunta almacenada en la base de datos.
     *
     * @param intPreguntaId identificador de la pregunta a obtener.
     * @return la pregunta almacenada.
     */
    public Pregunta getPregunta(int intPreguntaId);

    /**
     * Almacena una pregunta pasada por parámetro.
     *
     * @param pregunta pregunta a almacenar.
     * @param intDiseñoExamenId identificador del diseño del examen.
     * @param conexion la conexionbierta.
     * @throws java.sql.SQLException
     */
    public void guardarPregunta(Pregunta pregunta, int intDiseñoExamenId, Connection conexion) throws SQLException;

    /**
     * Obtiene todos las preguntas que responden a una cadena de Tag pasada por
     * parámetro.Notar que las preguntas devueltas no son completas.
     *
     * @param strCadena string con el cual comparar las preguntas.
     * @return Coleccion de preguntas de examenes que responden a una cadena de
     * Tag.
     */
    public ArrayList<Pregunta> getPreguntaPorTag(String strCadena);

    /**
     * Obtiene todos las preguntas que responden alguno de los filtros pasados
     * por parametro. Notar que las preguntas devueltas no son completas.
     *
     * @param colFiltro coleccion con el cual comparar las preguntas.
     * @return Coleccion de preguntas de examenes que responden a los filtros
     * pasados.
     */
    public ArrayList<Pregunta> getPreguntasPorFiltros(ArrayList<Filtro> colFiltro);

    /**
     * Obtiene todos las preguntas que pertencen a un conjunto de IDs.
     *
     * @param colIDPregunta coleccion con el cual comparar las preguntas.
     * @return Coleccion de preguntas de examenes que cumplen con el conjunto de
     * IDs.
     */
    public ArrayList<Pregunta> getPreguntasPorID(ArrayList<Integer> colIDPregunta);

    /**
     * Obtiene todos las preguntas que pertencen a un Diseño de Examen. Notar
     * que las preguntas devueltas no son completas.
     *
     * @param diseñoExamen coleccion con el cual comparar las preguntas.
     * @return Coleccion de preguntas del Diseño de Examen.
     */
    public ArrayList<Pregunta> getPreguntasPorDiseñoExamen(DiseñoExamen diseñoExamen);

    /**
     * Obtiene todos las preguntas que pertencen a una Institucion. Notar que
     * las preguntas devueltas no son completas.
     *
     * @param institucionSeleccionada coleccion con el cual comparar las
     * preguntas.
     * @return Coleccion de preguntas de la Institucion.
     */
    public ArrayList<Pregunta> buscarTodasLasPreguntasPorInstitucion(Institucion institucionSeleccionada);

    /**
     * Obtiene todos las preguntas que pertencen a un Curso. Notar que las
     * preguntas devueltas no son completas.
     *
     * @param cursoSeleccionado con el cual comparar las preguntas.
     * @return Coleccion de preguntas del Curso.
     */
    public ArrayList<Pregunta> buscarTodasLasPreguntasPorCurso(Curso cursoSeleccionado);
    
    /**
     * Elimina todas las preguntas de un diseño de examen.
     * 
     * @param diseñoExamen diseño cuyas preguntas deben eliminarse.
     * @param conexion conexión a la base de datos.
     * @throws java.sql.SQLException
     */
    public void eliminarPreguntas (DiseñoExamen diseñoExamen, Connection conexion) throws SQLException;
}
