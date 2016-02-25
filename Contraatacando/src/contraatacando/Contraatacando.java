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
import javax.swing.JFrame;

/**
 *
 * @authores luisfelipesv y melytc
 * 
 * Luis Felipe Salazar A00817158
 * Melissa Janet Treviño A00816715
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
        URL urlImagenFondo = this.getClass().getResource("fondo.jpg");
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
        
    }
    
    /** 
     * inicializoSonidos
     * 
     * Método que inicializa los sonidos del juego.
     */
    public void inicializoSonidos() {
        // Creo el sonido de colisión entre la bala y el malo.
        URL eaURL = Contraatacando.class.getResource("catch.wav");
        sonidoCatch = Applet.newAudioClip(eaURL);
        
        // Creo el sonido de colisión al disparar una bala.
        //URL eaURL2 = Contraatacando.class.getResource("dispara.wav");
        //sonidoDispara = Applet.newAudioClip(eaURL);
        
        // Creo el sonido de vida menos.
        URL eaURL3 = Contraatacando.class.getResource("pain.wav");
        sonidoVida = Applet.newAudioClip(eaURL3);
    }
    
    /** 
     * inicializoPrincipal
     * 
     * Método que inicializa al Principal.
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
     * Método que inicializa a la lista de malos.
     */
    public void inicializoMalos() {
        // Creo la lista de los malos.
        lklMalos = new LinkedList<Malo>();
        
        /* genero el random de los malos entre 10 y 15 */
        iRanMalos = (int) (Math.random() * 6) + 10;
        int iRanMalote = (int) (Math.random() * iRanMalos);
                
        // Defino la imagen de los malos.
	URL urlImagenMalos = this.getClass().getResource("malo.png");
        
        // Creo a los malos.
        for(int iI = 0; iI < iRanMalos; iI++){
            // Creo a un malo.
            Malo mloMalo = new Malo (0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenMalos));
            if (iI == iRanMalote){
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
        /* mientras dure el juego, se actualizan posiciones de jugadores,
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
            case 1:
                basPrincipal.setX(basPrincipal.getX() - 3);
                break;
            case 2:
                basPrincipal.setX(basPrincipal.getX() + 3);
                break;
            default:
                break;
        }
        actualizaMalosyBalas(); // Actualiza a los malos y las balas
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
    }
    
    /**
     * checaColisionMalos
     * 
     * Metodo usado para checar la colision de los malos.
     * 
     */
    public void checaColisionMalos(){
        for (Malo mloMalo : lklMalos) {
            // Checa si algun malo llego hasta el fondo
            if (mloMalo.getY() >= getHeight()) {
                // Se reposiciona malo
                mloMalo.setX((int)(Math.random()*(getWidth()- mloMalo.getAncho())));
                mloMalo.setY((int)(Math.random()*(-getHeight() * 2)));
            }

            // Checar si malo colisiona con el principal
            if (basPrincipal.colisiona(mloMalo)){
                if (iPuntos > 0){
                     iPuntos--; 
                }
                            // Bajamos un punto por la colisión
                iContColisionMalo++;    // Sumar una colision al contador
                
                // Se reposiciona el malo
                mloMalo.setX((int)(Math.random()*(getWidth() * 2) + getWidth()));
                mloMalo.setY((int)(Math.random()*(getHeight()-mloMalo.getAlto())));
                
                // Checamos si ya van 5 colisiones
                if (iContColisionMalo == 5) {
                    aumentarVelocidad();
                    iVidas--; // Quitamos una vida
                    iContColisionMalo = 0; // Se pone el contador  en 0
                    sonidoVida.play();// Suena efecto cuando se pierde una vida
                    
                }
            }  
        }
        
    }
    
    /**
     * aumentarVelocidad
     * 
     * Metodo usado para aumentar la velocidad de todos los malos.
     * 
     */
    public void aumentarVelocidad(){
        for (Malo mloMalo : lklMalos) {
            int iV = mloMalo.getVel();
            mloMalo.setVel(++iV);
        }
    }
    
    /**
     * checaColisionBalas
     * 
     * Metodo usado para checar la colision de las balas.
     * 
     */
    public void checaColisionBalas(){
        for (Bala blaBala : lklBalas) {
            // Checa si algun malo choco con la ventana
            if (blaBala.getX() <= 0 || blaBala.getY() <= 0 || 
                    blaBala.getX() > (getWidth() - blaBala.getAncho())  ) {
                // Se elimina la bala
                
            } else {
               // Checar si malo colisiona con algun malo
                for (Malo mloMalo : lklMalos) {
                    if (blaBala.colisiona(mloMalo)){
                        // Desaparecemos la bala y al malo
                        // Sumamos 10 puntos
                    }
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
            // si la imagen ya se cargo
            if (basPrincipal != null && imaImagenFondo != null && 
                    lklMalos != null && imaVida != null && imaPausa != null) {
                // llamamos funcion que dibuja el juego
                dibujarJuego(graDibujo);
                dibujarVidas(graDibujo);
                dibujarPausa(graDibujo);
            } // si no se ha cargado se dibuja un mensaje 
            else {
                // Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 50);
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
        graDibujo.setFont(new Font("Arial",Font.BOLD,25));
        graDibujo.setColor(new Color(255, 255, 255));
        graDibujo.drawString("Puntos: " + iPuntos , 30, 50);
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
     * En este metodo se dibuja la pausa del juego.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void dibujarVidas(Graphics graDibujo){
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
        // Se cambia la dirección de Principal a 0 para que no se mueva
        iDireccion = 0;
        
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
            // Disparo hacia arriba             cTipo = a
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A){
            // Disparo a 45 grados a la izq     cTipo = i
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            // Disparo a 45 grados a la der     cTipo = d
        }
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
