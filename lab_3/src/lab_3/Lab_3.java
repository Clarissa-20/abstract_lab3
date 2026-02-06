/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab_3;

import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Lab_3 {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        String p1 = JOptionPane.showInputDialog("Nombre Jugador 1:");
        String p2 = JOptionPane.showInputDialog("Nombre Jugador 2:");

        if (p1 == null || p2 == null || p1.trim().isEmpty() || p2.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los nombres son obligatorios");
            return;
        }
        new JuegoDeCartasGUI(p1, p2).setVisible(true);
    }
    
}
