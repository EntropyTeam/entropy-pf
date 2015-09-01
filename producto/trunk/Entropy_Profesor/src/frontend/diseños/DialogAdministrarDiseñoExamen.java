package frontend.diseños;

import backend.auxiliares.Mensajes;
import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Institucion;
import backend.gestores.GestorDiseñoExamen;
import backend.reporte.GestorGenerarReporteDiseñoExamen;
import backend.gestores.GestorImportarPregunta;
import frontend.auxiliares.CeldaListaRendererEntropy;
import frontend.auxiliares.ComponentMover;
import frontend.auxiliares.ComponentResizer;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.GestorImagenes;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.inicio.VentanaPrincipal;
import frontend.tomaexamenes.PanelTomaExamen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
public class DialogAdministrarDiseñoExamen extends javax.swing.JDialog {

    public enum TipoAccion {

        ADMINISTRAR, EDITAR, TOMAR
    }

    private final GestorBarrasDeEstado gestorEstados;
    private final GestorImportarPregunta gestorImportarPregunta;
    private final VentanaPrincipal mPadre;
    private final TipoAccion accion;
    private Object ultimoComboActivo;
    private DiseñoExamen examenSeleccionado;

    /**
     * Constructor de la clase.
     *
     * @param padre ventana principal de la aplicación.
     * @param modal true si mantiene el foco, false de lo contrario.
     * @param accion instancia de TipoAccion: ADMINISTRAR, EDITAR, TOMAR
     */
    public DialogAdministrarDiseñoExamen(VentanaPrincipal padre, boolean modal, TipoAccion accion) {
        super(padre, modal);
        this.mPadre = padre;
        initComponents();
        this.accion = accion;
        this.setSize(new Dimension(550, 400));
        this.getRootPane().registerKeyboardAction(new EscapeAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.pnlCentral.add(pnlInformacion);
        this.pnlInformacion.setVisible(false);
        this.pnlInformacion.setPreferredSize(new Dimension (200, 80));

        this.gestorImportarPregunta = new GestorImportarPregunta();
        this.gestorEstados = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);

        //Fondo translúcido.
        this.pnlEstado.setBackground(new Color(255, 255, 255, 123));
        this.tree.setBackground(new Color(255, 255, 255, 123));
        this.scrTree.getViewport().setOpaque(false);
        this.scrDescripcion.getViewport().setOpaque(false);
        this.tpnDescripcion.setHighlighter(null);
        this.pnlInformacion.setBackground(new Color(240, 230, 210));
        this.scrDescripcion.setOpaque(false);
        this.scrTree.setOpaque(false);

        //Seteo del árbol.
        SelectionListener s = new SelectionListener();
        this.tree.addTreeWillExpandListener(s);
        this.tree.addTreeExpansionListener(s);
        this.tree.addTreeSelectionListener(s);
        this.tree.addMouseListener(new TreeMouseClickListener());
        this.scrTree.getVerticalScrollBar().addAdjustmentListener(s);
        this.scrTree.getHorizontalScrollBar().addAdjustmentListener(s);
        this.cargarArbol();

        //Seteo de botones según opciones
        if (accion.equals(TipoAccion.EDITAR)) {
            pnlBotones.add(btnEditar);
        } else if (accion.equals(TipoAccion.TOMAR)) {
            pnlBotones.add(btnTomar);
        } else if (accion.equals(TipoAccion.ADMINISTRAR)) {
            lblBarraTitulo.setText("Administración de diseños de exámenes");
            pnlBotones.add(btnEditar);
            pnlBotones.add(btnDuplicar);
            pnlBotones.add(btnTomar);
            pnlBotones.add(btnImprimir);
            pnlBotones.add(btnEliminar);
        }

        //Para que el undecorated dialog pueda moverse y ajustarse en tamaño.
        ComponentMover cm = new ComponentMover(JDialog.class, lblBarraTitulo);
        ComponentResizer cr = new ComponentResizer();
        cr.setSnapSize(new Dimension(10, 10));
        cr.registerComponent(this);

        //Seteo de combos de filtros.
        this.cmbInstitucion.setRenderer(new CeldaListaRendererEntropy());
        this.cmbCurso.setRenderer(new CeldaListaRendererEntropy());
        ArrayList<Institucion> colInstitucion = gestorImportarPregunta.getInstituciones();
        this.cmbInstitucion.addItem("No aplica");
        this.cmbInstitucion.addItem("Todas");
        for (Institucion institucion : colInstitucion) {
            this.cmbInstitucion.addItem(institucion);
        }
        this.cmbInstitucion.setSelectedIndex(0);
        this.cmbCurso.addItem("No aplica");
        this.cmbCurso.setSelectedIndex(0);
        
        this.setLocationRelativeTo(padre);
    }

    /**
     * Clase que maneja los eventos de click en el mouse. Necesaria porque Java
     * está loco y pinta los componentes de una manera muy rara.
     */
    private class TreeMouseClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (accion.equals(TipoAccion.EDITAR)) {
                    editarDiseño();
                } else if (accion.equals(TipoAccion.TOMAR)) {
                    tomarDiseño();
                }
            }
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getClickCount() > 0) {
                scrTree.repaint();
                tree.repaint();
                e.consume();
            }
            repaint();
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

            if (node != null && node.getUserObject() instanceof DiseñoExamen) {
                examenSeleccionado = (DiseñoExamen) node.getUserObject();
                String strDescripcion = examenSeleccionado.getStrDescripcion();
                tpnDescripcion.setText((strDescripcion.isEmpty()) ? "Sin descripción." : strDescripcion);
                pnlInformacion.setVisible(true);
            } else {
                pnlInformacion.setVisible(false);
            }
            
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
        scrDescripcion = new javax.swing.JScrollPane();
        tpnDescripcion = new javax.swing.JTextPane();
        pnlBotones = new javax.swing.JPanel();
        btnEditar = new javax.swing.JButton();
        btnDuplicar = new javax.swing.JButton();
        btnTomar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lpnCapas = new javax.swing.JLayeredPane();
        pnlFondo = new frontend.auxiliares.PanelConFondo();
        lblBarraTitulo = new javax.swing.JLabel();
        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        pnlFiltros = new javax.swing.JPanel();
        cmbInstitucion = new javax.swing.JComboBox();
        lblNombreInstitucion = new javax.swing.JLabel();
        lblCurso = new javax.swing.JLabel();
        cmbCurso = new javax.swing.JComboBox();
        pnlCentral = new javax.swing.JPanel();
        scrTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        pnlInformacion.setMaximumSize(new java.awt.Dimension(150, 32793));
        pnlInformacion.setMinimumSize(new java.awt.Dimension(132, 54));
        pnlInformacion.setPreferredSize(new java.awt.Dimension(132, 132));
        pnlInformacion.setLayout(new javax.swing.BoxLayout(pnlInformacion, javax.swing.BoxLayout.Y_AXIS));

        scrDescripcion.setBackground(new java.awt.Color(204, 204, 204));
        scrDescripcion.setBorder(null);

        tpnDescripcion.setEditable(false);
        tpnDescripcion.setBackground(new java.awt.Color(204, 204, 204));
        tpnDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descripción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, LookAndFeelEntropy.FUENTE_REGULAR));
        tpnDescripcion.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        tpnDescripcion.setOpaque(false);
        scrDescripcion.setViewportView(tpnDescripcion);

        pnlInformacion.add(scrDescripcion);

        pnlBotones.setMaximumSize(new java.awt.Dimension(32767, 33));
        pnlBotones.setPreferredSize(new java.awt.Dimension(259, 33));
        pnlBotones.setLayout(new java.awt.GridLayout(1, 0, 3, 0));
        pnlInformacion.add(pnlBotones);

        btnEditar.setBackground(new java.awt.Color(244, 225, 200));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_edit_25x25.png"))); // NOI18N
        btnEditar.setToolTipText("");
        btnEditar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnEditar.setContentAreaFilled(false);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnEditar.setMinimumSize(new java.awt.Dimension(33, 33));
        btnEditar.setOpaque(true);
        btnEditar.setPreferredSize(new java.awt.Dimension(33, 33));
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditarMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditarMouseEntered(evt);
            }
        });
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnDuplicar.setBackground(new java.awt.Color(244, 225, 200));
        btnDuplicar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_duplicar_25x25.png"))); // NOI18N
        btnDuplicar.setToolTipText("");
        btnDuplicar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnDuplicar.setContentAreaFilled(false);
        btnDuplicar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDuplicar.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnDuplicar.setMinimumSize(new java.awt.Dimension(33, 33));
        btnDuplicar.setOpaque(true);
        btnDuplicar.setPreferredSize(new java.awt.Dimension(33, 33));
        btnDuplicar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDuplicarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDuplicarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDuplicarMouseExited(evt);
            }
        });

        btnTomar.setBackground(new java.awt.Color(244, 225, 200));
        btnTomar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_tomar_25x25.png"))); // NOI18N
        btnTomar.setToolTipText("");
        btnTomar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnTomar.setContentAreaFilled(false);
        btnTomar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTomar.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnTomar.setMinimumSize(new java.awt.Dimension(33, 33));
        btnTomar.setOpaque(true);
        btnTomar.setPreferredSize(new java.awt.Dimension(33, 33));
        btnTomar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTomarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTomarMouseExited(evt);
            }
        });
        btnTomar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTomarActionPerformed(evt);
            }
        });

        btnImprimir.setBackground(new java.awt.Color(244, 225, 200));
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_imprimir_25x25.png"))); // NOI18N
        btnImprimir.setActionCommand("btnImprimir");
        btnImprimir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        btnImprimir.getAccessibleContext().setAccessibleName("btnImprimir");

        btnEliminar.setBackground(new java.awt.Color(244, 225, 200));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_borrar_25x25.png"))); // NOI18N
        btnEliminar.setToolTipText("");
        btnEliminar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.setMaximumSize(new java.awt.Dimension(2345, 33));
        btnEliminar.setMinimumSize(new java.awt.Dimension(33, 33));
        btnEliminar.setOpaque(true);
        btnEliminar.setPreferredSize(new java.awt.Dimension(33, 33));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEliminarMouseExited(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        pnlFondo.setImagen(GestorImagenes.crearImage("/frontend/imagenes/bg2.jpg"));
        pnlFondo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 0), 1, true));

        lblBarraTitulo.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblBarraTitulo.setForeground(new java.awt.Color(255, 102, 0));
        lblBarraTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarraTitulo.setText("Selector de diseños");

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
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_mensajes_cerrar_opcion.png"))); // NOI18N
        btnCerrar.setBorder(null);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout pnlFiltrosLayout = new javax.swing.GroupLayout(pnlFiltros);
        pnlFiltros.setLayout(pnlFiltrosLayout);
        pnlFiltrosLayout.setHorizontalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbInstitucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbCurso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlFiltrosLayout.setVerticalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(lblBarraTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCerrar))
                    .addComponent(pnlCentral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBarraTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout lpnCapasLayout = new javax.swing.GroupLayout(lpnCapas);
        lpnCapas.setLayout(lpnCapasLayout);
        lpnCapasLayout.setHorizontalGroup(
            lpnCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
            .addGroup(lpnCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lpnCapasLayout.setVerticalGroup(
            lpnCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
            .addGroup(lpnCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lpnCapas.setLayer(pnlFondo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lpnCapas)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lpnCapas, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void cmbInstitucionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInstitucionItemStateChanged
        if (ultimoComboActivo == evt.getSource()) {
            int intIndexSeleccionado = this.cmbInstitucion.getSelectedIndex();
            if (intIndexSeleccionado > 1) { //Seleccionó una institución.
                this.limpiarCursos("Todos");
                ArrayList<Curso> colCurso = this.getCursos((Institucion) this.cmbInstitucion.getSelectedItem());
                for (Curso curso : colCurso) {
                    this.cmbCurso.addItem(curso);
                }
                ArrayList<Institucion> colInstituciones = new ArrayList<>();
                colInstituciones.add((Institucion) this.cmbInstitucion.getSelectedItem());
                this.cargarArbol(colInstituciones);
            } else if (intIndexSeleccionado > 0) { //Selecciónó "Todas".
                this.limpiarCursos("Todos");
                this.cargarArbol(this.gestorImportarPregunta.getInstituciones());
            } else { //Seleccionó "No aplica".
                this.limpiarCursos("No aplica");
                this.cargarArbol();
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

    private void btnEditarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Editar examen.");
        this.repaint();
    }//GEN-LAST:event_btnEditarMouseEntered

    private void btnEditarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnEditarMouseExited

    private void btnDuplicarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDuplicarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnDuplicarMouseExited

    private void btnDuplicarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDuplicarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Duplicar examen.");
        this.repaint();
    }//GEN-LAST:event_btnDuplicarMouseEntered

    private void btnTomarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTomarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Tomar examen.");
        this.repaint();
    }//GEN-LAST:event_btnTomarMouseEntered

    private void btnTomarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTomarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnTomarMouseExited

    private void btnEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseEntered
        this.gestorEstados.setEstadoInstantaneo("Eliminar examen.");
        this.repaint();
    }//GEN-LAST:event_btnEliminarMouseEntered

    private void btnEliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseExited
        this.gestorEstados.volverAEstadoImportante();
        this.repaint();
    }//GEN-LAST:event_btnEliminarMouseExited

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed

        this.editarDiseño();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnTomarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTomarActionPerformed
        this.tomarDiseño();
    }//GEN-LAST:event_btnTomarActionPerformed

    private void btnDuplicarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDuplicarMouseClicked
        this.duplicarDiseño();
    }//GEN-LAST:event_btnDuplicarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        try {
            this.eliminarDiseño();
        } catch (Exception e) {
            Mensajes.mostrarError("Problemas con la base de datos. Imposible borrar diseño.");
        }
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        this.reportar();
    }//GEN-LAST:event_btnImprimirActionPerformed

    public GestorBarrasDeEstado getGestorEstados() {
        return gestorEstados;
    }
    
    /**
     * Funcionalidad para empezar a editar el diseño seleccionado luego de usar
     * el boton editar diseño examen.
     */
    private void editarDiseño() {
        try {
            PanelDiseño pnlNuevoExamen = new PanelDiseño(mPadre, examenSeleccionado, false);
            pnlNuevoExamen.setName("Edición Examen");
            pnlNuevoExamen.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de edición de examen!");
            mPadre.setTitle(examenSeleccionado.getStrNombre());
            this.dispose();
            mPadre.getPanelDeslizante().setPanelMostrado(pnlNuevoExamen);
            if (mPadre.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                mPadre.pack();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Mensajes.mostrarError("Debe seleccionar un diseño de examen.");
        }
    }

    /*
     *Funcionalidad para empezar a editar un diseño de examen seleccionado duplicado luego de usar en el boton duplicar diseño examen
     */
    private void duplicarDiseño() {
        try {
            PanelDiseño pnlNuevoExamen = new PanelDiseño(mPadre, examenSeleccionado, true);
            pnlNuevoExamen.setName("Edición Examen");
            pnlNuevoExamen.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de edición de examen!");
            mPadre.setTitle(examenSeleccionado.getStrNombre());
            this.dispose();
            mPadre.getPanelDeslizante().setPanelMostrado(pnlNuevoExamen);
            if (mPadre.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                mPadre.pack();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Mensajes.mostrarError("Debe seleccionar un diseño de examen.");
        }
    }
    /*
     * Metodo para eliminar un diseñó de examen seleccionado luego de hacer click en el boton eliminar
     */

    private void eliminarDiseño() throws SQLException {
        GestorDiseñoExamen gestor = new GestorDiseñoExamen(examenSeleccionado);
        gestor.setColPreguntas(gestor.getPreguntasDiseño());
        gestor.eliminarDiseño();
        this.cargarArbol();
        tree.repaint();
    }

    /**
     * Funcionalidad para empezar a tomar el diseño seleccionado.
     */
    private void tomarDiseño() {
        try {
            if (!new GestorDiseñoExamen().controlarPuntajesCompletos(examenSeleccionado)) {
                if (Mensajes.mostrarConfirmacion("Hay preguntas que no tienen un puntaje asociado. Debe cargar todos los datos para tomar el examen.\n\n¿Desea hacerlo ahora?")) {
                    PanelDiseño pnlNuevoExamen = new PanelDiseño(mPadre, examenSeleccionado, false);
                    pnlNuevoExamen.setName("Edición Examen");
                    pnlNuevoExamen.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de edición de examen!");
                    mPadre.setTitle(examenSeleccionado.getStrNombre());
                    mPadre.getPanelDeslizante().setPanelMostrado(pnlNuevoExamen);
                    if (mPadre.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                        mPadre.pack();
                    }
                    this.dispose();
                }
                return;
            }
            PanelTomaExamen pnlTomaExamen = new PanelTomaExamen(mPadre, examenSeleccionado);
            pnlTomaExamen.setName("Toma Examen");
            pnlTomaExamen.getGestorEstados().setNuevoEstadoImportante("¡Bienvenido a la interfaz de toma de examen!");
            mPadre.setTitle("Toma de examen - Paso 1 - " + examenSeleccionado.getStrNombre());
            this.dispose();
            mPadre.getPanelDeslizante().setPanelMostrado(pnlTomaExamen);
            if (mPadre.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                mPadre.pack();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Mensajes.mostrarError("Debe seleccionar un diseño de examen.");
        }
    }
    
    private void reportar(){
        //
        //
        //
        //ATENCION,
        //
        //Que mierda pasa cuando se llenan las preguntas en el examen seleccionado.
        //
        //Corregir.
        //
        //
        
        PanelDiseño pnlNuevoExamen = new PanelDiseño(mPadre, examenSeleccionado, true);
        GestorGenerarReporteDiseñoExamen gestorGenerarReporte = new GestorGenerarReporteDiseñoExamen(examenSeleccionado);
        this.dispose();//Conviene poner dispose aca?, ver eso despues.
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDuplicar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnTomar;
    private javax.swing.JComboBox cmbCurso;
    private javax.swing.JComboBox cmbInstitucion;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblBarraTitulo;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JLabel lblNombreInstitucion;
    private javax.swing.JLayeredPane lpnCapas;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlCentral;
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
     */
    private void cargarArbol() {
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(" Exámenes diseñados");
        for (Institucion institucion : this.gestorImportarPregunta.getInstituciones()) {
            DefaultMutableTreeNode nodoInstitucion = new DefaultMutableTreeNode(institucion);
            for (Curso curso : this.gestorImportarPregunta.getCursos(institucion)) {
                DefaultMutableTreeNode nodoCurso = new DefaultMutableTreeNode(curso);
                for (DiseñoExamen diseño : this.gestorImportarPregunta.getDiseñoExamen(curso)) {
                    nodoCurso.add(new DefaultMutableTreeNode(diseño));
                }
                nodoInstitucion.add(nodoCurso);
            }
            nodoPadre.add(nodoInstitucion);
        }
        for (DiseñoExamen sinCurso : this.gestorImportarPregunta.getDiseñosExamenesSinCurso()) {
            nodoPadre.add(new DefaultMutableTreeNode(sinCurso));
        }
        tree.setModel(new DefaultTreeModel(nodoPadre));
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    /**
     * Carga la jerarquía del árbol desde la base de datos.
     *
     * @param instituciones instituciones para la cuales se recuperarán
     * exámenes.
     */
    private void cargarArbol(ArrayList<Institucion> instituciones) {
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(" Exámenes diseñados");
        for (Institucion institucion : instituciones) {
            DefaultMutableTreeNode nodoInstitucion = new DefaultMutableTreeNode(institucion);
            for (Curso curso : this.gestorImportarPregunta.getCursos(institucion)) {
                DefaultMutableTreeNode nodoCurso = new DefaultMutableTreeNode(curso);
                for (DiseñoExamen diseño : this.gestorImportarPregunta.getDiseñoExamen(curso)) {
                    nodoCurso.add(new DefaultMutableTreeNode(diseño));
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
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(" Exámenes diseñados");
        DefaultMutableTreeNode nodoInstitucion = new DefaultMutableTreeNode((Institucion) this.cmbInstitucion.getSelectedItem());
        DefaultMutableTreeNode nodoCurso = new DefaultMutableTreeNode(curso);
        for (DiseñoExamen diseño : this.gestorImportarPregunta.getDiseñoExamen(curso)) {
            nodoCurso.add(new DefaultMutableTreeNode(diseño));
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
    private void limpiarCursos(String strDescripcionPrimerItem) {
        this.cmbCurso.removeAllItems();
        this.cmbCurso.addItem(strDescripcionPrimerItem);
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
        GestorImportarPregunta gestPregunta = new GestorImportarPregunta();
        return gestPregunta.getCursos(institucion);
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
