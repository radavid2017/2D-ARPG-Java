package npc;

import features.Direction;
import game.GamePanel;

import java.util.Random;
import java.util.random.RandomGenerator;

public class NPC_OldMan extends NPC {

    public NPC_OldMan(GamePanel gPanel) {

        super(gPanel);

        direction = Direction.DOWN;
        speed = 1;

        timeToChangeInMotion = RandomGenerator.getDefault().nextInt(120)+1;
        timeToChangeDirection = RandomGenerator.getDefault().nextInt(220)+timeToChangeInMotion;

        // incarcare animatii movement
        setupMovement("res\\npc\\oldMan");
        setupIdle("res\\npc\\oldMan");
        //loadMovementAnimations("res\\npc\\oldMan");
        // setarea dialogului
        setDialogue();
    }

    private void setDialogue() {
        dialogue.addText("Buna, aventurierule!");
        dialogue.addText("Stiu ce cauti aici...comoara!!");
        dialogue.addText("Candva faceam parte din prima linie a maretilor vrajitori!\nDar acum...sunt putin cam batran pentru a mai cauta comoara secreta.");
        dialogue.addText("Ei bine, succes tie!");
    }

    /** AI Old Man */
    @Override
    public void AI() {

        actionLockCounterDirection++;
        actionLockCounterInMotion++;

        if (actionLockCounterInMotion == timeToChangeInMotion) {

            Random random = new Random();
            int randomMotion = random.nextInt(100) + 1;

            inMotion = randomMotion > 20; // 60% sanse de a se afla in miscare
            actionLockCounterInMotion = 0;
        }
        if (actionLockCounterDirection == timeToChangeDirection) {

            Random random = new Random();
            int randomDirection = random.nextInt(100) + 1; // primeste un nr random intre 1-100

            int wantToChange = random.nextInt(100) + 1;

            if (wantToChange > 10) { // 50 % sanse de a schimba directia

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

    public void speak() {
        super.speak();
    }
}
