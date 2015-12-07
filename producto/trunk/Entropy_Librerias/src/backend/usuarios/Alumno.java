package backend.usuarios;

import java.io.Serializable;

/**
 *
 * @author Pelito
 */
public class Alumno extends Usuario implements Serializable {
    
    private int intAlumnoId;
    private String strCodigo;

    public Alumno() {
    }

    public Alumno(String strNombre, String strApellido, String strTipoDocumento, int intNroDocumento, String strEmail, String strLegajo, String strDescripcion, Object imgFoto, String strIP) {
        this.strNombre = strNombre;
        this.strApellido = strApellido;
        this.strTipoDocumento = strTipoDocumento;
        this.intNroDocumento = intNroDocumento;
        this.strEmail = strEmail;
        this.strLegajo = strLegajo;
        this.strDescripcion = strDescripcion;
        this.imgFoto = imgFoto;
        this.strIP = strIP;
    }

    public int getIntAlumnoId() {
        return intAlumnoId;
    }

    public void setIntAlumnoId(int intAlumnoId) {
        this.intAlumnoId = intAlumnoId;
    }

    public String getStrCodigo() {
        return strCodigo;
    }

    public void setStrCodigo(String strCodigo) {
        this.strCodigo = strCodigo;
    }
}
