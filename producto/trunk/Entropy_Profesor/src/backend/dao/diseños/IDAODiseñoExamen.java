package backend.dao.diseños;

import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones necesarias en los manejadores DAO de los
 * diseños de examenes almacenados en la base de datos.
 * 
 * @author Lucas Cunibertti comentada por Gaston Noves
 */
public interface IDAODiseñoExamen {
    
    /**
     * Obtiene todos los diseños de examenes almacenados.
     * @return lista de diseños de examenes almacenados.
     */
    public ArrayList<DiseñoExamen> getDiseñosExamenes();
    
    /**
     * Obtiene un diseño de examen almacenado.
     * @param intDiseñoExamenId identificador del diseño de examen.
     * @return el examen que contiene ese identificador.
     */
    public DiseñoExamen getDiseñoExamen(int intDiseñoExamenId);
    
    /**
     * Obtiene todos los diseños de examen con el titulo pasado por parametro.
     * @param strTitulo string con el cual comparar los diseños.
     * @return lista de diseños de examenes que contienen la cadena pasada por parámetro.
     */
    public ArrayList<DiseñoExamen> getDiseñosExamenes(String strTitulo);
    /**
     * Obtiene todos los diseños de examen que pertenecen a un curso.
     * @param curso del que quiero obtener los Diseños de Examen.
     * @return lista de diseños de examenes que pertenesen al curso.
     */
    public ArrayList<DiseñoExamen> getDiseñosExamenes(Curso curso);
    
    /**
     * Almacena un diseño de examen pasado por parámetro.
     * @param diseñoExamen diseño del examen a almacenar.
     */
    public void guardarDiseñoExamen(DiseñoExamen diseñoExamen);
}
