package backend.usuarios;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase para el manejo de la información acera de un usuario profesor o alumno.
 * 
 * @author denise
 */
public class Usuario implements Serializable  {

    protected String strNombre;
    protected String strApellido;
    protected String strTipoDocumento;
    protected int intNroDocumento;
    protected String strEmail;
    protected String strDescripcion;
    protected String strLegajo;
    protected Object imgFoto;
    protected String strIP;

    public Usuario() {
        this.intNroDocumento = -1;        
    }

    public Usuario(String strNombre, String strApellido, String strTipoDocumento, int intNroDocumento, String strEmail, String strLegajo) {
        this.strNombre = strNombre;
        this.strApellido = strApellido;
        this.strTipoDocumento = strTipoDocumento;
        this.intNroDocumento = intNroDocumento;
        this.strEmail = strEmail;
        this.strLegajo = strLegajo;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrApellido() {
        return strApellido;
    }

    public void setStrApellido(String strApellido) {
        this.strApellido = strApellido;
    }

    public String getStrTipoDocumento() {
        return strTipoDocumento;
    }

    public void setStrTipoDocumento(String strTipoDocumento) {
        this.strTipoDocumento = strTipoDocumento;
    }

    public int getIntNroDocumento() {
        return intNroDocumento;
    }

    public void setIntNroDocumento(int intNroDocumento) {
        this.intNroDocumento = intNroDocumento;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrLegajo() {
        return strLegajo;
    }

    public void setStrLegajo(String strLegajo) {
        this.strLegajo = strLegajo;
    }

    public Object getImgFoto() {
        return imgFoto;
    }

    public void setImgFoto(Object imgFoto) {
        this.imgFoto = imgFoto;
    }

    public String getStrIP() {
        return strIP;
    }

    public void setStrIP(String strIP) {
        this.strIP = strIP;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.strTipoDocumento);
        hash = 17 * hash + this.intNroDocumento;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.strTipoDocumento, other.strTipoDocumento)) {
            return false;
        }
        if (this.intNroDocumento != other.intNroDocumento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return strApellido.toUpperCase() + ", " + strNombre + " - " + strTipoDocumento + " " + intNroDocumento + ((strLegajo == null) ? "" : strLegajo);
    }

}
