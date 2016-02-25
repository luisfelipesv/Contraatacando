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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 *
 * @authores luisfelipesv y melytc
 * 
 * Luis Felipe Salazar A00817158
 * Melissa Janet Treviño A00816715
 * 
 * 24/FEB/16
 * @version 1.0
 */
public class Contraatacando extends JFrame implements Runnable, KeyListener {

    private static final int WIDTH = 1100;  // Ancho del JFrame
    private static final int HEIGHT = 650;  // Alto del JFrame
    
    private Base basPrincipal;              // Objeto principal

    /* lista de los malos */
    private LinkedList<Malo> lklMalos; 
    private int iRanMalos;
    /* lista de las balas */
    private LinkedList<Bala> lklBalas;
    private Image imaImagenFondo;           // para dibujar la imagen de fondo

   
    /* objetos para manejar el buffer y que la imagen no parpadee */
    private Image imaImagenApplet;          // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;      // Objeto grafico de la Imagen
    private AudioClip sonidoCatch;          // Sonido de colisión de buenos
    private AudioClip sonidoVida;           // Sonido de colisión con malos
    private AudioClip sonidoDispara;        // Sonido al disparar una bala
    
    /* variables para controlar movimiento */
    private int iDireccion;                 // Dirección del personaje principal
    
    /* variable e imagen para el control de vidas */
    private int iVidas;                     // Cantidad de vidas
    private int iContColisionMalo;          // Cantidad de malos colisionados
    private Image imaGameOver;              // Imagen fondo terminado el juego
    private Image imaVida;
    
    /* variable para el control de puntos */
    private int iPuntos;                    // Cantidad de puntos acumulados
    
    /* variables para el control de pausa */
    private boolean bPausa;                 // Booleano para pausar
    private Image imaPausa;                 // Imagen al pausar
    
    /* variable para la bala */
    private Image imaBala;                  // Imagen de la bala.
    private char cDireccion;                // Char para el manejo de dirección.
    
    public Contraatacando(){
        // Inicializamos el contador de las colisiones de los malos.
        iContColisionMalo = 0;
        iVidas = 5;         // Inicializamos las vidas con 5.
        iPuntos = 0;        // Inicializamos los puntos en 0.
        bPausa = false;     // Inicializamos el booleano de pausa.
        
        // Añadir KeyListener para el uso del teclado.
        addKeyListener(this);

        // Inicializamos otras partes del juego.
        inicializoPrincipal();      // Inicialización del principal
        inicializoMalos();          // Inicialización de los malos
        inicializoSonidos();        // Inicialización de los sonidos
        inicializoImagenes();       // Inicialización de las imagenes
        
        lklBalas= new LinkedList<Bala>();   // Creo la lista de las Balas 
        Thread th = new Thread (this);      // Declaración de un hilo
        th.start ();                        // Empieza el hilo
    }
    
    /** 
     * inicializoImagenes
     * 
     * Método que inicializa las imágenes de Fondo, Game Over y Pausa.
     */
    public void inicializoImagenes() {
        // Creo la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("background.jpg");
        imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        
        // Creo la imagen del Game Over.
        URL urlImagenGameOver = this.getClass().getResource("GameOver.png");
        imaGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);
        
        // Creo la imagen de Pausa.
        URL urlImagenPausa = this.getClass().getResource("Pausa.png");
        imaPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
        
        // Creo la imagen de las vidas.
        URL urlImagenVida = this.getClass().getResource("vidaB.png");
        imaVida = Toolkit.getDefaultToolkit().getImage(urlImagenVida);
        
        // Creo la imagen de las balas
        URL urlImagenBala = this.getClass().getResource("vidaR.png");
        imaBala= Toolkit.getDefaultToolkit().getImage(urlImagenBala);
        
    }
    
    /** 
     * inicializoSonidos
     * 
     * Método que inicializa los sonidos del juego.
     */
    public void inicializoSonidos() {
        // Creo el sonido de colisión entre la bala y el malo.
        URL eaURL = Contraatacando.class.getResource("pain.wav");
        sonidoCatch = Applet.newAudioClip(eaURL);
        
        // Creo el sonido de colisión al disparar una bala.
        URL eaURL2 = Contraatacando.class.getResource("kiss.wav");
        sonidoDispara = Applet.newAudioClip(eaURL2);
        
        // Creo el sonido de vida menos.
        URL eaURL3 = Contraatacando.class.getResource("ghost.wav");
        sonidoVida = Applet.newAudioClip(eaURL3);
    }
    
    /** 
     * inicializoPrincipal
     * 
     * Método que inicializa al Principal.
     */
    public void inicializoPrincipal() {
        // Defino la imagen de principal.
        URL urlImagenPrincipal = this.getClass().getResource("pusheen.png");
        
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
     * Método que inicializa la lista de malos.
     */
    public void inicializoMalos() {
        // Creo la lista de los malos.
        lklMalos = new LinkedList<Malo>();
        
        /* genero el random de los malos entre 10 y 15 */
        iRanMalos = (int) (Math.random() * 6) + 10;
        int iRanMalote = (int) (Math.random() * iRanMalos);
                
        // Defino la imagen de los malos.
	URL urlImagenMalos = this.getClass().getResource("dislike.png");
        
        // Creo a los malos.
        for(int iM = 0; iM < iRanMalos; iM++){
            // Creo a un malo.
            Malo mloMalo = new Malo (0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenMalos));
            if (iM == iRanMalote){
                mloMalo.setMalote(true);
            }
            // Añado al malo a la lista.
            lklMalos.add(mloMalo);
        }
        
        // Posiciono a los malos.
        for (Malo mloMalo : lklMalos){
            mloMalo.setX((int)(Math.random()*(getWidth()- mloMalo.getAncho())));
            mloMalo.setY((int)(Math.random()*(-getHeight() * 2)));
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        // Creo un juego nuevo.
        Contraatacando jfrmJuego = new Contraatacando();
        // Ajusto el tamaño de la pantalla.
        jfrmJuego.setSize(WIDTH,HEIGHT);
        
        jfrmJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrmJuego.setVisible(true);
    }
    
    /** 
     * run
     * 
     * Método sobrescrito de la clase <code>Thread</code>.<P>
     * En este método se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     */
    public void run() {
        /* Mientras dure el juego, se actualizan posiciones de jugadores,
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas > 0) {            // Si el principal tiene vidas
            if (!bPausa){               // Y el juego no está en pausa
                actualiza();            // Se actualizan los personajes
                checaColision();        // Se checan las colisiones
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
     * Método que actualiza la posición de los objetos.
     */
    public void actualiza(){
        // Movimiento del principal dependiendo de la dirección.
        switch (iDireccion) {
            case 1:         // Movimiento a la izquierda.
                basPrincipal.setX(basPrincipal.getX() - 3);
                break;
            case 2:         // Movimiento a la derecha.
                basPrincipal.setX(basPrincipal.getX() + 3);
                break;
            default:
                break;
        }
        actualizaMalosyBalas(); // Actualiza a los malos y las balas.
    }
    
    /** 
     * actualizaMalosyBalas
     * 
     * Método que actualiza la posición de los malos y la de las balas. 
     */
    public void actualizaMalosyBalas() {
        // Ciclo para actualizar a cada malo de la lista.
        for (Malo mloMalo : lklMalos){
            // Se actualiza la posicion del malo.
            mloMalo.avanza(basPrincipal); 
        }
        // Ciclo para actualizar a cada bala de la lista.
        for (Bala blaBala : lklBalas){
            // Se actualiza la posicion de la bala.
            blaBala.avanza(); 
        }
    }

    /**
     * checaColision
     * 
     * Método usado para checar la colisión entre objetos.
     */
    public void checaColision(){
        // Checa que el principal no pase el borde izquierdo.
        if (basPrincipal.getX() <= 0) {
            basPrincipal.setX(0);
        } 
        // Checa que el principal no pase el borde derecho.
        if (basPrincipal.getX() + basPrincipal.getAncho() >= getWidth() ) {
            basPrincipal.setX(getWidth() - basPrincipal.getAncho());
        }
        
        // Llamamos a la funcion para checar las colisiones de los malos
        checaColisionMalos();
        checaColisionBalas();
    }
    
    /**
     * checaColisionMalos
     * 
     * Metodo usado para checar la colisión de los malos.
     */
    public void checaColisionMalos(){
        // Checar la colisión de cada malo con la pantalla.
        checaColisionPantallaMalos();
        
        // Checar la colisión de cada malo con el principal.
        checaColisionPrincipalMalos();
        
    }
    
    public void checaColisionPantallaMalos() {
        for (Malo mloMalo : lklMalos) {
            // Checa si algún malo llega hasta el borde de abajo.
            if (mloMalo.getY() >= getHeight()) {
                // Se reposiciona malo en la parte superior de la pantalla.
                mloMalo.setX((int)(Math.random()*(getWidth() - 
                        mloMalo.getAncho())));
                mloMalo.setY((int)(Math.random()*(-getHeight() * 2)));
            }
        }
    }
        
    public void checaColisionPrincipalMalos() {
        for (Malo mloMalo : lklMalos) {
            // Checar si malo colisiona con el principal.
            if (basPrincipal.colisiona(mloMalo)){
                if (iPuntos > 0){
                     iPuntos--;         // Bajamos un punto por la colisión.
                }     
                iContColisionMalo++;    // Sumar una colisión al contador.
                
                // Se reposiciona el malo
                mloMalo.setX((int)(Math.random() * (getWidth() * 2) 
                        + getWidth()));
                mloMalo.setY((int)(Math.random() * (getHeight() 
                        - mloMalo.getAlto())));
                // Checamos si ya van 5 colisiones
                if (iContColisionMalo == 5) { 
                    aumentarVelocidad();        // Aumentamos la velocidad
                    iVidas--;                   // Quitamos una vida
                    iContColisionMalo = 0;      // Se pone el contador  en 0
                    sonidoVida.play();          // Sonido al perder una vida
                }
            }  
        }
    }
    
    /**
     * checaColisionBalas
     * 
     * Método usado para checar la colisión de las balas.
     */
    public void checaColisionBalas(){
        for (int iA = 0; iA < lklBalas.size() ; iA++) {
            Bala blaBala = (Bala) lklBalas.get(iA);
            
            // Checa si alguna bala choca con la pantalla.
            if (blaBala.getX() <= 0 || blaBala.getY() <= 0 || 
                    blaBala.getX() > (getWidth() - blaBala.getAncho())  ) {
                lklBalas.remove(blaBala);           // Se elimina la bala.
            } 
            else {
               // Checar si el malo colisiona con alguna bala.
                for (int iM = 0; iM < lklMalos.size(); iM++) {
                    Malo mloMalo = (Malo) lklMalos.get(iM);
                    if (blaBala.colisiona(mloMalo)){
                        lklBalas.remove(blaBala);   // Se elimina la bala.
                        // Se reposiciona malo en la parte superior.
                        mloMalo.setX((int)(Math.random() * (getWidth() - 
                                mloMalo.getAncho())));
                        mloMalo.setY(- 100);
                        iPuntos += 10;              // Se suman 10 puntos.
                        sonidoCatch.play();         // Emite el sonido.
                    }
                } 
            }
        }
    }
        
    /**
     * aumentarVelocidad
     * 
     * Método usado para aumentar la velocidad de todos los malos.
     */
    public void aumentarVelocidad(){
        // Ciclo para aumentar la velocidad para cada malo.
        for (Malo mloMalo : lklMalos) {
            int iV = mloMalo.getVel();
            mloMalo.setVel(++iV);
        }
    }
    
    public void disparaBala() {
        // Disparo según la direccion del caracter.
            Bala blaBala = new Bala(basPrincipal.getX(), 
                    basPrincipal.getY(), imaBala, cDireccion);
            lklBalas.add(blaBala);
            
            // Emitir sonido de disparo.
            sonidoDispara.play();
    }
    
    /**
     * paint
     * 
     * Método sobrescrito de la clase <code>JFrame</code>,
     * heredado de la clase Window.<P>
     * En este método lo que hace es actualizar la ventana y 
     * define cuando usar ahora el paint1.
     * 
     * @param graGrafico es el <code>objeto gráfico</code> usado para dibujar.
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("background.jpg");
        Image imaImagenFondo = Toolkit.getDefaultToolkit()
                .getImage(urlImagenFondo);
        graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), 
                getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint1
     * 
     * Método sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este método se dibuja la imagen con la posición actualizada,
     * además que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> 
     * usado para dibujar.
     */
    public void paint1(Graphics graDibujo) {
        if (iVidas > 0) {
            // Si la imagen ya se cargo
            if (basPrincipal != null && imaImagenFondo != null && 
                    lklMalos != null && imaVida != null && imaPausa != null) {
                // Llamamos función que dibuja el juego.
                dibujarJuego(graDibujo);
                dibujarVidas(graDibujo);
                dibujarPausa(graDibujo);
            } else {    // Si no se ha cargado
                // Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 50);
            }
        } else {
            // Dibujo la imagen de fin de juego.
            graDibujo.drawImage(imaGameOver, 0, 0, getWidth(), getHeight(), 
                    this);
            // Muestro el puntaje.
            graDibujo.setFont(new Font("Arial",Font.BOLD,20));
            graDibujo.setColor(Color.white);
            graDibujo.drawString("Puntaje final: " + iPuntos, 30, 50);
            // Aviso al usuario para pulsar reiniciar 
            graDibujo.drawString("Pulsa R para reiniciar el juego...", 
                    getWidth()/2-100, getHeight()/2+100);
        } 
    }
    
    /**
     * dibujarJuego
     * 
     * Método donde se dibuja la imagen del juego.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> 
     * usado para dibujar.
     */
    public void dibujarJuego(Graphics graDibujo){
        // Dibuja la imagen de fondo.
        graDibujo.drawImage(imaImagenFondo, 0, 0, getWidth(),getHeight(), this);
        
        //Dibuja la imagen de principal en el Applet.
        basPrincipal.paint(graDibujo, this);
        
        // Dibujo cada malo.
        for (Malo mloMalo : lklMalos){
            mloMalo.paint(graDibujo, this);
        }
        // Dibujo cada bala.
        for (Bala blaBala : lklBalas){
            blaBala.paint(graDibujo, this);
        }
        
        // Dibujamos el texto con las vidas y el puntaje.
        graDibujo.setFont(new Font("Arial",Font.BOLD,25));
        graDibujo.setColor(new Color(41, 217, 218));
        graDibujo.drawString("Puntos: " + iPuntos , 30, 50);
    }
    
    /**
     * dibujarPausa
     * 
     * Método donde se dibuja la pausa del juego.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> 
     * usado para dibujar.
     */
    public void dibujarPausa(Graphics graDibujo){
        // Si el booleano está encendido.
        if (bPausa){
            // Dibuja la imagen de fondo
            graDibujo.drawImage(imaPausa, 0, 0, getWidth(),getHeight(), this);
            
            // Dibujamos el texto con las vidas y el puntaje
            graDibujo.setFont(new Font("Arial",Font.BOLD,25));
            graDibujo.setColor(new Color(255, 255, 255));
            graDibujo.drawString("Presiona la tecla P para salir de Pausa", 
                getWidth()/2 - 220, getHeight()/4);      
        }
    }
    
    /**
     * dibujarPausa
     * 
     * Método donde se dibujan las vidas del juego.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> 
     * usado para dibujar.
     */
    public void dibujarVidas(Graphics graDibujo){
        // Ciclo para dibujar cada vida en la pantalla con la imagen.
        for (int iV = 0; iV < iVidas; iV++){
            graDibujo.drawImage(imaVida, getWidth()- 40 - (32 * iV), 30, this);
        }
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
        // Se cambia la dirección de Principal a 0 para que no se mueva.
        iDireccion = 0;
        // Según la dirección se dispara la bala.
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
            cDireccion = ' ';
            disparaBala();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A){
            cDireccion = 'a';
            disparaBala();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            cDireccion = 's';
            disparaBala();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_R) {
            if (iVidas == 0) {
                // Se reinicia el juego.
                reinicia();
            }
        }
    }
    
    public void reinicia() {
        // Elimino el juego.
        dispose();
        
        // Creo un juego (jFrame) nuevo.
        Contraatacando jfrmJuego = new Contraatacando();
        
        // Ajusto el tamaño de la pantalla.
        jfrmJuego.setSize(WIDTH,HEIGHT);
        jfrmJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrmJuego.setVisible(true);
    }
    
    /**
     * getWidth
     * 
     * Método de acceso que regresa el ancho de la pantalla
     * 
     * @return un <code>entero</code> que es el ancho de la pantalla.
     */
    public int getWidth(){
        return WIDTH;
    }
    
    /**
     * getHeight
     * 
     * Método de acceso que regresa la altura de la pantalla
     * 
     * @return un <code>entero</code> que es la altura de la pantalla.
     */
    public int getHeight(){
        return HEIGHT;
    }
}