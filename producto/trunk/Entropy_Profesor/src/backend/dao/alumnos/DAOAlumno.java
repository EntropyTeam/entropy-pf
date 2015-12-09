package backend.dao.alumnos;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.usuarios.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Denise
 */
public class DAOAlumno implements IDAOAlumno{

    @Override
    public ArrayList<Alumno> buscarAlumnos(String strNombre, String strApellido, String strDocumento, String strLegajo, Institucion institucion, Curso curso) {
        Connection conexion;
        PreparedStatement psConsulta;
        ResultSet rsResultado;
        ArrayList<Alumno> lstAlumnos = new ArrayList<>();
        try {
            conexion = DAOConexion.conectarBaseDatos();
            
            String strConsulta = "SELECT "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_ID + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_NOMBRE + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_APELLIDO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO + ", "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + " "
                    + "FROM "
                    + EntropyDB.GRL_TBL_ALUMNO + " AL LEFT JOIN " + EntropyDB.RES_TBL_RESOLUCION + " RE ON AL." + EntropyDB.GRL_COL_ALUMNO_ID + " = RE." + EntropyDB.RES_COL_RESOLUCION_ALUMNO_ID + " "
                    + "LEFT JOIN " + EntropyDB.EXA_TBL_EXAMEN + " EX ON RE." + EntropyDB.RES_COL_RESOLUCION_EXAMEN_ID + " = EX." + EntropyDB.EXA_COL_EXAMEN_ID + " "
                    + "LEFT JOIN " + EntropyDB.GRL_TBL_CURSO + " C ON EX." + EntropyDB.EXA_COL_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID + " "
                    + "LEFT JOIN " + EntropyDB.GRL_TBL_INSTITUCION + " I ON I." + EntropyDB.GRL_COL_INSTITUCION_ID + " = C." + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " "
                    + "WHERE "
                    + "AL." + EntropyDB.GRL_COL_ALUMNO_NOMBRE + " LIKE (?) "
                    + "AND AL." + EntropyDB.GRL_COL_ALUMNO_APELLIDO + " LIKE (?) "
                   + "AND AL." + EntropyDB.GRL_COL_ALUMNO_DOCUMENTO + " LIKE (?) "
                    + "AND (AL." + EntropyDB.GRL_COL_ALUMNO_LEGAJO + " LIKE (?) OR AL." + EntropyDB.GRL_COL_ALUMNO_LEGAJO + " IS NULL)";

            if (curso != null) {
                strConsulta += "AND C." + EntropyDB.GRL_COL_CURSO_ID + " = ? ";
            } else if (institucion != null) {
                strConsulta += "AND I." + EntropyDB.GRL_COL_INSTITUCION_ID + " = ? ";
            }
            
            psConsulta = conexion.prepareStatement(strConsulta);

            psConsulta.setString(1, strNombre + "%");
            psConsulta.setString(2, strApellido + "%");
            psConsulta.setString(3, strDocumento + "%");
            psConsulta.setString(4, strLegajo + "%");
            
            if (curso != null) {
                psConsulta.setInt(5, curso.getIntCursoId());
            } else if (institucion != null) {
                psConsulta.setInt(5, institucion.getIntInstitucionId());
            }

            rsResultado = psConsulta.executeQuery();
            while(rsResultado.next()) {
                Alumno alumno = new Alumno();
                alumno.setIntAlumnoId(Integer.parseInt(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_ID)));
                alumno.setStrNombre(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_NOMBRE));
                alumno.setStrApellido(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_APELLIDO));
                alumno.setStrTipoDocumento(rsResultado.getString(EntropyDB.GRL_COL_ALUMNO_TIPO_DOCUMENTO));
                alumno.setIntNroDocumento(rsResultado.getInt(EntropyDB.GRL_COL_ALUMNO_DOCUMENTO));
                lstAlumnos.add(alumno);
            }
            rsResultado.close();
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lstAlumnos;
    }
    
}
