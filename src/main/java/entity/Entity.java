package entity;

import features.AnimationState;
import features.Direction;
import features.StateMachine;

import java.awt.*;
import java.awt.image.BufferedImage;

/** Super clasa - Entitate
 *  Aceasta contine variabile care pot fi folosite
 *  de orice entitate precum north-ul, monstrii si NPC-uri
 */
public abstract class Entity {
    public double worldX; // pozitia pe axa ox
    public double worldY; // pozitia pe axa oy
    public double speed; // viteza de deplasare a entitatii

    /** Liste de imagini pentru realizarea animatiilor de miscare */
    public AnimationState walkUp, walkDown, walkLeft, walkRight;
    /** Lista de animatii */
    public StateMachine movement = new StateMachine();
//    // directii diagonale - optional
    public BufferedImage[] upLeft,upRight,downLeft,downRight;
    /** Variabila pentru a declansa animatia corecta in functie de directia de miscare */
    public Direction direction;

    /** Declararea blocului de coliziune */
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    /** Metodele de actualizare si scriere a informatiilor asupra entitatii */
    // actualizare
    public abstract void update();
    // desenare
    public abstract void draw(Graphics2D g2d);
    // incarcarea animatiilor de miscare
    public abstract void loadMovementAnimations();

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

}
