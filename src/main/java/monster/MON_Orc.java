package monster;

import animations.AnimationState;
import entity.AttackStyle;
import features.Direction;
import game.GamePanel;

import java.util.random.RandomGenerator;

public class MON_Orc extends Monster {

    public MON_Orc(GamePanel gp) {
        super(gp);

        name = "Orc";

        exp = 10;

        direction = Direction.DOWN;

        timeToChangeInMotion = RandomGenerator.getDefault().nextInt(120)+1;
        timeToChangeDirection = RandomGenerator.getDefault().nextInt(220)+timeToChangeInMotion;

        typeMonster = TypeMonster.Orc;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 10;
        life = maxLife;

        attackStyle = AttackStyle.Nearly;
        attack = 8;
        defense = 2;

        // TODO: adauga arma pentru Orc
//        this.weapon = new Rock(gp);

        setDefaultSolidArea();

        // setare animatii
        setupMovement("res\\monster\\orc");
        setupIdle("res\\monster\\orc");
        setupAttack("res\\monster\\orc");
        for (AnimationState state : attackState.states) {
            state.motion1_duration = 25;
            state.motion2_duration = 45;
        }
    }

    public void AI() {
        super.AI();
    }

    public void update() {
        super.update();
    }

    @Override
    public void setDefaultSolidArea() {
//        solidArea.x = 4;
//        solidArea.y = 4;
//        solidArea.width = 40;
//        solidArea.height = 44;
//        solidAreaDefaultX = solidArea.x;
//        solidAreaDefaultY = solidArea.y;
        attackArea.width = solidArea.width + 20;
        attackArea.height = solidArea.width + 20;
    }

    @Override
    public void checkDrop() {

    }

    @Override
    public void offensiveBehaviour() {
        // Verifica daca ataca
        if (!attacking) {
            checkAttackOrNot(30, getGamePanel().tileSize * 4, getGamePanel().tileSize);
        }
    }
}
