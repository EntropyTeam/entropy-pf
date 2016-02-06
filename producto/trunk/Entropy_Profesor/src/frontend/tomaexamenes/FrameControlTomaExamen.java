package frontend.tomaexamenes;
import backend.auxiliares.Mensajes;
import backend.examenes.EstadoTomaExamen;
import backend.examenes.Examen;
import backend.gestores.GestorExamen;
import backend.gestores.GestorTomaExamen;
import backend.red.GestorRedAdHoc;
import backend.usuarios.Alumno;
import frontend.auxiliares.CeldaBotonRendererEntropy;
import frontend.auxiliares.CeldaHeaderMultiLineRenderer;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.VentanaPrincipal;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Denise
 */
public class FrameControlTomaExamen extends javax.swing.JFrame {

    private final VentanaPrincipal mPadre;
    private final GestorBarrasDeEstado gestorEstados;
    private DefaultTableModel defaultTblAlumnos;
    private DialogModificarTiempo dlgModificarTiempo;
    private GestorTomaExamen gestorTomaExamen;
    private Examen examenTomar;
    private ArrayList<CuentaRegresiva> colCuentasRegresivas;
    private int intFinalizado;

    /**
     * Constructor de la clase.
     *
     * @param mPadre Ventana Principal padre
     */
    public FrameControlTomaExamen(VentanaPrincipal mPadre, Examen examenTomar) {
        initComponents();
        this.mPadre = mPadre;
        this.mPadre.setBlnSeEstaTomandoExamen(true);
        this.intFinalizado = 0;
        setLocationRelativeTo(null);
        this.setIconImage(this.getIconImage());
        setTitle("Toma de examen");
        this.gestorEstados = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);
        this.gestorEstados.setNuevoEstadoImportante("¡Bienvenido al panel de control de toma de examen!");

        //Fondo translúcido.
        this.pnlEstado.setBackground(new Color(255, 255, 255, 123));
        this.tblAlumnos.setBackground(new Color(255, 255, 255, 123));
        this.scrTablaAlumnos.setBackground(new Color(255, 255, 255, 123));
        this.scrTablaAlumnos.getViewport().setOpaque(false);
        this.pnlDatosExamen.setBackground(new Color(255, 255, 255, 123));

        //Configuración de la tabla.
        tblAlumnos.setDefaultRenderer(Object.class, new CeldaTomarExamenRenderer());
        tblAlumnos.getTableHeader().setDefaultRenderer(new CeldaHeaderMultiLineRenderer());
        tblAlumnos.getColumn("Anular").setMaxWidth(70);
        tblAlumnos.getColumn("Otros").setMaxWidth(70);
        tblAlumnos.setShowVerticalLines(false);
        tblAlumnos.setGridColor(LookAndFeelEntropy.COLOR_SELECCION_TEXTO);
        tblAlumnos.setIgnoreRepaint(false);
        defaultTblAlumnos = (DefaultTableModel) tblAlumnos.getModel();

        // Seteo el examen
        this.examenTomar = examenTomar;
        lblExamen.setText(examenTomar.getStrNombre());
        lblCurso.setText(examenTomar.getCurso().getStrNombre());
        lblInstitucion.setText(examenTomar.getCurso().getInstitucion().getStrNombre());
        if (examenTomar.getStrDescripcion().isEmpty()){
            lblsDescripcion.setVisible(false);
            lblDescripcion.setVisible(false);
        } else {
            lblDescripcion.setText(examenTomar.getStrDescripcion());
            lblDescripcion.setToolTipText(examenTomar.getStrDescripcion());
        }

        //Creo el gestor de Toma de Examen
        gestorTomaExamen = new GestorTomaExamen(this, examenTomar);
        gestorTomaExamen.comenzarConexiones();
        colCuentasRegresivas = new ArrayList<>();

        Action anular = new AbstractAction("-") {
            @Override
            public void actionPerformed(ActionEvent e) {
                anularAlumno(e);
            }
        };
        CeldaBotonRendererEntropy anularRenderer = new CeldaBotonRendererEntropy(
                tblAlumnos,
                anular,
                6,
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
                7,
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
        btnFinalizar = new javax.swing.JButton();
        btnModificarTiempo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        scrTablaAlumnos = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();
        lblsPorcentajeTerminados = new javax.swing.JLabel();
        lblPorcentajeTerminados = new javax.swing.JLabel();
        pnlDatosExamen = new javax.swing.JPanel();
        lblsExamen = new javax.swing.JLabel();
        lblsCurso = new javax.swing.JLabel();
        lblsInstitucion = new javax.swing.JLabel();
        lblsDescripcion = new javax.swing.JLabel();
        lblExamen = new javax.swing.JLabel();
        lblCurso = new javax.swing.JLabel();
        lblInstitucion = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        pnlFondo.setImagen(GestorImagenes.crearImage("/frontend/imagenes/bg_gris.jpg"));
        pnlFondo.setPreferredSize(new java.awt.Dimension(200, 400));

        pnlBotones.setLayout(new java.awt.GridLayout(1, 0));

        btnFinalizar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnFinalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_tiempo_terminado_35x35.png"))); // NOI18N
        btnFinalizar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnFinalizar.setContentAreaFilled(false);
        btnFinalizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFinalizar.setIconTextGap(10);
        btnFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFinalizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFinalizarMouseExited(evt);
            }
        });
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnFinalizar);

        btnModificarTiempo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnModificarTiempo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_tiempo_multicolor.png"))); // NOI18N
        btnModificarTiempo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnModificarTiempo.setContentAreaFilled(false);
        btnModificarTiempo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModificarTiempo.setIconTextGap(10);
        btnModificarTiempo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarTiempoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnModificarTiempoMouseExited(evt);
            }
        });
        btnModificarTiempo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarTiempoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnModificarTiempo);

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

        scrTablaAlumnos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Panel de control", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_CURSIVA));
        scrTablaAlumnos.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Alumno", "Estado", "Tiempo inicio", "Tiempo restante", "Preguntas respondidas", "Progreso", "Anular", "Otros"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
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

        lblsPorcentajeTerminados.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsPorcentajeTerminados.setText("Porcentaje de alumnos que han terminado:");

        lblPorcentajeTerminados.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblPorcentajeTerminados.setText("0%");

        pnlDatosExamen.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Examen en curso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_CURSIVA, LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL));

        lblsExamen.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsExamen.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        lblsExamen.setText("Examen:");

        lblsCurso.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsCurso.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        lblsCurso.setText("Curso:");

        lblsInstitucion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsInstitucion.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        lblsInstitucion.setText("Institución:");

        lblsDescripcion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsDescripcion.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        lblsDescripcion.setText("Descripción:");

        lblExamen.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblExamen.setText("Examen");

        lblCurso.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblCurso.setText("Curso");

        lblInstitucion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblInstitucion.setText("Institución");

        lblDescripcion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblDescripcion.setText("Descripción");

        javax.swing.GroupLayout pnlDatosExamenLayout = new javax.swing.GroupLayout(pnlDatosExamen);
        pnlDatosExamen.setLayout(pnlDatosExamenLayout);
        pnlDatosExamenLayout.setHorizontalGroup(
            pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                        .addComponent(lblsDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                        .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblsInstitucion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, Short.MAX_VALUE)
                            .addComponent(lblsExamen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsCurso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblExamen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblInstitucion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlDatosExamenLayout.setVerticalGroup(
            pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsExamen)
                    .addComponent(lblExamen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsCurso)
                    .addComponent(lblCurso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsInstitucion)
                    .addComponent(lblInstitucion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsDescripcion)
                    .addComponent(lblDescripcion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(lblsPorcentajeTerminados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPorcentajeTerminados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(513, 513, 513))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlDatosExamen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlEstado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrTablaAlumnos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE)))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(pnlDatosExamen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrTablaAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsPorcentajeTerminados)
                    .addComponent(lblPorcentajeTerminados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlFondoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(325, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 769, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarTiempoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarTiempoMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Modificar el tiempo asignado para el examen.");
        repaint();
    }//GEN-LAST:event_btnModificarTiempoMouseEntered

    private void btnModificarTiempoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarTiempoMouseExited
        this.gestorEstados.volverAEstadoImportante();
        repaint();
    }//GEN-LAST:event_btnModificarTiempoMouseExited

    private void btnModificarTiempoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarTiempoActionPerformed
        getGraphics().dispose();
        dlgModificarTiempo = new DialogModificarTiempo(this, true);
        dlgModificarTiempo.setVisible(true);
        tblAlumnos.paintComponents(tblAlumnos.getGraphics());
    }//GEN-LAST:event_btnModificarTiempoActionPerformed

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Cancelar la toma del examen.");
        repaint();
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        repaint();
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.terminarTomaDeExamen(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.terminarTomaDeExamen(false);
    }//GEN-LAST:event_formWindowClosing

    private void btnFinalizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFinalizarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Termina la toma de examen.");
        repaint();
    }//GEN-LAST:event_btnFinalizarMouseEntered

    private void btnFinalizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFinalizarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        repaint(); 
    }//GEN-LAST:event_btnFinalizarMouseExited

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        this.gestorEstados.setEstadoInstantaneo("Está a punto de finalizar el examen.");
        // Terminar examen, pedir resoluciones y finalizar las conexiones.
        if (Mensajes.mostrarConfirmacion("Está a punto de finalizar el examen."
                + " Los exámenes de alumnos que no hayan terminado serán interrumpidos"
                + " y se guardará su estado actual. ¿Realmente desea continuar?")) {
            gestorTomaExamen.notificarFinalizacionExamen();
            this.dispose();
            GestorRedAdHoc gestorRedAdHoc = new GestorRedAdHoc();
            gestorRedAdHoc.desconectar();
            // throw new UnsupportedOperationException("Falta cerrar los hilos y todo el lío.");
        }
        repaint();
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        this.gestorTomaExamen.pararConexiones();
        this.mPadre.setBlnSeEstaTomandoExamen(false);
    }//GEN-LAST:event_formWindowClosed

    /**
     * Anula el examen del alumno en cuestión.
     *
     * @param e evento de acción provocado al clickear el botón en la tabla.
     */
    private void anularAlumno(ActionEvent e) {

        if (tblAlumnos.getModel().getRowCount() > 0
                && Mensajes.mostrarConfirmacion("Está a punto de anular el examen del alumno. ¿Realmente desea continuar?")) {
            DialogCancelarExamen dlgCancelar = new DialogCancelarExamen(this, true);
            dlgCancelar.setVisible(true);
            if (dlgCancelar.getAccionElegida().equals(DialogCancelarExamen.Accion.CONTINUAR)) {
                int intFila = Integer.valueOf(e.getActionCommand());
                String strJustificacion = dlgCancelar.getStrMotivoCancelacion();
                gestorTomaExamen.anularResolucion(intFila, strJustificacion);
                ((DefaultTableModel) tblAlumnos.getModel()).setValueAt(EstadoTomaExamen.INTERRUMPIDO.toString(), intFila, 1);
                for (CuentaRegresiva cuentaRegresiva : colCuentasRegresivas) {
                    if (cuentaRegresiva.intIndice == intFila) {
                        cuentaRegresiva.parar();
                    }
                }
            }
        }

    }

    /**
     * Muestra más información relacionada a un alumno en particular.
     * Información técnica.
     *
     * @param e evento de acción provocado al clickear el botón en la tabla.
     */
    private void mostrarMasInfo(ActionEvent e) {
        int intFila = Integer.valueOf(e.getActionCommand());
        gestorTomaExamen.mostrarDatosAlumno(intFila);
    }

    @Override
    public Image getIconImage() {
        return GestorImagenes.crearImage("/frontend/imagenes/ic_system.png");
    }

    public Examen getExamenATomar() {
        return examenTomar;
    }

    public void setExamenATomar(Examen examenATomar) {
        this.examenTomar = examenATomar;
    }

    public int agregarAlumno(Alumno alumno) {
        if (defaultTblAlumnos.getRowCount() == 0) {
            GestorExamen.getInstancia().guardarExamen(examenTomar);
        }
        String nombre = alumno.getStrNombre();
        String estado = EstadoTomaExamen.NO_AUTENTICADO.toString();
        defaultTblAlumnos.addRow(new Object[]{nombre, estado, "-", "-", 0 + "/" + gestorTomaExamen.getExamenResolver().getColPreguntas().size()});
        return tblAlumnos.getRowCount() - 1;
    }

    public void interrumpirAlumno(int intIndice) {
        defaultTblAlumnos.setValueAt(EstadoTomaExamen.INTERRUMPIDO, intIndice, 1);
    }

    public void iniciarExamen(int intIndice) {
        defaultTblAlumnos.setValueAt(EstadoTomaExamen.INICIADO, intIndice, 1);

        DateFormat formatoTiempo = new SimpleDateFormat("HH:mm:ss");
        defaultTblAlumnos.setValueAt(formatoTiempo.format(new Date()), intIndice, 2);
        defaultTblAlumnos.setValueAt(0 + "/" + gestorTomaExamen.getExamenResolver().getColPreguntas().size(), intIndice, 4);
        colCuentasRegresivas.add(new CuentaRegresiva(intIndice));
    }

    public void notificarProgreso(int intIndice, int intCantPreguntasRespondidas) {
        defaultTblAlumnos.setValueAt(intCantPreguntasRespondidas + "/" + gestorTomaExamen.getExamenResolver().getColPreguntas().size(), intIndice, 4);
        float floatProgreso = (float) intCantPreguntasRespondidas / gestorTomaExamen.getExamenResolver().getColPreguntas().size();
        defaultTblAlumnos.setValueAt((int) (floatProgreso * 100), intIndice, 5);
    }

    public void finalizarExamenAlumno(int intIndice) {
        intFinalizado++;
        lblPorcentajeTerminados.setText((double) (intFinalizado * 100 / tblAlumnos.getRowCount()) + "%");
        defaultTblAlumnos.setValueAt(EstadoTomaExamen.COMPLETADO, intIndice, 1);
        for (CuentaRegresiva cuentaRegresiva : colCuentasRegresivas) {
            if (cuentaRegresiva.intIndice == intIndice) {
                cuentaRegresiva.parar();
            }
        }
    }

    public void mostrarErrorAlEnviarExamen(int intIndice) {
        Mensajes.mostrarError("Error al enviar el mensaje a " + tblAlumnos.getValueAt(intIndice, 0));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnModificarTiempo;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblExamen;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JLabel lblInstitucion;
    private javax.swing.JLabel lblPorcentajeTerminados;
    private javax.swing.JLabel lblsCurso;
    private javax.swing.JLabel lblsDescripcion;
    private javax.swing.JLabel lblsExamen;
    private javax.swing.JLabel lblsInstitucion;
    private javax.swing.JLabel lblsPorcentajeTerminados;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDatosExamen;
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
    private void terminarTomaDeExamen(boolean blnEsCancelacion) {
        if (Mensajes.mostrarConfirmacion("Está a punto de cancelar el examen. ¿Realmente desea continuar?")) {
            if (blnEsCancelacion && tblAlumnos.getModel().getRowCount() > 0) {
                DialogCancelarExamen dlgCancelar = new DialogCancelarExamen(this, true);
                dlgCancelar.setVisible(true);
                if (dlgCancelar.getAccionElegida().equals(DialogCancelarExamen.Accion.CONTINUAR)) {
                    GestorExamen.getInstancia().cancelarExamen(examenTomar.getIntExamenId(), dlgCancelar.getStrMotivoCancelacion());
                    gestorTomaExamen.notificarCancelacionExamen();
                } else {
                    return;
                }
            } else if (!blnEsCancelacion) {
                GestorExamen.getInstancia().actualizarEstadoExamen(examenTomar.getIntExamenId());
            }
            this.dispose();
            GestorRedAdHoc gestorRedAdHoc = new GestorRedAdHoc();
            gestorRedAdHoc.desconectar();
            throw new UnsupportedOperationException("Falta cerrar los hilos y todo el lío.");
        }
    }

    public void notificarAutenticacionExitosa(int intIndice) {
        defaultTblAlumnos.setValueAt(EstadoTomaExamen.AUTENTICADO, intIndice, 1);
    }
    
    public boolean validarTiempoQuitar(int intMinutosQuitar) {
        for (CuentaRegresiva cuentaRegresiva : colCuentasRegresivas) {
            if (cuentaRegresiva.getMinutos() < intMinutosQuitar) return false;
        }
        return true;
    }
    
    public void agregarTiempo(int intMinutosAgregados) {
        this.gestorTomaExamen.agregarTiempo(intMinutosAgregados);
        for (CuentaRegresiva cuentaRegresiva : colCuentasRegresivas) {
            cuentaRegresiva.agregarTiempo(intMinutosAgregados);
        }
    }
    
    public void quitarTiempo(int intMinutosQuitados) {
        this.gestorTomaExamen.quitarTiempo(intMinutosQuitados);
        for (CuentaRegresiva cuentaRegresiva : colCuentasRegresivas) {
            cuentaRegresiva.quitarTiempo(intMinutosQuitados);
        }
    }

    /**
     * Clase que se encarga de manejar el timer de cuenta regresiva.
     */
    private class CuentaRegresiva {

        private final Timer tmrTemporizador;
        private int intIndice;
        private int minutos;
        private int segundos;

        public CuentaRegresiva(final int intIndice) {
            this.intIndice = intIndice;
            minutos = gestorTomaExamen.getExamenResolver().getIntTiempo();
            segundos = 0;
            String strTiempoRestante = String.format("%02d", (int) minutos / 60) + ":"
                    + String.format("%02d", minutos % 60) + ":"
                    + String.format("%02d", segundos);
            defaultTblAlumnos.setValueAt(strTiempoRestante, intIndice, 3);

            this.tmrTemporizador = new Timer(1000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == tmrTemporizador) {

                        if (segundos == 0) {
                            segundos = 59;
                            minutos--;
                        }

                        segundos--;

                        String strTiempoRestante = String.format("%02d", (int) minutos / 60) + ":"
                                + String.format("%02d", minutos % 60) + ":"
                                + String.format("%02d", segundos);
                        defaultTblAlumnos.setValueAt(strTiempoRestante, intIndice, 3);

                        if (segundos == 0 && minutos == 0) {
                            tmrTemporizador.stop();
                        }
                    }
                }
            });

            tmrTemporizador.start();
        }

        private void parar() {
            tmrTemporizador.stop();
        }
        
        public int getMinutos() {
            return this.minutos;
        }
        
        public void agregarTiempo(int intMinutosAgregados) {
            this.minutos += intMinutosAgregados;
        }

        public void quitarTiempo(int intMinutosQuitados) {
            this.minutos -= intMinutosQuitados;
        }
    }
}
