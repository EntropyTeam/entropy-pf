package backend.dao.examenes;

import backend.diseños.Curso;
import backend.examenes.Examen;
import java.util.ArrayList;

/**
 *
 * @author Jose Ruiz
 */
public interface IDAOExamen {

    /**
     * Obtiene todos los examenes almacenados.
     *
     * @return lista examenes almacenados.
     */
    public ArrayList<Examen> getExamenes();

    /**
     * Obtiene un examen almacenado.
     *
     * @param intExamenId identificador del examen.
     * @return el examen que contiene ese identificador.
     */
    public Examen getExamen(int intExamenId);

    /**
     * Obtiene todos los examenes que pertenecen a un curso.
     *
     * @param curso del que quiero obtener los examenes.
     * @return lista de examenes que pertenesen al curso.
     */
    public ArrayList<Examen> getExamenes(Curso curso);

    /**
     * Almacena un examen pasado por parámetro.
     *
     * @param examen diseño del examen a almacenar.
     */
    public void guardarExamen(Examen examen);

    /**
     * Actualiza el estado del examen en la base de datos.
     *
     * @param intEstado nuevo estado del examen.
     * @param intIDExamen id del examen a actualizar.
     * @return true si la actualización fue exitosa, false de lo contrario.
     */
    public boolean actualizarEstado(int intEstado, int intIDExamen);

    /**
     * Chequea si todas las resoluciones han sido corregidas, y de ser el
     * caso cambia el estado del examen a CORREGIDO. Sino, se asigna un estado 
     * FINALIZADO.
     *
     * @param intIDExamen id del examen a actualizar
     * @return true si la actualización fue exitosa, false de lo contrario
     */
    public boolean actualizarEstado(int intIDExamen);
    
    /**
     * Permite cancelar un examen.
     * 
     * @param intExamenId id del examen a cancelar.
     * @param strMotivoCancelacion descripción del motivo de cancelación.
     * @return true si la cancelación fue exitosa, false de lo contrario.
     */
    public boolean cancelarExamen(int intExamenId, String strMotivoCancelacion);
}
