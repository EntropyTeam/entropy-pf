package backend.reporte;

import backend.dao.diseños.DAOAdjunto;
import backend.dao.diseños.DAOInstitucion;
import backend.diseños.CombinacionRelacionColumnas;
import backend.diseños.DiseñoExamen;
import backend.diseños.OpcionMultipleOpcion;
import backend.diseños.Pregunta;
import backend.diseños.PreguntaMultipleOpcion;
import backend.diseños.PreguntaNumerica;
import backend.diseños.PreguntaRelacionColumnas;
import backend.diseños.PreguntaVerdaderoFalso;
import backend.diseños.TipoPregunta;
import static backend.reporte.GestorGenerarReporteResolucion.COMENTARIO;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author gaston
 */
public class GestorGenerarReporteDisenoExamen {

    private DiseñoExamen examenSeleccionado;
    private String path = "ReporteExamenPrueba.pdf";

    //Estilos de texto
    public static final Font RED = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED);
    public static final Font BLUE = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
    public static final Font GREEN = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN);
    public static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
    public static final Font ENUNCIADO = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.BLACK);
    public static final Font CHOISE = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.ORANGE);
    public static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
    public static final Font COMENTARIO = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, BaseColor.DARK_GRAY);
    
    public GestorGenerarReporteDisenoExamen(DiseñoExamen examenSeleccionado) {
        this.examenSeleccionado = examenSeleccionado;
        generarReporteDiseñoExamen();
    }

    public void generarReporteDiseñoExamen() {

        Document document = new Document();

        try {
            int contadorOrden = 1;
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            //Tabla Encabezado Encabezado
            PdfPTable tablaEncabezado = new PdfPTable(2); // 2 columns.
            float[] columnWidths = {15f, 100f};
            tablaEncabezado.setWidths(columnWidths);
            tablaEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            //Contenido Encabezado
            PdfPTable contenidoEncabezado = new PdfPTable(1);
            contenidoEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPCell cellTitulo = new PdfPCell(new Paragraph(this.examenSeleccionado.getStrNombre()));
            cellTitulo.setBorder(Rectangle.NO_BORDER);
            cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
            contenidoEncabezado.addCell(cellTitulo);

            PdfPTable contenidoEncabezadoInstFecha = new PdfPTable(2);
            DAOInstitucion dAOInstitucion = new DAOInstitucion();
            this.examenSeleccionado.getCurso().setInstitucion(dAOInstitucion.buscarInstitucion(this.examenSeleccionado.getCurso().getIntCursoId()));
            String institucion = dAOInstitucion.buscarInstitucion(this.examenSeleccionado.getCurso().getIntCursoId()).getStrNombre();
            PdfPCell cellInst = new PdfPCell(new Paragraph(institucion));
            cellInst.setBorder(Rectangle.NO_BORDER);
            PdfPCell cellFecha = new PdfPCell(new Paragraph("__/__/____"));
            cellFecha.setBorder(Rectangle.NO_BORDER);
            cellFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
            contenidoEncabezadoInstFecha.addCell(cellInst);
            contenidoEncabezadoInstFecha.addCell(cellFecha);
            contenidoEncabezado.addCell(contenidoEncabezadoInstFecha);
            contenidoEncabezadoInstFecha.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPTable contenidoEncabezadoNombreNota = new PdfPTable(2);
            PdfPCell cellNombre = new PdfPCell(new Paragraph("Alumno:                             "));
            cellNombre.setBorder(Rectangle.NO_BORDER);
            PdfPCell cellNota = new PdfPCell(new Paragraph("Nota:_________"));
            cellNota.setBorder(Rectangle.NO_BORDER);
            cellNota.setHorizontalAlignment(Element.ALIGN_RIGHT);
            contenidoEncabezadoNombreNota.addCell(cellNombre);
            contenidoEncabezadoNombreNota.addCell(cellNota);
            contenidoEncabezado.addCell(contenidoEncabezadoNombreNota);
            contenidoEncabezadoNombreNota.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            //Imagen encabezado 
            Image imagenLogoCurso = null;
            byte[] bytesImagen = (byte[]) this.examenSeleccionado.getCurso().getInstitucion().getImgLogo();
            if (this.examenSeleccionado.getCurso().getInstitucion().getImgLogo() == null) {

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

            for (Pregunta pregunta : this.examenSeleccionado.getColPreguntas()) {

                switch (pregunta.getIntTipo()) {

                    case TipoPregunta.DESARROLLAR:
                        Paragraph parrafoPreguntaDesarrollar = new Paragraph(13, contadorOrden + ") " + pregunta.getStrEnunciado() + "\n");
                        
                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempDesarrollo = this.getImagePdfTable(pregunta);
                        if (imageTempDesarrollo != null) {
                            imageTempDesarrollo.setSpacingAfter(15);
                            parrafoPreguntaDesarrollar.add(imageTempDesarrollo);                            
                        }
                        
                        parrafoPreguntaDesarrollar.add("______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
                        parrafoPreguntaDesarrollar.setSpacingBefore(30);
                        parrafoPreguntaDesarrollar.setSpacingAfter(15);
                        document.add(parrafoPreguntaDesarrollar);
                        contadorOrden++;
//                        if (this.examenSeleccionado.getColPreguntas().size() >= contadorOrden) {
//                            parrafoPreguntaDesarrollar.setSpacingAfter(15);
//                            Chunk lineSeparator = new Chunk("____________________________________________________________________________________", COMENTARIO);
//                            parrafoPreguntaDesarrollar.add(lineSeparator);
//                        }
//                        document.add(parrafoPreguntaDesarrollar);
                        break;

                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion preguntaMultipleOpcion = (PreguntaMultipleOpcion) pregunta;
                        Paragraph parrafoPreguntaMultipleOpcion = new Paragraph(13, contadorOrden + ") " + preguntaMultipleOpcion.getStrEnunciado() + "\n");
                        
                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempMO = this.getImagePdfTable(preguntaMultipleOpcion);
                        if (imageTempMO != null) {
                            imageTempMO.setSpacingAfter(15);
                            parrafoPreguntaMultipleOpcion.add(imageTempMO);                            
                        }
                        
                        parrafoPreguntaMultipleOpcion.setSpacingBefore(30);
                        parrafoPreguntaMultipleOpcion.setSpacingAfter(15);
                        document.add(parrafoPreguntaMultipleOpcion);

                        List orderedList = new List(List.ORDERED);//Ver que pasa si es ordenada o desordenada.
                        for (OpcionMultipleOpcion opcionMultipleOpcion : preguntaMultipleOpcion.getColOpciones()) {
                            orderedList.add(new ListItem(opcionMultipleOpcion.getStrRespuesta()));
                        }
                        document.add(orderedList);
                        contadorOrden++;
//                        if (this.examenSeleccionado.getColPreguntas().size() >= contadorOrden) {
//                            parrafoPreguntaMultipleOpcion.setSpacingAfter(15);
//                            Chunk lineSeparator = new Chunk("____________________________________________________________________________________", COMENTARIO);
//                            parrafoPreguntaMultipleOpcion.add(lineSeparator);
//                        }
//                        document.add(parrafoPreguntaMultipleOpcion);
                        break;

                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica preguntaNumerica = (PreguntaNumerica) pregunta;
                        Paragraph parrafoPreguntaNumerica = new Paragraph(13, contadorOrden + ") " + preguntaNumerica.getStrEnunciado() + "\n");
                        
                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempNumerica = this.getImagePdfTable(preguntaNumerica);
                        if (imageTempNumerica != null) {
                            imageTempNumerica.setSpacingAfter(15);
                            parrafoPreguntaNumerica.add(imageTempNumerica);                            
                        }
                        
                        parrafoPreguntaNumerica.setSpacingBefore(30);
                        parrafoPreguntaNumerica.setSpacingAfter(15);
                        document.add(parrafoPreguntaNumerica);

                        Paragraph parrafoNumero = new Paragraph("Respuesta: _______________");
                        document.add(parrafoNumero);
                        contadorOrden++;
//                        if (this.examenSeleccionado.getColPreguntas().size() >= contadorOrden) {
//                            parrafoPreguntaNumerica.setSpacingAfter(15);
//                            Chunk lineSeparator = new Chunk("____________________________________________________________________________________", COMENTARIO);
//                            parrafoPreguntaNumerica.add(lineSeparator);
//                        }
//                        document.add(parrafoPreguntaNumerica);
                        break;

                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas preguntaRelacionColumnas = (PreguntaRelacionColumnas) pregunta;
                        Paragraph parrafoPreguntaRelacionColumnas = new Paragraph(13, contadorOrden + ") " + preguntaRelacionColumnas.getStrEnunciado() + "\n");
                        
                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempRC = this.getImagePdfTable(preguntaRelacionColumnas);
                        if (imageTempRC != null) {
                            imageTempRC.setSpacingAfter(15);
                            parrafoPreguntaRelacionColumnas.add(imageTempRC);                            
                        }
                        
                        parrafoPreguntaRelacionColumnas.setSpacingBefore(30);
                        parrafoPreguntaRelacionColumnas.setSpacingAfter(15);
                        document.add(parrafoPreguntaRelacionColumnas);

                        PdfPTable tableRelacionesColumnas = new PdfPTable(3);

                        float[] columnWidthsRC = {2.5f, 1f, 2.5f};
                        tableRelacionesColumnas.setWidths(columnWidthsRC);

                        String columnaIzquierdaRC = "";
                        String columnaDerechaRC = "";
                        String columnaCentral = "";

                        //Armado de columnas
                        for (CombinacionRelacionColumnas elementoColumna : preguntaRelacionColumnas.getColCombinaciones()) {
                            columnaIzquierdaRC = columnaIzquierdaRC + elementoColumna.getStrColumnaIzquierda()  + " #" + "\n\n";
                            columnaDerechaRC = columnaDerechaRC + "# " + elementoColumna.getStrColumnaDerecha() + "\n\n";
                            columnaCentral = columnaCentral + "                " + "\n\n";
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
                        contadorOrden++;
//                        if (this.examenSeleccionado.getColPreguntas().size() >= contadorOrden) {
//                            parrafoPreguntaRelacionColumnas.setSpacingAfter(15);
//                            Chunk lineSeparator = new Chunk("____________________________________________________________________________________", COMENTARIO);
//                            parrafoPreguntaRelacionColumnas.add(lineSeparator);
//                        }
//                        document.add(parrafoPreguntaRelacionColumnas);
                        break;

                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso preguntaVerdaderoFalso = (PreguntaVerdaderoFalso) pregunta;
                        Paragraph parrafoPreguntaVerdaderoFalso = new Paragraph(13, contadorOrden + ") " + preguntaVerdaderoFalso.getStrEnunciado() + "\n");
                        
                        //Agrego la imagen de la respuesta.
                        PdfPTable imageTempVF = this.getImagePdfTable(preguntaVerdaderoFalso);
                        if (imageTempVF != null) {
                            parrafoPreguntaVerdaderoFalso.add(imageTempVF);                            
                        }
                        
                        parrafoPreguntaVerdaderoFalso.setSpacingBefore(30);
                        parrafoPreguntaVerdaderoFalso.setSpacingAfter(15);
                        document.add(parrafoPreguntaVerdaderoFalso);

                        PdfPTable tableVerdaderoFalso = new PdfPTable(3);

                        float[] columnWidthsVF = {2.5f, 1f, 2.5f};
                        tableVerdaderoFalso.setWidths(columnWidthsVF);

                        String columnaIzquierdaVF = "[ ] Verdadero";
                        String columnaDerechaVF = "[ ] Falso";

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
                        contadorOrden++;
//                        if (this.examenSeleccionado.getColPreguntas().size() >= contadorOrden) {
//                            parrafoPreguntaVerdaderoFalso.setSpacingAfter(15);
//                            Chunk lineSeparator = new Chunk("____________________________________________________________________________________", COMENTARIO);
//                            parrafoPreguntaVerdaderoFalso.add(lineSeparator);
//                        }
//                        document.add(parrafoPreguntaVerdaderoFalso);
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

    public String getPath() {
        return path;
    }
    
    private PdfPTable getImagePdfTable(Pregunta pregunta) {
        
        DAOAdjunto dAOAdjunto = new DAOAdjunto();
        ArrayList<Object> colAdjuntos = new ArrayList<>();
        colAdjuntos.add(dAOAdjunto.recuperarAdjuntoDiseño(pregunta.getIntPreguntaId()));
        pregunta.setColAdjuntos(colAdjuntos);
        
        PdfPTable tblImage = null;
        
        if (pregunta.getColAdjuntos().get(0) != null) {
            tblImage = new PdfPTable(1);
            Image image = null;
            byte[] bytesImagen = (byte[]) pregunta.getColAdjuntos().get(0);
            
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
