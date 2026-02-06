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

public class JuegoDeCartasGUI extends JPanel {
    private JFrame frameContenedor;
    private JButton[][] botonesCartas;
    private JLabel lblNombreJ1, lblAciertosJ1;
    private JLabel lblNombreJ2, lblAciertosJ2;
    
    private JTextField txtJugador1, txtJugador2;
    private JPanel panelInicio, panelJuego;
    
    private JButton primerBotonSeleccionado = null;
    private JButton segundoBotonSeleccionado = null;
    private int primeraFila = -1, primeraColumna = -1;
    private int segundaFila = -1, segundaColumna = -1;
    
    private int[][] valoresCartas;
    private ImageIcon[] imagenesBalatro;
    private ImageIcon dorsoCarta;
    private Image imgFondo;

    private final int ANCHO_CARTA = 80; 
    private final int ALTO_CARTA = 107;

    public JuegoDeCartasGUI(JFrame frame) {
        this.frameContenedor = frame;
        this.botonesCartas = new JButton[6][6];
        this.valoresCartas = new int[6][6];
        
        cargarRecursos();
        
        setLayout(new CardLayout());
        crearPantallaInicio();
        crearPantallaJuego();
        
        add(panelInicio, "INICIO");
        add(panelJuego, "JUEGO");
    }

    private void cargarRecursos() {
        imagenesBalatro = new ImageIcon[6]; // Solo 6 imágenes ahora
        String rutaCarpeta = "img" + File.separator;
        
        try {
            imgFondo = new ImageIcon(rutaCarpeta + "FondoBalatro.png").getImage();
            for (int i = 0; i < 6; i++) {
                imagenesBalatro[i] = escalarImagen(new ImageIcon(rutaCarpeta + "Carta" + (i + 1) + ".png"), ANCHO_CARTA, ALTO_CARTA);
            }
            dorsoCarta = escalarImagen(new ImageIcon(rutaCarpeta + "dorso.png"), ANCHO_CARTA, ALTO_CARTA);
        } catch (Exception e) {
            System.err.println("Error al cargar imágenes: " + e.getMessage());
        }
    }

    private ImageIcon escalarImagen(ImageIcon icono, int ancho, int alto) {
        if (icono.getImageLoadStatus() != MediaTracker.COMPLETE) return null;
        Image img = icono.getImage();
        return new ImageIcon(img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }

    class PanelConFondo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imgFondo != null) g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
            else { g.setColor(new Color(20, 20, 30)); g.fillRect(0, 0, getWidth(), getHeight()); }
        }
    }

    private void crearPantallaInicio() {
        panelInicio = new PanelConFondo();
        panelInicio.setLayout(new GridBagLayout());
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(10, 10, 15, 230));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("BALATRO");
        lblTitulo.setFont(new Font("Impact", Font.PLAIN, 40));
        lblTitulo.setForeground(new Color(255, 50, 50));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtJugador1 = crearTextField("Jimbo");
        txtJugador2 = crearTextField("Player 2");

        JButton btnComenzar = new JButton("INICIAR");
        estilizarBoton(btnComenzar, new Color(255, 50, 50));
        btnComenzar.addActionListener(e -> iniciarJuegoGUI());

        contenedor.add(lblTitulo);
        contenedor.add(Box.createRigidArea(new Dimension(0, 20)));
        contenedor.add(txtJugador1);
        contenedor.add(Box.createRigidArea(new Dimension(0, 10)));
        contenedor.add(txtJugador2);
        contenedor.add(Box.createRigidArea(new Dimension(0, 20)));
        contenedor.add(btnComenzar);
        panelInicio.add(contenedor);
    }

    private JTextField crearTextField(String def) {
        JTextField tf = new JTextField(def);
        tf.setMaximumSize(new Dimension(180, 30));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBackground(new Color(30, 30, 40));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        return tf;
    }

    private void crearPantallaJuego() {
        panelJuego = new JPanel(new BorderLayout());
        panelJuego.setBackground(new Color(15, 15, 20));

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 5, 0));
        panelSuperior.setPreferredSize(new Dimension(0, 60));
        panelSuperior.add(crearPanelJugadorJuego(1));
        panelSuperior.add(crearPanelJugadorJuego(2));
        panelJuego.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelTablero = new JPanel(new GridLayout(6, 6, 4, 4));
        panelTablero.setOpaque(false);
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                JButton btn = new JButton(dorsoCarta);
                btn.setBackground(new Color(40, 40, 45));
                btn.setBorder(null);
                final int f = i, c = j;
                btn.addActionListener(e -> seleccionarCarta(f, c));
                botonesCartas[i][j] = btn;
                panelTablero.add(btn);
            }
        }
        panelJuego.add(panelTablero, BorderLayout.CENTER);

        JButton btnSalir = new JButton("SALIR");
        estilizarBoton(btnSalir, new Color(60, 60, 70));
        btnSalir.addActionListener(e -> volverAlMenu());
        panelJuego.add(btnSalir, BorderLayout.SOUTH);
    }

    private void estilizarBoton(JButton btn, Color color) {
        btn.setFont(new Font("Impact", Font.PLAIN, 18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JPanel crearPanelJugadorJuego(int num) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(num == 1 ? new Color(180, 40, 40) : new Color(40, 80, 180));
        JLabel nom = (num == 1) ? (lblNombreJ1 = new JLabel()) : (lblNombreJ2 = new JLabel());
        JLabel aci = (num == 1) ? (lblAciertosJ1 = new JLabel("0")) : (lblAciertosJ2 = new JLabel("0"));
        for (JLabel l : new JLabel[]{nom, aci}) {
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setForeground(Color.WHITE);
            p.add(l);
        }
        return p;
    }

    private void seleccionarCarta(int f, int c) {
        JButton btn = botonesCartas[f][c];
        if (btn.getBackground().equals(Color.BLACK) || btn == primerBotonSeleccionado) return;

        btn.setIcon(imagenesBalatro[valoresCartas[f][c]]);

        if (primerBotonSeleccionado == null) {
            primerBotonSeleccionado = btn;
            primeraFila = f; primeraColumna = c;
        } else {
            segundoBotonSeleccionado = btn;
            segundaFila = f; segundaColumna = c;
            
            if (valoresCartas[primeraFila][primeraColumna] == valoresCartas[segundaFila][segundaColumna]) {
                primerBotonSeleccionado.setBackground(Color.BLACK);
                segundoBotonSeleccionado.setBackground(Color.BLACK);
                sumarPunto();
            } else {
                JOptionPane.showMessageDialog(this, "¡No es coincidencia!");
                primerBotonSeleccionado.setIcon(dorsoCarta);
                segundoBotonSeleccionado.setIcon(dorsoCarta);
                cambiarTurno();
            }
            primerBotonSeleccionado = null;
            segundoBotonSeleccionado = null;
        }
    }

    private void sumarPunto() {
        JLabel l = lblNombreJ1.getForeground() == Color.YELLOW ? lblAciertosJ1 : lblAciertosJ2;
        l.setText("" + (Integer.parseInt(l.getText()) + 1));
    }

    private void cambiarTurno() {
        if (lblNombreJ1.getForeground() == Color.YELLOW) {
            lblNombreJ1.setForeground(Color.WHITE); lblNombreJ2.setForeground(Color.YELLOW);
        } else {
            lblNombreJ1.setForeground(Color.YELLOW); lblNombreJ2.setForeground(Color.WHITE);
        }
    }

    private void mezclarCartas() {
        List<Integer> lista = new ArrayList<>();
        // Llenar 36 espacios usando 6 imágenes (cada una aparece 6 veces, formando tríos de parejas)
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                lista.add(i);
            }
        }
        Collections.shuffle(lista);
        int idx = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                valoresCartas[i][j] = lista.get(idx++);
            }
        }
    }

    private void iniciarJuegoGUI() {
        lblNombreJ1.setText(txtJugador1.getText());
        lblNombreJ2.setText(txtJugador2.getText());
        lblNombreJ1.setForeground(Color.YELLOW);
        lblAciertosJ1.setText("0"); lblAciertosJ2.setText("0");
        
        // Reiniciar visualmente todos los botones antes de empezar
        for(int i=0; i<6; i++) {
            for(int j=0; j<6; j++) {
                botonesCartas[i][j].setIcon(dorsoCarta);
                botonesCartas[i][j].setBackground(new Color(40, 40, 45));
            }
        }
        
        mezclarCartas();
        ((CardLayout)getLayout()).show(this, "JUEGO");
    }

    private void volverAlMenu() {
        primerBotonSeleccionado = null;
        segundoBotonSeleccionado = null;
        ((CardLayout)getLayout()).show(this, "INICIO");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Balatro Mini");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(550, 750); 
            f.add(new JuegoDeCartasGUI(f));
            f.setLocationRelativeTo(null);
            f.setResizable(false);
            f.setVisible(true);
        });
    }
}