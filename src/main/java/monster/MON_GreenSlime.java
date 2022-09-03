package monster;

import entity.AttackStyle;
import features.Direction;
import game.GamePanel;

import java.util.random.RandomGenerator;

public class MON_GreenSlime extends Monster{

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        name = "Green Slime";

        exp = 2;

        direction = Direction.DOWN;

        timeToChangeInMotion = RandomGenerator.getDefault().nextInt(120)+1;
        timeToChangeDirection = RandomGenerator.getDefault().nextInt(220)+timeToChangeInMotion;

        typeMonster = TypeMonster.GreenSlime;
        speed = 1;
        maxLife = 4;
        life = maxLife;

        attackStyle = AttackStyle.Touching;
        attack = 5;
        defense = 0;


        setDefaultSolidArea();

        // setare animatii
        setupMovement("res\\monster\\greenSlime");
        setupIdle("res\\monster\\greenSlime");
    }

    public void AI() {

        super.AI();
    }

    public void update() {
        super.update();

    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.x = getGamePanel().tileSize / 16;
        solidArea.y = (int) (getGamePanel().tileSize / 2.66);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = (int) (getGamePanel().tileSize / 1.15);
        solidArea.height = (int) (getGamePanel().tileSize / 1.6);
    }
}
