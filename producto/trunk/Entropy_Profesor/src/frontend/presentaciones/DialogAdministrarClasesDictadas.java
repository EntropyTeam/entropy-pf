package frontend.presentaciones;

import backend.Presentacion.Presentacion;
import backend.auxiliares.Mensajes;
import backend.dao.diseños.DAOCurso;
import backend.dao.diseños.DAOInstitucion;
import backend.dao.presentacion.DAOPresentacion;
import backend.dao.presentacion.IDAOPresentacion;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.reporte.GestorGenerarReportePresentacionesRealizadas;
import frontend.auxiliares.CeldaListaRendererEntropy;
import frontend.auxiliares.LookAndFeelEntropy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Jose
 */
public class DialogAdministrarClasesDictadas extends javax.swing.JDialog {

    private DAOInstitucion daoInstitucion = new DAOInstitucion();
    private DAOCurso daoCurso = new DAOCurso();

    /**
     * Creates new form DialogAdministrarClasesDictadas
     */
    public DialogAdministrarClasesDictadas(java.awt.Frame parent, boolean modal) {
        super(parent, false);
        initComponents();
        setLocationRelativeTo(parent);
        setTitle("Administrar presentaciones");
        lstClasesDictadas.setCellRenderer(new CeldaListaRendererEntropy());
        cargarComboInstituciones();
        cargarComboCursos(((Institucion) cmbInstitucion.getSelectedItem()).getIntInstitucionId());
    }

    private void cargarComboInstituciones() {
        DAOInstitucion daoInstitucion = new DAOInstitucion();
        ArrayList<Institucion> listaInstituciones = daoInstitucion.recuperarTodasLasInstituciones("");
        DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
        for (Institucion institucion : listaInstituciones) {
            modeloCombo.addElement(institucion);
        }
        cmbInstitucion.setModel(modeloCombo);
    }

    private void cargarComboCursos(int idInstitucion) {
        DAOCurso daoCurso = new DAOCurso();
        Institucion institucion = daoInstitucion.recuperarInstitucion(idInstitucion);
        ArrayList<Curso> listacurso = daoCurso.recuperarTodosLosCursos(institucion, "");
        DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
        for (Curso curso : listacurso) {
            modeloCombo.addElement(curso);

        }
        cmbCurso.setModel(modeloCombo);
    }

    private void buscarListaDeClasesDictadas() {
        IDAOPresentacion daoPresentacion = new DAOPresentacion();
        ArrayList<Presentacion> presentaciones = daoPresentacion.recuperarPresentaciones(((Curso) cmbCurso.getSelectedItem()).getIntCursoId(), dcFechaDesde.getDate(), dcFechaHasta.getDate());
        if (presentaciones == null) {
            Mensajes.mostrarInformacion("No se encontraron resultados para la búsqueda realizada.");
            return;
        }
        DefaultListModel modeloLista = new DefaultListModel();
        for (Presentacion presentacion : presentaciones) {
            modeloLista.addElement(presentacion);

        }
        lstClasesDictadas.setModel(modeloLista);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDatosClase = new javax.swing.JPanel();
        lblInstitucion1 = new javax.swing.JLabel();
        lblCurso1 = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnVistaPrevia = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        lblNombreClase = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        cmbInstitucion = new javax.swing.JComboBox();
        cmbCurso = new javax.swing.JComboBox();
        dcFechaDesde = new com.toedter.calendar.JDateChooser();
        dcFechaHasta = new com.toedter.calendar.JDateChooser();
        scrClases = new javax.swing.JScrollPane();
        lstClasesDictadas = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlDatosClase.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clases dictadas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        pnlDatosClase.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        pnlDatosClase.setMinimumSize(pnlDatosClase.getPreferredSize());

        lblInstitucion1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblInstitucion1.setText("Institución:");

        lblCurso1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblCurso1.setText("Curso:");

        pnlBotones.setLayout(new java.awt.GridLayout(1, 0));

        btnVolver.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_volver.png"))); // NOI18N
        btnVolver.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolver.setIconTextGap(10);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVolver);

        btnVistaPrevia.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnVistaPrevia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_examinar_25x25.png"))); // NOI18N
        btnVistaPrevia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVistaPrevia.setContentAreaFilled(false);
        btnVistaPrevia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVistaPrevia.setIconTextGap(10);
        btnVistaPrevia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVistaPreviaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVistaPreviaMouseExited(evt);
            }
        });
        btnVistaPrevia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVistaPreviaActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVistaPrevia);

        btnImprimir.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_imprimir.png"))); // NOI18N
        btnImprimir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.setIconTextGap(10);
        btnImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnImprimirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnImprimirMouseExited(evt);
            }
        });
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        pnlBotones.add(btnImprimir);

        lblNombreClase.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblNombreClase.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNombreClase.setText("Desde:");

        lblDescripcion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblDescripcion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDescripcion.setText("Hasta:");

        cmbInstitucion.setBackground(new java.awt.Color(255, 204, 102));
        cmbInstitucion.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbInstitucion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbInstitucionItemStateChanged(evt);
            }
        });

        cmbCurso.setBackground(new java.awt.Color(255, 204, 102));
        cmbCurso.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        dcFechaDesde.setDate(new Date());
        dcFechaDesde.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        dcFechaHasta.setDate(new Date());
        dcFechaHasta.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        lstClasesDictadas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrClases.setViewportView(lstClasesDictadas);

        javax.swing.GroupLayout pnlDatosClaseLayout = new javax.swing.GroupLayout(pnlDatosClase);
        pnlDatosClase.setLayout(pnlDatosClaseLayout);
        pnlDatosClaseLayout.setHorizontalGroup(
            pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosClaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrClases, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addGroup(pnlDatosClaseLayout.createSequentialGroup()
                        .addGroup(pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCurso1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblInstitucion1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreClase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12)
                        .addGroup(pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCurso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbInstitucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcFechaDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcFechaHasta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlDatosClaseLayout.setVerticalGroup(
            pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosClaseLayout.createSequentialGroup()
                .addGroup(pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblInstitucion1)
                    .addComponent(cmbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCurso1)
                    .addComponent(cmbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosClaseLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblNombreClase)
                        .addGap(16, 16, 16)
                        .addComponent(lblDescripcion))
                    .addGroup(pnlDatosClaseLayout.createSequentialGroup()
                        .addComponent(dcFechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(scrClases, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDatosClase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDatosClase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVistaPreviaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVistaPreviaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVistaPreviaMouseEntered

    private void btnVistaPreviaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVistaPreviaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVistaPreviaMouseExited

    private void btnVistaPreviaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVistaPreviaActionPerformed
        if (validarFechas()) {
            buscarListaDeClasesDictadas();
        }
    }//GEN-LAST:event_btnVistaPreviaActionPerformed

    private void btnImprimirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimirMouseEntered

    private void btnImprimirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimirMouseExited

    /*private ArrayList<Presentacion> buscarClasesDictadas()
     {
     return null;
     }*/
    private boolean validarSeleccion() {
        if (lstClasesDictadas.getSelectedValue() != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validarFechas() {
        if (dcFechaDesde == null) {
            Mensajes.mostrarError("Debe elegir una Fecha desde valida");
            return false;
        }
        if (dcFechaHasta == null) {
            Mensajes.mostrarError("Debe elegir una fecha hasta valida");
            return false;
        }
        return true;
    }

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        try {
            if (validarSeleccion()) {
                String institucion = ((Institucion) cmbInstitucion.getSelectedItem()).getStrNombre();
                String curso = ((Curso) cmbCurso.getSelectedItem()).getStrNombre();
                Presentacion presentacion = (Presentacion)lstClasesDictadas.getSelectedValue();
                String fechaString = new SimpleDateFormat("yyyy-MM-dd").format(presentacion.getDteFecha()); 
                GestorGenerarReportePresentacionesRealizadas.reportePresentacionRealizada(institucion, curso, fechaString, presentacion.getIntIdCurso(), presentacion.getDteFecha().getTime());
            } else {
                Mensajes.mostrarError("Debe seleccionar algun clase dictada de la ");
            }
        } catch (JRException ex) {
            Logger.getLogger(DialogAdministrarClasesDictadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void cmbInstitucionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInstitucionItemStateChanged
        cargarComboCursos(((Institucion)cmbInstitucion.getSelectedItem()).getIntInstitucionId());
    }//GEN-LAST:event_cmbInstitucionItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnVistaPrevia;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox cmbCurso;
    private javax.swing.JComboBox cmbInstitucion;
    private com.toedter.calendar.JDateChooser dcFechaDesde;
    private com.toedter.calendar.JDateChooser dcFechaHasta;
    private javax.swing.JLabel lblCurso1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblInstitucion1;
    private javax.swing.JLabel lblNombreClase;
    private javax.swing.JList lstClasesDictadas;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDatosClase;
    private javax.swing.JScrollPane scrClases;
    // End of variables declaration//GEN-END:variables
}
