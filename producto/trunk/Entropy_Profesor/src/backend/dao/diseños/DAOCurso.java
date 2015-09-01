package backend.dao.diseños;

import backend.dao.DAOConexion;
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

        } catch (Exception e) {
            System.err.println("Ocurrio un error mientras se recuperaba una institucion " + e.toString());
        }
        return curso;
    }

    @Override
    public ArrayList<Curso> recuperarTodosLosCursos(Institucion institucion, String strNombre) {
        ArrayList<Curso> cursos = new ArrayList<Curso>();
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta;
            if (strNombre.equals(""))//Devuelve todos los cursos para una institucion en caso que no se haya realiza una busqueda especifica
            {
                strConsulta = "SELECT * FROM curso WHERE institucionId=?";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, institucion.getIntInstitucionId());
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int cursoId = rsConsulta.getInt(1);
                    String nombre = rsConsulta.getString(2);
                    String descripcion = rsConsulta.getString(3);
                    Curso curso = new Curso(cursoId, nombre, descripcion, institucion);
                    cursos.add(curso);
                }
            } else // Devuelve un curso o mas  segun coincidan con la busqueda del nombre y la institucion seleccionada
            {
                strConsulta = "SELECT * FROM curso WHERE institucionId=? AND nombre LIKE (?)";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, institucion.getIntInstitucionId());
                psConsulta.setString(2, "%" + strNombre + "%");
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int cursoId = rsConsulta.getInt(1);
                    String nombre = rsConsulta.getString(2);
                    String descripcion = rsConsulta.getString(3);
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
                String strConsulta = "INSERT INTO curso(nombre, descripcion, institucionId) VALUES(?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, curso.getStrNombre());
                psConsulta.setObject(2, curso.getStrDescripcion());
                psConsulta.setObject(3, institucion.getIntInstitucionId());
            } else {
                String strConsulta = "UPDATE Curso SET nombre=?, descripcion=? WHERE cursoId = ? ";
                psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setString(1, curso.getStrNombre());
                psConsulta.setObject(2, curso.getStrDescripcion());
                psConsulta.setInt(3, curso.getIntCursoId());
            }
            psConsulta.execute();

        } catch (SQLException e) {
            System.err.println("Ha ocurrido un error mientras se guardaba la institucion en la BD " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
    }
    
    public void guardarCurso(Curso curso, Connection conexion) throws SQLException{
        String strConsulta = "INSERT INTO curso(nombre, descripcion, institucionId) VALUES(?,?,?)";
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
            String strConsulta = "SELECT  MAX (cursoId) FROM curso";
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
            String strConsulta = "DELETE FROM curso WHERE cursoId=? AND institucionId=?";
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
        Curso curso = new Curso();
        Connection conexion = DAOConexion.conectarBaseDatos();
        try {
            String strConsulta;
            strConsulta = "SELECT * FROM disenoexamen WHERE disenoexamenId=?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, idDiseño);
            ResultSet rsConsulta = psConsulta.executeQuery();

            DiseñoExamen diseño = new DiseñoExamen();
            diseño.setIntDiseñoExamenId(idDiseño);
            diseño.setStrDescripcion(rsConsulta.getString(4));
            diseño.setStrNombre(rsConsulta.getString(3));

            int id = diseño.getIntDiseñoExamenId();
            strConsulta = "SELECT c.cursoId, c.nombre, c.descripcion FROM curso C, disenoexamen DE WHERE de.cursoID=c.cursoID and de.disenoexamenId=?";
            psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, id);
            rsConsulta = psConsulta.executeQuery();
            if (rsConsulta.next()) {
                int cursoId = rsConsulta.getInt(1);
                String nombre = rsConsulta.getString(2);
                String descripcion = rsConsulta.getString(3);
                curso.setIntCursoId(cursoId);
                curso.setStrNombre(nombre);
                curso.setStrDescripcion(descripcion);
                return curso;
            }
            return null;

        } catch (SQLException e) {
            System.err.println("pelitoOcurrio un error mientras se recuperaba un curso " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();

        }
        
         return curso;
    }    
}
