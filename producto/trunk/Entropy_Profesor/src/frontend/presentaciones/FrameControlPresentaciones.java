package frontend.presentaciones;
import backend.auxiliares.Mensajes;
import backend.examenes.EstadoTomaExamen;
import backend.presentaciones.EstadoPresentacion;
import backend.gestores.GestorDePresentacion;
import backend.red.GestorRedAdHoc;
import backend.usuarios.Alumno;
import frontend.auxiliares.CeldaBotonRendererEntropy;
import frontend.auxiliares.CeldaHeaderMultiLineRenderer;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.VentanaPrincipal;
import frontend.tomaexamenes.CeldaTomarExamenRenderer;
import frontend.tomaexamenes.DialogCancelarExamen;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Denise
 */
public class FrameControlPresentaciones extends javax.swing.JFrame {

    private final VentanaPrincipal mPadre;
    private final GestorBarrasDeEstado gestorEstados;
    private DefaultTableModel defaultTblAlumnos;
    private GestorDePresentacion gestorDePresentacion;

    /**
     * Constructor de la clase.
     *
     * @param mPadre Ventana Principal padre
     */
    public FrameControlPresentaciones(VentanaPrincipal mPadre) {
        initComponents();
        this.mPadre = mPadre;
        setLocationRelativeTo(null);
        this.setIconImage(this.getIconImage());
        setTitle("Presentación en tiempo real");
        this.gestorEstados = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);
        this.gestorEstados.setNuevoEstadoImportante("¡Bienvenido al panel de control de presentaciones!");

        //Fondo translúcido.
        this.pnlEstado.setBackground(new Color(255, 255, 255, 123));
        this.tblAlumnos.setBackground(new Color(255, 255, 255, 123));
        this.scrTablaAlumnos.setBackground(new Color(255, 255, 255, 123));
        this.scrTablaAlumnos.getViewport().setOpaque(false);

        //Configuración de la tabla.
        tblAlumnos.setDefaultRenderer(Object.class, new CeldaTomarExamenRenderer());
        tblAlumnos.getTableHeader().setDefaultRenderer(new CeldaHeaderMultiLineRenderer());
        tblAlumnos.getColumn("Desconectar").setMaxWidth(70);
        tblAlumnos.getColumn("Otros").setMaxWidth(70);
        tblAlumnos.setShowVerticalLines(false);
        tblAlumnos.setGridColor(LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
        tblAlumnos.setIgnoreRepaint(false);
        defaultTblAlumnos = (DefaultTableModel) tblAlumnos.getModel();

        //Creo el gestor de Toma de Examen
        gestorDePresentacion = new GestorDePresentacion(this);
        gestorDePresentacion.comenzarConexiones();

        Action anular = new AbstractAction("-") {
            @Override
            public void actionPerformed(ActionEvent e) {
                anularAlumno(e);
            }
        };
        CeldaBotonRendererEntropy anularRenderer = new CeldaBotonRendererEntropy(
                tblAlumnos,
                anular,
                4,
                GestorImagenes.crearImageIcon("/frontend/imagenes/ic_borrar_cuadrado.png"),
                GestorImagenes.crearImageIcon("/frontend/imagenes/ic_borrar_cuadrado_presionado.png"));

        Action mostrarInfo = new AbstractAction("+") {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarMasInfo(e);
            }
        };
        CeldaBotonRendererEntropy masInfoRenderer = new CeldaBotonRendererEntropy(
                tblAlumnos,
                mostrarInfo,
                5,
                GestorImagenes.crearImageIcon("/frontend/imagenes/ic_info.png"),
                GestorImagenes.crearImageIcon("/frontend/imagenes/ic_info.png"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFondo = new frontend.auxiliares.PanelConFondo();
        pnlBotones = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnGuardarAsistencia = new javax.swing.JButton();
        scrTablaAlumnos = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlFondo.setImagen(GestorImagenes.crearImage("/frontend/imagenes/bg_gris.jpg"));

        pnlBotones.setLayout(new java.awt.GridLayout(1, 0));

        btnCancelar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_cancelar.png"))); // NOI18N
        btnCancelar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.setIconTextGap(10);
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelarMouseExited(evt);
            }
        });
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCancelar);

        btnGuardarAsistencia.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnGuardarAsistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_guardar.png"))); // NOI18N
        btnGuardarAsistencia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnGuardarAsistencia.setContentAreaFilled(false);
        btnGuardarAsistencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarAsistencia.setIconTextGap(10);
        btnGuardarAsistencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarAsistenciaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardarAsistenciaMouseExited(evt);
            }
        });
        btnGuardarAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAsistenciaActionPerformed(evt);
            }
        });
        pnlBotones.add(btnGuardarAsistencia);

        scrTablaAlumnos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Panel de control", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_CURSIVA));
        scrTablaAlumnos.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Alumno", "Estado", "Inicio", "Finalizo", "Desconectar", "Otros"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlumnos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrTablaAlumnos.setViewportView(tblAlumnos);

        pnlEstado.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.lightGray));

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
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlEstadoLayout.setVerticalGroup(
            pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEstadoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrTablaAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
                    .addComponent(pnlEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(scrTablaAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(320, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Cancelar la toma del examen.");
        repaint();
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        repaint();
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.terminarPresentacion(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.terminarPresentacion(false);
    }//GEN-LAST:event_formWindowClosing

    private void btnGuardarAsistenciaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarAsistenciaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarAsistenciaMouseEntered

    private void btnGuardarAsistenciaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarAsistenciaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarAsistenciaMouseExited

    private void btnGuardarAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAsistenciaActionPerformed
        DialogGuardarAsistencia dialogGuardarClase = new DialogGuardarAsistencia(this, true, gestorDePresentacion);
        dialogGuardarClase.setVisible(true);
    }//GEN-LAST:event_btnGuardarAsistenciaActionPerformed

    /**
     * Anula el examen del alumno en cuestión.
     *
     * @param e evento de acción provocado al clickear el botón en la tabla.
     */
    private void anularAlumno(ActionEvent e) {

        if (tblAlumnos.getModel().getRowCount() > 0
                && Mensajes.mostrarConfirmacion("Está a punto de desconectar al alumno de la presentacion. ¿Realmente desea continuar?")) {
            int intFila = Integer.valueOf(e.getActionCommand());
            gestorDePresentacion.anularPresentacion(intFila);
        }

        throw new UnsupportedOperationException("COMPLETAR");
    }

    /**
     * Muestra más información relacionada a un alumno en particular.
     * Información técnica.
     *
     * @param e evento de acción provocado al clickear el botón en la tabla.
     */
    private void mostrarMasInfo(ActionEvent e) {
        int intFila = Integer.valueOf(e.getActionCommand());
        gestorDePresentacion.mostrarDatosAlumno(intFila);
    }

    @Override
    public Image getIconImage() {
        return GestorImagenes.crearImage("/frontend/imagenes/ic_system.png");
    }

    public int agregarAlumno(Alumno alumno) {
        String nombre = alumno.getStrNombre();
        String estado = EstadoPresentacion.AUTENTICADO.toString();
        defaultTblAlumnos.addRow(new Object[]{nombre, estado, "-", "-"});
        return tblAlumnos.getRowCount() - 1;
    }

    public void desconectarAlumno(int intIndice) {
        defaultTblAlumnos.setValueAt(EstadoPresentacion.INTERRUMPIDO, intIndice, 1);
    }

    public void iniciarPresentacion(int intIndice) {
        defaultTblAlumnos.setValueAt(EstadoPresentacion.CONECTADO, intIndice, 1);
        DateFormat formatoTiempo = new SimpleDateFormat("HH:mm:ss");
        defaultTblAlumnos.setValueAt(formatoTiempo.format(new Date()), intIndice, 2);
    }

    public void finalizarPresentacion(int intIndice) {
        defaultTblAlumnos.setValueAt(EstadoPresentacion.DESCONECTADO, intIndice, 1);
        DateFormat formatoTiempo = new SimpleDateFormat("HH:mm:ss");
        defaultTblAlumnos.setValueAt(formatoTiempo.format(new Date()), intIndice, 3);
    }

    public void mostrarErrorAlEnviarPresentacion(int intIndice) {
        Mensajes.mostrarError("Error al enviar el mensaje a " + tblAlumnos.getValueAt(intIndice, 0));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardarAsistencia;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlEstado;
    private frontend.auxiliares.PanelConFondo pnlFondo;
    private javax.swing.JScrollPane scrTablaAlumnos;
    private javax.swing.JTable tblAlumnos;
    // End of variables declaration//GEN-END:variables

    /**
     * Funcionalidad cuando el usuario cierra la ventana.
     *
     * @param blnEsCancelacion true si se está cancelando el examen, false si se
     * ha terminado la toma.
     */
    private void terminarPresentacion(boolean blnEsCancelacion) {
        if (Mensajes.mostrarConfirmacion("Está a punto de finalizar la presentacion. ¿Realmente desea continuar?")) {
            //gestorDePresentacion.notificarFinalizacionPresentacion();
        }
        this.dispose();
        GestorRedAdHoc gestorRedAdHoc = new GestorRedAdHoc();
        gestorRedAdHoc.desconectar();
        throw new UnsupportedOperationException("Falta cerrar los hilos y todo el lío.");
    }
}
