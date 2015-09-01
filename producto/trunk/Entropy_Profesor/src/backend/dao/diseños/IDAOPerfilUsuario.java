package backend.dao.diseños;

import backend.diseños.PerfilUsuario;

/**
 * Clase que representa el objeto interfaz para la entidad PerfilUsuario.
 * @author Lucas Cunibertti
 */
public interface IDAOPerfilUsuario {

    /**
     * Metodo que almacena un perfil de usuario pasado por parámetro.
     * @param perfilUsuario a almacenar.
     */
    public void guardarPerfilUsuario(PerfilUsuario perfilUsuario);
    
    /**
     * Metodo que verifica un inicio de sesion al sistema.
     * @param strPerfilUsuarioId a comparar.
     * @param strContraseña a comparar.
     * @return el perfil usuario logueado.
     */
    public PerfilUsuario login(String strPerfilUsuarioId, String strContraseña);
}
