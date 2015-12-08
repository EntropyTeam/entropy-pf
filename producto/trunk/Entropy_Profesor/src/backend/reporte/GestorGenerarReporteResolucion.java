package backend.reporte;

import backend.dao.diseños.DAOCurso;
import backend.dao.diseños.DAOInstitucion;
import backend.dao.diseños.DAOPregunta;
import backend.dao.examenes.DAOExamen;
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
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.font.TextAttribute;
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

    private Resolucion resolucionExamen;
    private String strFilePath = "ReporteResolucionPrueba.pdf";

    //Estilos de texto
    public static final Font RED = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED);
    public static final Font BLUE = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
    public static final Font GREEN = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN);
    public static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
    public static final Font COMENTARIO = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);

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

            //Tabla Encabezado Encabezado
            PdfPTable tablaEncabezado = new PdfPTable(2); // 2 columns.
            float[] columnWidths = {10f, 100f};
            tablaEncabezado.setWidths(columnWidths);
            tablaEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            //Contenido Encabezado
            PdfPTable contenidoEncabezado = new PdfPTable(1);
            contenidoEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            String titulo = this.resolucionExamen.getExamen().getStrNombre();
            PdfPCell cellTitulo = new PdfPCell(new Paragraph(titulo, TITULO));
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
            contenidoEncabezado.addCell(cellTitulo);

            PdfPTable contenidoEncabezadoInstFecha = new PdfPTable(2);
            DAOInstitucion dAOInstitucion = new DAOInstitucion();
            this.resolucionExamen.getExamen().getCurso().setInstitucion(dAOInstitucion.buscarInstitucion(this.resolucionExamen.getExamen().getCurso().getIntCursoId()));
            String institucion = dAOInstitucion.buscarInstitucion(this.resolucionExamen.getExamen().getCurso().getIntCursoId()).getStrNombre();
            PdfPCell cellInst = new PdfPCell(new Paragraph(institucion));
            cellInst.setBorder(Rectangle.NO_BORDER);
            String fecha = this.getDate(this.resolucionExamen.getExamen().getDteFecha());
            PdfPCell cellFecha = new PdfPCell(new Paragraph("Fecha: " + fecha));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            cellFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
            contenidoEncabezadoInstFecha.addCell(cellInst);
            contenidoEncabezadoInstFecha.addCell(cellFecha);
            contenidoEncabezado.addCell(contenidoEncabezadoInstFecha);
            contenidoEncabezadoInstFecha.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPTable contenidoEncabezadoNombreNota = new PdfPTable(2);
            String Nombre = this.resolucionExamen.getAlumno().getStrNombre();
            String Apellido = this.resolucionExamen.getAlumno().getStrApellido();
            PdfPCell cellNombre = new PdfPCell(new Paragraph("Alumno: " + Apellido + " " + Nombre));
            cellNombre.setBorder(Rectangle.NO_BORDER);
            PdfPCell cellNota = new PdfPCell(new Paragraph(this.getNota()));
            cellNota.setBorder(Rectangle.NO_BORDER);
            cellNota.setHorizontalAlignment(Element.ALIGN_RIGHT);
            contenidoEncabezadoNombreNota.addCell(cellNombre);
            contenidoEncabezadoNombreNota.addCell(cellNota);
            contenidoEncabezado.addCell(contenidoEncabezadoNombreNota);
            contenidoEncabezadoNombreNota.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            //Imagen encabezado 
            Image imagenLogoCurso = null;
            byte[] bytesImagen = (byte[]) this.resolucionExamen.getExamen().getCurso().getInstitucion().getImgLogo();
            if (this.resolucionExamen.getExamen().getCurso().getInstitucion().getImgLogo() == null) {

                try {
                    imagenLogoCurso = Image.getInstance(getClass().getResource("/frontend/imagenes/ic_examen_default.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                try {
                    imagenLogoCurso = Image.getInstance(bytesImagen);
                } catch (BadElementException ex) {
                    Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            PdfPCell cellLogo = new PdfPCell(imagenLogoCurso, true);//Con el True en este metodo y sin setear un tamaño para la imagen esta ocupa toda la celda, ver que pasa cuando la celda se hace mas grande por las columnas del lado
            cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellLogo.setBorder(Rectangle.NO_BORDER);

            tablaEncabezado.addCell(cellLogo);
            tablaEncabezado.addCell(contenidoEncabezado);
            tablaEncabezado.setSpacingAfter(25);
            tablaEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            document.add(tablaEncabezado);

            for (Respuesta respuesta : this.resolucionExamen.getColRespuestas()) {

                String clase = respuesta.getClass().getSimpleName();
                switch (clase) {

                    case "RespuestaDesarrollo":
                        RespuestaDesarrollo respuestaDesarrollo = (RespuestaDesarrollo) respuesta;
                        Paragraph parrafoRespuestaDesarrollar = new Paragraph(13, contadorOrden + ") " + respuesta.getPregunta().getStrEnunciado() + "\n\n");
                        parrafoRespuestaDesarrollar.add("   " + respuestaDesarrollo.getStrRespuesta() + "\n");
                        parrafoRespuestaDesarrollar.add(new Chunk("Calificacion: " + respuestaDesarrollo.getCalificacion() + "\n", BLUE));
                        if (respuestaDesarrollo.getStrComentario() != null) {
                            parrafoRespuestaDesarrollar.add(new Chunk("Comentario Docente: " + respuestaDesarrollo.getStrComentario(), COMENTARIO));
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
                        ArrayList<OpcionMultipleOpcion> colOpcionMultipleOpcionCorrectas = new ArrayList<>();

                        for (OpcionMultipleOpcion opcionMultipleOpcionCorrecta : respuestaPreguntaMultipleOpcion.getPregunta().getColOpciones()) {
                            if (opcionMultipleOpcionCorrecta.isBlnEsVerdadera()) {
                                colOpcionMultipleOpcionCorrectas.add(opcionMultipleOpcionCorrecta);
                            }
                        }

                        List orderedList = new List(List.ORDERED);//Ver que pasa si es ordenada o desordenada.
                        for (RespuestaOpcionMultipleOpcion respuestaOpcionMultipleOpcion : respuestaPreguntaMultipleOpcion.getColeccionOpciones()) {
                            OpcionMultipleOpcion opcionTemporal = new OpcionMultipleOpcion();
                            opcionTemporal.setStrRespuesta(respuestaOpcionMultipleOpcion.getStrRespuesta());
                            if (colOpcionMultipleOpcionCorrectas.contains(opcionTemporal) && respuestaOpcionMultipleOpcion.isBlnEsMarcada()) {
                                orderedList.add(new ListItem(respuestaOpcionMultipleOpcion.getStrRespuesta() + "✔"));
                            } else {
                                if (respuestaOpcionMultipleOpcion.isBlnEsMarcada()) {
                                    orderedList.add(new ListItem(respuestaOpcionMultipleOpcion.getStrRespuesta() + "X"));
                                } else {
                                    orderedList.add(new ListItem(respuestaOpcionMultipleOpcion.getStrRespuesta()));
                                }
                            }
                        }
                        document.add(orderedList);

                        Paragraph parrafoCalificacionComentarioMultipleOpcion = new Paragraph(13, "Calificacion: " + respuestaPreguntaMultipleOpcion.getCalificacion() + "\n", BLUE);
                           if (respuestaPreguntaMultipleOpcion.getStrComentario() != null) {
                            parrafoCalificacionComentarioMultipleOpcion.add(new Chunk("Comentario Docente: " + respuestaPreguntaMultipleOpcion.getStrComentario(), COMENTARIO));
                        }
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

                        Paragraph parrafoCalificacionComentarioNumerico = new Paragraph(13, "Calificacion: " + respuestaPreguntaNumerica.getCalificacion() + "\n", BLUE);
                         if (respuestaPreguntaNumerica.getStrComentario() != null) {
                            parrafoPreguntaNumerica.add(new Chunk("Comentario Docente: " + respuestaPreguntaNumerica.getStrComentario(), COMENTARIO));
                        }
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
                        String columnaCentral = "";

                        //Armado de columnas
                        for (RespuestaCombinacionRelacionColumnas elementoColumna : respuestaPreguntaRelacionColumnas.getColCombinaciones()) {
                            columnaIzquierdaRC = columnaIzquierdaRC + "#" + elementoColumna.getStrColumnaIzquierda() + "\n\n";
                            columnaDerechaRC = columnaDerechaRC + "#" + elementoColumna.getStrColumnaDerecha() + "\n\n";
                            columnaCentral = columnaCentral + "----------------" + "\n\n";
                        }

                        //Armado de la tabla
                        PdfPCell cell1RC = new PdfPCell(new Paragraph(columnaIzquierdaRC));
                        cell1RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell1RC.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell2RC = new PdfPCell(new Paragraph(columnaCentral));
                        cell2RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2RC.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell3RC = new PdfPCell(new Paragraph(columnaDerechaRC));
                        cell3RC.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3RC.setBorder(Rectangle.NO_BORDER);

                        tableRelacionesColumnas.addCell(cell1RC);
                        tableRelacionesColumnas.addCell(cell2RC);
                        tableRelacionesColumnas.addCell(cell3RC);
                        document.add(tableRelacionesColumnas);

                        Paragraph parrafoCalificacionComentarioRelacionColumnas = new Paragraph(13, "Calificacion: " + respuestaPreguntaRelacionColumnas.getCalificacion() + "\n", BLUE);
                        if (respuestaPreguntaRelacionColumnas.getStrComentario() != null) {
                            parrafoCalificacionComentarioRelacionColumnas.add(new Chunk("Comentario Docente: " + respuestaPreguntaRelacionColumnas.getStrComentario(), COMENTARIO));
                        }
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

                        PdfPTable tableVerdaderoFalso = new PdfPTable(3);

                        float[] columnWidthsVF = {2.5f, 1f, 2.5f};
                        tableVerdaderoFalso.setWidths(columnWidthsVF);
                        
                        String columnaIzquierdaVF = "[X] Verdadero";
                        String columnaDerechaVF = "[X] Falso";

                        if (respuestaPreguntaVerdaderoFalso.isBlnSeleccionoVerdadero()) {
                            columnaIzquierdaVF = "[X] Verdadero";
                            columnaDerechaVF = "[ ] Falso";
                        } else {
                            columnaIzquierdaVF = "[ ] Verdadero";
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

                        Paragraph parrafoCalificacionComentarioVF = new Paragraph(13, "Calificacion: " + respuestaPreguntaVerdaderoFalso.getCalificacion() + "\n", BLUE);
                        if (respuestaPreguntaVerdaderoFalso.getStrComentario() != null) {
                            parrafoCalificacionComentarioVF.add(new Chunk("Comentario Docente: " + respuestaPreguntaVerdaderoFalso.getStrComentario(), COMENTARIO));
                        }
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

    public Chunk getNota() {
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
                chunk = new Chunk(String.valueOf("Nota: " + this.resolucionExamen.getCalificacion()), GREEN);
            } else {
                chunk = new Chunk(String.valueOf("Nota: " + this.resolucionExamen.getCalificacion()), RED);
            }
        } else {
            chunk = new Chunk("Nota: ----", BLUE);
        }
        return chunk;
    }

    public ArrayList<Pregunta> getPreguntasExamen() {
        DAOPreguntaExamen dAOPreguntaExamen = new DAOPreguntaExamen();
        return dAOPreguntaExamen.getPreguntasPorExamen(this.resolucionExamen.getExamen());
    }

    public String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
}
