package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.DiseñoExamen;
import backend.diseños.Tema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class DAOTema implements IDAOTema {

    @Override
    public ArrayList<Tema> getTemas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Tema> getTemas(String strCadena) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Tema> getTemasPorExamen(int intDiseñoExamenId) {
        ArrayList<Tema> temas = new ArrayList<Tema>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta;

            {
                strConsulta = "SELECT * FROM Tema t WHERE disenoExamenId=? ";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, intDiseñoExamenId);
                ResultSet rsConsulta = psConsulta.executeQuery();
                while (rsConsulta.next()) {
                    int temaId = rsConsulta.getInt(1);
                    String nombreTema = rsConsulta.getString(3);
                    Tema tema = new Tema(temaId, nombreTema);
                    temas.add(tema);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrio un error mientras se recuperaba un tema " + e.toString());
        } finally {
            DAOConexion.desconectarBaseDatos();
        }

        return temas;
    }

    @Override
    public void guardarTemas(ArrayList<Tema> colTemas) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void guardarTema(Tema tema, int intDiseñoExamenId, Connection conexion) throws SQLException {
        PreparedStatement psConsulta;
        String strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_TEMA + " ("
                + EntropyDB.DIS_COL_TEMA_DISEÑO_EXAMEN_ID + ", "
                + EntropyDB.DIS_COL_TEMA_NOMBRE + ") VALUES(?,?)";
        psConsulta = conexion.prepareStatement(strConsulta);
        psConsulta.setInt(1, intDiseñoExamenId);
        psConsulta.setString(2, tema.getStrNombre());
        psConsulta.execute();

        // Obtener el ID del tema
        String strConsultaTemaId = "SELECT last_insert_rowid();";
        PreparedStatement psConsultaTemaId = conexion.prepareStatement(strConsultaTemaId);
        ResultSet rsConsultaTemaId = psConsultaTemaId.executeQuery();
        int intUltimoIdTema = rsConsultaTemaId.getInt(1);
        tema.setIntTemaId(intUltimoIdTema);
    }

    @Override
    public void eliminarTemas(DiseñoExamen diseño, Connection conexion) throws SQLException {
        PreparedStatement psConsulta;
        String strConsulta = "DELETE FROM tema WHERE disenoExamenId = ?";
        psConsulta = conexion.prepareStatement(strConsulta);
        psConsulta.setInt(1, diseño.getIntDiseñoExamenId());
        psConsulta.execute();
    }

    @Override
    public int getIDsiExiste(int idDiseño, String strTema, Connection conexion) throws SQLException {
        int intID = 0;
        String strConsultaTemaId = "SELECT "+EntropyDB.DIS_COL_TEMA_ID
                + " FROM " + EntropyDB.DIS_TBL_TEMA 
                + " WHERE " + EntropyDB.DIS_COL_TEMA_DISEÑO_EXAMEN_ID + " = ? "
                + " AND " + EntropyDB.DIS_COL_TEMA_NOMBRE + " = ?";
        PreparedStatement psConsultaTemaId = conexion.prepareStatement(strConsultaTemaId);
        psConsultaTemaId.setInt(1, idDiseño);
        psConsultaTemaId.setString(2, strTema);
        ResultSet rsConsultaTemaId = psConsultaTemaId.executeQuery();
        if (rsConsultaTemaId.next())
            intID = rsConsultaTemaId.getInt(EntropyDB.DIS_COL_TEMA_ID);
        return intID;
    }
}
