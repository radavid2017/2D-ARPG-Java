package entity;

import features.Direction;
import game.GamePanel;

import java.util.Random;

public abstract class ArtificialIntelligence extends Entity {

    public TypeAI typeAI;
    public int actionLockCounterDirection = 0;
    public int actionLockCounterInMotion = 0;
    public int timeToChangeInMotion;
    public int timeToChangeDirection;
    
    public ArtificialIntelligence(GamePanel gp) {
        super(gp);

    }

    public void AI() {
        actionLockCounterDirection++;
        actionLockCounterInMotion++;

        if (actionLockCounterInMotion >= timeToChangeInMotion) {

            Random random = new Random();
            int randomMotion = random.nextInt(100) + 1;

            int chancesToChange = random.nextInt(100)+1;

            inMotion = randomMotion > chancesToChange; // 60% sanse de a se afla in miscare
            actionLockCounterInMotion = 0;
        }
        if (actionLockCounterDirection >= timeToChangeDirection) {

            Random random = new Random();
            int randomDirection = random.nextInt(100) + 1; // primeste un nr random intre 1-100

            int wantToChange = random.nextInt(100) + 1;

            int chancesToChange = random.nextInt(100)+1;

            if (wantToChange > chancesToChange) { // 50 % sanse de a schimba directia

                if (randomDirection <= 25) {
                    direction = Direction.UP;
                }
                if (randomDirection > 25 && randomDirection <= 50) {
                    direction = Direction.DOWN;
                }
                if (randomDirection > 50 && randomDirection <= 75) {
                    direction = Direction.LEFT;
                }
                if (randomDirection > 75) {
                    direction = Direction.RIGHT;
                }

                actionLockCounterDirection = 0;
            }
        }
    }
}
