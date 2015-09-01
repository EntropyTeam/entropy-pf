package backend.dao.diseños;

import backend.dao.DAOConexion;
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
        String strConsulta = "INSERT INTO pregunta(disenoExamenId, orden, temaId, tipoPreguntaId, nivel, enunciado, puntaje, referencia) VALUES(?,?,?,?,?,?,?,?)";
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
            strConsulta = "INSERT INTO tagXPregunta(tagId, preguntaId) VALUES(?,?)";
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
                    strConsulta = "INSERT INTO preguntaMultipleOpcion(preguntaId,orden,opcion,correcto) VALUES(?,?,?,?)";
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
                strConsulta = "INSERT INTO preguntaVerdaderoFalso(preguntaId,verdadero,justificacion) VALUES(?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);

                psConsulta.setInt(1, intUltimoId);
                psConsulta.setBoolean(2, preguntaVF.isBlnEsVerdadera());
                psConsulta.setBoolean(3, preguntaVF.isBlnConJustificacion());

                psConsulta.execute();
                break;

            case TipoPregunta.RELACION_COLUMNAS:
                PreguntaRelacionColumnas preguntaRC = (PreguntaRelacionColumnas) pregunta;
                for (CombinacionRelacionColumnas combinacion : preguntaRC.getColCombinaciones()) {
                    strConsulta = "INSERT INTO preguntaRelacionColumnas(preguntaId,orden,columnaIzquierda,columnaDerecha) VALUES(?,?,?,?)";
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
                strConsulta = "INSERT INTO preguntaNumerica(preguntaId,esRango,numero,rangoDesde,rangoHasta,variacion) VALUES(?,?,?,?,?,?)";
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
            String strConsulta = "SELECT P.preguntaId,P.disenoExamenId,P.orden,P.temaId,P.tipoPreguntaId,P.nivel,P.enunciado,P.puntaje,P.referencia FROM tagXPregunta TP INNER JOIN pregunta P ON P.preguntaId = TP.preguntaId WHERE TP.tagId LIKE (?)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setString(1, strCadena);
            ResultSet rsConsulta = psConsulta.executeQuery();
            pregunta = new ArrayList<>();
            int intOrden = 0;
            while (rsConsulta.next()) {
                ArrayList adjuntos = null;
                ArrayList<String> tags = null;
                int intIdPreg = rsConsulta.getInt(1);
                intOrden = intOrden + 1;
                int intIdTema = rsConsulta.getInt(4);//No me debaria interesar porque el tema depende del examen.
                int intIdTipoPre = rsConsulta.getInt(5);
                String strNivel = rsConsulta.getString(6);
                String strEnunciado = rsConsulta.getString(7);
                double dblPuntaje = rsConsulta.getDouble(8);
                String strReferencia = rsConsulta.getString(9);

                try {
                    String strConsultaAdjunto = "SELECT * FROM Adjunto WHERE preguntaId = ?";
                    PreparedStatement psConsultaAdjunto = conexion.prepareStatement(strConsultaAdjunto);
                    psConsultaAdjunto.setInt(1, intIdPreg);
                    ResultSet rsConsultaAdjunto = psConsultaAdjunto.executeQuery();
                    adjuntos = new ArrayList();
                    while (rsConsultaAdjunto.next()) {
                        adjuntos.add(rsConsultaAdjunto.getObject(1));
                    }

                } catch (SQLException e) {
                    System.out.println(e);
                }
                try {
                    String strConsultaTag = "SELECT TP.tagId FROM tagXPregunta TP WHERE TP.preguntaId = ?";
                    PreparedStatement psConsultaTag = conexion.prepareStatement(strConsultaTag);
                    psConsultaTag.setInt(1, intIdPreg);
                    ResultSet rsConsultaTag = psConsultaTag.executeQuery();
                    tags = new ArrayList();
                    while (rsConsultaTag.next()) {
                        tags.add(rsConsultaTag.getString(1));
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
                            String strConsultaMO = "SELECT PMO.preguntaId, PMO.orden, PMO.opcion, PMO.correcto FROM preguntaMultipleOpcion PMO, pregunta P WHERE PMO.preguntaId = ?";
                            PreparedStatement psConsultaMO = conexion.prepareStatement(strConsultaMO);
                            psConsultaMO.setInt(1, intIdPreg);
                            ResultSet rsConsultaMO = psConsultaMO.executeQuery();

                            while (rsConsultaMO.next()) {

                                int intIdPr = rsConsultaMO.getInt(1);
                                int intOr = rsConsultaMO.getInt(2);
                                String stOpcion = rsConsultaMO.getString(3);
                                boolean blCorrecto = rsConsultaMO.getBoolean(4);
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
                            String strConsultaPVF = "SELECT PVF.preguntaId, PVF.verdadero, PVF.justificacion FROM preguntaVerdaderoFalso PVF, pregunta P WHERE PVF.preguntaId = ?";
                            PreparedStatement psConsultaPVF = conexion.prepareStatement(strConsultaPVF);
                            psConsultaPVF.setInt(1, intIdPreg);
                            ResultSet rsConsultaPVF = psConsultaPVF.executeQuery();

                            int intPVFId = rsConsultaPVF.getInt(1);
                            boolean blVerdadera = rsConsultaPVF.getBoolean(2);
                            boolean blJustificacion = rsConsultaPVF.getBoolean(3);

                            PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(blVerdadera, blJustificacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                            pregunta.add(pVF);
                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        try {
                            String strConsultaPRC = "SELECT PRC.preguntaId, PRC.orden, PRC.columnaIzquierda, PRC.columnaDerecha FROM preguntaRelacionColumnas PRC WHERE PRC.preguntaId = ?";
                            PreparedStatement psConsultaPRC = conexion.prepareStatement(strConsultaPRC);
                            psConsultaPRC.setInt(1, intIdPreg);
                            ResultSet rsConsultaPRC = psConsultaPRC.executeQuery();

                            int intPRCId = rsConsultaPRC.getInt(1);
                            ArrayList<CombinacionRelacionColumnas> colCombinacion = new ArrayList<>();

                            while (rsConsultaPRC.next()) {

                                String strColIzq = rsConsultaPRC.getString(3);
                                String strColDer = rsConsultaPRC.getString(4);
                                int intOrdenPRC = rsConsultaPRC.getInt(2);
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
                            String strConsultaPN = "SELECT PN.preguntaId, PN.esRango, PN.numero, PN.rangoDesde, PN.rangoHasta, PN.variacion FROM preguntaNumerica PN WHERE PN.preguntaId = ?";
                            PreparedStatement psConsultaPN = conexion.prepareStatement(strConsultaPN);
                            psConsultaPN.setInt(1, intIdPreg);
                            ResultSet rsConsultaPN = psConsultaPN.executeQuery();

                            int intPNId = rsConsultaPN.getInt(1);
                            boolean blRango = rsConsultaPN.getBoolean(2);
                            double dblNumero = rsConsultaPN.getDouble(3);
                            double dblRangoDesde = rsConsultaPN.getDouble(4);
                            double dblRangoHasta = rsConsultaPN.getDouble(5);
                            double dblVariacion = rsConsultaPN.getDouble(6);
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
            String strConsulta = "SELECT DISTINCT P.preguntaId, P.enunciado, P.nivel, P.tipoPreguntaId "
                    + "FROM disenoExamen DE LEFT JOIN curso C ON DE.cursoId = C.cursoId "
                    + "                     LEFT JOIN pregunta P ON P.disenoExamenId = DE.disenoExamenId "
                    + "                     LEFT JOIN institucion I ON C.institucionId = I.institucionId "
                    + "                     LEFT JOIN tagXPregunta TP ON P.preguntaId = TP.preguntaId ";
            String strWhere = "";
            for (Filtro filtro : colFiltro) {
                switch (filtro.getFiltro()) {
                    case INSTITUCION:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " I.institucionId = ?";
                        break;
                    case CON_CURSO:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " DE.cursoId IS NOT NULL";
                        break;
                    case CURSO:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " C.cursoId = ?";
                        break;
                    case SIN_CURSO:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " DE.cursoId IS NULL";
                        break;
                    case DISEÑOEXAMEN:
                        strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " DE.disenoExamenId = ?";
                        break;
                    case TAG:
                        if (firstloop) {
                            strWhere += ((strWhere.isEmpty()) ? "" : " AND") + " TP.tagId LIKE ?";
                            firstloop = false;
                        } else {
                            strWhere += ((strWhere.isEmpty()) ? "" : " OR") + " TP.tagId LIKE ?";
                        }
                        break;
                }
            }
            strConsulta += ((strWhere.isEmpty()) ? "" : " WHERE" + strWhere) + " GROUP BY P.preguntaId";
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
                String strConsulta = "SELECT P.preguntaId,P.disenoExamenId,P.orden,P.temaId,P.tipoPreguntaId,P.nivel,P.enunciado,P.puntaje,P.referencia,T.nombre FROM pregunta P LEFT JOIN tema T ON P.temaId = T.temaId WHERE P.preguntaId = ?";
                PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
                psConsulta.setInt(1, intIDPregunta);
                ResultSet rsConsulta = psConsulta.executeQuery();
                
                int intOrden = 0;
                while (rsConsulta.next()) {
                    ArrayList adjuntos = null;
                    ArrayList<String> tags = null;
                    int intIdPreg = rsConsulta.getInt(1);
                    intOrden = intOrden + 1;
                    
                    int intIdTema = rsConsulta.getInt(4);
                    Tema tema = null;
                    if(intIdTema != 0) {
                        tema = new Tema();
                        tema.setIntTemaId(intIdTema);
                        tema.setStrNombre(rsConsulta.getString(10));
                    }
                    
                    int intIdTipoPre = rsConsulta.getInt(5);
                    String strNivel = rsConsulta.getString(6);
                    String strEnunciado = rsConsulta.getString(7);
                    double dblPuntaje = rsConsulta.getDouble(8);
                    String strReferencia = rsConsulta.getString(9);

                    try {
                        String strConsultaAdjunto = "SELECT * FROM Adjunto WHERE preguntaId = ?";
                        PreparedStatement psConsultaAdjunto = conexion.prepareStatement(strConsultaAdjunto);
                        psConsultaAdjunto.setInt(1, intIdPreg);
                        ResultSet rsConsultaAdjunto = psConsultaAdjunto.executeQuery();
                        adjuntos = new ArrayList();
                        while (rsConsultaAdjunto.next()) {
                            adjuntos.add(rsConsultaAdjunto.getObject(1));
                        }

                    } catch (SQLException e) {
                        System.err.println(e);
                    }
                    try {
                        String strConsultaTag = "SELECT TP.tagId FROM tagXPregunta TP WHERE TP.preguntaId = ?";
                        PreparedStatement psConsultaTag = conexion.prepareStatement(strConsultaTag);
                        psConsultaTag.setInt(1, intIdPreg);
                        ResultSet rsConsultaTag = psConsultaTag.executeQuery();
                        tags = new ArrayList();
                        while (rsConsultaTag.next()) {
                            tags.add(rsConsultaTag.getString(1));
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
                                String strConsultaMO = "SELECT PMO.preguntaId, PMO.orden, PMO.opcion, PMO.correcto FROM preguntaMultipleOpcion PMO WHERE PMO.preguntaId = ? GROUP BY PMO.preguntaId, PMO.orden, PMO.opcion, PMO.correcto";
                                PreparedStatement psConsultaMO = conexion.prepareStatement(strConsultaMO);
                                psConsultaMO.setInt(1, intIdPreg);
                                ResultSet rsConsultaMO = psConsultaMO.executeQuery();

                                while (rsConsultaMO.next()) {

                                    int intIdPr = rsConsultaMO.getInt(1);
                                    int intOr = rsConsultaMO.getInt(2);
                                    String stOpcion = rsConsultaMO.getString(3);
                                    boolean blCorrecto = rsConsultaMO.getBoolean(4);
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
                                String strConsultaPVF = "SELECT PVF.preguntaId, PVF.verdadero, PVF.justificacion FROM preguntaVerdaderoFalso PVF WHERE PVF.preguntaId = ?";
                                PreparedStatement psConsultaPVF = conexion.prepareStatement(strConsultaPVF);
                                psConsultaPVF.setInt(1, intIdPreg);
                                ResultSet rsConsultaPVF = psConsultaPVF.executeQuery();

                                int intPVFId = rsConsultaPVF.getInt(1);
                                boolean blVerdadera = rsConsultaPVF.getBoolean(2);
                                boolean blJustificacion = rsConsultaPVF.getBoolean(3);

                                PreguntaVerdaderoFalso pVF = new PreguntaVerdaderoFalso(blVerdadera, blJustificacion, intIdPreg, intOrden, strEnunciado, intIdTipoPre, strNivel, dblPuntaje, strReferencia, adjuntos, tags, null);
                                pVF.setTema(tema);
                                colPregunta.add(pVF);
                            } catch (SQLException e) {
                                System.err.println(e);
                            }
                            break;

                        case TipoPregunta.RELACION_COLUMNAS:
                            try {
                                String strConsultaPRC = "SELECT PRC.preguntaId, PRC.orden, PRC.columnaIzquierda, PRC.columnaDerecha FROM preguntaRelacionColumnas PRC WHERE PRC.preguntaId = ?";
                                PreparedStatement psConsultaPRC = conexion.prepareStatement(strConsultaPRC);
                                psConsultaPRC.setInt(1, intIdPreg);
                                ResultSet rsConsultaPRC = psConsultaPRC.executeQuery();

                                int intPRCId = rsConsultaPRC.getInt(1);
                                ArrayList<CombinacionRelacionColumnas> colCombinacion = new ArrayList<>();

                                while (rsConsultaPRC.next()) {

                                    String strColIzq = rsConsultaPRC.getString(3);
                                    String strColDer = rsConsultaPRC.getString(4);
                                    int intOrdenPRC = rsConsultaPRC.getInt(2);
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
                                String strConsultaPN = "SELECT PN.preguntaId, PN.esRango, PN.numero, PN.rangoDesde, PN.rangoHasta, PN.variacion FROM preguntaNumerica PN WHERE PN.preguntaId = ?";
                                PreparedStatement psConsultaPN = conexion.prepareStatement(strConsultaPN);
                                psConsultaPN.setInt(1, intIdPreg);
                                ResultSet rsConsultaPN = psConsultaPN.executeQuery();

                                int intPNId = rsConsultaPN.getInt(1);
                                boolean blRango = rsConsultaPN.getBoolean(2);
                                double dblNumero = rsConsultaPN.getDouble(3);
                                double dblRangoDesde = rsConsultaPN.getDouble(4);
                                double dblRangoHasta = rsConsultaPN.getDouble(5);
                                double dblVariacion = rsConsultaPN.getDouble(6);
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
            String strConsulta = "SELECT P.preguntaId, P.enunciado, P.tipoPreguntaId, P.nivel, P.puntaje FROM pregunta P, disenoExamen DE WHERE P.disenoExamenId = DE.disenoExamenId AND DE.disenoExamenId = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, diseñoExamen.getIntDiseñoExamenId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(1);
                String strEnunciado = rsConsulta.getString(2);
                int intIdTipoPre = rsConsulta.getInt(3);
                String strNivel = rsConsulta.getString(4);

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
            String strConsulta = "SELECT P.preguntaId, P.enunciado, P.tipoPreguntaId, P.nivel FROM pregunta P";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(1);
                String strEnunciado = rsConsulta.getString(2);
                int intIdTipoPre = rsConsulta.getInt(3);
                String strNivel = rsConsulta.getString(4);

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
            String strConsulta = "SELECT P.preguntaId, P.enunciado, P.tipoPreguntaId, P.nivel FROM pregunta P, disenoExamen DE, institucion I, curso C WHERE P.disenoExamenId = DE.disenoExamenId AND DE.cursoId = C.cursoId AND C.institucionId = I.institucionId AND I.institucionId = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, institucionSeleccionada.getIntInstitucionId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(1);
                String strEnunciado = rsConsulta.getString(2);
                int intIdTipoPre = rsConsulta.getInt(3);
                String strNivel = rsConsulta.getString(4);

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
            String strConsulta = "SELECT P.preguntaId, P.enunciado, P.tipoPreguntaId, P.nivel FROM pregunta P, disenoExamen DE, curso C WHERE P.disenoExamenId = DE.disenoExamenId AND DE.cursoId = C.cursoId AND C.cursoId = ?";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, cursoSeleccionado.getIntCursoId());
            ResultSet rsConsulta = psConsulta.executeQuery();

            while (rsConsulta.next()) {
                int intIdPreg = rsConsulta.getInt(1);
                String strEnunciado = rsConsulta.getString(2);
                int intIdTipoPre = rsConsulta.getInt(3);
                String strNivel = rsConsulta.getString(4);

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
            "DELETE FROM preguntaMultipleOpcion WHERE preguntaId IN (SELECT P.preguntaId FROM Pregunta P WHERE P.disenoExamenId = ? );",
            "DELETE FROM preguntaNumerica WHERE preguntaId IN (SELECT P.preguntaId FROM Pregunta P WHERE P.disenoExamenId = ? );",
            "DELETE FROM preguntaRelacionColumnas WHERE preguntaId IN (SELECT P.preguntaId FROM Pregunta P WHERE P.disenoExamenId = ? );",
            "DELETE FROM preguntaVerdaderoFalso WHERE preguntaId IN (SELECT P.preguntaId FROM Pregunta P WHERE P.disenoExamenId = ? );",
            "DELETE FROM tagXPregunta WHERE preguntaId IN (SELECT P.preguntaId FROM Pregunta P WHERE P.disenoExamenId = ? );",
            "DELETE FROM adjunto WHERE preguntaId IN (SELECT P.preguntaId FROM Pregunta P WHERE P.disenoExamenId = ? );",
            "DELETE FROM pregunta WHERE disenoExamenId = ? ;"
        };

        for (String strConsulta : colConsultas) {
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, diseñoExamen.getIntDiseñoExamenId());
            psConsulta.execute();
        }

        conexion.prepareStatement("DELETE FROM tag WHERE tagId IN (SELECT T1.tagId FROM tag T1 LEFT JOIN tagXPregunta TP ON T1.tagId = TP.tagId WHERE TP.tagId IS NULL );").execute();
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
