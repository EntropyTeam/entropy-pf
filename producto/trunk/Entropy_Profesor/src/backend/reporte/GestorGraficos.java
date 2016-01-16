package backend.reporte;

import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.swing.ImageIcon;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 * Clase encargada de producir los gráficos para estadísticas.
 *
 * @author Denise
 */
public class GestorGraficos {

    private JFreeChart lastChart;

    private final ArrayList<Resolucion> colResoluciones;

    private int intTotalResoluciones;
    private int intTotalAprobados;
    private int intTotalAnulados;
    private int intTotalRespuestas;

    private ArrayList<Double> lstPorcentajeCalificaciones;
    private ArrayList<Categoria> lstTemas;
    private ArrayList<Categoria> lstDificultad;
    private ArrayList<Categoria> lstPreguntas;

    private boolean esProcesarSoloAprobadas = false;
    private boolean esProcesarSoloDesaprobadas = false;

    private String[][] matResultados;

    public GestorGraficos(ArrayList<Resolucion> colResoluciones) {
        this.colResoluciones = colResoluciones;
        this.intTotalRespuestas = colResoluciones.get(0).getExamen().getColPreguntas().size();
        matResultados = new String[colResoluciones.size()][intTotalRespuestas+2];
        
        procesarResoluciones();
    }
    
    public Object[][] crearMatrizRespuestas(){
        boolean blnAnulada;
        for (int j = 0; j < colResoluciones.size(); j++) {
            Resolucion resolucion = colResoluciones.get(j);
            matResultados[j][0] = resolucion.getAlumno().toString();
            matResultados[j][1] = String.format("%.2f", resolucion.getPorcentajeAprobacion()) + "%";
            blnAnulada = resolucion.isBlnAnulada();
            for (int i = 0; i < resolucion.getColRespuestas().size(); i++){
                Respuesta respuesta = resolucion.getColRespuestas().get(i);
                if (blnAnulada) {
                    matResultados[j][i+2] = "Anulada";
                } else {
                    int esCorrecta = respuesta.esCorrecta();
                    //Datos para filtros de tema
                    switch (esCorrecta) {
                        case 2: // Contestó bien la pregunta
                            matResultados[j][i+2] = "Incompleta";
                            break;
                        case 1: // Contestó bien la pregunta
                            matResultados[j][i+2] = "Correcta";
                            break;
                        case 0: // Contestó mal
                            matResultados[j][i+2] = "Incorrecta";
                            break;
                        case -1: // No es corrección automática
                            matResultados[j][i+2] = "Sin calificar";
                            break;
                    }
                }
            }
        }
        return matResultados;
    }

    public String[] getEncabezadoMatriz() {
        String[] encabezados = new String[lstPreguntas.size() + 2];
        encabezados[0] = "Alumnos";
        encabezados[1] = "Calificación";
        for (int i = 0; i < lstPreguntas.size(); i++){
            encabezados[i + 2] = (i + 1) + " - " + lstPreguntas.get(i).nombre;
        }
        return encabezados;
    }
    
    private void procesarResoluciones() {

        this.lstPorcentajeCalificaciones = new ArrayList<>();
        this.lstTemas = new ArrayList<>();
        this.lstDificultad = new ArrayList<>();
        this.lstPreguntas = new ArrayList<>();
        intTotalResoluciones = 0;
        intTotalAprobados = 0;
        intTotalAnulados = 0;
        intTotalRespuestas = 0;

        for (Resolucion resolucion : colResoluciones) {
            try {

                if (resolucion.isBlnAnulada()) {
                    intTotalAnulados++;
                    continue;
                }

                boolean esAprobada = resolucion.estaAprobada();

                if ((esProcesarSoloAprobadas && !esAprobada) || (esProcesarSoloDesaprobadas && esAprobada)) {
                    continue;
                }

                intTotalResoluciones++;

                lstPorcentajeCalificaciones.add(resolucion.getPorcentajeAprobacion());

                if (esAprobada) {
                    intTotalAprobados++;
                }

                for (Respuesta respuesta : resolucion.getColRespuestas()) {
                    
                    intTotalRespuestas++;
                    
                    Categoria pregunta = new Categoria(respuesta.getPregunta().getStrEnunciado());
                    int indexPregunta = lstPreguntas.indexOf(pregunta);
                    if (indexPregunta != -1) {
                        pregunta = lstPreguntas.get(indexPregunta);
                    } else {
                        lstPreguntas.add(pregunta);
                    }
                    
                    Categoria tema = new Categoria(respuesta.getPregunta().getTema().getStrNombre());
                    int indexTema = lstTemas.indexOf(tema);
                    if (indexTema != -1) {
                        tema = lstTemas.get(indexTema);
                    } else {
                        lstTemas.add(tema);
                    }

                    Categoria dificultad = new Categoria(respuesta.getPregunta().getStrNivel());
                    int indexDificultad = lstDificultad.indexOf(dificultad);
                    if (indexDificultad != -1) {
                        dificultad = lstDificultad.get(indexDificultad);
                    } else {
                        lstDificultad.add(dificultad);
                    }

                    if (!respuesta.fueRespondida()) {
                        tema.cantNoRespondidas++;
                        dificultad.cantNoRespondidas++;
                        pregunta.cantNoRespondidas++;
                        continue;
                    }
                    
                    int esCorrecta = respuesta.esCorrecta();
                    //Datos para filtros de tema
                    switch (esCorrecta) {
                        case 1: // Contestó bien la pregunta
                            tema.cantCorrectas++;
                            dificultad.cantCorrectas++;
                            pregunta.cantCorrectas++;
                            break;
                        case 0: // Contestó mal
                            tema.cantIncorrectas++;
                            dificultad.cantIncorrectas++;
                            pregunta.cantIncorrectas++;
                            break;
                        case 2: // Pregunta de desarrollo incompleta
                            tema.cantIncompletas++;
                            dificultad.cantIncompletas++;
                            pregunta.cantIncompletas++;
                            break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println(ex.getMessage());
            }
        }
    }

    public void setEsProcesarSoloAprobadas(boolean esProcesarSoloAprobadas) {
        this.esProcesarSoloAprobadas = esProcesarSoloAprobadas;

        procesarResoluciones();
    }

    public void setEsProcesarSoloDesaprobadas(boolean esProcesarSoloDesaprobadas) {
        this.esProcesarSoloDesaprobadas = esProcesarSoloDesaprobadas;

        procesarResoluciones();
    }

    
    public String[] getEnunciadosPreguntas() {
        String[] preguntas = new String[lstPreguntas.size()];
        for (int i = 0; i < lstPreguntas.size(); i++){
            preguntas[i] = (i + 1) + " - " + lstPreguntas.get(i).nombre;
        }
        return preguntas;
    }
    
    public BufferedImage getUltimoGrafico(int anchoImagen, int altoImagen){
        return this.lastChart.createBufferedImage(anchoImagen, altoImagen);
    }
    
    public void guardarUltimoGrafico(File filename, int anchoImagen, int altoImagen) throws DocumentException, FileNotFoundException, BadElementException, IOException{
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(new ImageIcon(this.lastChart.createBufferedImage(anchoImagen, altoImagen)).getImage(), null);
        document.add(img);
        document.close();
    }
    /**
     * Estadísticas del examen.
     */
    
    public BufferedImage generarGraficoLinealResolucionesPreguntas(boolean porcentaje, boolean leyenda, int anchoImagen, int altoImagen) {
        int cantIntervalos = 20;
        double intervalos[] = new double[cantIntervalos];
        int amplitud = 100 / cantIntervalos;
        String series[] = new String[cantIntervalos];
        for (int i = 0; i < cantIntervalos; i++) {
            intervalos[i] = 0;
            if (i != cantIntervalos - 1) {
                series[i] = "[" + (amplitud * i) + "-" + (amplitud * (i + 1) - 1) + "]%";
            } else {
                series[i] = "[95-100]%";
            }
        }
        for (Double calificacion : lstPorcentajeCalificaciones) {
            if (calificacion != 100) {
                intervalos[(int) (calificacion / amplitud)]++;
            } else {
                intervalos[cantIntervalos - 1]++;
            }
        }
        if (porcentaje){
            for (int i = 0; i < intervalos.length; i++) {
                intervalos[i] = intervalos[i] / intTotalResoluciones;
            }
        }
        return GestorGraficos.this.generarGraficoLineal(
                "Resultado de los exámenes",
                "Porcentaje de aprobación",
                "Cantidad de resoluciones",
                intervalos,
                series,
                porcentaje,
                leyenda,
                anchoImagen,
                altoImagen);
    }
    
    public BufferedImage generarGraficoBarrasApiladasResolucionesPreguntas(boolean porcentaje, int anchoImagen, int altoImagen) {
        
        ArrayList<Serie> series = new ArrayList<>();

        for (int i = 0; i < lstPreguntas.size(); i++) {
            Categoria pregunta = lstPreguntas.get(i);
            series.add(
                new Serie(
                        (i + 1) + "",
                        new double[]{
                            porcentaje ? (double) pregunta.cantCorrectas / intTotalRespuestas : pregunta.cantCorrectas,
                            porcentaje ? (double) pregunta.cantIncorrectas / intTotalRespuestas : pregunta.cantIncorrectas,
                            porcentaje ? (double) pregunta.cantIncompletas / intTotalRespuestas : pregunta.cantIncompletas,
                            porcentaje ? (double) pregunta.cantNoRespondidas / intTotalRespuestas : pregunta.cantNoRespondidas
                        },
                        new String[]{"Correctas", "Incorrectas", "Incompletas", "No respondidas"})
            );
        }

        return generarGraficoBarrasApiladas(
                "Resultado de los exámenes",
                "Calificación de las respuestas por pregunta",
                "Cantidad de respuestas",
                series,
                porcentaje,
                true,
                PlotOrientation.VERTICAL,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasResolucionesPreguntas(boolean porcentaje, int anchoImagen, int altoImagen) {
        
        ArrayList<Serie> series = new ArrayList<>();

        double[] correctas = new double[lstPreguntas.size()];
        double[] incorrectas = new double[lstPreguntas.size()];
        double[] desarrollo = new double[lstPreguntas.size()];
        double[] noRespondidas = new double[lstPreguntas.size()];
        
        String[] preguntas = new String[lstPreguntas.size()];
        
        for (int i = 0; i < lstPreguntas.size(); i++) {
            preguntas[i] = (i + 1) + "";
            correctas[i] = porcentaje ? (double) lstPreguntas.get(i).cantCorrectas / intTotalRespuestas : lstPreguntas.get(i).cantCorrectas;
            incorrectas[i] = porcentaje ? (double) lstPreguntas.get(i).cantIncorrectas / intTotalRespuestas : lstPreguntas.get(i).cantIncorrectas;
            desarrollo[i] = porcentaje ? (double) lstPreguntas.get(i).cantIncompletas / intTotalRespuestas : lstPreguntas.get(i).cantIncompletas;
            noRespondidas[i] = porcentaje ? (double) lstPreguntas.get(i).cantNoRespondidas / intTotalRespuestas : lstPreguntas.get(i).cantNoRespondidas;
        }

        series.add(new Serie("Correctas", correctas, preguntas));
        series.add(new Serie("Incorrectas", incorrectas, preguntas));
        series.add(new Serie("Incompletas", desarrollo, preguntas));
        series.add(new Serie("No respondidas", noRespondidas, preguntas));
        
        return generarGraficoBarras(
                "Resultado de los exámenes",
                "Calificación de las respuestas por pregunta",
                "Cantidad de respuestas",
                series,
                porcentaje,
                true,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoTortaResolucionesPreguntas(int anchoImagen, int altoImagen) {
        
        String[] preguntas = new String[lstPreguntas.size()];
        double[] cantRespuestasPorPregunta = new double[lstPreguntas.size()];
        
        for (int i = 0; i < lstPreguntas.size(); i++) {
            Categoria pregunta = lstPreguntas.get(i);
            int total = pregunta.cantCorrectas;
            preguntas[i] = "Pregunta " + (i + 1);
            cantRespuestasPorPregunta[i] = total;
        }

        return generarGraficoTorta(
                "Cantidad de respuestas correctas por pregunta",
                cantRespuestasPorPregunta,
                preguntas,
                anchoImagen,
                altoImagen);
    
    }
    
    
    public BufferedImage generarGraficoPoblacionResolucionesDificultad(boolean porcentaje, int anchoImagen, int altoImagen) {

        ArrayList<Serie> series = new ArrayList<>();

        for (Categoria dificultad : lstDificultad) {
            series.add(
                    new Serie(
                            dificultad.nombre,
                            new double[]{
                                porcentaje ? (double) dificultad.cantCorrectas / intTotalRespuestas : dificultad.cantCorrectas,
                                porcentaje ? (double) dificultad.cantIncorrectas + dificultad.cantNoRespondidas / intTotalRespuestas : (dificultad.cantIncorrectas + dificultad.cantNoRespondidas)
                            },
                            new String[]{"Correctas", "Incorrectas/No respondidas"})
            );
        }

        return generarGraficoBarrasApiladas(
                "Resultado de los exámenes",
                "Calificación de preguntas por dificultad",
                "Cantidad de preguntas",
                series,
                porcentaje,
                true,
                PlotOrientation.HORIZONTAL,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasApiladasResolucionesDificultad(boolean porcentaje, int anchoImagen, int altoImagen) {

        ArrayList<Serie> series = new ArrayList<>();

        for (Categoria dificultad : lstDificultad) {
            series.add(
                    new Serie(
                            dificultad.nombre,
                            new double[]{
                                porcentaje ? (double) dificultad.cantCorrectas / intTotalRespuestas : dificultad.cantCorrectas,
                                porcentaje ? (double) dificultad.cantIncorrectas / intTotalRespuestas : dificultad.cantIncorrectas,
                                porcentaje ? (double) dificultad.cantIncompletas / intTotalRespuestas : dificultad.cantIncompletas,
                                porcentaje ? (double) dificultad.cantNoRespondidas / intTotalRespuestas : dificultad.cantNoRespondidas
                            },
                            new String[]{"Correctas", "Incorrectas", "Incompletas", "No respondidas"})
            );
        }

        return generarGraficoBarrasApiladas(
                "Resultado de los exámenes",
                "Calificación de las preguntas por dificultad",
                "Cantidad de preguntas",
                series,
                porcentaje,
                true,
                PlotOrientation.VERTICAL,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasResolucionesDificultad(boolean porcentaje, int anchoImagen, int altoImagen) {

        ArrayList<Serie> series = new ArrayList<>();

        double[] correctas = new double[lstDificultad.size()];
        double[] incorrectas = new double[lstDificultad.size()];
        double[] desarrollo = new double[lstDificultad.size()];
        double[] noRespondidas = new double[lstDificultad.size()];
        String[] dificultades = new String[lstDificultad.size()];
        for (int i = 0; i < lstDificultad.size(); i++) {
            dificultades[i] = lstDificultad.get(i).nombre;            
            correctas[i] = porcentaje ? (double) lstDificultad.get(i).cantCorrectas / intTotalRespuestas : lstDificultad.get(i).cantCorrectas;
            incorrectas[i] = porcentaje ? (double) lstDificultad.get(i).cantIncorrectas / intTotalRespuestas : lstDificultad.get(i).cantIncorrectas;
            desarrollo[i] = porcentaje ? (double) lstDificultad.get(i).cantIncompletas / intTotalRespuestas : lstDificultad.get(i).cantIncompletas;
            noRespondidas[i] = porcentaje ? (double) lstDificultad.get(i).cantNoRespondidas / intTotalRespuestas : lstDificultad.get(i).cantNoRespondidas;
        }

        series.add(new Serie("Correctas", correctas, dificultades));
        series.add(new Serie("Incorrectas", incorrectas, dificultades));
        series.add(new Serie("Incompletas", desarrollo, dificultades));
        series.add(new Serie("No respondidas", noRespondidas, dificultades));

        return generarGraficoBarras(
                "Resultado de los exámenes",
                "Calificación de las preguntas por dificultad",
                "Cantidad de preguntas",
                series,
                porcentaje,
                true,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoPoblacionResolucionesTemas(boolean porcentaje, int anchoImagen, int altoImagen) {

        ArrayList<Serie> series = new ArrayList<>();

        for (Categoria tema : lstTemas) {
            series.add(
                    new Serie(
                            tema.nombre,
                            new double[]{
                                porcentaje ? (double) tema.cantCorrectas / intTotalRespuestas : tema.cantCorrectas,
                                porcentaje ? (double) (tema.cantIncorrectas + tema.cantNoRespondidas) / intTotalRespuestas: (tema.cantIncorrectas + tema.cantNoRespondidas)},
                            new String[]{"Correctas", "Incorrectas/No respondidas"})
            );
        }

        return generarGraficoBarrasApiladas(
                "Resultado de los exámenes",
                "Calificación de preguntas por temas",
                "Cantidad de preguntas",
                series,
                porcentaje,
                true,
                PlotOrientation.HORIZONTAL,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasApiladasResolucionesTemas(boolean porcentaje, int anchoImagen, int altoImagen) {

        ArrayList<Serie> series = new ArrayList<>();

        for (Categoria tema : lstTemas) {
            series.add(
                    new Serie(
                            tema.nombre,
                            new double[]{
                                porcentaje ? (double) tema.cantCorrectas / intTotalRespuestas : tema.cantCorrectas,
                                porcentaje ? (double) tema.cantIncorrectas / intTotalRespuestas : tema.cantIncorrectas,
                                porcentaje ? (double) tema.cantIncompletas / intTotalRespuestas : tema.cantIncompletas,
                                porcentaje ? (double) tema.cantNoRespondidas / intTotalRespuestas : tema.cantNoRespondidas
                            },
                            new String[]{"Correctas", "Incorrectas", "Incompletas", "No respondidas"})
            );
        }

        return generarGraficoBarrasApiladas(
                "Resultado de los exámenes",
                "Calificación de preguntas por temas",
                "Cantidad de preguntas",
                series,
                porcentaje,
                true,
                PlotOrientation.VERTICAL,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasResolucionesTemas(final boolean porcentaje, int anchoImagen, int altoImagen) {

        ArrayList<Serie> series = new ArrayList<>();

        double[] correctas = new double[lstTemas.size()];
        double[] incorrectas = new double[lstTemas.size()];
        double[] desarrollo = new double[lstTemas.size()];
        double[] noRespondidas = new double[lstTemas.size()];
        String[] temas = new String[lstTemas.size()];
        for (int i = 0; i < lstTemas.size(); i++) {
            temas[i] = lstTemas.get(i).nombre;
            correctas[i] = porcentaje ? (double) lstTemas.get(i).cantCorrectas / intTotalRespuestas : lstTemas.get(i).cantCorrectas;
            incorrectas[i] = porcentaje ? (double) lstTemas.get(i).cantIncorrectas / intTotalRespuestas : lstTemas.get(i).cantIncorrectas;
            desarrollo[i] = porcentaje ? (double) lstTemas.get(i).cantIncompletas / intTotalRespuestas : lstTemas.get(i).cantIncompletas;
            noRespondidas[i] = porcentaje ? (double) lstTemas.get(i).cantNoRespondidas / intTotalRespuestas : lstTemas.get(i).cantNoRespondidas;
        }

        series.add(new Serie("Correctas", correctas, temas));
        series.add(new Serie("Incorrectas", incorrectas, temas));
        series.add(new Serie("Incompletas", desarrollo, temas));
        series.add(new Serie("No respondidas", noRespondidas, temas));

        return generarGraficoBarras(
                "Resultado de los exámenes",
                "Calificación de preguntas por temas",
                "Cantidad de preguntas",
                series,
                porcentaje,
                true,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoLinealResoluciones(boolean porcentaje, boolean leyenda, int anchoImagen, int altoImagen) {
        int cantIntervalos = 20;
        double intervalos[] = new double[cantIntervalos];
        int amplitud = 100 / cantIntervalos;
        String series[] = new String[cantIntervalos];
        for (int i = 0; i < cantIntervalos; i++) {
            intervalos[i] = 0;
            if (i != cantIntervalos - 1) {
                series[i] = "[" + (amplitud * i) + "-" + (amplitud * (i + 1) - 1) + "]%";
            } else {
                series[i] = "[95-100]%";
            }
        }
        for (Double calificacion : lstPorcentajeCalificaciones) {
            if (calificacion != 100) {
                intervalos[(int) (calificacion / amplitud)]++;
            } else {
                intervalos[cantIntervalos - 1]++;
            }
        }
        if (porcentaje){
            for (int i = 0; i < intervalos.length; i++) {
                intervalos[i] = intervalos[i] / intTotalResoluciones;
            }
        }
        return GestorGraficos.this.generarGraficoLineal(
                "Resultado de los exámenes",
                "Porcentaje de aprobación",
                "Cantidad de resoluciones",
                intervalos,
                series,
                porcentaje,
                leyenda,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasDistribucionResultadosResoluciones(boolean porcentaje, boolean leyenda, int anchoImagen, int altoImagen) {
        double porcentajeAprobacion = colResoluciones.get(0).getExamen().getDblPorcentajeAprobacion();
        int cantIntervalos = 20;
        double intervalos[] = new double[cantIntervalos];
        int amplitud = 100 / cantIntervalos;
        int primerIntervaloAprobado = (int) porcentajeAprobacion / amplitud;
        String series[] = new String[cantIntervalos];
        for (int i = 0; i < cantIntervalos; i++) {
            intervalos[i] = 0;
            if (i != cantIntervalos - 1) {
                series[i] = "[" + (amplitud * i) + "-" + (amplitud * (i + 1) - 1) + "]%";
            } else {
                series[i] = "[95-100]%";
            }
        }
        for (Double calificacion : lstPorcentajeCalificaciones) {
            if (calificacion != 100) {
                intervalos[ (int) (calificacion / amplitud) ]++;
            } else {
                intervalos[cantIntervalos - 1]++;
            }
        }
        if (porcentaje){
            for (int i = 0; i < intervalos.length; i++) {
                intervalos[i] = intervalos[i] / intTotalResoluciones;
            }
        }
        return GestorGraficos.this.generarGraficoBarrasDistribucionResultados(
                "Resultado de los exámenes",
                "Porcentaje de aprobación",
                "Cantidad de resoluciones",
                intervalos,
                series,
                porcentaje,
                leyenda,
                primerIntervaloAprobado,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoBarrasResoluciones(final boolean porcentaje, boolean leyenda, int anchoImagen, int altoImagen) {
        return generarGraficoBarras(
                "Resultado de los exámenes",
                "Resultados",
                "Cantidad de resoluciones",
                new ArrayList<Serie>() {
                    {
                        add(
                                new Serie(
                                        "Resultados",
                                        new double[]{
                                            porcentaje ? (intTotalAprobados / intTotalResoluciones) : intTotalAprobados,
                                            porcentaje ? (intTotalResoluciones - intTotalAprobados) / intTotalResoluciones : (intTotalResoluciones - intTotalAprobados),
                                            porcentaje ? (intTotalAnulados / intTotalResoluciones) : intTotalAnulados},
                                        new String[]{"Aprobados", "Desaprobados", "Anulados"})
                        );
                    }
                },
                porcentaje,
                leyenda,
                anchoImagen,
                altoImagen);
    }

    public BufferedImage generarGraficoTortaResoluciones(int anchoImagen, int altoImagen) {
        return generarGraficoTorta(
                "Resultado de los exámenes",
                new double[]{intTotalAprobados, intTotalResoluciones - intTotalAprobados, intTotalAnulados},
                new String[]{"Aprobados", "Desaprobados", "Anulados"},
                anchoImagen,
                altoImagen);
    }

    /**
     * Comienzo de mètodos básicos.
     */
    private BufferedImage generarGraficoBarrasApiladas(String titulo, String ejeHorizontal, String ejeVertical, ArrayList<Serie> series, boolean porcentaje, boolean leyenda, PlotOrientation orientacion, int anchoImagen, int altoImagen) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Serie serie : series) {
            if (serie.valores.length != serie.funciones.length) {
                continue;
            }
            for (int i = 0; i < serie.valores.length; i++) {
                dataset.addValue(serie.valores[i], serie.funciones[i], serie.nombre);
            }
        }
        JFreeChart chart = ChartFactory.createStackedBarChart(
                titulo,
                ejeHorizontal,
                ejeVertical,
                dataset,
                orientacion,
                leyenda,
                true, // tooltips
                false // urls
        );

        chart.setBackgroundPaint(null);
        chart.getTitle().setPaint(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        chart.getTitle().setFont(LookAndFeelEntropy.FUENTE_TITULO_GRANDE);
        chart.setBorderVisible(false);
        if (leyenda) {
            chart.getLegend().setItemFont(LookAndFeelEntropy.FUENTE_REGULAR);
            chart.getLegend().setBackgroundPaint(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
        }

        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap(series.get(0).nombre);
        for (Serie serie : series) {
            map.mapKeyToGroup(serie.nombre, serie.nombre);
        }
        renderer.setSeriesToGroupMap(map);
        renderer.setDrawBarOutline(false);
        renderer.setRenderAsPercentages(porcentaje);
        renderer.setItemLabelsVisible(true);
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

        for (Serie serie : series) {
            for (int i = 0; i < serie.valores.length; i++) {
                if (Serie.colores.size() - 1 < i || Serie.colores.get(i) == null) {
                    GradientPaint gp = new GradientPaint(
                            0.0f, 0.0f, Color.orange,
                            0.0f, 0.0f, generarColorAleatorio(Color.orange)
                    );
                    Serie.colores.add(gp);
                }
                renderer.setSeriesPaint(i, Serie.colores.get(i));
            }
        }

        renderer.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL)
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        if (porcentaje) {
            rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        } else {
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        domainAxis.setCategoryMargin(0.05);

        plot.setRenderer(renderer);

        //Generamos una imagen
        this.lastChart = chart;
        return chart.createBufferedImage(anchoImagen, altoImagen);
    }

    private BufferedImage generarGraficoLineal(String titulo, String ejeHorizontal, String ejeVertical, double[] valores, String[] funciones, boolean porcentaje, boolean leyenda, int anchoImagen, int altoImagen) {
        XYSeries frecObservadas = new XYSeries("Cantidades de resoluciones por calificación");
        //Calculamos las frecuencias por intervalo
        for (int i = 0; i < valores.length; i++) {
            frecObservadas.add(i, valores[i]);
        }
        XYDataset dataset = new XYSeriesCollection(frecObservadas);
        //Creamos el gráfico
        JFreeChart chart = ChartFactory.createXYLineChart(
                titulo,
                ejeHorizontal,
                ejeVertical,
                null,
                PlotOrientation.VERTICAL,
                leyenda,
                false,
                true
        );
        //Seteamos color de título
        chart.setBackgroundPaint(null);
        chart.setBorderVisible(false);
        chart.getTitle().setPaint(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        chart.getTitle().setFont(LookAndFeelEntropy.FUENTE_TITULO_GRANDE);
        if (leyenda) {
            chart.getLegend().setItemFont(LookAndFeelEntropy.FUENTE_REGULAR);
            chart.getLegend().setBackgroundPaint(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
        }
        //Seteamos datasets
        XYPlot xyplot = chart.getXYPlot();
        xyplot.setDataset(1, dataset);
        xyplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        //Seteamos características de la función lineal
        XYItemRenderer xyLineRendererObs = new XYLineAndShapeRenderer();
        xyLineRendererObs.setSeriesShape(0, new Line2D.Double(0.0, 0.0, 0.0, 0.0));
        xyLineRendererObs.setSeriesStroke(0, new BasicStroke(3.5f));
        xyLineRendererObs.setBasePaint(generarColorAleatorio(Color.orange));
        xyplot.setRenderer(1, xyLineRendererObs);
        //Seteamos color de labels de ejes
        ValueAxis axisX = new SymbolAxis(ejeHorizontal, funciones);
        axisX.setVerticalTickLabels(true);
        xyplot.setDomainAxis(axisX);
        xyplot.getDomainAxis().setTickLabelPaint(Color.BLACK);
        xyplot.getDomainAxis().setLabelPaint(Color.BLACK);
        xyplot.getDomainAxis().setTickLabelFont(LookAndFeelEntropy.FUENTE_REGULAR);
        xyplot.getRangeAxis().setTickLabelPaint(Color.BLACK);
        xyplot.getRangeAxis().setLabelPaint(Color.BLACK);
        xyplot.getRangeAxis().setTickLabelFont(LookAndFeelEntropy.FUENTE_REGULAR);
        //Seteamos porcentaje
        final NumberAxis rangeAxis = (NumberAxis) xyplot.getRangeAxis();
        if (porcentaje) {
            rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        } else {
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }
        //Seteamos transparencia de fondo
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setOutlineVisible(false);
        //Generamos una imagen
        this.lastChart = chart;
        return chart.createBufferedImage(anchoImagen, altoImagen);
    }

    private BufferedImage generarGraficoBarrasDistribucionResultados(String titulo, String ejeHorizontal, String ejeVertical, double[] valores, String[] funciones, boolean porcentaje, boolean leyenda, int intervaloAprobacion, int anchoImagen, int altoImagen) {
        if (valores.length != funciones.length) {
            return null;
        }
        DefaultCategoryDataset pieDataset = new DefaultCategoryDataset();
        for (int i = 0; i < valores.length; i++) {
            pieDataset.addValue(valores[i], "Serie 1", funciones[i]);
        }
        JFreeChart chart = ChartFactory.createBarChart(titulo, ejeHorizontal, ejeVertical, pieDataset, PlotOrientation.VERTICAL, leyenda, true, false);
        chart.setBackgroundPaint(null);
        chart.setBorderVisible(false);
        chart.getTitle().setPaint(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        chart.getTitle().setFont(LookAndFeelEntropy.FUENTE_TITULO_GRANDE);
        if (leyenda) {
            chart.getLegend().setItemFont(LookAndFeelEntropy.FUENTE_REGULAR);
            chart.getLegend().setBackgroundPaint(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
        }

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        if (porcentaje) {
            rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        } else {
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        for (int i = 0; i < valores.length; i++) {
            if (i == intervaloAprobacion - 1) {
                renderer.setSeriesPaint(i, new GradientPaint(
                        0.0f, 0.0f, Color.orange,
                        0.0f, 0.0f, generarColorAleatorio(Color.red)
                ));
            } else {
                renderer.setSeriesPaint(i, new GradientPaint(
                        0.0f, 0.0f, Color.orange,
                        0.0f, 0.0f, generarColorAleatorio(Color.green)
                ));
            }
        }

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );

        this.lastChart = chart;

        //Generamos una imagen
        return chart.createBufferedImage(anchoImagen, altoImagen);
    }

    private BufferedImage generarGraficoBarras(String titulo, String ejeHorizontal, String ejeVertical, ArrayList<Serie> series, boolean porcentaje, boolean leyenda, int anchoImagen, int altoImagen) {
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        for (Serie serie : series) {
            if (serie.valores.length != serie.funciones.length) {
                continue;
            }
            for (int i = 0; i < serie.valores.length; i++) {
                barDataset.addValue(serie.valores[i], serie.nombre, serie.funciones[i]);
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(titulo, ejeHorizontal, ejeVertical, barDataset, PlotOrientation.VERTICAL, leyenda, true, false);
        chart.setBackgroundPaint(null);
        chart.getTitle().setPaint(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        chart.getTitle().setFont(LookAndFeelEntropy.FUENTE_TITULO_GRANDE);
        chart.setBorderVisible(false);
        if (leyenda) {
            chart.getLegend().setItemFont(LookAndFeelEntropy.FUENTE_REGULAR);
            chart.getLegend().setBackgroundPaint(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
        }

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        if (porcentaje) {
            rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        } else {
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        }
        
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        for (Serie serie : series) {
            for (int i = 0; i < serie.valores.length; i++) {
                if (Serie.colores.size() - 1 < i || Serie.colores.get(i) == null) {
                    GradientPaint gp = new GradientPaint(
                            0.0f, 0.0f, Color.orange,
                            0.0f, 0.0f, generarColorAleatorio(Color.orange)
                    );
                    Serie.colores.add(gp);
                }
                renderer.setSeriesPaint(i, Serie.colores.get(i));
            }
        }

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        //Generamos una imagen
        this.lastChart = chart;
        return chart.createBufferedImage(anchoImagen, altoImagen);
    }

    private BufferedImage generarGraficoTorta(String titulo, double[] valores, String[] funciones, int anchoImagen, int altoImagen) {
        if (valores.length != funciones.length) {
            return null;
        }
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (int i = 0; i < valores.length; i++) {
            pieDataset.setValue(funciones[i], valores[i]);
        }
        JFreeChart chart = ChartFactory.createPieChart(titulo, pieDataset, true, true, false);
        chart.setBackgroundPaint(null);
        chart.getTitle().setPaint(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        chart.getTitle().setFont(LookAndFeelEntropy.FUENTE_TITULO_GRANDE);
        chart.setBorderVisible(false);
        chart.getLegend().setItemFont(LookAndFeelEntropy.FUENTE_REGULAR);
        chart.getLegend().setBackgroundPaint(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setSimpleLabels(true);
        plot.setOutlineVisible(false);
        for (int i = 0; i < valores.length; i++) {
            plot.setSectionPaint(funciones[i], generarColorAleatorio(Color.ORANGE));//LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL));
            plot.setExplodePercent(funciones[i], 0.10);
        }
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);

        this.lastChart = chart;

        //Generamos una imagen
        return chart.createBufferedImage(anchoImagen, altoImagen);
    }

    
    private Color generarColorAleatorio(Color base) {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256); //100
        int blue = random.nextInt(256); // 60

        if (base != null) {
            red = (red + base.getRed()) / 2;
            green = (green + base.getGreen()) / 2;
            blue = (blue + base.getBlue()) / 2;
        }

        Color color = new Color(red, green, blue);
        return color;
    }

    private static class Serie {

        private final double[] valores;
        private final String nombre;
        private final String[] funciones;
        private static ArrayList<GradientPaint> colores = new ArrayList<>();

        public Serie(String nombre, double[] valores, String[] funciones) {
            this.nombre = nombre;
            this.valores = valores;
            this.funciones = funciones;
        }

    }

    /**
     * Clase que permite llevar el conteo de diferentes categorías
     */
    private class Categoria {

        //Nombre del filtro: rtdos. generales, nombre del tema, nivel de dificultad
        private String nombre;
        // Respecto a resoluciones
        private int cantAprobados;
        private int cantDesaprobados;
        private int cantAnulados;
        // Respecto a las preguntas
        private int cantCorrectas;
        private int cantIncorrectas;
        private int cantIncompletas;
        private int cantNoRespondidas;
        //Respecto a los niveles
        private int cantMuyFacil;
        private int cantFacil;
        private int cantNormal;
        private int cantDificil;
        private int cantExperto;

        public Categoria(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Categoria other = (Categoria) obj;
            if (!Objects.equals(this.nombre, other.nombre)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return nombre;
        }

    }
}
