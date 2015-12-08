/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.reporte;

import backend.dao.DAOConexion;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Jose
 */
public class GestorGenerarReportePresentacionesRealizadas {
    
    public static void  reportePresentacionRealizada(String institucion, String curso, String fecha, int idCurso, Long longFecha) throws JRException
    {
        Connection conexion = DAOConexion.conectarBaseDatos();
        Map parametros = new HashMap();
        parametros.put("parametroInstitucion", institucion);
        parametros.put("parametroCurso",  curso);
        parametros.put("parametroFecha", fecha);
        parametros.put("parametroCursoId", idCurso);
        parametros.put("parametroFechaLong", longFecha);
        JasperReport reporte=null;
        reporte= (JasperReport)JRLoader.loadObjectFromFile("."+ File.separator +"src"+File.separator+"reportes"+File.separator+"reporteListaDeAlumnosPorClase.jasper");
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexion);
        JasperViewer.viewReport(print, false);
    }
}
