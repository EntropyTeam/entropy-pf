package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.diseños.PerfilUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Lucas Cunibertti
 */
public class DAOPerfilUsuario implements IDAOPerfilUsuario {

    @Override
    public void guardarPerfilUsuario(PerfilUsuario perfilUsuario) {
        try {
            PreparedStatement psConsulta;
            Connection conexion = DAOConexion.conectarBaseDatos();
            
            String strConsulta = "INSERT INTO perfilUsuario(tipoDocumento, documento, nombre, apellido, email, perfilUsuarioId, contraseña) VALUES(?,?,?,?,?,?,?)";
            psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, perfilUsuario.getStrTipoDocumento());
            psConsulta.setInt(2, perfilUsuario.getIntDocumento());
            psConsulta.setString(3, perfilUsuario.getStrNombre());
            psConsulta.setString(4, perfilUsuario.getStrApellido());
            psConsulta.setString(5, perfilUsuario.getStrEmail());
            psConsulta.setString(6, perfilUsuario.getStrPerfilUsuarioId());
            psConsulta.setString(7, perfilUsuario.getStrContraseña());
            psConsulta.execute();
        } catch (SQLException e) {
            System.out.println("A ocurrido un erro mientras se guardaba el perfil del usuario en la BD " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    @Override
    public PerfilUsuario login(String strPerfilUsuarioId, String strContraseña) {
        PerfilUsuario perfilUsuario = null;
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta = "SELECT * FROM perfilUsuario WHERE perfilUsuarioId = ? AND contraseña = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, strPerfilUsuarioId);
            psConsulta.setString(2, strContraseña);
            
            ResultSet rsConsulta = psConsulta.executeQuery();
            if(rsConsulta.next()) {
                String tipoDocumento = rsConsulta.getString(1);
                int documento = rsConsulta.getInt(2);
                String nombre = rsConsulta.getString(3);
                String apellido = rsConsulta.getString(4);
                String email = rsConsulta.getString(5);
                String perfilUsuarioId = rsConsulta.getString(6);
                String contraseña = rsConsulta.getString(7);
                perfilUsuario = new PerfilUsuario(tipoDocumento, documento, nombre, apellido, email, perfilUsuarioId, contraseña);
            }
        } catch (SQLException e) {
            System.out.println("Ocurrio un error mientras se recuperaba el perfil del usuario " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        
        return perfilUsuario;
    }
}
