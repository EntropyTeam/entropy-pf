package backend.gestores;

import backend.auxiliares.Mensajes;
import backend.diseños.Pregunta;
import backend.diseños.PreguntaMultipleOpcion;
import backend.diseños.PreguntaNumerica;
import backend.diseños.PreguntaRelacionColumnas;
import backend.diseños.PreguntaVerdaderoFalso;
import backend.examenes.Examen;
import backend.persistencia.GestorPersistencia;
import backend.red.HiloSocketAlumno;
import backend.red.Mensaje;
import backend.red.TipoMensaje;
import backend.usuarios.Alumno;
import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import backend.resoluciones.RespuestaDesarrollo;
import backend.resoluciones.RespuestaPreguntaMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaNumerica;
import backend.resoluciones.RespuestaPreguntaRelacionColumnas;
import backend.resoluciones.RespuestaPreguntaVerdaderoFalso;
import backend.usuarios.Usuario;
import frontend.examenes.DialogRealizarExamen;
import frontend.examenes.DialogValidarCodigo;
import frontend.examenes.PanelIniciarExamen;
import frontend.examenes.PanelPregunta;
import frontend.examenes.PanelRespuesta;
import frontend.inicio.PanelInicio;
import frontend.inicio.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Clase responsable de gestionar la toma de un examen en el módulo alumno.
 *
 * @author Denise
 */
public class GestorResolucionExamen {

    private String ipServidor;
    private int intPuerto;
    private HiloSocketAlumno hiloSocketAlumno;
    private VentanaPrincipal mPadre;
    private Resolucion resolucion;
    private Resolucion resolucionRecuperar;
    private GestorPersistencia gestorPersistencia;
    private DialogRealizarExamen dialogRealizarExamen;
    private Timer timerEspera;
    private Alumno alumno;
    private Usuario profesor;
    private boolean blnValidacion = false;
    private boolean blnFueValidado;

    public GestorResolucionExamen(String ipServidor, int intPuerto) throws  Exception {
        this.ipServidor = ipServidor;
        this.intPuerto = intPuerto;
        iniciarConexion();
    }

    public GestorResolucionExamen(String ipServidor, int intPuerto, Resolucion resolucionRecuperar) throws Exception {
        this.ipServidor = ipServidor;
        this.intPuerto = intPuerto;
        this.resolucionRecuperar = resolucionRecuperar;
        iniciarConexion();
    }

    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public int getIntPuerto() {
        return intPuerto;
    }

    public void setIntPuerto(int intPuerto) {
        this.intPuerto = intPuerto;
    }

    public void setmPadre(VentanaPrincipal mPadre) {
        this.mPadre = mPadre;
    }

    public void iniciarConexion() throws Exception {
        hiloSocketAlumno = new HiloSocketAlumno(ipServidor, intPuerto, this);
        if(hiloSocketAlumno.getSocket()!=null)
        {
            hiloSocketAlumno.start();
        }  
    }

    public void avisarServidorCierre() throws IOException {
        Mensaje mensaje = new Mensaje(TipoMensaje.DESCONECTAR_CLIENTE);
        hiloSocketAlumno.enviarMensaje(mensaje);
        hiloSocketAlumno.interrupt();
        hiloSocketAlumno.cerrarSocket();
    }

    public void conectarAlumno(Alumno alumno) throws IOException {
        this.alumno = alumno;
        Mensaje mensaje = new Mensaje(TipoMensaje.CONECTAR_CLIENTE_EXAMEN, alumno);
        hiloSocketAlumno.enviarMensaje(mensaje);
    }

    public boolean estaConectado() {
        return hiloSocketAlumno.getSocket() != null || hiloSocketAlumno.getSocket().isConnected();
    }

    /**
     * Notiica al alumno que se ha recibido un examen.
     *
     * @param examen examen recibido.
     */
    public void notificarRecepcionExamen(Examen examen) {
        if (examen == null) {
            Mensajes.mostrarError("Ha ocurrido un error al recibir el examen.");
            return;
        }
        if (resolucionRecuperar == null) {
            resolucion = new Resolucion();
            resolucion.setExamen(examen);
            resolucion.setAlumno(alumno);
        } else {
            if (resolucionRecuperar.getExamen().getIntExamenId() != examen.getIntExamenId()) {
                Mensajes.mostrarError("El examen recibido no coincide con el examen que desea reanudar.");
                return;
            }

            if (resolucionRecuperar.getAlumno().getIntNroDocumento() != alumno.getIntNroDocumento()) {
                Mensajes.mostrarError("El alumno del examen a reanudar no coincide con el alumno de esta sesión.");
                return;
            }

            resolucion = resolucionRecuperar;
        }
        gestorPersistencia = new GestorPersistencia();
        gestorPersistencia.guardarResolucion(this.resolucion);
        timerEspera = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelInicio();
            }
        });
        timerEspera.start();
    }

    /**
     * Muestra el panel de notificación de examen recibido al alumno.
     */
    private void mostrarPanelInicio() {
        timerEspera.stop();
        PanelIniciarExamen pnlIniciarExamen = new PanelIniciarExamen(this);
        pnlIniciarExamen.setName("Iniciar Examen");
        mPadre.getPanelDeslizante().setPanelMostrado(pnlIniciarExamen);
        mPadre.setTitle("Iniciar Examen");
        if (mPadre.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            mPadre.pack();
        }
    }

    /**
     * Retorna el examen que se está tomando.
     *
     * @return el examn que se está tomando.
     */
    public Examen getExamen() {
        if (this.resolucion != null) {
            return this.resolucion.getExamen();
        }
        return null;
    }

    /**
     * Comienza el examen. Comunica al profesor que se inició el examen, prepara
     * las respuestas para cada pregunta, y las muestra al alumno.
     *
     * @throws IOException excepcion si es imposible comunicarse con el
     * profesor.
     */
    public void comenzarExamen(String codigo) throws IOException {
        
        //valido el codigo con el servidor (MAndo mensajes de ida y vuelta)
        Mensaje mnsValidarAlumno = new Mensaje(TipoMensaje.VALIDAR_ALUMNO, codigo);
        hiloSocketAlumno.enviarMensaje(mnsValidarAlumno);
        DialogValidarCodigo validarAlumno = new DialogValidarCodigo(mPadre, this);
        validarAlumno.setVisible(true);
        if (this.blnValidacion == false) {
            return;
        }

        // Se avisa al servidor
        Mensaje mnsAvisarComienzo = new Mensaje(TipoMensaje.INICIAR_EXAMEN);
        hiloSocketAlumno.enviarMensaje(mnsAvisarComienzo);

        if (resolucionRecuperar == null) {
            // Se crean los objetos que albergarán las respuestas.
            ArrayList<Respuesta> colRespuestas = new ArrayList<>();

            for (Pregunta pregunta : getExamen().getColPreguntas()) {

                if (pregunta instanceof PreguntaMultipleOpcion) {

                    RespuestaPreguntaMultipleOpcion rtaPMO = new RespuestaPreguntaMultipleOpcion((PreguntaMultipleOpcion) pregunta);
                    colRespuestas.add(rtaPMO);

                } else if (pregunta instanceof PreguntaNumerica) {

                    RespuestaPreguntaNumerica rtaNum = new RespuestaPreguntaNumerica((PreguntaNumerica) pregunta);
                    colRespuestas.add(rtaNum);

                } else if (pregunta instanceof PreguntaRelacionColumnas) {

                    RespuestaPreguntaRelacionColumnas rtaCol = new RespuestaPreguntaRelacionColumnas((PreguntaRelacionColumnas) pregunta);
                    colRespuestas.add(rtaCol);

                } else if (pregunta instanceof PreguntaVerdaderoFalso) {

                    RespuestaPreguntaVerdaderoFalso rtaVF = new RespuestaPreguntaVerdaderoFalso((PreguntaVerdaderoFalso) pregunta);
                    colRespuestas.add(rtaVF);

                } else if (pregunta instanceof Pregunta) {

                    RespuestaDesarrollo rtaDesarrollo = new RespuestaDesarrollo(pregunta);
                    colRespuestas.add(rtaDesarrollo);

                }
            }

            if (getExamen().getOrdenPresentacion().equals(Examen.OrdenLista.ALEATORIO)) {
                aleatorizarColeccion(colRespuestas);
            } else if (getExamen().getOrdenPresentacion().equals(Examen.OrdenLista.POR_TEMA)) {
                ordenarPorTema(colRespuestas);
            }

            resolucion.setColRespuestas(colRespuestas);
        }
        mPadre.setTitle("Examen iniciado: " + getExamen().getStrNombre());
        PanelPregunta pnlPreguntas = new PanelPregunta(mPadre, this);
        pnlPreguntas.setName("Preguntas");
        VentanaPrincipal.getInstancia().volverAInicio();
        dialogRealizarExamen = new DialogRealizarExamen(mPadre, true, pnlPreguntas);
        dialogRealizarExamen.setVisible(true);
    }

    /**
     * Devuelve la colección de respuestas.
     *
     * @return colección de respuestas.
     */
    public ArrayList<Respuesta> getRespuestas() {
        if (this.resolucion != null) {
            return this.resolucion.getColRespuestas();
        }
        return null;
    }

    /**
     * Envía la resolución al profesor.
     *
     * @throws IOException
     */
    public void finalizarExamen() throws IOException {
        Mensaje mnsAvisarFin = new Mensaje(TipoMensaje.FINALIZAR_EXAMEN, resolucion);
        hiloSocketAlumno.enviarMensaje(mnsAvisarFin);
        
        // Cerramos la conexión
        this.avisarServidorCierre();
        this.eliminarArchivoTemporal();
        this.dialogRealizarExamen.dispose(); //Esta linea de codigo esta preventivamente hasta decidir a donde se retorna luego de realizar el examen.
        this.mPadre.setVisible(true);//Esta linea de codigo esta preventivamente hasta decidir a donde se retorna luego de realizar el examen.
        if (resolucion.getExamen().esMostrarCorreccionAutomatica()) {
            if (!esCorreccionAutomatica()) {
                Mensajes.mostrarExito("¡Examen finalizado exitosamente!"
                    + "\nEl examen contiene respuestas a ser corregidas por el instructor. "
                    + "\nA continuación se muestra la calificación de las preguntas de corrección automática."
                    + "\nLa calificación final será enviada a "
                    + "la dirección de correo electrónico proporcionada.");
            } else {
                Mensajes.mostrarExito("¡Examen finalizado exitosamente!\nLa resolución "
                        + "será enviada a la dirección de correo electrónico proporcionada.");
            }
            PanelRespuesta pnlRespuestas = new PanelRespuesta(resolucion);
            pnlRespuestas.setName("Respuestas");
            mPadre.getPanelDeslizante().setPanelMostrado(pnlRespuestas);
            mPadre.setTitle("Sistema de Administración de Entornos Educativos");
        } else {
            Mensajes.mostrarExito("¡Examen finalizado exitosamente!\nLa resolución será "
                    + "enviada a la dirección de correo electrónico proporcionada luego de ser corregida por el profesor.");
        }        
    }

    public boolean esCorreccionAutomatica() {
        ArrayList<Respuesta> colRespuestas = resolucion.getColRespuestas();
        for (Respuesta respuesta : colRespuestas) {
            if (respuesta instanceof RespuestaDesarrollo
                    || (respuesta instanceof RespuestaPreguntaVerdaderoFalso
                    && ((PreguntaVerdaderoFalso) respuesta.getPregunta()).isBlnConJustificacion())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Guarda la resolución en el disco duro.
     */
    public void guardarCambiosADisco() {
        this.gestorPersistencia.guardarResolucion(this.resolucion);
    }

    /**
     * Guarda la resolución en el disco duro.
     */
    public void eliminarArchivoTemporal() {
        this.gestorPersistencia.eliminarResolucion();
    }

    /**
     * Envía el número de preguntas respondidas hasta el momento al profesor.
     *
     * @param intPreguntasRespondidas cantidad de preguntas ya respondidas.
     */
    public void notificarProgreso(Integer intPreguntasRespondidas) {
        Mensaje mnsEnviarProgreso = new Mensaje(TipoMensaje.ENVIAR_PROGRESO, intPreguntasRespondidas);
        hiloSocketAlumno.enviarMensaje(mnsEnviarProgreso);
    }

    /**
     * Desordena la lista que contiene las respuestas a mostrarse..
     *
     * @param original lista a desordenar.
     * @return lista desordenada.
     */
    private void aleatorizarColeccion(ArrayList<Respuesta> original) {
        int i = original.size() - 1;
        while (i > -1) {
            int j = (int) Math.floor(Math.random() * (i + 1));
            Respuesta tmp = original.get(i);
            original.set(i, original.get(j));
            original.set(j, tmp);
            i--;
        }
    }

    public int getMinutosTranscurridos() {
        return this.resolucion.getIntTiempoEmpleado();
    }

    public void setMinutosTranscurridos(int intMinutosTranscurridos) {
        this.resolucion.setIntTiempoEmpleado(intMinutosTranscurridos);
    }

    /**
     * Ordena las respuestas por id de tema.
     */
    private void ordenarPorTema(ArrayList<Respuesta> colRespuestas) {
        quicksort(colRespuestas, 0, colRespuestas.size() - 1);
    }

    /**
     * Ordena una lista de respuestas en un cierto intervalo por quicksort con
     * mediana de 3
     *
     * @param izq límite izquierdo del intervalo
     * @param der límite derecho del intervalo
     */
    private void quicksort(ArrayList<Respuesta> colRespuestas, int izq, int der) {

        if (izq < der) {
            //elección del pivote
            int medio = (izq + der) / 2;
            if (colRespuestas.get(medio).getPregunta().getTema().getIntTemaId() < colRespuestas.get(izq).getPregunta().getTema().getIntTemaId()) {
                intercambiar(colRespuestas, izq, medio);
            }
            if (colRespuestas.get(der).getPregunta().getTema().getIntTemaId() < colRespuestas.get(izq).getPregunta().getTema().getIntTemaId()) {
                intercambiar(colRespuestas, izq, der);
            }
            if (colRespuestas.get(der).getPregunta().getTema().getIntTemaId() < colRespuestas.get(medio).getPregunta().getTema().getIntTemaId()) {
                intercambiar(colRespuestas, medio, der);
            }

            long pivot = colRespuestas.get(medio).getPregunta().getTema().getIntTemaId(); //pivote = mediana

            intercambiar(colRespuestas, medio, der - 1);

            // Partición del array
            int i, j;
            for (i = izq, j = der - 1; i < j;) { // recorremos con i de izquierda a derecha y con j de derecha a izquierda
                // i se mueve hasta llegar al primer elemento mayor que el pivote (desde la izquierda)
                while (colRespuestas.get(++i).getPregunta().getTema().getIntTemaId() < pivot);
                // j se mueve hasta llegar al primer elemento menor que el pivote (desde la derecha)
                while (pivot < colRespuestas.get(--j).getPregunta().getTema().getIntTemaId());
                if (i < j) // si no están bien colocados los intercambiamos
                {
                    intercambiar(colRespuestas, i, j);
                } else {
                    break;
                }

            }
            // Volvemos a colocar el pivote en su lugar
            intercambiar(colRespuestas, i, der - 1);

            quicksort(colRespuestas, izq, i - 1);     // ordenamos el fragmento izquierdo (elementos menores)
            quicksort(colRespuestas, i + 1, der);    // ordenamos el fragmento derecho (elementos mayores)
        }
    }

    /**
     * Intercambia las respuestas i y j en la lista de respuestas.
     *
     * @param i posición i de respuesta a intercambiar
     * @param j posición j de respuesta a intercambiar
     */
    private void intercambiar(ArrayList<Respuesta> colRespuestas, int i, int j) {
        Respuesta tmp = colRespuestas.get(i);
        colRespuestas.set(i, colRespuestas.get(j));
        colRespuestas.set(j, tmp);
    }

    /**
     * Cancela el examen actual y vuelve a la ventana principal.
     */
    public void notificarCancelacionExamen() {
        GestorSeguridad gestorSeguridad = new GestorSeguridad();
        gestorSeguridad.habilitarExplorer();
        gestorSeguridad.habilitarTaskManager();
        this.resolucion = null;
        this.timerEspera = null;
        try {
            this.avisarServidorCierre();
        } catch (IOException ex) {
            Mensajes.mostrarError("Error al cerrar la conexión con el profesor.");
        }
        this.eliminarArchivoTemporal();
        this.dialogRealizarExamen.dispose();
        this.dialogRealizarExamen = null;
        volverPanelInicio();
        Mensajes.mostrarAdvertencia("El examen ha sido cancelado por su instructor.\nComuníquese con el mismo para mayor información.");
    }

    /**
     * Cancela el examen actual y vuelve a la ventana principal.
     */
    public void notificarFinalizacionExamen() {
        GestorSeguridad gestorSeguridad = new GestorSeguridad();
        gestorSeguridad.habilitarExplorer();
        gestorSeguridad.habilitarTaskManager();
        
        Mensaje mnsAvisarFin = new Mensaje(TipoMensaje.FINALIZAR_EXAMEN, resolucion);
        hiloSocketAlumno.enviarMensaje(mnsAvisarFin);
        
        // Cerramos la conexión
        try {
            this.avisarServidorCierre();
        } catch (IOException ex) {
            Mensajes.mostrarError("Error al cerrar la conexión con el profesor.");
        }
        this.eliminarArchivoTemporal();
        this.dialogRealizarExamen.dispose(); //Esta linea de codigo esta preventivamente hasta decidir a donde se retorna luego de realizar el examen.
        this.dialogRealizarExamen = null;
        this.mPadre.setVisible(true); //Esta linea de codigo esta preventivamente hasta decidir a donde se retorna luego de realizar el examen.
        if (!esCorreccionAutomatica()) {
            Mensajes.mostrarExito("¡Examen finalizado por su instructor!"
                    + "\nEl examen contiene respuestas a ser corregidas por el instructor. "
                    + "\nA continuación se muestra la resolución de las preguntas de corrección automática."
                    + "\nLa calificación final será enviada a "
                    + "la dirección de correo electrónico proporcionada.");
        } else {
            Mensajes.mostrarExito("¡Examen finalizado por su instructor!");
        }
        PanelRespuesta pnlRespuestas = new PanelRespuesta(resolucion);
        pnlRespuestas.setName("Respuestas");
        mPadre.getPanelDeslizante().setPanelMostrado(pnlRespuestas);
        mPadre.setTitle("Sistema de Administración de Entornos Educativos");
    }

    public void volverPanelInicio() {
        PanelInicio pnlInicio = new PanelInicio();
        pnlInicio.setName("Panel inicio");
        mPadre.getPanelDeslizante().setPanelMostrado(pnlInicio);
        mPadre.setTitle("Sistema de Administración de Entornos Educativos");
    }

    public void notificarAnulacionResolucion(String strJustificacion) {
        GestorSeguridad gestorSeguridad = new GestorSeguridad();
        gestorSeguridad.habilitarExplorer();
        gestorSeguridad.habilitarTaskManager();
        this.eliminarArchivoTemporal();
        this.dialogRealizarExamen.dispose();
        this.volverPanelInicio();
        Mensajes.mostrarAdvertencia("Su examen ha sido anulado por su instructor con el siguiente motivo:\n\n\"" + strJustificacion + "\"\n\nConsulte con su instructor.");
        this.resolucion.setStrJustificacionAnulacion(strJustificacion);
        Mensaje mnsResolucionAnulada = new Mensaje(TipoMensaje.ANULAR_RESOLUCION, resolucion);
        hiloSocketAlumno.enviarMensaje(mnsResolucionAnulada);
        try {
            this.avisarServidorCierre();
        } catch (IOException ex) {
            Mensajes.mostrarError("Error al cerrar la conexión con el profesor.");
        }
        this.resolucion = null;
        this.timerEspera = null;
        this.dialogRealizarExamen = null;
    }
    
    public void agregarTiempo(int intMinutosAgregados) {
        this.dialogRealizarExamen.agregarTiempo(intMinutosAgregados);
    }
    
    public void quitarTiempo(int intMinutosQuitados) {
        this.dialogRealizarExamen.quitarTiempo(intMinutosQuitados);
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public Usuario getProfesor() {
        return this.profesor;
    }
    
    // Resultado de la validación
    
    public void setBlnValidacion(boolean blnValidacion) {
        this.blnValidacion = blnValidacion;
    }
    
    public boolean isBlnValidacion() {
        return blnValidacion;
    }
    
    // ¿Finalizó el proceso de validación?
    
    public void setFueValidado(boolean blnFueValidado) {
        this.blnFueValidado = blnFueValidado;
    }
    
    public boolean fueValidado() {
        return this.blnFueValidado;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public HiloSocketAlumno getHiloSocketAlumno() {
        return hiloSocketAlumno;
    }    
}
