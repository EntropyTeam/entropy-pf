package frontend.diseños;

import backend.auxiliares.Mensajes;
import backend.diseños.DiseñoExamen;
import backend.gestores.GestorDiseñoExamen;
import backend.reporte.GestorGenerarReporteDisenoExamen;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.IValidarSalida;
import frontend.inicio.VentanaPrincipal;
import frontend.auxiliares.PanelDeslizante;
import frontend.tomaexamenes.PanelTomaExamen;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Clase que representa al panel de edición de diseños de examen.
 *
 * @author Denise
 */
public class PanelDiseño extends javax.swing.JPanel implements IValidarSalida {

    private final VentanaPrincipal mPadre;
    private final GestorDiseñoExamen gestorDiseñoExamen;
    private final GestorBarrasDeEstado gestorEstados;
    private boolean blnEsGuardado;

    /**
     * Constructor de la clase.
     *
     * @param mPadre Ventana Principal padre
     */
    public PanelDiseño(VentanaPrincipal mPadre) {
        this.mPadre = mPadre;
        this.gestorDiseñoExamen = new GestorDiseñoExamen();
        initComponents();
        this.gestorEstados = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);
    }

    /**
     * Constructor para editar un diseño de examen.
     *
     * @param mPadre Ventana Principal padre
     * @param examenSeleccionado diseño de examen a ser editado.
     * @param duplicar boolean que indica si es para editar el examen o para
     * duplicar
     */
    public PanelDiseño(VentanaPrincipal mPadre, DiseñoExamen examenSeleccionado, boolean duplicar) {
        this.mPadre = mPadre;
        this.gestorDiseñoExamen = new GestorDiseñoExamen(examenSeleccionado);
        initComponents();
        pnlCabecera.cargarCabecera();
        this.gestorEstados = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);
        // Mantener este código al final.
        if (duplicar) {
            examenSeleccionado.setIntDiseñoExamenId(-1);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * ADVERTENCIA: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBotones = new javax.swing.JPanel();
        btnGuardarExamen = new javax.swing.JButton();
        btnTomarExamen = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnVolverInicio = new javax.swing.JButton();
        pnlSlides = new javax.swing.JPanel();
        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();

        pnlBotones.setLayout(new java.awt.GridLayout(1, 0));

        btnGuardarExamen.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnGuardarExamen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_guardar.png"))); // NOI18N
        btnGuardarExamen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnGuardarExamen.setContentAreaFilled(false);
        btnGuardarExamen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarExamen.setIconTextGap(10);
        btnGuardarExamen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardarExamenMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarExamenMouseEntered(evt);
            }
        });
        btnGuardarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarExamenActionPerformed(evt);
            }
        });
        pnlBotones.add(btnGuardarExamen);

        btnTomarExamen.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnTomarExamen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_tomar_35x35.png"))); // NOI18N
        btnTomarExamen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnTomarExamen.setContentAreaFilled(false);
        btnTomarExamen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTomarExamen.setIconTextGap(10);
        btnTomarExamen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTomarExamenMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTomarExamenMouseEntered(evt);
            }
        });
        btnTomarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTomarExamenActionPerformed(evt);
            }
        });
        pnlBotones.add(btnTomarExamen);

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

        btnVolverInicio.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnVolverInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_inicio.png"))); // NOI18N
        btnVolverInicio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVolverInicio.setContentAreaFilled(false);
        btnVolverInicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolverInicio.setIconTextGap(10);
        btnVolverInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVolverInicioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVolverInicioMouseExited(evt);
            }
        });
        btnVolverInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverInicioActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVolverInicio);

        pnlAuxSlides = new PanelDeslizante();
        pnlSlides = pnlAuxSlides;
        pnlCabecera = new PanelCabeceraExamen(this);
        pnlContenido = new PanelContenidoExamen(this);

        javax.swing.GroupLayout pnlSlidesLayout = new javax.swing.GroupLayout(pnlSlides);
        pnlSlides.setLayout(pnlSlidesLayout);
        pnlSlidesLayout.setHorizontalGroup(
            pnlSlidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlSlidesLayout.setVerticalGroup(
            pnlSlidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        pnlSlides.setLayout(new java.awt.CardLayout());
        pnlCabecera.setName("Cabecera");
        pnlContenido.setName("Contenido");
        pnlSlides.add(pnlCabecera, "card1");
        pnlSlides.add(pnlContenido, "card2");

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
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSlides, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSlides, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverInicioActionPerformed
        mPadre.getPanelDeslizante().setPanelMostrado(mPadre.getPnlInicio());
        mPadre.setTitle("Sistema de Administración de Entornos Educativos");
    }//GEN-LAST:event_btnVolverInicioActionPerformed

    private void btnGuardarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarExamenActionPerformed
        this.guardarDiseño();
    }//GEN-LAST:event_btnGuardarExamenActionPerformed

    private void btnGuardarExamenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarExamenMouseExited
        this.gestorEstados.volverAEstadoImportante();
    }//GEN-LAST:event_btnGuardarExamenMouseExited

    private void btnImprimirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirMouseExited
        this.gestorEstados.volverAEstadoImportante();
    }//GEN-LAST:event_btnImprimirMouseExited

    private void btnVolverInicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverInicioMouseExited
        this.gestorEstados.volverAEstadoImportante();
    }//GEN-LAST:event_btnVolverInicioMouseExited

    private void btnGuardarExamenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarExamenMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Guardar este diseño de examen en la base de datos.");
    }//GEN-LAST:event_btnGuardarExamenMouseEntered

    private void btnImprimirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Imprimir este diseño de examen.");
    }//GEN-LAST:event_btnImprimirMouseEntered

    private void btnVolverInicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverInicioMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Volver a la pantalla inicial.");
    }//GEN-LAST:event_btnVolverInicioMouseEntered

    private void btnTomarExamenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTomarExamenMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Tomar un examen con este diseño.");
    }//GEN-LAST:event_btnTomarExamenMouseEntered

    private void btnTomarExamenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTomarExamenMouseExited
        this.gestorEstados.volverAEstadoImportante();
    }//GEN-LAST:event_btnTomarExamenMouseExited

    private void btnTomarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTomarExamenActionPerformed
        tomarDiseño();
    }//GEN-LAST:event_btnTomarExamenActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (!this.gestorDiseñoExamen.isNuevoExamen()) {
            GestorGenerarReporteDisenoExamen gestorGenerarReporte = new GestorGenerarReporteDisenoExamen(this.gestorDiseñoExamen.getDiseñoExamen());
            try {
                File fPath = new File(gestorGenerarReporte.getPath());
                Desktop.getDesktop().open(fPath);
            } catch (IOException ex) {
                ex.printStackTrace();
                Mensajes.mostrarError(ex.getMessage().replace("Failed to open ", "Imposible abrir el archivo ").replace("Error message: ", ""));
            }
        } else {
            Mensajes.mostrarAdvertencia("Su diseño de examen esta vacío.");
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    public PanelDeslizante getPanelDeslizante() {
        return pnlAuxSlides;
    }

    public PanelCabeceraExamen getPanelCabecera() {
        return pnlCabecera;
    }

    public PanelContenidoExamen getPanelContenido() {
        return pnlContenido;
    }

    public GestorDiseñoExamen getGestorDiseñoExamen() {
        return gestorDiseñoExamen;
    }

    public VentanaPrincipal getVentanaPrincipal() {
        return mPadre;
    }

    public GestorBarrasDeEstado getGestorEstados() {
        return gestorEstados;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarExamen;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnTomarExamen;
    private javax.swing.JButton btnVolverInicio;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlEstado;
    private javax.swing.JPanel pnlSlides;
    private PanelDeslizante pnlAuxSlides;
    private PanelCabeceraExamen pnlCabecera;
    private PanelContenidoExamen pnlContenido;
    // End of variables declaration//GEN-END:variables

    /**
     * Obtiene una cadena de caracteres que describe al examen actual.
     *
     * @return descripción de examen
     */
    public String getResumenCabecera() {
        return this.pnlCabecera.getResumenExamen();
    }

    /**
     * Guarda el diseño de examen en la bd.
     */
    private boolean guardarDiseño() {
        pnlCabecera.armarCabeceraExamen();
        pnlContenido.actualizarExamen();
        if (pnlCabecera.validarDatos() && gestorDiseñoExamen.validarDatosPreguntas()) {
            gestorDiseñoExamen.guardarDiseñoExamen();
            this.gestorEstados.setNuevoEstadoImportante("¡El examen ha sido guardado exitosamente!", GestorBarrasDeEstado.TipoEstado.OK);
            this.blnEsGuardado = true;
            return true;
        } else {
            this.gestorEstados.setNuevoEstadoImportante("Problemas al guardar.", GestorBarrasDeEstado.TipoEstado.ADVERTENCIA);
            return false;
        }
    }

    @Override
    public IValidarSalida.TipoAccion validarSalida() {
        if (!blnEsGuardado) {
            int intOpcionElegida = Mensajes.mostrarOpcion("Se perderán los cambios no guardados. ¿Desea guardar antes de salir?");
            if (intOpcionElegida == JOptionPane.CANCEL_OPTION) {
                return IValidarSalida.TipoAccion.CANCELAR;
            } else if (intOpcionElegida == JOptionPane.YES_OPTION) {
                if (guardarDiseño()) {
                    return IValidarSalida.TipoAccion.CONTINUAR;
                } else {
                    return IValidarSalida.TipoAccion.CANCELAR;
                }
            }
        }
        return IValidarSalida.TipoAccion.CONTINUAR;
    }

    /**
     * Funcionalidad para empezar a tomar el diseño seleccionado.
     */
    private void tomarDiseño() {
        try {
            guardarDiseño();
            if (pnlCabecera.validarDatos() && gestorDiseñoExamen.validarDatosPreguntas()) {
                if (gestorDiseñoExamen.getColPreguntas().isEmpty()) {
                    Mensajes.mostrarError("El examen debe tener al menos una pregunta.");
                    return;
                }
                if (!gestorDiseñoExamen.controlarPuntajesCompletos(gestorDiseñoExamen.getDiseñoExamen())) {
                    Mensajes.mostrarError("Hay preguntas que no tienen un puntaje asociado.\nCargue todos los datos antes de tomar el examen.");
                    return;
                }
                PanelTomaExamen pnlTomaExamen = new PanelTomaExamen(mPadre, gestorDiseñoExamen.getDiseñoExamen());
                pnlTomaExamen.setName("Toma Examen");
                pnlTomaExamen.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de toma de examen!");
                mPadre.setTitle("Toma de examen - Paso 1 - " + gestorDiseñoExamen.getDiseñoExamen().getStrNombre());
                mPadre.getPanelDeslizante().setPanelMostrado(pnlTomaExamen);
                if (!mPadre.isMaximized()){
                    mPadre.pack();
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Mensajes.mostrarError("Debe seleccionar un diseño de examen.");
        }
    }
}
