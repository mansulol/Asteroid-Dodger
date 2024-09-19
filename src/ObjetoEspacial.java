import java.awt.*;
import javax.swing.*;

public class ObjetoEspacial {
    int posX;
    int posY;
    int ancho;
    int alto;

    Image img;

    public ObjetoEspacial(int x, int y, int ancho, int alto, String rutaImagen) {
        this.posX = x;
        this.posY = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
    }

    Image getImage(){
        return img;
    }
}
