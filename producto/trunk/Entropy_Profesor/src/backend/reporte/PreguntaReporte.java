package backend.reporte;

import java.util.ArrayList;

/**
 *
 * @author gaston
 */
public class PreguntaReporte {
    
    private ArrayList colPreguntaReporteDesarrollar;
    private ArrayList colPreguntaReporteMultipleOpcion;
    private ArrayList colPreguntaReporteNumerica;
    private ArrayList colPreguntaReporteVerdaderoFalso;
    private ArrayList colPreguntaReporteRelacionColumnas;

    
    public PreguntaReporte() {
        this.colPreguntaReporteDesarrollar = new ArrayList();
        this.colPreguntaReporteMultipleOpcion = new ArrayList();
        this.colPreguntaReporteNumerica = new ArrayList();
        this.colPreguntaReporteVerdaderoFalso = new ArrayList();
        this.colPreguntaReporteRelacionColumnas = new ArrayList();
    }

    public PreguntaReporte(ArrayList colPreguntaReporteDesarrollar, ArrayList colPreguntaReporteMultipleOpcion, ArrayList colPreguntaReporteNumerica, ArrayList colPreguntaReporteVerdaderoFalso, ArrayList colPreguntaReporteRelacionColumnas) {
        this.colPreguntaReporteDesarrollar = colPreguntaReporteDesarrollar;
        this.colPreguntaReporteMultipleOpcion = colPreguntaReporteMultipleOpcion;
        this.colPreguntaReporteNumerica = colPreguntaReporteNumerica;
        this.colPreguntaReporteVerdaderoFalso = colPreguntaReporteVerdaderoFalso;
        this.colPreguntaReporteRelacionColumnas = colPreguntaReporteRelacionColumnas;
    }

    public ArrayList getColPreguntaReporteDesarrollar() {
        return colPreguntaReporteDesarrollar;
    }

    public void setColPreguntaReporteDesarrollar(ArrayList colPreguntaReporteDesarrollar) {
        this.colPreguntaReporteDesarrollar = colPreguntaReporteDesarrollar;
    }

    public ArrayList getColPreguntaReporteMultipleOpcion() {
        return colPreguntaReporteMultipleOpcion;
    }

    public void setColPreguntaReporteMultipleOpcion(ArrayList colPreguntaReporteMultipleOpcion) {
        this.colPreguntaReporteMultipleOpcion = colPreguntaReporteMultipleOpcion;
    }

    public ArrayList getColPreguntaReporteNumerica() {
        return colPreguntaReporteNumerica;
    }

    public void setColPreguntaReporteNumerica(ArrayList colPreguntaReporteNumerica) {
        this.colPreguntaReporteNumerica = colPreguntaReporteNumerica;
    }

    public ArrayList getColPreguntaReporteVerdaderoFalso() {
        return colPreguntaReporteVerdaderoFalso;
    }

    public void setColPreguntaReporteVerdaderoFalso(ArrayList colPreguntaReporteVerdaderoFalso) {
        this.colPreguntaReporteVerdaderoFalso = colPreguntaReporteVerdaderoFalso;
    }

    public ArrayList getColPreguntaReporteRelacionColumnas() {
        return colPreguntaReporteRelacionColumnas;
    }

    public void setColPreguntaReporteRelacionColumnas(ArrayList colPreguntaReporteRelacionColumnas) {
        this.colPreguntaReporteRelacionColumnas = colPreguntaReporteRelacionColumnas;
    }   
}
