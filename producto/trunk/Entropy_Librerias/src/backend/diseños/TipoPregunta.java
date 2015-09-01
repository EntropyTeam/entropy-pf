package backend.diseños;

import java.io.Serializable;

/**
 *
 * @author Lucas Cunibertti
 */
public class TipoPregunta implements Serializable {
    
    public static final int DESARROLLAR = 1;
    public static final int MULTIPLE_OPCION = 2;
    public static final int VERDADERO_FALSO = 3;
    public static final int RELACION_COLUMNAS = 4;
    public static final int NUMERICA = 5;
}
