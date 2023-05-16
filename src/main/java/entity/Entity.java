package entity;

import animations.AnimationState;
import animations.StateMachine;
import animations.TypeAnimation;
import features.*;
import game.GamePanel;
import particles.Particle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

/** Super clasa - Entitate
 *  Aceasta contine variabile care pot fi folosite
 *  de orice entitate precum north-ul, monstrii si NPC-uri
 */
public abstract class Entity {

    public Camera camera;
    GamePanel gPanel;
    public double worldX; // pozitia pe axa ox
    public double worldY; // pozitia pe axa oy
    public double speed; // viteza de deplasare a entitatii

    public double screenX;
    public double screenY;

    public TypeAnimation typeAnimation;
//    // directii diagonale - optional
    public BufferedImage[] upLeft,upRight,downLeft,downRight;
    /** Variabila pentru a declansa animatia corecta in functie de directia de miscare */
    public Direction direction;

    /** Declararea blocului de coliziune */
    public Rectangle solidArea = new Rectangle();
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean isSolid = true;

    public int knockBackCounter = 0;
    public boolean knockBack = false;
    public int knockBackPower = 0;

    public String name;

    /** Suprafata de detectare a loviturilor */
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int invincibleTime = 40;
    public int invincibleCounter = 0;
    public boolean invincible = false;

    public int timeToChangeFrame = 12;
    public int currentTime = 0;
//    public ArrayList<String> dialogues = new ArrayList<>();
//    private int dialogueIndex = 0;
//    public boolean endedDialogue = false;
    public Dialogue dialogue = new Dialogue();
//    private Iterator<String> dialogueIndex = dialogue.textStack.iterator();

    public AnimationState currentAnimation;

    public boolean inMotion = false;
    public boolean isPlayer = false;

    /** Creature stuff */
    public boolean alive = true;
    public boolean dying = false;
    public int dyingCounter = 0;

    /** Status caracter */
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int attack = 0;
    public int defense = 0;
    public int exp;

    public Entity attacker;
    public Direction knockBackDirection;

    /** Constrcutor entitate */
    public Entity(GamePanel gPanel) {
        this.gPanel = gPanel;
        solidArea = new Rectangle(gPanel.tileSize/8, gPanel.tileSize/2, (int) (gPanel.tileSize/1.5), (int) (gPanel.tileSize/2.25)); // aria de coliziune implicita pentru toate entitatile
        solidAreaDefaultX = gPanel.tileSize/8;
        solidAreaDefaultY = gPanel.tileSize/2;
    }

    public Entity() {

    }

    /** Metodele de actualizare si scriere a informatiilor asupra entitatii */
    // actualizare
    public void update() {

    }

    public void speak() {

        gPanel.ui.setCurrentDialogue(dialogue);
        dialogue.indexText = dialogue.indexText >= dialogue.size()-1 ? 0 : ++dialogue.indexText;
//          endedDialogue = dialogueIndex >= dialogues.size() - 1;
//        dialogueIndex = dialogueIndex >= dialogues.size()-1 ? 0 : ++dialogueIndex;

        switch (gPanel.player.direction) {
            case UP -> direction = Direction.DOWN;
            case DOWN -> direction = Direction.UP;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    /** Movement management */
    public void manageMovement() {
        switch (this.direction) {
            case UP -> this.worldY -= this.speed;
            case DOWN -> this.worldY += this.speed;
            case LEFT -> this.worldX -= this.speed;
            case RIGHT -> this.worldX += this.speed;
        }
    }

    /** Desenarea entitatii */
    public void draw(Graphics2D g2D) {
        screenX = worldX - gPanel.player.worldX + gPanel.player.screenX;
        screenY = worldY - gPanel.player.worldY + gPanel.player.screenY;

        // Instantiere camera
        camera = new Camera(worldX, worldY, screenX, screenY, gPanel);

        camera.playerIsTouchingEdgesOfCamera();
    }

    public void drawSolidArea(Graphics2D g2D) {
        g2D.setColor(Color.red);
        g2D.drawRect((int) (screenX + solidArea.x), (int) (screenY + solidArea.y), solidArea.width, solidArea.height);
    }



    // incarcarea animatiilor de miscare
        /** ANIMATII MOVEMENT */
    public void loadMovementAnimations(String entityPath) {
//        String movementFolderName = JOptionPane.showInputDialog("Numele folderului de animatie:");
//        String characterName = "archer";
        // UP
//        RenameFolderFiles.rename("E:\\AplicatiiCV\\2DAdventure\\res\\player\\movement\\north");
//        walkUp = new AnimationState(this.gPanel, "walkUp", Direction.UP, entityPath + "\\" + characterName + "\\movement\\north");

//        walkUp = new AnimationState(this.gPanel, "walkUp", Direction.UP, entityPath + "\\movement\\north");
//        System.out.println("Animatia " + walkUp.title + " incarcata cu succes.");
//        // RIGHT
////        walkRight = new AnimationState(this.gPanel, "walkRight", Direction.RIGHT,entityPath + "\\" + characterName + "\\movement\\east");
//        walkRight = new AnimationState(this.gPanel, "walkRight", Direction.RIGHT,entityPath + "\\movement\\east");
//        System.out.println("Animatia " + walkRight.title + " incarcata cu succes.");
//        // DOWN
////        walkDown = new AnimationState(this.gPanel, "walkDown", Direction.DOWN, entityPath + "\\" + characterName + "\\movement\\south");
//        walkDown = new AnimationState(this.gPanel, "walkDown", Direction.DOWN, entityPath + "\\movement\\south");
//        System.out.println("Animatia " + walkDown.title + " incarcata cu succes.");
//        // LEFT
////        walkLeft = new AnimationState(this.gPanel, "walkLeft", Direction.LEFT, entityPath + "\\" + characterName + "\\movement\\west");
//        walkLeft = new AnimationState(this.gPanel, "walkLeft", Direction.LEFT, entityPath + "\\movement\\west");
//        System.out.println("Animatia " + walkLeft.title + " incarcata cu succes.");

//        movement = new StateMachine();
//        for (AnimationState animationState : Arrays.asList(this.walkUp, this.walkDown, this.walkRight, this.walkLeft)) {
//            this.movement.add(animationState);
//        }
    }

    public void setPosition(double worldX, double worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public void manageInvincible() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > invincibleTime) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }


    /** Efect de moarte */
    public void dyingEffect(Graphics2D g2D) {

        dyingCounter++;
        blinkingEffect(g2D, dyingCounter);
        if (dyingCounter > 40) {
            dying = false;
            alive = false;
        }
    }

    /** Efect de clipire */
    public void blinkingEffect (Graphics2D g2D, int blinkCounter) {
        if (blinkCounter % 10 >= 1 && blinkCounter % 10 <= 5) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }
        if (blinkCounter % 10 >= 6 || blinkCounter % 10 == 0) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public void damageReaction() {

    }

    public int touchingDamage(Entity target) {
        int totalDamage = attack - target.defense;
        if (totalDamage < 0) {
            totalDamage = 1;
        }
        target.life -= totalDamage;
        return totalDamage;
    }

    public int nearlyDamage(Entity target) {
        int totalDamage = attack - target.defense;
        if (totalDamage < 0) {
            totalDamage = 1;
        }
        target.life -= totalDamage;
        return totalDamage;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

    public GamePanel getGamePanel() {
        return gPanel;
    }

    public abstract void setDefaultSolidArea();

    public void generateParticle(Entity target, Color color, int size, int speed, int maxLife) {
        getGamePanel().particleList.add(new Particle(getGamePanel(), target, color, size, speed, maxLife,-2, -1));
        getGamePanel().particleList.add(new Particle(getGamePanel(), target, color, size, speed, maxLife,2, -1));
        getGamePanel().particleList.add(new Particle(getGamePanel(), target, color, size, speed, maxLife,-2, 1));
        getGamePanel().particleList.add(new Particle(getGamePanel(), target, color, size, speed, maxLife,2, 1));
    }

    public int getLeftX() {
        return (int) (worldX + solidArea.x);
    }

    public int getRightX() {
        return (int) (worldX + solidArea.x + solidArea.width);
    }

    public int getTopY() {
        return (int) (worldY + solidArea.y);
    }

    public int getBottomY() {
        return (int) (worldY + solidArea.y + solidArea.height);
    }

    public int getCol() {
        return (int) ((worldX + solidArea.x)/gPanel.tileSize);
    }

    public int getRow() {
        return (int) ((worldY + solidArea.y)/gPanel.tileSize);
    }
}
