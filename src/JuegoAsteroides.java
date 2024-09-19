import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class JuegoAsteroides extends JPanel implements ActionListener, KeyListener {
    
    int anchoTablero;
    int alturaTablero;

    Random random = new Random();

    // Nave
    Nave nave;
    int posXNave = 300;
    int posYNave = 500;
    int anchoNave = 30;
    int altoNave = anchoNave;

    // Asteroides
    ArrayList<Asteroide> asteroides;
    int asteroideX = 0;
    int asteroideY = 0;
    int anchoAsteroide = 50;
    int altoAsteroide = anchoAsteroide;
    int altoMaxAsteroide = 70;
    int anchoMaxAsteroide = 75;

    int maxAsteroides = 5;

    Asteroide asteroide;

    // Rutas de imagenes
    String rutaImgNave;
    String rutaImgAsteroide;

    // Logica del juego
    Timer gameLoop;
    Timer placeAsteroids;
    Timer scoreTimer;
    int score = 0;
    int maxScore = 0;
    boolean gameOver = false;
    int diferenciaAsteroidesAltura = 300;
    int delayAsteroides = 1500;
    int velocidadConstante = 4;
    int velocidadY = 0;
    int velocidadX = 0;
    int velocidadYAsteroide = 2;

    JuegoAsteroides(int anchoTablero, int alturaTablero){
        this.anchoTablero = anchoTablero;
        this.alturaTablero = alturaTablero;
        
        setPreferredSize(new Dimension(this.anchoTablero, this.alturaTablero));
        setBackground(Color.BLACK);

        // Es para que se encarga de escuchar los eventos
        setFocusable(true);
        // Para que se encargue de llamar a los metodos de encargados de los key events
        addKeyListener(this);

        // Cargar assets
        rutaImgNave = "./imgs/nave.png";
        rutaImgAsteroide = "./imgs/asteroide_2.png";

        // Instanciar Objetos
        nave = new Nave(posXNave, posYNave, anchoNave, altoNave, rutaImgNave);
        asteroides = new ArrayList<Asteroide>();

        // Temporizador del juego
        // Este metodo llama 30 veces en un segundo a esta clase, lo que activa el escuchador de la clase
        // 1000 ms = 1s => 30 veces/1s
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // Temporizador para poner asteroides
        placeAsteroids = new Timer(delayAsteroides, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placeAsteroids();
            }
        });
        placeAsteroids.start();

        // Temporizador del score
        scoreTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                score++;
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        });
        scoreTimer.start();
    }
    
    // Este metodo permite pintar en la pantalla usando un objeto de tipo graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // Metodo para pintar en la pantalla
    public void draw(Graphics g){
        //Nave
        g.drawImage(nave.getImage(), nave.posX, nave.posY, nave.ancho, nave.alto, null);

        // Asterioides
        for (int i=0; i<asteroides.size(); i++) {
            Asteroide nuevoAsteroide = asteroides.get(i);

            g.drawImage(nuevoAsteroide.img, nuevoAsteroide.posX, nuevoAsteroide.posY, nuevoAsteroide.ancho, nuevoAsteroide.alto, null);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 18));

        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over: "+String.valueOf(score), 10, 20);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Presione barra espaciadora para reintentar... ", 10, 40);
            g.drawString("Best Score: "+String.valueOf(maxScore), 10, 60);
        }else{
            g.setColor(Color.WHITE);
            g.drawString("Score: "+String.valueOf(score), 10, 20);
        }
        
    }

    public void placeAsteroids(){
        rutaImgAsteroide = "./imgs/asteroide_"+random.nextInt(1, 4)+".png";

        for(int i=0; i<maxAsteroides; i++){
            asteroide = new Asteroide( (int) (Math.random() * anchoTablero), -random.nextInt(0, diferenciaAsteroidesAltura), random.nextInt(anchoAsteroide, anchoMaxAsteroide), random.nextInt(altoAsteroide, altoMaxAsteroide), rutaImgAsteroide);
            asteroides.add(asteroide);
        }
        for(int i=0; i<asteroides.size(); i++){
            if(asteroides.get(i).posY + anchoAsteroide > alturaTablero ){
                asteroides.remove(i);
            }
        }
    }

    public void move(){
        // Nave
        nave.posX += velocidadX;
        nave.posX = Math.max(nave.posX, 0);
        nave.posX = Math.min(nave.posX, anchoTablero - nave.ancho);
        
        nave.posY += velocidadY;
        nave.posY = Math.max(nave.posY, 0);
        nave.posY = Math.min(nave.posY, alturaTablero - nave.ancho);

        // Asteroide
       for(int i=0; i<asteroides.size(); i++){
            asteroides.get(i).posY += random.nextInt(velocidadYAsteroide, velocidadYAsteroide*3);
            if ( collision(nave, asteroides.get(i)) ) {
                gameOver = true;
            }
       }
    }

    /* La clase actua como escuchador por lo que cuando ocurre un evento se llama a este metodo
     * y este metodo a su vez llama al metodo repaint que vuelve a pintar los objetos en la pantalla
     */ 
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver){
            placeAsteroids.stop();
            gameLoop.stop();
            placeAsteroids.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if( e.getKeyCode() == KeyEvent.VK_W && e.getKeyCode() == KeyEvent.VK_D){
            velocidadX = velocidadConstante;
            velocidadY = -velocidadConstante;
        }else if( e.getKeyCode() == KeyEvent.VK_W && e.getKeyCode() == KeyEvent.VK_A){
            velocidadX = -velocidadConstante;
            velocidadY = -velocidadConstante;
        }else if( e.getKeyCode() == KeyEvent.VK_S && e.getKeyCode() == KeyEvent.VK_D){
            velocidadX = velocidadConstante;
            velocidadY = velocidadConstante;
        }else if( e.getKeyCode() == KeyEvent.VK_S && e.getKeyCode() == KeyEvent.VK_A){
            velocidadX = -velocidadConstante;
            velocidadY = velocidadConstante;
        }else if( e.getKeyCode() == KeyEvent.VK_W){
            velocidadX = 0;
            velocidadY = -velocidadConstante;
        }else if( e.getKeyCode() == KeyEvent.VK_S){
            velocidadX = 0;
            velocidadY = velocidadConstante;
        }else if( e.getKeyCode() == KeyEvent.VK_D ){
            velocidadX = velocidadConstante;
            velocidadY = 0;
        }else if( e.getKeyCode() == KeyEvent.VK_A ){
            velocidadX = -velocidadConstante;
            velocidadY = 0;
        }else if( e.getKeyCode() == KeyEvent.VK_SPACE ){
            if(gameOver){
                reiniciar();
            }
        }

    }

    public boolean collision(Nave nave, Asteroide asteroide){
        
        /*return nave.posX + nave.ancho/2 == asteroide.posX + asteroide.ancho/2
            && nave.posY + nave.alto/2 == asteroide.posX + asteroide.alto/2;
        */
            
        return nave.posX < asteroide.posX + asteroide.ancho/2 + nave.ancho/2 && // El asteroide no entre en la parte trasera de la nave
                nave.posX + nave.ancho/2 > asteroide.posX &&                    // El asteroide no entre en la parte delantera de la nave
                nave.posY < asteroide.posY + asteroide.alto/2 + nave.alto/2 &&  // El asteroide no entre en la parte inferior de la nave
                nave.posY + nave.alto/2 > asteroide.posY;                       // El asteroide no entre en la parte superior de la nave
    }

    public void reiniciar(){
        gameOver = false;
        asteroides.clear();
        score = 0;
        gameLoop.start();
        placeAsteroids.start();
        nave.posX = posXNave;
        nave.posY = posYNave;
        velocidadX = 0;
        velocidadY = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
