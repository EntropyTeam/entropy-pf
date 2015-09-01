package backend.dao.examenes;

import backend.dao.EntropyDB;
import backend.diseños.Tema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Lucas Cunibertti
 */
public class DAOTemaExamen implements IDAOTemaExamen {

    @Override
    public void guardarTemaExamen(Tema tema, int intExamenId, Connection conexion) throws SQLException {
        PreparedStatement psConsulta;
        String strConsulta = "INSERT INTO " + EntropyDB.EXA_TBL_TEMA + " ("
                + EntropyDB.EXA_COL_TEMA_EXAMEN_ID + ", "
                + EntropyDB.EXA_COL_TEMA_NOMBRE + ") VALUES(?,?)";
        psConsulta = conexion.prepareStatement(strConsulta);
        psConsulta.setInt(1, intExamenId);
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
    public int getIDsiExiste(int idExamen, String strTema, Connection conexion) throws SQLException {
        int intID = 0;
        String strConsultaTemaId = "SELECT " + EntropyDB.EXA_COL_TEMA_ID
                + " FROM " + EntropyDB.EXA_TBL_TEMA 
                + " WHERE " + EntropyDB.EXA_COL_TEMA_EXAMEN_ID + " = ? "
                + " AND " + EntropyDB.EXA_COL_TEMA_NOMBRE + " = ?";
        PreparedStatement psConsultaTemaId = conexion.prepareStatement(strConsultaTemaId);
        psConsultaTemaId.setInt(1, idExamen);
        psConsultaTemaId.setString(2, strTema);
        ResultSet rsConsultaTemaId = psConsultaTemaId.executeQuery();
        if (rsConsultaTemaId.next())
            intID = rsConsultaTemaId.getInt(EntropyDB.DIS_COL_TEMA_ID);
        return intID;
    }
    
    @Override
    public boolean existe(Tema tema, int intExamenId, Connection conexion) throws SQLException {
        String strConsultaTemaId = "SELECT 1 FROM " + EntropyDB.EXA_TBL_TEMA
                + " WHERE " + EntropyDB.EXA_COL_TEMA_ID + " = ? "
                + " AND " + EntropyDB.EXA_COL_TEMA_EXAMEN_ID + "= ? ";
        PreparedStatement psConsultaTemaId = conexion.prepareStatement(strConsultaTemaId);
        ResultSet rsConsultaTemaId = psConsultaTemaId.executeQuery();
        return rsConsultaTemaId.next();
    }
}
