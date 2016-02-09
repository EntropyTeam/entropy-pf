package backend.reporte;

import backend.Presentacion.Presentacion;
import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author Denise
 */
public class GestorGraficosAlumno {
    
    public BufferedImage generarGraficoTimelineAsistencias(ArrayList<Presentacion> colPresentaciones, boolean leyenda, int anchoImagen, int altoImagen) {
        String chartTitle = "Línea de tiempo";
        XYDataset dataset = createDataset(colPresentaciones);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            chartTitle, 
            "Fechas", 
            "Cantidad de Asistencias",
            dataset, 
            true, 
            true, 
            false
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
        XYPlot xyplot = chart.getXYPlot();
//        NumberAxis axis2 = new NumberAxis("Secondary");
//        axis2.setAutoRangeIncludesZero(false);
//        xyplot.setRangeAxis(1, axis2);
//        xyplot.setDataset(1, createDataset());
//        xyplot.mapDatasetToRangeAxis(1, 1);
        
        //Seteamos características de la función lineal
        XYItemRenderer xyLineRendererObs = new XYLineAndShapeRenderer();
        xyLineRendererObs.setSeriesShape(0, new Line2D.Double(0.0, 0.0, 0.0, 0.0));
        xyLineRendererObs.setSeriesStroke(0, new BasicStroke(2.5f));
        xyLineRendererObs.setBasePaint(Color.ORANGE);
        xyplot.setRenderer(0, xyLineRendererObs);
        XYItemRenderer renderer = xyplot.getRenderer();
        renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
        renderer.setSeriesShape(0, cross);
        if (renderer instanceof StandardXYItemRenderer) {
            StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
            rr.setShapesFilled(true);
        }
        
        StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        xyplot.setRenderer(1, renderer2);
        
        DateAxis axis = (DateAxis) xyplot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy", new Locale("es", "ES")));
        axis.setLabelFont(LookAndFeelEntropy.FUENTE_REGULAR);
        
        NumberAxis rangeAxis = (NumberAxis) xyplot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelFont(LookAndFeelEntropy.FUENTE_REGULAR);
        
        //Seteamos transparencia de fondo
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setOutlineVisible(false);
        
        //Generamos una imagen
        return chart.createBufferedImage(anchoImagen, altoImagen);
    }
    
    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private XYDataset createDataset(ArrayList<Presentacion> colPresentaciones) {
        
        HashMap<Date, Integer> mapAsistencias = new HashMap<>();
        TimeSeries s1 = new TimeSeries("Asistencias", Month.class);
        
        for (Presentacion p : colPresentaciones) {
            mapAsistencias.put(p.getDteFecha(), (mapAsistencias.get(p.getDteFecha()) == null) ? 1 : (mapAsistencias.get(p.getDteFecha()) + 1 ));
        }
        
        Iterator it = mapAsistencias.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            s1.addOrUpdate(new Month((Date)pair.getKey()), (int)pair.getValue());
            it.remove();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);

        return dataset;

    }
}
