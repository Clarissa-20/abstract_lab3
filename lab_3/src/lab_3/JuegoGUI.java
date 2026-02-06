/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author HP
 */
public class JuegoGUI extends JFrame implements JuegoAcciones, ControlTurnos{
     private Jugador j1, j2, jugadorActual;
    private ArrayList<CartaBalatro> todasLasCartas = new ArrayList<>();
    private CartaBalatro primeraSeleccionada = null;
    private CartaBalatro segundaSeleccionada = null;
    private boolean bloqueado = false;
    private JLabel lblEstado;
    
    public JuegoGUI(String nombre1, String nombre2) {
        this.j1 = new Jugador(nombre1); 
        this.j2 = new Jugador(nombre2); 
        this.jugadorActual = j1; 

        configurarVentana();
        iniciarJuego(); // Implementación de interfaz [cite: 50]
    }
    
    private void configurarVentana() {
        setTitle("Memoria Balatro - 6x6");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        lblEstado = new JLabel("");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 16));
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        actualizarMarcador(); 
        add(lblEstado, BorderLayout.NORTH);
    }
    
    @Override
    public void iniciarJuego() {
        JPanel panelTablero = new JPanel(new GridLayout(6, 6, 5, 5)); 
        
        // REQUISITO: Try...Catch para manejo de errores 
        try {
            for (int i = 0; i < 18; i++) {
                // Ajusta las rutas a donde tengas tus imágenes de Balatro
                ImageIcon imgFrente = new ImageIcon("src/lab_3/res/balatro_" + (i % 12) + ".png"); 
                ImageIcon imgDorso = new ImageIcon("src/lab_3/res/dorso.png"); 

                // Creamos el par de cartas con el mismo ID [cite: 15]
                todasLasCartas.add(new CartaBalatro(i, imgFrente, imgDorso));
                todasLasCartas.add(new CartaBalatro(i, imgFrente, imgDorso));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando imágenes: " + e.getMessage());
        }

        Collections.shuffle(todasLasCartas); 

        for (CartaBalatro carta : todasLasCartas) {
            carta.addActionListener(e -> gestionarClic(carta));
            panelTablero.add(carta);
        }
        add(panelTablero, BorderLayout.CENTER);
    }

    private void gestionarClic(CartaBalatro carta) {
        // Evita clics si está bloqueado, si la carta ya se mostró o es la misma [cite: 76]
        if (bloqueado || carta.isEstaRevelada()) return;

        carta.mostrar(); 

        if (primeraSeleccionada == null) {
            primeraSeleccionada = carta;
        } else {
            segundaSeleccionada = carta;
            bloqueado = true;

            // Comparación usando los nuevos nombres de tus métodos
            if (primeraSeleccionada.getIdLogico() == segundaSeleccionada.getIdLogico()) { 
                // coinciden: permanecen visibles [cite: 80, 81]
                jugadorActual.sumarAcierto(); 
                actualizarMarcador(); 
                limpiarSeleccion(true);
                verificarFinJuego(); 
            } else {
                // no coinciden: se ocultan tras 1 segundo [cite: 84, 85]
                Timer timer = new Timer(1000, e -> {
                    primeraSeleccionada.ocultar(); 
                    segundaSeleccionada.ocultar(); 
                    cambiarTurno(); 
                    limpiarSeleccion(false);
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void limpiarSeleccion(boolean acierto) {
        primeraSeleccionada = null;
        segundaSeleccionada = null;
        bloqueado = false;
    }

    @Override
    public boolean verificarPareja(Carta c1, Carta c2) {
        // Este método cumple el requisito de polimorfismo de la interfaz 
        return c1.getId() == c2.getId();
    }

    @Override
    public void cambiarTurno() {
        jugadorActual = (jugadorActual == j1) ? j2 : j1;
        actualizarMarcador();
    }

    @Override
    public void actualizarMarcador() {
        lblEstado.setText("Turno de: " + jugadorActual.getNombre() + 
                         "  |  Aciertos -> " + j1.getNombre() + ": " + j1.getAciertos() + 
                         " - " + j2.getNombre() + ": " + j2.getAciertos()); 
    }

    private void verificarFinJuego() {
        if ((j1.getAciertos() + j2.getAciertos()) == 18) { 
            finalizarPartida(); 
        }
    }

    @Override
    public void finalizarPartida() {
        String mensaje;
        if (j1.getAciertos() > j2.getAciertos()) {
            mensaje = "¡Ganador: " + j1.getNombre() + "!"; 
        } else if (j2.getAciertos() > j1.getAciertos()) {
            mensaje = "¡Ganador: " + j2.getNombre() + "!"; 
        } else {
            mensaje = "¡Es un Empate!"; 
        }
        JOptionPane.showMessageDialog(this, mensaje); 
    }
}
