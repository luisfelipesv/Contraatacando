/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contraatacando;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JFrame;

/**
 *
 * @author luisfelipesv
 */
public class Contraatacando extends JFrame implements Runnable, KeyListener {

    private static final int WIDTH = 1100;    //Ancho del JFrame
    private static final int HEIGHT = 650;    //Alto del JFrame
    
    private Base basPrincipal;              // Objeto principal
    /* lista de los malos */
    private LinkedList<Base> lklMalos; 
    int iRanMalos;
    private Image imaImagenFondo;           // para dibujar la imagen de fondo
   
    /* objetos para manejar el buffer y que la imagen no parpadee */
    private Image    imaImagenApplet;       // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;      // Objeto grafico de la Imagen
    private AudioClip sonidoCatch;          // Sonido de colision de buenos
    private AudioClip sonidoPain;           // Sonido de colision con malos
    
    /* variables para controlar movimiento */
    private int iDireccion;                 // Direccion de principal
    
    /* variable e imagen para el control de vidas */
    private int iVidas;
    private int iContColisionMalo;
    private Image imaGameOver;
    
    /* variable para el control de puntos */
    private int iPuntos;
    
    /* variables para el control de pausa */
    private boolean bPausa;
    private Image imaPausa;
    
    public Contraatacando(){
        
        // Inicializamos las vidas con 5
        iVidas = 5;
        
        // Inicializamos el contador de las colisiones de los malos
        iContColisionMalo = 0;
       
        // Inicializamos los puntos en 0
        iPuntos = 0;
        
        // Inicializamos pausa
        bPausa = false;
        
        // Añadir KeyListener 
        addKeyListener(this);

        inicializoPrincipal(); // inicializo principal
        
        inicializoMalos(); // inicializo malos
        
        inicializoSonidos(); // inicializo sonidos
        
        inicializoImagenes(); // inicializo imagenes
        
        Thread th = new Thread (this); // Declaras un hilo
        
        th.start (); // Empieza el hilo
    }
    
    /** 
     * inicializoImagenes
     * 
     * Metodo que inicializa las imagenes de Fondo, Game Over y Pausa.
     * 
     */
    public void inicializoImagenes() {
        // Creo la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("fondo.jpg");
        imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        
        // Creo la imagen del Game Over,
        URL urlImagenGameOver = this.getClass().getResource("gameover.jpg");
        imaGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);
        
        // Creo la imagen de Pausa.
        URL urlImagenPausa = this.getClass().getResource("Pausa.png");
        imaPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
        
    }
    
    /** 
     * inicializoSonidos
     * 
     * Metodo que inicializa los sonidos
     * 
     */
    public void inicializoSonidos() {
        
        // Creo el sonido de colision entre buenos y principal
        URL eaURL = Contraatacando.class.getResource("catch.wav");
        sonidoCatch = Applet.newAudioClip(eaURL);
        
        // Creo el sonido de vida menos
        URL eaURL2 = Contraatacando.class.getResource("pain.wav");
        sonidoPain = Applet.newAudioClip(eaURL2);
    }
    
    /** 
     * inicializoPrincipal
     * 
     * Metodo que inicializa a Principal
     * 
     */
    public void inicializoPrincipal() {
        // Defino la imagen de principal.
        URL urlImagenPrincipal = this.getClass().getResource("principal.png");
        
        // Creo el objeto principal 
	basPrincipal = new Base(0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // Posiciono al objeto principal
        basPrincipal.setX((getWidth() - basPrincipal.getAncho()) / 2);
        basPrincipal.setY((getHeight() - basPrincipal.getAlto()));
    }
    
    /** 
     * inicializoMalos
     * 
     * Metodo que inicializa a todos los malos
     * 
     */
    public void inicializoMalos() {
        /* creo la lista de los malos */
        lklMalos = new LinkedList<Base>();
        
        /* genero el random de los malos entre 8 y 10 */
        iRanMalos = (int) (Math.random() * 3) + 8;
        
        // Defino la de los malos.
	URL urlImagenMalos = this.getClass().getResource("malo.png");
        
        // Creo a los malos
        for(int iI = 0; iI < iRanMalos; iI++){
            // Creo a un malo
            Base basMalo = new Base (0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenMalos));
            // Añado al malo a la lista
            lklMalos.add(basMalo);
        }
        
        // Posiciono a los malos
        for (Base basMalo : lklMalos){
            basMalo.setX((int)(Math.random()*(getWidth() * 2) + getWidth()));
            basMalo.setY((int)(Math.random()*(getHeight()-basMalo.getAlto())));
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Contraatacando jfrmJuego = new Contraatacando();
        jfrmJuego.setSize(WIDTH,HEIGHT);
        jfrmJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrmJuego.setVisible(true);
    }
    
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run() {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas > 0) {
            if (!bPausa){
                actualiza();
                checaColision();
            } 
            repaint();
            try	{
                // El hilo del juego se duerme.
                Thread.sleep (40);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
    
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){

        // Mueve a principal dependiendo de la dirección
        switch (iDireccion) {
            case 1:
                basPrincipal.setX(basPrincipal.getX() - 3);
                break;
            case 2:
                basPrincipal.setX(basPrincipal.getX() + 3);
                break;
            default:
                break;
        }
        
        actualizaBuenosyMalos(); // actualizamos buenos y malos
    }
    
    /** 
     * actualizaBuenosyMalos
     * 
     * Metodo que actualiza la posicion de los buenos y malos 
     * 
     */
    public void actualizaBuenosyMalos() {
        for (Base basMalo : lklMalos){
            /* genero el random de la velocidad del malo entre 3 y 5 */
            int iRanVelocidad = (int) (Math.random() * 3) + 3;
            
            // Se actualiza la posicion del bueno
            basMalo.setX(basMalo.getX() - iRanVelocidad); 
        }
    }

    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos.
     * 
     */
    public void checaColision(){
        
        // Checa que el principal no salga del marco
        if (basPrincipal.getX() <= 0) {
            basPrincipal.setX(0);
        } 
        if (basPrincipal.getX() + basPrincipal.getAncho() >= getWidth() ) {
            basPrincipal.setX(getWidth() - basPrincipal.getAncho());
        }
        
        // Llamamos a la funcion para checar las colisiones de los malos
        checaColisionMalos();
    }
    
    /**
     * checaColisionMalos
     * 
     * Metodo usado para checar la colision de los malos.
     * 
     */
    public void checaColisionMalos(){
        for (Base basMalo : lklMalos) {
            // Checa si algun malo llego hasta el lado izquierdo
            if (basMalo.getX() <= 0) {
                // Se reposiciona malo
                basMalo.setX((int)(Math.random()*(getWidth() * 2) + getWidth()));
                basMalo.setY((int)(Math.random()*(getHeight()-basMalo.getAlto())));
            }

            // Checar si malo colisiona con el principal
            if (basPrincipal.colisiona(basMalo)){
                iContColisionMalo++; // Sumar una colision al contador
                
                // Se reposiciona el malo
                basMalo.setX((int)(Math.random()*(getWidth() * 2) + getWidth()));
                basMalo.setY((int)(Math.random()*(getHeight()-basMalo.getAlto())));
                
                // Checamos si ya van 5 colisiones
                if (iContColisionMalo == 5) {
                    iVidas--; // Quitamos una vida
                    iContColisionMalo = 0; // Se pone el contador  en 0
                    sonidoPain.play();// Suena efecto cuando se pierde una vida
                }
            }  
        }
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>,
     * heredado de la clase Window.<P>
     * En este metodo lo que hace es actualizar la ventana y 
     * define cuando usar ahora el paint1
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("fondo.jpg");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint1
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint1(Graphics graDibujo) {
        if (iVidas > 0) {
            if (bPausa){
                // si la imagen ya se cargo
                if (imaPausa != null) {
                    // llamamos funcion que dibuja el juego
                    dibujarPausa(graDibujo);
                } // si no se ha cargado se dibuja un mensaje 
                else {
                    // Da un mensaje mientras se carga el dibujo	
                    graDibujo.drawString("No se cargo la imagen..", 20, 50);
                }    
            } else {
                // si la imagen ya se cargo
                if (basPrincipal != null && imaImagenFondo != null && 
                    lklMalos != null) {
                    // llamamos funcion que dibuja el juego
                    dibujarJuego(graDibujo);
                } // si no se ha cargado se dibuja un mensaje 
                else {
                    // Da un mensaje mientras se carga el dibujo	
                    graDibujo.drawString("No se cargo la imagen..", 20, 50);
                }
            }
            
        } else {
            // dibujo la imagen de fin de juego
            graDibujo.drawImage(imaGameOver, 0, 0, getWidth(), getHeight(), this);
            // muestra puntaje	
            graDibujo.setFont(new Font("Arial",Font.BOLD,20));
            graDibujo.setColor(Color.white);
            graDibujo.drawString("Puntaje final: " + iPuntos, 30, 50);
        } 
    }
    
    /**
     * dibujarJuego
     * 
     * En este metodo se dibuja la imagen del juego.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void dibujarJuego(Graphics graDibujo){
        // Dibuja la imagen de fondo
        graDibujo.drawImage(imaImagenFondo, 0, 0, getWidth(),getHeight(), this);
        //Dibuja la imagen de principal en el Applet
        basPrincipal.paint(graDibujo, this);
        // Dibujo al malo
        for (Base basMalo : lklMalos){
            basMalo.paint(graDibujo, this);
        }
        // Dibujamos el texto con las vidas y el puntaje
        graDibujo.setFont(new Font("Arial",Font.BOLD,20));
        graDibujo.setColor(new Color(208, 2, 27));
        graDibujo.drawString("Vidas: " + iVidas + "  Puntos: " + iPuntos , 30, 50);
    }
    
    /**
     * dibujarPausa
     * 
     * En este metodo se dibuja la pausa del juego.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void dibujarPausa(Graphics graDibujo){
        // Dibuja la imagen de fondo
        graDibujo.drawImage(imaPausa, 0, 0, getWidth(),getHeight(), this);
        // Dibujamos el texto con las vidas y el puntaje
        graDibujo.setFont(new Font("Arial",Font.BOLD,20));
        graDibujo.setColor(new Color(208, 2, 27));
        graDibujo.drawString("Vidas: " + iVidas + "  Puntos: " + iPuntos , 30, 50);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        /* Dependiendo de si el usuario da click a la flecha izq o der 
        se asigna direccion a Principal */
       
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
            iDireccion = 1;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
            iDireccion = 2;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
            // Disparo hacia arriba
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A){
            // Disparo a 45 grados a la izq
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            // Disparo a 45 grados a la der
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_P) {
            if (bPausa) {
                bPausa = false;
            } else {
                bPausa = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // Se cambia la dirección de Principal a 0 para que no se mueva
        iDireccion = 0;
    }
    
    /**
     * getWidth
     * 
     * Metodo de acceso que regresa el ancho de la pantalla
     * 
     * @return un <code>entero</code> que es el ancho de la pantalla.
     * 
     */
    public int getWidth(){
        return WIDTH;
    }
    
    /**
     * getHeight
     * 
     * Metodo de acceso que regresa la altura de la pantalla
     * 
     * @return un <code>entero</code> que es la altura de la pantalla.
     * 
     */
    public int getHeight(){
        return HEIGHT;
    }
    
    
    
}
