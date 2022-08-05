package features;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class UtilityTool {

    /** Functie ce returneaza imaginea scalata */
    public static BufferedImage scaledImage(BufferedImage original, int width, int height) {
        // crearea imaginii scalate
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // crearea graficii imaginii
        Graphics2D g2 = scaledImage.createGraphics();
        // furnizarea imaginii originale
        g2.drawImage(original, 0, 0, width, height, null);
        // eliberarea contextului grafic redundant
        g2.dispose();

        // returnarea imaginii scalate
        return scaledImage;
    }
}
