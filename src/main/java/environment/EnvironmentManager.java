package environment;

import game.GamePanel;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setup() {
        lighting = new Lighting(gp, gp.screenHeight/2.3f);
    }

    public void draw(Graphics2D g2D) {
        lighting.draw(g2D);
    }
}
