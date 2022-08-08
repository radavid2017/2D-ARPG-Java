package entity;

import features.Direction;
import game.GamePanel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gPanel) {

        super(gPanel);

        direction = Direction.DOWN;
        speed = 1;

        // incarcare animatii movement
        loadMovementAnimations("res\\npc\\oldMan");
    }

    public NPC_OldMan() {

    }

    public void setAction() {

        actionLockCounter++;

        if (actionLockCounter == 120) {

            Random random = new Random();
            int randomDirection = random.nextInt(100) + 1; // primeste un nr random intre 1-100

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

            actionLockCounter = 0;
        }
    }

}
