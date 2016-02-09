package frontend.diseños;

import backend.diseños.Pregunta;
import frontend.auxiliares.GestorImagenes;
import frontend.inicio.VentanaPrincipal;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DialogImportarAdjuntos extends javax.swing.JDialog {

    private final PanelContenidoExamen mPadre;
    private byte[] bytesImagen = null;

    /**
     * Creates new form DialogImportarAdjuntos
     * @param parent
     * @param modal
     * @param mPadre
     */
    public DialogImportarAdjuntos(java.awt.Frame parent, boolean modal, PanelContenidoExamen mPadre) {
        super(parent, modal);
        this.mPadre = mPadre;
        this.setLocationRelativeTo(VentanaPrincipal.getInstancia());
        initComponents();
        validarImagen();
    }

    private void validarImagen() {
        Pregunta pregunta = mPadre.getPreguntaSeleccionada();
        if (pregunta.getColAdjuntos() !=null && pregunta.getColAdjuntos().size()>0) {
                this.bytesImagen =  (byte[]) pregunta.getColAdjuntos().get(0);
                Image img = Toolkit.getDefaultToolkit().createImage(bytesImagen);
                img.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                this.lblImagenMuestra.setVisible(false);
                this.pnlmagen.setImagen(img); 
        }
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

    private void guardarAdjuntoEnPregunta(Object imagen) {
        Pregunta pregunta = mPadre.getPreguntaSeleccionada();
        ArrayList<Object> adjuntos = new ArrayList<>();
        adjuntos.add(imagen);
        pregunta.setColAdjuntos(adjuntos);
        this.mPadre.getRutaAdjunto().setText("Se ha cargado una imagen.");
    }
    
    private void removerAdjunto(){
        mPadre.getPreguntaSeleccionada().getColAdjuntos().clear();
        this.mPadre.getRutaAdjunto().setText("Seleccione un archivo a cargar...");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBorrarImagen = new javax.swing.JButton();
        btnCargaImagen = new javax.swing.JButton();
        pnlmagen = new frontend.auxiliares.PanelConFondo();
        lblImagenMuestra = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        btnVolverDiseno = new javax.swing.JButton();
        btnAgregarImagen = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Importar adjuntos...");
        setResizable(false);

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

        pnlmagen.setPreferredSize(new java.awt.Dimension(155, 155));

        lblImagenMuestra.setBackground(new java.awt.Color(248, 246, 246));
        lblImagenMuestra.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblImagenMuestra.setForeground(new java.awt.Color(102, 102, 102));
        lblImagenMuestra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagenMuestra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_camara.png"))); // NOI18N
        lblImagenMuestra.setText("Vista previa");
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
                .addComponent(lblImagenMuestra, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
        pnlmagenLayout.setVerticalGroup(
            pnlmagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
            .addGroup(pnlmagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblImagenMuestra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );

        pnlBotones.setLayout(new java.awt.GridLayout(1, 0));

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
        pnlBotones.add(btnVolverDiseno);

        btnAgregarImagen.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnAgregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_guardar.png"))); // NOI18N
        btnAgregarImagen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnAgregarImagen.setContentAreaFilled(false);
        btnAgregarImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarImagen.setIconTextGap(10);
        btnAgregarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarImagenActionPerformed(evt);
            }
        });
        pnlBotones.add(btnAgregarImagen);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlmagen, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBorrarImagen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCargaImagen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCargaImagen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrarImagen))
                    .addComponent(pnlmagen, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(273, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarImagenActionPerformed
        if (bytesImagen != null) {
            Object imagen = bytesImagen;
            guardarAdjuntoEnPregunta(imagen);
            this.dispose();
        } else {
            removerAdjunto();
            this.dispose();
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
            } else {
                System.out.println("OCurrio un error al querer cargar la foto");
            }

        } else {
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
    private javax.swing.JLabel lblImagenMuestra;
    private javax.swing.JPanel pnlBotones;
    private frontend.auxiliares.PanelConFondo pnlmagen;
    // End of variables declaration//GEN-END:variables
}
