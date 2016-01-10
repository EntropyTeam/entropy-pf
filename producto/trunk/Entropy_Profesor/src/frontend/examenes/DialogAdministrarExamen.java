package frontend.examenes;

import backend.auxiliares.Mensajes;
import backend.dao.resoluciones.DAOResolucion;
import backend.mail.*;
import backend.diseños.Curso;
import backend.diseños.Institucion;
import backend.examenes.EstadoExamen;
import backend.examenes.Examen;
import backend.gestores.GestorExamen;
import backend.gestores.GestorImportarPregunta;
import backend.reporte.GestorGenerarReporteResolucion;
import backend.resoluciones.Resolucion;
import frontend.auxiliares.CeldaListaRendererEntropy;
import frontend.auxiliares.ComponentMover;
import frontend.auxiliares.ComponentResizer;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.VentanaPrincipal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

/**
 * Clase que representa la interfaz de administración de diseños de examen.
 *
 * @author Denise
 */
public class DialogAdministrarExamen extends javax.swing.JDialog {

    private Object ultimoComboActivo;
    private Examen examenSeleccionado;
    private final GestorBarrasDeEstado gestorEstados;
    private final GestorImportarPregunta gestorImportarPregunta;

    /**
     * Constructor de la clase.
     *
     * @param padre ventana principal de la aplicación.
     * @param modal true si mantiene el foco, false de lo contrario.
     */
    public DialogAdministrarExamen(VentanaPrincipal padre, boolean modal) {
        super(padre, modal);
        initComponents();
        this.setSize(new Dimension(600, 400));
        this.setLocationRelativeTo(padre);
        this.getRootPane().registerKeyboardAction(new EscapeAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.gestorImportarPregunta = new GestorImportarPregunta();
        this.gestorEstados = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);

        this.pnlCentral.add(pnlInformacion);
        this.pnlInformacion.setVisible(false);
        this.pnlInformacion.setPreferredSize(new Dimension(250, 80));

        //Fondo translúcido.
        this.pnlEstado.setBackground(new Color(255, 255, 255, 123));
        this.tree.setBackground(new Color(255, 255, 255, 123));
        this.scrTree.getViewport().setOpaque(false);
        this.scrDescripcion.getViewport().setOpaque(false);
        this.tpnDescripcion.setHighlighter(null);
        this.pnlInformacion.setBackground(new Color(240, 230, 210));
        this.scrDescripcion.setOpaque(false);
        this.scrTree.setOpaque(false);
        this.pnlDatos.setOpaque(false);

        //Seteo del árbol.
        SelectionListener s = new SelectionListener();
        this.tree.addTreeWillExpandListener(s);
        this.tree.addTreeExpansionListener(s);
        this.tree.addTreeSelectionListener(s);
        this.tree.addMouseListener(new TreeMouseClickListener());
        this.scrTree.getVerticalScrollBar().addAdjustmentListener(s);
        this.scrTree.getHorizontalScrollBar().addAdjustmentListener(s);

        //Para que el undecorated dialog pueda moverse y ajustarse en tamaño.
        ComponentMover cm = new ComponentMover(JDialog.class, lblBarraTitulo);
        ComponentResizer cr = new ComponentResizer();
        cr.setSnapSize(new Dimension(10, 10));
        cr.registerComponent(this);

        //Seteo de combos de filtros.
        this.cmbInstitucion.setRenderer(new CeldaListaRendererEntropy());
        this.cmbCurso.setRenderer(new CeldaListaRendererEntropy());
        this.cmbEstado.setRenderer(new CeldaListaRendererEntropy());
        ArrayList<Institucion> colInstitucion = gestorImportarPregunta.getInstituciones();
        this.cmbInstitucion.addItem(new Institucion("Todas"));
        for (Institucion institucion : colInstitucion) {
            this.cmbInstitucion.addItem(institucion);
        }
        this.cmbInstitucion.setSelectedIndex(0);
        this.cmbCurso.addItem(new Curso("Todos"));
        this.cmbCurso.setSelectedIndex(0);
        this.cmbEstado.addItem("Todos");
        this.cmbEstado.addItem("Corrección incompleta");
        this.cmbEstado.addItem("Corrección completa");
        this.cmbEstado.setSelectedIndex(0);
        this.cargarArbol(this.gestorImportarPregunta.getInstituciones());
    }

    private void verResoluciones() {
        if (GestorExamen.getInstancia().verResolucion(this.examenSeleccionado)) {
            this.dispose();
        }
    }

    private void verEstadisticas() {
        if (GestorExamen.getInstancia().verEstadisticas(this.examenSeleccionado)) {
            this.dispose();
        }
    }

    /**
     * Clase que maneja los eventos de click en el mouse. Necesaria porque Java
     * está loco y pinta los componentes de una manera muy rara.
     */
    private class TreeMouseClickListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getClickCount() > 0) {
                scrTree.repaint();
                tree.repaint();
                e.consume();
            }
        }
    }

    /**
     * Clase que maneja los eventos de selección en el árbol. Necesaria porque
     * Java está loco y pinta los componentes de una manera muy rara.
     */
    private class SelectionListener implements TreeSelectionListener, TreeWillExpandListener, TreeExpansionListener, AdjustmentListener {

        @Override
        public void valueChanged(TreeSelectionEvent se) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            pnlBotones.setVisible(true);
            pnlBotones.add(btnCorregirFaltantes, 1);
            if (node != null && node.getUserObject() instanceof Examen) {
                examenSeleccionado = (Examen) node.getUserObject();
                lblNombre.setText("<html>" + examenSeleccionado.getStrNombre() + "</html>");
                if (examenSeleccionado.getDteFecha().getTime() > 0) {
                    lblFecha.setText(new SimpleDateFormat("dd/MM/yyyy  -  HH:mm").format(examenSeleccionado.getDteFecha()));
                } else {
                    lblFecha.setText(" --- ");
                }
                String strDescripcion = examenSeleccionado.getStrDescripcion();
                switch (examenSeleccionado.getIntEstado()) {
                    case EstadoExamen.DESARROLLANDO:
                        lblEstado.setText("Pendiente.");
                        pnlBotones.remove(btnCorregirFaltantes);
                        break;
                    case EstadoExamen.FINALIZADO:
                        lblEstado.setText("No corregido.");
                        break;
                    case EstadoExamen.CORREGIDO:
                        lblEstado.setText("Corregido.");
                        pnlBotones.remove(btnCorregirFaltantes);
                        break;
                    case EstadoExamen.CANCELADO:
                        lblEstado.setText("Cancelado.");
                        pnlBotones.setVisible(false);
                        strDescripcion += "\n\nMotivo cancelación: " + examenSeleccionado.getStrMotivoCancelacion();
                        break;
                }
                tpnDescripcion.setText((strDescripcion.isEmpty()) ? "Sin descripción." : strDescripcion);
                pnlInformacion.setVisible(true);
            } else {
                pnlInformacion.setVisible(false);
            }

            pnlBotones.repaint();
            revalidate();
            repaint();
        }

        @Override
        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            scrTree.repaint();
            tree.repaint();
        }

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            scrTree.repaint();
            tree.repaint();
        }

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            scrTree.repaint();
            tree.repaint();
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            scrTree.repaint();
            tree.repaint();
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            scrTree.repaint();
            tree.repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlInformacion = new javax.swing.JPanel();
        pnlDatos = new javax.swing.JPanel();
        lblsNombre = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblsFecha = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblsEstado = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        scrDescripcion = new javax.swing.JScrollPane();
        tpnDescripcion = new javax.swing.JTextPane();
        pnlBotones = new javax.swing.JPanel();
        btnVerResoluciones = new javax.swing.JButton();
        btnCorregirFaltantes = new javax.swing.JButton();
        btnEstadisticas = new javax.swing.JButton();
        btnCompartir = new javax.swing.JButton();
        pnlFondo = new frontend.auxiliares.PanelConFondo();
        lblBarraTitulo = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();
        pnlFiltros = new javax.swing.JPanel();
        cmbInstitucion = new javax.swing.JComboBox();
        lblNombreInstitucion = new javax.swing.JLabel();
        lblCurso = new javax.swing.JLabel();
        cmbCurso = new javax.swing.JComboBox();
        lblEstadoExamen = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox();
        pnlCentral = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        pnlInformacion.setMaximumSize(new java.awt.Dimension(150, 32793));
        pnlInformacion.setMinimumSize(new java.awt.Dimension(132, 54));
        pnlInformacion.setPreferredSize(new java.awt.Dimension(132, 132));
        pnlInformacion.setLayout(new javax.swing.BoxLayout(pnlInformacion, javax.swing.BoxLayout.Y_AXIS));

        pnlDatos.setMaximumSize(new java.awt.Dimension(32767, 80));
        pnlDatos.setMinimumSize(new java.awt.Dimension(100, 80));

        lblsNombre.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsNombre.setText("<html>Nombre:</html>");
        lblsNombre.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblNombre.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblNombre.setText("Nombre");
        lblNombre.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblNombre.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        lblsFecha.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsFecha.setText("Fecha:");

        lblFecha.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblFecha.setText("01/01/01");

        lblsEstado.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsEstado.setText("Estado:");

        lblEstado.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblEstado.setText("Corregido");

        javax.swing.GroupLayout pnlDatosLayout = new javax.swing.GroupLayout(pnlDatos);
        pnlDatos.setLayout(pnlDatosLayout);
        pnlDatosLayout.setHorizontalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblsEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblsFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblsNombre, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDatosLayout.setVerticalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblsNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsFecha)
                    .addComponent(lblFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsEstado)
                    .addComponent(lblEstado))
                .addContainerGap())
        );

        pnlInformacion.add(pnlDatos);

        scrDescripcion.setBackground(new java.awt.Color(204, 204, 204));
        scrDescripcion.setBorder(null);

        tpnDescripcion.setEditable(false);
        tpnDescripcion.setBackground(new java.awt.Color(204, 204, 204));
        tpnDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descripción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_REGULAR));
        tpnDescripcion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        tpnDescripcion.setMaximumSize(new java.awt.Dimension(2147483647, 60));
        tpnDescripcion.setMinimumSize(new java.awt.Dimension(2147483647, 60));
        tpnDescripcion.setOpaque(false);
        tpnDescripcion.setPreferredSize(new java.awt.Dimension(28, 60));
        scrDescripcion.setViewportView(tpnDescripcion);

        pnlInformacion.add(scrDescripcion);

        pnlBotones.setMaximumSize(new java.awt.Dimension(32767, 33));
        pnlBotones.setPreferredSize(new java.awt.Dimension(259, 33));
        pnlBotones.setLayout(new java.awt.GridLayout(1, 0, 3, 0));

        btnVerResoluciones.setBackground(new java.awt.Color(244, 225, 200));
        btnVerResoluciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_examinar_25x25.png"))); // NOI18N
        btnVerResoluciones.setToolTipText("");
        btnVerResoluciones.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnVerResoluciones.setContentAreaFilled(false);
        btnVerResoluciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerResoluciones.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnVerResoluciones.setMinimumSize(new java.awt.Dimension(33, 33));
        btnVerResoluciones.setOpaque(true);
        btnVerResoluciones.setPreferredSize(new java.awt.Dimension(33, 33));
        btnVerResoluciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVerResolucionesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVerResolucionesMouseExited(evt);
            }
        });
        btnVerResoluciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerResolucionesActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVerResoluciones);

        btnCorregirFaltantes.setBackground(new java.awt.Color(244, 225, 200));
        btnCorregirFaltantes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_mano_25x25.png"))); // NOI18N
        btnCorregirFaltantes.setToolTipText("");
        btnCorregirFaltantes.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnCorregirFaltantes.setContentAreaFilled(false);
        btnCorregirFaltantes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCorregirFaltantes.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnCorregirFaltantes.setMinimumSize(new java.awt.Dimension(33, 33));
        btnCorregirFaltantes.setOpaque(true);
        btnCorregirFaltantes.setPreferredSize(new java.awt.Dimension(33, 33));
        btnCorregirFaltantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCorregirFaltantesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCorregirFaltantesMouseExited(evt);
            }
        });
        btnCorregirFaltantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorregirFaltantesActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCorregirFaltantes);

        btnEstadisticas.setBackground(new java.awt.Color(244, 225, 200));
        btnEstadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_estadisticas_25x25.png"))); // NOI18N
        btnEstadisticas.setToolTipText("");
        btnEstadisticas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnEstadisticas.setContentAreaFilled(false);
        btnEstadisticas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEstadisticas.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnEstadisticas.setMinimumSize(new java.awt.Dimension(33, 33));
        btnEstadisticas.setOpaque(true);
        btnEstadisticas.setPreferredSize(new java.awt.Dimension(33, 33));
        btnEstadisticas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEstadisticasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEstadisticasMouseExited(evt);
            }
        });
        btnEstadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstadisticasActionPerformed(evt);
            }
        });
        pnlBotones.add(btnEstadisticas);

        btnCompartir.setBackground(new java.awt.Color(244, 225, 200));
        btnCompartir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_compartir_25x25.png"))); // NOI18N
        btnCompartir.setToolTipText("");
        btnCompartir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnCompartir.setContentAreaFilled(false);
        btnCompartir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCompartir.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnCompartir.setMinimumSize(new java.awt.Dimension(33, 33));
        btnCompartir.setOpaque(true);
        btnCompartir.setPreferredSize(new java.awt.Dimension(33, 33));
        btnCompartir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCompartirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCompartirMouseExited(evt);
            }
        });
        btnCompartir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompartirActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCompartir);

        pnlInformacion.add(pnlBotones);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        pnlFondo.setImagen(GestorImagenes.crearImage("/frontend/imagenes/bg2.jpg"));
        pnlFondo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 0), 1, true));

        lblBarraTitulo.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo.setText("Selector de exámenes");

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_mensajes_cerrar_opcion.png"))); // NOI18N
        btnCerrar.setBorder(null);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

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
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
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

        pnlFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar por", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_REGULAR));
        pnlFiltros.setOpaque(false);

        cmbInstitucion.setBackground(new java.awt.Color(255, 204, 102));
        cmbInstitucion.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbInstitucion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbInstitucionItemStateChanged(evt);
            }
        });
        cmbInstitucion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbInstitucionFocusGained(evt);
            }
        });

        lblNombreInstitucion.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblNombreInstitucion.setText("Institución:");

        lblCurso.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblCurso.setText("Curso:");

        cmbCurso.setBackground(new java.awt.Color(255, 204, 102));
        cmbCurso.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbCurso.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCursoItemStateChanged(evt);
            }
        });
        cmbCurso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbCursoFocusGained(evt);
            }
        });

        lblEstadoExamen.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblEstadoExamen.setText("Estado:");

        cmbEstado.setBackground(new java.awt.Color(255, 204, 102));
        cmbEstado.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbEstado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoItemStateChanged(evt);
            }
        });
        cmbEstado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbEstadoFocusGained(evt);
            }
        });

        javax.swing.GroupLayout pnlFiltrosLayout = new javax.swing.GroupLayout(pnlFiltros);
        pnlFiltros.setLayout(pnlFiltrosLayout);
        pnlFiltrosLayout.setHorizontalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblEstadoExamen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbInstitucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbCurso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlFiltrosLayout.setVerticalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstadoExamen)))
        );

        pnlCentral.setOpaque(false);
        pnlCentral.setLayout(new javax.swing.BoxLayout(pnlCentral, javax.swing.BoxLayout.X_AXIS));

        scrTree.setBorder(null);
        scrTree.setPreferredSize(new java.awt.Dimension(43, 200));

        tree.setCellRenderer(new frontend.auxiliares.CeldaTreeRendererEntropy());
        tree.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        scrTree.setViewportView(tree);

        pnlCentral.add(scrTree);

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(lblBarraTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCerrar))
                    .addComponent(pnlCentral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBarraTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 393, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerResolucionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerResolucionesMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Ver resoluciones de los alumnos.");
        this.repaint();
    }//GEN-LAST:event_btnVerResolucionesMouseEntered

    private void btnVerResolucionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerResolucionesMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnVerResolucionesMouseExited

    private void btnVerResolucionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerResolucionesActionPerformed
        verResoluciones();
    }//GEN-LAST:event_btnVerResolucionesActionPerformed

    private void btnEstadisticasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstadisticasMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Ver estadísticas del examen.");
        this.repaint();
    }//GEN-LAST:event_btnEstadisticasMouseEntered

    private void btnEstadisticasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstadisticasMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnEstadisticasMouseExited

    private void btnEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstadisticasActionPerformed
        this.verEstadisticas();
    }//GEN-LAST:event_btnEstadisticasActionPerformed

    private void btnCompartirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompartirMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Compartir este examen.");
        this.repaint();
    }//GEN-LAST:event_btnCompartirMouseEntered

    private void btnCompartirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompartirMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnCompartirMouseExited

    private void btnCompartirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompartirActionPerformed
        System.err.println("FALTA IMPLEMENTAR COMPARTIR");

        //Codigo mio para probar, esta mal.
        ArrayList<Resolucion> colResoluciones = new DAOResolucion().getResoluciones(examenSeleccionado);
        if (colResoluciones.isEmpty()) {
            Mensajes.mostrarInformacion("El examen no posee resoluciones.");
        } else {
            for (Resolucion resolucion : colResoluciones) {
                resolucion.setExamen(examenSeleccionado);
            }
        }
        GestorGenerarReporteResolucion gestorGenerarReporteResolucion = new GestorGenerarReporteResolucion(colResoluciones.get(0));
        gestorGenerarReporteResolucion.generarReporteResolucion();
        gestorGenerarReporteResolucion.borrarResolucionDeDisco();
        
        enviarMail("Se le envía el examen a los alumnos.", new ArrayList<String>());
    }//GEN-LAST:event_btnCompartirActionPerformed
    private void enviarMail(String cuerpoDelMensaje, ArrayList<String> destinarios) {
        GestorEnvioDeMail gestorEnvioDeMail = new GestorEnvioDeMail();
        Email nuevoMail = new Email();
        nuevoMail.setMessage(cuerpoDelMensaje);
        byte[] bytes = null;
        nuevoMail.setAdjunto(cuerpoDelMensaje, bytes);
        gestorEnvioDeMail.enviarMuchosDestinatarios(nuevoMail, destinarios);

    }
    private void cmbInstitucionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInstitucionItemStateChanged
        if (ultimoComboActivo == evt.getSource()) {
            int intIndexSeleccionado = this.cmbInstitucion.getSelectedIndex();
            if (intIndexSeleccionado > 0) { //Seleccionó una institución.
                this.limpiarCursos();
                ArrayList<Curso> colCurso = this.getCursos((Institucion) this.cmbInstitucion.getSelectedItem());
                for (Curso curso : colCurso) {
                    this.cmbCurso.addItem(curso);
                }
                ArrayList<Institucion> colInstituciones = new ArrayList<>();
                colInstituciones.add((Institucion) this.cmbInstitucion.getSelectedItem());
                this.cargarArbol(colInstituciones);
            } else if (intIndexSeleccionado == 0) { //Selecciónó "Todas".
                this.limpiarCursos();
                this.cargarArbol(this.gestorImportarPregunta.getInstituciones());
            }
        }
    }//GEN-LAST:event_cmbInstitucionItemStateChanged

    private void cmbInstitucionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbInstitucionFocusGained
        this.ultimoComboActivo = evt.getComponent();
    }//GEN-LAST:event_cmbInstitucionFocusGained

    private void cmbCursoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCursoItemStateChanged
        if (ultimoComboActivo == evt.getSource()) {
            if (this.cmbCurso.getSelectedIndex() > 0) { //Seleccionó un curso.
                this.cargarArbol((Curso) this.cmbCurso.getSelectedItem());
            } else if (this.cmbCurso.getSelectedItem().toString().equals("Todos")) {
                ArrayList<Institucion> colInstituciones = new ArrayList<>();
                colInstituciones.add((Institucion) this.cmbInstitucion.getSelectedItem());
                this.cargarArbol(colInstituciones);
            }
        }
    }//GEN-LAST:event_cmbCursoItemStateChanged

    private void cmbCursoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbCursoFocusGained
        this.ultimoComboActivo = evt.getComponent();
    }//GEN-LAST:event_cmbCursoFocusGained

    private void cmbEstadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoItemStateChanged
        if (ultimoComboActivo == evt.getSource()) {
            if (this.cmbCurso.getSelectedIndex() > 0) { //Seleccionó un curso.
                this.cargarArbol((Curso) this.cmbCurso.getSelectedItem());
            } else if (this.cmbCurso.getSelectedItem().toString().equals("Todos")) {
                cmbInstitucionItemStateChanged(evt);
            }
        }
        this.repaint();
    }//GEN-LAST:event_cmbEstadoItemStateChanged

    private void cmbEstadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbEstadoFocusGained
        this.ultimoComboActivo = evt.getComponent();
    }//GEN-LAST:event_cmbEstadoFocusGained

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnCorregirFaltantesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCorregirFaltantesMouseEntered
        gestorEstados.setEstadoInstantaneo("Corregir todas las preguntas aún sin calificación de este examen.");
        this.repaint();
    }//GEN-LAST:event_btnCorregirFaltantesMouseEntered

    private void btnCorregirFaltantesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCorregirFaltantesMouseExited
        gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnCorregirFaltantesMouseExited

    private void btnCorregirFaltantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorregirFaltantesActionPerformed
        if (GestorExamen.getInstancia().calificarRespuestasSinCalificacion(null, examenSeleccionado)) {
            this.dispose();
        }
    }//GEN-LAST:event_btnCorregirFaltantesActionPerformed

    public GestorBarrasDeEstado getGestorEstados() {
        return gestorEstados;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnCompartir;
    private javax.swing.JButton btnCorregirFaltantes;
    private javax.swing.JButton btnEstadisticas;
    private javax.swing.JButton btnVerResoluciones;
    private javax.swing.JComboBox cmbCurso;
    private javax.swing.JComboBox cmbEstado;
    private javax.swing.JComboBox cmbInstitucion;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblBarraTitulo;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstadoExamen;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreInstitucion;
    private javax.swing.JLabel lblsEstado;
    private javax.swing.JLabel lblsFecha;
    private javax.swing.JLabel lblsNombre;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCentral;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JPanel pnlEstado;
    private javax.swing.JPanel pnlFiltros;
    private frontend.auxiliares.PanelConFondo pnlFondo;
    private javax.swing.JPanel pnlInformacion;
    private javax.swing.JScrollPane scrDescripcion;
    private javax.swing.JScrollPane scrTree;
    private javax.swing.JTextPane tpnDescripcion;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

    /**
     * Carga la jerarquía del árbol desde la base de datos.
     *
     * @param instituciones instituciones para la cuales se recuperarán
     * exámenes.
     */
    private void cargarArbol(ArrayList<Institucion> instituciones) {
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(" Exámenes tomados");
        int intEstado = cmbEstado.getSelectedIndex();
        for (Institucion institucion : instituciones) {
            DefaultMutableTreeNode nodoInstitucion = new DefaultMutableTreeNode(institucion);
            for (Curso curso : this.gestorImportarPregunta.getCursos(institucion)) {
                DefaultMutableTreeNode nodoCurso = new DefaultMutableTreeNode(curso);
                for (Examen examen : GestorExamen.getInstancia().getExamen(curso)) {
                    if (intEstado == 0
                            || (intEstado == 1 && examen.getIntEstado() == EstadoExamen.FINALIZADO)
                            || (intEstado == 2 && examen.getIntEstado() == EstadoExamen.CORREGIDO)) {
                        nodoCurso.add(new DefaultMutableTreeNode(examen));
                    }
                }
                nodoInstitucion.add(nodoCurso);
            }
            nodoPadre.add(nodoInstitucion);
        }
        tree.setModel(new DefaultTreeModel(nodoPadre));
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    /**
     * Carga la jerarquía del árbol desde la base de datos.
     *
     * @param curso curso para el cual se recuperarán los diseños.
     */
    private void cargarArbol(Curso curso) {
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(" Exámenes tomados");
        DefaultMutableTreeNode nodoInstitucion = new DefaultMutableTreeNode((Institucion) this.cmbInstitucion.getSelectedItem());
        DefaultMutableTreeNode nodoCurso = new DefaultMutableTreeNode(curso);
        int intEstado = cmbEstado.getSelectedIndex();
        for (Examen examen : GestorExamen.getInstancia().getExamen(curso)) {
            if (intEstado == 0
                    || (intEstado == 1 && examen.getIntEstado() == EstadoExamen.FINALIZADO)
                    || (intEstado == 2 && examen.getIntEstado() == EstadoExamen.CORREGIDO)) {
                nodoCurso.add(new DefaultMutableTreeNode(examen));
            }
        }
        nodoInstitucion.add(nodoCurso);
        nodoPadre.add(nodoInstitucion);
        tree.setModel(new DefaultTreeModel(nodoPadre));
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    /**
     * Setea el combo box de cursos a sus valores iniciales.
     */
    private void limpiarCursos() {
        this.cmbCurso.removeAllItems();
        this.cmbCurso.addItem("Todos");
        this.cmbCurso.setSelectedIndex(0);
    }

    /**
     * Se comunica con el gestor de importacion para buscar todos los cursos que
     * pertenecen a una Institucion.
     *
     * @param institucion la institucion por la que se quiere filtrar.
     * @return Coleccion de cursos que tiene la institucion.
     */
    private ArrayList<Curso> getCursos(Institucion institucion) {
        return new GestorImportarPregunta().getCursos(institucion);
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
}
