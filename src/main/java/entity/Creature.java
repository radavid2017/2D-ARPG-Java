package entity;

import animations.AnimationState;
import animations.StateMachine;
import animations.TypeAnimation;
import game.GamePanel;

import java.util.LinkedHashMap;

public abstract class Creature extends Entity {
    /** Liste de imagini pentru realizarea animatiilor de miscare */
    public AnimationState walkUp, walkDown, walkLeft, walkRight;
    /** Lista de animatii */
    public StateMachine movement = null;
    public StateMachine idle = null;
    public StateMachine attackState = null;

    public Creature(GamePanel gp) {
        super(gp);
    }

    public void update() {
        collisionOn = false;
        gPanel.collisionDetector.manageTileCollision(this);
        gPanel.collisionDetector.manageObjCollision(this);
        gPanel.collisionDetector.checkEntity(this, gPanel.npcList);
        gPanel.collisionDetector.checkEntity(this, gPanel.monsterList);
        gPanel.collisionDetector.checkPlayer(this);
        gPanel.collisionDetector.checkEntity(this, gPanel.interactiveTiles);

        if (!collisionOn && inMotion)
            this.manageMovement();

        manageInvincible();

//        currentTime++;
//        if (currentTime >= timeToChangeFrame) {
        currentAnimation.updateFrames();
//            currentTime = 0;
//        }
    }

    public void setupMovement(String creaturePath) {
        movement = new StateMachine();
        movement.loadCompleteAnimation(gPanel, creaturePath + "\\movement", TypeAnimation.IN_MOTION);
        currentAnimation = movement.down;
    }

    public void setupIdle(String creaturePath) {
        idle = new StateMachine();
        idle.loadCompleteAnimation(gPanel, creaturePath + "\\idle", TypeAnimation.IDLE);
    }

    public void setupAttack(String creaturePath) {
        attackState = new StateMachine();
        attackState.loadCompleteAnimation(gPanel, creaturePath + "\\attack\\", TypeAnimation.ATTACK);
    }
}
