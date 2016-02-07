package backend.red;

import backend.examenes.Examen;
import backend.gestores.GestorResolucionExamen;
import backend.gestores.GestorPresentacion;
import backend.usuarios.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pelito
 */
public class HiloSocketAlumno extends Thread {

    private Socket socket;
    private ObjectInputStream objetoEntrante;
    private ObjectOutputStream objetoSaliente;
    private GestorResolucionExamen gestorResolucionExamen;
    private GestorPresentacion gestorPresentacion;

    public HiloSocketAlumno(String strIP, int intPuerto, GestorResolucionExamen gestorResolucionExamen) throws Exception {
        this.gestorResolucionExamen = gestorResolucionExamen;
            this.socket = new Socket(strIP, intPuerto);
            this.objetoSaliente = new ObjectOutputStream(socket.getOutputStream());
            this.objetoEntrante = new ObjectInputStream(socket.getInputStream());
    }

    public HiloSocketAlumno(String strIP, int intPuerto, GestorPresentacion gestorPresentacion) throws Exception {
        this.gestorPresentacion = gestorPresentacion;

            this.socket = new Socket(strIP, intPuerto);
            this.objetoSaliente = new ObjectOutputStream(socket.getOutputStream());
            this.objetoEntrante = new ObjectInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }
    
    public void cerrarSocket() {
        try {
            this.socket.close();
            System.out.println("SOCKET DEL ALUMNO CERRADO");
        } catch (IOException ex) {
            System.out.println("Error al cerrar el socket del alumno");
            Logger.getLogger(HiloSocketAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensaje(Mensaje mensaje) {
        if (!socket.isClosed()) {
            try {
                objetoSaliente.writeObject(mensaje);
            } catch (IOException ex) {
                System.out.println("Error al enviar el mensaje al profesor.");
            }
        }
    }

    public void procesarMensaje(Mensaje mensaje) {
        Usuario profesor = null;
        switch (mensaje.getTipo()) {
            case TipoMensaje.APROBAR_CONEXION_Y_ENVIAR_EXAMEN:
                Examen examen = (Examen) ((Object[]) mensaje.getPayload())[0];
                profesor = (Usuario) ((Object[]) mensaje.getPayload())[1];
                gestorResolucionExamen.setProfesor(profesor);
                gestorResolucionExamen.notificarRecepcionExamen(examen);
                break;
            case TipoMensaje.CANCELAR_EXAMEN:
                gestorResolucionExamen.notificarCancelacionExamen();
                break;
            case TipoMensaje.FINALIZAR_EXAMEN_DESDE_PROFESOR:
                gestorResolucionExamen.notificarFinalizacionExamen();
                break;
            case TipoMensaje.NOTIFICAR_ANULACION:
                gestorResolucionExamen.notificarAnulacionResolucion(mensaje.getPayload().toString());
                break;
            
            case TipoMensaje.APROBAR_CONEXION_PRESENTACION:
                profesor = (Usuario) mensaje.getPayload();
                gestorPresentacion.setProfesor(profesor);
                gestorPresentacion.notificarPresentacionValida();
                break;
            case TipoMensaje.ENVIAR_IMAGENES:
                byte[] bytesImg = (byte[]) mensaje.getPayload();
                gestorPresentacion.mostrarImagen(bytesImg);
                break;
            case TipoMensaje.DESCONECTAR_PRESENTACION:
                gestorPresentacion.desconectarAlumno();
                break;
            case TipoMensaje.FINALIZAR_PRESENTACION_DESDE_PROFESOR:
                gestorPresentacion.finalizar();
                break;
            case TipoMensaje.RESULTADO_VALIDACION:
                boolean blnValidacion = (boolean) mensaje.getPayload();
                gestorResolucionExamen.setBlnValidacion(blnValidacion);
                gestorResolucionExamen.setFueValidado(true);
                break;
            case TipoMensaje.AGREGAR_TIEMPO_EXAMEN:
                int intMinutosAgregados = (int) mensaje.getPayload();
                gestorResolucionExamen.agregarTiempo(intMinutosAgregados);
                break;
            case TipoMensaje.QUITAR_TIEMPO_EXAMEN:
                int intMinutosQuitados = (int) mensaje.getPayload();
                gestorResolucionExamen.quitarTiempo(intMinutosQuitados);
                break;
        }
    }

    @Override
    public synchronized void run() {

        while (!this.isInterrupted()) {
            try {
                if (!socket.isClosed()) {
                    //this.objetoEntrante = new ObjectInputStream(socket.getInputStream());
                    Object objRecibido = this.objetoEntrante.readObject();
                    if (objRecibido instanceof Mensaje) {
                        Mensaje mensaje = (Mensaje) objRecibido;
                        procesarMensaje(mensaje);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error de IOExcepcion al leer el mensaje.");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error de ClassNotFoundException al leer el mensaje.");
                e.printStackTrace();
            } 
        }
    }
}
