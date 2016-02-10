package backend.reporte;

import backend.diseños.CombinacionRelacionColumnas;
import backend.diseños.DiseñoExamen;
import backend.diseños.OpcionMultipleOpcion;
import backend.diseños.Pregunta;
import backend.diseños.PreguntaMultipleOpcion;
import backend.diseños.PreguntaNumerica;
import backend.diseños.PreguntaRelacionColumnas;
import backend.diseños.PreguntaVerdaderoFalso;
import backend.diseños.TipoPregunta;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author gaston
 */
public final class GestorGenerarReporteDisenoExamen {

    private final DiseñoExamen examenSeleccionado;
    private final String path = "ReporteExamenPrueba.pdf";

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

            //LOGO E INSTITUCION
            PdfPTable tablaEncabezado = new PdfPTable(2); // 2 columns.
            float[] columnWidths = {40f, 350f};
            tablaEncabezado.setTotalWidth(columnWidths);
            tablaEncabezado.setLockedWidth(true);
            tablaEncabezado.setWidths(columnWidths);
            tablaEncabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            String institucion = (this.examenSeleccionado.getCurso() != null
                    && this.examenSeleccionado.getCurso().getIntCursoId() > 0)
                            ? this.examenSeleccionado.getCurso().getInstitucion().getStrNombre() : "";
            PdfPCell cellInst = new PdfPCell(new Paragraph(institucion, TITULO));
            cellInst.setBorder(Rectangle.NO_BORDER);
            cellInst.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellInst.setVerticalAlignment(Element.ALIGN_MIDDLE);

            //INSTITUCION
            if (this.examenSeleccionado.getCurso() != null
                    && this.examenSeleccionado.getCurso().getIntCursoId() > 0
                    && this.examenSeleccionado.getCurso().getInstitucion().getImgLogo() != null) {
                try {
                    byte[] bytesImagen = (byte[]) this.examenSeleccionado.getCurso().getInstitucion().getImgLogo();
                    Image imagenLogoCurso = Image.getInstance(bytesImagen);
                    PdfPCell cellLogo = new PdfPCell(imagenLogoCurso, true);
                    cellLogo.setBorder(Rectangle.NO_BORDER);
                    tablaEncabezado.addCell(cellLogo);
                    cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
                } catch (BadElementException | IOException ex) {
                    Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            tablaEncabezado.addCell(cellInst);
            document.add(tablaEncabezado);

            //TITULO 
            PdfPTable tablaTituloExamen = new PdfPTable(1);
            tablaTituloExamen.setTotalWidth(PageSize.A4.getWidth());
            tablaTituloExamen.setLockedWidth(true);
            tablaTituloExamen.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            PdfPCell cellNombreExamen = new PdfPCell(new Paragraph(this.examenSeleccionado.getStrNombre(), TITULO));
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

            String nombreCurso = (this.examenSeleccionado.getCurso() != null
                    && this.examenSeleccionado.getCurso().getIntCursoId() > 0)
                            ? this.examenSeleccionado.getCurso().getStrNombre() : "";
            PdfPCell cellCurso = new PdfPCell(new Paragraph("Curso: " + nombreCurso));
            cellCurso.setBorder(Rectangle.NO_BORDER);
            cellCurso.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCurso.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cursoFecha.addCell(cellCurso);

            PdfPCell cellFecha = new PdfPCell(new Paragraph("Fecha: "));
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

            PdfPCell cellNombreAlumno = new PdfPCell(new Paragraph("Alumno: "));
            cellNombreAlumno.setBorder(Rectangle.NO_BORDER);
            cellNombreAlumno.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNombreAlumno.setVerticalAlignment(Element.ALIGN_MIDDLE);
            alumnoNota.addCell(cellNombreAlumno);

            PdfPCell cellNota = new PdfPCell(new Paragraph("Nota: _____"));
            cellNota.setBorder(Rectangle.NO_BORDER);
            cellNota.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNota.setVerticalAlignment(Element.ALIGN_MIDDLE);
            alumnoNota.addCell(cellNota);

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

                        List orderedList = new List(List.UNORDERED);//Ver que pasa si es ordenada o desordenada.
                        for (OpcionMultipleOpcion opcionMultipleOpcion : aleatorizarColeccion(preguntaMultipleOpcion.getColOpciones())) {
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
                        for (CombinacionRelacionColumnas elementoColumna : aleatorizarColeccionColumnas(preguntaRelacionColumnas.getColCombinaciones())) {
                            columnaIzquierdaRC = columnaIzquierdaRC + elementoColumna.getStrColumnaIzquierda() + " #" + "\n\n";
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

            System.out.println("PDF de diseño generado correctamente.");
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

        PdfPTable tblImage = null;

        if (!pregunta.getColAdjuntos().isEmpty()) {
            tblImage = new PdfPTable(1);
            Image image = null;
            byte[] bytesImagen = (byte[]) pregunta.getColAdjuntos().get(0);
            try {
                image = Image.getInstance(bytesImagen);
                image.scaleAbsolute(100f, 100f);
                //image.scalePercent((float)image.getWidth()/40);
            } catch (BadElementException | IOException ex) {
                Logger.getLogger(GestorGenerarReporteDisenoExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
            PdfPCell cellImage = new PdfPCell(image, false);
            cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellImage.setBorder(Rectangle.NO_BORDER);
            tblImage.addCell(cellImage);
            tblImage.setTotalWidth(PageSize.A4.getWidth());
        }
        return tblImage;
    }

    /**
     * Desordena la lista que contiene las respuestas a mostrarse..
     *
     * @param original lista a desordenar.
     * @return lista desordenada.
     */
    private ArrayList<OpcionMultipleOpcion> aleatorizarColeccion(ArrayList<OpcionMultipleOpcion> original) {
        int i = original.size() - 1;
        while (i > -1) {
            int j = (int) Math.floor(Math.random() * (i + 1));
            OpcionMultipleOpcion tmp = original.get(i);
            original.set(i, original.get(j));
            original.set(j, tmp);
            i--;
        }
        return original;
    }

    /**
     * Desordena la lista que contiene las respuestas a mostrarse..
     *
     * @param original lista a desordenar.
     * @return lista desordenada.
     */
    private ArrayList<CombinacionRelacionColumnas> aleatorizarColeccionColumnas(ArrayList<CombinacionRelacionColumnas> original) {
        int i = original.size() - 1;
        while (i > -1) {
            int j = (int) Math.floor(Math.random() * (i + 1));
            String tmp = original.get(i).getStrColumnaDerecha();
            original.get(i).setStrColumnaDerecha(original.get(j).getStrColumnaDerecha());
            original.get(j).setStrColumnaDerecha(tmp);
            i--;
        }
        return original;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(java.awt.Image img) {

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
