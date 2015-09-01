package frontend.auxiliares;

/**
 * Clase que representa en conjunto de operaciones a implementar por un panel 
 * que ejecuta una validación al momento de ocultarse del usuario.
 * 
 * @author Denise
 */
public interface IValidarSalida {

    public enum TipoAccion {CONTINUAR, CANCELAR}
    
    /**
     * Antes de ser escondido, el componente ejecuta esta rutina.
     * @return 
     */
    public TipoAccion validarSalida();
    
}
