package backend.presentaciones;

import java.io.Serializable;

/**
 * Diferentes tipos de estados en los que puede encontrarse el alumno mientras esta viendo una presentacion.
 *
 * @author Denise
 */
public enum EstadoPresentacion implements Serializable {
    
    AUTENTICADO("Autenticado"),
    CONECTADO("Conectado"),
    DESCONECTADO("Desconectado"),
    INTERRUMPIDO("Interrumpido");
    
    private final String value;

    /**
     * Constructor de la enumeración.
     *
     * @param value valor a almacenar.
     */
    private EstadoPresentacion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static EstadoPresentacion getEnum(String value) {
        for (EstadoPresentacion v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

}
