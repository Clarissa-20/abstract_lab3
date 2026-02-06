/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JuegoDeCartasGUI extends JPanel {
    private JFrame frameContenedor;
    private JButton[][] botonesCartas;
    private JLabel lblNombreJ1, lblAciertosJ1, lblTurnoJ1;
    private JLabel lblNombreJ2, lblAciertosJ2, lblTurnoJ2;
    private JButton btnReiniciar;
    
    // Componentes de pantalla inicio
    private JTextField txtJugador1;
    private JTextField txtJugador2;
    private JButton btnComenzar;
    private JPanel panelInicio;
    private JPanel panelJuego;
    
    public JuegoDeCartasGUI(JFrame frame) {
        this.frameContenedor = frame;
        this.botonesCartas = new JButton[6][6];
        
        setLayout(new CardLayout());
        setBackground(Color.WHITE);
        
        crearPantallaInicio();
        crearPantallaJuego();
        
        add(panelInicio, "INICIO");
        add(panelJuego, "JUEGO");
    }
    
    private void crearPantallaInicio() {
        panelInicio = new JPanel(new BorderLayout());
        panelInicio.setBackground(new Color(227, 242, 253));
        
        // Panel principal central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(new Color(227, 242, 253));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Titulo
        JLabel lblTitulo = new JLabel("POKEMON MEMORY GAME");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(new Color(211, 47, 47));
        
        // Espaciador
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));
        panelCentral.add(lblTitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Panel Jugador 1
        JPanel panelJugador1 = crearPanelJugadorInicio("Jugador 1:", 1);
        panelCentral.add(panelJugador1);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel Jugador 2
        JPanel panelJugador2 = crearPanelJugadorInicio("Jugador 2:", 2);
        panelCentral.add(panelJugador2);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Boton comenzar
        btnComenzar = new JButton("COMENZAR JUEGO");
        btnComenzar.setFont(new Font("Arial", Font.BOLD, 18));
        btnComenzar.setBackground(new Color(33, 150, 243));
        btnComenzar.setForeground(Color.WHITE);
        btnComenzar.setFocusPainted(false);
        btnComenzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComenzar.setPreferredSize(new Dimension(250, 50));
        btnComenzar.setMaximumSize(new Dimension(250, 50));
        
        btnComenzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui va la logica para iniciar juego
            }
        });
        
        panelCentral.add(btnComenzar);
        
        panelInicio.add(panelCentral, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelJugadorInicio(String etiqueta, int numeroJugador) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(227, 242, 253));
        panel.setMaximumSize(new Dimension(500, 60));
        
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        
        if (numeroJugador == 1) {
            txtJugador1 = txt;
        } else {
            txtJugador2 = txt;
        }
        
        panel.add(lbl);
        panel.add(txt);
        
        return panel;
    }
    
    private void crearPantallaJuego() {
        panelJuego = new JPanel(new BorderLayout());
        panelJuego.setBackground(Color.WHITE);
        
        // Panel superior con informacion de jugadores
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 20, 0));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel Jugador 1
        JPanel panelJ1 = crearPanelJugadorJuego(1);
        panelSuperior.add(panelJ1);
        
        // Panel Jugador 2
        JPanel panelJ2 = crearPanelJugadorJuego(2);
        panelSuperior.add(panelJ2);
        
        panelJuego.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con tablero
        JPanel panelTablero = new JPanel(new GridLayout(6, 6, 5, 5));
        panelTablero.setBackground(new Color(227, 242, 253));
        panelTablero.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                JButton btnCarta = new JButton("?");
                btnCarta.setFont(new Font("Arial", Font.BOLD, 24));
                btnCarta.setBackground(new Color(211, 47, 47));
                btnCarta.setForeground(Color.WHITE);
                btnCarta.setFocusPainted(false);
                
                final int fila = i;
                final int columna = j;
                
                btnCarta.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Aqui va la logica para seleccionar carta
                    }
                });
                
                botonesCartas[i][j] = btnCarta;
                panelTablero.add(btnCarta);
            }
        }
        
        panelJuego.add(panelTablero, BorderLayout.CENTER);
        
        // Panel inferior con boton reiniciar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        btnReiniciar = new JButton("REINICIAR JUEGO");
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnReiniciar.setBackground(new Color(76, 175, 80));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFocusPainted(false);
        
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui va la logica para reiniciar juego
            }
        });
        
        panelInferior.add(btnReiniciar);
        panelJuego.add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelJugadorJuego(int numeroJugador) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(numeroJugador == 1 ? new Color(255, 235, 59) : new Color(255, 82, 82));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setPreferredSize(new Dimension(300, 100));
        
        if (numeroJugador == 1) {
            lblNombreJ1 = new JLabel("Jugador 1: ");
            lblNombreJ1.setFont(new Font("Arial", Font.BOLD, 16));
            lblNombreJ1.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            lblAciertosJ1 = new JLabel("Aciertos: 0");
            lblAciertosJ1.setFont(new Font("Arial", Font.PLAIN, 14));
            lblAciertosJ1.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            lblTurnoJ1 = new JLabel("");
            lblTurnoJ1.setFont(new Font("Arial", Font.BOLD, 14));
            lblTurnoJ1.setForeground(new Color(0, 128, 0));
            lblTurnoJ1.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            panel.add(Box.createVerticalGlue());
            panel.add(lblNombreJ1);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(lblAciertosJ1);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(lblTurnoJ1);
            panel.add(Box.createVerticalGlue());
        } else {
            lblNombreJ2 = new JLabel("Jugador 2: ");
            lblNombreJ2.setFont(new Font("Arial", Font.BOLD, 16));
            lblNombreJ2.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            lblAciertosJ2 = new JLabel("Aciertos: 0");
            lblAciertosJ2.setFont(new Font("Arial", Font.PLAIN, 14));
            lblAciertosJ2.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            lblTurnoJ2 = new JLabel("");
            lblTurnoJ2.setFont(new Font("Arial", Font.BOLD, 14));
            lblTurnoJ2.setForeground(new Color(0, 128, 0));
            lblTurnoJ2.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            panel.add(Box.createVerticalGlue());
            panel.add(lblNombreJ2);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(lblAciertosJ2);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(lblTurnoJ2);
            panel.add(Box.createVerticalGlue());
        }
        
        return panel;
    }
    
    // Getters para acceder a los componentes desde otras clases
    public JTextField getTxtJugador1() {
        return txtJugador1;
    }
    
    public JTextField getTxtJugador2() {
        return txtJugador2;
    }
    
    public JButton getBtnComenzar() {
        return btnComenzar;
    }
    
    public JButton[][] getBotonesCartas() {
        return botonesCartas;
    }
    
    public JLabel getLblNombreJ1() {
        return lblNombreJ1;
    }
    
    public JLabel getLblAciertosJ1() {
        return lblAciertosJ1;
    }
    
    public JLabel getLblTurnoJ1() {
        return lblTurnoJ1;
    }
    
    public JLabel getLblNombreJ2() {
        return lblNombreJ2;
    }
    
    public JLabel getLblAciertosJ2() {
        return lblAciertosJ2;
    }
    
    public JLabel getLblTurnoJ2() {
        return lblTurnoJ2;
    }
    
    public JButton getBtnReiniciar() {
        return btnReiniciar;
    }
    
    public void cambiarAPantallaJuego() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "JUEGO");
    }
    
    public void cambiarAPantallaInicio() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "INICIO");
    }
    
    // Metodo main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Pokemon Memory Game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 700);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                
                JuegoDeCartasGUI juegoGUI = new JuegoDeCartasGUI(frame);
                frame.add(juegoGUI);
                
                frame.setVisible(true);
            }
        });
    }
}