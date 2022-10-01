package hud.window;

import hud.HUD;

import java.awt.*;

public abstract class WindowHUD extends HUD {

    public WindowHUD(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void drawWindow(Graphics2D g2D) {
        Color c = new Color(0, 0, 0, 210);
        g2D.setColor(c);
        g2D.fillRoundRect(x,y,width,height,35, 35);

        c = new Color(255, 255, 255);
        g2D.setColor(c);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

}
