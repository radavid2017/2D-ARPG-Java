package monster;

import entity.ArtificialIntelligence;
import entity.AttackStyle;
import entity.Entity;
import entity.TypeAI;
import game.GamePanel;
import item.Item;
import item.consumable.coin.OBJ_Coin;
import item.consumable.potion.PotionRed;
import item.equipable.shield.BlueShield;
import item.equipable.weapon.Weapon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class Monster extends ArtificialIntelligence {

    TypeMonster typeMonster;
    AttackStyle attackStyle;
    Weapon weapon;

    boolean hpBarOn = false;
    int hpBarCounter = 0;

    int shotAvailableCounter = 0;

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

            weapon.tryDoAttack(this, getGamePanel().player);

//            switch (attackStyle) {
//                case Touching -> {
//                    touchingDamage(getGamePanel().player);
//                }
//            }

            getGamePanel().player.invincible = true;
        }
    }

    /** Bara de viata a monstrului */
    public void showHPBar(Graphics2D g2D) {

        double oneScale = (double) getGamePanel().tileSize / maxLife;
        double hpBarValue = oneScale * life;

        int hpBarX = (int) screenX;
        int hpBarY = (int) screenY;

        g2D.setColor(new Color(35, 35, 35));
        g2D.fillRect((int) hpBarX - 1, (int) hpBarY - 16, getGamePanel().tileSize, 10);
        g2D.setColor(new Color(255, 0, 30));
        g2D.fillRect((int) hpBarX, (int) hpBarY - 15, (int) hpBarValue, 10);

//        System.out.println("screenX: " + screenX + " screenY: " + screenY);
//        System.out.println("worldX: " + worldX + " worldY: " + worldY);

        hpBarCounter ++;
        if (hpBarCounter >= 150) {
            hpBarCounter = 0;
            hpBarOn = false;
        }
    }

    public abstract void checkDrop();

    public void dropItem(Item item) {

        item.setPosition(worldX, worldY); // atribuim pozitia obiectului distrus (monstrul mort)

        for (int i = 0; i < getGamePanel().objects[1].length; i++) {
            if(getGamePanel().objects[getGamePanel().currentMap][i] == null) {
                getGamePanel().objects[getGamePanel().currentMap][i] = item;
                break;
            }
        }
//        getGamePanel().objects.add(item);
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
