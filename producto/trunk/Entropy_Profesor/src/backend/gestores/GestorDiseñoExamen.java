package backend.gestores;

import backend.auxiliares.Mensajes;
import backend.dao.diseños.DAOCurso;
import backend.dao.diseños.DAODiseñoExamen;
import backend.dao.diseños.DAOInstitucion;
import backend.dao.diseños.DAOPregunta;
import backend.dao.diseños.DAOTag;
import backend.dao.diseños.DAOTema;
import backend.dao.diseños.IDAOTag;
import backend.diseños.CombinacionRelacionColumnas;
import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Institucion;
import backend.diseños.Pregunta;
import backend.diseños.PreguntaMultipleOpcion;
import backend.diseños.PreguntaNumerica;
import backend.diseños.PreguntaRelacionColumnas;
import backend.diseños.PreguntaVerdaderoFalso;
import backend.diseños.Tema;
import backend.diseños.TipoPregunta;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Lucas Cunibertti
 */
public class GestorDiseñoExamen {

    private DiseñoExamen diseñoExamen;
    private ArrayList<Pregunta> colPreguntas;
    private ArrayList<Tema> colTemas;

    public GestorDiseñoExamen() {
        this.diseñoExamen = new DiseñoExamen();
        this.colPreguntas = new ArrayList<>();
        this.diseñoExamen.setColPreguntas(this.colPreguntas);
        this.colTemas = new ArrayList<>();
    }

    public GestorDiseñoExamen(DiseñoExamen diseñoExamen) {
        this.diseñoExamen = diseñoExamen;
        this.colPreguntas = diseñoExamen.getColPreguntas();
        this.colTemas = buscarTemasDiseñoSeleccionado(diseñoExamen.getIntDiseñoExamenId());
    }

    public DiseñoExamen getDiseñoExamen() {
        return diseñoExamen;
    }

    public void agregarPregunta(Pregunta p) {
        p.setIntOrden(colPreguntas.size() + 1);
        colPreguntas.add(p);
    }

    public void eliminarPregunta(int intIndex) {
        colPreguntas.remove(intIndex);

        for (int i = intIndex; i < colPreguntas.size(); i++) {
            colPreguntas.get(i).setIntOrden(i + 1);
        }
    }

    public void agregarTema(Tema t) {
        colTemas.add(t);
    }
    /*
     *Metodo para buscar los temas de acuerdo a los filtros cargados
     */

    public ArrayList<Tema> getTemasPorFiltro(String strFiltro) {
        if (strFiltro.isEmpty()) {
            return colTemas;
        }

        ArrayList<Tema> colTemasFiltro = new ArrayList<>();
        for (Tema t : colTemas) {
            if (t.getStrNombre().toLowerCase().contains(strFiltro.toLowerCase())) {
                colTemasFiltro.add(t);
            }
        }
        return colTemasFiltro;
    }

    public ArrayList<Institucion> getInstitucionesPorFiltro(String strFiltro) {
        DAOInstitucion daoInstitucion = new DAOInstitucion();
        ArrayList<Institucion> colInstituciones = daoInstitucion.recuperarTodasLasInstituciones(strFiltro);

        if (strFiltro.isEmpty()) {
            return colInstituciones;
        }

        ArrayList<Institucion> colInstitucionesFiltro = new ArrayList<>();
        for (Institucion i : colInstituciones) {
            if (i.getStrNombre().toLowerCase().contains(strFiltro.toLowerCase())) {
                colInstitucionesFiltro.add(i);
            }
        }
        return colInstitucionesFiltro;
    }

    public ArrayList<Curso> getCursosPorFiltro(String strFiltro, Institucion i) {
        DAOCurso daoCurso = new DAOCurso();
        ArrayList<Curso> colCursos = daoCurso.recuperarTodosLosCursos(i, strFiltro);

        if (strFiltro.isEmpty()) {
            return colCursos;
        }

        ArrayList<Curso> colCursosFiltro = new ArrayList<>();
        for (Curso c : colCursos) {
            if (c.getStrNombre().toLowerCase().contains(strFiltro.toLowerCase())) {
                colCursosFiltro.add(c);
            }
        }
        return colCursosFiltro;
    }

    public void guardarDiseñoExamen() {
        DAODiseñoExamen daoDiseñoExamen = new DAODiseñoExamen();
        daoDiseñoExamen.guardarDiseñoExamen(diseñoExamen);
    }

    public boolean validarDatosPreguntas() {
        for (Pregunta p : colPreguntas) {
            if (p.getStrEnunciado() == null || p.getStrEnunciado().isEmpty()) {
                Mensajes.mostrarError("Hay preguntas que no tienen enunciado.");
                return false;
            }

            if (p instanceof PreguntaMultipleOpcion) {
                PreguntaMultipleOpcion pMO = (PreguntaMultipleOpcion) p;
                if (pMO.getColOpciones().size() < 2) {
                    Mensajes.mostrarError("Hay preguntas múltiple opción con menos de dos opciones.");
                    return false;
                }
            }

            if (p instanceof PreguntaRelacionColumnas) {
                PreguntaRelacionColumnas pRC = (PreguntaRelacionColumnas) p;

                if (pRC.getColCombinaciones().size() < 2) {
                    Mensajes.mostrarError("Hay preguntas de relación de columnas con menos de dos combinaciones de respuestas.");
                    return false;
                }

                for (CombinacionRelacionColumnas cRC : pRC.getColCombinaciones()) {
                    if (cRC.getStrColumnaIzquierda() == null
                            || cRC.getStrColumnaIzquierda().isEmpty()
                            || cRC.getStrColumnaDerecha() == null
                            || cRC.getStrColumnaDerecha().isEmpty()) {
                        Mensajes.mostrarError("Hay preguntas de relación de columnas con datos faltantes.");
                        return false;
                    }
                }
            }

            if (p instanceof PreguntaNumerica) {
                PreguntaNumerica pn = (PreguntaNumerica) p;
                if (pn.esRango()) {
                    if (pn.getDblRangoDesde() >= pn.getDblRangoHasta()) {
                        Mensajes.mostrarError("Hay preguntas numéricas de rango con un límite superior de menor o igual valor que el límite inferior.");
                        return false;
                    }
                    if (pn.getDblRangoDesde() > Double.MAX_VALUE || pn.getDblRangoHasta() > Double.MAX_VALUE) {
                        Mensajes.mostrarError("Hay preguntas numéricas con valores superiores a " + Double.MAX_VALUE + ".");
                        return false;
                    }
//                    if (pn.getDblRangoDesde() < Double.MIN_VALUE || pn.getDblRangoHasta() < Double.MIN_VALUE){
//                        Mensajes.mostrarError("Hay preguntas numéricas con valores inferiores a "+Double.MIN_VALUE+".");
//                        return false;
//                    }

                } else {
                    if (pn.getDblNumero() > Double.MAX_VALUE) {
                        Mensajes.mostrarError("Hay preguntas numéricas con valores superiores a " + Double.MAX_VALUE + ".");
                        return false;
                    }
//                    if (pn.getDblNumero() < Double.MIN_VALUE){
//                        Mensajes.mostrarError("Hay preguntas numéricas con valores inferiores a "+Double.MIN_VALUE+".");
//                        return false;
//                    }
                }
            }

        }
        return true;
    }

    public Pregunta getPreguntaSeleccionada(int intIndex) {
        return colPreguntas.get(intIndex);
    }

    public void setPreguntaSeleccionada(int intIndex, Pregunta p) {
        colPreguntas.set(intIndex, p);
    }

    public ArrayList<Pregunta> getColPreguntas() {
        return colPreguntas;
    }

    public ArrayList<Tema> getColTemas() {
        return colTemas;
    }

    public void setColTemas(ArrayList<Tema> colTemas) {
        this.colTemas = colTemas;
    }

    public Pregunta clonarPregunta(Pregunta aClonar) {
        Pregunta clonada = new Pregunta();

        switch (aClonar.getIntTipo()) {
            case TipoPregunta.DESARROLLAR:
                aClonar.clone(clonada);
                break;
            case TipoPregunta.MULTIPLE_OPCION:
                clonada = ((PreguntaMultipleOpcion) aClonar).clone();
                break;
            case TipoPregunta.VERDADERO_FALSO:
                clonada = ((PreguntaVerdaderoFalso) aClonar).clone();
                break;
            case TipoPregunta.NUMERICA:
                clonada = ((PreguntaNumerica) aClonar).clone();
                break;
            case TipoPregunta.RELACION_COLUMNAS:
                clonada = ((PreguntaRelacionColumnas) aClonar).clone();
        }

        return clonada;
    }

    public void cambiarOrdenPregunta(int intIndexEste, int intIndexPorEste) {
        Pregunta esta = colPreguntas.get(intIndexEste);
        Pregunta porEsta = colPreguntas.get(intIndexPorEste);

        colPreguntas.set(intIndexEste, porEsta);
        colPreguntas.set(intIndexPorEste, esta);

        esta.setIntOrden(intIndexPorEste + 1);
        porEsta.setIntOrden(intIndexEste + 1);
    }

    /**
     * Devuelve un objeto que implementa a la interfaz <code>IDAOTag</code>.
     *
     * @return interfaz dao par tags
     */
    public IDAOTag getGestorTags() {
        return new DAOTag();
    }

    /*
     * Metodo para buscar una institucion de acuerdo a un numero de diseño de examen
     * @param idDiseño, el id del diseño del examen seleccionado para editar
     */
    public Institucion buscarInstitucion(int idDiseño) {
        DAOInstitucion daoInstitucion = new DAOInstitucion();
        return daoInstitucion.buscarInstitucion(idDiseño);
    }

    /*
     * Metodo para buscar la lista de preguntas de acuerdo a un numero de diseño de examen
     * @param idDiseño, el id del diseño del examen seleccionado para editar
     */
    public ArrayList<Pregunta> getPreguntasDiseño() {
        ArrayList<Integer> colId = new ArrayList<>();
        DAOPregunta daoPregunta = new DAOPregunta();
        ArrayList<Pregunta> colPreguntas = daoPregunta.getPreguntasPorDiseñoExamen(diseñoExamen);
        for (Pregunta pregunta : colPreguntas) {
            colId.add(pregunta.getIntPreguntaId());
        }
        return daoPregunta.getPreguntasPorID(colId);
    }

    /*
     * Metodo para buscar la lista de preguntas de acuerdo a un numero de diseño de examen, no fue tenido en cueenta el parametro exterior
     * @param idDiseño
     */
    public ArrayList<Pregunta> getPreguntasDiseño(DiseñoExamen diseExamen) {
        ArrayList<Integer> colId = new ArrayList<>();
        DAOPregunta daoPregunta = new DAOPregunta();
        ArrayList<Pregunta> colPreguntas = daoPregunta.getPreguntasPorDiseñoExamen(diseExamen);
        for (Pregunta pregunta : colPreguntas) {
            colId.add(pregunta.getIntPreguntaId());
        }
        return daoPregunta.getPreguntasPorID(colId);
    }

    /*
     * Metodo para setear la lista de preguntas de un diseño de examen
     * @param colPreguntas ArraayList de preguntas
     */
    public void setColPreguntas(ArrayList<Pregunta> colPreguntas) {
        this.colPreguntas = colPreguntas;
        this.diseñoExamen.setColPreguntas(colPreguntas);

        for (Pregunta pregunta : colPreguntas) {
            for (Tema tema : colTemas) {
                if (pregunta.getTema() != null && pregunta.getTema().getIntTemaId() == tema.getIntTemaId()) {
                    pregunta.setTema(tema);
                }
            }
        }
    }

    /*
     * Metodo para buscar un curso de acuerdo a un numero de diseño de examen
     * @param idDiseño, el id del diseño del examen seleccionado para editar
     */
    public Curso buscarCurso(int idDiseño) {
        DAOCurso daoCurso = new DAOCurso();
        return daoCurso.buscarCurso(idDiseño);
    }

    /*
     * Metodo para eliminar un diseño de examen de la base de datos
     * @param idDiseño, el id del diseño del examen seleccionado para editar
     */
    public void eliminarDiseño() throws SQLException {
        DAOPregunta daoPregunta = new DAOPregunta();
        daoPregunta.eliminarPreguntas(diseñoExamen);
        DAODiseñoExamen daoDiseñoExamen = new DAODiseñoExamen();
        daoDiseñoExamen.borrarDiseñoExamen(diseñoExamen.getIntDiseñoExamenId());

    }

    private ArrayList<Tema> buscarTemasDiseñoSeleccionado(int intDiseñoExamenId) {
        return (new DAOTema()).getTemasPorExamen(intDiseñoExamenId);
    }

    /**
     * Verifica que todas las preguntas de un diseño tengan un puntaje asociado.
     *
     * @param examenSeleccionado diseño a verificar
     * @return true si todas las preguntas tienen puntaje, false de lo
     * contrario.
     */
    public boolean controlarPuntajesCompletos(DiseñoExamen examenSeleccionado) {
        ArrayList<Pregunta> listaPreguntas = getPreguntasDiseño(examenSeleccionado);
        if (listaPreguntas.isEmpty()) {
            return false;
        }
        for (Pregunta pregunta : listaPreguntas) {
            if (pregunta.getDblPuntaje() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isNuevoExamen() {
        return this.diseñoExamen.isNuevoExamen();
    }

}
