package contraatacando;
/**
 * Base
 *
 * Modela la definición de todos los objetos de tipo <code>Base</code>.
 *
 * @author Luis Felipe Salazar
 * @version 1.0
 * @date 10/Feb/16
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class Base {

    private int iX;                 // Posición en x.       
    private int iY;                 // Posición en y.
    private int iAncho;             // Ancho del objeto.
    private int iAlto;              // Largo del objeto.
    private Image imaImagen;        // Imagen.
    private ImageIcon imiImagen;    // Imagen con medidas.

    /**
     * Base
     * 
     * Método constructor usado para crear el objeto
     * creando el icono a partir de una imagen
     * 
     * @param iX es la <code>posición en x</code> del objeto.
     * @param iY es la <code>posición en y</code> del objeto.
     * @param imaImagen es la <code>imagen</code> del objeto.
     * iAncho es el <code>ancho</code> del objeto.
     * iAlto es el <code>Largo</code> del objeto.
     */
    public Base(int iX, int iY, Image imaImagen) {
        this.iX = iX;
        this.iY = iY;
        this.imaImagen = imaImagen;
        this.imiImagen = new ImageIcon(imaImagen);
        this.iAncho = this.imiImagen.getIconWidth();
        this.iAlto = this.imiImagen.getIconHeight();
    }

    
    /**
     * setX
     * 
     * Método modificador usado para cambiar la posición en x del objeto.
     * 
     * @param iX es la <code>posición en x</code> del objeto.
     */
    public void setX(int iX) {
        this.iX = iX;
    }

    /**
     * getX
     * 
     * Método de acceso que regresa la posición en x del objeto.
     * 
     * @return iX es la <code>posición en x</code> del objeto.
     */
    public int getX() {
            return iX;
    }

    /**
     * setY
     * 
     * Método modificador usado para cambiar la posición en y del objeto.
     * 
     * @param iY es la <code>posición en y</code> del objeto.
     */
    public void setY(int iY) {
            this.iY = iY;
    }

    /**
     * getY
     * 
     * Método de acceso que regresa la posición en y del objeto.
     * 
     * @return posY es la <code>posición en y</code> del objeto.
     */
    public int getY() {
        return iY;
    }

    /**
     * setImagen
     * 
     * Método modificador usado para cambiar el ícono de imagen del objeto
     * tomándolo de un objeto imagen.
     * 
     * @param imaImagen es la <code>imagen</code> del objeto.
     */
    public void setImagen(Image imaImagen) {
        this.imaImagen = imaImagen;
        this.imiImagen = new ImageIcon(imaImagen);
        this.iAncho = this.imiImagen.getIconWidth();
        this.iAlto = this.imiImagen.getIconHeight();
    }

    /**
     * getImagen
     * 
     * Método de acceso que regresa la imagen 
     * que representa el ícono del objeto.
     * 
     * @return la imagen a partir del <code>ícono</code> del objeto.
     */
    public Image getImagen() {
        return imaImagen;
    }

    /**
     * getAncho
     * 
     * Método de acceso que regresa el ancho del ícono.
     * 
     * @return un <code>entero</code> que es el ancho de la imagen.
     */
    public int getAncho() {
        return iAncho;
    }

    /**
     * getAlto
     * 
     * Método que  da el alto del ícono.
     * 
     * @return un <code>entero</code> que es el alto de la imagen.
     */
    public int getAlto() {
        return iAlto;
    }
    
    /**
     * paint
     * 
     * Método para pintar el animal.
     * 
     * @param graGrafico objeto de la clase <code>Graphics</code> para graficar
     * @param imoObserver objeto de la clase <code>ImageObserver</code> es el 
     *    Applet donde se pintara
     */
    public void paint(Graphics graGrafico, ImageObserver imoObserver) {
        graGrafico.drawImage(getImagen(), getX(), getY(), getAncho(), 
                getAlto(), imoObserver);
    }

    /**
     * equals
     * 
     * Método para checar igualdad con otro objeto.
     * 
     * @param objObjeto objeto de la clase <code>Object</code> para comparar.
     * @return un valor <code>boleano</code> que será verdadero si el objeto
     *   que invoca es igual al objeto recibido como parámetro.
     */
    public boolean equals(Object objObjeto) {
        // Si el objeto parámetro es una instancia de la clase Base
        if (objObjeto instanceof Base) {
            // Se regresa la comparación entre este objeto que invoca y el
            // objeto recibido como parámetro
            Base basParam = (Base) objObjeto;
            return (this.getX() ==  basParam.getX() && 
                    this.getY() == basParam.getY() &&
                    this.getAncho() == basParam.getAncho() &&
                    this.getAlto() == basParam.getAlto() &&
                    this.getImagen() == basParam.getImagen());
        } else {
            // Se regresa un falso porque el objeto recibido no es tipo Base.
            return false;
        }
    }

    /**
     * toString
     * 
     * Método para obtener la interfaz del objeto.
     * 
     * @return un valor <code>String</code> que representa al objeto. 
     */
    public String toString() {
        return " x: " + this.getX() + " y: "+ this.getY() +
                " ancho: " + this.getAncho() + " alto: " + this.getAlto();
    }
    
    /**
     * Colisiona
     *
     * Checa la colisión con un objeto Base.
     *
     * @param obj objeto de la clase <code>Object</code>.
     * @return boleano <code> true</code> si colisiona <code> false
     *      </code> si no colisiona.
    */
    public boolean colisiona(Object obj){
        // Checo si el objeto es de la clase Base.
        if(obj instanceof Base){
            // Se crea un rectangulo para el objeto.
            Rectangle recEste = new Rectangle(getX(), getY(), getAncho(), 
                   getAlto());
            // Se crea un rectangulo para obj del parámetro.
            Rectangle recOtro = new Rectangle(((Base) obj).getX(),
                   ((Base) obj).getY(), ((Base)obj).getAncho(), 
                   ((Base) obj).getAlto());
            // Se regresa true si se intersectan.
            return recEste.intersects(recOtro);
       }
        else {
            // Se regresa un falso porque el objeto recibido no es tipo Base.
            return false;
        }
    } 
}