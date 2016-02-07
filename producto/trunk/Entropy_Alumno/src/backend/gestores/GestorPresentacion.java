/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.gestores;

import backend.red.HiloSocketAlumno;
import backend.red.Mensaje;
import backend.red.TipoMensaje;
import backend.usuarios.Alumno;
import backend.usuarios.Usuario;
import frontend.inicio.VentanaPrincipal;
import frontend.presentacion.DialogPresentacion;
import frontend.presentacion.PanelIniciarPresentacion;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
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
    private boolean blnEsPrimeraVez = true;
    private Dimension screenSize;
    


    public GestorPresentacion(String ipServidor, int intPuerto) throws Exception {
        this.ipServidor = ipServidor;
        this.intPuerto = intPuerto;
        this.dialogPresentacion = new DialogPresentacion(mPadre, true, this);
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

    public void iniciarConexion() throws IOException, Exception {
        hiloSocketAlumno = new HiloSocketAlumno(ipServidor, intPuerto, this);
        hiloSocketAlumno.start();
        blnEstaConectado = true;
    }

    public void avisarServidorCierre() throws IOException {
        Mensaje mensaje = new Mensaje(TipoMensaje.DESCONECTAR_CLIENTE);
        hiloSocketAlumno.enviarMensaje(mensaje);
        hiloSocketAlumno.interrupt();
        hiloSocketAlumno.cerrarSocket();
    }

    public void conectarAlumno(Alumno alumno) throws IOException {
        this.alumno = alumno;
        Mensaje mensaje = new Mensaje(TipoMensaje.CONECTAR_CLIENTE_PRESENTACION, alumno);
        hiloSocketAlumno.enviarMensaje(mensaje);
    }

    public void finalizarPresentacion() throws IOException {
        Mensaje mnsAvisarFin = new Mensaje(TipoMensaje.FINALIZAR_PRESENTACION);
        hiloSocketAlumno.enviarMensaje(mnsAvisarFin);
        
        this.avisarServidorCierre();
    }

    public boolean estaConectado() {
        return hiloSocketAlumno.getSocket() != null || hiloSocketAlumno.getSocket().isConnected();
    }

    public void mostrarImagen(byte[] bytesImg) {
        try {
            ByteArrayInputStream bufferImg = new ByteArrayInputStream(bytesImg);
            BufferedImage imagen = ImageIO.read(bufferImg);
            if(this.dialogPresentacion.isVisible()) {
                this.dialogPresentacion.setLblImagen(imagen);
            } else if (blnEsPrimeraVez) {
                blnEsPrimeraVez = false;
                this.dialogPresentacion.setLblImagen(imagen);
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
        pnlIniciarPresentacion.setName("Iniciar presentación");
        mPadre.getPanelDeslizante().setPanelMostrado(pnlIniciarPresentacion);
        mPadre.setTitle("Iniciar presentación");
        if (!mPadre.isMaximized()) {
            mPadre.pack();
        }
    }
    
     /**
     * Comienza la presentacion. Comunica al profesor que se inició la presentacion
     */
    public void comenzarPresentacion() throws IOException {
        Mensaje mnsAvisarComienzoPresentacion = new Mensaje(TipoMensaje.INICIAR_PRESENTACION);
        hiloSocketAlumno.enviarMensaje(mnsAvisarComienzoPresentacion);
        
        VentanaPrincipal.getInstancia().volverAInicio();
    }

    public Usuario getProfesor() {
        return this.profesor;
    }
    
    public class HiloDialogPresentacion extends Thread {
        
        @Override
        public synchronized void run() {
            dialogPresentacion.setVisible(true);
        }
    }
}
