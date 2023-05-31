package monster;

import animations.TypeAnimation;
import entity.ArtificialIntelligence;
import entity.AttackStyle;
import entity.Entity;
import entity.TypeAI;
import features.Direction;
import game.GamePanel;
import item.Item;
import item.equipable.weapon.Weapon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public abstract class Monster extends ArtificialIntelligence {

    TypeMonster typeMonster;
    AttackStyle attackStyle;
    Weapon weapon;

    boolean hpBarOn = false;
    int hpBarCounter = 0;

    int shotAvailableCounter = 0;
    final int shotAvailableTrigger = 30;

    public BufferedImage sprite;

    public Monster(GamePanel gp) {
        super(gp);
        typeAI = TypeAI.Monster;
        this.invincibleTime = 20;
    }

    public void damageReaction() {

        actionLockCounterDirection = 0;
        actionLockCounterInMotion = 0;
//        direction = getGamePanel().player.direction;
        onPath = true;
        inMotion = true;
    }

    public void update() {
        AI();
        super.update();


        if (getGamePanel().collisionDetector.checkPlayer(this)) {
            doDamage();
        }

        if (onPath) {
            checkStopChasingOrNot(getGamePanel().player, 8, 100);
        }
        else {
            checkStartChasingOrNot(getGamePanel().player, 5, 100);
        }
    }

    public void doDamage() {
        if (!getGamePanel().player.invincible && !dying) {
            // ofera daune
            if (weapon != null) {
                getGamePanel().playSE("receivedamage.wav");
                weapon.tryDoAttack(this, getGamePanel().player);
            }
            else {
                switch (attackStyle) {
                    case Nearly -> {
                        if (currentAnimation.currentFrame == 1) {
                            nearlyDamage(getGamePanel().player);
                        }
                    }
                }
            }

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

//        for (int i = 0; i < getGamePanel().objects[1].length; i++) {
//            if(getGamePanel().objects[getGamePanel().currentMap][i] == null) {
//                getGamePanel().objects[getGamePanel().currentMap][i] = item;
//                break;
//            }
//        }

        replaceWithItem(item, getGamePanel().objects.get(getGamePanel().currentMap));

//        getGamePanel().objects.add(item);
    }

    private void replaceWithItem(Item item, ArrayList<Entity> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) {
                objects.set(i, item);
                return;
            }
        }
    }

    public void draw(Graphics2D g2D) {

        super.draw(g2D);

        if (attacking) {
            sprite = attackState.manageAnimations(this, direction);
        }
        else {
            if (inMotion)
                sprite = movement.manageAnimations(this, direction);
            else
                sprite = idle.manageAnimations(this, direction);
        }


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

        int x = (int) screenX;
        int y = (int) screenY;

        if (screenX > worldX) {
            x = (int) worldX;
        }
        if (screenY > worldY) {
            y = (int) worldY;
        }
        int rightOffset = (int) (getGamePanel().screenWidth - screenX);
        if (rightOffset > getGamePanel().worldWidth - worldX) {
            x = (int) (getGamePanel().screenWidth - (getGamePanel().worldWidth - worldX));
        }
        int bottomOffset = (int) (getGamePanel().screenHeight - screenY);
        if (bottomOffset > getGamePanel().worldHeight - worldY) {
            y = (int) (getGamePanel().screenHeight - (getGamePanel().worldHeight - worldY));
        }

//        // setarea opacitatii
//        if (invincible) {
//            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
//        }

        if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.UP)
            g2D.drawImage(sprite, x, y-getGamePanel().tileSize, null);
        else if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.LEFT)
            g2D.drawImage(sprite, x-getGamePanel().tileSize, y, null);
        else
            camera.drawEntity(g2D, sprite);



        drawSolidArea(g2D);

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public abstract void offensiveBehaviour();

    public void checkStartChasingOrNot(Entity target, int distance, int rate) {
        if (getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = true;
            }
        }
    }

    public void checkStopChasingOrNot(Entity target, int distance, int rate) {
        if (getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = false;
            }
        }
    }

    @Override
    public void pathFinding() {
//        int goalCol = (int) ((getGamePanel().player.worldX + getGamePanel().player.solidArea.x)/getGamePanel().tileSize);
//        int goalRow = (int) ((getGamePanel().player.worldY + getGamePanel().player.solidArea.y)/getGamePanel().tileSize);

        searchPath(getGoalCol(getGamePanel().player), getGoalRow(getGamePanel().player), true);

        offensiveBehaviour();
    }
}
