/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.dao.presentacion;

import backend.Presentacion.Presentacion;
import backend.usuarios.Alumno;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Jose
 */
public interface IDAOPresentacion {
    public Presentacion recuperarPresentacion(int Presentacion);
    
    public ArrayList<Presentacion> recuperarPresentaciones(int idCurso);
    
    public ArrayList<Presentacion> recuperarPresentacionesDeUnAlumno(int idAlumno);
    
    public ArrayList<Presentacion> recuperarPresentaciones(int idCurso, Date date);
    
    public ArrayList<Presentacion> recuperarPresentaciones(int idCurso, Date desde, Date hasta);
    
    public void modificarPresentacion(int idPresentacion);
    
    public void borarPresentacion(int idPresentacion);
    
    public boolean guardarPresentacion(Presentacion presentacion);
}
