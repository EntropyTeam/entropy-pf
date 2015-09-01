package backend.resoluciones;

import backend.usuarios.Usuario;
import java.io.Serializable;

/**
 *
 * @author Pelito
 */
public class Alumno extends Usuario implements Serializable {

    private String strCodigo;

    public Alumno() {

    }

    public Alumno(String strNombre, String strApellido, String strTipoDocumento, int intNroDocumento, String strEmail, String strLegajo, String strCodigo, String strIP) {
        super(strNombre, strApellido, strTipoDocumento, intNroDocumento, strEmail, strLegajo);
        this.strCodigo = strCodigo;
        this.strIP = strIP;

    }

    public Alumno(String strNombre, String strLegajo, String strCodigo) {
        super();
        this.strNombre = strNombre;
        this.strLegajo = strLegajo;
        this.strCodigo = strCodigo;
    }

    public String getStrCodigo() {
        return strCodigo;
    }

    public void setStrCodigo(String strCodigo) {
        this.strCodigo = strCodigo;
    }

}
