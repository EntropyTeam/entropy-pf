/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.Presentacion;

import backend.Asistencia.Asistencia;
import backend.usuarios.Alumno;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Jose
 */
public class Presentacion {

    private int intIdPresentacion;
    private int  intIdCurso;
    private String strNombre;
    private String strDescripcion;
    private Date dteFecha;
    private ArrayList<Asistencia> asistencia;

    public int getIntIdPresentacion() {
        return intIdPresentacion;
    }

    public void setIntIdPresentacion(int intIdPresentacion) {
        this.intIdPresentacion = intIdPresentacion;
    }

    public int getIntIdCurso() {
        return intIdCurso;
    }

    public void setIntIdCurso(int intIdCurso) {
        this.intIdCurso = intIdCurso;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public Date getDteFecha() {
        return dteFecha;
    }

    public void setDteFecha(Date dteFecha) {
        this.dteFecha = dteFecha;
    }

    public ArrayList<Asistencia> getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(ArrayList<Asistencia> asistencia) {
        this.asistencia = asistencia;
    }


    public String toString()
    {
        String fechaString = new SimpleDateFormat("yyyy-MM-dd").format(this.getDteFecha()); 
        return "Nombre: "+this.strNombre+"- Fecha: "+fechaString;
    }
    
    
}
