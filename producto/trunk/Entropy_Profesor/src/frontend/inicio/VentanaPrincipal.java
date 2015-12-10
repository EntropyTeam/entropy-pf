package frontend.inicio;

import frontend.alumnos.DialogSelectorAlumno;
import backend.red.GestorRedAdHoc;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.auxiliares.PanelDeslizante;
import frontend.auxiliares.PanelConMenu;
import frontend.diseños.DialogAdministrarDiseñoExamen;
import frontend.diseños.PanelDiseño;
import frontend.examenes.DialogAdministrarExamen;
import frontend.presentaciones.DialogAdministrarClasesDictadas;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * Clase que representa la Ventana Principal de la aplicación en el módulo del
 * profesor.
 *
 * @author Denise
 */
public class VentanaPrincipal extends javax.swing.JFrame implements IVentanaPrincipal {

    private static VentanaPrincipal INSTANCIA = null;

    /**
     * Constructor por defecto.
     */
    private VentanaPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.pnlSlides.setPanelMostrado(pnlInicio);
        this.setIconImage(this.getIconImage());
        this.getRootPane().registerKeyboardAction(new EscapeAction(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private synchronized static void createInstance() {
        if (INSTANCIA == null) {
            INSTANCIA = new VentanaPrincipal();
        }
    }

    public static VentanaPrincipal getInstancia() {
        createInstance();
        return INSTANCIA;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlConMenu = new PanelConMenu(new MenuPrincipal(this));
        pnlBackground = new frontend.auxiliares.PanelConFondo();
        pnlMargen = new javax.swing.JPanel();
        lblSeparador = new javax.swing.JLabel();
        pnlSlides = new frontend.auxiliares.PanelDeslizante();
        mnbMenuBar = new javax.swing.JMenuBar();
        mncDiseños = new javax.swing.JMenu();
        mniNuevoExamen = new javax.swing.JMenuItem();
        mniEditarExamen = new javax.swing.JMenuItem();
        mniAdministrarDiseños = new javax.swing.JMenuItem();
        mncExámenes = new javax.swing.JMenu();
        mniTomarExamen = new javax.swing.JMenuItem();
        mniAdministrarExamenes = new javax.swing.JMenuItem();
        mncAlumnos = new javax.swing.JMenu();
        mniBuscarAlumno = new javax.swing.JMenuItem();
        mncPresentacion = new javax.swing.JMenu();
        mniAdministrarClasesDictadas = new javax.swing.JMenuItem();
        mncHerramientas = new javax.swing.JMenu();
        mncVentana = new javax.swing.JMenu();
        mncAyuda = new javax.swing.JMenu();
        mniAyuda = new javax.swing.JMenuItem();
        mniAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Administración de Entornos Educativos");
        setMinimumSize(new java.awt.Dimension(500, 200));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlBackground.setImagen(GestorImagenes.crearImage("/frontend/imagenes/main_background.jpg"));
        pnlBackground.setLayout(new javax.swing.BoxLayout(pnlBackground, javax.swing.BoxLayout.LINE_AXIS));

        pnlMargen.setBackground(new java.awt.Color(255, 255, 255));
        pnlMargen.setOpaque(false);
        pnlMargen.setLayout(new java.awt.GridLayout(1, 0));

        lblSeparador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSeparador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/separator.png"))); // NOI18N
        lblSeparador.setMaximumSize(new java.awt.Dimension(30, 50));
        lblSeparador.setMinimumSize(new java.awt.Dimension(30, 50));
        lblSeparador.setPreferredSize(new java.awt.Dimension(30, 50));
        pnlMargen.add(lblSeparador);

        pnlBackground.add(pnlMargen);

        pnlInicio  = new PanelInicio();
        pnlSlides.setOpaque(false);
        pnlInicio.setOpaque(false);

        javax.swing.GroupLayout pnlSlidesLayout = new javax.swing.GroupLayout(pnlSlides);
        pnlSlides.setLayout(pnlSlidesLayout);
        pnlSlidesLayout.setHorizontalGroup(
            pnlSlidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );
        pnlSlidesLayout.setVerticalGroup(
            pnlSlidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        pnlSlides.setName("Central");
        pnlSlides.setLayout(new java.awt.CardLayout());
        pnlInicio.setName("Inicial");
        pnlSlides.add(pnlInicio, "card1");

        pnlBackground.add(pnlSlides);

        javax.swing.GroupLayout pnlConMenuLayout = new javax.swing.GroupLayout(pnlConMenu);
        pnlConMenu.setLayout(pnlConMenuLayout);
        pnlConMenuLayout.setHorizontalGroup(
            pnlConMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlConMenuLayout.setVerticalGroup(
            pnlConMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlConMenu.setLayer(pnlBackground, javax.swing.JLayeredPane.DEFAULT_LAYER);

        mnbMenuBar.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        mncDiseños.setText("Diseños");
        mncDiseños.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        mniNuevoExamen.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniNuevoExamen.setText("Nuevo diseño");
        mniNuevoExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniNuevoExamenActionPerformed(evt);
            }
        });
        mncDiseños.add(mniNuevoExamen);

        mniEditarExamen.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniEditarExamen.setText("Editar diseño");
        mniEditarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniEditarExamenActionPerformed(evt);
            }
        });
        mncDiseños.add(mniEditarExamen);

        mniAdministrarDiseños.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniAdministrarDiseños.setText("Administrar diseños");
        mniAdministrarDiseños.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniAdministrarDiseñosActionPerformed(evt);
            }
        });
        mncDiseños.add(mniAdministrarDiseños);

        mnbMenuBar.add(mncDiseños);

        mncExámenes.setText("Exámenes");
        mncExámenes.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        mniTomarExamen.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniTomarExamen.setText("Tomar examen");
        mniTomarExamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniTomarExamenActionPerformed(evt);
            }
        });
        mncExámenes.add(mniTomarExamen);

        mniAdministrarExamenes.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniAdministrarExamenes.setText("Administrar exámenes tomados");
        mniAdministrarExamenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniAdministrarExamenesActionPerformed(evt);
            }
        });
        mncExámenes.add(mniAdministrarExamenes);

        mnbMenuBar.add(mncExámenes);

        mncAlumnos.setText("Alumnos");
        mncAlumnos.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        mniBuscarAlumno.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniBuscarAlumno.setText("Buscar...");
        mniBuscarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniBuscarAlumnoActionPerformed(evt);
            }
        });
        mncAlumnos.add(mniBuscarAlumno);

        mnbMenuBar.add(mncAlumnos);

        mncPresentacion.setText("Clases");
        mncPresentacion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        mniAdministrarClasesDictadas.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniAdministrarClasesDictadas.setText("Administrar clases dictadas");
        mniAdministrarClasesDictadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniAdministrarClasesDictadasActionPerformed(evt);
            }
        });
        mncPresentacion.add(mniAdministrarClasesDictadas);

        mnbMenuBar.add(mncPresentacion);

        mncHerramientas.setText("Herramientas");
        mncHerramientas.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mnbMenuBar.add(mncHerramientas);

        mncVentana.setText("Ventana");
        mncVentana.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mnbMenuBar.add(mncVentana);

        mncAyuda.setText("Ayuda");
        mncAyuda.setFont(LookAndFeelEntropy.FUENTE_REGULAR);

        mniAyuda.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniAyuda.setText("Ver Ayuda");
        mncAyuda.add(mniAyuda);

        mniAcercaDe.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        mniAcercaDe.setText("Acerca de...");
        mncAyuda.add(mniAcercaDe);

        mnbMenuBar.add(mncAyuda);

        setJMenuBar(mnbMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConMenu)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConMenu, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mniAdministrarDiseñosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniAdministrarDiseñosActionPerformed
        ocultarMenu();
        DialogAdministrarDiseñoExamen dlgSeleccionarExamenes = new DialogAdministrarDiseñoExamen(this, true, DialogAdministrarDiseñoExamen.TipoAccion.ADMINISTRAR);
        dlgSeleccionarExamenes.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de administración de diseños de exámenes! Seleccione un examen...");
        dlgSeleccionarExamenes.setVisible(true);
    }//GEN-LAST:event_mniAdministrarDiseñosActionPerformed

    private void mniNuevoExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNuevoExamenActionPerformed
        ocultarMenu();
        PanelDiseño pnlNuevoExamen = new PanelDiseño(this);
        pnlNuevoExamen.setName("Edición Examen");
        pnlNuevoExamen.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de nuevo examen!");
        getPanelDeslizante().setPanelMostrado(pnlNuevoExamen);
        setTitle("Nuevo examen sin título");
        if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            pack();
        }
    }//GEN-LAST:event_mniNuevoExamenActionPerformed

    private void mniEditarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEditarExamenActionPerformed
        ocultarMenu();
        DialogAdministrarDiseñoExamen dlgSeleccionarExamenes = new DialogAdministrarDiseñoExamen(this, true, DialogAdministrarDiseñoExamen.TipoAccion.EDITAR);
        dlgSeleccionarExamenes.getGestorEstados().setNuevoEstadoImportante("Seleccione un diseño para editar... Utilice doble click o el botón correspondiente.");
        dlgSeleccionarExamenes.setVisible(true);
    }//GEN-LAST:event_mniEditarExamenActionPerformed

    private void mniTomarExamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniTomarExamenActionPerformed
        ocultarMenu();
        DialogAdministrarDiseñoExamen dlgSeleccionarExamenes = new DialogAdministrarDiseñoExamen(this, true, DialogAdministrarDiseñoExamen.TipoAccion.TOMAR);
        dlgSeleccionarExamenes.getGestorEstados().setNuevoEstadoImportante("Seleccione un diseño para tomar... Utilice doble click o el botón correspondiente.");
        dlgSeleccionarExamenes.setVisible(true);
    }//GEN-LAST:event_mniTomarExamenActionPerformed

    private void mniAdministrarExamenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniAdministrarExamenesActionPerformed
        ocultarMenu();
        DialogAdministrarExamen dlgSeleccionarExamenes = new DialogAdministrarExamen(this, true);
        dlgSeleccionarExamenes.getGestorEstados().setNuevoEstadoImportante("Seleccione un examen para trabajar... Utilice doble click o el botón correspondiente.");
        dlgSeleccionarExamenes.setVisible(true);
    }//GEN-LAST:event_mniAdministrarExamenesActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        GestorRedAdHoc gestorRedAdHoc = new GestorRedAdHoc();
        gestorRedAdHoc.desconectar();
    }//GEN-LAST:event_formWindowClosing

    private void mniAdministrarClasesDictadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniAdministrarClasesDictadasActionPerformed
        ocultarMenu();
        DialogAdministrarClasesDictadas presentacionesRealizadas = new DialogAdministrarClasesDictadas(this, true);
        presentacionesRealizadas.setVisible(true);
    }//GEN-LAST:event_mniAdministrarClasesDictadasActionPerformed

    private void mniBuscarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniBuscarAlumnoActionPerformed
        ocultarMenu();
        DialogSelectorAlumno dlgSeleccionarAlumno = new DialogSelectorAlumno(this, true);
        dlgSeleccionarAlumno.getGestorEstados().setNuevoEstadoImportante("Seleccione un examen para trabajar... Utilice doble click o el botón correspondiente.");
        dlgSeleccionarAlumno.setVisible(true);
    }//GEN-LAST:event_mniBuscarAlumnoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblSeparador;
    private javax.swing.JMenuBar mnbMenuBar;
    private javax.swing.JMenu mncAlumnos;
    private javax.swing.JMenu mncAyuda;
    private javax.swing.JMenu mncDiseños;
    private javax.swing.JMenu mncExámenes;
    private javax.swing.JMenu mncHerramientas;
    private javax.swing.JMenu mncPresentacion;
    private javax.swing.JMenu mncVentana;
    private javax.swing.JMenuItem mniAcercaDe;
    private javax.swing.JMenuItem mniAdministrarClasesDictadas;
    private javax.swing.JMenuItem mniAdministrarDiseños;
    private javax.swing.JMenuItem mniAdministrarExamenes;
    private javax.swing.JMenuItem mniAyuda;
    private javax.swing.JMenuItem mniBuscarAlumno;
    private javax.swing.JMenuItem mniEditarExamen;
    private javax.swing.JMenuItem mniNuevoExamen;
    private javax.swing.JMenuItem mniTomarExamen;
    private frontend.auxiliares.PanelConFondo pnlBackground;
    private javax.swing.JLayeredPane pnlConMenu;
    private javax.swing.JPanel pnlMargen;
    private frontend.auxiliares.PanelDeslizante pnlSlides;
    private PanelInicio pnlInicio;
    // End of variables declaration//GEN-END:variables

    @Override
    public PanelDeslizante getPanelDeslizante() {
        return pnlSlides;
    }

    @Override
    public PanelInicio getPnlInicio() {
        return pnlInicio;
    }

    @Override
    public Image getIconImage() {
        return GestorImagenes.crearImage("/frontend/imagenes/ic_system.png");
    }

    /**
     * Oculta el menú lateral.
     */
    @Override
    public void ocultarMenu() {
        try {
            ((PanelConMenu) pnlConMenu).ocultar();
        } catch (Exception e) {
            System.err.println("Error al castear el panel con menú: " + e.getMessage());
        }
    }

    @Override
    public void volverAInicio(){
        VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado(VentanaPrincipal.getInstancia().getPnlInicio());
        VentanaPrincipal.getInstancia().setTitle("Sistema de Administración de Entornos Educativos");
    }
    
    @Override
    public void setTitulo (String strTitulo) {
        VentanaPrincipal.getInstancia().setTitle(strTitulo);
    }
    
    /**
     * Clase que escucha por el tecleo de la tecla Esc.
     */
    private class EscapeAction implements ActionListener {

        VentanaPrincipal frmPrincipal;

        public EscapeAction(VentanaPrincipal frame) {
            this.frmPrincipal = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (pnlSlides.esPanelMostrado(pnlInicio)) {
                return;
            }
            pnlSlides.setPanelMostrado(pnlInicio, false);
        }
    }
}
