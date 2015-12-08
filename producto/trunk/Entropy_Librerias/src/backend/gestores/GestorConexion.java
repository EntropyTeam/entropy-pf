package backend.gestores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorConexion {
     // Se encarga de manejar la conexion, hacer la consulta contra la base de datos y devolver el resultset
    // El parametro "parametros" almacena los parametros que se van a utilizar para crear la consulta contra la base de datos

    public static ResultSet getResultSet(Connection conexion, String consulta, Object[] parametros) {

        ResultSet resultSet = null;

        PreparedStatement statement = null;

        try {
            statement = armarPreparedStatement(conexion, consulta, parametros);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Se produjo el siguiente error: " + e.getMessage());
        }

        return resultSet;
    }

    // Arma un PreparedStatement en base a los parametros contenidos en el arreglo que se pasa por parametro y en base
    // a la consulta tambien pasada por parametro
    public static PreparedStatement armarPreparedStatement(Connection conexion, String consulta, Object[] parametros) throws SQLException {
        PreparedStatement statement = conexion.prepareStatement(consulta);

        if (parametros != null) {
            // Recorremos el arreglo de parametros y vamos arreglando el PreparedStatement
            for (int i = 0; i < parametros.length; i++) {
                // Comprobamos si el elemento en la posicion i es de tipo String, int, etc
                if (parametros[i] instanceof String) {
                    statement.setString(i + 1, (String) parametros[i]);
                } else if (parametros[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) parametros[i]);
                }else if(parametros[i] instanceof Long){    
                    statement.setLong(i + 1, (Long) parametros[i]);
                };
            }
        }
        return statement;
    }
}
