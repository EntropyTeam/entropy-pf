package backend.red;

import backend.auxiliares.Mensajes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author Gaston
 */
public class GestorRedAdHoc {

    private RedAdHocCreator pnlHijo;
    private Timer timer;

    public GestorRedAdHoc() {
    }
    
    public GestorRedAdHoc(RedAdHocCreator jPanelHijo) {
        this.pnlHijo = jPanelHijo;
    }

    public void crearRed(String nombre, String pass) {
        Runtime cmdRunTime = Runtime.getRuntime();
        try {
            String strComando = "netsh wlan set hostednetwork mode=allow ssid=" + nombre + " key=" + pass + "";
            String strComandoActivar = "netsh wlan start hostednetwork";
            Process proEjecutado = cmdRunTime.exec(strComando);
            proEjecutado.waitFor();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(proEjecutado.getInputStream()));
            System.out.println(this.leerBuffer(buffReader));
            proEjecutado = cmdRunTime.exec(strComandoActivar);
            proEjecutado.waitFor();
            BufferedReader buffReaderActivar = new BufferedReader(new InputStreamReader(proEjecutado.getInputStream()));
            System.out.println(this.leerBuffer(buffReaderActivar));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void desconectar() {
        Runtime cmdRunTime = Runtime.getRuntime();
        try {
            String strComandoDesactivar = "netsh wlan stop hostednetwork";
            Process proEjecutado = cmdRunTime.exec(strComandoDesactivar);
            proEjecutado.waitFor();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(proEjecutado.getInputStream()));
            System.out.println(this.leerBuffer(buffReader));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String leerBuffer(BufferedReader buffMensaje) {
        String strMensaje = "";
        String strLinea;
        try {
            while ((strLinea = buffMensaje.readLine()) != null) {
                strMensaje = strMensaje + "\n" + strLinea;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMensaje;
    }

    public String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (!intf.isLoopback() && !intf.isVirtual() && intf.isUp()) {                
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress iaddr = enumIpAddr.nextElement();
                        if (iaddr instanceof Inet4Address) {
                            return iaddr.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Error retrieving network interface list");
        }
        
        return "No estas conectado a ninguna red";
    }

    public void redAdHoc(String ssid, String pass1, String pass2) {//Activar la creacion de la red adhoc
        if (!pass1.equals(pass2) || pass1.length() < 8) {
            Mensajes.mostrarError("Las contraseñas no concuerdan o tienen menos de 8 caracteres");
        } else {
            System.out.println(getIP());
            this.crearRed(ssid, pass1);
            pnlHijo.setIpAddress("Espere mientras se crea la red...");
            timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!getIP().startsWith("127")){
                        pnlHijo.setIpAddress(getIP());
                        timer.stop();
                    }
                }
            });
            timer.start();
        }
    }
}
