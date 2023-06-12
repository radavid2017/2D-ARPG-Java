package environment;

import game.Area;
import game.GamePanel;
import item.equipable.light.DayState;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;

    public void resetDayCounter() {
        dayCounter = 0;
    }

    // Variabile Ciclu Zi-Noapte
    int dayCounter;
    public float filterAlpha = 0f;
    public DayState dayState = DayState.Day;

    public Lighting(GamePanel gp) {
        this.gp = gp;
        setLightSource();
    }

    public void setLightSource() {
        // Creare imagine
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = (Graphics2D) darknessFilter.getGraphics();

        if (gp.player.currentLight == null) {
            g2D.setColor(new Color(0, 0, 0.1f, 0.98f));
        }
        else {
            //        // Creeaza o suprafata de dimensiunea ecranului
//        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

            // Obtine centrul centrul de greutate al cercului luminos
            float centerX = gp.player.screenX + (gp.tileSize)/2f;
            float centerY = gp.player.screenY + (gp.tileSize)/2f;

//        // Obtine x si y din stanga sus a cercului luminos
//        double x = centerX - (circleSize/2d);
//        double y = centerY - (circleSize/2d);
//
//        // Creeaza forma cercului luminos
//        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
//
//        // Creeaza suprafata cercului luminos
//        Area lightArea = new Area(circleShape);
//
//        // Scaderea cercului luminos din dreptunghiul ecranului
//        screenArea.subtract(lightArea);

            // Crearea unui efect de gradare in cadrul cercului luminos
            int gradationSize = 12;
            float darknessLevel = 0.1f;
            float distanceBetween = 0f;
            Color[] colors = new Color[gradationSize];
            float[] fractions = new float[gradationSize];
            for (int i = 0; i < gradationSize; i++) {
                colors[i] = new Color(0, 0, 0.1f, darknessLevel);
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
            RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, gp.player.currentLight.getRadius(), fractions, colors);

            // Setez datele gradientului pe g2D
            g2D.setPaint(gradientPaint);
        }

        g2D.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

//        // Desenez cercul luminos
//        g2D.fill(lightArea);
//
//        // Desenarea dreptunhiului exceptand zona cercului de lumina
//        g2D.fill(screenArea);

        g2D.dispose();
    }

    public void resetDay() {
        dayState = DayState.Day;
        filterAlpha = 0f;
    }

    public void update() {
        if (gp.player.lightUpdated) {
            setLightSource();
            gp.player.lightUpdated = false;
        }

        // Verifica starea zilei
        switch (dayState) {
            case Day -> {
                dayCounter++;

                if (dayCounter > 600) {
                    dayState = DayState.Dusk;
                    dayCounter = 0;
                }
            }

            case Dusk -> {
                filterAlpha += 0.001f; // ecranul devine mai intunecat

                if (filterAlpha > 1f) {
                    filterAlpha = 1f;
                    dayState = DayState.Night;
                }
            }

            case Night -> {
                dayCounter++;

                if (dayCounter > 600) {
                    dayState = DayState.Dawn;
                    dayCounter = 0;
                }
            }

            case Dawn -> {
                filterAlpha -= 0.001f;

                if (filterAlpha < 0f) {
                    filterAlpha = 0f;
                    dayState = DayState.Day;
                }
            }
        }
    }

    public void draw(Graphics2D g2D) {

        if (gp.currentArea == Area.Outside) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        }
        if (gp.currentArea == Area.Outside || gp.currentArea == Area.Dungeon) {
            g2D.drawImage(darknessFilter, 0, 0, null);
        }
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // DEBUG
        String situation = "";
        switch (dayState) {
            case Day -> situation = "Ziua";
            case Dusk -> situation = "Apus";
            case Night -> situation = "Noapte";
            case Dawn -> situation = "Insorit";
        }
        g2D.setColor(Color.white);
        g2D.setFont(gp.ui.font);
        g2D.setFont(g2D.getFont().deriveFont(70f));
        int x = gp.ui.getXForAlignToRightText(situation, gp.screenWidth-200);
        g2D.drawString(situation, x, 75);
    }
}
