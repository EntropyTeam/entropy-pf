package backend.dao.examenes;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.dao.diseños.DAOAdjunto;
import backend.diseños.Pregunta;
import backend.diseños.Curso;
import backend.examenes.EstadoExamen;
import backend.examenes.Examen;
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
 * @author Pelito
 */
public class DAOExamen implements IDAOExamen {

    @Override
    public ArrayList<Examen> getExamenes() {
        ArrayList<Examen> colExamen = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            
            String strConsulta = "SELECT "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_ID + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_NOMBRE + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_DESCRIPCION + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_ESTADO + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_TIEMPO + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_FECHA + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION + " "
                    + "FROM " + EntropyDB.EXA_TBL_EXAMEN + " E ";
            
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intExamenId = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_ID);
                int intCursoId = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_CURSO_ID);
                Curso curso = new Curso();
                curso.setIntCursoId(intCursoId);
                String strNombre = rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_NOMBRE);
                String strDescripcion = rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_DESCRIPCION);
                int intEstado = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_ESTADO);
                int intTiempo = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_TIEMPO);
                Examen examen = new Examen(intExamenId, strNombre, strDescripcion, curso, intEstado, intTiempo);
                examen.setDteFecha(new Date(rsConsulta.getLong(EntropyDB.EXA_COL_EXAMEN_FECHA)));
                examen.setDblPorcentajeAprobacion(rsConsulta.getDouble(EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION));
                if (intEstado == EstadoExamen.CANCELADO) {
                    examen.setStrMotivoCancelacion(rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION));
                }
                colExamen.add(examen);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return colExamen;
    }

    @Override
    public Examen getExamen(int intExamenId) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Examen examen = null;
        try {
            String strConsulta = "SELECT "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_NOMBRE + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_DESCRIPCION + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_ESTADO + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_TIEMPO + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_FECHA + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION + " "
                    + "FROM " + EntropyDB.EXA_TBL_EXAMEN + " E "
                    + "WHERE E." + EntropyDB.EXA_COL_EXAMEN_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intExamenId);
            ResultSet rsConsulta = psConsulta.executeQuery();

            if (rsConsulta.next()) {
                int intCursoId = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_CURSO_ID);
                Curso curso = new Curso();
                curso.setIntCursoId(intCursoId);
                String strNombre = rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_NOMBRE);
                String strDescripcion = rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_DESCRIPCION);
                int intEstado = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_ESTADO);
                int intTiempo = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_TIEMPO);
                examen = new Examen(intExamenId, strNombre, strDescripcion, curso, intEstado, intTiempo);
                examen.setDteFecha(new Date(rsConsulta.getLong(EntropyDB.EXA_COL_EXAMEN_FECHA)));
                examen.setDblPorcentajeAprobacion(rsConsulta.getDouble(EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION));
                if (intEstado == EstadoExamen.CANCELADO) {
                    examen.setStrMotivoCancelacion(rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION));
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return examen;
    }

    @Override
    public ArrayList<Examen> getExamenes(Curso curso) {
        ArrayList<Examen> colExamen = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_ID + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_NOMBRE + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_DESCRIPCION + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_ESTADO + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_TIEMPO + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_FECHA + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION + ", "
                    + "E." + EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION + " "
                    + "FROM " + EntropyDB.EXA_TBL_EXAMEN + " E "
                    + "WHERE E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, curso.getIntCursoId());

            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intExamenId = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_ID);
                String strNombre = rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_NOMBRE);
                String strDescripcion = rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_DESCRIPCION);
                int intEstado = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_ESTADO);
                int intTiempo = rsConsulta.getInt(EntropyDB.EXA_COL_EXAMEN_TIEMPO);
                Examen examen = new Examen(intExamenId, strNombre, strDescripcion, curso, intEstado, intTiempo);
                examen.setDteFecha(new Date(rsConsulta.getLong(EntropyDB.EXA_COL_EXAMEN_FECHA)));
                examen.setDblPorcentajeAprobacion(rsConsulta.getDouble(EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION));
                if (intEstado == EstadoExamen.CANCELADO) {
                    examen.setStrMotivoCancelacion(rsConsulta.getString(EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION));
                }
                colExamen.add(examen);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return colExamen;
    }

    @Override
    public void guardarExamen(Examen examen) {
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {

            conexion.setAutoCommit(false);

            // Guardar el examen
            String strConsulta = "INSERT INTO " + EntropyDB.EXA_TBL_EXAMEN + "("
                    + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + ", "
                    + EntropyDB.EXA_COL_EXAMEN_NOMBRE + ", "
                    + EntropyDB.EXA_COL_EXAMEN_DESCRIPCION + ", "
                    + EntropyDB.EXA_COL_EXAMEN_ESTADO + ", "
                    + EntropyDB.EXA_COL_EXAMEN_TIEMPO + ", "
                    + EntropyDB.EXA_COL_EXAMEN_FECHA + ", "
                    + EntropyDB.EXA_COL_EXAMEN_PORCENTAJE_APROBACION + ") "
                    + "VALUES(?,?,?,?,?,?,?)";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, examen.getCurso().getIntCursoId());
            psConsulta.setString(2, examen.getStrNombre());
            psConsulta.setString(3, examen.getStrDescripcion());
            psConsulta.setInt(4, examen.getIntEstado());
            psConsulta.setInt(5, examen.getIntTiempo());
            psConsulta.setLong(6, examen.getDteFecha().getTime());
            psConsulta.setDouble(7, examen.getDblPorcentajeAprobacion());

            psConsulta.execute();

            // Obtener el ID del del examen
            strConsulta = "SELECT last_insert_rowid();";
            psConsulta = conexion.prepareStatement(strConsulta);

            ResultSet rsConsulta = psConsulta.executeQuery();
            int intUltimoIdExamen = rsConsulta.getInt(1);
            examen.setIntExamenId(intUltimoIdExamen);
            
            // Guardar las preguntas
            DAOPreguntaExamen daoPreguntaExamen = new DAOPreguntaExamen();
            for (Pregunta p : examen.getColPreguntas()) {
                daoPreguntaExamen.guardarPregunta(p, intUltimoIdExamen, conexion);
                
                //Se agrega el guardar adjunto en en el examen
                
                if(p.getColAdjuntos()!=null && p.getColAdjuntos().size()>0)
                {
                    DAOAdjunto daoAdjunto= new DAOAdjunto();
                    daoAdjunto.guardarAdjuntoExamen(p.getIntPreguntaId(), p.getColAdjuntos(), conexion); 
                }
                
            }

            conexion.commit();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DAOExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    @Override
    public boolean actualizarEstado(int intEstado, int intIDExamen) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean blnExito = false;
        try {

            String strUpdate = "UPDATE  " + EntropyDB.EXA_TBL_EXAMEN
                    + " SET " + EntropyDB.EXA_COL_EXAMEN_ESTADO + " = ?"
                    + " WHERE " + EntropyDB.EXA_COL_EXAMEN_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strUpdate);
            psConsulta.setInt(1, intEstado);
            psConsulta.setInt(2, intIDExamen);
            psConsulta.executeUpdate();

            blnExito = true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return blnExito;
    }

    /**
     * Chequea si todas las resoluciones han sido corregidas, y de ser el caso
     * cambia el estado del examen a CORREGIDO.Sino, se asigna un estado
     * FINALIZADO.
     *
     * @param intIDExamen id del examen a actualizar
     * @return true si la actualización fue exitosa, false de lo contrario
     */
    @Override
    public boolean actualizarEstado(int intIDExamen) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean blnExito = false;
        int intEstado;
        try {

            String strConsulta = "SELECT EX.examenId  "
                    + "FROM exa_examen EX  "
                    + "WHERE (EX.examenId IN  "
                    + "(SELECT R.examenId  "
                    + "FROM res_resolucion R  "
                    + "INNER JOIN exa_examen E ON R.examenID = E.examenId  "
                    + "INNER JOIN res_respuesta RE ON R.resolucionId = RE.resolucionId    "
                    + "INNER JOIN res_respuestadesarrollo RD ON RE.respuestaId = RD.respuestaId  "
                    + "WHERE RD.calificacion = -1)  "
                    + "OR EX.examenId IN  "
                    + "(SELECT R.examenId  "
                    + "FROM res_resolucion R  "
                    + "INNER JOIN exa_examen E ON R.examenID = E.examenId  "
                    + "INNER JOIN res_respuesta RE ON R.resolucionId = RE.resolucionId    "
                    + "INNER JOIN res_respuestaverdaderofalso RVF ON RE.respuestaId = RVF.respuestaId  "
                    + "WHERE RVF.calificacion = -1))  "
                    + "AND EX.examenId = ? ";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intIDExamen);
            ResultSet rsConsulta = psConsulta.executeQuery();
            if (rsConsulta.next()) {
                // Existen preguntas sin corregir
                intEstado = EstadoExamen.FINALIZADO;
            } else {
                intEstado = EstadoExamen.CORREGIDO;
            }

            String strUpdate = "UPDATE  " + EntropyDB.EXA_TBL_EXAMEN
                    + " SET " + EntropyDB.EXA_COL_EXAMEN_ESTADO + " = ?"
                    + " WHERE " + EntropyDB.EXA_COL_EXAMEN_ID + " = ?";

            psConsulta = conexion.prepareStatement(strUpdate);
            psConsulta.setInt(1, intEstado);
            psConsulta.setInt(2, intIDExamen);
            psConsulta.executeUpdate();

            blnExito = true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return blnExito;
    }

    @Override
    public boolean cancelarExamen(int intExamenId, String strMotivoCancelacion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean blnExito = false;
        try {

            String strUpdate = "UPDATE  " + EntropyDB.EXA_TBL_EXAMEN
                    + " SET " + EntropyDB.EXA_COL_EXAMEN_ESTADO + " = " + EstadoExamen.CANCELADO + " , "
                    + " SET " + EntropyDB.EXA_COL_EXAMEN_MOTIVO_CANCELACION + " = ? "
                    + " WHERE " + EntropyDB.EXA_COL_EXAMEN_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strUpdate);
            psConsulta.setString(1, strMotivoCancelacion);
            psConsulta.setInt(2, intExamenId);
            psConsulta.executeUpdate();

            blnExito = true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return blnExito;
    }
}
