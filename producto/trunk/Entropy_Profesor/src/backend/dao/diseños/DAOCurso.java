package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Institucion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que representa el objeto para el acceso a la entidad curso.
 * @author Jose Ruiz
 * 
 */
public class DAOCurso implements IDAOCurso {

    @Override
    public Curso recuperarCurso(int intCursoId) {
        Curso curso = null;
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta = "SELECT "
                    + "C." + EntropyDB.GRL_COL_CURSO_NOMBRE + ","
                    + "C." + EntropyDB.GRL_COL_CURSO_DESCRIPCION + ", "
                    + "I." + EntropyDB.GRL_COL_INSTITUCION_ID + ", "
                    + "I." + EntropyDB.GRL_COL_INSTITUCION_NOMBRE + ", "
                    + "I." + EntropyDB.GRL_COL_INSTITUCION_DESCRIPCION + ", "
                    + "I." + EntropyDB.GRL_COL_INSTITUCION_LOGO + " "
                    + "FROM " + EntropyDB.GRL_TBL_CURSO + " C JOIN "
                    + EntropyDB.GRL_TBL_INSTITUCION + " I ON C." 
                    + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = "
                    + "I." + EntropyDB.GRL_COL_INSTITUCION_ID + " "
                    + "WHERE C." + EntropyDB.GRL_COL_CURSO_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intCursoId);
            ResultSet rsConsulta = psConsulta.executeQuery();
            while (rsConsulta.next()) {
                curso = new Curso();
                curso.setIntCursoId(intCursoId);
                curso.setStrNombre(rsConsulta.getString(1));
                curso.setStrDescripcion(rsConsulta.getString(2));
                
                Institucion institucion = new Institucion();
                institucion.setIntInstitucionId(rsConsulta.getInt(3));
                institucion.setStrNombre(rsConsulta.getString(4));
                institucion.setStrDescripcion(rsConsulta.getString(5));
                institucion.setImgLogo(rsConsulta.getObject(6));
                
                curso.setInstitucion(institucion);                
            }
        } catch (Exception e) {
            System.err.println("Ocurrió un error mientras se recuperaba el curso: " + e.toString());
        }
        return curso;
    }

    @Override
    public ArrayList<Curso> recuperarTodosLosCursos(Institucion institucion, String strNombre) {
        ArrayList<Curso> cursos = new ArrayList<Curso>();
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta;
            if (strNombre.equals("")) {
                // Devuelve todos los cursos para una institucion en caso que no se haya realiza una busqueda especifica
                strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_CURSO + " "
                        + "WHERE " + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = ?";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, institucion.getIntInstitucionId());
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int cursoId = rsConsulta.getInt(EntropyDB.GRL_COL_CURSO_ID);
                    String nombre = rsConsulta.getString(EntropyDB.GRL_COL_CURSO_NOMBRE);
                    String descripcion = rsConsulta.getString(EntropyDB.GRL_COL_CURSO_DESCRIPCION);
                    Curso curso = new Curso(cursoId, nombre, descripcion, institucion);
                    cursos.add(curso);
                }
            } else {
                // Devuelve un curso o mas  segun coincidan con la busqueda del nombre y la institucion seleccionada
                strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_CURSO + " "
                        + "WHERE " + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = ? "
                        + "AND " + EntropyDB.GRL_COL_CURSO_NOMBRE + " LIKE (?)";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, institucion.getIntInstitucionId());
                psConsulta.setString(2, "%" + strNombre + "%");
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int cursoId = rsConsulta.getInt(EntropyDB.GRL_COL_CURSO_ID);
                    String nombre = rsConsulta.getString(EntropyDB.GRL_COL_CURSO_NOMBRE);
                    String descripcion = rsConsulta.getString(EntropyDB.GRL_COL_CURSO_DESCRIPCION);
                    Curso curso = new Curso(cursoId, nombre, descripcion, institucion);
                    cursos.add(curso);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrio un error mientras se recuperaba una institucion " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return cursos;
    }

    @Override
    public void guardarCurso(Curso curso, Institucion institucion, boolean editar) {
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            PreparedStatement psConsulta;
            if (!editar) {
                String strConsulta = "INSERT INTO " + EntropyDB.GRL_TBL_CURSO + " ("
                        + EntropyDB.GRL_COL_CURSO_NOMBRE + ", "
                        + EntropyDB.GRL_COL_CURSO_DESCRIPCION + ", "
                        + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID
                        + ") VALUES(?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, curso.getStrNombre());
                psConsulta.setObject(2, curso.getStrDescripcion());
                psConsulta.setObject(3, institucion.getIntInstitucionId());
            } else {
                String strConsulta = "UPDATE " + EntropyDB.GRL_TBL_CURSO + " "
                        + "SET " + EntropyDB.GRL_COL_CURSO_NOMBRE + " = ?, "
                        + EntropyDB.GRL_COL_CURSO_DESCRIPCION + " = ? "
                        + "WHERE " + EntropyDB.GRL_COL_CURSO_ID + " = ?";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, curso.getStrNombre());
                psConsulta.setObject(2, curso.getStrDescripcion());
                psConsulta.setInt(3, curso.getIntCursoId());
            }
            psConsulta.execute();
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba el curso en la BD " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }
    
    public void guardarCurso(Curso curso, Connection conexion) throws SQLException{
        String strConsulta = "INSERT INTO " + EntropyDB.GRL_TBL_CURSO + " ("
                        + EntropyDB.GRL_COL_CURSO_NOMBRE + ", "
                        + EntropyDB.GRL_COL_CURSO_DESCRIPCION + ", "
                        + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID
                        + ") VALUES(?,?,?)";
        PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
        psConsulta.setString(1, curso.getStrNombre());
        psConsulta.setObject(2, curso.getStrDescripcion());
        psConsulta.setObject(3, curso.getInstitucion().getIntInstitucionId());
        psConsulta.execute();
    }

    @Override
    public int idCurso() {
        int idUltimoCurso = 0;
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta = "SELECT  MAX (" + EntropyDB.GRL_COL_CURSO_ID + ") "
                    + "FROM " + EntropyDB.GRL_TBL_CURSO;
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();
            idUltimoCurso = rsConsulta.getInt(1);
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institucion en la BD " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return idUltimoCurso;
    }

    @Override
    public void borrarCurso(int intIdCurso, int intInstitucionId) {
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta = "DELETE FROM " + EntropyDB.GRL_TBL_CURSO + " "
                    + "WHERE " + EntropyDB.GRL_COL_CURSO_ID + " = ? "
                    + "AND " + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intIdCurso);
            psConsulta.setInt(2, intInstitucionId);
            psConsulta.execute();
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institucion en la BD " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }

    /*
    * Metodo para buscar un curso de acuerdo a un numero de diseño de examen
    * @param idDiseño, el id del diseño del examen seleccionado para editar
    * @return el curso encontrado en la consulta 
    */
    
    public Curso buscarCurso(int idDiseño) {
        Curso curso = null;
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta;
            strConsulta = "SELECT " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " "
                    + "FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " "
                    + "WHERE " + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idDiseño);
            ResultSet rsConsulta = psConsulta.executeQuery();

            int cursoId = rsConsulta.getInt(EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID);
            if (!rsConsulta.wasNull()) {
                strConsulta = "SELECT * "
                        + "FROM " + EntropyDB.GRL_TBL_CURSO + " "
                        + "WHERE " + EntropyDB.GRL_COL_CURSO_ID + " = ?";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, cursoId);
                rsConsulta = psConsulta.executeQuery();
                if (rsConsulta.next()) {
                    curso = new Curso();
                    curso.setIntCursoId(rsConsulta.getInt(EntropyDB.GRL_COL_CURSO_ID));
                    curso.setStrNombre(rsConsulta.getString(EntropyDB.GRL_COL_CURSO_NOMBRE));
                    curso.setStrDescripcion(rsConsulta.getString(EntropyDB.GRL_COL_CURSO_DESCRIPCION));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrio un error mientras se recuperaba un curso " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        
        return curso;
    }    

    @Override
    public boolean tieneDiseñosOExamenesAsociados(int intCursoId) {
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta = "SELECT 1  "
                    + "FROM " + EntropyDB.GRL_TBL_CURSO + " C "
                    + "WHERE (C." + EntropyDB.GRL_COL_CURSO_ID + " IN  "
                    + "(SELECT D." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID +" "
                    + "FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " D "
                    + "WHERE D." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID +" = ?)  "
                    + "OR C." + EntropyDB.GRL_COL_CURSO_ID + " IN  "
                    + "(SELECT E." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID +" "
                    + "FROM " + EntropyDB.EXA_TBL_EXAMEN + " E "
                    + "WHERE E." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID +" = ?))  ";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intCursoId);
            ResultSet rsConsulta = psConsulta.executeQuery();
            while (rsConsulta.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institucion en la BD " + e.toString());
            return true;
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }
}
