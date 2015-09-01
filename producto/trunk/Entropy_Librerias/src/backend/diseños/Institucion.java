package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class Institucion implements Serializable {

    private int intInstitucionId;
    private String strNombre;
    private String strDescripcion;
    private Object imgLogo;

    public Institucion() {
        this.intInstitucionId = -1;
    }

    /**
     * Constructor de la clase.
     *
     * @param strNombre nombre o titulo del diseño de examen.
     */
    public Institucion(String strNombre) {
        this.intInstitucionId = -1;
        this.strNombre = strNombre;
    }

    public Institucion(String strNombre, String strDescripcion, Object imgLogo) {
        this.intInstitucionId = -1;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.imgLogo = imgLogo;
    }

    public Institucion(String strNombre, Object imgLogo) {
        this.intInstitucionId = -1;
        this.strNombre = strNombre;
        this.imgLogo = imgLogo;
    }

    public Institucion(int intInstitucionId, String strNombre) {
        this.intInstitucionId = intInstitucionId;
        this.strNombre = strNombre;
    }

    public Institucion(int intInstitucionId, String strNombre, String strDescripcion, Object imgLogo) {
        this.intInstitucionId = intInstitucionId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
        this.imgLogo = imgLogo;
    }

    public Institucion(int intInstitucionId, String strNombre, String strDescripcion) {
        this.intInstitucionId = intInstitucionId;
        this.strNombre = strNombre;
        this.strDescripcion = strDescripcion;
    }

    public int getIntInstitucionId() {
        return intInstitucionId;
    }

    public void setIntInstitucionId(int intInstitucionId) {
        this.intInstitucionId = intInstitucionId;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public Object getImgLogo() {
        return imgLogo;
    }

    public void setImgLogo(Object imgLogo) {
        this.imgLogo = imgLogo;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    @Override
    public String toString() {
        return this.strNombre;
    }
       
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Institucion)) return false;
        Institucion institucion=(Institucion) obj;
        return institucion.getIntInstitucionId()==this.getIntInstitucionId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.intInstitucionId;
        return hash;
    }
}
