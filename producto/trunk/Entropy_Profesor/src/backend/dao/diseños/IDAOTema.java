package backend.dao.diseños;

import backend.diseños.DiseñoExamen;
import backend.diseños.Tema;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones necesarias en los manejadores DAO de los
 * temas almacenados en la base de datos.
 *
 * @author Denise
 */
public interface IDAOTema {

    /**
     * Obtiene todos los temas almacenados.
     *
     * @return lista de temas almacenados
     */
    public ArrayList<Tema> getTemas();

    /**
     * Obtiene todos los temas que contienen la cadena pasada por parámetro.
     *
     * @param strCadena string con el cual comparar los temas
     * @return lista de temas que contienen la cadena pasada por parámetro
     */
    public ArrayList<Tema> getTemas(String strCadena);

    /**
     * Obtiene todos los temas que pertenecen a un diseño de examen.
     *
     * @param intDiseñoExamenId id del diseño de examen
     * @return lista de temas que estan en ese diseño de examen
     */
    public ArrayList<Tema> getTemasPorExamen(int intDiseñoExamenId);

    /**
     * Almacena temas pasados por parámetro.
     *
     * @param colTemas lista de temas a almacenar.
     */
    public void guardarTemas(ArrayList<Tema> colTemas);

    /**
     * Almacena un tema pasado por parámetro.
     *
     * @param tema tema a almacenar.
     * @param intDiseñoExamenId el ID del diseño de examen.
     * @param conexion la conexion por ser parte de una transaccion.
     * @throws java.sql.SQLException
     */
    public void guardarTema(Tema tema, int intDiseñoExamenId, Connection conexion) throws SQLException;

    /**
     * Eliminar los temas de un diseño de la bd.
     *
     * @param diseño diseño cuyos temas eliminar.
     * @param conexion conexión a la bd.
     * @throws java.sql.SQLException
     */
    public void eliminarTemas(DiseñoExamen diseño, Connection conexion) throws SQLException;

    /**
     * Comprueba si un determinado tema para un determinado diseño ya posee un
     * ID asignado; es decir, si ese tema ya está guardado en la BD.
     * 
     * @param idDiseño diseño al cual pertenece el tema.
     * @param strTema descripción del tema.
     * @param conexion conexión a la BD.
     * @return el ID del tema si existe, 0 de lo contrario.
     * @throws SQLException 
     */
    public int getIDsiExiste(int idDiseño, String strTema, Connection conexion) throws SQLException;
}
