package backend.reporte;


import backend.diseños.OpcionMultipleOpcion;
import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import backend.resoluciones.RespuestaCombinacionRelacionColumnas;
import backend.resoluciones.RespuestaDesarrollo;
import backend.resoluciones.RespuestaOpcionMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaNumerica;
import backend.resoluciones.RespuestaPreguntaRelacionColumnas;
import backend.resoluciones.RespuestaPreguntaVerdaderoFalso;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Gaston Noves
 */
public class GestorGenerarReporteResolucion {
    
    private Resolucion resolucionExamen;
    private String strFilePath = "ReporteResolucionPrueba.pdf";
    
    public GestorGenerarReporteResolucion(Resolucion resolucionExamen){
        this.resolucionExamen = resolucionExamen;
        this.strFilePath = resolucionExamen.getAlumno().getStrApellido() + resolucionExamen.getAlumno().getStrNombre() + ".pdf";
    }
    
    public void generarReporteResolucion() {

        Document document = new Document();

        try {
            int contadorOrden = 1;
            PdfWriter.getInstance(document, new FileOutputStream(strFilePath));
            document.open();

            //Titulo Encabezado
            Paragraph paragraph = new Paragraph(this.resolucionExamen.getExamen().getStrNombre());
            paragraph.setSpacingAfter(25);
            paragraph.setSpacingBefore(25);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setIndentationLeft(50);
            paragraph.setIndentationRight(50);
            document.add(paragraph);
            
            //Datos de la Resolucion
            Paragraph parrafoDetalles = new Paragraph(13, "Alumno: " + this.resolucionExamen.getAlumno().getStrApellido() + ", " + this.resolucionExamen.getAlumno().getStrNombre() + "\n");
            parrafoDetalles.add("-Nota: " + this.resolucionExamen.getCalificacion() + ",   -Tiempo empleado: " + this.resolucionExamen.getIntTiempoEmpleado());
            document.add(parrafoDetalles);
            
            for (Respuesta respuesta : this.resolucionExamen.getColRespuestas()) {

                String clase = respuesta.getClass().getSimpleName();
                switch (clase) {
                    
                    case "RespuestaDesarrollo":
                        RespuestaDesarrollo respuestaDesarrollo = (RespuestaDesarrollo) respuesta;
                        Paragraph parrafoRespuestaDesarrollar = new Paragraph(13, contadorOrden + ") " + respuesta.getPregunta().getStrEnunciado() + "\n\n");
                        parrafoRespuestaDesarrollar.add("   " + respuestaDesarrollo.getStrRespuesta());
                        parrafoRespuestaDesarrollar.add("Calificacion: " + respuestaDesarrollo.getCalificacion());
                        if (!respuestaDesarrollo.getStrComentario().equals("")) {
                            parrafoRespuestaDesarrollar.add("Comentario Docente: " + respuestaDesarrollo.getStrComentario());
                        }
                        parrafoRespuestaDesarrollar.setSpacingBefore(30);
                        document.add(parrafoRespuestaDesarrollar);
                        contadorOrden++;
                        break;
                        
                    case "RespuestaPreguntaMultipleOpcion":
                        RespuestaPreguntaMultipleOpcion respuestaPreguntaMultipleOpcion = (RespuestaPreguntaMultipleOpcion) respuesta;
                        Paragraph parrafoPreguntaMultipleOpcion = new Paragraph(13, contadorOrden + ") " + respuestaPreguntaMultipleOpcion.getPregunta().getStrEnunciado() + "\n\n");
                        parrafoPreguntaMultipleOpcion.setSpacingAfter(15);
                        //parrafoPreguntaMultipleOpcion.setSpacingBefore(30);
                        document.add(parrafoPreguntaMultipleOpcion);
                        ArrayList <OpcionMultipleOpcion> colOpcionMultipleOpcionCorrectas = new ArrayList<>();
                        
                        for (OpcionMultipleOpcion opcionMultipleOpcionCorrecta : respuestaPreguntaMultipleOpcion.getPregunta().getColOpciones()) {
                            if(opcionMultipleOpcionCorrecta.isBlnEsVerdadera()){
                                colOpcionMultipleOpcionCorrectas.add(opcionMultipleOpcionCorrecta);
                            }
                        }
                                
                        List orderedList = new List(List.ORDERED);//Ver que pasa si es ordenada o desordenada.
                        for (RespuestaOpcionMultipleOpcion respuestaOpcionMultipleOpcion : respuestaPreguntaMultipleOpcion.getColeccionOpciones()) {
                            OpcionMultipleOpcion opcionTemporal = new OpcionMultipleOpcion();
                            opcionTemporal.setStrRespuesta(respuestaOpcionMultipleOpcion.getStrRespuesta());
                            if(colOpcionMultipleOpcionCorrectas.contains(opcionTemporal) && respuestaOpcionMultipleOpcion.isBlnEsMarcada()){
                                orderedList.add(new ListItem(respuestaOpcionMultipleOpcion.getStrRespuesta() + "✔"));
                            }else{
                                if(respuestaOpcionMultipleOpcion.isBlnEsMarcada()){
                                     orderedList.add(new ListItem(respuestaOpcionMultipleOpcion.getStrRespuesta() + "X"));
                                }else {
                                     orderedList.add(new ListItem(respuestaOpcionMultipleOpcion.getStrRespuesta()));
                                }
                            }
                        }
                        document.add(orderedList);
                        
                        Paragraph parrafoCalificacionComentarioMultipleOpcion = new Paragraph(13, "Calificacion: " + respuestaPreguntaMultipleOpcion.getCalificacion() + "\n");
                        parrafoCalificacionComentarioMultipleOpcion.add("Comentario Docente: " + respuestaPreguntaMultipleOpcion.getStrComentario());
                        parrafoCalificacionComentarioMultipleOpcion.setSpacingAfter(15);
                        parrafoCalificacionComentarioMultipleOpcion.setSpacingBefore(30);
                        document.add(parrafoCalificacionComentarioMultipleOpcion);
                        
                        contadorOrden++;
                        break;
                        
                    case "RespuestaPreguntaNumerica":
                        RespuestaPreguntaNumerica respuestaPreguntaNumerica = (RespuestaPreguntaNumerica) respuesta;
                        Paragraph parrafoPreguntaNumerica = new Paragraph(13, contadorOrden + ") " + respuestaPreguntaNumerica.getPregunta().getStrEnunciado() + "\n");
                        parrafoPreguntaNumerica.setSpacingAfter(15);
                        //parrafoPreguntaNumerica.setSpacingBefore(30);
                        document.add(parrafoPreguntaNumerica);
                        
                        Paragraph parrafoCalificacionComentarioNumerico = new Paragraph(13, "Calificacion: " + respuestaPreguntaNumerica.getCalificacion() + "\n");
                        parrafoCalificacionComentarioNumerico.add("Comentario Docente: " + respuestaPreguntaNumerica.getStrComentario());
                        parrafoCalificacionComentarioNumerico.setSpacingAfter(15);
                        parrafoCalificacionComentarioNumerico.setSpacingBefore(30);
                        document.add(parrafoCalificacionComentarioNumerico);
                        
                        contadorOrden++;
                        break;
                        
                    case "RespuestaPreguntaRelacionColumnas":
                        RespuestaPreguntaRelacionColumnas respuestaPreguntaRelacionColumnas = (RespuestaPreguntaRelacionColumnas) respuesta;
                        Paragraph parrafoPreguntaRelacionColumnas = new Paragraph(13, contadorOrden + ") " + respuestaPreguntaRelacionColumnas.getPregunta().getStrEnunciado() + "\n");
                        parrafoPreguntaRelacionColumnas.setSpacingAfter(15);
                        //parrafoPreguntaRelacionColumnas.setSpacingBefore(30);
                        document.add(parrafoPreguntaRelacionColumnas);
                        
                        PdfPTable tableRelacionesColumnas = new PdfPTable(3);

                        float[] columnWidthsRC = {2.5f, 1f, 2.5f};
                        tableRelacionesColumnas.setWidths(columnWidthsRC);
                        
                        String columnaIzquierdaRC = "";
                        String columnaDerechaRC = "";
                        
                        //Armado de columnas
                        for (RespuestaCombinacionRelacionColumnas elementoColumna : respuestaPreguntaRelacionColumnas.getColCombinaciones()) {
                            columnaIzquierdaRC = columnaIzquierdaRC + "#" + elementoColumna.getStrColumnaIzquierda() + "\n\n";
                            columnaDerechaRC = columnaDerechaRC + "#" + elementoColumna.getStrColumnaDerecha() + "\n\n";
                        }

                        //Armado de la tabla
                        PdfPCell cell1RC = new PdfPCell(new Paragraph(columnaIzquierdaRC));
                        cell1RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell1RC.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell2RC = new PdfPCell(new Paragraph("       "));
                        cell2RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2RC.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell3RC = new PdfPCell(new Paragraph(columnaDerechaRC));
                        cell3RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3RC.setBorder(Rectangle.NO_BORDER);

                        tableRelacionesColumnas.addCell(cell1RC);
                        tableRelacionesColumnas.addCell(cell2RC);
                        tableRelacionesColumnas.addCell(cell3RC);
                        document.add(tableRelacionesColumnas);
                        
                        Paragraph parrafoCalificacionComentarioRelacionColumnas = new Paragraph(13, "Calificacion: " + respuestaPreguntaRelacionColumnas.getCalificacion() + "\n");
                        parrafoCalificacionComentarioRelacionColumnas.add("Comentario Docente: " + respuestaPreguntaRelacionColumnas.getStrComentario());
                        parrafoCalificacionComentarioRelacionColumnas.setSpacingAfter(15);
                        parrafoCalificacionComentarioRelacionColumnas.setSpacingBefore(30);
                        document.add(parrafoCalificacionComentarioRelacionColumnas);
                        
                        contadorOrden++;
                        break;
                        
                    case "RespuestaPreguntaVerdaderoFalso":
                        RespuestaPreguntaVerdaderoFalso respuestaPreguntaVerdaderoFalso = (RespuestaPreguntaVerdaderoFalso) respuesta;
                        Paragraph parrafoPreguntaVerdaderoFalso = new Paragraph(13, contadorOrden + ") " + respuestaPreguntaVerdaderoFalso.getPregunta().getStrEnunciado() + "\n");
                        parrafoPreguntaVerdaderoFalso.setSpacingAfter(15);
                        //parrafoPreguntaVerdaderoFalso.setSpacingBefore(30);
                        document.add(parrafoPreguntaVerdaderoFalso);
                        
//                        PdfPTable tableVerdaderoFalso = new PdfPTable(3);
//
//                        float[] columnWidthsVF = {2.5f, 1f, 2.5f};
//                        tableVerdaderoFalso.setWidths(columnWidthsVF);
//                        
//                        String columnaIzquierdaVF = "[ ] ☐ Verdadero";
//                        String columnaDerechaVF = "[ ] ☐ Falso";
//                        
//
//                        //Armado de la tabla
//                        PdfPCell cell1VF = new PdfPCell(new Paragraph(columnaIzquierdaVF));
//                        cell1VF.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        cell1VF.setBorder(Rectangle.NO_BORDER);
//                        PdfPCell cell2VF = new PdfPCell(new Paragraph("       "));
//                        cell2VF.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        cell2VF.setBorder(Rectangle.NO_BORDER);
//                        PdfPCell cell3VF = new PdfPCell(new Paragraph(columnaDerechaVF));
//                        cell3VF.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        cell3VF.setBorder(Rectangle.NO_BORDER);
//
//                        tableVerdaderoFalso.addCell(cell1VF);
//                        tableVerdaderoFalso.addCell(cell2VF);
//                        tableVerdaderoFalso.addCell(cell3VF);
//                        document.add(tableVerdaderoFalso);
                        
                        Paragraph parrafoCalificacionComentarioVF = new Paragraph(13, "Calificacion: " + respuestaPreguntaVerdaderoFalso.getCalificacion() + "\n");
                        parrafoCalificacionComentarioVF.add("Comentario Docente: " + respuestaPreguntaVerdaderoFalso.getStrComentario());
                        parrafoCalificacionComentarioVF.setSpacingAfter(15);
                        parrafoCalificacionComentarioVF.setSpacingBefore(30);
                        document.add(parrafoCalificacionComentarioVF);
                        
                        contadorOrden++;
                        break;
                }
            }
            document.close();
            
            System.out.println("All Done");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }
    
    public String getResolucion(){
        return this.strFilePath;
    }
    
    public void borrarResolucionDeDisco(){
        File temporalFile = new File(this.strFilePath);
        if(temporalFile.delete()){
            System.out.println("Deleted File");
        }else{
            System.out.println("File not deleted");
        }
    }

}
