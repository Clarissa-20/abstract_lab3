/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_3;

/**
 *
 * @author HP
 */
public abstract class Carta {
    protected int id;
    protected boolean revelada;
    
    public Carta(int id, boolean revelada){
        this.id = id;
        this.revelada = false;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean getRevelada(){
        return revelada;
    }
    
    public void setRevelada(){
        this.revelada = revelada;
    }
    
    public abstract void mostrar();
    
    public abstract void ocultar();
}
