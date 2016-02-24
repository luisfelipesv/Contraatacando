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
public class Bala extends Base{    
    
    char cTipo;     // Reconocedor de directtion de la bala.
    int iVel;       // Variable para controlar la velocidad de la bala.
    
    /**
     * Bala
     * 
     * Método constructor usado para crear el objeto bala
     * creando el icono a partir de una imagen.
     * 
     * @param iX es la <code>posición en x</code> del objeto.
     * @param iY es la <code>posición en y</code> del objeto
     * @param imaImagen es la <code>imagen</code> del objeto.
     * @param cTipo es la <code>dirección</code> de la bala.
     * @param iVel es la <code>velocidad</code> del objeto
     */
    public Bala(int iX, int iY, Image imaImagen, char cTipo, int iVel) {
        super(iX, iY, imaImagen);
        this.cTipo = cTipo;
        this.iVel = iVel;
    }
    
    /**
     * avanza
     * 
     * Método que actualiza la posición de la bala, según la dirección en
     * la que vaya.
     */
    public void avanza(){
        switch(cTipo){
                // Caso en que la bala vaya hacia arriba.
                case ' ': {
                    this.setY(this.getY() - this.iVel);
                    break;
                }
                // Caso en que la bala vaya hacia la izquierda.
                case 'a': {
                    this.setY(this.getY() - this.iVel);
                    break;
                }
                // Caso en que la bala vaya hacia la derecha.
                case 's': {
                    this.setY(this.getY() - this.iVel);
                    this.setX(this.getX() - this.iVel);
                    break;
                }
        }
    }
}
