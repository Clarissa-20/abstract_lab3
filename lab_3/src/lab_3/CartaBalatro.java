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
    private int idLogico;
    private boolean estaRevelada = false;
    private ImageIcon frente;
    private ImageIcon dorso;

    public CartaBalatro(int id, ImageIcon frente, ImageIcon dorso) {
        this.idLogico = id;
        this.frente = frente;
        this.dorso = dorso;
        
        this.setIcon(dorso);
        this.setBorderPainted(true);
    }

    
    public void mostrar() {
        this.setIcon(frente);
        this.estaRevelada = true;
    }

    public void ocultar() {
        this.setIcon(dorso);
        this.estaRevelada = false;
    }

    public int getIdLogico() { return idLogico; }
    public boolean isEstaRevelada() { return estaRevelada; }
}