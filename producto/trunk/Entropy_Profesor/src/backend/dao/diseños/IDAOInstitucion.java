package backend.dao.diseños;

import backend.diseños.Institucion;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Clase que representa el objeto interfaz para la entidad Institucion.
 * @author Jose Ruiz
 */
public interface IDAOInstitucion {

    /**
     * Obtiene una institucion almacenada en la base de datos.
     * @param intInstitucion identificador de la pregunta a obtener.
     * @return la pregunta almacenada.
     */
    public Institucion recuperarInstitucion(int intInstitucion);

    /**
     * Recupera todas las instituciones segun el parametro en caso de quedar vacio recuperara todas las instituciones.
     * @param strNombre variable para realizar la busqueda de las instituciones
     * @return una coleccion de preguntas segun el filtro, en caso de estar vacio retornara todas
     */
    public ArrayList<Institucion> recuperarTodasLasInstituciones(String strNombre);

    /**
     * Metodo que almacena una institucion pasada por parámetro.
     * @param institucion institucion a almacenar.
     * @param editar variable que verifica si se a guardar o editar
     */
    public void guardarInstitucion(Institucion institucion, boolean editar);

    /**
     * Metodo que recibe un id de institucion como parametro y borra la institucion correspondiente a ese id de la BD
     * @param idInstitucion representa el id de la institucion a borrar.
    */
    public void borrarInstitucion(int idInstitucion);

    /**
     * Metodo que trae  el id de la ultima institucion insertada.
     * @return un id que representa la ultima institucion agregada. 
     */
    public int idInstitucion(); 

}
