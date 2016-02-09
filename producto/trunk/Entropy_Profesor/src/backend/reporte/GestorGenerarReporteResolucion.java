package backend.reporte;

import backend.dao.diseños.DAOAdjunto;
import backend.dao.diseños.DAOInstitucion;
import backend.dao.examenes.DAOPreguntaExamen;
import backend.diseños.OpcionMultipleOpcion;
import backend.diseños.Pregunta;
import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import backend.resoluciones.RespuestaCombinacionRelacionColumnas;
import backend.resoluciones.RespuestaDesarrollo;
import backend.resoluciones.RespuestaOpcionMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaNumerica;
import backend.resoluciones.RespuestaPreguntaRelacionColumnas;
import backend.resoluciones.RespuestaPreguntaVerdaderoFalso;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gaston Noves
 */
public class GestorGenerarReporteResolucion {

    @SuppressWarnings("FieldMayBeFinal")
    private Resolucion resolucionExamen;
    private String strFilePath = "ReporteResolucionPrueba.pdf";

    //Estilos de texto
    public static final Font RED = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED);
    public static final Font BLUE = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
    public static final Font GREEN = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN);
    public static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
    public static final Font ANULADO = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);
    public static final Font MOTIVODEANULACION = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.RED);
    public static final Font ENUNCIADO = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.BLACK);
    public static final Font CHOISE = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.ORANGE);
    public static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
    public static final Font COMENTARIO = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, BaseColor.DARK_GRAY);

    public GestorGenerarReporteResolucion(Resolucion resolucionExamen) {
        this.resolucionExamen = resolucionExamen;
        this.strFilePath = resolucionExamen.getAlumno().getStrApellido() + resolucionExamen.getAlumno().getStrNombre() + ".pdf";
    }

    public void generarReporteResolucion() {

        Document document = new Document();

        try {
            int contadorOrden = 1;
            PdfWriter.getInstance(document, new FileOutputStream(strFilePath));
            document.open();

            //AGREGAR EL ESTADO ANULADO
            if (this.resolucionExamen.isBlnAnulada()) {
                PdfPTable contenidoEncabezado2 = new PdfPTable(1);
                contenidoEncabezado2.setTotalWidth(PageSize.A4.getWidth());
                contenidoEncabezado2.setLockedWidth(true);
                contenidoEncabezado2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                PdfPCell cellTitulo = new PdfPCell(new Paragraph("EXAMEN ANULADO", ANULADO));
                cellTitulo.setBorder(Rectangle.NO_BORDER);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
                contenidoEncabezado2.addCell(cellTitulo);
                document.add(contenidoEncabezado2);
            }

            //LOGO E INSTITUCION
            PdfPTable tablaEncabezado = new PdfPTable(2);
            float[] columnWidths = {40f, 350f};
            tablaEncabezado.setTotalWidth(columnWidths);
            tablaEncabezado.setLockedWidth(true);
            tablaEncabezado.setWidths(columnWidths);
            tablaEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            //INSTITUCION
            DAOInstitucion dAOInstitucion = new DAOInstitucion();
            String institucion = dAOInstitucion.buscarInstitucion(this.resolucionExamen.getExamen().getCurso().getIntCursoId()).getStrNombre();
            Image imagenLogoCurso = null;
            PdfPCell cellInst = new PdfPCell(new Paragraph(institucion, TITULO));
            cellInst.setBorder(Rectangle.NO_BORDER);
            cellInst.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellInst.setVerticalAlignment(Element.ALIGN_MIDDLE);
            if (this.resolucionExamen.getExamen().getCurso() != null
                    && this.resolucionExamen.getExamen().getCurso().getIntCursoId() > 0
                    && this.resolucionExamen.getExamen().getCurso().getInstitucion().getImgLogo() != null) {
                try {
                    byte[] bytesImagen = (byte[]) this.resolucionExamen.getExamen().getCurso().getInstitucion().getImgLogo();
                    imagenLogoCurso = Image.getInstance(bytesImagen);
                    PdfPCell cellLogo = new PdfPCell(imagenLogoCurso, true);
                    cellLogo.setBorder(Rectangle.NO_BORDER);
                    tablaEncabezado.addCell(cellLogo);
                    cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
                } catch (BadElementException | IOException ex) {
                    Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            PdfPCell cellLogo = new PdfPCell(imagenLogoCurso, true);
            cellLogo.setBorder(Rectangle.NO_BORDER);
            tablaEncabezado.addCell(cellLogo);
            cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaEncabezado.addCell(cellInst);
            document.add(tablaEncabezado);

            //TITULO 
            PdfPTable tablaTituloExamen = new PdfPTable(1);
            tablaTituloExamen.setTotalWidth(PageSize.A4.getWidth());
            tablaTituloExamen.setLockedWidth(true);
            tablaTituloExamen.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            PdfPCell cellNombreExamen = new PdfPCell(new Paragraph(this.resolucionExamen.getExamen().getStrNombre(), TITULO));
            cellNombreExamen.setBorder(Rectangle.NO_BORDER);
            cellNombreExamen.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaTituloExamen.addCell(cellNombreExamen);
            document.add(tablaTituloExamen);

            //CURSO-FECHA
            PdfPTable cursoFecha = new PdfPTable(2);
            float[] columnWidths2 = {400f, 400f};
            cursoFecha.setTotalWidth(columnWidths2);
            cursoFecha.setLockedWidth(true);
            cursoFecha.setWidths(columnWidths2);
            cursoFecha.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            cursoFecha.setHorizontalAlignment(Element.ALIGN_CENTER);

            String nombreCurso = this.resolucionExamen.getExamen().getCurso().getStrNombre();
            PdfPCell cellCurso = new PdfPCell(new Paragraph("Curso: " + nombreCurso));
            cellCurso.setBorder(Rectangle.NO_BORDER);
            cellCurso.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCurso.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cursoFecha.addCell(cellCurso);

            String fecha = this.getDate(this.resolucionExamen.getExamen().getDteFecha());
            PdfPCell cellFecha = new PdfPCell(new Paragraph("Fecha: " + fecha));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            cellFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cursoFecha.addCell(cellFecha);
            document.add(cursoFecha);

            //ALUMNO-NOTA
            PdfPTable alumnoNota = new PdfPTable(2);
            float[] columnWidths3 = {388f, 350f};
            alumnoNota.setTotalWidth(columnWidths3);
            alumnoNota.setLockedWidth(true);
            alumnoNota.setWidths(columnWidths3);
            alumnoNota.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            alumnoNota.setHorizontalAlignment(Element.ALIGN_CENTER);

            String nombreAlumno = this.resolucionExamen.getAlumno().getStrApellido() + ", " + this.resolucionExamen.getAlumno().getStrNombre();
            PdfPCell cellNombreAlumno = new PdfPCell(new Paragraph("Alumno: " + nombreAlumno));
            cellNombreAlumno.setBorder(Rectangle.NO_BORDER);
            cellNombreAlumno.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNombreAlumno.setVerticalAlignment(Element.ALIGN_MIDDLE);
            alumnoNota.addCell(cellNombreAlumno);

            PdfPCell cellNota = new PdfPCell(new Paragraph(this.getNota()));
            cellNota.setBorder(Rectangle.NO_BORDER);
            cellNota.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNota.setVerticalAlignment(Element.ALIGN_MIDDLE);
            alumnoNota.addCell(cellNota);
            //document.add(alumnoNota);
            
            
            if (this.resolucionExamen.isBlnAnulada()) {
                document.add(alumnoNota);
                PdfPTable contenidoEncabezado5 = new PdfPTable(1);
                float[] columnWidths4 = {495f};
                contenidoEncabezado5.setTotalWidth(columnWidths4);
                contenidoEncabezado5.setLockedWidth(true);
                contenidoEncabezado5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                PdfPCell cellTitulo = new PdfPCell(new Paragraph("MOTIVO DE ANULACIÓN: "+this.resolucionExamen.getStrJustificacionAnulacion(), MOTIVODEANULACION));
                cellTitulo.setBorder(Rectangle.NO_BORDER);
                cellTitulo.setHorizontalAlignment(Element.ALIGN_LEFT);
                contenidoEncabezado5.addCell(cellTitulo);
                contenidoEncabezado5.setSpacingAfter(25);
                document.add(contenidoEncabezado5);
            }
            else
            {
                alumnoNota.setSpacingAfter(25);
                document.add(alumnoNota);
            }
            
            for (Respuesta respuesta : this.resolucionExamen.getColRespuestas()) {

                String clase = respuesta.getClass().getSimpleName();
                switch (clase) {

                    case "RespuestaDesarrollo":
                        RespuestaDesarrollo respuestaDesarrollo = (RespuestaDesarrollo) respuesta;
                        Paragraph parrafoRespuestaDesarrollar = new Paragraph();
                        Chunk enunciadoDesarrollo = new Chunk(contadorOrden + ") " + respuesta.getPregunta().getStrEnunciado(), ENUNCIADO);
                        parrafoRespuestaDesarrollar.add(enunciadoDesarrollo);

                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempDesarrollo = this.getImagePdfTable(respuestaDesarrollo);
                        if (imageTempDesarrollo != null) {
                            imageTempDesarrollo.setSpacingAfter(15);
                            parrafoRespuestaDesarrollar.add(imageTempDesarrollo);
                        } else {
                            parrafoRespuestaDesarrollar.add("\n\n");//Espaciado con el contenido luego del enunciado
                        }

                        parrafoRespuestaDesarrollar.add("   " + respuestaDesarrollo.getStrRespuesta() + "\n\n");
                        if (respuestaDesarrollo.getCalificacion() == -1) {
                            parrafoRespuestaDesarrollar.add(new Chunk("\n" + "Calificacion: Sin calificar/" + respuestaDesarrollo.getPregunta().getDblPuntaje() + "\n", BLUE));
                        } else {
                            parrafoRespuestaDesarrollar.add(new Chunk("\n" + "Calificacion: " + respuestaDesarrollo.getCalificacion() + "/" + respuestaDesarrollo.getPregunta().getDblPuntaje() + "\n", BLUE));

                        }

                        if (respuestaDesarrollo.getStrComentario() != null && !respuestaDesarrollo.getStrComentario().isEmpty()) {
                            parrafoRespuestaDesarrollar.add(new Chunk("Comentario Docente: " + respuestaDesarrollo.getStrComentario() + "\n", COMENTARIO));
                        }
                        contadorOrden++;
                        if (this.resolucionExamen.getColRespuestas().size() >= contadorOrden) {
                            parrafoRespuestaDesarrollar.setSpacingAfter(15);
                            Chunk lineSeparator = new Chunk("______________________________________________________________________________", COMENTARIO);
                            parrafoRespuestaDesarrollar.add(lineSeparator);
                        }
                        parrafoRespuestaDesarrollar.setSpacingAfter(15);
                        document.add(parrafoRespuestaDesarrollar);
                        break;

                    case "RespuestaPreguntaMultipleOpcion":
                        RespuestaPreguntaMultipleOpcion respuestaPreguntaMultipleOpcion = (RespuestaPreguntaMultipleOpcion) respuesta;
                        Paragraph parrafoPreguntaMultipleOpcion = new Paragraph();
                        Chunk enunciadoRespuestaPreguntaMultipleOpcion = new Chunk(contadorOrden + ") " + respuestaPreguntaMultipleOpcion.getPregunta().getStrEnunciado(), ENUNCIADO);
                        parrafoPreguntaMultipleOpcion.add(enunciadoRespuestaPreguntaMultipleOpcion);

                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempMO = this.getImagePdfTable(respuestaPreguntaMultipleOpcion);
                        if (imageTempMO != null) {
                            imageTempMO.setSpacingAfter(15);
                            parrafoPreguntaMultipleOpcion.add(imageTempMO);
                        } else {
                            parrafoPreguntaMultipleOpcion.add("\n\n");//Espaciado con el contenido luego del enunciado
                        }

                        parrafoPreguntaMultipleOpcion.setSpacingAfter(15);
                        ArrayList<OpcionMultipleOpcion> colOpcionMultipleOpcionCorrectas = new ArrayList<>();

                        for (RespuestaOpcionMultipleOpcion respuestaOpcionMultipleOpcion : respuestaPreguntaMultipleOpcion.getColeccionOpciones()) {
                            if (respuestaOpcionMultipleOpcion.isBlnEsMarcada()) {
                                Chunk choise = new Chunk("- " + respuestaOpcionMultipleOpcion.getStrRespuesta() + "\n", CHOISE);
                                parrafoPreguntaMultipleOpcion.add(choise);
                            } else {
                                Chunk choise = new Chunk("- " + respuestaOpcionMultipleOpcion.getStrRespuesta() + "\n", NORMAL);
                                parrafoPreguntaMultipleOpcion.add(choise);
                            }
                        }
                        document.add(parrafoPreguntaMultipleOpcion);
                        Paragraph parrafoCalificacionComentarioMultipleOpcion;
                        if (respuestaPreguntaMultipleOpcion.getCalificacion() == -1) {
                            parrafoCalificacionComentarioMultipleOpcion = new Paragraph(13, "\n" + "Calificacion: Sin calificar/" + respuestaPreguntaMultipleOpcion.getPregunta().getDblPuntaje() + "\n", BLUE);
                        } else {
                            parrafoCalificacionComentarioMultipleOpcion = new Paragraph(13, "\n" + "Calificacion: " + respuestaPreguntaMultipleOpcion.getCalificacion() + "/" + respuestaPreguntaMultipleOpcion.getPregunta().getDblPuntaje() + "\n", BLUE);
                        }
                        if (respuestaPreguntaMultipleOpcion.getStrComentario() != null && !respuestaPreguntaMultipleOpcion.getStrComentario().isEmpty()) {
                            parrafoCalificacionComentarioMultipleOpcion.add(new Chunk("Comentario Docente: " + respuestaPreguntaMultipleOpcion.getStrComentario() + "\n", COMENTARIO));
                        }
                        contadorOrden++;
                        if (this.resolucionExamen.getColRespuestas().size() >= contadorOrden) {
                            parrafoCalificacionComentarioMultipleOpcion.setSpacingAfter(15);
                            Chunk lineSeparator = new Chunk("______________________________________________________________________________", COMENTARIO);
                            parrafoCalificacionComentarioMultipleOpcion.add(lineSeparator);
                        }
                        document.add(parrafoCalificacionComentarioMultipleOpcion);
                        break;

                    case "RespuestaPreguntaNumerica":
                        RespuestaPreguntaNumerica respuestaPreguntaNumerica = (RespuestaPreguntaNumerica) respuesta;
                        Paragraph parrafoPreguntaNumerica = new Paragraph();
                        Chunk enunciadoRespuestaPreguntaNumerica = new Chunk(contadorOrden + ") " + respuestaPreguntaNumerica.getPregunta().getStrEnunciado(), ENUNCIADO);
                        parrafoPreguntaNumerica.add(enunciadoRespuestaPreguntaNumerica);

                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempNumerica = this.getImagePdfTable(respuestaPreguntaNumerica);
                        if (imageTempNumerica != null) {
                            imageTempNumerica.setSpacingAfter(15);
                            parrafoPreguntaNumerica.add(imageTempNumerica);
                        } else {
                            parrafoPreguntaNumerica.add("\n\n");//Espaciado con el contenido luego del enunciado
                        }

                        parrafoPreguntaNumerica.add("   " + respuestaPreguntaNumerica.getDblRespuestaNumero() + "\n");
                        document.add(parrafoPreguntaNumerica);
                        Paragraph parrafoCalificacionComentarioNumerico;
                        if (respuestaPreguntaNumerica.getCalificacion() == -1) {
                            parrafoCalificacionComentarioNumerico = new Paragraph(13, "\n" + "Calificacion: Sin calificar /" + respuestaPreguntaNumerica.getPregunta().getDblPuntaje() + "\n", BLUE);
                        } else {
                            parrafoCalificacionComentarioNumerico = new Paragraph(13, "\n" + "Calificacion: " + respuestaPreguntaNumerica.getCalificacion() + "/" + respuestaPreguntaNumerica.getPregunta().getDblPuntaje() + "\n", BLUE);
                        }
                        if (respuestaPreguntaNumerica.getStrComentario() != null && !respuestaPreguntaNumerica.getStrComentario().isEmpty()) {
                            parrafoPreguntaNumerica.add(new Chunk("Comentario Docente: " + respuestaPreguntaNumerica.getStrComentario() + "\n", COMENTARIO));
                        }
                        contadorOrden++;
                        if (this.resolucionExamen.getColRespuestas().size() >= contadorOrden) {
                            parrafoCalificacionComentarioNumerico.setSpacingAfter(15);
                            Chunk lineSeparator = new Chunk("______________________________________________________________________________", COMENTARIO);
                            parrafoCalificacionComentarioNumerico.add(lineSeparator);
                        }
                        document.add(parrafoCalificacionComentarioNumerico);
                        break;

                    case "RespuestaPreguntaRelacionColumnas":
                        RespuestaPreguntaRelacionColumnas respuestaPreguntaRelacionColumnas = (RespuestaPreguntaRelacionColumnas) respuesta;
                        Paragraph parrafoPreguntaRelacionColumnas = new Paragraph();
                        Chunk enunciadoRespuestaPreguntaRelacionColumnas = new Chunk(contadorOrden + ") " + respuestaPreguntaRelacionColumnas.getPregunta().getStrEnunciado(), ENUNCIADO);
                        parrafoPreguntaRelacionColumnas.add(enunciadoRespuestaPreguntaRelacionColumnas);

                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempRC = this.getImagePdfTable(respuestaPreguntaRelacionColumnas);
                        if (imageTempRC != null) {
                            imageTempRC.setSpacingAfter(15);
                            parrafoPreguntaRelacionColumnas.add(imageTempRC);
                        } else {
                            parrafoPreguntaRelacionColumnas.add("\n");//Espaciado con el contenido luego del enunciado
                        }

                        parrafoPreguntaRelacionColumnas.setSpacingAfter(15);
                        document.add(parrafoPreguntaRelacionColumnas);

                        PdfPTable tableRelacionesColumnas = new PdfPTable(3);

                        float[] columnWidthsRC = {2.5f, 1f, 2.5f};
                        tableRelacionesColumnas.setWidths(columnWidthsRC);

                        String columnaIzquierdaRC = "";
                        String columnaDerechaRC = "";
                        String columnaCentral = "";

                        //Armado de columnas
                        for (RespuestaCombinacionRelacionColumnas elementoColumna : respuestaPreguntaRelacionColumnas.getColCombinaciones()) {
                            columnaIzquierdaRC = columnaIzquierdaRC + "#" + elementoColumna.getStrColumnaIzquierda() + "\n\n";
                            columnaDerechaRC = columnaDerechaRC + "#" + elementoColumna.getStrColumnaDerecha() + "\n\n";
                            columnaCentral = columnaCentral + "----------------" + "\n\n";
                        }

                        //Armado de la tabla
                        PdfPCell cell1RC = new PdfPCell(new Paragraph(columnaIzquierdaRC));
                        cell1RC.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell1RC.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell2RC = new PdfPCell(new Paragraph(columnaCentral));
                        cell2RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2RC.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell3RC = new PdfPCell(new Paragraph(columnaDerechaRC));
                        cell3RC.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell3RC.setBorder(Rectangle.NO_BORDER);

                        tableRelacionesColumnas.addCell(cell1RC);
                        tableRelacionesColumnas.addCell(cell2RC);
                        tableRelacionesColumnas.addCell(cell3RC);
                        document.add(tableRelacionesColumnas);
                        Paragraph parrafoCalificacionComentarioRelacionColumnas;
                        if (respuestaPreguntaRelacionColumnas.getCalificacion() == -1) {
                            parrafoCalificacionComentarioRelacionColumnas = new Paragraph(13, "\n" + "Calificacion: Sin calificar/" + respuestaPreguntaRelacionColumnas.getPregunta().getDblPuntaje() + "\n", BLUE);
                        } else {
                            parrafoCalificacionComentarioRelacionColumnas = new Paragraph(13, "\n" + "Calificacion: " + respuestaPreguntaRelacionColumnas.getCalificacion() + "/" + respuestaPreguntaRelacionColumnas.getPregunta().getDblPuntaje() + "\n", BLUE);
                        }

                        if (respuestaPreguntaRelacionColumnas.getStrComentario() != null && !respuestaPreguntaRelacionColumnas.getStrComentario().isEmpty()) {
                            parrafoCalificacionComentarioRelacionColumnas.add(new Chunk("Comentario Docente: " + respuestaPreguntaRelacionColumnas.getStrComentario() + "\n", COMENTARIO));
                        }
                        contadorOrden++;
                        if (this.resolucionExamen.getColRespuestas().size() >= contadorOrden) {
                            parrafoCalificacionComentarioRelacionColumnas.setSpacingAfter(15);
                            Chunk lineSeparator = new Chunk("______________________________________________________________________________", COMENTARIO);
                            parrafoCalificacionComentarioRelacionColumnas.add(lineSeparator);
                        }
                        document.add(parrafoCalificacionComentarioRelacionColumnas);

                        break;

                    case "RespuestaPreguntaVerdaderoFalso":
                        RespuestaPreguntaVerdaderoFalso respuestaPreguntaVerdaderoFalso = (RespuestaPreguntaVerdaderoFalso) respuesta;
                        Paragraph parrafoPreguntaVerdaderoFalso = new Paragraph();
                        Chunk enunciadoRespuestaPreguntaVerdaderoFalso = new Chunk(contadorOrden + ") " + respuestaPreguntaVerdaderoFalso.getPregunta().getStrEnunciado(), ENUNCIADO);
                        parrafoPreguntaVerdaderoFalso.add(enunciadoRespuestaPreguntaVerdaderoFalso);

                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempVF = this.getImagePdfTable(respuestaPreguntaVerdaderoFalso);
                        if (imageTempVF != null) {
                            parrafoPreguntaVerdaderoFalso.add(imageTempVF);
                        } else {
                            parrafoPreguntaVerdaderoFalso.add("\n");//Espaciado con el contenido luego del enunciado
                        }

                        parrafoPreguntaVerdaderoFalso.setSpacingAfter(15);
                        document.add(parrafoPreguntaVerdaderoFalso);

                        PdfPTable tableVerdaderoFalso = new PdfPTable(3);

                        float[] columnWidthsVF = {2.5f, 1f, 2.5f};
                        tableVerdaderoFalso.setWidths(columnWidthsVF);

                        String columnaIzquierdaVF = "[X] Verdadero";
                        String columnaDerechaVF = "[X] Falso";

                        if (respuestaPreguntaVerdaderoFalso.isBlnSeleccionoVerdadero()) {
                            columnaIzquierdaVF = "[X] Verdadero";
                            columnaDerechaVF = "[  ] Falso";
                        } else {
                            columnaIzquierdaVF = "[  ] Verdadero";
                            columnaDerechaVF = "[X] Falso";
                        }

                        //Armado de la tabla
                        PdfPCell cell1VF = new PdfPCell(new Paragraph(columnaIzquierdaVF));
                        cell1VF.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell1VF.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell2VF = new PdfPCell(new Paragraph("       "));
                        cell2VF.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2VF.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell3VF = new PdfPCell(new Paragraph(columnaDerechaVF));
                        cell3VF.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3VF.setBorder(Rectangle.NO_BORDER);

                        tableVerdaderoFalso.addCell(cell1VF);
                        tableVerdaderoFalso.addCell(cell2VF);
                        tableVerdaderoFalso.addCell(cell3VF);
                        document.add(tableVerdaderoFalso);
                        Paragraph parrafoCalificacionComentarioVF;
                        if (respuestaPreguntaVerdaderoFalso.getCalificacion() == -1) {
                            parrafoCalificacionComentarioVF = new Paragraph(13, "\n" + "Calificacion: Sin calificar/" + respuestaPreguntaVerdaderoFalso.getPregunta().getDblPuntaje() + "\n", BLUE);
                        } else {
                            parrafoCalificacionComentarioVF = new Paragraph(13, "\n" + "Calificacion: " + respuestaPreguntaVerdaderoFalso.getCalificacion() + "/" + respuestaPreguntaVerdaderoFalso.getPregunta().getDblPuntaje() + "\n", BLUE);
                        }

                        if (respuestaPreguntaVerdaderoFalso.getStrComentario() != null && !respuestaPreguntaVerdaderoFalso.getStrComentario().isEmpty()) {
                            parrafoCalificacionComentarioVF.add(new Chunk("Comentario Docente: " + respuestaPreguntaVerdaderoFalso.getStrComentario() + "\n", COMENTARIO));
                        }
                        contadorOrden++;
                        if (this.resolucionExamen.getColRespuestas().size() >= contadorOrden) {
                            parrafoCalificacionComentarioVF.setSpacingAfter(15);
                            Chunk lineSeparator = new Chunk("______________________________________________________________________________", COMENTARIO);
                            parrafoCalificacionComentarioVF.add(lineSeparator);
                        }
                        document.add(parrafoCalificacionComentarioVF);
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

    public String getResolucion() {
        return this.strFilePath;
    }

    public void borrarResolucionDeDisco() {
        File temporalFile = new File(this.strFilePath);
        if (temporalFile.delete()) {
            System.out.println("Deleted File");
        } else {
            System.out.println("File not deleted");
        }
    }

    private Chunk getNota() {
        Chunk chunk = new Chunk();
        boolean esAprobada = false;
        this.resolucionExamen.getExamen().setColPreguntas(this.getPreguntasExamen());
        try {
            esAprobada = this.resolucionExamen.estaAprobada();
        } catch (Exception ex) {
            Logger.getLogger(GestorGenerarReporteResolucion.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (this.resolucionExamen.esCorreccionCompleta()) {
            if (esAprobada) {
                chunk = new Chunk(String.valueOf("Nota: " + this.resolucionExamen.getCalificacionDeTrajo() +"%"), GREEN);
            } else {
                chunk = new Chunk(String.valueOf("Nota: " + this.resolucionExamen.getCalificacionDeTrajo()) +"%", RED);
            }
        } else {
            chunk = new Chunk("Nota: ----", BLUE);
        }
        return chunk;
    }

    private ArrayList<Pregunta> getPreguntasExamen() {
        DAOPreguntaExamen dAOPreguntaExamen = new DAOPreguntaExamen();
        return dAOPreguntaExamen.getPreguntasPorExamen(this.resolucionExamen.getExamen());
    }

    private String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    private PdfPTable getImagePdfTable(Respuesta respuesta) {

        DAOAdjunto dAOAdjunto = new DAOAdjunto();
        ArrayList<Object> colAdjuntos = new ArrayList<>();
        colAdjuntos.add(dAOAdjunto.recuperarAdjuntoExamen(respuesta.getPregunta().getIntPreguntaId()));
        respuesta.getPregunta().setColAdjuntos(colAdjuntos);

        PdfPTable tblImage = null;

        if (respuesta.getPregunta().getColAdjuntos().get(0) != null) {
            tblImage = new PdfPTable(1);
            Image image = null;
            byte[] bytesImagen = (byte[]) respuesta.getPregunta().getColAdjuntos().get(0);

            try {
                image = Image.getInstance(bytesImagen);
                image.scaleAbsolute(100f, 100f);
            } catch (BadElementException ex) {
                Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
            }

            PdfPCell cellImage = new PdfPCell(image, true);
            cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellImage.setBorder(Rectangle.NO_BORDER);
            tblImage.addCell(cellImage);
        }
        return tblImage;
    }
}
