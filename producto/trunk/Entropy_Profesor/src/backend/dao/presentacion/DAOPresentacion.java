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
        Presentacion presentacion=null;
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
        Presentacion presentacion =null;
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
        ArrayList<Presentacion> presentaciones = null;
        Presentacion presentacion =null;
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "=? AND " + EntropyDB.PRE_COL_PRESENTACION_FECHA + " BETWEEN ? AND ?";
        Object[] parametros = {idCurso, fechaDesde.getTime(), fechaHasta.getTime()};
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
        String consulta = "DELETE FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_ID + "=?";
        Object[] parametros = {idPresentacion};
        try {
            PreparedStatement ps = GestorConexion.armarPreparedStatement(conexion, consulta, parametros);

            ps.execute();
        } catch (SQLException e) {
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    @Override
    public boolean guardarPresentacion(Presentacion presentacion) {
        Connection conexion = DAOConexion.conectarBaseDatos();

        boolean retorno = true;
        String consulta = "INSERT " + EntropyDB.PRE_TBL_PRESENTACION + " INTO(" + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "," + EntropyDB.PRE_COL_PRESENTACION_NOMBRE + "," + EntropyDB.PRE_COL_PRESENTACION_FECHA + "," + EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION + ") VALUES(?,?,?,?)";
        Object[] parametros = {presentacion.getIntIdCurso(), presentacion.getStrNombre(), presentacion.getDteFecha().getTime(), presentacion.getStrDescripcion()};
        try {
            conexion.setAutoCommit(false);
            PreparedStatement ps = GestorConexion.armarPreparedStatement(conexion, consulta, parametros);
            ps.execute();
            for (Asistencia asistencia : presentacion.getAsistencia()) {
                String consultaDos = "INSERT " + EntropyDB.PRE_TBL_ASISTENCIA + " INTO (" + EntropyDB.PRE_COL_PRESENTACION_CURSO_ID + "," + EntropyDB.PRE_COL_ASISTENCIA_ALUMNO_ID + ") VALUES(?,?)";
                Object[] parametrosDos = {presentacion.getIntIdCurso(), asistencia.getAlumno().getIntAlumnoId(), asistencia.isBlnAnulada(), asistencia.getStrMotivoAnulacion(), asistencia.getStrIp()};
                PreparedStatement psDos = GestorConexion.armarPreparedStatement(conexion, consultaDos, parametrosDos);
                psDos.execute();
            }
        } catch (SQLException e) {
            try {
                conexion.rollback();
                retorno = false;
                System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
            } catch (SQLException ex) {
                Logger.getLogger(DAOPresentacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return retorno;
    }

}
