/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.diseños;

import backend.auxiliares.Mensajes;
import backend.dao.diseños.DAOCurso;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.diseños.Pregunta;
import backend.gestores.GestorCursosEInstituciones;
import frontend.auxiliares.CeldaListaRendererEntropy;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.GestorImagenes;
import frontend.inicio.VentanaPrincipal;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DialogImportarAdjuntos extends javax.swing.JDialog {

    private final PanelContenidoExamen mPadre;
    private boolean blnEdicion = false;
    private boolean blnEdicionCurso = false;
    private byte[] bytesImagen = null;

    /**
     * Creates new form DialogImportarAdjuntos
     */
    public DialogImportarAdjuntos(java.awt.Frame parent, boolean modal, PanelContenidoExamen mPadre ) {
        super(parent, modal);
        this.mPadre=mPadre;
        initComponents();
    }

    private byte[] guardarParametrosDeImagen(String ruta) {
        File fila = new File(ruta);
        byte[] bytes = null;
        FileInputStream entrada;
        try {
            entrada = new FileInputStream(fila);
            bytes = new byte[(int) fila.length()];
            entrada.read(bytes);
        } catch (IOException ex) {
            Logger.getLogger(DialogImportarAdjuntos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes;
    }
    
    private void guardarAdjuntoEnPregunta()
    {
        Pregunta pregunta = mPadre.getPreguntaSeleccionada();
        pregunta.setColAdjuntos(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBorrarImagen = new javax.swing.JButton();
        btnCargaImagen = new javax.swing.JButton();
        btnAgregarImagen = new javax.swing.JButton();
        btnVolverDiseno = new javax.swing.JButton();
        lblEstadoInstitucion = new javax.swing.JLabel();
        pnlmagen = new frontend.auxiliares.PanelConFondo();
        lblImagenMuestra = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnBorrarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_papelera25x25.png"))); // NOI18N
        btnBorrarImagen.setToolTipText("");
        btnBorrarImagen.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBorrarImagen.setContentAreaFilled(false);
        btnBorrarImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarImagenActionPerformed(evt);
            }
        });

        btnCargaImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_actualizar25x25.png"))); // NOI18N
        btnCargaImagen.setToolTipText("");
        btnCargaImagen.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCargaImagen.setContentAreaFilled(false);
        btnCargaImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargaImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargaImagenActionPerformed(evt);
            }
        });

        btnAgregarImagen.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnAgregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_guardar.png"))); // NOI18N
        btnAgregarImagen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnAgregarImagen.setContentAreaFilled(false);
        btnAgregarImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarImagen.setIconTextGap(10);
        btnAgregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregarImagenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregarImagenMouseExited(evt);
            }
        });
        btnAgregarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarImagenActionPerformed(evt);
            }
        });

        btnVolverDiseno.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnVolverDiseno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_volver.png"))); // NOI18N
        btnVolverDiseno.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVolverDiseno.setContentAreaFilled(false);
        btnVolverDiseno.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolverDiseno.setIconTextGap(10);
        btnVolverDiseno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverDisenoActionPerformed(evt);
            }
        });

        lblEstadoInstitucion.setFont(new java.awt.Font("Calibri", 2, 12)); // NOI18N
        lblEstadoInstitucion.setForeground(new java.awt.Color(102, 102, 102));
        lblEstadoInstitucion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstadoInstitucion.setText("Acá se escriben estados...");
        lblEstadoInstitucion.setAlignmentX(0.5F);
        lblEstadoInstitucion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        pnlmagen.setPreferredSize(new java.awt.Dimension(155, 155));

        lblImagenMuestra.setBackground(new java.awt.Color(248, 246, 246));
        lblImagenMuestra.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblImagenMuestra.setForeground(new java.awt.Color(102, 102, 102));
        lblImagenMuestra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagenMuestra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_camara.png"))); // NOI18N
        lblImagenMuestra.setText("Sin imagen");
        lblImagenMuestra.setToolTipText("Imagen");
        lblImagenMuestra.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblImagenMuestra.setOpaque(true);
        lblImagenMuestra.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnlmagenLayout = new javax.swing.GroupLayout(pnlmagen);
        pnlmagen.setLayout(pnlmagenLayout);
        pnlmagenLayout.setHorizontalGroup(
            pnlmagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
            .addGroup(pnlmagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlmagenLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblImagenMuestra, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlmagenLayout.setVerticalGroup(
            pnlmagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
            .addGroup(pnlmagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlmagenLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblImagenMuestra, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(btnVolverDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(btnAgregarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(115, 115, 115)
                                .addComponent(pnlmagen, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBorrarImagen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCargaImagen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 58, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblEstadoInstitucion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(16, Short.MAX_VALUE)
                        .addComponent(pnlmagen, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnCargaImagen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBorrarImagen)
                        .addGap(31, 31, 31)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVolverDiseno, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(lblEstadoInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarImagenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarImagenMouseEntered
    }//GEN-LAST:event_btnAgregarImagenMouseEntered

    private void btnAgregarImagenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarImagenMouseExited

    }//GEN-LAST:event_btnAgregarImagenMouseExited

    private void btnAgregarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarImagenActionPerformed
        if (bytesImagen != null) {
            Object imagen = bytesImagen;
        } else {
            System.err.print("No se ha cargado ninguna imagen, si no desea cargar ninguna imagen oprima el boton regresar");
        }

    }//GEN-LAST:event_btnAgregarImagenActionPerformed

    private void btnCargaImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargaImagenActionPerformed
        JFileChooser flcChooser = new JFileChooser();
        flcChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        flcChooser.setDialogTitle("Cargar imagen");
        flcChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        flcChooser.setMultiSelectionEnabled(false);
        flcChooser.setAcceptAllFileFilterUsed(false);
        int intEstado = flcChooser.showOpenDialog(this);
        if (intEstado == JFileChooser.APPROVE_OPTION) {
            File archivoElegido = flcChooser.getSelectedFile();
            String strRutaAbsoluta = archivoElegido.getPath();
            if (archivoElegido.exists()) {
                BufferedImage imgSeleccionada = GestorImagenes.redimensionarImagen(strRutaAbsoluta, 250, 250);
                this.pnlmagen.setImagen(imgSeleccionada);
                this.lblImagenMuestra.setVisible(false);
                bytesImagen = guardarParametrosDeImagen(strRutaAbsoluta);
            }
            else
            {
                 System.out.println("OCurrio un error al querer cargar la foto");
            }
            
        }
        else
        {
            System.out.println("OCurrio un error al querer cargar la foto");
        }
    }//GEN-LAST:event_btnCargaImagenActionPerformed

    private void btnBorrarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarImagenActionPerformed
        this.lblImagenMuestra.setVisible(true);
        this.bytesImagen = null;
    }//GEN-LAST:event_btnBorrarImagenActionPerformed

    private void btnVolverDisenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverDisenoActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverDisenoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarImagen;
    private javax.swing.JButton btnBorrarImagen;
    private javax.swing.JButton btnCargaImagen;
    private javax.swing.JButton btnVolverDiseno;
    private javax.swing.JLabel lblEstadoInstitucion;
    private javax.swing.JLabel lblImagenMuestra;
    private frontend.auxiliares.PanelConFondo pnlmagen;
    // End of variables declaration//GEN-END:variables
}
