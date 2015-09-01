package backend.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lucas Cunibertti
 */
public class DAOConexion {
    private static final String strUrl = "jdbc:sqlite:." + File.separator + "db" + File.separator + "data.sqlite";
    private static Connection conConexion = null;

    /**
     * Metodo utilizado para abrir una conexion y retornar una conexión a la base de datos.
     *
     * @return DAOConexion abierta
     */
    public static Connection conectarBaseDatos() {
        try {
            Class.forName("org.sqlite.JDBC");
            if (conConexion == null) {
                conConexion = DriverManager.getConnection(strUrl);
                System.out.println("Conexión a base de datos " + strUrl + " exitosa.");
            }
            
            return conConexion;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Hubo un problema al intentar conectarse con la base de datos " + strUrl + ".\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * Metodo utilizado para cerrar una conexion abierta.
     */
    public static void desconectarBaseDatos() {
        try {
            conConexion.close();
            conConexion = null;
            System.out.println("Desconexión exitosa.");
        } catch (SQLException ex) {
            System.out.println("No se pudo cerrar la conexión.\n" + ex.getMessage());
        }
    }
}