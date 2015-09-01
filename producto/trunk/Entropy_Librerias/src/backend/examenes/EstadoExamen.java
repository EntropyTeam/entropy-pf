package backend.examenes;

import java.io.Serializable;

/**
 *
 * @author gaston
 */
public class EstadoExamen implements Serializable {

    public static final int DEFINIDO = 1;
    public static final int DESARROLLANDO = 2;
    public static final int FINALIZADO = 3;
    public static final int CORREGIDO = 4;
    public static final int CANCELADO = 5;
}
