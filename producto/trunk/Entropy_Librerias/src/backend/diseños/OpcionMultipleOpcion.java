package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class OpcionMultipleOpcion implements Serializable {
    
    private int intOrden;
    private String strRespuesta;
    private boolean blnEsVerdadera;

    public OpcionMultipleOpcion() {
    }

    public OpcionMultipleOpcion(String strRespuesta, boolean blnEsVerdadera) {
        this.strRespuesta = strRespuesta;
        this.blnEsVerdadera = blnEsVerdadera;
    }

    public OpcionMultipleOpcion(int intOrden, String strRespuesta, boolean blnEsVerdadera) {
        this.intOrden = intOrden;
        this.strRespuesta = strRespuesta;
        this.blnEsVerdadera = blnEsVerdadera;
    }

    public int getIntOrden() {
        return intOrden;
    }

    public void setIntOrden(int intOrden) {
        this.intOrden = intOrden;
    }

    public String getStrRespuesta() {
        return strRespuesta;
    }

    public void setStrRespuesta(String strRespuesta) {
        this.strRespuesta = strRespuesta;
    }

    public boolean isBlnEsVerdadera() {
        return blnEsVerdadera;
    }

    public void setBlnEsVerdadera(boolean blnEsVerdadera) {
        this.blnEsVerdadera = blnEsVerdadera;
    }
    
    public boolean equals(OpcionMultipleOpcion o) {
        if(this.getStrRespuesta().equals(o.getStrRespuesta())){
            return true;
        }else {
            return false;
        }
        
    }
    
    
}
