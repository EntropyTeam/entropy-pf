/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.Seguridad;

import java.util.UUID;

/**
 *
 * @author gaston2
 */
public class GestorSeguridadAutenticacion {
    
    public GestorSeguridadAutenticacion(){

}
    public String GenerarCodigoAlfNum(){
        String codigo =  UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
        return codigo;
    }
}
