package environment;

import game.GamePanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;

    public Lighting(GamePanel gp, float circleSize) {
        // Creare imagine
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = (Graphics2D) darknessFilter.getGraphics();

        // Creeaza o suprafata de dimensiunea ecranului
        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

        // Obtine centrul centrul de greutate al cercului luminos
        float centerX = gp.player.screenX + (gp.tileSize)/2f;
        float centerY = gp.player.screenY + (gp.tileSize)/2f;

        // Obtine x si y din stanga sus a cercului luminos
        double x = centerX - (circleSize/2d);
        double y = centerY - (circleSize/2d);

        // Creeaza forma cercului luminos
        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);

        // Creeaza suprafata cercului luminos
        Area lightArea = new Area(circleShape);

        // Scaderea cercului luminos din dreptunghiul ecranului
        screenArea.subtract(lightArea);

        // Crearea unui efect de gradare in cadrul cercului luminos
        int gradationSize = 12;
        float darknessLevel = 0.1f;
        float distanceBetween = 0f;
        Color[] colors = new Color[gradationSize];
        float[] fractions = new float[gradationSize];
        for (int i = 0; i < gradationSize; i++) {
            colors[i] = new Color(0, 0, 0, darknessLevel);
            darknessLevel += 0.0816f;

            if (i == 1) {
                distanceBetween = 0.4f;
            }
            else if (i < 3) {
                distanceBetween += 0.1f;
            }
            else {
                distanceBetween += 0.05f;
            }
            fractions[i] = distanceBetween;
        }

        // Creeaza setari de vopsea in gradient pentru cercul de lumina
        RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, (circleSize/2), fractions, colors);

        // Setez datele gradientului pe g2D
        g2D.setPaint(gradientPaint);

        // Desenez cercul luminos
        g2D.fill(lightArea);

        // Desenarea dreptunhiului exceptand zona cercului de lumina
        g2D.fill(screenArea);

        g2D.dispose();
    }

    public void draw(Graphics2D g2D) {
        g2D.drawImage(darknessFilter, 0, 0, null);
    }
}
