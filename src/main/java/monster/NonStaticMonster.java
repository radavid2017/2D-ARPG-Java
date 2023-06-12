package monster;

import features.Direction;
import game.GamePanel;

public abstract class NonStaticMonster extends Monster {

    public NonStaticMonster(GamePanel gp) {
        super(gp);
        timeToChangeInMotion = -1;
        inMotion = true;
    }

    public void update() {
        super.update();
        alwaysMoving();
    }
}
