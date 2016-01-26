package backend.dao.usuarios;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.usuarios.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    
    
    @Override
    public Alumno getAlumno(int idAlumno) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Alumno alumno = null;
        
        try {
            String strConsulta = "SELECT * "
                    + "FROM " + EntropyDB.GRL_TBL_ALUMNO + " "
                    + "WHERE " + EntropyDB.GRL_COL_ALUMNO_ID + " = ? ";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1,idAlumno);
            ResultSet rsContulta = psConsulta.executeQuery();
            if(rsContulta.next()) {
                alumno = new Alumno();
                int intIDAlumno = rsContulta.getInt(EntropyDB.GRL_COL_ALUMNO_ID);
                String strNombre = rsContulta.getString(EntropyDB.GRL_COL_ALUMNO_NOMBRE);
                String strApellido = rsContulta.getString(EntropyDB.GRL_COL_ALUMNO_APELLIDO);
                int intTipoDocumento = rsContulta.getInt(EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO);
                String strDocumento = rsContulta.getString(EntropyDB.GRL_COL_ALUMNO_DOCUMENTO);
                String strLegajo = rsContulta.getString(EntropyDB.GRL_COL_ALUMNO_LEGAJO);
                String strEmail = rsContulta.getString(EntropyDB.GRL_COL_ALUMNO_EMAIL);
                alumno.setIntAlumnoId(intIDAlumno);
                alumno.setStrNombre(strNombre);
                alumno.setStrApellido(strApellido);
                alumno.setIntNroDocumento(intTipoDocumento);
                alumno.setStrTipoDocumento(strDocumento);
                alumno.setStrLegajo(strLegajo);
                alumno.setStrEmail(strEmail);
            }
        } catch (Exception e) {
            System.err.println("Problemas al obtener el usuario de la BD: " + e.getMessage());
        }
        finally
        {
            DAOConexion.desconectarBaseDatos();
        }
        return alumno;
    }
    
    @Override
    public ArrayList<Alumno> getAlumnosBy(String strNombre, String strApellido, String strDocumento, String strLegajo, Institucion institucion, Curso curso) {
        Connection conexion;
        PreparedStatement psConsulta;
        ResultSet rsResultado;
        ArrayList<Alumno> lstAlumnos = new ArrayList<>();
        try {
            conexion = DAOConexion.conectarBaseDatos();
            
            String strConsulta = "SELECT DISTINCT "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_ID + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_NOMBRE + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_APELLIDO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_LEGAJO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_EMAIL + " "
                    + "FROM "
                    + EntropyDB.GRL_TBL_ALUMNO + " AL LEFT JOIN " + EntropyDB.RES_TBL_RESOLUCION + " RE ON AL." + EntropyDB.GRL_COL_ALUMNO_ID + " = RE." + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + " "
                    + "LEFT JOIN " + EntropyDB.EXA_TBL_EXAMEN + " EX ON RE." + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + " = EX." + EntropyDB.EXA_COL_EXAMEN_ID + " "
                    + "LEFT JOIN " + EntropyDB.GRL_TBL_CURSO + " C ON EX." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID + " "
                    + "LEFT JOIN " + EntropyDB.GRL_TBL_INSTITUCION + " I ON I." + EntropyDB.GRL_COL_INSTITUCION_ID + " = C." + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " "
                    + "WHERE "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_NOMBRE + " LIKE (?) "
                    + "AND AL." + EntropyDB.GRL_COL_ALUMNO_APELLIDO + " LIKE (?) "
                   + "AND AL." + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + " LIKE (?) ";
            
            if (strLegajo != null && !strLegajo.isEmpty()){
                strConsulta += "AND AL." + EntropyDB.GRL_COL_ALUMNO_LEGAJO + " LIKE (?)";
            }                    

            if (curso != null) {
                strConsulta += "AND C." + EntropyDB.GRL_COL_CURSO_ID + " = ? ";
            } else if (institucion != null) {
                strConsulta += "AND I." + EntropyDB.GRL_COL_INSTITUCION_ID + " = ? ";
            }
            
            psConsulta = conexion.prepareStatement(strConsulta);

            psConsulta.setString(1, strNombre + "%");
            psConsulta.setString(2, strApellido + "%");
            psConsulta.setString(3, strDocumento + "%");
            
            if (strLegajo != null && !strLegajo.isEmpty()){
                psConsulta.setString(4, strLegajo + "%");
            } 
            
            if (curso != null) {
                psConsulta.setInt(5, curso.getIntCursoId());
            } else if (institucion != null) {
                psConsulta.setInt(5, institucion.getIntInstitucionId());
            }

            rsResultado = psConsulta.executeQuery();
            while(rsResultado.next()) {
                Alumno alumno = new Alumno();
                alumno.setIntAlumnoId(Integer.parseInt(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_ID)));
                alumno.setStrNombre(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_NOMBRE));
                alumno.setStrApellido(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_APELLIDO));
                alumno.setStrTipoDocumento(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO));
                alumno.setIntNroDocumento(rsResultado.getInt(EntropyDB.GRL_COL_ALUMNO_DOCUMENTO));
                alumno.setStrLegajo(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_LEGAJO));
                alumno.setStrEmail(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_EMAIL));
                lstAlumnos.add(alumno);
            }
            rsResultado.close();
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lstAlumnos;
    }
    
    
    @Override
    public Alumno getAlumnoByResolucion(int intResolucionID) {
        Connection conexion;
        PreparedStatement psConsulta;
        ResultSet rsResultado;
        Alumno alumno = null;
        try {
            conexion = DAOConexion.conectarBaseDatos();
            
            String strConsulta = "SELECT "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_ID + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_NOMBRE + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_APELLIDO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + " "
                    + "FROM "
                    + EntropyDB.GRL_TBL_ALUMNO + " AL JOIN " + EntropyDB.RES_TBL_RESOLUCION + " RE ON AL." + EntropyDB.GRL_COL_ALUMNO_ID + " = RE." + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + " "
                    + "WHERE RE." + EntropyDB.RES_COL_RESOLUCION_ID + " = ? ";

            
            psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intResolucionID);
            
            rsResultado = psConsulta.executeQuery();
            while(rsResultado.next()) {
                alumno = new Alumno();
                alumno.setIntAlumnoId(Integer.parseInt(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_ID)));
                alumno.setStrNombre(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_NOMBRE));
                alumno.setStrApellido(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_APELLIDO));
                alumno.setStrTipoDocumento(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO));
                alumno.setIntNroDocumento(rsResultado.getInt(EntropyDB.GRL_COL_ALUMNO_DOCUMENTO));
            }
            rsResultado.close();
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return alumno;
    }

    @Override
    public Alumno getAlumnoByRespuesta(int intRespuestaID) {
        Connection conexion;
        PreparedStatement psConsulta;
        ResultSet rsResultado;
        Alumno alumno = null;
        try {
            conexion = DAOConexion.conectarBaseDatos();
            
            String strConsulta = "SELECT "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_ID + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_NOMBRE + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_APELLIDO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + " "
                    + "FROM "
                    + EntropyDB.GRL_TBL_ALUMNO + " AL JOIN " + EntropyDB.RES_TBL_RESOLUCION + " RE ON AL." + EntropyDB.GRL_COL_ALUMNO_ID + " = RE." + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + " "
                    + "JOIN " + EntropyDB.RES_TBL_RESPUESTA + " RTA ON RE." + EntropyDB.RES_COL_RESOLUCION_ID + " = RTA." + EntropyDB.RES_COL_RESPUESTA_RESOLUCION_ID + " "
                    + "WHERE RTA." + EntropyDB.RES_COL_RESPUESTA_ID + " = ? ";

            
            psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intRespuestaID);
            
            rsResultado = psConsulta.executeQuery();
            while(rsResultado.next()) {
                alumno = new Alumno();
                alumno.setIntAlumnoId(Integer.parseInt(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_ID)));
                alumno.setStrNombre(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_NOMBRE));
                alumno.setStrApellido(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_APELLIDO));
                alumno.setStrTipoDocumento(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO));
                alumno.setIntNroDocumento(rsResultado.getInt(EntropyDB.GRL_COL_ALUMNO_DOCUMENTO));
            }
            rsResultado.close();
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return alumno;
    }
}
