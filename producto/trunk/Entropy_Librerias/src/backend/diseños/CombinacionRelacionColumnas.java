package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class CombinacionRelacionColumnas implements Serializable {
    
    private int intOrden;
    private String strColumnaIzquierda;
    private String strColumnaDerecha;

    public CombinacionRelacionColumnas() {
    }

    public CombinacionRelacionColumnas(int intOrden, String strColumnaIzquierda, String strColumnaDerecha) {
        this.intOrden = intOrden;
        this.strColumnaIzquierda = strColumnaIzquierda;
        this.strColumnaDerecha = strColumnaDerecha;
    }

    public int getIntOrden() {
        return intOrden;
    }

    public void setIntOrden(int intOrden) {
        this.intOrden = intOrden;
    }

    public String getStrColumnaIzquierda() {
        return strColumnaIzquierda;
    }

    public void setStrColumnaIzquierda(String strColumnaIzquierda) {
        this.strColumnaIzquierda = strColumnaIzquierda;
    }

    public String getStrColumnaDerecha() {
        return strColumnaDerecha;
    }

    public void setStrColumnaDerecha(String strColumnaDerecha) {
        this.strColumnaDerecha = strColumnaDerecha;
    }
}
