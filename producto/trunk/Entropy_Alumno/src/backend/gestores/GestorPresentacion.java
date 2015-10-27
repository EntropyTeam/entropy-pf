/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.gestores;

import backend.red.HiloSocketAlumno;
import backend.red.Mensaje;
import backend.red.TipoMensaje;
import backend.resoluciones.Alumno;
import backend.usuarios.Usuario;
import frontend.inicio.VentanaPrincipal;
import frontend.presentacion.DialogPresentacion;
import frontend.presentacion.PanelIniciarPresentacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;


/**
 *
 * @author Pelito
 */
public class GestorPresentacion {

    private String ipServidor;
    private int intPuerto;
    private Alumno alumno;
    private HiloSocketAlumno hiloSocketAlumno;
    private VentanaPrincipal mPadre;
    private DialogPresentacion dialogPresentacion;
    private Timer timerEspera;
    private Usuario profesor;
    private boolean blnEstaConectado = false;


    public GestorPresentacion(String ipServidor, int intPuerto) throws IOException {
        this.ipServidor = ipServidor;
        this.intPuerto = intPuerto;
        this.dialogPresentacion = new DialogPresentacion(mPadre,true);
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

    public void iniciarConexion() throws IOException {
        hiloSocketAlumno = new HiloSocketAlumno(ipServidor, intPuerto, this);
        hiloSocketAlumno.start();
        blnEstaConectado = true;
    }

    public void avisarServidorCierre() throws IOException {
        Mensaje mensaje = new Mensaje(TipoMensaje.DESCONECTAR_CLIENTE);
        hiloSocketAlumno.enviarMensaje(mensaje);
        hiloSocketAlumno.interrupt();
    }

    public void conectarAlumno(Alumno alumno) throws IOException {
        this.alumno = alumno;
        Mensaje mensaje = new Mensaje(TipoMensaje.CONECTAR_CLIENTE_PRESENTACION, alumno);
        hiloSocketAlumno.enviarMensaje(mensaje);
    }

    public boolean estaConectado() {
        return hiloSocketAlumno.getSocket() != null || hiloSocketAlumno.getSocket().isConnected();
    }

    public void mostrarImagen(byte[] bytesImg) {
        try {
            ByteArrayInputStream bufferImg = new ByteArrayInputStream(bytesImg);
            BufferedImage imagen = ImageIO.read(bufferImg);
            if(this.dialogPresentacion.isVisible()) {
                this.dialogPresentacion.setLblImagen(new ImageIcon(imagen));
            } else if (blnEstaConectado) {
                new HiloDialogPresentacion().start();
            }
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Notiica al alumno que se puede unir a una presentacion.
     */
    public void notificarPresentacionValida() {
        timerEspera = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelInicio();
            }
        });
        timerEspera.start();
    }
    
    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    /**
     * Muestra el panel de notificación de examen recibido al alumno.
     */
    private void mostrarPanelInicio() {
        timerEspera.stop();
        PanelIniciarPresentacion pnlIniciarPresentacion = new PanelIniciarPresentacion(this);
        pnlIniciarPresentacion.setName("Iniciar Presentacion");
        mPadre.getPanelDeslizante().setPanelMostrado(pnlIniciarPresentacion);
        mPadre.setTitle("Iniciar Presentacion");
        if (mPadre.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            mPadre.pack();
        }
    }
    
     /**
     * Comienza la presentacion. Comunica al profesor que se inició la presentacion
     */
    public void comenzarPresentacion() throws IOException {
        Mensaje mnsAvisarComienzoPresentacion = new Mensaje(TipoMensaje.INICIAR_PRESENTACION);
        hiloSocketAlumno.enviarMensaje(mnsAvisarComienzoPresentacion);
    }
    
    public class HiloDialogPresentacion extends Thread {
        
        @Override
        public synchronized void run() {
            dialogPresentacion.setVisible(true);
        }
    }
}
