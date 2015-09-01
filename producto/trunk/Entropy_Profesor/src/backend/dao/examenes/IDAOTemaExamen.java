/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backend.dao.examenes;

import backend.diseños.Tema;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Pelito
 */
public interface IDAOTemaExamen {
    
     /**
     * Almacena un tema pasado por parámetro.
     *
     * @param tema tema a almacenar.
     * @param intExamenId el ID del examen,.
     * @param conexion la conexion por ser parte de una transaccion.
     * @throws java.sql.SQLException
     */
    public void guardarTemaExamen(Tema tema, int intExamenId, Connection conexion) throws SQLException;

    /**
     * Verifica si un tema ya existe para un determinado examen en la BD.
     *
     * @param tema el tema a verificar.
     * @param intExamenId id del examen al que pertenece el tema.
     * @param conexion la conexion por ser parte de una transaccion.
     * @return true si el tema ya existe, false de lo contrario
     * @throws java.sql.SQLException
     */
    public boolean existe(Tema tema, int intExamenId, Connection conexion) throws SQLException ;
    
    
    /**
     * Comprueba si un determinado tema para un determinado examen ya posee un
     * ID asignado; es decir, si ese tema ya está guardado en la BD.
     * 
     * @param idExamen ID del examen al cual pertenece el tema.
     * @param strTema descripción del tema.
     * @param conexion conexión a la BD.
     * @return el ID del tema si existe, 0 de lo contrario.
     * @throws SQLException 
     */
    public int getIDsiExiste(int idExamen, String strTema, Connection conexion) throws SQLException;
    
}
