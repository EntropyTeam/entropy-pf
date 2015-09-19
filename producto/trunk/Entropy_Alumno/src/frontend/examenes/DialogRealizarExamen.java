package frontend.examenes;

import backend.gestores.GestorSeguridad;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import javax.swing.JPanel;

/**
 *
 * @author Denise
 */
public class DialogRealizarExamen extends javax.swing.JDialog {

    private GestorSeguridad gestorSeguridad;
    
    /**
     * Creates new form DialogRealizarExamen
     */
    public DialogRealizarExamen(java.awt.Frame parent, boolean modal, PanelPregunta pnl) {
        super(parent, modal);
        initComponents();
        //Se debe activar el always on top para que ande la seguridad.
//        this.setAlwaysOnTop(true);
        this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
        this.setMaximumSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
        this.pnlCentral.add(pnl);
        gestorSeguridad = new GestorSeguridad();
        gestorSeguridad.deshabilitarExplorer();
        gestorSeguridad.deshabilitarTaskManager();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCentral = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlFondoAux = new PanelGradiente(Color.ORANGE);
        pnlCentral = pnlFondoAux;
        pnlCentral.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCentral, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //Activando el TaskMgr y explorer. Se prueba no activar aca, para que no haya redundancia de comandos que generan la apertura de ventanas extranias.
        this.gestorSeguridad.habilitarExplorer();
        this.gestorSeguridad.habilitarTaskManager();
        System.out.println("Activados el Tskmgr y Explorer");
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //Activando el TaskMgr y explorer. Se prueba no activar aca, para que no haya redundancia de comandos que generan la apertura de ventanas extranias.
        this.gestorSeguridad.habilitarExplorer();
        this.gestorSeguridad.habilitarTaskManager();
        System.out.println("Activados el Tskmgr y Explorer");
    }//GEN-LAST:event_formWindowClosed

    /**
     * Representa un panel con fondo de gradiente.
     */
    private class PanelGradiente extends JPanel {

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

//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//
//            int h = getHeight();
//            int w = getWidth();
//
//            GradientPaint gradientPaint = new GradientPaint(0, 0, Color.WHITE, 0, h, new Color(235, 204, 114));
//
//            Graphics2D g2D = (Graphics2D) g;
//            g2D.setPaint(gradientPaint);
//            g2D.fillRect(0, 0, w, h);
//            repaint();
//        }

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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlCentral;
    private PanelGradiente pnlFondoAux;
    // End of variables declaration//GEN-END:variables
}