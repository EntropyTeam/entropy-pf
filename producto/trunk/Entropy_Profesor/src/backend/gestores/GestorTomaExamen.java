package backend.gestores;

import backend.Seguridad.GestorSeguridadAutenticacion;
import backend.auxiliares.Mensajes;
import backend.dao.resoluciones.DAOResolucion;
import backend.dao.usuarios.DAOAlumno;
import backend.examenes.Examen;
import backend.usuarios.Alumno;
import backend.red.HiloSocketProfesorPorAlumno;
import backend.red.HiloSocketProfesorConexion;
import backend.red.Mensaje;
import backend.red.ParsearRoute;
import backend.red.TipoMensaje;
import backend.resoluciones.Resolucion;
import backend.usuarios.Usuario;
import frontend.tomaexamenes.DialogInfoAlumno;
import frontend.tomaexamenes.FrameControlTomaExamen;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase responsable de gestionar la toma de un examen en el módulo profesor.
 *
 * @author Denise
 */
public class GestorTomaExamen {

    private final FrameControlTomaExamen frmControlTomaExamen;
    private final HiloSocketProfesorConexion hiloSocketConexion;
    private final ArrayList<HiloSocketProfesorPorAlumno> colHilosSocketsAlumnos;
    private final Examen examenResolver;

    public GestorTomaExamen(FrameControlTomaExamen frmControlTomaExamen, Examen examenResolver) {
        this.frmControlTomaExamen = frmControlTomaExamen;
        this.hiloSocketConexion = new HiloSocketProfesorConexion(this);
        this.colHilosSocketsAlumnos = new ArrayList<>();
        this.examenResolver = examenResolver;
    }

    public Examen getExamenResolver() {
        return examenResolver;
    }

    /**
     * Metodo que ejecuta el run hilo del profesor y pone a escuchar el socket
     * servidor para acceptar las conexiones entrantes.
     */
    public void comenzarConexiones() {
        hiloSocketConexion.start();
    }

    /**
     * Metodo que cierra el socket servidor e interrumpe el hilo del profesor
     * para que se terminen de aceptar conexiones.
     */
    public void pararConexiones() {
        try {
            hiloSocketConexion.pararDeEscuchar();
            hiloSocketConexion.interrupt();
        } catch (IOException ex) {
            // Informar Error
            Logger.getLogger(GestorTomaExamen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que acepta una conexión entrante por parte de un alumno y crea un
     * hilo de conexion para ese alumno y lo agrega a la lista.
     *
     * @param accept socket que se utilizara para la comunicación con el alumno.
     */
    public void aceptarConexion(Socket accept) {
        HiloSocketProfesorPorAlumno hiloSocketPorAlumno = new HiloSocketProfesorPorAlumno(accept, this);
        colHilosSocketsAlumnos.add(hiloSocketPorAlumno);
        hiloSocketPorAlumno.start();
    }

    /**
     * Toma un alumno y llama a la ventana para agregarlo en la lista de alumnos
     * que van a resolver el examen.
     *
     * @param alumno el alumno a agregar en la lista
     * @return el índice con el cúal se va a identificar al socket del alumno en
     * la tabla de control de examen.
     */
    public int agregarAlumno(Alumno alumno) {
        
        //Agrego el codigo unico al alumno que luego debe ser comunicado de nuevo al alumno.
        GestorSeguridadAutenticacion gestorSeguridadAutenticacion = new GestorSeguridadAutenticacion();
        String codigo = gestorSeguridadAutenticacion.generarCodigoAlfNum();
        alumno.setStrCodigo(codigo);
        
        // Le asignamos el ID
        alumno.setIntAlumnoId(new DAOAlumno().getAlumnoId(alumno));
        
        int indice = frmControlTomaExamen.agregarAlumno(alumno);
        try {
            Usuario profesor = GestorConfiguracion.getInstancia().getIDAOUsuarios().getUsuario();
            profesor.setStrIP(ParsearRoute.getInstance().getLocalIPAddress());
            Mensaje mensaje = new Mensaje(TipoMensaje.APROBAR_CONEXION_Y_ENVIAR_EXAMEN, examenResolver, profesor);
            colHilosSocketsAlumnos.get(indice).enviarMensaje(mensaje);
        } catch (IOException e) {
            frmControlTomaExamen.mostrarErrorAlEnviarExamen(indice);
            System.out.println(e.toString());
        }

        return indice;
    }

    public void interrumpirAlumno(int intIndice) {
        frmControlTomaExamen.interrumpirAlumno(intIndice);
    }

    public void iniciarExamen(int intIndice) {
        frmControlTomaExamen.iniciarExamen(intIndice);
    }

    public void notificarProgreso(int intIndice, int intCantPreguntasRespondidas) {
        frmControlTomaExamen.notificarProgreso(intIndice, intCantPreguntasRespondidas);
    }

    public void finalizarExamenAlumno(int intIndice, Resolucion resolucion) {
        frmControlTomaExamen.finalizarExamenAlumno(intIndice);
        resolucion.setAlumno(colHilosSocketsAlumnos.get(intIndice).getAlumno());
        if (!(new DAOResolucion().guardarResolucion(resolucion))) {
            System.err.println("PROBLEMAS AL GUARDAR LA RESOLUCIÓN.");
        }
    }

    public void notificarCancelacionExamen() {
        for (HiloSocketProfesorPorAlumno hiloAlumno : colHilosSocketsAlumnos) {
            hiloAlumno.notificarCancelacionExamen();
        }
    }

    public void notificarFinalizacionExamen() {
        for (HiloSocketProfesorPorAlumno hiloAlumno : colHilosSocketsAlumnos) {
            hiloAlumno.notificarFinalizacionExamen();
        }
    }

    public boolean anularResolucion(int intIndiceAlumno, String strJustificacion) {
        return colHilosSocketsAlumnos.get(intIndiceAlumno).notificarAnulacionResolucion(strJustificacion);
    }

    public void confirmarAnulacionResolucion(Resolucion resolucion) {
        if (new DAOResolucion().guardarResolucion(resolucion)) {
            Mensajes.mostrarExito("¡Anulación exitosa!");
        } else {
            Mensajes.mostrarError("Problemas al anular el examen del alumno.");
        }
    }

    public void mostrarDatosAlumno(int intIndiceAlumno) {
        new DialogInfoAlumno(frmControlTomaExamen, true, colHilosSocketsAlumnos.get(intIndiceAlumno).getAlumno()).setVisible(true);
    }

    public void notificarAutenticacionExitosa(int intIndice) {
        frmControlTomaExamen.notificarAutenticacionExitosa(intIndice);
    }
    
    public void agregarTiempo(int intMinutosAgregados) {
        Mensaje mensaje = new Mensaje(TipoMensaje.AGREGAR_TIEMPO_EXAMEN, intMinutosAgregados);
        for (HiloSocketProfesorPorAlumno hiloSocketProfesorPorAlumno : colHilosSocketsAlumnos) {
            try {
                hiloSocketProfesorPorAlumno.enviarMensaje(mensaje);
            } catch (IOException ex) {
                Logger.getLogger(GestorTomaExamen.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("ERROR AL AGREGAR TIEMPO AL ALUMNO " + hiloSocketProfesorPorAlumno.getIndice());
            }
        }
    }
    
    public void quitarTiempo(int intMinutosQuitados) {
        Mensaje mensaje = new Mensaje(TipoMensaje.QUITAR_TIEMPO_EXAMEN, intMinutosQuitados);
        for (HiloSocketProfesorPorAlumno hiloSocketProfesorPorAlumno : colHilosSocketsAlumnos) {
            try {
                hiloSocketProfesorPorAlumno.enviarMensaje(mensaje);
            } catch (IOException ex) {
                Logger.getLogger(GestorTomaExamen.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("ERROR AL QUITAR TIEMPO AL ALUMNO " + hiloSocketProfesorPorAlumno.getIndice());
            }
        }
    }
}
