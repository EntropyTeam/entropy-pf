package backend.dao.resoluciones;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.dao.diseños.DAODiseñoExamen;
import backend.dao.usuarios.DAOAlumno;
import backend.examenes.Examen;
import backend.usuarios.Alumno;
import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Denise
 */
public class DAOResolucion implements IDAOResolucion {

    @Override
    public boolean guardarResolucion(Resolucion resolucion) {
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            conexion.setAutoCommit(false);

            String strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESOLUCION + " ("
                    + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + ", "
                    + EntropyDB.RES_COL_RESOLUCION_TIEMPO_EMPLEADO + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ANULADA + ", "
                    + EntropyDB.RES_COL_RESOLUCION_MOTIVO_ANULACION + ", "
                    + EntropyDB.RES_COL_RESOLUCION_IP + ", "
                    + EntropyDB.RES_COL_RESOLUCION_CODIGO + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ENVIADA +") "
                    + "VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, resolucion.getExamen().getIntExamenId());
            psConsulta.setInt(2, resolucion.getAlumno().getIntAlumnoId());
            psConsulta.setInt(3, resolucion.getIntTiempoEmpleado());
            psConsulta.setBoolean(4, resolucion.isBlnAnulada());
            psConsulta.setString(5, resolucion.getStrJustificacionAnulacion());
            psConsulta.setString(6, resolucion.getAlumno().getStrIP());
            psConsulta.setString(7, resolucion.getAlumno().getStrCodigo());
            psConsulta.setBoolean(8, false);
            psConsulta.execute();
            
            String strConsultaUltimoID = "SELECT last_insert_rowid();";
            psConsulta = conexion.prepareStatement(strConsultaUltimoID);
            ResultSet rsConsulta = psConsulta.executeQuery();
            int intIDResolucion = rsConsulta.getInt(1);
            resolucion.setIntID(intIDResolucion);

            // Guardar las respuestas
            DAORespuesta daoRespuesta = new DAORespuesta();
            for (Respuesta respuesta : resolucion.getColRespuestas()) {
                daoRespuesta.guardarRespuesta(respuesta, intIDResolucion, conexion);
            }
            conexion.commit();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DAODiseñoExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return true;
    }

    @Override
    public Resolucion getResolucionCompleta(int idResolucion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Resolucion resolucion = null;

        try {
            resolucion = new Resolucion();
            resolucion.setIntID(idResolucion);

            String strConsulta = "SELECT "
                    + EntropyDB.RES_COL_RESOLUCION_CODIGO + ", "
                    + EntropyDB.RES_COL_RESOLUCION_IP + ", "
                    + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + ", "
                    + EntropyDB.RES_COL_RESOLUCION_TIEMPO_EMPLEADO + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ANULADA + ", "
                    + EntropyDB.RES_COL_RESOLUCION_MOTIVO_ANULACION + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ENVIADA + " "
                    + " FROM " + EntropyDB.RES_TBL_RESOLUCION
                    + " WHERE " + EntropyDB.RES_COL_RESOLUCION_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idResolucion);
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                Alumno alumno = new DAOAlumno().getAlumnoByResolucion(resolucion.getIntID());
                String strCodigo = rs.getString(EntropyDB.RES_COL_RESOLUCION_CODIGO);
                String strIP = rs.getString(EntropyDB.RES_COL_RESOLUCION_IP);
                alumno.setStrCodigo(strCodigo);
                alumno.setStrIP(strIP);
                resolucion.setAlumno(alumno);
                Examen examen = new Examen();
                examen.setIntExamenId(rs.getInt(EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID));
                resolucion.setExamen(examen);
                resolucion.setIntTiempoEmpleado(rs.getInt(EntropyDB.RES_COL_RESOLUCION_TIEMPO_EMPLEADO));
                resolucion.setBlnAnulada(rs.getBoolean(EntropyDB.RES_COL_RESOLUCION_ANULADA));
                resolucion.setStrJustificacionAnulacion(rs.getString(EntropyDB.RES_COL_RESOLUCION_MOTIVO_ANULACION));
                resolucion.setFueEnviadaPorEmail(rs.getBoolean(EntropyDB.RES_COL_RESOLUCION_ENVIADA));
            }

            resolucion.setColRespuestas(new DAORespuesta().getRespuestas(resolucion));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
//            DAOConexion.desconectarBaseDatos();
        }
        
        return resolucion;
    }

    @Override
    public ArrayList<Resolucion> getResoluciones(Examen examen) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Resolucion> resoluciones = new ArrayList<>();
        
        try {

            String strConsulta = "SELECT "
                    + EntropyDB.RES_COL_RESOLUCION_ID + " "
                    + " FROM " + EntropyDB.RES_TBL_RESOLUCION
                    + " WHERE " + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, examen.getIntExamenId());
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                resoluciones.add(getResolucionCompleta(rs.getInt(EntropyDB.RES_COL_RESOLUCION_ID)));
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
//            DAOConexion.desconectarBaseDatos();
        }
        
        return resoluciones;
    }

    @Override
    public ArrayList<Resolucion> getResolucionesDeUnAlumno(int idAlumno) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Resolucion> resoluciones = new ArrayList<>();
        
        try {

            String strConsulta = "SELECT "
                    + EntropyDB.RES_COL_RESOLUCION_ID + " "
                    + " FROM " + EntropyDB.RES_TBL_RESOLUCION
                    + " WHERE " + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idAlumno);
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                resoluciones.add(getResolucionCompleta(rs.getInt(EntropyDB.RES_COL_RESOLUCION_ID)));
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
//            DAOConexion.desconectarBaseDatos();
        }
        
        return resoluciones;
    }

    @Override
    public ArrayList<Resolucion> getResolucionesByCurso(int idCurso) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Resolucion> resoluciones = new ArrayList<>();
        try {
            String strConsulta = "SELECT "
                    + EntropyDB.RES_COL_RESOLUCION_ID + " "
                    + " FROM " + EntropyDB.RES_TBL_RESOLUCION + " R INNER JOIN " + EntropyDB.EXA_TBL_EXAMEN + " E "
                    + " ON R." + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + " = E." + EntropyDB.EXA_COL_EXAMEN_ID
                    + " INNER JOIN " + EntropyDB.GRL_TBL_CURSO + " C ON E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID
                    + " WHERE E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idCurso);
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                resoluciones.add(getResolucionCompleta(rs.getInt(EntropyDB.RES_COL_RESOLUCION_ID)));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resoluciones;
    }

    @Override
    public ArrayList<Resolucion> getResolucionesByInstitucion(int idInstitucion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Resolucion> resoluciones = new ArrayList<>();
        try {
            String strConsulta = "SELECT "
                    + EntropyDB.RES_COL_RESOLUCION_ID + " "
                    + " FROM " + EntropyDB.RES_TBL_RESOLUCION + " R INNER JOIN " + EntropyDB.EXA_TBL_EXAMEN + " E "
                    + " ON R." + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + " = E." + EntropyDB.EXA_COL_EXAMEN_ID
                    + " INNER JOIN " + EntropyDB.GRL_TBL_CURSO + " C ON E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID
                    + " INNER JOIN " + EntropyDB.GRL_TBL_INSTITUCION + " I ON C." + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = I." + EntropyDB.GRL_COL_INSTITUCION_ID
                    + " WHERE C." + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idInstitucion);
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                resoluciones.add(getResolucionCompleta(rs.getInt(EntropyDB.RES_COL_RESOLUCION_ID)));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resoluciones;
    }

    @Override
    public boolean setFueEnviadaPorEmail(int resolucionID, boolean fueEnviada) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta = "UPDATE " + EntropyDB.RES_TBL_RESOLUCION 
                    + " SET " + EntropyDB.RES_COL_RESOLUCION_ENVIADA +" = ? "
                    + " WHERE " + EntropyDB.RES_COL_RESOLUCION_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setBoolean(1, fueEnviada);
            psConsulta.setInt(2, resolucionID);
            return psConsulta.execute();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }
}
