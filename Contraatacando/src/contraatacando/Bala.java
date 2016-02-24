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
    
    char cTipo;     // Reconocedor de tipo de bala: 
                    // a(arriba), i(izquierda), d(derecha).
    int iVel;       // Variable para controlar la velocidad de la bala.
    
    public Bala(int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
    }
    
    public Bala(int iX, int iY, Image imaImage, char cTipo, int iVel) {
        super(iX, iY, imaImage);
        cTipo = 'a';
        iVel = 3;
    }
    
    public void avanza(){
        switch(cTipo){
                case 'a': {
                    break;
                }
                case 'b': {
                    break;
                }
                case 'c': {
                    break;
                }
        }
    }
}
