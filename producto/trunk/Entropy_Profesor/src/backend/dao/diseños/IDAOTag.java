package backend.dao.diseños;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones necesarias en los manejadores DAO de los
 * tags almacenados en la base de datos.
 *
 * @author Denise
 */
public interface IDAOTag {

    /**
     * Obtiene todos los tags almacenados.
     *
     * @return lista de tags almacenados
     */
    public ArrayList<String> getTags();

    /**
     * Obtiene todos los tags que comienzan con la cadena pasada por parámetro.
     *
     * @param cadena string con el cual comparar los tags
     * @return lista de primeros 10 tags que comienzan con la cadena pasada por
     * parámetro
     */
    public ArrayList<String> getTags(String cadena);

    /**
     * Almacena tags pasados por parámetro, todos en minúscula, sin espacions
     * iniciales o finales.
     *
     * @param tags lista de tags a almacenar.
     * @param conexion la conexion abierta.
     * @throws java.sql.SQLException
     */
    public void guardarTags(ArrayList<String> tags, Connection conexion) throws SQLException;
}
