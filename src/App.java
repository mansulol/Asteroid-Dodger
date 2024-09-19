import java.awt.Dimension;
import javax.swing.JFrame;

public class App {

    public static void main(String[] args) {
        int anchoTablero = 600;
        int alturaTablero = anchoTablero;


        JFrame frame = new JFrame("Juego de asteroides");
        frame.setSize(new Dimension(anchoTablero, alturaTablero));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JuegoAsteroides juego = new JuegoAsteroides(anchoTablero, alturaTablero);
        // Pedirle que se encargue de los eventos, que actue de escuchador
        frame.add(juego);
        frame.pack();
        juego.requestFocus();

        frame.setVisible(true);
    }
}
