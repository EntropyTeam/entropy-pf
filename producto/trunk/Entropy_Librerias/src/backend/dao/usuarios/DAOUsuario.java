package backend.dao.usuarios;

import backend.dao.DAOConexion;
import backend.usuarios.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manejo de la persistenvia de entidades del tipo usuario.
 * 
 * @author denise
 */
public class DAOUsuario implements IDAOUsuario {

    @Override
    public boolean guardarUsuario(Usuario usuario) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean blnExito = false;
        try {
            
            String strConsulta = "SELECT 1 FROM usuario WHERE (tipoDocumento = ? AND documento  = ?) OR legajo = ?";
            PreparedStatement psconsulta = conexion.prepareStatement(strConsulta);
            psconsulta.setString(1, usuario.getStrTipoDocumento());
            psconsulta.setInt(2, usuario.getIntNroDocumento());
            psconsulta.setString(3, usuario.getStrLegajo());

            String strUpdate;
            PreparedStatement psUpdate;

            if (!psconsulta.executeQuery().next()) { //Usuario nuevo
                strUpdate = "INSERT INTO usuario (usuarioId, nombre, apellido, tipoDocumento, documento, email, foto, descripcion, legajo) VALUES (null,?,?,?,?,?,?,?,?)";
                psUpdate = conexion.prepareStatement(strUpdate);
                psUpdate.setString(1, usuario.getStrNombre());
                psUpdate.setString(2, usuario.getStrApellido());
                psUpdate.setString(3, usuario.getStrTipoDocumento());
                psUpdate.setInt(4, usuario.getIntNroDocumento());
                psUpdate.setString(5, usuario.getStrEmail());
                psUpdate.setObject(6, usuario.getImgFoto());
                psUpdate.setString(7, usuario.getStrDescripcion());
                psUpdate.setString(8, usuario.getStrLegajo());
            } else {
                strUpdate = "UPDATE usuario SET nombre = ?, apellido = ?, tipoDocumento = ?, documento = ?, email = ?, foto = ?, descripcion = ?, legajo = ? WHERE tipoDocumento = ? AND documento  = ?";
                psUpdate = conexion.prepareStatement(strUpdate);
                psUpdate.setString(1, usuario.getStrNombre());
                psUpdate.setString(2, usuario.getStrApellido());
                psUpdate.setString(3, usuario.getStrTipoDocumento());
                psUpdate.setInt(4, usuario.getIntNroDocumento());
                psUpdate.setString(5, usuario.getStrEmail());
                psUpdate.setObject(6, usuario.getImgFoto());
                psUpdate.setString(7, usuario.getStrDescripcion());
                psUpdate.setString(8, usuario.getStrLegajo());
                psUpdate.setString(9, usuario.getStrTipoDocumento());
                psUpdate.setInt(10, usuario.getIntNroDocumento());
            }

            if (psUpdate.executeUpdate() > 0)
                blnExito = true;

        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                System.err.println("Problemas la guardar/actualizar un usuario en la BD: " + ex.getMessage());
            }
        }
        return blnExito;
    }

    @Override
    public Usuario getUsuario(String strTipoDocumento, int intNroDocumento) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Usuario usuario = null;
        try {
            String strConsulta = "SELECT nombre, apellido, tipoDocumento, documento, email, foto, descripcion, legajo FROM Usuario WHERE tipoDocumento = ? AND nroDocumento = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, strTipoDocumento);
            psConsulta.setInt(2, intNroDocumento);
            ResultSet rsContulta = psConsulta.executeQuery();
            while (rsContulta.next()) {
                usuario = new Usuario();
                usuario.setStrNombre(rsContulta.getString("nombre"));
                usuario.setStrApellido(rsContulta.getString("apellido"));
                usuario.setStrTipoDocumento(rsContulta.getString("tipoDocumento"));
                usuario.setIntNroDocumento(rsContulta.getInt("documento"));
                usuario.setStrEmail(rsContulta.getString("email"));
                usuario.setImgFoto(rsContulta.getObject("foto"));
                usuario.setStrDescripcion(rsContulta.getString("descripcion"));
                usuario.setStrLegajo(rsContulta.getString("legajo"));
                break;
            }
        } catch (Exception e) {
            System.err.println("Problemas al levantar el usuario de la BD: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public Usuario getUsuario() {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Usuario usuario = null;
        try {
            String strConsulta = "SELECT nombre, apellido, tipoDocumento, documento, email, foto, descripcion, legajo FROM Usuario ";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsContulta = psConsulta.executeQuery();
            while (rsContulta.next()) {
                usuario = new Usuario();
                usuario.setStrNombre(rsContulta.getString("nombre"));
                usuario.setStrApellido(rsContulta.getString("apellido"));
                usuario.setStrTipoDocumento(rsContulta.getString("tipoDocumento"));
                usuario.setIntNroDocumento(rsContulta.getInt("documento"));
                usuario.setStrEmail(rsContulta.getString("email"));
                usuario.setImgFoto(rsContulta.getObject("foto"));
                usuario.setStrDescripcion(rsContulta.getString("descripcion"));
                usuario.setStrLegajo(rsContulta.getString("legajo"));
                break;
            }
        } catch (Exception e) {
            System.err.println("Problemas al levantar el usuario de la BD: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public boolean hayUsuariosGuardados() throws Exception {
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta = "SELECT 1 FROM Usuario";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsContulta = psConsulta.executeQuery();
            return rsContulta.next();
        } catch (Exception e) {
            System.err.println("Problemas al levantar el usuario de la BD: " + e.getMessage());
            throw e;
        }
    }
}
