package frontend.auxiliares;

/**
 * Los objetos que implementen esta interfaz deben ser capaces de notar si se ha
 * realizado algún cambio importante en alguno de sus atributos, determinado por
 * el programador.
 * 
 * @author Denise
 */
public interface IIdentificadorCambios {
    
    /**
     * Determina si el objeto o alguno de sus atributos a cambiado en algo.
     * 
     * @return true de haber cambiado, false de los contrario.
     */
    public boolean seModifico();
}
