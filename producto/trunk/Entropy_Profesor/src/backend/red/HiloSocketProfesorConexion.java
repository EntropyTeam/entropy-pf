package backend.red;

import backend.gestores.GestorDePresentacion;
import backend.gestores.GestorTomaExamen;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Esta clase se encarga de aceptar peticiones de entrada y de conexión al
 * sistema, y por cada una de ellas crea una instancia de SocketEscuchaPorAlumno
 * para mantener viva la conexión entre ambos módulos e intercambiar mensajes
 * que servirán para el inicio, control y terminación de un examen.
 *
 * @author Pelito
 */
public class HiloSocketProfesorConexion extends Thread {

    private ServerSocket serverSocket;
    private final GestorTomaExamen gestorTomaExamen;
    private final GestorDePresentacion gestorPresentacion;
    private int intPuertoEscucha;

    public HiloSocketProfesorConexion(GestorTomaExamen gestorTomaExamen) {
        this.gestorTomaExamen = gestorTomaExamen;
        this.gestorPresentacion = null;
        this.intPuertoEscucha = VariablesRed.PUERTO_TCP_EXAMEN;
    }
    
    public HiloSocketProfesorConexion(GestorDePresentacion gestorPresentacion) {
        this.gestorPresentacion = gestorPresentacion;
        this.gestorTomaExamen=null;
        this.intPuertoEscucha = VariablesRed.PUERTO_TCP_PRESENTACION;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(this.intPuertoEscucha);
            System.out.println("Server en linea...");
            while (true) {
                if (gestorTomaExamen != null) gestorTomaExamen.aceptarConexion(serverSocket.accept());
                else if (gestorPresentacion != null) gestorPresentacion.aceptarConexion(serverSocket.accept());
            }
        } catch (IOException e) {
            System.out.println("Error a aceptar la conexion.");
        }
    }

    public void pararDeEscuchar() throws IOException {
        serverSocket.close();
    }
}
