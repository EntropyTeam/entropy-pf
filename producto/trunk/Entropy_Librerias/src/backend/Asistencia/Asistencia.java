/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.Asistencia;

import backend.usuarios.Alumno;

/**
 *
 * @author Jose
 */
public class Asistencia {
    
   private int intIdAsistencia;
   private int intIdPresentacion;
   private Alumno alumno;
   private boolean blnAnulada;
   private String strMotivoAnulacion;
   private String strIp;

    public int getIntIdAsistencia() {
        return intIdAsistencia;
    }

    public void setIntIdAsistencia(int intIdAsistencia) {
        this.intIdAsistencia = intIdAsistencia;
    }

    public int getIntIdPresentacion() {
        return intIdPresentacion;
    }

    public void setIntIdPresentacion(int intIdPresentacion) {
        this.intIdPresentacion = intIdPresentacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public boolean isBlnAnulada() {
        return blnAnulada;
    }

    public void setBlnAnulada(boolean blnAnulada) {
        this.blnAnulada = blnAnulada;
    }

    public String getStrMotivoAnulacion() {
        return strMotivoAnulacion;
    }

    public void setStrMotivoAnulacion(String strMotivoAnulacion) {
        this.strMotivoAnulacion = strMotivoAnulacion;
    }

    public String getStrIp() {
        return strIp;
    }

    public void setStrIp(String strIp) {
        this.strIp = strIp;
    }
   
   
   
}
