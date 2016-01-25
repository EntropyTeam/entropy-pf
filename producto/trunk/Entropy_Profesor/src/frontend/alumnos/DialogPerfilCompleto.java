package frontend.alumnos;

import backend.usuarios.Alumno;
import frontend.auxiliares.ComponentMover;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.auxiliares.PanelConFondo;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * Clase que representa la interfaz para mostrar demás datos asociados al alumno
 * que realiza el examen.
 *
 * @author Denise
 */
public class DialogPerfilCompleto extends javax.swing.JDialog {


    /**
     * Constructor de la clase.
     *
     * @param padre ventana principal de la aplicación.
     * @param modal true si mantiene el foco, false de lo contrario.
     * @param alumno
     */
    public DialogPerfilCompleto(JFrame padre, boolean modal, Alumno alumno) {
        super(padre, modal);
        initComponents();
        this.setLocationRelativeTo(padre);
        this.getRootPane().registerKeyboardAction(new EscapeAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Fondo translúcido.
        this.pnlCentral.setBackground(LookAndFeelEntropy.COLOR_BLANCO_TRANSLUCIDO);

        //Para que el undecorated dialog pueda moverse.
        ComponentMover cm = new ComponentMover(JDialog.class, pnlImagen);
        
        this.cargarDatosUsuario(alumno);
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
        pnlCentral = new javax.swing.JPanel();
        pnlImagen = new javax.swing.JPanel();
        lblApellido = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        lblsDocumento = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        lblsLegajo = new javax.swing.JLabel();
        lblLegajo = new javax.swing.JLabel();
        lblsEmail = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        //pnlFondo.setImagen(GestorImagenes.crearImage("/frontend/imagenes/bg2.jpg"));
        pnlFondoAux = new PanelGradiente(new Color(235, 204, 114));
        pnlFondo = pnlFondoAux;
        pnlFondo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 0), 1, true));

        pnlImagen.setBackground(new java.awt.Color(250, 225, 175));
        pnlImagen.setBorder(LookAndFeelEntropy.BORDE_NARANJA);
        pnlImagen.setMaximumSize(new java.awt.Dimension(204, 32767));

        lblApellido.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(255, 102, 0));
        lblApellido.setText("Denise Giusto");

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_mensajes_cerrar_opcion.png"))); // NOI18N
        btnCerrar.setBorder(null);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        lblsDocumento.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblsDocumento.setText("Documento:");

        lblDocumento.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblDocumento.setText("DNI 35580949");

        lblsLegajo.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblsLegajo.setText("Legajo:");

        lblLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblLegajo.setText("55192");

        lblsEmail.setFont(LookAndFeelEntropy.FUENTE_CURSIVA);
        lblsEmail.setText("E-mail:");

        lblEmail.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblEmail.setText("dg@gmail.com");

        javax.swing.GroupLayout pnlImagenLayout = new javax.swing.GroupLayout(pnlImagen);
        pnlImagen.setLayout(pnlImagenLayout);
        pnlImagenLayout.setHorizontalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlImagenLayout.createSequentialGroup()
                        .addComponent(lblApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCerrar))
                    .addGroup(pnlImagenLayout.createSequentialGroup()
                        .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblsDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsLegajo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblLegajo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(lblDocumento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlImagenLayout.setVerticalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblApellido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblsDocumento)
                    .addComponent(lblDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsLegajo)
                    .addComponent(lblLegajo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblsEmail)
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCentralLayout = new javax.swing.GroupLayout(pnlCentral);
        pnlCentral.setLayout(pnlCentralLayout);
        pnlCentralLayout.setHorizontalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlCentralLayout.setVerticalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblLegajo;
    private javax.swing.JLabel lblsDocumento;
    private javax.swing.JLabel lblsEmail;
    private javax.swing.JLabel lblsLegajo;
    private javax.swing.JPanel pnlCentral;
    private frontend.auxiliares.PanelConFondo pnlFondo;
    private PanelGradiente pnlFondoAux;
    private javax.swing.JPanel pnlImagen;
    // End of variables declaration//GEN-END:variables

    private void cargarDatosUsuario(Alumno alumno) {
        this.lblApellido.setText(alumno.getStrNombre() + " " + alumno.getStrApellido());
        this.lblEmail.setText((alumno.getStrEmail() != null && !alumno.getStrEmail().isEmpty()) ? alumno.getStrEmail() : "---");
        this.lblLegajo.setText((alumno.getStrLegajo() != null && !alumno.getStrLegajo().isEmpty()) ? alumno.getStrLegajo() : "---");
        if (alumno.getIntNroDocumento() != -1) {
            lblDocumento.setText(alumno.getStrTipoDocumento() + " " + alumno.getIntNroDocumento());
        }
    }

    /**
     * Clase que escucha por el tecleo de la tecla Esc.
     */
    private class EscapeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    /**
     * Representa un panel con fondo de gradiente.
     */
    private class PanelGradiente extends PanelConFondo {

        private Color color;

        /**
         * Constructor de la clase.
         *
         * @param color el gradiente comenzará en blanco hasta el color pasado
         * por parámetro, desde arriba hacia abajo.
         */
        public PanelGradiente(Color color) {
            this.color = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int h = getHeight();
            int w = getWidth();

            GradientPaint gradientPaint = new GradientPaint(0, 0, Color.WHITE, 0, h, color);

            Graphics2D g2D = (Graphics2D) g;
            g2D.setPaint(gradientPaint);
            g2D.fillRect(0, 0, w, h);
            repaint();
        }

        /**
         * Modifica el color de gradiente del panel.
         *
         * @param color nuevo color.
         */
        public void setColor(Color color) {
            this.color = color;
            this.repaint();
        }

    }
}
