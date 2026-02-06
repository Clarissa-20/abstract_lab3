/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;

public class JuegoGui extends JPanel implements ControlTurnos, JuegoAcciones {
    private JFrame frameContenedor;
    private CartaBalatro[][] botonesCartas;
    private JLabel lblNombreJ1, lblAciertosJ1;
    private JLabel lblNombreJ2, lblAciertosJ2;
    private JLabel lblIndicadorTurno;
    
    private JTextField txtJugador1, txtJugador2;
    private JPanel panelInicio, panelJuego;
    
    private Jugador j1, j2, jugadorActual;
    private CartaBalatro primerBotonSeleccionado = null;
    private CartaBalatro segundoBotonSeleccionado = null;
    
    private List<ImageIcon> poolDeImagenesContenidoExtra;
    private ImageIcon dorsoCarta;
    private Image imgFondo;

    private final int ANCHO_CARTA = 80; 
    private final int ALTO_CARTA = 107;

    public JuegoGui(JFrame frame) {
        this.frameContenedor = frame;
        this.botonesCartas = new CartaBalatro[6][6];
        this.poolDeImagenesContenidoExtra = new ArrayList<>();
        
        cargarRecursos();
        setLayout(new CardLayout());
        crearPantallaInicio();
        crearPantallaJuego();
        
        add(panelInicio, "INICIO");
        add(panelJuego, "JUEGO");
    }

    private void cargarRecursos() {
        String rutaBase = "img/" + File.separator;
        
        for (int i = 1; i <= 18; i++) {
            ImageIcon img = escalarImagenSegura(rutaBase + "Carta" + i + ".png");
            if (img != null) {
                poolDeImagenesContenidoExtra.add(img);
            }
        }
        
        dorsoCarta = escalarImagenSegura(rutaBase + "dorso.png");
        File fileFondo = new File(rutaBase + "FondoBalatro.png");
        if(fileFondo.exists()) imgFondo = new ImageIcon(fileFondo.getAbsolutePath()).getImage();
    }

    private void mezclarCartas() {
        if (poolDeImagenesContenidoExtra.size() < 18) {
            return;
        }

        List<ImageIcon> copiaPool = new ArrayList<>(poolDeImagenesContenidoExtra);
        Collections.shuffle(copiaPool);
        
        List<ImageIcon> cartasDeEstaRonda = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            ImageIcon img = copiaPool.get(i);
            cartasDeEstaRonda.add(img);
            cartasDeEstaRonda.add(img);
        }

        Collections.shuffle(cartasDeEstaRonda);

        JPanel pTablero = (JPanel) panelJuego.getComponent(1);
        pTablero.removeAll();

        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                ImageIcon imgAsignada = cartasDeEstaRonda.get(index);
                int idLogico = poolDeImagenesContenidoExtra.indexOf(imgAsignada);

                botonesCartas[i][j] = new CartaBalatro(idLogico, imgAsignada, dorsoCarta);
                botonesCartas[i][j].addActionListener(e -> seleccionarCarta((CartaBalatro) e.getSource()));
                
                pTablero.add(botonesCartas[i][j]);
                index++;
            }
        }
        
        pTablero.revalidate();
        pTablero.repaint();
    }

    private ImageIcon escalarImagenSegura(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return null;
        ImageIcon iconoOriginal = new ImageIcon(f.getAbsolutePath());
        Image img = iconoOriginal.getImage().getScaledInstance(ANCHO_CARTA, ALTO_CARTA, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public void iniciarJuego() {
        if (txtJugador1.getText().trim().isEmpty() || txtJugador2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa nombres para ambos jugadores");
            return;
        }

        j1 = new Jugador(txtJugador1.getText().trim());
        j2 = new Jugador(txtJugador2.getText().trim());
        jugadorActual = j1;

        lblNombreJ1.setText(j1.getNombre());
        lblNombreJ2.setText(j2.getNombre());
        lblNombreJ1.setForeground(Color.YELLOW);
        lblNombreJ2.setForeground(Color.WHITE);
        lblIndicadorTurno.setText("TURNO DE: " + jugadorActual.getNombre());
        
        actualizarMarcador();
        mezclarCartas();
        
        ((CardLayout)getLayout()).show(this, "JUEGO");
    }

    private void seleccionarCarta(CartaBalatro btn) {
        if (btn.isEstaRevelada() || btn.getBackground().equals(Color.BLACK) || btn == primerBotonSeleccionado) return;

        btn.mostrar();

        if (primerBotonSeleccionado == null) {
            primerBotonSeleccionado = btn;
        } else {
            segundoBotonSeleccionado = btn;
            
            if (primerBotonSeleccionado.getIdLogico() == segundoBotonSeleccionado.getIdLogico()) {
                JOptionPane.showMessageDialog(this, "¡Pareja encontrada!");
                primerBotonSeleccionado.setBackground(Color.BLACK);
                segundoBotonSeleccionado.setBackground(Color.BLACK);
                jugadorActual.sumarAcierto();
                actualizarMarcador();
                
                if ((j1.getAciertos() + j2.getAciertos()) == 18) finalizarPartida();
            } else {
                JOptionPane.showMessageDialog(this, "No coinciden");
                primerBotonSeleccionado.ocultar();
                segundoBotonSeleccionado.ocultar();
                cambiarTurno();
            }
            primerBotonSeleccionado = null;
            segundoBotonSeleccionado = null;
        }
    }

    @Override public void cambiarTurno() {
        jugadorActual = (jugadorActual == j1) ? j2 : j1;
        lblIndicadorTurno.setText("Turno de: " + jugadorActual.getNombre());
        lblNombreJ1.setForeground(jugadorActual == j1 ? Color.YELLOW : Color.WHITE);
        lblNombreJ2.setForeground(jugadorActual == j2 ? Color.YELLOW : Color.WHITE);
    }

    @Override public void actualizarMarcador() {
        lblAciertosJ1.setText(String.valueOf(j1.getAciertos()));
        lblAciertosJ2.setText(String.valueOf(j2.getAciertos()));
    }

    @Override public void finalizarPartida() {
        String win = (j1.getAciertos() > j2.getAciertos()) ? j1.getNombre() : j2.getNombre();
        if(j1.getAciertos() == j2.getAciertos()) win = "Empate";
        JOptionPane.showMessageDialog(this, "Ganador: " + win);
        volverAlMenu();
    }

    private void volverAlMenu() { ((CardLayout)getLayout()).show(this, "INICIO"); }

    @Override public boolean verificarPareja(Carta c1, Carta c2) { return false; }

    private void crearPantallaInicio() {
        panelInicio = new PanelConFondo();
        panelInicio.setLayout(new GridBagLayout());
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(10, 10, 15, 230));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("BALATRO MEMORY GAME");
        lblTitulo.setFont(new Font("Impact", Font.PLAIN, 32));
        lblTitulo.setForeground(new Color(255, 50, 50));
        lblTitulo.setAlignmentX(0.5f);

        txtJugador1 = new JTextField("Player 1");
        txtJugador2 = new JTextField("Player 2");
        txtJugador1.setMaximumSize(new Dimension(200, 30));
        txtJugador2.setMaximumSize(new Dimension(200, 30));

        JButton btnComenzar = new JButton("INICIAR PARTIDA");
        btnComenzar.setAlignmentX(0.5f);
        btnComenzar.addActionListener(e -> iniciarJuego());

        contenedor.add(lblTitulo);
        contenedor.add(Box.createRigidArea(new Dimension(0, 20)));
        contenedor.add(txtJugador1);
        contenedor.add(Box.createRigidArea(new Dimension(0, 10)));
        contenedor.add(txtJugador2);
        contenedor.add(Box.createRigidArea(new Dimension(0, 20)));
        contenedor.add(btnComenzar);
        panelInicio.add(contenedor);
    }

    private void crearPantallaJuego() {
        panelJuego = new JPanel(new BorderLayout());
        panelJuego.setBackground(new Color(15, 15, 20));

        JPanel panelSuperior = new JPanel(new GridLayout(1, 3));
        panelSuperior.setPreferredSize(new Dimension(0, 90));
        panelSuperior.add(crearPanelJugador(1));
        
        lblIndicadorTurno = new JLabel("TURNO");
        lblIndicadorTurno.setForeground(new Color(30, 30, 30)); 
        lblIndicadorTurno.setFont(new Font("Impact", Font.PLAIN, 16));
        lblIndicadorTurno.setHorizontalAlignment(0);
        panelSuperior.add(lblIndicadorTurno);
        
        panelSuperior.add(crearPanelJugador(2));
        panelJuego.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelTablero = new JPanel(new GridLayout(6, 6, 4, 4));
        panelTablero.setOpaque(false);
        panelJuego.add(panelTablero, BorderLayout.CENTER);

        JButton btnSalir = new JButton("SALIR");
        btnSalir.addActionListener(e -> volverAlMenu());
        panelJuego.add(btnSalir, BorderLayout.SOUTH);
    }

    private JPanel crearPanelJugador(int n) {
        JPanel p = new JPanel(new GridLayout(3, 1));
        p.setBackground(n == 1 ? new Color(120, 20, 20) : new Color(20, 20, 120));
        JLabel nom = (n == 1) ? (lblNombreJ1 = new JLabel()) : (lblNombreJ2 = new JLabel());
        JLabel tag = new JLabel("ACIERTOS DEL JUGADOR");
        JLabel aci = (n == 1) ? (lblAciertosJ1 = new JLabel("0")) : (lblAciertosJ2 = new JLabel("0"));
        for(JLabel l : new JLabel[]{nom, tag, aci}){
            l.setForeground(Color.WHITE); l.setHorizontalAlignment(0); p.add(l);
        }
        return p;
    }

    class PanelConFondo extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imgFondo != null) g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}