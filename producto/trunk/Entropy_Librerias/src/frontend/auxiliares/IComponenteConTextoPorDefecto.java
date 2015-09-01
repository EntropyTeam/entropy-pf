package frontend.auxiliares;

/**
 * Interfaz que estipula los métodos a ser implementados por aquellos
 * componentes de texto que permitan texto por defecto.
 *
 * @author Denise
 */
public interface IComponenteConTextoPorDefecto {

    /**
     * Setea el texto por defecto.
     *
     * @param strTextoPorDefecto texto por defecto
     */
    public void setTextoPorDefecto(String strTextoPorDefecto);

    /**
     * Devuelve el texto por defecto.
     *
     * @return texto por defecto
     */
    public String getTextoPorDefecto();

    
    /**
     * Elimina el texto por defecto y los listener de foco relacionados.
     */
    public void eliminarTextoPorDefecto();
    
    /**
     * Muestra el actual texto por defecto en el display del componente.
     */
    public void mostrarTextoPorDefecto();

    /**
     * Corrobora si actualmente se muestra el texto por defecto, de existir el
     * mismo.
     *
     * @return true si actualmente se muestra el texto por defecto, false de lo
     * contrario.
     */
    public boolean muestraTextoPorDefecto();
    
    /**
     * Devuelve el texto actualmente mostrado.
     *
     * @return texto actualmente mostrado
     */
    public String getText();

    /**
     * Setea el texto a mostrar.
     *
     * @param strTexto texto a mostrar
     */
    public void setText(String strTexto);
    
}
