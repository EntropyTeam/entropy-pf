/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.red;

/**
 * Clase que representa los distintos tipos de mensaje que se intercambian entre
 * la conexión de los módulos profesor y alumno.
 *
 * @author Pelito
 */
public class TipoMensaje {

    /**
     * Se manda al conectar un cliente Alumno al modulo Profesor. Object payload
     * = objeto de tipo Alumno, que representa el Alumno que se quiere conectar.
     */
    public static final int CONECTAR_CLIENTE_EXAMEN = 1;

    /**
     * Se manda al aprobar la conexion del cliente por parte del servidor.
     * Object payload = objeto de tipo Examen, que representa el exámen a
     * resolver por el Alumno.
     */
    public static final int APROBAR_CONEXION_Y_ENVIAR_EXAMEN = 2;

    /**
     * Se manda cuando un cliente se desconecta del modulo Profesor. Payload =
     * null.
     */
    public static final int DESCONECTAR_CLIENTE = 3;

    /**
     * Se manda cuando un cliente hace click en el boton para comenzar el
     * examen. Object payload = null.
     */
    public static final int INICIAR_EXAMEN = 4;

    /**
     * Se manda cuando un cliente finaliza el examen. Object payload = objeto de
     * tipo Examen, que representa el Examen finalizado por el alumno.
     */
    public static final int FINALIZAR_EXAMEN = 5;

    /**
     * Se manda cuando el servidor anula el examen que esta siendo resuelto por
     * el cliente. El cliente responde con un FINALIZAR_EXAMEN, de esa manera el
     * servidor obtiene el examen resuelto del cliente hasta el momento en que
     * fue anulado. Object payload = null
     */
    public static final int ANULAR_EXAMEN = 6;

    /**
     * Lo manda cada cierto tiempo el módulo del profesor, para verificar que la
     * conectividad entre los módulos siga "viva". Payload = null.
     */
    public static final int SALUDAR_ALUMNO = 8;

    /**
     * Se manda como respuesta al saludo del profesor. Payload = null.
     */
    public static final int RESPONDER_SALUDO = 9;

    /**
     * Tipo de mensaje que envía el alumno, actualizando la cantidad de
     * preguntas respondidas al momento. El payload es un Integer que indica la
     * cantidad de preguntas respondidas.
     */
    public static final int ENVIAR_PROGRESO = 10;

    /**
     * Avisa a los alumnos conectados que el examen se canceló. En el módulo
     * alumno, este mensaje cierra el examen y vuelve a la ventana principal.
     */
    public static final int CANCELAR_EXAMEN = 11;

    /**
     * Notifica a un alumno que su parcial fue anulado, mostrando la
     * justificación del profesor. El mensaje es la justificación. El módulo
     * alumno luego crea la resolución, le adjunta la justificación, y lo envía
     * al profesor con el mensaje <code>ANULAR_RESOLUCION</code>.
     */
    public static final int NOTIFICAR_ANULACION = 12;

    /**
     * Envía al profesor un Objeto Resolución con las respuestas anuladas del
     * alumno, para que el módulo profesor lo almacene en la BD.
     */
    public static final int ANULAR_RESOLUCION = 13;
    
    /**
     * Se manda al conectar un cliente Alumno al modulo Profesor. Object payload
     * = objeto de tipo Alumno, que representa el Alumno que se quiere conectar.
     */
    public static final int CONECTAR_CLIENTE_PRESENTACION = 14;
    
    /**
     * Se manda la aprobacion de unirse a la presentacion por parte del servidor
     * al cliente.
     */
    public static final int APROBAR_CONEXION_PRESENTACION = 15;
    
    /**
     * Se manda cuando un cliente hace click en el boton para comenzar la
     * presentacion. Object payload = null.
     */
    public static final int INICIAR_PRESENTACION = 16;
    
    /**
     * bytes[] imagenes = objeto de tipo vector de bytes q se envian al alumno
     * para que lo pueda visualizar.
     */
    public static final int ENVIAR_IMAGENES = 17;
    
    /**
     * Se manda cuando el servidor desea desconectar a un cliente de la
     * presentacion.
     */
    public static final int DESCONECTAR_PRESENTACION = 18;
    
    /**
     * Se manda con el codigo de validacion de alumno
     * antes de comenzar el examen.
     */
    public static final int VALIDAR_ALUMNO = 19;
    /**
     * Se manda con el codigo el booleano que resulte
     * de la validacion del codigo del alumno en el profesor.
     */
    public static final int RESULTADO_VALIDACION = 20;
    
    /**
     * Se manda cuando se interrumpe la conexión de un alumno por una excepción.
     */
    public static final int CLIENTE_INTERRUMPIDO = 21;
    
    /**
     * Se manda cuando el alumno finaliza la conexión de una presentacion.
     */
    public static final int FINALIZAR_PRESENTACION = 22;
}
