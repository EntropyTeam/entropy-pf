package frontend.historialAlumno;

import backend.Asistencia.Asistencia;
import backend.Presentacion.Presentacion;
import frontend.alumnos.*;
import backend.alumnos.GestorAlumnos;
import backend.auxiliares.Mensajes;
import backend.dao.resoluciones.DAOResolucion;
import backend.mail.*;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.examenes.Examen;
import backend.gestores.GestorExamen;
import backend.gestores.GestorHistorialAlumno;
import backend.gestores.GestorImportarPregunta;
import backend.reporte.GestorGenerarReporteResolucion;
import backend.resoluciones.Resolucion;
import backend.usuarios.Alumno;
import frontend.auxiliares.CeldaListaRendererEntropy;
import frontend.auxiliares.ComponentMover;
import frontend.auxiliares.ComponentResizer;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;

import frontend.inicio.VentanaPrincipal;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa la interfaz de administración de diseños de examen.
 *
 * @author Denise
 */
public class DialogHistorialAlumno extends javax.swing.JDialog {


    private DialogSelectorAlumno padre;
    private GestorHistorialAlumno gestorHistorial;
    /**
     * Constructor de la clase.
     *
     * @param padre ventana principal de la aplicación.
     * @param modal true si mantiene el foco, false de lo contrario.
     */
    
    public DialogHistorialAlumno(DialogSelectorAlumno padre, boolean modal) {
        super(padre, modal);
        this.padre=padre;
        initComponents();
        this.setSize(new Dimension(800, 600));
        this.setLocationRelativeTo(padre);
        this.getRootPane().registerKeyboardAction(new EscapeAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        //Fondo translúcido.
        this.pnlEstado.setBackground(new Color(255, 255, 255, 123));
        //Para que el undecorated dialog pueda moverse y ajustarse en tamaño.
        ComponentMover cm = new ComponentMover(JDialog.class, lblBarraTitulo);
        ComponentResizer cr = new ComponentResizer();
        cr.setSnapSize(new Dimension(10, 10));
        cr.registerComponent(this);
        gestorHistorial= new GestorHistorialAlumno();
        cargarDatosAlumno();
        cargarExamenesRendidos();
        cargarClasesAsisitidas();
    }
    
    private void cargarDatosAlumno()
    {
        Alumno alumno = (Alumno)padre.getLstAlumnos().getSelectedValue();
        lblNombre.setText(alumno.getStrApellido() +", "+alumno.getStrNombre());
        lblLegajo.setText(alumno.getStrLegajo());
    }
    
    private void cargarExamenesRendidos()
    {
        Alumno alumno = (Alumno)padre.getLstAlumnos().getSelectedValue();
        ArrayList<Resolucion> resolucion = gestorHistorial.getResoluciones(alumno.getIntAlumnoId());
        DefaultTableModel modeloTabla = (DefaultTableModel)jtExamenesRendidos.getModel();
        for (int i = 0; i < resolucion.size(); i++) {
            if(resolucion.get(i).getExamen()!=null)
            {
            modeloTabla.addRow(new Vector());
            Examen examen = gestorHistorial.getExamene(resolucion.get(i).getExamen().getIntExamenId());
            resolucion.get(i).setExamen(examen);
            modeloTabla.setValueAt(examen.getStrNombre(), i, 0);
            modeloTabla.setValueAt(resolucion.get(i).getCalificacion(), i, 1);
            modeloTabla.setValueAt(examen.getDteFecha(), i, 2);
            modeloTabla.setValueAt(resolucion.get(i), i, 3);
            }
        }
        jtExamenesRendidos.setModel(modeloTabla);  
    }
    
    private void cargarClasesAsisitidas()
    {
        Alumno alumno = (Alumno)padre.getLstAlumnos().getSelectedValue();
        ArrayList<Presentacion>  presentaciones = gestorHistorial.getAsisntencias(alumno.getIntAlumnoId());
        DefaultTableModel modeloTabla = (DefaultTableModel)jtClasesAsistidas.getModel();
        for (int i = 0; i < presentaciones.size(); i++) {
            modeloTabla.addRow(new Vector());
            modeloTabla.setValueAt(presentaciones.get(i).getStrNombre(), i, 0);
            modeloTabla.setValueAt(presentaciones.get(i).getStrDescripcion(), i, 1);
            modeloTabla.setValueAt(presentaciones.get(i).getDteFecha(), i, 2);
            modeloTabla.setValueAt(presentaciones.get(i), i, 3);
        }
        jtClasesAsistidas.setModel(modeloTabla);
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
        pnlDatosDelAlumno = new javax.swing.JPanel();
        lblLegajo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblsNombre = new javax.swing.JLabel();
        lblsLegajo = new javax.swing.JLabel();
        pnlCentral = new javax.swing.JPanel();
        jspExamenesRendidos = new javax.swing.JScrollPane();
        jtExamenesRendidos = new javax.swing.JTable();
        jspClasesAsistidas = new javax.swing.JScrollPane();
        jtClasesAsistidas = new javax.swing.JTable();
        lblBarraTitulo = new javax.swing.JLabel();
        pnlEstado = new javax.swing.JPanel();
        lblIconoEstado = new javax.swing.JLabel();
        lblActualizacionEstado = new javax.swing.JLabel();
        btnRegresar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setSize(new java.awt.Dimension(640, 480));

        pnlFondo.setImagen(GestorImagenes.crearImage("/frontend/imagenes/bg2.jpg"));
        pnlFondo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 0), 1, true));

        pnlDatosDelAlumno.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del alumno", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_REGULAR));
        pnlDatosDelAlumno.setOpaque(false);

        lblLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblLegajo.setText("55192");

        lblNombre.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblNombre.setText("Nombre completo");

        lblsNombre.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsNombre.setText("Nombre:");

        lblsLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsLegajo.setText("Legajo:");

        javax.swing.GroupLayout pnlDatosDelAlumnoLayout = new javax.swing.GroupLayout(pnlDatosDelAlumno);
        pnlDatosDelAlumno.setLayout(pnlDatosDelAlumnoLayout);
        pnlDatosDelAlumnoLayout.setHorizontalGroup(
            pnlDatosDelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosDelAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosDelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosDelAlumnoLayout.createSequentialGroup()
                        .addComponent(lblsNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE))
                    .addGroup(pnlDatosDelAlumnoLayout.createSequentialGroup()
                        .addComponent(lblsLegajo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLegajo, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlDatosDelAlumnoLayout.setVerticalGroup(
            pnlDatosDelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosDelAlumnoLayout.createSequentialGroup()
                .addGroup(pnlDatosDelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsNombre)
                    .addComponent(lblNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosDelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsLegajo)
                    .addComponent(lblLegajo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCentral.setOpaque(false);
        pnlCentral.setLayout(new javax.swing.BoxLayout(pnlCentral, javax.swing.BoxLayout.X_AXIS));

        jspExamenesRendidos.setBorder(javax.swing.BorderFactory.createTitledBorder("Examenes Rendidos"));
        jspExamenesRendidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jspExamenesRendidosMouseClicked(evt);
            }
        });

        jtExamenesRendidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EXAMEN", "NOTA", "FECHA", "OBJETO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtExamenesRendidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtExamenesRendidosMouseClicked(evt);
            }
        });
        jspExamenesRendidos.setViewportView(jtExamenesRendidos);

        pnlCentral.add(jspExamenesRendidos);

        jspClasesAsistidas.setBorder(javax.swing.BorderFactory.createTitledBorder("Clases Asistidas"));

        jtClasesAsistidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOMBRE", "DESCRIPCION", "FECHA", "OBJETO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jspClasesAsistidas.setViewportView(jtClasesAsistidas);

        pnlCentral.add(jspClasesAsistidas);

        lblBarraTitulo.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo.setText("Historial de alumno");

        lblIconoEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconoEstado.setText(" ");
        lblIconoEstado.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblActualizacionEstado.setFont(new java.awt.Font("Calibri", 2, 12)); // NOI18N
        lblActualizacionEstado.setForeground(new java.awt.Color(102, 102, 102));
        lblActualizacionEstado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblActualizacionEstado.setText("Acá se escriben estados...");
        lblActualizacionEstado.setAlignmentX(0.5F);
        lblActualizacionEstado.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout pnlEstadoLayout = new javax.swing.GroupLayout(pnlEstado);
        pnlEstado.setLayout(pnlEstadoLayout);
        pnlEstadoLayout.setHorizontalGroup(
            pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadoLayout.createSequentialGroup()
                .addContainerGap(590, Short.MAX_VALUE)
                .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlEstadoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlEstadoLayout.setVerticalGroup(
            pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEstadoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlEstadoLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        btnRegresar2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnRegresar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_volver.png"))); // NOI18N
        btnRegresar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegresar2.setContentAreaFilled(false);
        btnRegresar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegresar2.setIconTextGap(10);
        btnRegresar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegresar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDatosDelAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCentral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addComponent(pnlEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBarraTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblBarraTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(pnlDatosDelAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCentral, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(btnRegresar2)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 586, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 48, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresar2ActionPerformed
        dispose();
    }//GEN-LAST:event_btnRegresar2ActionPerformed

    private void jspExamenesRendidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jspExamenesRendidosMouseClicked

    }//GEN-LAST:event_jspExamenesRendidosMouseClicked

    private void jtExamenesRendidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtExamenesRendidosMouseClicked
        if(evt.getClickCount()==2 )
        {
          try {
                Resolucion  resolucion  = (Resolucion) jtExamenesRendidos.getModel().getValueAt(jtExamenesRendidos.getSelectedRow(), 3);
                if(resolucion!=null)
                {
                GestorGenerarReporteResolucion gestorReporte = new GestorGenerarReporteResolucion(resolucion);
                gestorReporte.generarReporteResolucion();
                String pathArchivo= gestorReporte.getResolucion();
                Path path = Paths.get(pathArchivo);
                byte[] pdf = Files.readAllBytes(path);
                File pdfArchivo = new File(pathArchivo);
                Desktop.getDesktop().open(pdfArchivo);
                }
            }
            catch(Exception e) {
                System.err.println("Ocurrió una excepción creando el PDF:  "+e.toString());
            }
        }
                // TODO add your handling code here:
    }//GEN-LAST:event_jtExamenesRendidosMouseClicked
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JScrollPane jspClasesAsistidas;
    private javax.swing.JScrollPane jspExamenesRendidos;
    private javax.swing.JTable jtClasesAsistidas;
    private javax.swing.JTable jtExamenesRendidos;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblBarraTitulo;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JLabel lblLegajo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblsLegajo;
    private javax.swing.JLabel lblsNombre;
    private javax.swing.JPanel pnlCentral;
    private javax.swing.JPanel pnlDatosDelAlumno;
    private javax.swing.JPanel pnlEstado;
    private frontend.auxiliares.PanelConFondo pnlFondo;
    // End of variables declaration//GEN-END:variables



    /**
     * Clase que escucha por el tecleo de la tecla Esc.
     */
    private class EscapeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
