/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.Presentacion;

import java.util.Date;

/**
 *
 * @author Jose
 */
public class Presentacion {
    
    /*    public static final String PRE_TBL_PRESENTACION = "pre_presentacion";
    public static final String PRE_COL_PRESENTACION_ID = "presentacionId";
    public static final String PRE_COL_PRESENTACION_CURSO_ID = "cursoId";
    public static final String PRE_COL_PRESENTACION_NOMBRE = "nombre";
    public static final String PRE_COL_PRESENTACION_DESCRIPCION = "descripcion";
    public static final String PRE_COL_PRESENTACION_FECHA = "fecha";*/
    
    private int intIdPresentacion;
    private int  intIdCurso;
    private String strNombre;
    private String strDescripcion;
    private Date dteFecha;

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

    public String toString()
    {
        return this.strNombre;
    }
}
