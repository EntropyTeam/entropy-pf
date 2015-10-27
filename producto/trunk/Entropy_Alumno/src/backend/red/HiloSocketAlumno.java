package backend.red;

import backend.examenes.Examen;
import backend.gestores.GestorResolucionExamen;
import backend.gestores.GestorPresentacion;
import backend.usuarios.Usuario;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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

    public HiloSocketAlumno(String strIP, int intPuerto, GestorResolucionExamen gestorResolucionExamen) {
        this.gestorResolucionExamen = gestorResolucionExamen;

        try {
            this.socket = new Socket(strIP, intPuerto);
            this.objetoSaliente = new ObjectOutputStream(socket.getOutputStream());
            this.objetoEntrante = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(HiloSocketAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HiloSocketAlumno(String strIP, int intPuerto, GestorPresentacion gestorPresentacion) {
        this.gestorPresentacion = gestorPresentacion;
        try {
            this.socket = new Socket(strIP, intPuerto);
            this.objetoSaliente = new ObjectOutputStream(socket.getOutputStream());
            this.objetoEntrante = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(HiloSocketAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void enviarMensaje(Mensaje mensaje) throws IOException {
        objetoSaliente.writeObject(mensaje);
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
            case TipoMensaje.RESULTADO_VALIDACION:
                boolean blnValidacion = (boolean) mensaje.getPayload();
                gestorResolucionExamen.setBlnValidacion(blnValidacion);
                break;
        }
    }

    @Override
    public synchronized void run() {

        while (!this.isInterrupted()) {
            try {
                Object objRecibido = this.objetoEntrante.readObject();
                if (objRecibido instanceof Mensaje) {
                    Mensaje mensaje = (Mensaje) objRecibido;
                    procesarMensaje(mensaje);
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
