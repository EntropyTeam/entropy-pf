package backend.dao.diseños;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.diseños.CombinacionRelacionColumnas;
import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Filtro;
import backend.diseños.Institucion;
import backend.diseños.OpcionMultipleOpcion;
import backend.diseños.Pregunta;
import backend.diseños.PreguntaMultipleOpcion;
import backend.diseños.PreguntaNumerica;
import backend.diseños.PreguntaRelacionColumnas;
import backend.diseños.PreguntaVerdaderoFalso;
import backend.diseños.Tema;
import backend.diseños.TipoPregunta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti Comentada por Gaston Noves
 */
public class DAOPregunta implements IDAOPregunta {

    @Override
    public Pregunta getPregunta(int intPreguntaId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardarPregunta(Pregunta pregunta, int intDiseñoExamenId, Connection conexion) throws SQLException {
        String strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_PREGUNTA + " ("
                + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + ", "
                + EntropyDB.DIS_COL_PREGUNTA_ORDEN + ", "
                + EntropyDB.DIS_COL_PREGUNTA_TEMA_ID + ", "
                + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                + EntropyDB.DIS_COL_PREGUNTA_NIVEL + ", "
                + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                + EntropyDB.DIS_COL_PREGUNTA_PUNTAJE + ", "
                + EntropyDB.DIS_COL_PREGUNTA_REFERENCIA
                + ") VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);

        psConsulta.setInt(1, intDiseñoExamenId);
        psConsulta.setInt(2, pregunta.getIntOrden());

        // Consultar el tema o guardar en caso que no este, y traer id. Por ahora NULL.
        if (pregunta.getTema() == null) {
            psConsulta.setNull(3, Types.NULL);
        } else {
            DAOTema daoTema = new DAOTema();
            pregunta.getTema().setIntTemaId(daoTema.getIDsiExiste(intDiseñoExamenId, pregunta.getTema().getStrNombre(), conexion));
            if (pregunta.getTema().getIntTemaId() == 0){
                daoTema.guardarTema(pregunta.getTema(), intDiseñoExamenId, conexion);
            }
            psConsulta.setInt(3, pregunta.getTema().getIntTemaId());
        }

        psConsulta.setInt(4, pregunta.getIntTipo());
        psConsulta.setString(5, pregunta.getStrNivel());
        psConsulta.setString(6, pregunta.getStrEnunciado());
        psConsulta.setDouble(7, pregunta.getDblPuntaje());
        psConsulta.setString(8, pregunta.getStrReferencia());

        psConsulta.execute();

        // Obtener el ID del la preguntas
        strConsulta = "SELECT last_insert_rowid();";
        psConsulta = conexion.prepareStatement(strConsulta);

        ResultSet rsConsulta = psConsulta.executeQuery();
        int intUltimoId = rsConsulta.getInt(1);

        // Guardar Tags
        DAOTag daoTag = new DAOTag();
        daoTag.guardarTags(pregunta.getColTags(), conexion);

        // Guardar TagsXPreguntas
        for (String tag : pregunta.getColTags()) {
            strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " ("
                    + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + ", "
                    + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID
                    + ") VALUES (?,?)";
            psConsulta = conexion.prepareStatement(strConsulta);

            psConsulta.setString(1, tag);
            psConsulta.setInt(2, intUltimoId);

            psConsulta.execute();
        }

        // Guardar de acuerdo al Tipo de Pregunta
        switch (pregunta.getIntTipo()) {
            case TipoPregunta.MULTIPLE_OPCION:
                PreguntaMultipleOpcion preguntaMO = (PreguntaMultipleOpcion) pregunta;
                for (OpcionMultipleOpcion opcion : preguntaMO.getColOpciones()) {
                    strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_PREGUNTA_MO + " ("
                            + EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_MO_ORDEN + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_MO_OPCION + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_MO_CORRECTO
                            + ") VALUES (?,?,?,?)";
                    psConsulta = conexion.prepareStatement(strConsulta);

                    psConsulta.setInt(1, intUltimoId);
                    psConsulta.setInt(2, opcion.getIntOrden());
                    psConsulta.setString(3, opcion.getStrRespuesta());
                    psConsulta.setBoolean(4, opcion.isBlnEsVerdadera());

                    psConsulta.execute();
                }
                break;

            case TipoPregunta.VERDADERO_FALSO:
                PreguntaVerdaderoFalso preguntaVF = (PreguntaVerdaderoFalso) pregunta;
                strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_PREGUNTA_VF + " ("
                            + EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_VF_VERDADERO + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_VF_JUSTIFICACION
                            + ") VALUES (?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);

                psConsulta.setInt(1, intUltimoId);
                psConsulta.setBoolean(2, preguntaVF.isBlnEsVerdadera());
                psConsulta.setBoolean(3, preguntaVF.isBlnConJustificacion());

                psConsulta.execute();
                break;

            case TipoPregunta.RELACION_COLUMNAS:
                PreguntaRelacionColumnas preguntaRC = (PreguntaRelacionColumnas) pregunta;
                for (CombinacionRelacionColumnas combinacion : preguntaRC.getColCombinaciones()) {
                    strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_PREGUNTA_RC + " ("
                            + EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_RC_ORDEN + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_DERECHA
                            + ") VALUES (?,?,?,?)";
                    psConsulta = conexion.prepareStatement(strConsulta);

                    psConsulta.setInt(1, intUltimoId);
                    psConsulta.setInt(2, combinacion.getIntOrden());
                    psConsulta.setString(3, combinacion.getStrColumnaIzquierda());
                    psConsulta.setString(4, combinacion.getStrColumnaDerecha());

                    psConsulta.execute();
                }
                break;

            case TipoPregunta.NUMERICA:
                PreguntaNumerica preguntaNU = (PreguntaNumerica) pregunta;
                strConsulta = "INSERT INTO " + EntropyDB.DIS_TBL_PREGUNTA_NU + " ("
                            + EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_NU_ES_RANGO + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_NU_NUMERO + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_DESDE + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_HASTA + ", "
                            + EntropyDB.DIS_COL_PREGUNTA_NU_VARIACION
                            + ") VALUES (?,?,?,?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);

                psConsulta.setInt(1, intUltimoId);
                psConsulta.setBoolean(2, preguntaNU.esRango());
                if (preguntaNU.esRango()) {
                    psConsulta.setDouble(3, 0);
                    psConsulta.setDouble(4, preguntaNU.getDblRangoDesde());
                    psConsulta.setDouble(5, preguntaNU.getDblRangoHasta());
                } else {
                    psConsulta.setDouble(3, preguntaNU.getDblNumero());
                    psConsulta.setDouble(4, 0);
                    psConsulta.setDouble(5, 0);
                }
                psConsulta.setDouble(6, preguntaNU.getDblVariacion());

                psConsulta.execute();
        }
    }

    @Override
    public ArrayList<Pregunta> getPreguntaPorTag(String strCadena) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Pregunta> pregunta = null;

        try {
            String strConsulta = "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ORDEN + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TEMA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_PUNTAJE + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_REFERENCIA + " "
                    + "FROM " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " TP "
                    + "INNER JOIN " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                    + "ON P." + EntropyDB.DIS_COL_PREGUNTA_ID + " = TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID + " "
                    + "WHERE TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + " LIKE (?)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, strCadena);
            ResultSet rsConsulta = psConsulta.executeQuery();
            pregunta = new ArrayList<>();
            int intOrden = 0;
            while (rsConsulta.next()) {
                ArrayList adjuntos = null;
                ArrayList<String> tags = null;
                int intIdPreg = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_ID);
                intOrden = intOrden + 1;
                int intIdTema = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TEMA_ID);//No me debaria interesar porque el tema depende del examen.
                int intIdTipoPre = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID);
                String strNivel = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_NIVEL);
                String strEnunciado = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO);
                double dblPuntaje = rsConsulta.getDouble(EntropyDB.DIS_COL_PREGUNTA_PUNTAJE);
                String strReferencia = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_REFERENCIA);

                try {
                    String strConsultaAdjunto = "SELECT * "
                            + "FROM " + EntropyDB.DIS_TBL_ADJUNTO + " "
                            + "WHERE " + EntropyDB.DIS_COL_ADJUNTO_PREGUNTA_ID + " = ?";
                    PreparedStatement psConsultaAdjunto = conexion.prepareStatement(strConsultaAdjunto);
                    psConsultaAdjunto.setInt(1, intIdPreg);
                    ResultSet rsConsultaAdjunto = psConsultaAdjunto.executeQuery();
                    adjuntos = new ArrayList();
                    while (rsConsultaAdjunto.next()) {
                        adjuntos.add(rsConsultaAdjunto.getObject(EntropyDB.DIS_COL_ADJUNTO_ADJUNTO));
                    }

                } catch (SQLException e) {
                    System.out.println(e);
                }
                try {
                    String strConsultaTag = "SELECT " + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + " "
                            + "FROM " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " "
                            + "WHERE " + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID + " = ?";
                    PreparedStatement psConsultaTag = conexion.prepareStatement(strConsultaTag);
                    psConsultaTag.setInt(1, intIdPreg);
                    ResultSet rsConsultaTag = psConsultaTag.executeQuery();
                    tags = new ArrayList();
                    while (rsConsultaTag.next()) {
                        tags.add(rsConsultaTag.getString(EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID));
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }

                switch (intIdTipoPre) {
                    case TipoPregunta.DESARROLLAR:
                        Pregunta pregDesarrollar = new Pregunta(intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                        pregunta.add(pregDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:

                        try {
                            ArrayList<OpcionMultipleOpcion> opciones = new ArrayList<>();
                            String strConsultaMO = "SELECT * "
                                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_MO + " "
                                    + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID + " = ?";
                            PreparedStatement psConsultaMO = conexion.prepareStatement(strConsultaMO);
                            psConsultaMO.setInt(1, intIdPreg);
                            ResultSet rsConsultaMO = psConsultaMO.executeQuery();

                            while (rsConsultaMO.next()) {
                                int intIdPr = rsConsultaMO.getInt(EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID);
                                int intOr = rsConsultaMO.getInt(EntropyDB.DIS_COL_PREGUNTA_MO_ORDEN);
                                String stOpcion = rsConsultaMO.getString(EntropyDB.DIS_COL_PREGUNTA_MO_OPCION);
                                boolean blCorrecto = rsConsultaMO.getBoolean(EntropyDB.DIS_COL_PREGUNTA_MO_CORRECTO);
                                OpcionMultipleOpcion opcion = new OpcionMultipleOpcion(intOr, stOpcion, blCorrecto);
                                opciones.add(opcion);
                            }

                            PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(opciones, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                            pregunta.add(pregMO);
                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        try {
                            String strConsultaPVF = "SELECT * "
                                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_VF + " "
                                    + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID + " = ?";
                            PreparedStatement psConsultaPVF = conexion.prepareStatement(strConsultaPVF);
                            psConsultaPVF.setInt(1, intIdPreg);
                            ResultSet rsConsultaPVF = psConsultaPVF.executeQuery();

                            int intPVFId = rsConsultaPVF.getInt(EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID);
                            boolean blVerdadera = rsConsultaPVF.getBoolean(EntropyDB.DIS_COL_PREGUNTA_VF_VERDADERO);
                            boolean blJustificacion = rsConsultaPVF.getBoolean(EntropyDB.DIS_COL_PREGUNTA_VF_JUSTIFICACION);

                            PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(blVerdadera, blJustificacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                            pregunta.add(pVF);
                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        try {
                            String strConsultaPRC = "SELECT * "
                                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_RC + " "
                                    + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID + " = ?";
                            PreparedStatement psConsultaPRC = conexion.prepareStatement(strConsultaPRC);
                            psConsultaPRC.setInt(1, intIdPreg);
                            ResultSet rsConsultaPRC = psConsultaPRC.executeQuery();

                            int intPRCId = rsConsultaPRC.getInt(EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID);
                            ArrayList<CombinacionRelacionColumnas> colCombinacion = new ArrayList<>();

                            while (rsConsultaPRC.next()) {
                                String strColIzq = rsConsultaPRC.getString(EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA);
                                String strColDer = rsConsultaPRC.getString(EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_DERECHA);
                                int intOrdenPRC = rsConsultaPRC.getInt(EntropyDB.DIS_COL_PREGUNTA_RC_ORDEN);
                                CombinacionRelacionColumnas combinacionRelacionColumnas = new CombinacionRelacionColumnas(intOrdenPRC, strColIzq, strColDer);
                                colCombinacion.add(combinacionRelacionColumnas);
                            }
                            PreguntaRelacionColumnas preguntaRelacionColumnas = new PreguntaRelacionColumnas(colCombinacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags);
                            pregunta.add(preguntaRelacionColumnas);
                        } catch (SQLException e) {
                            System.err.println(e);
                        }
                        break;

                    case TipoPregunta.NUMERICA:
                        try {
                            String strConsultaPN = "SELECT * "
                                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_NU + " "
                                    + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID + " = ?";
                            PreparedStatement psConsultaPN = conexion.prepareStatement(strConsultaPN);
                            psConsultaPN.setInt(1, intIdPreg);
                            ResultSet rsConsultaPN = psConsultaPN.executeQuery();

                            int intPNId = rsConsultaPN.getInt(EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID);
                            boolean blRango = rsConsultaPN.getBoolean(EntropyDB.DIS_COL_PREGUNTA_NU_ES_RANGO);
                            double dblNumero = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_NUMERO);
                            double dblRangoDesde = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_DESDE);
                            double dblRangoHasta = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_HASTA);
                            double dblVariacion = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_VARIACION);
                            PreguntaNumerica pregNum = new PreguntaNumerica(blRango, dblNumero, dblRangoDesde, dblRangoHasta, dblVariacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags);
                            pregunta.add(pregNum);
                        } catch (SQLException e) {
                            System.err.println(e);
                        }
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return pregunta;
    }

    @Override
    public ArrayList<Pregunta> getPreguntasPorFiltros(ArrayList<Filtro> colFiltro) {
        boolean firstloop = true;
        ArrayList<Pregunta> colPreguntas = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();
        int contParametros = 1;
        PreparedStatement psConsulta;
        try {
            String strConsulta = "SELECT DISTINCT "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + " "
                    + "FROM " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " DE "
                    + "LEFT JOIN " + EntropyDB.GRL_TBL_CURSO + " C ON DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID + " "
                    + "LEFT JOIN " + EntropyDB.DIS_TBL_PREGUNTA + " P ON P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " "
                    + "LEFT JOIN " + EntropyDB.GRL_TBL_INSTITUCION + " I ON C." + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = I." + EntropyDB.GRL_COL_INSTITUCION_ID + " "
                    + "LEFT JOIN " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " TP ON P." + EntropyDB.DIS_COL_PREGUNTA_ID + " = TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID;
            String strWhere = "";
            for (Filtro filtro : colFiltro) {
                switch (filtro.getFiltro()) {
                    case INSTITUCION:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " I." + EntropyDB.GRL_COL_INSTITUCION_ID + " = ?";
                        break;
                    case CON_CURSO:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " IS NOT NULL";
                        break;
                    case CURSO:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " C." + EntropyDB.GRL_COL_CURSO_ID + " = ?";
                        break;
                    case SIN_CURSO:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " IS NULL";
                        break;
                    case DISEÑOEXAMEN:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " = ?";
                        break;
                    case TAG:
                        if (firstloop) {
                            strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + " LIKE ?";
                            firstloop = false;
                        } else {
                            strWhere += ((strWhere.isEmpty()) ? "" : " OR") + " TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + " LIKE ?";
                        }
                        break;
                }
            }
            strConsulta += ((strWhere.isEmpty()) ? "" : " WHERE" + strWhere) + " GROUP BY P." + EntropyDB.DIS_COL_PREGUNTA_ID;
            psConsulta = conexion.prepareStatement(strConsulta);
            for (Filtro filtro : colFiltro) {
                switch (filtro.getFiltro()) {
                    case INSTITUCION:
                        psConsulta.setObject(contParametros, filtro.getInstitucion().getIntInstitucionId());
                        break;
                    case CURSO:
                        psConsulta.setObject(contParametros, filtro.getCurso().getIntCursoId());
                        break;
                    case DISEÑOEXAMEN:
                        psConsulta.setObject(contParametros, filtro.getDiseñoExamen().getIntDiseñoExamenId());
                        break;
                    case TAG:
                        psConsulta.setObject(contParametros, filtro.getTag());
                        break;
                }
                contParametros++;
            }

            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(1);
                String strEnunciado = rsConsulta.getString(2);
                String strNivel = rsConsulta.getString(3);
                int intIdTipoPre = rsConsulta.getInt(4);

                switch (intIdTipoPre) {
                    case TipoPregunta.DESARROLLAR:
                        Pregunta pregDesarrollar = new Pregunta(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntas.add(pregDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntas.add(pregMO);

                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntas.add(pVF);

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas pregRelCol = new PreguntaRelacionColumnas(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntas.add(pregRelCol);
                        break;

                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica pregNum = new PreguntaNumerica(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntas.add(pregNum);
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return colPreguntas;
    }

    @Override
    public ArrayList<Pregunta> getPreguntasPorID(ArrayList<Integer> colIDPregunta) {
        ArrayList<Pregunta> colPregunta = new ArrayList<>();
        for (Integer intIDPregunta : colIDPregunta) {
            Connection conexion = DAOConexion.conectarBaseDatos();

            try {
                String strConsulta = "SELECT "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_ORDEN + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_TEMA_ID + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_PUNTAJE + ", "
                        + "P." + EntropyDB.DIS_COL_PREGUNTA_REFERENCIA + ", "
                        + "T." + EntropyDB.DIS_COL_TEMA_NOMBRE + " "
                        + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                        + "LEFT JOIN " + EntropyDB.DIS_TBL_TEMA + " T "
                        + "ON P." + EntropyDB.DIS_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.DIS_COL_TEMA_ID + " "
                        + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_ID + " = ?";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, intIDPregunta);
                ResultSet rsConsulta = psConsulta.executeQuery();
                
                int intOrden = 0;
                while (rsConsulta.next()) {
                    ArrayList adjuntos = null;
                    ArrayList<String> tags = null;
                    int intIdPreg = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_ID);
                    intOrden = intOrden + 1;
                    
                    int intIdTema = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TEMA_ID);
                    Tema tema = null;
                    if(intIdTema != 0) {
                        tema = new Tema();
                        tema.setIntTemaId(intIdTema);
                        tema.setStrNombre(rsConsulta.getString(EntropyDB.DIS_COL_TEMA_NOMBRE));
                    }
                    
                    int intIdTipoPre = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID);
                    String strNivel = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_NIVEL);
                    String strEnunciado = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO);
                    double dblPuntaje = rsConsulta.getDouble(EntropyDB.DIS_COL_PREGUNTA_PUNTAJE);
                    String strReferencia = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_REFERENCIA);

                    try {
                        String strConsultaAdjunto = "SELECT * "
                                + "FROM " + EntropyDB.DIS_TBL_ADJUNTO + " "
                                + "WHERE " + EntropyDB.DIS_COL_ADJUNTO_PREGUNTA_ID + " = ?";
                        PreparedStatement psConsultaAdjunto = conexion.prepareStatement(strConsultaAdjunto);
                        psConsultaAdjunto.setInt(1, intIdPreg);
                        ResultSet rsConsultaAdjunto = psConsultaAdjunto.executeQuery();
                        adjuntos = new ArrayList();
                        while (rsConsultaAdjunto.next()) {
                            adjuntos.add(rsConsultaAdjunto.getObject(EntropyDB.DIS_COL_ADJUNTO_ADJUNTO));
                        }

                    } catch (SQLException e) {
                        System.err.println(e);
                    }
                    try {
                        String strConsultaTag = "SELECT * "
                                + "FROM " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " "
                                + "WHERE " + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID + " = ?";
                        PreparedStatement psConsultaTag = conexion.prepareStatement(strConsultaTag);
                        psConsultaTag.setInt(1, intIdPreg);
                        ResultSet rsConsultaTag = psConsultaTag.executeQuery();
                        tags = new ArrayList();
                        while (rsConsultaTag.next()) {
                            tags.add(rsConsultaTag.getString(EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID));
                        }
                    } catch (SQLException e) {
                        System.err.println(e);
                    }

                    switch (intIdTipoPre) {
                        case TipoPregunta.DESARROLLAR:
                            Pregunta pregDesarrollar = new Pregunta(intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                            pregDesarrollar.setTema(tema);
                            colPregunta.add(pregDesarrollar);
                            break;

                        case TipoPregunta.MULTIPLE_OPCION:

                            try {
                                ArrayList<OpcionMultipleOpcion> opciones = new ArrayList<>();
                                String strConsultaMO = "SELECT "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID + ", "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_ORDEN + ", "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_OPCION + ", "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_CORRECTO + " "
                                        + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_MO + " PMO "
                                        + "WHERE PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID + " = ? "
                                        + "GROUP BY PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID + ", "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_ORDEN + ", "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_OPCION + ", "
                                        + "PMO." + EntropyDB.DIS_COL_PREGUNTA_MO_CORRECTO;
                                PreparedStatement psConsultaMO = conexion.prepareStatement(strConsultaMO);
                                psConsultaMO.setInt(1, intIdPreg);
                                ResultSet rsConsultaMO = psConsultaMO.executeQuery();

                                while (rsConsultaMO.next()) {

                                    int intIdPr = rsConsultaMO.getInt(EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID);
                                    int intOr = rsConsultaMO.getInt(EntropyDB.DIS_COL_PREGUNTA_MO_ORDEN);
                                    String stOpcion = rsConsultaMO.getString(EntropyDB.DIS_COL_PREGUNTA_MO_OPCION);
                                    boolean blCorrecto = rsConsultaMO.getBoolean(EntropyDB.DIS_COL_PREGUNTA_MO_CORRECTO);
                                    OpcionMultipleOpcion opcion = new OpcionMultipleOpcion(intOr, stOpcion, blCorrecto);
                                    opciones.add(opcion);
                                }

                                PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(opciones, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                                pregMO.setTema(tema);
                                colPregunta.add(pregMO);
                            } catch (SQLException e) {
                                System.err.println(e);
                            }
                            break;

                        case TipoPregunta.VERDADERO_FALSO:
                            try {
                                String strConsultaPVF = "SELECT "
                                        + "PVF." + EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID + ", "
                                        + "PVF." + EntropyDB.DIS_COL_PREGUNTA_VF_VERDADERO + ", "
                                        + "PVF." + EntropyDB.DIS_COL_PREGUNTA_VF_JUSTIFICACION + " "
                                        + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_VF + " PVF "
                                        + "WHERE PVF." + EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID + " = ?";
                                PreparedStatement psConsultaPVF = conexion.prepareStatement(strConsultaPVF);
                                psConsultaPVF.setInt(1, intIdPreg);
                                ResultSet rsConsultaPVF = psConsultaPVF.executeQuery();

                                int intPVFId = rsConsultaPVF.getInt(EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID);
                                boolean blVerdadera = rsConsultaPVF.getBoolean(EntropyDB.DIS_COL_PREGUNTA_VF_VERDADERO);
                                boolean blJustificacion = rsConsultaPVF.getBoolean(EntropyDB.DIS_COL_PREGUNTA_VF_JUSTIFICACION);

                                PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(blVerdadera, blJustificacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                                pVF.setTema(tema);
                                colPregunta.add(pVF);
                            } catch (SQLException e) {
                                System.err.println(e);
                            }
                            break;

                        case TipoPregunta.RELACION_COLUMNAS:
                            try {
                                String strConsultaPRC = "SELECT "
                                        + "PRC." + EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID + ", "
                                        + "PRC." + EntropyDB.DIS_COL_PREGUNTA_RC_ORDEN + ", "
                                        + "PRC." + EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA + ", "
                                        + "PRC." + EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_DERECHA + " "
                                        + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_RC + " PRC "
                                        + "WHERE PRC." + EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID + " = ?";
                                PreparedStatement psConsultaPRC = conexion.prepareStatement(strConsultaPRC);
                                psConsultaPRC.setInt(1, intIdPreg);
                                ResultSet rsConsultaPRC = psConsultaPRC.executeQuery();

                                int intPRCId = rsConsultaPRC.getInt(EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID);
                                ArrayList<CombinacionRelacionColumnas> colCombinacion = new ArrayList<>();

                                while (rsConsultaPRC.next()) {

                                    String strColIzq = rsConsultaPRC.getString(EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA);
                                    String strColDer = rsConsultaPRC.getString(EntropyDB.DIS_COL_PREGUNTA_RC_COLUMNA_DERECHA);
                                    int intOrdenPRC = rsConsultaPRC.getInt(EntropyDB.DIS_COL_PREGUNTA_RC_ORDEN);
                                    CombinacionRelacionColumnas combinacionRelacionColumnas = new CombinacionRelacionColumnas(intOrdenPRC, strColIzq, strColDer);
                                    colCombinacion.add(combinacionRelacionColumnas);
                                }
                                PreguntaRelacionColumnas preguntaRelacionColumnas = new PreguntaRelacionColumnas(colCombinacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags);
                                preguntaRelacionColumnas.setTema(tema);
                                colPregunta.add(preguntaRelacionColumnas);
                            } catch (SQLException e) {
                                System.err.println(e);
                            }
                            break;

                        case TipoPregunta.NUMERICA:
                            try {
                                String strConsultaPN = "SELECT "
                                        + "PN." + EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID + ", "
                                        + "PN." + EntropyDB.DIS_COL_PREGUNTA_NU_ES_RANGO + ", "
                                        + "PN." + EntropyDB.DIS_COL_PREGUNTA_NU_NUMERO + ", "
                                        + "PN." + EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_DESDE + ", "
                                        + "PN." + EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_HASTA + ", "
                                        + "PN." + EntropyDB.DIS_COL_PREGUNTA_NU_VARIACION + " "
                                        + "FROM " + EntropyDB.DIS_TBL_PREGUNTA_NU + " PN "
                                        + "WHERE PN." + EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID + " = ?";
                                PreparedStatement psConsultaPN = conexion.prepareStatement(strConsultaPN);
                                psConsultaPN.setInt(1, intIdPreg);
                                ResultSet rsConsultaPN = psConsultaPN.executeQuery();

                                int intPNId = rsConsultaPN.getInt(EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID);
                                boolean blRango = rsConsultaPN.getBoolean(EntropyDB.DIS_COL_PREGUNTA_NU_ES_RANGO);
                                double dblNumero = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_NUMERO);
                                double dblRangoDesde = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_DESDE);
                                double dblRangoHasta = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_RANGO_HASTA);
                                double dblVariacion = rsConsultaPN.getDouble(EntropyDB.DIS_COL_PREGUNTA_NU_VARIACION);
                                PreguntaNumerica pregNum = new PreguntaNumerica(blRango, dblNumero, dblRangoDesde, dblRangoHasta, dblVariacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags);
                                pregNum.setTema(tema);
                                colPregunta.add(pregNum);
                            } catch (SQLException e) {
                                System.err.println(e);
                            }
                            break;
                    }
                }

            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                DAOConexion.desconectarBaseDatos();
            }
        }

        return colPregunta;
    }

    @Override
    public ArrayList<Pregunta> getPreguntasPorDiseñoExamen(DiseñoExamen diseñoExamen) {
        ArrayList<Pregunta> colPreuntaDE = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_PUNTAJE + " "
                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P, " + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " DE "
                    + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " "
                    + "AND DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, diseñoExamen.getIntDiseñoExamenId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_ID);
                String strEnunciado = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO);
                int intIdTipoPre = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID);
                String strNivel = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_NIVEL);

                switch (intIdTipoPre) {
                    case TipoPregunta.DESARROLLAR:
                        Pregunta pregDesarrollar = new Pregunta(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaDE.add(pregDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaDE.add(pregMO);

                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaDE.add(pVF);

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas pregRelCol = new PreguntaRelacionColumnas(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaDE.add(pregRelCol);
                        break;

                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica pregNum = new PreguntaNumerica(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaDE.add(pregNum);
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }

        return colPreuntaDE;
    }

    public ArrayList<Pregunta> buscarTodasLasPreguntas() {
        ArrayList<Pregunta> colPreuntaTodas = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + " "
                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_ID);
                String strEnunciado = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO);
                int intIdTipoPre = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID);
                String strNivel = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_NIVEL);

                switch (intIdTipoPre) {
                    case TipoPregunta.DESARROLLAR:
                        Pregunta pregDesarrollar = new Pregunta(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaTodas.add(pregDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaTodas.add(pregMO);

                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaTodas.add(pVF);

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas pregRelCol = new PreguntaRelacionColumnas(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaTodas.add(pregRelCol);
                        break;

                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica pregNum = new PreguntaNumerica(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaTodas.add(pregNum);
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }

        return colPreuntaTodas;
    }

    @Override
    public ArrayList<Pregunta> buscarTodasLasPreguntasPorInstitucion(Institucion institucionSeleccionada) {
        ArrayList<Pregunta> colPreuntaInstitucion = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + " "
                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P, "
                    + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " DE, "
                    + EntropyDB.GRL_TBL_INSTITUCION + " I, "
                    + EntropyDB.GRL_TBL_CURSO + " C "
                    + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " "
                    + "AND DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID + " "
                    + "AND C." + EntropyDB.GRL_COL_CURSO_INSTITUCION_ID + " = I." + EntropyDB.GRL_COL_INSTITUCION_ID + " "
                    + "AND I." + EntropyDB.GRL_COL_INSTITUCION_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, institucionSeleccionada.getIntInstitucionId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_ID);
                String strEnunciado = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO);
                int intIdTipoPre = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID);
                String strNivel = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_NIVEL);

                switch (intIdTipoPre) {
                    case TipoPregunta.DESARROLLAR:
                        Pregunta pregDesarrollar = new Pregunta(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaInstitucion.add(pregDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaInstitucion.add(pregMO);

                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaInstitucion.add(pVF);

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas pregRelCol = new PreguntaRelacionColumnas(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaInstitucion.add(pregRelCol);
                        break;

                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica pregNum = new PreguntaNumerica(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreuntaInstitucion.add(pregNum);
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }

        return colPreuntaInstitucion;

    }

    @Override
    public ArrayList<Pregunta> buscarTodasLasPreguntasPorCurso(Curso cursoSeleccionado) {
        ArrayList<Pregunta> colPreguntaCurso = new ArrayList<>();
        Connection conexion = DAOConexion.conectarBaseDatos();

        try {
            String strConsulta = "SELECT "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID + ", "
                    + "P." + EntropyDB.DIS_COL_PREGUNTA_NIVEL + " "
                    + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P, "
                    + EntropyDB.DIS_TBL_DISEÑO_EXAMEN + " DE, "
                    + EntropyDB.GRL_TBL_CURSO + " C "
                    + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_ID + " "
                    + "AND DE." + EntropyDB.DIS_COL_DISEÑO_EXAMEN_CURSO_ID + " = C." + EntropyDB.GRL_COL_CURSO_ID + " "
                    + "AND I." + EntropyDB.GRL_COL_CURSO_ID + " = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, cursoSeleccionado.getIntCursoId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_ID);
                String strEnunciado = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_ENUNCIADO);
                int intIdTipoPre = rsConsulta.getInt(EntropyDB.DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID);
                String strNivel = rsConsulta.getString(EntropyDB.DIS_COL_PREGUNTA_NIVEL);

                switch (intIdTipoPre) {
                    case TipoPregunta.DESARROLLAR:
                        Pregunta pregDesarrollar = new Pregunta(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntaCurso.add(pregDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion pregMO = new PreguntaMultipleOpcion(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntaCurso.add(pregMO);

                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso pregVF = new PreguntaVerdaderoFalso(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntaCurso.add(pregVF);

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas pregRelCol = new PreguntaRelacionColumnas(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntaCurso.add(pregRelCol);
                        break;

                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica pregNum = new PreguntaNumerica(intIdPreg, strEnunciado, intIdTipoPre, strNivel);
                        colPreguntaCurso.add(pregNum);
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }

        return colPreguntaCurso;

    }

   /*
    * Metodo para borrar una pregunta de la bd cuando se elimina un diseño
    * @param diseñoexamen, El diseño de examen a borrar de la base de datos
    */
    public void eliminarPreguntas(DiseñoExamen diseñoExamen) throws SQLException {
        
        Connection conexion = DAOConexion.conectarBaseDatos();
        String[] colConsultas = {
            "DELETE FROM " + EntropyDB.DIS_TBL_PREGUNTA_MO + " "
                + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_MO_PREGUNTA_ID + " IN ("
                + "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ?);",
            "DELETE FROM " + EntropyDB.DIS_TBL_PREGUNTA_NU + " "
                + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_NU_PREGUNTA_ID + " IN ("
                + "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ?);",
            "DELETE FROM " + EntropyDB.DIS_TBL_PREGUNTA_RC + " "
                + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_RC_PREGUNTA_ID + " IN ("
                + "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ?);",
            "DELETE FROM " + EntropyDB.DIS_TBL_PREGUNTA_VF + " "
                + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_VF_PREGUNTA_ID + " IN ("
                + "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ?);",
            "DELETE FROM " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " "
                + "WHERE " + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID + " IN ("
                + "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ?);",
            "DELETE FROM " + EntropyDB.DIS_TBL_ADJUNTO + " "
                + "WHERE " + EntropyDB.DIS_COL_ADJUNTO_PREGUNTA_ID + " IN ("
                + "SELECT P." + EntropyDB.DIS_COL_PREGUNTA_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_PREGUNTA + " P "
                + "WHERE P." + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ?);",
            "DELETE FROM " + EntropyDB.DIS_TBL_PREGUNTA + " "
                + "WHERE " + EntropyDB.DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID + " = ? ;"
        };

        for (String strConsulta : colConsultas) {
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, diseñoExamen.getIntDiseñoExamenId());
            psConsulta.execute();
        }

        conexion.prepareStatement("DELETE FROM " + EntropyDB.DIS_TBL_TAG + " "
                + "WHERE " + EntropyDB.DIS_COL_TAG_ID + " IN ("
                + "SELECT T1." + EntropyDB.DIS_COL_TAG_ID + " "
                + "FROM " + EntropyDB.DIS_TBL_TAG + " T1 "
                + "LEFT JOIN " + EntropyDB.DIS_TBL_TAG_POR_PREGUNTA + " TP "
                + "ON T1." + EntropyDB.DIS_COL_TAG_ID + " = TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + " "
                + "WHERE TP." + EntropyDB.DIS_COL_TAG_POR_PREGUNTA_TAG_ID + " IS NULL);").execute();
    }

    /*
    * Metodo para borrar una pregunta de la bd cuando se elimina un diseño(No lo use porque le cambie los parametros pero me lo pide por IDAOPregunta)
    * @param diseñoexamen, El diseño de examen a borrar de la base de datos
    * @conexion, conexion a la base de datos
    */
    @Override
    public void eliminarPreguntas(DiseñoExamen diseñoExamen, Connection conexion) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
