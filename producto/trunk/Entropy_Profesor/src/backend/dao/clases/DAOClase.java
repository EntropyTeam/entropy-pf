package backend.dao.clases;

import backend.dao.diseños.*;
import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.DiseñoExamen;
import backend.diseños.Tema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class DAOClase   {

    public void eliminarClase(Connection conexion) throws SQLException {
        PreparedStatement psConsulta;
        String strConsulta = "DELETE FROM clase WHERE idClase = ?";
        psConsulta = conexion.prepareStatement(strConsulta);

        psConsulta.execute();
    }


}
