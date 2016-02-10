package backend.gestores;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 *
 * @author Gaston
 */
public class GestorSeguridad {

    private Runtime cmd;
    boolean blnControl;

    /**
     * Constructor por defecto de la clase, que inicializa una instancia de
     * Runtime para los demas metodos.
     *
     */
    public GestorSeguridad() {
        cmd = Runtime.getRuntime();
        blnControl = false;
    }

    /**
     * Metodo que desabilita la ejecucion de un Task Manager en la computadora.
     *
     */
    public void deshabilitarTaskManager() {
        String path = "HKCU" + File.separator
                + "Software" + File.separator
                + "Microsoft" + File.separator
                + "Windows" + File.separator
                + "CurrentVersion" + File.separator
                + "Policies" + File.separator
                + "System";
        try {
            String comando = "reg add " + path + " /v DisableTaskMgr /t REG_DWORD /d 1 /f";
            Process pro = cmd.exec(comando);
            pro.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            System.out.println(this.leerBuffer(buf) + "\nEl Taskmng esta deshabilitado");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("deshabilitarTaskManager()");
    }

    /**
     * Metodo que habilita el Task Manager en la computadora.
     *
     */
    public void habilitarTaskManager() {
        String path = "HKCU" + File.separator
                + "Software" + File.separator
                + "Microsoft" + File.separator
                + "Windows" + File.separator
                + "CurrentVersion" + File.separator
                + "Policies" + File.separator
                + "System";
        try {
            String comando = "reg add " + path + " /v DisableTaskMgr /t REG_DWORD /d 0 /f";
            Process pro = cmd.exec(comando);
            pro.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            System.out.println(this.leerBuffer(buf) + "\nEl Taskmng esta habilitado");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("habilitarTaskManager()");
    }

    /**
     * Metodo que cierra el proceso explorer.exe
     *
     */
    public void deshabilitarExplorer() {
        if (!blnControl) {

            try {
                String comando = "taskkill /f /im explorer.exe";
                Process pro = cmd.exec(comando);
//            pro.waitFor();//En un principio, esto debe pemanecer comentado para que no genere un bucle indefinido.
//            BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));//En un principio, esto debe pemanecer comentado para que no genere un bucle indefinido.
//            System.out.println(this.leerBuffer(buf));//En un principio, esto debe pemanecer comentado para que no genere un bucle indefinido.
                blnControl = true;
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\nEl explorer esta deshabilitado");
            }
            System.out.println("deshabilitarExplorer()");
        }
    }

    /**
     * Metodo que comienza un metodo explorer.exe
     *
     */
    public void habilitarExplorer() {
        if (blnControl) {

            try {
                String comando = "explorer.exe";
                Process pro = cmd.exec(comando);
                blnControl = false;
//            pro.waitFor();//En un principio, esto debe pemanecer comentado para que no genere un bucle indefinido.
//            BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));//En un principio, esto debe pemanecer comentado para que no genere un bucle indefinido.
//            System.out.println(this.leerBuffer(buf)+ "\nEl explorer esta habilitado");//En un principio, esto debe pemanecer comentado para que no genere un bucle indefinido.
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("habilitarExplorer()");
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
//        return "Sin Seguridad";
    }
//        Para desactivar la seguridad se debe: Comentar cada metodo de esta clase, Desactivar el always on top de DialogRealizarExamen y modificar el comportamiento del boton listo luego del jOptionPane en ambas opciones.

//acordarse de activar el always on top del dialog tambien y en el boton listo, luego del joptionpane la opcion de no
}
