/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_3;

/**
 *
 * @author spodi
 */

import javax.swing.*;

public class CartaBalatro extends JButton {
    private Carta logica; // Referencia a la lógica abstracta
    private ImageIcon imagenFrente;
    private ImageIcon imagenDorso;

    public CartaBalatro(int id, ImageIcon frente, ImageIcon dorso) {
        // Creamos la lógica anónima o mediante una clase interna
        this.logica = new Carta(id) {
            @Override
            public void mostrar() { setIcon(imagenFrente); setRevelada(true); }
            @Override
            public void ocultar() { setIcon(imagenDorso); setRevelada(false); }
        };
        this.imagenFrente = frente;
        this.imagenDorso = dorso;
        this.setIcon(dorso); // Inicia boca abajo [cite: 18]
    } 

    public Carta getLogica() { return logica; }
}