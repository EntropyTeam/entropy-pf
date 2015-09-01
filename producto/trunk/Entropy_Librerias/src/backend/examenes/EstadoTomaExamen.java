package backend.examenes;

import java.io.Serializable;

/**
 * Diferentes tipos de estados en los que puede encontrarse el examen mientras
 * se está tomando.
 *
 * @author Denise
 */
public enum EstadoTomaExamen implements Serializable {

    NO_AUTENTICADO("No Autenticado"),
    AUTENTICADO("Autenticado"),
    INICIADO("Iniciado"),
    COMPLETADO("Completado"),
    INTERRUMPIDO("Interrumpido");
    
    private final String value;

    /**
     * Constructor de la enumeración.
     *
     * @param value valor a almacenar.
     */
    private EstadoTomaExamen(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static EstadoTomaExamen getEnum(String value) {
        for (EstadoTomaExamen v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

}
