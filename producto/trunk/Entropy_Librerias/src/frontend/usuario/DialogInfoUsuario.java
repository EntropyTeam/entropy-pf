package frontend.usuario;

import backend.usuarios.Usuario;
import frontend.auxiliares.ComponentMover;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.auxiliares.PanelConFondo;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
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
public class DialogInfoUsuario extends javax.swing.JDialog {


    /**
     * Constructor de la clase.
     *
     * @param padre ventana principal de la aplicación.
     * @param modal true si mantiene el foco, false de lo contrario.
     * @param usuario
     */
    public DialogInfoUsuario(JFrame padre, boolean modal, Usuario usuario) {
        super(padre, modal);
        initComponents();
        this.setLocationRelativeTo(padre);
        this.getRootPane().registerKeyboardAction(new EscapeAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Fondo translúcido.
        this.pnlCentral.setBackground(LookAndFeelEntropy.COLOR_BLANCO_TRANSLUCIDO);
        this.scrDescripcion.getViewport().setOpaque(false);
        this.scrClavePublica.getViewport().setOpaque(false);
        this.scrDescripcion.setOpaque(false);
        this.scrClavePublica.setOpaque(false);
        this.txaDescripcion.setOpaque(false);
        this.txaClavePublica.setOpaque(false);

        //Para que el undecorated dialog pueda moverse.
        ComponentMover cm = new ComponentMover(JDialog.class, pnlImagen);
        
        this.cargarDatosUsuario(usuario);
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
        pnlImagenLogo = new frontend.auxiliares.PanelConFondo();
        lblLogo = new javax.swing.JLabel();
        lblApellido = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        lblsDocumento = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        lblsLegajo = new javax.swing.JLabel();
        lblLegajo = new javax.swing.JLabel();
        lblsEmail = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        PanelDatosAlumno = new javax.swing.JPanel();
        scrDescripcion = new javax.swing.JScrollPane();
        txaDescripcion = new frontend.auxiliares.TextAreaEntropy();
        scrClavePublica = new javax.swing.JScrollPane();
        txaClavePublica = new frontend.auxiliares.TextAreaEntropy();
        pnlDatosHost = new javax.swing.JPanel();
        lblsIP = new javax.swing.JLabel();
        lblIP = new javax.swing.JLabel();
        lblsCodigo = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();

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

        pnlImagenLogo.setPreferredSize(new java.awt.Dimension(155, 155));

        lblLogo.setBackground(new java.awt.Color(248, 246, 246));
        lblLogo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
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

        lblsDocumento.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsDocumento.setText("Documento:");

        lblDocumento.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblDocumento.setText("DNI 35580949");

        lblsLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsLegajo.setText("Legajo:");

        lblLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblLegajo.setText("55192");

        lblsEmail.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsEmail.setText("E-mail:");

        lblEmail.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblEmail.setText("dg@gmail.com");

        javax.swing.GroupLayout pnlImagenLayout = new javax.swing.GroupLayout(pnlImagen);
        pnlImagen.setLayout(pnlImagenLayout);
        pnlImagenLayout.setHorizontalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlImagenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlImagenLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlImagenLayout.createSequentialGroup()
                        .addComponent(lblApellido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGroup(pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlImagenLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlImagenLayout.createSequentialGroup()
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
                            .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelDatosAlumno.setOpaque(false);

        scrDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descripción del perfil", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_CURSIVA, new java.awt.Color(204, 102, 0)));
        scrDescripcion.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrDescripcion.setToolTipText("");

        txaDescripcion.setEditable(false);
        txaDescripcion.setColumns(20);
        txaDescripcion.setRows(5);
        txaDescripcion.setText("mQGiBDc63i0RBAD9MWBVMGw2+NvyQQHFiF1sN7efjisFngFskggY9AfIdnQuLfIq\nsiQITwgeRSTKCb6PyQPk3CqLImU3xqXbuDIoLOlf/Xcmf2twcKG07kSThTWX0WyA\nr81yMqgsE+4W7lj/AGPIUltjcJublwpPYVBG6RAC5nAPXICr1PjijDtAXQCg/wBL\n7HTuBtOJ7qXeYIl4Ozfmsq0EANSdKecm6dqnOYA11rTBk/VAgyLYh3heDboj0WAb\nTIpSf2Pjk7gIVBYutIzIocZ51j0YTG2/Sjs2gwxfRNz2zJt0COin6HrrUIAmGAE+\nKVrRP1NZdSmWZCFSNkB/GzXfooe3rhRAhRgdyTV2qYeKpZMbc8txScqSpbfDoW+T\nmH0vA/9/UvEfv6SClSjBa3/9Dq1QAqJsYEulqF1QzhmvI10fCKvtkzxTuj5vwlXC\no2G0iQoZG0dLOwazNTokW1Oo1VGmSxUb+zIp1LzNUSAzHEqI4iMOS4QakYgkeNH7\nLgwQrlLnmdnL2kRZvKPjrab1nAEyFka1fVGYu8w59mymteSF+rQuQ2FybG9zIFNl\ncnJhbm8gQ2luY2EgPHNlcnJhbm9AcG9zdGEudW5pemFyLmVzPokASwQQEQIACwUC\nNzreLQQLAwIBAAoJEFlyxNs7kDT0la4Ani3+z1B3su+uuVnYRjknWsqkMOmfAJ9Y\nNXuiASG3ABhOKHAcqE29rGB2H7kCDQQ3Ot4uEAgA9kJXtwh/CBdyorrWqULzBej5\nUxE5T7bxbrlLOCDaAadWoxTpj0BV89AHxstDqZSt90xkhkn4DIO9ZekX1KHTUPj1\nWV/cdlJPPT2N286Z4VeSWc39uK50T8X8dryDxUcwYc58yWb/Ffm7/ZFexwGq01ue\njaClcjrUGvC/RgBYK+X0iP1YTknbzSC0neSRBzZrM2w4DUUdD3yIsxx8Wy2O9vPJ\nI8BD8KVbGI2Ou1WMuF040zT9fBdXQ6MdGGzeMyEstSr/POGxKUAYEY18hKcKctaG\nxAMZyAcpesqVDNmWn6vQClCbAkbTCD1mpF1Bn5x8vYlLIhkmuquiXsNV6TILOwAC\nAgf8D5yY0PJdoNcZ72DhRsfqz/EMGh6QWZRVd0lreAG1gdNL5oRkN3RSP1cSmiU6\nRDrbBkfbPqKLIvhMZR8kzUSOIAcEhEaW1m4zanTK7iQwYifoWVnWAgKXIg9wzVMv\nZks37w/ekbaPWV8k9q1zmNezlscdHLF3WswEuhdKOnkdnuDmIb1hZkLhbXgspCJD\nC6jZhJ7lJRF7Am56VKHNV1ArIENVdngiNDEYCQ4PgxzX2RL3PX/J5AqXp/hyJ9mi\n1gOZjI91ZjCf70IY/khpKH5jd7cpFsfl1meNJnjbr3wSa+Vkx0Pv0MmoUZB1uvVo\nRdsV9RiKOIn2YIov8sVjqXxrIokARgQYEQIABgUCNzreLgAKCRBZcsTbO5A09Aql\nAKDtecIL1V+v8N5Av+c2EavWlJld4QCg704enpDj31SSO3hTKnl47dUQX8Q=\n=YQDR");
        scrDescripcion.setViewportView(txaDescripcion);

        scrClavePublica.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clave pública", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_CURSIVA, new java.awt.Color(204, 102, 0)));
        scrClavePublica.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrClavePublica.setToolTipText("");

        txaClavePublica.setEditable(false);
        txaClavePublica.setColumns(20);
        txaClavePublica.setRows(5);
        txaClavePublica.setText("mQGiBDc63i0RBAD9MWBVMGw2+NvyQQHFiF1sN7efjisFngFskggY9AfIdnQuLfIq\nsiQITwgeRSTKCb6PyQPk3CqLImU3xqXbuDIoLOlf/Xcmf2twcKG07kSThTWX0WyA\nr81yMqgsE+4W7lj/AGPIUltjcJublwpPYVBG6RAC5nAPXICr1PjijDtAXQCg/wBL\n7HTuBtOJ7qXeYIl4Ozfmsq0EANSdKecm6dqnOYA11rTBk/VAgyLYh3heDboj0WAb\nTIpSf2Pjk7gIVBYutIzIocZ51j0YTG2/Sjs2gwxfRNz2zJt0COin6HrrUIAmGAE+\nKVrRP1NZdSmWZCFSNkB/GzXfooe3rhRAhRgdyTV2qYeKpZMbc8txScqSpbfDoW+T\nmH0vA/9/UvEfv6SClSjBa3/9Dq1QAqJsYEulqF1QzhmvI10fCKvtkzxTuj5vwlXC\no2G0iQoZG0dLOwazNTokW1Oo1VGmSxUb+zIp1LzNUSAzHEqI4iMOS4QakYgkeNH7\nLgwQrlLnmdnL2kRZvKPjrab1nAEyFka1fVGYu8w59mymteSF+rQuQ2FybG9zIFNl\ncnJhbm8gQ2luY2EgPHNlcnJhbm9AcG9zdGEudW5pemFyLmVzPokASwQQEQIACwUC\nNzreLQQLAwIBAAoJEFlyxNs7kDT0la4Ani3+z1B3su+uuVnYRjknWsqkMOmfAJ9Y\nNXuiASG3ABhOKHAcqE29rGB2H7kCDQQ3Ot4uEAgA9kJXtwh/CBdyorrWqULzBej5\nUxE5T7bxbrlLOCDaAadWoxTpj0BV89AHxstDqZSt90xkhkn4DIO9ZekX1KHTUPj1\nWV/cdlJPPT2N286Z4VeSWc39uK50T8X8dryDxUcwYc58yWb/Ffm7/ZFexwGq01ue\njaClcjrUGvC/RgBYK+X0iP1YTknbzSC0neSRBzZrM2w4DUUdD3yIsxx8Wy2O9vPJ\nI8BD8KVbGI2Ou1WMuF040zT9fBdXQ6MdGGzeMyEstSr/POGxKUAYEY18hKcKctaG\nxAMZyAcpesqVDNmWn6vQClCbAkbTCD1mpF1Bn5x8vYlLIhkmuquiXsNV6TILOwAC\nAgf8D5yY0PJdoNcZ72DhRsfqz/EMGh6QWZRVd0lreAG1gdNL5oRkN3RSP1cSmiU6\nRDrbBkfbPqKLIvhMZR8kzUSOIAcEhEaW1m4zanTK7iQwYifoWVnWAgKXIg9wzVMv\nZks37w/ekbaPWV8k9q1zmNezlscdHLF3WswEuhdKOnkdnuDmIb1hZkLhbXgspCJD\nC6jZhJ7lJRF7Am56VKHNV1ArIENVdngiNDEYCQ4PgxzX2RL3PX/J5AqXp/hyJ9mi\n1gOZjI91ZjCf70IY/khpKH5jd7cpFsfl1meNJnjbr3wSa+Vkx0Pv0MmoUZB1uvVo\nRdsV9RiKOIn2YIov8sVjqXxrIokARgQYEQIABgUCNzreLgAKCRBZcsTbO5A09Aql\nAKDtecIL1V+v8N5Av+c2EavWlJld4QCg704enpDj31SSO3hTKnl47dUQX8Q=\n=YQDR");
        scrClavePublica.setViewportView(txaClavePublica);

        pnlDatosHost.setOpaque(false);

        lblsIP.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsIP.setText("IP Equipo:");

        lblIP.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblIP.setText("192.168.0.3");

        lblsCodigo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsCodigo.setText("Código:");

        lblCodigo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblCodigo.setText("YDHewr435");

        javax.swing.GroupLayout pnlDatosHostLayout = new javax.swing.GroupLayout(pnlDatosHost);
        pnlDatosHost.setLayout(pnlDatosHostLayout);
        pnlDatosHostLayout.setHorizontalGroup(
            pnlDatosHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosHostLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlDatosHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosHostLayout.createSequentialGroup()
                        .addComponent(lblsCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosHostLayout.createSequentialGroup()
                        .addComponent(lblsIP, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblIP, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlDatosHostLayout.setVerticalGroup(
            pnlDatosHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosHostLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIP)
                    .addComponent(lblsIP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(lblsCodigo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelDatosAlumnoLayout = new javax.swing.GroupLayout(PanelDatosAlumno);
        PanelDatosAlumno.setLayout(PanelDatosAlumnoLayout);
        PanelDatosAlumnoLayout.setHorizontalGroup(
            PanelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrDescripcion, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(PanelDatosAlumnoLayout.createSequentialGroup()
                .addComponent(pnlDatosHost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(scrClavePublica)
        );
        PanelDatosAlumnoLayout.setVerticalGroup(
            PanelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDatosAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDatosHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrClavePublica, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlCentralLayout = new javax.swing.GroupLayout(pnlCentral);
        pnlCentral.setLayout(pnlCentralLayout);
        pnlCentralLayout.setHorizontalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelDatosAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCentralLayout.setVerticalGroup(
            pnlCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelDatosAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
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
    private javax.swing.JPanel PanelDatosAlumno;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblIP;
    private javax.swing.JLabel lblLegajo;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblsCodigo;
    private javax.swing.JLabel lblsDocumento;
    private javax.swing.JLabel lblsEmail;
    private javax.swing.JLabel lblsIP;
    private javax.swing.JLabel lblsLegajo;
    private javax.swing.JPanel pnlCentral;
    private javax.swing.JPanel pnlDatosHost;
    private frontend.auxiliares.PanelConFondo pnlFondo;
    private PanelGradiente pnlFondoAux;
    private javax.swing.JPanel pnlImagen;
    private frontend.auxiliares.PanelConFondo pnlImagenLogo;
    private javax.swing.JScrollPane scrClavePublica;
    private javax.swing.JScrollPane scrDescripcion;
    private frontend.auxiliares.TextAreaEntropy txaClavePublica;
    private frontend.auxiliares.TextAreaEntropy txaDescripcion;
    // End of variables declaration//GEN-END:variables

    private void cargarDatosUsuario(Usuario usuario) {
        this.lblApellido.setText(usuario.getStrNombre() + " " + usuario.getStrApellido());
        this.lblEmail.setText(usuario.getStrEmail());
        this.lblLegajo.setText(usuario.getStrLegajo());
        if (usuario.getIntNroDocumento() != -1) {
            lblDocumento.setText(usuario.getStrTipoDocumento() + " " + usuario.getIntNroDocumento());
        }
        this.txaDescripcion.setText(usuario.getStrDescripcion());
        if ((byte[]) usuario.getImgFoto() != null) {
            this.pnlImagenLogo.setImagen(Toolkit.getDefaultToolkit().createImage((byte[]) usuario.getImgFoto()));
            this.lblLogo.setVisible(false);
        }
        this.lblIP.setText(usuario.getStrIP());
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
