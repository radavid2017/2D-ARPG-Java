package npc;

import entity.ArtificialIntelligence;
import entity.Entity;
import entity.TypeAI;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.random.RandomGenerator;

public abstract class NPC extends ArtificialIntelligence {

    TypeNPC typeNPC;

    public NPC(GamePanel gp) {
        super(gp);
        typeAI = TypeAI.NPC;
    }

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
