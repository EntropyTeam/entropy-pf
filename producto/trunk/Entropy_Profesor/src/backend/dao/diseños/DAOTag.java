package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que realiza gestiona el guardado y consulta de tags en la base de 
 * datos.
 * 
 * @author Lucas Cunibertti, y Denise metió mano.
 */
public class DAOTag implements IDAOTag {

    @Override
    public ArrayList<String> getTags() {
        Connection conexion;
        PreparedStatement psConsulta;
        ResultSet rsResultado;
        ArrayList<String> colTags = new ArrayList<>();
        try {
                conexion = DAOConexion.conectarBaseDatos();
                
                String strConsulta = "SELECT " + EntropyDB.DIS_COL_TAG_ID + " FROM " + EntropyDB.DIS_TBL_TAG;
                psConsulta = conexion.prepareStatement(strConsulta);
                
                psConsulta.executeQuery();
                rsResultado = psConsulta.getResultSet();
                while(rsResultado.next())
                {
                    colTags.add(rsResultado.getString(EntropyDB.DIS_COL_TAG_ID));
                }
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return colTags;
    }

    @Override
    public ArrayList<String> getTags(String cadena) {
        Connection conexion;
        PreparedStatement psConsulta;
        ResultSet rsResultado;
        ArrayList<String> colTags = new ArrayList<>();
        try {
                conexion = DAOConexion.conectarBaseDatos();
                                
                String strConsulta = "SELECT " + EntropyDB.DIS_COL_TAG_ID + " "
                        + "FROM " + EntropyDB.DIS_TBL_TAG + " "
                        + "WHERE " + EntropyDB.DIS_COL_TAG_ID + " LIKE (?) LIMIT 10";
                psConsulta = conexion.prepareStatement(strConsulta);
                
                psConsulta.setString(1, cadena.trim().toLowerCase()+"%");
                
                rsResultado = psConsulta.executeQuery();
                while(rsResultado.next())
                {
                    colTags.add(rsResultado.getString(EntropyDB.DIS_COL_TAG_ID));
                }
                rsResultado.close();
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return colTags;
    };

    @Override
    public void guardarTags(ArrayList<String> tags, Connection conexion) throws SQLException{
        for(String tag : tags) {
            String strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_TAG + " ("
                    + EntropyDB.DIS_COL_TAG_ID
                    + ") VALUES (?)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);

            psConsulta.setString(1, tag.trim().toLowerCase());

            // Siempre intento guardar, total si es clave duplicada en la BD es mas rápido que consultar y guardar
            try {
                psConsulta.execute();
            } catch (SQLException e) {;
            }
        }
    }
}
