package monster;

import entity.ArtificialIntelligence;
import entity.Entity;
import entity.TypeAI;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Monster extends ArtificialIntelligence {

    TypeMonster typeMonster;

    public Monster(GamePanel gp) {
        super(gp);
        typeAI = TypeAI.Monster;
    }

    public void update() {
        AI();
        super.update();


        if (getGamePanel().collisionDetector.checkPlayer(this)) {
            doDamage();
        }
    }

    private void doDamage() {
        if (!getGamePanel().player.invincible) {
            // ofera daune
            getGamePanel().player.life -= 1;
            getGamePanel().player.invincible = true;
        }
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
