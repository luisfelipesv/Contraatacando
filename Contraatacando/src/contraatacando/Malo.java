/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contraatacando;

import java.awt.Image;

/**
 *
 * @author luisfelipesv
 */
public class Malo extends Base{
    
    private boolean bMalote;
    private int iVel;
    
    public Malo(int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
        this.bMalote = false;
        this.iVel = 1;
    }
    
    public Malo(int iX, int iY, Image imaImagen, boolean bMalote) {
        super(iX, iY, imaImagen);
        this.bMalote = bMalote;
        this.iVel = 1;
    }
    
    /**
     * setVel
     * 
     * Método modificador usado para cambiar la velocidad del malo.
     * 
     * @param iVel es la <code>velocidad</code> del malo.
     */
    public void setVel(int iVel) {
        this.iVel = iVel;
    }
    
    /**
     * getVel
     * 
     * Método de acceso que regresa la velocidad del malo.
     * 
     * @return iVel es la <code>pvelocidad</code> del malo.
     */
    public int getVel() {
            return iVel;
    }
    
    /**
     * avanza
     * 
     * Método que actualiza la posición del malo, según sea malote o no.
     */
    public void avanza(){
        if (this.bMalote) {
            
        } else {
            this.setY(this.getY() + this.iVel);
        }
        
    }
    
    
    
}
