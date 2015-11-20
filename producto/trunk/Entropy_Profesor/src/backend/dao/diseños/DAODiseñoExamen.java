package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.dao.usuarios.DAOUsuario;
import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Institucion;
import backend.diseños.Pregunta;
import backend.usuarios.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas Cunibertti comentada por Gaston Noves
 */
public class DAODiseñoExamen implements IDAODiseñoExamen {

    @Override
    public ArrayList<DiseñoExamen> getDiseñosExamenes() {
        ArrayList<DiseñoExamen> colDiseñoExamen = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION + " "
                    + "FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN;
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intDiseñoExamenId = rsConsulta.getInt(EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID);
                String strTitulo = rsConsulta.getString(EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE);
                String strDescripcion = rsConsulta.getString(EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION);

                DiseñoExamen diseñoExamen = new DiseñoExamen(intDiseñoExamenId, strTitulo);
                diseñoExamen.setStrDescripcion(strDescripcion);
                colDiseñoExamen.add(diseñoExamen);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return colDiseñoExamen;
    }

    @Override
    public DiseñoExamen getDiseñoExamen(int intDiseñoExamenId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<DiseñoExamen> getDiseñosExamenes(String strCadena) {//A implementar por mi despues. Para cargar e combo.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardarDiseñoExamen(DiseñoExamen diseñoExamen) {
        if (diseñoExamen.getIntDiseñoExamenId() == -1) {
            this.guardarDiseño(diseñoExamen);
        } else {
            this.editarDiseño(diseñoExamen);
        }
    }

    @Override
    public ArrayList<DiseñoExamen> getDiseñosExamenes(Curso curso) {
        ArrayList<DiseñoExamen> colDiseñoExamen = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + ", "
                    + "DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE + ", "
                    + "DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION + " "
                    + "FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " DE,"
                    + EntropyDB.GRL_TBL_CURSO + " C "
                    + "WHERE DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID + " "
                    + "AND C." + EntropyDB.GRL_COL_CURSO_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, curso.getIntCursoId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intDiseñoExamenId = rsConsulta.getInt(EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID);
                String strTitulo = rsConsulta.getString(EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE);
                String strDescripcion = rsConsulta.getString(EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION);

                DiseñoExamen diseñoExamen = new DiseñoExamen(intDiseñoExamenId, strTitulo);
                diseñoExamen.setStrDescripcion(strDescripcion);
                colDiseñoExamen.add(diseñoExamen);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return colDiseñoExamen;
    }

    public ArrayList<DiseñoExamen> getDiseñosExamenesSinCurso() {
        ArrayList<DiseñoExamen> colDiseñoExamen = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION + " "
                    + "FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " "
                    + "WHERE " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " IS NULL";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intDiseñoExamenId = rsConsulta.getInt(EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID);
                String strTitulo = rsConsulta.getString(EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE);
                String strDescripcion = rsConsulta.getString(EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION);

                DiseñoExamen diseñoExamen = new DiseñoExamen(intDiseñoExamenId, strTitulo);
                diseñoExamen.setStrDescripcion(strDescripcion);
                colDiseñoExamen.add(diseñoExamen);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return colDiseñoExamen;
    }

    /**
     * Guarda un nuevo diseño de examen en la bd.
     *
     * @param diseñoExamen diseño a guardar.
     */
    private void guardarDiseño(DiseñoExamen diseñoExamen) {
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            conexion.setAutoCommit(false);

            // Guardar el diseño de examen
            String strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " ("
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION + ", "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_USUARIO_ID
                    + ") VALUES (?,?,?,?)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);

            if (diseñoExamen.getCurso() == null) {
                psConsulta.setNull(1, Types.NULL);
            } else {
                if (diseñoExamen.getCurso().getIntCursoId() != 0) {
                    psConsulta.setInt(1, diseñoExamen.getCurso().getIntCursoId());
                } else { // Hay un nuevo curso
                    if (diseñoExamen.getCurso().getInstitucion().getIntInstitucionId() != 0) {
                        DAOCurso daoCurso = new DAOCurso();
                        daoCurso.guardarCurso(diseñoExamen.getCurso(), conexion);
                    } else {
                        // Hay una nueva institucion también
                        DAOInstitucion daoInstitucion = new DAOInstitucion();
                        daoInstitucion.guardarInstitucionTransaccion(diseñoExamen.getCurso().getInstitucion(), conexion);

                        // Obtener el ID de la institucion
                        String strConsultaInstitucionId = "SELECT last_insert_rowid();";
                        PreparedStatement psConsultaInstitucionId = conexion.prepareStatement(strConsultaInstitucionId);

                        ResultSet rsConsultaInstitucionId = psConsultaInstitucionId.executeQuery();
                        int intUltimoIdInstitucion = rsConsultaInstitucionId.getInt(1);

                        Institucion instituciónNueva = new Institucion();
                        instituciónNueva.setIntInstitucionId(intUltimoIdInstitucion);
                        diseñoExamen.getCurso().setInstitucion(instituciónNueva);
                        DAOCurso dAOCurso = new DAOCurso();
                        dAOCurso.guardarCurso(diseñoExamen.getCurso(), conexion);
                    }

                    // Obtener el ID del curso
                    String strConsultaCursoId = "SELECT last_insert_rowid();";
                    PreparedStatement psConsultaCursoId = conexion.prepareStatement(strConsultaCursoId);

                    ResultSet rsConsultaCursoId = psConsultaCursoId.executeQuery();
                    int intUltimoIdCurso = rsConsultaCursoId.getInt(1);

                    psConsulta.setInt(1, intUltimoIdCurso);
                }
            }

            psConsulta.setString(2, diseñoExamen.getStrNombre());
            psConsulta.setString(3, diseñoExamen.getStrDescripcion());
            
            Usuario usuario = new DAOUsuario().getUsuario();
            psConsulta.setInt(4, usuario.getIntNroDocumento());

            psConsulta.execute();

            // Obtener el ID del diseño de examen
            strConsulta = "SELECT last_insert_rowid();";
            psConsulta = conexion.prepareStatement(strConsulta);

            ResultSet rsConsulta = psConsulta.executeQuery();
            int intUltimoIdDiseñoExamen = rsConsulta.getInt(1);
            diseñoExamen.setIntDiseñoExamenId(intUltimoIdDiseñoExamen);

            // Guardar las preguntas
            DAOPregunta daoPregunta = new DAOPregunta();
            for (Pregunta p : diseñoExamen.getColPreguntas()) {
                daoPregunta.guardarPregunta(p, intUltimoIdDiseñoExamen, conexion);
            }
            conexion.commit();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DAODiseñoExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    /**
     * Edita un diseño de examen en la bd.
     *
     * @param diseñoExamen diseño a editar.
     */
    private void editarDiseño(DiseñoExamen diseñoExamen) {

        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            conexion.setAutoCommit(false);

            new DAOTema().eliminarTemas(diseñoExamen, conexion);

            // Guardar el diseño de examen
            String strConsulta = "UPDATE " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " "
                    + "SET " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " = ?, "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_NOMBRE + " = ?, "
                    + EntropyDB.DIS_COL_DISEÑO_EXAMEN_DESCRIPCION + " = ?, "
                    + "WHERE " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);

            if (diseñoExamen.getCurso() == null) {
                psConsulta.setNull(1, Types.NULL);
            } else {
                if (diseñoExamen.getCurso().getIntCursoId() != 0) {
                    psConsulta.setInt(1, diseñoExamen.getCurso().getIntCursoId());
                } else { // Hay un nuevo curso
                    if (diseñoExamen.getCurso().getInstitucion().getIntInstitucionId() != 0) {
                        DAOCurso daoCurso = new DAOCurso();
                        daoCurso.guardarCurso(diseñoExamen.getCurso(), conexion);
                    } else {
                        // Hay una nueva institucion también
                        DAOInstitucion daoInstitucion = new DAOInstitucion();
                        daoInstitucion.guardarInstitucionTransaccion(diseñoExamen.getCurso().getInstitucion(), conexion);

                        // Obtener el ID del curso
                        String strConsultaInstitucionId = "SELECT last_insert_rowid();";
                        PreparedStatement psConsultaInstitucionId = conexion.prepareStatement(strConsultaInstitucionId);

                        ResultSet rsConsultaInstitucionId = psConsultaInstitucionId.executeQuery();
                        int intUltimoIdInstitucion = rsConsultaInstitucionId.getInt(1);

                        Institucion instituciónNueva = new Institucion();
                        instituciónNueva.setIntInstitucionId(intUltimoIdInstitucion);
                        diseñoExamen.getCurso().setInstitucion(instituciónNueva);
                        DAOCurso dAOCurso = new DAOCurso();
                        dAOCurso.guardarCurso(diseñoExamen.getCurso(), conexion);
                    }

                    // Obtener el ID del curso
                    String strConsultaCursoId = "SELECT last_insert_rowid();";
                    PreparedStatement psConsultaCursoId = conexion.prepareStatement(strConsultaCursoId);

                    ResultSet rsConsultaCursoId = psConsultaCursoId.executeQuery();
                    int intUltimoIdCurso = rsConsultaCursoId.getInt(1);

                    psConsulta.setInt(1, intUltimoIdCurso);
                }
            }

            psConsulta.setString(2, diseñoExamen.getStrNombre());
            psConsulta.setString(3, diseñoExamen.getStrDescripcion());
            psConsulta.setInt(4, diseñoExamen.getIntDiseñoExamenId());

            psConsulta.execute();

            DAOPregunta daoPregunta = new DAOPregunta();
            // Eliminar preguntas.
            daoPregunta.eliminarPreguntas(diseñoExamen);
            // Guardar las preguntas
            for (Pregunta p : diseñoExamen.getColPreguntas()) {
                daoPregunta.guardarPregunta(p, diseñoExamen.getIntDiseñoExamenId(), conexion);
            }

            conexion.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DAODiseñoExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    /*
     * Metodo para borrar de la bd un diseño de examen seleccionado en la administracion de diseños de examen
     * @param idDiseño, el id del diseño del examen seleccionado para editar
     */
    public void borrarDiseñoExamen(int diseñoExamenId) {
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            //conexion.setAutoCommit(false);
            String strConsulta = "DELETE FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " "
                    + "WHERE " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, diseñoExamenId);
            psConsulta.execute();
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institución en la BD: " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }
}
