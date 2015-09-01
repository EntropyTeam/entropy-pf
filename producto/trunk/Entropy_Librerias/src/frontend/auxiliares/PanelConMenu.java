package frontend.auxiliares;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Clase que representa a un panel con menú expandible.
 *
 * @author Denise
 */
public class PanelConMenu extends JLayeredPane {

    private static final int INTERVALO_TEMPORIZADOR = 1;
    private static final int INTERVALO_PIXELES = 5;
    private int MENU_ANCHURA;
    private int MENU_ALTURA;
    private int OFFSET;
    private JPanel mPanel;
    private Dimension dimensionPanel;
    private Timer temporizadorMostrar = new TemporizadorMostrarMenu();
    private Timer temporizadorOcultar = new TemporizadorOcultarMenu();
    private boolean waiting = false;
    Timer timer;

    /**
     * Constructor de la clase.
     *
     * @param pnlMenu JPanel menú
     */
    public PanelConMenu(final JPanel pnlMenu) {
        mPanel = pnlMenu;
        mPanel.setVisible(false);
        MENU_ANCHURA = mPanel.getPreferredSize().width;
        MENU_ALTURA = mPanel.getPreferredSize().height;
        dimensionPanel = new Dimension(MENU_ANCHURA, MENU_ALTURA);
        OFFSET = -MENU_ANCHURA;
        setPosicionPanel(OFFSET);
        add(mPanel, 2, 0);

        pnlMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                temporizadorMostrar.stop();
                temporizadorOcultar.start();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                final int intAnchura = e.getX();
                if (intAnchura < mPanel.getWidth() && mPanel.isVisible()) {
                    return;
                } else if (intAnchura < 30 && !temporizadorMostrar.isRunning() && !mPanel.isVisible()) {
                    if (!waiting) {
                        timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                waiting = false;
                                Point p = MouseInfo.getPointerInfo().getLocation();
                                SwingUtilities.convertPointFromScreen(p, PanelConMenu.this);
                                if (p.x < pnlMenu.getWidth() && !temporizadorMostrar.isRunning() && !mPanel.isVisible()) {
                                    temporizadorOcultar.stop();
                                    temporizadorMostrar.start();
                                }
                                timer.stop();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                        waiting = true;
                    }
                } else if (intAnchura > mPanel.getWidth() && !temporizadorOcultar.isRunning() && mPanel.isVisible()) {
                    timer.stop();
                    temporizadorMostrar.stop();
                    temporizadorOcultar.start();
                }
            }
        });
    }

    /**
     * Devuelve el panel menú.
     *
     * @return el <code>JPanel</code> menu
     */
    public JPanel getmPanel() {
        return mPanel;
    }

    /**
     * Configura un nuevo panel Menú.
     *
     * @param mPanel
     */
    public void setmPanel(JPanel mPanel) {
        this.mPanel = mPanel;
        this.mPanel.setVisible(false);
        this.MENU_ANCHURA = this.mPanel.getPreferredSize().width;
        this.MENU_ALTURA = this.mPanel.getPreferredSize().height;
        this.dimensionPanel = new Dimension(MENU_ANCHURA, MENU_ALTURA);
        this.OFFSET = -MENU_ANCHURA;
        this.setPosicionPanel(OFFSET);
    }

    /**
     * Método para ubicar el panel menú en su nueva posición.
     *
     * @param offset
     */
    private void setPosicionPanel(final int offset) {
        int intAlturaPanel = getHeight();

        dimensionPanel.setSize(MENU_ANCHURA, intAlturaPanel);

        Insets insets = getInsets();

        Dimension tamaño = mPanel.getPreferredSize();

        mPanel.setBounds(offset + insets.left,
                insets.top,
                tamaño.width,
                tamaño.height
        );
    }

    /**
     * Clase que representa al temporizador que es llamado a intervalos
     * regulares para mostrar el Menu.
     */
    private class TemporizadorMostrarMenu extends Timer implements ActionListener {

        TemporizadorMostrarMenu() {
            super(INTERVALO_TEMPORIZADOR, null);
            addActionListener(this);
        }

        /**
         * Comienza el <code>Timer</code>, haciendo que envíe eventos de acción
         * a los ActionListeners.
         */
        @Override
        public void start() {
            OFFSET = -MENU_ANCHURA;
            mPanel.setVisible(true);
            super.start();
        }

        /**
         * Actualiza la pantalla
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (OFFSET <= 0) {
                setPosicionPanel(OFFSET);
            } else {
                temporizadorMostrar.stop();
            }

            OFFSET += INTERVALO_PIXELES;
        }
    }

    /**
     * Clase que representa al temporizador que es llamado a intervalos
     * regulares para ocultar el Menu.
     */
    private class TemporizadorOcultarMenu extends Timer implements ActionListener {

        TemporizadorOcultarMenu() {
            super(INTERVALO_TEMPORIZADOR, null);
            addActionListener(this);
        }

        /**
         * Detiene el <code>Timer</code>, haciendo que deje de eventos de acción
         * a los ActionListeners.
         */
        @Override
        public void stop() {
            mPanel.setVisible(false);
            super.stop();
        }

        /**
         * Actualiza la pantalla
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (OFFSET >= (-MENU_ANCHURA)) {
                OFFSET -= INTERVALO_PIXELES;
                setPosicionPanel(OFFSET);
            } else {
                temporizadorOcultar.stop();
            }
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        refrescar();
    }

    @Override
    public void repaint(int x, int y, int width, int height) {
        super.repaint(x, y, width, height);
        refrescar();
    }

    @Override
    public void repaint(Rectangle r) {
        super.repaint(r);
        refrescar();
    }

    @Override
    public void repaint(long tm) {
        super.repaint(tm);
        refrescar();
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm, x, y, width, height);
        refrescar();
    }

    /**
     * Redibuja el menú ante un cambio de tamaño de la pantalla que lo contiene.
     */
    private void refrescar() {
        if (mPanel != null) {
            MENU_ALTURA = this.getHeight();
            dimensionPanel = new Dimension(MENU_ANCHURA, MENU_ALTURA);
            mPanel.setSize(dimensionPanel);
        }
    }

    /**
     * Oculta el menú.
     */
    public void ocultar() {
        temporizadorOcultar.start();
    }
}
