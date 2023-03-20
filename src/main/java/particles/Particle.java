package particles;

import entity.Entity;
import game.GamePanel;

import java.awt.*;

public class Particle extends Entity {

    Entity generator;
    Color color;
    int size;
    int xd;
    int yd;

    public Particle(GamePanel gp, Entity generator, Color color,
                    int size, int speed, int maxLife, int xd, int yd) {
        super(gp);
        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxLife = maxLife;
        this.xd = xd;
        this.yd = yd;

        life = maxLife;
        int offset = (gp.tileSize/2) - (size/2);
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset * 1.5f;
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public void update() {

        if (life > 0) {
            life--;

            // GRAVITATIE
            if (life < maxLife/3) {
                yd++;
            }

            worldX += xd*speed;
            worldY += yd*speed;
        }

        if (life == 0) {
            alive = false;
        }
    }

    public void draw(Graphics2D g2D) {
        int screenX = (int) (worldX - getGamePanel().player.worldX + getGamePanel().player.screenX);
        int screenY = (int) (worldY - getGamePanel().player.worldY + getGamePanel().player.screenY);

        g2D.setColor(color);
        g2D.fillRect(screenX, screenY, size, size);
    }
}
