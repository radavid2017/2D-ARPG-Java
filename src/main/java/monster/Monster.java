package monster;

import entity.ArtificialIntelligence;
import entity.AttackStyle;
import entity.Entity;
import entity.TypeAI;
import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Monster extends ArtificialIntelligence {

    TypeMonster typeMonster;
    AttackStyle attackStyle;

    boolean hpBarOn = false;
    int hpBarCounter = 0;

    public Monster(GamePanel gp) {
        super(gp);
        typeAI = TypeAI.Monster;
        this.invincibleTime = 20;
    }

    public void damageReaction() {

        actionLockCounterDirection = 0;
        actionLockCounterInMotion = 0;
        direction = getGamePanel().player.direction;
        inMotion = true;
    }

    public void update() {
        AI();
        super.update();


        if (getGamePanel().collisionDetector.checkPlayer(this)) {
            doDamage();
        }
    }

    public void doDamage() {
        if (!getGamePanel().player.invincible && !dying) {
            // ofera daune
            getGamePanel().playSE("receivedamage.wav");

            switch (attackStyle) {
                case Touching -> {
                    touchingDamage(getGamePanel().player);
                }
            }

            getGamePanel().player.invincible = true;
        }
    }

    /** Bara de viata a monstrului */
    public void showHPBar(Graphics2D g2D) {

        double oneScale = (double) getGamePanel().tileSize / maxLife;
        double hpBarValue = oneScale * life;

        g2D.setColor(new Color(35, 35, 35));
        g2D.fillRect((int) screenX - 1, (int) screenY - 16, getGamePanel().tileSize, 10);
        g2D.setColor(new Color(255, 0, 30));
        g2D.fillRect((int) screenX, (int) screenY - 15, (int) hpBarValue, 10);

        hpBarCounter ++;
        if (hpBarCounter >= 150) {
            hpBarCounter = 0;
            hpBarOn = false;
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

//        System.out.println("monster life: " + life);

        // Monster HP Bar
        if (hpBarOn) showHPBar(g2D);

        if (invincible) {
            hpBarOn = true;
            hpBarCounter = 0;
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        if (dying) {
            isSolid = false;
            hpBarOn = false;
            dyingEffect(g2D);
        }

        camera.drawEntity(g2D, sprite);

        drawSolidArea(g2D);

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
