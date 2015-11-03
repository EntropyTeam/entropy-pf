package backend.dao.resoluciones;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.dao.diseños.DAODiseñoExamen;
import backend.examenes.Examen;
import backend.resoluciones.Alumno;
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

            String strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_ALUMNO + " ("
                    + EntropyDB.RES_COL_ALUMNO_NOMBRE + ", "
                    + EntropyDB.RES_COL_ALUMNO_LEGAJO + ", "
                    + EntropyDB.RES_COL_ALUMNO_CODIGO + ", "
                    + EntropyDB.RES_COL_ALUMNO_IP + ") "
                    + "VALUES(?,?,?,?)";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, resolucion.getAlumno().getStrNombre());
            psConsulta.setString(2, (resolucion.getAlumno().getStrLegajo() == null) ? "" : resolucion.getAlumno().getStrLegajo());
            psConsulta.setString(3, (resolucion.getAlumno().getStrCodigo() == null) ? "" : resolucion.getAlumno().getStrCodigo());
            psConsulta.setString(4, resolucion.getAlumno().getStrIP());

            psConsulta.execute();

            String strConsultaUltimoID = "SELECT last_insert_rowid();";
            PreparedStatement psConsultaUltimoId = conexion.prepareStatement(strConsultaUltimoID);
            ResultSet rsConsultaUltimoId = psConsultaUltimoId.executeQuery();
            int intIDAlumno = rsConsultaUltimoId.getInt(1);

            strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESOLUCION + " ("
                    + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + ", "
                    + EntropyDB.RES_COL_RESOLUCION_TIEMPO_EMPLEADO + ", "
                    + EntropyDB.RES_COL_RESOLUCION_ANULADA + ", "
                    + EntropyDB.RES_COL_RESOLUCION_MOTIVO_ANULACION + ") "
                    + "VALUES(?,?,?,?,?)";

            psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, resolucion.getExamen().getIntExamenId());
            psConsulta.setInt(2, intIDAlumno);
            psConsulta.setInt(3, resolucion.getIntTiempoEmpleado());
            psConsulta.setBoolean(4, resolucion.isBlnAnulada());
            psConsulta.setString(5, resolucion.getStrJustificacionAnulacion());
            psConsulta.execute();
            
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
                    + "A." + EntropyDB.RES_COL_ALUMNO_NOMBRE + ", "
                    + "A." + EntropyDB.RES_COL_ALUMNO_LEGAJO + ", "
                    + "A." + EntropyDB.RES_COL_ALUMNO_CODIGO + ", "
                    + "A." + EntropyDB.RES_COL_ALUMNO_IP + ", "
                    + "R." + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + ", "
                    + "R." + EntropyDB.RES_COL_RESOLUCION_TIEMPO_EMPLEADO + ", "
                    + "R." + EntropyDB.RES_COL_RESOLUCION_ANULADA + ", "
                    + "R." + EntropyDB.RES_COL_RESOLUCION_MOTIVO_ANULACION + " "
                    + " FROM " + EntropyDB.RES_TBL_ALUMNO
                    + " A INNER JOIN " + EntropyDB.RES_TBL_RESOLUCION
                    + " R ON R." + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + " = A." + EntropyDB.RES_COL_ALUMNO_ID
                    + " WHERE R." + EntropyDB.RES_COL_RESOLUCION_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idResolucion);
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                String strNombre = rs.getString(EntropyDB.RES_COL_ALUMNO_NOMBRE);
                String strLegajo = rs.getString(EntropyDB.RES_COL_ALUMNO_LEGAJO);
                String strCodigo = rs.getString(EntropyDB.RES_COL_ALUMNO_CODIGO);
                String strIP = rs.getString(EntropyDB.RES_COL_ALUMNO_IP);
                Alumno alumno = new Alumno(strNombre, strLegajo, strCodigo);
                alumno.setStrIP(strIP);
                resolucion.setAlumno(alumno);
                Examen examen = new Examen();
                examen.setIntExamenId(rs.getInt(EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID));
                resolucion.setExamen(examen);
                resolucion.setIntTiempoEmpleado(rs.getInt(EntropyDB.RES_COL_RESOLUCION_TIEMPO_EMPLEADO));
                resolucion.setBlnAnulada(rs.getBoolean(EntropyDB.RES_COL_RESOLUCION_ANULADA));
                resolucion.setStrJustificacionAnulacion(rs.getString(EntropyDB.RES_COL_RESOLUCION_MOTIVO_ANULACION));
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

}
