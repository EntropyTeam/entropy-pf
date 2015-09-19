package frontend.examenes;

import backend.resoluciones.Resolucion;
import backend.resoluciones.Respuesta;
import backend.resoluciones.RespuestaDesarrollo;
import backend.resoluciones.RespuestaPreguntaMultipleOpcion;
import backend.resoluciones.RespuestaPreguntaNumerica;
import backend.resoluciones.RespuestaPreguntaRelacionColumnas;
import backend.resoluciones.RespuestaPreguntaVerdaderoFalso;
import frontend.auxiliares.FiltroTexto;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.PanelInicio;
import frontend.inicio.VentanaPrincipal;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Clase que representa al panel donde se muestran las preguntas.
 *
 * @author Denise
 */
public class PanelRespuesta extends javax.swing.JPanel {

    private JLabel[] colCasillas;
    private JPanel[] colPaneles;
    private int intIDSeleccionada;
    private final Resolucion resolucion;
    private final GestorBarrasDeEstado gestorEstado;

    /**
     * Constructor de la clase.
     *
     * @param localResolucion
     */
    public PanelRespuesta(Resolucion localResolucion) {
        initComponents();
        this.txtCalificacion.setEditable(false);
        this.gestorEstado = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);
        this.gestorEstado.setNuevoEstadoImportante("Panel de corrección de repuestas...");
        this.resolucion = localResolucion;
        double dblCalificacion = resolucion.getCalificacion();
        double dblPuntajeTotal = resolucion.getExamen().getPuntajeTotal();
        lblPorcentajeAprobado.setText("Porcentaje aprobado: " 
                + String.format("%.2f", (100 * dblCalificacion / dblPuntajeTotal)) 
                + "% ~ Porcentaje necesario: " 
                + String.format("%.2f", resolucion.getExamen().getDblPorcentajeAprobacion()) + "%");
        this.colCasillas = crearCasillasPreguntas();
        this.colPaneles = crearPanelesRespuestas();
        this.intIDSeleccionada = 0;
        this.cambiarPreguntaSeleccionada(intIDSeleccionada);
        colCasillas[0].setPreferredSize(new Dimension(26, 26));
        colCasillas[0].setMaximumSize(new Dimension(26, 26));
        colCasillas[0].setMinimumSize(new Dimension(26, 26));
        colCasillas[0].setBorder(LookAndFeelEntropy.BORDE_NARANJA);
        colCasillas[0].setBackground(new Color(255, 204, 102));
        pnlCasillas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cargarCasillasPreguntas();
            }
        });
        this.registerKeyboardAction(new EscapeAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContenido = new javax.swing.JPanel();
        pnlCentral = new javax.swing.JPanel();
        pnlCorreccion = new javax.swing.JPanel();
        txtCalificacion = new frontend.auxiliares.TextFieldEntropy();
        lblsCalificacion = new javax.swing.JLabel();
        lblPuntaje = new javax.swing.JLabel();
        lblSiguiente = new javax.swing.JLabel();
        lblAnterior = new javax.swing.JLabel();
        pnlPregunta = new javax.swing.JPanel();
        pnlEnunciado = new javax.swing.JPanel();
        lblEnunciado = new javax.swing.JLabel();
        lblsTema = new javax.swing.JLabel();
        lblTema = new javax.swing.JLabel();
        lblsComentarios = new javax.swing.JLabel();
        lblComentarios = new javax.swing.JLabel();
        pnlContenidoPregunta = new javax.swing.JPanel();
        pnlCasillas = new javax.swing.JPanel();
        pnlFila1 = new javax.swing.JPanel();
        lblCasillaTemplate = new javax.swing.JLabel();
        lblPorcentajeAprobado = new javax.swing.JLabel();
        lblCorrecto = new javax.swing.JLabel();
        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();

        pnlContenido.setLayout(new javax.swing.BoxLayout(pnlContenido, javax.swing.BoxLayout.Y_AXIS));

        txtCalificacion.setDocument(new FiltroTexto(FiltroTexto.TipoFiltro.NUMEROS_DECIMALES_POSITIVOS));
        txtCalificacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCalificacion.setTextoPorDefecto("---");
        txtCalificacion.mostrarTextoPorDefecto();

        lblsCalificacion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsCalificacion.setText("Calificación:");

        lblPuntaje.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblPuntaje.setText("/100 pts");

        javax.swing.GroupLayout pnlCorreccionLayout = new javax.swing.GroupLayout(pnlCorreccion);
        pnlCorreccion.setLayout(pnlCorreccionLayout);
        pnlCorreccionLayout.setHorizontalGroup(
            pnlCorreccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCorreccionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblsCalificacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPuntaje, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlCorreccionLayout.setVerticalGroup(
            pnlCorreccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCorreccionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCorreccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsCalificacion)
                    .addComponent(lblPuntaje)
                    .addComponent(txtCalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblSiguiente.setBackground(new java.awt.Color(227, 226, 226));
        lblSiguiente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_siguiente.png"))); // NOI18N
        lblSiguiente.setToolTipText("");
        lblSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSiguiente.setOpaque(true);
        lblSiguiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSiguienteMouseClicked(evt);
            }
        });

        lblAnterior.setBackground(new java.awt.Color(227, 226, 226));
        lblAnterior.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_anterior.png"))); // NOI18N
        lblAnterior.setToolTipText("");
        lblAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnterior.setOpaque(true);
        lblAnterior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnteriorMouseClicked(evt);
            }
        });

        pnlPregunta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        pnlPregunta.setOpaque(false);

        pnlEnunciado.setOpaque(false);
        pnlEnunciado.setPreferredSize(new java.awt.Dimension(280, 79));

        lblEnunciado.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        lblEnunciado.setText("1. ¿Qué le pasa al papagayo en celo?");
        lblEnunciado.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblsTema.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblsTema.setText("Tema:");

        lblTema.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblTema.setText("Unidad 1");

        lblsComentarios.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblsComentarios.setText("<html>Comentarios:</html>");
        lblsComentarios.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblComentarios.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblComentarios.setText("No tengo comentarios.");
        lblComentarios.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout pnlEnunciadoLayout = new javax.swing.GroupLayout(pnlEnunciado);
        pnlEnunciado.setLayout(pnlEnunciadoLayout);
        pnlEnunciadoLayout.setHorizontalGroup(
            pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnunciadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEnunciado, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addGroup(pnlEnunciadoLayout.createSequentialGroup()
                        .addGroup(pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblsComentarios)
                            .addComponent(lblsTema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblComentarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlEnunciadoLayout.setVerticalGroup(
            pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnunciadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEnunciado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsTema)
                    .addComponent(lblTema))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEnunciadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblsComentarios)
                    .addComponent(lblComentarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlContenidoPregunta.setOpaque(false);
        pnlContenidoPregunta.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout pnlPreguntaLayout = new javax.swing.GroupLayout(pnlPregunta);
        pnlPregunta.setLayout(pnlPreguntaLayout);
        pnlPreguntaLayout.setHorizontalGroup(
            pnlPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPreguntaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pnlPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlEnunciado, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                        .addComponent(pnlContenidoPregunta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        pnlPreguntaLayout.setVerticalGroup(
            pnlPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
            .addGroup(pnlPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPreguntaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlEnunciado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pnlContenidoPregunta, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pnlCasillas.setOpaque(false);
        pnlCasillas.setLayout(new javax.swing.BoxLayout(pnlCasillas, javax.swing.BoxLayout.Y_AXIS));

        pnlFila1.setName("Fila1"); // NOI18N
        pnlFila1.setOpaque(false);
        pnlFila1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblCasillaTemplate.setBackground(new java.awt.Color(255, 204, 102));
        lblCasillaTemplate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCasillaTemplate.setText("1");
        lblCasillaTemplate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCasillaTemplate.setMaximumSize(new java.awt.Dimension(25, 25));
        lblCasillaTemplate.setMinimumSize(new java.awt.Dimension(25, 25));
        lblCasillaTemplate.setOpaque(true);
        lblCasillaTemplate.setPreferredSize(new java.awt.Dimension(25, 25));
        pnlFila1.add(lblCasillaTemplate);

        pnlCasillas.add(pnlFila1);

        lblPorcentajeAprobado.setBackground(new java.awt.Color(204, 204, 204));
        lblPorcentajeAprobado.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lblPorcentajeAprobado.setForeground(new java.awt.Color(102, 102, 102));
        lblPorcentajeAprobado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPorcentajeAprobado.setText("Porcentaje aprobado:");
        lblPorcentajeAprobado.setOpaque(true);

        lblCorrecto.setBackground(new java.awt.Color(255, 102, 102));
        lblCorrecto.setFont(new java.awt.Font("sansserif", 3, 14)); // NOI18N
        lblCorrecto.setForeground(new java.awt.Color(255, 255, 255));
        lblCorrecto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCorrecto.setText("Pregunta Correcta");
        lblCorrecto.setOpaque(true);

        javax.swing.GroupLayout pnlCentralLayout = new javax.swing.GroupLayout(pnlCentral);
        pnlCentral.setLayout(pnlCentralLayout);
        pnlCentralLayout.setHorizontalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCasillas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCentralLayout.createSequentialGroup()
                        .addComponent(lblAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlCorreccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlPregunta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCorrecto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPorcentajeAprobado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCentralLayout.setVerticalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPorcentajeAprobado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCorrecto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCentralLayout.createSequentialGroup()
                        .addComponent(pnlPregunta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlCorreccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblSiguiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAnterior, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCasillas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlContenido.add(pnlCentral);

        pnlEstado.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.lightGray));
        pnlEstado.setMaximumSize(new java.awt.Dimension(32767, 32));
        pnlEstado.setMinimumSize(new java.awt.Dimension(0, 32));
        pnlEstado.setPreferredSize(new java.awt.Dimension(798, 32));

        lblActualizacionEstado.setFont(new java.awt.Font("Calibri", 2, 12)); // NOI18N
        lblActualizacionEstado.setForeground(new java.awt.Color(102, 102, 102));
        lblActualizacionEstado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblActualizacionEstado.setText("Acá se escriben estados...");
        lblActualizacionEstado.setAlignmentX(0.5F);
        lblActualizacionEstado.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblIconoEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconoEstado.setText(" ");
        lblIconoEstado.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout pnlEstadoLayout = new javax.swing.GroupLayout(pnlEstado);
        pnlEstado.setLayout(pnlEstadoLayout);
        pnlEstadoLayout.setHorizontalGroup(
            pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadoLayout.createSequentialGroup()
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlEstadoLayout.setVerticalGroup(
            pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pnlEstadoLayout.createSequentialGroup()
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        pnlContenido.add(pnlEstado);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlContenido, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblAnteriorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnteriorMouseClicked
        cambiarPreguntaSeleccionada(intIDSeleccionada - 1);
    }//GEN-LAST:event_lblAnteriorMouseClicked

    private void lblSiguienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSiguienteMouseClicked
        cambiarPreguntaSeleccionada(intIDSeleccionada + 1);
    }//GEN-LAST:event_lblSiguienteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblAnterior;
    private javax.swing.JLabel lblCasillaTemplate;
    private javax.swing.JLabel lblComentarios;
    private javax.swing.JLabel lblCorrecto;
    private javax.swing.JLabel lblEnunciado;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JLabel lblPorcentajeAprobado;
    private javax.swing.JLabel lblPuntaje;
    private javax.swing.JLabel lblSiguiente;
    private javax.swing.JLabel lblTema;
    private javax.swing.JLabel lblsCalificacion;
    private javax.swing.JLabel lblsComentarios;
    private javax.swing.JLabel lblsTema;
    private javax.swing.JPanel pnlCasillas;
    private javax.swing.JPanel pnlCentral;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlContenidoPregunta;
    private javax.swing.JPanel pnlCorreccion;
    private javax.swing.JPanel pnlEnunciado;
    private javax.swing.JPanel pnlEstado;
    private javax.swing.JPanel pnlFila1;
    private javax.swing.JPanel pnlPregunta;
    private frontend.auxiliares.TextFieldEntropy txtCalificacion;
    // End of variables declaration//GEN-END:variables

    /**
     * Crea los labels de la parte inferior de la pantalla.
     *
     * @param cantPreguntas cantidad de preguntas a responder.
     * @return la colección de labels.
     */
    private JLabel[] crearCasillasPreguntas() {

        JLabel[] colCasillas = new JLabel[resolucion.getColRespuestas().size()];

        for (int i = 0; i < resolucion.getColRespuestas().size(); i++) {
            JLabel lbl = new JLabel();
            lbl.setPreferredSize(new Dimension(21, 21));
            lbl.setMaximumSize(new Dimension(21, 21));
            lbl.setMinimumSize(new Dimension(21, 21));
            lbl.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (resolucion.getColRespuestas().get(i).getCalificacion() >= 0) {
                lbl.setBackground(new Color(255, 204, 102));
            } else {
                lbl.setBackground(Color.LIGHT_GRAY);
            }
            lbl.setText("" + (i + 1));
            lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    lblCasillaMouseClicked(e);
                }
            });
            colCasillas[i] = lbl;
        }

        return colCasillas;
    }

    /**
     * Para la colección de labels actual, las acomoda en un panel del ancho
     * correcto.
     */
    private void cargarCasillasPreguntas() {
        int cantPreguntas = colCasillas.length;

        int anchoDeseado = pnlCasillas.getSize().width;
        if (anchoDeseado == 0) {
            anchoDeseado = pnlCasillas.getPreferredSize().width;
        }

        int cantFilas = (int) Math.ceil(((cantPreguntas * 32) / (double) anchoDeseado));

        pnlCasillas.removeAll();

        HashMap<Integer, JPanel> colFilas = new HashMap<>();

        for (int i = 1; i <= cantFilas; i++) {
            JPanel pnlNuevaFila = new JPanel();
            pnlNuevaFila.setLayout(new FlowLayout(FlowLayout.LEFT));
            colFilas.put(i, pnlNuevaFila);
            pnlCasillas.add(pnlNuevaFila);
        }

        int ancho = 0;
        int fila = 1;
        for (JLabel lbl : colCasillas) {
            ancho += (6 + lbl.getPreferredSize().width);
            if (ancho > anchoDeseado) {
                fila++;
                ancho = 0;
            }
            try {
                colFilas.get(fila).add(lbl);
            } catch (Exception e) {
                System.err.println("No existe la fila " + fila);
            }
        }

        pnlCasillas.revalidate();
        pnlCasillas.repaint();

    }

    private JPanel[] crearPanelesRespuestas() {

        JPanel[] colPaneles = new JPanel[resolucion.getColRespuestas().size()];

        for (int i = 0; i < colPaneles.length; i++) {

            Respuesta respuesta = resolucion.getColRespuestas().get(i);

            JPanel pnl = null;

            if (respuesta instanceof RespuestaPreguntaMultipleOpcion) {

                RespuestaPreguntaMultipleOpcion rmo = (RespuestaPreguntaMultipleOpcion) respuesta;
                pnl = new PanelPreguntaMultiplesOpciones(rmo, true);

            } else if (respuesta instanceof RespuestaPreguntaNumerica) {

                RespuestaPreguntaNumerica rmo = (RespuestaPreguntaNumerica) respuesta;
                pnl = new PanelPreguntaNumerica(rmo, true);

            } else if (respuesta instanceof RespuestaPreguntaRelacionColumnas) {

                RespuestaPreguntaRelacionColumnas rmo = (RespuestaPreguntaRelacionColumnas) respuesta;
                pnl = new PanelPreguntaRelacionColumnas(rmo, true);

            } else if (respuesta instanceof RespuestaPreguntaVerdaderoFalso) {

                RespuestaPreguntaVerdaderoFalso rmo = (RespuestaPreguntaVerdaderoFalso) respuesta;
                pnl = new PanelPreguntaVerdaderoFalso(rmo, true);

            } else if (respuesta instanceof RespuestaDesarrollo) {

                RespuestaDesarrollo rmo = (RespuestaDesarrollo) respuesta;
                pnl = new PanelPreguntaDesarrollar(rmo, true);

            }

            colPaneles[i] = pnl;
        }

        return colPaneles;
    }

    /**
     * Se llama cuando el usuario presiona alguna de las casillas apra moverse
     * hacia otra pregunta.
     *
     * @param evt MouseEvent que originó la acción.
     */
    private void lblCasillaMouseClicked(MouseEvent evt) {
        cambiarPreguntaSeleccionada(Integer.valueOf(((JLabel) evt.getSource()).getText()) - 1);
    }

    /**
     * Maneja la transición entre la pregunta anteriormente seleccionada y la
     * próxima.
     *
     * @param intIDPregunta id de la nueva pregunta a seleccionar, siendo este
     * id el orden de la pregunta en el vector o lista que la contiene.
     */
    private void cambiarPreguntaSeleccionada(int intIDPregunta) {

        // Procesamos la casilla vieja.
        colCasillas[intIDSeleccionada].setPreferredSize(new Dimension(21, 21));
        colCasillas[intIDSeleccionada].setMaximumSize(new Dimension(21, 21));
        colCasillas[intIDSeleccionada].setMinimumSize(new Dimension(21, 21));
        colCasillas[intIDSeleccionada].setBorder(null);
        if (resolucion.getColRespuestas().get(intIDSeleccionada).getCalificacion() < 0) {
            colCasillas[intIDSeleccionada].setBackground(Color.LIGHT_GRAY);
        }

        // Procesamos la casilla nueva.
        JLabel lblSeleccionada = colCasillas[intIDPregunta];
        lblSeleccionada.setPreferredSize(new Dimension(26, 26));
        lblSeleccionada.setMaximumSize(new Dimension(26, 26));
        lblSeleccionada.setMinimumSize(new Dimension(26, 26));
        lblSeleccionada.setBorder(LookAndFeelEntropy.BORDE_NARANJA);
        lblSeleccionada.setBackground(new Color(255, 204, 102));
        cargarPanelPreguntaSeleccionada(intIDPregunta);

    }

    /**
     * Carga en el panel de pregunta una nueva pregunta a mostrar.
     *
     * @param idRespuesta id de la respuesta cuya pregunta debe ser completada.
     */
    private void cargarPanelPreguntaSeleccionada(int idRespuesta) {

        limpiarEnunciado();

        this.intIDSeleccionada = idRespuesta;

        lblSiguiente.setVisible(true);
        lblAnterior.setVisible(true);

        if (intIDSeleccionada == colCasillas.length - 1) {
            lblSiguiente.setVisible(false);
        } else if (intIDSeleccionada == 0) {
            lblAnterior.setVisible(false);
        }

        JPanel pnl = colPaneles[idRespuesta];

        Respuesta respuesta = resolucion.getColRespuestas().get(idRespuesta);

        double dblCalificacion = respuesta.getCalificacion();

        if (dblCalificacion >= 0) {
            txtCalificacion.setText(dblCalificacion + "");
        } else {
            txtCalificacion.setText("");
        }

        pintarLabelSuperior(dblCalificacion, respuesta);

        lblPuntaje.setText("/ " + respuesta.getPregunta().getDblPuntaje() + " puntos");

        lblEnunciado.setText("<html>" + respuesta.getPregunta().getStrEnunciado() + "<br><br></html>");

        if (respuesta.getPregunta().getStrReferencia() != null) {
            lblComentarios.setText("<html>" + respuesta.getPregunta().getStrReferencia() + "</html>");
            lblComentarios.setVisible(true);
            lblsComentarios.setVisible(true);
        }

        if (respuesta.getPregunta().getTema() != null) {
            lblTema.setText("<html>" + respuesta.getPregunta().getTema().getStrNombre() + "</html>");
            lblTema.setVisible(true);
            lblsTema.setVisible(true);
        }

        if (pnl != null) {
            pnl.setOpaque(false);
        }

        this.pnlContenidoPregunta.removeAll();
        this.pnlContenidoPregunta.add(pnl);
        this.revalidate();
        this.repaint();
    }

    /**
     * Borra los datos básicos de una pregunta: enunciado, comentario y tema.
     */
    private void limpiarEnunciado() {
        lblEnunciado.setText("");
        lblsComentarios.setVisible(false);
        lblComentarios.setVisible(false);
        lblComentarios.setText("");
        lblsTema.setVisible(false);
        lblTema.setVisible(false);
        lblTema.setText("");
    }

    private void pintarLabelSuperior(double dblCalificacion, Respuesta respuesta) {
        if (dblCalificacion == respuesta.getPregunta().getDblPuntaje()) {
            lblCorrecto.setBackground(new Color(140, 164, 91));
            lblCorrecto.setText("Respuesta Correcta");
        } else if (dblCalificacion > 0) {
            lblCorrecto.setBackground(new Color(255, 204, 102));
            lblCorrecto.setText("Respuesta Incompleta");
        } else if (dblCalificacion == 0) {
            lblCorrecto.setBackground(new Color(255, 102, 102));
            lblCorrecto.setText("Respuesta Incorrecta");
        } else if (dblCalificacion < 0) {
            lblCorrecto.setBackground(new Color(255, 204, 102));
            lblCorrecto.setText("No calificada aún");
        }
    }

    /**
     * Clase que escucha por el tecleo de la tecla Esc.
     */
    private class EscapeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PanelInicio pnlInicio = new PanelInicio();
            pnlInicio.setName("Panel inicio");
            VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(pnlInicio);
            VentanaPrincipal.getInstancia().setTitle("Resoluciones - " + resolucion.getExamen().getStrNombre());
            if (VentanaPrincipal.getInstancia().getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                VentanaPrincipal.getInstancia().pack();
            }
        }
    }

}