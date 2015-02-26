/**
 * Juego JFrame
 * 
 * Brick Braker
 * 
 * @author Gonzalo Gutierrez & Isaac Siso
 * 
 * @date 2/25/2015
 */

package jframe1;
import java.applet.AudioClip;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.net.URL;
import java.util.LinkedList;





public class Bricks extends JFrame implements Runnable, KeyListener{
    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maximo numero de personajes por alto
    private static final int iWidth = 600; //ancho de la pantalla
    private static final int iHeight = 800; //alto de la pantalla
    private Base basMalo;        // Objeto Bate
    private LinkedList <Base> lilBricks;   //Linkedlist de Bricks
    private SoundClip aucSonido1; 
    private Image    imaImagenApplet;   // 	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private int iDireccion;
    private int iAcabar;
    private boolean bListo;
    private boolean bPerder2;
 
    private boolean bPause;    //Pausa
    
    private Base basPelota;
    private int iMoveX;
    private int iMoveY;
    private boolean bPintar;
    private int iMovBola;
    private boolean bPerder;
    
    public Bricks (){
       
        
        aucSonido1 = new SoundClip("sonido.wav");
        
        URL urlImagenBate = this.getClass().getResource("Calzon.gif");
        
        int iPosX = (iMAXANCHO /2) * iWidth / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * iHeight / iMAXALTO;        
	basMalo = new Base(iPosX,iPosY, 100,100,
                Toolkit.getDefaultToolkit().getImage(urlImagenBate));
        
        URL urlImagenPelota = this.getClass().getResource("Hanks.gif");
        
         iPosX = (iMAXANCHO /2) * iWidth / iMAXANCHO;
         iPosY = (iMAXALTO - 1) * iHeight / iMAXALTO;        
	basPelota = new Base(iPosX,iPosY, 40,40,
                Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
    
        
        URL urlImagenBricks = this.getClass().getResource("azul1.gif");
        lilBricks = new LinkedList <Base> ();
        
        
        int iCounterX = 0;
        int iCounterY = 0;
        for (int iI = 0; iI < 12; iI++){
            iPosX = 110 + iCounterX;  
            iPosY = 40 + iCounterY;
            
            iCounterX+= 100;
            if (iCounterX >= 400) {
                iCounterY += 100;
                iCounterX = 0;
            }
            Base basBrick = new Base(iPosX,iPosY,60,60,
            Toolkit.getDefaultToolkit().getImage(urlImagenBricks));
            lilBricks.add(basBrick);
        }
        
        iDireccion = 0;
        bListo = true;
        iMoveX = 3;
        iMoveY = 3;
        bPintar = true;
        iMovBola = 1;
        bPerder = true;
        bPerder2 = true;
        

        //Inicializando Pausa
        bPause = false;
        iAcabar = 0;
        
       
        
        //Inicializando el keylistener
        addKeyListener(this);
        
        //Inicializacion del Hilo
        Thread th = new Thread (this);
        th.start();
    }
    
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        
        while (bPerder) {
            
            repaint();
            
                if(!bPause){
                    actualiza();
                    checaColision();
                }
                
                try	{
                    // El thread se duerme.
                    Thread.sleep (20);
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
        switch(iDireccion) {
           
            case 3: {
                basMalo.setX(basMalo.getX() - 10);
                break;    //se mueve hacia izquierda
            }
            case 4: {
                basMalo.setX(basMalo.getX() + 10);
                break;    //se mueve hacia derecha	
            }
        }
        
        basPelota.setX(basPelota.getX() - iMoveX);
        basPelota.setY(basPelota.getY() - iMoveY);
        
        
        
        
        
        
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        for (Base basBrick : lilBricks) {

                if (basPelota.intersecta(basBrick)){

                    
                    iAcabar++;
                    aucSonido1.play();
                    

                } 
          }
            
        if (basMalo.getX() < 0) {
            iDireccion = 0;
            basMalo.setX(0);
        }
                      	
         if (basMalo.getX() + basMalo.getAncho() > iWidth) {
            iDireccion = 0;
            basMalo.setX(iWidth - basMalo.getAncho());
        }
        
         
        if (iMovBola == 1) {
            for (Base basBrick : lilBricks) {

                if (basPelota.intersecta(basBrick)){

                    
                    iMoveY = - 3;
                    iMoveX = + 3;  
                    basBrick.setX(-100);
                    basBrick.setY(-100);
                    iMovBola = 2;
                    

                } 
          }
            
            if (basPelota.getX() < 0) {
                iMoveY = + 3;
                iMoveX = - 3;  
                iMovBola = 4;
            }
            if (basPelota.getY() < 50) {
                iMoveY = - 3;
                iMoveX = + 3;  
                iMovBola = 2;
            }
            
        }
        
        if (iMovBola == 2) {
            if (basPelota.getX() < 0) {
                iMoveY = - 3;
                iMoveX = - 3;
                iMovBola = 3;
            }
            if (basPelota.intersecta(basMalo)){

                    
                    iMoveY = + 3;
                    iMoveX = + 3;  
                   
                    iMovBola = 1;

                } 
            if (basPelota.getY() > iHeight) {
               
                bPerder2 = false;
            }
            
            
            for (Base basBrick : lilBricks) {

                if (basPelota.intersecta(basBrick)){

                   
                    iMoveY = + 3;
                    iMoveX = + 3;  
                    basBrick.setX(-100);
                    basBrick.setY(-100);
                    iMovBola = 1;

                } 
          }
        }
        
        if (iMovBola == 3) {
              if (basPelota.getX() >= iWidth -100) {
                  iMoveY = - 3;
                  iMoveX = + 3;
                  iMovBola = 2;
              }
              if (basPelota.intersecta(basMalo)){

                     
                      iMoveY = + 3;
                      iMoveX = - 3;  
                      
                      iMovBola = 4;

                  } 
              
              if (basPelota.getY() > iHeight) {
                
                bPerder2 = false;
            }


              for (Base basBrick : lilBricks) {

                  if (basPelota.intersecta(basBrick)){

                      
                      iMoveY = + 3;
                      iMoveX = - 3;  
                      basBrick.setX(-100);
                      basBrick.setY(-100);
                      iMovBola = 4;

                  } 
            }
          }
        if (iMovBola == 4) {
              if (basPelota.getX() > iWidth -100) {
                  iMoveY = + 3;
                  iMoveX = + 3;
                  iMovBola = 1;
              }
              if (basPelota.getY() <= 90) {
                iMoveY = - 3;
                iMoveX = - 3;
                iMovBola = 3;
            }


              for (Base basBrick : lilBricks) {

                  if (basPelota.intersecta(basBrick)){

                   
                      iMoveY = - 3;
                      iMoveX = - 3;  
                      basBrick.setX(-100);
                      basBrick.setY(-100);
                      iMovBola = 3;

                  } 
            }
          }  
         
     
       
    }
        
     
    
        /*
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
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
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, iWidth, iHeight, this);

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
    public void paint1 (Graphics graDibujo) {
      
        if(bPerder2 && bListo){
            
        
            if (basMalo != null && basPelota != null ) {
               basMalo.paint(graDibujo, this);
               basPelota.paint(graDibujo, this);



                for(Base basBrick : lilBricks){
                    basBrick.paint(graDibujo, this);

                }   

            }
      }
        
        if (!bPerder2) {
            graDibujo.setColor(Color.white);
            graDibujo.drawString("GAME OVER!!", 250, 200);
            
        }
        
        if (iAcabar >= 12) {
        
        graDibujo.drawString("GANASTE!!!!!!", 250, 200);
        bListo = false;
        
        }
       
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono flecha izquierda
            iDireccion = 3;
        } 
        
        else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
            iDireccion = 4;
        }
        else if (ke.getKeyCode() == KeyEvent.VK_R) {
            
            bPerder2 = true;
            
        }
  
        //Al presionar P se pausa el juego
        if (ke.getKeyCode() == KeyEvent.VK_P){
            bPause = !bPause;
        }
        
        
       
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono flecha izquierda
            iDireccion = 1;
        } 
        
        else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
            iDireccion = 1;
        }
    }
    
    
    /**
     * Clase main del programa
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Bricks juego = new Bricks();
        juego.setSize(iWidth, iHeight);
        juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juego.setVisible(true);
    }

    
    
}
