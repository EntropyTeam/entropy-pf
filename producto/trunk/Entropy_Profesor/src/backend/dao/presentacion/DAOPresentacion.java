/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.dao.presentacion;

import backend.Presentacion.Presentacion;
import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.gestores.GestorConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_ID + "=?";
        Object[] parametros = {idCurso};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            while (rs.next()) {
                Presentacion presentacion = new Presentacion();
                presentacion.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                presentacion.setIntIdCurso(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                presentacion.setStrNombre(rs.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                presentacion.setDteFecha(new Date(rs.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                presentacion.setStrDescripcion(rs.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));
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
        String consulta = "SELECT * FROM " + EntropyDB.PRE_TBL_PRESENTACION + " WHERE " + EntropyDB.PRE_COL_PRESENTACION_ID + "=? AND "+EntropyDB.PRE_COL_PRESENTACION_FECHA +"=?";
        Object[] parametros = {idCurso, date.getTime()};
        ResultSet rs = GestorConexion.getResultSet(conexion, consulta, parametros);
        try {
            while (rs.next()) {
                Presentacion presentacion = new Presentacion();
                presentacion.setIntIdPresentacion(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_ID));
                presentacion.setIntIdCurso(rs.getInt(EntropyDB.PRE_COL_PRESENTACION_CURSO_ID));
                presentacion.setStrNombre(rs.getString(EntropyDB.PRE_COL_PRESENTACION_NOMBRE));
                presentacion.setDteFecha(new Date(rs.getLong(EntropyDB.PRE_COL_PRESENTACION_FECHA)));
                presentacion.setStrDescripcion(rs.getString(EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION));
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
        boolean retorno=true;
        String consulta = "INSERT INTO("+EntropyDB.PRE_COL_PRESENTACION_CURSO_ID+","+EntropyDB.PRE_COL_PRESENTACION_NOMBRE+","+EntropyDB.PRE_COL_PRESENTACION_FECHA+","+EntropyDB.PRE_COL_PRESENTACION_DESCRIPCION+") VALUES(?,?,?,?)";
        Object[] parametros = {presentacion.getIntIdCurso(), presentacion.getStrNombre(), presentacion.getDteFecha().getTime(), presentacion.getStrDescripcion()};
        try {
            PreparedStatement ps = GestorConexion.armarPreparedStatement(conexion, consulta, parametros);
            ps.execute();
        } catch (SQLException e) {
            retorno=false;
            System.err.println("OCURRIO UN ERROR AL TRATAR DE EJECUTAR LA CONSULTA");
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return retorno;
    }
    
    

}
