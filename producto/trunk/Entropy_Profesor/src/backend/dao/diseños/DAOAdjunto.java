/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose
 */
public class DAOAdjunto implements IDAOAdjunto {

    @Override
    public void guardarAdjunto(int idPregunta, ArrayList<Object> adjuntos, Connection conexionParametro) {
        Connection conexion = conexionParametro;
        try {
            String strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_ADJUNTO + "(" + EntropyDB.DIS_COL_ADJUNTO_ADJUNTO + "," + EntropyDB.DIS_COL_ADJUNTO_PREGUNTA_ID + ") VALUES(?,?)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            for (Object adjunto1 : adjuntos) {
                psConsulta.setBytes(1, (byte[]) adjunto1);
                psConsulta.setInt(2, idPregunta);
                psConsulta.execute();
            }
        } catch (SQLException e) {
            System.err.println("Ocurrió un error mientras se recuperaba el adjunto: " + e.toString());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DAOAdjunto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public Object recuperarAdjunto(int idPregunta) {
        Object adjunto = null;
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();

            String strConsulta = "SELECT * " + "FROM " + EntropyDB.DIS_TBL_ADJUNTO + " " + "WHERE " + EntropyDB.DIS_COL_ADJUNTO_PREGUNTA_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idPregunta);
            ResultSet rsConsulta = psConsulta.executeQuery();
            if (rsConsulta.next()) {
                adjunto = rsConsulta.getBlob(EntropyDB.DIS_COL_ADJUNTO_ADJUNTO);
            }

        } catch (SQLException e) {
            System.err.println("Ocurrió un error mientras se recuperaba el adjunto: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return adjunto;
    }
    
        public Object recuperarAdjuntoDiseño(int idPregunta) {
        Object adjunto = null;
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();

            String strConsulta = "SELECT * " + "FROM " + EntropyDB.DIS_TBL_ADJUNTO + " " + "WHERE " + EntropyDB.DIS_COL_ADJUNTO_PREGUNTA_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idPregunta);
            ResultSet rsConsulta = psConsulta.executeQuery();
            if (rsConsulta.next()) {
                adjunto = rsConsulta.getObject(EntropyDB.DIS_COL_ADJUNTO_ADJUNTO);
            }

        } catch (SQLException e) {
            System.err.println("Ocurrió un error mientras se recuperaba el adjunto: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return adjunto;
    }

    @Override
    public Object recuperarAdjuntoExamen(int idPregunta) {
        Object adjunto = null;
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();

            String strConsulta = "SELECT " + EntropyDB.EXA_COL_ADJUNTO_ADJUNTO + " FROM " + EntropyDB.EXA_TBL_ADJUNTO + " " + "WHERE " + EntropyDB.EXA_COL_ADJUNTO_PREGUNTA_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idPregunta);
            ResultSet rsConsulta = psConsulta.executeQuery();
            if (rsConsulta.next()) {
                adjunto = rsConsulta.getObject(EntropyDB.EXA_COL_ADJUNTO_ADJUNTO);
            }

        } catch (SQLException e) {
            System.err.println("Ocurrió un error mientras se recuperaba el adjunto: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return adjunto;
    }

}
