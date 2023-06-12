package monster;

import animations.AnimationState;
import animations.TypeAnimation;
import data.GameProgress;
import entity.AttackStyle;
import entity.Entity;
import features.Dialogue;
import features.Direction;
import game.GamePanel;
import item.consumable.coin.Coin;
import item.consumable.potion.PotionRed;
import item.equipable.shield.BlueShield;
import object.obstacle.door.IronDoor;

import java.util.Random;
import java.util.random.RandomGenerator;

public class MON_SkeletonLord extends Monster {

    public MON_SkeletonLord(GamePanel gp) {
        super(gp);
        name = "Lordul Schelet";

        boss = true;
        sleep = true;

        exp = 50;
        knockBackPower = 5;

        direction = Direction.DOWN;

        timeToChangeInMotion = -1;
        timeToChangeDirection = RandomGenerator.getDefault().nextInt(220)+timeToChangeInMotion;

        typeMonster = TypeMonster.SkeletonLord;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 50;
        life = maxLife;

        attackStyle = AttackStyle.Nearly;
        attack = 10;
        defense = 2;

        setDefaultSolidArea();

        // setare animatii
        setupMovement(5, "res\\monster\\skeletonLord");
        setupIdle(5, "res\\monster\\skeletonLord");
        setupAttack(5, "res\\monster\\skeletonLord");
        movementInRage.loadCompleteAnimation(gp, 5, "res\\monster\\skeletonLord\\movementInRage", TypeAnimation.IN_MOTION);
        attackInRage.loadCompleteAnimation(gp, 5, "res\\monster\\skeletonLord\\attackInRage", TypeAnimation.ATTACK);
        idleInRage.loadCompleteAnimation(gp, 5, "res\\monster\\skeletonLord\\idleInRage", TypeAnimation.IDLE);

        for (AnimationState state : attackState.states) {
            state.motion1_duration = 15;
            state.motion2_duration = 27;
        }

        setDialogues();
    }

    public void update() {
        super.update();
        if (!attacking) {
            inMotion = true;
        }
//        checkCollisionsExceptPlayer();
//        alwaysMoving();
    }

    @Override
    public void AI() {

        if (!inRage && life < maxLife/2) {
            inRage = true;
            defaultSpeed++;
            speed = defaultSpeed;
            attack *= 2;

        }
        if (getTileDistance(getGamePanel().player) < 10) {
            moveTowardPlayer(60);
        }
        else {
            defaultBehavior();
        }
        if (!attacking) {
            checkAttackOrNot(60, getGamePanel().tileSize * 7, getGamePanel().tileSize * 5);
        }

    }

    @Override
    public void setDefaultSolidArea() {
        int size = getGamePanel().tileSize*5;
        solidArea.x = getGamePanel().tileSize;
        solidArea.y = getGamePanel().tileSize;
        solidAreaDefaultX = getGamePanel().tileSize;
        solidAreaDefaultY = getGamePanel().tileSize;
        solidArea.width = size - getGamePanel().tileSize*2;
        solidArea.height = size - getGamePanel().tileSize;
        attackArea.width = (int) (getGamePanel().tileSize * 3.5);
        attackArea.height = (int) (getGamePanel().tileSize * 3.5);
    }

    @Override
    public void checkDrop() {

        getGamePanel().bossBattleOn = false;
        GameProgress.skeletonLordDefeated = true;

        // Restore the previous music
        getGamePanel().stopMusic();
        getGamePanel().playMusic("Dungeon.wav");

        if (getGamePanel().objects.get(getGamePanel().currentMap) != null) {
            for (int i = 0; i < getGamePanel().objects.get(getGamePanel().currentMap).size(); i++) {
                Entity entity = getGamePanel().objects.get(getGamePanel().currentMap).get(i);
                if (entity instanceof IronDoor) {
                    getGamePanel().playSE("dooropen.wav");
                    getGamePanel().objects.get(getGamePanel().currentMap).set(i, null);
                }
            }
        }



        int i = new Random().nextInt(100) + 1;

        // 50% sanse de a arunca un banut
        if (i < 50) {
            dropItem(new Coin(getGamePanel()));
        }
        // 25% sanse de a arunca potiune rosie
        if (i >= 50 && i < 75) {
            dropItem(new PotionRed(getGamePanel()));
        }
        // 25% sanse de a arunca un scut albstru
        if (i >= 75 && i < 100) {
            dropItem(new BlueShield(getGamePanel()));
        }
    }

    public void setDialogues() {
        dialogue.addText("Nimeni nu se atinge de cufarul meu!\nSfarsitul ti se scrie aici si acum!\nPRIMESTE-TI SOARTA!");
//        dialogue.addText("");
//        dialogue.addText("");
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    @Override
    public void offensiveBehaviour() {

    }
}
