package backend.dao.usuarios;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.usuarios.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manejo de la persistencia de entidades del tipo usuario.
 * 
 * @author denise
 */
public class DAOUsuario implements IDAOUsuario {

    @Override
    public boolean guardarUsuario(Usuario usuario) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean blnExito = false;
        try {
            String strConsulta = "SELECT * "
                    + "FROM " + EntropyDB.GRL_TBL_USUARIO + " "
                    + "WHERE (" + EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO + " = ? "
                    + "AND " + EntropyDB.GRL_COL_USUARIO_DOCUMENTO + " = ?) "
                    + "OR " + EntropyDB.GRL_COL_USUARIO_LEGAJO + " = ?";
            PreparedStatement psconsulta = conexion.prepareStatement(strConsulta);
            psconsulta.setString(1, usuario.getStrTipoDocumento());
            psconsulta.setInt(2, usuario.getIntNroDocumento());
            psconsulta.setString(3, usuario.getStrLegajo());

            String strUpdate;
            PreparedStatement psUpdate;

            if (!psconsulta.executeQuery().next()) {
                // Usuario nuevo
                strUpdate = "INSERT INTO " + EntropyDB.GRL_TBL_USUARIO + " ("
                        + EntropyDB.GRL_COL_USUARIO_NOMBRE + ", "
                        + EntropyDB.GRL_COL_USUARIO_APELLIDO + ", "
                        + EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO + ", "
                        + EntropyDB.GRL_COL_USUARIO_DOCUMENTO + ", "
                        + EntropyDB.GRL_COL_USUARIO_EMAIL + ", "
                        + EntropyDB.GRL_COL_USUARIO_FOTO + ", "
                        + EntropyDB.GRL_COL_USUARIO_DESCRIPCION + ", "
                        + EntropyDB.GRL_COL_USUARIO_LEGAJO 
                        + ") VALUES (?,?,?,?,?,?,?,?)";
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
                strUpdate = "UPDATE " + EntropyDB.GRL_TBL_USUARIO + " "
                        + "SET " + EntropyDB.GRL_COL_USUARIO_NOMBRE + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_APELLIDO + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_DOCUMENTO + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_EMAIL + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_FOTO + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_DESCRIPCION + " = ?, "
                        + EntropyDB.GRL_COL_USUARIO_LEGAJO + " = ? "
                        + "WHERE " + EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO + " = ? "
                        + "AND " + EntropyDB.GRL_COL_USUARIO_DOCUMENTO + " = ?";
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
            String strConsulta = "SELECT * FROM " + EntropyDB.GRL_TBL_USUARIO + " "
                    + "WHERE " + EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO + " = ? "
                    + "AND " + EntropyDB.GRL_COL_USUARIO_DOCUMENTO + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, strTipoDocumento);
            psConsulta.setInt(2, intNroDocumento);
            ResultSet rsContulta = psConsulta.executeQuery();
            while (rsContulta.next()) {
                usuario = new Usuario();
                usuario.setStrNombre(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_NOMBRE));
                usuario.setStrApellido(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_APELLIDO));
                usuario.setStrTipoDocumento(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO));
                usuario.setIntNroDocumento(rsContulta.getInt(EntropyDB.GRL_COL_USUARIO_DOCUMENTO));
                usuario.setStrEmail(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_EMAIL));
                usuario.setImgFoto(rsContulta.getObject(EntropyDB.GRL_COL_USUARIO_FOTO));
                usuario.setStrDescripcion(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_DESCRIPCION));
                usuario.setStrLegajo(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_LEGAJO));
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
            String strConsulta = "SELECT * FROM " + EntropyDB.GRL_TBL_USUARIO;
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsContulta = psConsulta.executeQuery();
            while (rsContulta.next()) {
                usuario = new Usuario();
                usuario.setStrNombre(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_NOMBRE));
                usuario.setStrApellido(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_APELLIDO));
                usuario.setStrTipoDocumento(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_TIPO_DOCUMENTO));
                usuario.setIntNroDocumento(rsContulta.getInt(EntropyDB.GRL_COL_USUARIO_DOCUMENTO));
                usuario.setStrEmail(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_EMAIL));
                usuario.setImgFoto(rsContulta.getObject(EntropyDB.GRL_COL_USUARIO_FOTO));
                usuario.setStrDescripcion(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_DESCRIPCION));
                usuario.setStrLegajo(rsContulta.getString(EntropyDB.GRL_COL_USUARIO_LEGAJO));
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
            String strConsulta = "SELECT * FROM " + EntropyDB.GRL_TBL_USUARIO;
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsContulta = psConsulta.executeQuery();
            return rsContulta.next();
        } catch (Exception e) {
            System.err.println("Problemas al levantar el usuario de la BD: " + e.getMessage());
            throw e;
        }
    }
}
