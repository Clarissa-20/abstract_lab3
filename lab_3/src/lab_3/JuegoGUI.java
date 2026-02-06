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
    private JLabel lblNombreJ1, lblAciertosJ1, lblTagAciertos1;
    private JLabel lblNombreJ2, lblAciertosJ2, lblTagAciertos2;
    private JLabel lblIndicadorTurno; // NUEVO: Indica de quién es el turno
    
    private JTextField txtJugador1, txtJugador2;
    private JPanel panelInicio, panelJuego;
    
    private Jugador j1, j2;
    private Jugador jugadorActual;
    
    private CartaBalatro primerBotonSeleccionado = null;
    private CartaBalatro segundoBotonSeleccionado = null;
    
    private int[][] valoresCartas;
    private ImageIcon[] imagenesBalatro;
    private ImageIcon dorsoCarta;
    private Image imgFondo;

    private final int ANCHO_CARTA = 80; 
    private final int ALTO_CARTA = 107;

    public JuegoGui(JFrame frame) {
        this.frameContenedor = frame;
        this.botonesCartas = new CartaBalatro[6][6];
        this.valoresCartas = new int[6][6];
        
        cargarRecursos();
        setLayout(new CardLayout());
        crearPantallaInicio();
        crearPantallaJuego();
        
        add(panelInicio, "INICIO");
        add(panelJuego, "JUEGO");
    }

    // --- LÓGICA DE INTERFACES ---
    @Override
    public void iniciarJuego() {
        // VALIDACIÓN: No permite iniciar si los campos están vacíos o solo tienen espacios
        if (txtJugador1.getText().trim().isEmpty() || txtJugador2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa los nombres de ambos jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        j1 = new Jugador(txtJugador1.getText().trim());
        j2 = new Jugador(txtJugador2.getText().trim());
        jugadorActual = j1;

        lblNombreJ1.setText(j1.getNombre());
        lblNombreJ2.setText(j2.getNombre());
        lblIndicadorTurno.setText("TURNO DE: " + jugadorActual.getNombre());
        
        actualizarMarcador();
        mezclarCartas();
        
        ((CardLayout)getLayout()).show(this, "JUEGO");
    }

    @Override
    public void cambiarTurno() {
        jugadorActual = (jugadorActual == j1) ? j2 : j1;
        lblIndicadorTurno.setText("TURNO DE: " + jugadorActual.getNombre());
        
        // Efecto visual en los nombres
        if (jugadorActual == j1) {
            lblNombreJ1.setForeground(Color.YELLOW); lblNombreJ2.setForeground(Color.WHITE);
        } else {
            lblNombreJ1.setForeground(Color.WHITE); lblNombreJ2.setForeground(Color.YELLOW);
        }
    }

    @Override
    public void actualizarMarcador() {
        lblAciertosJ1.setText(String.valueOf(j1.getAciertos()));
        lblAciertosJ2.setText(String.valueOf(j2.getAciertos()));
    }

    @Override public boolean verificarPareja(Carta c1, Carta c2) { return false; }

    @Override
    public void finalizarPartida() {
        String mensaje = "";
        if (j1.getAciertos() > j2.getAciertos()) mensaje = "¡Ganó " + j1.getNombre() + "!";
        else if (j2.getAciertos() > j1.getAciertos()) mensaje = "¡Ganó " + j2.getNombre() + "!";
        else mensaje = "¡Es un Empate!";
        
        JOptionPane.showMessageDialog(this, mensaje, "Fin de Partida", JOptionPane.INFORMATION_MESSAGE);
        volverAlMenu();
    }

    // --- PANTALLAS ---
    private void crearPantallaInicio() {
        panelInicio = new PanelConFondo();
        panelInicio.setLayout(new GridBagLayout());
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(10, 10, 15, 230));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("BALATRO MEMORY GAME"); // NOMBRE ACTUALIZADO
        lblTitulo.setFont(new Font("Impact", Font.PLAIN, 35));
        lblTitulo.setForeground(new Color(255, 50, 50));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtJugador1 = crearTextField("Player 1"); // DEFAULT ACTUALIZADO
        txtJugador2 = crearTextField("Player 2");

        JButton btnComenzar = new JButton("INICIAR PARTIDA");
        estilizarBoton(btnComenzar, new Color(255, 50, 50));
        btnComenzar.addActionListener(e -> iniciarJuego());

        contenedor.add(lblTitulo);
        contenedor.add(Box.createRigidArea(new Dimension(0, 20)));
        contenedor.add(new JLabel("Nombre Jugador 1:") {{ setForeground(Color.GRAY); }});
        contenedor.add(txtJugador1);
        contenedor.add(Box.createRigidArea(new Dimension(0, 10)));
        contenedor.add(new JLabel("Nombre Jugador 2:") {{ setForeground(Color.GRAY); }});
        contenedor.add(txtJugador2);
        contenedor.add(Box.createRigidArea(new Dimension(0, 20)));
        contenedor.add(btnComenzar);
        panelInicio.add(contenedor);
    }

    private void crearPantallaJuego() {
        panelJuego = new JPanel(new BorderLayout());
        panelJuego.setBackground(new Color(15, 15, 20));

        // PANEL SUPERIOR: Nombres, Aciertos e Indicador de Turno
        JPanel panelSuperior = new JPanel(new GridLayout(1, 3, 10, 0));
        panelSuperior.setPreferredSize(new Dimension(0, 80));
        panelSuperior.setBackground(new Color(20, 20, 25));

        panelSuperior.add(crearPanelJugadorJuego(1));
        
        lblIndicadorTurno = new JLabel("TURNO");
        lblIndicadorTurno.setFont(new Font("Impact", Font.PLAIN, 16));
        lblIndicadorTurno.setForeground(Color.CYAN);
        lblIndicadorTurno.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblIndicadorTurno);

        panelSuperior.add(crearPanelJugadorJuego(2));
        
        panelJuego.add(panelSuperior, BorderLayout.NORTH);

        // PANEL CENTRAL: Tablero
        JPanel panelTablero = new JPanel(new GridLayout(6, 6, 4, 4));
        panelTablero.setOpaque(false);
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                botonesCartas[i][j] = new CartaBalatro(-1, null, dorsoCarta); 
                panelTablero.add(botonesCartas[i][j]);
            }
        }
        panelJuego.add(panelTablero, BorderLayout.CENTER);

        JButton btnSalir = new JButton("ABANDONAR");
        estilizarBoton(btnSalir, new Color(60, 60, 70));
        btnSalir.addActionListener(e -> volverAlMenu());
        panelJuego.add(btnSalir, BorderLayout.SOUTH);
    }

    private JPanel crearPanelJugadorJuego(int num) {
        JPanel p = new JPanel(new GridLayout(3, 1)); // 3 filas: Nombre, "Aciertos", Numero
        p.setBackground(num == 1 ? new Color(150, 30, 30) : new Color(30, 60, 150));
        
        JLabel nom = (num == 1) ? (lblNombreJ1 = new JLabel()) : (lblNombreJ2 = new JLabel());
        JLabel tag = new JLabel("ACIERTOS"); // El texto "Aciertos"
        JLabel aci = (num == 1) ? (lblAciertosJ1 = new JLabel("0")) : (lblAciertosJ2 = new JLabel("0"));
        
        nom.setFont(new Font("Arial", Font.BOLD, 14));
        tag.setFont(new Font("Arial", Font.ITALIC, 10));
        aci.setFont(new Font("Impact", Font.PLAIN, 18));

        for (JLabel l : new JLabel[]{nom, tag, aci}) {
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setForeground(Color.WHITE);
            p.add(l);
        }
        return p;
    }

    // --- RECURSOS Y AUXILIARES ---
    private void seleccionarCarta(CartaBalatro btn) {
        if (btn.isEstaRevelada() || btn.getBackground().equals(Color.BLACK) || btn == primerBotonSeleccionado) return;

        btn.mostrar();

        if (primerBotonSeleccionado == null) {
            primerBotonSeleccionado = btn;
        } else {
            segundoBotonSeleccionado = btn;
            
            // Pausa pequeña para que el usuario vea la carta (opcional, aquí es por click)
            if (primerBotonSeleccionado.getIdLogico() == segundoBotonSeleccionado.getIdLogico()) {
                primerBotonSeleccionado.setBackground(Color.BLACK);
                segundoBotonSeleccionado.setBackground(Color.BLACK);
                jugadorActual.sumarAcierto();
                actualizarMarcador();
                verificarFinTablero();
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

    private void mezclarCartas() {
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) lista.add(i);
        }
        Collections.shuffle(lista);
        
        int idx = 0;
        JPanel pTablero = (JPanel)panelJuego.getComponent(1);
        pTablero.removeAll(); // Limpiar tablero visual

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int id = lista.get(idx++);
                botonesCartas[i][j] = new CartaBalatro(id, imagenesBalatro[id], dorsoCarta);
                botonesCartas[i][j].addActionListener(e -> seleccionarCarta((CartaBalatro)e.getSource()));
                pTablero.add(botonesCartas[i][j]);
            }
        }
        pTablero.revalidate();
        pTablero.repaint();
    }

    private void verificarFinTablero() {
        if ((j1.getAciertos() + j2.getAciertos()) == 18) {
            finalizarPartida();
        }
    }

    private void volverAlMenu() {
        ((CardLayout)getLayout()).show(this, "INICIO");
    }

    private void cargarRecursos() {
        imagenesBalatro = new ImageIcon[6];
        String ruta = "img" + File.separator;
        try {
            imgFondo = new ImageIcon(ruta + "FondoBalatro.png").getImage();
            for (int i = 0; i < 6; i++) 
                imagenesBalatro[i] = escalarImagen(new ImageIcon(ruta + "Carta" + (i + 1) + ".png"), ANCHO_CARTA, ALTO_CARTA);
            dorsoCarta = escalarImagen(new ImageIcon(ruta + "dorso.png"), ANCHO_CARTA, ALTO_CARTA);
        } catch (Exception e) {}
    }

    private ImageIcon escalarImagen(ImageIcon icono, int ancho, int alto) {
        Image img = icono.getImage();
        return (img != null) ? new ImageIcon(img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)) : null;
    }

    private JTextField crearTextField(String def) {
        JTextField tf = new JTextField(def);
        tf.setMaximumSize(new Dimension(200, 30));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBackground(new Color(30, 30, 40));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        return tf;
    }

    private void estilizarBoton(JButton btn, Color color) {
        btn.setFont(new Font("Impact", Font.PLAIN, 18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    class PanelConFondo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imgFondo != null) g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}