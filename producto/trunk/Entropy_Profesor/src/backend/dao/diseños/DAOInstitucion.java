package backend.dao.diseños;

import backend.dao.DAOConexion;
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
            String strConsulta = "SELECT * FROM Institucion WHERE institucionId=?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intInstitucion);
            ResultSet rsConsulta = psConsulta.executeQuery();
            int institucionId = rsConsulta.getInt(1);
            String nombre = rsConsulta.getString(2);
            String strDescripcion = rsConsulta.getString(3);
            Object logo = rsConsulta.getObject(4);
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
                strConsulta = "SELECT * FROM Institucion";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int institucionId = rsConsulta.getInt(1);
                    String nombre = rsConsulta.getString(2);
                    String strDescripcion = rsConsulta.getString(3);
                    Object logo = rsConsulta.getBytes(4);
                    Institucion institucion = new Institucion(institucionId, nombre, strDescripcion, logo);
                    instituciones.add(institucion);
                }
            } else // si escribe algo te trae los que existan con ese valor de nombre
            {
                strConsulta = "SELECT * FROM Institucion WHERE nombre LIKE (?)";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, "%" + strNombre + "%");
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int institucionId = rsConsulta.getInt(1);
                    String nombre = rsConsulta.getString(2);
                    String strDescripcion = rsConsulta.getString(3);
                    Object logo = rsConsulta.getBytes(4);
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
                String strConsulta = "INSERT INTO institucion(nombre, descripcion, logo) VALUES(?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, institucion.getStrNombre());
                psConsulta.setString(2, institucion.getStrDescripcion());
                psConsulta.setBytes(3, (byte[])institucion.getImgLogo());
                psConsulta.execute();

            } else {
                String strConsulta = "UPDATE institucion SET nombre=?, descripcion= ?, logo=? WHERE institucionId=?";
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
            String strConsulta = "INSERT INTO institucion(nombre, descripcion, logo) VALUES(?,?,?)";
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
            String strConsulta = "SELECT  MAX (institucionId) FROM Institucion";
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
            String strConsulta = "DELETE FROM institucion WHERE institucionId=?";
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
    public Institucion buscarInstitucion(int idDiseño) {
        Institucion institucion = new Institucion();
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta;
            strConsulta = "SELECT * FROM disenoexamen WHERE disenoexamenId=?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idDiseño);
            ResultSet rsConsulta = psConsulta.executeQuery();

            DiseñoExamen diseño = new DiseñoExamen();
            diseño.setIntDiseñoExamenId(idDiseño);
            diseño.setStrDescripcion(rsConsulta.getString(4));
            diseño.setStrNombre(rsConsulta.getString(3));

            int id = diseño.getIntDiseñoExamenId();
            strConsulta = "SELECT I.institucionid, I.nombre, I.logo  FROM curso C, disenoexamen DE, Institucion I WHERE de.cursoID=c.cursoID and c.institucionid=i.institucionid and de.disenoexamenId=?";
            psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, id);
            rsConsulta = psConsulta.executeQuery();

            if (rsConsulta.next()) {
                Object logo = rsConsulta.getObject(3);
                institucion.setIntInstitucionId(rsConsulta.getInt(1));
                institucion.setStrNombre(rsConsulta.getString(2));
                institucion.setImgLogo(logo);
                return institucion;
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Ocurrio un error mientras se recuperaba una institucion " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();

        }
        return null;
}
}