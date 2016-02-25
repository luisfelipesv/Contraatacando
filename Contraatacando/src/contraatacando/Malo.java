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
public class Malo extends Base {
    
    private boolean bMalote;    // Boleano para identificar al 10% de malos que
                                // van en diagonal.
    private int iVel;           // Variable para guardar la velocidad del malo.
    
    /**
     * Malo
     * 
     * Método constructor usado para crear el objeto malo
     * creando el ícono a partir de una imagen.
     * 
     * @param iX es la <code>posición en x</code> del objeto.
     * @param iY es la <code>posición en y</code> del objeto
     * @param imaImagen es la <code>imagen</code> del objeto.
     */
    public Malo(int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
        this.bMalote = false;
        this.iVel = 1;
    }
    
    /**
     * Mala
     * 
     * Método constructor usado para crear el objeto malo
     * creando el ícono a partir de una imagen.
     * 
     * @param iX es la <code>posición en x</code> del objeto.
     * @param iY es la <code>posición en y</code> del objeto
     * @param imaImagen es la <code>imagen</code> del objeto.
     * @param bMalote es el <code>boleano</code> para identificar 
     * el tipo de malo.
     */
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
     * setMalote
     * 
     * Método modificador usado para cambiar la propiedad de malote.
     * 
     * @param bMalote nos dice si es un<code>malote</code> o un malo normal.
     */
    public void setMalote(boolean bMalote) {
        this.bMalote = bMalote;
    }
    
    /**
     * avanza
     * 
     * Método que actualiza la posición del malo.
     */
    public void avanza(Base bP){
        if (this.bMalote) {
            if (bP.getX() > this.getX()){
                this.setX(this.getX() + iVel);
            }
            if (bP.getX() < this.getX()) {
                this.setX(this.getX() - iVel);
            }
            if (bP.getY() > this.getY()){
                this.setY(this.getY() + iVel);
            } 
            if (bP.getY() < this.getY()) {
                this.setY(this.getY() + -iVel);
            }
        } else {
            this.setY(this.getY() + this.iVel);
        }
        
    }
}
