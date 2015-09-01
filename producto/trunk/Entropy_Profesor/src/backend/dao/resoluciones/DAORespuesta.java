package backend.dao.resoluciones;

import backend.dao.DAOConexion;
import backend.dao.EntropyDB;
import backend.dao.diseños.DAODiseñoExamen;
import backend.diseños.CombinacionRelacionColumnas;
import backend.diseños.OpcionMultipleOpcion;
import backend.diseños.Pregunta;
import backend.diseños.PreguntaMultipleOpcion;
import backend.diseños.PreguntaNumerica;
import backend.diseños.PreguntaRelacionColumnas;
import backend.diseños.PreguntaVerdaderoFalso;
import backend.diseños.Tema;
import backend.diseños.TipoPregunta;
import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import backend.resoluciones.RespuestaCombinacionRelacionColumnas;
import backend.resoluciones.RespuestaDesarrollo;
import backend.resoluciones.RespuestaOpcionMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaNumerica;
import backend.resoluciones.RespuestaPreguntaRelacionColumnas;
import backend.resoluciones.RespuestaPreguntaVerdaderoFalso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Denise
 */
public class DAORespuesta implements IDAORespuesta {

    @Override
    public boolean guardarRespuesta(Respuesta respuesta, int idResolucion, Connection conexion) throws SQLException {

        String strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESPUESTA + " ("
                + EntropyDB.RES_COL_RESPUESTA_RESOLUCION_ID + ", "
                + EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID + ", "
                + EntropyDB.RES_COL_RESPUESTA_COMENTARIO + ") "
                + "VALUES(?,?,?)";

        PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);

        psConsulta.setInt(1, idResolucion);
        psConsulta.setInt(2, respuesta.getPregunta().getIntPreguntaId());
        psConsulta.setString(3, respuesta.getStrComentario());

        psConsulta.execute();

        // Obtener el ID del la respuestas
        strConsulta = "SELECT last_insert_rowid();";
        psConsulta = conexion.prepareStatement(strConsulta);

        ResultSet rsConsulta = psConsulta.executeQuery();
        int intIDRespuesta = rsConsulta.getInt(1);
        respuesta.setIntRespuestaId(intIDRespuesta);

        // Guardar de acuerdo al Tipo de respuesta
        switch (respuesta.getPregunta().getIntTipo()) {
            case TipoPregunta.MULTIPLE_OPCION:
                RespuestaPreguntaMultipleOpcion respuestaMO = (RespuestaPreguntaMultipleOpcion) respuesta;
                for (RespuestaOpcionMultipleOpcion opcion : respuestaMO.getColeccionOpciones()) {

                    strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESPUESTA_MO + "("
                            + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA_ID + ", "
                            + EntropyDB.RES_COL_RESPUESTA_MO_ORDEN + ", "
                            + EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA + ", "
                            + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA + ") "
                            + "VALUES(?,?,?,?)";

                    psConsulta = conexion.prepareStatement(strConsulta);

                    psConsulta.setInt(1, intIDRespuesta);
                    psConsulta.setInt(2, opcion.getIntOrden());
                    psConsulta.setBoolean(3, opcion.isBlnEsMarcada());
                    psConsulta.setString(4, opcion.getStrRespuesta());

                    psConsulta.execute();
                }
                break;

            case TipoPregunta.VERDADERO_FALSO:

                RespuestaPreguntaVerdaderoFalso respuestaVF = (RespuestaPreguntaVerdaderoFalso) respuesta;

                strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESPUESTA_VF + "("
                        + EntropyDB.RES_COL_RESPUESTA_VF_RESPUESTA_ID + ", "
                        + EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_VERDADERO + ", "
                        + EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_FALSO + ", "
                        + EntropyDB.RES_COL_RESPUESTA_VF_JUSTIFICACION + ", "
                        + EntropyDB.RES_COL_RESPUESTA_VF_CALIFICACION + ") "
                        + "VALUES(?,?,?,?,?)";

                psConsulta = conexion.prepareStatement(strConsulta);

                psConsulta.setInt(1, intIDRespuesta);
                psConsulta.setBoolean(2, respuestaVF.isBlnSeleccionoVerdadero());
                psConsulta.setBoolean(3, respuestaVF.isBlnSeleccionoFalso());
                psConsulta.setString(4, respuestaVF.getStrJustificacion());
                psConsulta.setDouble(5, respuestaVF.getCalificacion());
                psConsulta.execute();
                break;

            case TipoPregunta.RELACION_COLUMNAS:
                RespuestaPreguntaRelacionColumnas respuestaRC = (RespuestaPreguntaRelacionColumnas) respuesta;
                for (RespuestaCombinacionRelacionColumnas combinacion : respuestaRC.getColCombinaciones()) {
                    strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESPUESTA_RC + "("
                            + EntropyDB.RES_COL_RESPUESTA_RC_RESPUESTA_ID + ", "
                            + EntropyDB.RES_COL_RESPUESTA_RC_ORDEN + ", "
                            + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA + ", "
                            + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA + ") "
                            + "VALUES(?,?,?,?)";
                    psConsulta = conexion.prepareStatement(strConsulta);

                    psConsulta.setInt(1, intIDRespuesta);
                    psConsulta.setInt(2, combinacion.getIntOrden());
                    psConsulta.setString(3, combinacion.getStrColumnaIzquierda());
                    psConsulta.setString(4, combinacion.getStrColumnaDerecha());

                    psConsulta.execute();
                }
                break;

            case TipoPregunta.NUMERICA:
                RespuestaPreguntaNumerica respuestaNU = (RespuestaPreguntaNumerica) respuesta;
                strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESPUESTA_NU + "("
                        + EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_ID + ", "
                        + EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_NUMERO + ", "
                        + EntropyDB.RES_COL_RESPUESTA_NU_RANGO_DESDE + ", "
                        + EntropyDB.RES_COL_RESPUESTA_NU_RANGO_HASTA + ") "
                        + "VALUES(?,?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);

                psConsulta.setInt(1, intIDRespuesta);
                if (respuestaNU.getPregunta().esRango()) {
                    psConsulta.setDouble(2, 0);
                    psConsulta.setDouble(3, respuestaNU.getDblRangoDesde());
                    psConsulta.setDouble(4, respuestaNU.getDblRangoHasta());
                } else {
                    psConsulta.setDouble(2, respuestaNU.getDblRespuestaNumero());
                    psConsulta.setDouble(3, 0);
                    psConsulta.setDouble(4, 0);
                }

                psConsulta.execute();

                break;

            case TipoPregunta.DESARROLLAR:
                RespuestaDesarrollo respuestaDE = (RespuestaDesarrollo) respuesta;
                strConsulta = "INSERT INTO " + EntropyDB.RES_TBL_RESPUESTA_DE + "("
                        + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA_ID + ", "
                        + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA + ", "
                        + EntropyDB.RES_COL_RESPUESTA_DE_CALIFICACION + ") "
                        + "VALUES(?,?,?)";
                psConsulta = conexion.prepareStatement(strConsulta);

                psConsulta.setInt(1, intIDRespuesta);
                psConsulta.setString(2, respuestaDE.getStrRespuesta());
                psConsulta.setDouble(3, respuestaDE.getCalificacion());
                
                psConsulta.execute();

                break;
        }

        return true;
    }

    @Override
    public ArrayList<Respuesta> getRespuestas(Resolucion resolucion) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        ArrayList<Respuesta> colRespuestas = new ArrayList<>();

        try {

            String strConsulta = "SELECT "
                    + " R." + EntropyDB.RES_COL_RESPUESTA_ID + ", "
                    + " R." + EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID + ", "
                    + " R." + EntropyDB.RES_COL_RESPUESTA_COMENTARIO + ", "
                    + " P." + EntropyDB.EXA_COL_PREGUNTA_TIPO_PREGUNTA_ID + " "
                    + " FROM " + EntropyDB.RES_TBL_RESPUESTA + " R "
                    + " INNER JOIN " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                    + " ON R." + EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID + " = P." + EntropyDB.EXA_COL_PREGUNTA_ID
                    + " WHERE R." + EntropyDB.RES_COL_RESOLUCION_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, resolucion.getIntID());
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                Respuesta rta = null;
                int intIDRespuesta = rs.getInt(EntropyDB.RES_COL_RESPUESTA_ID);
                int intIDPregunta = rs.getInt(EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID);
                String strComentario = rs.getString(EntropyDB.RES_COL_RESPUESTA_COMENTARIO);
                int intTipoPregunta = rs.getInt(EntropyDB.EXA_COL_PREGUNTA_TIPO_PREGUNTA_ID);

                switch (intTipoPregunta) {
                    case TipoPregunta.DESARROLLAR:
                        RespuestaDesarrollo rtaDE = new RespuestaDesarrollo();
                        rtaDE.setIntRespuestaId(intIDRespuesta);
                        rtaDE.setStrComentario(strComentario);

                        Pregunta preguntaDE = new Pregunta();
                        preguntaDE.setIntPreguntaId(intIDPregunta);
                        preguntaDE.setIntTipo(intTipoPregunta);

                        String strPreguntaDE = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaDE = conexion.prepareStatement(strPreguntaDE);
                        psPreguntaDE.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaDE = psPreguntaDE.executeQuery();
                        while (rsPreguntaDE.next()) {

                            preguntaDE.setIntOrden(rsPreguntaDE.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaDE.setTema(new Tema(rsPreguntaDE.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaDE.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaDE.setStrNivel(rsPreguntaDE.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaDE.setDblPuntaje(rsPreguntaDE.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaDE.setStrEnunciado(rsPreguntaDE.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaDE.setStrReferencia(rsPreguntaDE.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));
                        }

                        rtaDE.setPregunta(preguntaDE);

                        String strRespuestaDE = "SELECT " + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA
                                + " , "+ EntropyDB.RES_COL_RESPUESTA_DE_CALIFICACION
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_DE
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA_ID + " = ? ";

                        PreparedStatement psRespuestaDE = conexion.prepareStatement(strRespuestaDE);
                        psRespuestaDE.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaDE = psRespuestaDE.executeQuery();
                        while (rsRespuestaDE.next()) {
                            rtaDE.setStrRespuesta(rsRespuestaDE.getString(EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA));
                            rtaDE.setCalificacion(rsRespuestaDE.getDouble(EntropyDB.RES_COL_RESPUESTA_DE_CALIFICACION));
                        }

                        rta = rtaDE;

                        psPreguntaDE.close();
                        rsPreguntaDE.close();
                        psRespuestaDE.close();
                        rsRespuestaDE.close();

                        break;

                    case TipoPregunta.MULTIPLE_OPCION:

                        RespuestaPreguntaMultipleOpcion rtaMO = new RespuestaPreguntaMultipleOpcion();
                        rtaMO.setIntRespuestaId(intIDRespuesta);
                        rtaMO.setStrComentario(strComentario);

                        PreguntaMultipleOpcion preguntaMO = new PreguntaMultipleOpcion();
                        preguntaMO.setIntPreguntaId(intIDPregunta);
                        preguntaMO.setIntTipo(intTipoPregunta);

                        String strPreguntaMO = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaMO = conexion.prepareStatement(strPreguntaMO);
                        psPreguntaMO.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaMO = psPreguntaMO.executeQuery();
                        while (rsPreguntaMO.next()) {
                            preguntaMO.setIntOrden(rsPreguntaMO.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaMO.setTema(new Tema(rsPreguntaMO.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaMO.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaMO.setStrNivel(rsPreguntaMO.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaMO.setDblPuntaje(rsPreguntaMO.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaMO.setStrEnunciado(rsPreguntaMO.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaMO.setStrReferencia(rsPreguntaMO.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));
                        }

                        String strPreguntaMOOpciones = "SELECT "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_CORRECTO + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_OPCION + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_ORDEN + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA_MO
                                + " WHERE " + EntropyDB.EXA_COL_PREGUNTA_MO_PREGUNTA_ID + " = ? "
                                + " GROUP BY "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_PREGUNTA_ID + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_CORRECTO + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_OPCION + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_ORDEN;

                        PreparedStatement psPreguntaMOOpciones = conexion.prepareStatement(strPreguntaMOOpciones);
                        psPreguntaMOOpciones.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaMOOpciones = psPreguntaMOOpciones.executeQuery();
                        ArrayList<OpcionMultipleOpcion> lstOpcionesPregunta = new ArrayList<>();
                        while (rsPreguntaMOOpciones.next()) {
                            OpcionMultipleOpcion opcionPregunta = new OpcionMultipleOpcion();
                            opcionPregunta.setBlnEsVerdadera(rsPreguntaMOOpciones.getBoolean(EntropyDB.EXA_COL_PREGUNTA_MO_CORRECTO));
                            opcionPregunta.setStrRespuesta(rsPreguntaMOOpciones.getString(EntropyDB.EXA_COL_PREGUNTA_MO_OPCION));
                            opcionPregunta.setIntOrden(rsPreguntaMOOpciones.getInt(EntropyDB.EXA_COL_PREGUNTA_MO_ORDEN));
                            lstOpcionesPregunta.add(opcionPregunta);
                        }
                        preguntaMO.setColOpciones(lstOpcionesPregunta);

                        rtaMO.setPreguntaMultipleOpcion(preguntaMO);

                        String strRespuestaMO = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_MO
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA_ID + " = ?"
                                + " GROUP BY " + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA_ID + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA;

                        PreparedStatement psRespuestaMO = conexion.prepareStatement(strRespuestaMO);
                        psRespuestaMO.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaMO = psRespuestaMO.executeQuery();
                        ArrayList<RespuestaOpcionMultipleOpcion> lstOpcionesRespuesta = new ArrayList<>();
                        while (rsRespuestaMO.next()) {
                            int intOrden = rsRespuestaMO.getInt(EntropyDB.RES_COL_RESPUESTA_MO_ORDEN);
                            boolean blnEsMarcada = rsRespuestaMO.getBoolean(EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA);
                            String strRespuesta = rsRespuestaMO.getString(EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA);
                            RespuestaOpcionMultipleOpcion rtaOpcion = new RespuestaOpcionMultipleOpcion(intOrden, strRespuesta, blnEsMarcada);
                            lstOpcionesRespuesta.add(rtaOpcion);
                        }
                        rtaMO.setColeccionOpciones(lstOpcionesRespuesta);

                        rta = rtaMO;

                        psPreguntaMOOpciones.close();
                        rsPreguntaMOOpciones.close();
                        psPreguntaMO.close();
                        rsPreguntaMO.close();
                        psRespuestaMO.close();
                        rsRespuestaMO.close();

                        break;

                    case TipoPregunta.NUMERICA:

                        RespuestaPreguntaNumerica rtaNU = new RespuestaPreguntaNumerica();
                        rtaNU.setIntRespuestaId(intIDRespuesta);
                        rtaNU.setStrComentario(strComentario);

                        PreguntaNumerica preguntaNU = new PreguntaNumerica();
                        preguntaNU.setIntPreguntaId(intIDPregunta);
                        preguntaNU.setIntTipo(intTipoPregunta);

                        String strPreguntaNU = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_ES_RANGO + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_NUMERO + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_DESDE + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_HASTA + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_VARIACION + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " INNER JOIN " + EntropyDB.EXA_TBL_PREGUNTA_NU + " NU "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = NU." + EntropyDB.EXA_COL_PREGUNTA_NU_PREGUNTA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaNU = conexion.prepareStatement(strPreguntaNU);
                        psPreguntaNU.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaNU = psPreguntaNU.executeQuery();
                        while (rsPreguntaNU.next()) {

                            preguntaNU.setIntOrden(rsPreguntaNU.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaNU.setTema(new Tema(rsPreguntaNU.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaNU.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaNU.setStrNivel(rsPreguntaNU.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaNU.setDblPuntaje(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaNU.setStrEnunciado(rsPreguntaNU.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaNU.setStrReferencia(rsPreguntaNU.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));

                            boolean blnEsRango = rsPreguntaNU.getBoolean(EntropyDB.EXA_COL_PREGUNTA_NU_ES_RANGO);
                            preguntaNU.setBlnEsRango(blnEsRango);
                            if (blnEsRango) {
                                preguntaNU.setDblNumero(0);
                                preguntaNU.setDblRangoDesde(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_DESDE));
                                preguntaNU.setDblRangoHasta(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_HASTA));
                            } else {
                                preguntaNU.setDblNumero(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_NUMERO));
                                preguntaNU.setDblRangoDesde(0);
                                preguntaNU.setDblRangoHasta(0);
                            }
                            preguntaNU.setDblVariacion(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_VARIACION));
                        }

                        rtaNU.setPreguntaNumerica(preguntaNU);

                        String strRespuestaNU = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_NUMERO + ", "
                                + EntropyDB.RES_COL_RESPUESTA_NU_RANGO_DESDE + ", "
                                + EntropyDB.RES_COL_RESPUESTA_NU_RANGO_HASTA + " "
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_NU
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_ID + " = ? ";

                        PreparedStatement psRespuestaNU = conexion.prepareStatement(strRespuestaNU);
                        psRespuestaNU.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaNU = psRespuestaNU.executeQuery();

                        while (rsRespuestaNU.next()) {
                            rtaNU.setDblRespuestaNumero(rsRespuestaNU.getDouble(EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_NUMERO));
                            rtaNU.setDblRangoDesde(rsRespuestaNU.getDouble(EntropyDB.RES_COL_RESPUESTA_NU_RANGO_DESDE));
                            rtaNU.setDblRangoHasta(rsRespuestaNU.getDouble(EntropyDB.RES_COL_RESPUESTA_NU_RANGO_HASTA));
                        }

                        rta = rtaNU;

                        psPreguntaNU.close();
                        rsPreguntaNU.close();
                        psRespuestaNU.close();
                        rsRespuestaNU.close();

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:

                        RespuestaPreguntaRelacionColumnas rtaRC = new RespuestaPreguntaRelacionColumnas();
                        rtaRC.setIntRespuestaId(intIDRespuesta);
                        rtaRC.setStrComentario(strComentario);

                        PreguntaRelacionColumnas preguntaRC = new PreguntaRelacionColumnas();
                        preguntaRC.setIntPreguntaId(intIDPregunta);
                        preguntaRC.setIntTipo(intTipoPregunta);

                        String strPreguntaRC = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaRC = conexion.prepareStatement(strPreguntaRC);
                        psPreguntaRC.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaRC = psPreguntaRC.executeQuery();
                        while (rsPreguntaRC.next()) {
                            preguntaRC.setIntOrden(rsPreguntaRC.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaRC.setTema(new Tema(rsPreguntaRC.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaRC.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaRC.setStrNivel(rsPreguntaRC.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaRC.setDblPuntaje(rsPreguntaRC.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaRC.setStrEnunciado(rsPreguntaRC.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaRC.setStrReferencia(rsPreguntaRC.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));
                        }

                        String strPreguntaRCCombinaciones = "SELECT "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_ORDEN + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA_RC
                                + " WHERE " + EntropyDB.EXA_COL_PREGUNTA_RC_PREGUNTA_ID + " = ? "
                                + " GROUP BY "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_PREGUNTA_ID + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_ORDEN + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA;

                        PreparedStatement psPreguntaRCCombinaciones = conexion.prepareStatement(strPreguntaRCCombinaciones);
                        psPreguntaRCCombinaciones.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaRCCombinaciones = psPreguntaRCCombinaciones.executeQuery();
                        ArrayList<CombinacionRelacionColumnas> lstCombinacionesPregunta = new ArrayList<>();
                        while (rsPreguntaRCCombinaciones.next()) {
                            CombinacionRelacionColumnas combinacionPregunta = new CombinacionRelacionColumnas();
                            combinacionPregunta.setIntOrden(rsPreguntaRCCombinaciones.getInt(EntropyDB.EXA_COL_PREGUNTA_RC_ORDEN));
                            combinacionPregunta.setStrColumnaIzquierda(rsPreguntaRCCombinaciones.getString(EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA));
                            combinacionPregunta.setStrColumnaDerecha(rsPreguntaRCCombinaciones.getString(EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA));
                            lstCombinacionesPregunta.add(combinacionPregunta);
                        }
                        preguntaRC.setColCombinaciones(lstCombinacionesPregunta);

                        rtaRC.setPreguntaRelacionColumnas(preguntaRC);

                        String strRespuestaRC = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_RC_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_RC
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_RC_RESPUESTA_ID + " = ?"
                                + " GROUP BY " + EntropyDB.RES_COL_RESPUESTA_RC_RESPUESTA_ID + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA;

                        PreparedStatement psRespuestaRC = conexion.prepareStatement(strRespuestaRC);
                        psRespuestaRC.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaRC = psRespuestaRC.executeQuery();
                        ArrayList<RespuestaCombinacionRelacionColumnas> lstCombinacionesRespuesta = new ArrayList<>();
                        while (rsRespuestaRC.next()) {
                            int intOrden = rsRespuestaRC.getInt(EntropyDB.RES_COL_RESPUESTA_RC_ORDEN);
                            String strColIzq = rsRespuestaRC.getString(EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA);
                            String strColDer = rsRespuestaRC.getString(EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA);
                            RespuestaCombinacionRelacionColumnas rtaCombinacion = new RespuestaCombinacionRelacionColumnas(intOrden, strColIzq, strColDer);
                            lstCombinacionesRespuesta.add(rtaCombinacion);
                        }

                        rtaRC.setColCombinaciones(lstCombinacionesRespuesta);

                        rta = rtaRC;

                        psPreguntaRCCombinaciones.close();
                        rsPreguntaRCCombinaciones.close();
                        psPreguntaRC.close();
                        rsPreguntaRC.close();
                        psRespuestaRC.close();
                        rsRespuestaRC.close();

                        break;

                    case TipoPregunta.VERDADERO_FALSO:

                        RespuestaPreguntaVerdaderoFalso rtaVF = new RespuestaPreguntaVerdaderoFalso();
                        rtaVF.setIntRespuestaId(intIDRespuesta);
                        rtaVF.setStrComentario(strComentario);

                        PreguntaVerdaderoFalso preguntaVF = new PreguntaVerdaderoFalso();
                        preguntaVF.setIntPreguntaId(intIDPregunta);
                        preguntaVF.setIntTipo(intTipoPregunta);

                        String strPreguntaVF = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + ", "
                                + " VF." + EntropyDB.EXA_COL_PREGUNTA_VF_JUSTIFICACION + ", "
                                + " VF." + EntropyDB.EXA_COL_PREGUNTA_VF_VERDADERO + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " INNER JOIN " + EntropyDB.EXA_TBL_PREGUNTA_VF + " VF "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = VF." + EntropyDB.EXA_COL_PREGUNTA_VF_PREGUNTA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaVF = conexion.prepareStatement(strPreguntaVF);
                        psPreguntaVF.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaVF = psPreguntaVF.executeQuery();
                        while (rsPreguntaVF.next()) {

                            preguntaVF.setIntOrden(rsPreguntaVF.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaVF.setTema(new Tema(rsPreguntaVF.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaVF.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaVF.setStrNivel(rsPreguntaVF.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaVF.setDblPuntaje(rsPreguntaVF.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaVF.setStrEnunciado(rsPreguntaVF.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaVF.setStrReferencia(rsPreguntaVF.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));

                            preguntaVF.setBlnConJustificacion(rsPreguntaVF.getBoolean(EntropyDB.EXA_COL_PREGUNTA_VF_JUSTIFICACION));
                            preguntaVF.setBlnEsVerdadera(rsPreguntaVF.getBoolean(EntropyDB.EXA_COL_PREGUNTA_VF_VERDADERO));
                        }

                        rtaVF.setPreguntaVerdaderoFalso(preguntaVF);

                        String strRespuestaVF = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_VF_JUSTIFICACION + ", "
                                + EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_FALSO + ", "
                                + EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_VERDADERO + ", "
                                + EntropyDB.RES_COL_RESPUESTA_VF_CALIFICACION
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_VF
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_VF_RESPUESTA_ID + " = ? ";

                        PreparedStatement psRespuestaVF = conexion.prepareStatement(strRespuestaVF);
                        psRespuestaVF.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaVF = psRespuestaVF.executeQuery();

                        while (rsRespuestaVF.next()) {
                            rtaVF.setStrJustificacion(rsRespuestaVF.getString(EntropyDB.RES_COL_RESPUESTA_VF_JUSTIFICACION));
                            rtaVF.setBlnSeleccionoFalso(rsRespuestaVF.getBoolean(EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_FALSO));
                            rtaVF.setBlnSeleccionoVerdadero(rsRespuestaVF.getBoolean(EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_VERDADERO));
                            rtaVF.setCalificacion(rsRespuestaVF.getDouble(EntropyDB.RES_COL_RESPUESTA_VF_CALIFICACION));
                        }

                        rta = rtaVF;

                        psPreguntaVF.close();
                        rsPreguntaVF.close();
                        psRespuestaVF.close();
                        rsRespuestaVF.close();

                        break;
                } // end switch

                if (rta != null) {
                    colRespuestas.add(rta);
                }

            } //end while

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
//            DAOConexion.desconectarBaseDatos();
        }

        return colRespuestas;
    }

    @Override
    public Respuesta getRespuesta(int intPreguntaID) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Respuesta respuesta = null;
        
        try {

            String strConsulta = "SELECT "
                    + " R." + EntropyDB.RES_COL_RESPUESTA_ID + ", "
                    + " R." + EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID + ", "
                    + " R." + EntropyDB.RES_COL_RESPUESTA_COMENTARIO + ", "
                    + " P." + EntropyDB.EXA_COL_PREGUNTA_TIPO_PREGUNTA_ID + " "
                    + " FROM " + EntropyDB.RES_TBL_RESPUESTA + " R "
                    + " INNER JOIN " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                    + " ON R." + EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID + " = P." + EntropyDB.EXA_COL_PREGUNTA_ID
                    + " WHERE R." + EntropyDB.RES_COL_RESPUESTA_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intPreguntaID);
            ResultSet rs = psConsulta.executeQuery();
            while (rs.next()) {
                int intIDRespuesta = rs.getInt(EntropyDB.RES_COL_RESPUESTA_ID);
                int intIDPregunta = rs.getInt(EntropyDB.RES_COL_RESPUESTA_PREGUNTA_ID);
                String strComentario = rs.getString(EntropyDB.RES_COL_RESPUESTA_COMENTARIO);
                int intTipoPregunta = rs.getInt(EntropyDB.EXA_COL_PREGUNTA_TIPO_PREGUNTA_ID);

                switch (intTipoPregunta) {
                    case TipoPregunta.DESARROLLAR:
                        RespuestaDesarrollo rtaDE = new RespuestaDesarrollo();
                        rtaDE.setIntRespuestaId(intIDRespuesta);
                        rtaDE.setStrComentario(strComentario);

                        Pregunta preguntaDE = new Pregunta();
                        preguntaDE.setIntPreguntaId(intIDPregunta);
                        preguntaDE.setIntTipo(intTipoPregunta);

                        String strPreguntaDE = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaDE = conexion.prepareStatement(strPreguntaDE);
                        psPreguntaDE.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaDE = psPreguntaDE.executeQuery();
                        while (rsPreguntaDE.next()) {

                            preguntaDE.setIntOrden(rsPreguntaDE.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaDE.setTema(new Tema(rsPreguntaDE.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaDE.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaDE.setStrNivel(rsPreguntaDE.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaDE.setDblPuntaje(rsPreguntaDE.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaDE.setStrEnunciado(rsPreguntaDE.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaDE.setStrReferencia(rsPreguntaDE.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));
                        }

                        rtaDE.setPregunta(preguntaDE);

                        String strRespuestaDE = "SELECT " + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA
                                + " , "+ EntropyDB.RES_COL_RESPUESTA_DE_CALIFICACION
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_DE
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA_ID + " = ? ";

                        PreparedStatement psRespuestaDE = conexion.prepareStatement(strRespuestaDE);
                        psRespuestaDE.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaDE = psRespuestaDE.executeQuery();
                        while (rsRespuestaDE.next()) {
                            rtaDE.setStrRespuesta(rsRespuestaDE.getString(EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA));
                            rtaDE.setCalificacion(rsRespuestaDE.getDouble(EntropyDB.RES_COL_RESPUESTA_DE_CALIFICACION));
                        }

                        respuesta = rtaDE;

                        psPreguntaDE.close();
                        rsPreguntaDE.close();
                        psRespuestaDE.close();
                        rsRespuestaDE.close();

                        break;

                    case TipoPregunta.MULTIPLE_OPCION:

                        RespuestaPreguntaMultipleOpcion rtaMO = new RespuestaPreguntaMultipleOpcion();
                        rtaMO.setIntRespuestaId(intIDRespuesta);
                        rtaMO.setStrComentario(strComentario);

                        PreguntaMultipleOpcion preguntaMO = new PreguntaMultipleOpcion();
                        preguntaMO.setIntPreguntaId(intIDPregunta);
                        preguntaMO.setIntTipo(intTipoPregunta);

                        String strPreguntaMO = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaMO = conexion.prepareStatement(strPreguntaMO);
                        psPreguntaMO.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaMO = psPreguntaMO.executeQuery();
                        while (rsPreguntaMO.next()) {
                            preguntaMO.setIntOrden(rsPreguntaMO.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaMO.setTema(new Tema(rsPreguntaMO.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaMO.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaMO.setStrNivel(rsPreguntaMO.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaMO.setDblPuntaje(rsPreguntaMO.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaMO.setStrEnunciado(rsPreguntaMO.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaMO.setStrReferencia(rsPreguntaMO.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));
                        }

                        String strPreguntaMOOpciones = "SELECT "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_CORRECTO + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_OPCION + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_ORDEN + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA_MO
                                + " WHERE " + EntropyDB.EXA_COL_PREGUNTA_MO_PREGUNTA_ID + " = ? "
                                + " GROUP BY "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_PREGUNTA_ID + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_CORRECTO + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_OPCION + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_MO_ORDEN;

                        PreparedStatement psPreguntaMOOpciones = conexion.prepareStatement(strPreguntaMOOpciones);
                        psPreguntaMOOpciones.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaMOOpciones = psPreguntaMOOpciones.executeQuery();
                        ArrayList<OpcionMultipleOpcion> lstOpcionesPregunta = new ArrayList<>();
                        while (rsPreguntaMOOpciones.next()) {
                            OpcionMultipleOpcion opcionPregunta = new OpcionMultipleOpcion();
                            opcionPregunta.setBlnEsVerdadera(rsPreguntaMOOpciones.getBoolean(EntropyDB.EXA_COL_PREGUNTA_MO_CORRECTO));
                            opcionPregunta.setStrRespuesta(rsPreguntaMOOpciones.getString(EntropyDB.EXA_COL_PREGUNTA_MO_OPCION));
                            opcionPregunta.setIntOrden(rsPreguntaMOOpciones.getInt(EntropyDB.EXA_COL_PREGUNTA_MO_ORDEN));
                            lstOpcionesPregunta.add(opcionPregunta);
                        }
                        preguntaMO.setColOpciones(lstOpcionesPregunta);

                        rtaMO.setPreguntaMultipleOpcion(preguntaMO);

                        String strRespuestaMO = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_MO
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA_ID + " = ?"
                                + " GROUP BY " + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA_ID + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA;

                        PreparedStatement psRespuestaMO = conexion.prepareStatement(strRespuestaMO);
                        psRespuestaMO.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaMO = psRespuestaMO.executeQuery();
                        ArrayList<RespuestaOpcionMultipleOpcion> lstOpcionesRespuesta = new ArrayList<>();
                        while (rsRespuestaMO.next()) {
                            int intOrden = rsRespuestaMO.getInt(EntropyDB.RES_COL_RESPUESTA_MO_ORDEN);
                            boolean blnEsMarcada = rsRespuestaMO.getBoolean(EntropyDB.RES_COL_RESPUESTA_MO_ES_MARCADA);
                            String strRespuesta = rsRespuestaMO.getString(EntropyDB.RES_COL_RESPUESTA_MO_RESPUESTA);
                            RespuestaOpcionMultipleOpcion rtaOpcion = new RespuestaOpcionMultipleOpcion(intOrden, strRespuesta, blnEsMarcada);
                            lstOpcionesRespuesta.add(rtaOpcion);
                        }
                        rtaMO.setColeccionOpciones(lstOpcionesRespuesta);

                        respuesta = rtaMO;

                        psPreguntaMOOpciones.close();
                        rsPreguntaMOOpciones.close();
                        psPreguntaMO.close();
                        rsPreguntaMO.close();
                        psRespuestaMO.close();
                        rsRespuestaMO.close();

                        break;

                    case TipoPregunta.NUMERICA:

                        RespuestaPreguntaNumerica rtaNU = new RespuestaPreguntaNumerica();
                        rtaNU.setIntRespuestaId(intIDRespuesta);
                        rtaNU.setStrComentario(strComentario);

                        PreguntaNumerica preguntaNU = new PreguntaNumerica();
                        preguntaNU.setIntPreguntaId(intIDPregunta);
                        preguntaNU.setIntTipo(intTipoPregunta);

                        String strPreguntaNU = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_ES_RANGO + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_NUMERO + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_DESDE + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_HASTA + ", "
                                + " NU." + EntropyDB.EXA_COL_PREGUNTA_NU_VARIACION + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " INNER JOIN " + EntropyDB.EXA_TBL_PREGUNTA_NU + " NU "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = NU." + EntropyDB.EXA_COL_PREGUNTA_NU_PREGUNTA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaNU = conexion.prepareStatement(strPreguntaNU);
                        psPreguntaNU.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaNU = psPreguntaNU.executeQuery();
                        while (rsPreguntaNU.next()) {

                            preguntaNU.setIntOrden(rsPreguntaNU.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaNU.setTema(new Tema(rsPreguntaNU.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaNU.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaNU.setStrNivel(rsPreguntaNU.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaNU.setDblPuntaje(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaNU.setStrEnunciado(rsPreguntaNU.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaNU.setStrReferencia(rsPreguntaNU.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));

                            boolean blnEsRango = rsPreguntaNU.getBoolean(EntropyDB.EXA_COL_PREGUNTA_NU_ES_RANGO);
                            preguntaNU.setBlnEsRango(blnEsRango);
                            if (blnEsRango) {
                                preguntaNU.setDblNumero(0);
                                preguntaNU.setDblRangoDesde(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_DESDE));
                                preguntaNU.setDblRangoHasta(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_RANGO_HASTA));
                            } else {
                                preguntaNU.setDblNumero(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_NUMERO));
                                preguntaNU.setDblRangoDesde(0);
                                preguntaNU.setDblRangoHasta(0);
                            }
                            preguntaNU.setDblVariacion(rsPreguntaNU.getDouble(EntropyDB.EXA_COL_PREGUNTA_NU_VARIACION));
                        }

                        rtaNU.setPreguntaNumerica(preguntaNU);

                        String strRespuestaNU = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_NUMERO + ", "
                                + EntropyDB.RES_COL_RESPUESTA_NU_RANGO_DESDE + ", "
                                + EntropyDB.RES_COL_RESPUESTA_NU_RANGO_HASTA + " "
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_NU
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_ID + " = ? ";

                        PreparedStatement psRespuestaNU = conexion.prepareStatement(strRespuestaNU);
                        psRespuestaNU.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaNU = psRespuestaNU.executeQuery();

                        while (rsRespuestaNU.next()) {
                            rtaNU.setDblRespuestaNumero(rsRespuestaNU.getDouble(EntropyDB.RES_COL_RESPUESTA_NU_RESPUESTA_NUMERO));
                            rtaNU.setDblRangoDesde(rsRespuestaNU.getDouble(EntropyDB.RES_COL_RESPUESTA_NU_RANGO_DESDE));
                            rtaNU.setDblRangoHasta(rsRespuestaNU.getDouble(EntropyDB.RES_COL_RESPUESTA_NU_RANGO_HASTA));
                        }

                        respuesta = rtaNU;

                        psPreguntaNU.close();
                        rsPreguntaNU.close();
                        psRespuestaNU.close();
                        rsRespuestaNU.close();

                        break;

                    case TipoPregunta.RELACION_COLUMNAS:

                        RespuestaPreguntaRelacionColumnas rtaRC = new RespuestaPreguntaRelacionColumnas();
                        rtaRC.setIntRespuestaId(intIDRespuesta);
                        rtaRC.setStrComentario(strComentario);

                        PreguntaRelacionColumnas preguntaRC = new PreguntaRelacionColumnas();
                        preguntaRC.setIntPreguntaId(intIDPregunta);
                        preguntaRC.setIntTipo(intTipoPregunta);

                        String strPreguntaRC = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaRC = conexion.prepareStatement(strPreguntaRC);
                        psPreguntaRC.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaRC = psPreguntaRC.executeQuery();
                        while (rsPreguntaRC.next()) {
                            preguntaRC.setIntOrden(rsPreguntaRC.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaRC.setTema(new Tema(rsPreguntaRC.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaRC.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaRC.setStrNivel(rsPreguntaRC.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaRC.setDblPuntaje(rsPreguntaRC.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaRC.setStrEnunciado(rsPreguntaRC.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaRC.setStrReferencia(rsPreguntaRC.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));
                        }

                        String strPreguntaRCCombinaciones = "SELECT "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_ORDEN + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA_RC
                                + " WHERE " + EntropyDB.EXA_COL_PREGUNTA_RC_PREGUNTA_ID + " = ? "
                                + " GROUP BY "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_PREGUNTA_ID + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_ORDEN + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA;

                        PreparedStatement psPreguntaRCCombinaciones = conexion.prepareStatement(strPreguntaRCCombinaciones);
                        psPreguntaRCCombinaciones.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaRCCombinaciones = psPreguntaRCCombinaciones.executeQuery();
                        ArrayList<CombinacionRelacionColumnas> lstCombinacionesPregunta = new ArrayList<>();
                        while (rsPreguntaRCCombinaciones.next()) {
                            CombinacionRelacionColumnas combinacionPregunta = new CombinacionRelacionColumnas();
                            combinacionPregunta.setIntOrden(rsPreguntaRCCombinaciones.getInt(EntropyDB.EXA_COL_PREGUNTA_RC_ORDEN));
                            combinacionPregunta.setStrColumnaIzquierda(rsPreguntaRCCombinaciones.getString(EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA));
                            combinacionPregunta.setStrColumnaDerecha(rsPreguntaRCCombinaciones.getString(EntropyDB.EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA));
                            lstCombinacionesPregunta.add(combinacionPregunta);
                        }
                        preguntaRC.setColCombinaciones(lstCombinacionesPregunta);

                        rtaRC.setPreguntaRelacionColumnas(preguntaRC);

                        String strRespuestaRC = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_RC_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_RC
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_RC_RESPUESTA_ID + " = ?"
                                + " GROUP BY " + EntropyDB.RES_COL_RESPUESTA_RC_RESPUESTA_ID + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_ORDEN + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA + ", "
                                + EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA;

                        PreparedStatement psRespuestaRC = conexion.prepareStatement(strRespuestaRC);
                        psRespuestaRC.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaRC = psRespuestaRC.executeQuery();
                        ArrayList<RespuestaCombinacionRelacionColumnas> lstCombinacionesRespuesta = new ArrayList<>();
                        while (rsRespuestaRC.next()) {
                            int intOrden = rsRespuestaRC.getInt(EntropyDB.RES_COL_RESPUESTA_RC_ORDEN);
                            String strColIzq = rsRespuestaRC.getString(EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA);
                            String strColDer = rsRespuestaRC.getString(EntropyDB.RES_COL_RESPUESTA_RC_COLUMNA_DERECHA);
                            RespuestaCombinacionRelacionColumnas rtaCombinacion = new RespuestaCombinacionRelacionColumnas(intOrden, strColIzq, strColDer);
                            lstCombinacionesRespuesta.add(rtaCombinacion);
                        }

                        rtaRC.setColCombinaciones(lstCombinacionesRespuesta);

                        respuesta = rtaRC;

                        psPreguntaRCCombinaciones.close();
                        rsPreguntaRCCombinaciones.close();
                        psPreguntaRC.close();
                        rsPreguntaRC.close();
                        psRespuestaRC.close();
                        rsRespuestaRC.close();

                        break;

                    case TipoPregunta.VERDADERO_FALSO:

                        RespuestaPreguntaVerdaderoFalso rtaVF = new RespuestaPreguntaVerdaderoFalso();
                        rtaVF.setIntRespuestaId(intIDRespuesta);
                        rtaVF.setStrComentario(strComentario);

                        PreguntaVerdaderoFalso preguntaVF = new PreguntaVerdaderoFalso();
                        preguntaVF.setIntPreguntaId(intIDPregunta);
                        preguntaVF.setIntTipo(intTipoPregunta);

                        String strPreguntaVF = "SELECT "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ORDEN + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + ", "
                                + " T." + EntropyDB.EXA_COL_TEMA_NOMBRE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_NIVEL + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_PUNTAJE + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO + ", "
                                + " P." + EntropyDB.EXA_COL_PREGUNTA_REFERENCIA + ", "
                                + " VF." + EntropyDB.EXA_COL_PREGUNTA_VF_JUSTIFICACION + ", "
                                + " VF." + EntropyDB.EXA_COL_PREGUNTA_VF_VERDADERO + " "
                                + " FROM " + EntropyDB.EXA_TBL_PREGUNTA + " P "
                                + "LEFT JOIN " + EntropyDB.EXA_TBL_TEMA + " T "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_TEMA_ID + " = T." + EntropyDB.EXA_COL_TEMA_ID
                                + " INNER JOIN " + EntropyDB.EXA_TBL_PREGUNTA_VF + " VF "
                                + " ON P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = VF." + EntropyDB.EXA_COL_PREGUNTA_VF_PREGUNTA_ID
                                + " WHERE P." + EntropyDB.EXA_COL_PREGUNTA_ID + " = ? ";

                        PreparedStatement psPreguntaVF = conexion.prepareStatement(strPreguntaVF);
                        psPreguntaVF.setInt(1, intIDPregunta);
                        ResultSet rsPreguntaVF = psPreguntaVF.executeQuery();
                        while (rsPreguntaVF.next()) {

                            preguntaVF.setIntOrden(rsPreguntaVF.getInt(EntropyDB.EXA_COL_PREGUNTA_ORDEN));
                            preguntaVF.setTema(new Tema(rsPreguntaVF.getInt(EntropyDB.EXA_COL_PREGUNTA_TEMA_ID),
                                    rsPreguntaVF.getString(EntropyDB.EXA_COL_TEMA_NOMBRE)
                            ));
                            preguntaVF.setStrNivel(rsPreguntaVF.getString(EntropyDB.EXA_COL_PREGUNTA_NIVEL));
                            preguntaVF.setDblPuntaje(rsPreguntaVF.getDouble(EntropyDB.EXA_COL_PREGUNTA_PUNTAJE));
                            preguntaVF.setStrEnunciado(rsPreguntaVF.getString(EntropyDB.EXA_COL_PREGUNTA_ENUNCIADO));
                            preguntaVF.setStrReferencia(rsPreguntaVF.getString(EntropyDB.EXA_COL_PREGUNTA_REFERENCIA));

                            preguntaVF.setBlnConJustificacion(rsPreguntaVF.getBoolean(EntropyDB.EXA_COL_PREGUNTA_VF_JUSTIFICACION));
                            preguntaVF.setBlnEsVerdadera(rsPreguntaVF.getBoolean(EntropyDB.EXA_COL_PREGUNTA_VF_VERDADERO));
                        }

                        rtaVF.setPreguntaVerdaderoFalso(preguntaVF);

                        String strRespuestaVF = "SELECT "
                                + EntropyDB.RES_COL_RESPUESTA_VF_JUSTIFICACION + ", "
                                + EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_FALSO + ", "
                                + EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_VERDADERO + ", "
                                + EntropyDB.RES_COL_RESPUESTA_VF_CALIFICACION
                                + " FROM " + EntropyDB.RES_TBL_RESPUESTA_VF
                                + " WHERE " + EntropyDB.RES_COL_RESPUESTA_VF_RESPUESTA_ID + " = ? ";

                        PreparedStatement psRespuestaVF = conexion.prepareStatement(strRespuestaVF);
                        psRespuestaVF.setInt(1, intIDRespuesta);
                        ResultSet rsRespuestaVF = psRespuestaVF.executeQuery();

                        while (rsRespuestaVF.next()) {
                            rtaVF.setStrJustificacion(rsRespuestaVF.getString(EntropyDB.RES_COL_RESPUESTA_VF_JUSTIFICACION));
                            rtaVF.setBlnSeleccionoFalso(rsRespuestaVF.getBoolean(EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_FALSO));
                            rtaVF.setBlnSeleccionoVerdadero(rsRespuestaVF.getBoolean(EntropyDB.RES_COL_RESPUESTA_VF_SELECCIONO_VERDADERO));
                            rtaVF.setCalificacion(rsRespuestaVF.getDouble(EntropyDB.RES_COL_RESPUESTA_VF_CALIFICACION));
                        }

                        respuesta = rtaVF;

                        psPreguntaVF.close();
                        rsPreguntaVF.close();
                        psRespuestaVF.close();
                        rsRespuestaVF.close();

                        break;
                } // end switch

            } //end while

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
//            DAOConexion.desconectarBaseDatos();
        }

        return respuesta;
    }

    @Override
    public boolean actualizarRespuesta(Respuesta respuesta) {
        Connection conexion = DAOConexion.conectarBaseDatos();
        boolean blnExito = false;
        try {
            
            conexion.setAutoCommit(false);
            
            String strUpdate = "UPDATE  "+ EntropyDB.RES_TBL_RESPUESTA
                    + " SET " + EntropyDB.RES_COL_RESPUESTA_COMENTARIO + " = ?"
                    + " WHERE " + EntropyDB.RES_COL_RESPUESTA_ID + " = ?";

            PreparedStatement psConsulta = conexion.prepareStatement(strUpdate);
            psConsulta.setString(1, respuesta.getStrComentario());
            psConsulta.setInt(2, respuesta.getIntRespuestaId());
            psConsulta.executeUpdate();
            
            if (!respuesta.esCorreccionAutomatica()) {
                
                if (respuesta instanceof RespuestaDesarrollo) {
                    
                    strUpdate = "UPDATE  "+ EntropyDB.RES_TBL_RESPUESTA_DE
                    + " SET " + EntropyDB.RES_COL_RESPUESTA_DE_CALIFICACION + " = ?"
                    + " WHERE " + EntropyDB.RES_COL_RESPUESTA_DE_RESPUESTA_ID + " = ?";
                    
                    psConsulta = conexion.prepareStatement(strUpdate);
                    psConsulta.setDouble(1, ((RespuestaDesarrollo) respuesta).getCalificacion());
                    psConsulta.setInt(2, respuesta.getIntRespuestaId());
                    
                } else if (respuesta instanceof RespuestaPreguntaVerdaderoFalso) {
                    
                    strUpdate = "UPDATE  "+ EntropyDB.RES_TBL_RESPUESTA_VF
                    + " SET " + EntropyDB.RES_COL_RESPUESTA_VF_CALIFICACION + " = ?"
                    + " WHERE " + EntropyDB.RES_COL_RESPUESTA_VF_RESPUESTA_ID + " = ?";
                    
                    psConsulta = conexion.prepareStatement(strUpdate);
                    psConsulta.setDouble(1, ((RespuestaPreguntaVerdaderoFalso) respuesta).getCalificacion());
                    psConsulta.setInt(2, respuesta.getIntRespuestaId());
                    
                }
                
                psConsulta.executeUpdate();
                
            }
            
            conexion.commit();
            
            blnExito = true;
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DAODiseñoExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return blnExito;
    }

    @Override
    public ArrayList<Respuesta> getRespuestasNoCalificadas(int intIDExamen) {
        ArrayList<Respuesta> colRespuestas = new ArrayList<>();
        try {
            Connection conexion = DAOConexion.conectarBaseDatos();
            String strConsulta = "SELECT RE.respuestaId "
                    + "FROM res_respuesta RE "
                    + "WHERE RE.respuestaId IN "
                    + "(SELECT RE.respuestaId "
                    + "FROM res_resolucion R "
                    + "INNER JOIN res_respuesta RE ON R.resolucionId = RE.resolucionId "
                    + "INNER JOIN res_respuestadesarrollo RD ON RE.respuestaId = RD.respuestaId "
                    + "WHERE R.examenId = ? AND RD.calificacion = -1 "
                    + "GROUP BY RE.respuestaId)  "
                    + "OR RE.respuestaId IN  "
                    + "(SELECT RE.respuestaId "
                    + "FROM res_resolucion R "
                    + "INNER JOIN res_respuesta RE ON R.resolucionId = RE.resolucionId "
                    + "INNER JOIN res_respuestaverdaderofalso RVF ON RE.respuestaId = RVF.respuestaId "
                    + "WHERE R.examenId = ? AND RVF.calificacion = -1 "
                    + "GROUP BY RE.respuestaId)";
            PreparedStatement psConsulta = conexion.prepareStatement(strConsulta);
            psConsulta.setInt(1, intIDExamen);
            psConsulta.setInt(2, intIDExamen);

            ResultSet rsConsulta = psConsulta.executeQuery();
            
            while (rsConsulta.next()){
                colRespuestas.add(getRespuesta(rsConsulta.getInt(1)));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DAORespuesta.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOConexion.desconectarBaseDatos();
        }
        return colRespuestas;
    }

}
