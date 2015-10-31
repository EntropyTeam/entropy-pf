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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 *
 * @author gaston
 */
public class GestorGenerarReporteDiseñoExamen {

    private DiseñoExamen examenSeleccionado;
    private String path = "ReporteExamenPrueba.pdf";

    public GestorGenerarReporteDiseñoExamen(DiseñoExamen examenSeleccionado) {
        this.examenSeleccionado = examenSeleccionado;
        generarReporteDiseñoExamen();
    }

    public void generarReporteDiseñoExamen() {

        Document document = new Document();

        try {
            int contadorOrden = 1;
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            //Titulo Encabezado
            Paragraph paragraph = new Paragraph(this.examenSeleccionado.getStrNombre());
            paragraph.setSpacingAfter(25);
            paragraph.setSpacingBefore(25);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setIndentationLeft(50);
            paragraph.setIndentationRight(50);
            document.add(paragraph);

            for (Pregunta pregunta : this.examenSeleccionado.getColPreguntas()) {

                switch (pregunta.getIntTipo()) {
                    
                    case TipoPregunta.DESARROLLAR:
                        Paragraph parrafoPreguntaDesarrollar = new Paragraph(13, contadorOrden + ") " + pregunta.getStrEnunciado() + "\n");
                        parrafoPreguntaDesarrollar.add("______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
                        parrafoPreguntaDesarrollar.setSpacingBefore(30);
                        document.add(parrafoPreguntaDesarrollar);
                        contadorOrden++;
                        break;
                        
                    case TipoPregunta.MULTIPLE_OPCION:
                        PreguntaMultipleOpcion preguntaMultipleOpcion = (PreguntaMultipleOpcion) pregunta;
                        Paragraph parrafoPreguntaMultipleOpcion = new Paragraph(13, contadorOrden + ") " + preguntaMultipleOpcion.getStrEnunciado() + "\n");
                        parrafoPreguntaMultipleOpcion.setSpacingAfter(15);
                        parrafoPreguntaMultipleOpcion.setSpacingBefore(30);
                        document.add(parrafoPreguntaMultipleOpcion);
                        
                        List orderedList = new List(List.ORDERED);//Ver que pasa si es ordenada o desordenada.
                        for (OpcionMultipleOpcion opcionMultipleOpcion : preguntaMultipleOpcion.getColOpciones()) {
                            orderedList.add(new ListItem(opcionMultipleOpcion.getStrRespuesta()));
                        }
                        document.add(orderedList);
                        contadorOrden++;
                        break;
                        
                    case TipoPregunta.NUMERICA:
                        PreguntaNumerica preguntaNumerica = (PreguntaNumerica) pregunta;
                        Paragraph parrafoPreguntaNumerica = new Paragraph(13, contadorOrden + ") " + preguntaNumerica.getStrEnunciado() + "\n");
                        parrafoPreguntaNumerica.setSpacingAfter(15);
                        parrafoPreguntaNumerica.setSpacingBefore(30);
                        document.add(parrafoPreguntaNumerica);
                        
                        Paragraph parrafoNumero = new Paragraph("Respuesta: _______________");
                        document.add(parrafoNumero);
                        contadorOrden++;
                        break;
                        
                    case TipoPregunta.RELACION_COLUMNAS:
                        PreguntaRelacionColumnas preguntaRelacionColumnas = (PreguntaRelacionColumnas) pregunta;
                        Paragraph parrafoPreguntaRelacionColumnas = new Paragraph(13, contadorOrden + ") " + preguntaRelacionColumnas.getStrEnunciado() + "\n");
                        parrafoPreguntaRelacionColumnas.setSpacingAfter(15);
                        parrafoPreguntaRelacionColumnas.setSpacingBefore(30);
                        document.add(parrafoPreguntaRelacionColumnas);
                        
                        PdfPTable tableRelacionesColumnas = new PdfPTable(3);

                        float[] columnWidthsRC = {2.5f, 1f, 2.5f};
                        tableRelacionesColumnas.setWidths(columnWidthsRC);
                        
                        String columnaIzquierdaRC = "";
                        String columnaDerechaRC = "";
                        
                        //Armado de columnas
                        for (CombinacionRelacionColumnas elementoColumna : preguntaRelacionColumnas.getColCombinaciones()) {
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
                        contadorOrden++;
                        break;
                        
                    case TipoPregunta.VERDADERO_FALSO:
                        PreguntaVerdaderoFalso preguntaVerdaderoFalso = (PreguntaVerdaderoFalso) pregunta;
                        Paragraph parrafoPreguntaVerdaderoFalso = new Paragraph(13, contadorOrden + ") " + preguntaVerdaderoFalso.getStrEnunciado() + "\n");
                        parrafoPreguntaVerdaderoFalso.setSpacingAfter(15);
                        parrafoPreguntaVerdaderoFalso.setSpacingBefore(30);
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
}