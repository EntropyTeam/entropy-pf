/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.gestores;

import backend.red.HiloSocketProfesorConexion;
import backend.red.HiloSocketProfesorPorAlumno;
import backend.red.Mensaje;
import backend.red.ParsearRoute;
import backend.red.TipoMensaje;
import backend.resoluciones.Alumno;
import backend.usuarios.Usuario;
import frontend.presentaciones.FrameControlPresentaciones;
import frontend.usuario.DialogInfoUsuario;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Pelito
 */
public class GestorDePresentacion {

    private final FrameControlPresentaciones frmControlPresentacion;
    private final HiloSocketProfesorConexion hiloSocketConexion;
    private final ArrayList<HiloSocketProfesorPorAlumno> colHilosSocketsAlumnos;

    public GestorDePresentacion(FrameControlPresentaciones frmControlPresentacion) {
        this.frmControlPresentacion = frmControlPresentacion;
        this.hiloSocketConexion = new HiloSocketProfesorConexion(this);
        this.colHilosSocketsAlumnos = new ArrayList<>();

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
        int indice = frmControlPresentacion.agregarAlumno(alumno);
        try {
            Usuario profesor = GestorConfiguracion.getInstancia().getIDAOUsuarios().getUsuario();
            profesor.setStrIP(ParsearRoute.getInstance().getLocalIPAddress());
            Mensaje mensaje = new Mensaje(TipoMensaje.APROBAR_CONEXION_PRESENTACION, profesor);
            colHilosSocketsAlumnos.get(indice).enviarMensaje(mensaje);
        } catch (IOException e) {
            frmControlPresentacion.mostrarErrorAlEnviarPresentacion(indice);
            System.out.println(e.toString());
        } catch (Exception ex) {
            Logger.getLogger(GestorDePresentacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return indice;
    }
    
    public void anularPresentacion(int intIndice) {
        try {
            Mensaje mensaje = new Mensaje(TipoMensaje.DESCONECTAR_PRESENTACION);
            colHilosSocketsAlumnos.get(intIndice).enviarMensaje(mensaje);
        } catch (IOException e) {
            frmControlPresentacion.mostrarErrorAlEnviarPresentacion(intIndice);
            System.out.println(e.toString());
        } catch (Exception ex) {
            Logger.getLogger(GestorDePresentacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarImagenes(int intIndice) {
        try {
            Mensaje mensaje = new Mensaje(TipoMensaje.ENVIAR_IMAGENES, capturarPantalla());
            colHilosSocketsAlumnos.get(intIndice).enviarMensaje(mensaje);
        } catch (IOException e) {
            frmControlPresentacion.mostrarErrorAlEnviarPresentacion(intIndice);
            System.out.println(e.toString());
        } catch (Exception ex) {
            Logger.getLogger(GestorDePresentacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconectarAlumno(int intIndice) {
        this.frmControlPresentacion.desconectarAlumno(intIndice);
    }

    public void iniciarPresentacion(int intIndice) {
        this.frmControlPresentacion.iniciarPresentacion(intIndice);
        HiloCapturarImagen hiloCapturarImagen = new HiloCapturarImagen(intIndice);
        hiloCapturarImagen.start();
    }

    public void mostrarDatosAlumno(int intIndice) {
        new DialogInfoUsuario(this.frmControlPresentacion, true, colHilosSocketsAlumnos.get(intIndice).getAlumno()).setVisible(true);
    }

    private byte[] capturarPantalla() throws Exception {
        ByteArrayOutputStream imgSalida = new ByteArrayOutputStream();
        //obtener el tamaño de la pantalla
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //crear un objeto del tipo Rectangle con el tamaño obtenido
        Rectangle screenRectangle = new Rectangle(screenSize);
        //inicializar un objeto robot
        Robot r = new Robot();
        //obtener una captura de pantalla del tamaño de la pantalla
        BufferedImage image = r.createScreenCapture(screenRectangle);
        
        ImageIO.write(image, "jpeg", imgSalida);
        
        return imgSalida.toByteArray();
    }

    private class HiloCapturarImagen extends Thread {
        
        int intIndice;
        boolean bandera = true;
        
        public HiloCapturarImagen(int intIndice) {
            this.intIndice = intIndice;
        }

        @Override
        public void run() {
            while (bandera) {
                try {
                    enviarImagenes(this.intIndice);
                    HiloCapturarImagen.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestorDePresentacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public void detenerHilo() {
            this.bandera = false;
        }
    }
}
