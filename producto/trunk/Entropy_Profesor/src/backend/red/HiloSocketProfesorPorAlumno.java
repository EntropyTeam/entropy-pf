package backend.red;

import backend.auxiliares.Mensajes;
import backend.examenes.Examen;
import backend.gestores.GestorDePresentacion;
import backend.gestores.GestorTomaExamen;
import backend.resoluciones.Alumno;
import backend.resoluciones.Resolucion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose Ruiz
 */
public class HiloSocketProfesorPorAlumno extends Thread {

    private boolean blnVive = true;
    private Socket socket;
    private GestorTomaExamen gestorTomaExamen;
    private GestorDePresentacion gestorPresentacion;
    private ObjectInputStream objetoEntrante;
    private ObjectOutput objetoSaliente;
    private int intIndice;
    private Alumno alumno;

    /**
     * Constructor necesario para inicilizar un hilo servidor por cada alumno.
     *
     * @param socket objeto socket para crear el hilo.
     */
    public HiloSocketProfesorPorAlumno(Socket socket, GestorTomaExamen gestorTomaExamen) {
        this.socket = socket;
        this.gestorTomaExamen = gestorTomaExamen;

        // Creo los objetos para leer y escribir en el socket
        try {
            this.objetoEntrante = new ObjectInputStream(this.socket.getInputStream());
            this.objetoSaliente = new ObjectOutputStream(this.socket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(HiloSocketProfesorPorAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
        public HiloSocketProfesorPorAlumno(Socket socket, GestorDePresentacion gestorPresentacion) {
        this.socket = socket;
        this.gestorPresentacion = gestorPresentacion;

        // Creo los objetos para leer y escribir en el socket
        try {
            this.objetoEntrante = new ObjectInputStream(this.socket.getInputStream());
            this.objetoSaliente = new ObjectOutputStream(this.socket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(HiloSocketProfesorPorAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sobreescritura del metodo run perteneciente al HiloSocketServidor que
     * sirve para dar la respuesta a cada cliente.
     */
    @Override
    public void run() {
        try {
            while (blnVive) {
                Object objRecibido = objetoEntrante.readObject(); //El alumno envio un mensaje
                if (objRecibido instanceof Mensaje) {
                    Mensaje mensaje = (Mensaje) objRecibido;
                    this.procesarMensaje(mensaje);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("EL alumno se fue");
            System.err.println("SI OCURRE UNA EXCEP ANTES DE AGREGAR UN USUARIO, LA SIGUIENTE LINEA PRODUCE UNA EXCEPCIÓN EN LA TABL, INTENTANDO BORRAR UNA LÍNEA QUE NO EXISTE.");
            procesarMensaje(new Mensaje(TipoMensaje.DESCONECTAR_CLIENTE, (Object) null));
            //SocketProfesor.socketsAlumnos.remove(this);
        }
    }

    public void responder(Object objeto) throws IOException {
        Mensaje mensaje = new Mensaje(objeto);
        objetoSaliente.writeObject(mensaje);
    }

    public ObjectInputStream getObjetoEntrante() {
        return objetoEntrante;
    }

    public void setObjetoEntrante(ObjectInputStream objetoEntrante) {
        this.objetoEntrante = objetoEntrante;
    }

    public synchronized void procesarMensaje(Mensaje mensaje) {
        switch (mensaje.getTipo()) {
            // Tipos de mensajes de examenes
            case TipoMensaje.CONECTAR_CLIENTE_EXAMEN:
                alumno = (Alumno) mensaje.getPayload();
                this.intIndice = gestorTomaExamen.agregarAlumno(alumno);
                break;
 
            case TipoMensaje.DESCONECTAR_CLIENTE:
                gestorTomaExamen.desconectarAlumno(intIndice);
                break;

            case TipoMensaje.INICIAR_EXAMEN:
                gestorTomaExamen.iniciarExamen(intIndice);
                break;

            case TipoMensaje.FINALIZAR_EXAMEN:
                Resolucion resolucion = (Resolucion) mensaje.getPayload();
                gestorTomaExamen.finalizarExamenAlumno(intIndice, resolucion);
                break;

            case TipoMensaje.ENVIAR_PROGRESO:
                int intCantPreguntasRespondidas = (Integer) mensaje.getPayload();
                gestorTomaExamen.notificarProgreso(intIndice, intCantPreguntasRespondidas);
                break;

            case TipoMensaje.ANULAR_RESOLUCION:
                gestorTomaExamen.confirmarAnulacionResolucion((Resolucion) mensaje.getPayload());
                break;
            
            // Tipos de mensajes de presentaciones
            case TipoMensaje.CONECTAR_CLIENTE_PRESENTACION:
                alumno = (Alumno) mensaje.getPayload();
                this.intIndice = gestorPresentacion.agregarAlumno(alumno);
                break;
                
            case TipoMensaje.INICIAR_PRESENTACION:;
                gestorPresentacion.iniciarPresentacion(intIndice);
                break;
            
            // Tipos de mensajes generales
            case TipoMensaje.RESPONDER_SALUDO:
                break;
        }
    }

    /**
     * Metodo que envía un mensaje de cualquier tipo al alumno.
     *
     * @param mensaje el mensaje a enviar.
     * @throws IOException que puede lanzar si hay algún problema al escribir
     * los datos en el objeto saliente. Se debe capturar en la llamada al
     * método.
     */
    public void enviarMensaje(Mensaje mensaje) throws IOException {
        objetoSaliente.writeObject(mensaje);
    }

    /**
     * Método utilizado para enviar un saludo al alumno.
     */
    public void enviarSaludo() {
        Mensaje mensaje = new Mensaje(TipoMensaje.SALUDAR_ALUMNO);
        try {
            enviarMensaje(mensaje);
        } catch (IOException e) {
            Mensajes.mostrarError("Error al saludar a un alumno.");
        }
    }

    public int getIndice() {
        return intIndice;
    }

    public void setIndice(int indice) {
        this.intIndice = indice;
    }

    public void notificarCancelacionExamen() {
        Mensaje mensaje = new Mensaje(TipoMensaje.CANCELAR_EXAMEN);
        try {
            enviarMensaje(mensaje);
        } catch (IOException e) {
            Mensajes.mostrarError("Error al saludar a un alumno.");
        }
    }

    public boolean notificarAnulacionResolucion(String strJustificacion) {
        try {
            enviarMensaje(new Mensaje(TipoMensaje.NOTIFICAR_ANULACION, strJustificacion)
            );
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    public Alumno getAlumno() {
        return alumno;
    }
}