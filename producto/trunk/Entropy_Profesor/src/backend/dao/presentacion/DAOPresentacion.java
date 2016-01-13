/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.dao.presentacion;

import backend.Asistencia.Asistencia;
import backend.Presentacion.Presentacion;
import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.dao.usuarios.DAOAlumno;
import backend.dao.usuarios.IDAOAlumno;
import backend.gestores.GestorConexion;
import backend.usuarios.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose
 */
public class DAOPresentacion implements IDAOPresentacion {

    @Override
    public Presentacion recuperarPresentacion(int idPresentacion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Presentacion presentacion = null;
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_ID + "=?";
        Object[] parametros = {idPresentacion};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            if (rs.next()) {
                presentacion = new Presentacion();
                presentacion.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                presentacion.setIntIdCurso(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                presentacion.setStrNombre(rs.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                presentacion.setDteFecha(new Date(rs.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                presentacion.setStrDescripcion(rs.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));
            }
            String consultaDos = "SELECT * FROM " + EntropyDB.PRE_TBL_ASISTENCIA + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID + "=?";
            Object[] parametrosDos = {idPresentacion};
            ResultSet rsDos = GestorConexion.getResultSet(conexion, consultaDos, parametrosDos);
            ArrayList<Asistencia> asistencias = new ArrayList<Asistencia>();
            while (rsDos.next()) {
                Asistencia asistencia = new Asistencia();
                IDAOAlumno daoAlumno = new DAOAlumno();
                Alumno alumno = null; //daoAlumno.getAlumno(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID));
                asistencia.setAlumno(alumno);
                asistencia.setBlnAnulada(rs.getBoolean(EntropyDB.PRE_COL_ASISTENCIA_ANULADA));
                asistencia.setIntIdAsistencia(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ID));
                asistencia.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID));
                asistencia.setStrMotivoAnulacion(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_MOTIVO_ANULACION));
                asistencia.setStrIp(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_IP));
                asistencias.add(asistencia);
            }
            presentacion.setAsistencia(asistencias);
        } catch (SQLException e) {
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return presentacion;
    }

    @Override
    public ArrayList<Presentacion> recuperarPresentaciones(int idCurso) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Presentacion> presentaciones = null;
        Presentacion presentacion = null;
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "=?";
        Object[] parametros = {idCurso};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            while (rs.next()) {
                presentacion = new Presentacion();
                presentacion.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                presentacion.setIntIdCurso(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                presentacion.setStrNombre(rs.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                presentacion.setDteFecha(new Date(rs.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                presentacion.setStrDescripcion(rs.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));

                String consultaDos = "SELECT * FROM " + EntropyDB.PRE_TBL_ASISTENCIA + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID + "=?";
                Object[] parametrosDos = {rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID)};
                ResultSet rsDos = GestorConexion.getResultSet(conexion, consultaDos, parametrosDos);
                ArrayList<Asistencia> asistencias = new ArrayList<Asistencia>();
                while (rsDos.next()) {
                    Asistencia asistencia = new Asistencia();
                    IDAOAlumno daoAlumno = new DAOAlumno();
                    Alumno alumno = daoAlumno.getAlumno(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID));
                    asistencia.setAlumno(alumno);
                    asistencia.setBlnAnulada(rs.getBoolean(EntropyDB.PRE_COL_ASISTENCIA_ANULADA));
                    asistencia.setIntIdAsistencia(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ID));
                    asistencia.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID));
                    asistencia.setStrMotivoAnulacion(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_MOTIVO_ANULACION));
                    asistencia.setStrIp(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_IP));
                    asistencias.add(asistencia);
                }
                presentacion.setAsistencia(asistencias);
                presentaciones.add(presentacion);
            }

        } catch (SQLException e) {
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return presentaciones;
    }

    @Override
    public ArrayList<Presentacion> recuperarPresentaciones(int idCurso, Date date) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Presentacion> presentaciones = null;
        Presentacion presentacion = null;
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "=? AND " + EntropyDB.PRE_COL_PRESENTACION_FECHA + "=?";
        Object[] parametros = {idCurso, date.getTime()};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            while (rs.next()) {
                presentacion = new Presentacion();
                presentacion.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                presentacion.setIntIdCurso(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                presentacion.setStrNombre(rs.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                presentacion.setDteFecha(new Date(rs.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                presentacion.setStrDescripcion(rs.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));

                String consultaDos = "SELECT * FROM " + EntropyDB.PRE_TBL_ASISTENCIA + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID + "=?";
                Object[] parametrosDos = {rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID)};
                ResultSet rsDos = GestorConexion.getResultSet(conexion, consultaDos, parametrosDos);
                ArrayList<Asistencia> asistencias = new ArrayList<Asistencia>();
                while (rsDos.next()) {
                    Asistencia asistencia = new Asistencia();
                    IDAOAlumno daoAlumno = new DAOAlumno();
                    Alumno alumno = daoAlumno.getAlumno(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID));
                    asistencia.setAlumno(alumno);
                    asistencia.setBlnAnulada(rs.getBoolean(EntropyDB.PRE_COL_ASISTENCIA_ANULADA));
                    asistencia.setIntIdAsistencia(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ID));
                    asistencia.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID));
                    asistencia.setStrMotivoAnulacion(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_MOTIVO_ANULACION));
                    asistencia.setStrIp(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_IP));
                    asistencias.add(asistencia);
                }
                presentacion.setAsistencia(asistencias);
                presentaciones.add(presentacion);
            }

        } catch (SQLException e) {
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return presentaciones;
    }

    @Override
    public ArrayList<Presentacion> recuperarPresentaciones(int idCurso, Date fechaDesde, Date fechaHasta) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Presentacion> presentaciones = new ArrayList<Presentacion>();
        Presentacion presentacion = null;
        Calendar calendarDesde = Calendar.getInstance();
        calendarDesde.setTime(fechaDesde);
        calendarDesde.add(Calendar.HOUR, -24);
        Long fechaDesdeRestada = calendarDesde.getTimeInMillis();
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "=? AND (" + EntropyDB.PRE_COL_PRESENTACION_FECHA + " >= ?  AND " + EntropyDB.PRE_COL_PRESENTACION_FECHA + " <=?)";
        Object[] parametros = {idCurso, fechaDesdeRestada, fechaHasta.getTime()};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            while (rs.next()) {
                presentacion = new Presentacion();
                presentacion.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                presentacion.setIntIdCurso(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                presentacion.setStrNombre(rs.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                presentacion.setDteFecha(new Date(rs.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                presentacion.setStrDescripcion(rs.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));
                /*String consultaDos = "SELECT * FROM " + EntropyDB.PRE_TBL_ASISTENCIA + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID + "=?";
                Object[] parametrosDos = {rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID)};
                ResultSet rsDos = GestorConexion.getResultSet(conexion, consultaDos, parametrosDos);
                ArrayList<Asistencia> asistencias = new ArrayList<Asistencia>();
                while (rsDos.next()) {
                    Asistencia asistencia = new Asistencia();
                    IDAOAlumno daoAlumno = new DAOAlumno();
                    Alumno alumno = daoAlumno.getAlumno(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID));
                    asistencia.setAlumno(alumno);
                    asistencia.setBlnAnulada(rs.getBoolean(EntropyDB.PRE_COL_ASISTENCIA_ANULADA));
                    asistencia.setIntIdAsistencia(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ID));
                    asistencia.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID));
                    asistencia.setStrMotivoAnulacion(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_MOTIVO_ANULACION));
                    asistencia.setStrIp(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_IP));
                    asistencias.add(asistencia);
                }
                presentacion.setAsistencia(asistencias);*/
                presentaciones.add(presentacion);
            }

        } catch (SQLException e) {
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return presentaciones;
    }

    @Override
    public void modificarPresentacion(int idPresentacion) {
    }

    @Override
    public void borarPresentacion(int idPresentacion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String consulta = "DELETE FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_ID + "=?";
            Object[] parametros = {idPresentacion};
            conexion.setAutoCommit(false);
            PreparedStatement ps = GestorConexion.armarPreparedStatement(conexion, consulta, parametros);
            ps.execute();
            String consultaDos = "DELETE FROM " + EntropyDB.PRE_TBL_ASISTENCIA + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_ID + "=?";
            Object[] parametrosDos = {idPresentacion};
            PreparedStatement psDos = GestorConexion.armarPreparedStatement(conexion, consultaDos, parametrosDos);
            psDos.execute();
            conexion.commit();
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                System.err.println("OCURRIO UN ERROR AL TRATAR DE REALIZAR EL ROLLBACK");
            }
            System.err.println("OCURRIO UN ERROR AL TRATAR DE BORAR LA PRESENTACION");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    @Override
    public boolean guardarPresentacion(Presentacion presentacion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean retorno = true;
        String consulta = "INSERT INTO " + EntropyDB.PRE_TBL_PRESENTACION + " (" + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "," + EntropyDB.PRE_COL_PRESENTACION_NOMBRE + "," + EntropyDB.PRE_COL_PRESENTACION_FECHA + "," + EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION + ") VALUES(?,?,?,?)";
        Object[] parametros = {presentacion.getIntIdCurso(), presentacion.getStrNombre(), presentacion.getDteFecha().getTime(), presentacion.getStrDescripcion()};
        try {
            conexion.setAutoCommit(false);
            PreparedStatement ps = GestorConexion.armarPreparedStatement(conexion, consulta, parametros);
            ps.execute();
            String strConsultaUltimoID = "SELECT last_insert_rowid();";
            PreparedStatement psConsultaUltimoId = conexion.prepareStatement(strConsultaUltimoID);
            ResultSet rsConsultaUltimoId = psConsultaUltimoId.executeQuery();
            int idPresentacion = rsConsultaUltimoId.getInt(1);
            for (Asistencia asistencia : presentacion.getAsistencia()) {
                String consultaDos = "INSERT INTO " + EntropyDB.PRE_TBL_ASISTENCIA + "("+EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID+","+EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID+","+EntropyDB.PRE_COL_ASISTENCIA_ANULADA+","+EntropyDB.PRE_COL_ASISTENCIA_MOTIVO_ANULACION+","+EntropyDB.PRE_COL_ASISTENCIA_IP+") VALUES(?,?,?,?,?)";
                Object[] parametrosDos = {idPresentacion, asistencia.getAlumno().getIntAlumnoId(), asistencia.isBlnAnulada(), asistencia.getStrMotivoAnulacion(), asistencia.getStrIp()};
                PreparedStatement psDos = GestorConexion.armarPreparedStatement(conexion, consultaDos, parametrosDos);
                psDos.execute();   
            }
            conexion.commit();
        } catch (SQLException e) {
            try {
                conexion.rollback();
                retorno = false;
                System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA inserccion: "+e.toString());
            } catch (SQLException ex) {
                System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA inserccion: "+e.toString());
                Logger.getLogger(DAOPresentacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return retorno;
    }

    @Override
    public ArrayList<Presentacion> recuperarPresentacionesDeUnAlumno(int idAlumno) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Asistencia> asistencias = new ArrayList<Asistencia>();
        ArrayList<Presentacion> presentaciones = new ArrayList<Presentacion>();
        Presentacion presentacion = new Presentacion();
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_ASISTENCIA + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID + "=?";
        Object[] parametros = {idAlumno};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            while (rs.next()) {
                    Asistencia asistencia = new Asistencia();
                    asistencia.setBlnAnulada(rs.getBoolean(EntropyDB.PRE_COL_ASISTENCIA_ANULADA));
                    asistencia.setIntIdAsistencia(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_ID));
                    asistencia.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID));
                    asistencia.setStrMotivoAnulacion(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_MOTIVO_ANULACION));
                    asistencia.setStrIp(rs.getString(EntropyDB.PRE_COL_ASISTENCIA_IP));
                    String strConsulta = "SELECT * "
                    + "FROM " + EntropyDB.GRL_TBL_ALUMNO + " "
                    + "WHERE " + EntropyDB.GRL_COL_ALUMNO_ID + " = ? ";
                    PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                    psConsulta.setInt(1,idAlumno);
                    ResultSet rsContulta = psConsulta.executeQuery();
                    if(rsContulta.next()) {
                        Alumno alumno = new Alumno();
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
                        asistencia.setAlumno(alumno);
                    }
                    asistencias.add(asistencia);
                }
            
            for (Asistencia asistencia : asistencias) {           
                    String consultaDos = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_ASISTENCIA_PRESENTACION_ID+"=?";
                    Object[] parametrosDos = {asistencia.getIntIdPresentacion()};
                    ResultSet rsDos = GestorConexion.getResultSet(conexion, consultaDos, parametrosDos);
                    if(rsDos.next())
                    {
                        presentacion = new Presentacion();
                        presentacion.setIntIdPresentacion(rsDos.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                        presentacion.setIntIdCurso(rsDos.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                        presentacion.setStrNombre(rsDos.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                        presentacion.setDteFecha(new Date(rsDos.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                        presentacion.setStrDescripcion(rsDos.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));
                        presentacion.setAsistencia(asistencias);
                    }
                    presentaciones.add(presentacion);
            }
   
        } catch (SQLException e) {
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA :"+e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return presentaciones;
    }


}
