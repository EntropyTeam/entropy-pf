package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class Tema implements Serializable {
    
    private int intTemaId;
    private String strNombre;

    public Tema() {
        intTemaId = -1;
    }

    public Tema(String strNombre) {
        intTemaId = -1;
        this.strNombre = strNombre;
    }

    public Tema(int intTemaId, String strNombre) {
        this.intTemaId = intTemaId;
        this.strNombre = strNombre;
    }

    public int getIntTemaId() {
        return intTemaId;
    }

    public void setIntTemaId(int intTemaId) {
        this.intTemaId = intTemaId;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    @Override
    public String toString() {
        return this.strNombre;
    }
}
