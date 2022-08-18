package entity;

import features.*;
import game.GamePanel;
import game.GameState;
import game.UI;
import object.TypeObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

// clasa north ce detine informatii despre jucator
// precum pozitia sa si viteza de deplasare
public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public String characterClassPath = "mage";
    // numarul de chei pe care jucatorul le detine in timp real
//    public int numKeys = 0;


    /** Constructor player */
    public Player(GamePanel gPanel, KeyHandler keyH, int x, int y, Direction direction) {

        super(gPanel);

        this.keyH = keyH;
        this.worldX = x;
        this.worldY = y;
        this.speed = this.gPanel.worldWidth/600.0d;
        System.out.println("VITEZA PLAYER: " + this.speed);
        this.direction = direction;

        screenX = gPanel.screenWidth/2 - (gPanel.tileSize/2);
        screenY = gPanel.screenHeight/2 - (gPanel.tileSize/2);

        // INSTANTIERE COLIZIUNE
        solidArea = new Rectangle();
        solidArea.x = gPanel.tileSize/4;
        solidArea.y = gPanel.tileSize/2;
        solidAreaDefaultX = gPanel.tileSize/4;
        solidAreaDefaultY = gPanel.tileSize/2;
        solidArea.width = gPanel.tileSize/2;
        solidArea.height = (int) (gPanel.tileSize/2.25);

        // INSTANTIERE STATUS JUCATOR
        maxLife = 6;
        life = maxLife;

//        solidArea.x = 32;
//        solidArea.y = 32;
//        solidAreaDefaultX = 32;
//        solidAreaDefaultY = 32;
//        solidArea.width = 38; // 64;
//        solidArea.height = 62; //48; //62;

        this.getPlayerSprites();
    }

    /** incarcarea animatiilor pentru player */
    public void getPlayerSprites() {
//        String playerPath = "res/player/"+characterClassPath;
        this.loadMovementAnimations("res/player/" + characterClassPath);
    }
    public void update() {

        // Deplasari jucator
        this.managePlayerMovement();

        /** actualizare imagine/avansare animatie cadru urmator dupa un interval de cadre rulate din cele 60 per secunda */
        AnimationState.updateFrames();
    }

    public void draw(Graphics2D g2D) {

        /** Management animatii */
        BufferedImage sprite = null;
        boolean inMotion = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
        sprite = movement.manageAnimations(direction, inMotion);

        int x = screenX;
        int y = screenY;

        if (screenX > worldX) {
            x = (int) worldX;
        }
        if (screenY > worldY) {
            y = (int) worldY;
        }
        int rightOffset = gPanel.screenWidth - screenX;
        if (rightOffset > gPanel.worldWidth - worldX) {
            x = (int) (gPanel.screenWidth - (gPanel.worldWidth - worldX));
        }
        int bottomOffset = gPanel.screenHeight - screenY;
        if (bottomOffset > gPanel.worldHeight - worldY) {
            y = (int) (gPanel.screenHeight - (gPanel.worldHeight - worldY));
        }

        g2D.drawImage(sprite, x, y, null);
//        g2D.setColor(Color.red);
//        g2D.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

    /** inregistrarea animatiilor pentru player */
//    @Override
//    public void loadMovementAnimations() {
//        /** ANIMATII MOVEMENT */
//        String movementFolderName = JOptionPane.showInputDialog("Numele folderului de animatie:");
////        String movementFolderName = "archer";
//        // UP
////        RenameFolderFiles.rename("E:\\AplicatiiCV\\2DAdventure\\res\\player\\movement\\north");
//        walkUp = new AnimationState(this.gPanel, "walkUp", Direction.UP, "res\\player\\" + movementFolderName + "\\north");
//        System.out.println("Animatia " + walkUp.title + " incarcata cu succes.");
//        // RIGHT
//        walkRight = new AnimationState(this.gPanel, "walkRight", Direction.RIGHT,"res\\player\\" + movementFolderName + "\\east");
//        System.out.println("Animatia " + walkRight.title + " incarcata cu succes.");
//        // DOWN
//        walkDown = new AnimationState(this.gPanel, "walkDown", Direction.DOWN, "res\\player\\" + movementFolderName + "\\south");
//        System.out.println("Animatia " + walkDown.title + " incarcata cu succes.");
//        // LEFT
//        walkLeft = new AnimationState(this.gPanel, "walkLeft", Direction.LEFT, "res\\player\\" + movementFolderName + "\\west");
//        System.out.println("Animatia " + walkLeft.title + " incarcata cu succes.");
//
//        movement = new StateMachine();
//        for (AnimationState animationState : Arrays.asList(this.walkUp, this.walkDown, this.walkRight, this.walkLeft)) {
//            this.movement.add(animationState);
//        }
//    }

    public void pickUpObj(int objIndex) {
        if (objIndex > -1) {

//            TypeObject typeObject = gPanel.objects.get(objIndex).typeObject;
//            switch (typeObject) {
//                case Key -> {
//                    gPanel.playSE("coin.wav");
//                    numKeys++;
//                    gPanel.objects.remove(objIndex);
//                    gPanel.ui.showMessage("Ai gasit o cheie!");
//                }
//                case Door -> {
//                    if (numKeys > 0) {
//                        gPanel.playSE("unlock.wav");
//                        gPanel.objects.remove(objIndex);
//                        numKeys--;
//                        gPanel.ui.showMessage("Ai deschis usa!");
//                    } else {
//                        gPanel.ui.showMessage("Iti trebuie o cheie!");
//                    }
//                }
//                case Boots -> {
//                    gPanel.playSE("powerup.wav");
//                    // mareste viteza jucatorului
//                    float increaseSpeed = 4f;
//                    speed += increaseSpeed;
//                    AnimationState.timeToChangeFrame -= increaseSpeed;
//                    gPanel.objects.remove(objIndex);
//                    gPanel.ui.showMessage("Acum esti mai rapid!");
//                }
//                case Chest -> {
//                    gPanel.ui.gameOver = true;
//                    UI.GAME_OVER = true;
//                    gPanel.stopMusic();
//                    gPanel.playSE("fanfare.wav");
//                    gPanel.hasPlayed = true;
//                }
//            }
        }
    }

    public boolean isMoving() {
        if (keyH.upPressed) { // nord
            this.direction = Direction.UP;
            return true;
        }
        if (keyH.downPressed) { // deplasare sud
            this.direction = Direction.DOWN;
            return true;
        }
        if (keyH.leftPressed) { // deplasare vest
            this.direction = Direction.LEFT;
            return true;
        }
        if (keyH.rightPressed) { // deplasare est
            this.direction = Direction.RIGHT;
            return true;
        }
        return false;
    }

    public void managePlayerMovement() {
        if (isMoving()) {
//            System.out.println("up: " + keyH.upPressed + " down: " + keyH.downPressed + " left: " + keyH.leftPressed + " right: " + keyH.rightPressed);
            // verifica coliziunea cu texturile hartii
            collisionOn = false;
            gPanel.collisionDetector.manageTileCollision(this);

            // verifica coliziunea cu obiecte
            int objIndex = gPanel.collisionDetector.manageObjCollision(this, true);
            pickUpObj(objIndex);

            // verifica coliziunea cu NPC
            int npcIndex = gPanel.collisionDetector.checkEntity(this, gPanel.npc);
            interactNPC(npcIndex);

            // verifica evenimente
            gPanel.eHandler.checkEvent();

            gPanel.keyH.enterPressed = false;

            if (!collisionOn)
                manageMovement();
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex > -1) {
            if (gPanel.keyH.enterPressed) {
                // interactionari cu npc-uri
                GamePanel.gameState = GameState.Dialogue;
                gPanel.npc.get(npcIndex).speak();
            }
        }
    }
}
