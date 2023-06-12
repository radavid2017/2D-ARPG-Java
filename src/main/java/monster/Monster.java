package monster;

import animations.StateMachine;
import animations.TypeAnimation;
import entity.*;
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
    public AttackStyle attackStyle;
    Weapon weapon;



    int shotAvailableCounter = 0;
    final int shotAvailableTrigger = 30;

    public BufferedImage sprite;

    public boolean inRage = false;
    public boolean boss = false;
    public StateMachine movementInRage = new StateMachine();
    public StateMachine attackInRage = new StateMachine();
    public StateMachine idleInRage = new StateMachine();

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

    public void alwaysMoving() {
        if (collisionOn) {
            switch (direction) {
                case UP -> direction = Direction.DOWN;
                case DOWN -> direction = Direction.UP;
                case LEFT -> direction = Direction.RIGHT;
                case RIGHT -> direction = Direction.LEFT;
            }
            collisionOn = false;
            inMotion = true;
        }
    }

    public void checkCollisionsExceptPlayer() {
        collisionOn = false;
        getGamePanel().collisionDetector.manageTileCollision(this);
        getGamePanel().collisionDetector.checkEntity(this, getGamePanel().npcList.get(getGamePanel().currentMap));
        getGamePanel().collisionDetector.checkEntity(this, getGamePanel().monsterList.get(getGamePanel().currentMap));
        getGamePanel().collisionDetector.checkPlayer(this);
        getGamePanel().collisionDetector.checkEntity(this, getGamePanel().interactiveTiles.get(getGamePanel().currentMap));
    }

    public void update() {
        if (!sleep) {
            AI();
            super.update();

            if (getGamePanel().collisionDetector.checkPlayer(this)) {
                doDamage();
            }

            if (onPath) {
                checkStopChasingOrNot(getGamePanel().player, 8, 100);
            } else {
                checkStartChasingOrNot(getGamePanel().player, 5, 100);
            }
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
                    case Touching -> {
                        touchingDamage(getGamePanel().player);
                    }
                }
            }

            getGamePanel().player.invincible = true;
        }
    }

    public void showHPBossBar(Graphics2D g2D) {
        double oneScale = (double) getGamePanel().tileSize*8 / maxLife;
        double hpBarValue = oneScale * life;

        int hpBarX = getGamePanel().screenWidth/2 - getGamePanel().tileSize*4;
        int hpBarY = getGamePanel().tileSize*10;

        g2D.setColor(new Color(35, 35, 35));
        g2D.fillRect(hpBarX - 1, hpBarY - 16, getGamePanel().tileSize*8+2, 22);
        g2D.setColor(new Color(255, 0, 30));
        g2D.fillRect(hpBarX, hpBarY - 15, (int) hpBarValue, 20);

        g2D.setFont(g2D.getFont().deriveFont(24f));
        g2D.setColor(Color.white);
        g2D.drawString(name, hpBarX+4, hpBarY-20);
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

        getGamePanel().objects.get(getGamePanel().currentMap).add(item);
//        replaceWithItem(item, getGamePanel().objects.get(getGamePanel().currentMap));

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
        if (!inRage) {
            if (attacking) {
                sprite = attackState.manageAnimations(this, direction);
            } else {
                if (inMotion)
                    sprite = movement.manageAnimations(this, direction);
                else
                    sprite = idle.manageAnimations(this, direction);
            }
        }
        else {
            if (attacking) {
                sprite = attackInRage.manageAnimations(this, direction);
            }
            else {
                if (inMotion) {
                    sprite = movementInRage.manageAnimations(this, direction);
                }
                else {
                    sprite = idleInRage.manageAnimations(this, direction);
                }
            }
        }


        // Management Camera

//        System.out.println("monster life: " + life);

//        // Monster HP Bar
//        if (hpBarOn) showHPBar(g2D);

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

        if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.UP && camera.isOnCamera())
            g2D.drawImage(sprite, x, y-sprite.getHeight()/2, null);
        else if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.LEFT && camera.isOnCamera())
            g2D.drawImage(sprite, x-sprite.getWidth()/2, y, null);
        else
            camera.drawEntity(g2D, sprite);



        drawSolidArea(g2D);

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public abstract void offensiveBehaviour();

    public void moveTowardPlayer(int interval) {
        actionLockCounterDirection++;

        if (actionLockCounterDirection > interval) {
            if (getXDistance(getGamePanel().player) > getYDistance(getGamePanel().player)) {
                // Miscare spre dreapta / stanga
                if (getGamePanel().player.getCenterX() < getCenterX()) {
                    direction = Direction.LEFT;
                }
                else {
                    direction = Direction.RIGHT;
                }
            }
            else if (getXDistance(getGamePanel().player) < getYDistance(getGamePanel().player)) {
                // Misacre spre nord / sud
                if (getGamePanel().player.getCenterY() < getCenterY()) {
                    direction = Direction.UP;
                }
                else {
                    direction = Direction.DOWN;
                }
            }
            actionLockCounterDirection = 0;
        }
    }

    public void checkStartChasingOrNot(Creature target, int distance, int rate) {
        if (getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = true;
            }
        }
    }

    public void checkStopChasingOrNot(Creature target, int distance, int rate) {
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
