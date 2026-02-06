/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab_3;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Lab_3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Balatro Memory Lab 3");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(600, 800); 
            
            JuegoGUI juego = new JuegoGUI(f);
            
            f.add(juego);
            f.setLocationRelativeTo(null);
            f.setResizable(false);
            f.setVisible(true);
        });
    }
}