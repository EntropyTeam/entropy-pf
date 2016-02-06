package frontend.estadisticas;

import frontend.alumnos.DialogPerfilCompleto;
import frontend.auxiliares.TabbedPaneEntropy;
import frontend.inicio.VentanaPrincipal;
import backend.Presentacion.Presentacion;
import backend.dao.diseños.DAOCurso;
import backend.diseños.Curso;
import backend.examenes.Examen;
import backend.gestores.GestorExamen;
import backend.gestores.GestorHistorialAlumno;
import backend.reporte.GestorGenerarReporteResolucion;
import backend.reporte.GestorGraficosAlumno;
import backend.reporte.GestorGraficosExamen;
import backend.resoluciones.Resolucion;
import backend.usuarios.Alumno;
import frontend.alumnos.DialogSelectorAlumno;
import frontend.auxiliares.CeldaMultiLineaRendererEntropy;
import frontend.auxiliares.LookAndFeelEntropy;
import frontend.resoluciones.PanelResoluciones;
import java.awt.Desktop;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Denise
 */
public class PanelEstadisticasAlumno extends javax.swing.JPanel {

    private final Alumno alumno;
    private final GestorHistorialAlumno gestorHistorial;
    private GestorGraficosExamen gestorGraficos;
    private ArrayList<Resolucion> colResoluciones;
    private final int ancho = 400;
    private final int alto = 400;
    private ArrayList<Presentacion> presentaciones;
    private final Object pnlPadre;

    /**
     * Creates new form PanelEstadisticasAlumno
     *
     * @param pnlPadre
     * @param alumno cuyas estadísticas deben mostrarse.
     */
    public PanelEstadisticasAlumno(Object pnlPadre, Alumno alumno) {
        initComponents();
        this.pnlPadre = pnlPadre;
        this.tbpSolapas.setUI(new TabbedPaneEntropy());
        this.alumno = alumno;
        this.lblAlumno.setText(alumno.getStrApellido() + ", " + alumno.getStrNombre());
        this.lblLegajo.setText((alumno.getStrLegajo() != null && !alumno.getStrLegajo().isEmpty()) ? alumno.getStrLegajo() : "---");
        gestorHistorial = new GestorHistorialAlumno();
        // Formato de tablas
        tblExamenesRendidos.getTableHeader().setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        tblExamenesRendidos.setDefaultRenderer(Object.class, new CeldaMultiLineaRendererEntropy(tblExamenesRendidos, false));
        tblClasesAsistidas.getTableHeader().setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        tblClasesAsistidas.setDefaultRenderer(Object.class, new CeldaMultiLineaRendererEntropy(tblClasesAsistidas, false));

        cargarExamenesRendidos();
        cargarClasesAsisitidas();
        ocultarColumnas();
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                generarGrafico();
            }
        });
        
    }

    private void cargarExamenesRendidos() {
        colResoluciones = gestorHistorial.getResoluciones(alumno.getIntAlumnoId());
        DefaultTableModel modeloTabla = (DefaultTableModel) tblExamenesRendidos.getModel();
        for (int i = 0; i < colResoluciones.size(); i++) {
            if (colResoluciones.get(i).getExamen()!= null) {
                modeloTabla.addRow(new Vector());
                Examen examen = gestorHistorial.getExamen(colResoluciones.get(i).getExamen().getIntExamenId());
                colResoluciones.get(i).setExamen(examen);
                modeloTabla.setValueAt(examen.getStrNombre(), i, 0);
                modeloTabla.setValueAt(String.format("%.2f", colResoluciones.get(i).getPorcentajeAprobacion()) + "%", i, 1);
                Resolucion resolucion = colResoluciones.get(i);
                String estado = "DESAPROBADO";
                if (!resolucion.esCorreccionCompleta()) {
                    estado = "FALTA CORREGIR";
                } else {
                    try {
                        if (resolucion.estaAprobada()){
                            estado = "APROBADO";
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PanelEstadisticasAlumno.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                modeloTabla.setValueAt(estado, i, 2);
                modeloTabla.setValueAt(new SimpleDateFormat("dd-MM-yyyy").format(examen.getDteFecha()), i, 3);
                modeloTabla.setValueAt(examen.getCurso().getStrNombre(), i, 4);
                modeloTabla.setValueAt(examen.getCurso().getInstitucion().getStrNombre(), i, 5);                
                modeloTabla.setValueAt(colResoluciones.get(i), i, 6);
            }
        }
        ocultarColumnas();
        tblExamenesRendidos.setModel(modeloTabla);
        gestorGraficos = new GestorGraficosExamen(colResoluciones);
        this.lblRendidos.setText(colResoluciones.size()+"");
        int corregidos = getCantidadCorregidos(colResoluciones);
        this.lblCorregidos.setText(corregidos+"");
        int aprobados = getCantidadAprobados(colResoluciones);
        this.lblAprobados.setText(aprobados+"/"+corregidos);
        this.lblPorcentajeAprobados.setText(String.format("%.2f", (double) aprobados * 100 / corregidos)+"%");
        double mayor = 0;
        double menor = 0;
        double suma = 0;
        for (Resolucion resolucion : colResoluciones) {
            if (resolucion.esCorreccionCompleta()){
                double calificacion = resolucion.getPorcentajeAprobacion();
                if (calificacion > mayor) mayor = calificacion;
                if (menor == 0 || calificacion < menor) menor = calificacion;
                suma += calificacion;
            }
        }        
        this.lblNotaMayor.setText(String.format("%.2f", mayor)+"%");
        this.lblNotaMenor.setText(String.format("%.2f", menor)+"%");
        this.lblNotaPromedio.setText(String.format("%.2f", suma/corregidos)+"%");
    }

    private void cargarClasesAsisitidas() {
        presentaciones = gestorHistorial.getAsistencias(alumno.getIntAlumnoId());
        DefaultTableModel modeloTabla = (DefaultTableModel) tblClasesAsistidas.getModel();
        for (int i = 0; i < presentaciones.size(); i++) {
            modeloTabla.addRow(new Vector());
            modeloTabla.setValueAt(presentaciones.get(i).getStrNombre(), i, 0);
            modeloTabla.setValueAt(presentaciones.get(i).getStrDescripcion(), i, 1);
            modeloTabla.setValueAt(new SimpleDateFormat("dd-MM-yyyy").format(presentaciones.get(i).getDteFecha()), i, 2);
            modeloTabla.setValueAt(presentaciones.get(i), i, 5);
            //Denise dice: Lo que sigue es horrible, pero el modelo está mal hecho y no tengo ganas de cambiarlo. Fue.
            Curso curso = new DAOCurso().recuperarCurso(presentaciones.get(i).getIntIdCurso());
            modeloTabla.setValueAt(curso.getStrNombre(), i, 3);
            modeloTabla.setValueAt(curso.getInstitucion().getStrNombre(), i, 4);            
        }
        tblClasesAsistidas.setModel(modeloTabla);        
    }

    private void ocultarColumnas() {
        tblClasesAsistidas.getColumnModel().getColumn(5).setMaxWidth(0);
        tblClasesAsistidas.getColumnModel().getColumn(5).setMinWidth(0);
        tblClasesAsistidas.getColumnModel().getColumn(5).setPreferredWidth(0);
        tblExamenesRendidos.getColumnModel().getColumn(6).setMaxWidth(0);
        tblExamenesRendidos.getColumnModel().getColumn(6).setMinWidth(0);
        tblExamenesRendidos.getColumnModel().getColumn(6).setPreferredWidth(0);
    }

    private void generarGrafico() {
        int ancho = lblResultados.getSize().width;
        int alto = lblResultados.getSize().height;
        this.lblResultados.setIcon(new ImageIcon(gestorGraficos.generarGraficoBarrasResoluciones(
                false,
                true,
                (ancho < 200) ? this.ancho: ancho,
                (alto < 200) ? this.alto: alto)));        
        ancho = lblNotas.getSize().width;
        alto = lblNotas.getSize().height;
        this.lblNotas.setIcon(new ImageIcon(gestorGraficos.generarGraficoLinealResoluciones(
                false,
                true,
                (ancho < 200) ? this.ancho: ancho,
                (alto < 200) ? this.alto: alto)));
        ancho = lblAsistencias.getSize().width;
        alto = lblAsistencias.getSize().height;
        this.lblAsistencias.setIcon(new ImageIcon(new GestorGraficosAlumno().generarGraficoTimelineAsistencias(
                presentaciones,
                true,
                (ancho < 200) ? this.ancho: ancho,
                (alto < 200) ? this.alto: alto)));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSuperior = new javax.swing.JPanel();
        pnlBotones = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnVolverInicio = new javax.swing.JButton();
        pnlDatosExamen = new javax.swing.JPanel();
        upperSeparator = new javax.swing.JSeparator();
        lowerSeparator = new javax.swing.JSeparator();
        lblsAlumno = new javax.swing.JLabel();
        lblAlumno = new javax.swing.JLabel();
        lblsLegajo = new javax.swing.JLabel();
        lblLegajo = new javax.swing.JLabel();
        lblPerfilCompleto = new javax.swing.JLabel();
        tbpSolapas = new javax.swing.JTabbedPane();
        pnlGeneral = new javax.swing.JPanel();
        lblsRendidos = new javax.swing.JLabel();
        lblRendidos = new javax.swing.JLabel();
        lblsAprobados = new javax.swing.JLabel();
        lblAprobados = new javax.swing.JLabel();
        lblsCorregidos = new javax.swing.JLabel();
        lblCorregidos = new javax.swing.JLabel();
        lblsPorcentajeAprobados = new javax.swing.JLabel();
        lblPorcentajeAprobados = new javax.swing.JLabel();
        lblsNotaPromedio = new javax.swing.JLabel();
        lblNotaPromedio = new javax.swing.JLabel();
        lblsNotaMayor = new javax.swing.JLabel();
        lblNotaMayor = new javax.swing.JLabel();
        lblsNotaMenor = new javax.swing.JLabel();
        lblNotaMenor = new javax.swing.JLabel();
        lblMasEstadisticas = new javax.swing.JLabel();
        pnlResultados = new javax.swing.JPanel();
        lblResultados = new javax.swing.JLabel();
        pnlNotas = new javax.swing.JPanel();
        lblNotas = new javax.swing.JLabel();
        pnlExamenes = new javax.swing.JPanel();
        scrExamenesRendidos = new javax.swing.JScrollPane();
        tblExamenesRendidos = new javax.swing.JTable();
        lblInfoExamenes = new javax.swing.JLabel();
        pnlAsistencias = new javax.swing.JPanel();
        lblAsistencias = new javax.swing.JLabel();
        pnlClases = new javax.swing.JPanel();
        scrClasesAsistidas = new javax.swing.JScrollPane();
        tblClasesAsistidas = new javax.swing.JTable();

        pnlBotones.setLayout(new java.awt.GridLayout());

        btnVolver.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_volver.png"))); // NOI18N
        btnVolver.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolver.setIconTextGap(10);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVolver);

        btnVolverInicio.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnVolverInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_inicio.png"))); // NOI18N
        btnVolverInicio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnVolverInicio.setContentAreaFilled(false);
        btnVolverInicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolverInicio.setIconTextGap(10);
        btnVolverInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverInicioActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVolverInicio);

        javax.swing.GroupLayout pnlSuperiorLayout = new javax.swing.GroupLayout(pnlSuperior);
        pnlSuperior.setLayout(pnlSuperiorLayout);
        pnlSuperiorLayout.setHorizontalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE))
        );
        pnlSuperiorLayout.setVerticalGroup(
            pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
            .addGroup(pnlSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlSuperiorLayout.createSequentialGroup()
                    .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlDatosExamen.setBackground(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
        pnlDatosExamen.setMaximumSize(new java.awt.Dimension(32767, 109));
        pnlDatosExamen.setName(""); // NOI18N

        upperSeparator.setForeground(LookAndFeelEntropy.COLOR_ENTROPY);

        lowerSeparator.setForeground(LookAndFeelEntropy.COLOR_ENTROPY);

        lblsAlumno.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsAlumno.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        lblsAlumno.setText("Alumno:");

        lblAlumno.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblAlumno.setText("Alumno");

        lblsLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsLegajo.setForeground(LookAndFeelEntropy.COLOR_FUENTE_TITULO_PANEL);
        lblsLegajo.setText("Legajo:");

        lblLegajo.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblLegajo.setText("---");

        lblPerfilCompleto.setFont(LookAndFeelEntropy.FUENTE_TITULO);
        lblPerfilCompleto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_estadisticas_alumno_25x25.png"))); // NOI18N
        lblPerfilCompleto.setText("Ver perfil completo");
        lblPerfilCompleto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblPerfilCompleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPerfilCompletoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlDatosExamenLayout = new javax.swing.GroupLayout(pnlDatosExamen);
        pnlDatosExamen.setLayout(pnlDatosExamenLayout);
        pnlDatosExamenLayout.setHorizontalGroup(
            pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lowerSeparator, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(upperSeparator)
                    .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPerfilCompleto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblsLegajo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblsAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblLegajo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        pnlDatosExamenLayout.setVerticalGroup(
            pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosExamenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(upperSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsAlumno)
                    .addComponent(lblAlumno))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosExamenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsLegajo)
                    .addComponent(lblLegajo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPerfilCompleto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lowerSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        tbpSolapas.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);

        lblsRendidos.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsRendidos.setText("Exámenes Rendidos:");

        lblRendidos.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblRendidos.setText("30");

        lblsAprobados.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsAprobados.setText("Aprobados / Corregidos:");

        lblAprobados.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblAprobados.setText("15/30");

        lblsCorregidos.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsCorregidos.setText("Exámenes Corregidos:");

        lblCorregidos.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblCorregidos.setText("30");

        lblsPorcentajeAprobados.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsPorcentajeAprobados.setText("Porcentaje de aprobados:");

        lblPorcentajeAprobados.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblPorcentajeAprobados.setText("50%");

        lblsNotaPromedio.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsNotaPromedio.setText("Nota promedio:");

        lblNotaPromedio.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblNotaPromedio.setText("5");

        lblsNotaMayor.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsNotaMayor.setText("Mayor nota obtenida:");

        lblNotaMayor.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblNotaMayor.setText("7");

        lblsNotaMenor.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblsNotaMenor.setText("Menor nota obtenida:");

        lblNotaMenor.setFont(LookAndFeelEntropy.FUENTE_NEGRITA);
        lblNotaMenor.setText("2");

        lblMasEstadisticas.setFont(LookAndFeelEntropy.FUENTE_TITULO);
        lblMasEstadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_estadisticas_25x25.png"))); // NOI18N
        lblMasEstadisticas.setText("Ver estadísticas por dificultad, tema o preguntas.");
        lblMasEstadisticas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMasEstadisticas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMasEstadisticasMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlGeneralLayout = new javax.swing.GroupLayout(pnlGeneral);
        pnlGeneral.setLayout(pnlGeneralLayout);
        pnlGeneralLayout.setHorizontalGroup(
            pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblMasEstadisticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlGeneralLayout.createSequentialGroup()
                        .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblsNotaMenor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsNotaMayor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsNotaPromedio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsPorcentajeAprobados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsRendidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsCorregidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblsAprobados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRendidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAprobados, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                            .addComponent(lblCorregidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPorcentajeAprobados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNotaPromedio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNotaMayor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNotaMenor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlGeneralLayout.setVerticalGroup(
            pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsRendidos)
                    .addComponent(lblRendidos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsCorregidos)
                    .addComponent(lblCorregidos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsAprobados)
                    .addComponent(lblAprobados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsPorcentajeAprobados)
                    .addComponent(lblPorcentajeAprobados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsNotaPromedio)
                    .addComponent(lblNotaPromedio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsNotaMayor)
                    .addComponent(lblNotaMayor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblsNotaMenor)
                    .addComponent(lblNotaMenor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addComponent(lblMasEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tbpSolapas.addTab("General", pnlGeneral);

        lblResultados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlResultadosLayout = new javax.swing.GroupLayout(pnlResultados);
        pnlResultados.setLayout(pnlResultadosLayout);
        pnlResultadosLayout.setHorizontalGroup(
            pnlResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblResultados, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
        );
        pnlResultadosLayout.setVerticalGroup(
            pnlResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblResultados, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );

        tbpSolapas.addTab("Resultados", pnlResultados);

        lblNotas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlNotasLayout = new javax.swing.GroupLayout(pnlNotas);
        pnlNotas.setLayout(pnlNotasLayout);
        pnlNotasLayout.setHorizontalGroup(
            pnlNotasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNotas, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
        );
        pnlNotasLayout.setVerticalGroup(
            pnlNotasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNotas, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );

        tbpSolapas.addTab("Notas", pnlNotas);

        scrExamenesRendidos.setBorder(null);

        tblExamenesRendidos.setAutoCreateRowSorter(true);
        tblExamenesRendidos.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        tblExamenesRendidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Examen", "Calificación", "Estado", "Fecha", "Curso", "Institución", "Objeto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblExamenesRendidos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblExamenesRendidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblExamenesRendidosMouseClicked(evt);
            }
        });
        scrExamenesRendidos.setViewportView(tblExamenesRendidos);

        lblInfoExamenes.setBackground(LookAndFeelEntropy.COLOR_TABLA_PRIMARIO);
        lblInfoExamenes.setFont(LookAndFeelEntropy.FUENTE_REGULAR);
        lblInfoExamenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frontend/imagenes/ic_mensajes_informacion_orange.png"))); // NOI18N
        lblInfoExamenes.setText("Hacer doble clic en cada fila para guardar la resolución en PDF.");
        lblInfoExamenes.setBorder(javax.swing.BorderFactory.createLineBorder(LookAndFeelEntropy.COLOR_ENTROPY));
        lblInfoExamenes.setOpaque(true);

        javax.swing.GroupLayout pnlExamenesLayout = new javax.swing.GroupLayout(pnlExamenes);
        pnlExamenes.setLayout(pnlExamenesLayout);
        pnlExamenesLayout.setHorizontalGroup(
            pnlExamenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExamenesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlExamenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrExamenesRendidos, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addComponent(lblInfoExamenes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlExamenesLayout.setVerticalGroup(
            pnlExamenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExamenesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrExamenesRendidos, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoExamenes, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tbpSolapas.addTab("Exámenes", pnlExamenes);

        lblAsistencias.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlAsistenciasLayout = new javax.swing.GroupLayout(pnlAsistencias);
        pnlAsistencias.setLayout(pnlAsistenciasLayout);
        pnlAsistenciasLayout.setHorizontalGroup(
            pnlAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAsistencias, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
        );
        pnlAsistenciasLayout.setVerticalGroup(
            pnlAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAsistencias, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );

        tbpSolapas.addTab("Asistencias", pnlAsistencias);

        scrClasesAsistidas.setBorder(null);

        tblClasesAsistidas.setAutoCreateRowSorter(true);
        tblClasesAsistidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Presentación", "Descripción", "Fecha", "Curso", "Institución", "Objeto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrClasesAsistidas.setViewportView(tblClasesAsistidas);

        javax.swing.GroupLayout pnlClasesLayout = new javax.swing.GroupLayout(pnlClases);
        pnlClases.setLayout(pnlClasesLayout);
        pnlClasesLayout.setHorizontalGroup(
            pnlClasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClasesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrClasesAsistidas, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlClasesLayout.setVerticalGroup(
            pnlClasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClasesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrClasesAsistidas, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbpSolapas.addTab("Clases asistidas", pnlClases);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tbpSolapas)
                    .addComponent(pnlDatosExamen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(pnlDatosExamen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbpSolapas)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(484, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblPerfilCompletoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPerfilCompletoMouseClicked
        new DialogPerfilCompleto(VentanaPrincipal.getInstancia(), true, alumno).setVisible(true);
    }//GEN-LAST:event_lblPerfilCompletoMouseClicked

    private void tblExamenesRendidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblExamenesRendidosMouseClicked
        if (evt.getClickCount() == 2) {
            try {
                Resolucion resolucion = (Resolucion) tblExamenesRendidos.getModel().getValueAt(tblExamenesRendidos.getSelectedRow(), 6);
                if (resolucion != null) {
                    GestorGenerarReporteResolucion gestorReporte = new GestorGenerarReporteResolucion(resolucion);
                    gestorReporte.generarReporteResolucion();
                    String pathArchivo = gestorReporte.getResolucion();
                    Path path = Paths.get(pathArchivo);
                    byte[] pdf = Files.readAllBytes(path);
                    File pdfArchivo = new File(pathArchivo);
                    Desktop.getDesktop().open(pdfArchivo);
                }
            } catch (Exception e) {
                System.err.println("Ocurrió una excepción creando el PDF:  " + e.toString());
            }
        }
    }//GEN-LAST:event_tblExamenesRendidosMouseClicked

    private void lblMasEstadisticasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMasEstadisticasMouseClicked
        GestorExamen.getInstancia().verEstadisticas(this, this.colResoluciones);
    }//GEN-LAST:event_lblMasEstadisticasMouseClicked

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        if (pnlPadre instanceof DialogSelectorAlumno) {
            VentanaPrincipal.getInstancia().volverAInicio();
            ((DialogSelectorAlumno) pnlPadre).setVisible(true);
        } else if (pnlPadre instanceof PanelResoluciones) {
            VentanaPrincipal.getInstancia().getPanelDeslizante().setPanelMostrado((PanelResoluciones) pnlPadre);
            VentanaPrincipal.getInstancia().setTitle("Estadísticas del alumno " + alumno.toString());
            if (!VentanaPrincipal.getInstancia().isMaximized()){
                VentanaPrincipal.getInstancia().pack();
            }
        }
        
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnVolverInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverInicioActionPerformed
        VentanaPrincipal.getInstancia().volverAInicio();
    }//GEN-LAST:event_btnVolverInicioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton btnVolverInicio;
    private javax.swing.JLabel lblAlumno;
    private javax.swing.JLabel lblAprobados;
    private javax.swing.JLabel lblAsistencias;
    private javax.swing.JLabel lblCorregidos;
    private javax.swing.JLabel lblInfoExamenes;
    private javax.swing.JLabel lblLegajo;
    private javax.swing.JLabel lblMasEstadisticas;
    private javax.swing.JLabel lblNotaMayor;
    private javax.swing.JLabel lblNotaMenor;
    private javax.swing.JLabel lblNotaPromedio;
    private javax.swing.JLabel lblNotas;
    private javax.swing.JLabel lblPerfilCompleto;
    private javax.swing.JLabel lblPorcentajeAprobados;
    private javax.swing.JLabel lblRendidos;
    private javax.swing.JLabel lblResultados;
    private javax.swing.JLabel lblsAlumno;
    private javax.swing.JLabel lblsAprobados;
    private javax.swing.JLabel lblsCorregidos;
    private javax.swing.JLabel lblsLegajo;
    private javax.swing.JLabel lblsNotaMayor;
    private javax.swing.JLabel lblsNotaMenor;
    private javax.swing.JLabel lblsNotaPromedio;
    private javax.swing.JLabel lblsPorcentajeAprobados;
    private javax.swing.JLabel lblsRendidos;
    private javax.swing.JSeparator lowerSeparator;
    private javax.swing.JPanel pnlAsistencias;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlClases;
    private javax.swing.JPanel pnlDatosExamen;
    private javax.swing.JPanel pnlExamenes;
    private javax.swing.JPanel pnlGeneral;
    private javax.swing.JPanel pnlNotas;
    private javax.swing.JPanel pnlResultados;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JScrollPane scrClasesAsistidas;
    private javax.swing.JScrollPane scrExamenesRendidos;
    private javax.swing.JTable tblClasesAsistidas;
    private javax.swing.JTable tblExamenesRendidos;
    private javax.swing.JTabbedPane tbpSolapas;
    private javax.swing.JSeparator upperSeparator;
    // End of variables declaration//GEN-END:variables

    private int getCantidadCorregidos(ArrayList<Resolucion> colResoluciones) {
        int count = 0;
        for (Resolucion r : colResoluciones) {
            if (r.esCorreccionCompleta()) {
                count++;
            }
        }
        return count;
    }

    private int getCantidadAprobados(ArrayList<Resolucion> colResoluciones) {
        int count = 0;
        for (Resolucion r : colResoluciones) {
            try {
                if (r.estaAprobada()) {
                    count++;
                }
            } catch (Exception ex) {
            }
        }
        return count;
    }
}
