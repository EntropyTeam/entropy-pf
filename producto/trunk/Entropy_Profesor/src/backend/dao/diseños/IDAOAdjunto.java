/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.dao.diseños;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Jose
 */
public interface IDAOAdjunto {
    
    public void guardarAdjunto(int idPregunta, ArrayList<Object> adjunto, Connection conexion);

    public Object recuperarAdjunto(int idPregunta);
    
    public Object recuperarAdjuntoExamen(int idPregunta);
}
