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
        imagenesBalatro = new ImageIcon[6];
        String rutaCarpeta = "img" + File.separator;

        // Verificamos si la carpeta existe para depurar
        File carpeta = new File("img");
        if (!carpeta.exists()) {
            System.out.println("ERROR: La carpeta 'img' no existe en: " + carpeta.getAbsolutePath());
        }

        try {
            // Carga del fondo
            imgFondo = new ImageIcon(rutaCarpeta + "FondoBalatro.png").getImage();

            for (int i = 0; i < 6; i++) {
                String path = rutaCarpeta + "Carta" + (i + 1) + ".png";
                File archivo = new File(path);

                if (archivo.exists()) {
                    System.out.println("Cargando: " + path);
                    // Usamos ImageIO o forzamos la carga con getImage()
                    ImageIcon tempIcon = new ImageIcon(path);
                    imagenesBalatro[i] = escalarImagen(tempIcon, ANCHO_CARTA, ALTO_CARTA);
                } else {
                    System.out.println("ERROR: No existe el archivo " + path);
                }
            }

            dorsoCarta = escalarImagen(new ImageIcon(rutaCarpeta + "dorso.png"), ANCHO_CARTA, ALTO_CARTA);

        } catch (Exception e) {
            System.err.println("Excepción al cargar recursos: " + e.getMessage());
        }
    }

   private ImageIcon escalarImagen(ImageIcon icono, int ancho, int alto) {
        // Forzamos la carga de la imagen para evitar que devuelva un icono vacío
        Image img = icono.getImage();
        if (img == null) return null;

        // El método getScaledInstance a veces falla si la imagen no ha terminado de cargar
        // Usamos SCALE_FAST para asegurar que pinte algo rápidamente
        Image nuevaImg = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        ImageIcon finalIcon = new ImageIcon(nuevaImg);

        // Verificación final
        if (finalIcon.getIconWidth() <= 0) {
            System.out.println("Advertencia: Icono generado con tamaño 0");
        }
        return finalIcon;
    }
    class PanelConFondo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imgFondo != null) g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void crearPantallaInicio() {
        panelInicio = new PanelConFondo();
        panelInicio.setLayout(new GridBagLayout());
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(10, 10, 15, 230));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("BALATRO TEST");
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
                
                // AQUÍ SE AÑADEN LOS MENSAJES DE PRUEBA
                btn.addActionListener(e -> {
                    System.out.println("Botón presionado en: Fila " + f + ", Columna " + c);
                    // Descomenta la siguiente línea si quieres un mensaje emergente CADA vez que tocas una carta:
                    // JOptionPane.showMessageDialog(this, "Click en: " + f + "," + c);
                    seleccionarCarta(f, c);
                });
                
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
        
        // Bloqueo si la carta ya está emparejada (fondo negro)
        if (btn.getBackground().equals(Color.BLACK) || btn == primerBotonSeleccionado) return;

        // Mostrar imagen asignada
        int idImagen = valoresCartas[f][c];
        btn.setIcon(imagenesBalatro[idImagen]);
        System.out.println("Mostrando imagen ID: " + idImagen);

        if (primerBotonSeleccionado == null) {
            primerBotonSeleccionado = btn;
            primeraFila = f; primeraColumna = c;
        } else {
            segundoBotonSeleccionado = btn;
            segundaFila = f; segundaColumna = c;
            
            // Comparación instantánea
            if (valoresCartas[primeraFila][primeraColumna] == valoresCartas[segundaFila][segundaColumna]) {
                JOptionPane.showMessageDialog(this, "¡COINCIDENCIA ENCONTRADA!");
                primerBotonSeleccionado.setBackground(Color.BLACK);
                segundoBotonSeleccionado.setBackground(Color.BLACK);
                sumarPunto();
            } else {
                JOptionPane.showMessageDialog(this, "NO SON IGUALES");
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
        // 6 imágenes x 6 veces cada una = 36 cartas
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
        lblNombreJ1.setForeground(Color.YELLOW); // Indica turno del J1
        lblNombreJ2.setForeground(Color.WHITE);
        lblAciertosJ1.setText("0"); lblAciertosJ2.setText("0");
        
        // REINICIO TOTAL DEL TABLERO
        for(int i=0; i<6; i++) {
            for(int j=0; j<6; j++) {
                botonesCartas[i][j].setIcon(dorsoCarta);
                botonesCartas[i][j].setBackground(new Color(40, 40, 45));
                botonesCartas[i][j].setEnabled(true);
            }
        }
        
        mezclarCartas();
        ((CardLayout)getLayout()).show(this, "JUEGO");
        System.out.println("Partida iniciada y cartas mezcladas.");
    }

    private void volverAlMenu() {
        primerBotonSeleccionado = null;
        segundoBotonSeleccionado = null;
        ((CardLayout)getLayout()).show(this, "INICIO");
        System.out.println("Regresando al menú principal.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Balatro Depuración");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(550, 750); 
            f.add(new JuegoDeCartasGUI(f));
            f.setLocationRelativeTo(null);
            f.setResizable(false);
            f.setVisible(true);
        });
    }
}