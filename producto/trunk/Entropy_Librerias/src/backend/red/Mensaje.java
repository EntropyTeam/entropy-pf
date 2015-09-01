package backend.red;

import java.io.Serializable;

/**
 * Clase que reprensenta un mensaje que se envía a través de la red.
 * 
 * @author Denise
 */
public class Mensaje implements Serializable {
    
    private int tipo;
    private Object payload;
    
    public Mensaje(int tipo) {
        this.tipo = tipo;
    }
    
    //ESTE MÉTODO NO DEBIESE USARSE.
    @Deprecated
    public Mensaje(Object payload) {
        this.payload = payload;
    }
    
    public Mensaje(int tipo, Object payload) {
        this.tipo = tipo;
        this.payload = payload;
    }
    
    public Mensaje(int tipo, Object... payload) {
        this.tipo = tipo;
        this.payload = payload;
    }
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Object getPayload() {
        return payload;
    }
    
    public void setPayload(Object payload) {
        this.payload = payload;
    }
    
    public void setPayloads(Object... payload) {
        this.payload = payload;
    }
    
    
}
