package frontend.diseños;

import backend.diseños.Curso;
import backend.diseños.DiseñoExamen;
import backend.diseños.Filtro;
import backend.diseños.Institucion;
import backend.diseños.Pregunta;
import backend.gestores.GestorDiseñoExamen;
import backend.gestores.GestorImportarPregunta;
import frontend.auxiliares.CeldaBooleanaRendererEntropy;
import frontend.auxiliares.CeldaListaRendererEntropy;
import frontend.auxiliares.CeldaMultiLineaRendererEntropy;
import frontend.auxiliares.GestorBarrasDeEstado;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.auxiliares.TextAreaTags;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa la interfaz para la importación de preguntas de la BD.
 *
 * @author Denise, Gaston Noves
 */
public class DialogImportarPregunta extends javax.swing.JDialog {

    private final PanelContenidoExamen mPadre;
    private final GestorBarrasDeEstado gestorEstado;
    private ArrayList<Pregunta> colPregunta;
    private Object ultimoComboActivo;
    private final GestorDiseñoExamen gestorDiseño;
    private final GestorImportarPregunta gestorImportarPreguntas;

    /**
     * Constructor de la clase.
     *
     * @param parent panel padre de la interfaz
     * @param modal true si mantiene el foco, false de lo contrario
     * @param gestorDiseño
     */
    public DialogImportarPregunta(PanelContenidoExamen parent, boolean modal, GestorDiseñoExamen gestorDiseño) {
        //Mantener las siguientes dos línea antes del constructor.
        super(parent.getPanelExamenPadre().getVentanaPrincipal(), modal);
        this.gestorDiseño = gestorDiseño;
        this.gestorImportarPreguntas = new GestorImportarPregunta();
        mPadre = parent;
        initComponents();
        this.setLocationRelativeTo(parent);
        this.cmbInstitucion.setRenderer(new CeldaListaRendererEntropy());
        this.cmbCurso.setRenderer(new CeldaListaRendererEntropy());
        this.cmbExamen.setRenderer(new CeldaListaRendererEntropy());
        this.gestorEstado = new GestorBarrasDeEstado(lblActualizacionEstado, lblIconoEstado);
        this.gestorEstado.setNuevoEstadoImportante("Bienvenido al asistente para importar preguntas desde la base de datos.");
        tblPreguntas.getColumn("Importar").setMaxWidth(70);
        tblPreguntas.getTableHeader().setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        tblPreguntas.setDefaultRenderer(String.class, new CeldaMultiLineaRendererEntropy(tblPreguntas, false));
        tblPreguntas.setDefaultRenderer(Boolean.class, new CeldaBooleanaRendererEntropy());

        //Define la columna de idPregunta en la tabla como invisible.
        tblPreguntas.getColumnModel().getColumn(0).setMaxWidth(0);
        tblPreguntas.getColumnModel().getColumn(0).setMinWidth(0);
        tblPreguntas.getColumnModel().getColumn(0).setPreferredWidth(0);

        //Seteo de combos de filtros.
        ArrayList<Institucion> colInstitucion = gestorImportarPreguntas.getInstituciones();
        this.cmbInstitucion.addItem("No aplica");
        this.cmbInstitucion.addItem("Ninguna");
        this.cmbInstitucion.addItem("Todas");
        for (Institucion institucion : colInstitucion) {
            this.cmbInstitucion.addItem(institucion);
        }
        this.cmbInstitucion.setSelectedIndex(0);
        this.cmbCurso.addItem("No aplica");
        this.cmbCurso.setSelectedIndex(0);
        this.cmbExamen.addItem("Todos");
        this.cmbExamen.setSelectedIndex(0);
        for (DiseñoExamen examen : gestorImportarPreguntas.getDiseñosExamenes()) {
            if (!examen.equals(gestorDiseño.getDiseñoExamen())) {
                cmbExamen.addItem(examen);
            }
        }

    }

    /**
     * Metodo para cargar la tabla de preguntas. Segun las preguntas que recibe
     * las recorre y escribe su tipo.
     *
     * @param colPregunta recibe las preguntas que debe cargar.
     *
     */
    private void cargarTblPregunta(ArrayList<Pregunta> colPregunta) {
        DefaultTableModel tblMod = (DefaultTableModel) this.tblPreguntas.getModel();

        for (Pregunta pregunta : colPregunta) {
            String tipo = "";
            switch (pregunta.getIntTipo()) {
                case 1:
                    tipo = "Desarrollar";
                    break;
                case 2:
                    tipo = "Múltiple Opción";
                    break;
                case 3:
                    tipo = "Verdadero/Falso";
                    break;
                case 4:
                    tipo = "Relación de columnas";
                    break;
                case 5:
                    tipo = "Numérica";
                    break;
            }

            Object rowData[] = {pregunta.getIntPreguntaId(), false, pregunta.getStrEnunciado(), pregunta.getStrNivel(), tipo};
            tblMod.addRow(rowData);

        }

    }

    /**
     * Tomas las preguntas seleccionadas de la tabla, recorriendola y
     * verificando cual se selecciono y agrega su id a una coleccion.
     *
     * @return Coleccion de ids de las preguntas seleccionadas.
     */
    private ArrayList<Integer> getIDPreguntasSeleccionadas() {
        ArrayList<Integer> colIDPregunta = new ArrayList<>();
        int rows = this.tblPreguntas.getRowCount();
        for (int i = 0; i < rows; i++) {
            boolean selec = (Boolean) this.tblPreguntas.getValueAt(i, 1);
            if (selec) {
                colIDPregunta.add((Integer) this.tblPreguntas.getValueAt(i, 0));
            }
        }
        return colIDPregunta;
    }

    /**
     * Genera una coleccion de cadenas que son los Tags por los que se quiere
     * filtar.
     *
     * @return Coleccion de Strings que son los tags escritos.
     */
    private ArrayList<String> getTagsFiltro() {
        ArrayList<String> colTags = new ArrayList<>();

        if (!txaTags.getText().trim().isEmpty()) {
            String[] tags = txaTags.getText().split(",");
            colTags = new ArrayList<>();

            for (String tag : tags) {
                tag = tag.trim();
                colTags.add(tag);
            }
        }

        return colTags;
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
     * Se comunica con el gestor de importacion para buscar todos los Diseños de
     * Examen que pertenecen a un curso.
     *
     * @param curso el curso por el cual se quiere filtrar.
     * @return Coleccion de Diseños de examen que pertenesen a un curso.
     */
    private ArrayList<DiseñoExamen> getDiseñoExamen(Curso curso) {
        GestorImportarPregunta gestPregunta = new GestorImportarPregunta();
        return gestPregunta.getDiseñoExamen(curso);
    }

    /**
     * Se comunica con el gestor de importacion para buscar todas las preguntas
     * que pertenecen a un Diseño de Examen.
     *
     * @param diseñoExamen por el cual se quiere filtrar.
     * @return Coleccion de todas las preguntas que pertenecen a un Diseño de
     * Examen.
     */
    private ArrayList<Pregunta> getPreguntaPorDE(DiseñoExamen diseñoExamen) {
        GestorImportarPregunta gestPregunta = new GestorImportarPregunta();
        return gestPregunta.getPreguntasPorDE(diseñoExamen);
    }

    /**
     * Se comunica con el gestor de importacion para buscar todas las preguntas
     * que estan formadas por un id de pregunta de una coleccion.
     *
     * @param colID coleccion de ids de preguntas que quiero recuperar.
     * @return Coleccion de preguntas que tienen los ids pasados.
     */
    private ArrayList<Pregunta> getPreguntaPorID(ArrayList<Integer> colID) {
        GestorImportarPregunta gestPregunta = new GestorImportarPregunta();
        return gestPregunta.getPreguntasPorId(colID);
    }

    /**
     * Se comunica con el gestor de importacion para buscar todas las preguntas
     * de la bd.
     *
     * @return Coleccion de todas las preguntas de la bd.
     */
    private ArrayList<Pregunta> buscarTodasLasPreguntas() {
        GestorImportarPregunta gestImportar = new GestorImportarPregunta();
        return gestImportar.buscarTodasLasPreguntas();
    }

    /**
     * Se comunica con el getor para buscar todas las preguntas de una
     * determinada institucion.
     *
     * @param institucionSeleccionada institucion sobre la cual se realiza la
     * busqueda.
     * @return Coleccion de pregunts que pertenecen a una determinada
     * institucion.
     */
    private ArrayList<Pregunta> buscarTodasLasPreguntasPorInstitucion(Institucion institucionSeleccionada) {
        GestorImportarPregunta gestImportar = new GestorImportarPregunta();
        return gestImportar.buscarPreguntasPorInstitucion(institucionSeleccionada);
    }

    /**
     * Se comunica con el getor para buscar todas las preguntas de un
     * tererminado curso.
     *
     * @param cursoSeleccionado curso sobre el cual se realiza la busqueda.
     * @return Coleccion de preguntas que pertenecen a un determinado curso.
     */
    private ArrayList<Pregunta> buscarTodasLasPreguntasPorCurso(Curso cursoSeleccionado) {
        GestorImportarPregunta gestImportar = new GestorImportarPregunta();
        return gestImportar.buscarPreguntasPorCurso(cursoSeleccionado);
    }

    /**
     * Se comunica con el getor para buscar todas las preguntas que cumplen con
     * un vector de filtros.
     *
     * @param colFiltro coleccion de filtros sobre los cuales se realizaran la
     * busqueda.
     * @return Coleccion de preguntas que cumplen con los filtros pasados por
     * parametros
     */
    private ArrayList<Pregunta> getPreguntaPorFiltro(ArrayList<Filtro> colFiltro) {
        GestorImportarPregunta gestImportar = new GestorImportarPregunta();
        return gestImportar.getPreguntaPorFiltro(colFiltro);
    }

    /**
     * Vacia la tabla de los datos que pueda tener cargados.
     *
     */
    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) this.tblPreguntas.getModel();
        int cantFilas = modelo.getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            modelo.removeRow(0);
        }
    }

    /**
     * Construve un conjunto de filtros y fallida la situacion en la que debe
     * usar cada uno para obtener las preguntas necesarias en casa caso.
     *
     * @return Coleccion de preguntas que seran cargadas en la tabla de
     * preguntas.
     */
    private ArrayList<Pregunta> buscarPreguntas() {
        ArrayList<Filtro> colFiltro = new ArrayList<>();
        ArrayList<Pregunta> colResultado = new ArrayList<>();
        
        //Construye los filtros definidos en el area de texto de Tags.
        for (String strFiltro : this.getTagsFiltro()) {
            Filtro filtro = new Filtro(Filtro.TipoFiltro.TAG, strFiltro);
            colFiltro.add(filtro);
        }
        
        if (this.cmbExamen.getSelectedIndex() > 0) { //Seleccionó un examen.
            DiseñoExamen diseñoExamenSeleccionado = (DiseñoExamen) this.cmbExamen.getSelectedItem();
            Filtro filtroDiseñoExamen = new Filtro(Filtro.TipoFiltro.DISEÑOEXAMEN, diseñoExamenSeleccionado);
            colFiltro.add(filtroDiseñoExamen);
        } else if (this.cmbCurso.getSelectedIndex() > 0){ // Seleccionó un curso.
            Curso cursoSeleccionado = (Curso) this.cmbCurso.getSelectedItem();
            Filtro filtroCurso = new Filtro(Filtro.TipoFiltro.CURSO, cursoSeleccionado);
            colFiltro.add(filtroCurso);
        } else if (this.cmbInstitucion.getSelectedIndex() > 2){ //Seleccionó una institución.
            Institucion institucionSeleccionada = (Institucion) this.cmbInstitucion.getSelectedItem();
            Filtro filtroInstitucion = new Filtro(Filtro.TipoFiltro.INSTITUCION, institucionSeleccionada);
            colFiltro.add(filtroInstitucion);
        } else if (this.cmbInstitucion.getSelectedIndex() > 1){ //Seleccionó "Todas" las instituciones.
            Filtro filtroInstitucionNoNula = new Filtro(Filtro.TipoFiltro.CON_CURSO);
            colFiltro.add(filtroInstitucionNoNula);
        } else if (this.cmbInstitucion.getSelectedIndex() > 0){ //Seleccionó "Ninguna" institución.
            Filtro filtroInstitucionNula = new Filtro(Filtro.TipoFiltro.SIN_CURSO);
            colFiltro.add(filtroInstitucionNula);
        } else if (colFiltro.isEmpty()) {
            this.gestorEstado.setNuevoEstadoImportante("Debe ingresar al menos un filtro.", GestorBarrasDeEstado.TipoEstado.ADVERTENCIA);
            return colResultado;
        }
        
        colResultado = this.getPreguntaPorFiltro(colFiltro);
        
        if (!colResultado.isEmpty()) {
            this.gestorEstado.setNuevoEstadoImportante("Búsqueda exitosa.");
        } else {
            this.gestorEstado.setNuevoEstadoImportante("Búsqueda sin resultados.");
        }
        
        return colResultado;
    }
    
    
    
    private void seleccionarDeseleccionar(boolean valor)
    {
        DefaultTableModel modelo = (DefaultTableModel) this.tblPreguntas.getModel();
        int cantFilas = modelo.getRowCount();
        for (int i = 0; i < cantFilas; i++) {
            this.tblPreguntas.getModel().setValueAt(valor, i, 1);
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

        pnlEstado = new javax.swing.JPanel();
        lblActualizacionEstado = new javax.swing.JLabel();
        lblIconoEstado = new javax.swing.JLabel();
        pnlFiltros = new javax.swing.JPanel();
        lblNombreInstitucion = new javax.swing.JLabel();
        cmbInstitucion = new javax.swing.JComboBox();
        lblCurso = new javax.swing.JLabel();
        cmbCurso = new javax.swing.JComboBox();
        lblNombreExamen = new javax.swing.JLabel();
        cmbExamen = new javax.swing.JComboBox();
        lblTags = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        scrTags = new javax.swing.JScrollPane();
        txaTags = new TextAreaTags(this, this, mPadre.getPanelExamenPadre().getGestorDiseñoExamen().getGestorTags());
        pnlResultado = new javax.swing.JPanel();
        scrOpciones = new javax.swing.JScrollPane();
        tblPreguntas = new javax.swing.JTable();
        cbSeleccionarDeseleccionarTodasLasPreguntas = new javax.swing.JCheckBox();
        btnImportar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Importar pregunta");

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
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIconoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlEstadoLayout.setVerticalGroup(
            pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblIconoEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addComponent(lblActualizacionEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar por", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        pnlFiltros.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        lblNombreInstitucion.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblNombreInstitucion.setText("Institución:");

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

        lblNombreExamen.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblNombreExamen.setText("Título:");

        cmbExamen.setBackground(new java.awt.Color(255, 204, 102));
        cmbExamen.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbExamen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbExamenFocusGained(evt);
            }
        });

        lblTags.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblTags.setText("Tags:");

        btnBuscar.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_vista_previa.png"))); // NOI18N
        btnBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setIconTextGap(10);
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        txaTags.setTextoPorDefecto("Ingrese Tags");
        txaTags.mostrarTextoPorDefecto();
        txaTags.setColumns(20);
        txaTags.setRows(2);
        scrTags.setViewportView(txaTags);

        javax.swing.GroupLayout pnlFiltrosLayout = new javax.swing.GroupLayout(pnlFiltros);
        pnlFiltros.setLayout(pnlFiltrosLayout);
        pnlFiltrosLayout.setHorizontalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombreExamen, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cmbExamen, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbCurso, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbInstitucion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTags, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrTags, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        pnlFiltrosLayout.setVerticalGroup(
            pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltrosLayout.createSequentialGroup()
                .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(scrTags)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFiltrosLayout.createSequentialGroup()
                        .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTags, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbExamen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreExamen, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnlResultado.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultado de búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        pnlResultado.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        tblPreguntas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblPreguntas.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        tblPreguntas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idPregunta", "Importar", "Enunciado", "Nivel", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPreguntas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPreguntas.getTableHeader().setReorderingAllowed(false);
        scrOpciones.setViewportView(tblPreguntas);
        if (tblPreguntas.getColumnModel().getColumnCount() > 0) {
            tblPreguntas.getColumnModel().getColumn(0).setResizable(false);
        }

        cbSeleccionarDeseleccionarTodasLasPreguntas.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        cbSeleccionarDeseleccionarTodasLasPreguntas.setText("Seleccionar todas/Deseleccionar todas");
        cbSeleccionarDeseleccionarTodasLasPreguntas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSeleccionarDeseleccionarTodasLasPreguntasItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlResultadoLayout = new javax.swing.GroupLayout(pnlResultado);
        pnlResultado.setLayout(pnlResultadoLayout);
        pnlResultadoLayout.setHorizontalGroup(
            pnlResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
            .addGroup(pnlResultadoLayout.createSequentialGroup()
                .addComponent(cbSeleccionarDeseleccionarTodasLasPreguntas)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlResultadoLayout.setVerticalGroup(
            pnlResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlResultadoLayout.createSequentialGroup()
                .addComponent(scrOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSeleccionarDeseleccionarTodasLasPreguntas, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnImportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_nuevo_25x25.png"))); // NOI18N
        btnImportar.setToolTipText("");
        btnImportar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnImportar.setContentAreaFilled(false);
        btnImportar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImportar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnImportarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnImportarMouseExited(evt);
            }
        });
        btnImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnlEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnImportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        this.gestorEstado.setEstadoInstantaneo("Comenzar la búsqueda en la base de datos.");
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        this.gestorEstado.volverAEstadoImportante();
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnImportarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportarMouseEntered
        this.gestorEstado.setEstadoInstantaneo("Importar preguntas marcadas al examen en edición.");
    }//GEN-LAST:event_btnImportarMouseEntered

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.limpiarTabla();
        this.cargarTblPregunta(this.buscarPreguntas());
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnImportarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportarMouseExited
        this.gestorEstado.volverAEstadoImportante();
    }//GEN-LAST:event_btnImportarMouseExited

    private void cmbInstitucionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInstitucionItemStateChanged
        if (ultimoComboActivo == evt.getSource()) {
            int intIndexSeleccionado = this.cmbInstitucion.getSelectedIndex();
            this.limpiarExamenes();
            if (intIndexSeleccionado > 2) { //Seleccionó una institución.
                this.limpiarCursos("Todos");
                ArrayList<Curso> colCurso = this.getCursos((Institucion) this.cmbInstitucion.getSelectedItem());
                for (Curso curso : colCurso) {
                    this.cmbCurso.addItem(curso);
                }
            } else if (intIndexSeleccionado > 1) { //Selecciónó "Todas".
                this.limpiarCursos("Todos");
            } else if (intIndexSeleccionado > 0) { //Selecciónó "Ninguna".
                this.limpiarCursos("No aplica");
                for (DiseñoExamen examen : gestorImportarPreguntas.getDiseñosExamenesSinCurso()) {
                    if (!examen.equals(gestorDiseño.getDiseñoExamen())) {
                        cmbExamen.addItem(examen);
                    }
                }
            } else { //Seleccionó "No aplica".
                this.limpiarCursos("No aplica");
                for (DiseñoExamen examen : gestorImportarPreguntas.getDiseñosExamenes()) {
                    if (!examen.equals(gestorDiseño.getDiseñoExamen())) {
                        cmbExamen.addItem(examen);
                    }
                }
            }
        }
    }//GEN-LAST:event_cmbInstitucionItemStateChanged

    private void cmbCursoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCursoItemStateChanged
        if (ultimoComboActivo == evt.getSource()) {
            this.limpiarExamenes();
            if (this.cmbCurso.getSelectedIndex() > 0) { //Seleccionó un curso.
                ArrayList<DiseñoExamen> colDiseñoExamen = this.getDiseñoExamen((Curso) this.cmbCurso.getSelectedItem());
                for (DiseñoExamen diseñoExamen : colDiseñoExamen) {
                    this.cmbExamen.addItem(diseñoExamen);
                }
            }
        }
    }//GEN-LAST:event_cmbCursoItemStateChanged

    private void btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarActionPerformed
        ArrayList<Integer> colId;
        colId = this.getIDPreguntasSeleccionadas();
        colPregunta = this.getPreguntaPorID(colId);
        this.mPadre.importarPreguntas(colPregunta);
        this.dispose();
    }//GEN-LAST:event_btnImportarActionPerformed

    private void cmbInstitucionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbInstitucionFocusGained
        this.ultimoComboActivo =  evt.getComponent();
    }//GEN-LAST:event_cmbInstitucionFocusGained

    private void cmbCursoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbCursoFocusGained
        this.ultimoComboActivo = evt.getComponent();
    }//GEN-LAST:event_cmbCursoFocusGained

    private void cmbExamenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbExamenFocusGained
        this.ultimoComboActivo = evt.getComponent();
    }//GEN-LAST:event_cmbExamenFocusGained

    private void cbSeleccionarDeseleccionarTodasLasPreguntasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSeleccionarDeseleccionarTodasLasPreguntasItemStateChanged
        if(cbSeleccionarDeseleccionarTodasLasPreguntas.isSelected()){
            seleccionarDeseleccionar(true);
        }
        else{
            seleccionarDeseleccionar(false);
        }  
    }//GEN-LAST:event_cbSeleccionarDeseleccionarTodasLasPreguntasItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnImportar;
    private javax.swing.JCheckBox cbSeleccionarDeseleccionarTodasLasPreguntas;
    private javax.swing.JComboBox cmbCurso;
    private javax.swing.JComboBox cmbExamen;
    private javax.swing.JComboBox cmbInstitucion;
    private javax.swing.JLabel lblActualizacionEstado;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblIconoEstado;
    private javax.swing.JLabel lblNombreExamen;
    private javax.swing.JLabel lblNombreInstitucion;
    private javax.swing.JLabel lblTags;
    private javax.swing.JPanel pnlEstado;
    private javax.swing.JPanel pnlFiltros;
    private javax.swing.JPanel pnlResultado;
    private javax.swing.JScrollPane scrOpciones;
    private javax.swing.JScrollPane scrTags;
    private javax.swing.JTable tblPreguntas;
    private frontend.auxiliares.TextAreaEntropy txaTags;
    // End of variables declaration//GEN-END:variables

    /**
     * Setea el combo box de cursos a sus valores iniciales.
     */
    private void limpiarCursos(String strDescripcionPrimerItem) {
        this.cmbCurso.removeAllItems();
        this.cmbCurso.addItem(strDescripcionPrimerItem);
        this.cmbCurso.setSelectedIndex(0);
    }
    /**
     * Setea el combo box de instituciones a sus valores iniciales.
     */
    private void limpiarExamenes() {
        this.cmbExamen.removeAllItems();
        this.cmbExamen.addItem("Todos");
        this.cmbExamen.setSelectedIndex(0);
    }

}
