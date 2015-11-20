package backend.dao;

/**
 * Clase que tiene los nombres de las tablas y columnas de la BD para usar en los DAO.
 * 
 * @author lucas
 */
public class EntropyDB {
    
    /*******************************************/
    /************ TABLAS GENERALES *************/
    /*******************************************/
    
    public static final String GRL_TBL_USUARIO = "grl_usuario";
    public static final String GRL_COL_USUARIO_ID = "usuarioId";
    public static final String GRL_COL_USUARIO_NOMBRE = "nombre";
    public static final String GRL_COL_USUARIO_APELLIDO = "apellido";
    public static final String GRL_COL_USUARIO_TIPO_DOCUMENTO = "tipoDocumento";
    public static final String GRL_COL_USUARIO_DOCUMENTO = "documento";
    public static final String GRL_COL_USUARIO_LEGAJO = "legajo";
    public static final String GRL_COL_USUARIO_EMAIL = "email";
    public static final String GRL_COL_USUARIO_DESCRIPCION = "descripcion";
    public static final String GRL_COL_USUARIO_FOTO = "foto";
    
    public static final String GRL_TBL_INSTITUCION = "grl_institucion";
    public static final String GRL_COL_INSTITUCION_ID = "institucionId";
    public static final String GRL_COL_INSTITUCION_NOMBRE = "nombre";
    public static final String GRL_COL_INSTITUCION_DESCRIPCION = "descripcion";
    public static final String GRL_COL_INSTITUCION_LOGO = "logo";
    
    public static final String GRL_TBL_CURSO = "grl_curso";
    public static final String GRL_COL_CURSO_ID = "cursoId";
    public static final String GRL_COL_CURSO_NOMBRE = "nombre";
    public static final String GRL_COL_CURSO_DESCRIPCION = "descripcion";
    public static final String GRL_COL_CURSO_INSTITUCION_ID = "institucionId";
    
    public static final String GRL_TBL_ALUMNO = "grl_alumno";
    public static final String GRL_COL_ALUMNO_ID = "alumnoId";
    public static final String GRL_COL_ALUMNO_NOMBRE = "nombre";
    public static final String GRL_COL_ALUMNO_APELLIDO = "apellido";
    public static final String GRL_COL_ALUMNO_TIPO_DOCUMENTO = "tipoDocumento";
    public static final String GRL_COL_ALUMNO_DOCUMENTO = "documento";
    public static final String GRL_COL_ALUMNO_EMAIL = "email";
    
    public static final String GRL_TBL_TIPO_PREGUNTA = "grl_tipopregunta";
    public static final String GRL_COL_TIPO_PREGUNTA_ID = "tipoPreguntaId";
    public static final String GRL_COL_TIPO_PREGUNTA_NOMBRE = "nombre";
    
    
    /*******************************************/
    /******* TABLAS DISEÑOS DE EXAMENES ********/
    /*******************************************/
    
    public static final String DIS_TBL_DISEÑO_EXAMEN = "dis_disenoexamen";
    public static final String DIS_COL_DISEÑO_EXAMEN_ID = "disenoExamenId";
    public static final String DIS_COL_DISEÑO_EXAMEN_CURSO_ID = "cursoId";
    public static final String DIS_COL_DISEÑO_EXAMEN_NOMBRE = "nombre";
    public static final String DIS_COL_DISEÑO_EXAMEN_DESCRIPCION = "descripcion";
    public static final String DIS_COL_DISEÑO_EXAMEN_USUARIO_ID = "usuarioId";
    
    public static final String DIS_TBL_PREGUNTA = "dis_pregunta";
    public static final String DIS_COL_PREGUNTA_ID = "preguntaId";
    public static final String DIS_COL_PREGUNTA_DISEÑO_EXAMEN_ID = "disenoExamenId";
    public static final String DIS_COL_PREGUNTA_ORDEN = "orden";
    public static final String DIS_COL_PREGUNTA_TEMA_ID = "temaId";
    public static final String DIS_COL_PREGUNTA_TIPO_PREGUNTA_ID = "tipoPreguntaId";
    public static final String DIS_COL_PREGUNTA_NIVEL = "nivel";
    public static final String DIS_COL_PREGUNTA_ENUNCIADO = "enunciado";
    public static final String DIS_COL_PREGUNTA_PUNTAJE = "puntaje";
    public static final String DIS_COL_PREGUNTA_REFERENCIA = "referencia";
    
    public static final String DIS_TBL_PREGUNTA_MO = "dis_preguntamultipleopcion";
    public static final String DIS_COL_PREGUNTA_MO_PREGUNTA_ID = "preguntaId";
    public static final String DIS_COL_PREGUNTA_MO_ORDEN = "orden";
    public static final String DIS_COL_PREGUNTA_MO_OPCION = "opcion";
    public static final String DIS_COL_PREGUNTA_MO_CORRECTO = "correcto";
    
    public static final String DIS_TBL_PREGUNTA_NU = "dis_preguntanumerica";
    public static final String DIS_COL_PREGUNTA_NU_PREGUNTA_ID = "preguntaId";
    public static final String DIS_COL_PREGUNTA_NU_ES_RANGO = "esRango";
    public static final String DIS_COL_PREGUNTA_NU_NUMERO = "numero";
    public static final String DIS_COL_PREGUNTA_NU_RANGO_DESDE = "rangoDesde";
    public static final String DIS_COL_PREGUNTA_NU_RANGO_HASTA = "rangoHasta";
    public static final String DIS_COL_PREGUNTA_NU_VARIACION = "variacion";
    
    public static final String DIS_TBL_PREGUNTA_RC = "dis_preguntarelacioncolumnas";
    public static final String DIS_COL_PREGUNTA_RC_PREGUNTA_ID = "preguntaId";
    public static final String DIS_COL_PREGUNTA_RC_ORDEN = "orden";
    public static final String DIS_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA = "columnaIzquierda";
    public static final String DIS_COL_PREGUNTA_RC_COLUMNA_DERECHA = "columnaDerecha";
    
    public static final String DIS_TBL_PREGUNTA_VF = "dis_preguntaverdaderofalso";
    public static final String DIS_COL_PREGUNTA_VF_PREGUNTA_ID = "preguntaId";
    public static final String DIS_COL_PREGUNTA_VF_VERDADERO = "verdadero";
    public static final String DIS_COL_PREGUNTA_VF_JUSTIFICACION = "justificacion";
    
    public static final String DIS_TBL_TAG = "dis_tag";
    public static final String DIS_COL_TAG_ID = "tagId";
    
    public static final String DIS_TBL_TAG_POR_PREGUNTA = "dis_tagporpregunta";
    public static final String DIS_COL_TAG_POR_PREGUNTA_TAG_ID = "tagId";
    public static final String DIS_COL_TAG_POR_PREGUNTA_PREGUNTA_ID = "preguntaId";
    
    public static final String DIS_TBL_TEMA = "dis_tema";
    public static final String DIS_COL_TEMA_ID = "temaId";
    public static final String DIS_COL_TEMA_DISEÑO_EXAMEN_ID = "disenoExamenId";
    public static final String DIS_COL_TEMA_NOMBRE = "nombre";
    
    public static final String DIS_TBL_ADJUNTO = "dis_adjunto";
    public static final String DIS_COL_ADJUNTO_ID = "adjuntoId";
    public static final String DIS_COL_ADJUNTO_ADJUNTO = "adjunto";
    public static final String DIS_COL_ADJUNTO_PREGUNTA_ID = "preguntaId";
    
    
    /*******************************************/
    /************* TABLAS EXAMENES *************/
    /*******************************************/
    
    public static final String EXA_TBL_EXAMEN = "exa_examen";
    public static final String EXA_COL_EXAMEN_ID = "examenId";
    public static final String EXA_COL_EXAMEN_CURSO_ID = "cursoId";
    public static final String EXA_COL_EXAMEN_NOMBRE = "nombre";
    public static final String EXA_COL_EXAMEN_DESCRIPCION = "descripcion";
    public static final String EXA_COL_EXAMEN_ESTADO = "estado";
    public static final String EXA_COL_EXAMEN_TIEMPO = "tiempo";
    public static final String EXA_COL_EXAMEN_FECHA = "fecha";
    public static final String EXA_COL_EXAMEN_PORCENTAJE_APROBACION = "porcentajeAprobado";
    public static final String EXA_COL_EXAMEN_MOTIVO_CANCELACION = "motivoCancelacion";
    
    public static final String EXA_TBL_PREGUNTA = "exa_pregunta";
    public static final String EXA_COL_PREGUNTA_ID = "preguntaId";
    public static final String EXA_COL_PREGUNTA_EXAMEN_ID = "examenId";
    public static final String EXA_COL_PREGUNTA_ORDEN = "orden";
    public static final String EXA_COL_PREGUNTA_TEMA_ID = "temaId";
    public static final String EXA_COL_PREGUNTA_TIPO_PREGUNTA_ID = "tipoPreguntaId";
    public static final String EXA_COL_PREGUNTA_NIVEL = "nivel";
    public static final String EXA_COL_PREGUNTA_ENUNCIADO = "enunciado";
    public static final String EXA_COL_PREGUNTA_PUNTAJE = "puntaje";
    public static final String EXA_COL_PREGUNTA_REFERENCIA = "referencia";
    
    public static final String EXA_TBL_PREGUNTA_MO = "exa_preguntamultipleopcion";
    public static final String EXA_COL_PREGUNTA_MO_PREGUNTA_ID = "preguntaId";
    public static final String EXA_COL_PREGUNTA_MO_ORDEN = "orden";
    public static final String EXA_COL_PREGUNTA_MO_OPCION = "opcion";
    public static final String EXA_COL_PREGUNTA_MO_CORRECTO = "correcto";
    
    public static final String EXA_TBL_PREGUNTA_NU = "exa_preguntanumerica";
    public static final String EXA_COL_PREGUNTA_NU_PREGUNTA_ID = "preguntaId";
    public static final String EXA_COL_PREGUNTA_NU_ES_RANGO = "esRango";
    public static final String EXA_COL_PREGUNTA_NU_NUMERO = "numero";
    public static final String EXA_COL_PREGUNTA_NU_RANGO_DESDE = "rangoDesde";
    public static final String EXA_COL_PREGUNTA_NU_RANGO_HASTA = "rangoHasta";
    public static final String EXA_COL_PREGUNTA_NU_VARIACION = "variacion";
    
    public static final String EXA_TBL_PREGUNTA_RC = "exa_preguntarelacioncolumnas";
    public static final String EXA_COL_PREGUNTA_RC_PREGUNTA_ID = "preguntaId";
    public static final String EXA_COL_PREGUNTA_RC_ORDEN = "orden";
    public static final String EXA_COL_PREGUNTA_RC_COLUMNA_IZQUIERDA = "columnaIzquierda";
    public static final String EXA_COL_PREGUNTA_RC_COLUMNA_DERECHA = "columnaDerecha";
    
    public static final String EXA_TBL_PREGUNTA_VF = "exa_preguntaverdaderofalso";
    public static final String EXA_COL_PREGUNTA_VF_PREGUNTA_ID = "preguntaId";
    public static final String EXA_COL_PREGUNTA_VF_VERDADERO = "verdadero";
    public static final String EXA_COL_PREGUNTA_VF_JUSTIFICACION = "justificacion";
    
    public static final String EXA_TBL_TEMA = "exa_tema";
    public static final String EXA_COL_TEMA_ID = "temaId";
    public static final String EXA_COL_TEMA_EXAMEN_ID = "examenId";
    public static final String EXA_COL_TEMA_NOMBRE = "nombre";
    
    public static final String EXA_TBL_ADJUNTO = "exa_adjunto";
    public static final String EXA_COL_ADJUNTO_ID = "adjuntoId";
    public static final String EXA_COL_ADJUNTO_ADJUNTO = "adjunto";
    public static final String EXA_COL_ADJUNTO_PREGUNTA_ID = "preguntaId";
    
    
    /*******************************************/
    /*********** TABLAS RESOLUCIONES ***********/
    /*******************************************/
    
    public static final String RES_TBL_RESOLUCION = "res_resolucion";
    public static final String RES_COL_RESOLUCION_ID = "resolucionId";
    public static final String RES_COL_RESOLUCION_EXAMEN_ID = "examenId";
    public static final String RES_COL_RESOLUCION_ALUMNO_ID = "alumnoId";
    public static final String RES_COL_RESOLUCION_TIEMPO_EMPLEADO = "tiempoEmpleado";
    public static final String RES_COL_RESOLUCION_ANULADA = "anulada";
    public static final String RES_COL_RESOLUCION_MOTIVO_ANULACION = "motivoAnulacion";
    public static final String RES_COL_RESOLUCION_IP = "ip";
    public static final String RES_COL_RESOLUCION_CODIGO = "codigo";
    
    public static final String RES_TBL_RESPUESTA = "res_respuesta";
    public static final String RES_COL_RESPUESTA_ID = "respuestaId";
    public static final String RES_COL_RESPUESTA_RESOLUCION_ID = "resolucionId";
    public static final String RES_COL_RESPUESTA_PREGUNTA_ID = "preguntaId";
    public static final String RES_COL_RESPUESTA_COMENTARIO = "comentario";
    
    public static final String RES_TBL_RESPUESTA_DE = "res_respuestadesarrollo";
    public static final String RES_COL_RESPUESTA_DE_RESPUESTA_ID = "respuestaId";
    public static final String RES_COL_RESPUESTA_DE_RESPUESTA = "respuesta";
    public static final String RES_COL_RESPUESTA_DE_CALIFICACION = "calificacion";
    
    public static final String RES_TBL_RESPUESTA_MO = "res_respuestamultipleopcion";
    public static final String RES_COL_RESPUESTA_MO_RESPUESTA_ID = "respuestaId";
    public static final String RES_COL_RESPUESTA_MO_ORDEN = "orden";
    public static final String RES_COL_RESPUESTA_MO_ES_MARCADA = "esMarcada";
    public static final String RES_COL_RESPUESTA_MO_RESPUESTA = "respuesta";
    
    public static final String RES_TBL_RESPUESTA_NU = "res_respuestanumerica";
    public static final String RES_COL_RESPUESTA_NU_RESPUESTA_ID = "respuestaId";
    public static final String RES_COL_RESPUESTA_NU_RESPUESTA_NUMERO = "respuestaNumero";
    public static final String RES_COL_RESPUESTA_NU_RANGO_DESDE = "rangoDesde";
    public static final String RES_COL_RESPUESTA_NU_RANGO_HASTA = "rangoHasta";
    
    public static final String RES_TBL_RESPUESTA_RC = "res_respuestarelacioncolumnas";
    public static final String RES_COL_RESPUESTA_RC_RESPUESTA_ID = "respuestaId";
    public static final String RES_COL_RESPUESTA_RC_ORDEN = "orden";
    public static final String RES_COL_RESPUESTA_RC_COLUMNA_IZQUIERDA = "columnaIzquierda";
    public static final String RES_COL_RESPUESTA_RC_COLUMNA_DERECHA = "columnaDerecha";
    
    public static final String RES_TBL_RESPUESTA_VF = "res_respuestaverdaderofalso";
    public static final String RES_COL_RESPUESTA_VF_RESPUESTA_ID = "respuestaId";
    public static final String RES_COL_RESPUESTA_VF_SELECCIONO_VERDADERO = "seleccionoVerdadero";
    public static final String RES_COL_RESPUESTA_VF_SELECCIONO_FALSO = "seleccionoFalso";
    public static final String RES_COL_RESPUESTA_VF_JUSTIFICACION = "justificacion";
    public static final String RES_COL_RESPUESTA_VF_CALIFICACION = "calificacion";
    
    
    /*******************************************/
    /*********** TABLAS PRESENTACIONES ***********/
    /*******************************************/
    
    public static final String PRE_TBL_PRESENTACION = "pre_presentacion";
    public static final String PRE_COL_PRESENTACION_ID = "presentacionId";
    public static final String PRE_COL_PRESENTACION_FECHA = "fecha";
    
    public static final String PRE_TBL_ASISTENCIA = "pre_asistencia";
    public static final String PRE_COL_ASISTENCIA_ID = "asistenciaId";
    public static final String PRE_COL_ASISTENCIA_PRESENTACION_ID = "presentacionId";
    public static final String PRE_COL_ASISTENCIA_ALUMNO_ID = "alumnoId";
    public static final String PRE_COL_ASISTENCIA_ANULADA = "anulada";
    public static final String PRE_COL_ASISTENCIA_MOTIVO_ANULACION = "motivoAnulacion";
    public static final String PRE_COL_ASISTENCIA_IP = "ip";
}
