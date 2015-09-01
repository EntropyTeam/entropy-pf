package backend.gestores;

import backend.dao.usuarios.DAOUsuario;
import backend.dao.usuarios.IDAOUsuario;

/**
 * Gestiona diferentes configuraciones de la aplicaciòn: redes, datos de 
 * usuario, etc.
 * 
 * @author Denise
 */
public class GestorConfiguracion {
    
    private static GestorConfiguracion INSTANCIA = null;
    
    
    /**
     * Constructor por defecto.
     */
    private GestorConfiguracion() {}
    
    private synchronized static void createInstance() {
        if (INSTANCIA == null) {
            INSTANCIA = new GestorConfiguracion();
        }
    }

    public static GestorConfiguracion getInstancia() {
        createInstance();
        return INSTANCIA;
    }

    public IDAOUsuario getIDAOUsuarios() {
        return new DAOUsuario();
    }
}
