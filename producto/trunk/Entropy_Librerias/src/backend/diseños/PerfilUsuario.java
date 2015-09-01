package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class PerfilUsuario implements Serializable {

    private String strTipoDocumento;
    private int intDocumento;
    private String strNombre;
    private String strApellido;
    private String strEmail;
    private String strPerfilUsuarioId;
    private String strContraseña;
    private String strIp;

    public PerfilUsuario() {
    }

    public PerfilUsuario(String strPerfilUsuarioId, String strContraseña) {
        this.strPerfilUsuarioId = strPerfilUsuarioId;
        this.strContraseña = strContraseña;
    }

    public PerfilUsuario(String strTipoDocumento, int intDocumento, String strNombre, String strApellido, String strEmail, String strPerfilUsuarioId, String strContraseña) {
        this.strTipoDocumento = strTipoDocumento;
        this.intDocumento = intDocumento;
        this.strNombre = strNombre;
        this.strApellido = strApellido;
        this.strEmail = strEmail;
        this.strPerfilUsuarioId = strPerfilUsuarioId;
        this.strContraseña = strContraseña;
    }

    public String getStrTipoDocumento() {
        return strTipoDocumento;
    }

    public void setStrTipoDocumento(String strTipoDocumento) {
        this.strTipoDocumento = strTipoDocumento;
    }

    public int getIntDocumento() {
        return intDocumento;
    }

    public void setIntDocumento(int intDocumento) {
        this.intDocumento = intDocumento;
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

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrPerfilUsuarioId() {
        return strPerfilUsuarioId;
    }

    public void setStrPerfilUsuarioId(String strPerfilUsuarioId) {
        this.strPerfilUsuarioId = strPerfilUsuarioId;
    }

    public String getStrContraseña() {
        return strContraseña;
    }

    public void setStrContraseña(String strContraseña) {
        this.strContraseña = strContraseña;
    }

    public String getStrIp() {
        return strIp;
    }

    public void setStrIp(String strIp) {
        this.strIp = strIp;
    }

    @Override
    public String toString() {
        return this.strNombre + " " + this.strApellido + "(" + this.strPerfilUsuarioId + ")";
    }
}
