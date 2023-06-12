package entity;

import animations.AnimationState;
import animations.StateMachine;
import animations.TypeAnimation;
import features.Direction;
import game.GamePanel;
import monster.Monster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class Creature extends Entity {
    /** Liste de imagini pentru realizarea animatiilor de miscare */
    public AnimationState walkUp, walkDown, walkLeft, walkRight;
    /** Lista de animatii */
    public StateMachine movement = null;
    public StateMachine idle = null;
    public StateMachine attackState = null;

    public double defaultSpeed;

    public boolean attacking = false;

    public Creature(GamePanel gp) {
        super(gp);
    }

    public boolean hasToStop() {
        checkCollisions();
        if (collisionOn) {
            knockBackCounter = 0;
            knockBack = false;
            speed = defaultSpeed;
            return true;
        }
        return false;
    }

    public void update() {

        if (knockBack) {
            if(!hasToStop()) {
                int i = 0;
                switch (knockBackDirection) {
                    case UP -> {
                        while (i < speed) {
                            if(hasToStop()) {
                                break;
                            }
                            else {
                                worldY--;
                                i++;
                            }
                        }
                    }
                    case DOWN -> {
                        while (i < speed) {
                            if (hasToStop()) {
                                break;
                            }
                            else {
                                worldY++;
                                i++;
                            }
                        }
                    }
                    case LEFT -> {
                        while (i < speed) {
                            if (hasToStop()) {
                                break;
                            }
                            else {
                                worldX--;
                                i++;
                            }
                        }
                    }
                    case RIGHT -> {
                        while (i < speed) {
                            if (hasToStop()) {
                                break;
                            }
                            else {
                                worldX++;
                                i++;
                            }
                        }
                    }
                }
            }

            knockBackCounter++;
            if (knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
//        else if (attacking) {
//            attacking();
//        }
        else {
            checkCollisions();

            if (!collisionOn && inMotion) {
                this.manageMovement();
            }
        }

        manageInvincible();

//        currentTime++;
//        if (currentTime >= timeToChangeFrame) {
        currentAnimation.updateFrames(this);
//            currentTime = 0;
//        }
    }

    public int getCenterX() {
        return (int) (worldX + currentAnimation.nextFrame().getWidth()/2);
    }

    public int getCenterY() {
        return (int) (worldY + currentAnimation.nextFrame().getHeight()/2);
    }

    public void setupMovement(String creaturePath) {
        movement = new StateMachine();
        movement.loadCompleteAnimation(gPanel, creaturePath + "\\movement", TypeAnimation.IN_MOTION);
        currentAnimation = movement.down;
    }

    public void setupMovement(int scale, String creaturePath) {
        movement = new StateMachine();
        movement.loadCompleteAnimation(gPanel, scale, creaturePath + "\\movement", TypeAnimation.IN_MOTION);
        currentAnimation = movement.down;
    }

    public void setupIdle(String creaturePath) {
        idle = new StateMachine();
        idle.loadCompleteAnimation(gPanel, creaturePath + "\\idle", TypeAnimation.IDLE);
    }

    public void setupIdle(int scale, String creaturePath) {
        idle = new StateMachine();
        idle.loadCompleteAnimation(gPanel, scale, creaturePath + "\\idle", TypeAnimation.IDLE);
    }

    public void setupStaticIdle(String creaturePath) {
        idle = new StateMachine();
        idle.loadCompleteAnimation(gPanel, creaturePath + "\\idle", TypeAnimation.IDLE);
        currentAnimation = idle.down;
//        idle.loadSingleAnimation(gPanel, creaturePath + "\\idle", TypeAnimation.IDLE, Direction.DOWN);

        /** setari statice */
        this.collisionOn = false;
        inMotion = false;
        this.direction = Direction.DOWN;
        for (int i = 0; i < idle.states.size(); i++) {
            idle.states.get(i).timeToChangeFrame = 28;
        }
    }

    public void setupStaticIdle(int scale, String creaturePath) {
        idle = new StateMachine();
        idle.loadCompleteAnimation(gPanel, scale, creaturePath + "\\idle", TypeAnimation.IDLE);
        currentAnimation = idle.down;
//        idle.loadSingleAnimation(gPanel, creaturePath + "\\idle", TypeAnimation.IDLE, Direction.DOWN);

        /** setari statice */
        this.collisionOn = false;
        inMotion = false;
        this.direction = Direction.DOWN;
        for (int i = 0; i < idle.states.size(); i++) {
            idle.states.get(i).timeToChangeFrame = 28;
        }
    }

    public void setupAttack(String creaturePath) {
        attackState = new StateMachine();
        attackState.loadCompleteAnimation(gPanel, creaturePath + "\\attack", TypeAnimation.ATTACK);
    }

    public void setupAttack(int scale, String creaturePath) {
        attackState = new StateMachine();
        attackState.loadCompleteAnimation(gPanel, scale, creaturePath + "\\attack", TypeAnimation.ATTACK);
    }

    public int getXDistance(Creature target) {
        return Math.abs(getCenterX() - target.getCenterX());
    }

    public int getYDistance(Creature target) {
        return Math.abs(getCenterY() - target.getCenterY());
    }

    public int getTileDistance(Creature target) {
        return (getXDistance(target) + getYDistance(target)) / getGamePanel().tileSize;
    }

    public int getGoalCol(Entity target) {
        return (int) ((target.worldX + target.solidArea.x) / getGamePanel().tileSize);
    }

    public int getGoalRow(Entity target) {
        return (int) ((target.worldY + target.solidArea.y) / getGamePanel().tileSize);
    }

    public void attacking() {
        // salveaza datele curente ale ariei solide
        int currentWorldX = (int) worldX;
        int currentWorldY = (int) worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        // ajusteaza coordonatele jucatorului pentru aria de atac
        switch (direction) {
            case UP -> worldY -= attackArea.height;
            case DOWN -> worldY += attackArea.height;
            case LEFT -> worldX -= attackArea.width;
            case RIGHT -> worldX += attackArea.width;
        }

        // schimbarea dimensiunii ariei solide pentru aria de atac
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        if (this instanceof Monster monster) {
            if (gPanel.collisionDetector.checkPlayer(this)) {
                monster.doDamage();
            }
        }
        else { // Jucator

            // verifica coliziunea ariei de atac cu monstrii
            int monsterIndex = gPanel.collisionDetector.
                    checkEntity(this, gPanel.monsterList.get(gPanel.currentMap));
            gPanel.player.doDamageToMonster(monsterIndex, this, gPanel.player.currentWeapon.knockBackPower);

            // coliziunea cu tiles interactive
            int iTileIndex = gPanel.collisionDetector.checkEntity(this, gPanel.interactiveTiles.get(gPanel.currentMap));
            gPanel.player.doDamageToITile(iTileIndex);

            // coliziunea cu proiectile
            int projectileIndex = gPanel.collisionDetector.checkEntity(this, gPanel.projectileList);
            gPanel.player.doDamageToProjectile(projectileIndex);
        }

        // dupa verificarea coliziunii, restabileste datele originale
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;

    }

    public void checkAttackOrNot(int rate, int straight, int horizontal) {

        boolean targetInRange = false;
        int xDis = getXDistance(getGamePanel().player);
        int yDis = getYDistance(getGamePanel().player);
        attacking = false;

        switch (direction) {
            case UP -> {
                if (getGamePanel().player.getCenterY() < getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
            }
            case DOWN -> {
                if (getGamePanel().player.getCenterY() > getCenterY() && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
            }
            case LEFT -> {
                if (getGamePanel().player.getCenterX() < getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
            }
            case RIGHT -> {
                if (getGamePanel().player.getCenterX() > getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
            }
        }

        if (targetInRange) {
            // Verifica daca initiaza un atac
            int i = new Random().nextInt(rate);
            if (i == 0) {
                // TODO: poate trebuie modificat aici
                attacking = true;
//                currentAnimation.typeAnimation = TypeAnimation.ATTACK;
//                currentAnimation.numFrames = 1;
//                currentAnimation.currentFrame = 0;
            }
        }
//        else {
//            if(attacking) {
//                attacking = false;
//                currentAnimation.typeAnimation = TypeAnimation.IDLE;
//            }
//        }
    }
}
