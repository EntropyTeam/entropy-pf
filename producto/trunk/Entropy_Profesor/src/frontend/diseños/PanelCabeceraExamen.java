package frontend.diseños;

import backend.auxiliares.Mensajes;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import frontend.auxiliares.FiltroTexto;
import frontend.auxiliares.LookAndFeelEntropy;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Clase que representa al panel de cabecera de examen.
 *
 * @author Denise
 */
public class PanelCabeceraExamen extends javax.swing.JPanel {

    private final PanelDiseño mPadre;
    private byte[] bytesImagen = null;
    private Object ultimoCombo = null;

    /**
     * Constructor de la clase.
     *
     * @param mPadre
     */
    public PanelCabeceraExamen(PanelDiseño mPadre) {
        this.mPadre = mPadre;
        initComponents();

        btnBorrarImagen.setVisible(false);
        
        this.cmbInstitucion.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        this.cmbInstitucion.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyReleasedCmbInstitucion(e);
            }
        });

        this.cmbCurso.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        this.cmbCurso.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyReleasedCmbCurso(e);
            }
        });

        for (Institucion i : mPadre.getGestorDiseñoExamen().getInstitucionesPorFiltro("")) {
            cmbInstitucion.addItem(i);
        }
        cmbInstitucion.setSelectedIndex(-1);
        cmbInstitucion.getEditor().setItem(cmbInstitucion.getTextoPorDefecto());
        cmbCurso.getEditor().setItem(cmbCurso.getTextoPorDefecto());
        cmbCurso.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlEncabezado = new javax.swing.JPanel();
        pnlDatosEncabezado = new javax.swing.JPanel();
        lblNombreInstitucion = new javax.swing.JLabel();
        lblNombreExamen = new javax.swing.JLabel();
        lblIdentificadorCurso = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        txtNombreExamen = new frontend.auxiliares.TextFieldEntropy();
        scrDescripcion = new javax.swing.JScrollPane();
        txaDescripcion = new frontend.auxiliares.TextAreaEntropy();
        cmbInstitucion = new frontend.auxiliares.ComboBoxEntropy();
        cmbCurso = new frontend.auxiliares.ComboBoxEntropy();
        lblCamposObligatorios = new javax.swing.JLabel();
        pnlImagenEncabezado = new javax.swing.JPanel();
        btnBorrarImagen = new javax.swing.JButton();
        pnlImagenLogo = new frontend.auxiliares.PanelConFondo();
        lblLogo = new javax.swing.JLabel();
        lblSiguiente = new javax.swing.JLabel();

        pnlEncabezado.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos generales del examen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_REGULAR, new java.awt.Color(204, 102, 0)));

        lblNombreInstitucion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblNombreInstitucion.setText("Institución:");

        lblNombreExamen.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblNombreExamen.setText("Título (*):");

        lblIdentificadorCurso.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblIdentificadorCurso.setText("Curso:");

        lblDescripcion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblDescripcion.setText("Descripción:");

        txtNombreExamen.setTextoPorDefecto("Ingrese un título para el examen");
        txtNombreExamen.mostrarTextoPorDefecto();

        scrDescripcion.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txaDescripcion.setTextoPorDefecto("Ingrese una descripción del examen");
        txaDescripcion.mostrarTextoPorDefecto();
        txaDescripcion.setColumns(20);
        txaDescripcion.setRows(5);
        scrDescripcion.setViewportView(txaDescripcion);

        cmbInstitucion.setTextoPorDefecto("Ingrese nombre de institución");
        cmbInstitucion.mostrarTextoPorDefecto();
        cmbInstitucion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbInstitucionItemStateChanged(evt);
            }
        });

        cmbCurso.setTextoPorDefecto("Ingrese curso");
        cmbCurso.mostrarTextoPorDefecto();
        cmbCurso.setEnabled(false);

        lblCamposObligatorios.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblCamposObligatorios.setText("(*) Campos obligatorios");

        javax.swing.GroupLayout pnlDatosEncabezadoLayout = new javax.swing.GroupLayout(pnlDatosEncabezado);
        pnlDatosEncabezado.setLayout(pnlDatosEncabezadoLayout);
        pnlDatosEncabezadoLayout.setHorizontalGroup(
            pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNombreExamen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(lblIdentificadorCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreExamen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(cmbInstitucion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbCurso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlDatosEncabezadoLayout.createSequentialGroup()
                        .addComponent(lblCamposObligatorios)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlDatosEncabezadoLayout.setVerticalGroup(
            pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreExamen, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreExamen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdentificadorCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescripcion)
                    .addComponent(scrDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(lblCamposObligatorios)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        btnBorrarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_papelera25x25.png"))); // NOI18N
        btnBorrarImagen.setToolTipText("Borrar Imagen");
        btnBorrarImagen.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBorrarImagen.setContentAreaFilled(false);
        btnBorrarImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBorrarImagenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBorrarImagenMouseExited(evt);
            }
        });

        pnlImagenLogo.setPreferredSize(new java.awt.Dimension(155, 155));

        lblLogo.setBackground(new java.awt.Color(248, 246, 246));
        lblLogo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblLogo.setForeground(new java.awt.Color(102, 102, 102));
        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_camara.png"))); // NOI18N
        lblLogo.setText("Sin imagen");
        lblLogo.setToolTipText("Imagen");
        lblLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblLogo.setOpaque(true);
        lblLogo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnlImagenLogoLayout = new javax.swing.GroupLayout(pnlImagenLogo);
        pnlImagenLogo.setLayout(pnlImagenLogoLayout);
        pnlImagenLogoLayout.setHorizontalGroup(
            pnlImagenLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlImagenLogoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlImagenLogoLayout.setVerticalGroup(
            pnlImagenLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlImagenEncabezadoLayout = new javax.swing.GroupLayout(pnlImagenEncabezado);
        pnlImagenEncabezado.setLayout(pnlImagenEncabezadoLayout);
        pnlImagenEncabezadoLayout.setHorizontalGroup(
            pnlImagenEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlImagenLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrarImagen)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlImagenEncabezadoLayout.setVerticalGroup(
            pnlImagenEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlImagenEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlImagenLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrarImagen))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addComponent(pnlDatosEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlImagenEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDatosEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addComponent(pnlImagenEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSiguienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSiguienteMouseExited(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSiguiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblSiguienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSiguienteMouseClicked
        mPadre.getPanelDeslizante().setPanelMostrado(mPadre.getPanelContenido(), false);
        mPadre.getVentanaPrincipal().setTitle(this.getResumenExamen());
    }//GEN-LAST:event_lblSiguienteMouseClicked

    private void lblSiguienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSiguienteMouseEntered
        mPadre.getGestorEstados().setEstadoInstantaneo("Ir a la pantalla de edición de contenido.");
    }//GEN-LAST:event_lblSiguienteMouseEntered

    private void lblSiguienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSiguienteMouseExited
        mPadre.getGestorEstados().volverAEstadoImportante();
    }//GEN-LAST:event_lblSiguienteMouseExited

    private void btnBorrarImagenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarImagenMouseExited
        mPadre.getGestorEstados().volverAEstadoImportante();
    }//GEN-LAST:event_btnBorrarImagenMouseExited

    private void btnBorrarImagenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarImagenMouseEntered
        mPadre.getGestorEstados().setEstadoInstantaneo("Quitar la imagen actual del examen.");
    }//GEN-LAST:event_btnBorrarImagenMouseEntered

    private void cmbInstitucionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInstitucionItemStateChanged
        if (cmbInstitucion.getSelectedItem() == null || !(cmbInstitucion.getSelectedItem() instanceof Institucion)) // if (cmbInstitucion.getSelectedIndex() == -1)  no anda esa validacion (Doni)
        {
            cmbCurso.removeAllItems();
            cmbCurso.setSelectedIndex(-1);
            this.lblLogo.setVisible(true);
        } else {

            Institucion i = (Institucion) cmbInstitucion.getSelectedItem();
            byte[] bytesImagen = null;
            if (((byte[]) i.getImgLogo()) != null) {
                bytesImagen = (byte[]) i.getImgLogo();
                Image img = Toolkit.getDefaultToolkit().createImage(bytesImagen);
                this.pnlImagenLogo.setImagen(img);
                this.lblLogo.setVisible(false);
            } else {
                this.lblLogo.setVisible(true);
            }

            cmbCurso.setEnabled(true);
            cmbCurso.removeAllItems();
            for (Curso c : mPadre.getGestorDiseñoExamen().getCursosPorFiltro("", i)) {
                cmbCurso.addItem(c);
            }
            
            if (cmbCurso.getItemCount() > 0) {
                cmbCurso.setSelectedIndex(0);
            }
            //cmbCurso.getEditor().setItem(cmbCurso.getText());
            //cmbCurso.setSelectedItem(cmbCurso.getEditor().getItem());            
        }
    }//GEN-LAST:event_cmbInstitucionItemStateChanged

    private void keyReleasedCmbInstitucion(KeyEvent evt) {
        if (FiltroTexto.TipoFiltro.DIGITOS_Y_LETRAS.toString().contains(String.valueOf(evt.getKeyChar()))
                || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            cmbInstitucion.hidePopup();
            Object item = cmbInstitucion.getEditor().getItem();
            cmbInstitucion.removeAllItems();

            for (Institucion i : mPadre.getGestorDiseñoExamen().getInstitucionesPorFiltro(item.toString())) {
                cmbInstitucion.addItem(i);
            }

            cmbInstitucion.getEditor().setItem(item);
            if (cmbInstitucion.getItemCount() != 0) {
                cmbInstitucion.showPopup();
            }

            if (item.toString().isEmpty()) {
                cmbCurso.getEditor().setItem(cmbCurso.getTextoPorDefecto());
                cmbCurso.setEnabled(false);
            } else {
                cmbCurso.setEnabled(true);
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Object item = cmbInstitucion.getEditor().getItem();
            if (item.toString().isEmpty()) {
                cmbCurso.getEditor().setItem(cmbCurso.getTextoPorDefecto());
                cmbCurso.setEnabled(false);
            } else {
                cmbCurso.setEnabled(true);
            }
        }
    }

    private void keyReleasedCmbCurso(KeyEvent evt) {
        if (FiltroTexto.TipoFiltro.DIGITOS_Y_LETRAS.toString().contains(String.valueOf(evt.getKeyChar()))
                || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            cmbCurso.hidePopup();
            Object item = cmbCurso.getEditor().getItem();
            cmbCurso.removeAllItems();

            if (cmbInstitucion.getSelectedIndex() != -1) {
                for (Curso c : mPadre.getGestorDiseñoExamen().getCursosPorFiltro(item.toString(), (Institucion) cmbInstitucion.getSelectedItem())) {
                    cmbCurso.addItem(c);
                }
            }

            cmbCurso.getEditor().setItem(item);
            if (cmbCurso.getItemCount() != 0) {
                cmbCurso.showPopup();
            }
        }
    }

    public boolean validarDatos() {
        if (txtNombreExamen.getText().isEmpty()) {
            Mensajes.mostrarError("Debe ingresar un nombre para el exámen");
            return false;
        }

        return true;
    }

    public void armarCabeceraExamen() {
        mPadre.getGestorDiseñoExamen().getDiseñoExamen().setStrNombre(txtNombreExamen.getText());
        mPadre.getGestorDiseñoExamen().getDiseñoExamen().setStrDescripcion(txaDescripcion.getText());

        System.err.println("FALTA IMPLEMENTAR RESPONSABLE");

        if (cmbCurso.getSelectedIndex() != -1) {
            mPadre.getGestorDiseñoExamen().getDiseñoExamen().setCurso((Curso) cmbCurso.getSelectedItem());
        } else {
            if (cmbInstitucion.getSelectedIndex() != -1) {
                if (!cmbCurso.getEditor().getItem().toString().equals(cmbCurso.getTextoPorDefecto())
                        && !cmbCurso.getEditor().getItem().toString().isEmpty()) {
                    Curso nuevoCurso = new Curso(cmbCurso.getEditor().getItem().toString(), ((Institucion) cmbInstitucion.getSelectedItem()));
                    mPadre.getGestorDiseñoExamen().getDiseñoExamen().setCurso(nuevoCurso);
                }
            } else {
                if (!cmbCurso.getEditor().getItem().toString().equals(cmbCurso.getTextoPorDefecto())
                        && !cmbCurso.getEditor().getItem().toString().isEmpty()) {
                    Curso nuevoCurso = new Curso(cmbCurso.getEditor().getItem().toString(), new Institucion(cmbInstitucion.getEditor().getItem().toString()));
                    mPadre.getGestorDiseñoExamen().getDiseñoExamen().setCurso(nuevoCurso);
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrarImagen;
    private frontend.auxiliares.ComboBoxEntropy cmbCurso;
    private frontend.auxiliares.ComboBoxEntropy cmbInstitucion;
    private javax.swing.JLabel lblCamposObligatorios;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblIdentificadorCurso;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNombreExamen;
    private javax.swing.JLabel lblNombreInstitucion;
    private javax.swing.JLabel lblSiguiente;
    private javax.swing.JPanel pnlDatosEncabezado;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlImagenEncabezado;
    private frontend.auxiliares.PanelConFondo pnlImagenLogo;
    private javax.swing.JScrollPane scrDescripcion;
    private frontend.auxiliares.TextAreaEntropy txaDescripcion;
    private frontend.auxiliares.TextFieldEntropy txtNombreExamen;
    // End of variables declaration//GEN-END:variables

    /**
     * Devuelve una breve descripción del examen actual.
     *
     * @return
     */
    public String getResumenExamen() {
        String nombre = txtNombreExamen.getText();
        String institucion = (cmbInstitucion.getSelectedItem() != null) ? cmbInstitucion.getSelectedItem().toString() : "";
        String curso = (cmbCurso.getSelectedItem() != null) ? cmbCurso.getSelectedItem().toString() : "";
        return ((nombre.isEmpty()) ? "Nuevo examen sin título" : (nombre))
                + ((institucion.isEmpty()) ? "" : (" - " + institucion))
                + ((curso.isEmpty()) ? "" : (" - " + curso));
    }

    /*
     * Metodo para cargar los datos del diseño de examen a duplicar o editar en la cabecera
     */
    public void cargarCabecera() {
        
        scrDescripcion.setToolTipText(mPadre.getGestorDiseñoExamen().getDiseñoExamen().getStrDescripcion());
        this.txtNombreExamen.setText(mPadre.getGestorDiseñoExamen().getDiseñoExamen().getStrNombre());
        this.txaDescripcion.setText(mPadre.getGestorDiseñoExamen().getDiseñoExamen().getStrDescripcion());
        //Id Curso
        Curso curso = mPadre.getGestorDiseñoExamen().buscarCurso(mPadre.getGestorDiseñoExamen().getDiseñoExamen().getIntDiseñoExamenId());
        //Id institucion
        Institucion institucion = null;
        if (curso != null) institucion = mPadre.getGestorDiseñoExamen().buscarInstitucion(curso.getIntCursoId());
        if (institucion != null) {
            if ((institucion.getImgLogo()) != null) {
                bytesImagen = (byte[]) institucion.getImgLogo();
                Image img = Toolkit.getDefaultToolkit().createImage(bytesImagen);
                this.pnlImagenLogo.setImagen(img);
                this.lblLogo.setVisible(false);
            } else {
                this.lblLogo.setVisible(true);
            }
            this.cmbInstitucion.setSelectedItem(institucion);
            if (curso != null) {
                this.cmbCurso.setEnabled(true);
                this.cmbCurso.setSelectedItem(curso);
                mPadre.getGestorDiseñoExamen().getDiseñoExamen().setCurso(curso);
                mPadre.getGestorDiseñoExamen().getDiseñoExamen().getCurso().setInstitucion(institucion);
                mPadre.getGestorDiseñoExamen().getDiseñoExamen().setCurso(curso);
                mPadre.getGestorDiseñoExamen().getDiseñoExamen().getCurso().setInstitucion(institucion);
            } else {
                this.cmbCurso.setSelectedIndex(0);
            }

        } else {
            this.cmbInstitucion.setSelectedIndex(-1);
            this.cmbCurso.setSelectedIndex(-1);
        }

    }
}
