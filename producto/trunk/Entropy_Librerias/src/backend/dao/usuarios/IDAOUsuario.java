package backend.dao.usuarios;

import backend.usuarios.Usuario;

/**
 *
 * @author Denise
 */
public interface IDAOUsuario {

    /**
     * Almacena o altualiza un usuario en la BD.
     *
     * @param usuario Entidad Usuario a almacenar o actualizar.
     * @return true si la operaciòn fue exitosa, false de lo contrario.
     */
    public boolean guardarUsuario(Usuario usuario);

    /**
     * Obtiene un usuario guardado en la BD (por si las dudas se necesita en
     * un futuro).
     *
     * @param strTipoDocumento tipo de documento
     * @param intNroDocumento número de documento
     * @return entidad Usuario almacenada, null de lo contrario.
     */
    public Usuario getUsuario(String strTipoDocumento, int intNroDocumento);

    /**
     * Obtiene el usuario guardado en la BD (el único, puesto que se trata del
     * dueño del equipo).
     *
     * @return entidad Usuario almacenada, null de lo contrario.
     */
    public Usuario getUsuario();

    /**
     * EL único usuario en la BD local es el usuario del sistema. Si no hay uno
     * usuario guardado, al inicio de la aplicación se le debe solicitar al
     * usuario que cargue sus datos.
     *
     * También se puede usar <code>if(getUsuario() == null){}</code>;
     *
     * @return true si hay usuarios guardados, false de lo contrario.
     * @throws java.lang.Exception Error al leer la BD.
     */
    public boolean hayUsuariosGuardados() throws Exception;

}
