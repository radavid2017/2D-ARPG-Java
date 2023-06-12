package entity;

import features.Camera;
import game.GamePanel;

import java.awt.*;

public class PlayerDummy extends Creature {

    public static final String npcName = "Dummy";

    public PlayerDummy(GamePanel gp) {
        super(gp);

        name = npcName;
        setupIdle("res/player/" + gp.player.characterClassPath);
        currentAnimation = idle.up;
    }

    public void draw(Graphics2D g2D) {
        screenX = getScreenX();
        screenY = getScreenY();

        // Instantiere camera
        camera = new Camera(worldX, worldY, screenX, screenY, gPanel);

        // Management Camera
        camera.drawEntity(g2D, currentAnimation.nextFrame());
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
