package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.DiseñoExamen;
import backend.diseños.Institucion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que representa el objeto para el acceso a la entidad institucion.
 * 
 * @author Jose Ruiz 
 */
public class DAOInstitucion implements IDAOInstitucion {

    @Override
    public Institucion recuperarInstitucion(int intInstitucion) {
        Institucion institucion = new Institucion();
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            conexion.setAutoCommit(false);
            String strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_INSTITUCION + " "
                        + "WHERE " + EntropyDB.GRL_COL_INSTITUCION_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intInstitucion);
            ResultSet rsConsulta = psConsulta.executeQuery();
            int institucionId = rsConsulta.getInt(EntropyDB.GRL_COL_INSTITUCION_ID);
            String nombre = rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_NOMBRE);
            String strDescripcion = rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION);
            Object logo = rsConsulta.getObject(EntropyDB.GRL_COL_INSTITUCION_LOGO);
            institucion = new Institucion(institucionId, nombre, strDescripcion, logo);

        } catch (SQLException e) {
            System.err.println("Ocurrió un error mientras se recuperaba una institución: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return institucion;
    }

    @Override
    public ArrayList<Institucion> recuperarTodasLasInstituciones(String strNombre) {
        ArrayList<Institucion> instituciones = new ArrayList<>();
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta;
            if (strNombre.equals("")) // Si la cadena esta vacia te trae todas las instituciones  
            {
                strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_INSTITUCION;
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int institucionId = rsConsulta.getInt(EntropyDB.GRL_COL_INSTITUCION_ID);
                    String nombre = rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_NOMBRE);
                    String strDescripcion = rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION);
                    Object logo = rsConsulta.getObject(EntropyDB.GRL_COL_INSTITUCION_LOGO);
                    Institucion institucion = new Institucion(institucionId, nombre, strDescripcion, logo);
                    instituciones.add(institucion);
                }
            } else // si escribe algo te trae los que existan con ese valor de nombre
            {
                strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_INSTITUCION + " "
                        + "WHERE " + EntropyDB.GRL_COL_INSTITUCION_NOMBRE + " LIKE (?)";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, "%" + strNombre + "%");
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int institucionId = rsConsulta.getInt(EntropyDB.GRL_COL_INSTITUCION_ID);
                    String nombre = rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_NOMBRE);
                    String strDescripcion = rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION);
                    Object logo = rsConsulta.getObject(EntropyDB.GRL_COL_INSTITUCION_LOGO);
                    Institucion institucion = new Institucion(institucionId, nombre, strDescripcion, logo);
                    instituciones.add(institucion);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrió un error mientras se recuperaba todas las instituciones: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return instituciones;
    }

    @Override
    public void guardarInstitucion(Institucion institucion, boolean editar) {
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            PreparedStatement psConsulta;
            if (!editar) {
                String strConsulta = "INSERT INTO " + EntropyDB.GRL_TBL_INSTITUCION + " ("
                        + EntropyDB.GRL_COL_INSTITUCION_NOMBRE + ", "
                        + EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION + ", "
                        + EntropyDB.GRL_COL_INSTITUCION_LOGO
                        + ") VALUES (?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, institucion.getStrNombre());
                psConsulta.setString(2, institucion.getStrDescripcion());
                psConsulta.setBytes(3, (byte[])institucion.getImgLogo());
                psConsulta.execute();

            } else {
                String strConsulta = "UPDATE " + EntropyDB.GRL_TBL_INSTITUCION + " "
                        + "SET " + EntropyDB.GRL_COL_INSTITUCION_NOMBRE + " = ?, "
                        + EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION + " = ?, "
                        + EntropyDB.GRL_COL_INSTITUCION_LOGO + " = ? "
                        + "WHERE " + EntropyDB.GRL_COL_INSTITUCION_ID + " = ?";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, institucion.getStrNombre());
                psConsulta.setString(2, institucion.getStrDescripcion());
                psConsulta.setBytes(3, (byte[])institucion.getImgLogo());
                psConsulta.setInt(4, institucion.getIntInstitucionId());
                psConsulta.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institución en la BD: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }
    
    public void guardarInstitucionTransaccion(Institucion institucion, Connection conexion) {
        try {
            String strConsulta = "INSERT INTO " + EntropyDB.GRL_TBL_INSTITUCION + " ("
                        + EntropyDB.GRL_COL_INSTITUCION_NOMBRE + ", "
                        + EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION + ", "
                        + EntropyDB.GRL_COL_INSTITUCION_LOGO
                        + ") VALUES (?,?,?)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, institucion.getStrNombre());
            psConsulta.setString(2, institucion.getStrDescripcion());
            psConsulta.setBytes(3, (byte[])institucion.getImgLogo());
            psConsulta.execute();
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institución en la BD: " + e.toString());
        }
    }

    @Override
    public int idInstitucion() {
        int idUltimaInstitucion = 0;
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta = "SELECT  MAX(" + EntropyDB.GRL_COL_INSTITUCION_ID + ") "
                    + "FROM " + EntropyDB.GRL_TBL_INSTITUCION;
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();
            idUltimaInstitucion = rsConsulta.getInt(1);
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institución en la BD: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return idUltimaInstitucion;
    }

    @Override 

    public void borrarInstitucion(int intInstitucionId) {
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            //conexion.setAutoCommit(false);
            String strConsulta = "DELETE FROM " + EntropyDB.GRL_TBL_INSTITUCION + " "
                    + "WHERE " + EntropyDB.GRL_COL_INSTITUCION_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intInstitucionId);
            psConsulta.execute();
            //Borrar Cursos asociados a la institucion    
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institución en la BD: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    /*
    * Metodo para buscar una institucion de acuerdo a un numero de diseño de examen
    * @param idDiseño, el id del diseño del examen seleccionado para editar
    */
    public Institucion buscarInstitucion(int idCurso) {
        Institucion institucion = null;
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta;
            strConsulta = "SELECT " + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " "
                    + "FROM " + EntropyDB.GRL_TBL_CURSO + " "
                    + "WHERE " + EntropyDB.GRL_COL_CURSO_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idCurso);
            ResultSet rsConsulta = psConsulta.executeQuery();
            
            int institucionId = rsConsulta.getInt(EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID);
            if (!rsConsulta.wasNull()) {
                strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_INSTITUCION + " "
                        + "WHERE " + EntropyDB.GRL_COL_INSTITUCION_ID + " = ?";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, institucionId);
                rsConsulta = psConsulta.executeQuery();
                if (rsConsulta.next()) {
                    institucion = new Institucion();
                    institucion.setIntInstitucionId(rsConsulta.getInt(EntropyDB.GRL_COL_INSTITUCION_ID));
                    institucion.setStrNombre(rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_NOMBRE));
                    institucion.setStrDescripcion(rsConsulta.getString(EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION));
                    institucion.setImgLogo(rsConsulta.getObject(EntropyDB.GRL_COL_INSTITUCION_LOGO));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrio un error mientras se recuperaba una institucion " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();

        }
        return null;
}
}