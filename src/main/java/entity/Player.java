package entity;

import animations.TypeAnimation;
import features.*;
import game.GamePanel;
import game.GameState;
import item.Shield;
import item.Weapon;
import shield.NormalShield;
import sword.NormalSword;

import java.awt.*;
import java.awt.image.BufferedImage;

// clasa north ce detine informatii despre jucator
// precum pozitia sa si viteza de deplasare
public class Player extends Entity {

    Graphics2D g2D = null;

    public KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public String characterClassPath = "warrior";

    public int level;
    public int strength;
    public int dexterity;
    public int attack = 0;
    public int defense = 0;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Weapon currentWeapon;
    public Shield currentShield;

    // ATRIBUTE ITEME
    public int attackValue;
    public int defenseValue;

    // numarul de chei pe care jucatorul le detine in timp real
//    public int numKeys = 0;

//    public boolean invincible = false;
//    public int invincibleCounter = 0;

    /** Constructor player */
    public Player(GamePanel gPanel, KeyHandler keyH, int x, int y, Direction direction) {

        super(gPanel);

        isPlayer = true;

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
        level = 1;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new NormalSword(gPanel);
        currentShield = new NormalShield(gPanel);

        updateAttack();
        updateDefense();

        // debug
//        setPosition(gPanel.tileSize * 10, gPanel.tileSize * 13);

//        solidArea.x = 32;
//        solidArea.y = 32;
//        solidAreaDefaultX = 32;
//        solidAreaDefaultY = 32;
//        solidArea.width = 38; // 64;
//        solidArea.height = 62; //48; //62;

        setDefaultAttackArea();

        this.getPlayerSprites();
    }

    /** incarcarea animatiilor pentru player */
    public void getPlayerSprites() {
//        String playerPath = "res/player/"+characterClassPath;
        setupMovement("res/player/" + characterClassPath);
        setupIdle("res/player/" + characterClassPath);
        setupAttack("res/player/" + characterClassPath);
//        this.loadMovementAnimations("res/player/" + characterClassPath);
    }

    @Override
    public void update() {

        // Animatii jucator
        this.managePlayerMovement();

        /** actualizare imagine/avansare animatie cadru urmator dupa un interval de cadre rulate din cele 60 per secunda */
        currentAnimation.updateFrames();

        manageInvincible();
    }

//    private void manageInvincible() {
//        if (invincible) {
//            invincibleCounter++;
//            if (invincibleCounter > 60) {
//                invincible = false;
//                invincibleCounter = 0;
//            }
//        }
//    }

    @Override
    public void draw(Graphics2D g2D) {

        if (gPanel == null)
            return;

        if (this.g2D == null)
            this.g2D = g2D;

        /** Management animatii */
        BufferedImage sprite = null;

        inMotion = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
        if (isAttacking()) {
            if(inMotion) inMotion = false;
            sprite = attacks.manageAnimations(this,direction);
        }
        else if (inMotion) {
            sprite = movement.manageAnimations(this, direction);
        }
        else {
            sprite = idle.manageAnimations(this, direction);
        }

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

        // setarea opacitatii
        if (invincible) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.UP)
            g2D.drawImage(sprite, x, y-gPanel.tileSize, null);
        else if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.LEFT)
            g2D.drawImage(sprite, x-gPanel.tileSize, y, null);
        else
            g2D.drawImage(sprite, x, y, null);

        g2D.setColor(Color.red);
        g2D.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        // resetarea opacitatii
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    @Override
    public void setDefaultSolidArea() {
        gPanel.player.solidAreaDefaultX = gPanel.tileSize / 4;
        gPanel.player.solidAreaDefaultY = gPanel.tileSize / 2;
        gPanel.player.solidArea.width = gPanel.tileSize / 2;
        gPanel.player.solidArea.height = (int) (gPanel.tileSize / 2.25);
    }

    void updateAttack() {
        attack = strength * currentWeapon.damage;
    }

    void updateDefense() {
        defense = dexterity * currentShield.defense;
    }

    public void setDefaultAttackArea() {
        attackArea.width = 36;
        attackArea.height = 36;
    }

    public void attackingMonster() {
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

        // verifica coliziunea ariei de atac cu monstrii
        int monsterIndex = gPanel.collisionDetector.
                checkEntity(this, gPanel.monsterList);
        doDamage(monsterIndex);

        // dupa verificarea coliziunii, restabileste datele originale
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;

    }

    private void doDamage(int monsterIndex) {
        if (monsterIndex > -1) {
            if (!gPanel.monsterList.get(monsterIndex).invincible) {
                // ofera daune
                gPanel.playSE("hitmonster.wav");
                gPanel.monsterList.get(monsterIndex).life -= 1;
                gPanel.monsterList.get(monsterIndex).invincible = true;
                gPanel.monsterList.get(monsterIndex).damageReaction();

                if (gPanel.monsterList.get(monsterIndex).life <= 0) {
                    gPanel.monsterList.get(monsterIndex).dying = true;
                }
            }
        }
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

    public boolean isAttacking() {
        return keyH.spacePressed;
    }


    public void managePlayerMovement() {

        // verifica evenimente
        gPanel.eHandler.checkEvent();

        if (isMoving()) {
//            System.out.println("up: " + keyH.upPressed + " down: " + keyH.downPressed + " left: " + keyH.leftPressed + " right: " + keyH.rightPressed);
            // verifica coliziunea cu texturile hartii
            collisionOn = false;
            gPanel.collisionDetector.manageTileCollision(this);

            // verifica coliziunea cu NPC
            gPanel.collisionDetector.checkEntity(this, gPanel.npcList);

            // verifica coliziunea cu obiecte
            int objIndex = gPanel.collisionDetector.manageObjCollision(this);
            pickUpObj(objIndex);

            // verifica coliziuni cu monstrii
            int monsterIndex = gPanel.collisionDetector.checkEntity(this, gPanel.monsterList);
            contactMonster(monsterIndex);

            if (!collisionOn && inMotion)
                manageMovement();
        }
        // interactiune cu npc
        if (keyH.enterPressed) {
            int npcIndex = gPanel.collisionDetector.checkEntity(this, gPanel.npcList);
            interactNPC(npcIndex);
            gPanel.keyH.enterPressed = false;
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex > -1) {
            if (gPanel.keyH.enterPressed) {
                // interactionari cu npc-uri
                GamePanel.gameState = GameState.Dialogue;
                gPanel.npcList.get(npcIndex).speak();
            }
        }
    }

    private void contactMonster(int monsterIndex) {
        if (monsterIndex > -1) {
            if (!invincible && !gPanel.monsterList.get(monsterIndex).dying) {
                gPanel.playSE("receivedamage.wav");
                life -= 1;
                invincible = true;
            }
        }
    }

}
