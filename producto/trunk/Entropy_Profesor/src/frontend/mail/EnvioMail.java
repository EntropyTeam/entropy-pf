package frontend.mail;

import backend.auxiliares.Mensajes;
import backend.mail.Email;
import backend.mail.GestorEnvioDeMail;
import backend.usuarios.Alumno;
import java.util.ArrayList;

/**
 *
 * @author Jose Ruiz
 */
public class EnvioMail extends javax.swing.JFrame {

    private ArrayList<Alumno> alumnos = null;
    private Alumno alumno = null;
    private ArrayList<byte[]> pdfs = null;
    private byte[] pdf = null;

    /**
     * Creates new form EnvioMail
     */
    public EnvioMail(Alumno alumno, byte[] pdf) {
        this.alumno = alumno;
        this.pdf = pdf;
        initComponents();
        this.setLocationRelativeTo(this);
        recuperarDatosGenericosDelMail();
    }

    public EnvioMail(ArrayList<Alumno> alumnos, ArrayList<byte[]> pdfs) {
        this.alumnos = alumnos;
        this.pdfs = pdfs;
        initComponents();
        recuperarDatosGenericosDelMailMultiples();
    }

    private void recuperarDatosGenericosDelMail() {
        txtPara.setText(this.alumno.getStrEmail());
        lblAdjunto.setText(alumno.getStrApellido() + ", " + alumno.getStrNombre() + " - " + alumno.getStrTipoDocumento() + " " + alumno.getIntNroDocumento() + ".pdf");
    }

    private void recuperarDatosGenericosDelMailMultiples() {
        String todasLasDirecciones = concatenarTodosLasDireccionesDeMail(recuperarTodasLasDireccionesDeCorreo(this.alumnos));
        txtPara.setText(todasLasDirecciones);
        lblAdjunto.setText("examen.pdf");
    }

    private void enviarMailUnicoDestinatario(Alumno alumno, byte[] pdf) {
        GestorEnvioDeMail gestorEnvioDeMail = new GestorEnvioDeMail();
        Email nuevoMail = new Email();
        byte[] bytes = pdf;
        nuevoMail.setTo(alumno.getStrEmail());
        nuevoMail.setSubject(txtAsunto.getText());
        nuevoMail.setMessage(txtCuerpo.getText());
        nuevoMail.setAdjunto(alumno.getStrApellido() + ", " + alumno.getStrNombre() + " - " + alumno.getStrTipoDocumento() + " " + alumno.getIntNroDocumento(), bytes);
        gestorEnvioDeMail.enviarMail(nuevoMail);
    }

    private void enviarMailMultiplesDestinatarios(ArrayList<Alumno> alumnos, ArrayList<byte[]> pdfs) {
        GestorEnvioDeMail gestorEnvioDeMail = new GestorEnvioDeMail();
        for (Alumno alumno : alumnos) {
            Email nuevoMail = new Email();
            nuevoMail.setTo(alumno.getStrEmail());
            nuevoMail.setSubject(txtAsunto.getText());
            nuevoMail.setMessage(txtCuerpo.getText());
            for (byte[] pdf : pdfs) {
                byte[] bytes = pdf;
                nuevoMail.setAdjunto(alumno.getStrApellido() + ", " + alumno.getStrNombre() + " - " + alumno.getStrTipoDocumento() + " " + alumno.getIntNroDocumento(), bytes);
                gestorEnvioDeMail.enviarMail(nuevoMail);
            }
        }
    }

    private ArrayList<String> recuperarTodasLasDireccionesDeCorreo(ArrayList<Alumno> alumnos) {
        ArrayList<String> direccionesDeCorreo = new ArrayList<String>();
        for (Alumno alumno : alumnos) {
            direccionesDeCorreo.add(alumno.getStrEmail());
        }
        return direccionesDeCorreo;
    }

    private String concatenarTodosLasDireccionesDeMail(ArrayList<String> direccionesMail) {
        String todasLasDirecciones = "";
        for (String mail : direccionesMail) {
            todasLasDirecciones = todasLasDirecciones + mail + ",";
        }
        return todasLasDirecciones;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBarraTitulo = new javax.swing.JLabel();
        lblBarraTitulo1 = new javax.swing.JLabel();
        lblBarraTitulo2 = new javax.swing.JLabel();
        txtAsunto = new frontend.auxiliares.TextFieldEntropy();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCuerpo = new javax.swing.JTextArea();
        lblBarraTitulo3 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnEnviar = new javax.swing.JButton();
        lblBarraTitulo4 = new javax.swing.JLabel();
        lblAdjunto = new javax.swing.JLabel();
        txtPara = new frontend.auxiliares.TextFieldEntropy();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblBarraTitulo.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        lblBarraTitulo.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo.setText("ENVIO DE MAIL");

        lblBarraTitulo1.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo1.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo1.setText("Para");

        lblBarraTitulo2.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo2.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo2.setText("Asunto");

        txtPara.setTextoPorDefecto("Filtrar por nombre");
        txtPara.mostrarTextoPorDefecto();

        txtCuerpo.setColumns(20);
        txtCuerpo.setRows(5);
        jScrollPane1.setViewportView(txtCuerpo);

        lblBarraTitulo3.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo3.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo3.setText("Cuerpo");

        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("");
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.setToolTipText("");
        btnEnviar.setContentAreaFilled(false);
        btnEnviar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        lblBarraTitulo4.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo4.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo4.setText("Adjunto");

        lblAdjunto.setText("archivo.pdf");

        txtPara.setTextoPorDefecto("Filtrar por nombre");
        txtPara.mostrarTextoPorDefecto();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblBarraTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPara, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblBarraTitulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblBarraTitulo3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblBarraTitulo4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(lblAdjunto, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(215, 215, 215))
                                                .addComponent(txtAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(51, Short.MAX_VALUE))
                .addComponent(lblBarraTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblBarraTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblBarraTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPara, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblBarraTitulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAdjunto)
                                .addComponent(lblBarraTitulo4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblBarraTitulo3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );

        lblBarraTitulo.getAccessibleContext().setAccessibleName("ENVIO DE MAIL");

        pack();
    }// </editor-fold>

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        alumno.setStrEmail(this.txtPara.getText());
        try {
            if (alumnos == null) {
                enviarMailUnicoDestinatario(alumno, pdf);
                Mensajes.mostrarConfirmacion("El Email fue enviado correctamente");
                this.dispose();
            } else {
                enviarMailMultiplesDestinatarios(this.alumnos, this.pdfs);
                Mensajes.mostrarConfirmacion("Los Emails fueron enviados correctamente");
                this.dispose();
            }
        } catch (Exception e) {
            Mensajes.mostrarError("Imposible enviar el Email");
            System.err.printf(e.toString());
        }
    }

    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAdjunto;
    private javax.swing.JLabel lblBarraTitulo;
    private javax.swing.JLabel lblBarraTitulo1;
    private javax.swing.JLabel lblBarraTitulo2;
    private javax.swing.JLabel lblBarraTitulo3;
    private javax.swing.JLabel lblBarraTitulo4;
    private frontend.auxiliares.TextFieldEntropy txtAsunto;
    private javax.swing.JTextArea txtCuerpo;
    private frontend.auxiliares.TextFieldEntropy txtPara;
}
