package npc;

import entity.Entity;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.random.RandomGenerator;

public abstract class NPC extends Entity {

    public int actionLockCounterDirection = 0;
    public int actionLockCounterInMotion = 0;
    public int timeToChangeInMotion;
    public int timeToChangeDirection;
    TypeNPC typeNPC;

    public NPC(GamePanel gp) {
        super(gp);
    }

    public abstract void AI();

    public void update() {
        AI();
        super.update();
    }

    public void draw(Graphics2D g2D) {

        super.draw(g2D);

        BufferedImage sprite;

        if (inMotion)
            sprite = movement.manageAnimations(this, direction);
        else
            sprite = idle.manageAnimations(this, direction);

        // Management Camera
        camera.drawEntity(g2D, sprite);

        drawSolidArea(g2D);
    }
}
