package backend.dao.usuarios;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.usuarios.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lucas
 */
public class DAOAlumno implements IDAOAlumno {

    @Override
    public int guardarAlumno(Alumno alumno) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        
        int intIDAlumno = -1;
        
        try {
            String strConsulta = "INSERT INTO " + EntropyDB.GRL_TBL_ALUMNO + " ("
                        + EntropyDB.GRL_COL_ALUMNO_NOMBRE + ", "
                        + EntropyDB.GRL_COL_ALUMNO_APELLIDO + ", "
                        + EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO + ", "
                        + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + ", "
                        + EntropyDB.GRL_COL_ALUMNO_LEGAJO + ", "
                        + EntropyDB.GRL_COL_ALUMNO_EMAIL
                        + ") VALUES (?,?,?,?,?,?)";
            PreparedStatement psconsulta = conexion.prepareStatement(strConsulta);
            psconsulta.setString(1, alumno.getStrNombre());
            psconsulta.setString(2, alumno.getStrApellido());
            psconsulta.setString(3, alumno.getStrTipoDocumento());
            psconsulta.setInt(4, alumno.getIntNroDocumento());
            psconsulta.setString(5, alumno.getStrLegajo());
            psconsulta.setString(6, alumno.getStrEmail());
            psconsulta.execute();

            String strConsultaUltimoID = "SELECT last_insert_rowid();";
            PreparedStatement psConsultaUltimoId = conexion.prepareStatement(strConsultaUltimoID);
            ResultSet rsConsultaUltimoId = psConsultaUltimoId.executeQuery();
            intIDAlumno = rsConsultaUltimoId.getInt(1);
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                System.err.println("Problemas la guardar un alumno en la BD: " + ex.getMessage());
            }
        }
        
        return intIDAlumno;
    }

    @Override
    public int getAlumnoId(Alumno alumno) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        
        int intIDAlumno = -1;
        
        try {
            String strConsulta = "SELECT * "
                    + "FROM " + EntropyDB.GRL_TBL_ALUMNO + " "
                    + "WHERE " + EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO + " = ? "
                    + "AND " + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, alumno.getStrTipoDocumento());
            psConsulta.setInt(2, alumno.getIntNroDocumento());
            ResultSet rsContulta = psConsulta.executeQuery();
            while (rsContulta.next()) {
                intIDAlumno = rsContulta.getInt(EntropyDB.GRL_COL_ALUMNO_ID);
                break;
            }
        } catch (Exception e) {
            System.err.println("Problemas al obtener el usuario de la BD: " + e.getMessage());
        }
        
        if (intIDAlumno != -1) return intIDAlumno;
        else return this.guardarAlumno(alumno);
    }
    
}
